package scene.popupwindow;

import controllers.ImageController;
import utils.CommandSolver;
import gameobj.button.Button;
import scene.Scene;

import java.awt.*;
import java.util.ArrayList;

public class PopUpFindPic extends PopUpTask{

    private Image img;
    private ArrayList<Button> buttons;
    private ArrayList<Button> buttonsAfter;
    private Button tmp;
    private boolean isDone;


    @Override
    public void sceneBegin() {
        super.sceneBegin();
        img = ImageController.getInstance().tryGet("/findPic/blue.png");
        buttons = new ArrayList<>();
        buttonsAfter = new ArrayList<>();
        for(int i = 0; i < 4; i ++){
            buttons.add(new Button(280 + i *100, 270,64,64
                    , ImageController.getInstance().tryGet("/findPic/transparent.png")));
        }
        buttonsAfter.add(new Button(280, 270,64,64
                , ImageController.getInstance().tryGet("/findPic/green.png")));
        buttonsAfter.add(new Button(380, 270,64,64
                , ImageController.getInstance().tryGet("/findPic/blue.png")));
        buttonsAfter.add(new Button(480, 270,64,64
                , ImageController.getInstance().tryGet("/findPic/yellow.png")));
        buttonsAfter.add(new Button(580, 270,64,64
                , ImageController.getInstance().tryGet("/findPic/red.png")));
        isDone = false;
    }

    @Override
    public void sceneEnd() {
        img=null;
        buttons=null;
        buttonsAfter=null;
        tmp=null;
        super.sceneEnd();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(img, 430,150,null);
        for(int i = 0; i < buttons.size(); i++) {
            buttons.get(i).paint(g);
            if(tmp == buttons.get(i)){
                buttonsAfter.get(i).paint(g);
            }
        }
        if(isDone()){
            g.drawImage(finish, 350,125,null);
        }
    }

    @Override
    public void update() {
        super.update();
           if(tmp == buttons.get(1)){
               setDone(true);
           }
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (!isDone()){
                switch (state){
                    case CLICKED:
                        for(int i = 0 ; i < buttons.size(); i ++){
                            if(buttons.get(i).state(e.getPoint())){
                                tmp = buttons.get(i);
                                return;
                            }
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
