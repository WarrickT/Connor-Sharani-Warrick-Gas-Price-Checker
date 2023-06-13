import java.awt.Dimension;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The main page panel, inherited from its parent JPanel class.
 * It has access to the Frame class and Facade class to access the backend and allow for 
 * page navigation.
 */
public class MainPage extends JPanel{
    private Frame frame;
    private Facade facade;

    //The input boxes available to the user.
    private JComboBox<String> stationDropdown;
    private JTextField addressField;
    private JComboBox<String> radiusDropdown;

    private JLabel gasLabel;
    private JLabel locationLabel;
    private JLabel budgetLabel;
    private JLabel maintitleLabel;

    //The options available for the dropdown boxes.
    private String[] stationOptions = {"Regular", "Midgrade", "Premium", "Diesel", "Tesla Supercharge"};
    private String[] radiusOptions = {"2.5", "5", "7.5", "10", "12.5", "15"};

    private JLabel backgroundLabel = new JLabel();
    private ImageIcon icon = new ImageIcon("Images/LocalGasPriceBackground.png");

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

        
        //Creating Labels (Tiles and subtiles) on the main page and assigning coordinates  
        gasLabel = new JLabel("Choose your gas type");
        gasLabel.setBounds(550, 140, 200, 50);
        locationLabel = new JLabel("Please enter your address:");
        locationLabel.setBounds(550,240,200,50); 
        budgetLabel = new JLabel("Select your radius of search:");
        budgetLabel.setBounds(550,310,300,100);

        backgroundLabel.setBounds(0, 0, 900, 600);
        backgroundLabel.setIcon(icon);

        this.add(gasLabel);
        this.add(locationLabel);
        this.add(budgetLabel);

        //Add the input boxes and findGasbutton to the scream
        createInputs();
        createFindGasButton();

        this.add(backgroundLabel);

        //Overwrite the current screen with the boxes and buttons. 
        revalidate();
        repaint();
    }

    /**
     * Creating the input boxes. 
     */
    void createInputs() {
        stationDropdown = new JComboBox<>(stationOptions);
        stationDropdown.setSelectedIndex(0);
        stationDropdown.setBounds(550, 180, 200, 50);

        addressField = new JTextField();
        addressField.setPreferredSize(new Dimension(200, 50));
        addressField.setBounds(525, 275, 350, 50);

        radiusDropdown = new JComboBox<>(radiusOptions);
        radiusDropdown.setSelectedIndex(0);
        radiusDropdown.setBounds(550, 375, 100, 50);

        this.add(stationDropdown);
        this.add(addressField);
        this.add(radiusDropdown);
    }

    /**
     * Creating the find gas button.
     */
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
        findGasButton.setBounds(650, 450, 200, 100);
        this.add(findGasButton);
    }

    /**
     * Refers back to facade to see if address is indeedly in Ontairo. 
     * @throws IOException Throws IOException from BufferedReader and FileWriter
     * @throws InterruptedException Throws InterruptedException from webscraping
     */
    void checkAddress() throws IOException, InterruptedException{
        if(!facade.setUserAddress(addressField.getText())){
            JOptionPane.showMessageDialog(this,"Please Enter A Valid Address in Ontario. \nEnter the city name for better results.","Reprompt", JOptionPane.WARNING_MESSAGE);
        }
        else{
            frame.showSecondPage();
        }
    }

    /**
     * Returns station type from input box. 
     * @return station type from input box. 
     * 
     */
    String getStation(){
        return stationDropdown.getSelectedItem().toString();
    }
    /**
     * Returns radius from input box.
     * @return double value of radius from input box. 
     */
    double getRadius(){
        return Double.parseDouble(radiusDropdown.getSelectedItem().toString());
    }
    
}
