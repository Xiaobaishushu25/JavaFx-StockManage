package Xbss.view

import Xbss.bean.NoticeData
import Xbss.bean.TableInfo
import Xbss.service.deleteNotice
import Xbss.service.queryAllNotice
import Xbss.service.updateNoticeRead
import Xbss.view.UIFun.getFocusTextFlow
import Xbss.view.UIFun.getTempTextFlow
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.geometry.Side
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

/**
 * @author  Xbss
 * @create 2022-11-01-21:20
 * @version  1.0
 * @describe :消息窗口
 */
class NoticeStage :Application(){
    //声明为全局变量是为了能在Task和主窗口使用这些数据
    companion object{
        var stage = Stage()
        //查询所有的监控数据
        var focusNotice:MutableList<NoticeData> = queryAllNotice()
        //对所有的数据分到两个列表，已触发和未触发的列表
        var focusIsTrigger:MutableList<NoticeData> = focusNotice.filter { it.isTrigger==1 } as MutableList<NoticeData>
        var focusNoTrigger:MutableList<NoticeData> = focusNotice.filter { it.isTrigger==0 } as MutableList<NoticeData>
        //根据两个列表生成两个可观察列表，用于构造listview
        var focusIsTriggerObs: ObservableList<NoticeData> = FXCollections.observableArrayList<NoticeData>(focusIsTrigger)
        var focusNoTriggerObs: ObservableList<NoticeData> = FXCollections.observableArrayList<NoticeData>(focusNoTrigger)
    }
    override fun start(primaryStage: Stage?) {
        //每次重新打开消息窗口都重新加载这些数据
        stage = primaryStage!!
        focusNotice = queryAllNotice()
        focusIsTrigger = focusNotice.filter { it.isTrigger==1 } as MutableList<NoticeData>
        focusNoTrigger = focusNotice.filter { it.isTrigger==0 } as MutableList<NoticeData>
        //根据主程序的初始数据过滤得到未关注列表
        val tableInfoList =
        MainWindow.originObservableList.filter { it.boxArea[0].value.contains("突破") || it.boxArea[0].value.contains("下轨") }
            .filter { !it.maMsg.contains("空头") }
        val tableInfoListObs = FXCollections.observableArrayList<TableInfo>(tableInfoList)
        focusIsTriggerObs = FXCollections.observableArrayList<NoticeData>(focusIsTrigger)
        focusNoTriggerObs = FXCollections.observableArrayList<NoticeData>(focusNoTrigger)
        val isTrigger = Tab("已触发").apply {
            isClosable  = false
            content = getFocusListView(focusIsTriggerObs)
        }
        val noTrigger = Tab("监控中").apply {
            isClosable  = false
            content = getFocusListView(focusNoTriggerObs)
        }
        val focusPane = TabPane(isTrigger, noTrigger).apply {
            styleClass.addAll("Xbss-tab-pane")
        }
        val all = Tab("全部").apply {
            isClosable  = false
            content = getTempListView(tableInfoListObs)
        }
        val focus = Tab("关注").apply {
            isClosable  = false
            content = focusPane
        }
        val tabPane = TabPane(focus,all).apply {
            styleClass.addAll("Xbss-tab-pane")
            side  = Side.LEFT
        }
        val vBox = VBox(tabPane).apply {
            padding= Insets(20.0)
            spacing=20.0
        }
        val scene = Scene(vBox).apply { stylesheets.addAll("css/core.css","css/color.css","css/Xbss.css") }
        stage.apply {
            this.scene = scene
            width = 780.0
            height = 780.0
            title = "消息列表"
            show()
        }
    }
    fun show(){
        stage.show()
    }
    /** 获得关注消息的 listview **/
    private fun getFocusListView(observableList:ObservableList<NoticeData>): ListView<NoticeData> {
        val listView = ListView<NoticeData>(observableList).apply {
            styleClass.addAll("Xbss-list-view")
            this.prefHeight = 780*0.8
            setCellFactory {
                val listCell =object : ListCell<NoticeData>(){
                    override fun updateItem(item: NoticeData?, empty: Boolean) {
                        super.updateItem(item, empty)
                        if (item!=null){
                            val detail = Button("删除").apply {
                                if (item.isRead==0&&item.isTrigger==1) //如果已触发且未读，则不能删除
                                    this.isDisable = true
                                setOnAction {
                                    val i = deleteNotice(item.id)
                                    if (i==1&&item.isTrigger==1)
                                        focusIsTriggerObs.remove(item)
                                    if (i==1&&item.isTrigger==0){
                                        val remove = focusNoTriggerObs.remove(item)
                                    }
                                }
                            }
                            val rec = Rectangle(20.0,20.0).apply {
                                updateRecFill(this,item)
                                setOnMouseClicked {
                                    if (item.isRead == 0&&item.isTrigger ==1){
                                        item.isRead = 1
                                        updateNoticeRead(item.id,1)
                                        updateRecFill(this,item)
                                        detail.isDisable = false
                                    }
                                }
                            }
                            val textFlow = getFocusTextFlow(item)
                            val pane = Pane()
                            val time = Label(if(item.isTrigger == 0) item.createTime else item.triggerTime).apply { style = "-fx-font-size:16" }
                            if (item.isTrigger==1){
                                val timeTooltip = Tooltip().apply { style = "-fx-background-color: #FFFACD" }
                                val create = Label(item.createTime).apply { style = "-fx-text-fill:black;" + "-fx-font-size: 16" }
                                timeTooltip.graphic = create
                                time.tooltip =timeTooltip
                            }
                            val hBox = HBox(rec,textFlow,pane,time,detail).apply {
                                if(item.condition.contains("上轨")||item.condition.contains("跌破下轨")){
                                    styleClass.addAll("Xbss-hbox-down")
                                }else if(item.condition.contains("突破上轨")||item.condition.contains("下轨")){
                                    styleClass.addAll("Xbss-hbox-up")
                                }else{
                                    styleClass.addAll(if(item.direction.contains("跌"))"Xbss-hbox-down" else "Xbss-hbox-up")
                                }
                                spacing = 10.0
                                HBox.setHgrow(pane, Priority.ALWAYS)
                            }
                            this.graphic = hBox
                        }else{
                            this.graphic = null
                        }
                    }
                }
                listCell
            }
        }
        return listView
    }
    private fun getTempListView(observableList:ObservableList<TableInfo>): ListView<TableInfo> {
        val listView = ListView<TableInfo>(observableList).apply {
            styleClass.addAll("Xbss-list-view")
            this.prefHeight = 780*0.8
            setCellFactory {
                val listCell =object : ListCell<TableInfo>(){
                    override fun updateItem(item: TableInfo?, empty: Boolean) {
                        super.updateItem(item, empty)
                        if (item!=null){
                            val rec = Rectangle(20.0,20.0).apply { fill = Paint.valueOf("#FF6A6A") }
                            val textFlow = getTempTextFlow(item)
                            val hBox = HBox(rec,textFlow).apply {
                                styleClass.addAll("Xbss-hbox-up")
                                spacing = 10.0
                            }
                            this.graphic = hBox
                        }else{
                            this.graphic = null
                        }
                    }
                }
                listCell
            }
        }
        return listView
    }
    /** 传入rec和NoticeData 来决定颜色 **/
    private fun updateRecFill(rectangle: Rectangle,item:NoticeData){
        rectangle.fill = if(item.isRead == 0&&item.isTrigger ==1 ){//已触发且未读
            Paint.valueOf("#FFFF00")
        }else{
            if(item.condition.contains("上轨")||item.condition.contains("跌破下轨")){
                Paint.valueOf("#98FB98")
            }else if(item.condition.contains("突破上轨")||item.condition.contains("下轨")){
                Paint.valueOf("#FF6A6A")
            }else{
                if(item.direction.contains("跌")) Paint.valueOf("#98FB98") else Paint.valueOf("#FF6A6A")
            }
//            if(item.direction.contains("跌")) Paint.valueOf("#98FB98") else Paint.valueOf("#FF6A6A")
        }
    }
}