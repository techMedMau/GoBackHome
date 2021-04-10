package scene;

import camera.Camera;
import gameobj.Alien;
import gameobj.GameObject;
import gameobj.Map;
import gameobj.tile;
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
    private Map map;
    private Camera cam;
    private ArrayList<GameObject> gameObjectArr ;
    private Alien alien;

    @Override
    public void sceneBegin() {
        map = new Map();
        gameObjectArr = new ArrayList<>();
        cam = new Camera.Builder(Global.WINDOW_WIDTH,Global.WINDOW_HEIGHT).setChaseObj(alien).gen();
        try {
            MapLoader mapLoader = new MapLoader("/genMap_wait.bmp", "/genMap_wait.txt");
            ArrayList<MapInfo> test = mapLoader.combineInfo();
            this.gameObjectArr = mapLoader.creatObjectArray("Name", 32, test, new MapLoader.CompareClass() {
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
        }catch(IOException ex) {
            Logger.getLogger(MapScene.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        return null;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public void update() {

    }
}
