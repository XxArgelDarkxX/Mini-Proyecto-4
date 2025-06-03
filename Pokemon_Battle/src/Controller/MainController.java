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

    public void nextView() {
        // Aquí irá la lógica para iniciar el juego
        if (!isGui) {
            view.showMessage("Dirigiéndote a la vista de Entrenador...");
            view.nextView();
        } else {
            view.nextView();
        }
    }

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

    public void finish() {
        view.stopMusic();
        view.showMessage("¡Gracias por jugar!");
        System.exit(0);
    }
}