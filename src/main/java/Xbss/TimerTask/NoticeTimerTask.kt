package Xbss.TimerTask

import Xbss.bean.NoticeData
import Xbss.service.updateNotice
import Xbss.view.MainWindow
import Xbss.view.NoticeStage
import Xbss.view.Notification
import javafx.application.Platform
import javafx.collections.ObservableList
import java.time.LocalDateTime
import java.util.TimerTask

/**
 * @author  Xbss
 * @create 2022-11-02-10:06
 * @version  1.0
 * @describe :这个NoticeTimerTask不是运行在fx线程，不能对focusNoTrigger等列表进行操作，整个放在Platform.runLater才行，但是感觉这样不好
 */
class NoticeTimerTask:TimerTask() {
    override fun run() {
        Platform.runLater {
//            println("开始扫描监控")
//            println("消息线程名称是 ${Thread.currentThread().name}")
//            println("在消息定时器中的表格数据数量：${MainWindow.observableList.size} 起初数据数量：${MainWindow.originObservableList.size}")
            val iterator: MutableIterator<NoticeData> = NoticeStage.focusNoTriggerObs.iterator()
            while (iterator.hasNext()){
                val noticeData = iterator.next()
                val condition = noticeData.condition
                val nowData = MainWindow.originObservableList.filter { it.code == noticeData.code }[0]//根据代码在表格数据查询当前数据
                val nowPrice = if(nowData.price.split(",")[0]=="无") 0.0 else nowData.price.split(",")[0].toDouble()
                val nowDay = LocalDateTime.now().toString().replace("T"," ").dropLast(4)
                if (condition.contains("轨")){
                    val target = when(condition){
                        "突破上轨区" -> "已突破箱体"
                        "上轨区" -> "上轨区"
                        "中轨区" -> "中轨区"
                        "下轨区" -> "下轨区"
                        "跌破下轨区" -> "已跌破箱体"
                        else -> "上轨区"
                    }
                    if (nowData.boxArea[0].value==target){ //如果现在的轨道等于条件轨道，说明触发了
                        iterator.remove()
                        doSome(noticeData,nowDay)
                    }
                }else if(condition.contains("线")){
                    val ma = when(condition){
                        "5日线" -> nowData.dayK.MA5.toDouble()
                        "10日线" -> nowData.dayK.MA10.toDouble()
                        "20日线" -> nowData.dayK.MA20.toDouble()
                        else -> 0.0
                    }
                    if (nowPrice.compareTo(ma)>0){
                        if (noticeData.direction.contains("突")){//当前价格大于条件日线价格，且方向是突破，则触发
                            iterator.remove()
                            doSome(noticeData,nowDay)
                        }
                    }else{
                        if (noticeData.direction.contains("跌")){//当前价格大于条件日线价格，且方向是突破，则触发
                            iterator.remove()
                            doSome(noticeData,nowDay)
                        }
                    }
                }else{
                    if (nowPrice.compareTo(noticeData.condition.toDouble())>0){
                        if (noticeData.direction.contains("突")){//当前价格大于条件价格，且方向是突破，则触发
                            iterator.remove()
                            doSome(noticeData,nowDay)
                        }
                    }else{
                        if (noticeData.direction.contains("跌")){
                            iterator.remove()
                            doSome(noticeData,nowDay)
                        }
                    }
                }
            }
        }
    }
    private fun doSome(noticeData: NoticeData,nowDay:String){
            noticeData.isTrigger = 1
            noticeData.triggerTime = nowDay
            if (updateNotice(noticeData.id,1,nowDay)==1){
                NoticeStage.focusIsTriggerObs.add(0,noticeData)
                Notification(noticeData)
            }
    }
}