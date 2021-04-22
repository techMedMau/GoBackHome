package scene;

import camera.Camera;
import controllers.ImageController;
import gameobj.Input;
import gameobj.TalkFrame;
import internet.server.ClientClass;
import utils.CommandSolver;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class TalkRoomScene extends Scene {
    private Image title;
    private TalkFrame talkFrame;
    private Input input;
    private String header;
    private Camera cam;
    private String password;

    public TalkRoomScene(String password){
        this.password=password;
    }

    public TalkFrame getTalkFrame() {
        return talkFrame;
    }

    public void setHeader(String num) {
        this.header = "/talkRoom/p" + num + "_dieHead.png";
    }

    @Override
    public void sceneBegin() {
        talkFrame = new TalkFrame(960, 36, 20);
        input = new Input(970, 445, 20, 20, 240, true);
        title = ImageController.getInstance().tryGet("/talkRoom/talkRoomTitle.jpg");
        cam = new Camera.Builder(260, 404)
                .setCameraLockDirection(true, false, true, false)
                .setCameraStartLocation(960, 36)
                .setCameraWindowLocation(960, 36)
                .setChaseObj(null)
                .gen();

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
                switch (Global.KeyCommand.getKeyCommand(commandCode)) {
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
                switch (Global.KeyCommand.getKeyCommand(commandCode)) {
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
                input.setWrite(() -> {
                    ArrayList<String> tmp=input.writeStrings();
                    talkFrame.getMessage(header, tmp);
                    ArrayList<String> message=new ArrayList<>();
                    message.add(password);
                    message.add(header);
                    message.addAll(tmp);
                    ClientClass.getInstance().sent(Global.InternetCommand.Message,message);
                });
                input.add(c);
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(960, 0, 260, 640);
        g.drawImage(title, 960, 0, null);
        cam.start(g);
        talkFrame.paint(g);
        cam.end(g);
        input.paint(g);
    }

    @Override
    public void update() {
        talkFrame.update();
        cam.resetY(talkFrame.bottom() - 404, talkFrame.bottom());
        cam.update();
    }

}
