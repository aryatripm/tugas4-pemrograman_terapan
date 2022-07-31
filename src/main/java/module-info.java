module com.arya.tugas4_praktikum {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires jasperreports;


    opens com.arya.tugas4_praktikum to javafx.fxml;
    exports com.arya.tugas4_praktikum;
    exports com.arya.tugas4_praktikum.controller;
    exports com.arya.tugas4_praktikum.model;
    opens com.arya.tugas4_praktikum.controller to javafx.fxml;
}