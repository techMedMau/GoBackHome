package scene;

import controllers.ImageController;
import controllers.SceneController;
import gameobj.button.Button;
import internet.server.ClientClass;
import internet.server.Server;
import scene.popupwindow.PopUpConnect;
import scene.popupwindow.PopUpCreateRoom;
import utils.CommandSolver;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Scanner;

public class OpenScene extends Scene {
    private Image image;
    private Image titleImg;
    private Button creatRoomButton;
    private Button inputButton;
    private PopUpConnect popUpConnect;
    private PopUpCreateRoom popUpCreateRoom;
    @Override
    public void paint(Graphics g) {
        g.drawImage(image,0,0,null);
        g.drawImage(titleImg,100,150,null);
        creatRoomButton.paint(g);
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


    }

    @Override
    public void sceneBegin() {
        image= ImageController.getInstance().tryGet("/mainmenu.jpg");
        titleImg= ImageController.getInstance().tryGet("/title.png");
        creatRoomButton=new Button(270,300,359,113,ImageController.getInstance()
                .tryGet("/creatRoom.png"));
        inputButton=new Button(370,450,180,96,ImageController.getInstance()
                .tryGet("/inputNum.png"));
        popUpConnect=new PopUpConnect();
        popUpCreateRoom=new PopUpCreateRoom();
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
            if (popUpCreateRoom.isShow()){
                popUpCreateRoom.mouseListener().mouseTrig(e,state,trigTime);
            }
            if (popUpConnect.isShow()){
                popUpConnect.mouseListener().mouseTrig(e,state,trigTime);
            }
            switch (state){
                case CLICKED:
                    if (creatRoomButton.state(e.getPoint())){
                        popUpCreateRoom.sceneBegin();
                    }
                    if (inputButton.state(e.getPoint())){
                        popUpConnect.sceneBegin();
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
                /*Scanner sc=new Scanner(System.in);
                    if ( commandCode == 0) {
                        Server s=Server.instance();
                        s.create(12345);
                        s.start();
                        try {
                            ClientClass.getInstance().connect("127.0.0.1",12345);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("主機IP：" + Server.instance().getLocalAddress()[0] +
                                "\n主機PORT：" + Server.instance().getLocalAddress()[1]);
                    }else if(commandCode==5){
                        System.out.println("請輸入IP:");
                        String str=sc.next();
                        try {
                            ClientClass.getInstance().connect(str,12345);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                SceneController.getInstance().changeScene(new MapScene());*/
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
