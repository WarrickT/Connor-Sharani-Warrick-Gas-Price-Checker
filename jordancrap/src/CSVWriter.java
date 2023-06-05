import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CSVWriter {
    private String[][] formattedStationData;
    private String stationType;
    private String userAddress;
    File newCSVFile;
    FileWriter writer;

    CSVWriter(String[][] formattedStationData, String stationType, String userAddress) throws IOException{
        this.formattedStationData = formattedStationData;
        this.stationType = stationType;
        this.userAddress = userAddress;
        System.out.println("Nice");
        createCSVFile();
    }
    
    void createCSVFile() throws IOException{
        if(stationType.equals("Tesla SuperCharge")){
            this.newCSVFile = new File("TeslaSuperchargeStations.csv");
        }
        else if(stationType.equals("Diesel")){
            this.newCSVFile= new File("DieselStations.csv");
        }
        else{
            this.newCSVFile = new File(stationType + "GasolineStations.csv");
        }

        this.writer = new FileWriter(newCSVFile);
        writer.write(stationType + " Refill Stations, User Address: " + userAddress + "\n");
        writer.write("Station Name, Station Address, Station Price, Displacement, Maps Link" + "\n");

        for(String[] station:formattedStationData){
            for(int index = 0; index < station.length; index ++){
                writer.write("\"" + station[index].replace(" ¢", " cents") + "\"");
                if(index != 4){
                    writer.write(", ");
                }
            }
            writer.write("\n");
        }
        writer.close();
    }
    }


     



