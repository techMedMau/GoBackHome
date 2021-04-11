package scene;

import camera.Camera;
import controllers.ImageController;
import gameobj.*;
import maploader.MapInfo;
import maploader.MapLoader;
import utils.CommandSolver;
import utils.Global;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScene extends Scene{
    private ArrayList<Alien> aliens;
    private Camera cam;
    private Map map;
    private Image backGroundTest;
    private ArrayList<GameObject> forGame;
    private MapLoader mapLoader;
    private ArrayList<WaitingScene.Location> location;

    public GameScene(ArrayList<WaitingScene.Location> location){
        this.location = location;
    }


    @Override
    public void sceneBegin() {
        map = new Map();
        backGroundTest = ImageController.getInstance().tryGet("/dirt.png");
        mapLoader = MapGameGen();
        cam = new Camera.Builder(Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT).setChaseObj(aliens.get(0)).gen();
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
                Global.Direction direction = Global.Direction.getDirection(commandCode);
                switch (direction) {
                    case LEFT, RIGHT -> aliens.get(0).setHorizontalDir(direction);
                    case UP, DOWN -> aliens.get(0).setVerticalDir(direction);
                }
                switch (direction){
                    case DOWN:
                        for (int i=0;i<forGame.size();i++){
                            if (aliens.get(0).isCollision(forGame.get(i))&&
                                    aliens.get(0).bottomIsCollision(forGame.get(i))){
                                aliens.get(0).translateY(-2);
                                break;
                            }
                        }
                        aliens.get(0).update();
                        break;
                    case UP:
                        for (int i=0;i<forGame.size();i++){
                            if (aliens.get(0).isCollision(forGame.get(i))&&
                                    aliens.get(0).topIsCollision(forGame.get(i))){
                                aliens.get(0).translateY(2);
                                break;
                            }
                        }
                        aliens.get(0).update();
                        break;
                    case LEFT:
                        for (int i=0;i<forGame.size();i++){
                            if (aliens.get(0).isCollision(forGame.get(i))
                                    && aliens.get(0).leftIsCollision(forGame.get(i))){
                                aliens.get(0).translateX(2);
                                break;
                            }
                        }
                        aliens.get(0).update();
                        break;
                    case RIGHT:
                        for (int i=0;i<forGame.size();i++){
                            if (aliens.get(0).isCollision(forGame.get(i))&&
                                    aliens.get(0).rightIsCollision(forGame.get(i))){
                                aliens.get(0).translateX(-2);
                                break;
                            }
                        }
                        aliens.get(0).update();
                        break;
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                Global.Direction direction = Global.Direction.getDirection(commandCode);
                System.out.println(direction.name());
                switch (direction) {
                    case LEFT, RIGHT -> aliens.get(0).setHorizontalDir(Global.Direction.NO_DIR);
                    case UP, DOWN -> aliens.get(0).setVerticalDir(Global.Direction.NO_DIR);
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
        g.drawImage(backGroundTest,0,0,null);
        for (int i = 0; i < forGame.size(); i++) {
            if(cam.isCollision(forGame.get(i)))
            forGame.get(i).paint(g);
        }
        aliens.get(0).paint(g);
        aliens.get(1).paint(g);
        cam.paint(g);
        cam.end(g);
    }

    @Override
    public void update() {
        for(int i = 0; i < aliens.size(); i++) {
            if (aliens.get(i).painter().left() <= 0) {
                aliens.get(i).translateX(2);
                return;
            }
            if (aliens.get(i).painter().top() <= 0) {
                aliens.get(i).translateY(2);
            }
            if (aliens.get(i).painter().top() >= Global.SCREEN_Y) {
                aliens.get(i).translateY(-2);
            }
            if (aliens.get(i).painter().right() >= Global.SCREEN_X) {
                aliens.get(i).translateX(-2);
            }
        }
//        alien.update();
        cam.update();
    }

    public MapLoader MapGameGen(){
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
                                tmp = new GameObjectForMap("/coal.png",mapInfo.getX() * size, mapInfo.getY() * size,32,32);
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
                                tmp = new GameObjectForMap("/brown.png",mapInfo.getX() * size, mapInfo.getY() * size,32,32);
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
                                tmp = new GameObjectForMap("/diamond.png",mapInfo.getX() * size, mapInfo.getY() * size,32,32);
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
                                tmp = new GameObjectForMap("/gold.png",mapInfo.getX() * size, mapInfo.getY() * size,32,32);
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
                                tmp = new GameObjectForMap("/gravel.png",mapInfo.getX() * size, mapInfo.getY() * size,32,32);
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
                                tmp = new GameObjectForMap("/iron.png",mapInfo.getX() * size, mapInfo.getY() * size,32,32);
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
                                tmp = new GameObjectForMap("/ruby.png",mapInfo.getX() * size, mapInfo.getY() * size,32,32);
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
                                tmp = new GameObjectForMap("/silver.png",mapInfo.getX() * size, mapInfo.getY() * size,32,32);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
        }catch(IOException ex) {
            Logger.getLogger(MapScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapLoader;
    }
}
