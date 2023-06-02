import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class AddressValidation{
    private String userAddress = "";
    private String searchDirectory = "#rso > div.ULSxyf > div > div > div > div > div:nth-child(2) > div > div:nth-child(2) > b > div > div > span";

    void setAddress(String userAddress){
        this.userAddress = userAddress;
    }

    String promptAddress(){
        Scanner reader = new Scanner(System.in);

        while(validateAddress() == false){
            System.out.println("Enter a valid address: ");
            this.userAddress = reader.nextLine();
        }

        return userAddress;
    }

    boolean validateAddress(){
        String searchLink = "https://www.google.com/search?q=" + userAddress + "+google+maps";
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        WebDriver validateAddressDriver = new ChromeDriver(options);
        validateAddressDriver.get(searchLink);
        
        try{
            System.out.println(searchLink);
            WebElement validAddressElement = validateAddressDriver.findElement(By.cssSelector(searchDirectory));
            if(validAddressElement.getText().contains(", ON")){
                System.out.println("Indeedly in Ontario. ");
                return true;
            }
            else{
                System.out.println("Not in Ontario.");
                return false;

            }
        }
        catch(org.openqa.selenium.NoSuchElementException exception){
            System.out.println("No address found for this input.");
            return false;
        }
        finally{
            validateAddressDriver.close();
        }
    }
}
