from fastapi import APIRouter, HTTPException, Depends
import httpx
import asyncio
import app.db.mongo as mongo
from app.auth.deps import get_current_user

router = APIRouter()

def _serialize(doc: dict) -> dict:
    if doc is None:
        return doc
    if "_id" in doc:
        doc["_id"] = str(doc["_id"])
    return doc

async def _seed_pokemon_from_pokeapi(pokedex_id: str, client: httpx.AsyncClient) -> dict:
    """Helper function to seed a single pokemon from PokeAPI"""
    url = f"https://pokeapi.co/api/v2/pokemon/{pokedex_id}"
    try:
        response = await client.get(url, timeout=10)
    except Exception:
        return None
    
    if response.status_code != 200:
        return None

    data = response.json()
    stats = {s["stat"]["name"]: s["base_stat"] for s in data.get("stats", [])}
    types = data.get("types", [])
    primary_type = types[0]["type"]["name"].upper() if types else "UNKNOWN"
    sprites = data.get("sprites", {})
    
    # Extract moves from PokeAPI response with detailed move information
    moves = []
    for move_obj in data.get("moves", [])[:4]:  # Get first 4 moves (like in Pokemon games)
        move_name = move_obj.get("move", {}).get("name", "")
        if move_name:
            try:
                # Fetch move details from PokeAPI
                move_detail_response = await client.get(f"https://pokeapi.co/api/v2/move/{move_name}", timeout=10)
                if move_detail_response.status_code == 200:
                    move_data = move_detail_response.json()
                    moves.append({
                        "name": move_data.get("name", move_name).replace("-", " ").title(),
                        "type": move_data.get("type", {}).get("name", "normal").upper(),
                        "power": move_data.get("power") or 0,
                        "accuracy": move_data.get("accuracy") or 100
                    })
                else:
                    # Fallback if move details can't be fetched
                    moves.append({
                        "name": move_name.replace("-", " ").title(),
                        "type": "NORMAL",
                        "power": 0,
                        "accuracy": 100
                    })
            except Exception:
                # Fallback for any API errors
                moves.append({
                    "name": move_name.replace("-", " ").title(),
                    "type": "NORMAL",
                    "power": 0,
                    "accuracy": 100
                })

    evolution_info = await _get_evolution_info(data, client)

    payload = {
        "pokedex_id": str(data.get("id")),
        "name": data.get("name"),
        "type": primary_type,
        "base_hp": stats.get("hp", 0),
        "base_attack": stats.get("attack", 0),
        "base_defense": stats.get("defense", 0),
        "base_special_attack": stats.get("special-attack", 0),
        "base_special_defense": stats.get("special-defense", 0),
        "base_speed": stats.get("speed", 0),
        "front_sprite": sprites.get("front_default"),
        "back_sprite": sprites.get("back_default"),
        "icon": sprites.get("front_default"),
        "level": 5,
        "experience": 0,
        "next_level_exp": 100,
        "evolution": evolution_info,
        "moves": moves
    }

    return payload

def _get_species_id(species: dict) -> str:
    url = species.get("url", "")
    return url.rstrip("/").split("/")[-1]

def _build_evolution_map(chain: dict) -> dict:
    mapping = {}

    def walk(node: dict):
        species = node.get("species", {})
        species_id = _get_species_id(species)
        for evo in node.get("evolves_to", []):
            evo_species_id = _get_species_id(evo.get("species", {}))
            details = evo.get("evolution_details", [])
            min_level = details[0].get("min_level") if details else None
            if species_id and evo_species_id:
                mapping[species_id] = {
                    "next_id": evo_species_id,
                    "level": min_level
                }
            walk(evo)

    walk(chain)
    return mapping

