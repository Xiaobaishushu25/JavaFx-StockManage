package Xbss.service

import Xbss.Mapper.InsertData
import Xbss.Mapper.Query
import Xbss.Utils.GetSqlSession
import Xbss.bean.NoticeData

/**
 * @author  Xbss
 * @create 2022-11-01-21:24
 * @version  1.0
 * @describe
 */
//查询所有关注的消息
fun queryAllNotice():MutableList<NoticeData>{
    val session = GetSqlSession.getSqlSession()
    val query = session.getMapper(Query::class.java)
    val allNotice: ArrayList<NoticeData> = query.queryAllNotice()
    session.close()
    return allNotice
}