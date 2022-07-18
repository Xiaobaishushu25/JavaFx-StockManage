package Xbss.view;

import Xbss.Utils.DoubleUtil;
import Xbss.Utils.IndexCell;
import Xbss.bean.DayK;
import Xbss.bean.StockInfo;
import Xbss.bean.TableInfo;
import Xbss.data.ComputeBoxArea;
import Xbss.data.ComputeMAMsg;
import Xbss.data.ComputePriceToMA;
import Xbss.data.GetNowData;
import Xbss.service.*;
import javafx.application.Application;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-23:40
 * @descirbe
 */
public class MainWindow extends Application {
    private static double width=500;
    private static double height=500;
    private static ArrayList<Object> nowData =new ArrayList<Object>();
    @Override
    public void start(Stage primaryStage) throws Exception {
        TextField textField = new TextField();
        textField.getStyleClass().add("cf-text-field");
        Button search = new Button("搜索");
        search.getStyleClass().add("cf-primary-but");
        Label msg = new Label();
        msg.setVisible(false);
        msg.getStyleClass().add("cf-warn-label");
        msg.setStyle("-fx-font-size: 20;"+"-fx-text-fill: #8B7355");
        Button add = new Button("添加");
        add.setVisible(false);
        add.getStyleClass().add("cf-warn-but");
//        ArrayList<Object> nowData = new ArrayList<Object>();
        search.setOnAction(event -> {
            String code = textField.getText();
            nowData = GetNowData.getNowData(code);
            msg.setText("名称: "+nowData.get(0)+" 现价："+nowData.get(1)+" 涨幅"+nowData.get(2));
            msg.setVisible(true);
            add.setVisible(true);
        });
        textField.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)&&event.getClickCount()==2){
                textField.setText("");
            }
        });
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")){
                msg.setText("");
                msg.setVisible(false);
                add.setVisible(false);
            }
        });
        HBox hBox = new HBox(textField, search,msg,add);
        hBox.setSpacing(30);
        hBox.setPadding(new Insets(10,0,10,20));
        ObservableList<TableInfo> observableList = FXCollections.observableArrayList();
        ObservableList<SimpleStringProperty> wu = FXCollections.observableArrayList(new SimpleStringProperty("无"),new SimpleStringProperty("无"),new SimpleStringProperty("无"));
        for (StockInfo stockInfo : QueryAllStock.queryAllStock()) {
            DayK dayK = QueryLatestDayK.queryLatestDayK(stockInfo.getCode());
            observableList.add(new TableInfo(stockInfo.getCode(),stockInfo.getName(),new SimpleStringProperty("无"),new SimpleStringProperty("无"), new SimpleListProperty<>(wu),new SimpleStringProperty(ComputeMAMsg.computeMAMsg(dayK)),new SimpleStringProperty("无"),new SimpleStringProperty("无"),stockInfo.getBox(),dayK));
        }

        add.setOnAction(event -> {
            DownLoadStage downLoadStage = new DownLoadStage();
            try {
                downLoadStage.start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            new Thread(() ->{
                AddStock.addStock(textField.getText(),nowData.get(0).toString());
                DayK dayK = QueryLatestDayK.queryLatestDayK(nowData.get(3).toString());
                StockInfo stockInfo = QueryStockByCode.queryStockByCode(nowData.get(3).toString());
                observableList.add(new TableInfo(nowData.get(3).toString(),nowData.get(0).toString(),new SimpleStringProperty("无"),new SimpleStringProperty("无"), new SimpleListProperty<>(wu),new SimpleStringProperty(ComputeMAMsg.computeMAMsg(dayK)),new SimpleStringProperty("无"),new SimpleStringProperty("无"),stockInfo.getBox(),dayK));
                }).start();
        });
        TableView<TableInfo> tableView = new TableView<>(observableList);
        tableView.getStyleClass().add("cf-table-view");
        TableColumn id = IndexCell.getIndexCell();
        TableColumn<TableInfo, String> code = new TableColumn<>("代码");
        code.setCellValueFactory(param -> {
            SimpleStringProperty simpleStringProperty = new SimpleStringProperty(param.getValue().getCode());
            return simpleStringProperty;
        });
        TableColumn<TableInfo, String> name = new TableColumn<>("名称");
        name.setCellValueFactory(param -> {
            SimpleStringProperty simpleStringProperty = new SimpleStringProperty(param.getValue().getName());
            return simpleStringProperty;
        });
        TableColumn<TableInfo, String> price = new TableColumn<>("现价");
        price.setCellValueFactory(param -> param.getValue().priceProperty());
        price.setCellFactory(param -> {
            TableCell<TableInfo, String> cell = new TableCell<TableInfo,String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty==false){
                        if (!item.equals("无")){
                            String[] split = item.split(",");
                            Label priceLab = new Label(split[0]);
                            Double v = Double.parseDouble(split[1]);
                            priceLab.setBackground(new Background(new BackgroundImage(new Image("img/highlight.gif"),null,null, null,null)));
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
                    }
                }
            };
            return cell;
        });
        TableColumn<TableInfo, String> change = new TableColumn<>("涨跌幅%");
        change.setCellValueFactory(param -> param.getValue().changeProperty());
        change.setCellFactory(param -> {
            TableCell<TableInfo, String> cell = new TableCell<TableInfo,String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty==false){
                        if (!item.equals("无")){
                            Label changeLab = new Label(item);
                            Double v = Double.parseDouble(item);
                            changeLab.setBackground(new Background(new BackgroundImage(new Image("img/highlight.gif"),null,null, null,null)));
                            if (v.compareTo(0.0)>0){
                                changeLab.setStyle("-fx-text-fill: red");
                            }else if (v.compareTo(0.0)<0){
                                changeLab.setStyle("-fx-text-fill: green");
                            }
                            this.setGraphic(changeLab);
                        }
                    }
                }
            };
            return cell;
        });
        TableColumn<TableInfo, List<SimpleStringProperty>> boxArea = new TableColumn<>("箱体");
        boxArea.setCellValueFactory(param -> {
            ObservableValue objects = param.getValue().boxAreaProperty();
            return objects;
        });
        boxArea.setCellFactory(param -> {
            TableCell<TableInfo, List<SimpleStringProperty>> cell = new TableCell<TableInfo,List<SimpleStringProperty>>(){
                @Override
                protected void updateItem(List<SimpleStringProperty> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty==false){
                        String s = item.get(0).get();
                        Label label = new Label(s);
                        VBox vBox = new VBox();
                        if (item.size()==3){
                            Label down = new Label(item.get(1).get());
                            Label up = new Label(item.get(2).get());
                            down.setStyle("-fx-text-fill:black;"+"-fx-font-size: 13");
                            up.setStyle("-fx-text-fill: black;"+"-fx-font-size: 13");
                            vBox = new VBox(down,up);
                        }else {
                            Label tip = new Label(item.get(1).get());
                            tip.setStyle("-fx-text-fill:black;"+"-fx-font-size: 13");
                            vBox = new VBox(tip);
                        }
                        Tooltip tooltip = new Tooltip();
                        tooltip.setStyle("-fx-background-color: #FFFACD");
                        tooltip.setGraphic(vBox);
                        label.setTooltip(tooltip);
                        this.setOnMouseClicked(event -> {
                            TableInfo tableInfo = (TableInfo) this.getTableRow().getItem();
                            DayKWindow dayKWindow = new DayKWindow(tableInfo.getCode(), tableInfo.getData());
                            try {
                                dayKWindow.start(new Stage());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
                        this.setGraphic(label);
                    }
                }
            };
            return cell;
        });
        TableColumn<TableInfo, String> MaMsg = new TableColumn<>("均线排列");
        MaMsg.setCellValueFactory(param -> param.getValue().maMsgProperty());
        MaMsg.setCellFactory(param -> {
            TableCell<TableInfo, String> cell = new TableCell<TableInfo, String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty==false){
                        this.setGraphic(new Label(item.toString()));
                        this.setOnMouseClicked(event -> {
                            TableInfo tableInfo = (TableInfo) this.getTableRow().getItem();
                            Kxian kxian = new Kxian(tableInfo.getCode());
                            kxian.start(new Stage());
                        });
                    }
                }
            };
            return cell;
        });
        TableColumn<TableInfo, String> priceToMA = new TableColumn<>("现价与均线情况");
        priceToMA.setCellValueFactory(param -> param.getValue().priceToMaProperty());

