package Xbss.data;

import Xbss.Utils.DoubleUtil;
import Xbss.bean.DayK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-12-20:53
 * @descirbe
 */
public class ComputeBox {
    public static ArrayList<Double> computeBox(String code,Double allMax,Double allMin, ArrayList<DayK> list){
        double step = Double.parseDouble(String.format("%.2f", GetNowData.GetNowPrice(code)*0.01));
        ArrayList<Double> boxList = new ArrayList<>();
        for (Double d : compute(step, allMin, DoubleUtil.addDouble(allMin, allMax) / 2.0, list)) {
            boxList.add(d);
        }
        for (Double d : compute(step, DoubleUtil.addDouble(allMin, allMax) / 2.0, allMax, list)) {
            boxList.add(d);
        }
        return boxList;
    }
    private static List<Double> compute(Double step,Double down,Double up,ArrayList<DayK> list){
        System.out.println("down"+down+"up"+up);
        HashMap<Double, Integer> map = new HashMap<>();
        int count=0;
        double[] f=new double[list.size()];
        int sum=0;
        for(Double i=down;i<up;i=DoubleUtil.addDouble(i,step)){
            count=0;
            for (DayK g : list) {
                Double min=DoubleUtil.subDouble(i,step);
                Double high=Double.parseDouble(g.getHigh());
                Double low=Double.parseDouble(g.getLow());
                if ((min.compareTo(high)<0&&high.compareTo(i)<0)||(min.compareTo(low)<0&&low.compareTo(i)<0)){
                    count++;
                }
            }
            if (count>4){
                f[sum++]=i;
                map.put(i,count);
            }
        }
        //取平均值法
//        for (int i = 0; i < sum; i++) {
//            Double v = addDouble(f[i], 2.0*step);
//            if(f[i+1]!=0){
//                if (v.compareTo(f[i+1])>=0){
//                    f[i+1]=addDouble(f[i],f[i+1])/2.0;
//                    f[i]=0;
//                }
//            }
//        }
        //取最大值法
        for (int i = 0; i < sum; i++) {
            Double v = DoubleUtil.addDouble(f[i], step*10.0);
            if (v.compareTo(f[i+1])>=0&&f[i+1]!=0){
                Integer left = map.get(f[i]);
                Integer right = map.get(f[i + 1]);
                if (left==right){
                    double v1 = Double.parseDouble(String.format("%.3f",DoubleUtil.addDouble(f[i],f[i+1])/2.0));
                    map.put(v1,left);
                    f[i]=0;
                    f[i+1]=v1;
                }else {
                    f[i+1]=right>left?f[i+1]:f[i];
                    right=right>left?right:left;
                    f[i]=0;
                    map.put(f[i],right);
                }
            }
        }
        ArrayList<Double> arrayList = new ArrayList<>();
        for (int i = 0; i < sum; i++) {
            if (f[i]!=0)
//                arrayList.add(f[i]);
                arrayList.add(Double.parseDouble(String.format("%.3f",f[i])));
        }
        return arrayList;
    }
}
