package windowing;

import windowing.datastructures.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;

/**
 * This class is the link between the GUI and the data structures.
 * It contains the methods that are called by the GUI to perform the different operations.
 * This class loads the dataset and stores the segments in the PrioritySearchTree.
 * It also contains the methods to perform the windowing operations.
 */
public class AppWindowing {

    public ArrayList<Segment> segments;
    public ArrayList<Segment> invertedSegments;
    public ArrayList<Double> window;
    private PrioritySearchTree pst;
    private PrioritySearchTree invertedPst;
    

    /**
     * Method that read the file at the given path and transform
     * the information of each line into a Segment or an invertedSegment.
     * The result is stored in the 'segments' resp. ('invertedSegments') ArrayList
     * @param file : name of the dataset file
     */
    public void load_segments(String file) {
        
        segments = new ArrayList<Segment>();
        invertedSegments = new ArrayList<Segment>();
        window = new ArrayList<Double>();
        try {
            File myFile = new File(file);
            Scanner reader = new Scanner(myFile); 
            boolean firstLine = true;
            while ( reader.hasNextLine() ) {
                String line = reader.nextLine();
                if ( firstLine ) {
                    for ( String w : line.split(" ", 0) ) {
                        window.add(Double.parseDouble(w));
                    }
                    firstLine = false;
                }
                else {
                    String[] c = line.split(" ");


                    //Create a segment like this : S = ((x1,y1);(x2,y2)) In order to treat horizontal segments
                    Segment segment = new Segment(new CompositeNumber(Double.parseDouble(c[0]), Double.parseDouble(c[1])),
                            new CompositeNumber(Double.parseDouble(c[2]), Double.parseDouble(c[3])));
                    segments.add(segment);

                    //Create a segment like this : S = ((y1,x1);(y2,x2)) in order to treat vertical segments
                    Segment invertedSegment = new Segment(new CompositeNumber(Double.parseDouble(c[1]), Double.parseDouble(c[0])),
                            new CompositeNumber(Double.parseDouble(c[3]), Double.parseDouble(c[2])));
                    invertedSegments.add(invertedSegment);
                }
            }
            reader.close();
            pst = new PrioritySearchTree(segments);
            invertedPst = new PrioritySearchTree(invertedSegments);

        } catch ( FileNotFoundException e ) {
            Alert alert = new Alert(AlertType.ERROR, "File " + file + " not found");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "File " + file + " could not be loaded");
            alert.show();
        }
    }

    /**
     * Method that query the PrioritySearchTree with the given window
     * and return the segments that are within the window.
     * As we retrieve the segments from the PrioritySearchTree, we need to re_flip the vertical segments to their original axis.
     * We then add both horizontal and vertical segments to the segmentsToReport ArrayList (And check if they are not already in the list).
    * @param queryWindow : bounds of the window
    * @return an arrayList of segments that are within the window and that will be displayed.
    */
    public ArrayList<Segment> query(ArrayList<Double> queryWindow) {
        //In the inverted pst y component are inverted with x components
        ArrayList<Segment> verticalSegments = invertedPst.query(queryWindow.get(2), queryWindow.get(3),
                queryWindow.get(0), queryWindow.get(1), true);

        //We need to re_flip vertical segments to their original axis
        ArrayList<Segment> horizontalSegments = pst.query(queryWindow.get(0), queryWindow.get(1),
                queryWindow.get(2), queryWindow.get(3), false);


        ArrayList<Segment> segmentsToReport = new ArrayList<>();
        for (Segment hSegment : horizontalSegments){
            if (!segmentsToReport.contains(hSegment)){
                segmentsToReport.add(hSegment);
            }
        }

        for(Segment segment : verticalSegments){
            Segment reInvertedSegment = new Segment(new CompositeNumber(segment.get_startComp().get_coord2(), segment.get_startComp().get_coord1()),
                                            new CompositeNumber(segment.get_endComp().get_coord2(), segment.get_endComp().get_coord1()));
            if (!segmentsToReport.contains(reInvertedSegment)){
                segmentsToReport.add(reInvertedSegment);
            }
        }
        verticalSegments.clear();
        horizontalSegments.clear();
        return segmentsToReport;
    }

    public void print_segments(ArrayList<Segment> segments) {
        segments.forEach( (s) -> System.out.println(s) );
    }
}
