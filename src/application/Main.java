package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TitleScreen.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        URL css = getClass().getResource("application.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle("Jackaroo");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
