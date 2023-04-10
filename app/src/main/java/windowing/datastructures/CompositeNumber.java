package windowing.datastructures;

/**
 * This class defines the CompositeNumber object.
 * It's an object that comes from the reference.
 * A composite number is composed of two double values.
 * The class implements methods to compare two composite numbers.
 */
public class CompositeNumber {

    private double coord1;
    private double coord2;

    /**
     * Constructor for the CompositeNumber object.
     * @param coord1 The first double value.
     * @param coord2 The second double value.
     */
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

    /**
     * This method test the equality of two composite numbers.
     * @param n The composite number to compare to.
     * @return True if the two composite numbers are equal, false otherwise.
     */
    public boolean is_equal_to(CompositeNumber n){
        double nCoord1 = n.get_coord1();
        double nCoord2 = n.get_coord2();
        if(coord2 == nCoord2 && coord1 == nCoord1){
            return true;
        }
        return false;
    }

    /**
     * This method test if the first composite number is smaller than the second one based on the x coordinate.
     * @param n The composite number to compare to.
     * @return True if the first composite number is smaller than the second one based on the x coordinate, false otherwise.
     */
    public boolean is_x_smaller_than(CompositeNumber n) {
        double nCoord1 = n.get_coord1();
        double nCoord2 = n.get_coord2();
        if (coord1 < nCoord1 || (coord1==nCoord1 && coord2 < nCoord2)) {
            return true;
        }
        return false;
    }

    /**
     * This method test if the first composite number is smaller than the second one based on the y coordinate.
     * @param n The composite number to compare to.
     * @return True if the first composite number is smaller than the second one based on the y coordinate, false otherwise.
     */
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
