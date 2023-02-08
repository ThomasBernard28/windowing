package windowing.scenes;

import windowing.datastructures.CompositeNumber;
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
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import java.util.ArrayList;

public class SegmentsScene extends Scene {
    
    private StackPane canvas;
    private Stage stage;
    private AppWindowing app;
    private String path = "";
    private boolean popupOnScreen = false;

    public SegmentsScene(Stage stage, AppWindowing app, VBox root) {
        super(root, 1000, 500);
        this.canvas = new StackPane();
        this.stage = stage;
        this.app = app;
        ScrollPane scrollPane = new ScrollPane();
        Button importButton = new Button("import");
        importButton.setStyle("-fx-background-color: #457b9d;");
        Button reloadButton = new Button("reload");
        reloadButton.setStyle("-fx-background-color: #f1faee;");
        Button windowButton = new Button("window");
        windowButton.setStyle("-fx-background-color: #a8dadc;");
        Button clearButton  = new Button("clear");
        clearButton.setStyle("-fx-background-color: #e63946;");
        ToolBar toolbar = new ToolBar(importButton, windowButton, reloadButton, clearButton);
        toolbar.setStyle("-fx-background-color: #1d3557;");
        
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

        CompositeNumber xComp;
        CompositeNumber yComp;
        Group group = new Group();
        for ( Segment s : segments ) {
            xComp = s.get_xComp();
            yComp = s.get_yComp();

            Line l = new Line(xComp.get_coord1()*20, yComp.get_coord1()*20, xComp.get_coord2()*20, yComp.get_coord2()*20);
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
        if ( !popupOnScreen ) {
            popupOnScreen = true;
            Popup popup = new Popup();
            popup.setHideOnEscape(false);
            VBox vb = new VBox(10);
            vb.setStyle("-fx-background-color: #457b9d; -fx-padding: 10px;");
            vb.setAlignment(Pos.CENTER);
            Label l = new Label("Path :");
            TextField tf = new TextField(System.getProperty("user.dir")+"/build/resources/main/segments2.txt");
            Button b = new Button("import");
            tf.setPrefWidth(700);
            tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ESCAPE)) {
                        popupOnScreen = false;
                        popup.hide();
                    }
            }});
            b.setOnAction( e -> { path = tf.getText(); app.load_segments(path); show_segments(app.segments); popupOnScreen = false; popup.hide(); });
            vb.getChildren().addAll(l, tf, b);
            popup.getContent().add(vb);
            popup.show(stage, stage.getX()+125, stage.getY()+100);
        }
    }

    public void window_popup() {
        /**
        * display a popup window asking for a window's size
        */
        if ( !popupOnScreen ) {
            popupOnScreen = true;
            Popup popup = new Popup();
            VBox vb = new VBox(10);
            vb.setStyle("-fx-background-color: #a8dadc; -fx-padding: 10px;");
            vb.setAlignment(Pos.CENTER);
            Label l = new Label("Window size :");
            TextField tf = new TextField("x1 y1 x2 y2");
            Button b = new Button("ok");
            tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ESCAPE)) {
                        popupOnScreen = false;
                        popup.hide();
                    }
            }});
            b.setOnAction( e -> { app.window(tf.getText().split(" ", 0)); popupOnScreen = false; popup.hide(); });
            vb.getChildren().addAll(l, tf, b);
            popup.getContent().add(vb);
            popup.show(stage, stage.getX()+400, stage.getY()+100);
        }
    }
}
