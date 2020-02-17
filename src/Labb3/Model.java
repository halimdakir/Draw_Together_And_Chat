package Labb3;

import Labb3.Shape.Drawable;
import Labb3.String.StringHandle;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.util.Callback;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Model {
    final ObservableList<Drawable> shapes;
    //--------------------Network---------------------
    Socket socket;
    PrintWriter writer, writer1;
    BufferedReader reader, reader1;
    ExecutorService threadPool;
    PaintController paintController;

    public Model(){

        shapes =  FXCollections.observableArrayList(new Callback<Drawable, Observable[]>() {
                                                        @Override
                                                        public Observable[] call(Drawable param) {
                                                            return new Observable[]{
                                                                    param.xposProperty(),
                                                                    param.yposProperty(),
                                                                    param.sizeProperty(),
                                                                    param.paintFillProperty(),
                                                                    param.paintStrokeProperty()
                                                            };
                                                        }
                                                    }
        );

        threadPool = Executors.newFixedThreadPool(4);
    }

    public ObservableList<Drawable> getShapes() {
        return shapes;
    }

    public Optional<Drawable> findIntersection(double x, double y) {
        return shapes.stream().filter(s -> s.intersects(x,y)).findFirst();
    }

    //---------------------------------Chat----------------------------------
    //<editor-fold desc="chatMessages">
    private ObservableList<String> chatMessages = FXCollections.observableArrayList();
    static List<String> shapeGet = new ArrayList<>();

    public ObservableList<String> getChatMessages() {
        return chatMessages;
    }

    public static List<String> getShapeGet() {
        return shapeGet;
    }

    private SimpleBooleanProperty connected = new SimpleBooleanProperty(false);
    public SimpleBooleanProperty connectedProperty() {
        return connected;
    }
    public boolean isConnected() {
        return connected.get();
    }
    public void setConnected(boolean connected) {
        this.connected.set(connected);
    }

    StringProperty message = new SimpleStringProperty("");

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public void connect(String host, Integer port) {

        Task<Socket> task = new Task<Socket>() {
            @Override
            protected Socket call() throws Exception {
                return new Socket(host, port);
            }
        };
        task.setOnRunning(event -> chatMessages.add("is trying to Connect..."));
        task.setOnFailed(event -> chatMessages.add("Error connecting"));
        task.setOnSucceeded(event -> chatMessages.add("Connected"));

        task.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                socket = newValue;
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                setConnected(true);
                threadPool.submit(this::receiveMessages);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        threadPool.submit(task);
    }

    public void sendMessage() {
        if (message.get().length() > 0) {
            final String mess = message.get();
            threadPool.submit(() -> writer.println(mess));
            message.setValue("");
        }
    }
    public void sendShape() {
        // write  we want to send
        if (shapes.size()>0) {
            int index = shapes.size();
            threadPool.submit(() -> writer.println(shapes.get(index-1)));

        }

        //paintController.drawableList.clear();

    }

    private void receiveMessages() {
        while (true) {
            try {
                String message = reader.readLine();
                String typeOfMessage = StringHandle.checkTypeOfShape(message);
                if (typeOfMessage.equals("rect") || typeOfMessage.equals("circle")){
                    Platform.runLater(() -> shapeGet.add(message));
                }else if (typeOfMessage.equals("message")){
                    Platform.runLater(() ->
                            chatMessages.add(message)
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() ->
                        setConnected(false)
                );
                return;
            }
        }
    }

}