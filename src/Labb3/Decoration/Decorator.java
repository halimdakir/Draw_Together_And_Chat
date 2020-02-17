package Labb3.Decoration;

import Labb3.Shape.Drawable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Decorator implements Drawable {
    private final Drawable drawable;

    public Decorator(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void setXpos(double xpos) {
        drawable.setXpos(xpos);

    }

    @Override
    public void setYpos(double ypos) {
        drawable.setYpos(ypos);
    }

    @Override
    public double getXpos() {
        return drawable.getXpos();
    }

    @Override
    public double getYpos() {
        return drawable.getYpos();
    }

    @Override
    public DoubleProperty xposProperty() {
        return drawable.xposProperty();
    }

    @Override
    public DoubleProperty yposProperty() {
        return drawable.yposProperty();
    }

    @Override
    public void setSize(double size) {
        drawable.setSize(size);
    }

    @Override
    public double getSize() {
        return drawable.getSize();
    }

    @Override
    public DoubleProperty sizeProperty() {
        return drawable.sizeProperty();
    }

    @Override
    public void draw(GraphicsContext gc) {
        drawable.draw(gc);
    }

    @Override
    public Paint getPaintStroke() {
        return drawable.getPaintStroke();
    }

    @Override
    public ObjectProperty<Paint> paintStrokeProperty() {
        return drawable.paintStrokeProperty();
    }

    @Override
    public void setPaintStroke(Paint paint) {
        drawable.setPaintStroke(paint);
    }

    @Override
    public Paint getPaintFill() {
        return drawable.getPaintFill();
    }

    @Override
    public ObjectProperty<Paint> paintFillProperty() {
        return drawable.paintFillProperty();
    }

    @Override
    public void setPaintFill(Paint paint) {
        drawable.setPaintFill(paint);
    }

    @Override
    public boolean intersects(double x, double y) {
        return drawable.intersects(x,y);
    }
}
