import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DistanceCalculator {
    public String userAddress;
    private double userLat;
    private double userLong;

    private WebDriver distanceCalculatorDriver;

    private String newLink = "https://gps-coordinates.org/coordinate-converter.php";
    private String newInput = "/html/body/table[4]/tbody/tr/td[1]/input";
    private String newFind = "/html/body/table[4]/tbody/tr/td[1]/button[1]";
    private String newLatBox = "/html/body/table[4]/tbody/tr/td[1]/table[1]/tbody/tr[1]/td[2]/input";
    private String newLongBox = "/html/body/table[4]/tbody/tr/td[1]/table[1]/tbody/tr[2]/td[2]/input";

    private ChromeOptions options = new ChromeOptions();

    DistanceCalculator(String userAddress){
        this.userAddress = userAddress;
    }

    void scrapeUserCoordinates() throws InterruptedException{
        options.addArguments("--headless=new");
        this.distanceCalculatorDriver = new ChromeDriver(options);

        distanceCalculatorDriver.get(newLink);
        WebElement inputBox = distanceCalculatorDriver.findElement(By.xpath(newInput));
        inputBox.clear();
        inputBox.sendKeys(userAddress);

        WebElement findButton = distanceCalculatorDriver.findElement(By.xpath(newFind));
        findButton.click();
        Thread.sleep(2000);

        userLat = Double.parseDouble(distanceCalculatorDriver.findElement(By.xpath(newLatBox)).getAttribute("value"));
        userLong = Double.parseDouble(distanceCalculatorDriver.findElement(By.xpath(newLongBox)).getAttribute("value"));

        System.out.println(userLat);
        System.out.println(userLong);
        distanceCalculatorDriver.close();
    }

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
