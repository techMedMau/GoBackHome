package scene;

import camera.Camera;
import controllers.AudioResourceController;
import controllers.ImageController;
import controllers.SceneController;
import controllers.TaskController;
import gameobj.*;
import internet.server.ClientClass;
import internet.server.CommandReceiver;
import maploader.MapInfo;
import maploader.MapLoader;
import utils.CommandSolver;
import utils.Delay;
import utils.Global;
import gameobj.Line.Point;
import gameobj.button.Button;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScene extends Scene {
    private ArrayList<Alien> aliens;
    private ArrayList<Alien> deadBody;
    private Camera cam;
    private Map map;
    private Image backGround;
    private ArrayList<GameObject> forGame;
    private MapLoader mapLoader;
    private ArrayList<TaskItem> taskItems;
    private TaskController taskController;
    private int homeOwner;
    private BackgroundItem backgroundItem;
    private String password;
    private int playMax;
    private int witchNum;
    private Alien.Role winRole;
    private Button ruleButton;
    private boolean tutorial;
    private Button exitButton;
    private Image tutorialImg;
    public Button tutorialClose;
    private static int[][] location = new int[][]{
            {1025, 1120}, {1728, 100}, {180, 150}, {64, 640}, {288, 1003}
            , {1220, 1120}, {1216, 425}, {1600, 790}, {672, 224}, {672, 652}};
    private int locationNum;
    private TalkRoomScene talkRoomScene;
    private Button declare;
    private Delay winPicture;

    public GameScene(ArrayList<Alien> aliens, String password, int homeOwner, int playMax) {
        this.aliens = aliens;
        this.password = password;
        this.homeOwner = homeOwner;
        this.playMax = playMax;
    }

    @Override
    public void sceneBegin() {
        ruleButton = new Button(810, 555, 150, 70, ImageController.getInstance().tryGet("/tutorial.png"));
        exitButton = new Button(810, 485, 150, 70, ImageController.getInstance().tryGet("/exit.png"));
        tutorialClose = new gameobj.button.Button(0, 20, 48, 48, ImageController.getInstance().tryGet("/button/close.png"));
        talkRoomScene = new TalkRoomScene(password);
        tutorialImg = ImageController.getInstance().tryGet("/rule.png");
        talkRoomScene.sceneBegin();
        talkRoomScene.setHeader(String.valueOf(aliens.get(0).getNum()));
        map = new Map();
        deadBody = new ArrayList<>();
        backGround = ImageController.getInstance().tryGet("/map/background.png");
        mapLoader = MapGameGen();
        cam = new Camera.Builder(Global.SCREEN_X, Global.SCREEN_Y).setChaseObj(aliens.get(0)).gen();
        taskItems = new ArrayList<>();
        createTaskBox();
        taskController = TaskController.getTaskController();
        this.backgroundItem = new BackgroundItem("/arrowRight.png", 500, 500 + 32, 28, 8, 500, 500, 64, 64);
        this.locationNum = Global.random(0, 9);
        aliens.get(0).painter().setCenter(location[locationNum][0], location[locationNum][1]);
        aliens.get(0).collider().setCenter(location[locationNum][0], location[locationNum][1]);
        //-----做屍體
        if (aliens.get(0).getId() == homeOwner) {
            ArrayList<Integer> locations = new ArrayList<>();
            if (playMax == 3 || playMax == 5) {
                int k;
                for (int i = 0; i < 3; i++) {
                    while (true) {
                        k = Global.random(0, 9);
                        if (!locations.contains(k)) {
                            locations.add(k);
                            break;
                        }

                    }
                    createDeadBody(k);
                }
            }
            if (playMax == 4 || playMax == 6) {
                int g;
                for (int i = 0; i < 2; i++) {
                    while (true) {
                        g = Global.random(0, 9);
                        if (!locations.contains(g)) {
                            locations.add(g);
                            break;
                        }
                    }
                    createDeadBody(g);
                }
            }
            witchNum = (aliens.size() + deadBody.size()) / 2;
            assignRole();
        }

        declare = new Button(855, 390, 92, 96, ImageController.getInstance().tryGet("/button/declare.png"));
        winPicture = new Delay(300);
    }

    //分職業
    public void assignRole() {
        int n = Global.random(0, witchNum);
        for (int i = 0; i < n; i++) {
            while (true) {
                int select = Global.random(0, aliens.size() - 1);
                if (aliens.get(select).getRole() != Alien.Role.WITCH) {
                    aliens.get(select).setRole();
                    ArrayList<String> str = new ArrayList<>();
                    str.add(password);
                    str.add(aliens.get(select).getId() + "");
                    ClientClass.getInstance().sent(Global.InternetCommand.WITCH, str);
                    break;
                }
            }
        }
        for (int i = 0; i < witchNum - n; i++) {
            while (true) {
                int select = Global.random(0, deadBody.size() - 1);
                if (deadBody.get(select).getRole() != Alien.Role.WITCH) {
                    deadBody.get(select).setRole();
                    ArrayList<String> str = new ArrayList<>();
                    str.add(password);
                    str.add(deadBody.get(select).toString());
                    ClientClass.getInstance().sent(Global.InternetCommand.WITCH_DEAD, str);
                    break;
                }
            }
        }
    }

    //做屍體
    public void createDeadBody(int z) {
        int n;
        while (true) {
            n = Global.random(1, 8);
            int i;
            int k = 0;
            for (i = 0; i < aliens.size(); i++) {
                if (n == aliens.get(i).getNum()) {
                    break;
                }
            }
            if (deadBody.size() != 0) {
                for (k = 0; k < deadBody.size(); k++) {
                    if (n == deadBody.get(k).getNum()) {
                        break;
                    }
                }
            }
            if (i == aliens.size() && k == deadBody.size()) {
                break;
            }
        }
        Alien tmp = new Alien(location[z][0], location[z][1], n);
        tmp.painter().setCenter(location[z][0], location[z][1]);
        tmp.collider().setCenter(location[z][0], location[z][1]);
        tmp.setAliveState(Alien.AliveState.ZOMBIE);
        deadBody.add(tmp);
        ArrayList<String> str = new ArrayList<>();
        str.add(password);
        str.add(String.valueOf(tmp.left()));
        str.add(String.valueOf(tmp.top()));
        str.add(String.valueOf(n));
        str.add(String.valueOf(tmp.getAliveState().ordinal()));
        ClientClass.getInstance().sent(Global.InternetCommand.DEAD_BODY, str);
    }

    @Override
    public void sceneEnd() {
        AudioResourceController.getInstance().stop("/sound/buttonzz.wav");
        AudioResourceController.getInstance().stop("/sound/killppl.wav");
        aliens=null;
        deadBody=null;
        cam=null;
        map=null;
        backGround=null;
        forGame=null;
        mapLoader=null;
        taskItems=null;
        taskController=null;
        backgroundItem=null;
        password=null;
        winRole=null;
        ruleButton=null;
        exitButton=null;
        tutorialImg=null;
        tutorialClose=null;
        talkRoomScene=null;
        declare=null;
        winPicture=null;

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            if (state == null || aliens.get(0).getAliveState() == Alien.AliveState.DEATH) {
                return;
            }
            if (taskController.getCurrentPopUp() != null && taskController.getCurrentPopUp().isShow()) {
                taskController.getCurrentPopUp().mouseListener().mouseTrig(e, state, trigTime);
                return;
            }
            switch (state) {
                case CLICKED:
                    for (int i = 0; i < taskItems.size(); i++) {
                        if (taskItems.get(i).getState() && taskItems.get(i).state(e.getX() + cam.painter().left(), e.getY() + cam.painter().top())) {
                            final TaskController.Task task = taskItems.get(i).getTask();
                            if (aliens.get(0).isDone(task)) {
                                return;
                            }
                            AudioResourceController.getInstance().shot("/sound/buttonzz.wav");
                            taskController.changePopUp(taskItems.get(i).getTask());
                            taskController.getCurrentPopUp().setFinish(() -> aliens.get(0).setDone(task));
                            return;
                        }
                    }
                    //殺活人
                    for (int i = 0; i < aliens.size(); i++) {
                        if (aliens.get(0).getSwordsNum()>0&&aliens.get(0).getAliveState() != Alien.AliveState.DEATH && aliens.get(0).isAbleToKill() && aliens.get(0).isTriggered(aliens.get(i))
                                && aliens.get(i).getAliveState() == Alien.AliveState.ALIVE && aliens.get(i).state(e.getX() + cam.painter().left(), e.getY() + cam.painter().top())) {
                            aliens.get(i).kill();
                            AudioResourceController.getInstance().shot("/sound/killppl.wav");
                            aliens.get(0).setSwordNum();
                            aliens.get(0).useSword();
                            ArrayList<String> str = new ArrayList<>();
                            str.add(password);
                            str.add(aliens.get(i).getId() + "");
                            //sent 是你要告訴別人
                            ClientClass.getInstance().sent(Global.InternetCommand.ZOMBIE, str);
                            return;
                        }
                    }
                    //殺zombie
                    for (int i = 1; i < aliens.size(); i++) {
                        if (aliens.get(0).getSwordsNum()>0&&aliens.get(0).getAliveState() != Alien.AliveState.DEATH && aliens.get(0).isAbleToKill() && aliens.get(0).isTriggered(aliens.get(i))
                                && aliens.get(i).getAliveState() == Alien.AliveState.ZOMBIE && aliens.get(i).state(e.getX() + cam.painter().left(), e.getY() + cam.painter().top())) {
                            aliens.get(i).death();
                            AudioResourceController.getInstance().shot("/sound/killppl.wav");
                            aliens.get(0).setSwordNum();
                            aliens.get(0).useSword();
                            ArrayList<String> str = new ArrayList<>();
                            str.add(password);
                            str.add(aliens.get(i).getId() + "");
                            //sent 是你要告訴別人
                            ClientClass.getInstance().sent(Global.InternetCommand.DEATH, str);
                            return;
                        }
                    }
                    if (declare.state(e.getPoint())) {
                        winPicture.play();
                        for (int i = 1; i < aliens.size(); i++) {
                            if (aliens.get(0).getRole() != aliens.get(i).getRole()) {
                                winRole = aliens.get(i).getRole();
                                break;
                            }
                        }
                        if (winRole == null) {
                            winRole = aliens.get(0).getRole();
                        }
                        ArrayList<String> str = new ArrayList<>();
                        str.add(password);
                        str.add(winRole.name());
                        ClientClass.getInstance().sent(Global.InternetCommand.WIN_ROLE, str);
                        return;
                    }

                    if (ruleButton.state(e.getPoint())) {
                        tutorial = true;
                        return;
                    }
                    if (tutorialClose.state(e.getPoint())) {
                        tutorial = false;
                        return;
                    }
                    if (exitButton.state(e.getPoint())){
                        ArrayList<String> str=new ArrayList<>();
                        str.add(password);
                        str.add(String.valueOf(aliens.get(0).getId()));
                        ClientClass.getInstance().sent(Global.InternetCommand.EXIT,str);
                        aliens.remove(0);
                        SceneController.getInstance().changeScene(new OpenScene());
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
                if(!AudioResourceController.getInstance().isPlaying("/sound/walk.wav")) {
                    AudioResourceController.getInstance().play("/sound/walk.wav");
                }
                talkRoomScene.keyListener().keyPressed(commandCode, trigTime);
                if (aliens.get(0).getAliveState() == Alien.AliveState.DEATH) {
                    return;
                }
                Global.Direction direction = Global.Direction.getDirection(commandCode);
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
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                if(AudioResourceController.getInstance().isPlaying("/sound/walk.wav")) {
                    AudioResourceController.getInstance().stop("/sound/walk.wav");
                }
                talkRoomScene.keyListener().keyReleased(commandCode, trigTime);
                if (aliens.get(0).getAliveState() == Alien.AliveState.DEATH) {
                    return;
                }
                Global.Direction direction = Global.Direction.getDirection(commandCode);
                switch (direction) {
                    case LEFT:
                    case RIGHT:
                        aliens.get(0).setHorizontalDir(Global.Direction.NO_DIR);
                        break;
                    case UP:
                    case DOWN:
                        aliens.get(0).setVerticalDir(Global.Direction.NO_DIR);
                        break;
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {
                talkRoomScene.keyListener().keyTyped(c, trigTime);

            }
        };
    }

    @Override
    public void paint(Graphics g) {
        talkRoomScene.paint(g);
        if (winRole != null) {
            paintVictory(g, winRole);
            if (winPicture.count()) {
                SceneController.getInstance().changeScene(new OpenScene());
            }
            return;
        }
        cam.start(g);
        g.drawImage(backGround, 0, 0, null);
        for (int i = 1; i < aliens.size(); i++) {
            if (aliens.get(i).getAliveState() != Alien.AliveState.DEATH) {
                g.setColor(Color.BLACK);
                g.drawString(aliens.get(i).getRole().name(), aliens.get(i).painter().centerX() - 28, aliens.get(i).painter().top());
            }
        }
        if (aliens.get(0).getAliveState() == Alien.AliveState.ZOMBIE) {
            for (int i = 0; i < deadBody.size(); i++) {
                g.setColor(Color.BLACK);
                g.drawString(deadBody.get(i).getRole().name(), deadBody.get(i).painter().centerX() - 32, deadBody.get(i).painter().centerY());
            }
        }
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).getAliveState() != Alien.AliveState.DEATH) {
                aliens.get(i).paint(g);
                if (aliens.get(0).getAliveState() != Alien.AliveState.DEATH&&aliens.get(0).getSwordsNum()>0){
                    g.setColor(Color.RED);
                    if (aliens.get(0).getKillDelay().isPlaying()){
                        g.drawString(String.valueOf(aliens.get(0).getKillDelay().getCount()/60), aliens.get(i).painter().centerX() - 8, aliens.get(i).painter().top()-8);
                    }else {
                        g.drawString("Kill?", aliens.get(i).painter().centerX() - 17, aliens.get(i).painter().top()-8);
                    }
                    g.setColor(Color.BLACK);
                }
            }
        }
        for (int i = 0; i < deadBody.size(); i++) {
            deadBody.get(i).paint(g);
        }

        g.setColor(Color.BLACK);
        for (int i = 0; i < forGame.size(); i++) {
            if (cam.isCollision(forGame.get(i))) {
                paintShadow(g, forGame.get(i));
            }
        }

        for (int i = 0; i < forGame.size(); i++) {
            if (cam.isCollision(forGame.get(i))) {
                forGame.get(i).paint(g);
            }
        }

        for (int i = 0; i < taskItems.size(); i++) {
            taskItems.get(i).paint(g);
        }
        backgroundItem.paint(g);

        cam.paint(g);
        cam.end(g);
        //畫劍
        for (int i = 0; i < aliens.get(0).getSwordsNum(); i++) {
            g.drawImage(aliens.get(0).getSword(), 0 + i * 50, 0, null);
        }
        if (taskController.getCurrentPopUp() != null && taskController.getCurrentPopUp().isShow()) {
            taskController.getCurrentPopUp().paint(g);
        }
        ruleButton.paint(g);
        exitButton.paint(g);
        if (tutorial) {
            g.drawImage(tutorialImg, 50, 20, null);
            tutorialClose.paint(g);
        }
        talkRoomScene.paint(g);
        if (aliens.get(0).getAliveState() != Alien.AliveState.DEATH) {
            declare.paint(g);
        }
    }

    @Override
    public void update() {
        aliens.get(0).update();
        colliedWithWall();
        ArrayList<String> str = new ArrayList<>();
        str.add(password);
        str.add(ClientClass.getInstance().getID() + "");
        str.add(aliens.get(0).painter().centerX() + "");
        str.add(aliens.get(0).painter().centerY() + "");
        str.add(aliens.get(0).getHorizontalDir().getValue() + "");
        str.add(aliens.get(0).getVerticalDir().getValue() + "");
        ClientClass.getInstance().sent(Global.InternetCommand.MOVE, str);
        //任物箱亮
        for (int i = 0; i < taskItems.size(); i++) {
            taskItems.get(i).isTriggered(aliens.get(0));
        }
        if (taskController.getCurrentPopUp() != null && taskController.getCurrentPopUp().isShow()) {
            taskController.getCurrentPopUp().update();
        }
        cam.update();
        ClientClass.getInstance().consume(new CommandReceiver() {
            @Override
            public void receive(int serialNum, int commandCode, ArrayList<String> strs) {
                if (strs.get(0).equals(password)) {
                    switch (commandCode) {
                        case Global.InternetCommand.MOVE:
                            if (aliens==null){
                                return;
                            }
                            for (int i = 1; i < aliens.size(); i++) {
                                if (aliens.get(i).getId() == Integer.parseInt(strs.get(1))) {
                                    aliens.get(i).painter().setCenter(Integer.parseInt(strs.get(2)), Integer.parseInt(strs.get(3)));
                                    aliens.get(i).collider().setCenter(Integer.parseInt(strs.get(2)), Integer.parseInt(strs.get(3)));
                                    aliens.get(i).setHorizontalDir(Global.Direction.getDirection(Integer.parseInt(strs.get(4))));
                                    aliens.get(i).setVerticalDir(Global.Direction.getDirection(Integer.parseInt(strs.get(5))));
                                    break;
                                }
                            }
                            break;
                        case Global.InternetCommand.DEATH:
                            for (int i = 0; i < aliens.size(); i++) {
                                if (aliens.get(i).getId() == Integer.parseInt(strs.get(1))) {
                                    aliens.get(i).death();
                                    break;
                                }
                            }
                            break;
                        case Global.InternetCommand.ZOMBIE:
                            for (int i = 0; i < aliens.size(); i++) {
                                if (aliens.get(i).getId() == Integer.parseInt(strs.get(1))) {
                                    aliens.get(i).kill();
                                    break;
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
                        case Global.InternetCommand.WITCH:
                            for (int i = 0; i < aliens.size(); i++) {
                                if (aliens.get(i).getId() == Integer.parseInt(strs.get(1))) {
                                    aliens.get(i).setRole();
                                }
                            }
                            break;
                        case Global.InternetCommand.WITCH_DEAD:
                            for (int i = 0; i < deadBody.size(); i++) {
                                if (deadBody.get(i).toString() == strs.get(1)) {
                                    deadBody.get(i).setRole();
                                }
                            }
                            break;
                        case Global.InternetCommand.DEAD_BODY:
                            if (serialNum != ClientClass.getInstance().getID()) {
                                Alien tmp = new Alien(Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)), Integer.parseInt(strs.get(3)));
                                tmp.setAliveState(Alien.AliveState.values()[Integer.parseInt(strs.get(4))]);
                                deadBody.add(tmp);
                                break;
                            }
                            break;
                        case Global.InternetCommand.WIN_ROLE:
                            if (winRole != null) {
                                break;
                            }
                            winRole = Alien.Role.valueOf(strs.get(1));
                            winPicture.play();
                            break;
                        case Global.InternetCommand.EXIT:
                            for (int i=0;i<aliens.size();i++){
                                if (Integer.parseInt(strs.get(1))==aliens.get(i).getId()){
                                    aliens.remove(i);
                                    break;
                                }
                            }
                            break;
                    }
                }
            }
        });
        talkRoomScene.update();
        int count = 0;
        Alien tmp = null;
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).getAliveState() != Alien.AliveState.DEATH) {
                count++;
                tmp = aliens.get(i);
            }
        }
