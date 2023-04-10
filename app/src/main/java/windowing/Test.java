package windowing;

import windowing.scenes.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

/**
 * This class is the main class of the application.
 * The class starts the application and creates the scene.
 */
public class Test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method is called by the JavaFX runtime when the application is launched.
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Windowing app");
        primaryStage.setResizable(false);
        SegmentsScene scene = new SegmentsScene(primaryStage, new AppWindowing(), new VBox());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
