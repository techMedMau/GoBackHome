package scene.popupwindow;

import controllers.ImageController;
import gameobj.Card;
import utils.CommandSolver;
import gameobj.button.Button;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;


public class PopUpFindDifferent extends PopUpTask{
    private ArrayList<KeyPair> keyPairs;
    private ArrayList<Card> cards;
    private Button tmp;
    private boolean a;
    private boolean b;
    private boolean c;
    private boolean d;
    private boolean e;
    private boolean f;

    @Override
    public void sceneBegin() {
        super.sceneBegin();
        cards = new ArrayList<>();
        this.keyPairs = new ArrayList<>();
        this.keyPairs.add(new KeyPair(new Card(270, 200,32,38
                        ,ImageController.getInstance().tryGet("/findDifferent/cloud.png"))
                ,new Card(480,400,32,38
                        ,ImageController.getInstance().tryGet("/findDifferent/cloud.png"))));

        this.keyPairs.add(new KeyPair(new Card(340, 200,32,38
                        , ImageController.getInstance().tryGet("/findDifferent/bulb.png")),
                new Card(270, 400,32,38
                        , ImageController.getInstance().tryGet("/findDifferent/bulb.png"))));

        this.keyPairs.add(new KeyPair(new Card(410, 200,32,38
                        , ImageController.getInstance().tryGet("/findDifferent/drop.png")),
                new Card(620, 400,32,38
                        , ImageController.getInstance().tryGet("/findDifferent/drop.png"))));

        this.keyPairs.add(new KeyPair(new Card(480, 200,32,38
                , ImageController.getInstance().tryGet("/findDifferent/haha.png")),
                new Card(410, 400,32,38
                        , ImageController.getInstance().tryGet("/findDifferent/haha.png"))));

        this.keyPairs.add(new KeyPair(new Card(550, 200,32,38
                , ImageController.getInstance().tryGet("/findDifferent/heart.png")),
                new Card(340, 400,32,38
                        , ImageController.getInstance().tryGet("/findDifferent/heart.png"))));

        this.keyPairs.add(new KeyPair(new Card(620, 200,32,38
                , ImageController.getInstance().tryGet("/findDifferent/star.png")),
                new Card(550, 400,32,38
                        , ImageController.getInstance().tryGet("/findDifferent/star.png"))));
        a = b = c = d = e = f = false;
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
                    for(int i = 0; i < keyPairs.size(); i ++){
                        keyPairs.get(i).button.changeState(e.getPoint());
                    }
                    for(int i = 0; i < keyPairs.size(); i ++){
                        keyPairs.get(i).button1.changeState(e.getPoint());
                    }

                    int i;
                    for(i = 0; i < keyPairs.size(); i++) {
                        if (keyPairs.get(i).button.state(e.getPoint())) {
                            tmp = keyPairs.get(i).button1;
                            break;
                        }
                        if(keyPairs.get(i).button1.state(e.getPoint())) {
                            tmp = keyPairs.get(i).button;
                            break;
                        }
                    }

                    if (i<keyPairs.size()){
                        break;
                    }

                    for(int k = 0; k < keyPairs.size(); k++) {
                        if (tmp == keyPairs.get(k).button1 && keyPairs.get(k).button1.state(e.getPoint())) {

                            keyPairs.get(0).button.setState(Card.State.SHOW);
                            keyPairs.get(0).button1.setState(Card.State.SHOW);

                        }
                        if (tmp == keyPairs.get(k).button && keyPairs.get(k).button.state(e.getPoint())) {
                            keyPairs.get(k).button.setState(Card.State.SHOW);
                            keyPairs.get(k).button1.setState(Card.State.SHOW);

                        }
                    }
//                    for(int k = 0; k < keyPairs.size(); k++) {
//                        if (tmp == keyPairs.get(k).button && keyPairs.get(k).button.state(e.getPoint())) {
//                                keyPairs.get(k).button.setState(Card.State.SHOW);
//                                keyPairs.get(k).button1.setState(Card.State.SHOW);
//
//                        }
//                    }
                    tmp = null;
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


        for(int i = 0; i <keyPairs.size(); i++){
            keyPairs.get(i).button.paint(g);
        }
        for(int i = 0; i <keyPairs.size(); i++){
            keyPairs.get(i).button1.paint(g);
        }
    }

    @Override
    public void update() {
        for(int i = 0; i <keyPairs.size(); i++){
            keyPairs.get(i).button.update();
        }
        for(int i = 0; i <keyPairs.size(); i++){
            keyPairs.get(i).button1.update();
        }

    }

    private static class KeyPair{
        private Card button;
        private Card button1;
        public KeyPair(Card button,Card button1){
            this.button = button;
            this.button1 = button1;
        }
    }

}
