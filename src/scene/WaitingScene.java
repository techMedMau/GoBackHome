package scene;

import camera.Camera;
import camera.MapInformation;
import controllers.ImageController;
import controllers.SceneController;
import gameobj.*;
import maploader.MapInfo;
import maploader.MapLoader;
import utils.CommandSolver;
import utils.Global;
import gameobj.button.Button;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WaitingScene extends Scene {
    private Button startButton;
    private Alien alien;
    private int num;
    private Camera cam;
    private Map map;
    private ArrayList<GameObject> forWaitingRoom;
    private ArrayList<GameObject> forGame;
    private MapLoader mapLoader;
    private Image img;

    @Override
    public void sceneBegin() {
        map = new Map();
        num = (int) (Math.random() * 3 + 1);
        switch (num) {
            case 1:
                alien = new Alien(450, 300, 72, 97, Alien.AlienType.A);
                break;
            case 2:
                alien = new Alien(450, 300, 72, 97, Alien.AlienType.B);
                break;
            case 3:
                alien = new Alien(450, 300, 72, 97, Alien.AlienType.C);
                break;
        }
        startButton = new Button(300, 100, 300, 300, ImageController.getInstance()
                .tryGet("/Picture1.png"));
        cam = new Camera.Builder(Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT).setChaseObj(alien).gen();
        mapLoader = MapGameGen();
        img = ImageController.getInstance().tryGet("/waitingRoomBackGround.jpg");
    }

    @Override
    public void sceneEnd() {
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                Global.Direction direction = Global.Direction.getDirection(commandCode);
                switch (direction) {
                    case LEFT:
                        alien.setHorizontalDir(direction);
                        break;
                    case RIGHT:
                        alien.setHorizontalDir(direction);
                        break;
                    case UP:
                        alien.setVerticalDir(direction);
                        break;
                    case DOWN:
                        alien.setVerticalDir(direction);
                        break;
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                Global.Direction direction = Global.Direction.getDirection(commandCode);
                switch (direction) {
                    case LEFT:
                        alien.setHorizontalDir(Global.Direction.NO_DIR);
                        break;
                        case RIGHT :
                        alien.setHorizontalDir(Global.Direction.NO_DIR);
                        break;
                    case UP:
                        alien.setVerticalDir(Global.Direction.NO_DIR);
                        break;
                    case DOWN :
                        alien.setVerticalDir(Global.Direction.NO_DIR);
                        break;
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return new CommandSolver.MouseListener() {
            @Override
            public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
                if (state == CommandSolver.MouseState.CLICKED) {
                    mapLoader = MapGameGen();
                }
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        cam.start(g);
        for (int i = 0; i < forGame.size(); i++) {
            forGame.get(i).paint(g);
        }
        g.drawImage(img, 0, 0, 960, 640, null);
        startButton.paint(g);
        alien.paint(g);
//        cam.paint(g);
        cam.end(g);
    }

    @Override
    public void update() {
        alien.update();
        cam.update();
    }

    public MapLoader MapWaitGen() {
        try {
            mapLoader = new MapLoader("/genMap_wait.bmp", "/genMap_wait.txt");
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
            Logger.getLogger(MapScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapLoader;
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
            this.forGame.addAll(mapLoader.creatObjectArray("dirt32", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/dirt32.png", mapInfo.getX() * size, mapInfo.getY() * size, 32, 32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("dirt64", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/dirt64.png", mapInfo.getX() * size, mapInfo.getY() * size, 64, 64);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("dirt96", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/dirt96.png", mapInfo.getX() * size, mapInfo.getY() * size, 96, 96);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.forGame.addAll(mapLoader.creatObjectArray("dirt128", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new GameObjectForMap("/dirt128.png", mapInfo.getX() * size, mapInfo.getY() * size, 128, 128);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));

        } catch (IOException ex) {
            Logger.getLogger(MapScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapLoader;
    }
}