package Model.Exceptions.pokemonBattle;

public class pokemonSelectionMovementException extends Exception {
    
    public pokemonSelectionMovementException() {
        super("Ambos entrenadores deben seleccionar un movimiento primero"); // Llama al constructor de la clase padre Exception con el mensaje proporcionado
    }
    
}
