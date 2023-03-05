package windowing.datastructures;

public class CompositeNumber{

    private double firstTerm;

    private double secondTerm;

    public CompositeNumber(double firstTerm, double secondTerm) {
        this.firstTerm = firstTerm;
        this.secondTerm = secondTerm;
    }

    public double getFirstTerm() {
        return firstTerm;
    }

    public void setFirstTerm(double firstTerm) {
        this.firstTerm = firstTerm;
    }

    public double getSecondTerm() {
        return secondTerm;
    }

    public void setSecondTerm(double secondTerm) {
        this.secondTerm = secondTerm;
    }

    //This method aims to compare x coordinates of two points.
    // return 1 if cn1 > cn2 and 0 if cn1 < cn2
    public static int compare(CompositeNumber cn1, CompositeNumber cn2){
        if (cn1.firstTerm < cn2.firstTerm){
            return 0;
        }
        else {
            if (cn1.firstTerm > cn2.firstTerm){
                return 1;
            }
            else{
                if (cn1.secondTerm < cn2.secondTerm){
                    return 0;
                }return 1;
            }
        }
    }

    @Override
    public String toString() {
        return "CompositeNumber{" +
                "firstTerm=" + firstTerm +
                ", secondTerm=" + secondTerm +
                '}';
    }
}
