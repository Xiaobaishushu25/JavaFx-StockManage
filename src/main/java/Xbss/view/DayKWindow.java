package Xbss.view;

import Xbss.data.GetKLineImg;
import Xbss.service.InsertBox;
import Xbss.service.UpdateBox;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-15-17:16
 * @descirbe
 */
public class DayKWindow extends Application {
    private  String code;
    private  String box;
    private static int width;
    private SimpleStringProperty ssp=new SimpleStringProperty();

    public DayKWindow(String code,String box) {
        this.code = code;
        this.box = box;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Slider slider = new Slider(1, 8, 6);
        width=0-(int)slider.getValue();
        slider.getStyleClass().add("cf-warn-slider");
        ImageView imageView = new ImageView(GetKLineImg.getDayKImg(code,width));
        imageView.setFitWidth(600);
        imageView.setFitHeight(400);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ssp.set(String.valueOf(newValue).substring(0, 1));
        });
        ssp.addListener((observable, oldValue, newValue) -> {
            int i=Integer.valueOf(ssp.get());
            width=0-i;
            imageView.setImage(GetKLineImg.getDayKImg(code,width));
        });
        Label label = new Label("箱体位");
        label.getStyleClass().add("cf-info-label-b");

        TextArea textArea = new TextArea();
        textArea.setPrefWidth(80.0);
        for (String s : box.split(",")) {
            textArea.appendText(s+"\n");
        }
        textArea.setEditable(false);
        textArea.setStyle("-fx-font-size: 16");
        textArea.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)&&event.getClickCount()==2){
                textArea.setEditable(true);
                slider.requestFocus();
            }
        });
        textArea.getStyleClass().add("cf-text-area");
        Button button = new Button("提交");
        button.getStyleClass().addAll("cf-success-but","round");
        button.setOnAction(event -> {
            textArea.setEditable(false);
            String text = textArea.getText();
            StringBuilder builder = new StringBuilder();
            for (String s : text.split("\n")) {
                builder.append(s+",");
            }
            builder.deleteCharAt(builder.length()-1);
            UpdateBox.updateBox(code,builder.toString());
            System.out.println(builder);
        });
        VBox vBox = new VBox(label,textArea,button);
        vBox.setPadding(new Insets(20));
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        HBox hBox = new HBox(imageView,vBox);
        hBox.setPadding(new Insets(20));
        hBox.setSpacing(20);
        VBox box = new VBox(hBox,slider);
        box.setPadding(new Insets(20));
        box.setSpacing(20);
        Scene scene = new Scene(box);
        scene.getStylesheets().addAll("css/core.css","css/color.css");
        primaryStage.setScene(scene);
        primaryStage.setWidth(830);
        primaryStage.setHeight(600);
        primaryStage.setTitle("日K线");
        primaryStage.show();
    }
}
