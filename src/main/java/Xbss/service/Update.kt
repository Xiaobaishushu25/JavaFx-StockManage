package Xbss.service

import Xbss.Mapper.UpdateData
import Xbss.Utils.GetSqlSession
import Xbss.bean.TableInfo
import javafx.collections.ObservableList
import java.time.LocalDate

/**
 * @author  Xbss
 * @create 2022-08-03-16:03
 * @version  1.0
 * @describe
 */
/**
 * TODO:更新金叉死叉数据到数据库中
 *
 * @param flag
 * @param code
 * @return
 */
fun updateGoldOrDie(flag:String,code:String):Int{
    val session = GetSqlSession.getSqlSession()
    val update = session.getMapper(UpdateData::class.java)
    val i = update.updateGoldOrDie(flag, code)
    session.close()
    return i
}

/**
 * TODO :返回true代表最新日线数据是最新的
 *
 * @param code
 * @return
 */
fun judgeTodayUpdate(code: String):Boolean{
    val latestDayK = QueryLatestDayK.queryLatestDayK(code)
    val latestDay = LocalDate.parse(latestDayK.date)
    val yesterday = LocalDate.now().minusDays(1)
    if (yesterday.equals(latestDay)){
        return true
    }
    return false
}

fun updateIndex(code: String,index:Int):Int{
    val session = GetSqlSession.getSqlSession()
    val update = session.getMapper(UpdateData::class.java)
    val i = update.updateIndex(index, code)
    session.close()
    return i
}
fun updateIndexList(observableList: ObservableList<TableInfo>){
    val session = GetSqlSession.getSqlSession()
    val update = session.getMapper(UpdateData::class.java)
    for ((index,value) in observableList.withIndex())
        update.updateIndex(index, value.code)
    session.close()
}
