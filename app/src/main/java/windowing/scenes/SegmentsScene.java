package windowing.scenes;

import windowing.datastructures.Segment;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.shape.Line;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.util.ArrayList;

public class SegmentsScene extends Scene {
    
    private StackPane canvas;

    public SegmentsScene(VBox root) {
        super(root, 1000, 500);
        this.canvas = new StackPane();
        ScrollPane scrollPane = new ScrollPane();
        Button importButton = new Button("import");
        Button windowButton = new Button("window");
        ToolBar toolbar = new ToolBar(importButton, windowButton);
        scrollPane.setPrefHeight(500);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setContent(canvas);
        root.getChildren().add(toolbar);
        root.getChildren().add(scrollPane);
    }
    
    /**
     * @Param segments : Segments to be displayed
    **/
    public void show_segments(ArrayList<Segment> segments) {
        ArrayList<Double> c; 
        Group group = new Group();
        for ( Segment s : segments ) {
            c = s.get_coords();
            Line l = new Line(c.get(0)*20, c.get(1)*20, c.get(2)*20, c.get(3)*20); 
            group.getChildren().add(l);
        }
        canvas.getChildren().add(group);
        canvas.setMargin(group, new Insets(20, 20, 20, 20));
    }
}
