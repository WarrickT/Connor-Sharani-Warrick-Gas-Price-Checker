import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class CSVWriter {
    private ArrayList<Station> stationData;
    private String stationType;    
    private String userAddress;
    File newCSVFile;

    CSVWriter(ArrayList<Station> stationData, String stationType, String userAddress){
        this.stationData = stationData;
        this.stationType = stationType;
        this.userAddress = userAddress;
        createCSVFile();
    }

    void writeToCSV() throws IOException{
        FileWriter writer = new FileWriter(newCSVFile);

        if(stationType.equals("Tesla")){
            writer.write("Closest Tesla Recharge Stations, User Address: " + userAddress + "\n");
        }
        else{
            writer.write("Cheapest " + stationType + " refill Stations, User Address: " + userAddress + "\n");
        }
        writer.write("Station Name, Station Address, Station Price, Maps Link" + "\n");

        for(Station station:stationData){
            writer.write(station.getStationName() + ", " + station.getAddress() + ", " + station.getUnitCost() + ", " + station.getGoogleMapsDirections() + "\n");
        }
        writer.close();
    }

    void createCSVFile(){
        if(stationType.equals("Tesla")){
            this.newCSVFile = new File("NearestTeslaChargers.csv");
        }
        else if(stationType.equals("Diesel")){
            this.newCSVFile= new File("CheapeestDieselStations.csv");
        }
        else{
            this.newCSVFile = new File("Cheapest" + stationType + "GasolineStations.csv");
        }
    }


}
