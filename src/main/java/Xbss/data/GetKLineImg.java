package Xbss.data;

import Xbss.Utils.JudgeStockMarket;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.*;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-15-15:50
 * @descirbe
 */
public class GetKLineImg {
    public static ImageView getMinKImg(String code){
        String url="";
        String market = JudgeStockMarket.judgeStockMarket(code);
        if (market.equals("sh")){
            url="http://webquotepic.eastmoney.com/GetPic.aspx?nid=1."+code+"&imageType=GNR&token=4f1862fc3b5e77c150a2b985b12db0fd";
        }else if (market.equals("sz")){
            url="http://webquotepic.eastmoney.com/GetPic.aspx?nid=0."+code+"&imageType=GNR&token=4f1862fc3b5e77c150a2b985b12db0fd";
        }
        HttpResponse<byte[]> response = Unirest.get(url).asBytes();
        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(response.getBody())));
        return imageView;
    }
    public static Image getDayKImg(String code,int width){
        String url="";
        String unitWidth=String.valueOf(width);
        String market = JudgeStockMarket.judgeStockMarket(code);
        if (market.equals("sh")){
            url="http://webquoteklinepic.eastmoney.com/GetPic.aspx?nid=1."+code+"&type=&unitWidth="+unitWidth+"&ef=&formula=RSI&AT=1&imageType=KXL&timespan=1657875538";
        }else if (market.equals("sz")){
            url="http://webquoteklinepic.eastmoney.com/GetPic.aspx?nid=0."+code+"&type=&unitWidth="+unitWidth+"&ef=&formula=RSI&AT=1&imageType=KXL&timespan=1657875538";
        }
        HttpResponse<byte[]> response = Unirest.get(url).asBytes();
        return new Image(new ByteArrayInputStream(response.getBody()));
    }
}
