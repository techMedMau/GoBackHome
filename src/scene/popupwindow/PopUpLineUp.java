package scene.popupwindow;

import controllers.ImageController;

import java.awt.*;
import java.util.ArrayList;

public class PopUpLineUp extends PopUpTask {
    private ArrayList<Image> images;

    public PopUpLineUp(){
        images = new ArrayList<>();
        images.add(ImageController.getInstance().tryGet("/lineUp/diamond.png"));
        images.add(ImageController.getInstance().tryGet("/lineUp/gold.png"));
        images.add(ImageController.getInstance().tryGet("/lineUp/iron.png"));
        images.add(ImageController.getInstance().tryGet("/lineUp/ruby.png"));
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        for(int i = 0; i < images.size(); i++) {
            g.drawImage(images.get(i), 200, 100+ i*5, null);
        }
    }



}
