import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Frame extends JFrame {
    //Each page in the stored as an object in the frame. 
    private MainPage mainPage;
    private SecondPage secondPage;

    //The parent facade obejct is created to access the backend. 
    private Facade facade;

    /**
     * Constructor for the main GUI Frame. 
     * @param facade Reference to the facade object to allow for interaction between GUI and backend. 
     * @throws IOException Throws IOException from CSVWriting. 
     */
    Frame(Facade facade) throws IOException{
        this.facade = facade;
        setTitle("Local Gas Price Finder");

        //Managing the basic navigations of the GUI window. 
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        //Creating the main page and second page objects. 
        mainPage = new MainPage(this, facade);
        try{
            secondPage = new SecondPage(this, facade);
        }
        catch(IOException ioe){
            ioe.printStackTrace(System.err);
        }

        //The program opens on the main page. 
        setContentPane(mainPage);
    }

    /**
     * Showing the main page of the GUI. Called upon clicking the "back" button.
     * It clears the data from the previous search, both visually on the GUI and in the backend. 
     */
    void showMainPage() {
        facade.clearStationData();
        secondPage.clearCurrentData();

        setContentPane(mainPage);
        revalidate();
        repaint();
}
    /**
     * Showing the second page of the GUI. Called upon clicking the "Find Gas" button.
     * @throws IOException Throws IOException from FileWriter and BufferedReader. 
     * @throws InterruptedException Throws InterruptedException from Selenium Webscraping. 
     */

     void showSecondPage() throws IOException, InterruptedException{        
        facade.setStationType(mainPage.getStation());
        facade.setUserRadius(mainPage.getRadius());
        facade.scrapeStations();

        secondPage.getFormattedStationData();
        secondPage.showResults();

        setContentPane(secondPage);
        revalidate();
        repaint();
    }

    /**
     * Refers to the facade to creata a CSV File of the data upon calling. 
     * @throws IOException Throws IOException from FileWriter.
     */
    void createCSV() throws IOException{
        facade.createCSVWriter(secondPage.getCurrentStationData());
        JOptionPane.showMessageDialog(this,"The CSV File for your station data has been created.","CSV Created", JOptionPane.INFORMATION_MESSAGE);
    }

}