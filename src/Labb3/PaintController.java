package Labb3;

import Labb3.Client.Client;
import Labb3.Command.UndoRedo;
import Labb3.Server.Server;
import Labb3.String.StringHandle;
import Labb3.Shape.*;
import com.sun.webkit.Timer;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;


public class PaintController implements Initializable {
    Stage primaryStage;
    private GraphicsContext gcB;
    private double x, y;
    @FXML ToggleButton  btnRectangle, btnCircle;
    @FXML private Canvas canvas;
    @FXML Button btnUndo, btnRedo, btnSave, btnOpen, btnSend, btnClient, btnServer;
    @FXML private Slider sizeSlider;
    @FXML ColorPicker strokeColor, fillColor;
    @FXML Label sliderLabel;
    @FXML TextField textField, localHost;
    @FXML ListView<String> listView;

    Model model;
    UndoRedo undoRedo;
    Thread thread;
    Client client = new Client();
    List<String> drawableList = new ArrayList<>();



    public PaintController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gcB = canvas.getGraphicsContext2D();
        model = new Model();
        undoRedo = new UndoRedo(model.getShapes());


        canvas.widthProperty().addListener(observable -> drawShapesOnCanvas());
        canvas.heightProperty().addListener(observable -> drawShapesOnCanvas());
        model.getShapes().addListener(this::changeOnListOfShapes);

        //-----------------------Chat
        textField.textProperty().bindBidirectional(model.messageProperty());
        listView.setItems(model.getChatMessages());
        //--------------------------------------------

        // Slider Size
        sizeSlider.setMin(1);
        sizeSlider.setMax(50);
        // set default values Slider & Slider's Label
        sizeSlider.setValue(25);
        sliderLabel.setText("25.0");
        sizeSlider.valueProperty().addListener(e -> {
            double width = sizeSlider.getValue();
            sliderLabel.setText(String.format("%.1f", width));
        });

        // List of ToggleButton
        ToggleButton[] toolsArr = {btnRectangle, btnCircle};
        ToggleGroup tools = new ToggleGroup();
        for (ToggleButton tool : toolsArr) {
            tool.setToggleGroup(tools);
            tool.setMinWidth(90);
            tool.setCursor(Cursor.HAND);
        }

        // set default colors
        strokeColor.setValue(Color.BLACK);
        fillColor.setValue(Color.WHITE);

        //anchorPane.setMaxHeight(800);
        //anchorPane.setMaxWidth(1150);
        canvas.setWidth(1250);
        canvas.setHeight(800);

