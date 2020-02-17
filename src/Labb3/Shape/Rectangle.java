package Labb3.Shape;

import Labb3.PaintController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Rectangle extends Shape {
    private double h;
    PaintController paintController = new PaintController();

    public Rectangle(double xpos, double ypos, double h, double size, Paint paintStroke, Paint paintFill) {
        super(xpos, ypos, size, paintStroke, paintFill);
        this.h = h;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getPaintStroke());
        gc.setFill(getPaintFill());
        gc.fillRect(getXpos()-(getSize()+getSize()/2), getYpos()-getSize(), getSize()*3.0, getSize()*2.0);
        gc.strokeRect(getXpos()-(getSize()+getSize()/2), getYpos()-getSize(), getSize()*3.0, getSize()*2.0);
    }

    @Override
    public boolean intersects(double x, double y) {
        if (( x >= getXpos()-(getSize()+getSize()/2) && x <= getXpos()+(getSize()+getSize()/2)) && ( y >= getYpos()-getSize() && y <= getYpos()+getSize() ))
            return true;

        return false;

    }

   @Override
    public String toString() {
        return
                "<rect"+" "+"x=\""+ (getXpos()-getSize()*3/2)+"\""+" "+"y=\""+(getYpos()-getSize())+"\""+" "+"width=\""+getSize()*3+"\""+" "+"height=\""+getSize()*2+"\""+" "+"fill=\""+paintController.toHexString((Color) getPaintFill())+"\""+" "+"stroke=\""+paintController.toHexString((Color) getPaintStroke())+"\"/>";
    }
}

