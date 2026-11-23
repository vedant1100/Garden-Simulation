module com.example.gardensimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gardensimulator to javafx.fxml;
    exports com.example.gardensimulator;
    exports garden.ui;
}