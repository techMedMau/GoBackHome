package scene;

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
    private PopUpConnect popUpConnect;
    private PopUpCreateRoom popUpCreateRoom;
    @Override
    public void paint(Graphics g) {
        g.drawImage(image,0,0,null);
        g.drawImage(titleImg,100,150,null);
        createRoomButton.paint(g);
        inputButton.paint(g);
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
                case Global.InternetCommand.CREAT:
                    if (Global.WAIT_SCENES.containsKey(strs.get(0))){
                        return;
                    }
                    Global.WAIT_SCENES.put(strs.get(0),
                            new WaitingScene(strs.get(0),Integer.parseInt(strs.get(1)),Integer.parseInt(strs.get(2)),Integer.parseInt(strs.get(3))));
                    break;
//                case Global.InternetCommand.GET_ROOM:
//                    System.out.println("GET_ROOM");
//                    Global.WAIT_SCENES.forEach((s, waitingScene) -> {
//                        ArrayList<String> str=new ArrayList<>();
//                        str.add(s);
//                        str.add(String.valueOf(waitingScene.getTraitor()));
//                        str.add(String.valueOf(waitingScene.getPlayMax()));
//                        str.add(String.valueOf(waitingScene.getHomeOwner()));
//                        ClientClass.getInstance().sent(Global.InternetCommand.CREAT,str);
//                        System.out.println(s);
//                    });
//                    break;
            }
        });


    }

    @Override
    public void sceneBegin() {
        image= ImageController.getInstance().tryGet("/mainmenu.jpg");
        titleImg= ImageController.getInstance().tryGet("/title.png");
        createRoomButton=new Button(270,300,359,113,ImageController.getInstance()
                .tryGet("/creatRoom.png"));
        inputButton=new Button(370,450,180,96,ImageController.getInstance()
                .tryGet("/inputNum.png"));
        popUpConnect=new PopUpConnect();
        popUpCreateRoom=new PopUpCreateRoom();
        Server s=Server.instance();
        System.out.println(s.getLocalAddress()[0]);
        if (s.getLocalAddress()[0].equals(Global.SERVE_IP)){
            s.create(12345);
            s.start();
        }
        try {
            ClientClass.getInstance().connect(Global.SERVE_IP,12345);
        } catch (IOException ee) {
            ee.printStackTrace();
        }
        ClientClass.getInstance().sent(Global.InternetCommand.GET_ROOM,new ArrayList<>());
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime)->{
            if (state==null){
                return;
            }
            switch (state){
                case CLICKED:
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
                    break;
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                popUpConnect.keyListener().keyPressed(commandCode,trigTime);
                }
            @Override
            public void keyReleased(int commandCode, long trigTime) {

            }
            @Override
            public void keyTyped(char c, long trigTime) {
                popUpConnect.keyListener().keyTyped(c,trigTime);

            }
        };
    }
}
