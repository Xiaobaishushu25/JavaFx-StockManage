package Xbss.Utils;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-15-16:12
 * @descirbe :根据输入的股票代码判断是沪市（sh）还是深市（sz）的
 */
public class JudgeStockMarket {
    public static String judgeStockMarket(String code){
        if (code.startsWith("15")||code.startsWith("000")||code.startsWith("002")||code.startsWith("300")||code.startsWith("200"))
            return "sz";//深圳
        else if (code.startsWith("51")||code.startsWith("60")||code.startsWith("588")||code.startsWith("688")||code.startsWith("900")){
            return "sh";//上海
        }else {
            return "unknown";
        }
    }
}
