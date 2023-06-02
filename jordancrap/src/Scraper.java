import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class Scraper {
    protected String stationType;
    protected String userAddress;

    protected double userRadius;

    protected ChromeOptions options = new ChromeOptions();
    protected WebDriver driver;
    
    protected DistanceCalculator distanceCalculator;
    protected ArrayList<Station> stationData;
    protected ArrayList<String> stationDatabase;

    String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0";

    Scraper(String stationType, ArrayList<Station> stationData, String userAddress){
        this.distanceCalculator = new DistanceCalculator(userAddress + ", ON, Canada");
        System.out.println(distanceCalculator.userAddress);
        this.userAddress = modifyLocation(userAddress + "%2C+ON%2C+Canada");
        this.stationData = stationData;

        options.addArguments("--headless=new");
        this.driver = new ChromeDriver(options);
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\warri\\OneDrive\\Desktop\\Connor, Sharani, Warrick GasPrice\\demo\\src\\drivers\\chromedriver.exe");
    }

    String modifyLocation(String userAddress){
        userAddress = userAddress.replace(" ", "+");
        userAddress = userAddress.replace(",", "%2C");
        return userAddress;
    }

    String getLocation() throws IOException{
        return userAddress;
    }
    void setUserRadius(double userRadius){
        this.userRadius = userRadius;
    }

    void printStationData(){
        for(int stationNum = 0; stationNum < stationData.size(); stationNum ++){
            System.out.println("Station number: " + stationNum);
            Station currentStation = stationData.get(stationNum);
            System.out.println(currentStation.getStationName());
            System.out.println(currentStation.getAddress());
            System.out.println(currentStation.getUnitCost());
            System.out.println(currentStation.getGoogleMapsDirections());
            System.out.println(currentStation.getDisplacement() + "km");
        }
        
    }    

    void setStationDatabase(ArrayList<String> stationDatabase){
        this.stationDatabase = stationDatabase;
    }

    void scrapeUserCoordinates() throws InterruptedException{
        distanceCalculator.scrapeUserCoordinates();
    }
    //Required to make a table of data on the GUI
    String[][] getFormattedData(){
        String[][] formattedStationData = new String[stationData.size()][5];

        for(int stationNum = 0; stationNum < stationData.size(); stationNum ++){
            formattedStationData[stationNum][0] = stationData.get(stationNum).getStationName();
            formattedStationData[stationNum][1] = stationData.get(stationNum).getAddress();
            formattedStationData[stationNum][2] = stationData.get(stationNum).getUnitCost() + " ¢";
            formattedStationData[stationNum][3] = stationData.get(stationNum).getDisplacement() + " km";
            formattedStationData[stationNum][4] = stationData.get(stationNum).getGoogleMapsDirections();
        }

        for(String[] stationNum:formattedStationData){
            for(String information:stationNum){
                System.out.println(information);
            }
            System.out.println();
        }

        return formattedStationData;
    }
}
