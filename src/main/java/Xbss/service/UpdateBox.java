package Xbss.service;

import Xbss.Mapper.UpdateData;
import Xbss.Utils.GetSqlSession;
import org.apache.ibatis.session.SqlSession;


/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-15-18:47
 * @descirbe
 */
public class UpdateBox {
    public static Integer updateBox(String code,String box){
        SqlSession session = GetSqlSession.getSqlSession();
        UpdateData update = session.getMapper(UpdateData.class);
        Integer integer = update.updateBox(box, code);
        session.close();
        return integer;
    }
}
