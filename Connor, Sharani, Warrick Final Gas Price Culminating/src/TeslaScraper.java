import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Reads the Tesla database and stores the data accordingly.
 */
public class TeslaScraper extends Scraper{
    /**
     * Constructor for the TeslaScraper object; Only calls its parent constructor. 
     * @param stationType The user inputted station type. 
     * @param stationData The ArrayList<Station> of station data. 
     * @param userAddress The address of the user
     * @param userRadius The radius entered by the user
     * @throws IOException IOException thrown by BufferedReader
     * @throws InterruptedException InterruptedException thrown by scraping the user's lat long coordinates. 
     */
    TeslaScraper(String stationType, ArrayList<Station> stationData, String userAddress, double userRadius) throws IOException, InterruptedException{
        super(stationType, stationData, userAddress, userRadius);
    }

    void scrapeTesla() throws IOException{
        for(String line:stationDatabase){
            if(Character.isDigit(line.charAt(0)) && line.contains(", ON")){
                double stationLat = Double.parseDouble(stationDatabase.get(stationDatabase.indexOf(line) + 2));
                double stationLong = Double.parseDouble(stationDatabase.get(stationDatabase.indexOf(line) + 3));
        
                double currentDisplacement = distanceCalculator.calculateDistance(stationLat, stationLong)/1000.0;
                currentDisplacement = Double.parseDouble((new DecimalFormat("##.##")).format(currentDisplacement));

                if(currentDisplacement > userRadius){
                    continue;
                }
                String currentStationAddress = line;
                String currentStationName = stationDatabase.get(stationDatabase.indexOf(line) + 1);

                Station currentStation = new Station(currentStationName, currentStationAddress, 0.0);
                currentStation.setGoogleMapsDirections("https://www.google.com/maps/dir/?api=1&origin=" + userAddress + "&destination=" + modifyLocation(currentStationAddress.replace("9th", "Ninth").replace("16th", "Sixteenth").replace("Ln", "Line")));
                currentStation.setDisplacement(currentDisplacement);
                stationData.add(currentStation);
            }
            else{
                continue;
            }
        }

    }
    
}
