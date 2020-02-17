package Labb3.Shape;


import javafx.scene.paint.Paint;

public class ShapeFactory {

     public static Drawable createShape(ShapeType type, double x, double y, double size, Paint paintstroke, Paint paintFill) {
        if (type == ShapeType.CIRCLE)
            return new Circle(x, y, size, paintstroke, paintFill);
        if (type == ShapeType.RECTANGLE)
            return new Rectangle(x, y, size, size, paintstroke, paintFill);
        return null;
    }
}
