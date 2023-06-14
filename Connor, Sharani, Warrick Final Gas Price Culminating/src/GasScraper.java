import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * This object specifically uses Selenium to scrape the gas station data on gGasBuddy.ocm
 * Bypasses barriers like accept cookies button by clicking them,
 * and clicks the button to load more stations.
 */

public class GasScraper extends Scraper{
    //The query links for the buttons on GasBuddy.com
    String siteQuery = "body > #root > div.default__theme___1urxG > div.Page-module__content___196kn.Page-module__padded___3hQ0U > div.container__container___2Wq7c.container__padded___2u4nK.container__snap___1RrVv > div.grid__grid___1bWao.grid__gridContainer___nyUt1 > div.grid__column___nhz7X.grid__tablet7___WBfn5.grid__desktop8___38Y4U > div > div.panel__panel___3Q2zW.panel__white___19KTz.colors__bgWhite___1stjL.panel__bordered___1Xe-S.panel__rounded___2etNE.GenericStationListItem-module__station___1O4vF > div.GenericStationListItem-module__stationListItem___3Jmn4";
    String stationNameQuery = "div.StationDisplay-module__mainInfoColumn___1ZBwz.StationDisplay-module__column___3h4Wf > h3.header__header3___1b1oq.header__header___1zII0.header__midnight___1tdCQ.header__snug___lRSNK.StationDisplay-module__stationNameHeader___1A2q8";
    String addressQuery = "div.StationDisplay-module__mainInfoColumn___1ZBwz.StationDisplay-module__column___3h4Wf > div.StationDisplay-module__address___2_c7v";
    String unitCostQuery = "div.Belt__mainContainer___1tzUC.GenericStationListItem-module__beltContainer___3s8Px > div.Belt__childContainer___PJhvM > div.GenericStationListItem-module__priceCard___27wng.GenericStationListItem-module__card___15qon.GenericStationListItem-module__clickable___30MZX > div.StationDisplayPrice-module__priceContainer___J6Ibm.StationDisplayPrice-module__asCard___1kypj > div.StationDisplayPrice-module__borderContainer___FfIQJ.StationDisplayPrice-module__bordered___ExChJ > span.text__xl___2MXGo.text__left___1iOw3.StationDisplayPrice-module__price___3rARL";
    String loadMoreButtonQuery = "body > #root > div.default__theme___1urxG > div.Page-module__content___196kn.Page-module__padded___3hQ0U > div.container__container___2Wq7c.container__padded___2u4nK.container__snap___1RrVv > div.grid__grid___1bWao.grid__gridContainer___nyUt1 > div.grid__column___nhz7X.grid__tablet7___WBfn5.grid__desktop8___38Y4U > div > a.button__button___fo2tk.forms__field___E4Q71.forms__formControlBase___3Cl7I.button__fluid___2ez5a.button__secondary___1xuZs.button__branded___3hDeX";
    String acceptCookiesQuery = "body > div.cc-window.cc-banner.cc-type-info.cc-theme-block.cc-bottom.cc-color-override-530831885 > div.cc-compliance > a.cc-btn.cc-dismiss";

    //Creating the buttons for webscraping. 
    private WebElement acceptCookiesButton;
    private WebElement loadMoreButton;

    /**
     * Constructor for the Gas Scraper object.
     * @param stationType User's entered station type.
     * @param stationData User's entered station data.
     * @param userAddress User's entered address.
     * @param userRadius User's entered radius.
     * @throws IOException Throws IOException from FileWriter and BufferedReader
     * @throws InterruptedException Throws InterruptedException from Selenium Webscraping. 
     */
    GasScraper(String stationType, ArrayList<Station> stationData, String userAddress, double userRadius) throws IOException, InterruptedException{
        //Calling the constructor of its parent class, Scraper
        super(stationType, stationData, userAddress, userRadius);
        
        //The number in the gasBuddy search link dictates the station type it searches.
        if(stationType.equals("Regular")){
            this.stationType = "1";
        }
        else if(stationType.equals("Midgrade")){
            this.stationType = "2";
        }
        else if(stationType.equals("Premium")){
            this.stationType = "3";
        }
        else if(stationType.equals("Diesel")){
            this.stationType = "4";
        }
    }

