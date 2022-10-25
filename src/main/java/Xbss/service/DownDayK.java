package Xbss.service;

import Xbss.Mapper.InsertData;
import Xbss.Mapper.Query;
import Xbss.Mapper.UpdateData;
import Xbss.Utils.GetSqlSession;
import Xbss.Utils.GetTableName;
import Xbss.Utils.JudgeStockMarket;
import Xbss.bean.StockInfo;
import Xbss.data.ComputeMA;
import Xbss.view.DownLoadStage;
import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.ibatis.session.SqlSession;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.temporal.ChronoUnit.DAYS;


/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-12-12:27
 * @descirbe
 */
public class DownDayK {
    private static Double allMax=0.0;
    private static Double allMin=2000.0;
    public static void downDayK(String code,String beginDay,String endDay){
        DownLoadStage.msg.set("正在下载日K线数据");
        String url="";
        LocalDate begin= LocalDate.parse(beginDay);
        LocalDate end= LocalDate.parse(endDay);
        long between = DAYS.between(begin, end)+1;
        DownLoadStage.denominator=(int)between;

//        String type=code.substring(0, 2);
//        if (type.equals("51")){
//            url="https://web.ifzq.gtimg.cn/appstock/app/fqkline/get?param="+"sh"+code+",day,"+beginDay+","+endDay+","+between+",qfq";
//        }else if (type.equals("15")){
//            url="https://web.ifzq.gtimg.cn/appstock/app/fqkline/get?param="+"sz"+code+",day,"+beginDay+","+endDay+","+between+",qfq";
//        }
        url="https://web.ifzq.gtimg.cn/appstock/app/fqkline/get?param="+ JudgeStockMarket.judgeStockMarket(code) +code+",day,"+beginDay+","+endDay+","+between+",qfq";
        System.out.println(url);
        HttpResponse<String> response = Unirest.get(url).asString();
        Matcher matcher = Pattern.compile("(?<=\"data\":\\{\".{1,10}\":)\\{\".{0,3}day\":.*?(?=,\"qt)").matcher(response.getBody());
        String js="";
        while (matcher.find()){
            js= matcher.group()+"}";
        }
        js = js.replaceAll("qfq", "");//去掉前复权
        js = js.replaceAll(",\\{\"nd\":.*?\\}", "");//去掉分红
        Array data = new Gson().fromJson(js, Array.class);

        DownLoadStage.progress.set(DownLoadStage.proValue++);

        SqlSession session = GetSqlSession.getSqlSession();
        Query query = session.getMapper(Query.class);
        InsertData insert = session.getMapper(InsertData.class);
        UpdateData update = session.getMapper(UpdateData.class);
        StockInfo stockInfo = query.queryStockInfo(code);
        allMax= stockInfo.getAllMax();
        allMin= stockInfo.getAllMin();
        for (String[] strings : data.getDay()) {
            insert.insertDayK(GetTableName.getTableName(code),stockInfo.getName(),strings[0],strings[1],strings[2],
                    strings[3],strings[4],strings[5],null, null,null, null);
            Double high = Double.parseDouble(strings[3]);
            if (high.compareTo(allMax) > 0) {
                allMax = high;
            } else if (high.compareTo(allMin) < 0)
                allMin = high;
            update.updateMA(GetTableName.getTableName(code),String.valueOf(ComputeMA.computeMA(code, 5,query)),
                    String.valueOf(ComputeMA.computeMA(code, 10,query)),String.valueOf(ComputeMA.computeMA(code, 20,query)),
                    String.valueOf(ComputeMA.computeMA(code, 60,query)),strings[0]);

            DownLoadStage.numerator.set(DownLoadStage.numValue++);
            if (DownLoadStage.numValue%50==0){
                DownLoadStage.progress.set(DownLoadStage.proValue++);
            }
        }
        DownLoadStage.numerator.set(1000);
        update.updatePriceEst(String.valueOf(allMax),String.valueOf(allMin),code);
        DownLoadStage.msg.set("正在更新股票数据...");
        session.close();
        DownLoadStage.progress.set(DownLoadStage.proValue++);
    }
}
class Array{
    public String[][] day;

    public String[][] getDay() {
        return day;
    }

    public void setDay(String[][] day) {
        this.day = day;
    }
}
