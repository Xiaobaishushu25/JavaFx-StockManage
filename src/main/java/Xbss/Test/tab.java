package Xbss.Test;

import Xbss.bean.TableInfo;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-15:10
 * @descirbe
 */
public class tab extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        TableInfo tableInfo = new TableInfo("515030","新能源车");
        VBox box = new VBox();
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
