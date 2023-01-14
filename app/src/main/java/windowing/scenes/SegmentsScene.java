package windowing.scenes;

import windowing.datastructures.Segment;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.control.*;
import javafx.scene.shape.Line;
import java.util.ArrayList;

public class SegmentsScene extends Scene {
    
    private StackPane root;

    public SegmentsScene(StackPane root) {
        super(root, 1000, 500);
        this.root = root;
    }
    
    /**
     * @Param segments : Segments to be displayed
    **/
    public void show_segments(ArrayList<Segment> segments) {
        ArrayList<Double> c; 
        Group group = new Group();
        for ( Segment s : segments ) {
            c = s.get_coords();
            Line l = new Line(c.get(0), c.get(1), c.get(2), c.get(3)); 
            group.getChildren().add(l);
        }
        root.getChildren().add(group);
    }
}
