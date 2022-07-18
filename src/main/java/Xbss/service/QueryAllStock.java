package Xbss.service;

import Xbss.Mapper.Query;
import Xbss.Utils.GetSqlSession;
import Xbss.bean.StockInfo;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-23:52
 * @descirbe
 */
public class QueryAllStock {
    public static ArrayList<StockInfo> queryAllStock(){
        SqlSession session = GetSqlSession.getSqlSession();
        Query query = session.getMapper(Query.class);
        ArrayList<StockInfo> stockInfoList = query.queryAllStock();
        session.close();
        return stockInfoList;
    }
}
