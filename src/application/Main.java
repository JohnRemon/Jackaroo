package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    private static Parent root;
    private static Scene scene;
    private static Stage primaryStage;
    private static FXMLLoader loader;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    public static FXMLLoader getLoader() {
        return loader;
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public static Parent getRoot() {
        return root;
    }
    public static void setRoot(Parent root) {
        Main.root = root;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        loader = new FXMLLoader(getClass().getResource("TitleScreen.fxml"));
        root = loader.load();
        scene = new Scene(root);

        URL css = getClass().getResource("application.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
        Main.primaryStage = primaryStage;

        primaryStage.setScene(scene);
        primaryStage.setTitle("Jackaroo Launcher");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
