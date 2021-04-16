package gameobj;

import controllers.ImageController;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class Rock extends GameObject {

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
    private int x;
    private int y;
    private Picture pic;

    public Rock() {
        super(Global.random(0,960), Global.random(0,640), 32, 32);
        x = Global.random(1,4);
        pic = Picture.values()[Global.random(0,4)];
        img = ImageController.getInstance().tryGet(pic.path);
    }

    public void moveDir(){
                this.translateX(1);
                this.translateY(1);
    }

    public boolean isShow(){
        return painter().top()>=140 || painter().left()>= 250
                || painter().bottom()<=473 || painter().right() <= 650;
    }

    public boolean isOutside(){
        return this.painter().left()<250 || this.painter().right()>650
                || this.painter().bottom()>473 || this.painter().top()<140;
    }

    public boolean isClicked(Point point){
        return point.getX() < 650 && point.getX() > 140 && point.getY() > 140 && point.getY() < 473;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, painter().left(),painter().top(),null);
    }

    @Override
    public void update() {
        moveDir();
    }
}
