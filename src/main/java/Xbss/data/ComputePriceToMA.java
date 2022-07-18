package Xbss.data;

import Xbss.bean.DayK;

import java.time.OffsetDateTime;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-14-13:59
 * @descirbe
 */
public class ComputePriceToMA {
    public static String computePriceToMA(Double price, DayK dayK){
        StringBuilder builder = new StringBuilder();
        Double ma5 = Double.parseDouble(dayK.getMA5());
        Double ma10 = Double.parseDouble(dayK.getMA10());
        Double ma20 = Double.parseDouble(dayK.getMA20());
        Double ma60 = Double.parseDouble(dayK.getMA60());
        if (price.compareTo(ma20)<0){
            builder.append("跌破20日线");
        }else if(price.compareTo(ma10)<0){
            builder.append("跌破10日线");
        }else if (price.compareTo(ma5)<0){
            builder.append("跌破5日线");
        }
        if (builder.length()>0){
            builder.append(",");
        }
        if (price.compareTo(ma5)>0){
            builder.append("在5日线上");
        }else if (price.compareTo(ma10)>0){
            builder.append("在10日线上");
        }else if (price.compareTo(ma20)>0){
            builder.append("在20日线上");
        }else if (price.compareTo(ma60)>0){
            builder.append("在60日线上");
        }else {
            return "跌破六十日线了，没救了";
        }
        return builder.toString();
    }
}
