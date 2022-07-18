package Xbss.data;

import Xbss.Utils.DoubleUtil;
import java.util.ArrayList;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-13-18:04
 * @descirbe
 */
public class ComputeBoxArea {
    public static ArrayList<String> computeBoxArea(Double price,String box){
        ArrayList<String> list = new ArrayList<>();
        if (box.endsWith(",999")){
            //如果箱体数据最后一个是999的话表示人工检查过了，要去掉；
            box=box.substring(0,box.length()-4);
        }
        String[] boxs = box.split(",");

        for (int i = 0; i < boxs.length; i++) {
            if(price.compareTo(Double.parseDouble(boxs[0]))<0){
                list.add("已跌破箱体");
                list.add("箱体下轨："+boxs[0]);
                break;
            }else if (price.compareTo(Double.parseDouble(boxs[boxs.length-1]))>0){
                list.add("已突破箱体");
                list.add("箱体上轨："+boxs[boxs.length-1]);
                break;
            }else if (price.compareTo(Double.parseDouble(boxs[i]))>=0&&price.compareTo(Double.parseDouble(boxs[i+1]))<=0){
                String divideBox = divideBox(price, Double.parseDouble(boxs[i]), Double.parseDouble(boxs[i + 1]));
                list.add(divideBox);
                list.add("支撑位："+boxs[i]);
                list.add("压力位："+boxs[i+1]);
                break;
            }
        }
        return list;
    }
    private static String divideBox(Double price,Double down,Double up){
        double subDouble = DoubleUtil.subDouble(up, down)/4.0;
        double downArea = DoubleUtil.addDouble(down, subDouble);
        double upArea = DoubleUtil.subDouble(up, subDouble);
        if (price.compareTo(downArea)<=0){
            return "下轨区";
        }else if (price.compareTo(upArea)>0){
            return "上轨区";
        }else {
            return "中轨区";
        }
    }
}
