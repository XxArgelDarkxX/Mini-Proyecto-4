package Model;

import java.util.ArrayList;

/**
 * Administra la lógica de la batalla Pokémon incluyendo:
 * - Orden de turnos basado en la velocidad de los Pokémon
 * - Ejecución de movimientos y sus efectos
 * - Determinación del resultado de la batalla
 */
public class PokemonBattle {
    private final Trainer[] trainers;
    private final ArrayList<Pokemon> battlingPokemons;
    private byte trainer1Move = -1;
    private byte trainer2Move = -1;
    private byte trainer1CurrentIndex = 0;
    private byte trainer2CurrentIndex = 0;
    
    public PokemonBattle(Trainer[] trainers, Pokemon[] initialPokemons) {
        if (trainers == null || trainers.length != 2 || 
            initialPokemons == null || initialPokemons.length != 2) {
            throw new IllegalArgumentException("Se requieren exactamente 2 entrenadores y 2 Pokémon");
        }
        
        this.trainers = trainers;
        this.battlingPokemons = new ArrayList<>(2);
        this.battlingPokemons.add(initialPokemons[0]);
        this.battlingPokemons.add(initialPokemons[1]);
        
        initializeCurrentPokemonIndexes();
    }
    
    private void initializeCurrentPokemonIndexes() {
        trainer1CurrentIndex = findPokemonIndex(0, battlingPokemons.get(0));
        trainer2CurrentIndex = findPokemonIndex(1, battlingPokemons.get(1));
    }
    
    private byte findPokemonIndex(int trainer, Pokemon pokemon) {
        for (byte i = 0; i < trainers[trainer].getPokemonTeam().size(); i++) {
            if (pokemon.equals(trainers[trainer].getPokemonTeam().get(i))) {
                return i;
            }
        }
        throw new IllegalArgumentException("Pokémon no encontrado en el equipo del entrenador");
    }
    
    public void selectPokemon(byte trainer, byte pokemonIndex) {
        validateTrainerIndex(trainer);
        
        if (trainer == 0) {
            trainer1CurrentIndex = (byte) pokemonIndex;
            battlingPokemons.set(0, getCurrentPokemon(0));
        } else {
            trainer2CurrentIndex = (byte) pokemonIndex;
            battlingPokemons.set(1, getCurrentPokemon(1));
        }
    }
    
    private void validateTrainerIndex(byte trainer) {
        if (trainer != 0 && trainer != 1) {
            throw new IllegalArgumentException("El índice del entrenador debe ser 0 o 1");
        }
    }
    
    public void setMoveTrainer1(byte move) {
        this.trainer1Move = move;
    }
    
    public void setMoveTrainer2(byte move) {
        this.trainer2Move = move;
    }
    
    /**
     * Ejecuta un turno de batalla basado en la velocidad de los Pokémon
     * Retorna el resultado del turno incluyendo cambios de HP y estado de debilitamiento
     */
    public BattleResult executeTurn() {
        if (trainer1Move == -1 || trainer2Move == -1) {
            throw new IllegalStateException("Ambos entrenadores deben seleccionar un movimiento primero");
        }
        
        Pokemon pokemon1 = getCurrentPokemon(0);
        Pokemon pokemon2 = getCurrentPokemon(1);
        BattleResult result = new BattleResult();
        
        if (pokemon1.getSpeed() >= pokemon2.getSpeed()) {
            executeAttack(pokemon1, pokemon2, trainer1Move, result, true);
            if (!result.isPokemon2Fainted()) {
                executeAttack(pokemon2, pokemon1, trainer2Move, result, false);
            }
        } else {
            executeAttack(pokemon2, pokemon1, trainer2Move, result, false);
            if (!result.isPokemon1Fainted()) {
                executeAttack(pokemon1, pokemon2, trainer1Move, result, true);
            }
        }
        
        // Reinicia los movimientos para el siguiente turno
        trainer1Move = -1;
        trainer2Move = -1;
        
        return result;
    }
    
    private void executeAttack(Pokemon attacker, Pokemon defender, byte move, 
                             BattleResult result, boolean isTrainer1Attacking) {
        attacker.executeMove(defender, move);
        
        if (isTrainer1Attacking) {
            result.setPokemon2HP(defender.getHp());
            if (defender.getHp() <= 0) {
                defender.setHp((short) 0);
                result.setPokemon2Fainted(true);
            }
        } else {
            result.setPokemon1HP(defender.getHp());
            if (defender.getHp() <= 0) {
                defender.setHp((short) 0);
                result.setPokemon1Fainted(true);
            }
        }
    }
    
    public boolean isBattleOver() {
        return !trainerHasLivePokemon((byte) 0) || !trainerHasLivePokemon(1);
    }
    
    private boolean trainerHasLivePokemon(int trainer) {
        for (Pokemon pokemon : trainers[trainer].getPokemonTeam()) {
            if (pokemon.getHp() > 0) {
                return true;
            }
        }
        return false;
    }
    
    public String getWinnerName() {
        if (!isBattleOver()) {
            return null;
        }
        return trainerHasLivePokemon(0) ? trainers[0].getName() : trainers[1].getName();
    }
    
    // Getters
    public Trainer[] getTrainers() { return trainers; }
    public ArrayList<Pokemon> getBattlingPokemons() { return battlingPokemons; }
    public byte getCurrentPokemonIndex(int trainer) { 
        return trainer == 0 ? trainer1CurrentIndex : trainer2CurrentIndex; 
    }
    public Pokemon getCurrentPokemon(int trainer) { 
        return trainers[trainer].getPokemonTeam().get(
            trainer == 0 ? trainer1CurrentIndex : trainer2CurrentIndex
        ); 
    }
}

class BattleResult {
    private short pokemon1HP;
    private short pokemon2HP;
    private boolean pokemon1Fainted;
    private boolean pokemon2Fainted;
    
    public short getPokemon1HP() { return pokemon1HP; }
    public void setPokemon1HP(short hp) { this.pokemon1HP = hp; }
    public short getPokemon2HP() { return pokemon2HP; }
    public void setPokemon2HP(short hp) { this.pokemon2HP = hp; }
    public boolean isPokemon1Fainted() { return pokemon1Fainted; }
    public void setPokemon1Fainted(boolean fainted) { this.pokemon1Fainted = fainted; }
    public boolean isPokemon2Fainted() { return pokemon2Fainted; }
    public void setPokemon2Fainted(boolean fainted) { this.pokemon2Fainted = fainted; }
}