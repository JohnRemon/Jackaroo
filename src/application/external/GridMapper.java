package application.external;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class GridMapper extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gridMapper.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("mapperSheet.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Grid Mapper");
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    @FXML
    public void chooseTheme(MouseEvent event){

    }
}
