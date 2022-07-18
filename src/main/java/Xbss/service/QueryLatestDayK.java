package Xbss.service;

import Xbss.Mapper.Query;
import Xbss.Utils.GetSqlSession;
import Xbss.Utils.GetTableName;
import Xbss.bean.DayK;
import Xbss.bean.StockInfo;
import org.apache.ibatis.session.SqlSession;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-23:27
 * @descirbe
 */
public class QueryLatestDayK {
    public static DayK queryLatestDayK(String code){
        SqlSession session = GetSqlSession.getSqlSession();
        Query query = session.getMapper(Query.class);
        DayK dayK = query.queryLatestDayKByCode(GetTableName.getTableName(code));
        session.close();
        return dayK;
    }
}
