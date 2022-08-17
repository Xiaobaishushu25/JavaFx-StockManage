package Xbss.view

import Xbss.Utils.DoubleUtil
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Tooltip
import java.util.StringJoiner

/**
 * @author  Xbss
 * @create 2022-07-17-14:07
 * @version  1.0
 * @descirbe
 */

fun getLineChart(type:MutableList<Int>,mapData:ArrayList<ArrayList<Pair<String,Double>>>,allMin:Double,allMax:Double,chartName:String): LineChart<String, Number> {
    val x = CategoryAxis()
    val y = NumberAxis("价格", allMin-0.1, allMax+0.1, 0.05)
    val lineChart = LineChart<String, Number>(x, y).apply {
        title="$chartName 均线图"
        createSymbols=false
        prefHeight=800.0
        for ((index,value) in type.withIndex()){
             data.add(XYChart.Series<String, Number>().apply {
                when(value){
                    5->name="MA5"
                    10->name="MA10"
                    20->name="MA20"
                }
                for (pair in mapData[index]){
                    data.add(XYChart.Data<String,Number>(pair.first,pair.second))
                }
            })
        }
        //必须在 LineChart加载data结点后才能得到data的node
        data.forEach { m-> kotlin.run {
            m.data.forEach { t->run{
                Tooltip.install(t.node,Tooltip(t.xValue + "-" + t.yValue))
        } } } }
    }
    return lineChart
}
