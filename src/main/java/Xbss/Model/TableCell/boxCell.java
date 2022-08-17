package Xbss.Model.TableCell;

import Xbss.bean.TableInfo;
import Xbss.view.DayKWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-08-17-10:48
 * @describe
 */
public class boxCell extends TableCell<TableInfo, List<SimpleStringProperty>> {
    private String s;
   private Label label;
   private VBox vBox;
   private Label down;
   private Label up;
   private Label tip;
   private Tooltip tooltip = new Tooltip();
   private TableInfo tableInfo;
   private DayKWindow dayKWindow;
    @Override
    protected void updateItem(List<SimpleStringProperty> item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty){
            s = item.get(0).get();
            label = new Label(s);
            vBox = new VBox();
            if (item.size()==3){
                down = new Label(item.get(1).get());
                up = new Label(item.get(2).get());
                down.setStyle("-fx-text-fill:black;"+"-fx-font-size: 13");
                up.setStyle("-fx-text-fill: black;"+"-fx-font-size: 13");
                vBox = new VBox(down,up);
            }else {
                tip = new Label(item.get(1).get());
                tip.setStyle("-fx-text-fill:black;"+"-fx-font-size: 13");
                vBox = new VBox(tip);
            }
//            Tooltip tooltip = new Tooltip();
            tooltip.setStyle("-fx-background-color: #FFFACD");
            tooltip.setGraphic(vBox);
            label.setTooltip(tooltip);
            this.setOnMouseClicked(event -> {
                tableInfo = (TableInfo) this.getTableRow().getItem();
                dayKWindow = new DayKWindow(tableInfo.getCode(), tableInfo.getData());
                try {
                    dayKWindow.start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            this.setGraphic(label);
        }else {
            this.setGraphic(null);
        }
    }
}
