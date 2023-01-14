package windowing.datastructures;

import java.util.ArrayList;

public class Segment {
    
    private double x1; //Smallest x
    private double x2; //biggest x
    private double y1; //smallest y
    private double y2; //biggest y

    public Segment(double x1, double y1, double x2, double y2){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }
    
    public ArrayList get_coords() {
        ArrayList<Double> coords = new ArrayList<Double>();
        coords.add(x1);
        coords.add(y1);
        coords.add(x2);
        coords.add(y2);
        return coords;
    }

    /*
    public Arraylist get_composite_coord() {
    }
    */

    @Override
    public String toString() {
        return "  x1:" + Double.toString(x1) + 
               "; x2:" + Double.toString(x2) +
               "; y1:" + Double.toString(y1) +
               "; y2:" + Double.toString(y2);
    }
}
