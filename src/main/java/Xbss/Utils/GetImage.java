package Xbss.Utils;

import javafx.scene.image.Image;

import java.io.File;

/**
 * @author Xbss
 * @version 1.0
 * @create 2022-06-11-22:51
 * @descirbe
 */
public class GetImage {
    public static Image getImage(String RelativePath){
        String pro_path = System.getProperty("user.dir");
        File file = new File(pro_path+"/"+RelativePath);
        return new Image(file.toURI().toString());
    }
    public static Image getImage(String RelativePath,  Double width, Double height ){
        String pro_path = System.getProperty("user.dir");
        File file = new File(pro_path+"/"+RelativePath);
        return new Image(file.toURI().toString(),width,height,false,false);
    }
}
