package scene.popupwindow;

import controllers.ImageController;
import gameobj.Card;
import utils.CommandSolver;

import java.awt.*;
import java.util.ArrayList;


public class PopUpFindDifferent extends PopUpTask{
    private ArrayList<KeyPair> keyPairs;
    private Card tmp;

    @Override
    public void sceneBegin() {
        super.sceneBegin();
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
        tmp = null;
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
                        keyPairs.get(i).card.changeState(e.getPoint());
                    }
                    for(int i = 0; i < keyPairs.size(); i ++){
                        keyPairs.get(i).card1.changeState(e.getPoint());
                    }
                    for(int i = 0; i < keyPairs.size(); i++) {
                        if (keyPairs.get(i).card1.state(e.getPoint())
                                && tmp == keyPairs.get(i).getPartner(keyPairs.get(i).card1)){
                            keyPairs.get(i).card1.setState(Card.State.SHOW);
                            keyPairs.get(i).card.setState(Card.State.SHOW);
                            tmp = null;
                            break;
                        }else if(keyPairs.get(i).card.state(e.getPoint())
                                && tmp == keyPairs.get(i).getPartner(keyPairs.get(i).card)){
                            keyPairs.get(i).card1.setState(Card.State.SHOW);
                            keyPairs.get(i).card.setState(Card.State.SHOW);
                            tmp = null;
                            break;
                        }
                    }

                    for(int i = 0; i< keyPairs.size(); i++){
                        if (keyPairs.get(i).card1.state(e.getPoint())){
                            tmp = keyPairs.get(i).getPartner(keyPairs.get(i).card1);
                            break;
                        }else if(keyPairs.get(i).card.state(e.getPoint())) {
                            tmp = keyPairs.get(i).getPartner(keyPairs.get(i).card);
                            break;
                        }
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
        super.paint(g);
        for(int i = 0; i <keyPairs.size(); i++){
            keyPairs.get(i).card.paint(g);
        }
        for(int i = 0; i <keyPairs.size(); i++){
            keyPairs.get(i).card1.paint(g);
        }
        if(isDone()){
            g.drawImage(finish, 350,125,null);
        }
    }

    @Override
    public void update() {
        for(int i = 0; i < keyPairs.size(); i++){
            keyPairs.get(i).card.update();
            keyPairs.get(i).card1.update();
        }
        int i;
        for(i = 0; i < keyPairs.size(); i++) {
            if(keyPairs.get(i).card.getState() == Card.State.HIDE
                    || keyPairs.get(i).card.getState() == Card.State.HIDE){
              break;
            }
        }
        if(i == keyPairs.size()){
            setDone(true);
        }


    }

    private static class KeyPair{
        private Card card;
        private Card card1;
        public KeyPair(Card card,Card card1){
            this.card = card;
            this.card1 = card1;
        }

        public Card getPartner(Card card){
            if(card == card){
                return card1;
            }else if(card == card1){
                return card;
            }
            return null;
        }
    }

}
