package scene.popupwindow;

import controllers.ImageController;
import gameobj.Nums;
import gameobj.button.Num;
import gameobj.button.XMark;
import utils.CommandSolver;
import scene.Scene;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class PopUpCreateRoom extends PopUpWindows{
    private Image img;
    private XMark xMark;
    private Nums traitorNums;
    private Nums playNums;
    public PopUpCreateRoom() {
        super(485,276);
    }

    @Override
    public void sceneBegin() {
        xMark=new XMark(660,220,50);
        img= ImageController.getInstance().tryGet("/popupcreat.png");
        show();
        traitorNums=new Nums(2,380,220);
        playNums=new Nums(8,280,350);
    }

    @Override
    public void sceneEnd() {
        disShow();

    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (state == null) {
                return;
            }
            switch (state) {
                case CLICKED:
                    if (xMark.state(e.getPoint())){
                        sceneEnd();
                        break;
                    }
                    System.out.println("AA");
                    traitorNums.show(e.getPoint());
                    playNums.show(e.getPoint());
                    break;
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,(Global.WINDOW_WIDTH - getWidth()) / 2, (Global.WINDOW_HEIGHT - getHeight()) / 2, null);
        xMark.paint(g);
        traitorNums.paint(g);
        playNums.paint(g);
    }

    @Override
    public void update() {

    }
}
