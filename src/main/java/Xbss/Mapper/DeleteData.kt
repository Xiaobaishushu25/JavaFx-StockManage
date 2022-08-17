package Xbss.Mapper

import Xbss.bean.DayK
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

/**
 * @author  Xbss
 * @create 2022-08-04-9:58
 * @version  1.0
 * @describe
 */
interface DeleteData {
    @Delete("DROP TABLE ${'$'}{tableName}")
    fun deleteTable(@Param("tableName")tableName: String)
    @Delete("DELETE FROM stockinfo WHERE code = #{code}")
    fun deleteStockInfo(@Param("code")code: String)
//    @Select("select * from ${'$'}{tableName}")
//    fun selectByCode(@Param("tableName") tableName: String?): List<DayK?>?
}