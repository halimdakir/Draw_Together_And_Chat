package Labb3.Command;

import Labb3.Shape.Drawable;

public class ShapeSize implements Command {
    private double shapeSize, oldShapeSize;
    private Drawable shape;

    public ShapeSize(double shapeSize, Drawable shape) {
        this.shapeSize = shapeSize;
        this.shape = shape;
    }

    @Override
    public void execute() {
        oldShapeSize = shape.getSize();
        shape.setSize(shapeSize);
    }
}
