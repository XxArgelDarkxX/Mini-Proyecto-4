package Model.Exceptions.pokemonBattle;

public class unknownPokemonTypeException extends Exception {

    public unknownPokemonTypeException(String message) {
        super(message); // Llama al constructor de la clase padre Exception con el mensaje proporcionado
    }

}
