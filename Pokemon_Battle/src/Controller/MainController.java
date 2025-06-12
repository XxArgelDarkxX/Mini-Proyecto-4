package Controller;

import View.Console.MainConsoleView;
import View.GUI.MainGUIView;
import View.Interfaces.MainView;

public class MainController {
    private MainView view;
    private boolean isGui;
    
    public MainController(MainView view, boolean isGui) {
        this.view = view;
        this.isGui = isGui;
        view.setController(this);
    }

    public void initialize() {
        view.playMusic("/View/Utils/Pokemon-RubySapphireEmerald-Pokemon-Center.wav");
        view.showMenu();
    }

    // Método para iniciar el juego
    // Este método se llamará cuando el usuario quiera iniciar el juego
    // En este caso, simplemente cambiaremos a la vista de Entrenador
    public void nextView() {
        // Aquí irá la lógica para iniciar el juego
        if (!isGui) {
            view.showMessage("Dirigiéndote a la vista de Entrenador...");
            view.nextView();
        } else {
            view.nextView();
        }
    }

    // Método para cambiar entre la vista de consola y la vista gráfica
    // Este método se llamará cuando el usuario quiera cambiar la vista

    public void changeView() {
        if (isGui) {
            view = new MainConsoleView();
        } else {
            view = new MainGUIView();
        }
        isGui = !isGui;
        view.setController(this);
        view.showMenu();
    }

    // Método para finalizar el juego
    // Este método se llamará cuando el usuario quiera salir del juego
    public void finish() {
        view.stopMusic();
        view.showMessage("¡Gracias por jugar!");
        System.exit(0);
    }

    

}