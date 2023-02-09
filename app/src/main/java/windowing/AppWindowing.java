package windowing;

import org.checkerframework.checker.units.qual.C;
import windowing.datastructures.*;
import java.util.ArrayList; 
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class AppWindowing {

    public  ArrayList<Segment> segments;
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
        try {
            //TODO : The first line in the file is not a segment : bounds of the initial window
            File myFile = new File(file);
            Scanner reader = new Scanner(myFile); 
            while ( reader.hasNextLine() ) {
                String line = reader.nextLine();
                String[] c = line.split(" ");
                Segment segment = new Segment(new CompositeNumber(Integer.parseInt(c[0]), Integer.parseInt(c[2])),
                                                new CompositeNumber(Integer.parseInt(c[1]), Integer.parseInt(c[3])));
                segments.add(segment);

            }
            reader.close();
            //Before we give the set of segments to our construct_tree() method we will sort them base on the y component in order
            //to later be able to compute the median of the set in O(1). Using Quicksort we can ensure a mean complexity of O(nlogn)
            quicksort(segments, segments.get(0), segments.get(segments.size() - 1));
        } catch ( FileNotFoundException e ) {
            System.out.println("File " + file + " not found");
        }
    }

    public void window(String[] window) {
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
        //TODO
        return 0;
    }

    public void print_segments() {
        segments.forEach( (s) -> System.out.println(s) );
    }
}

