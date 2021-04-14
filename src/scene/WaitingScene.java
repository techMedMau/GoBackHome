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
    private int currentPlay;
    private int playMax;
    private int traitor;
    private String password;
    private Button startButton;
    private int num;
    private ArrayList<GameObject> forWaitingRoom;
    private MapLoader mapLoader;
    private ArrayList<Alien> aliens;
    private int homeOwner;
    public WaitingScene(String password,int traitor,int playMax,int homeOwner){
        this.password=password;
        this.traitor=traitor;
        this.playMax=playMax;
        this.homeOwner=homeOwner;
        this.currentPlay=1;
    }

    @Override
    public void sceneBegin() {
        aliens = new ArrayList<>();
        num = (int) (Math.random() * 5 + 1);
        //要傳出去的東西
        ArrayList<String> str = new ArrayList<>();
        str.add("500");
        str.add("500");
        str.add(num+"");
        aliens.add(new Alien(Integer.parseInt(str.get(0)), Integer.parseInt(str.get(1)), num));
        ClientClass.getInstance().sent(Global.InternetCommand.CONNECT,str);
        aliens.get(0).setId(ClientClass.getInstance().getID());
        startButton = new Button(400, 500, 120, 55, ImageController.getInstance()
                .tryGet("/Picture1.png"));
        mapLoader = MapWaitGen();
    }

    @Override
    public void sceneEnd() {

    }
    public boolean canEnter(){
        return currentPlay<playMax;
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
        return (e, state, trigTime) -> {
            if (state == CommandSolver.MouseState.CLICKED) {
                if (ClientClass.getInstance().getID()==homeOwner&&startButton.state(e.getPoint())) {
                    for (int i=0;i<traitor;i++){
                        while (true){
                            int select=Global.random(0,aliens.size()-1);
                            if (!aliens.get(select).isTraitor()){
                                aliens.get(select).setTraitor();
                                ArrayList<String> str=new ArrayList<>();
                                str.add(String.valueOf(aliens.get(select).getId()));
                                ClientClass.getInstance().sent(Global.InternetCommand.TRAITOR,str);
                                break;
                            }
                        }
                    }
                    ArrayList<String> str=new ArrayList<>();
                    str.add(password);
                    ClientClass.getInstance().sent(Global.InternetCommand.START,str);
                    Global.WAIT_SCENES.remove(password,this);
                    SceneController.getInstance().changeScene(new GameScene(aliens));
                }
            }
        };
    }

        @Override
        public void paint (Graphics g){

            for (int i = 0; i < forWaitingRoom.size(); i++) {
                forWaitingRoom.get(i).paint(g);
            }
            for (int i = 0; i < aliens.size(); i++) {
                aliens.get(i).paint(g);
            }
            if (ClientClass.getInstance().getID()==homeOwner){
                startButton.paint(g);
            }
            Font font=new Font(Global.FONT,Font.PLAIN,30);
            g.setFont(font);
            g.drawString(password,395,480);
            Font numFont=new Font(Global.FONT,Font.PLAIN,20);
            g.setFont(numFont);
            g.drawString(currentPlay+"/"+playMax,445,580);
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
                strr.add(aliens.get(0).getHorizontalDir().getValue()+"");
            }else if(aliens.get(0).getVerticalDir() == Global.Direction.DOWN || aliens.get(0).getVerticalDir() == Global.Direction.UP){
                strr.add(aliens.get(0).getVerticalDir().getValue()+"");
            }else{
                strr.add(Global.Direction.NO_DIR.getValue()+"");
            }
            ClientClass.getInstance().sent(Global.InternetCommand.MOVE,strr);
            ClientClass.getInstance().consume((serialNum, internetcommand, strs) -> {
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
                            aliens.add(new Alien(Integer.parseInt(strs.get(0)), Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2))));
                            aliens.get(aliens.size() - 1).setId(serialNum);
                            ArrayList<String> str=new ArrayList<>();
                            str.add(aliens.get(0).painter().centerX()+"");
                            str.add(aliens.get(0).painter().centerY()+"");
                            str.add(aliens.get(0).getNum()+"");
                            currentPlay++;
                            ClientClass.getInstance().sent(Global.InternetCommand.CONNECT,str);
                        }
                        break;
                    case Global.InternetCommand.MOVE:
                        for(int i=1;i<aliens.size();i++) {
                            if(aliens.get(i).getId()==Integer.parseInt(strs.get(0))) {
                                //setCenter? 還是應該要是setX setY?
                                aliens.get(i).painter().setCenter(Integer.parseInt(strs.get(1)),Integer.parseInt(strs.get(2)));
                                aliens.get(i).collider().setCenter(Integer.parseInt(strs.get(1)),Integer.parseInt(strs.get(2)));
                                if(aliens.get(i).getHorizontalDir() == Global.Direction.LEFT || aliens.get(i).getHorizontalDir() == Global.Direction.RIGHT) {
                                    aliens.get(i).setHorizontalDir(Global.Direction.getDirection(Integer.parseInt(strs.get(3))));
                                }else if(aliens.get(i).getVerticalDir() == Global.Direction.DOWN || aliens.get(i).getVerticalDir() == Global.Direction.UP){
                                    aliens.get(i).setVerticalDir(Global.Direction.getDirection(Integer.parseInt(strs.get(3))));
                                }else if(aliens.get(i).getNoDirection()){
                                    aliens.get(i).setVerticalDir(Global.Direction.getDirection(Integer.parseInt(strs.get(3))));
                                    aliens.get(i).setHorizontalDir(Global.Direction.getDirection(Integer.parseInt(strs.get(3))));
                                }
                                break;
                            }
                        }
                        break;
                    case Global.InternetCommand.DISCONNECT:
                        for(int i=0;i<aliens.size();i++){
                            if(aliens.get(i).getId()==Integer.parseInt(strs.get(0))){
                                aliens.remove(i);
                            }
                        }
                        break;
                    case Global.InternetCommand.TRAITOR:
                        for(int i=0;i<aliens.size();i++){
                            if(aliens.get(i).getId()==Integer.parseInt(strs.get(0))){
                                aliens.get(i).setTraitor();
                            }
                        }
                        break;
                    case Global.InternetCommand.START:
                        Global.WAIT_SCENES.remove(strs.get(0),Global.WAIT_SCENES.get(strs.get(0)));
                        SceneController.getInstance().changeScene(new GameScene(aliens));
                        break;
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
                Logger.getLogger(WaitingScene.class.getName()).log(Level.SEVERE, null, ex);
            }
            return mapLoader;
        }




}