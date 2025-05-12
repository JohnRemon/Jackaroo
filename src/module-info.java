module Jackaroo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires junit;
    requires java.desktop;

    opens application;
    opens application.boardView;
}