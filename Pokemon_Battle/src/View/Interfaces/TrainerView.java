package View.Interfaces;
import Controller.TrainerController;

public interface TrainerView {
    void showMenu();
    void showValidationError(String message);
    void clearFields();
    boolean validateNames(String name1, String name2);
    void setController(TrainerController controller);
    void nextView();
}