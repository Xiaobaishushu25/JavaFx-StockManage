package Xbss.service

import Xbss.bean.DayK
import Xbss.data.getAll
import Xbss.data.getKLine
import Xbss.view.getLineChart
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.chart.XYChart
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.beans.EventHandler
import kotlin.concurrent.thread

/**
 * @author  Xbss
 * @create 2022-07-17-14:49
 * @version  1.0
 * @describe
 */
class Kxian(code:String):Application() {
    var code=code
    override fun start(primaryStage: Stage) {
        val dayKS = QueryDayKByYear.queryDayKByYear(code).reversed()
        val kLine = getKLine(dayKS as ArrayList<DayK>)
        val size = dayKS.size
        val msg = getAll(code)
        var lineChart = getLineChart(mutableListOf(5,10,20), kLine, msg[0].toString().toDouble(),msg[1].toString().toDouble(),msg[2].toString())
//        val button = Button().apply{
//            setOnAction {
//                lineChart.data[0].data.clear()
//                lineChart.data[1].data.clear()
//                lineChart.data[2].data.clear()
//                var mapData=getKLine(dayKS.slice(60 until size) as ArrayList<DayK>)[0]
//                for (pairs in mapData) {
//                    lineChart.data[0].data.add(XYChart.Data<String,Number>(pairs.first,pairs.second))
//                }
//            }
//        }
        val vBox = VBox(lineChart).apply{
            padding= Insets(20.0)
            spacing=20.0
            alignment=Pos.CENTER
        }
        val scene = Scene(vBox)
        primaryStage.apply {
            this.scene=scene
            isMaximized=true
//            width=1700.0
//            height=850.0
            title="均线图"
            show()
        }
    }
}
fun main() {
    Application.launch(Kxian::class.java)
}
