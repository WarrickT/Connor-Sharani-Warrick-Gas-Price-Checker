import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The main page panel, inherited from its parent JPanel class.
 * It has access to the Frame class and Facade class to access the backend and allow for 
 * page navigation.
 */
public class MainPage extends JPanel{
    //Watch out for encapsulation

    private Frame frame;
    private Facade facade;

    //The input boxes available to the user.
    private JComboBox<String> stationDropdown;
    private JTextField addressField;
    private JComboBox<String> radiusDropdown;

    //The options available for the dropdown boxes.
    private String[] stationOptions = {"Regular", "Midgrade", "Premium", "Diesel", "Tesla Supercharge"};
    private String[] radiusOptions = {"2.5", "5", "7.5", "10", "12.5", "15"};

    /**
     * The MainPage constructor method
     * @param frame Reference to the frame object to allow for navigation between frames
     * @param facade Reference to the facade object to allow for interaction with backend
     */
    MainPage(Frame frame, Facade facade){
        //Set the layout to null to allow for manual coordinate placements.
        super(null);
        this.frame = frame;
        this.facade = facade;

        //Add the input boxes and findGasbutton to the scream
        createInputs();
        createFindGasButton();

        //Overwrite the current screen with the boxes and buttons. 
        revalidate();
        repaint();
    }

    //Creating the input boxes. 
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

    //Creating the "Find Gas" button.
    void createFindGasButton() {
        JButton findGasButton = new JButton("Find Gas");

        //Try-catch must be implemented.
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
        this.add(findGasButton);
    }

    //Refers back to the facade to check if the address is indeedly in Ontario.
    void checkAddress() throws IOException, InterruptedException{
        if(!facade.setUserAddress(addressField.getText())){
            JOptionPane.showMessageDialog(this,"Please Enter A Valid Address in Ontario. \nEnter the city name for better results.","Reprompt", JOptionPane.WARNING_MESSAGE);
        }
        else{
            frame.showSecondPage();
        }
    }

    //Returns the station type from the input boxes.
    String getStation(){
        return stationDropdown.getSelectedItem().toString();
    }
    //Returns the user's radius from the input boxes.
    double getRadius(){
        return Double.parseDouble(radiusDropdown.getSelectedItem().toString());
    }
    
}
