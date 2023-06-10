import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/**
 * We chose the Facade design pattern. 
 * This class dictates the interactions between the GUI and Backend, while concealing all the complicatedlogic
 * in the objects created.
 */
public class Facade {
    /**
     * The GUI Window, and the 
     * User-inputted information from the GUI
     */
    Frame guiFrame;
    String stationType;
    String userAddress;
    double userRadius;

    /**
     * Objects used for scraping Gas Station or Tesla Supercharger data.
     */
    GasScraper gasScraper;
    TeslaScraper teslaScraper;

    /**
     * Used for validating the user's address
     */
    AddressValidation addressValidation;
    CSVWriter csvWriter;

    ArrayList<Station> stationData = new ArrayList<Station>();


    /**
     * Facade Constructor; Creates the GUI Frame object and boots it up.
     * @throws IOException
     * @throws InterruptedException
     */
    Facade() throws IOException, InterruptedException{
        guiFrame = new Frame(this);
        SwingUtilities.invokeLater(() -> guiFrame.setVisible(true)); 
    }

    /**
     * Begins scraping station data using the gasScraper or teslaScraper object.
     * @throws IOException 
     * @throws InterruptedException
     */
    void scrapeStations() throws IOException, InterruptedException{
        if(stationType.equals("Tesla Supercharge")){
            teslaScraper = new TeslaScraper(stationType, stationData, userAddress, userRadius);
            teslaScraper.scrapeTesla();
        }
        else{
            gasScraper = new GasScraper(stationType, stationData, userAddress, userRadius);
            gasScraper.scrapeGasDiesel();
        }
    }

    /**
     * Sets the user's station type, and the scraper finds the data based on it
     * @param stationType The station type entered by the user through the dropdown box. 
     */
    void setStationType(String stationType){
        this.stationType = stationType;
    }

    /**
     * Validates the user's entered address using the addressValidation object. 
     * @param userAddress The address entered by the user
     * @return Whether or not the address entered is valid and in Ontario. The program will not proceed until this method returns true.
     * It will proceeed by setting the userAddress accordingly.
     */
    boolean setUserAddress(String userAddress){
        this.addressValidation = new AddressValidation(userAddress);

        if(addressValidation.validateAddress()){
            this.userAddress = userAddress;
            return true;
        }
        return false;
    }

    /**
     * Sets the user's entered radius, and the scraper finds the data based on it. 
     * @param userRadius
     */
    void setUserRadius(double userRadius){
        this.userRadius = userRadius;
    }

    /**
     * Clears all backend data when the user returns to the main page from the second page. 
     */
    void clearStationData(){
        stationData.clear();
    }

    /**
     * Retrieves the station data in the form of a 2D String Array to allow for table display.
     * @return The station data as a 2D String Array, which will be sent to the SecondPage object.
     */
    String[][] getFormattedStationData(){
        if(stationType.equals("Tesla Supercharge")){
            return teslaScraper.getFormattedData();
        }
        return gasScraper.getFormattedData();

    }

    /**
     * Creates the CSV Object to write the CSV
     * @param formattedStationData The statino data in the form of a 2D String Array
     * @throws IOException
     */
    void createCSVWriter(String[][] formattedStationData) throws IOException{
        csvWriter = new CSVWriter(formattedStationData, stationType, userAddress);
    }
}
