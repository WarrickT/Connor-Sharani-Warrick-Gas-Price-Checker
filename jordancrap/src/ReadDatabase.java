import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadDatabase {
    protected ArrayList<String> stationDatabase = new ArrayList<String>();
    private BufferedReader br = null;
    private String databaseName;
    private String stationType;

    ReadDatabase(String stationType){
        this.stationType = stationType;
    }

    ArrayList<String> getGasStationDatabase() throws IOException{
        if(stationType.equals("Tesla Supercharge")){
            databaseName = "teslaDatabase.txt";
        }
        else{
            databaseName = "modifiedDatabase.txt";
        }
        try{
            br = new BufferedReader(new FileReader("database" + "/" + databaseName));

            String contentLine = br.readLine();
            while (contentLine != null) {
                if(contentLine == null){
                    continue;
                }
                stationDatabase.add(contentLine);
                contentLine = br.readLine();
            }
        }

        catch(IOException ioe){
            System.out.println("Cannot find file");
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
		        System.out.println("Error in closing the BufferedReader");
	   }
        }
        return stationDatabase;    
    }

    ArrayList<String> getTeslaData() throws IOException{

        return null;
    }

    void printDataBase(ArrayList<String> list){
        for(String el:list){
            System.out.println(el);
        }
    }

}
