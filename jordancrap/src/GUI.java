import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GUI extends JFrame {
    public static void main(String[] args) throws IOException, InterruptedException{
        GUIController guiController = new GUIController();
        SwingUtilities.invokeLater(() -> guiController.setVisible(true));
    }
}