    /**
     * Scraping gas and diesel stations. 
     * @throws IOException Throws IOException from FileWriter and BufferedReader
     * @throws InterruptedException Throws InterruptedException from Selenium Webscraping. 
     */
    void scrapeGasDiesel() throws IOException, InterruptedException{
        //Creating the search link
        String searchLink = "https://www.gasbuddy.com/home?search=" + userAddress + "&fuel=" + stationType + "&method=all&maxAge=48";

        //Setting up the chromedriver for webscraping. 
        driver.get(searchLink);

        //Ensures the scraper does not scrape the entire site again by tracking the amount of stations scraped. 
        int gasStationCount = 0;

        //Accepts cookies to proceed to the site and scrape data
        acceptCookiesButton = driver.findElement(By.cssSelector(acceptCookiesQuery));
        acceptCookiesButton.click();
        Thread.sleep(1000);

        outerloop:
        while(true){
            //The load button may not always exist, so the error will be catched. 
            try{
                loadMoreButton = driver.findElement(By.cssSelector(loadMoreButtonQuery));
            }
            catch(org.openqa.selenium.NoSuchElementException exception){
            }

            //Stores the web elements of each station. 
            List<WebElement> stationList = driver.findElements(By.cssSelector(siteQuery)).subList(gasStationCount, driver.findElements(By.cssSelector(siteQuery)).size());

            //Iterates through each station in the list of webelements and scrapes them. 
            innerloop:
            for(WebElement currentStation:stationList){
                gasStationCount ++;
                
                //Properties stored in Station objects. 
                String currentName = "";
                String currentStationAddress = "";
                double currentUnitCost = 0.0;
                double currentDisplacement;
    
                //A try catch is needed for every proeprty since the station web element may not have them. 
                //For example, if a unit cost isn't provided, the html path for that element will not exist. 

                try{
                    WebElement addressElement = currentStation.findElement(By.cssSelector(addressQuery));
                    //Format the address properly
                    currentStationAddress = addressElement.getText().replace("\n", ", ") + ", Canada";

                    //If an improper address (i.e. only a street name) is provided, skip the element. 
                    if(!Character.isDigit(currentStationAddress.charAt(0))){
                        continue innerloop;
                    }

                    //Searches if the station is contained the database containing most stations in Ontario and their corresponding lat-long coordinates. 
                    //Skip the element if the station isn't found in the database. 
                    if(containsInDatabase(currentStationAddress)){
                        //Uses the distanceCalculator to calculate the displacement between the user location and the gas station
                        double stationLat = Double.parseDouble(stationDatabase.get(stationDatabase.indexOf(currentStationAddress) + 1));
                        double stationLong = Double.parseDouble(stationDatabase.get(stationDatabase.indexOf(currentStationAddress) + 2));
                        currentDisplacement = distanceCalculator.calculateDistance(stationLat, stationLong)/1000.0;

                        currentDisplacement = Double.parseDouble((new DecimalFormat("##.##")).format(currentDisplacement));
                    }
                    else{
                        continue innerloop;
                    }
                }
                catch(org.openqa.selenium.NoSuchElementException exception){
                    continue innerloop;
                }

                //GasBuddy.com is sorted by displacement. If the current station's radius is beyond the 
                //user's entered range, the scraping will stop. 


                //Retreving the station name. 
                try{
                    WebElement nameElement = currentStation.findElement(By.cssSelector(stationNameQuery + " > a"));
                    currentName = nameElement.getText();
                }
                //For gas stations with two names (e.g. Shell & Circle K), another directory is used in the site. 
                catch(org.openqa.selenium.NoSuchElementException exception){
                    WebElement nameElement = currentStation.findElement(By.cssSelector(stationNameQuery + " > span > a"));
                    currentName = nameElement.getText();
                }
                //If a stationName isn't provided, the station will be skipped. (Does not happen most of the time)
                finally{
                    if(currentName.length() == 0){
                        continue innerloop;
                    }
                }
    
                //Retrieving the station's gas price
                try{
                    WebElement costElement = currentStation.findElement(By.cssSelector(unitCostQuery));
                    currentUnitCost = Double.parseDouble(costElement.getText().substring(0, 5));
                }
                catch(org.openqa.selenium.NoSuchElementException exception){
                    continue innerloop;
                }

                if(currentDisplacement > userRadius){
                    break outerloop;
                    }

                System.out.println(currentName);
                //Creates the station object if all the properties of the station has been validated, and adds them to the stationData ArrayList.
                Station currentStationObject = new Station(currentName, currentStationAddress, currentUnitCost);
                currentStationObject.setGoogleMapsDirections("https://www.google.com/maps/dir/?api=1&origin=" + userAddress + "&destination=" + modifyLocation(currentStationAddress.replace("9th", "Ninth").replace("16th", "Sixteenth").replace("Ln", "Line")));
                currentStationObject.setDisplacement(currentDisplacement);
                stationData.add(currentStationObject);
            }

            //Trying clicking the load more button after scraping the current page
            try{
                Thread.sleep(1000);
                loadMoreButton.click();
                Thread.sleep(2000);
            }
            //Two errors may be thrown if the load more button does not exist for that page. 
            catch(org.openqa.selenium.NoSuchElementException NSE){
                break outerloop;
            }
            catch(org.openqa.selenium.StaleElementReferenceException SER){
                break outerloop;
            }
            
        }

        //Closing the chromedriver used for scraping. 
        driver.close();
    }
    

    /**
     * Checks if the station address is in the database so a lat-long coordinate can be retrieved, and the displacement can be found. 
     * Each line of the database is also not neccessarily the station address
     * @param stationAddress
     * @return  Whether or not the scraped gas station is in the database. 
     */
    boolean containsInDatabase(String stationAddress){
        for(String element:stationDatabase){
            if(element.equals(stationAddress)){
                return true;
            }
        }
        return false;
    }

}
