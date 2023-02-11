package windowing.datastructures;

import java.util.ArrayList;

public class Node {

    private final double median;
    private Segment segment;

    public Node(double median, Segment segment){
        this.median = median;
        this.segment = segment;
    }

}