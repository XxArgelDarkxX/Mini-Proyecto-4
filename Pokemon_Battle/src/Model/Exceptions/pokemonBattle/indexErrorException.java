package Model.Exceptions.pokemonBattle;

public class indexErrorException extends Exception {

    public indexErrorException() {
        super("El índice del entrenador debe ser 0 o 1"); 
    }
    
}
