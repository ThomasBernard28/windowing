package windowing.scenes;

import windowing.datastructures.Segment;
import windowing.AppWindowing;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.shape.Line;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Popup;
import java.util.ArrayList;

public class SegmentsScene extends Scene {
    
    private StackPane canvas;
    private Stage stage;
    private AppWindowing app;
    private String path = "";

    public SegmentsScene(Stage stage, AppWindowing app, VBox root) {
        super(root, 1000, 500);
        this.canvas = new StackPane();
        this.stage = stage;
        this.app = app;
        ScrollPane scrollPane = new ScrollPane();
        Button importButton = new Button("import");
        Button reloadButton = new Button("reload");
        Button windowButton = new Button("window");
        Button clearButton  = new Button("clear");
        ToolBar toolbar = new ToolBar(importButton, reloadButton, windowButton, clearButton);
        
        // scrollPane config
        scrollPane.setPrefHeight(500);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setContent(canvas);

        root.getChildren().add(toolbar);
        root.getChildren().add(scrollPane);

        // buttons event
        importButton.setOnAction( e -> import_popup() );
        windowButton.setOnAction( e -> window_popup() );
        clearButton.setOnAction( e -> canvas.getChildren().clear() );
        reloadButton.setOnAction( e -> show_segments(app.segments) );

        // click event
        //canvas.setOnMousePressed( e -> System.out.println(Double.toString(e.getX()) + ", " +  Double.toString(e.getY())) );
        //canvas.setOnMouseReleased( e -> System.out.println(Double.toString(e.getX()) + ", " +  Double.toString(e.getY())) );
    }
    
    /**
     * @Param segments : Segments to be displayed
     * TODO: use first line of dataset file to print the grid
    **/
    public void show_segments(ArrayList<Segment> segments) {
        /**
        * method to display the segments from the given array list
        */

        ArrayList<Double> c; 
        Group group = new Group();
        for ( Segment s : segments ) {
            c = s.get_coords();
            Line l = new Line(c.get(0)*20, c.get(1)*20, c.get(2)*20, c.get(3)*20); 
            group.getChildren().add(l);
        }
        canvas.getChildren().clear();
        canvas.getChildren().add(group);
        canvas.setMargin(group, new Insets(20, 20, 20, 20));
    }

    public void import_popup() {
        /**
        * display a popup window asking for a segments file 
        */
        Popup popup = new Popup();
        VBox vb = new VBox(10);
        vb.setStyle("-fx-background-color: #DDF5C6;");
        Label l = new Label("path :");
        TextField tf = new TextField(System.getProperty("user.dir")+"/build/resources/main/segments1.txt");
        Button b = new Button("Import");
        b.setOnAction( e -> { path = tf.getText(); app.load_segments(path); show_segments(app.segments); popup.hide(); });
        vb.getChildren().addAll(l, tf, b);
        popup.getContent().add(vb);
        popup.show(stage);
    }

    public void window_popup() {
    }
}
