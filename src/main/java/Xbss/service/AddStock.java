package Xbss.service;

import Xbss.Mapper.Create;
import Xbss.Mapper.InsertData;
import Xbss.Utils.GetSqlSession;
import Xbss.Utils.GetTableName;
import Xbss.view.DownLoadStage;
import org.apache.ibatis.session.SqlSession;

import java.time.LocalDate;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-14-16:58
 * @descirbe
 */
public class AddStock {
    public static void addStock(String code,String name){
        DownLoadStage.msg.set("正在建表");
        DownLoadStage.progress.set(DownLoadStage.proValue++);
        SqlSession session = GetSqlSession.getSqlSession();
        Create create = session.getMapper(Create.class);
        InsertData insertData = session.getMapper(InsertData.class);
        create.createTable(GetTableName.getTableName(code));
        DownLoadStage.msg.set("正在插入股票数据");
        Integer integer = insertData.insertNewStock(code, name);
        session.close();
        if (integer==1){
            DownLoadStage.msg.set("股票数据插入完成");
            DownLoadStage.progress.set(DownLoadStage.proValue++);
        }
        String beginDay="2021-01-01";
        LocalDate endDay = LocalDate.now().minusDays(1);
        DownDayK.downDayK(code,beginDay,endDay.toString());
        DownLoadStage.msg.set("日线数据下载完成");
        Integer updateBox = InsertBox.updateBox(code);
        if (updateBox==1){
            DownLoadStage.msg.set("箱体数据计算完成");
            DownLoadStage.progress.set(DownLoadStage.proValue++);
        }
        DownLoadStage.msg.set("添加成功");
        DownLoadStage.progress.set(10);
    }
}
