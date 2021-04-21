package gameobj;

import controllers.ImageController;
import controllers.TaskController;
import gameobj.button.ClickState;
import gameobj.button.Range;
import scene.popupwindow.PopUpTask;
import utils.Animator;
import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Alien extends GameObject implements ClickState, Range {
    @Override
    public boolean isTriggered(Alien alien) {
        return (Math.sqrt(Math.abs((alien.painter().centerX() - this.painter().centerX()) * (alien.painter().centerX() - this.painter().centerX())
                + (alien.painter().centerY() - this.painter().centerY()) * (alien.painter().centerY() - this.painter().centerY()))) < 90.0) ;
    }

    @Override
    public boolean state(Point point){
        return point.getX()>painter().left() && point.getX()<painter().right()&&
                point.getY()>painter().top() && point.getY()<painter().bottom();
    }
    @Override
    public boolean state(int x,int y){
        return x>painter().left() && x<painter().right()&&
                y>painter().top() && y<painter().bottom();
    }
    public void setTraitor(){
        isTraitor=true;
    }
    public boolean isTraitor(){
        return isTraitor;
    }

    public enum AlienType {
        A,
        B,
        C,
        D,
        E,
        F,
        G,
        H
    }

    private static HashMap<State, Animator> getDeathAnimator(AlienType type){
        switch (type) {
            case A:
                HashMap<State, Animator> map = new HashMap<>();
                map.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p3_dieBody2.png"))));
                map.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p3_dieBody2.png"))));
                map.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p3_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p3_dieBody2.png")
                )));
                map.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p3_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p3_dieBody2.png")
                )));
                return map;
            case B:
                HashMap<State, Animator> map1 = new HashMap<>();
                map1.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p2_dieBody2.png"))));
                map1.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p2_dieBody2.png"))));
                map1.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p2_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p2_dieBody2.png")
                )));
                map1.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p2_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p2_dieBody2.png")
                )));
                return map1;
            case C:
                HashMap<State, Animator> map2 = new HashMap<>();
                map2.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p1_dieBody2.png"))));
                map2.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p1_dieBody2.png"))));
                map2.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p1_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p1_dieBody2.png")
                )));
                map2.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p1_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p1_dieBody2.png")
                )));
                return map2;
            case D:
                HashMap<State, Animator> map3 = new HashMap<>();
                map3.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p4_dieBody2.png"))));
                map3.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p4_dieBody2.png"))));
                map3.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p4_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p4_dieBody2.png")
                )));
                map3.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p4_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p4_dieBody2.png")
                )));
                return map3;
            case E:
                HashMap<State, Animator> map4 = new HashMap<>();
                map4.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p5_dieBody2.png"))));
                map4.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p5_dieBody2.png"))));
                map4.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p5_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p5_dieBody2.png")
                )));
                map4.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p5_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p5_dieBody2.png")
                )));
                return map4;
            case F:
                HashMap<State, Animator> map5 = new HashMap<>();
                map5.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p6_dieBody2.png"))));
                map5.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p6_dieBody2.png"))));
                map5.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p6_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p6_dieBody2.png")
                )));
                map5.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p6_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p6_dieBody2.png")
                )));
                return map5;
            case G:
                HashMap<State, Animator> map6 = new HashMap<>();
                map6.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p7_dieBody2.png"))));
                map6.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p7_dieBody2.png"))));
                map6.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p7_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p7_dieBody2.png")
                )));
                map6.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p7_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p7_dieBody2.png")
                )));
                return map6;
            case H:
                HashMap<State, Animator> map7 = new HashMap<>();
                map7.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p8_dieBody2.png"))));
                map7.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p8_dieBody2.png"))));
                map7.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p8_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p8_dieBody2.png")
                )));
                map7.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p8_dieBody.png"),
                        ImageController.getInstance().tryGet("/player/p8_dieBody2.png")
                )));
                return map7;
            default:
                return null;
        }
    }

    private static HashMap<State, Animator> getAliveAnimator(AlienType type) {
        switch (type) {
            case A:
                HashMap<State, Animator> map = new HashMap<>();
                map.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p3_walk04r.png"))));
                map.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p3_walk04.png"))));
                map.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p3_walk04r.png"),
                        ImageController.getInstance().tryGet("/player/p3_walk05r.png")
                )));
                map.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p3_walk04.png"),
                        ImageController.getInstance().tryGet("/player/p3_walk05.png")
                )));

                return map;
            case B:
                HashMap<State, Animator> map1 = new HashMap<>();
                map1.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p2_walk05r.png"))));
                map1.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p2_walk05.png"))));
                map1.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p2_walk06r.png"),
                        ImageController.getInstance().tryGet("/player/p2_walk05r.png")
                )));
                map1.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p2_walk06.png"),
                        ImageController.getInstance().tryGet("/player/p2_walk05.png")
                )));
                return map1;
            case C:
                HashMap<State, Animator> map2 = new HashMap<>();
                map2.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p1_walk04r.png"))));
                map2.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p1_walk04.png"))));
                map2.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p1_walk05r.png"),
                        ImageController.getInstance().tryGet("/player/p1_walk04r.png")
                )));
                map2.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p1_walk05.png"),
                        ImageController.getInstance().tryGet("/player/p1_walk04.png")
                )));

                return map2;
            case D:
                HashMap<State, Animator> map3 = new HashMap<>();
                map3.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p4_walk01r.png"))));
                map3.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p4_walk01.png"))));
                map3.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p4_walkr.png"),
                        ImageController.getInstance().tryGet("/player/p4_walk01r.png")
                )));
                map3.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p4_walk02.png"),
                        ImageController.getInstance().tryGet("/player/p4_walk01.png")
                )));

                return map3;
            case E:
                HashMap<State, Animator> map4 = new HashMap<>();
                map4.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p5_walk1r.png"))));
                map4.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p5_walk1.png"))));
                map4.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p5_walk2r.png"),
                        ImageController.getInstance().tryGet("/player/p5_walk1r.png")
                )));
                map4.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p5_walk2.png"),
                        ImageController.getInstance().tryGet("/player/p5_walk1.png")
                )));

                return map4;
            case F:
                HashMap<State, Animator> map5 = new HashMap<>();
                map5.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p6_walk1r.png"))));
                map5.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p6_walk1.png"))));
                map5.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p6_walk2r.png"),
                        ImageController.getInstance().tryGet("/player/p6_walk1r.png")
                )));
                map5.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p6_walk2.png"),
                        ImageController.getInstance().tryGet("/player/p6_walk1.png")
                )));

                return map5;
            case G:
                HashMap<State, Animator> map6 = new HashMap<>();
                map6.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p7_walk1r.png"))));
                map6.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p7_walk1.png"))));
                map6.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p7_walk2r.png"),
                        ImageController.getInstance().tryGet("/player/p7_walk1r.png")
                )));
                map6.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p7_walk2.png"),
                        ImageController.getInstance().tryGet("/player/p7_walk1.png")
                )));
                return map6;
            case H:
                HashMap<State, Animator> map7 = new HashMap<>();
                map7.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p8_walk1r.png"))));
                map7.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p8_walk1.png"))));
                map7.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p8_walk2r.png"),
                        ImageController.getInstance().tryGet("/player/p8_walk1r.png")
                )));
                map7.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/player/p8_walk2.png"),
                        ImageController.getInstance().tryGet("/player/p8_walk1.png")
                )));
                return map7;
            default:
                return null;
        }
    }
    // 如果要改善效能問題 存圖片路徑 載入時再導入圖片

   public enum AliveState {
        ALIVE,
       ZOMBIE,
       DEATH
   }

    public enum State {
        STAND_LEFT,
        STAND_RIGHT,
        WALK_LEFT,
        WALK_RIGHT;

        public boolean checkWalkConflict(State lastState) {
            if (lastState != WALK_LEFT && lastState != WALK_RIGHT) {
                return false;
            }
            return this == WALK_LEFT || this == WALK_RIGHT;
        }
    }

    public enum Role{
        WITCH,
        HUNTER
    }


    private HashMap<State, Animator> stateAnimator; //丟state會給animator
    private State currentState;// 角色動畫狀態
    private Global.Direction verticalDir;// 角色方向, enum包含四方向 這樣做有風險
    private Global.Direction horizontalDir;// 角色方向, enum包含四方向 這樣做有風險
    private AlienType alienType;
    private int ID;
    private int num;
    private boolean isTraitor;
    private int swordsNum;
    private HashSet<TaskController.Task> tasks;
    private Image sword;
    private Role role;
    private AliveState aliveState;
    private Delay killDelay;

    public Alien(int x, int y, int num) {
        super(x, y, 54, 73);
        currentState = State.STAND_RIGHT;
        switch (num) {
            case 1:
                this.alienType = AlienType.A;
                break;
            case 2:
                this.alienType = AlienType.B;
                break;
            case 3:
                this.alienType = AlienType.C;
                break;
            case 4:
                this.alienType = AlienType.D;
                break;
            case 5:
                this.alienType = AlienType.E;
                break;
            case 6:
                this.alienType = AlienType.F;
                break;
            case 7:
                this.alienType = AlienType.G;
                break;
            case 8:
                this.alienType = AlienType.H;
                break;
        }
        stateAnimator = getAliveAnimator(alienType);
        stateAnimator.get(currentState).play();
        horizontalDir = verticalDir = Global.Direction.NO_DIR;
        this.num = num;
        this.isTraitor = false;
        this.swordsNum = 0;
        this.tasks = new HashSet<>();
        this.sword = ImageController.getInstance().tryGet("/sword.png");
        this.role = Role.values()[1];
        this.aliveState = AliveState.ALIVE;
        this.killDelay = new Delay(300);
    }

    public void setRole() {
        this.role = Role.values()[0];;
    }

    public Role getRole() {
        return role;
    }

    public void setDone(TaskController.Task task){
        swordsNum++;
        tasks.add(task);
    }
