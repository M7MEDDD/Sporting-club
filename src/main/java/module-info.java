module com.example.club_sporting_final {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires jakarta.mail;

    exports com.example.club_sporting_final.utils;
    opens com.example.club_sporting_final.utils to javafx.fxml;
    exports com.example.club_sporting_final;
    opens com.example.club_sporting_final.login.Controller to javafx.fxml;
    opens com.example.club_sporting_final.admin.Controller to javafx.fxml;

    // Open admin.module to javafx.base for runtime reflection
    opens com.example.club_sporting_final.admin.module to javafx.fxml, javafx.base;
}