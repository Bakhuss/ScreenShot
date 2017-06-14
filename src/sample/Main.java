package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    static Stage primaryStage;
    static Scene mainScene;
    static int mainWidth = 370;
    static int mainHeight = 450;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("ScreenShot");
        mainScene = new Scene(root, mainWidth, mainHeight);
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public Scene getMainScene() {
        return mainScene;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setMainWidthHeight(int mainWidth, int mainHeight) {
        Main.mainWidth = mainWidth;
        Main.mainHeight = mainHeight;
    }



}
