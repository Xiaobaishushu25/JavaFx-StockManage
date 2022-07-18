package Xbss.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-21:07
 * @descirbe
 */
public interface InsertData {
    @Insert("INSERT INTO ${tableName} VALUES(#{name},#{date},#{open},#{close},#{high},#{low},#{vol},#{MA5},#{MA10},#{MA20},#{MA60});")
    Integer insertDayK(@Param("tableName") String tableName, @Param("name") String name, @Param("date")String date,
                       @Param("open")String open, @Param("close")String close, @Param("high")String high, @Param("low")String low,
                       @Param("vol")String vol, @Param("MA5")String MA5, @Param("MA10")String MA10, @Param("MA20")String MA20, @Param("MA60")String MA60);
    @Insert("INSERT INTO stockinfo(`code`,`name`,allMax,allMin) VALUES(#{code},#{name},'0.0','2000.0');")
    Integer insertNewStock(@Param("code")String code,@Param("name")String name);
}
