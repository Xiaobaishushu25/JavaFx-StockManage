package Xbss.service;

import Xbss.Mapper.Query;
import Xbss.Utils.GetSqlSession;
import Xbss.Utils.GetTableName;
import Xbss.bean.DayK;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-18:07
 * @descirbe
 */
public class QueryDayKByYear {
    public static ArrayList<DayK> queryDayKByYear(String code){
        SqlSession session = GetSqlSession.getSqlSession();
        Query query = session.getMapper(Query.class);
        ArrayList<DayK> dayKList = query.queryDayKByNum(GetTableName.getTableName(code), 365);
        session.close();
        return dayKList;
    }
}
