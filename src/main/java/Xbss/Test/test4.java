package Xbss.Test;

import Xbss.view.DayKWindow;
import Xbss.view.MinKWindow;
import javafx.application.Application;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-15-16:03
 * @descirbe
 */
public class test4 extends Application {
    private static ImageView imageView = new ImageView();
    @Override
    public void start(Stage primaryStage) throws Exception {
//        MinKWindow minK = new MinKWindow("515030");
//        minK.start(new Stage());
        DayKWindow kWindow = new DayKWindow("515030","1.223,1.393,1.608,1.672,1.782,1.972");
        kWindow.start(new Stage());
    }
}
