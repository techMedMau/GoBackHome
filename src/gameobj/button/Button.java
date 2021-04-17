package gameobj.button;

import controllers.ImageController;
import gameobj.GameObject;


import java.awt.*;

public class Button extends GameObject implements ClickState{
    private Image img;


    public Button(int x, int y, int width, int height, Image img) {
        super(x, y, width, height);
        this.img=img;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(String path) {
        this.img = ImageController.getInstance().tryGet(path);
    }

    @Override
    //小任務
    public boolean state(Point point){
        return point.getX()>painter().left() && point.getX()<painter().right()&&
                point.getY()>painter().top() && point.getY()<painter().bottom();
    }

    //任務箱子
    @Override
    public boolean state(int x,int y){
        return x>painter().left() && x<painter().right()&&
                y>painter().top() && y<painter().bottom();
    }
    @Override
    public void update() {
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img,painter().left(),painter().top(),null);

    }
}