//        if (count == 1 && tmp != null) {
//            winRole = tmp.getRole();
//            winPicture.play();
//        }

    }

    public void createTaskBox() {
        taskItems.add(new TaskItem("/taskBox/boxItem.png", 100, 360, TaskController.Task.FIND_DIFFERENT));
        taskItems.add(new TaskItem("/taskBox/greenBox.png", 32, 80, TaskController.Task.PUSH));
        taskItems.add(new TaskItem("/taskBox/redBox.png", 790, 1110, TaskController.Task.FIND_PIC));
        taskItems.add(new TaskItem("/taskBox/warningBox.png", 570, 985, TaskController.Task.LINE_UP));
        taskItems.add(new TaskItem("/taskBox/woodBox.png", 700, 450, TaskController.Task.PASSWORD));
        taskItems.add(new TaskItem("/taskBox/blueBox.png", 1755, 900, TaskController.Task.CENTER));
        taskItems.add(new TaskItem("/taskBox/warningWood.png", 1600, 400, TaskController.Task.ROCK));
        taskItems.add(new TaskItem("/taskBox/blackBox.png", 880, 370, TaskController.Task.COLOR_CHANGE));
    }

    public MapLoader MapGameGen() {
        try {
            mapLoader = new MapLoader("/map/genMap.bmp", "/map/genMap.txt");
            ArrayList<MapInfo> test = mapLoader.combineInfo();
            forGame = new ArrayList<>();
            // to be continued
            this.forGame = mapLoader.creatObjectArray("black", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/map/black.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            );
            this.forGame.addAll(mapLoader.creatObjectArray("black2_2", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/map/black2_2.png", mapInfo.getX() * size, mapInfo.getY() * size, 64, 64);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("block", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/map/block.png", mapInfo.getX() * size, mapInfo.getY() * size, 64, 64);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("border", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/map/border.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("brick", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/map/brick.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("castle", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/map/castle.png", mapInfo.getX() * size, mapInfo.getY() * size, 64, 64);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("crate", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/map/crate.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("diagonal", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/map/diagonal.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("grassWall", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/map/grassWall.png", mapInfo.getX() * size, mapInfo.getY() * size, 64, 64);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("sandWall", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/map/sandWall.png", mapInfo.getX() * size, mapInfo.getY() * size, 64, 64);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("stone", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/map/stone.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("tile", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/map/tile.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("waterWall", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/map/waterWall.png", mapInfo.getX() * size, mapInfo.getY() * size, 64, 64);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
        } catch (IOException ex) {
            Logger.getLogger(GameScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapLoader;
    }

    public void colliedWithWall() {
        Global.Direction horizontalDir = aliens.get(0).getHorizontalDir();
        switch (horizontalDir) {
            case LEFT:
                for (int i = 0; i < forGame.size(); i++) {
                    if (aliens.get(0).isCollision(forGame.get(i))
                            && aliens.get(0).leftIsCollision(forGame.get(i))) {
                        aliens.get(0).translateX(Global.MOVE_SPEED);
                        break;
                    }
                }
                if (aliens.get(0).isCollision(backgroundItem) &&
                        aliens.get(0).leftIsCollision(backgroundItem)) {
                    aliens.get(0).translateX(Global.MOVE_SPEED);
                    break;
                }
                for (int i = 0; i < taskItems.size(); i++) {
                    if (aliens.get(0).isCollision(taskItems.get(i))
                            && aliens.get(0).leftIsCollision(taskItems.get(i))) {
                        aliens.get(0).translateX(Global.MOVE_SPEED);
                        break;
                    }
                }
                break;
            case RIGHT:
                for (int i = 0; i < forGame.size(); i++) {
                    if (aliens.get(0).isCollision(forGame.get(i)) &&
                            aliens.get(0).rightIsCollision(forGame.get(i))) {
                        aliens.get(0).translateX(-Global.MOVE_SPEED);
                        break;
                    }
                }
                if (aliens.get(0).isCollision(backgroundItem) &&
                        aliens.get(0).rightIsCollision(backgroundItem)) {
                    aliens.get(0).translateX(-Global.MOVE_SPEED);
                    break;
                }
                for (int i = 0; i < taskItems.size(); i++) {
                    if (aliens.get(0).isCollision(taskItems.get(i)) &&
                            aliens.get(0).rightIsCollision(taskItems.get(i))) {
                        aliens.get(0).translateX(-Global.MOVE_SPEED);
                        break;
                    }
                }
                break;
        }
        Global.Direction verticalDir = aliens.get(0).getVerticalDir();
        switch (verticalDir) {
            case DOWN:
                for (int i = 0; i < forGame.size(); i++) {
                    if (aliens.get(0).isCollision(forGame.get(i)) &&
                            aliens.get(0).bottomIsCollision(forGame.get(i))) {
                        aliens.get(0).translateY(-Global.MOVE_SPEED);
                        break;
                    }
                }
                if (aliens.get(0).isCollision(backgroundItem) &&
                        aliens.get(0).bottomIsCollision(backgroundItem)) {
                    aliens.get(0).translateY(-Global.MOVE_SPEED);
                    break;
                }
                for (int i = 0; i < taskItems.size(); i++) {
                    if (aliens.get(0).isCollision(taskItems.get(i)) &&
                            aliens.get(0).bottomIsCollision(taskItems.get(i))) {
                        aliens.get(0).translateY(-Global.MOVE_SPEED);
                        break;
                    }
                }
                break;
            case UP:
                for (int i = 0; i < forGame.size(); i++) {
                    if (aliens.get(0).isCollision(forGame.get(i)) &&
                            aliens.get(0).topIsCollision(forGame.get(i))) {
                        aliens.get(0).translateY(Global.MOVE_SPEED);
                        break;
                    }
                }
                if (aliens.get(0).isCollision(backgroundItem) &&
                        aliens.get(0).topIsCollision(backgroundItem)) {
                    aliens.get(0).translateY(Global.MOVE_SPEED);
                    break;
                }
                for (int i = 0; i < taskItems.size(); i++) {
                    if (aliens.get(0).isCollision(taskItems.get(i)) &&
                            aliens.get(0).topIsCollision(taskItems.get(i))) {
                        aliens.get(0).translateY(Global.MOVE_SPEED);
                        break;
                    }
                }
                break;
        }
    }

    public void paintShadow(Graphics g, GameObject obj) {
        ArrayList<Point> shadowPoints = new ArrayList<>();
        ArrayList<Line> lines = new ArrayList<>();
        lines.add(new Line(aliens.get(0).centerX(), aliens.get(0).centerY(), obj.left(), obj.top()));
        lines.add(new Line(aliens.get(0).centerX(), aliens.get(0).centerY(), obj.right(), obj.top()));
        lines.add(new Line(aliens.get(0).centerX(), aliens.get(0).centerY(), obj.left(), obj.bottom()));
        lines.add(new Line(aliens.get(0).centerX(), aliens.get(0).centerY(), obj.right(), obj.bottom()));
        ArrayList<Line> shadowLines = new ArrayList<>();
        lines.forEach(line -> {
            if (!line.collision(obj)) {
                shadowLines.add(line);
            }

        });
        lines.clear();
        if (shadowLines.size() > 2) {
            Line tmp = shadowLines.get(0);
            for (int i = 1; i < shadowLines.size(); i++) {
                if (tmp.distance() > shadowLines.get(i).distance()) {
                    tmp = shadowLines.get(i);
                }
            }
            shadowPoints.add(tmp.getPoint2());
            shadowLines.remove(tmp);
        }

        shadowPoints.add(shadowLines.get(0).getPoint2());
        Global.Direction[] dir = new Global.Direction[2];
        for (int i = 0; i < shadowLines.size(); i++) {
            Point[] points = new Point[2];
            Global.Direction[] dirTmp = new Global.Direction[2];
            if (shadowLines.get(i).getHorizontal() == Global.Direction.LEFT) {
                points[0] = shadowLines.get(i).getNum(cam.left(), true);
                dirTmp[0] = Global.Direction.LEFT;
            } else if (shadowLines.get(i).getHorizontal() == Global.Direction.RIGHT) {
                points[0] = shadowLines.get(i).getNum(cam.right(), true);
                dirTmp[0] = Global.Direction.RIGHT;
            }
            if (shadowLines.get(i).getVertical() == Global.Direction.UP) {
                points[1] = shadowLines.get(i).getNum(cam.top(), false);
                dirTmp[1] = Global.Direction.UP;
            } else if (shadowLines.get(i).getVertical() == Global.Direction.DOWN) {
                points[1] = shadowLines.get(i).getNum(cam.bottom(), false);
                dirTmp[1] = Global.Direction.DOWN;
            }
            Point point = null;
            for (int k = 0; k < points.length; k++) {
                if (points[k] == null) {
                    point = points[1 - k];
                    dir[i] = dirTmp[1 - k];
                }
            }
            if (point == null && shadowLines.get(i).getPoint1().distance(points[0]) < shadowLines.get(i).getPoint1().distance(points[1])) {
                point = points[0];
                dir[i] = dirTmp[0];
            } else if (point == null && shadowLines.get(i).getPoint1().distance(points[0]) >= shadowLines.get(i).getPoint1().distance(points[1])) {
                point = points[1];
                dir[i] = dirTmp[1];
            }
            if (dir[1] != null) {
                if ((dir[0] == Global.Direction.LEFT && dir[1] == Global.Direction.UP) ||
                        (dir[1] == Global.Direction.LEFT && dir[0] == Global.Direction.UP)) {
                    shadowPoints.add(new Point(cam.left(), cam.top()));
                } else if ((dir[0] == Global.Direction.RIGHT && dir[1] == Global.Direction.UP) ||
                        (dir[1] == Global.Direction.RIGHT && dir[0] == Global.Direction.UP)) {
                    shadowPoints.add(new Point(cam.right(), cam.top()));
                } else if ((dir[0] == Global.Direction.LEFT && dir[1] == Global.Direction.DOWN) ||
                        (dir[1] == Global.Direction.LEFT && dir[0] == Global.Direction.DOWN)) {
                    shadowPoints.add(new Point(cam.left(), cam.bottom()));
                } else if ((dir[0] == Global.Direction.RIGHT && dir[1] == Global.Direction.DOWN) ||
                        (dir[1] == Global.Direction.RIGHT && dir[0] == Global.Direction.DOWN)) {
                    shadowPoints.add(new Point(cam.right(), cam.bottom()));
                } else if ((dir[0] == Global.Direction.UP && dir[1] == Global.Direction.DOWN) ||
                        (dir[1] == Global.Direction.UP && dir[0] == Global.Direction.DOWN)) {
                    if (aliens.get(0).centerX() > points[shadowPoints.size() - 1].getX()) {
                        shadowPoints.add(new Point(cam.left(), cam.top()));
                        shadowPoints.add(new Point(cam.left(), cam.bottom()));
                    } else if (aliens.get(0).centerX() < points[shadowPoints.size() - 1].getX()) {
                        shadowPoints.add(new Point(cam.right(), cam.top()));
                        shadowPoints.add(new Point(cam.right(), cam.bottom()));
                    }
                } else if ((dir[0] == Global.Direction.LEFT && dir[1] == Global.Direction.RIGHT) ||
                        (dir[1] == Global.Direction.LEFT && dir[0] == Global.Direction.RIGHT)) {
                    if (aliens.get(0).centerY() > points[shadowPoints.size() - 1].getY()) {
                        shadowPoints.add(new Point(cam.left(), cam.top()));
                        shadowPoints.add(new Point(cam.right(), cam.top()));
                    } else if (aliens.get(0).centerY() < points[shadowPoints.size() - 1].getY()) {
                        shadowPoints.add(new Point(cam.left(), cam.bottom()));
                        shadowPoints.add(new Point(cam.right(), cam.bottom()));
                    }
                }
            }
            shadowPoints.add(point);
        }
        shadowPoints.add(shadowLines.get(1).getPoint2());
        Polygon p = new Polygon();
        shadowPoints.forEach(sp -> {
            p.addPoint((int) sp.getX(), (int) sp.getY());
        });
        g.fillPolygon(p);
        shadowPoints.clear();
        shadowLines.clear();
    }

    public void paintVictory(Graphics g, Alien.Role role) {
        role.getVictoryBG(g);
        int[][] tmp = {{399, 290}, {299, 270}, {499, 270}, {199, 250}, {599, 250}, {99, 230}};
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).getRole() == winRole) {
                aliens.get(i).painter().setTop(tmp[i][1]);
                aliens.get(i).painter().setLeft(tmp[i][0]);
                aliens.get(i).painter().setRight(tmp[i][0] + 162);
                aliens.get(i).painter().setBottom(tmp[i][1] + 219);
            }
        }
        for (int i = aliens.size() - 1; i >= 0; i--) {
            if (aliens.get(i).getRole() == winRole) {
                aliens.get(i).paintComponent(g);
            }
        }

    }

}
