package windowing;

import windowing.scenes.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Windowing app");
        primaryStage.setResizable(false);
        PointsScene scene = new PointsScene(primaryStage, new AppWindowing(), new VBox());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
