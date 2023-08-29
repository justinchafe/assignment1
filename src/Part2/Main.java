package Part2;

import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    Button btn;
    Slider slider;
    Label label;

    //overkill:
    enum SlideDefaults {
        MIN(150.0),
        MAX(1000.0),
        DEFAULT(275.0);
        private final double slideCode;

        private SlideDefaults(double slideCode) {
            this.slideCode = slideCode;
        }
        public double value() {
            return this.slideCode;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        //Button:
        btn = new Button();
        btn.setText("Quit");
        btn.setOnAction(e -> clicked());

        //Slider:
        slider = new Slider();
        slider.setMin(SlideDefaults.MIN.value());
        slider.setMax(SlideDefaults.MAX.value());
        slider.setValue(SlideDefaults.DEFAULT.value());
        slider.setShowTickLabels(false);
        slider.setShowTickMarks(false);
        //ChangeListener<T> interface:
        slider.valueProperty().addListener((ov, oldVal, newVal) ->
            label.setText(String.format("%.1f", newVal.floatValue())));

        //Label:
        label = new Label(String.format("%.1f", slider.getValue()));
        label.setFont(new Font("Arial", 24));

        //Layout (VBOX Pane):
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(slider, label, btn);

        //Scene & Stage:
        Scene scene = new Scene(vBox, 325, 275);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Part 2 - Slider");
        primaryStage.show();
    }

    private void clicked() {
        System.out.println("Goodbye");
        Platform.exit();
        System.exit(0);
    }

    private void about() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}

