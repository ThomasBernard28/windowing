package windowing.scenes;

import windowing.datastructures.CompositeNumber;
import windowing.datastructures.Segment;
import windowing.AppWindowing;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.ArrayList;

/**
 * This is the main scene of the application.
 * It contains buttons and layouts to display and interact with segments sets.
 */
public class SegmentsScene extends Scene {
    
    private StackPane canvas;
    private Stage stage;
    private AppWindowing app;
    private Popup popup;
    private boolean popupOnScreen = false;
    private Double startX = 0.0;
    private Double startY = 0.0;
    private Double endX = 0.0;
    private Double endY = 0.0;
    private Rectangle rectangle;
    private Group group;
    private Double zoomLevel = 1.0;
    private ArrayList<Segment> segments = new ArrayList<Segment>();
    private ArrayList<Double> window = new ArrayList<Double>();

    /**
     * The constructor initiate the basic elements of the scene.
     * It creates the references to the layouts and configure them.
     * It also add the handlers for mouse events. 
     * @param stage is the main (and only) stage of the application.
     * @param app is an instance of AppWindowing the class linking ui and datastructures.
     * @param root is the root layout of the scene. It is a VBox containing the topbar and a scrollpane.
     */
    public SegmentsScene(Stage stage, AppWindowing app, VBox root) {
        super(root, 1000, 500);
        this.canvas = new StackPane();
        this.popup = new Popup();
        this.group = new Group();
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
            if ( deltaY < 0 ) { // zoom in
                zoomLevel /= 1.5;
                show_segments(segments);
            }
            if ( deltaY > 0 ) { // zoom out
                zoomLevel *= 1.5;
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

        // popup event
        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ESCAPE)) {
                    popupOnScreen = false;
                    popup.hide();
                }
        }});

        // buttons event
        importButton.setOnAction( e -> import_popup() );
        windowButton.setOnAction( e -> window_popup() );
        clearButton.setOnAction( e -> { canvas.getChildren().clear(); window.clear(); });
        reloadButton.setOnAction( e -> { window.clear(); show_segments(app.segments); });

        // mouse event 
        group.setOnMousePressed( e -> {
            rectangle = new Rectangle();
            rectangle.setStroke(Color.BLUE);
            rectangle.setFill(Color.TRANSPARENT);  
            startX = e.getX();
            startY = e.getY();
            rectangle.setX(startX);
            rectangle.setY(startY);
            group.getChildren().add(rectangle);
        });

        group.setOnMouseDragged( e -> {
            endX = e.getX();
            endY = e.getY();
            rectangle.setX(Math.min(startX, e.getX()));
            rectangle.setY(Math.min(startY, e.getY()));
            rectangle.setWidth(Math.abs(endX - startX));
            rectangle.setHeight(Math.abs(endY - startY));
        });

        group.setOnMouseReleased( e -> {
            endX = e.getX();
            endY = e.getY();
            group.getChildren().remove(rectangle);
            window.clear();
            window.add(Math.min(startX, endX)/zoomLevel);
            window.add(Math.max(startX, endX)/zoomLevel);
            window.add(Math.min(startY, endY)/zoomLevel);
            window.add(Math.max(startY, endY)/zoomLevel);
            show_segments(app.query(this.window)); 
        });
    }
    
    /**
     * This method display the segments from the given array list.
     * Each segment is added to the group and is multiplied by zoomLevel to scale their size.
     * The method also calls draw_window and draw_grid.
     * @Param segments : Segments to be displayed
    **/
    private void show_segments(ArrayList<Segment> segments) {

        CompositeNumber startComp;
        CompositeNumber endComp;
        group.getChildren().clear();
        canvas.getChildren().clear();
        draw_grid(group);
        for ( Segment s : segments ) {
            startComp = s.get_startComp();
            endComp = s.get_endComp();

            Line l = new Line(startComp.get_coord1()*zoomLevel, startComp.get_coord2()*zoomLevel, endComp.get_coord1()*zoomLevel, endComp.get_coord2()*zoomLevel);
            l.setStrokeWidth(3.0);
            l.setStroke(Color.GREEN);
            group.getChildren().add(l);
        }
        draw_window(group);
        canvas.getChildren().add(group);
        this.segments = segments;
    }

    /**
     * This method will display the window based on the window described in the file or given by the user.
     * Each line of the window is scaled with the zoomLevel variable.
     * @param group the Group object containing all Lines to be displayed to the user inferface.
     */
    private void draw_window(Group group) {
        Double x1 = app.window.get(0);
        Double x2 = app.window.get(1);
        Double y1 = app.window.get(2);
        Double y2 = app.window.get(3);
        if (window.size() != 0) {
            x1 = window.get(0);
            x2 = window.get(1);
            y1 = window.get(2);
            y2 = window.get(3);
        }
        ArrayList<Line> lines = new ArrayList<Line>();
        lines.add(new Line(x1*zoomLevel, y1*zoomLevel, x2*zoomLevel, y1*zoomLevel));
        lines.add(new Line(x1*zoomLevel, y2*zoomLevel, x2*zoomLevel, y2*zoomLevel));
        lines.add(new Line(x1*zoomLevel, y1*zoomLevel, x1*zoomLevel, y2*zoomLevel));
        lines.add(new Line(x2*zoomLevel, y1*zoomLevel, x2*zoomLevel, y2*zoomLevel));
        lines.forEach( (l) -> { l.setStrokeWidth(3.0); l.setStroke(Color.RED); group.getChildren().add(l); });
    }

    /**
     * This method will display the grid. 
     * The width of the global window of the segemnts set is used to determine the side of each square (it is the variable step).
     * Like other lines displayed in the ui, the lines of the grid are scaled using the zoomlevel.
     * @param group the Group object containing all Lines to be displayed to the user interface.
     */
    private void draw_grid(Group group) {
        double xMin = app.window.get(0);
        double yMin = app.window.get(2);
        double xMax = app.window.get(1);
        double yMax = app.window.get(3);

        // background for mouse click detection
        Rectangle bg = new Rectangle(Math.abs(xMax-xMin)*zoomLevel, Math.abs(yMax-yMin)*zoomLevel);
        bg.setX(xMin*zoomLevel);
        bg.setY(yMin*zoomLevel);
        bg.setFill(Color.TRANSPARENT);
        group.getChildren().add(bg);

        // determine step
        double width = Math.abs(xMin) + Math.abs(xMax);
        int step = 1;
        if ( width >= 100 ) { step = 100; }
        if ( width >= 1000 ) { step = 200; }

        // drawing vertical lines
        double position = xMin;
        while ( position <= xMax) {
            Text num = new Text(position*zoomLevel, yMin*zoomLevel-10, Integer.toString((int)position));
            Line l = new Line(position*zoomLevel, yMin*zoomLevel, position*zoomLevel, yMax*zoomLevel);
            l.setStyle("-fx-opacity: 0.5;");
            group.getChildren().add(l);
            group.getChildren().add(num);
            position += step;
        }

        // drawing horizontal lines
        position = yMin;
        while ( position <= yMax ) {
            Text num = new Text(xMin*zoomLevel-30, position*zoomLevel, Integer.toString((int)position));
            Line l = new Line(xMin*zoomLevel, position*zoomLevel, xMax*zoomLevel, position*zoomLevel);
            l.setStyle("-fx-opacity: 0.5;");
            group.getChildren().add(l);
            group.getChildren().add(num);
            position += step;
        }
    }

    /**
     * Transforms a user input into an ArrayList.
     * The user input consists of an array of 4 strings describing the window.
     * This method will parse the array and transfrom every value into a double before adding them in the window arraylist.
     * For the -inf and +inf values, we transfrom them into the minimum/maximum possible value of the window.
     * @param window : array of String containing user input 
     */
    private void set_window(String[] window) {
        this.window.clear();
        for (int x=0; x<4; x++) {
            if (window[x].equals("-inf") || window[x].equals("+inf")) {
                this.window.add(app.window.get(x));  
            } 
            else {
                this.window.add(Double.parseDouble(window[x]));
            }
        }
    }

    /**
    * display a popup window asking for a segments file 
    */
    private void import_popup() {
        if ( !popupOnScreen ) {
            popupOnScreen = true;

            // popup
            popup = new Popup();
            popup.setHideOnEscape(false);

            // vbox
            VBox vb = new VBox(10);
            vb.setStyle("-fx-background-color: #457b9d; -fx-padding: 10px;");
            vb.setAlignment(Pos.CENTER);

            // hbox
            HBox hb = new HBox();
            hb.setStyle("-fx-background-color: #457b9d; -fx-padding: 0px;");

            // file chooser
            FileChooser fc = new FileChooser();

            // label
            Label l = new Label("Path :");

            // textfield
            TextField tf = new TextField(System.getProperty("user.dir")+"/build/resources/main/5000.txt");
            tf.setPrefWidth(700);

            // buttons
            Button b = new Button("import");
            b.setOnAction( e -> { app.load_segments(tf.getText()); 
                                  this.window.clear(); // reset custom window
                                  auto_zoom(Math.abs(app.window.get(3) - app.window.get(2)));
                                  show_segments(app.segments);
                                  popupOnScreen = false; 
                                  popup.hide(); 
            });

            Button fileChooserButton = new Button("...");
            fileChooserButton.setOnAction( e -> {
                File file = fc.showOpenDialog(stage);
                if (file != null) {
                    tf.setText(file.getAbsolutePath());
                    b.fire();
                }
            });

            hb.getChildren().addAll(tf, fileChooserButton);
            vb.getChildren().addAll(l, hb, b);
            popup.getContent().add(vb);
            popup.show(stage, stage.getX()+125, stage.getY()+100);
        }
    }

    /**
    * display a popup window asking for a window's size
    */
    private void window_popup() {
        if ( !popupOnScreen ) {
            popupOnScreen = true;
            
            // popup
            popup = new Popup();
            popup.setHideOnEscape(false);

            //vbox
            VBox vb = new VBox(10);
            vb.setStyle("-fx-background-color: #a8dadc; -fx-padding: 10px;");
            vb.setAlignment(Pos.CENTER);
                
            // label
            Label l = new Label("Window size :");

            // textfield
            TextField tf = new TextField("x1 x2 y1 y2");

            // button
            Button b = new Button("apply");
            b.setOnAction( e -> { 
                if (verify_window_input(tf.getText().split(" ", 0))) {
                    set_window(tf.getText().split(" ", 0));
                    show_segments(app.query(this.window)); 
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText(null);
                    alert.setContentText("The input can contain only float numbers and -inf or +inf\n The window can be :\n - bounded according to both components, meaning in the form [x:x'] x [y:y'];\n - partially bounded in the x component, meaning in the form (-∞:x'] x [y:y'] or [x:+∞) x [y:y'];\n - partially bounded in the y component, meaning in the form [x:x'] x (-∞:y'] or [x:x'] x [y:+∞).");
                    alert.showAndWait();      
                }
                popupOnScreen = false; 
                popup.hide(); 
            });

            vb.getChildren().addAll(l, tf, b);
            popup.getContent().add(vb);
            popup.show(stage, stage.getX()+400, stage.getY()+100);
        }
    }

    /**
     * This method verify if the window entered by the user is correct.
     * @param input : user inputs. It consists of an array of 4 strings.
     * @return true if the input is correct
     * @return false if the input is not correct
     */
    private boolean verify_window_input(String[] input) {
        if (input.length != 4) {
            return false;
        }
        if (input[0].equals("-inf") && input[1].equals("+inf") || input[2].equals("-inf") && input[3].equals("+inf")) {
            return false;
        }
        if (input[0].equals("-inf") && input[2].equals("-inf") || input[1].equals("+inf") && input[3].equals("+inf")) {
            return false;
        }
        for (String str : input) {
            if (!str.matches("[-+]?((\\d+\\.?\\d*)|(\\.\\d+))|([-+]?inf)")) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method calculate the zoomLevel for the segments set
     * @param size : the height of the segments set.
     */
    private void auto_zoom(Double size) {
        zoomLevel = 400.0/size; 
    }

}
