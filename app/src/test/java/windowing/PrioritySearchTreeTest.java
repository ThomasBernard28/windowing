package windowing;

import windowing.datastructures.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PrioritySearchTreeTest {

    @Test
    public void construct_tree(){
        ArrayList<Segment> segments = new ArrayList<Segment>();
        ArrayList<Node> nodes = new ArrayList<Node>();
        segments.add(new Segment(new CompositeNumber(3.0, 4.0), new CompositeNumber(4.0, 4.0)));
        segments.add(new Segment(new CompositeNumber(13.0, 0.0), new CompositeNumber(13.0, -2.0)));
        segments.add(new Segment(new CompositeNumber(2.0, 7.0), new CompositeNumber(4.0, 7.0)));
        segments.add(new Segment(new CompositeNumber(-12.0, 2.0), new CompositeNumber(0.0, 2.0)));
        segments.add(new Segment(new CompositeNumber(2.0, 14.0), new CompositeNumber(3.0, 14.0)));
        segments.add(new Segment(new CompositeNumber(8.0, -3.0), new CompositeNumber(8.0, -6.0)));
        segments.add(new Segment(new CompositeNumber(11.0, 4.0), new CompositeNumber(8.0, 4.0)));
        segments.add(new Segment(new CompositeNumber(-6.0, 5.0), new CompositeNumber(-6.0, 12.0)));
        segments.add(new Segment(new CompositeNumber(7.0, 9.0), new CompositeNumber(1.0, 9.0)));
        PrioritySearchTree pst = new PrioritySearchTree(segments);

        //Check x condition.

        Assertions.assertTrue(pst.getData().point.is_x_smaller_than(pst.getLeftTree().getData().point) &&
                    pst.getData().point.is_x_smaller_than(pst.getRightTree().getData().point));
    }
}
