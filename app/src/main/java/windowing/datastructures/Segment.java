package windowing.datastructures;

/**
 * This class defines the Segment object.
 * A segment is defined by two CompositeNumber objects.
 * The first CompositeNumber object is the start of the segment.
 * The second CompositeNumber object is the end of the segment.
 * A composite number is represented as (x,y) in this case.
 * So the segment is represented as ((x,y);(x',y'))
 */
public class Segment {

    private final CompositeNumber startComp;
    private final CompositeNumber endComp;

    /**
     * Constructor for the Segment object.
     * @param startComp The start of the segment.
     * @param endComp The end of the segment.
     */
    public Segment(CompositeNumber startComp, CompositeNumber endComp){
        this.startComp = startComp;
        this.endComp = endComp;
    }
    public CompositeNumber get_startComp(){
        return startComp;
    }
    public CompositeNumber get_endComp(){
        return endComp;
    }
    @Override
    public String toString() {
        return " ((x,y);(x',y')) : (" + startComp.toString() + ";" +
                endComp.toString() + ")";
    }

}
