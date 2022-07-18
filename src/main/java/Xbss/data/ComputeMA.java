package Xbss.data;

import Xbss.Mapper.Query;
import Xbss.Utils.DoubleUtil;
import Xbss.Utils.GetTableName;
import Xbss.bean.DayK;

import java.util.List;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-12-12:49
 * @descirbe
 */
public class ComputeMA {
    static Double sum=0.0;
    public static Double computeMA(String code, Integer num, Query query){
        sum=0.0;
        List<DayK> dayKS = query.queryDayKByNum(GetTableName.getTableName(code), num);
        if (dayKS.size()==num){
            dayKS.forEach(dayK -> {
                sum= DoubleUtil.addDouble(sum,Double.parseDouble(dayK.getClose()));
            });
//            System.out.println("-------------------------------------------");
//            System.out.println("sum"+sum+"   num"+num);
//            System.out.println(sum/num);
//            System.out.println("MA"+Double.parseDouble(String.format("%.3f",sum/num)));
//            System.out.println("-------------------------------------------");
            return Double.parseDouble(String.format("%.3f",sum/num));
        }
        return null;
    }
}
