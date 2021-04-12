package gameobj.button;

import utils.Global;

import java.awt.*;

public class XMark extends Button{
    private int size;
    public XMark(int x, int y, int size) {
        super(x, y, 28,28, null);
        this.size=size;
    }
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        Font font=new Font(Global.FONT,Font.PLAIN,size);
        g.setFont(font);
        g.drawString("×", painter().left(), painter().bottom());
    }
}
