package controllers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//單例模式的圖片管理者
//需求: 所有圖片由圖片管理者掌控
//      且每張圖片只會產生一次
public class ImageController {
    private static ImageController imageController; //靜態實體
    private HashMap<String,Image> keyPairs;
    private ImageController(){//私有化建構子，杜絕外部new
        keyPairs=new HashMap<>();
    }

    //單例模式靜態方法;取得實體-->且只會產生一個實體
    public static ImageController getInstance(){
        if(imageController==null){
            imageController=new ImageController();
        }
        return imageController;
    }

    //加入圖片的方法，傳入路徑，創建圖片-->私有化
    private Image add(String path){
        Image img=null;
        try {
            img=ImageIO.read(getClass().getResource(path));
            this.keyPairs.put(path,img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
    //取得陣列中圖片的方法-->根據路徑找該圖片
    public Image tryGet(String path){
        if(this.keyPairs.containsKey(path)){ //假如傳進來的路徑相等
            return this.keyPairs.get(path);
        }
        return add(path);
    }


}
