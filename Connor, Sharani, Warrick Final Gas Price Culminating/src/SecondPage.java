import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * The object for the second page panel. 
 */
public class SecondPage extends JPanel{
    //Reference to the parent frame
    private Frame frame;
    private Facade facade;
    private JLabel titleLabel;

    //The table displaying station data. 
    private JTable stationDataTable;
    private JScrollPane stationDataTableScroll;
    
    //The column headings for the table
    private String[] columnsName = {"Station Name", "Address", "Unit Cost", "Displacement", "Directions"};
    //The formattedStationData for the table
    private String[][] formattedStationData;

    //The sorter object, used to toggle sort the data
    private TableRowSorter<TableModel> sorter;
    private List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();

    /**
     * Constructor method for the SecondPage Panel
     * @param frame Reference to the parent frame object
     * @param facade Reference to the facade object
     * @throws IOException Throws IOException from the CSV Writer.
     */

    SecondPage(Frame frame, Facade facade) throws IOException{
        //Instantiating the panel object
        super(null);
        //Setting references to the frame and facade objects. 
        this.frame = frame;
        this.facade = facade;

        //Instantiating the title label
        titleLabel = new JLabel("Results");
        titleLabel.setBounds(400, 20, 100, 50);

        //Instantiating the back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> frame.showMainPage());
        backButton.setBounds(25, 475, 100, 50);

        //Instantiating the createCSV button
        JButton csvButton = new JButton("Create CSV");
        csvButton.addActionListener(e -> {
            try {
                frame.createCSV();
            } catch (IOException ioe) {
                // TODO Auto-generated catch block
                ioe.printStackTrace();
            }
        });
        csvButton.setBounds(775, 475, 100, 50);

        //Adding the lable and the buttons onto the screen.
        this.add(titleLabel);
        this.add(backButton);
        this.add(csvButton);
    }
    
    /**
     * Show the results from webscraping in the form a table. 
     */
    void showResults(){
        //Instantiating the stationDataTable object
        stationDataTable = new JTable(formattedStationData, columnsName);

        //Setting the dimensions of the columns
        for(int columnNum = 0; columnNum < 5; columnNum++) {
            TableColumn column = stationDataTable.getColumnModel().getColumn(columnNum);
            if(columnNum == 1){
                column.setPreferredWidth(300);
            }
            else if(columnNum == 2){
                column.setPreferredWidth(75);
            }
            else if(columnNum == 3){
                column.setPreferredWidth(125);
            }
            else{
                column.setPreferredWidth(250);
            }
        }
        
        //Setting up the toggle sort
        sorter = new TableRowSorter<>(stationDataTable.getModel());

        //Override the default sorting methods provided by the TableRowSorter objects. 
        //Instead of sorting by alphabetically, the table will sort by the numerical values of the unit cost and and displacement. 
        //This is necessary due to all the data being in the form of a 2D String array. 
        sorter.setComparator(2, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                    return Double.compare((Double.parseDouble(o1.replace(" ¢", ""))), (Double.parseDouble((o2.replace(" ¢", "")))));
                }
        });
            
        sorter.setComparator(3, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                    return Double.compare((Double.parseDouble(o1.replace(" km", ""))), (Double.parseDouble((o2.replace(" km", "")))));
                }
        });
        
        sorter.setSortsOnUpdates(true);
        stationDataTable.setRowSorter(sorter);
        
        //Allow for scrolling if the stationData extends beyond the page panel. 
        stationDataTableScroll = new JScrollPane(stationDataTable);
        stationDataTableScroll.setBounds(150, 90, 600, 450);

        //Add the entire table onto the panel. 
        this.add(stationDataTableScroll);
        revalidate();
        repaint();

    }

    /**
     * Delete the previous results of webscraping if the user decides to head back to the main page. 
     */
    void clearCurrentData(){
        this.remove(stationDataTable);
        this.remove(stationDataTableScroll);
    }

    /**
     * Fetch the formattedStationData from the facade backend. 
     */
    void getFormattedStationData(){
        formattedStationData = facade.getFormattedStationData();
    }

    /**
     * The row sorter only sorts the data as displayed to the screen.
     * This method will read the "status" of the sorter and use lambda methods to sort the array accordinly.
     * @return The 2D String Array of the station data. 
     */
    String[][] getCurrentStationData(){
        //This list returns the "status" of each column's sort. 
        List<? extends javax.swing.RowSorter.SortKey> sortStatusList = sorter.getSortKeys();

        //It is possible the user does not toggle at all. 
        if(sortStatusList.size() != 0){
            //The sort status of the table
            String currentSortStatus = sortStatusList.get(0).getSortOrder().toString();
            //The column that is sorted. 
            int sortedColumn = sortStatusList.get(0).getColumn();

            //Creating lambda methods to sort the 2D String Array by column. 
            //Column 2 and 3 require additional code since they need to be sorted by numerical value. 
            if(sortedColumn == 2 || sortedColumn == 3){
                if(currentSortStatus.equals("ASCENDING")){
                    Arrays.sort(formattedStationData, (a, b) -> Double.compare(Double.parseDouble(a[sortedColumn].replace(" ¢", "").replace(" km", "")), 
                    Double.parseDouble(b[sortedColumn].replace(" ¢", "").replace(" km", ""))));
                }
                else{
                    Arrays.sort(formattedStationData, (a, b) -> Double.compare(Double.parseDouble(b[sortedColumn].replace(" ¢", "").replace(" km", "")), 
                    Double.parseDouble(a[sortedColumn].replace(" ¢", "").replace(" km", ""))));
                }
            }
            else{
                if(currentSortStatus.equals("ASCENDING")){
                    Arrays.sort(formattedStationData, (a, b) -> a[sortedColumn].compareTo(b[sortedColumn]));
                }
                else{
                    Arrays.sort(formattedStationData, (a, b) -> b[sortedColumn].compareTo(a[sortedColumn]));
                }
            }
        }
        return this.formattedStationData;
    }
}

