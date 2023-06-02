import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class CreateDataBase {
    private WebDriver databaseDriver;

    private String newDatabaseLink = "https://gps-coordinates.org/coordinate-converter.php";
    private String newInput = "/html/body/table[4]/tbody/tr/td[1]/input";
    private String newFind = "/html/body/table[4]/tbody/tr/td[1]/button[1]";
    private String newLatBox = "/html/body/table[4]/tbody/tr/td[1]/table[1]/tbody/tr[1]/td[2]/input";
    private String newLongBox = "/html/body/table[4]/tbody/tr/td[1]/table[1]/tbody/tr[2]/td[2]/input";

    private BufferedReader br;

    void scrapeTeslaData() throws IOException, InterruptedException{
        this.databaseDriver = new ChromeDriver(); 
        databaseDriver.get(newDatabaseLink);
        try{
            br = new BufferedReader(new FileReader("database" + "/" + "teslaDatabase.txt"));
            String contentLine = br.readLine();

            //Prevents the bug of reading a null content line.
	        while (contentLine != null) {
                if(contentLine == null){
                    continue;
                }
                if(!Character.isDigit(contentLine.charAt(0))){
                    System.out.println(contentLine);
                    contentLine = br.readLine();
                    continue;
                }
                System.out.println(contentLine);
                WebElement inputBox = databaseDriver.findElement(By.xpath(newInput));
                inputBox.clear();
                inputBox.sendKeys(contentLine);

                WebElement findButton = databaseDriver.findElement(By.xpath(newFind));
                findButton.click();
                Thread.sleep(2000);

                Double newLat = Double.parseDouble(databaseDriver.findElement(By.xpath(newLatBox)).getAttribute("value"));
                Double newLong = Double.parseDouble(databaseDriver.findElement(By.xpath(newLongBox)).getAttribute("value"));

                System.out.println(newLat);
                System.out.println(newLong);
                contentLine = br.readLine();
        }
    }
        catch(IOException ioe){
            System.out.println("Cannot find file");
	        ioe.printStackTrace();
        }
        finally{
            try {
	            if (br != null){
                    br.close();
            }
	   } 
	        catch (IOException ioe) 
                {
		        System.out.println("Error in closing the BufferedReader");
	   }
        }

    }
    String clearCommas(String string){
        return "\"" + string + "\"";
    }
}
