package Labb3.Shape;

import Labb3.PaintController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public class Circle extends Shape {
    PaintController paintController = new PaintController();




    public Circle(double xpos, double ypos, double size, Paint paintStroke, Paint paintFill) {
        super(xpos, ypos, size, paintStroke, paintFill);
    }



    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getPaintStroke());
        gc.setFill(getPaintFill());
        gc.fillOval(getXpos() - getSize(), getYpos() - getSize(), getSize() * 2.0, getSize() * 2.0);
        gc.strokeOval(getXpos() - getSize(), getYpos() - getSize(), getSize() * 2.0, getSize() * 2.0);
    }

    @Override
    public boolean intersects(double x, double y) {
        double r = getSize();
        double x2 = (x - getXpos());
        double y2 = (y - getYpos());
        if ((x2 * x2 + y2 * y2) < r * r) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "<circle"+" "+"cx=\""+getXpos()+"\""+" "+"cy=\""+getYpos()+"\""+" "+"r=\""+getSize()+"\""+" "+"fill=\""+paintController.toHexString((Color) getPaintFill())+"\""+" "+"stroke=\""+paintController.toHexString((Color) getPaintStroke())+"\"/>";

    }
}
