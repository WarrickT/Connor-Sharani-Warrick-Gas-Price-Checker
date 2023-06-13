import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads the database files and returns a String ArrayList.
 */
public class ReadDatabase {
    //Contains all the lines in the database.
    protected ArrayList<String> stationDatabase = new ArrayList<String>();

    //Instantiating the BufferedReader object.
    private BufferedReader br = null;
    private String databaseName;
    private String stationType;

    /**
     * ReadDatabase constructor method; sets the stationType property according to the text box input. 
     * @param stationType The type of station chosen by the user
     */
    ReadDatabase(String stationType){
        this.stationType = stationType;
    }

    /**
     * Uses FileReader to read the appropriate database .txt file. 
     * This ArrayList will be referred for checking if the database includes the lat long coordinates of the gas staiton scraped. 
     * @return String ArrayList of the Gas Station Data: Station Name, Station Address, then it's lat-long coordinates
     * @throws IOException
     */
    ArrayList<String> getGasStationDatabase() throws IOException{
        if(stationType.equals("Tesla Supercharge")){
            databaseName = "teslaDatabase.txt";
        }
        else{
            databaseName = "gasDatabase.txt";
        }

        try{
            br = new BufferedReader(new FileReader("database" + "/" + databaseName));
            String contentLine = br.readLine();
            //Preventing the BufferedReader bug.
            while (contentLine != null) {
                if(contentLine == null){
                    continue;
                }

                //Adding the line in the buffferedReader into the ArrayList. 
                stationDatabase.add(contentLine);
                //Reading the next line
                contentLine = br.readLine();
            }
        }

        catch(IOException ioe){
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
	   }
        }
        //Returning the String ArrayList of the data
        return stationDatabase;  
    }
}