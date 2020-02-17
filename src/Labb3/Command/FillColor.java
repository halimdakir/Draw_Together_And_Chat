package Labb3.Command;

import Labb3.Shape.Drawable;
import javafx.scene.paint.Paint;

public class FillColor implements Command {
    private Paint fillcolor, oldFillColor;
    private Drawable shape;

    public FillColor(Paint fillcolor, Drawable shape) {
        this.fillcolor = fillcolor;
        this.shape = shape;
    }


    @Override
    public void execute() {
        oldFillColor = shape.getPaintFill();
        shape.setPaintFill(fillcolor);
    }
}
