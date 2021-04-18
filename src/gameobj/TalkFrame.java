package gameobj;


import java.awt.*;

public class TalkFrame extends GameObject{

    public TalkFrame(int x, int y) {
        super(x, y, 258, 198);

    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.GRAY);
        g.drawRect(left(),top(),258,198);


    }

    @Override
    public void update() {

    }
}
