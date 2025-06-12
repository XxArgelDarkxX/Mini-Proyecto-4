package View.Console;

import Controller.MainController;
import Controller.TrainerController;
import Model.BattleHistory;
import View.Interfaces.MainView;
import View.Utils.MusicPlayer;
import java.util.Scanner;

public class MainConsoleView implements MainView {
    private Scanner scanner;
    private boolean isRunning;
    private MainController controller;

    public MainConsoleView() {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    @Override
    public void showMenu() {
        System.out.println("Bienvenido al juego de Pokémon Battle");
        System.out.println("1. Jugar");
        System.out.println("2. Cambiar a GUI");
        System.out.println("3. Salir");
        System.out.println("4. Mostrar historial de batallas");
        System.out.print("Seleccione una opción: ");
        while (isRunning) {
            String input = scanner.nextLine();
            switch (input) {
                case "1" -> 
                    controller.nextView();
                case "2" -> {
                    controller.changeView();
                    isRunning = false;
                }
                case "3" -> {
                    System.out.println("Saliendo del juego...");
                    isRunning = false;
                }
                case "4" -> {
                    BattleHistory history = new BattleHistory("Entrenador1", "Entrenador2");
                    history.saveBattleHistory();
                    System.out.println("Historial de batallas:");
                    System.out.println(history.getHistoryAsText());
                }
                default -> System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    @Override
    public void showMessage(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public void setController(MainController controller) {
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
    public void nextView() {
        System.out.println("Iniciando el juego...");
        TrainerController trainerController = new TrainerController(new TrainerConsoleView(), false);
        trainerController.initialize();
        isRunning = false; // Para salir del bucle de ejecución
    }

}