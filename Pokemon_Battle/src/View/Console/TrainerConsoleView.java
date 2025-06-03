package View.Console;

import Controller.PokemonsSelectionController;
import Controller.TrainerController;
import View.Interfaces.TrainerView;
import java.util.Scanner;

public class TrainerConsoleView implements TrainerView {
    private final Scanner scanner;
    private TrainerController controller;
    private boolean isRunning;

    public TrainerConsoleView() {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    @Override
    public void showMenu() {
        while (isRunning) {
            System.out.println("=== Que desea hacer ===");
            System.out.println("1. Registrar Entrenadores");
            System.out.println("2. Cambiar a Vista GUI");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1" -> {
                    System.out.println("\n=== Registro de Entrenadores ===");
                    System.out.println("Ingrese los nombres de los entrenadores:");
                    
                    System.out.print("Nombre del Entrenador 1: ");
                    String nombre1 = scanner.nextLine().trim();
                    
                    System.out.print("Nombre del Entrenador 2: ");
                    String nombre2 = scanner.nextLine().trim();

                    controller.proccessTrainers(nombre1, nombre2);
                    System.out.println("Entrenadores registrados correctamente.");
                    isRunning = false;
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
    public void setController(TrainerController controller) {
        this.controller = (TrainerController) controller;
    }

    @Override
    public void showValidationError(String mensaje) {
        System.out.println("ERROR: " + mensaje);
    }

    @Override
    public void clearFields() {
        // No es necesario en consola
    }

    @Override
    public boolean validateNames(String nombre1, String nombre2) {
        if (nombre1.isEmpty() || nombre2.isEmpty()) {
            showValidationError("Los nombres no pueden estar vacíos");
            return false;
        }
        if (nombre1.equals(nombre2)) {
            showValidationError("Los nombres deben ser diferentes");
            return false;
        }
        if (nombre1.length() < 3 || nombre2.length() < 3) {
            showValidationError("Los nombres deben tener al menos 3 caracteres");
            return false;
        }
        if (nombre1.length() > 12 || nombre2.length() > 12) {
            showValidationError("Los nombres no pueden tener más de 12 caracteres");
            return false;
        }
        return true;
    }

    @Override
    public void nextView() {
        PokemonsSelectionConsoleView pokemonSelectView = new PokemonsSelectionConsoleView();
        PokemonsSelectionController pokemonSelectController = new PokemonsSelectionController(pokemonSelectView, controller.getTrainers(), false);
        pokemonSelectController.initialize();
    }
}