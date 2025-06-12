package Controller;

import Model.Trainer;
import View.Interfaces.BattleView;

public class BattleController {
    private BattleView view;
    private boolean isGui;
    private Trainer[] trainers;
    private String[] selectedPokemons;

    public BattleController(BattleView view, boolean isGui) {
        this.view = view;
        this.isGui = isGui;
        if(this.view != null) {
            this.view.setController(this);
        }
        this.trainers = new Trainer[2];
    }

    public void changeView() {
        if (isGui) {
            // Cambiar de GUI a consola
            View.Console.BattleConsola consola = new View.Console.BattleConsola(trainers, selectedPokemons, this);
            consola.startBattle();
        } else {
            // Cambiar de consola a GUI
            View.GUI.BattleGUIView gui = new View.GUI.BattleGUIView(trainers, selectedPokemons);
            gui.setVisible(true);
        }
    }
}