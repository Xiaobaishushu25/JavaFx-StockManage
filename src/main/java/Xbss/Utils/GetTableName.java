package Xbss.Utils;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-12-11:24
 * @descirbe
 */
public class GetTableName {
    public static String getTableName(String code){
        return JudgeStockMarket.judgeStockMarket(code)+"_"+code;
//        String type=code.substring(0, 2);
//        if (type.equals("51")){
//            return "sh_"+code;
//        }else if (type.equals("15")){
//            return "sz_"+code;
//        }
//        return null;
    }
}
