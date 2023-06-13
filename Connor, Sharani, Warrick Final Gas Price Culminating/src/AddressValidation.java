import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Performs a google search using Selenium to ensure the user enters a valid address. 
 */
public class AddressValidation{
    private String userAddress;
    //CSS Selector: This selector exists if one google searchs a valid address, as a google maps results will appear. 
     
    private String searchDirectory = "#rso > div.ULSxyf > div > div > div > div > div:nth-child(2) > div > div:nth-child(2) > b > div > div > span";

    /**
     * AddressValidation Constructor Method
     * Reformats the user address to allow for google seraching
     * @param userAddress The user's address
     */
    AddressValidation(String userAddress){
        this.userAddress = userAddress.replace(" ", "+").replace(",", "%2C");
    }

    /**
     * Webscrapes the Google search, using the original google search link and the user's address.
     * @return Whether the address is indeed found on Google, and is in Ontario.
     */
    boolean validateAddress(){
        //Constructing the search link for webscraping
        String searchLink = "https://www.google.com/search?q=" + userAddress + "+google+maps";

        //Headless mode to prevent showing the search window while webscraping
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");

        //Setting up the Selenium webdriver required to webscrape. 
        WebDriver validateAddressDriver = new ChromeDriver(options);
        validateAddressDriver.get(searchLink);
        
        try{
            //This element does not exist if there is the entered address is invalid, hence the try & catch method.
            WebElement validAddressElement = validateAddressDriver.findElement(By.cssSelector(searchDirectory));
            if(validAddressElement.getText().contains(", ON")){
                return true;
            }
            else{
                return false;

            }
        }
        catch(org.openqa.selenium.NoSuchElementException exception){
            //Catches the NoSuchElementException thrown by Selenium, if the validAddressElement does not exist. 
            return false;
        }
        finally{
            //Closing the webDriver used for webscraping
            validateAddressDriver.close();
        }
    }
}
