package Xbss.service;

import Xbss.Mapper.Query;
import Xbss.Mapper.UpdateData;
import Xbss.Utils.GetSqlSession;
import Xbss.Utils.GetTableName;
import Xbss.bean.DayK;
import Xbss.bean.StockInfo;
import Xbss.data.ComputeBox;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-22:34
 * @descirbe :根据传入的股票代码计算箱体区域信息并更新到数据库中
 */
public class InsertBox {
    public static Integer updateBox(String code){
        System.out.println("开始计算箱体数据");
        StringBuilder box = new StringBuilder();
        SqlSession session = GetSqlSession.getSqlSession();
        Query query = session.getMapper(Query.class);
        UpdateData update = session.getMapper(UpdateData.class);
        ArrayList<DayK> dayKList = query.queryDayKByNum(GetTableName.getTableName(code), 365);
        StockInfo stockInfo = query.queryStockInfo(code);
        for (Double aDouble : ComputeBox.computeBox(code, stockInfo.getAllMax(), stockInfo.getAllMin(), dayKList)) {
            box.append(aDouble.toString()+",");
        }
        box.deleteCharAt(box.length()-1);
        System.out.println(box);
        Integer integer = update.updateBox(box.toString(), code);
        session.close();
        return integer;
    }
}
