package windowing;

import org.checkerframework.checker.units.qual.C;
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
    private PrioritySearchTree pst;
    
    /**
    * @Param file : name of the dataset file
    * TODO : test if file contains only numbers ( using regex )
    * TODO : better exceptions
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
                    System.out.println(window);
                }
                else {
                    String[] c = line.split(" ");
                    Segment segment = new Segment(new CompositeNumber(Integer.parseInt(c[0]), Integer.parseInt(c[2])),
                                                    new CompositeNumber(Integer.parseInt(c[1]), Integer.parseInt(c[3])));
                    segments.add(segment);
                }
            }
            reader.close();
            //Before we give the set of segments to our construct_tree() method we will sort them base on the y component in order
            //to later be able to compute the median of the set in O(1). Using Quicksort we can ensure a mean complexity of O(nlogn)
            //quicksort(segments, segments.get(0), segments.get(segments.size() - 1));
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


    public void quicksort(ArrayList<Segment> segments, Segment start, Segment end){
        CompositeNumber yStart = start.get_yComp();
        CompositeNumber yEnd = end.get_yComp();
        // We compare the mean of the y component of each segment because they could both have one y coordinate in common.
        if ((yStart.get_coord1() + yStart.get_coord2())/2 < (yEnd.get_coord1() + yEnd.get_coord2())/2){
            int partIndex = partition(segments, start, end);
            //left partition quicksort
            quicksort(segments, start, segments.get(partIndex -1));
            //right partition quicksort
            quicksort(segments, segments.get(partIndex + 1), end);
        }
    }

    public int partition(ArrayList<Segment> segments, Segment start, Segment end){
        int partIndex = segments.indexOf(start);
        int endIndex = segments.indexOf(end);
        for (int i = 0; i < endIndex -1; i ++){
            CompositeNumber yCurrent = segments.get(i).get_yComp();
            CompositeNumber yEnd = segments.get(endIndex).get_yComp();
            // We compare the mean of the y component of each segment because they could both have one y coordinate in common.
            if ((yCurrent.get_coord1() + yCurrent.get_coord2())/2 < (yEnd.get_coord1() + yEnd.get_coord2())/2){
                swap(segments, i, partIndex);
                partIndex ++;
            }
        }
        swap(segments, endIndex, partIndex);
        return partIndex;
    }

    public void swap(ArrayList<Segment> segments, int i, int j){
        //swap element at index i and j.
        Segment temp = segments.get(i);
        segments.set(i, segments.get(j));
        segments.set(j, temp);
    }

    public void print_segments() {
        segments.forEach( (s) -> System.out.println(s) );
    }
}

