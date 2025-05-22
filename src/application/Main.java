package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
    public static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return Main.primaryStage;
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TitleScreen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("title_screen.css").toExternalForm());
        MainMenuController controller = loader.getController();
        controller.loadValues();
        controller.initialize();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Jackaroo Launcher");
        primaryStage.setResizable(false);
        primaryStage.show();
        Main.primaryStage = primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
