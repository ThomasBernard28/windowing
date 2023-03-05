package windowing;

import windowing.datastructures.*;
import java.util.ArrayList; 
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;

import static windowing.datastructures.PrioritySearchTree.construct_tree;

public class AppWindowing {
    public ArrayList<Segment> hSegments;
    public ArrayList<Segment> vSegments;
    public ArrayList<Float> window;
    public PrioritySearchTree horizontalPst;
    public PrioritySearchTree verticalPst;
    
    /**
    * @Param file : name of the dataset file
    **/
    public void load_segments(String file) {
        /**
        * method that read the file at the given path and transform
        * the information of each line into a Segment
        * the result is stored in the 'segments' array list
        */
        
        hSegments = new ArrayList<Segment>();
        vSegments = new ArrayList<Segment>();
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
                    Segment hSegment = new Segment(new CompositeNumber(Integer.parseInt(c[0]), Integer.parseInt(c[1])),
                                                    new CompositeNumber(Integer.parseInt(c[2]), Integer.parseInt(c[3])));
                    hSegments.add(hSegment);
                    //Create a segment like this : S = ((y1,x1);(y2,x2)) In order to treat vertical segments
                    Segment vSegment = new Segment(new CompositeNumber(Integer.parseInt(c[1]), Integer.parseInt(c[0])),
                                                    new CompositeNumber(Integer.parseInt(c[3]), Integer.parseInt(c[2])));
                    vSegments.add(vSegment);

                }
            }
            reader.close();
            //Before we give the set of segments to our construct_tree() method we will sort them base on the y component in order
            //to later be able to compute the median of the set in O(1). Using Quicksort we can ensure a mean complexity of O(nlogn)

            //Sort the horizontal segments by the y Component
            quicksort(hSegments, hSegments.get(0), hSegments.get(hSegments.size() - 1));
            //Sort the vertical segments by their 'fake' y Component which is their real x component in this case.
            quicksort(vSegments, vSegments.get(0), vSegments.get(vSegments.size() - 1));

        } catch ( FileNotFoundException e ) {
            Alert alert = new Alert(AlertType.ERROR, "File " + file + " not found");
            alert.show();
        }
        //horizontalPst = construct_tree(hSegments);
        //verticalPst = construct_tree(vSegments);
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
        // yStart and yEnd are like (y1,y2)
        CompositeNumber yStart = new CompositeNumber(start.get_startComp().get_coord2(), start.get_endComp().get_coord2());
        CompositeNumber yEnd = new CompositeNumber(end.get_startComp().get_coord2(), end.get_endComp().get_coord2());
        // We compare the mean of the y component of each segment because they could both have one y coordinate in common.
        if ((yStart.get_coord1() + yStart.get_coord2())/2 < (yEnd.get_coord1() + yEnd.get_coord2())/2){
            int partIndex = partition(segments, start, end);

            if (partIndex >= 1 && partIndex < segments.size() - 1){
                //left partition quicksort
                quicksort(segments, start, segments.get(partIndex - 1));
                //right partition quicksort
                quicksort(segments, segments.get(partIndex + 1), end);
            }
        }
    }

    public int partition(ArrayList<Segment> segments, Segment start, Segment end){
        int partIndex = segments.indexOf(start);
        int startIndex = segments.indexOf(start);
        int endIndex = segments.indexOf(end);
        for (int i = startIndex; i < endIndex -1; i ++){

            CompositeNumber yCurrent = new CompositeNumber(segments.get(i).get_startComp().get_coord2(),
                                        segments.get(i).get_endComp().get_coord2());

            CompositeNumber yEnd = new CompositeNumber(segments.get(endIndex).get_startComp().get_coord2(),
                                        segments.get(endIndex).get_endComp().get_coord2());


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
    public void print_segments(ArrayList<Segment> segments) {
        segments.forEach( (s) -> System.out.println(s) );
    }
}

