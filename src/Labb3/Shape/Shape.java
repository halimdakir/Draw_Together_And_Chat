package Labb3.Shape;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Paint;

public abstract class Shape implements Drawable {
    private DoubleProperty xpos = new SimpleDoubleProperty();
    private DoubleProperty ypos = new SimpleDoubleProperty();
    private DoubleProperty size = new SimpleDoubleProperty();
    private ObjectProperty<Paint> paintStroke = new SimpleObjectProperty<>();
    private ObjectProperty<Paint> paintFill = new SimpleObjectProperty<>();

    public Shape(double xpos, double ypos, double size, Paint paintStroke, Paint paintFill) {
        setXpos(xpos);
        setYpos(ypos);
        setSize(size);
        this.paintFill.setValue(paintFill);
        this.paintStroke.setValue(paintStroke);
    }


    @Override
    public void setSize(double size) {
        this.size.set(size);
    }

    @Override
    public double getSize() {
        return size.get();
    }

    @Override
    public DoubleProperty sizeProperty() {
        return size;
    }

    @Override
    public void setXpos(double xpos) {
        this.xpos.set(xpos);
    }

    @Override
    public void setYpos(double ypos) {
        this.ypos.set(ypos);
    }

    @Override
    public double getXpos() {
        return xpos.get();
    }

    @Override
    public DoubleProperty xposProperty() {
        return xpos;
    }

    @Override
    public double getYpos() {
        return ypos.get();
    }

    @Override
    public DoubleProperty yposProperty() {
        return ypos;
    }

    @Override
    public Paint getPaintStroke() {
        return paintStroke.get();
    }

    @Override
    public ObjectProperty<Paint> paintStrokeProperty() {
        return paintStroke;
    }

    @Override
    public void setPaintStroke(Paint paint) {
        this.paintStroke.setValue(paint);
    }

    @Override
    public Paint getPaintFill() {
        return paintFill.get();
    }

    @Override
    public ObjectProperty<Paint> paintFillProperty() {
        return paintFill;
    }

    @Override
    public void setPaintFill(Paint paint) {
        this.paintFill.setValue(paint);
    }
}
