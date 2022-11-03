package Xbss.TimerTask;

import Xbss.bean.TableInfo;
import Xbss.data.ComputeBoxArea;
import Xbss.data.ComputePriceToMA;
import Xbss.data.GetNowData;
import Xbss.view.MainWindow;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Objects;
import java.util.TimerTask;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-08-16-10:26
 * @describe
 */
public class timerTask extends TimerTask {
    private final Object lock;
    private final ObservableList<TableInfo> observableList;
    private String tableInfoCode;
    private ArrayList<Object> nowData;
    private double price;
    private ObservableList<SimpleStringProperty> ossp;
    private ArrayList<String> boxArea;
    private SimpleListProperty<SimpleStringProperty> boxProperty;
    public timerTask(Object object,ObservableList<TableInfo> observableList){
        lock=object;
        this.observableList=observableList;
    }
    @Override
    public void run() {
        synchronized (lock){
//            System.out.println("更新线程名称是 "+Thread.currentThread().getName());
            observableList.forEach(tableInfo -> {
                tableInfoCode = tableInfo.getCode();
                nowData = GetNowData.getNowData(tableInfoCode);
                price = Double.parseDouble(String.valueOf(nowData.get(1)));
                ossp = FXCollections.observableArrayList();
                boxArea = ComputeBoxArea.computeBoxArea(price, tableInfo.getData());
                boxArea.forEach(s -> ossp.add(new SimpleStringProperty(s)));
                boxProperty = new SimpleListProperty<>(ossp);
                tableInfo.setPrice(String.valueOf(price)+","+String.valueOf(nowData.get(2)))
                        .setChange(String.valueOf(nowData.get(2))).setBoxArea(boxProperty)
                        .setPriceToMa(ComputePriceToMA.computePriceToMA(price,tableInfo.getDayK()));
                for (TableInfo t : MainWindow.originObservableList){
                    if (Objects.equals(t.getCode(), tableInfoCode)){
                        t.setPrice(String.valueOf(price)+","+String.valueOf(nowData.get(2)))
                                .setChange(String.valueOf(nowData.get(2))).setBoxArea(boxProperty)
                                .setPriceToMa(ComputePriceToMA.computePriceToMA(price,tableInfo.getDayK()));
                    }
                }
            });
        }
    }
}
