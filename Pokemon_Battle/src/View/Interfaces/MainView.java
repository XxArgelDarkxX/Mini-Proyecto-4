package View.Interfaces;
import Controller.MainController;

public interface MainView {
    void showMenu();
    void showMessage(String mensaje);
    void setController(MainController controlador);
    void playMusic(String rutaMusica);
    void stopMusic();
    void nextView();
}
