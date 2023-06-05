import java.io.IOException;
import java.util.ArrayList;

public class BackendController {
    String stationType;
    String userAddress;
    double userRadius;

    GasScraper gasScraper;
    TeslaScraper teslaScraper;

    AddressValidation addressValidation;
    ArrayList<Station> stationData = new ArrayList<Station>();
    CSVWriter csvWriter;

    void scrapeStations() throws IOException, InterruptedException{
        if(stationType.equals("Tesla Supercharge")){
            teslaScraper = new TeslaScraper(stationType, stationData, userAddress, userRadius);
            teslaScraper.scrapeTesla();
        }
        else{
            gasScraper = new GasScraper(stationType, stationData, userAddress, userRadius);
            gasScraper.scrapeGasDiesel();
        }

    }
    void setStationType(String stationType){
        this.stationType = stationType;
    }
    boolean setUserAddress(String userAddress){
        this.addressValidation = new AddressValidation(userAddress);

        if(addressValidation.validateAddress()){
            this.userAddress = userAddress;
            return true;
        }
        return false;
    }
    void setUserRadius(double userRadius){
        this.userRadius = userRadius;
    }

    String[][] getFormattedStationData(){
        if(stationType.equals("Tesla Supercharge")){
            return teslaScraper.getFormattedData();
        }
        return gasScraper.getFormattedData();

    }
    void clearStationData(){
        stationData.clear();
    }
    void createCSVWriter(String[][] formattedStationData) throws IOException{
        System.out.println(userAddress);
        csvWriter = new CSVWriter(formattedStationData, stationType, userAddress);
    }
}
