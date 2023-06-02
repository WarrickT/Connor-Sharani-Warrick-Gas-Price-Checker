public class Station implements Comparable<Station>{
    private String stationName;
    private String address;
    private double unitCost;
    private double displacement;
    private String googleMapsDirections;

    Station(String stationName, String address, double unitCost){
        this.stationName = stationName;
        this.address = address;
        this.unitCost = unitCost;
    }
    void setGoogleMapsDirections(String googleMapsDirections){
        this.googleMapsDirections = googleMapsDirections;
    }
    void setDisplacement(double displacement){
        this.displacement = displacement;
    }

    String getStationName(){
        return this.stationName;
    }
    String getAddress(){
        return this.address;
    }
    String getGoogleMapsDirections(){
        return this.googleMapsDirections;
    }
    double getUnitCost(){
        return this.unitCost;
    }
    double getDisplacement(){
        return this.displacement;
    }

    @Override
    public int compareTo(Station compareStation) {
		double comparePrice = ((Station) compareStation).getUnitCost();
        return Double.compare(this.unitCost, comparePrice);
	}
}