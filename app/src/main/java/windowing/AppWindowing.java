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
        } catch ( FileNotFoundException e ) {
            System.out.println("File " + file + " not found");
        }
    }

    public void print_segments() {
        segments.forEach( (s) -> System.out.println(s) );
    }
}

