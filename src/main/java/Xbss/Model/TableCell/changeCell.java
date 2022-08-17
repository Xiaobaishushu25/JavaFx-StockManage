package Xbss.Model.TableCell;

import Xbss.bean.TableInfo;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-08-17-10:45
 * @describe
 */
public class changeCell extends TableCell<TableInfo,String> {
    private Label changeLab;
    private Double v;
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty){
            if (!item.equals("无")){
                changeLab = new Label(item);
                v = Double.parseDouble(item);
//                changeLab.setBackground(new Background(new BackgroundImage(new Image("img/highlight.gif"),null,null, null,null)));
                if (v.compareTo(0.0)>0){
                    changeLab.setStyle("-fx-text-fill: red");
                }else if (v.compareTo(0.0)<0){
                    changeLab.setStyle("-fx-text-fill: green");
                }
                this.setGraphic(changeLab);
            }
        }else {
            this.setGraphic(null);
        }
    }
}
