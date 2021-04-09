package gameobj;

import utils.Delay;

import java.awt.*;

public class InputLine extends GameObject{
    private Delay delay;
    private boolean show;
    public InputLine(int x,int y,int height) {
        super(x, y, 1, height);
        delay=new Delay(30);
        delay.loop();
    }

    @Override
    public void paintComponent(Graphics g) {
        if (delay.count()){
            show=!show;
        }
        if (show){
            g.drawLine(painter().left(),painter().top(),painter().left(),painter().bottom());
        }
    }

    @Override
    public void update() {

    }
}
