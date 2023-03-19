package windowing.datastructures;

import java.util.ArrayList;

public class Node {

    public CompositeNumber point;
    public Segment segment;
    public int median;

    public Node(CompositeNumber point, Segment segment){
        this.point = point;
        this.segment = segment;
    }

    @Override
    public String toString() {
        return point.toString();
    }

}
