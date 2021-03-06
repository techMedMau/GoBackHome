package scene.popupwindow;

import controllers.ImageController;
import gameobj.GameObject;
import utils.CommandSolver;
import gameobj.button.Button;

import java.awt.*;

public class PopUpCenter extends PopUpTask{
    private Image img;
    private GameObject aim;
    private int x;
    private int y;
    private boolean isControlled;
    private Image hint;

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.drawImage(img, 260, 100, null);
        g.drawImage(hint,350,70,null);
        aim.paint(g);
        if(isDone()){
            g.drawImage(finish, 350,125,null);
        }
    }

    @Override
    public void update() {
        super.update();
        if(450<aim.collider().centerX()&&aim.collider().centerX()<470&&
                aim.collider().centerY()>290&&aim.collider().centerY()<310){
            setDone(true);
        }
    }

    @Override
    public void sceneBegin() {
        super.sceneBegin();
        this.img = ImageController.getInstance().tryGet("/center/paper.png");
        this.x = 300;
        this.y = 300;
        this.aim = new GameObject(x,y,50,50,x,y,200,200) {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(ImageController.getInstance().tryGet("/center/aim.png"), x-100, y-100, null );
            }

            @Override
            public void update() {

            }
        };
        isControlled = false;
        this.hint = ImageController.getInstance().tryGet("/center/center.png");
    }

    @Override
    public void sceneEnd() {
        aim=null;
        img=null;
        super.sceneEnd();


    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (!isDone()){
                switch (state){
                    case DRAGGED:
                        if(isControlled){
                            x = (int)(e.getPoint().getX());
                            y = (int)(e.getPoint().getY());
                            aim.collider().setCenter(x,y);
                            aim.painter().setCenter(x,y);
                        }
                        break;
                    case PRESSED:
                        if(e.getPoint().getX()<aim.collider().right() && e.getPoint().getX()>aim.collider().left()
                                && e.getPoint().getY()>aim.collider().top() && e.getPoint().getY()<aim.collider().bottom()){
                            isControlled = true;
                        }
                        break;
                    case RELEASED:
                        isControlled = false;
                        break;
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
