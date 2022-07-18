package Xbss.service;

import Xbss.Mapper.InsertData;
import Xbss.Utils.GetSqlSession;
import Xbss.Utils.GetTableName;
import Xbss.bean.DayK;
import Xbss.bean.StockInfo;
import Xbss.view.DownLoadStage;
import org.apache.ibatis.session.SqlSession;
import Xbss.Mapper.Query;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-21:04
 * @descirbe
 */
public class UpdateDayK {
    public static void updateDayK(){
        SqlSession sqlSession = GetSqlSession.getSqlSession();
        Query query = sqlSession.getMapper(Query.class);
        ArrayList<StockInfo> stockList = query.queryAllStock();

        DownLoadStage.denominator=stockList.size();
        System.out.println(stockList.size()+"        "+DownLoadStage.denominator+"--------------+-+++------------------------");
        stockList.forEach(stockInfo -> {

            DownLoadStage.progress.set(DownLoadStage.proValue++);
            DownLoadStage.numerator.set(DownLoadStage.numValue++);

            String code = stockInfo.getCode();
//            LocalDate leastDate = query.queryLatestDayKByCode(GetTableName.getTableName(code));
            DayK latestDayK = query.queryLatestDayKByCode(GetTableName.getTableName(code));
            LocalDate leastDate=LocalDate.parse(latestDayK.getDate());
            LocalDate yesterday = LocalDate.now().minusDays(1);
            if (leastDate.isBefore(LocalDate.now())){
                DownDayK.downDayK(code,String.valueOf(leastDate.plusDays(1)),yesterday.toString());
            }
        });
        DownLoadStage.numerator.set(1000);
        sqlSession.close();
        DownLoadStage.progress.set(DownLoadStage.proValue++);
        DownLoadStage.msg.set("数据更新完成");
        DownLoadStage.progress.set(10);
    }
}
