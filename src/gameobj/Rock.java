package gameobj;

import controllers.ImageController;
import utils.Global;

import java.awt.*;
import gameobj.button.Button;

public class Rock extends Button {

    public enum Picture{
        A("/rock/1.png"),
        B("/rock/2.png"),
        C("/rock/3.png"),
        D("/rock/4.png"),
        E("/rock/5.png");

        String path;

        Picture(String path){
            this.path = path;
        }
    }

    private Image img;
    private Picture pic;

    public Rock() {
        super(250, Global.random(140,473), 32, 32,
                ImageController.getInstance().tryGet(Picture.values()[Global.random(0,4)].path));

    }

    public void moveDir(){
        this.translateX(1);
        if (left()<450){
            this.translateY(-1);
        }else if (left()>500){
            this.translateY(1);
        }
    }

    public boolean isShow(){
        return painter().top()>=140 || painter().left()>= 250
                || painter().bottom()<=473 || painter().right() <= 650;
    }

    public boolean isOutside(){
        return this.painter().left()<250 || this.painter().right()>650
                || this.painter().bottom()>473 || this.painter().top()<140;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void update() {
        moveDir();
    }
}
