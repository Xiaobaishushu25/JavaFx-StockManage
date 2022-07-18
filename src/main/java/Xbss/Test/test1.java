package Xbss.Test;

import Xbss.Mapper.InsertData;
import Xbss.Mapper.Query;
import Xbss.Mapper.UpdateData;
import Xbss.Utils.GetSqlSession;
import Xbss.Utils.GetTableName;
import Xbss.bean.StockInfo;
import Xbss.data.ComputeMA;
import Xbss.data.GetNowData;
import Xbss.view.DownLoadStage;
import com.google.gson.Gson;
import javafx.beans.property.SimpleIntegerProperty;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.SqlSession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-11-19:38
 * @descirbe
 */
public class test1 {

    private static Double allMax=0.0;
    private static Double allMin=2000.0;
    public static void main(String[] args) {

    }
    public static List get(String code){
        DownLoadStage.denominator=300;
        DownLoadStage.progress.set(1);
        DownLoadStage.msg.set("正在下载日K线数据");
        String url="";
        String type=code.substring(0, 2);
        if (type.equals("51")){
            url="https://web.ifzq.gtimg.cn/appstock/app/fqkline/get?param="+"sh"+code+",day,2021-7-4,2022-7-11,300,qfq";
        }else if (type.equals("15")){
            url="https://web.ifzq.gtimg.cn/appstock/app/fqkline/get?param="+"sz"+code+",day,2021-7-4,2022-7-11,300,qfq";
        }
        HttpResponse<String> response = Unirest.get(url).asString();
        Matcher matcher = Pattern.compile("(?<=\"data\":\\{\".{1,10}\":)\\{\".{0,3}day\":.*?(?=,\"qt)").matcher(response.getBody());
        String js="";
//        System.out.println(response.getBody());
        while (matcher.find()){
            js= matcher.group()+"}";
//            System.out.println(matcher.group());
        }
        js = js.replaceAll("qfq", "");//去掉前复权
        js = js.replaceAll(",\\{\"nd\":.*?\\}", "");//去掉分红
//        System.out.println("js:"+js);
        ArrayList<gupiao> list = new ArrayList<>();
        cashi cashi = new Gson().fromJson(js, cashi.class);
//        SqlSession session = GetSqlSession.getSqlSession();
//        Query mapper = session.getMapper(Query.class);
//        InsertData insert = session.getMapper(InsertData.class);
//        UpdateData update = session.getMapper(UpdateData.class);
//        StockInfo stockInfo = mapper.queryStockInfo(code);
        for (String[] strings : cashi.getDay()) {
//            insert.insertDayK(GetTableName.getTableName(code),stockInfo.getName(),strings[0],strings[1],strings[2],
//                   strings[3],strings[4],strings[5],null, null,null, null);
//            update.updateMA(GetTableName.getTableName(code),String.valueOf(ComputeMA.computeMA(code, 5,mapper)),
//                     String.valueOf(ComputeMA.computeMA(code, 10,mapper)),String.valueOf(ComputeMA.computeMA(code, 20,mapper)),
//                     String.valueOf(ComputeMA.computeMA(code, 60,mapper)),strings[0]);
            list.add(new gupiao().setName("新能源ETF").setCode("51515").setDate(strings[0]).setOpen(strings[1])
                    .setClose(strings[2]).setHigh(strings[3]).setLow(strings[4]).setVol(strings[5]));
            DownLoadStage.numerator.set(DownLoadStage.numValue++);
//            System.out.println(strings[1]);
        }
        DownLoadStage.progress.set(3);
//        session.close();
//        list.forEach(gupiao -> System.out.println(gupiao));
        gupiao gupiao = new gupiao();
        for (gupiao g : list) {
//                Double min=subDouble(i,0.02);
            Double high = Double.parseDouble(g.getHigh());
            if (high.compareTo(allMax) > 0) {
                allMax = high;
                gupiao=g;
            } else if (high.compareTo(allMin) < 0)
                allMin = high;
        }
        System.out.println(gupiao);
//        for(double i=1.9;i<2.5;i=i+0.01){
        double step = Double.parseDouble(String.format("%.2f", GetNowData.GetNowPrice(code)*0.01));
        System.out.println("step"+step);
        ArrayList<Object> arrayList = new ArrayList<>();
        System.out.println("min"+allMin+"max"+allMax);
        for (Object o : compute(step, allMin, addDouble(allMin, allMax) / 2.0, list)) {
            arrayList.add(o);
        }
        for (Object o : compute(step, addDouble(allMin, allMax) / 2.0, allMax, list)) {
            arrayList.add(o);
        }
        return arrayList;
    }
    public static List compute(Double step,Double down,Double up,ArrayList<gupiao> list){
        System.out.println("down"+down+"up"+up);
        HashMap<Double, Integer> map = new HashMap<>();
        double x=0;
        int count=0;
        int max=0;
        double[] f=new double[120];
        int sum=0;
        for(Double i=down;i<up;i=addDouble(i,step)){
            count=0;
            for (gupiao g : list) {
//                Double min=subDouble(i,0.02);
                Double min=subDouble(i,step);
                Double high=Double.parseDouble(g.getHigh());
//                Double self=addDouble(i,0.02);
                Double self=addDouble(i,step);
                Double low=Double.parseDouble(g.getLow());
//                if ((min.compareTo(high)<0&&high.compareTo(self)<0)||(min.compareTo(low)<0&&low.compareTo(self)<0)){
                if ((min.compareTo(high)<0&&high.compareTo(i)<0)||(min.compareTo(low)<0&&low.compareTo(i)<0)){
                    count++;
                }
                Double open=Double.parseDouble(g.getOpen());
                Double close=Double.parseDouble(g.getClose());
//                if ((close.compareTo(i)<0&&i.compareTo(open)<0)||(open.compareTo(i)<0&&i.compareTo(close)<0))
//                    count--;

            }
//            if (count>max){
//                max=count;
//                x=i;
//            }
            if (count>4){
                f[sum++]=i;
                map.put(i,count);
            }
        }
//        System.out.println(max+"压力位"+x);
//        for (Map.Entry<Double, Integer> entry : map.entrySet()) {
//            System.out.println("价格："+entry.getKey()+"       -->"+entry.getValue());
//        }
        //取平均值法
//        for (int i = 0; i < sum; i++) {
//            Double v = addDouble(f[i], 2.0*step);
//            if(f[i+1]!=0){
//                if (v.compareTo(f[i+1])>=0){
//                    f[i+1]=addDouble(f[i],f[i+1])/2.0;
//                    f[i]=0;
//                }
//            }
//        }
        System.out.println("********");
        for (int i = 0; i < sum; i++) {
            if (f[i]!=0)
                System.out.println(f[i]+"  "+map.get(f[i]));
        }
        System.out.println("caozuo hou");
        //取最大值法
        for (int i = 0; i < sum; i++) {
            Double v = addDouble(f[i], step*10.0);
            if (v.compareTo(f[i+1])>=0&&f[i+1]!=0){
                Integer left = map.get(f[i]);
                Integer right = map.get(f[i + 1]);
                if (left==right){
                    double v1 = Double.parseDouble(String.format("%.3f",addDouble(f[i],f[i+1])/2.0));
//                    System.out.println("平均后："+v1);
                    map.put(v1,left);
                    f[i]=0;
                    f[i+1]=v1;
                }else {
                    f[i+1]=right>left?f[i+1]:f[i];
                    right=right>left?right:left;
                    f[i]=0;
                    map.put(f[i],right);
                }
            }
        }
        ArrayList<Object> arrayList = new ArrayList<>();
        for (int i = 0; i < sum; i++) {
            if (f[i]!=0)
                arrayList.add(f[i]);
                System.out.println(f[i]);
        }
        return arrayList;
    }
    public static double addDouble(double number1 , double number2) {
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(number1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(number2));
        return bigDecimal1.add(bigDecimal2).doubleValue();
    }

    /**
     * 减法运算
     * @param number1
     * @param number2
     * @return
     */
    public static double subDouble(double number1, double number2) {
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(number1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(number2));
        return bigDecimal1.subtract(bigDecimal2).doubleValue();
    }
}
@Data
@Accessors(chain = true)
class gupiao{
    public String name;
    public String code;
    public String date;
    public String open;
    public String close;
    public String high;
    public String low;
    public String vol;
}
class cashi{
    public String[][] day;

    public String[][] getDay() {
        return day;
    }

    public void setDay(String[][] day) {
        this.day = day;
    }
}
