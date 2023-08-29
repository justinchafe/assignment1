package Part3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/** A widget which allows the user to set a value within a bar-chart style vertical bar.
 * @author Justin Chafe
 * @version 1.0
 * @since 1.0
 */
public class BarControl extends Pane {

    //Normally make these adjustable:
    private final float HEIGHT = 200;
    private final float WIDTH = 50;
    private final float INSET = 5;

    private Canvas canvasBG;
    private Canvas canvasFG;
    private GraphicsContext gcBG;
    private GraphicsContext gcFG;
    private final FloatProperty barVal = new SimpleFloatProperty();
    private final StringProperty strVal = new SimpleStringProperty();
    private float initVal;
    private float minVal;
    private float maxVal;
    StackPane stackPane;

    /** Creates a Bar Control with the specified values.
     * @param initVal The initial value of the Bar Control.
     * @param minVal The minimum value of the Bar Control.
     * @param maxVal The maximum value of the Bar Control.
     */
    public BarControl(float initVal, float minVal, float maxVal)  {
        this.initVal = initVal;
        this.minVal = minVal;
        this.maxVal = maxVal;
        init();
    }

    /** Initializes the Bar Control values and property values. Draws the initial representation of the Bar Control.
     * Adds MouseEvent handlers to Bar Control.
     */
    final void init() throws IllegalArgumentException {
        //Check our constructor values for safety:
        if (minVal < 0)
            throw new IllegalArgumentException("minVal less than zero: " + minVal);
        if (maxVal <= minVal)
            throw new IllegalArgumentException("maxVal less than or equal to minVal: " + maxVal);
        if (initVal < minVal)
            throw new IllegalArgumentException("initVal less than minVal: " + initVal);
        if (initVal > maxVal)
            throw new IllegalArgumentException("initVal greater than maxVal: " + initVal);
        
        //Initialize Property Values:
        setBarVal(initVal);
        setStrVal(String.format("%.1f",initVal));

        //Initialize Canvas:
        canvasBG = new Canvas(WIDTH, HEIGHT);
        canvasFG= new Canvas(WIDTH-(INSET*2), HEIGHT-(INSET*2));
        gcBG = canvasBG.getGraphicsContext2D();
        gcFG = canvasFG.getGraphicsContext2D();

        //Internal mouse handling:
        canvasFG.setOnMousePressed(
                event -> reDrawBar(event));
        canvasFG.setOnMouseDragged(
                event -> reDrawBar(event));
        canvasBG.setOnMousePressed(
                event -> reDrawBar(event));
        canvasBG.setOnMouseDragged(
                event -> reDrawBar(event));

        //Draw defaults:
        drawDefaults();

        //Add to StackPane and Pane:
        stackPane = new StackPane();
        stackPane.getChildren().addAll(canvasBG,canvasFG);
        this.getChildren().addAll(stackPane);  //super works, but this instance is a Pane.
    }

    /** Called from init(). Used to draw the Bar Control based on initial values.
     */
    private void drawDefaults() {
        draw(gcBG, Color.BLACK,0,0,WIDTH, HEIGHT);//draw BG
        if ((int) this.initVal == (int) this.minVal) {
            draw(gcFG, Color.YELLOW, 0, canvasFG.getHeight()-1,canvasFG.getWidth(),1);//Leave 1 px.
        }else
            draw(gcFG, Color.YELLOW,0,calcCurrPoint(getBarVal()), canvasFG.getWidth(),
                    canvasFG.getHeight()-calcCurrPoint(getBarVal())); //initValue.
    }

    /** MouseEvent Handler for Bar Control. Re-Draws the bar and sets property values accordingly.
     * Ensures 1px of bar is drawn when Bar Control is at minimum value.
     * @param event MouseEvent listening to dragging and clicking on instance(s) of Canvas.
     */
      private void reDrawBar(MouseEvent event) {
        if ((int) event.getY() >= 0 && (int) event.getY() <= canvasFG.getHeight()) {
            draw(gcFG, Color.BLACK, 0, 0, WIDTH, HEIGHT); // remove the fg - draw it BLACK.
            draw(gcFG, Color.YELLOW, 0, event.getY(), canvasFG.getWidth(),
                     (canvasFG.getHeight() - event.getY()));

            //Update Property Values:
            setBarVal(calcBarVal(((float) (canvasFG.getHeight()-event.getY()))));
            setStrVal(String.format("%.1f", getBarVal()));

            //Leave 1 px when necessary:
            if ((int) event.getY() == (int) canvasFG.getHeight())
                draw(gcFG, Color.YELLOW, 0, canvasFG.getHeight()-1,canvasFG.getWidth(),1);
        }
    }

    /**Basic drawing method.  Draws a rectangle and fills it with specified color.
     * @param gc GraphicsContext, used for drawing on a Canvas object.
     * @param backgroundColor The color of the rectangle
     * @param x Coordinate x in [x,y]
     * @param y Coordinate y in [x,y]
     * @param w Width of the rectangle
     * @param h Height of the rectangle
     */
    private void draw(GraphicsContext gc, Color backgroundColor, double x, double y, double w, double h) {
        gc.setFill(backgroundColor);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, w,h);
        gc.fillRect(x, y, w,h);
    }

    public float getInitValue() {
        return initVal;
    }

   public final float getBarVal(){
        return barVal.get();
    }

    public final void setBarVal(float value){
        barVal.set(value);
    }

    public FloatProperty getBarValProperty() {
        return barVal;
    }

    public final String getStrVal(){
        return strVal.get();
    }

    public final void setStrVal(String value){
        strVal.set(value);
    }

    public StringProperty getStrProperty() {
        return strVal;
    }

    /** Calculates the Bar Control's value given a specified Bar Control y-coordinate.
     * @param currPoint y-Coordinate of the Bar Control.
     * @return A float representing a Bar Control's value.
     */
    private float calcBarVal(float currPoint) {
        float modifier =  currPoint / (float) canvasFG.getHeight();
        return ((maxVal-minVal)*modifier)+minVal;
    }

    /** Calculates the Bar Control's y-coordinate given a specified Bar Control value.
     * @param barVal Current value of the Bar Control.
     * @return A float representing a Bar Control's y-coordinate.
     */
    private float calcCurrPoint(float barVal) {
        float modifier = (barVal - minVal) / (maxVal-minVal);
        return (float) canvasFG.getHeight() - modifier* (float) canvasFG.getHeight();
    }
}