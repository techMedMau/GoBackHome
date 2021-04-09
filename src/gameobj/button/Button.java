package gameobj.button;

import gameobj.GameObject;

import java.awt.*;

public class Button extends GameObject {
    private Image img;

    public Button(int x, int y, int width, int height, Image img) {
        super(x, y, width, height);
        this.img=img;
    }
    public boolean state(Point point){
        return point.getX()>painter().left()&&point.getX()<painter().right()&&
                point.getY()>painter().top()&&point.getY()<painter().bottom();
    }
    @Override
    public void update() {
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img,painter().left(),painter().top(),null);

    }
}
