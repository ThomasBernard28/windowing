package windowing.datastructures;

import java.util.ArrayList;

public class Segment {
    
    private int x1; //Smallest x
    private int x2; //biggest x
    private int y1; //smallest y
    private int y2; //biggest y

    public Segment(int x1, int y1, int x2, int y2){
        this.x1 = x1;
	this.x2 = x2;
	this.y1 = y1;
	this.y2 = y2;
    }
    
    public ArrayList get_coord() {
        ArrayList coord = new ArrayList();
        return coord;
    }
    
    /*
    public Arraylist get_composite_coord() {
    }
    */

    @Override
    public String toString() {
        return "  x1:" + Integer.toString(x1) + 
               "; x2:" + Integer.toString(x2) +
               "; y1:" + Integer.toString(y1) +
               "; y2:" + Integer.toString(y2);
    }
}
