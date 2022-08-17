package Xbss.Model.TableCell;

import Xbss.bean.TableInfo;
import Xbss.view.MinKWindow;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.Stage;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-08-17-10:36
 * @describe
 */
public class priceCell extends TableCell<TableInfo,String> {
    private String[] split;
    private Label priceLab;
    private Double v;
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty){
            if (!item.equals("无")){
                split = item.split(",");
                priceLab = new Label(split[0]);
                v = Double.parseDouble(split[1]);
//                priceLab.setBackground(new Background(new BackgroundImage(new Image("img/highlight.gif"),null,null, null,null)));
                if (v.compareTo(0.0)>0){
                    priceLab.setStyle("-fx-text-fill: red");
                }else if (v.compareTo(0.0)<0){
                    priceLab.setStyle("-fx-text-fill: green");
                }
                this.setOnMouseClicked(event -> {
                    TableInfo tableInfo = (TableInfo) this.getTableRow().getItem();
                    MinKWindow minK = new MinKWindow(tableInfo.getCode());
                    try {
                        minK.start(new Stage());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                this.setGraphic(priceLab);
            }
        } else {
            this.setGraphic(null);
        }
    }
}
