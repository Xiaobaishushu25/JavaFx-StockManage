package Xbss.view

import Xbss.bean.CandleChartItem
import Xbss.data.GetNowData
import Xbss.service.QueryDayKByYear
import Xbss.service.QueryStockByCode
import Xbss.service.UpdateBox
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.stage.Stage
import java.time.LocalDate
import java.util.StringJoiner

/**
 * @author  Xbss
 * @create 2022-08-14-20:04
 * @version  1.0
 * @describe :展示k线图
 */
class ShowCandleChart(val code:String) :Application(){
    override fun start(primaryStage: Stage?) {
        val stage=primaryStage!!
        println("打印当时数据")
        val nowData = GetNowData.getNowData(code).apply {
            for (s in this)
                println(s)
        }
        val queryDayKByYear = QueryDayKByYear.queryDayKByYear(code)
        val chartItems = mutableListOf<CandleChartItem>().apply {
            add(CandleChartItem(LocalDate.now().toString(),nowData[5].toString().toDouble(),nowData[4].toString().toDouble(),
                nowData[1].toString().toDouble(),nowData[6].toString().toDouble(),null,null,null,null))
            for (s in queryDayKByYear)
                add(CandleChartItem(s.date,s.low.toDouble(),s.open.toDouble(),s.close.toDouble(),s.high.toDouble(),
//                    s.MA5?.toDouble(),s.MA10?.toDouble(),s.MA20?.toDouble(),s.MA60?.toDouble()))
                    castToDouble(s.MA5),castToDouble(s.MA10),castToDouble(s.MA20),castToDouble(s.MA60)))
        }
        println("最后一个是"+chartItems[chartItems.size-1])
        val boxList= mutableListOf<Double>()
        val stockInfo = QueryStockByCode.queryStockByCode(code).apply {
            if (box.endsWith(",999")) {
                //如果箱体数据最后一个是999的话表示人工检查过了，要去掉；
                box = box.substring(0, box.length - 4)
            }
            val boxs = box.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().apply {
                for ( s in this )
                    boxList.add(s.toDouble())
            }
        }
        val candle: CandleChart= CandleChart(stockInfo.name,chartItems, boxList)
        stage.apply {
            this.scene= Scene(candle)
            title="${stockInfo.name}日线图"
            isMaximized=true
            show()
            candle.drawBoxLine()
            onCloseRequest= EventHandler {
                StringJoiner(",").apply {
                    for (s in candle.getBoxList())
                        add(s.toString())
                    UpdateBox.updateBox(code,this.toString())
                }
            }
        }
    }
    private fun castToDouble(value:String):Double?{
        return if (value == "null") null else value.toDouble()
    }
}