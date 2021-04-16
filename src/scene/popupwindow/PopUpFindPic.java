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
    private boolean isDone;

    @Override
    public void sceneBegin() {
        super.sceneBegin();
        img = ImageController.getInstance().tryGet("/findPic/blue.png");
        buttons = new ArrayList<>();
        for(int i = 0; i < 4; i ++){
            buttons.add(new Button(280 + i *100, 270,64,64
                    , ImageController.getInstance().tryGet("/findPic/transparent.png")));
        }
        isDone = false;
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            switch (state){
                case CLICKED:
                    super.mouseListener().mouseTrig(e,state,trigTime);
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
        super.paint(g);
        g.drawImage(img, 430,150,null);
        for(int i = 0; i < buttons.size(); i++) {
            buttons.get(i).paint(g);
        }
    }

    @Override
    public void update() {
    }
}
