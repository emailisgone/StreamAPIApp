module edu.vu.streamapiapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;


    opens edu.vu.streamapiapp to javafx.fxml;
    exports edu.vu.streamapiapp;
}