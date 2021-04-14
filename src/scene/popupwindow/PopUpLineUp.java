package scene.popupwindow;

import controllers.ImageController;
import gameobj.button.Button;
import utils.CommandSolver;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PopUpLineUp extends PopUpTask {

    private ArrayList<KeyPair> keyPairs;
    private Button tmp;
    private boolean line;


    public PopUpLineUp(){
        keyPairs = new ArrayList<>();

        this.keyPairs.add(new KeyPair(new Button(300,100,48,48,ImageController.getInstance().tryGet("/lineUp/gold.png"))
                , new Button(600,300,48,48,ImageController.getInstance().tryGet("/lineUp/gold.png"))));
        this.keyPairs.add(new KeyPair(new Button(300,200,48,48,ImageController.getInstance().tryGet("/lineUp/diamond.png"))
                , new Button(600,100,48,48,ImageController.getInstance().tryGet("/lineUp/diamond.png"))));
        this.keyPairs.add(new KeyPair(new Button(300,300,48,48,ImageController.getInstance().tryGet("/lineUp/iron.png"))
                , new Button(600,400,48,48,ImageController.getInstance().tryGet("/lineUp/iron.png"))));
        this.keyPairs.add(new KeyPair(new Button(300,400,48,48,ImageController.getInstance().tryGet("/lineUp/ruby.png"))
                , new Button(600,200,48,48,ImageController.getInstance().tryGet("/lineUp/ruby.png"))));
    }



    @Override
    public void paint(Graphics g){
        super.paint(g);
        for(int i = 0; i < keyPairs.size(); i ++){
            keyPairs.get(i).button.paint(g);
            keyPairs.get(i).button1.paint(g);
        }
    }

    @Override
    public void update(){

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
                        break;
                    }
                    for(int k = 0; k < keyPairs.size(); k++) {
                        if (tmp == keyPairs.get(k).button1 && keyPairs.get(k).button1.state(e.getPoint())) {
                            System.out.println("correct");
                        }
                     }
                    tmp = null;



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
