import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

    /**
     * Webscrapes a lat-long coordinate from the user's inputted address using Selenium, 
     * Then calculates the direct displacement using the Math Packages.
     */
public class DistanceCalculator {
    //User's address and lat-long coordinates. 
    private String userAddress;
    private double userLat;
    private double userLong;

    //Instantiate the chromedriver used for webscraping lat-long coordinates. 
    private WebDriver distanceCalculatorDriver;

    //Links and directories for webscraping the lat-long coordinates
    private String searchLink = "https://gps-coordinates.org/coordinate-converter.php";
    private String inputQuery = "/html/body/table[4]/tbody/tr/td[1]/input";
    private String findButtonQuery = "/html/body/table[4]/tbody/tr/td[1]/button[1]";
    private String latBox = "/html/body/table[4]/tbody/tr/td[1]/table[1]/tbody/tr[1]/td[2]/input";
    private String longBox = "/html/body/table[4]/tbody/tr/td[1]/table[1]/tbody/tr[2]/td[2]/input";

    private ChromeOptions options = new ChromeOptions();

    /**
     * Constructor method for distanceCalculator.
     * @param userAddress
     */
    DistanceCalculator(String userAddress){
        this.userAddress = userAddress;
    }
    
    /**
     * Webscrape lat-long coordinates of the user's entered location using Selenium. 
     * @throws InterruptedException Throws interruted exception from selenium webscraping.
     */
    void scrapeUserCoordinates() throws InterruptedException{
        //Prevent the web browser from opening when webscraping.
        //options.addArguments("--headless=new");
        this.distanceCalculatorDriver = new ChromeDriver(options);

        //Set up the chromedriver object using the link.
        distanceCalculatorDriver.get(searchLink);

        //Access the input box for inputting the user's address, then retrieving the lat-long coordinates
        WebElement inputBox = distanceCalculatorDriver.findElement(By.xpath(inputQuery));
        inputBox.clear();
        inputBox.sendKeys(userAddress);

        //Access the "find" button to find the user address
        WebElement findButton = distanceCalculatorDriver.findElement(By.xpath(findButtonQuery));
        findButton.click();
        Thread.sleep(2000);

        //Store the values from the lat-long input boxes
        userLat = Double.parseDouble(distanceCalculatorDriver.findElement(By.xpath(latBox)).getAttribute("value"));
        userLong = Double.parseDouble(distanceCalculatorDriver.findElement(By.xpath(longBox)).getAttribute("value"));

        //Closing the chromedriver
        distanceCalculatorDriver.close();
    }

    /**
     *  Calculating the displacement between the user's location and the corresponding gas station location. 
     * @param stationLat Latitude of the station
     * @param stationLong Longitude of the staiton
     * @return Distance between the user's location and the station.
     */
    double calculateDistance(double stationLat, double stationLong) {
        final int earthRadius = 6371; // Radius of the earth

        double latDistance = Math.toRadians(stationLat - userLat);
        double lonDistance = Math.toRadians(stationLong - userLong);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(stationLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c * 1000; // convert to meters
        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
}
}
