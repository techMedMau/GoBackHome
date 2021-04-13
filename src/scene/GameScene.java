package scene;

import camera.Camera;
import controllers.ImageController;
import controllers.TaskController;
import gameobj.*;
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
    private Image backGroundTest;
    private ArrayList<GameObject> forGame;
    private MapLoader mapLoader;
    private ArrayList<TaskItem> taskItems;
    private TaskController taskController;


    public GameScene(ArrayList<Alien> aliens) {
        this.aliens = aliens;
    }


    @Override
    public void sceneBegin() {
        map = new Map();
        backGroundTest = ImageController.getInstance().tryGet("/ground.png");
        mapLoader = MapGameGen();
        cam = new Camera.Builder(Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT).setChaseObj(aliens.get(0)).gen();
        taskItems = new ArrayList<>();
        taskItems.add(new TaskItem("/boxItem.png", 161, 441, TaskController.Task.FIND_PRAY));
        taskItems.add(new TaskItem("/greenBox.png", 300, 500, TaskController.Task.LIT_UP));
        taskItems.add(new TaskItem("/redBox.png", 790, 1110, TaskController.Task.LUMBER));
        taskItems.add(new TaskItem("/warningBox.png", 600, 600, TaskController.Task.WATER));
        taskController = TaskController.getTaskController();
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            if (state == null) {
                return;
            }
            if (taskController.getCurrentPopUp()!=null&&taskController.getCurrentPopUp().isShow()){
                taskController.getCurrentPopUp().mouseListener().mouseTrig(e,state,trigTime);
                return;
            }
            switch (state) {
                case CLICKED:
                    for (int i = 0; i < taskItems.size(); i++) {
                        if (taskItems.get(i).getState() && taskItems.get(i).state(e.getX() + cam.painter().left(), e.getY() + cam.painter().top())) {
                            taskController.changePopUp(taskItems.get(i).getTask());
                            break;
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
        g.drawImage(backGroundTest, 0, 0, null);
        for (int i = 0; i < forGame.size(); i++) {
            if (cam.isCollision(forGame.get(i)))
                forGame.get(i).paint(g);
        }
        for (int i = 0; i < aliens.size(); i++) {
            aliens.get(i).paint(g);
        }
        for (int i = 0; i < taskItems.size(); i++) {
            taskItems.get(i).paint(g);
        }

        cam.paint(g);
        cam.end(g);
        if (taskController.getCurrentPopUp() != null && taskController.getCurrentPopUp().isShow()) {
            taskController.getCurrentPopUp().paint(g);
        }

    }

    @Override
    public void update() {
        aliens.get(0).update();
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
//                for(int i = 0; i < taskItems.size(); i++){
//                    if (aliens.get(0).isCollision(taskItems.get(i))
//                            && aliens.get(0).leftIsCollision(taskItems.get(i))) {
//                        aliens.get(0).translateX(Global.MOVE_SPEED);
//                        break;
//                    }
//                }
                break;
            case RIGHT:
                for (int i = 0; i < forGame.size(); i++) {
                    if (aliens.get(0).isCollision(forGame.get(i)) &&
                            aliens.get(0).rightIsCollision(forGame.get(i))) {
                        System.out.println("?");
                        aliens.get(0).translateX(-Global.MOVE_SPEED);
                        break;
                    }
                }
//                for (int i = 0; i < taskItems.size(); i++) {
//                    if (aliens.get(0).isCollision(taskItems.get(i)) &&
//                            aliens.get(0).rightIsCollision(taskItems.get(i))) {
//                        aliens.get(0).translateX(-Global.MOVE_SPEED);
//                        break;
//                    }
//                }
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
//                for (int i = 0; i < taskItems.size(); i++) {
//                    if (aliens.get(0).isCollision(taskItems.get(i)) &&
//                        aliens.get(0).bottomIsCollision(taskItems.get(i))) {
//                    aliens.get(0).translateY(-Global.MOVE_SPEED);
//                    break;
//                }
//            }
                break;
            case UP:
                for (int i = 0; i < forGame.size(); i++) {
                    if (aliens.get(0).isCollision(forGame.get(i)) &&
                            aliens.get(0).topIsCollision(forGame.get(i))) {
                        aliens.get(0).translateY(Global.MOVE_SPEED);
                        break;
                    }
                }
//                for (int i=0;i<taskItems.size();i++){
//                    if (aliens.get(0).isCollision(taskItems.get(i))&&
//                        aliens.get(0).topIsCollision(taskItems.get(i))){
//                    aliens.get(0).translateY(Global.MOVE_SPEED);
//                    break;
//                }
//            }
                break;
        }
        for (int i = 0; i < taskItems.size(); i++) {
            taskItems.get(i).isTriggered(aliens.get(0));
        }
        if (taskController.getCurrentPopUp() != null && taskController.getCurrentPopUp().isShow()) {
            taskController.getCurrentPopUp().update();
        }
        cam.update();
    }

    public MapLoader MapGameGen() {
        try {
            mapLoader = new MapLoader("/genMap.bmp", "/genMap.txt");
            ArrayList<MapInfo> test = mapLoader.combineInfo();
            forGame = new ArrayList<>();
            // to be continued
            this.forGame = mapLoader.creatObjectArray("coal", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/coal.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            );
            this.forGame.addAll(mapLoader.creatObjectArray("Name", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/brown.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("diamond", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/diamond.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("gold", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/gold.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("gravel", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/gravel.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("iron", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/iron.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("ruby", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/ruby.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("silver", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/silver.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
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
}
