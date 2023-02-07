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

    @Override
    public String toString(){
        return "(" + coord1 + "," + coord2 + ")";
    }
}
