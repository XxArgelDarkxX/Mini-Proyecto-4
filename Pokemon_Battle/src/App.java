import Controller.MainController;
import Model.api.ApiClient;
import View.GUI.MainGUIView;
import javax.swing.JOptionPane;
public class App {
    public static void main(String[] args) throws Exception {
        ApiClient apiClient = new ApiClient();
        String username = JOptionPane.showInputDialog(null, "Usuario API:", "Login", JOptionPane.QUESTION_MESSAGE);
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe iniciar sesion para acceder a la API.");
            return;
        }

        String password = JOptionPane.showInputDialog(null, "Contrasena API:", "Login", JOptionPane.QUESTION_MESSAGE);
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar la contrasena para acceder a la API.");
            return;
        }

        try {
            apiClient.login(username.trim(), password.trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Login fallido: " + e.getMessage());
            return;
        }

        MainGUIView main = new MainGUIView();
        MainController controller = new MainController(main, true);
        controller.initialize();

    }
}