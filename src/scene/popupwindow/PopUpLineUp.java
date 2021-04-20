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
    private ArrayList<Image> lines;
    private boolean line1;
    private boolean line2;
    private boolean line3;
    private boolean line4;
    private TaskController.Task task;

    public PopUpLineUp(){
        keyPairs = new ArrayList<>();
        lines = new ArrayList<>();
        this.keyPairs.add(new KeyPair(new Button(300,100,48,48,ImageController.getInstance().tryGet("/lineUp/gold.png"))
                , new Button(600,300,48,48,ImageController.getInstance().tryGet("/lineUp/gold.png"))));
        this.keyPairs.add(new KeyPair(new Button(300,200,48,48,ImageController.getInstance().tryGet("/lineUp/diamond.png"))
                , new Button(600,100,48,48,ImageController.getInstance().tryGet("/lineUp/diamond.png"))));
        this.keyPairs.add(new KeyPair(new Button(300,300,48,48,ImageController.getInstance().tryGet("/lineUp/iron.png"))
                , new Button(600,400,48,48,ImageController.getInstance().tryGet("/lineUp/iron.png"))));
        this.keyPairs.add(new KeyPair(new Button(300,400,48,48,ImageController.getInstance().tryGet("/lineUp/ruby.png"))
                , new Button(600,200,48,48,ImageController.getInstance().tryGet("/lineUp/ruby.png"))));
        this.line1 = false;
        this.line2 = false;
        this.line3 = false;
        this.line4 = false;
        this.task = TaskController.Task.LINE_UP;
    }


    @Override
    public void paint(Graphics g){
        super.paint(g);
        for(int i = 0; i < keyPairs.size(); i ++){
            keyPairs.get(i).button.paint(g);
            keyPairs.get(i).button1.paint(g);
        }

        if(line1) {
            g.drawLine(348, 124, 600, 324);
        }
        if(line2){
            g.drawLine(348,224,600,124);
        }
        if(line3){
            g.drawLine(348,324,600,424);
        }
        if(line4){
            g.drawLine(348,424,600,224);
        }
        if(isDone()){
            g.drawImage(finish, 350,125,null);
        }
    }

    @Override
    public void update(){
        if(line1 && line2 && line3 && line4){
            setDone(true);
        }
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            switch (state){
                case CLICKED:
                    super.mouseListener().mouseTrig(e,state,trigTime);
                    int i;
                    for(i = 0; i < keyPairs.size(); i++) {
                        if (keyPairs.get(i).button.state(e.getPoint())) {
                            tmp = keyPairs.get(i).button1;
                            break;
                        }
                    }
                    if (i<keyPairs.size()){
                        System.out.println("!");
                        break;
                    }
                    System.out.println(tmp);
                    for(int k = 0; k < keyPairs.size(); k++) {
                        if (tmp == keyPairs.get(k).button1 && keyPairs.get(k).button1.state(e.getPoint())) {
//                            System.out.println(keyPairs.get(k).button1);
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
                        }
                     }
                    tmp = null;
//
                    break;
            }
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
