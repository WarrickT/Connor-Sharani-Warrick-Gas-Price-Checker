/**
 * The station object. 
 */
public class Station{
    //Properties of the station object: Will be the 5 columns displayed on the table
    private String stationName;
    private String address;
    private double unitCost;
    private double displacement;
    private String googleMapsDirections;

    /**
     * Station constructor method
     * @param stationName Name of the staiton
     * @param address The station's address
     * @param unitCost The station's unit cost (Will be 0 for Tesla)
     */
    Station(String stationName, String address, double unitCost){
        this.stationName = stationName;
        this.address = address;
        this.unitCost = unitCost;
    }

    /**
     * Sets the googleMapsDirections property
     * @param googleMapsDirections Link for the Google Maps Directions
     */
    void setGoogleMapsDirections(String googleMapsDirections){
        this.googleMapsDirections = googleMapsDirections;
    }

    /**
     * Sets the displacement property
     * @param displacement Displacement of the station from the user's location
     */
    void setDisplacement(double displacement){
        this.displacement = displacement;
    }

    /**
     * Get method for the name of the station.
     * @return The name of the station.
     */
    String getStationName(){
        return this.stationName;
    }
    /**
     * Get method for the address of the station
     * @return The address of the station.
     */
    String getAddress(){
        return this.address;
    }

    /**
     * Get method for the Google Maps Directions
     * @return Google Maps Directions of the Gas Station
     */
    String getGoogleMapsDirections(){
        return this.googleMapsDirections;
    }

    /**
     * Get method for the unit cost
     * @return Unit cost of the Gas Station (0 for Tesla Stations)
     */
    double getUnitCost(){
        return this.unitCost;
    }

    /**
     * Get method for the displacement
     * @return Displacement of the station from the user's location
     */
    double getDisplacement(){
        return this.displacement;
    }
}