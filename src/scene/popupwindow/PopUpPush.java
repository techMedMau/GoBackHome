package scene.popupwindow;

import controllers.ImageController;
import utils.CommandSolver;

import java.awt.*;

import gameobj.button.Button;

public class PopUpPush extends PopUpTask {
    private Image img;
    private Button dot;
    private Image hint;

    @Override
    public void sceneBegin() {
        super.sceneBegin();
        this.img = ImageController.getInstance().tryGet("/push/push.png");
        this.dot = new Button(460, 370, 80, 80
                , ImageController.getInstance().tryGet("/push/yellow.png"));
        hint = ImageController.getInstance().tryGet("/push/pushHint.png");
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
        g.drawImage(hint, 360, 100, null);
        g.drawImage(img, 305, 200, null);
        dot.paint(g);
        if (isDone()) {
            g.drawImage(finish, 350, 125, null);
        }
    }

    @Override
    public void update() {
        super.update();
        if (460 <= dot.centerX() && dot.centerX() <= 540
                && 305 >= dot.centerY() && dot.centerY() >= 285) {
            setDone(true);
        }
    }


    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (!isDone()){
                switch (state) {
                    case DRAGGED:
                        if (dot.state(e.getPoint()) && 440 >= e.getY() && e.getY() >= 260) {
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
