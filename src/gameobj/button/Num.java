package gameobj.button;

import controllers.ImageController;
import utils.Global;

import java.awt.*;

public class Num extends Button {
    private boolean show;
    private String str;
    public Num(String str,int x, int y) {
        super(x, y, 51, 51, ImageController.getInstance().tryGet("/openScene/num.png"));
        this.str=str;
        this.show=false;
    }
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        Font font=new Font(Global.FONT,Font.PLAIN,36);
        g.setFont(font);
        g.drawString(str, painter().centerX()-8, painter().centerY()+13);
        if (show){
            super.paintComponent(g);
        }
    }
    public void show(){
        show=true;
    }
    public void disShow(){
        show=false;
    }
    public String getStr(){
        return str;
    }

}
