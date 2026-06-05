from fastapi import APIRouter, HTTPException
from pydantic import BaseModel, Field
import httpx
import json
import random
from typing import List, Optional

router = APIRouter()

OLLAMA_API_URL = "http://localhost:11434/api/chat"

# Pydantic Models
class MoveInfo(BaseModel):
    name: str
    type: str
    power: int

class PokemonInfo(BaseModel):
    name: str
    hp: int
    max_hp: int
    level: int
    type: List[str]
    moves: List[MoveInfo]

class BattleState(BaseModel):
    player_pokemon: PokemonInfo
    opponent_pokemon: PokemonInfo
    opponent_team: List[PokemonInfo] # Pokémons en la banca del oponente (IA)

class AIAction(BaseModel):
    action: str = Field("move", description="La acción a realizar: siempre será 'move'")
    move_index: int = Field(..., description="Índice del movimiento a usar (0-3)")


def format_prompt(state: BattleState) -> str:
    prompt = f"""
    Eres un entrenador Pokémon experto. Analiza el estado de la batalla y decide el mejor movimiento.
    Tu Pokémon actual es {state.opponent_pokemon.name} (Tipo: {', '.join(state.opponent_pokemon.type)}) con {state.opponent_pokemon.hp}/{state.opponent_pokemon.max_hp} HP.
    Tu oponente es {state.player_pokemon.name} (Tipo: {', '.join(state.player_pokemon.type)}) con {state.player_pokemon.hp}/{state.player_pokemon.max_hp} HP.

    Tus movimientos disponibles son:
    """
    for i, move in enumerate(state.opponent_pokemon.moves):
        prompt += f"{i}: {move.name} (Tipo: {move.type}, Poder: {move.power})\n"
    
    prompt += """
    Basado en la ventaja de tipos, HP y poder de los movimientos, ¿qué movimiento deberías usar?
    - Si un movimiento tiene ventaja de tipo sobre el oponente, úsalo.
    - Si no hay una ventaja clara, usa el movimiento más fuerte que tengas.
    Basado en la ventaja de tipos, nivel y poder de los movimientos, ¿A que pokemon deberias cambiar cuando el actual es derrotado?
    - Si tu pokemon actual es derrotado (HP <= 0), considera cambiar a un Pokémon de tu banca que tenga ventaja de tipo sobre el oponente.
    - Si no hay ventaja de tipo clara, entonces elige el pokemon sin debilidad al tipo del oponente.
    - Si estas en desventaja de tipo con cualquier pokemon, elige el que tenga nivel mas alto y/o movimentos con poder mas alto.

    Responde únicamente con el número del índice del movimiento que eliges (del 0 al 3). No incluyas ninguna otra palabra, explicación o formato JSON. Solo el número.
    En el caso de los cambios de pokemon, se respondera dentro del rango de indices de 0 a 2
    """
    return prompt

@router.post("/decide-action", response_model=int)
async def decide_action(state: BattleState):
    """
    Recibe el estado de la batalla y devuelve el índice del movimiento decidido por la IA.
    """
    # Fallback: si no hay movimientos, no se puede hacer nada.
    if not state.opponent_pokemon.moves:
        raise HTTPException(status_code=400, detail="El Pokémon oponente no tiene movimientos.")

    prompt = format_prompt(state)
    
    try:
        async with httpx.AsyncClient(timeout=20.0) as client:
            response = await client.post(
                OLLAMA_API_URL,
                json={
                    "model": "mistral",
                    "messages": [{"role": "user", "content": prompt}],
                    "stream": False
                }
            )
            response.raise_for_status()
            
            ollama_response = response.json()
            message_content = ollama_response.get("message", {}).get("content", "")
            
            # La respuesta de la IA debería ser solo un número.
            move_index = int(message_content.strip())

            if 0 <= move_index < len(state.opponent_pokemon.moves):
                return move_index

    except (httpx.RequestError, json.JSONDecodeError, ValueError, KeyError) as e:
        # Si Ollama falla o la respuesta es inválida, se usa una lógica de fallback simple.
        pass

    # --- Lógica de Fallback ---
    # Si la IA falla, se elige un movimiento aleatorio que no tenga 0 de poder.
    available_moves = [i for i, move in enumerate(state.opponent_pokemon.moves) if move.power > 0]
    if available_moves:
        return random.choice(available_moves)
    
    # Si no hay movimientos con poder, devuelve el primero.
    return 0