import java.io.IOException;
import java.util.ArrayList;

public class Facade {
    String stationType;
    String userAddress;
    double userRadius;
    GasScraper gasScraper;
    TeslaScraper teslaScraper;
    AddressValidation addressValidation;

    ArrayList<Station> stationData = new ArrayList<Station>();
    ReadDatabase readDatabase = new ReadDatabase();

    void scrapeTesla() throws IOException, InterruptedException{
        //teslaScraper.scrapeTesla();
        //teslaScraper.printStationData();
    }

    void scrapeStations() throws IOException, InterruptedException{
        if(stationType.equals("Regular") || stationType.equals("Midgrade") || stationType.equals("Premium") || stationType.equals("Diesel")){
            gasScraper = new GasScraper(stationType, stationData, userAddress);
            System.out.println(stationType);
            gasScraper.setUserRadius(userRadius);
            gasScraper.scrapeUserCoordinates();
            gasScraper.setStationDatabase(readDatabase.getGasStationDatabase());
            gasScraper.scrapeGasDiesel();
            gasScraper.sortByPrice();
            gasScraper.printStationData();
            gasScraper.getFormattedData();
        }
        else{
            teslaScraper = new TeslaScraper(stationType, stationData, userAddress);
        }

    }
    void setStationType(String stationType){
        this.stationType = stationType;
    }
    void setUserAddress(String userAddress){
        this.userAddress = userAddress;
    }
    void setUserRadius(double userRadius){
        this.userRadius = userRadius;
    }
    String[][] getFormattedStationData(){
        return gasScraper.getFormattedData();
    }
    void clearStationData(){
        stationData.clear();
    }

}