        //Hide buttons
        Button [] basicArr = {btnUndo, btnRedo, btnSave, btnOpen};
        for(Button btn : basicArr) {
            btn.setMinWidth(90);
            btn.setCursor(Cursor.HAND);
            btn.setTextFill(Color.WHITE);
            btn.setStyle("-fx-background-color: #666;");
        }
        btnSave.setStyle("-fx-background-color: #80334d;");
        btnOpen.setStyle("-fx-background-color: #80334d;");
        btnUndo.setTextFill(Color.GRAY);
        btnRedo.setTextFill(Color.GRAY);
    }

    //----------------Draw on canvas as well as clear canvas
    private void drawShapesOnCanvas() {
        gcB.setFill(Color.WHITE);
        gcB.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //gcB.clearRect(0, 0, canvas.getHeight(), canvas.getHeight());

        for (Drawable shape : model.getShapes()) {
            shape.draw(gcB);
        }

    }
    //---------------print all changes  on list of shapes
    private void changeOnListOfShapes(ListChangeListener.Change<? extends Drawable> c) {
        while (c.next()) {
            if (c.wasPermutated()) {
                for (int i = c.getFrom(); i < c.getTo(); ++i) {
                    System.out.println("Permuted: " + i + " " + model.getShapes().get(i));
                }
            } else if (c.wasUpdated()) {
                for (int i = c.getFrom(); i < c.getTo(); ++i) {
                    System.out.println("Updated: " + i + " " + model.getShapes().get(i));
                }
            } else {
                for (Drawable removedItem : c.getRemoved()) {
                    System.out.println("Removed: " + removedItem);
                }
                for (Drawable addedItem : c.getAddedSubList()) {
                    System.out.println("Added: " + addedItem);
                }
            }
        }
        drawShapesOnCanvas();
    }

    //---------------The Class ShapeFactory replace this methode
    private Drawable shapeFactories(ShapeType type, double x, double y) {
        if (type == ShapeType.CIRCLE)
            return new Circle(x, y, sizeSlider.getValue(), strokeColor.getValue(), fillColor.getValue());
        if (type == ShapeType.RECTANGLE)
            return new Rectangle(x, y, sizeSlider.getValue(), sizeSlider.getValue(), strokeColor.getValue(), fillColor.getValue());
        return null;
    }

    //------------On mouse Clicked Secondary update shapes Primary to Create new shape as well as send that shape to Client/Server if is connected
    @FXML
    public void onMouseClicked(MouseEvent e) {
        x = e.getX();
        y = e.getY();

        if (e.getButton() == MouseButton.SECONDARY) {
            Optional<Drawable> shape = model.findIntersection(x, y);
            //shape.ifPresent(drawable -> { drawable.setPaintFill(fillColor.getValue()); drawable.setPaintStroke(strokeColor.getValue()); drawable.setSize(sizeSlider.getValue());});
            //shape.ifPresent(drawable -> undoRedo.changeCommand(shape.get(), fillColor.getValue(), strokeColor.getValue(), sizeSlider.getValue()));
            shape.ifPresent(drawable -> {undoRedo.changeFillColorCommand(shape.get(), fillColor.getValue()); undoRedo.changeStrokeColorCommand(shape.get(), strokeColor.getValue()); undoRedo.changeSizeCommand(shape.get(), sizeSlider.getValue());});
            drawShapesOnCanvas();

        } else if (e.getButton() == MouseButton.PRIMARY) {
            btnUndo.setTextFill(Color.WHITE);

            if (btnCircle.isSelected()) {
                undoRedo.insertInUnDoRedoForInsert(ShapeFactory.createShape(ShapeType.CIRCLE, e.getX(), e.getY(), sizeSlider.getValue(), strokeColor.getValue(), fillColor.getValue()));
                drawableList.add((Objects.requireNonNull(ShapeFactory.createShape(ShapeType.CIRCLE, e.getX(), e.getY(), sizeSlider.getValue(), strokeColor.getValue(), fillColor.getValue()))).toString());
                if (model.isConnected()){
                    model.sendShape();
                }
            }
            if (btnRectangle.isSelected()) {
                undoRedo.insertInUnDoRedoForInsert(ShapeFactory.createShape(ShapeType.RECTANGLE, e.getX(), e.getY(), sizeSlider.getValue(), strokeColor.getValue(), fillColor.getValue()));
                drawableList.add((Objects.requireNonNull(ShapeFactory.createShape(ShapeType.RECTANGLE, e.getX(), e.getY(), sizeSlider.getValue(), strokeColor.getValue(), fillColor.getValue()))).toString());
                if (model.isConnected()){
                    model.sendShape();
                }
            }

        }
    }
    //-------------Save and Open. file as SVG format and convert svg format to shapes
    @FXML
    public void openPic(ActionEvent actionEvent) {
        JFileChooser jf = new JFileChooser();
        FileChooser openFile = new FileChooser();
        openFile.setTitle("Open File");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("SVG fORMAT", "svg");
        jf.setFileFilter(filter);
        File file = openFile.showOpenDialog(primaryStage);
        if (file.exists())
            try (Scanner scanner = new Scanner(file)){
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    convertSvgToShape(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        drawShapesOnCanvas();
    }
    @FXML
    public void savePic(ActionEvent actionEvent) {
        FileChooser savefile = new FileChooser();
        savefile.setTitle("Save File");
        File filePath = savefile.showSaveDialog(primaryStage);

        try (FileWriter out = new FileWriter(filePath + ".svg")) {
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            out.write("<svg width=\""+canvas.getWidth()+"\" height=\""+canvas.getHeight()+"\"  xmlns=\"http://www.w3.org/2000/svg\">\n");
            for (Drawable shape : model.getShapes()) {
                out.write(shape.toString() + "\n");
            }
            out.write("</svg>");
        } catch (IOException ex) {
            System.out.println("Error!");
        }
    }
    //--------------Undo and Redo
    @FXML
    public void undo(ActionEvent a) {
                btnRedo.setTextFill(Color.WHITE);
                undoRedo.undo(1);
                a.consume();
                drawShapesOnCanvas();
    }
    @FXML
    public void redo (ActionEvent a) {
                undoRedo.redo(1);
                a.consume();
                drawShapesOnCanvas();
    }

    //-----------Connect as Client or as Server as well as Chat / draw on Client/Server's canvas as well
    @FXML
    public void sendAction(ActionEvent actionEvent) {
        if (model.isConnected())
            model.sendMessage();
    }
    @FXML
    public void connectClient(ActionEvent actionEvent){
        client.localH = localHost.getText();
        runClient();
        Optional<Pair<String, Integer>> result = showDialog();
        result.ifPresent(hostPort -> {
            //Run in threadpool?
            model.connect(hostPort.getKey(), hostPort.getValue());
        });
    }
    @FXML
    public void connectServer(ActionEvent actionEvent){
        runServer();
        Optional<Pair<String, Integer>> result = showDialog();
        result.ifPresent(hostPort -> {
            //Run in threadpool?
            model.connect(hostPort.getKey(), hostPort.getValue());
        });
    }
    private void runServer(){
        Runnable runnable = () -> {
            try {
                Server.runServer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }
    private void runClient(){
        Runnable runnable = client::runClient;
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public Optional<Pair<String, Integer>> showDialog() {
        // Create the custom dialog.
        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Connection");
        dialog.setHeaderText("Enter server IP and PORT");
        dialog.initStyle(StageStyle.UTILITY);


        ButtonType connectButtonType = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(connectButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField hostName = new TextField();
        hostName.setPromptText("hostname");
        //hostName.setText("82.196.127.10");
        hostName.setText("127.0.0.1");
        TextField port = new TextField();
        port.setPromptText("port number");
        port.setText("8000");

        grid.add(new Label("Host:"), 0, 0);
        grid.add(hostName, 1, 0);
        grid.add(new Label("Port:"), 0, 1);
        grid.add(port, 1, 1);

        Node connectButton = dialog.getDialogPane().lookupButton(connectButtonType);
        connectButton.setDisable(false);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(hostName::requestFocus);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == connectButtonType) {
                return new Pair<>(hostName.getText(), Integer.parseInt(port.getText()));
            }
            return null;
        });

        return dialog.showAndWait();
    }

    //----------on mouse released to draw coming shapes from Client/Server
    @FXML
    public void drawInClientCanvas(MouseEvent mouseEvent) {
            for (String s: Model.shapeGet) {
                convertToStringToShape(s);
            }

        Model.shapeGet.clear();
    }

    //----------- Convert Tostring to shapes & covert Color to Hexa
    public void convertToStringToShape(String message){

        List<String> stringList = new ArrayList<>();
        String[] words=message.split(" ");
        int l = words.length;
        for (int i= 0; i<l; i++){
            stringList.add(words[i]);
        }

        if( !stringList.get(0).equals("[Me]")) {

            //Type of the Shape
            String shapeTypeTemp = stringList.get(1);
            String shapeType = "";
            char[] chars = new char[shapeTypeTemp.length() - 1];
            shapeTypeTemp.getChars(1, shapeTypeTemp.length(), chars, 0);
            for (int i = 0; i < chars.length; i++) {
                shapeType += chars[i];
            }

            // IF shape is Rectangle
            if (shapeType.equals("rect")) {

                // get X of the shape
                String xTemp = stringList.get(2);
                String xfinal = "";
                char[] chars1 = new char[xTemp.length() - 3];
                xTemp.getChars(3, xTemp.length() - 1, chars1, 0);
                for (int i = 0; i < chars1.length; i++) {
                    xfinal += chars1[i];
                }
                double x = Double.parseDouble(xfinal);

                // get Y of the shape

                String yTemp = stringList.get(3);
                String yfinal = "";
                char[] chars2 = new char[yTemp.length() - 3];
                yTemp.getChars(3, yTemp.length() - 1, chars2, 0);
                for (int i = 0; i < chars2.length; i++) {
                    yfinal += chars2[i];
                }
                double y = Double.parseDouble(yfinal);

                // get Size of Rect
                String wTemp = stringList.get(4);
                String hTemp = stringList.get(5);
                String w = "";
                String h = "";
                char[] chars3 = new char[wTemp.length() - 8];
                char[] chars4 = new char[hTemp.length() - 9];
                wTemp.getChars(7, wTemp.length() - 2, chars3, 0);
                hTemp.getChars(8, hTemp.length() - 2, chars4, 0);
                for (int i = 0; i < chars3.length; i++) {
                    w += chars3[i];
                }
                for (int i = 0; i < chars4.length; i++) {
                    h += chars4[i];
                }
                double size = Double.parseDouble(w) - Double.parseDouble(h);

                // get Fill and Stroke Color
                String fillTemp = stringList.get(6);
                String strokeTemp = stringList.get(7);
                String fill = "";
                String stroke = "";
                char[] chars5 = new char[fillTemp.length() - 7];
                char[] chars6 = new char[strokeTemp.length() - 11];
                fillTemp.getChars(6, fillTemp.length() - 1, chars5, 0);
                strokeTemp.getChars(8, strokeTemp.length() - 3, chars6, 0);
                for (int i = 0; i < chars5.length; i++) {
                    fill += chars5[i];
                }
                for (int i = 0; i < chars6.length; i++) {
                    stroke += chars6[i];
                }
                Color fillColor1 = Color.valueOf(fill);
                Color strokeColor1 = Color.valueOf(stroke);
                undoRedo.insertInUnDoRedoForInsert(ShapeFactory.createShape(ShapeType.RECTANGLE, x+(size*3/2), y+size, size, strokeColor1, fillColor1));

            } else if (shapeType.equals("circle")) {

                //get X of the shape
                String xTemp = stringList.get(2);
                String xfinal = "";
                char[] chars1 = new char[xTemp.length() - 5];
                xTemp.getChars(4, xTemp.length() - 1, chars1, 0);
                for (int i = 0; i < chars1.length; i++) {
                    xfinal += chars1[i];
                }
                double x = Double.parseDouble(xfinal);

                //get Y of the shape
                String yTemp = stringList.get(3);
                String yfinal = "";
                char[] chars2 = new char[yTemp.length() - 5];
                yTemp.getChars(4, yTemp.length() - 1, chars2, 0);
                for (int i = 0; i < chars2.length; i++) {
                    yfinal += chars2[i];
                }
                double y = Double.parseDouble(yfinal);

                //get Size of Rect
                String rTemp = stringList.get(4);
                String rfinal = "";
                char[] chars4 = new char[rTemp.length() - 4];
                rTemp.getChars(3, rTemp.length() - 1, chars4, 0);
                for (int i = 0; i < chars4.length; i++) {
                    rfinal += chars4[i];
                }
                double size = Double.parseDouble(rfinal);


                //get Fill and Stroke Color
                String fillTemp = stringList.get(5);
                String strokeTemp = stringList.get(6);
                String fill = "";
                String stroke = "";
                char[] chars5 = new char[fillTemp.length() - 8];
                char[] chars6 = new char[strokeTemp.length() - 12];
                fillTemp.getChars(7, fillTemp.length() - 1, chars5, 0);
                strokeTemp.getChars(9, strokeTemp.length() - 3, chars6, 0);
                for (int i = 0; i < chars5.length; i++) {
                    fill += chars5[i];
                }
                for (int i = 0; i < chars6.length; i++) {
                    stroke += chars6[i];
                }
                Color fillColor1 = Color.valueOf(fill);
                Color strokeColor1 = Color.valueOf(stroke);
                undoRedo.insertInUnDoRedoForInsert(ShapeFactory.createShape(ShapeType.CIRCLE, x, y, size, strokeColor1, fillColor1));

            }
        }

    }
    public void convertSvgToShape(String text){
            List<String> stringList = new ArrayList<>();
            String[] words=text.split(" ");
            int l = words.length;
            for (int i= 0; i<l; i++){
                stringList.add(words[i]);
            }

            //Type of the Shape
            String shapeTypeTemp = stringList.get(0);
            String shapeType = "";
            char[] chars = new char[shapeTypeTemp.length() - 1];
            shapeTypeTemp.getChars(1, shapeTypeTemp.length(), chars, 0);
            for (int i = 0; i < chars.length; i++) {
                shapeType += chars[i];
            }

            // IF shape is Rectangle
            if (shapeType.equals("rect")) {

                // get X of the shape
                String xTemp = stringList.get(1);
                String xfinal = "";
                char[] chars1 = new char[xTemp.length() - 3];
                xTemp.getChars(3, xTemp.length() - 1, chars1, 0);
                for (int i = 0; i < chars1.length; i++) {
                    xfinal += chars1[i];
                }
                double x = Double.parseDouble(xfinal);

                // get Y of the shape

                String yTemp = stringList.get(2);
                String yfinal = "";
                char[] chars2 = new char[yTemp.length() - 3];
                yTemp.getChars(3, yTemp.length() - 1, chars2, 0);
                for (int i = 0; i < chars2.length; i++) {
                    yfinal += chars2[i];
                }
                double y = Double.parseDouble(yfinal);

                // get Size of Rect
                String wTemp = stringList.get(3);
                String hTemp = stringList.get(4);
                String w = "";
                String h = "";
                char[] chars3 = new char[wTemp.length() - 8];
                char[] chars4 = new char[hTemp.length() - 9];
                wTemp.getChars(7, wTemp.length() - 2, chars3, 0);
                hTemp.getChars(8, hTemp.length() - 2, chars4, 0);
                for (int i = 0; i < chars3.length; i++) {
                    w += chars3[i];
                }
                for (int i = 0; i < chars4.length; i++) {
                    h += chars4[i];
                }
                double size = Double.parseDouble(w) - Double.parseDouble(h);

                // get Fill and Stroke Color
                String fillTemp = stringList.get(5);
                String strokeTemp = stringList.get(6);
                String fill = "";
                String stroke = "";
                char[] chars5 = new char[fillTemp.length() - 7];
                char[] chars6 = new char[strokeTemp.length() - 11];
                fillTemp.getChars(6, fillTemp.length() - 1, chars5, 0);
                strokeTemp.getChars(8, strokeTemp.length() - 3, chars6, 0);
                for (int i = 0; i < chars5.length; i++) {
                    fill += chars5[i];
                }
                for (int i = 0; i < chars6.length; i++) {
                    stroke += chars6[i];
                }
                Color fillColor1 = Color.valueOf(fill);
                Color strokeColor1 = Color.valueOf(stroke);
                undoRedo.insertInUnDoRedoForInsert(ShapeFactory.createShape(ShapeType.RECTANGLE, x+(size*3/2), y+size, size, strokeColor1, fillColor1));

            } else if (shapeType.equals("circle")) {

                //get X of the shape
                String xTemp = stringList.get(1);
                String xfinal = "";
                char[] chars1 = new char[xTemp.length() - 5];
                xTemp.getChars(4, xTemp.length() - 1, chars1, 0);
                for (int i = 0; i < chars1.length; i++) {
                    xfinal += chars1[i];
                }
                double x = Double.parseDouble(xfinal);

                //get Y of the shape
                String yTemp = stringList.get(2);
                String yfinal = "";
                char[] chars2 = new char[yTemp.length() - 5];
                yTemp.getChars(4, yTemp.length() - 1, chars2, 0);
                for (int i = 0; i < chars2.length; i++) {
                    yfinal += chars2[i];
                }
                double y = Double.parseDouble(yfinal);

                //get Size of Rect
                String rTemp = stringList.get(3);
                String rfinal = "";
                char[] chars4 = new char[rTemp.length() - 4];
                rTemp.getChars(3, rTemp.length() - 1, chars4, 0);
                for (int i = 0; i < chars4.length; i++) {
                    rfinal += chars4[i];
                }
                double size = Double.parseDouble(rfinal);


                //get Fill and Stroke Color
                String fillTemp = stringList.get(4);
                String strokeTemp = stringList.get(5);
                String fill = "";
                String stroke = "";
                char[] chars5 = new char[fillTemp.length() - 8];
                char[] chars6 = new char[strokeTemp.length() - 12];
                fillTemp.getChars(7, fillTemp.length() - 1, chars5, 0);
                strokeTemp.getChars(9, strokeTemp.length() - 3, chars6, 0);
                for (int i = 0; i < chars5.length; i++) {
                    fill += chars5[i];
                }
                for (int i = 0; i < chars6.length; i++) {
                    stroke += chars6[i];
                }
                Color fillColor1 = Color.valueOf(fill);
                Color strokeColor1 = Color.valueOf(stroke);
                undoRedo.insertInUnDoRedoForInsert(ShapeFactory.createShape(ShapeType.CIRCLE, x, y, size, strokeColor1, fillColor1));
            }

    }
    public String toHexString(Color color) throws NullPointerException {
        String strRetour = "";
        String hexColour = color.toString();
        char[] chars = new char[6];
        hexColour.getChars(2, 8, chars, 0);
        for (int i = 0; i < chars.length; i++) {
            strRetour += chars[i];
        }
        return "#" + strRetour;
    }


}

