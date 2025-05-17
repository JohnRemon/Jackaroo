module Jackaroo {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;
    requires javafx.swing;

    opens application;
    opens application.boardView;
    opens application.external;
}