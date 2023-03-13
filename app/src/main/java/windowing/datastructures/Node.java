package windowing.datastructures;

public class Node {

    private Point point;
    private CompositeNumber median;

    public Node(Point point, CompositeNumber median) {
        this.point = point;
        this.median = median;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public CompositeNumber getMedian() {
        return median;
    }

    public void setMedian(CompositeNumber median) {
        this.median = median;
    }
}
