package scene.popupwindow;

import controllers.ImageController;
import utils.CommandSolver;
import gameobj.button.Button;
import utils.Global;

import java.awt.*;

public class PopUpFindPray extends PopUpWindows{
    private Button test;
    public PopUpFindPray() {
        super(800,500);
    }

    @Override
    public void sceneBegin() {
        test=new Button(200,200,186,73, ImageController.getInstance().tryGet("/confirm.png"));
        show();

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
            switch (state){
                case CLICKED:
                    if (test.state(e.getPoint())){
                    }
                    break;
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawRect((Global.WINDOW_WIDTH-getWidth())/2,(Global.WINDOW_HEIGHT-getHeight())/2,getWidth(),getHeight());
        test.paint(g);

    }

    @Override
    public void update() {

    }
}
