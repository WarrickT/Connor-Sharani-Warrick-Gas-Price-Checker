import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes a CSV of gas station data, using the FileWriter class. 
 */
public class CSVWriter {
    //The Station Data, and the user's inputted details. 
    private String[][] formattedStationData;
    private String stationType;
    private String userAddress;

    //Initiating the File and FileWriter objects.
    File newCSVFile;
    FileWriter writer;

    /**
     * Contructor for the CSVWriter
     * @param formattedStationData Station Data in the form of a 2D String Array
     * @param stationType User's entered station type
     * @param userAddress User's entered address
     * @throws IOException Throws the IOException caused by the FileWriter
     */
    CSVWriter(String[][] formattedStationData, String stationType, String userAddress) throws IOException{
        this.formattedStationData = formattedStationData;
        this.stationType = stationType;
        this.userAddress = userAddress;

        //Immediately creates the CSV Files afterwards. 
        createCSVFile();
    }
    
    /**
     * Creates the CSV File based on the user's entered details, and the webscraped data.
     * @throws IOException Throws the IOException caused by the FileWriter
     */
    void createCSVFile() throws IOException{
        //Names the CSV File accordingly 
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

        //Iterates through every station in the formatted station daat. 
        for(String[] station:formattedStationData){
            for(int index = 0; index < station.length; index ++){
                //Indicate there is no price available if no price is proviede.
                //This applies for All Tesla Stations.
                if(station[index].equals("0.0 ¢")){
                    writer.write("\"" + "N/A" + "\"");
                }
                else{
                    writer.write("\"" + station[index].replace(" ¢", " cents") + "\"");
                }
                if(index != 4){
                    writer.write(", ");
                }

            }
            writer.write("\n");
        }
        writer.close();
    }
    }





