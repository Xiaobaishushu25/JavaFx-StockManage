package Xbss.Test;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-07-29-10:19
 * @describe
 */
public class testList {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(7);
        list.add(8);
//        list.remove(0);
        new Thread(()->{
            for (Integer i : list) {
                System.out.println(i);
            }
        }).start();
//        Iterator<Integer> iterator = list.iterator();
//        while (iterator.hasNext()){
//            Integer next = iterator.next();
//            if (next==3)
//                iterator.remove();
//        }
//            iterator.remove();
        for (int i = 0; i < list.size(); i++) {
            if (i==list.size()-1){
                list.remove(i);
            }
        }
        for (Integer i : list) {
            System.out.println(i);
        }
    }

}
