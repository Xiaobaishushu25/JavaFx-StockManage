package Xbss.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-14-19:17
 * @descirbe
 */
public class DownLoadStage extends Application {
    public static SimpleStringProperty msg=new SimpleStringProperty("初始化");

    public static int numValue=0;
    public static SimpleIntegerProperty numerator =new SimpleIntegerProperty(0);
//    public SimpleIntegerProperty denominator=new SimpleIntegerProperty();
    public static int  denominator=0;
    public static int proValue=0;
    public static SimpleIntegerProperty progress=new SimpleIntegerProperty(0);
    private static ProgressBar progressBar;
    private static Label msgLabel;
    private static Label numLabel;
    @Override
    public void start(Stage primaryStage) throws Exception {
        numValue=0;
        numerator.set(0);
        denominator=0;
        proValue=0;
        progress.set(0);
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);
        progressBar.setPrefHeight(30);
        progressBar.setStyle("-fx-background-color: green");
//        primaryStage.setMaxHeight(100);
        Label msgLabel = new Label("初始化...");
        Label numLabel = new Label("");
        msgLabel.setStyle("-fx-font-size: 18");
        numLabel.setStyle("-fx-font-size: 18");
        HBox hBox = new HBox(msgLabel, numLabel);
        AnchorPane anchorPane = new AnchorPane(progressBar, hBox);
        AnchorPane.setTopAnchor(progressBar,50.0);
        AnchorPane.setLeftAnchor(progressBar,40.0);
        AnchorPane.setBottomAnchor(hBox,30.0);
        AnchorPane.setLeftAnchor(hBox,40.0);
        hBox.setPadding(new Insets(20));
        hBox.setSpacing(20);
        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.setWidth(500);
        primaryStage.setHeight(250);
        primaryStage.setTitle("下载");
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.show();
        msg.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(()->{
                msgLabel.setText(msg.get());
            });
            System.out.println("-------------------------"+msg.get());
        });
        numerator.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue()!=1000){
                Platform.runLater(()->{
                    numLabel.setText(String.valueOf(numerator.get())+"/"+denominator);
                });
                System.out.println("-------------------------"+String.valueOf(numerator.get())+"/"+denominator);
            }else {
                Platform.runLater(()->{
                    numLabel.setText("");
                });
            }
        });
        progress.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(()->{
                progressBar.setProgress((double) progress.get()/10.0);
            });
            System.out.println("-------------------------"+(double) progress.get()/10.0);
        });
//        Platform.runLater(()->{
//        });
    }
}
