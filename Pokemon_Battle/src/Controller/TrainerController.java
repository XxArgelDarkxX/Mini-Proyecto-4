package Controller;

import Model.Data.Data;
import Model.Trainer;
import View.Interfaces.TrainerView;

public class TrainerController {
    private TrainerView view;
    private final Trainer[] trainers;
    private boolean isGui;

    public TrainerController(TrainerView view, boolean isGui) {
        this.view = view;
        this.isGui = isGui;
        this.trainers = new Trainer[2];
        view.setController(this);
    }

    public void initialize() {
        view.showMenu();
    }

    public void proccessTrainers(String trainerName1, String trainerName2) {
        if(!view.validateNames(trainerName1, trainerName2)){
            view.validateNames(trainerName1, trainerName2);
        }else{
            // Actualizar los objetos entrenador
            trainers[0] = new Trainer();
            trainers[1] = new Trainer();
            trainers[0].setName(trainerName1);
            trainers[1].setName(trainerName2);
            //agregar los pokemones
            Data.initializeData();
            trainers[0].setRandomPokemonTeam(Data.getPokemons());
            trainers[1].setRandomPokemonTeam(Data.getPokemons());

            view.nextView();
        }
    }

    public void changeView() {
        if (isGui) {
            view = new View.Console.TrainerConsoleView();
        } else {
            view = new View.GUI.TrainerGUIView();
        }
        isGui = !isGui;
        view.setController(this);
        view.showMenu();
    }

    public void clearFields() {
        view.clearFields();
    }

    public Trainer[] getTrainers() {
        return trainers;
    }
}