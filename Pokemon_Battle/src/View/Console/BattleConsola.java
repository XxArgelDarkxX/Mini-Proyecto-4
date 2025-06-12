package View.Console;

import Controller.BattleController;
import Model.Exceptions.pokemonBattle.pokemonNotFoundException;
import Model.Exceptions.pokemonBattle.quantityCheckException;
import Model.Pokemon;
import Model.Trainer;
import java.util.Scanner;

public class BattleConsola {
    private final Trainer[] trainers;
    private final String[] selectedPokemons;
    private final Pokemon[] selectedPokemon;
    private final Scanner scanner;
    private final BattleController controller;
    private boolean battleEnded;

    public BattleConsola(Trainer[] trainers, String[] selectedPokemons, BattleController controller) {
        this.trainers = trainers;
        this.selectedPokemons = selectedPokemons != null ? selectedPokemons : new String[2];
        this.selectedPokemon = new Pokemon[2];
        this.scanner = new Scanner(System.in);
        this.controller = controller;
        this.battleEnded = false;
    }

    public void startBattle() throws quantityCheckException, pokemonNotFoundException {
        System.out.println("\n=== INICIO DE BATALLA POKÉMON ===");

        // Selección inicial de Pokémon si no están pre-seleccionados
        if (!arePokemonsSelected()) {
            choosePokemons();
        }

        // Cargar los Pokémon seleccionados
        loadSelectedPokemons();

        // Bucle principal de batalla
        while (!battleEnded) {
            displayBattleStatus();
            menuBattleOptions();

            if (!battleEnded) {
                battleRound();
            }
        }
    }

    private boolean arePokemonsSelected() {
        return selectedPokemons[0] != null && !selectedPokemons[0].isEmpty() &&
                selectedPokemons[1] != null && !selectedPokemons[1].isEmpty();
    }