async def _get_evolution_info(pokemon_data: dict, client: httpx.AsyncClient) -> dict:
    species = pokemon_data.get("species", {})
    species_url = species.get("url")
    if not species_url:
        return None

    try:
        species_response = await client.get(species_url, timeout=10)
        if species_response.status_code != 200:
            return None
        species_data = species_response.json()

        chain_url = species_data.get("evolution_chain", {}).get("url")
        if not chain_url:
            return None

        chain_response = await client.get(chain_url, timeout=10)
        if chain_response.status_code != 200:
            return None
        chain_data = chain_response.json().get("chain", {})

        evolution_map = _build_evolution_map(chain_data)
        current_id = str(pokemon_data.get("id"))
        return evolution_map.get(current_id)
    except Exception:
        return None

@router.get("/")
async def get_all_pokemons() -> dict:
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    pokemons = await mongo.db.pokemons.find().limit(150).to_list(length=150)
    return {"pokemons": [_serialize(p) for p in pokemons]}

@router.get("/{pokedex_id}")
async def get_pokemon(pokedex_id: str) -> dict:
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    pokemon = await mongo.db.pokemons.find_one({"pokedex_id": pokedex_id})
    if not pokemon:
        raise HTTPException(status_code=404, detail="Pokemon not found")
    return _serialize(pokemon)

@router.put("/{pokedex_id}")
async def update_pokemon(
    pokedex_id: str,
    payload: dict,
    _user: dict = Depends(get_current_user)
) -> dict:
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    result = await mongo.db.pokemons.update_one(
        {"pokedex_id": pokedex_id},
        {"$set": payload}
    )
    if result.matched_count == 0:
        raise HTTPException(status_code=404, detail="Pokemon not found")
    return {"status": "updated"}

@router.get("/pokeapi/{pokedex_id}")
async def proxy_pokeapi(pokedex_id: str) -> dict:
    url = f"https://pokeapi.co/api/v2/pokemon/{pokedex_id}"
    async with httpx.AsyncClient(timeout=10) as client:
        response = await client.get(url)
    if response.status_code != 200:
        raise HTTPException(status_code=404, detail="Pokemon not found in PokeAPI")
    return response.json()

@router.post("/seed/{pokedex_id}")
async def seed_from_pokeapi(
    pokedex_id: str,
    _user: dict = Depends(get_current_user)
) -> dict:
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    
    async with httpx.AsyncClient() as client:
        payload = await _seed_pokemon_from_pokeapi(pokedex_id, client)
    
    if payload is None:
        raise HTTPException(status_code=404, detail="Pokemon not found in PokeAPI")

    result = await mongo.db.pokemons.update_one(
        {"pokedex_id": payload["pokedex_id"]},
        {"$set": payload},
        upsert=True
    )

    return {
        "status": "seeded",
        "upserted": result.upserted_id is not None,
        "pokemon": payload
    }

@router.post("/seed-gen1")
async def seed_generation_1(
    _user: dict = Depends(get_current_user)
) -> dict:
    """Seed all Generation 1 Pokemons (IDs 1-151) from PokeAPI"""
    if mongo.db is None:
        raise HTTPException(status_code=500, detail="Database not initialized")
    
    seeded_count = 0
    failed_count = 0
    
    # Create client once for all requests
    async with httpx.AsyncClient(timeout=15) as client:
        # Create tasks with semaphore to limit concurrency
        semaphore = asyncio.Semaphore(5)  # Max 5 concurrent requests
        
        async def seed_with_limit(pokemon_id):
            async with semaphore:
                return await _seed_pokemon_from_pokeapi(str(pokemon_id), client)
        
        # Create tasks for all pokemons in Gen 1 (1-151)
        tasks = [seed_with_limit(pokemon_id) for pokemon_id in range(1, 152)]
        results = await asyncio.gather(*tasks, return_exceptions=True)
        
        # Save results to database
        for result in results:
            if result is None or isinstance(result, Exception):
                failed_count += 1
            else:
                try:
                    await mongo.db.pokemons.update_one(
                        {"pokedex_id": result["pokedex_id"]},
                        {"$set": result},
                        upsert=True
                    )
                    seeded_count += 1
                except Exception:
                    failed_count += 1
    
    return {
        "status": "completed",
        "total": 151,
        "seeded": seeded_count,
        "failed": failed_count,
        "message": f"Generation 1 seeding completed: {seeded_count} seeded, {failed_count} failed"
    }
