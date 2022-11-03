package Xbss.service

import Xbss.Mapper.DeleteData
import Xbss.Mapper.InsertData
import Xbss.Utils.GetSqlSession
import Xbss.Utils.GetTableName
import Xbss.bean.NoticeData

/**
 * @author  Xbss
 * @create 2022-11-01-20:44
 * @version  1.0
 * @describe
 */
fun insertNotice(list: MutableList<NoticeData>):Int{
    val session = GetSqlSession.getSqlSession()
    val insert = session.getMapper(InsertData::class.java)
    var x = 0
    for (s in list){
         val i = insert.insertNoticeData(
            s.id,
            s.code,
            s.name,
            s.direction,
            s.condition,
            s.isTrigger,
            s.createTime,
            s.triggerTime,
            s.data,
            s.isRead
        )
        x += i
    }
    session.close()
    return x
}