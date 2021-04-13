package utils;

import scene.WaitingScene;
import java.util.HashMap;

public class Global {
    public class InternetCommand{
        public static final int CONNECT = 0;
        public static final int MOVE = 1;
        public static final int DISCONNECT = 2;
        public static final int CREAT = 3;
    }

    public enum Direction {
        UP(3),
        DOWN(0),
        LEFT(1),
        RIGHT(2),
        NO_DIR(4);
        private int value;

        Direction(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }

        public static Direction getDirection(int value){
            for(Direction d : Direction.values()){
                if(d.getValue() == value){
                    return d;
                }
            }
            return Direction.NO_DIR;
        }
    }
    public static HashMap<String,WaitingScene> WAIT_SCENES=new HashMap<>();
    public static final boolean IS_DEBUG = false;
    public static final String SERVE_IP="192.168.1.38";
    public static final String FONT="Times New Roman";
    public static void log(String str) {
        if (IS_DEBUG) {
            System.out.println(str);
        }
    }

    // 視窗大小
    public static final int MAP_WIDTH = 1280;
    public static final int MAP_HEIGHT = 1280;
    public static final int WINDOW_WIDTH = 960;
    public static final int WINDOW_HEIGHT = 640;
    public static final int SCREEN_X = WINDOW_WIDTH - 8 - 8;
    public static final int SCREEN_Y = WINDOW_HEIGHT - 31 - 8;
    //彈跳視窗大小
    public static final int POPUP_WIDTH = 640;
    public static final int POPUP_HEIGHT = 320;
    // 資料刷新時間
    public static final int UPDATE_TIMES_PER_SEC = 60;// 每秒更新60次遊戲邏輯
    public static final int NANOSECOND_PER_UPDATE = 1000000000 / UPDATE_TIMES_PER_SEC;// 每一次要花費的奈秒數
    // 畫面更新時間
    public static final int FRAME_LIMIT = 60;
    public static final int LIMIT_DELTA_TIME = 1000000000 / FRAME_LIMIT;
    //人物移動速度
    public static final int MOVE_SPEED = 2;

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

}

//if(Global.random(10)){}