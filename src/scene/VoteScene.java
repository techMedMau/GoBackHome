package scene;

import controllers.ImageController;
import gameobj.Alien;
import utils.CommandSolver;

import java.awt.*;
import java.util.ArrayList;

public class VoteScene extends Scene{
    private ArrayList<Alien> aliens;
    private int reportID;
    private Image img;
    public VoteScene(ArrayList<Alien> aliens,int reportID){
        this.aliens=aliens;
        this.reportID=reportID;

    }
    @Override
    public void sceneBegin() {
        img= ImageController.getInstance().tryGet("/background.png");

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

    }

    @Override
    public void update() {

    }
}
