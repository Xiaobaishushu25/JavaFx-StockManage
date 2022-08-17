package Xbss.data

import Xbss.Utils.DoubleUtil
import Xbss.bean.DayK
import Xbss.bean.StockInfo
import Xbss.bean.TableInfo
import Xbss.service.QueryAllStock
import Xbss.service.QueryLatestDayK
import Xbss.service.updateGoldOrDie
import java.util.StringJoiner

/**
 * @author  Xbss
 * @create 2022-08-03-15:03
 * @version  1.0
 * @describe
 */
/**
 * TODO ：每天第一次启动程序时会调用此函数，计算并更新金叉死叉数据到数据库中，同时返回k线多空数据和金叉死叉列表
 * 计算逻辑：日线数据有三张状态：多头空头和缠绕，只在缠绕中讨论金叉死叉。多头的时候只有两种可能：金叉和大于，如果是金叉，当从多头跌入缠绕的时候会立刻显示金叉
 * 所以在多头的时候要将buy属性设置为大于
 *
 * @return
 */
fun computeGoldOrDieList():MutableList<String>{
    var DayKMsg= mutableListOf<String>()
    QueryAllStock.queryAllStock().apply {
        for (s in this){
            var flag:String
            val builder = StringBuilder()
            val latestDayK = QueryLatestDayK.queryLatestDayK(s.code)
            builder.append(ComputeMAMsg.computeMAMsg(latestDayK))
            flag = if (DoubleUtil.subDouble(latestDayK.MA10.toDouble(),latestDayK.MA20.toDouble())>0)
                "大于"
            else
                "小于"
            if (s.buy==null)
                updateGoldOrDie(flag,s.code)
            else if (builder.contains("多头")){
                updateGoldOrDie("大于",s.code)
            }else if (builder.contains("空头")){
                updateGoldOrDie("小于",s.code)
            } else{
                if (flag=="大于"){//最新的日线数据十日线大于二十日，有可能金叉，检查当前存的数据是否是死叉或小于
                    if (s.buy.equals("死叉")||s.buy.equals("小于")){
                        flag="金叉"
                        updateGoldOrDie(flag,s.code)
                    }
                    else if(s.buy.equals("金叉"))
                        flag="金叉"
//                        updateGoldOrDie(flag,s.code)
                }else{
                    if (s.buy.equals("金叉")||s.buy.equals("大于")){
                        flag="死叉"
                        updateGoldOrDie(flag,s.code)
                    }
                    else if (s.buy.equals("死叉"))
                        flag="死叉"
//                        updateGoldOrDie(flag,s.code)
                }
            }
            if (builder.contains("缠绕"))
                builder.append("|$flag")
            DayKMsg.add(builder.toString())
        }
    }
    return DayKMsg
}
fun computeGoldOrDie(stockInfo: StockInfo,dayK: DayK):String{
    return StringBuilder().apply {
        this.append(ComputeMAMsg.computeMAMsg(dayK))
        if (this.contains("缠绕"))
            append("|${stockInfo.buy}")
    }.toString()
}

/**
 * TODO ：更新股票列表的k线数据信息
 *
 * @param observableList
 * @param DaykMsgList
 */
fun updateObservableList(observableList:MutableList<TableInfo>, DaykMsgList:MutableList<String>){
    for ((index,value) in observableList.withIndex()){
        value.maMsg=DaykMsgList[index]
    }
}