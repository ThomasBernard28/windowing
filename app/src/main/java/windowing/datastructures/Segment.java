package windowing.datastructures;

public class Segment {

    private final CompositeNumber startComp;
    private final CompositeNumber endComp;

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
