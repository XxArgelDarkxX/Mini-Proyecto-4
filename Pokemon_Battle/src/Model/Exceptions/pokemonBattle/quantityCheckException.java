package Model.Exceptions.pokemonBattle;

public class quantityCheckException extends Exception {

    public quantityCheckException() {
        super("Se requieren exactamente 2 entrenadores y 2 Pokémon"); 
    }
}
