package View.Interfaces;
import Model.Trainer;
import Controller.BattleController;

public interface BattleView {
    void setController(BattleController controller);
    void choosePokemon(String[] selectedPokemons, Trainer[] trainers);
    void showBattleMenu(String[] selectedPokemons, Trainer[] trainers);
    void showMessage(Trainer[] trainers);
    void showWinnner(String winnerName);
    void changeView();

}
