package Xbss.view

import Xbss.data.delete
import Xbss.data.screening
import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Popup
import javafx.stage.Stage

/**
 * @author  Xbss
 * @create 2022-07-27-22:12
 * @version  1.0
 * @describe
 */
class ScreeningPop(val stage: Stage, private val timer: Any):Application() {
     private lateinit var popup:Popup
    override fun start(primaryStage: Stage?) {
        val buy=Button("买").apply {
            styleClass.addAll("cf-danger-but","round")
            tooltip= Tooltip("突破||下轨&&多头||缠绕&&5、10、20日线上的")
            setOnAction {
                screening(MainWindow.observableList, mutableListOf("突破","下轨"),mutableListOf("多头","缠绕"),mutableListOf("5日线上","10日线上","20日线上"),stage)
            }
        }
        val sell=Button("卖").apply {
            styleClass.addAll("cf-success-but","round")
            tooltip= Tooltip("跌破||上轨&&空头")
            setOnAction {
                screening(MainWindow.observableList, mutableListOf("跌破","上轨"),mutableListOf("空头"),mutableListOf(),stage)
            }
        }
        val hBox=HBox(buy,sell).apply {
            padding= Insets(10.0)
            spacing=10.0
        }
        val boxLabel=Label("箱体区")
        val surmount = getCheckBox("突破上轨")
        val upBox = getCheckBox("上轨区")
        val midBox = getCheckBox("中轨区")
        val downBox = getCheckBox("下轨区")
        val fall = getCheckBox("跌破下轨")
        val hhBox=HBox(surmount,upBox,midBox,downBox,fall).apply {
//            padding= Insets(20.0)
            spacing=10.0
        }
        val maLabel=Label("均线排列")
        val upMa = getCheckBox("多头")
        val bindMa = getCheckBox("震荡")
        val downMa = getCheckBox("空头")
        val hhhBox=HBox(upMa,bindMa,downMa).apply {
//            padding= Insets(20.0)
            spacing=10.0
        }
        val maToPriceLabel=Label("现价与均线情况")
        val superMa5 = getCheckBox("5日线上")
        val superMa10 = getCheckBox("10日线上")
        val superMa20 = getCheckBox("20日线上")
        val hhhhBox=HBox(superMa5,superMa10,superMa20).apply {
//            padding= Insets(20.0)
            spacing=10.0
        }
        val close = Button("关闭").apply {  styleClass.addAll("cf-but","round") }
        val resetting = Button("重置").apply{//重置要把删除的股票加入回来，并且把所有复选框更新为未选状态
            styleClass.addAll("cf-but","round")
            setOnAction {
                println("重置前删除的元素个数是： ${delete.deleteInfo.size}")
                for (tableInfo in delete.deleteInfo)
                    MainWindow.observableList.add(tableInfo)
                delete.deleteInfo.clear()
                println("重置后删除的元素个数是： ${delete.deleteInfo.size}")
                surmount.isSelected=false
                upBox.isSelected=false
                midBox.isSelected=false
                downBox.isSelected=false
                fall.isSelected=false
                upMa.isSelected=false
                bindMa.isSelected=false
                downMa.isSelected=false
                superMa5.isSelected=false
                superMa10.isSelected=false
                superMa20.isSelected=false
            }
        }
        val ok = Button("筛选").apply {
            styleClass.addAll("cf-but","round")
            setOnAction {
                val boxList = mutableListOf<String>()
                if (surmount.isSelected)
                    boxList.add("突破")
                if (upBox.isSelected)
                    boxList.add("上轨")
                if (midBox.isSelected)
                    boxList.add("中轨")
                if (downBox.isSelected)
                    boxList.add("下轨")
                if (fall.isSelected)
                    boxList.add("跌破")
                val maMsgList = mutableListOf<String>()
                if (upMa.isSelected)
                    maMsgList.add("多头")
                if (bindMa.isSelected)
                    maMsgList.add("缠绕")
                if (downMa.isSelected)
                    maMsgList.add("空头")
                val maToPriceList = mutableListOf<String>()
                if (superMa5.isSelected)
                    maToPriceList.add("5日线上")
                if (superMa10.isSelected)
                    maToPriceList.add("10日线上")
                if (superMa20.isSelected)
                    maToPriceList.add("20日线上")
                screening(MainWindow.observableList,boxList,maMsgList,maToPriceList,stage)
            }
        }
        val hhhhhBox=HBox(close,resetting,ok).apply {
//            padding= Insets(20.0)
            spacing=10.0
            alignment=Pos.BASELINE_RIGHT
        }
        val vBox = VBox(hBox,boxLabel,hhBox,maLabel,hhhBox,maToPriceLabel,hhhhBox,hhhhhBox).apply {
            padding= Insets(20.0)
            spacing=20.0
            stylesheets.addAll("css/core.css","css/color.css","css/Xbss.css")
            styleClass.addAll("Xbss-vbox")
        }
        popup = Popup().apply {
            content.addAll(vBox)
        }
        close.setOnAction {
            popup.hide()
        }
    }
    fun show(){
        popup.show(stage,150.0,300.0)
    }
}
fun getCheckBox(text:String):CheckBox{
    return CheckBox(text).apply {
        prefHeight=30.0
        skin=MyCheckBoxSkin(this)
        styleClass.addAll("Xbss-check-box")
    }
}