package Xbss.bean;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-15:06
 * @descirbe
 */
@Accessors(chain = true)
public class TableInfo {
    private String code;//股票代码
    private String name;//名称
    private SimpleStringProperty price=new SimpleStringProperty();//现价
    private SimpleStringProperty change=new SimpleStringProperty();//涨跌幅
    private SimpleListProperty<SimpleStringProperty> boxArea=new SimpleListProperty<>();//箱体区域信息
//    private ArrayList<SimpleStringProperty> boxArea=new ArrayList<>();
    private SimpleStringProperty MaMsg=new SimpleStringProperty();//均线多头缠绕空头信息
    private SimpleStringProperty priceToMa=new SimpleStringProperty();//现价与均线关系
    private SimpleStringProperty tip=new SimpleStringProperty();
    private String data;//这个data目前用于存放箱体数据信息
    private DayK dayK;
    public TableInfo(String code, String name) {
        this.code = code;
        this.name=name;
    }
    public TableInfo(String code, String name, SimpleStringProperty price, SimpleStringProperty change, SimpleListProperty<SimpleStringProperty> boxArea, SimpleStringProperty maMsg,SimpleStringProperty priceToMa, SimpleStringProperty tip,String data,DayK dayK) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.change = change;
        this.boxArea = boxArea;
        this.MaMsg = maMsg;
        this.priceToMa=priceToMa;
        this.tip = tip;
        this.data=data;
        this.dayK=dayK;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public TableInfo setPrice(String price) {
        this.price.set(price);
        return this;
    }

    public String getChange() {
        return change.get();
    }

    public SimpleStringProperty changeProperty() {
        return change;
    }

    public TableInfo setChange(String change) {
        this.change.set(change);
        return this;
    }

    public ObservableList<SimpleStringProperty> getBoxArea() {
        return boxArea.get();
    }

    public SimpleListProperty<SimpleStringProperty> boxAreaProperty() {
        return boxArea;
    }

    public TableInfo setBoxArea(ObservableList<SimpleStringProperty> boxArea) {
        this.boxArea.set(boxArea);
        return this;
    }

    public String getMaMsg() {
        return MaMsg.get();
    }

    public SimpleStringProperty maMsgProperty() {
        return MaMsg;
    }

    public void setMaMsg(String maMsg) {
        this.MaMsg.set(maMsg);
    }

    public String getPriceToMa() {
        return priceToMa.get();
    }

    public SimpleStringProperty priceToMaProperty() {
        return priceToMa;
    }

    public void setPriceToMa(String priceToMa) {
        this.priceToMa.set(priceToMa);
    }

    public String getTip() {
        return tip.get();
    }

    public SimpleStringProperty tipProperty() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip.set(tip);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DayK getDayK() {
        return dayK;
    }

    public void setDayK(DayK dayK) {
        this.dayK = dayK;
    }
}
