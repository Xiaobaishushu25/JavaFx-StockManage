package Xbss.data

import Xbss.bean.DayK
import Xbss.service.QueryDayKByYear
import Xbss.service.QueryStockByCode

/**
 * @author  Xbss
 * @create 2022-07-17-14:40
 * @version  1.0
 * @descirbe
 */
fun getKLine(dayKS:ArrayList<DayK>):ArrayList<ArrayList<Pair<String,Double>>>{
    val list = ArrayList<ArrayList<Pair<String, Double>>>().apply {
        add(ArrayList<Pair<String, Double>>().apply {
            for(i in dayKS){
                if (i.MA5!=null&&i.MA5 != "null"){
                    add(Pair<String,Double>(i.date,i.MA5.toDouble()))
                }
            }
        })
        add(ArrayList<Pair<String, Double>>().apply {
            for(i in dayKS){
                if (i.MA10!=null&&i.MA10 != "null"){
                    add(Pair<String,Double>(i.date,i.MA10.toDouble()))
                }
            }
        })
        add(ArrayList<Pair<String, Double>>().apply {
            for(i in dayKS){
                if (i.MA20!=null&&i.MA20 != "null"){
                    add(Pair<String,Double>(i.date,i.MA20.toDouble()))
                }
            }
        })
    }
    return list
}
fun getAll(code:String):ArrayList<Any>{
    val stockInfo = QueryStockByCode.queryStockByCode(code)
    return ArrayList<Any>().apply {
        add(stockInfo.allMin)
        add(stockInfo.allMax)
        add(stockInfo.name)
    }
}