package Xbss.Model.TableCell;

import Xbss.bean.TableInfo;
import Xbss.view.ShowCandleChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-08-17-10:55
 * @describe
 */
public class priceToMACell extends TableCell<TableInfo,String> {
    private TableInfo rowData;
    private ShowCandleChart candleChart;
    private Stage stage= new Stage();
    private Label label;
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty){
            this.setOnMouseClicked(event -> {
                rowData = (TableInfo) getTableRow().getItem();
                candleChart = new ShowCandleChart(rowData.getCode());
                candleChart.start(stage);
            });
            label = new Label(item);
            this.setGraphic(label);
        }else {
            this.setGraphic(null);
        }
    }
}
