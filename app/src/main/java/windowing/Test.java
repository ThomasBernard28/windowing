package windowing;

import windowing.scenes.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

public class Test extends Application {

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
