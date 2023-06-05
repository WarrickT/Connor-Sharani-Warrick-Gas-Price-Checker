import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Frame extends JFrame {
    private MainPage mainPage;
    private SecondPage secondPage;
    private BackendController backend = new BackendController();

    Frame() throws IOException{
        setTitle("Gas Finder App");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        mainPage = new MainPage(this, backend);
        try{
            secondPage = new SecondPage(this, backend);
        }
        catch(IOException ioe){
            ioe.printStackTrace(System.err);
        }

        setContentPane(mainPage);
    }

    void showMainPage() {
        backend.clearStationData();
        secondPage.clearCurrentData();

        setContentPane(mainPage);
        revalidate();
        repaint();
}
    

     void showSecondPage() throws IOException, InterruptedException{
        backend.setStationType(mainPage.getStation());
        backend.setUserRadius(mainPage.getRadius());
        backend.scrapeStations();
        secondPage.getFormattedStationData();

        secondPage.showResults();
        setContentPane(secondPage);
        revalidate();
        repaint();
    }

    BackendController getBackend(){
        return backend;
    }

    void createCSV() throws IOException{
        backend.createCSVWriter(secondPage.getCurrentStationData());
        JOptionPane.showMessageDialog(this,"The CSV File for your station data has been created.","CSV Created", JOptionPane.INFORMATION_MESSAGE);
    }

}