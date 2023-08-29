package Part3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {


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
    public void start(Stage primaryStage)  throws Exception {

        //BarControls:
        BarControl bar1 = new BarControl(250, 100, 500);
        BarControl bar2 = new BarControl(250, 100, 500);
        BarControl bar3 = new BarControl(250, 100, 500);

        //Slider:
        Slider slider = new Slider();
        slider.setMin(Part3.Main.SlideDefaults.MIN.value());
        slider.setMax(Part3.Main.SlideDefaults.MAX.value());
        slider.setValue(Part3.Main.SlideDefaults.DEFAULT.value());
        slider.setShowTickLabels(false);
        slider.setShowTickMarks(false);

        //Labels:
        Label label1 = new Label(String.format("%.1f",bar1.getInitValue()));
        Label label2 = new Label(String.format("%.1f",bar2.getInitValue()));
        Label label3 = new Label(String.format("%.1f",bar3.getInitValue()));
        Label label4 = new Label(String.format("%.1f", slider.getValue()));

        List<Label> lList = new ArrayList<>();
        lList.add(label1);
        lList.add(label2);
        lList.add(label3);
        lList.add(label4);

        //Set - font/size:
        for(Label l : lList)
            l.setFont(new Font("Arial", 24));

        //Listener - slide:
        slider.valueProperty().addListener((ov, oldVal, newVal) ->
                label4.setText(String.format("%.1f", newVal.floatValue())));

        //Listeners - bar1, bar2:
        bar1.getBarValProperty().addListener((ov, oldVal, newVal) ->
                label1.setText(String.format("%.1f", newVal)));
        bar2.getBarValProperty().addListener((ov, oldVal, newVal) ->
                label2.setText(String.format("%.1f", newVal)));

        //Bindings - label3 to bar3:
        Bindings.bindBidirectional(label3.textProperty(), bar3.getBarValProperty(), new NumberStringConverter("##.0"));
        //Note: Added StringProperty support later in BarControl. Following binding will work but unused:
        //label3.textProperty().bind(bar3.getStrProperty());

        //Button:
        Button btn = new Button();
        btn.setText("Quit");
        btn.setOnAction(e -> clicked());

        //Layout (HBox):
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.getChildren().addAll(bar1,label1,bar2,label2,bar3,label3);

        //Layout (VBox):
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.getChildren().addAll(hBox, slider, label4, btn);

        //Scene & Stage:
        Scene scene = new Scene(vBox, 400,400 );
        primaryStage.setScene(scene);
        primaryStage.setTitle("Part 3 - Bar Control");
        primaryStage.show();
    }

    public void clicked() {
        System.out.println("Goodbye");
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
