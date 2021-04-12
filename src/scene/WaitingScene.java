package scene;

import controllers.ImageController;
import controllers.SceneController;
import gameobj.*;
import internet.server.ClientClass;
import internet.server.CommandReceiver;
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
    private int playMax;
    private int traitor;
    private String password;
    private Button startButton;
    private int num;
    private ArrayList<GameObject> forWaitingRoom;
    private MapLoader mapLoader;
    private ArrayList<Alien> aliens;
    public WaitingScene(String password,int traitor,int playMax){
        this.password=password;
        this.traitor=traitor;
        this.playMax=playMax;
    }

    @Override
    public void sceneBegin() {
        aliens = new ArrayList<>();
        num = (int) (Math.random() * 3 + 1);
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
        //要傳出去的東西
        ArrayList<String> str = new ArrayList<>();
        str.add("450");
        str.add("300");
        str.add(String.valueOf(num));
        aliens.add(new Alien(Integer.valueOf(str.get(0)), Integer.valueOf(str.get(1)), num));
        ClientClass.getInstance().sent(Global.InternetCommand.CONNECT,str);
        aliens.get(0).setId(ClientClass.getInstance().getID());
        startButton = new Button(400, 500, 120, 55, ImageController.getInstance()
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
                if(commandCode==6){  //角色斷線時發送斷線訊息
                    ArrayList<String> strs = new ArrayList<String>();
                    strs.add(String.valueOf(ClientClass.getInstance().getID()));
                    ClientClass.getInstance().sent(Global.InternetCommand.DISCONNECT,strs);
                    ClientClass.getInstance().disConnect();
                    System.exit(0);
                }
                for(int i = 0; i< aliens.size(); i ++) {
                    switch (direction) {
                        case LEFT:
                            aliens.get(i).setHorizontalDir(direction);
                            break;
                        case RIGHT:
                            aliens.get(i).setHorizontalDir(direction);
                            break;
                        case UP:
                            aliens.get(i).setVerticalDir(direction);
                            break;
                        case DOWN:
                            aliens.get(i).setVerticalDir(direction);
                            break;
                    }
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                Global.Direction direction = Global.Direction.getDirection(commandCode);
                for(int i = 0; i< aliens.size(); i ++) {
                    switch (direction) {
                        case LEFT:
                            aliens.get(i).setHorizontalDir(Global.Direction.NO_DIR);
                            break;
                        case RIGHT:
                            aliens.get(i).setHorizontalDir(Global.Direction.NO_DIR);
                            break;
                        case UP:
                            aliens.get(i).setVerticalDir(Global.Direction.NO_DIR);
                            break;
                        case DOWN:
                            aliens.get(i).setVerticalDir(Global.Direction.NO_DIR);
                            break;
                    }
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
                    if (startButton.state(e.getPoint())) {

                        SceneController.getInstance().changeScene(new GameScene(aliens));
                    }
                }
            }
        };
    }

        @Override
        public void paint (Graphics g){
            for (int i = 0; i < forWaitingRoom.size(); i++) {
                forWaitingRoom.get(i).paint(g);
            }
            startButton.paint(g);

            for (int i = 0; i < aliens.size(); i++) {
                aliens.get(i).paint(g);
            }
            Font font=new Font(Global.FONT,Font.PLAIN,30);
            g.setFont(font);
            g.drawString(password,395,480);
        }


        @Override
        public void update () {
            for (int i = 0; i < aliens.size(); i++) {
                if (aliens.get(i).painter().left() <= 0) {
                    aliens.get(i).translateX(2);
                    return;
                }
                if (aliens.get(i).painter().top() <= 0) {
                    aliens.get(i).translateY(2);
                    return;
                }
                if (aliens.get(i).painter().bottom() >= Global.SCREEN_Y) {
                    aliens.get(i).translateY(-2);
                    return;
                }
                if (aliens.get(i).painter().right() >= Global.SCREEN_X) {
                    aliens.get(i).translateX(-2);
                    return;
                }
                aliens.get(i).update();
            }
            ArrayList<String> strr = new ArrayList<>();
            strr.add(ClientClass.getInstance().getID()+"");
            strr.add(aliens.get(0).painter().centerX()+"");
            strr.add(aliens.get(0).painter().centerY()+"");
//            strr.add(aliens.get(0).getDir()+"");
            if(aliens.get(0).getHorizontalDir() == Global.Direction.LEFT || aliens.get(0).getHorizontalDir() == Global.Direction.RIGHT) {
                strr.add(aliens.get(0).getHorizontalDir()+"");
            }else if(aliens.get(0).getVerticalDir() == Global.Direction.DOWN || aliens.get(0).getVerticalDir() == Global.Direction.UP){
                strr.add(aliens.get(0).getVerticalDir()+"");
            }else{
                strr.add("NO_DIR");
            }
            ClientClass.getInstance().sent(Global.InternetCommand.MOVE,strr);
            ClientClass.getInstance().consume(new CommandReceiver() {
                @Override
                public void receive(int serialNum, int internetcommand, ArrayList<String> strs) {
                    switch(internetcommand){
                        case Global.InternetCommand.CONNECT:
                            System.out.println("Connect " + serialNum);
                            boolean isBorn = false;
                            for (int i = 0; i < aliens.size(); i++) {
                                if (aliens.get(i).getId() == serialNum) {
                                    isBorn = true;
                                    break;
                                }
                            }
                            if(!isBorn) {
                                aliens.add(new Alien(Integer.valueOf(strs.get(0)), Integer.valueOf(strs.get(1)), Integer.valueOf(strs.get(2))));
                                aliens.get(aliens.size() - 1).setId(serialNum);
                                ArrayList<String> str=new ArrayList<>();
                                str.add(aliens.get(0).painter().centerX()+"");
                                str.add(aliens.get(0).painter().centerY()+"");
                                str.add(aliens.get(0).getNum()+"");
                                ClientClass.getInstance().sent(Global.InternetCommand.CONNECT,str);
                            }
                            break;
                        case Global.InternetCommand.MOVE:
                            for(int i=1;i<aliens.size();i++) {
                                if(aliens.get(i).getId()==Integer.valueOf(strs.get(0))) {
                                    aliens.get(i).painter().setCenter(Integer.valueOf(strs.get(1)),Integer.valueOf(strs.get(2)));
                                    if(aliens.get(i).getHorizontalDir() == Global.Direction.LEFT || aliens.get(i).getHorizontalDir() == Global.Direction.RIGHT) {
                                        aliens.get(i).setHorizontalDir(Global.Direction.getDirection(Integer.valueOf(strs.get(3))));
                                    }else if(aliens.get(i).getVerticalDir() == Global.Direction.DOWN || aliens.get(i).getVerticalDir() == Global.Direction.UP){
                                        aliens.get(i).setVerticalDir(Global.Direction.getDirection(Integer.valueOf(strs.get(3))));
                                    }else if(aliens.get(i).getNoDirection()){
                                        aliens.get(i).setVerticalDir(Global.Direction.getDirection(Integer.valueOf(strs.get(3))));
                                        aliens.get(i).setHorizontalDir(Global.Direction.getDirection(Integer.valueOf(strs.get(3))));
                                    }
                                    break;
                                }
                            }
                            break;
                        case Global.InternetCommand.DISCONNECT:
                            for(int i=0;i<aliens.size();i++){
                                if(aliens.get(i).getId()==Integer.valueOf(strs.get(0))){
                                    aliens.remove(i);
                                }
                            }
                            break;
                    }
                }
            });
        }

        public MapLoader MapWaitGen () {
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




}