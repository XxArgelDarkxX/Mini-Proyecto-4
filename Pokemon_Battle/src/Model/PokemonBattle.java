package Model;

import java.util.ArrayList;

import Model.Exceptions.pokemonBattle.pokemonNotFoundException;
import Model.Exceptions.pokemonBattle.pokemonSelectionMovementException;
import Model.Exceptions.pokemonBattle.quantityCheckException;
import Model.Exceptions.pokemonBattle.indexErrorException;

public class PokemonBattle {
    private final Trainer[] trainers;
    private final ArrayList<Pokemon> battlingPokemons;
    private byte trainer1Move = -1;
    private byte trainer2Move = -1;
    private byte trainer1CurrentIndex = 0;
    private byte trainer2CurrentIndex = 0;
    private final BattleHistory battleHistory;
    private boolean battleEnded = false;

    public PokemonBattle(Trainer[] trainers, Pokemon[] initialPokemons) 
            throws quantityCheckException, pokemonNotFoundException {
        
        if (trainers == null || trainers.length != 2 || 
            initialPokemons == null || initialPokemons.length != 2) {
            throw new quantityCheckException();
        }
        
        this.trainers = trainers;
        this.battlingPokemons = new ArrayList<>(2);
        this.battlingPokemons.add(initialPokemons[0]);
        this.battlingPokemons.add(initialPokemons[1]);
        this.battleHistory = new BattleHistory(trainers[0].getName(), trainers[1].getName());
        
        initializeCurrentPokemonIndexes();
        logInitialBattleState(initialPokemons);
    }
    
    private void initializeCurrentPokemonIndexes() throws pokemonNotFoundException {
        trainer1CurrentIndex = findPokemonIndex(0, battlingPokemons.get(0));
        trainer2CurrentIndex = findPokemonIndex(1, battlingPokemons.get(1));
    }
    
    private byte findPokemonIndex(int trainer, Pokemon pokemon) throws pokemonNotFoundException {
        for (byte i = 0; i < trainers[trainer].getPokemonTeam().size(); i++) {
            if (pokemon.equals(trainers[trainer].getPokemonTeam().get(i))) {
                return i;
            }
        }
        throw new pokemonNotFoundException();
    }
    
    private void logInitialBattleState(Pokemon[] initialPokemons) {
        battleHistory.logBattleStart();
        battleHistory.logPokemonSent(0, initialPokemons[0].getName());
        battleHistory.logPokemonSent(1, initialPokemons[1].getName());
    }
    
    public void selectPokemon(byte trainer, byte pokemonIndex) throws indexErrorException {
        validateTrainerIndex(trainer);
        
        Pokemon newPokemon = trainers[trainer].getPokemonTeam().get(pokemonIndex);
        if (trainer == 0) {
            battleHistory.logPokemonChange(0, battlingPokemons.get(0).getName(), newPokemon.getName());
            trainer1CurrentIndex = pokemonIndex;
            battlingPokemons.set(0, newPokemon);
        } else {
            battleHistory.logPokemonChange(1, battlingPokemons.get(1).getName(), newPokemon.getName());
            trainer2CurrentIndex = pokemonIndex;
            battlingPokemons.set(1, newPokemon);
        }
    }
    
    private void validateTrainerIndex(byte trainer) throws indexErrorException {
        if (trainer != 0 && trainer != 1) {
            throw new indexErrorException();
        }
    }
    
    public void setMoveTrainer1(byte move) {
        this.trainer1Move = move;
    }
    
    public void setMoveTrainer2(byte move) {
        this.trainer2Move = move;
    }
    
    public BattleResult executeTurn() throws pokemonSelectionMovementException {
        if (battleEnded) {
            throw new IllegalStateException("La batalla ya ha terminado");
        }
        
        if (trainer1Move == -1 || trainer2Move == -1) {
            throw new pokemonSelectionMovementException();
        }
        
        Pokemon pokemon1 = getCurrentPokemon(0);
        Pokemon pokemon2 = getCurrentPokemon(1);
        BattleResult result = new BattleResult();
        
        battleHistory.logMoveSelection(0, pokemon1.getName(), pokemon1.getMoveName(trainer1Move));
        battleHistory.logMoveSelection(1, pokemon2.getName(), pokemon2.getMoveName(trainer2Move));
        
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
        
        trainer1Move = -1;
        trainer2Move = -1;
        
        if (isBattleOver()) {
            battleEnded = true;
            battleHistory.logBattleEnd(getWinnerName());
            battleHistory.saveBattleHistory();
        }
        
        return result;
    }
    
    private void executeAttack(Pokemon attacker, Pokemon defender, byte move,
                           BattleResult result, boolean isTrainer1Attacking) {
        int damageDealt = attacker.executeMove(defender, move);
        
        battleHistory.logAttack(
            attacker.getName(), 
            attacker.getMoveName(move), 
            defender.getName(), 
            damageDealt, 
            defender.getHp()
        );
        
        if (isTrainer1Attacking) {
            result.setPokemon2HP(defender.getHp());
            if (defender.getHp() <= 0) {
                defender.setHp((short) 0);
                result.setPokemon2Fainted(true);
                battleHistory.logPokemonFainted(defender.getName());
            }
        } else {
            result.setPokemon1HP(defender.getHp());
            if (defender.getHp() <= 0) {
                defender.setHp((short) 0);
                result.setPokemon1Fainted(true);
                battleHistory.logPokemonFainted(defender.getName());
            }
        }
    }
    
    public boolean isBattleOver() {
        return !trainerHasLivePokemon((byte) 0) || !trainerHasLivePokemon((byte) 1);
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
    
    // Métodos para acceder al historial
    public String getBattleHistoryText() {
        return battleHistory.getHistoryAsText();
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
    public BattleHistory getBattleHistory() { return battleHistory; }

    public class BattleResult {
    private short pokemon1HP;
    private short pokemon2HP;
    private boolean pokemon1Fainted;
    private boolean pokemon2Fainted;
    
    // Getters y setters
    public short getPokemon1HP() { return pokemon1HP; }
    public void setPokemon1HP(short hp) { this.pokemon1HP = hp; }
    public short getPokemon2HP() { return pokemon2HP; }
    public void setPokemon2HP(short hp) { this.pokemon2HP = hp; }
    public boolean isPokemon1Fainted() { return pokemon1Fainted; }
    public void setPokemon1Fainted(boolean fainted) { this.pokemon1Fainted = fainted; }
    public boolean isPokemon2Fainted() { return pokemon2Fainted; }
    public void setPokemon2Fainted(boolean fainted) { this.pokemon2Fainted = fainted; }
}
}