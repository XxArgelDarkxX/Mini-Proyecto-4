package Controller;

import Model.Trainer;
import View.Console.PokemonsSelectionConsoleView;
import View.GUI.PokemonsSelectionGUIView;
import View.Interfaces.PokemonsSelectionView;

public class PokemonsSelectionController {
    private PokemonsSelectionView view;
    private final Trainer[] trainers;
    private boolean isGui;
    private final String[] pokemonsBattle;

    public PokemonsSelectionController(PokemonsSelectionView view, Trainer[] trainers, boolean isGui) {
        this.view = view;
        this.trainers = trainers;
        this.isGui = isGui;
        this.pokemonsBattle = new String[2];
        view.setController(this);
    }

    public void initialize() {
        view.showMenu();
    }

    public void processPokemonSelection() {
        view.stopMusic();
        view.nextView();

        
    }

    public void changeView() {
        if (isGui) {
            view = new PokemonsSelectionConsoleView();
        } else {
            view = new PokemonsSelectionGUIView(trainers);
        }
        isGui = !isGui;
        view.setController(this);
        initialize();
    }

    public Trainer[] getTrainers() {
        return trainers;
    }

    public String[] getPokemonsBattle() {
        return pokemonsBattle;
    }
}