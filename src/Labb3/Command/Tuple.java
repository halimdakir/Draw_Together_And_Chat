package Labb3.Command;

public class Tuple {

    public Command doIt;
    public Command undoIt;
    public Command doIt1;
    public Command undoIt1;
    public Command doIt2;
    public Command undoIt2;


   public Tuple(Command doIt, Command undoIt, Command doIt1, Command undoIt1, Command doIt2, Command undoIt2) {
        this.doIt = doIt;
        this.undoIt = undoIt;
        this.doIt1 = doIt1;
        this.undoIt1 = undoIt1;
        this.doIt2 = doIt2;
        this.undoIt2 = undoIt2;
    }

    public Tuple(Command doIt, Command undoIt) {
        this.doIt = doIt;
        this.undoIt = undoIt;
    }
}
