package gameobj;

import controllers.ImageController;
import gameobj.button.Button;
import utils.Delay;

import java.awt.*;

public class Card extends Button {
    private State state;
    private Delay delay;
    private Image img;

    public Card(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
        this.state = State.HIDE;
        this.delay = new Delay(120);
        this.img = ImageController.getInstance().tryGet("/findDifferent/empty.png");
    }

    public void changeState(Point point){
       if(state(point)){
           this.state = State.SHOW;
           delay.play();
       }
    }

    @Override
    public void update() {
        if(delay.count()){
            state = State.HIDE;
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(state == State.HIDE){
           g.drawImage(img, painter().left(), painter().top(), null);
        }
    }

    public enum State{
        HIDE,
        SHOW;
    }

    public State getState(){
        return state;
    }

    public void setState(State state) {
        this.state = state;
        this.delay.stop();
    }



    //    @Override
//    //小任務
//    public boolean state(Point point){
//        return point.getX()>painter().left() && point.getX()<painter().right()&&
//                point.getY()>painter().top() && point.getY()<painter().bottom();
//    }
//
//    //任務箱子
//    @Override
//    public boolean state(int x,int y){
//        return x>painter().left() && x<painter().right()&&
//                y>painter().top() && y<painter().bottom();
//    }
//    @Override
//    public void update() {
//    }

//    @Override
//    public void paintComponent(Graphics g) {
//        g.drawImage(img,painter().left(),painter().top(),null);
//
//    }
}
