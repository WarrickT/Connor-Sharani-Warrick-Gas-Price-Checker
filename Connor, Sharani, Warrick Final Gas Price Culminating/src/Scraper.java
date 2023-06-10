import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Abtract class for Scraping objects. 
 * Provides the general setup for webscraping:
 */
public abstract class Scraper {
    //The following properties will be inherited by all station objects.

    //User data from input boxes: Staiton type, user address, and user radius
    protected String stationType;
    protected String userAddress;
    protected double userRadius;

    //Chrome webdriver set up
    protected ChromeOptions options = new ChromeOptions();
    protected WebDriver driver;
    
    //Distance Calculator set up
    protected DistanceCalculator distanceCalculator;
    protected ArrayList<Station> stationData;
    protected ArrayList<String> stationDatabase;

    protected ReadDatabase readDatabase;

    /**
     * Parent constructor for the scraper class. 
     * @param stationType User inputted station type
     * @param stationData ArrayList of Station objects 
     * @param userAddress User inputted address
     * @param userRadius User inputted radius
     * @throws IOException IOException thrown by the ChromeDriver
     * @throws InterruptedException InterruptedException thrown by the Chrome Driver
     */
    Scraper(String stationType, ArrayList<Station> stationData, String userAddress, double userRadius) throws IOException, InterruptedException{
        //Creating a new readDatabase object
        this.readDatabase = new ReadDatabase(stationType);

        //Setting the user's inputted data as properties of the scraper object. 
        this.userRadius = userRadius;
        this.userAddress = modifyLocation(userAddress + "%2C+ON%2C+Canada");
        this.stationData = stationData;

        this.distanceCalculator = new DistanceCalculator(userAddress + ", ON, Canada");

        //Prevent chrome window from opening during scraping. 
        options.addArguments("--headless=new");
        this.driver = new ChromeDriver(options);
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\warri\\OneDrive\\Desktop\\Connor, Sharani, Warrick GasPrice\\demo\\src\\drivers\\chromedriver.exe");

        //Uses the readDatabase and distanceCalculator objects to retrieve data regarding th euser inputted 
        //station type and address. 
        setStationDatabase(readDatabase.getGasStationDatabase());
        scrapeUserCoordinates();
    }

    /**
     * Modifies the user's address to allow for google searching by link
     * @param userAddress User's inputted address
     * @return The address free of spaces and commas. 
     */
    String modifyLocation(String userAddress){
        userAddress = userAddress.replace(" ", "+").replace(",", "%2C");
        return userAddress;
    }

    /**
     * Refers the stationDatabase variable to the stationDatabase object created earlier. 
     * @param stationDatabase The database of the gas station.
     */
    void setStationDatabase(ArrayList<String> stationDatabase){
        this.stationDatabase = stationDatabase;
    }

    /**
     * Scrape the user's lat-long coordinates and sets them in the distanceCalculator object. 
     * @throws InterruptedException
     */
    void scrapeUserCoordinates() throws InterruptedException{
        distanceCalculator.scrapeUserCoordinates();
    }

    /**
     * Converts the station data into a form readable by the GUI
     * @return The stationData in the form of a 2D String Array. 
     */
    String[][] getFormattedData(){
        String[][] formattedStationData = new String[stationData.size()][5];

        for(int stationNum = 0; stationNum < stationData.size(); stationNum ++){
            formattedStationData[stationNum][0] = stationData.get(stationNum).getStationName();
            formattedStationData[stationNum][1] = stationData.get(stationNum).getAddress();
            formattedStationData[stationNum][2] = stationData.get(stationNum).getUnitCost() + " ¢";
            formattedStationData[stationNum][3] = stationData.get(stationNum).getDisplacement() + " km";
            formattedStationData[stationNum][4] = stationData.get(stationNum).getGoogleMapsDirections();
        }

        return formattedStationData;
    }
}
