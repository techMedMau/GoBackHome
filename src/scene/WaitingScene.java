package scene;
import camera.Camera;
import camera.MapInformation;
import camera.SmallMap;
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

    private int num;
    private ArrayList<GameObject> forWaitingRoom ;
    private MapLoader mapLoader;
    private ArrayList<Location> location;
    private Alien a;
    private Alien b;

    @Override
    public void sceneBegin() {
      
        location = new ArrayList<>();
//        num = (int) (Math.random() * 3 + 1);
//        switch (num) {
//            case 1:
//                alien = new Alien(450, 300, 54, 73, Alien.AlienType.A);
//                break;
//            case 2:
//                alien = new Alien(450, 300, 54, 73, Alien.AlienType.B);
//                break;
//            case 3:
//                alien = new Alien(450, 300, 54, 73, Alien.AlienType.C);
//                break;
//        }
        a = new Alien(450, 300, 54, 73, Alien.AlienType.A);
        b = new Alien(500, 200, 54, 73, Alien.AlienType.B);
        location.add(new Location(a,a.painter().left(),a.painter().top()));
        location.add(new Location(b,b.painter().left(), b.painter().top()));
        startButton = new Button(400,500,300,300, ImageController.getInstance()
                .tryGet("/Picture1.png"));
        mapLoader = MapWaitGen();
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
                    case LEFT, RIGHT -> location.get(0).alien.setHorizontalDir(direction);
                    case UP, DOWN -> location.get(0).alien.setVerticalDir(direction);
                }
                switch (direction) {
                    case LEFT, RIGHT -> location.get(1).alien.setHorizontalDir(direction);
                    case UP, DOWN -> location.get(1).alien.setVerticalDir(direction);
                }

            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                Global.Direction direction = Global.Direction.getDirection(commandCode);
                switch (direction) {
                    case LEFT, RIGHT -> location.get(0).alien.setHorizontalDir(Global.Direction.NO_DIR);
                    case UP, DOWN -> location.get(0).alien.setVerticalDir(Global.Direction.NO_DIR);
                }
                switch (direction) {
                    case LEFT, RIGHT -> location.get(1).alien.setHorizontalDir(Global.Direction.NO_DIR);
                    case UP, DOWN -> location.get(1).alien.setVerticalDir(Global.Direction.NO_DIR);
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
                if(state == CommandSolver.MouseState.CLICKED){
                    if(startButton.state(e.getPoint())) {

                        SceneController.getInstance().changeScene(new GameScene(location));
//
//                        cam = new Camera.Builder(Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT).setChaseObj(alien).gen();
                    }
                }
            }
        };
    }

    @Override
    public void paint(Graphics g) {

            for (int i = 0; i < forWaitingRoom.size(); i++) {
                forWaitingRoom.get(i).paint(g);
            }
            startButton.paint(g);

        for(int i = 0; i < location.size(); i++) {
            location.get(i).alien.paint(g);
        }

    }

    @Override
    public void update() {
        for(int i = 0; i < location.size(); i++) {
            if (location.get(i).alien.painter().left() <= 0) {
                location.get(i).alien.translateX(2);
                return;
            }
            if (location.get(i).alien.painter().top() <= 0) {
                location.get(i).alien.translateY(2);
                return;
            }
            if (location.get(i).alien.painter().top() >= Global.SCREEN_Y) {
                location.get(i).alien.translateY(-2);
                return;
            }
            if (location.get(i).alien.painter().right() >= Global.SCREEN_X) {
                location.get(i).alien.translateX(-2);
                return;
            }

            location.get(i).alien.update();
        }
    }

    public MapLoader MapWaitGen(){
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

        }catch(IOException ex) {
            Logger.getLogger(MapScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapLoader;
    }

    public static class Location{ //私有的靜態內部類
        private Alien alien;
        private int x;
        private int y;

        public Location(Alien alien,int x, int y){
            this.alien = alien;
            this.x = x;
            this.y = y;
        }

    }
}