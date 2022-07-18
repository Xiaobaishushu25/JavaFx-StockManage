package Xbss.Test;

import Xbss.view.MyDialog;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-14-18:59
 * @descirbe
 */
public class test3 extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MyDialog myDialog = new MyDialog();
        myDialog.start(new Stage());
    }
}
