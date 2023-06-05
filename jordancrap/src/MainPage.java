import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class MainPage extends JPanel{
    Frame frame;
    
    private JComboBox<String> stationDropdown;
    private JComboBox<String> radiusDropdown;
    private JTextField addressField;

    JLabel mainMenuPictureLabel = new JLabel(new ImageIcon("src/Images/MainScreen.png"));

    private String[] stationOptions = {"Regular", "Midgrade", "Premium", "Diesel", "Tesla Supercharge"};
    private String[] radiusOptions = {"2.5", "5", "7.5", "10", "12.5", "15"};

    private BackendController backend;

    MainPage(Frame frame, BackendController backend){
        super(null);
        this.frame = frame;
        this.backend = backend;

        createInputs();
        this.add(createFindGasButton());

        mainMenuPictureLabel.setBounds(0, 0, 900, 600);
        this.add(mainMenuPictureLabel);
        
        revalidate();
        repaint();
    }

    void createInputs() {
        stationDropdown = new JComboBox<>(stationOptions);
        stationDropdown.setSelectedIndex(0);
        stationDropdown.setBounds(100, 330, 200, 50);

        addressField = new JTextField();
        addressField.setPreferredSize(new Dimension(200, 50));
        addressField.setBounds(100, 400, 350, 50);

        radiusDropdown = new JComboBox<>(radiusOptions);
        radiusDropdown.setSelectedIndex(0);
        radiusDropdown.setBackground(new Color(255, 172, 28));
        radiusDropdown.setBounds(100, 480, 100, 50);

        this.add(stationDropdown);
        this.add(addressField);
        this.add(radiusDropdown);
    }

    private JButton createFindGasButton() {
        JButton findGasButton = new JButton("Find Gas");
        findGasButton.addActionListener(e ->
        {
            try {
                checkAddress();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            catch(InterruptedException e2){
                e2.printStackTrace();
            }
        }
        
        );
        findGasButton.setBounds(600, 400, 200, 100);
        return findGasButton;
    }
    void checkAddress() throws IOException, InterruptedException{
        if(!backend.setUserAddress(addressField.getText())){
            JOptionPane.showMessageDialog(this,"Please Enter A Valid Address in Ontario.","Reprompt", JOptionPane.WARNING_MESSAGE);
        }
        else{
            frame.showSecondPage();
        }
    }

    String getStation(){
        return stationDropdown.getSelectedItem().toString();
    }
    double getRadius(){
        return Double.parseDouble(radiusDropdown.getSelectedItem().toString());
    }
    
}
