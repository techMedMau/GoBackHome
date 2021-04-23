package scene;

import controllers.AudioResourceController;
import controllers.ImageController;
import gameobj.button.Button;
import internet.server.ClientClass;
import internet.server.Server;
import scene.popupwindow.PopUpConnect;
import scene.popupwindow.PopUpCreateRoom;
import utils.CommandSolver;
import utils.Global;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class OpenScene extends Scene {
    private Image image;
    private Image titleImg;
    private Button createRoomButton;
    private Button inputButton;
    private Button tutorialButton;
    private PopUpConnect popUpConnect;
    private PopUpCreateRoom popUpCreateRoom;
    private boolean tutorial;
    private Image img;
    public Button tutorialClose;
    @Override
    public void paint(Graphics g) {
        g.drawImage(image,0,0,null);
        g.drawImage(titleImg,95,80,null);
        createRoomButton.paint(g);
        inputButton.paint(g);
        tutorialButton.paint(g);

        if(tutorial){
            g.drawImage(img, 10,5,null);
            tutorialClose.paint(g);
        }
        if (popUpConnect.isShow()){
            popUpConnect.paint(g);
        }
        if (popUpCreateRoom.isShow()){
            popUpCreateRoom.paint(g);
        }

    }

    @Override
    public void update() {
        ClientClass.getInstance().consume((serialNum, commandCode, strs) -> {
            switch (commandCode){
                case Global.InternetCommand.CREATE:
                    if (Global.WAIT_SCENES.containsKey(strs.get(0))){
                        return;
                    }
                    Global.WAIT_SCENES.put(strs.get(0),
                            new WaitingScene(strs.get(0),Integer.parseInt(strs.get(1)),Integer.parseInt(strs.get(2))));
                    break;
            }
        });
    }

    @Override
    public void sceneBegin() {
        tutorialClose = new gameobj.button.Button(0, 0, 48, 48
                , ImageController.getInstance().tryGet("/button/close.png"));
        img = ImageController.getInstance().tryGet("/openScene/rule.png");
        image= ImageController.getInstance().tryGet("/openScene/mainmenu.jpg");
        titleImg= ImageController.getInstance().tryGet("/openScene/title.png");
        tutorialButton = new Button(440,220,359,112
                , ImageController.getInstance().tryGet("/openScene/tutorial.png"));
        createRoomButton=new Button(440,360,359,113,ImageController.getInstance()
                .tryGet("/openScene/creatRoom.png"));
        inputButton=new Button(530,490,180,96,ImageController.getInstance()
                .tryGet("/openScene/inputNum.png"));
        popUpConnect=new PopUpConnect();
        popUpCreateRoom=new PopUpCreateRoom();
        Server s=Server.instance();
        System.out.println(s.getLocalAddress()[0]);
        if (ClientClass.getInstance().getID()==0){
            if (s.getLocalAddress()[0].equals(Global.SERVE_IP)){
                s.create(12345);
                s.start();
            }
            try {
                ClientClass.getInstance().connect(Global.SERVE_IP,12345);
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }
        ClientClass.getInstance().sent(Global.InternetCommand.GET_ROOM,new ArrayList<String>());
        AudioResourceController.getInstance().loop("/sound/bgm.wav",-1);
    }

    @Override
    public void sceneEnd() {
        image=null;
        titleImg=null;
        createRoomButton=null;
        inputButton=null;
        popUpConnect=null;
        popUpCreateRoom=null;
        AudioResourceController.getInstance().stop("/sound/bgm.wav");
        AudioResourceController.getInstance().stop("/sound/buttonzz.wav");
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime)->{
            if (state==null){
                return;
            }
            switch (state){
                case CLICKED:
                    AudioResourceController.getInstance().shot("/sound/buttonzz.wav");
                    if (createRoomButton.state(e.getPoint())&&!popUpConnect.isShow()&&!popUpCreateRoom.isShow()){
                        popUpCreateRoom.sceneBegin();
                        break;
                    }
                    if (inputButton.state(e.getPoint())&&!popUpConnect.isShow()&&!popUpCreateRoom.isShow()){
                        popUpConnect.sceneBegin();
                        break;
                    }
                    if (popUpCreateRoom.isShow()){
                        popUpCreateRoom.mouseListener().mouseTrig(e,state,trigTime);
                        break;
                    }
                    if (popUpConnect.isShow()){
                        popUpConnect.mouseListener().mouseTrig(e,state,trigTime);
                        break;
                    }
                    if(tutorialButton.state(e.getPoint())){
                        tutorial = true;
                        break;
                    }
                    if (tutorialClose.state(e.getPoint())) {
                        tutorial = false;
                        return;
                    }
                    break;
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                if (popUpConnect.isShow()){
                    popUpConnect.keyListener().keyPressed(commandCode,trigTime);
                }
                }
            @Override
            public void keyReleased(int commandCode, long trigTime) {
                if (popUpConnect.isShow()){
                    popUpConnect.keyListener().keyReleased(commandCode,trigTime);
                }
            }
            @Override
            public void keyTyped(char c, long trigTime) {
                if (popUpConnect.isShow()){
                    popUpConnect.keyListener().keyTyped(c,trigTime);
                }
            }
        };
    }
}
