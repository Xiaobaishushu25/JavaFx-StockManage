package Xbss.view;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-06-10-11:51
 * @descirbe :调用这个类（实例化的时候需要传入一个String类型的tip）显示一个弹窗
 * param ：String tip;
 * return :一个dialog类，通过dialog.start（new Stage（））启动
 */
public class MyDialog extends Application {
    public SimpleStringProperty msg=new SimpleStringProperty();
    public SimpleIntegerProperty numerator =new SimpleIntegerProperty();
    public SimpleIntegerProperty denominator=new SimpleIntegerProperty();
    public SimpleIntegerProperty progress=new SimpleIntegerProperty();

    @Override
    public void start(Stage stage) throws Exception {
        Dialog<ButtonType> tip = new Dialog<>();
        ProgressBar progressBar = new ProgressBar(100);
//        progressBar.setPrefWidth(500);
        progressBar.setMaxWidth(800);
        Label msgLabel = new Label();
        Label numLabel = new Label();
        HBox hBox = new HBox(msgLabel, numLabel);
        VBox vBox = new VBox(progressBar, hBox);
        vBox.setPrefWidth(400);
        vBox.setPrefHeight(150);
        vBox.setStyle("-fx-background-color: red");
        tip.setGraphic(vBox);
        tip.setTitle("下载");
        tip.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Button close = (Button) tip.getDialogPane().lookupButton(ButtonType.CLOSE);
        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        tip.show();
        System.out.println(vBox.getWidth());
    }
}
