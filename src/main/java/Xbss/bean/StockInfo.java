package Xbss.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-12-9:08
 * @descirbe
 */
@Data
@Accessors(chain = true)
public class StockInfo {
    public String code;
    public String name;
    public String box;
    public String buy;//大于、金叉，小于、死叉
    public String sell;
    public Double allMax;
    public Double allMin;
}
