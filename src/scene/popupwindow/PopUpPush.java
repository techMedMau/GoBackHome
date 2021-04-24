package scene.popupwindow;

import controllers.ImageController;
import utils.CommandSolver;

import java.awt.*;

import gameobj.button.Button;

public class PopUpPush extends PopUpTask {
    private Image img;
    private Button dot;

    @Override
    public void sceneBegin() {
        super.sceneBegin();
        this.img = ImageController.getInstance().tryGet("/push/push.png");
        this.dot = new Button(465, 350, 80, 80
                , ImageController.getInstance().tryGet("/push/yellow.png"));
    }

    @Override
    public void sceneEnd() {
        img = null;
        dot = null;
        super.sceneEnd();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(img, 310, 140, null);
        dot.paint(g);
        if (isDone()) {
            g.drawImage(finish, 350, 125, null);
        }
    }

    @Override
    public void update() {
        super.update();
        if (510 >= dot.centerX() && dot.centerX() >= 500
                && 235 >= dot.centerY() && dot.centerY() >= 225) {
            setDone(true);
        }
    }


    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (!isDone()){
                switch (state) {
                    case DRAGGED:
                        if (dot.state(e.getPoint()) && 400 >= e.getY() && e.getY() >= 170) {
                            dot.painter().setCenter(dot.centerX(), e.getY());
                            dot.collider().setCenter(dot.centerX(), e.getY());
                        }
                }
            }
            super.mouseListener().mouseTrig(e,state,trigTime);
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

}
