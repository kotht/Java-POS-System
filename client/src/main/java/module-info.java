module controllers.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens controllers.client to javafx.fxml;
    exports controllers.client;
}