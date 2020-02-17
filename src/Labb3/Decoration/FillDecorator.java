/*package Labo3.Decoration;

import Labo3.Shapes.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;


public class FillDecorator extends Decorator {
    private Paint paintFill;

    public FillDecorator(Drawable drawable, Paint paintFill) {
        super(drawable);
        this.paintFill = paintFill;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.setFill(paintFill);
        super.draw(gc);
        gc.restore();
        super.draw(gc);
    }
}*/
