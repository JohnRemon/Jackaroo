module Jackaroo {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;
    requires java.desktop;
    requires javafx.media;
    requires com.google.gson;

    opens application;
    opens application.boardView;
}