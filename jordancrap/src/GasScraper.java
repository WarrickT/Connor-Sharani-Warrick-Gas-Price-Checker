import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class GasScraper extends Scraper{
    String siteQuery = "body > #root > div.default__theme___1urxG > div.Page-module__content___196kn.Page-module__padded___3hQ0U > div.container__container___2Wq7c.container__padded___2u4nK.container__snap___1RrVv > div.grid__grid___1bWao.grid__gridContainer___nyUt1 > div.grid__column___nhz7X.grid__tablet7___WBfn5.grid__desktop8___38Y4U > div > div.panel__panel___3Q2zW.panel__white___19KTz.colors__bgWhite___1stjL.panel__bordered___1Xe-S.panel__rounded___2etNE.GenericStationListItem-module__station___1O4vF > div.GenericStationListItem-module__stationListItem___3Jmn4";
    String stationNameQuery = "div.StationDisplay-module__mainInfoColumn___1ZBwz.StationDisplay-module__column___3h4Wf > h3.header__header3___1b1oq.header__header___1zII0.header__midnight___1tdCQ.header__snug___lRSNK.StationDisplay-module__stationNameHeader___1A2q8";
    String addressQuery = "div.StationDisplay-module__mainInfoColumn___1ZBwz.StationDisplay-module__column___3h4Wf > div.StationDisplay-module__address___2_c7v";
    String unitCostQuery = "div.Belt__mainContainer___1tzUC.GenericStationListItem-module__beltContainer___3s8Px > div.Belt__childContainer___PJhvM > div.GenericStationListItem-module__priceCard___27wng.GenericStationListItem-module__card___15qon.GenericStationListItem-module__clickable___30MZX > div.StationDisplayPrice-module__priceContainer___J6Ibm.StationDisplayPrice-module__asCard___1kypj > div.StationDisplayPrice-module__borderContainer___FfIQJ.StationDisplayPrice-module__bordered___ExChJ > span.text__xl___2MXGo.text__left___1iOw3.StationDisplayPrice-module__price___3rARL";
    String loadMoreButtonQuery = "body > #root > div.default__theme___1urxG > div.Page-module__content___196kn.Page-module__padded___3hQ0U > div.container__container___2Wq7c.container__padded___2u4nK.container__snap___1RrVv > div.grid__grid___1bWao.grid__gridContainer___nyUt1 > div.grid__column___nhz7X.grid__tablet7___WBfn5.grid__desktop8___38Y4U > div > a.button__button___fo2tk.forms__field___E4Q71.forms__formControlBase___3Cl7I.button__fluid___2ez5a.button__secondary___1xuZs.button__branded___3hDeX";
    String acceptCookiesQuery = "body > div.cc-window.cc-banner.cc-type-info.cc-theme-block.cc-bottom.cc-color-override-530831885 > div.cc-compliance > a.cc-btn.cc-dismiss";

    GasScraper(String stationType, ArrayList<Station> stationData, String userAddress, double userRadius) throws IOException, InterruptedException{
        super(stationType, stationData, userAddress, userRadius);
        
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

    void scrapeGasDiesel() throws IOException, InterruptedException{
        String searchLink = "https://www.gasbuddy.com/home?search=" + userAddress + "&fuel=" + stationType + "&method=all&maxAge=48";
        System.out.println(searchLink);

        driver.get(searchLink);

        //Add try catch
        WebElement loadMoreButton = driver.findElement(By.cssSelector(loadMoreButtonQuery));
        WebElement acceptCookiesButton = driver.findElement(By.cssSelector(acceptCookiesQuery));

        int gasStationCount = 0;

        acceptCookiesButton.click();
        Thread.sleep(1000);

        outerloop:
        while(true){
            List<WebElement> stationList = driver.findElements(By.cssSelector(siteQuery)).subList(gasStationCount, driver.findElements(By.cssSelector(siteQuery)).size());

            innerloop:
            for(WebElement currentStation:stationList){
                gasStationCount ++;
                
                String currentName = "";
                String currentStationAddress = "";
                double currentUnitCost = 0.0;
                double currentDisplacement;
    
                try{
                    WebElement addressElement = currentStation.findElement(By.cssSelector(addressQuery));
                    currentStationAddress = addressElement.getText().replace("\n", ", ") + ", Canada";

                    if(!Character.isDigit(currentStationAddress.charAt(0))){
                        continue innerloop;
                    }
                    if(containsInDatabase(currentStationAddress)){
                        System.out.println(currentStationAddress);
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

                try{
                    WebElement nameElement = currentStation.findElement(By.cssSelector(stationNameQuery + " > a"));
                    currentName = nameElement.getText();
                }
                catch(org.openqa.selenium.NoSuchElementException exception){
                    WebElement nameElement = currentStation.findElement(By.cssSelector(stationNameQuery + " > span > a"));
                    currentName = nameElement.getText();
                }
                finally{
                    if(currentName.length() == 0){
                        continue innerloop;
                    }
                }
    
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

                Station currentStationObject = new Station(currentName, currentStationAddress, currentUnitCost);
                currentStationObject.setGoogleMapsDirections("https://www.google.com/maps/dir/?api=1&origin=" + userAddress + "&destination=" + modifyLocation(currentStationAddress.replace("9th", "Ninth").replace("16th", "Sixteenth").replace("Ln", "Line")));
                currentStationObject.setDisplacement(currentDisplacement);
                stationData.add(currentStationObject);
            }

            try{
                Thread.sleep(1000);
                loadMoreButton.click();
                System.out.println("Button clicked!");
                Thread.sleep(2000);
            }
            catch(org.openqa.selenium.NoSuchElementException exception){
                break outerloop;
            }
            catch(org.openqa.selenium.StaleElementReferenceException exception2){
                break outerloop;
            }
            
        }

        driver.close();
    }
    

    boolean containsInDatabase(String stationAddress){
        for(String element:stationDatabase){
            if(element.equals(stationAddress)){
                return true;
            }
        }
        return false;
    }

}
