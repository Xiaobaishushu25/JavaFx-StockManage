package Xbss.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-12-9:06
 * @descirbe
 */
@Data
@Accessors(chain = true)
public class DayK {
//    private String code;
    public String name;
    public String date;
    public String open;
    public String close;
    public String high;
    public String low;
    public String vol;
    public String MA5;
    public String MA10;
    public String MA20;
    public String MA60;
}
