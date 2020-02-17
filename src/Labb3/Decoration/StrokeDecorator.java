package Labb3.Decoration;

import Labb3.Shape.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;


public class StrokeDecorator extends Decorator {
    private Paint paintStroke;
    private double strokeSize;

    public StrokeDecorator(Drawable drawable, Paint paintStroke, double strokeSize) {
        super(drawable);
        this.paintStroke = paintStroke;
        this.strokeSize = strokeSize;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setStroke(paintStroke);
        gc.setLineWidth(strokeSize);
        super.draw(gc);
        gc.restore();
        super.draw(gc);
    }
}
