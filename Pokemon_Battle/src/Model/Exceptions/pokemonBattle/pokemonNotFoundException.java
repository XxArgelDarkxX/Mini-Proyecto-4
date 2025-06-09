package Model.Exceptions.pokemonBattle;

public class pokemonNotFoundException extends Exception {

    public pokemonNotFoundException() {
        super("Pokémon no encontrado en el equipo del entrenador");
    }
    
}
