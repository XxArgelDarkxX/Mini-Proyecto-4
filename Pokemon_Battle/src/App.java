import Controller.MainController;
import View.GUI.MainGUIView;
public class App {
    public static void main(String[] args) throws Exception {
        MainGUIView main = new MainGUIView();
        MainController controller = new MainController(main, true);
        controller.initialize();
    }
}