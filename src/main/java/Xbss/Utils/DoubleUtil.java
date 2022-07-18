package Xbss.Utils;

import java.math.BigDecimal;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-12-12:57
 * @descirbe
 */
public class DoubleUtil {
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
