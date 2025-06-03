package View.Utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JToggleButton;

public class ViewChangeButton extends JToggleButton {
    private static final String BUTTON_TEXT = "Cambiar Vista";
    private static final Color HOVER_COLOR = new Color(255, 255, 255);
    private static final Color DEFAULT_COLOR = new Color(198, 198, 198);
    private static final Font BUTTON_FONT = new Font("Roboto Black", 3, 14);

    public ViewChangeButton() {
        super(BUTTON_TEXT);
        initializeButton();
    }

    private void initializeButton() {
        setFont(BUTTON_FONT);
        setBackground(DEFAULT_COLOR);
        setBorderPainted(true);
        setFocusPainted(false);
        setContentAreaFilled(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                setBackground(DEFAULT_COLOR);
            }
        });
    }
}