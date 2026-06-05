from fastapi import APIRouter, HTTPException, Depends
from pydantic import BaseModel
import random
import re
import httpx
import app.db.mongo as mongo
from app.auth.deps import get_current_user

router = APIRouter()

# Pydantic models
class TrainerCreate(BaseModel):
    name: str

class TrainerPokemonAdd(BaseModel):
    pokedex_id: str

class TrainerAddRestaurantPoints(BaseModel):
    points: int

class PokemonExperienceUpdate(BaseModel):
    experience: int

class PokemonLevelUpdate(BaseModel):
    level: int
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

def _to_int_pokedex_id(value) -> int | None:
    try:
        return int(str(value))
    except (TypeError, ValueError):
        return None

async def _get_level_range_for_capture(pokemon_doc: dict) -> tuple[int, int]:
    """
    Calcula rango de nivel para captura según etapa evolutiva:
    - Base: 5 hasta (nivel evolución siguiente - 1)
    - Intermedia: nivel de evolución previa hasta (nivel evolución siguiente - 1)
    - Final: nivel de evolución previa hasta 50
    - Sin cadena: 5 a 50
    """
    current_id = pokemon_doc.get("pokedex_id")
    evolution = pokemon_doc.get("evolution") if isinstance(pokemon_doc.get("evolution"), dict) else None
    next_level = evolution.get("level") if evolution else None

    previous_level = None
    previous_doc = await mongo.db.pokemons.find_one({"evolution.next_id": current_id})
    if previous_doc:
        previous_evo = previous_doc.get("evolution") if isinstance(previous_doc.get("evolution"), dict) else None
        if previous_evo and previous_evo.get("level") is not None:
            previous_level = int(previous_evo.get("level"))

    if previous_level is None and next_level is None:
        min_level, max_level = 5, 50
    elif previous_level is None and next_level is not None:
        min_level, max_level = 5, int(next_level) - 1
    elif previous_level is not None and next_level is not None:
        min_level, max_level = int(previous_level), int(next_level) - 1
    else:
        min_level, max_level = int(previous_level), 50

    min_level = max(1, min_level)
    max_level = min(50, max_level)
    if max_level < min_level:
        max_level = min_level

    return min_level, max_level

def _build_level_state(level: int) -> tuple[int, int]:
    next_level_exp = 100 + (level - 1) * 100
    return 0, next_level_exp

def _build_sprite_pool(gen1_pokemons: list[dict], size: int = 24) -> list[str]:
    if not gen1_pokemons:
        return []

    sampled = random.choices(gen1_pokemons, k=size)
    return [
        p.get("front_sprite")
        for p in sampled
        if p.get("front_sprite")
    ]

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
        "points": 0,
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

