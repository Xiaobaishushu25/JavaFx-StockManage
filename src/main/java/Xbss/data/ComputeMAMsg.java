package Xbss.data;

import Xbss.bean.DayK;

import java.util.ArrayList;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-23:14
 * @descirbe
 */
public class ComputeMAMsg {
    public static String computeMAMsg( DayK dayK){
        ArrayList<String> list = new ArrayList<>();
        Double ma5 = Double.parseDouble(dayK.getMA5());
        Double ma10 = Double.parseDouble(dayK.getMA10());
        Double ma20 = Double.parseDouble(dayK.getMA20());
        Double ma60 = Double.parseDouble(dayK.getMA60());
        if (ma5.compareTo(ma10)>0&&ma10.compareTo(ma20)>0){
            list.add("均线多头");
        }else if (ma5.compareTo(ma10)<0&&ma10.compareTo(ma20)<0){
            list.add("均线空头");
        }else {
            list.add("均线缠绕");
        }
        return list.toString();
    }
}
