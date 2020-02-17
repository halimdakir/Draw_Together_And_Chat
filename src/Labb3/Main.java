package Labb3;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    PaintController controller;


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("painthome.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        primaryStage.setTitle("Paint & Chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args){
        launch(args);
    }
}
