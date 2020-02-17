package Labb3.Command;

import Labb3.Shape.Drawable;
import javafx.scene.paint.Paint;

import java.util.List;
import java.util.Stack;

public class UndoRedo {
    Stack<Tuple> undoCommands = new Stack<>();
    Stack<Tuple> redoCommands = new Stack<>();

    List<Drawable> shapes;

    public UndoRedo(List<Drawable> shapeList) {
        shapes = shapeList;
    }


    public void redo(int levels) {
        for (int i = 1; i <= levels; i++) {
            if (redoCommands.size() != 0) {
                Tuple command = redoCommands.pop();
                command.doIt.execute();
                undoCommands.push(command);
            }
        }
    }

    public void undo(int levels) {
        for (int i = 1; i <= levels; i++) {
            if (undoCommands.size() != 0) {
                Tuple command = undoCommands.pop();
                command.undoIt.execute();
                redoCommands.push(command);
            }
        }
    }

    public void insertInUnDoRedoForInsert(Drawable shape) {
        Tuple tuple = new Tuple(()-> shapes.add(shape), ()-> shapes.remove(shape));
        tuple.doIt.execute();
        undoCommands.push(tuple);
        redoCommands.clear();
    }

    //TODO update color stroke and size
    public void changeFillColorCommand(Drawable shape, Paint fillColor/*, Paint strokeColor, double shapeSize*/) {
        var oldColorFill = shape.getPaintFill();
        //var oldColorStroke = shape.getPaintStroke();
        //var oldShapeSize = shape.getSize();
        Tuple tuple = new Tuple(()-> shape.setPaintFill(fillColor), ()-> shape.setPaintFill(oldColorFill));
        tuple.doIt.execute();
        undoCommands.push(tuple);
        /*Tuple tuple1 = new Tuple(()-> shape.setPaintStroke(strokeColor),  ()-> shape.setPaintStroke(oldColorStroke));
        tuple1.doIt.execute();
        undoCommands.push(tuple1);
        Tuple tuple2 = new Tuple(()-> shape.setSize(shapeSize), ()-> shape.setSize(oldShapeSize));

        //Tuple tuple = new Tuple(()-> shape.setPaintFill(fillColor), ()-> shape.setPaintFill(oldColorFill),
        //()-> shape.setPaintStroke(strokeColor),  ()-> shape.setPaintStroke(oldColorStroke),
        //()-> shape.setSize(shapeSize), ()-> shape.setSize(oldShapeSize));
         tuple2.doIt.execute();
        undoCommands.push(tuple2);*/
        redoCommands.clear();
    }
    public void changeStrokeColorCommand(Drawable shape, Paint strokeColor) {
        var oldColorStroke = shape.getPaintStroke();
        Tuple tuple = new Tuple(()-> shape.setPaintStroke(strokeColor),  ()-> shape.setPaintStroke(oldColorStroke));
        tuple.doIt.execute();
        undoCommands.push(tuple);
        redoCommands.clear();
    }
    public void changeSizeCommand(Drawable shape, double shapeSize) {
        var oldShapeSize = shape.getSize();
        Tuple tuple= new Tuple(()-> shape.setSize(shapeSize), ()-> shape.setSize(oldShapeSize));
        tuple.doIt.execute();
        undoCommands.push(tuple);
        redoCommands.clear();
    }
}
