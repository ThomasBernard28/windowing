package windowing;

import windowing.datastructures.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;

public class AppWindowing {

    public ArrayList<Segment> segments;
    public ArrayList<Segment> invertedSegments;
    public ArrayList<Double> window;
    private PrioritySearchTree pst;
    private PrioritySearchTree invertedPst;
    
    /**
    * @Param file : name of the dataset file
    **/
    /**
     * Method that read the file at the given path and transform
     * the information of each line into a Segment or an invertedSegment.
     * The result is stored in the 'segments' resp. ('invertedSegments') ArrayList
     * @param file : name of the dataset file
     * @return the window containing all segments
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
        }
    }

    /**
    * @param window : bounds of the window
    * @return an arrayList of segments that are within the window
    */
    public ArrayList<Segment> query(String[] window) {
        ArrayList<Double> queryWindow = new ArrayList<Double>();
        for (int x=0; x<4; x++) {
            if (window[x].equals("-inf") || window[x].equals("+inf")) {
                queryWindow.add(this.window.get(x));  
            } 
            else {
                queryWindow.add(Double.parseDouble(window[x]));
            }
        }
        ArrayList<Segment> horizontalSegments = pst.query(queryWindow.get(0), queryWindow.get(1),
                queryWindow.get(2), queryWindow.get(3));

        //In the inverted pst y component are inverted with x components
        ArrayList<Segment> verticalSegments = invertedPst.query(queryWindow.get(2), queryWindow.get(3),
                queryWindow.get(0), queryWindow.get(1));

        horizontalSegments.addAll(verticalSegments);
        return horizontalSegments;
    }

    public void print_segments(ArrayList<Segment> segments) {
        segments.forEach( (s) -> System.out.println(s) );
    }
}
