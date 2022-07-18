package Xbss.Utils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-12-10:04
 * @descirbe ：获取一个SqlSession的对象
 */
public class GetSqlSession {
    //属性
    private static SqlSessionFactory factory=null;
    static {
        String config= "Mybatis.xml";
        try {
            InputStream inputStream = Resources.getResourceAsStream(config);
            factory=new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //获取SqlSession的对象
    public static SqlSession getSqlSession(){
        SqlSession session=null;
        if(factory!=null){
            session = factory.openSession(true);//openSession(true)
        }
        return session;
    }

}

