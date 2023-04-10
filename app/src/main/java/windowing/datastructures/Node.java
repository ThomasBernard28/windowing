package windowing.datastructures;


/**
 * This class defines the Node object.
 * A node is defined by a CompositeNumber object representing the first endpoint of the segment.
 * A node also has a Segment object representing the segment itself.
 * Finally, a node has a CompositeNumber object representing the median of the segment.
 * Only the point and the median are used to construct the tree.
 * The segment is used when we retrieve the segment from the tree.
 */
public class Node {

    public CompositeNumber point;
    public Segment segment;
    public CompositeNumber compositeMedian;

    /**
     * Constructor for the Node object. The median is assigned later as we build the tree.
     * @param point The first endpoint of the segment.
     * @param segment The segment itself.
     */
    public Node(CompositeNumber point, Segment segment){
        this.point = point;
        this.segment = segment;
    }

    @Override
    public String toString() {
        return point.toString();
    }

}
