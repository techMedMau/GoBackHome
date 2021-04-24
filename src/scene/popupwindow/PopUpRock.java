package scene.popupwindow;

import controllers.ImageController;
import utils.CommandSolver;

import java.awt.*;
import java.util.ArrayList;

import gameobj.Rock;
import utils.Global;

public class PopUpRock extends PopUpTask{
    private Image background;
    private ArrayList<Rock> rocks;
    @Override
    public void sceneBegin(){
        super.sceneBegin();
        this.background = ImageController.getInstance().tryGet("/rock/taskBackground.png");
        this.rocks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            rocks.add(new Rock());
        }
    }
    @Override
    public void sceneEnd(){
        background=null;
        rocks=null;
        super.sceneEnd();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(background, 250,140, null);
        for(int i = 0; i < rocks.size(); i++) {
            if(rocks.get(i).isShow()) {
                rocks.get(i).paint(g);
            }
        }
        if(isDone()){
            g.drawImage(finish, 350,125,null);
        }
    }

    @Override
    public void update() {
        super.update();
        for(int i = 0; i < rocks.size(); i++) {
            rocks.get(i).update();
            if(rocks.get(i).isOutside()){
                int x = 250+32;
                int y = Global.random(150,450);
                rocks.get(i).painter().setCenter(x,y);
                rocks.get(i).collider().setCenter(x,y);
            }
        }

        if(rocks.size() == 0){
            setDone(true);
        }
    }


    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (!isDone()){
                switch (state){
                    case CLICKED:
                        for(int i = 0; i < rocks.size(); i++){
                            if(rocks.get(i).state(e.getPoint())){
                                rocks.remove(i);
                                break;
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
