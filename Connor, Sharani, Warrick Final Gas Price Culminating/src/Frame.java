import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Frame extends JFrame {
    //Each page in the stored as an object in the frame. 
    private MainPage mainPage;
    private SecondPage secondPage;

    //The parent facade obejct is created to access the backend. 
    private Facade facade;

    Frame(Facade facade) throws IOException{
        this.facade = facade;
        setTitle("Gas Finder App");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        mainPage = new MainPage(this, facade);
        try{
            secondPage = new SecondPage(this, facade);
        }
        catch(IOException ioe){
            ioe.printStackTrace(System.err);
        }

        setContentPane(mainPage);
    }

    void showMainPage() {
        facade.clearStationData();
        secondPage.clearCurrentData();

        setContentPane(mainPage);
        revalidate();
        repaint();
}
    

     void showSecondPage() throws IOException, InterruptedException{        
        facade.setStationType(mainPage.getStation());
        facade.setUserRadius(mainPage.getRadius());
        facade.scrapeStations();

        secondPage.getFormattedStationData();
        secondPage.showResults();

        setContentPane(secondPage);
        revalidate();
        repaint();
    }

    void createCSV() throws IOException{
        facade.createCSVWriter(secondPage.getCurrentStationData());
        JOptionPane.showMessageDialog(this,"The CSV File for your station data has been created.","CSV Created", JOptionPane.INFORMATION_MESSAGE);
    }

}