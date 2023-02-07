package windowing.datastructures;

public class Node {

    private final double median;
    private final Segment segment;

    public Node(double median, Segment segment){
        this.median = median;
        this.segment = segment;
    }

}