@router.post("/{trainer_suid}/inventory/{pokemon_index}")
async def change_pokemon_in_team(
    trainer_suid: int,
    pokemon_index: int,
    payload: TrainerPokemonAdd,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Change a pokemon in trainer's team by index (0-2) with a pokemon from inventory."""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")

    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")

    team = trainer.get("team", [])
    inventory = trainer.get("inventory", [])

    if pokemon_index < 0 or pokemon_index >= len(team):
        raise HTTPException(status_code=404, detail="Pokemon index not found")

    if payload.pokedex_id not in inventory:
        raise HTTPException(status_code=404, detail="Pokemon not found in inventory")

    # Swap the pokemon in team with the one in inventory
    old_pokemon_id = team[pokemon_index]
    team[pokemon_index] = payload.pokedex_id

    # Update inventory
    if old_pokemon_id not in inventory:
        inventory.append(old_pokemon_id)

    await mongo.db.trainers.update_one(
        {"suid": trainer_suid},
        {"$set": {
            "team": team,
            "inventory": inventory
        }}
    )

    return {
        "status": "updated",
        "new_pokemon_id": payload.pokedex_id,
        "old_pokemon_id": old_pokemon_id,
        "team_count": len(team),
        "inventory_count": len(inventory)
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
    current_experience = pokemon.get("experience", 0)
    gained_experience = max(0, payload.experience)
    experience = current_experience + gained_experience

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

    updated_pokemon = await mongo.db.pokemons.find_one({"pokedex_id": pokedex_id})
    if not updated_pokemon:
        raise HTTPException(status_code=404, detail="Updated pokemon not found")

    return {
        "status": "updated",
        "pokedex_id": pokedex_id,
        "gained_experience": gained_experience,
        "experience": experience,
        "level": level,
        "next_level_exp": next_level_exp,
        "evolved": evolved,
        "evolved_from": evolved_from if evolved else None,
        "evolved_to": pokedex_id if evolved else None,
        "pokemon": _serialize(updated_pokemon)
    }

@router.post("/{trainer_suid}/capture")
async def capture_random_pokemon(
    trainer_suid: int,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Captura un Pokémon aleatorio de Gen 1 y lo agrega al inventario (gacha). Costo: 15 puntos."""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")

    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")
    
    # Verificar que el usuario tenga suficientes puntos
    current_points = trainer.get("points", 0)
    if current_points < 15:
        raise HTTPException(
            status_code=400,
            detail=f"Insufficient points. You have {current_points} points, but 15 are required."
        )
    
    # Deducir 15 puntos
    new_points = current_points - 15
    await mongo.db.trainers.update_one(
        {"suid": trainer_suid},
        {"$set": {"points": new_points}}
    )

    all_pokemons = await mongo.db.pokemons.find(
        {},
        {
            "pokedex_id": 1,
            "name": 1,
            "front_sprite": 1,
            "back_sprite": 1,
            "icon": 1,
            "type": 1,
            "types": 1,
            "base_hp": 1,
            "base_attack": 1,
            "base_defense": 1,
            "base_special_attack": 1,
            "base_special_defense": 1,
            "base_speed": 1,
            "evolution": 1,
            "moves": 1
        }
    ).to_list(length=300)

    gen1_pokemons = [
        p for p in all_pokemons
        if (_to_int_pokedex_id(p.get("pokedex_id")) is not None and 1 <= _to_int_pokedex_id(p.get("pokedex_id")) <= 151)
    ]

    if not gen1_pokemons:
        raise HTTPException(status_code=404, detail="No Gen 1 pokemon available")

    captured = random.choice(gen1_pokemons)
    captured_id = captured.get("pokedex_id")

    min_level, max_level = await _get_level_range_for_capture(captured)
    level = random.randint(min_level, max_level)
    experience, next_level_exp = _build_level_state(level)

    await mongo.db.pokemons.update_one(
        {"pokedex_id": captured_id},
        {"$set": {
            "level": level,
            "experience": experience,
            "next_level_exp": next_level_exp
        }}
    )

    inventory = trainer.get("inventory", [])
    inventory.append(captured_id)

    team = trainer.get("team", [])
    if len(team) < 3:
        team.append(captured_id)

    try:
        await mongo.db.trainers.update_one(
            {"suid": trainer_suid},
            {"$set": {"inventory": inventory, "team": team}}
        )
    except Exception as e:
        # Si falla, revertir los puntos
        await mongo.db.trainers.update_one(
            {"suid": trainer_suid},
            {"$set": {"points": current_points}}
        )
        raise HTTPException(status_code=500, detail=f"Error updating trainer: {str(e)}")

    sprite_pool = _build_sprite_pool(gen1_pokemons)
    if captured.get("front_sprite"):
        sprite_pool.append(captured.get("front_sprite"))

    return {
        "status": "captured",
        "trainer_suid": trainer_suid,
        "points_remaining": new_points,
        "points_spent": 15,
        "team_count": len(team),
        "inventory_count": len(inventory),
        "spin": {
            "duration_ms": 3200,
            "sprites": sprite_pool,
            "final_sprite": captured.get("front_sprite")
        },
        "captured_pokemon": {
            "pokedex_id": captured_id,
            "name": captured.get("name"),
            "front_sprite": captured.get("front_sprite"),
            "back_sprite": captured.get("back_sprite"),
            "icon": captured.get("icon"),
            "type": captured.get("type"),
            "types": captured.get("types", []),
            "moves": captured.get("moves", []),
            "base_hp": captured.get("base_hp", 0),
            "base_attack": captured.get("base_attack", 0),
            "base_defense": captured.get("base_defense", 0),
            "base_special_attack": captured.get("base_special_attack", 0),
            "base_special_defense": captured.get("base_special_defense", 0),
            "base_speed": captured.get("base_speed", 0),
            "level": level,
            "experience": experience,
            "next_level_exp": next_level_exp,
            "level_range": {
                "min": min_level,
                "max": max_level
            }
        }
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

@router.put("/{trainer_suid}/team/{pokemon_index}")
async def update_team_member_level(
    trainer_suid: int,
    pokemon_index: int,
    payload: PokemonLevelUpdate,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Update level for a pokemon in trainer's team by index (0-2)."""
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

    level = max(1, payload.level)
    next_level_exp = 100 + (level - 1) * 100

    await mongo.db.pokemons.update_one(
        {"pokedex_id": pokedex_id},
        {"$set": {
            "level": level,
            "next_level_exp": next_level_exp
        }}
    )

    return {
        "status": "updated",
        "pokedex_id": pokedex_id,
        "level": level,
        "next_level_exp": next_level_exp
    }

@router.delete("/{trainer_suid}/pokemon/{pokemon_index}")
async def remove_pokemon_from_trainer(
    trainer_suid: int,
    pokemon_index: int,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Remove a pokemon from trainer's inventory and team by index (0-2)."""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")

    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")

    team = trainer.get("team", [])
    inventory = trainer.get("inventory", [])

    if pokemon_index < 0 or pokemon_index >= len(team):
        raise HTTPException(status_code=404, detail="Pokemon index not found")

    pokedex_id = team[pokemon_index]

    if pokedex_id in inventory:
        inventory.remove(pokedex_id)

    team.remove(pokedex_id)

    await mongo.db.trainers.update_one(
        {"suid": trainer_suid},
        {"$set": {
            "team": team,
            "inventory": inventory
        }}
    )

    return {
        "status": "removed",
        "pokedex_id": pokedex_id,
        "team_count": len(team),
        "inventory_count": len(inventory)
    }

@router.post("/{trainer_suid}/team")
async def add_pokemon_to_team(
    trainer_suid: int,
    payload: TrainerPokemonAdd,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Add a pokemon from inventory to battle team (max 3)"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")

    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")

    # Verify pokemon exists in inventory
    inventory = trainer.get("inventory", [])
    if payload.pokedex_id not in inventory:
        raise HTTPException(status_code=400, detail="Pokemon not in inventory")

    # Verify pokemon exists in database
    pokemon = await mongo.db.pokemons.find_one({"pokedex_id": payload.pokedex_id})
    if not pokemon:
        raise HTTPException(status_code=404, detail="Pokemon not found")

    # Check team size
    team = trainer.get("team", [])
    if len(team) >= 3:
        raise HTTPException(status_code=400, detail="Team is full (max 3 pokemon)")

    # Avoid duplicates in team
    if payload.pokedex_id in team:
        raise HTTPException(status_code=400, detail="Pokemon already in team")

    # Add to team
    team.append(payload.pokedex_id)

    # Update trainer
    await mongo.db.trainers.update_one(
        {"suid": trainer_suid},
        {"$set": {"team": team}}
    )

    return {
        "status": "added_to_team",
        "pokedex_id": payload.pokedex_id,
        "team_count": len(team),
        "inventory_count": len(inventory)
    }

@router.delete("/{trainer_suid}/team/{pokemon_index}")
async def remove_pokemon_from_team(
    trainer_suid: int,
    pokemon_index: int,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Remove a pokemon from battle team by index (0-2)"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")

    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")

    team = trainer.get("team", [])
    if pokemon_index < 0 or pokemon_index >= len(team):
        raise HTTPException(status_code=404, detail="Pokemon index not found in team")

    # Remove from team but keep in inventory
    pokedex_id = team.pop(pokemon_index)

    await mongo.db.trainers.update_one(
        {"suid": trainer_suid},
        {"$set": {"team": team}}
    )

    return {
        "status": "removed_from_team",
        "pokedex_id": pokedex_id,
        "team_count": len(team),
        "inventory_count": len(trainer.get("inventory", []))
    }

@router.get("/points/{trainer_suid}")
async def get_trainer_points(
    trainer_suid: int,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Get points for a trainer"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")

    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")

    return {
        "trainer_suid": trainer_suid,
        "points": trainer.get("points", 0)
    }

@router.put("/points/{trainer_suid}/win")
async def restaurant_points_win(
    trainer_suid: int,
    _user: dict = Depends(get_current_user)
    
) -> dict:
    """Give Points to a trainer based on wins (5 points per win)"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")

    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")

    points = trainer.get("wins", 0) * 5

    await mongo.db.trainers.update_one(
        {"suid": trainer_suid},
        {"$set": {"points": points}}
    )

    return {
        "status": "updated",
        "trainer_suid": trainer_suid,
        "points": points
}

@router.post("/points/{trainer_suid}/redeem_coupon")
async def redeem_coupon(
    trainer_suid: int,
    _user: dict = Depends(get_current_user)
) -> dict:
    """Redeem 1000 points for a restaurant coupon"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")

    trainer = await mongo.db.trainers.find_one({"suid": trainer_suid})
    if not trainer:
        raise HTTPException(status_code=404, detail="Trainer not found")

    current_points = trainer.get("points", 0)
    
    # Verificar que el usuario tenga suficientes puntos
    if current_points < 1000:
        raise HTTPException(
            status_code=400, 
            detail=f"Insufficient points. You have {current_points} points, but 1000 are required."
        )

    # Deducir los 1000 puntos
    new_points = current_points - 1000
    await mongo.db.trainers.update_one(
        {"suid": trainer_suid},
        {"$set": {"points": new_points}}
    )

    # Hacer la petición a la API del restaurante
    try:
        async with httpx.AsyncClient(timeout=50.0) as client:
            response = await client.post(
                "http://host.docker.internal:8080/api/pedidos/crear_cupon/",
                json={
                    "dias_validez": 30,
                    "origen_sistema": "sistema_puntos",
                    "usuario_externo_id": f"trainer_{trainer_suid}"
                }
            )
            response.raise_for_status()
            coupon_data = response.json()
            
            return {
                "status": "success",
                "trainer_suid": trainer_suid,
                "points_remaining": new_points,
                "points_redeemed": 1000,
                "coupon_code": coupon_data.get("codigo"),
                "expiration_date": coupon_data.get("fecha_expiracion"),
                "redirect_url": {coupon_data.get('url_redireccion')},
                "message": coupon_data.get("mensaje")
            }
    except httpx.HTTPStatusError as e:
        # Si falla la petición HTTP, revertir los puntos
        await mongo.db.trainers.update_one(
            {"suid": trainer_suid},
            {"$set": {"points": current_points}}
        )
        raise HTTPException(
            status_code=502,
            detail=f"Error communicating with restaurant API: {str(e)}"
        )
    except httpx.RequestError as e:
        # Si falla la conexión, revertir los puntos
        await mongo.db.trainers.update_one(
            {"suid": trainer_suid},
            {"$set": {"points": current_points}}
        )
        raise HTTPException(
            status_code=503,
            detail=f"Could not connect to restaurant API: {str(e)}"
        )