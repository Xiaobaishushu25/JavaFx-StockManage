import Xbss.Mapper.Create;
import Xbss.Utils.DoubleUtil;
import Xbss.Utils.GetSqlSession;
import Xbss.Utils.GetTableName;
import Xbss.bean.DayK;
import Xbss.bean.StockInfo;
import Xbss.data.ComputeBoxArea;
import Xbss.service.DownDayK;
import Xbss.service.InsertBox;
import Xbss.service.UpdateDayK;
import Xbss.view.MainWindow;
import com.sun.webkit.network.CookieManager;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kotlin.Pair;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import Xbss.Mapper.Query;

import javax.sound.midi.Soundbank;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
//import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-12-10:06
 * @descirbe
 */
public class MyTest  {

    @Test
    public void test1(){
        SqlSession session = GetSqlSession.getSqlSession();
        Query mapper = session.getMapper(Query.class);
//        List<DayK> dayKList = mapper.selectByCode("sh_515030");
        List<DayK> dayKList = mapper.selectByCode(GetTableName.getTableName("515030"));
        dayKList.forEach(dayK -> System.out.println(dayK));
        session.close();
    }
    @Test
    public void test2() {
//        Double aDouble = ComputeMA.computeMA("515030", 5);
//        System.out.println(aDouble);
        SqlSession session = GetSqlSession.getSqlSession();
        Query mapper = session.getMapper(Query.class);
        System.out.println(mapper.queryStockInfo("515030"));
        session.close();
    }
    @Test
    public void test3() {
        SqlSession session = GetSqlSession.getSqlSession();
        Query mapper = session.getMapper(Query.class);
//        LocalDate date = mapper.queryLatestDayKByCode(GetTableName.getTableName("515030"));
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.minusDays(1);
        System.out.println("昨天"+ localDate);
//        if (date.isBefore(now)){
//            System.out.println("bushi zuixin ");
//        }else {
//            System.out.println("yijing shi zuixin ");
//        }
//        long between = DAYS.between(date, now);
//        System.out.println("间隔天数"+between);
//        System.out.println(mapper.queryLatestDayKByCode(GetTableName.getTableName("515030")));
        session.close();
    }
    @Test
    public void test4() {
        String date="2022-05-01";
        String date2="2022-07-01";
        DownDayK.downDayK("515030",date,date2);
//        LocalDate parse = LocalDate.parse(date);
//        System.out.println(parse);
    }
    @Test
    public void test5() {
        System.out.println(LocalDate.now());
        LocalDate localDate = LocalDate.now().minusDays(1);
        System.out.println("昨天"+ localDate);
    }
    @Test
    public void test6() {
        UpdateDayK.updateDayK();
    }
    @Test
    public void test7() {
        SqlSession sqlSession = GetSqlSession.getSqlSession();
        Create create = sqlSession.getMapper(Create.class);
        create.createTable(GetTableName.getTableName("159992"));
    }
    @Test
    public void test8() {
        for (String s : ComputeBoxArea.computeBoxArea(2.5, "1.943,2.26,2.432")) {
            System.out.println(s);
        }
    }
    @Test
    public void test9() {
        String d="2021-08-01";
        String a="2022-07-11";
        SqlSession sqlSession = GetSqlSession.getSqlSession();
        Query query = sqlSession.getMapper(Query.class);
        StockInfo stockInfo = query.queryStockInfo("515030");
//        DownDayK.downDayK("515030",d,a);
//        Integer integer = UpdateBox.updateBox("515030");
        System.out.println("*****************");
        for (String s : ComputeBoxArea.computeBoxArea(2.459, stockInfo.getBox())) {
            System.out.println(s);
        }
        System.out.println("-----------------");
    }
    @Test
    public void test10() {
        ObservableList<SimpleStringProperty> objects1 = FXCollections.observableArrayList();
        SimpleListProperty<SimpleStringProperty> initList = new SimpleListProperty<>(objects1);
//        SimpleListProperty<String> initList = new SimpleListProperty<>();
//        initList.add(new SimpleStringProperty("无"));
        ObservableList<SimpleStringProperty> observableList = initList.get();
        System.out.println(observableList.isEmpty());

        SimpleStringProperty nihao = new SimpleStringProperty("nihao");
        observableList.add(nihao);
        System.out.println(initList.isEmpty());
//        initList.add("w");
//        initList.add(0,new SimpleStringProperty("无"));
    }
    @Test
    public void test11() {
        SqlSession sqlSession = GetSqlSession.getSqlSession();
        Create create = sqlSession.getMapper(Create.class);
        create.createTable(GetTableName.getTableName("159967"));
        sqlSession.close();
    }
    @Test
    public void test12() {
        String d="2021-08-01";
        String a="2022-07-13";
        SqlSession sqlSession = GetSqlSession.getSqlSession();
        Query query = sqlSession.getMapper(Query.class);
        StockInfo stockInfo = query.queryStockInfo("159967");
        DownDayK.downDayK("159967",d,a);
        InsertBox.updateBox("159967");
//        Integer integer = UpdateBox.updateBox("515030");
        System.out.println("*****************");

        System.out.println("-----------------");
    }
    @Test
    public void test13() {
        SqlSession sqlSession = GetSqlSession.getSqlSession();
        Query query = sqlSession.getMapper(Query.class);
        ArrayList<StockInfo> stockList = query.queryAllStock();
        System.out.println(stockList.size());
        sqlSession.close();
    }
    @Test
    public void test14() {
        String box="12,5,45,89";
        if (box.endsWith(",999")){
            box=box.substring(0,box.length()-4);
        }
        System.out.println(box);
    }
    @Test
    public void test15() throws IOException {
//        CookieHandler.setDefault(new java.net.CookieManager());
        CookieHandler.setDefault(new com.sun.webkit.network.CookieManager());
//        CookieManager handler = (CookieManager) CookieManager.getDefault();
//        CookieManager handler = (CookieManager) CookieHandler.getDefault();
        CookieManager handler = (CookieManager) CookieHandler.getDefault();
        try {
            Map<String, List<String>> map = handler.get(new URI("https://docs.microsoft.com/zh-cn/dotnet/api/java.net.cookiehandler?view=xamarin-android-sdk-12"), new HashMap<>());
//            System.out.println(map==null);
            System.out.println(map.size());
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                System.out.println(entry.getKey()+"----"+entry.getValue());
            }

//            List<String> cookie1 = map.get("Cookie");
//            System.out.println(cookie1.get(0));
//            for (String cookie : map.get("Cookie")) {
//                System.out.println(cookie);
//            }

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void test16(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(7);
        list.add(8);
//        list.remove(0);
        new Thread(()->{
            for (Integer i : list) {
                System.out.println(i);
            }
        }).start();
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()){
            Integer next = iterator.next();
            if (next==3)
                iterator.remove();
//                list.remove(3);
        }
//            iterator.remove();
//        for (int i = 0; i < list.size(); i++) {
//            if (i==list.size()-1){
//                list.remove(i);
//            }
//        }
        for (Integer i : list) {
            System.out.println(i);
        }
    }
    @Test
    public void test17(){
        LocalDateTime now = LocalDateTime.now();
        LocalTime now1 = LocalTime.now();
        System.out.println(now);
        System.out.println(now1);
    }
    @Test
    public void test18(){
        String x = "2022-11-02T09:51:14.077";
        String x1 = "2022-11-02T09:52:14.077";
//        System.out.println(x>x1);
    }

}