//        tableView.setRowFactory(param -> {
//            TableRow<TableInfo> row = new TableRow<TableInfo>(){
//                @Override
//                protected void updateItem(TableInfo item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty==false){
//                        System.out.println("在行里面打印"+item.getCode());
//                    }
//                }
//            };
//            return row;
//        });
        tableView.setPrefHeight(500.0);
        tableView.getColumns().addAll(id,code,name,price,change,boxArea,MaMsg,priceToMA);
        id.setPrefWidth(25.0);
        code.setPrefWidth(90.0);
        name.setPrefWidth(120.0);
        priceToMA.setPrefWidth(195.0);

        Button update = new Button("更新日K数据");
        update.getStyleClass().addAll("cf-warn-but",".round");
        update.setStyle("-fx-background-color: #FFA500;");
        update.setOnAction(event -> {
            DownLoadStage downLoadStage = new DownLoadStage();
            try {
                downLoadStage.start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            new Thread(()->{
                DownLoadStage.msg.set("正在更新股票日K数据");
                DownLoadStage.progress.set(DownLoadStage.proValue++);
                UpdateDayK.updateDayK();
            }).start();
        });

        HBox bottm = new HBox(update);
        bottm.setAlignment(Pos.BOTTOM_RIGHT);

        VBox box = new VBox(hBox,tableView,bottm);
        box.setPadding(new Insets(20));
        box.setSpacing(20);
        Scene scene = new Scene(box);
        scene.getStylesheets().addAll("css/core.css","css/color.css");
        primaryStage.setScene(scene);
        primaryStage.setWidth(900);
        primaryStage.setHeight(700);
        primaryStage.setTitle("金融宝典");
        primaryStage.getIcons().add(new Image("img/buy.png"));
        primaryStage.show();
        double colPrefWidth = DoubleUtil.subDouble(tableView.getWidth(),445.0)/3.0;
        price.setPrefWidth(colPrefWidth);
//        change.setPrefWidth(colPrefWidth);
        change.setMinWidth(colPrefWidth);
        boxArea.setPrefWidth(colPrefWidth);
        MaMsg.setPrefWidth(colPrefWidth);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                synchronized (observableList){
                    observableList.forEach(tableInfo -> {
                        String tableInfoCode = tableInfo.getCode();
                        ArrayList<Object> nowData = GetNowData.getNowData(tableInfoCode);
                        double price = Double.parseDouble(String.valueOf(nowData.get(1)));
                        ObservableList<SimpleStringProperty> ossp = FXCollections.observableArrayList();
                        ArrayList<String> boxArea = ComputeBoxArea.computeBoxArea(price, tableInfo.getData());
                        boxArea.forEach(s -> ossp.add(new SimpleStringProperty(s)));
                        SimpleListProperty<SimpleStringProperty> boxProperty = new SimpleListProperty<>(ossp);
                        tableInfo.setPrice(String.valueOf(price)+","+String.valueOf(nowData.get(2)))
                                .setChange(String.valueOf(nowData.get(2))).setBoxArea(boxProperty)
                                .setPriceToMa(ComputePriceToMA.computePriceToMA(price,tableInfo.getDayK()));
                    });
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 5000);
        primaryStage.setOnCloseRequest(event -> {
            timer.cancel();
        });
    }

    public static void main(String[] args) {
        MainWindow.launch();
    }
}
