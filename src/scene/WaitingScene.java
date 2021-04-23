package scene;

import controllers.AudioResourceController;
import controllers.ImageController;
import controllers.SceneController;
import gameobj.*;
import internet.server.ClientClass;
import maploader.MapInfo;
import maploader.MapLoader;
import utils.CommandSolver;
import utils.Global;
import gameobj.button.Button;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WaitingScene extends Scene {
    private int currentPlay;
    private int playMax;
    private String password;
    private Button startButton;
    private ArrayList<GameObject> forWaitingRoom;
    private MapLoader mapLoader;
    private ArrayList<Alien> aliens;
    private int homeOwner;
    private TalkRoomScene talkRoomScene;
    private Button exitButton;


    public WaitingScene(String password, int playMax, int homeOwner) {
        this.password = password;
        this.playMax = playMax;
        this.homeOwner = homeOwner;
        this.currentPlay = 1;
    }

    @Override
    public void sceneBegin() {
        AudioResourceController.getInstance().loop("/sound/waitingScene.wav", -1);
        aliens = new ArrayList<>();
        ArrayList<String> str = new ArrayList<>();
        str.add(password);
        //sent請幫我做這個動作
        ClientClass.getInstance().sent(Global.InternetCommand.GET_NUM, str);
        //請幫我要一個數字
        startButton = new Button(390, 490, 150, 69, ImageController.getInstance()
                .tryGet("/startButton.png"));
        mapLoader = MapWaitGen();
        talkRoomScene = new TalkRoomScene(password);
        talkRoomScene.sceneBegin();
        exitButton = new Button(810, 485, 150, 70, ImageController.getInstance().tryGet("/exit.png"));

    }

    @Override
    public void sceneEnd() {
        AudioResourceController.getInstance().stop("/sound/waitingScene.wav");
        AudioResourceController.getInstance().stop("/sound/buttonzz.wav");
        aliens=null;
        startButton=null;
        mapLoader=null;
        talkRoomScene.sceneEnd();
        talkRoomScene=null;
        exitButton=null;

    }

    public boolean canEnter() {
        return currentPlay < playMax;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                if (aliens.size() == 0) {
                    return;
                }
                Global.Direction direction = Global.Direction.getDirection(commandCode);
                if (commandCode == 9) {  //角色斷線時發送斷線訊息
                    ArrayList<String> strs = new ArrayList<String>();
                    strs.add(String.valueOf(ClientClass.getInstance().getID()));
                    strs.add(password);
                    ClientClass.getInstance().sent(Global.InternetCommand.DISCONNECT, strs);
                    ClientClass.getInstance().disConnect();
                    System.exit(0);
                }
                switch (direction) {
                    case LEFT:
                        aliens.get(0).setHorizontalDir(direction);
                        break;
                    case RIGHT:
                        aliens.get(0).setHorizontalDir(direction);
                        break;
                    case UP:
                        aliens.get(0).setVerticalDir(direction);
                        break;
                    case DOWN:
                        aliens.get(0).setVerticalDir(direction);
                        break;
                }
                talkRoomScene.keyListener().keyPressed(commandCode, trigTime);
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                if (aliens.size() == 0) {
                    return;
                }
                Global.Direction direction = Global.Direction.getDirection(commandCode);
                switch (direction) {
                    case LEFT:
                        aliens.get(0).setHorizontalDir(Global.Direction.NO_DIR);
                        break;
                    case RIGHT:
                        aliens.get(0).setHorizontalDir(Global.Direction.NO_DIR);
                        break;
                    case UP:
                        aliens.get(0).setVerticalDir(Global.Direction.NO_DIR);
                        break;
                    case DOWN:
                        aliens.get(0).setVerticalDir(Global.Direction.NO_DIR);
                        break;
                }
                talkRoomScene.keyListener().keyReleased(commandCode, trigTime);
            }

            @Override
            public void keyTyped(char c, long trigTime) {
                if (aliens.size() == 0) {
                    return;
                }
                talkRoomScene.keyListener().keyTyped(c, trigTime);
            }
        };
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (aliens.size() == 0) {
                return;
            }
            if (state == CommandSolver.MouseState.CLICKED) {
                AudioResourceController.getInstance().shot("/sound/buttonzz.wav");
                if (ClientClass.getInstance().getID() == homeOwner && startButton.state(e.getPoint())) {
                    ArrayList<String> str = new ArrayList<>();
                    str.add(password);
                    ClientClass.getInstance().sent(Global.InternetCommand.START, str);
                    Global.WAIT_SCENES.remove(password, this);
                    SceneController.getInstance().changeScene(new GameScene(aliens, password, homeOwner, playMax));
                    return;
                }
                if (exitButton.state(e.getPoint())) {
                    if (aliens.get(0).getId() == homeOwner && aliens.size() > 1) {
                        homeOwner = aliens.get(1).getId();
                    }
                    ArrayList<String> str = new ArrayList<>();
                    str.add(password);
                    str.add(String.valueOf(aliens.get(0).getId()));
                    ClientClass.getInstance().sent(Global.InternetCommand.EXIT, str);
                    aliens.remove(0);
                    SceneController.getInstance().changeScene(new OpenScene());
                    return;
                }
            }
        };
    }

    @Override
    public void paint(Graphics g) {

        for (int i = 0; i < forWaitingRoom.size(); i++) {
            forWaitingRoom.get(i).paint(g);
        }
        for (int i = 0; i < aliens.size(); i++) {
            aliens.get(i).paint(g);
        }
        if (ClientClass.getInstance().getID() == homeOwner) {
            startButton.paint(g);
        }
        Font font = new Font(Global.FONT, Font.PLAIN, 30);
        g.setFont(font);
        g.drawString(password, 425, 480);
        Font numFont = new Font(Global.FONT, Font.PLAIN, 20);
        g.setFont(numFont);
        g.drawString(currentPlay + "/" + playMax, 445, 580);
        talkRoomScene.paint(g);
    }


    @Override
    public void update() {
        if (aliens.size() != 0) {
            aliens.get(0).update();
            if (aliens.get(0).painter().left() <= 0) {
                aliens.get(0).translateX(2);
            }
            if (aliens.get(0).painter().top() <= 0) {
                aliens.get(0).translateY(2);
            }
            if (aliens.get(0).painter().bottom() >= Global.SCREEN_Y) {
                aliens.get(0).translateY(-2);
            }
            if (aliens.get(0).painter().right() >= Global.SCREEN_X) {
                aliens.get(0).translateX(-2);
            }

            ArrayList<String> strr = new ArrayList<>();
            strr.add(ClientClass.getInstance().getID() + "");
            strr.add(aliens.get(0).painter().centerX() + "");
            strr.add(aliens.get(0).painter().centerY() + "");
            strr.add(aliens.get(0).getHorizontalDir().getValue() + "");
            strr.add(aliens.get(0).getVerticalDir().getValue() + "");
            strr.add(password);
            ClientClass.getInstance().sent(Global.InternetCommand.MOVE, strr);
        }
        ClientClass.getInstance().consume((serialNum, internetcommand, strs) -> {
            switch (internetcommand) {
                case Global.InternetCommand.CONNECT:
                    System.out.println("Connect " + serialNum);
                    boolean isBorn = false;
                    for (int i = 0; i < aliens.size(); i++) {
                        if (aliens.get(i).getId() == serialNum) {
                            isBorn = true;
                            break;
                        }
                    }
                    if (!isBorn && strs.get(3).equals(password)) {
                        aliens.add(new Alien(Integer.parseInt(strs.get(0)), Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2))));
                        aliens.get(aliens.size() - 1).setId(serialNum);
                        ArrayList<String> str = new ArrayList<>();
                        str.add(aliens.get(0).painter().centerX() + "");
                        str.add(aliens.get(0).painter().centerY() + "");
                        str.add(aliens.get(0).getNum() + "");
                        str.add(password);
                        currentPlay++;
                        ClientClass.getInstance().sent(Global.InternetCommand.CONNECT, str);
                    }
                    break;
                case Global.InternetCommand.MOVE:
                    if (strs.get(5).equals(password)) {
                        for (int i = 1; i < aliens.size(); i++) {
                            if (aliens.get(i).getId() == Integer.parseInt(strs.get(0))) {
                                aliens.get(i).painter().setCenter(Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)));
                                aliens.get(i).collider().setCenter(Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)));
                                aliens.get(i).setHorizontalDir(Global.Direction.getDirection(Integer.parseInt(strs.get(3))));
                                aliens.get(i).setVerticalDir(Global.Direction.getDirection(Integer.parseInt(strs.get(4))));
                                break;
                            }
                        }
                    }
                    break;
                case Global.InternetCommand.DISCONNECT:
                    if (strs.get(1).equals(password)) {
                        for (int i = 0; i < aliens.size(); i++) {
                            if (aliens.get(i).getId() == Integer.parseInt(strs.get(0))) {
                                aliens.remove(i);
                                break;
                            }
                        }
                    }
                    break;
                case Global.InternetCommand.START:
                    if (strs.get(0).equals(password)) {
                        Global.WAIT_SCENES.remove(strs.get(0), Global.WAIT_SCENES.get(strs.get(0)));
                        SceneController.getInstance().changeScene(new GameScene(aliens, password, homeOwner, playMax));
                    }
                    break;
                case Global.InternetCommand.GET_ROOM:
                    Global.WAIT_SCENES.forEach((s, waitingScene) -> {
                        ArrayList<String> str = new ArrayList<>();
                        str.add(s);
                        str.add(String.valueOf(waitingScene.playMax));
                        str.add(String.valueOf(waitingScene.homeOwner));
                        ClientClass.getInstance().sent(Global.InternetCommand.CREATE, str);
                    });
                    break;
                //告訴別人哪個數字可以用
                case Global.InternetCommand.GET_NUM:
                    if (strs.get(0).equals(password) && ClientClass.getInstance().getID() == homeOwner) {
                        ArrayList<String> str = new ArrayList<>();
                        int n;
                        while (true) {
                            n = Global.random(1, 8);
                            int i;
                            for (i = 0; i < aliens.size(); i++) {
                                if (n == aliens.get(i).getNum()) {
                                    break;
                                }
                            }
                            if (i == aliens.size()) {
                                break;
                            }
                        }
                        str.add(String.valueOf(password));
                        str.add(String.valueOf(n));
                        str.add(String.valueOf(serialNum));
                        ClientClass.getInstance().sent(Global.InternetCommand.MAKE_ALIENS, str);
                    }
                    break;
                //給別人NUM
                case Global.InternetCommand.MAKE_ALIENS:
                    if (strs.get(0).equals(password)) {
                        if (String.valueOf(ClientClass.getInstance().getID()).equals(strs.get(2))) {
                            ArrayList<String> str = new ArrayList<>();
                            talkRoomScene.setHeader(strs.get(1));
                            aliens.add(new Alien(400, 300, Integer.parseInt(strs.get(1))));
                            str.add(String.valueOf(400));
                            str.add(String.valueOf(300));
                            str.add(strs.get(1));
                            str.add(password);
                            aliens.get(aliens.size() - 1).setId(Integer.parseInt(strs.get(2)));
                            ClientClass.getInstance().sent(Global.InternetCommand.CONNECT, str);
                        }
                    }
                    break;
                case Global.InternetCommand.MESSAGE:
                    if (password.equals(strs.get(0)) && serialNum != ClientClass.getInstance().getID()) {
                        String header = strs.get(1);
                        strs.remove(0);
                        strs.remove(0);
                        talkRoomScene.getTalkFrame().getMessage(header, strs);
                    }
                    break;
                case Global.InternetCommand.EXIT:
                    if (password.equals(strs.get(0)) && serialNum != ClientClass.getInstance().getID()) {
                        for (int i = 1; i < aliens.size(); i++) {
                            if (Integer.parseInt(strs.get(1)) == aliens.get(i).getId()) {
                                aliens.remove(i);
                                break;
                            }
                        }
                    }
                    break;
            }
        });
        talkRoomScene.update();
    }

    public MapLoader MapWaitGen() {
        try {
            mapLoader = new MapLoader("/map/genMap_wait.bmp", "/map/genMap_wait.txt");
            ArrayList<MapInfo> test = mapLoader.combineInfo();
            forWaitingRoom = new ArrayList<>();
            this.forWaitingRoom = mapLoader.creatObjectArray("Name", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new tile(mapInfo.getX() * size, mapInfo.getY() * size);
                                return tmp;
                            }
                            return null;
                        }
                    }
            );

        } catch (IOException ex) {
            Logger.getLogger(WaitingScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapLoader;
    }


}