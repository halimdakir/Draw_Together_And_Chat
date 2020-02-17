package Labb3.Command;


import Labb3.Shape.Drawable;
import javafx.scene.paint.Paint;

public class StrokeColor implements Command {
    private Paint strokeColor, oldStrokeColor;
    private Drawable shape;

    public StrokeColor(Paint strokeColor, Drawable shape) {
        this.strokeColor = strokeColor;
        this.shape = shape;
    }

    @Override
    public void execute() {
        oldStrokeColor = shape.getPaintStroke();
        shape.setPaintStroke(strokeColor);

    }
}
