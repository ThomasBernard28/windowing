package windowing.datastructures;

public class CompositeNumber {

    private final double coord1;
    private final double coord2;
    public CompositeNumber(double coord1, double coord2){
        this.coord1 = coord1;
        this.coord2 = coord2;
    }
    public double get_coord1(){
        return coord1;
    }
    public double get_coord2(){
        return coord2;
    }
    
    public boolean is_x_smaller_than(CompositeNumber n) {
        double nCoord1 = n.get_coord1();
        double nCoord2 = n.get_coord2();
        if (coord1 < nCoord1 || (coord1==nCoord1 && coord2 < nCoord2)) {
            return true;
        }
        return false;
    }

    public boolean is_y_smaller_than(CompositeNumber n) {
        double nCoord1 = n.get_coord1();
        double nCoord2 = n.get_coord2();
        if (coord2 < nCoord2 || (coord2==nCoord2 && coord1 < nCoord1)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "(" + coord1 + "," + coord2 + ")";
    }
}
