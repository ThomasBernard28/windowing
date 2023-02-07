package windowing.datastructures;

public class Segment {

    private final CompositeNumber xComp;
    private final CompositeNumber yComp;

    public Segment(CompositeNumber xComp, CompositeNumber yComp){
        this.xComp = xComp;
        this.yComp = yComp;
    }
    public CompositeNumber get_xComp(){
        return xComp;
    }
    public CompositeNumber get_yComp(){
        return yComp;
    }
    @Override
    public String toString() {
        return " ((x,x');(y,y')) : (" + xComp.toString() + ";" +
                yComp.toString() + ")";
    }
    /*
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

}