    private void loadSelectedPokemons() {
        for (int trainerIdx = 0; trainerIdx < trainers.length; trainerIdx++) {
            boolean found = false;
            for (Pokemon pokemon : trainers[trainerIdx].getPokemonTeam()) {
                if (pokemon.getPokedexId().equalsIgnoreCase(selectedPokemons[trainerIdx].trim())) {
                    selectedPokemon[trainerIdx] = pokemon;
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("¡Pokémon no encontrado para el Entrenador " + (trainerIdx + 1) + "!");
                choosePokemon(trainerIdx);
            }
        }
    }

    private void choosePokemons() {
        System.out.println("\n=== SELECCIÓN DE POKÉMON ===");
        for (int i = 0; i < trainers.length; i++) {
            if (selectedPokemons[i] == null || selectedPokemons[i].isEmpty()) {
                choosePokemon(i);
            }
        }
    }

    private void choosePokemon(int trainerIndex) {
        showPokemonList(trainers[trainerIndex].getPokemonTeam().toArray(new Pokemon[0]),
                trainers[trainerIndex].getName());

        while (true) {
            System.out.printf("\nEntrenador %d - Ingrese ID del Pokémon: ", trainerIndex + 1);
            String pokemonId = scanner.nextLine().trim();

            // Validar que el Pokémon existe
            for (Pokemon pokemon : trainers[trainerIndex].getPokemonTeam()) {
                if (pokemon.getPokedexId().equalsIgnoreCase(pokemonId)) {
                    selectedPokemons[trainerIndex] = pokemonId;
                    selectedPokemon[trainerIndex] = pokemon;
                    return;
                }
            }

            System.out.println("¡ID de Pokémon no válido! Intente nuevamente.");
        }
    }

    private void showPokemonList(Pokemon[] pokemons, String trainerName) {
        System.out.println("\nPokémon disponibles para " + trainerName + ":");
        for (Pokemon pokemon : pokemons) {
            System.out.printf("ID: %s | Nombre: %s | HP: %d | Tipo: %s%n",
                    pokemon.getPokedexId(),
                    pokemon.getName(),
                    pokemon.getHp(),
                    pokemon.getType()
            );
        }
    }

    private void menuBattleOptions() throws quantityCheckException, pokemonNotFoundException {
        System.out.println("\n=== OPCIONES DE BATALLA ===");
        System.out.println("1. Continuar batalla");
        System.out.println("2. Cambiar a interfaz gráfica");
        System.out.println("3. Rendirse");
        System.out.print("Seleccione una opción: ");

        int option = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        switch (option) {
            case 1:
                break; // Continuar con la batalla
            case 2:
                controller.changeView();
                battleEnded = true;
                break;
            case 3:
                System.out.println("¡Has abandonado la batalla!");
                battleEnded = true;
                break;
            default:
                System.out.println("Opción no válida. Continuando con la batalla...");
        }
    }

    private void battleRound() {
        int[] selectedAttacks = new int[2];

        // Selección de ataques
        for (int i = 0; i < selectedPokemon.length; i++) {
            System.out.printf("\nEntrenador %d - Seleccione ataque para %s:%n",
                    i + 1, selectedPokemon[i].getName());

            // Mostrar movimientos disponibles
            for (int j = 0; j < selectedPokemon[i].getMoves().size(); j++) {
                System.out.printf("%d. %s (Poder: %d)%n",
                        j + 1,
                        selectedPokemon[i].getMoves().get(j).getName(),
                        selectedPokemon[i].getMoves().get(j).getPower());
            }

            System.out.print("Seleccione ataque: ");
            selectedAttacks[i] = scanner.nextInt() - 1; // Convertir a índice 0-based
            scanner.nextLine(); // Limpiar buffer
        }

        // Ejecutar turno de batalla
        executeBattleTurn(selectedAttacks);

        // Verificar si la batalla ha terminado
        checkBattleEnd();
    }

    private void executeBattleTurn(int[] selectedAttacks) {
        // Determinar orden de ataque por velocidad
        int firstAttacker = selectedPokemon[0].getSpeed() >= selectedPokemon[1].getSpeed() ? 0 : 1;
        int secondAttacker = 1 - firstAttacker;

        // Primer ataque
        executeAttack(firstAttacker, secondAttacker, selectedAttacks[firstAttacker]);

        // Segundo ataque si el defensor sigue activo
        if (selectedPokemon[secondAttacker].getHp() > 0) {
            executeAttack(secondAttacker, firstAttacker, selectedAttacks[secondAttacker]);
        }
    }

    private void executeAttack(int attackerIdx, int defenderIdx, int attackIdx) {
        Pokemon attacker = selectedPokemon[attackerIdx];
        Pokemon defender = selectedPokemon[defenderIdx];

        System.out.printf("%n%s usa %s!%n",
                attacker.getName(),
                attacker.getMoves().get(attackIdx).getName());

        attacker.movement(defender, attackIdx);

        System.out.printf("%s ahora tiene %d HP.%n",
                defender.getName(),
                defender.getHp());
    }

    private void checkBattleEnd() {
        for (int i = 0; i < selectedPokemon.length; i++) {
            if (selectedPokemon[i].getHp() <= 0) {
                System.out.printf("%n¡%s de %s se ha debilitado!%n",
                        selectedPokemon[i].getName(),
                        trainers[i].getName());

                int winnerIdx = 1 - i;
                System.out.printf("¡%s gana la batalla!%n", trainers[winnerIdx].getName());
                battleEnded = true;
                return;
            }
        }
    }

    private void displayBattleStatus() {
        System.out.println("\n=== ESTADO DE LA BATALLA ===");
        for (int i = 0; i < selectedPokemon.length; i++) {
            System.out.printf("%s (%s): %d HP%n",
                    selectedPokemon[i].getName(),
                    trainers[i].getName(),
                    selectedPokemon[i].getHp());
        }
    }
}