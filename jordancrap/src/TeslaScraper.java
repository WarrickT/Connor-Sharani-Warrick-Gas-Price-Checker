import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class TeslaScraper extends Scraper{
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
