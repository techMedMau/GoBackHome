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
    private Button gold;
    private Button diamond;
    private Button iron;
    private Button ruby;
    private Button gold2;
    private Button diamond2;
    private Button iron2;
    private Button ruby2;
    private Button tmp;


    public PopUpLineUp(){
        keyPairs = new ArrayList<>();
        gold = new Button(300,100,48,48,ImageController.getInstance().tryGet("/lineUp/gold.png"));
        diamond = new Button(300,200,48,48,ImageController.getInstance().tryGet("/lineUp/diamond.png"));
        iron = new Button(300,300,48,48,ImageController.getInstance().tryGet("/lineUp/iron.png"));
        ruby = new Button(300,400,48,48,ImageController.getInstance().tryGet("/lineUp/ruby.png"));
        ruby2 = new Button(600,200,48,48,ImageController.getInstance().tryGet("/lineUp/ruby.png"));
        gold2 = new Button(600,300,48,48,ImageController.getInstance().tryGet("/lineUp/gold.png"));
        diamond2 = new Button(600,100,48,48,ImageController.getInstance().tryGet("/lineUp/diamond.png"));
        iron2 = new Button(600,400,48,48,ImageController.getInstance().tryGet("/lineUp/iron.png"));
        this.keyPairs.add(new KeyPair(new Button(300,100,48,48,ImageController.getInstance().tryGet("/lineUp/gold.png"))
                , new Button(600,300,48,48,ImageController.getInstance().tryGet("/lineUp/gold.png"))));
        this.keyPairs.add(new KeyPair(new Button(300,200,48,48,ImageController.getInstance().tryGet("/lineUp/diamond.png"))
                , new Button(600,100,48,48,ImageController.getInstance().tryGet("/lineUp/diamond.png"))));
        this.keyPairs.add(new KeyPair(iron, iron2));
        this.keyPairs.add(new KeyPair(ruby, ruby2));
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
