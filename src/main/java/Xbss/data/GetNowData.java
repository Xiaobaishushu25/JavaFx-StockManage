package Xbss.data;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.ArrayList;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-11-16:27
 * @descirbe
 */
public class GetNowData {
    /**
     * @Author Xiaobaishushu
     * @Description
     * @Date 2022/6/22 22:40
     * @Param [type, num] :type为51是上海基金（51开头(sh)），15是深圳基金（15开头(sz)）
     * @return int
     **/
    public static double GetNowPrice(String code){
        String url="";
        String type=code.substring(0, 2);
        if (type.equals("51")){
            url="http://qt.gtimg.cn/q="+"sh"+code;
        }else if (type.equals("15")){
            url="http://qt.gtimg.cn/q="+"sz"+code;
        }
        HttpResponse<String> httpResponse = Unirest.get(url).asString();
        String[] words = httpResponse.getBody().split("~");
        double price = Double.parseDouble(words[3]);
        return price;
    }
    /**
     * @Author Xiaobaishushu
     * @Description
     * @Date 2022/7/14 0:24
     * @Param [code]
     * @return java.util.ArrayList<java.lang.Object> :第一个是名称，第二个是当前价格，第三个是涨跌百分比（返回是一个double类型
     *          要自行加%）,第四个是代码code
     **/
    public static ArrayList<Object> getNowData(String code){
        ArrayList<Object> list = new ArrayList<>();
        String url="";
        String type=code.substring(0, 2);
        if (type.equals("51")){
            url="http://qt.gtimg.cn/q="+"sh"+code;
        }else if (type.equals("15")){
            url="http://qt.gtimg.cn/q="+"sz"+code;
        }
        HttpResponse<String> httpResponse = Unirest.get(url).asString();
        String[] words = httpResponse.getBody().split("~");
        list.add(words[1]);
        double price = Double.parseDouble(words[3]);
        list.add(price);
        list.add(words[32]);
        list.add(words[2]);
        return list;
    }
}
