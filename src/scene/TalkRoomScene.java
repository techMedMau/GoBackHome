package scene;

import controllers.ImageController;
import gameobj.Input;
import gameobj.TalkFrame;
import utils.CommandSolver;
import utils.Global;

import java.awt.*;

public class TalkRoomScene extends Scene{
    private Image title;
    private TalkFrame talkFrame;
    private Input input;
    private String header;
    @Override
    public void sceneBegin() {
        talkFrame=new TalkFrame(960,36,20);
        input=new Input(965,445,20,20,240,true);
        title= ImageController.getInstance().tryGet("/talkRoom/talkRoomTitle.jpg");
        header="";

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
                switch (Global.KeyCommand.getKeyCommand(commandCode)){
                    case BACK_SPACE:
                        input.substring();
                        break;
                    case SHIFT:
                        input.shift();
                        break;
                }

            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                switch (Global.KeyCommand.getKeyCommand(commandCode)){
                    case SHIFT:
                        input.disShift();
                        break;
                    case CAPS_LOCK:
                        input.CapsLock();
                        break;
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {
                input.setWrite(()->talkFrame.getMessage(header,input.writeStrings()));
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
        g.drawImage(title,960,0,null);

    }

    @Override
    public void update() {

    }
}
