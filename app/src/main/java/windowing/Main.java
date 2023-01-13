package windowing;

import windowing.datastructures.*;
import java.util.ArrayList; 
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class App {

    private ArrayList segments = new ArrayList();
    private PrioritySearchTree pst;
    
    /**
    * @Param file : name of the dataset file
    * TODO : test if file contains only numbers ( using regex )
    * TODO : better exceptions
    **/
    public void load_segments(String file) {
        String path = System.getProperty("user.dir") + "/build/resources/main/"+file;
        try {
            File myFile = new File(path);
            Scanner reader = new Scanner(myFile); 
            while ( reader.hasNextLine() ) {
                String line = reader.nextLine();
                String[] c = line.split(" "); 
                segments.add(new Segment(Integer.parseInt(c[0]), Integer.parseInt(c[1]), 
                                         Integer.parseInt(c[2]), Integer.parseInt(c[3])));
            }
            reader.close();
        } catch ( FileNotFoundException e) {
            System.out.println("File " + path + " not found");
        }
    }

    public void generate_pst() {}

    public void print_segments() {
        segments.forEach((s) -> System.out.println(s));
    }
}

public class Main {

    public static void main(String[] args) {
        App app = new App();
        app.load_segments("segments1.txt");
        app.print_segments();
    }
}
