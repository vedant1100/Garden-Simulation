package garden.ui;

import garden.model.Bed;
import garden.model.Garden;
import garden.model.Plant;
import garden.model.PlantProfile;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class GardenApp extends Application {

    private final Garden garden = new Garden();
    private final Bed bed = garden.addBed("A");
    private int day = 1;

    private final Label bedStatus = new Label();
    private final ListView<String> plantList = new ListView<>();

    @Override
    public void start(Stage stage) {
        ChoiceBox<PlantProfile> variety = new ChoiceBox<>();
        variety.getItems().addAll(
                PlantProfile.TOMATO,
                PlantProfile.ROSE,
                PlantProfile.BASIL,
                PlantProfile.LETTUCE
        );
        variety.setConverter(new StringConverter<>() {
            @Override
            public String toString(PlantProfile profile) {
                return profile == null ? "" : profile.name();
            }

            @Override
            public PlantProfile fromString(String name) {
                return variety.getItems().stream()
                        .filter(profile -> profile.name().equals(name))
                        .findFirst()
                        .orElse(null);
            }
        });
        variety.setValue(PlantProfile.TOMATO);

        Button sowButton = new Button("Sow one seed");
        sowButton.setOnAction(event -> {
            bed.sow(variety.getValue());
            refreshView();
        });

        Spinner<Integer> waterAmount = new Spinner<>();
        waterAmount.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 15)
        );
        waterAmount.setPrefWidth(90);

        Button waterButton = new Button("Water bed");
        waterButton.setOnAction(event -> {
            bed.water(waterAmount.getValue());
            refreshView();
        });

        Button nextDayButton = new Button("Advance one day");
        nextDayButton.setOnAction(event -> {
            garden.completeDay();
            day++;
            refreshView();
        });

        plantList.setPlaceholder(new Label("Bed A is empty. Choose a variety and sow a seed."));

        HBox plantingControls = new HBox(10, new Label("Variety"), variety, sowButton);
        HBox wateringControls = new HBox(10, new Label("Water units"), waterAmount, waterButton);

        VBox root = new VBox(
                12,
                bedStatus,
                plantingControls,
                wateringControls,
                plantList,
                nextDayButton
        );
        root.setPadding(new Insets(16));
        VBox.setVgrow(plantList, Priority.ALWAYS);

        refreshView();

        stage.setTitle("My Garden");
        stage.setScene(new Scene(root, 560, 380));
        stage.show();
    }

    private void refreshView() {
        bedStatus.setText(String.format(
                "Bed %s   |   Day %d   |   Stored water: %d units   |   Temperature: %d °F",
                bed.getId(), day, bed.getWaterUnits(), bed.getTemperatureF()
        ));

        plantList.getItems().clear();
        for (Plant plant : bed.getPlants()) {
            String condition = plant.isAlive()
                    ? plant.getGrowthStage().toString()
                    : "DEAD";

            plantList.getItems().add(String.format(
                    "%s   |   %s   |   %s   |   Health: %d/100",
                    plant.getId(), plant.getProfile().name(), condition, plant.getHealth()
            ));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}