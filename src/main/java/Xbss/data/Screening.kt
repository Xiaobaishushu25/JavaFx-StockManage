package Xbss.data

import Xbss.bean.TableInfo
import Xbss.view.MainWindow
import javafx.collections.FXCollections
import javafx.collections.ObservableList

/**
 * @author  Xbss
 * @create 2022-07-28-15:13
 * @version  1.0
 * @describe
 */
object delete{
    var deleteInfo= mutableListOf<TableInfo>()
}
/**
 * TODO
 *
 * @param observableList
 * @param boxList
 */
fun screening(observableList: ObservableList<TableInfo>,boxList:MutableList<String>,maMsgList:MutableList<String>, maToPriceList:MutableList<String>,timer:Any){
    synchronized(timer){
        val iterator = observableList.iterator()
        var i=0
        while (iterator.hasNext()){
            var tableInfo = iterator.next()
            if (!judge(tableInfo.boxArea[0].value,boxList)){
                iterator.remove()
                delete.deleteInfo.add(tableInfo)
                i++
                println("删除了$$i 因为箱体不符")
            }else if (!judge(tableInfo.maMsg.toString(),maMsgList)){
                iterator.remove()
                delete.deleteInfo.add(tableInfo)
                i++
                println("删除了$$i 因为均线不符")
            }else if (!judge(tableInfo.priceToMa,maToPriceList)){
                iterator.remove()
                delete.deleteInfo.add(tableInfo)
                i++
                println("删除了$$i 因为价格均线不符")
            }
        }
    }
}

/**
 * TODO:判断给定字符串是否包含集合任一条件，包含返回true，不包含返回false
 * @param value
 * @param mutableList
 * @return
 */
fun judge(value:String,mutableList: MutableList<String>):Boolean{
    if (mutableList.size==0)
        return true
    var i=0
    for (s in mutableList){
        if (!value.contains(s))
            i+=1
    }
    if (i==mutableList.size)
        return false
    return true
}