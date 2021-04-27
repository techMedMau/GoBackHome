package scene.popupwindow;

import controllers.ImageController;
import controllers.TaskController;
import gameobj.Alien;
import gameobj.button.Button;
import utils.CommandSolver;

import java.awt.*;
import java.util.ArrayList;

public class PopUpLineUp extends PopUpTask {

    private ArrayList<KeyPair> keyPairs;
    private Button tmp;
    private boolean line1;
    private boolean line2;
    private boolean line3;
    private boolean line4;
    private Image hint;


    @Override
    public void sceneBegin(){
        super.sceneBegin();
        keyPairs = new ArrayList<>();
        this.keyPairs.add(new KeyPair(new Button(275,225,48,48,ImageController.getInstance().tryGet("/lineUp/gold.png"))
                , new Button(575,345,48,48,ImageController.getInstance().tryGet("/lineUp/gold.png"))));
        this.keyPairs.add(new KeyPair(new Button(275,285,48,48,ImageController.getInstance().tryGet("/lineUp/diamond.png"))
                , new Button(575,405,48,48,ImageController.getInstance().tryGet("/lineUp/diamond.png"))));
        this.keyPairs.add(new KeyPair(new Button(275,345,48,48,ImageController.getInstance().tryGet("/lineUp/iron.png"))
                , new Button(575,285,48,48,ImageController.getInstance().tryGet("/lineUp/iron.png"))));
        this.keyPairs.add(new KeyPair(new Button(275,405,48,48,ImageController.getInstance().tryGet("/lineUp/ruby.png"))
                , new Button(575,225,48,48,ImageController.getInstance().tryGet("/lineUp/ruby.png"))));
        this.line1 = false;
        this.line2 = false;
        this.line3 = false;
        this.line4 = false;
        hint = ImageController.getInstance().tryGet("/findDifferent/finddif.png");
    }
    @Override
    public void sceneEnd(){
        keyPairs=null;
        tmp=null;
        super.sceneEnd();
    }


    @Override
    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(hint, 280,100, null);
        if(line1) {
            g.drawLine(299, 249, 599, 369);
        }
        if(line2){
            g.drawLine(299,309,599,429);
        }
        if(line3){
            g.drawLine(299,369,599,309);
        }
        if(line4){
            g.drawLine(299,429,599,249);
        }
        for(int i = 0; i < keyPairs.size(); i ++){
            keyPairs.get(i).button.paint(g);
            keyPairs.get(i).button1.paint(g);
        }
        if(isDone()){
            g.drawImage(finish, 350,125,null);
        }
    }

    @Override
    public void update(){
        super.update();
        if(line1 && line2 && line3 && line4){
            setDone(true);
        }
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (!isDone()){
                switch (state){
                    case CLICKED:
                        int i;
                        for(i = 0; i < keyPairs.size(); i++) {
                            if (tmp==null){
                                if (keyPairs.get(i).button.state(e.getPoint())) {
                                    tmp = keyPairs.get(i).button1;
                                    return;
                                }
                                if (keyPairs.get(i).button1.state(e.getPoint())) {
                                    tmp = keyPairs.get(i).button;
                                    return;
                                }
                            }
                        }
                        for(int k = 0; k < keyPairs.size(); k++) {
                            if (tmp == keyPairs.get(k).button1 && keyPairs.get(k).button1.state(e.getPoint())||
                                    tmp == keyPairs.get(k).button && keyPairs.get(k).button.state(e.getPoint())) {
                                if(k == 0){
                                    line1 = true;
                                }
                                if(k == 1){
                                    line2 = true;
                                }
                                if(k == 2){
                                    line3 = true;
                                }
                                if(k == 3){
                                    line4 = true;
                                }
                                break;
                            }
                        }
                        tmp = null;
                        break;
                }
            }
            super.mouseListener().mouseTrig(e,state,trigTime);
        };
    }

    private static class KeyPair{
        private Button button;
        private Button button1;
        public KeyPair(Button button,Button button1){
            this.button = button;
            this.button1 = button1;
        }
    }

}
