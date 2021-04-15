package scene;

import camera.Camera;
import controllers.ImageController;
import controllers.SceneController;
import controllers.TaskController;
import gameobj.*;
import internet.server.ClientClass;
import internet.server.CommandReceiver;
import maploader.MapInfo;
import maploader.MapLoader;
import utils.CommandSolver;
import utils.Global;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScene extends Scene {
    private ArrayList<Alien> aliens;
    private Camera cam;
    private Map map;
    private Image backGround;
    private ArrayList<GameObject> forGame;
    private MapLoader mapLoader;
    private ArrayList<TaskItem> taskItems;
    private TaskController taskController;
    private Image infoBoard;
    private BackgroundItem backgroundItem;

    public GameScene(ArrayList<Alien> aliens) {
        this.aliens = aliens;
        aliens.forEach(alien -> {
            alien.painter().setCenter(630, 200);
            alien.collider().setCenter(630, 200);
        });
    }

    @Override
    public void sceneBegin() {
        map = new Map();
        backGround = ImageController.getInstance().tryGet("/background.png");
        mapLoader = MapGameGen();
        cam = new Camera.Builder(Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT).setChaseObj(aliens.get(0)).gen();
        taskItems = new ArrayList<>();
        taskItems.add(new TaskItem("/taskBox/boxItem.png", 105, 441, TaskController.Task.GASOLINE));
        taskItems.add(new TaskItem("/taskBox/greenBox.png", 300, 500, TaskController.Task.COLOR_CHANGE));
        taskItems.add(new TaskItem("/taskBox/redBox.png", 790, 1110, TaskController.Task.FIND_PIC));
        taskItems.add(new TaskItem("/taskBox/warningBox.png", 600, 600, TaskController.Task.LINE_UP));
        taskItems.add(new TaskItem("/taskBox/woodBox.png", 1000, 66, TaskController.Task.PASSWORD));
        taskItems.add(new TaskItem("/taskBox/blueBox.png", 1750, 900, TaskController.Task.PUSH));
        taskItems.add(new TaskItem("/taskBox/warningWood.png", 1600, 400, TaskController.Task.ROCK));
        taskItems.add(new TaskItem("/taskBox/blackBox.png", 1100, 400, TaskController.Task.CENTER));
        taskController = TaskController.getTaskController();
        this.infoBoard = ImageController.getInstance().tryGet("/infoBoard.png");
        this.backgroundItem = new BackgroundItem("/arrowRight.png", 500, 500 + 32, 28, 8, 500, 500, 64, 64);
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            if (state == null || aliens.get(0).getCurrentState() == Alien.State.DEATH) {
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
                            taskController.changePopUp(taskItems.get(i).getTask());
                            return;
                        }
                    }
                    for (int i = 1; i < aliens.size(); i++) {
                        if (aliens.get(0).isTraitor() && aliens.get(0).isTriggered(aliens.get(i)) && !aliens.get(i).isTraitor() && aliens.get(i).state(e.getX() + cam.painter().left(), e.getY() + cam.painter().top())) {
                            aliens.get(i).death();
                            ArrayList<String> str = new ArrayList<>();
                            str.add(aliens.get(i).getId() + "");
                            ClientClass.getInstance().sent(Global.InternetCommand.DEATH, str);
                            return;
                        }
                    }
                    for (int i = 1; i < aliens.size(); i++) {
                        if (aliens.get(0).isTriggered(aliens.get(i)) && aliens.get(i).getCurrentState() == Alien.State.DEATH && aliens.get(i).state(e.getX() + cam.painter().left(), e.getY() + cam.painter().top())) {
                            ArrayList<String> str = new ArrayList<>();
                            str.add(String.valueOf(aliens.get(0).getId()));
                            ClientClass.getInstance().sent(Global.InternetCommand.TO_VOTE, str);
                            SceneController.getInstance().changeScene(new VoteScene(aliens,aliens.get(0).getId()));
                            return;
                        }
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
                if (aliens.get(0).getCurrentState() == Alien.State.DEATH) {
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
                if (aliens.get(0).getCurrentState() == Alien.State.DEATH) {
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

            }
        };
    }

    @Override
    public void paint(Graphics g) {
        cam.start(g);
        g.drawImage(backGround, 0, 0, null);
        Font font = new Font(Global.FONT, Font.PLAIN, 20);
        for (int i = 0; i < forGame.size(); i++) {
            if (cam.isCollision(forGame.get(i)))
                forGame.get(i).paint(g);
        }
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(0).isTraitor()) {
                g.setColor(Color.RED);
                g.setFont(font);
                if (aliens.get(i).isTraitor()) {
                    g.drawString("Traitor", aliens.get(i).painter().centerX() - 28, aliens.get(i).painter().top());
                }
                if (!aliens.get(i).isTraitor() && aliens.get(0).isTriggered(aliens.get(i))) {
                    g.drawString("Kill?", aliens.get(i).painter().centerX() - 20, aliens.get(i).painter().top());
                }
            }
            if (aliens.get(0).isTriggered(aliens.get(i)) && aliens.get(i).getCurrentState() == Alien.State.DEATH) {
                g.drawString("Report?", aliens.get(i).painter().centerX() - 28, aliens.get(i).painter().top());
            }
            aliens.get(i).paint(g);
        }
        for (int i = 0; i < taskItems.size(); i++) {
            taskItems.get(i).paint(g);

        }
        backgroundItem.paint(g);
        cam.paint(g);
        cam.end(g);
        drawInfo(g);
        if (taskController.getCurrentPopUp() != null && taskController.getCurrentPopUp().isShow()) {
            taskController.getCurrentPopUp().paint(g);
        }


    }

    @Override
    public void update() {
        aliens.get(0).update();
        colliedWithMap();
        colliedWithWall();
        ArrayList<String> str = new ArrayList<>();
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
                switch (commandCode) {
                    case Global.InternetCommand.MOVE:
                        for (int i = 1; i < aliens.size(); i++) {
                            if (aliens.get(i).getId() == Integer.parseInt(strs.get(0))) {
                                aliens.get(i).painter().setCenter(Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)));
                                aliens.get(i).collider().setCenter(Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)));
                                aliens.get(i).setHorizontalDir(Global.Direction.getDirection(Integer.parseInt(strs.get(3))));
                                aliens.get(i).setVerticalDir(Global.Direction.getDirection(Integer.parseInt(strs.get(4))));
                                break;
                            }
                        }
                        break;
                    case Global.InternetCommand.DEATH:
                        for (int i = 1; i < aliens.size(); i++) {
                            if (aliens.get(i).getId() == Integer.parseInt(strs.get(0))) {
                                aliens.get(i).death();
                                break;
                            }
                        }
                        break;
                    case Global.InternetCommand.TO_VOTE:
                        SceneController.getInstance().changeScene(new VoteScene(aliens,Integer.parseInt(str.get(0))));
                        break;
                }
            }
        });
    }

    public MapLoader MapGameGen() {
        try {
            mapLoader = new MapLoader("/genMap.bmp", "/genMap.txt");
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

    public void colliedWithMap() {
        for (int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).painter().left() <= map.painter().left()) {
                aliens.get(i).translateX(Global.MOVE_SPEED);
                return;
            }
            if (aliens.get(i).painter().top() <= map.painter().top()) {
                aliens.get(i).translateY(Global.MOVE_SPEED);
                return;
            }
            if (aliens.get(i).painter().bottom() >= map.painter().bottom()) {
                aliens.get(i).translateY(-Global.MOVE_SPEED);
                return;
            }
            if (aliens.get(i).painter().right() >= map.painter().right()) {
                aliens.get(i).translateX(-Global.MOVE_SPEED);
                return;
            }
        }
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

    public void drawInfo(Graphics g) {
        g.drawImage(infoBoard, 0, 0, null);
        g.setColor(Color.BLACK);
        Font font = new Font(Global.FONT, Font.PLAIN, 15);
        g.setFont(font);
        g.drawString("Task1", 10, 30);
        g.drawString("Task2", 10, 50);
        g.drawString("Task3", 10, 70);
        g.drawString("Task4", 10, 90);
        g.drawString("Task5", 10, 110);
        g.drawString("Task6", 10, 130);
        g.drawString("Task7", 10, 150);
        g.drawString("Task8", 10, 170);
    }
}
