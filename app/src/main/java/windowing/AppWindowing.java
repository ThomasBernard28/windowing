package windowing;

import windowing.datastructures.*;
import java.util.ArrayList; 
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import windowing.datastructures.PrioritySearchTree.*;

public class AppWindowing {
    public ArrayList<Point> points;
    public ArrayList<Float> window;
    private PrioritySearchTree pst;
    
    /**
    * @Param file : name of the dataset file
    **/
    public void load_points(String file) {
        /**
        * method that read the file at the given path and transform
        * the information of each line into a Segment
        * the result is stored in the 'segments' array list
        */
        points = new ArrayList<Point>();
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

                    // Create a point
                    Point point = new Point(Double.parseDouble(c[0]), Double.parseDouble(c[1]));
                    points.add(point);

                }
            }
            reader.close();
            //Before we give the set of points to our construct_tree() method we will sort them base on the y component in order
            //to later be able to compute the median of the set in O(1). Using Quicksort we can ensure a mean complexity of O(nlogn)

            //Sort the points by the y Component
            quicksort(points, points.get(0), points.get(points.size() - 1));

            for (Point p:
                 points) {
                System.out.println(p.getY());
            }

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


    public void quicksort(ArrayList<Point> points, Point start, Point end){
        // yStart and yEnd are like (y1,y2)
        int yStart = points.indexOf(start);
        System.out.println(yStart);
        int yEnd = points.indexOf(end);
        System.out.println(yEnd);
        // We compare the mean of the y component of each segment because they could both have one y coordinate in common.
        if (yStart < yEnd){
            int partIndex = partition(points, start, end);
            System.out.println(partIndex);
            if (partIndex >= 0 && partIndex < points.size() - 1){
                //left partition quicksort
                quicksort(points, start, points.get(partIndex));
                //right partition quicksort
                quicksort(points, points.get(partIndex + 1), end);
            }
        }
    }

    public int partition(ArrayList<Point> points, Point start, Point end){
        int partIndex = points.indexOf(start);
        int startIndex = points.indexOf(start);
        int endIndex = points.indexOf(end);
        for (int i = startIndex; i < endIndex -1; i ++){
            System.out.println(i);
            Double yCurrent = points.get(i).getY();
            Double yEnd = points.get(endIndex).getY();
            // We compare the mean of the y component of each segment because they could both have one y coordinate in common.
            if (yCurrent < yEnd){
                swap(points, i, partIndex);
                partIndex ++;
                System.out.println(points);
            }
        }
        swap(points, endIndex, partIndex);
        return partIndex;
    }

    public void swap(ArrayList<Point> points, int i, int j){
        //swap element at index i and j.
        Point temp = points.get(i);
        points.set(i, points.get(j));
        points.set(j, temp);
    }
    public void print_segments(ArrayList<Segment> segments) {
        segments.forEach( (s) -> System.out.println(s) );
    }
}

