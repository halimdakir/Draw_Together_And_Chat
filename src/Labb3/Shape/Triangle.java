package Labb3.Shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Triangle extends Shape {

    public Triangle(double xpos, double ypos, double size, Paint paintStroke, Paint paintFill) {
        super(xpos, ypos, size, paintStroke, paintFill);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getPaintStroke());
        gc.setFill(getPaintFill());
        gc.fillPolygon(new double[]{getXpos(), getYpos()},new double[]{getXpos(), getYpos()},(int) getSize());

    }

    @Override
    public boolean intersects(double x, double y) {
        return false;
    }
}