//
    public boolean isDone(TaskController.Task task){
        return tasks.contains(task);
    }

    @Override
    public void paintComponent(Graphics g) {
        stateAnimator.get(currentState).paintComponent(g, painter().left(), painter().top(), 54, 73);
    }

    @Override
    public void update() {
        if (aliveState == AliveState.DEATH){
            return;
        }
            switch (horizontalDir) {
                case LEFT:
                    if (verticalDir == Global.Direction.DOWN) {
                        translateX(-(int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                        translateY((int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                    } else if (verticalDir == Global.Direction.UP) {
                        translateX(-(int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                        translateY(-(int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                    } else {
                        translateX(-Global.MOVE_SPEED);
                    }
                    break;
                case RIGHT:
                    if (verticalDir == Global.Direction.DOWN) {
                        translateX((int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                        translateY((int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                    } else if (verticalDir == Global.Direction.UP) {
                        translateX((int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                        translateY(-(int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                    } else {
                        translateX(Global.MOVE_SPEED);
                    }
                    break;
            }
            switch (verticalDir) {
                case UP:
                    if (horizontalDir == Global.Direction.RIGHT) {
                        translateX((int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                        translateY(-(int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                    } else if (horizontalDir == Global.Direction.LEFT) {
                        translateX(-(int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                        translateY(-(int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                    } else {
                        translateY(-Global.MOVE_SPEED);
                    }
                    break;
                case DOWN:
                    if (horizontalDir == Global.Direction.RIGHT) {
                        translateX((int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                        translateY((int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                    } else if (horizontalDir == Global.Direction.LEFT) {
                        translateX((int) (-Global.MOVE_SPEED / (Math.sqrt(2))));
                        translateY((int) (Global.MOVE_SPEED / (Math.sqrt(2))));
                    } else {
                        translateY(Global.MOVE_SPEED);
                    }
                    break;
            }
    }

    public void setVerticalDir(Global.Direction dir) {
        if (aliveState == AliveState.DEATH){
            return;
        }
        this.verticalDir = dir;
        setState();
    }

    public void setHorizontalDir(Global.Direction dir) {
        if (aliveState == AliveState.DEATH){
            return;
        }
        this.horizontalDir = dir;
        setState();
    }

    public Global.Direction getVerticalDir() {
        return verticalDir;
    }

    public Global.Direction getHorizontalDir() {
        return horizontalDir;
    }

    public boolean getNoDirection(){
        return verticalDir == Global.Direction.NO_DIR && horizontalDir == Global.Direction.NO_DIR;
    }

    public int getNum() {
        return num;
    }

    public State getCurrentState() {
        return currentState;
    }

    private void setState() {
        if (aliveState == AliveState.DEATH){
            return;
        }
        State lastState = currentState;
        if (horizontalDir != Global.Direction.NO_DIR) {
            switch (horizontalDir) {
                case LEFT:
                    currentState = State.WALK_LEFT;
                    break;
                case RIGHT:
                    currentState = State.WALK_RIGHT;
                    break;
            }
        } else {
            switch (lastState) {
                case WALK_LEFT:
                case STAND_LEFT:
                    if (verticalDir == Global.Direction.NO_DIR) {
                        currentState = State.STAND_LEFT;
                    } else {
                        currentState = State.WALK_LEFT;
                    }
                    break;
                case WALK_RIGHT:
                case STAND_RIGHT:
                    if (verticalDir == Global.Direction.NO_DIR) {
                        currentState = State.STAND_RIGHT;
                    } else {
                        currentState = State.WALK_RIGHT;
                    }
                    break;
            }
        }
        if (currentState != lastState) {
            if (!currentState.checkWalkConflict(lastState)) {
                stateAnimator.get(lastState).stop();
            }
            stateAnimator.get(currentState).play();
        }
    }

    public void kill(){
        aliveState = AliveState.ZOMBIE;
        currentState = State.STAND_RIGHT;
        stateAnimator = getDeathAnimator(alienType);
        stateAnimator.get(currentState).play();
    }

    public void death(){
        aliveState = AliveState.DEATH;
    }

    public boolean ableToKill(){
        killDelay.play();
        return killDelay.count();
    }

    public void setId(int id) {
        this.ID = id;
    }

    public int getId() {
        return this.ID;
    }

    public void setSwordNum(){
        swordsNum--;
    }
    public int getSwordsNum(){
        return swordsNum;
    }

    public Image getSword(){
        return sword;
    }

    public AliveState getAliveState() {
        return aliveState;
    }

    public void setAliveState(AliveState aliveState) {
        this.aliveState = aliveState;
        if(aliveState == AliveState.ZOMBIE){
            stateAnimator = getDeathAnimator(alienType);
            stateAnimator.get(currentState).play();
        }
    }
}