package Part1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = FXMLLoader.load(getClass().getResource("part1.fxml"));
        //Add layout from FXML file to scene object:
        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("Part 1 - Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
