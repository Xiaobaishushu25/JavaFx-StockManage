package Xbss.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-06-10-11:51
 * @descirbe :调用这个类（实例化的时候需要传入一个String类型的tip）显示一个弹窗
 * param ：String tip;
 * return :一个dialog类，通过dialog.start（new Stage（））启动
 */
public class TipDialog extends Application {
    public  String tips;

    public TipDialog(String tip) {
        this.tips = tip;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Dialog<ButtonType> tip = new Dialog<>();
        tip.setTitle("提示");
        tip.setContentText(tips);
        tip.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Button close = (Button) tip.getDialogPane().lookupButton(ButtonType.CLOSE);
        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        tip.show();
    }
}
