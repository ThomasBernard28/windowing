package windowing;

import windowing.scenes.*;
import windowing.AppWindowing;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Windowing app");
        primaryStage.setResizable(false);
        SegmentsScene scene = new SegmentsScene(primaryStage, new AppWindowing(), new VBox());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
