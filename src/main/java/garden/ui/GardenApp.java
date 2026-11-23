package garden.ui;

import garden.model.Plant;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GardenApp extends Application {

    @Override
    public void start(Stage stage) {
        Plant seed = new Plant("Tomato-001");
        Label status = new Label(seed.getId() + " — " + seed.getGrowthStage());

        stage.setTitle("My Garden");
        stage.setScene(new Scene(new StackPane(status), 400, 200));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}