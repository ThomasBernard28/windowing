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
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
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
    private boolean popupOnScreen = false;
    private Double mouseX = 500.0;
    private Double mouseY = 250.0;
    private Double zoomLevel = 20.0;
    private ArrayList<Segment> segments = new ArrayList<Segment>();

    public SegmentsScene(Stage stage, AppWindowing app, VBox root) {
        super(root, 1000, 500);
        this.canvas = new StackPane();
        this.stage = stage;
        this.app = app;

        // toolbar config
        Button importButton = new Button("import");
        Button reloadButton = new Button("reload");
        Button windowButton = new Button("window");
        Button clearButton  = new Button("clear");
        ToolBar toolbar = new ToolBar(importButton, windowButton, reloadButton, clearButton);
        root.getChildren().add(toolbar);
        
        // canvas config
        canvas.setMinWidth(1000);
        canvas.setMinHeight(500);
        canvas.setAlignment(Pos.CENTER);
        canvas.setOnScroll( (ScrollEvent e) -> {
            double deltaY = e.getDeltaY();
            if ( deltaY < 0 && zoomLevel > 10 ) { // zoom in
                zoomLevel -= 1;
                show_segments(segments);
            }
            if ( deltaY > 0 && zoomLevel < 100 ) { // zoom out
                zoomLevel += 1;
                show_segments(segments);
            }
        } );

        // scrollPane config
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(500);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setContent(canvas);
        root.getChildren().add(scrollPane);

        // style
        importButton.setStyle("-fx-background-color: #457b9d;");
        reloadButton.setStyle("-fx-background-color: #f1faee;");
        windowButton.setStyle("-fx-background-color: #a8dadc;");
        clearButton.setStyle("-fx-background-color: #e63946;");
        toolbar.setStyle("-fx-background-color: #1d3557;");

        // buttons event
        importButton.setOnAction( e -> import_popup() );
        windowButton.setOnAction( e -> window_popup() );
        clearButton.setOnAction( e -> canvas.getChildren().clear() );
        reloadButton.setOnAction( e -> show_segments(app.segments));

        // mouse event
        canvas.setOnMouseDragged(e -> {
            if ( e.getX() < mouseX ) {
                scrollPane.setHvalue(scrollPane.getHvalue()-0.005);
            } 
            if ( e.getX() > mouseX ) {
                scrollPane.setHvalue(scrollPane.getHvalue()+0.005);
            }
            if ( e.getY() < mouseY ) {
                scrollPane.setVvalue(scrollPane.getVvalue()-0.005);
            }
            if ( e.getY() > mouseY ) {
                scrollPane.setVvalue(scrollPane.getVvalue()+0.005);
            }
            mouseX = e.getX();
            mouseY = e.getY();
        });

        // click event
        //canvas.setOnMousePressed( e -> System.out.println(Double.toString(e.getX()) + ", " +  Double.toString(e.getY())) );
        //canvas.setOnMouseReleased( e -> System.out.println(Double.toString(e.getX()) + ", " +  Double.toString(e.getY())) );
    }
    
    /**
     * @Param segments : Segments to be displayed
    **/
    public void show_segments(ArrayList<Segment> segments) {
        /**
        * method to display the segments from the given array list
        */

        CompositeNumber startComp;
        CompositeNumber endComp;
        Group group = new Group();
        for ( Segment s : segments ) {
            startComp = s.get_startComp();
            endComp = s.get_endComp();

            Line l = new Line(startComp.get_coord1()*zoomLevel, startComp.get_coord2()*zoomLevel, endComp.get_coord1()*zoomLevel, endComp.get_coord2()*zoomLevel);
            l.setStrokeWidth(3.0);
            l.setStroke(Color.GREEN);
            group.getChildren().add(l);
        }

        canvas.getChildren().clear();
        draw_grid();
        canvas.getChildren().add(group);
        this.segments = segments;
    }

    public void show_window(String[] window) {
    }

    public void draw_grid() {
        float xMin = app.window.get(0);
        float yMin = app.window.get(1);
        float xMax = app.window.get(2);
        float yMax = app.window.get(3);
        Group grid = new Group();
        Group text = new Group();

        // determine step
        float width = Math.abs(xMin) + Math.abs(xMax);
        int step = 1;
        if ( width >= 100 ) { step = 10; }

        // drawing vertical lines
        float position = xMin;
        while ( position <= xMax) {
            Text num = new Text(position*zoomLevel, yMin*zoomLevel-30, Integer.toString((int)position));
            Line l = new Line(position*zoomLevel, yMin*zoomLevel, position*zoomLevel, yMax*zoomLevel);
            l.setStyle("-fx-opacity: 0.5;");
            grid.getChildren().add(l);
            text.getChildren().add(num);
            position += step;
        }

        // drawing horizontal lines
        position = yMin;
        while ( position <= yMax ) {
            Text num = new Text(xMin*zoomLevel-30, position*zoomLevel, Integer.toString((int)position));
            Line l = new Line(xMin*zoomLevel, position*zoomLevel, xMax*zoomLevel, position*zoomLevel);
            l.setStyle("-fx-opacity: 0.5;");
            grid.getChildren().add(l);
            text.getChildren().add(num);
            position += step;
        }

        canvas.getChildren().add(grid);
        canvas.getChildren().add(text);
        //canvas.setMargin(grid, new Insets(20, 20, 20, 20));
    }

    public void import_popup() {
        /**
        * display a popup window asking for a segments file 
        */
        if ( !popupOnScreen ) {
            popupOnScreen = true;

            // popup
            Popup popup = new Popup();
            popup.setHideOnEscape(false);

            // vbox
            VBox vb = new VBox(10);
            vb.setStyle("-fx-background-color: #457b9d; -fx-padding: 10px;");
            vb.setAlignment(Pos.CENTER);

            // label
            Label l = new Label("Path :");

            // textfield
            TextField tf = new TextField(System.getProperty("user.dir")+"/build/resources/main/segments2.txt");
            tf.setPrefWidth(700);
            tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ESCAPE)) {
                        popupOnScreen = false;
                        popup.hide();
                    }
            }});

            // button
            Button b = new Button("import");
            b.setOnAction( e -> { app.load_segments(tf.getText()); 
                                  show_segments(app.segments);
                                  popupOnScreen = false; 
                                  popup.hide(); 
            });

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
            
            // popup
            Popup popup = new Popup();

            //vbox
            VBox vb = new VBox(10);
            vb.setStyle("-fx-background-color: #a8dadc; -fx-padding: 10px;");
            vb.setAlignment(Pos.CENTER);
                
            // label
            Label l = new Label("Window size :");

            // textfield
            TextField tf = new TextField("x1 y1 x2 y2");
            tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ESCAPE)) {
                        popupOnScreen = false;
                        popup.hide();
                    }
            }});

            // button
            Button b = new Button("apply");
            b.setOnAction( e -> { show_window(tf.getText().split(" ", 0));
                                  show_segments(app.query(tf.getText().split(" ", 0))); 
                                  popupOnScreen = false; 
                                  popup.hide(); 
            });

            vb.getChildren().addAll(l, tf, b);
            popup.getContent().add(vb);
            popup.show(stage, stage.getX()+400, stage.getY()+100);
        }
    }

}
