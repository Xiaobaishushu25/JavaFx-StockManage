package Xbss.view.UIFun

import Xbss.bean.NoticeData
import Xbss.bean.TableInfo
import javafx.scene.text.Text
import javafx.scene.text.TextFlow

/**
 * @author  Xbss
 * @create 2022-10-31-14:51
 * @version  1.0
 * @describe :sentence :您所关注的|创成长ETF|已经|跌破|五日线
 */
fun getFocusTextFlow(sentence:NoticeData):TextFlow{
    val text1:Text
    val text2 = Text(sentence.name).apply { styleClass.addAll("Xbss-text-info") }
    val text3:Text
    val text4 = Text(sentence.direction).apply{ styleClass.addAll(if (sentence.direction.contains("跌"))"Xbss-text-down" else "Xbss-text-up")}
    val text5 = Text("${sentence.condition}。").apply { styleClass.addAll("Xbss-text-info") }
    if (sentence.isTrigger==1){
        text1 = getNormalText("您所关注的")
        text3 = getNormalText("已经")
    }else{
        text1 = getNormalText("正在监控")
        text3 = getNormalText("是否")
    }
        return TextFlow(text1,text2,text3,text4,text5)
}
fun getTempTextFlow(sentence: TableInfo):TextFlow{
    val text1 = getNormalText("注意！")
    val text2 = Text(sentence.name).apply { styleClass.addAll("Xbss-text-info") }
    val text3 = getNormalText("已经处于")
    val text4 = Text("${sentence.boxArea[0].value}、").apply{ styleClass.addAll("Xbss-text-up")}
    val text5 = Text("${sentence.maMsg.subSequence(1,5)}、").apply{ styleClass.addAll("Xbss-text-up")}
    val text6 = Text("${sentence.priceToMa}。").apply{ styleClass.addAll("Xbss-text-up")}
    return TextFlow(text1,text2,text3,text4,text5,text6)
}
private fun getNormalText(value:String):Text{
    return  Text(value).apply { styleClass.addAll("Xbss-text") }
}