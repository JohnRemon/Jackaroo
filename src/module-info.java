module Jackaroo {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;
    requires java.desktop;
    requires javafx.media;

    opens application;
    opens application.boardView;
}