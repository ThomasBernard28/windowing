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
    public ArrayList<Float> window;

    public PrioritySearchTree pst;
    
    /**
    * @Param file : name of the dataset file
    **/
    public void load_segments(String file) {
        /**
        * method that read the file at the given path and transform
        * the information of each line into a Segment
        * the result is stored in the 'segments' array list
        */
        
        segments = new ArrayList<Segment>();
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

                }
            }
            reader.close();
            pst = new PrioritySearchTree(segments);
            System.out.println(pst); // DEBUG 

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
        ArrayList wSegments = new ArrayList();
        return wSegments;
    }

    public void print_segments(ArrayList<Segment> segments) {
        segments.forEach( (s) -> System.out.println(s) );
    }
}
