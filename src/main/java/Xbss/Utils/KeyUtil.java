package Xbss.Utils;


import java.util.Random;

public class KeyUtil {
    /**
     * 生成唯一的主键
     * 格式:时间+随机数
     * */
    public static synchronized String genUniqueKey(){//为了防止重复 加个synchronized
        Random random=new Random();
        Integer number=random.nextInt(900)+100;
        System.out.println(System.currentTimeMillis());
        return System.currentTimeMillis()+String.valueOf(number);
    }
}