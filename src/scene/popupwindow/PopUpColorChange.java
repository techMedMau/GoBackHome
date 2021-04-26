package scene.popupwindow;

import controllers.ImageController;
import controllers.TaskController;
import utils.CommandSolver;
import java.awt.*;
import gameobj.button.Button;

public class PopUpColorChange extends PopUpTask {
    private Image img;
    private Image img2;
    private Button button;
    private Button button2;
    private Image green;
    private Image yellow;
    private boolean click;
    private boolean click2;
    private Image finish;

    @Override
    public void sceneBegin() {
        super.sceneBegin();
        this.finish = ImageController.getInstance().tryGet("/password/finish.png");
        this.img = ImageController.getInstance().tryGet("/colorChange/purple.png");
        this.img2 = ImageController.getInstance().tryGet("/colorChange/red.png");
        this.green = ImageController.getInstance().tryGet("/colorChange/green.png");
        this.yellow = ImageController.getInstance().tryGet("/colorChange/yellow.png");
        this.button = new Button(300,250, 80,80
                , ImageController.getInstance().tryGet("/colorChange/greenTrans.png"));
        this.button2 = new Button(400,400, 80,80
                , ImageController.getInstance().tryGet("/colorChange/yellowTrans.png"));
    }
    @Override
    public void sceneEnd(){
        img=null;
        img2=null;
        button=null;
        button2=null;
        green=null;
        yellow=null;
        finish=null;
        click=false;
        click2=false;
        super.sceneEnd();

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(img, 500,200, null); //紫
        g.drawImage(img2, 600,300, null); //紅
        button.paint(g);
        button2.paint(g);
        if(click){
            g.drawImage(green, 300,250,null); //綠
        }
        if(click2) {
            g.drawImage(yellow, 400, 400, null); //黃
        }
        if(isDone()){
            g.drawImage(finish, 350,125,null);
        }
    }

    @Override
    public void update() {
        super.update();
        if(click && click2){
            setDone(true);
        }
    }


    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (!isDone()){
                switch (state){
                    case CLICKED:
                        if(button.state(e.getPoint())){
                            click = true;
                            return;
                        }
                        if(button2.state(e.getPoint())){
                            click2 = true;
                            return;
                        }
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
