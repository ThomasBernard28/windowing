package windowing.datastructures;

import org.checkerframework.checker.units.qual.C;

import static windowing.datastructures.CompositeNumber.compare;

public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    //This method compares two points by their x coordinates.
    //In case both x are equals we use the Composite number compare method
    // Return 1 if p1 > p2 based on x coordinates. else 0
    public static int compareX(Point p1, Point p2){
        if (p1.x < p2.x){
            return 0;
        }
        else if (p1.x > p2.x){
            return 1;
        }
        else{
            CompositeNumber cn1 = new CompositeNumber(p1.x, p1.y);
            CompositeNumber cn2 = new CompositeNumber(p2.x, p2.y);
            return compare(cn1, cn2);
        }
    }

    public static int compareY(Point p1, Point p2){
        if (p1.y < p2.y){
            return 0;
        }
        else if (p1.y > p2.y){
            return 1;
        }
        else{
            CompositeNumber cn1 = new CompositeNumber(p1.y, p1.x);
            CompositeNumber cn2 = new CompositeNumber(p2.y, p2.x);
            return compare(cn1, cn2);
        }
    }

    @Override
    public String toString() {
        return "Point {" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
