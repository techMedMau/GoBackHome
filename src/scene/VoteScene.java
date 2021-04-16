package scene;

import controllers.ImageController;
import gameobj.Alien;
import utils.CommandSolver;
import utils.Delay;

import java.awt.*;
import java.util.ArrayList;

public class VoteScene extends Scene{
    private ArrayList<Alien> aliens;
    private int reportID;
    private Image img;
    private Delay delay;
    private int count;

    public VoteScene(ArrayList<Alien> aliens,int reportID){
        this.aliens=aliens;
        this.reportID=reportID;
        this.delay = new Delay(60);
        delay.loop();
        this.count = 60;
    }

    @Override
    public void sceneBegin() {
        img= ImageController.getInstance().tryGet("/sheet2.png");

    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,0,0,null);

        g.drawString(String.valueOf(count),10,30);
        if(delay.count()){
            count --;
        }
    }

    @Override
    public void update() {
        if(count == 0){

        }
    }
}
