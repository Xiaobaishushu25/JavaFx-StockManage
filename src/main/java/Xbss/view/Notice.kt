package Xbss.view

import Xbss.Utils.KeyUtil
import Xbss.bean.NoticeData
import Xbss.bean.TableInfo
import Xbss.service.insertNotice
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @author  Xbss
 * @create 2022-11-01-19:02
 * @version  1.0
 * @describe :创建监控界面
 */
class Notice(val tableInfo: TableInfo) {
    private val stage = Stage()
    private val select = mutableListOf<String>("突破","跌破")
    private val boxSelect = mutableListOf<String>("突破上轨区","上轨区","中轨区","下轨区","跌破下轨区")
    private val dayKSelect = mutableListOf<String>("5日线","10日线","20日线")
    init {
        val titleNow = Label(tableInfo.name).apply {
            style = "-fx-font-size:28;"+"-fx-text-fill:red"
        }
        val priceNow = getLabel(tableInfo.price.split(",")[0])
        val boxNow = getLabel(tableInfo.boxArea[0].value)
        val maNow = getLabel(tableInfo.priceToMa)
        val vbox = VBox(priceNow,boxNow,maNow).apply {
            padding = Insets(10.0)
            spacing = 10.0
        }
        val hhhhhBox= HBox(titleNow,vbox).apply {
            padding = Insets(10.0)
            spacing = 20.0
            HBox.setMargin(titleNow,Insets(30.0))
        }
        val price = getCheckBox("价格")
        val priceComboBox = getComboBox("突破", select)
        val priceTextFiled = TextField().apply {
            styleClass.addAll("cf-text-field")
            prefWidth = 100.0
            text = priceNow.text
        }
        val hBox = getHBox(price, priceComboBox,priceTextFiled)
        val box = getCheckBox("箱体")
        val boxLabel = Label("进入").apply { styleClass.addAll("Xbss-label") }
        val boxComboBox2 = getComboBox("下轨区", boxSelect)
        val hhBox = getHBox(box,boxLabel, boxComboBox2)
        val dayK = getCheckBox("日线")
        val dayKComboBox = getComboBox("突破", select)
        val dayKComboBox2 = getComboBox("20日线", dayKSelect)
        val hhhBox = getHBox(dayK, dayKComboBox, dayKComboBox2)
        val create = Button("创建").apply {
            styleClass.addAll("cf-primary-but")
            isDisable = true
            setOnAction {
                val nowDay = LocalDateTime.now().toString().replace("T"," ").dropLast(4)
                val noticeList = mutableListOf<NoticeData>()
                if (price.isSelected){
                    noticeList.add(NoticeData(
                        KeyUtil.genUniqueKey(),
                        tableInfo.code,
                        tableInfo.name,
                        priceComboBox.value,
                        priceTextFiled.text,
                        0,
                        nowDay,
                        "no",
                        priceNow.text
                    ))
                }
                if (box.isSelected){
                    noticeList.add(NoticeData(
                        KeyUtil.genUniqueKey(),
                        tableInfo.code,
                        tableInfo.name,
                        boxLabel.text,
//                        boxComboBox.value,
                        boxComboBox2.value,
                        0,
                        nowDay,
                        "no",
                        priceNow.text
                    ))
                }
                if (dayK.isSelected){
                    noticeList.add(NoticeData(
                        KeyUtil.genUniqueKey(),
                        tableInfo.code,
                        tableInfo.name,
                        dayKComboBox.value,
                        dayKComboBox2.value,
                        0,
                        nowDay,
                        "no",
                        priceNow.text
                    ))
                }
                text = "创建成功"
                isDisable = true
                val num = insertNotice(noticeList)
                for (s in noticeList)
                    NoticeStage.focusNoTriggerObs.add(0,s)
            }
            price.selectedProperty().addListener { _,_,newValue->
                if (newValue)
                    this.isDisable = false
                else if (!box.isSelected&&!dayK.isSelected)
                    this.isDisable = true
            }
            box.selectedProperty().addListener { _,_,newValue->
                if (newValue)
                    this.isDisable = false
                else if (!price.isSelected&&!dayK.isSelected)
                    this.isDisable = true
            }
            dayK.selectedProperty().addListener { _,_,newValue->
                if (newValue)
                    this.isDisable = false
                else if (!box.isSelected&&!price.isSelected)
                    this.isDisable = true
            }
        }
        val hhhhBox = getHBox(create).apply { alignment = Pos.BASELINE_RIGHT }
        val vvBox = VBox(hhhhhBox,hBox,hhBox,hhhBox,hhhhBox).apply {
            padding = Insets(20.0)
            spacing = 20.0
        }
        val scene = Scene(vvBox).apply {
            stylesheets.addAll("css/Xbss.css","css/core.css","css/color.css")
        }
        stage.apply {
            this.scene=scene
            width=500.0
            height=500.0
            title="监控${tableInfo.name}"
            show()
            isResizable = false
        }
    }
    private fun getLabel(value: String):Label{
        return Label(value).apply { styleClass.addAll("cf-primary-label") }
    }
    private fun getCheckBox(value: String): CheckBox {
        return CheckBox(value).apply { styleClass.addAll("cf-check-box") }
    }
    private fun getHBox(vararg children: Node): HBox {
        return HBox().apply {
            getChildren().addAll(children)
            padding = Insets(10.0)
            spacing = 20.0
        }
    }
    private fun getComboBox(preValue:String,list:MutableList<String>): ComboBox<String> {
        val observableList = FXCollections.observableArrayList<String>(list)
        return ComboBox(observableList).apply {
            styleClass.addAll("cf-combo-box")
            value = preValue
        }
    }
}