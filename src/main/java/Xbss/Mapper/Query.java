package Xbss.Mapper;

import Xbss.bean.DayK;
import Xbss.bean.NoticeData;
import Xbss.bean.StockInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-12-9:56
 * @descirbe
 */
public interface Query {
    @Select("select * from stockInfo where code = #{code}")
    StockInfo queryStockInfo(@Param("code")String code);
//    @Select("select * from stockInfo order by index")
    @Select("select * from stockInfo ORDER BY `index` ASC")
    ArrayList<StockInfo> queryAllStock();
    @Select("select * from ${tableName}")
    List<DayK> selectByCode(@Param("tableName") String tableName);
    @Select("select * from ${tableName} ORDER BY date DESC LIMIT #{num}")
    ArrayList<DayK> queryDayKByNum(@Param("tableName") String tableName, @Param("num")Integer num);
    @Select("SELECT * FROM ${tableName} WHERE date=(SELECT MAX(date) FROM ${tableName})")
    DayK queryLatestDayKByCode(@Param("tableName") String tableName);
    @Select("SELECT * FROM notice ORDER BY createTime DESC")
    ArrayList<NoticeData> queryAllNotice();
}
