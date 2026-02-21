from fastapi import APIRouter, HTTPException, Depends
from pydantic import BaseModel
from bson import ObjectId
import random
import re
import app.db.mongo as mongo
from app.auth.deps import get_current_user

router = APIRouter()

# Pydantic models
class TrainerCreate(BaseModel):
    name: str

class TrainerPokemonAdd(BaseModel):
    pokedex_id: str

class PokemonExperienceUpdate(BaseModel):
    experience: int

class BattleStats(BaseModel):
    wins: int = 0
    losses: int = 0

def _serialize(doc: dict) -> dict:
    if doc is None:
        return doc
    # Remove MongoDB _id from response
    if "_id" in doc:
        del doc["_id"]
    return doc

def _get_random_sprite() -> int:
    """Get a random sprite number for trainer (1-8)"""
    return random.randint(1, 8)

async def _get_next_suid() -> int:
    """Get the next sequential user ID for trainers"""
    counter = await mongo.db.trainer_counters.find_one_and_update(
        {"_id": "trainer_suid"},
        {"$inc": {"seq": 1}},
        upsert=True,
        return_document=True
    )
    return counter.get("seq", 1)

@router.post("/")
async def create_trainer(
    payload: TrainerCreate,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Create a new trainer for the logged-in user"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    
    # Get next sequential ID
    suid = await _get_next_suid()
    
    trainer_doc = {
        "suid": suid,
        "username": _user.get("username"),
        "name": payload.name,
        "sprite": _get_random_sprite(),
        "wins": 0,
        "losses": 0,
        "team": [],  # Current battle team (max 3 pokemones)
        "inventory": []  # All pokemones owned
    }
    
    result = await mongo.db.trainers.insert_one(trainer_doc)
    trainer_doc["_id"] = str(result.inserted_id)
    return {
        "suid": suid,
        "status": "created",
        "trainer": _serialize(trainer_doc)
    }

@router.get("/me")
async def get_my_trainer(_user: dict = Depends(get_current_user)) -> dict:
    """Get current user's trainer profile"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    
    trainer = await mongo.db.trainers.find_one({"username": _user.get("username")})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")
    
    # Fetch full pokemon data for team and inventory
    team_pokemons = []
    inventory_pokemons = []
    
    if trainer.get("team"):
        for pokemon_id in trainer["team"]:
            poke = await mongo.db.pokemons.find_one({"pokedex_id": pokemon_id})
            if poke:
                team_pokemons.append(_serialize(poke))
    
    if trainer.get("inventory"):
        for pokemon_id in trainer["inventory"]:
            poke = await mongo.db.pokemons.find_one({"pokedex_id": pokemon_id})
            if poke:
                inventory_pokemons.append(_serialize(poke))
    
    trainer_data = _serialize(trainer)
    trainer_data["team_detail"] = team_pokemons
    trainer_data["inventory_detail"] = inventory_pokemons
    
    return trainer_data

@router.get("/search")
async def search_trainer(
    name: str,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Search trainer by exact name or username (case-insensitive)."""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")

    pattern = f"^{re.escape(name)}$"
    trainer = await mongo.db.trainers.find_one({
        "$or": [
            {"name": {"$regex": pattern, "$options": "i"}},
            {"username": {"$regex": pattern, "$options": "i"}}
        ]
    })

    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")

    team_pokemons = []
    inventory_pokemons = []

    if trainer.get("team"):
        for pokemon_id in trainer["team"]:
            poke = await mongo.db.pokemons.find_one({"pokedex_id": pokemon_id})
            if poke:
                team_pokemons.append(_serialize(poke))

    if trainer.get("inventory"):
        for pokemon_id in trainer["inventory"]:
            poke = await mongo.db.pokemons.find_one({"pokedex_id": pokemon_id})
            if poke:
                inventory_pokemons.append(_serialize(poke))

    trainer_data = _serialize(trainer)
    trainer_data["team_detail"] = team_pokemons
    trainer_data["inventory_detail"] = inventory_pokemons

    return trainer_data

@router.get("/{trainer_suid}")
async def get_trainer(
    trainer_suid: int,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Get trainer by SUID with detailed pokemon data"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    
    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")
    
    # Fetch full pokemon data for team and inventory
    team_pokemons = []
    inventory_pokemons = []
    
    if trainer.get("team"):
        for pokemon_id in trainer["team"]:
            poke = await mongo.db.pokemons.find_one({"pokedex_id": pokemon_id})
            if poke:
                team_pokemons.append(_serialize(poke))
    
    if trainer.get("inventory"):
        for pokemon_id in trainer["inventory"]:
            poke = await mongo.db.pokemons.find_one({"pokedex_id": pokemon_id})
            if poke:
                inventory_pokemons.append(_serialize(poke))
    
    trainer_data = _serialize(trainer)
    trainer_data["team_detail"] = team_pokemons
    trainer_data["inventory_detail"] = inventory_pokemons
    
    return trainer_data

@router.post("/{trainer_suid}/pokemon")
async def add_pokemon_to_trainer(
    trainer_suid: int,
    payload: TrainerPokemonAdd,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Add a pokemon to trainer's inventory and auto-assign to team if slots available"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    
    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")
    
    # Verify pokemon exists
    pokemon = await mongo.db.pokemons.find_one({"pokedex_id": payload.pokedex_id})
    if not pokemon:
        raise HTTPException(status_code=404, detail="Pokemon not found")
    
    # Add to inventory
    inventory = trainer.get("inventory", [])
    if payload.pokedex_id in inventory:
        raise HTTPException(status_code=400, detail="Pokemon already in inventory")
    
    inventory.append(payload.pokedex_id)
    
    # Auto-assign to team if team has less than 3 members
    team = trainer.get("team", [])
    if len(team) < 3:
        team.append(payload.pokedex_id)
    
    # Update trainer
    await mongo.db.trainers.update_one(
        {"suid": trainer_suid},
        {"$set": {
            "inventory": inventory,
            "team": team
        }}
    )
    
    return {
        "status": "added",
        "pokemon_id": payload.pokedex_id,
        "team_count": len(team),
        "inventory_count": len(inventory)
    }

@router.get("/{trainer_suid}/team")
async def get_trainer_team(
    trainer_suid: int,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Get trainer's current battle team with full pokemon data"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    
    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")
    
    team_pokemons = []
    for pokemon_id in trainer.get("team", []):
        poke = await mongo.db.pokemons.find_one({"pokedex_id": pokemon_id})
        if poke:
            team_pokemons.append(_serialize(poke))
    
    return {
        "trainer_suid": trainer.get("suid"),
        "trainer_name": trainer.get("name"),
        "team": team_pokemons,
        "team_size": len(team_pokemons)
    }

@router.get("/{trainer_suid}/team/exp")
async def get_trainer_team_exp(
    trainer_suid: int,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Get trainer's team experience info (level, exp, next level exp)."""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")

    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")

    team_exp = []
    for pokemon_id in trainer.get("team", []):
        poke = await mongo.db.pokemons.find_one({"pokedex_id": pokemon_id})
        if poke:
            team_exp.append({
                "pokedex_id": pokemon_id,
                "level": poke.get("level", 5),
                "experience": poke.get("experience", 0),
                "next_level_exp": poke.get("next_level_exp", 100)
            })

    return {
        "trainer_suid": trainer.get("suid"),
        "trainer_name": trainer.get("name"),
        "team_exp": team_exp
    }

@router.get("/{trainer_suid}/inventory")
async def get_trainer_inventory(
    trainer_suid: int,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Get trainer's full pokemon inventory with full pokemon data"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    
    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")
    
    inventory_pokemons = []
    for pokemon_id in trainer.get("inventory", []):
        poke = await mongo.db.pokemons.find_one({"pokedex_id": pokemon_id})
        if poke:
            inventory_pokemons.append(_serialize(poke))
    
    return {
        "trainer_suid": trainer.get("suid"),
        "trainer_name": trainer.get("name"),
        "inventory": inventory_pokemons,
        "inventory_size": len(inventory_pokemons)
    }

@router.put("/{trainer_suid}/pokemons/{pokemon_index}")
async def update_pokemon_experience(
    trainer_suid: int,
    pokemon_index: int,
    payload: PokemonExperienceUpdate,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Update experience for a pokemon in trainer's team by index (0-2)."""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")

    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")

    team = trainer.get("team", [])
    if pokemon_index < 0 or pokemon_index >= len(team):
        raise HTTPException(status_code=404, detail="Pokemon index not found")

    pokedex_id = team[pokemon_index]
    pokemon = await mongo.db.pokemons.find_one({"pokedex_id": pokedex_id})
    if not pokemon:
        raise HTTPException(status_code=404, detail="Pokemon not found")

    level = pokemon.get("level", 5)
    next_level_exp = pokemon.get("next_level_exp", 100)
    experience = max(0, payload.experience)

    while experience >= next_level_exp:
        experience -= next_level_exp
        level += 1
        next_level_exp += 100

    evolved = False
    evolved_from = pokedex_id
    evolved_to = pokedex_id

    current_id = pokedex_id
    while True:
        current_pokemon = await mongo.db.pokemons.find_one({"pokedex_id": current_id})
        if not current_pokemon:
            break
        evolution = current_pokemon.get("evolution")
        evo_level = evolution.get("level") if isinstance(evolution, dict) else None
        evo_next = evolution.get("next_id") if isinstance(evolution, dict) else None
        if evo_next and evo_level is not None and level >= evo_level:
            evolved = True
            evolved_to = evo_next
            current_id = evo_next
            continue
        break

    if evolved and evolved_to != evolved_from:
        team = [evolved_to if pid == evolved_from else pid for pid in team]
        inventory = [evolved_to if pid == evolved_from else pid for pid in trainer.get("inventory", [])]
        await mongo.db.trainers.update_one(
            {"suid": trainer_suid},
            {"$set": {"team": team, "inventory": inventory}}
        )
        pokedex_id = evolved_to

    await mongo.db.pokemons.update_one(
        {"pokedex_id": pokedex_id},
        {"$set": {
            "experience": experience,
            "level": level,
            "next_level_exp": next_level_exp
        }}
    )

    return {
        "status": "updated",
        "pokedex_id": pokedex_id,
        "experience": experience,
        "level": level,
        "next_level_exp": next_level_exp,
        "evolved": evolved,
        "evolved_from": evolved_from if evolved else None
    }

@router.post("/{trainer_suid}/stats/win")
async def add_win(
    trainer_suid: int,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Increment trainer's wins"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    
    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")
    
    new_wins = trainer.get("wins", 0) + 1
    
    await mongo.db.trainers.update_one(
        {"suid": trainer_suid},
        {"$set": {"wins": new_wins}}
    )
    
    return {
        "status": "updated",
        "wins": new_wins,
        "losses": trainer.get("losses", 0)
    }

@router.post("/{trainer_suid}/stats/loss")
async def add_loss(
    trainer_suid: int,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Increment trainer's losses"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    
    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")
    
    new_losses = trainer.get("losses", 0) + 1
    
    await mongo.db.trainers.update_one(
        {"suid": trainer_suid},
        {"$set": {"losses": new_losses}}
    )
    
    return {
        "status": "updated",
        "wins": trainer.get("wins", 0),
        "losses": new_losses
    }
