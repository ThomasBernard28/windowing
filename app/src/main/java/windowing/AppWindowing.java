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
    public ArrayList<Float> window;
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
     */
    public void load_segments(String file) {
        
        segments = new ArrayList<Segment>();
        invertedSegments = new ArrayList<Segment>();
        window = new ArrayList<Float>();
        try {
            File myFile = new File(file);
            Scanner reader = new Scanner(myFile); 
            boolean firstLine = true;
            while ( reader.hasNextLine() ) {
                String line = reader.nextLine();
                if ( firstLine ) {
                    for ( String w : line.split(" ", 0) ) {
                        window.add(Float.parseFloat(w));
                    }
                    firstLine = false;
                }
                else {
                    String[] c = line.split(" ");

                    //Create a segment like this : S = ((x1,y1);(x2,y2)) In order to treat horizontal segments
                    Segment segment = new Segment(new CompositeNumber(Integer.parseInt(c[0]), Integer.parseInt(c[1])),
                                                    new CompositeNumber(Integer.parseInt(c[2]), Integer.parseInt(c[3])));
                    segments.add(segment);

                    //Create a segment like this : S = ((y1,x1);(y2,x2)) in order to treat vertical segments
                    Segment invertedSegment = new Segment(new CompositeNumber(Integer.parseInt(c[1]), Integer.parseInt(c[0])),
                                                            new CompositeNumber(Integer.parseInt(c[3]), Integer.parseInt(c[2])));
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
    * @Param window : bounds of the window
    * @Return an arrayList of segments that are within the window
    */
    public ArrayList query(String[] window) {
        return pst.query(Double.parseDouble(window[0]), Double.parseDouble(window[1]),
                         Double.parseDouble(window[2]), Double.parseDouble(window[3]));
    }

    public void print_segments(ArrayList<Segment> segments) {
        segments.forEach( (s) -> System.out.println(s) );
    }
}
