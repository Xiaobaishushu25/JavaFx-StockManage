package Xbss.view;

import Xbss.Test.test1;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-11-16:26
 * @descirbe
 */
public class Start extends Application {
    private static double width=500;
    private static double height=500;
    @Override
    public void start(Stage primaryStage) throws Exception {
        TextField textField = new TextField();
        Button button = new Button("计算");
        TextArea textArea = new TextArea();
        button.setOnAction(event -> {
            DownLoadStage downLoadStage = new DownLoadStage();
            try {
                downLoadStage.start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            String code = textField.getText();
            List list = test1.get(code);


//            list.forEach((o)->{
//                textArea.appendText(o+"\n");
//            });
        });

        VBox box = new VBox(textField,button,textArea);
        box.setPadding(new Insets(20));
        box.setSpacing(20);
        Scene scene = new Scene(box);
        primaryStage.setScene(scene);
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setTitle("你好(500*500)");
        primaryStage.show();
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            width= (double) newValue;
            primaryStage.setTitle("你好("+width+"*"+height+")");
        });
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            height= (double) newValue;
            primaryStage.setTitle("你好("+width+"*"+height+")");
        });
    }
}
