package Xbss.Mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-21:08
 * @descirbe
 */
public interface UpdateData {
    @Update("UPDATE ${tableName} SET MA5=#{MA5},MA10=#{MA10},MA20=#{MA20},MA60=#{MA60} WHERE date = #{date}")
    Integer updateMA(@Param("tableName") String tableName, @Param("MA5") String MA5, @Param("MA10") String MA10, @Param("MA20") String MA20,
                     @Param("MA60") String MA60, @Param("date")String date);
    @Update("UPDATE stockInfo SET allMax=#{allMax},allMin=#{allMin} WHERE code = #{code}")
    Integer updatePriceEst(@Param("allMax")String allMax,@Param("allMin")String allMin,@Param("code")String code);
    @Update("UPDATE stockinfo SET box=#{box} WHERE `code` = #{code}")
    Integer updateBox(@Param("box")String box,@Param("code")String code);
    @Update("UPDATE stockinfo SET buy=#{buy} WHERE `code` = #{code}")
    Integer updateGoldOrDie(@Param("buy")String flag,@Param("code")String code);
    @Update("UPDATE stockinfo SET `index`=#{index} WHERE `code` = #{code}")
    Integer updateIndex(@Param("index")Integer index,@Param("code")String code);
    @Update("UPDATE notice SET `isTrigger`=#{isTrigger},`triggerTime`=#{triggerTime} WHERE id = #{id}")
    Integer updateNotice(@Param("id")String id,@Param("isTrigger")int isTrigger,@Param("triggerTime")String triggerTime);
    @Update("UPDATE notice SET `isRead`=#{isRead} WHERE id = #{id}")
    Integer updateNoticeRead(@Param("id")String id,@Param("isRead")int isRead);
}
