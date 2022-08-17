package Xbss.view;

import Xbss.TimerTask.timerTask;
import Xbss.Utils.DoubleUtil;
import Xbss.Utils.GetImage;
import Xbss.Utils.IndexCell;
import Xbss.bean.DayK;
import Xbss.bean.StockInfo;
import Xbss.bean.TableInfo;
import Xbss.data.*;
import Xbss.service.*;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.io.File;
import java.util.*;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-23:40
 * @descirbe
 */
public class MainWindow extends Application {
    public static  ObservableList<TableInfo> observableList = FXCollections.observableArrayList();
    private static ArrayList<Object> nowData =new ArrayList<Object>();
    private List<String> DayKMsg=new ArrayList<>();//这个DayKMsg用于存储更新后计算的金叉死叉和日线多空数据
    private SimpleBooleanProperty ready=new SimpleBooleanProperty(false);//程序启动时后台更新数据完成后会更改此属性为true
    private SimpleIntegerProperty deleteIndex=new SimpleIntegerProperty(-1);//这个属性和表格的selectindex属性绑定
    private Timer timer;
    @Override
    public void start(Stage primaryStage) throws Exception {
        TextField textField = new TextField();
        textField.setPromptText("双击清空内容");
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
        search.setOnAction(event -> {
            String code = textField.getText();
            nowData = GetNowData.getNowData(code);
            if (nowData.get(0).toString().contains("不存在")){
                TipDialog tipDialog = new TipDialog(nowData.get(0).toString());
                try {
                    tipDialog.start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else {
                msg.setText("名称: "+nowData.get(0)+" 现价："+nowData.get(1)+" 涨幅"+nowData.get(2));
                msg.setVisible(true);
                add.setVisible(true);
            }
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
//        ObservableList<TableInfo> observableList = FXCollections.observableArrayList();
        ObservableList<SimpleStringProperty> wu = FXCollections.observableArrayList(new SimpleStringProperty("无"),new SimpleStringProperty("无"),new SimpleStringProperty("无"));
        for (StockInfo stockInfo : QueryAllStock.queryAllStock()) {
            DayK dayK = QueryLatestDayK.queryLatestDayK(stockInfo.getCode());
//            observableList.add(new TableInfo(stockInfo.getCode(),stockInfo.getName(),new SimpleStringProperty("无"),new SimpleStringProperty("无"), new SimpleListProperty<>(wu),new SimpleStringProperty(ComputeMAMsg.computeMAMsg(dayK)),new SimpleStringProperty("无"),new SimpleStringProperty("无"),stockInfo.getBox(),dayK));
            observableList.add(new TableInfo(stockInfo.getCode(),stockInfo.getName(),new SimpleStringProperty("无"),new SimpleStringProperty("无"), new SimpleListProperty<>(wu),new SimpleStringProperty(ComputeRelateKt.computeGoldOrDie(stockInfo,dayK)),new SimpleStringProperty("无"),new SimpleStringProperty("无"),stockInfo.getBox(),dayK));
        }
        add.setOnAction(event -> {
            StockInfo info = QueryStockByCode.queryStockByCode(textField.getText());
            if (info!=null){
                TipDialog tipDialog = new TipDialog("该证券已存在！");
                try {
                    tipDialog.start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else {
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

            }
        });
        TableView<TableInfo> tableView = new TableView<>(observableList);
        tableView.getStyleClass().add("cf-table-view");
        deleteIndex.bind(tableView.getSelectionModel().selectedIndexProperty());

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
        name.setCellFactory(param -> {
            TableCell<TableInfo, String> cell = new TableCell<TableInfo,String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty){
                        this.setGraphic(new Label(item));
                        TableInfo info = (TableInfo) getTableRow().getItem();
                        if (info!=null){//这个info的判空是报错后试出来的，不知道为什么要加
                            //另外这里是初试化就直接造好了每一行的PopUpMenu，所以传索引不能传deleteIndex.get(),要直接把这个Property传过去
                            new PopUpMenu(this,info.getCode(),deleteIndex);
                        }
                    }
                    else {
                        this.setGraphic(null);
                    }
                }
            };
            return cell;
        });
        TableColumn<TableInfo, String> price = new TableColumn<>("现价");
        price.setCellValueFactory(param -> param.getValue().priceProperty());
        price.setCellFactory(param -> {
            TableCell<TableInfo, String> cell = new TableCell<TableInfo,String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty){
                        if (!item.equals("无")){
                            String[] split = item.split(",");
                            Label priceLab = new Label(split[0]);
                            Double v = Double.parseDouble(split[1]);
//                            priceLab.setBackground(new Background(new BackgroundImage(new Image("img/highlight.gif"),null,null, null,null)));
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
                    if (!empty){
                        if (!item.equals("无")){
                            Label changeLab = new Label(item);
                            Double v = Double.parseDouble(item);
//                            changeLab.setBackground(new Background(new BackgroundImage(new Image("img/highlight.gif"),null,null, null,null)));
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
                    if (!empty){
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
                    }else {
//                        this.setGraphic(new Label());
                        this.setGraphic(null);
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
                    if (!empty){
                        if (item.contains("缠绕")){
                            String[] msgs = item.split("\\|");
                            Label label = new Label();
                            Label masmg = new Label(msgs[0]);
                            HBox hBox = new HBox(masmg);
                            if (item.contains("金叉")){
                                ImageView imageView = new ImageView(new Image("img/up.png"));
                                hBox.getChildren().addAll(imageView);
                            }else if (item.contains("死叉")){
                                ImageView imageView = new ImageView(new Image("img/down.png"));
                                hBox.getChildren().addAll(imageView);
                            }
                            label.setGraphic(hBox);
                            Label tip = new Label("十日线"+msgs[1]+"二十日线");
                            tip.setStyle("-fx-text-fill:black;"+"-fx-font-size: 13");
                            Tooltip tooltip = new Tooltip();
                            tooltip.setStyle("-fx-background-color: #FFFACD");
                            tooltip.setGraphic(tip);
                            label.setTooltip(tooltip);
                            this.setGraphic(label);
                        }else {
                            Label label = new Label(item.toString());
                            if (item.contains("多"))
                                label.setStyle("-fx-text-fill: red");
                            else
                                label.setStyle("-fx-text-fill: green");
                            this.setGraphic(label);
                        }
                        this.setOnMouseClicked(event -> {
                            TableInfo tableInfo = (TableInfo) this.getTableRow().getItem();
                            Kxian kxian = new Kxian(tableInfo.getCode());
                            kxian.start(new Stage());
                        });
                    }else {
                        this.setGraphic(null);
                    }
                }
            };
            return cell;
        });
        TableColumn<TableInfo, String> priceToMA = new TableColumn<>("现价与均线情况");
        priceToMA.setCellValueFactory(param -> param.getValue().priceToMaProperty());
        priceToMA.setCellFactory(param -> {
            TableCell<TableInfo,String> cell = new TableCell<TableInfo,String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty){
                        this.setOnMouseClicked(event -> {
                            TableInfo rowData = (TableInfo) getTableRow().getItem();
                            ShowCandleChart candleChart = new ShowCandleChart(rowData.getCode());
                            candleChart.start(new Stage());
                        });
                        this.setGraphic(new Label(item));
                    }else {
                        this.setGraphic(null);
                    }
                }
            };
            return cell;
        });
        tableView.setRowFactory(new TableViewDragRows(tableView,primaryStage));
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
        Button choose = new Button("筛选");
        choose.setDisable(true);//后台数据更新未完成不能筛选
        choose.getStyleClass().addAll("cf-info-but","round");
        Button sleep = new Button("休眠");
        sleep.getStyleClass().addAll("cf-info-but","round");
        HBox bottm = new HBox(sleep,choose,update);
        bottm.setSpacing(20);
        bottm.setAlignment(Pos.BOTTOM_RIGHT);
        ready.addListener((observable, oldValue, newValue) -> {
            if (DayKMsg.size()!=0){
                ComputeRelateKt.updateObservableList(observableList,DayKMsg);
            }
            choose.setDisable(false);
        });
        //每日第一次启动程序时会在后台自动更新日线等数据
        new Thread(()->{
            //判断当日是否已经更新过了，避免重复更新、计算
            boolean b = UpdateKt.judgeTodayUpdate(observableList.get(0).getCode());
            if (!b){
                UpdateDayK.updateDayK();
                DayKMsg=ComputeRelateKt.computeGoldOrDieList();
                ready.set(true);
            }else {
                System.out.println("------------------------无需更新-----------------");
                ready.set(true);
            }
        }).start();
        VBox box = new VBox(hBox,tableView,bottm);
        box.setPadding(new Insets(20));
        box.setSpacing(20);
        Scene scene = new Scene(box);
        scene.getStylesheets().addAll("css/core.css","css/color.css");
        primaryStage.setScene(scene);
        primaryStage.setWidth(900);
        primaryStage.setHeight(700);
        primaryStage.setTitle("金融宝典 v5.0");
        primaryStage.getIcons().add(new Image("img/股票分析.png"));
        primaryStage.show();
        double colPrefWidth = DoubleUtil.subDouble(tableView.getWidth(),445.0)/3.0;
        price.setPrefWidth(colPrefWidth);
//        change.setPrefWidth(colPrefWidth);
        change.setMinWidth(colPrefWidth);
        boxArea.setPrefWidth(colPrefWidth);
        MaMsg.setPrefWidth(colPrefWidth);
//        Timer timer = new Timer();
        timer = new Timer();
//        TimerTask timerTask = new TimerTask() {//定时任务，每过5秒查询并更新数据
//            @Override
//            public void run() {
//                synchronized (primaryStage){
//                    observableList.forEach(tableInfo -> {
//                        String tableInfoCode = tableInfo.getCode();
//                        ArrayList<Object> nowData = GetNowData.getNowData(tableInfoCode);
//                        double price = Double.parseDouble(String.valueOf(nowData.get(1)));
//                        ObservableList<SimpleStringProperty> ossp = FXCollections.observableArrayList();
//                        ArrayList<String> boxArea = ComputeBoxArea.computeBoxArea(price, tableInfo.getData());
//                        boxArea.forEach(s -> ossp.add(new SimpleStringProperty(s)));
//                        SimpleListProperty<SimpleStringProperty> boxProperty = new SimpleListProperty<>(ossp);
//                        tableInfo.setPrice(String.valueOf(price)+","+String.valueOf(nowData.get(2)))
//                                .setChange(String.valueOf(nowData.get(2))).setBoxArea(boxProperty)
//                                .setPriceToMa(ComputePriceToMA.computePriceToMA(price,tableInfo.getDayK()));
//                    });
//                }
//            }
//        };
//        timer.scheduleAtFixedRate(timerTask, 0, 5000);
        timerTask timerTask = new timerTask(primaryStage, observableList);
        timer.scheduleAtFixedRate(timerTask, 0, 5000);
        ScreeningPop screeningPop = new ScreeningPop(primaryStage, timer);
        screeningPop.start(new Stage());
        choose.setOnAction(event -> {
            screeningPop.show();
        });
        sleep.setOnAction(event -> {
            if (sleep.textProperty().getValue().equals("休眠")){
                timer.cancel();
                sleep.textProperty().set("启动");
            }else {
                timer=new Timer();
                timer.scheduleAtFixedRate(new timerTask(primaryStage,observableList), 0, 5000);
                sleep.textProperty().set("休眠");
            }
        });
        primaryStage.setOnCloseRequest(event -> {
            timer.cancel();
            UpdateKt.updateIndexList(observableList);
        });
    }
    public static void main(String[] args) {
        MainWindow.launch();
    }
}
