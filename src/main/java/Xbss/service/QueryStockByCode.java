package Xbss.service;

import Xbss.Mapper.Query;
import Xbss.Utils.GetSqlSession;
import Xbss.bean.StockInfo;
import org.apache.ibatis.session.SqlSession;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-14-22:17
 * @descirbe
 */
public class QueryStockByCode {
    public static StockInfo queryStockByCode(String code){
        SqlSession sqlSession = GetSqlSession.getSqlSession();
        Query query = sqlSession.getMapper(Query.class);
        StockInfo stockInfo = query.queryStockInfo(code);
        sqlSession.close();
        return stockInfo;
    }
}
