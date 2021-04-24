package scene.popupwindow;

import controllers.ImageController;
import utils.CommandSolver;

import java.awt.*;
import gameobj.button.Button;

public class PopUpPush extends PopUpTask{
    private Image img;
    private Button dot;
    private Button dotTest;

    @Override
    public void sceneBegin(){
        super.sceneBegin();
        this.img = ImageController.getInstance().tryGet("/push/push.png");
        this.dot = new Button(465,350,80,80
                , ImageController.getInstance().tryGet("/push/yellow.png"));
        this.dotTest = new Button(465,190,80,80
                , ImageController.getInstance().tryGet("/push/yellow.png"));
    }
    @Override
    public void sceneEnd(){
        img=null;
        dot=null;
        dotTest=null;
        super.sceneEnd();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(img, 310,140, null);
        if(!isDone()) {
            dot.paint(g);
        }
        if(isDone()) {
            dotTest.paint(g);
            g.drawImage(finish, 350,125,null);
        }
    }

    @Override
    public void update() {
        super.update();
    }


    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            switch (state){
                case CLICKED:
                    if(510 >= e.getPoint().getX() && e.getPoint().getX() >= 500
                            && 235 >= e.getPoint().getY() && e.getPoint().getY() >= 225){
                        setDone(true);
                    }
                    super.mouseListener().mouseTrig(e,state,trigTime);
                    break;
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

}
