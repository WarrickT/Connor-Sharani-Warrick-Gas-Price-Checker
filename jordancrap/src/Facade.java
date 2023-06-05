import java.io.IOException;

import javax.swing.SwingUtilities;

public class Facade {
    Frame guiFrame = new Frame();
    CSVWriter csvWriter;

    Facade() throws IOException, InterruptedException{
        SwingUtilities.invokeLater(() -> guiFrame.setVisible(true)); 
    }

    //Use Action Listener for CSVWriter
}
