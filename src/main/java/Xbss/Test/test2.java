package Xbss.Test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-9:46
 * @descirbe
 */
public class test2 extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Label label = new Label("定期刷新");
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                label.setStyle("-fx-background-color: #FF000060");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                label.setStyle("-fx-background-color: #FFFAFA");
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 5000);
        VBox box = new VBox(label);
        box.setPadding(new Insets(20));
        box.setSpacing(20);
        Scene scene = new Scene(box);
        primaryStage.setScene(scene);
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setTitle("你好(500*500)");
        primaryStage.show();

    }
}
