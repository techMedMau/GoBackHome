package scene.popupwindow;

import gameobj.Actor;
import utils.CommandSolver;
import scene.Scene;

import java.awt.*;

public class GeneralSetting extends PopUpWindows{
    private Actor actor;

    public GeneralSetting() {
        super(0,0);
    }

    @Override
    public void sceneBegin() {
        actor=new Actor(0,0,0);

    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {
        actor.paint(g);

    }

    @Override
    public void update() {
        actor.update();

    }
}
