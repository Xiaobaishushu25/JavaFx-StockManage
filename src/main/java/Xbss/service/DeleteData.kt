package Xbss.service

import Xbss.Mapper.DeleteData
import Xbss.Mapper.UpdateData
import Xbss.Utils.GetSqlSession
import Xbss.Utils.GetTableName

/**
 * @author  Xbss
 * @create 2022-08-04-10:08
 * @version  1.0
 * @describe
 */
/**
 * TODO:删除日线表和股票信息表中的信息
 *
 * @param code
 */
fun delete(code:String){
    val session = GetSqlSession.getSqlSession()
    val delete = session.getMapper(DeleteData::class.java)
    delete.deleteTable(GetTableName.getTableName(code))
    delete.deleteStockInfo(code)
    session.close()
}
fun deleteNotice(id:String):Int{
    val session = GetSqlSession.getSqlSession()
    val delete = session.getMapper(DeleteData::class.java)
    val i = delete.deleteNotice(id)
    session.close()
    return i
}