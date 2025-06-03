package View.Interfaces;

import Controller.PokemonsSelectionController;
import Model.Pokemon;

public interface PokemonsSelectionView {
    void showMenu();
    void showMessage(String mensaje);
    void setController(PokemonsSelectionController controller);
    void showPokemonList(Pokemon[] pokemons, String trainerName);
    void showSelectedPokemon(String trainerName, Pokemon pokemon);
    boolean validatePokemonSelection(String pokemonId1, String pokemonId2);
    void playMusic(String musicFile);
    void stopMusic();
    void nextView();
}