package scene;

import gameobj.Input;
import gameobj.TalkFrame;
import utils.CommandSolver;

import java.awt.*;

public class TalkRoomScene extends Scene{
    private TalkFrame talkFrame;
    private Input input;
    @Override
    public void sceneBegin() {
        talkFrame=new TalkFrame(961,441);
        input=new Input(965,445,20,20,20);

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
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                switch (commandCode){
                    case 7:
                        if (input.length()==0){return;}
                        input.substring();
                        break;
                }

            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {

            }

            @Override
            public void keyTyped(char c, long trigTime) {
                input.add(c);
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(960,0,260,640);
        talkFrame.paint(g);
        input.paint(g);

    }

    @Override
    public void update() {

    }
}
