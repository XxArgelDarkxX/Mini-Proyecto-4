package View.Console;

import Controller.BattleController;
import Controller.PokemonsSelectionController;
import Model.Exceptions.pokemonBattle.pokemonNotFoundException;
import Model.Exceptions.pokemonBattle.quantityCheckException;
import Model.Pokemon;
import Model.Trainer;
import View.GUI.BattleGUIView;
import View.Interfaces.PokemonsSelectionView;
import View.Utils.MusicPlayer;
import java.util.Scanner;

public class PokemonsSelectionConsoleView implements PokemonsSelectionView {
    private final Scanner scanner;
    private PokemonsSelectionController controller;
    private boolean isRunning;

    public PokemonsSelectionConsoleView() {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    @Override
    public void showMenu() {
        while (isRunning) {
            System.out.println("=== Que desea hacer ===");
            System.out.println("1. Seleccionar Pokémon para la batalla");
            System.out.println("2. Cambiar a Vista GUI");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1" -> {
                    controller.processPokemonSelection();
                }
                case "2" -> {
                    controller.changeView();
                    isRunning = false;
                }
                case "3" -> {
                    System.out.println("Saliendo del juego...");
                    isRunning = false;
                }
                default -> System.out.println("Opción no válida. Intente de nuevo.");
            }
            
        }
    }

    @Override
    public void showPokemonList(Pokemon[] pokemons, String trainerName) {
        System.out.println("\nPokémon disponibles para " + trainerName + ":");
        for (Pokemon pokemon : pokemons) {
            System.out.printf("ID: %s | Pokemon: %s | HP: %d | Tipo: %s%n",
                pokemon.getPokedexId(),
                pokemon.getName(),
                pokemon.getHp(),
                pokemon.getType()
            );
        }
    }

    @Override
    public boolean validatePokemonSelection(String pokemon1Id, String pokemon2Id) {
        if (pokemon1Id.isEmpty() || pokemon2Id.isEmpty()) {
            showMessage("Por favor, seleccionen sus Pokemon para la batalla.");
            return false;
        }
        if (controller.getTrainers()[0].getPokemonTeam().stream()
                .noneMatch(p -> p.getPokedexId().equals(pokemon1Id))) {
            showMessage("El Pokemon seleccionado por el Entrenador 1 no es válido.");
            return false;
        }
        if (controller.getTrainers()[1].getPokemonTeam().stream()
                .noneMatch(p -> p.getPokedexId().equals(pokemon2Id))) {
            showMessage("El Pokemon seleccionado por el Entrenador 2 no es válido.");
            return false;
        }
        return true;
    }

    @Override
    public void showMessage(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public void setController(PokemonsSelectionController controller) {
        this.controller = controller;
    }

    @Override
    public void playMusic(String rutaMusica) {
        MusicPlayer.playMusic(rutaMusica);
    }

    @Override
    public void stopMusic() {
        MusicPlayer.stopMusic();
    }

    @Override
    public void showSelectedPokemon(String trainerName, Pokemon pokemon) {
        System.out.printf("%s ha seleccionado a %s (ID: %s, HP: %d, Tipo: %s)%n",
            trainerName, pokemon.getName(), pokemon.getPokedexId(), pokemon.getHp(), pokemon.getType());
    }

    @Override
    public void nextView() {
        // Obtener los IDs de los pokémon seleccionados por los entrenadores
        System.out.println("\n=== Selección de Pokémon para la Batalla ===");
        showPokemonList(controller.getTrainers()[0].getPokemonTeam().toArray(new Pokemon[0]), 
                        controller.getTrainers()[0].getName());
        System.out.print("\nEntrenador 1 - Ingrese ID del Pokémon: ");
        String pokemon1Id = scanner.nextLine().trim();

        showPokemonList(controller.getTrainers()[1].getPokemonTeam().toArray(new Pokemon[0]), 
                        controller.getTrainers()[1].getName());
        System.out.print("\nEntrenador 2 - Ingrese ID del Pokémon: ");
        String pokemon2Id = scanner.nextLine().trim();

        // Validar la selección antes de crear la vista de batalla
        if (validatePokemonSelection(pokemon1Id, pokemon2Id)) {
            stopMusic();
            playMusic("/View/Utils/Pokémon-Black-_-White-Elite-Four-Battle-Music-_HQ_.wav");
            isRunning = false;
            
            String[] selectedPokemon = new String[2];
            selectedPokemon[0] = pokemon1Id;
            selectedPokemon[1] = pokemon2Id;
            BattleController battleController = new BattleController(null, false);
            BattleConsola battleView = new BattleConsola(controller.getTrainers(), selectedPokemon,  battleController);
            battleView.startBattle();
        }
    }
}