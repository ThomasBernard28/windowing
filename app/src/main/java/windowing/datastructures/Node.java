package windowing.datastructures;

public class Node {

    private Segment segment;
    private CompositeNumber median;

    public Node(Segment segment, CompositeNumber median) {
        this.segment = segment;
        this.median = median;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public CompositeNumber getMedian() {
        return median;
    }

    public void setMedian(CompositeNumber median) {
        this.median = median;
    }
}
