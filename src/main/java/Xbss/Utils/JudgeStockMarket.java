package Xbss.Utils;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-15-16:12
 * @descirbe
 */
public class JudgeStockMarket {
    public static String judgeStockMarket(String code){
        if (code.startsWith("15")||code.startsWith("000")||code.startsWith("002")||code.startsWith("300")||code.startsWith("200"))
            return "sz";
        else if (code.startsWith("51")||code.startsWith("60")||code.startsWith("688")||code.startsWith("900")){
            return "sh";
        }else {
            return "unknown";
        }
    }
}
