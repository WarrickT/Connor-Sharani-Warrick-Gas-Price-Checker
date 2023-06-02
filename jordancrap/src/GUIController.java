import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

public class GUIController extends JFrame {
    //Main Page
    private JPanel mainPanel;
    private JComboBox<String> stationDropdown;
    private JComboBox<String> radiusDropdown;
    private JTextField addressField;
    private JPanel buttonsPanel;
    private JPanel interactablesPanel;

    private String[] stationOptions = {"Regular", "Midgrade", "Premium", "Diesel", "Tesla Supercharge"};
    private String[] radiusOptions = {"2.5", "5", "7.5", "10", "12.5", "15"};
    
    //Second Page
    private JPanel secondPagePanel;
    private JPanel titlePanel;
    private JLabel titleLabel;

    private JTable stationDataTable;
    private JScrollPane stationDataTableScroll;
    private JPanel tablePanel;
    
    private String[] columnsName = {"Station Name", "Address", "Unit Cost", "Displacement", "Directions"};
    private String[][] formattedStationData;

    //Backend
    private Facade facade = new Facade();
    private String userStationType;
    private String userLocationAddress;
    private double userRadius;

    GUIController() throws IOException{
        setTitle("Gas Finder App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createMainPanel();
        createSecondPagePanel();

        setContentPane(mainPanel);
    }

    private void showMainPage() {
        facade.clearStationData();
        //Could be button overlap
        //The database updates, but not the display!
        secondPagePanel.remove(stationDataTable);
        secondPagePanel.remove(stationDataTableScroll);
        secondPagePanel.remove(tablePanel);

        setContentPane(mainPanel);
        revalidate();
        repaint();
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        stationDropdown = new JComboBox<>(stationOptions);
        stationDropdown.setSelectedIndex(0);

        radiusDropdown = new JComboBox<>(radiusOptions);
        radiusDropdown.setSelectedIndex(0);

        addressField = new JTextField();

        buttonsPanel.add(createButton("-"));
        buttonsPanel.add(createButton("Fullscreen"));
        buttonsPanel.add(createButton("X"));

        mainPanel.add(buttonsPanel, BorderLayout.NORTH);
        mainPanel.add(createInteractablesPanel(), BorderLayout.CENTER);
        mainPanel.add(createFindGasButton(), BorderLayout.SOUTH);
    }

    private JPanel createInteractablesPanel() {
        interactablesPanel = new JPanel(new GridLayout(3, 1));
        interactablesPanel.add(stationDropdown);
        interactablesPanel.add(addressField);
        interactablesPanel.add(radiusDropdown);
        return interactablesPanel;
    }

    private JButton createButton(String label) {
        JButton button = new JButton(label);
        button.setFocusable(false);
        return button;
    }

    private JButton createFindGasButton() {
        JButton findGasButton = new JButton("Find Gas");
        findGasButton.addActionListener(e -> {
            try {
                showSecondPage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            catch(InterruptedException e2){
                e2.printStackTrace();
            }
        });
        return findGasButton;
    }

    private void createSecondPagePanel() {
        secondPagePanel = new JPanel(new BorderLayout());
        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titleLabel = new JLabel("Results");
        titlePanel.add(titleLabel);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButtonPanel.add(createBackButton());

        secondPagePanel.add(titlePanel, BorderLayout.NORTH);
        secondPagePanel.add(backButtonPanel, BorderLayout.SOUTH);
    }

    private JButton createBackButton() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMainPage());
        return backButton;
    }

    private void showSecondPage() throws IOException, InterruptedException{
        userStationType = stationDropdown.getSelectedItem().toString();
        userLocationAddress = addressField.getText();
        userRadius = Double.parseDouble(radiusDropdown.getSelectedItem().toString());

        facade.setStationType(userStationType);
        facade.setUserAddress(userLocationAddress);
        facade.setUserRadius(userRadius);

        facade.scrapeStations();
        getFormattedStationData();
        printStationData();

        createInfoPageTable();
        secondPagePanel.add(tablePanel, BorderLayout.CENTER);

        setContentPane(secondPagePanel);
        revalidate();
        repaint();
    }

    private void createInfoPageTable(){
        tablePanel = new JPanel();
        stationDataTable = new JTable(formattedStationData, columnsName);

        for(int columnNum = 0; columnNum < 5; columnNum++) {
            TableColumn column = stationDataTable.getColumnModel().getColumn(columnNum);
            if(columnNum == 1){
                column.setPreferredWidth(250);
            }
            else if(columnNum == 4){
                column.setPreferredWidth(100);
            }
            else{
                column.setPreferredWidth(120);
            }
        }
        stationDataTableScroll = new JScrollPane(stationDataTable);
        tablePanel.add(stationDataTableScroll);
    }
    void getFormattedStationData(){
        formattedStationData = facade.getFormattedStationData();
    }
    void printStationData(){
        for(String[] station:formattedStationData){
            for(String el:station){
                System.out.println(el);
            }
        }
    }

}