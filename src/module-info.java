module Jackaroo {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;
    requires javafx.swing;
    requires javafx.media;

    opens application;
    opens application.boardView;
    opens application.external;
}