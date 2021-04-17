package scene.popupwindow;

import controllers.ImageController;
import gameobj.button.Button;
import utils.CommandSolver;

import java.awt.*;

public class PopUpTask extends PopUpWindows{
    public Button close;
    public Image img;
    public Image finish;
    private boolean isDone;


    public PopUpTask() {
        super(600, 500);
    }

    @Override
    public void sceneBegin() {
        this.img = ImageController.getInstance().tryGet("/sheet.png");
        finish = ImageController.getInstance().tryGet("/password/finish.png");
        close=new gameobj.button.Button(110,50,48,48, ImageController.getInstance().tryGet("/button/close.png"));
        this.isDone = false;
        show();
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            switch (state){
                case CLICKED:
                    if(!getShow()){
                        show();
                        break;
                    }
                    if (close.state(e.getPoint())){
                        disShow();
                        break;
                    }

                    break;
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,158,50,null);
        close.paint(g);
    }

    @Override
    public void update() {
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isDone() {
        return isDone;
    }

    //    public int left(){
//        return img.
//    }
}
