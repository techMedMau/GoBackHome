package gameobj;

import utils.Delay;

import java.awt.*;

public class InputLine extends GameObject{
    private Delay delay;
    private boolean show;
    private int stringWidth;
    private int stringHeight;

    public InputLine(int x,int y,int height) {
        super(x, y, 1, height);
        delay=new Delay(30);
        delay.loop();
        stringWidth=0;
        stringHeight=0;
    }
    public void setString(int stringWidth,int stringHeight){
        this.stringWidth=stringWidth;
        this.stringHeight=stringHeight;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (delay.count()){
            show=!show;
        }
        if (show){
            g.drawLine(painter().left()+stringWidth,painter().top()+stringHeight,painter().left()+stringWidth,painter().bottom()+stringHeight);
        }
    }

    @Override
    public void update() {

    }
}
