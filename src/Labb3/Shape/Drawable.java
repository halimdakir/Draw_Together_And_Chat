package Labb3.Shape;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public interface Drawable {
    void setXpos(double xpos);
    void setYpos(double ypos);
    double getXpos();
    double getYpos();
    DoubleProperty xposProperty();
    DoubleProperty yposProperty();
    void setSize(double size);
    double getSize();
    DoubleProperty sizeProperty();
    void draw(GraphicsContext gc);
    Paint getPaintStroke();
    ObjectProperty<Paint> paintStrokeProperty();
    void setPaintStroke(Paint paint);
    Paint getPaintFill();
    ObjectProperty<Paint> paintFillProperty();
    void setPaintFill(Paint paint);
    boolean intersects(double x, double y);
}
