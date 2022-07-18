package Xbss.view;

import Xbss.data.GetKLineImg;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-15-16:31
 * @descirbe
 */
public class MinKWindow extends Application {
    private  String code;

    public MinKWindow(String code) {
        this.code = code;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox box = new VBox(GetKLineImg.getMinKImg(code));
        box.setPadding(new Insets(30));
        Scene scene = new Scene(box);
        primaryStage.setScene(scene);
        primaryStage.setWidth(810);
        primaryStage.setHeight(780);
        primaryStage.setTitle("分时线");
        primaryStage.show();
    }
}
