package gameobj;

import controllers.ImageController;
import utils.Animator;
import utils.Global;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class Alien extends GameObject {
    public enum AlienType{
        A,
        B,
        C;
    }

    private static HashMap<State, Animator> getAnimator(AlienType type){
        switch (type){
            case A :
                HashMap<State, Animator> map = new HashMap<>();
                map.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/p3_walk04r.png"))));
                map.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/p3_walk04.png"))));
                map.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/p3_walk04r.png"),
                        ImageController.getInstance().tryGet("/p3_walk05r.png")
                )));
                map.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/p3_walk04.png"),
                        ImageController.getInstance().tryGet("/p3_walk05.png")
                )));
                return map;
            case B :
                HashMap<State, Animator> map1 = new HashMap<>();
                map1.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/p2_walk05r.png"))));
                map1.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/p2_walk05.png"))));
                map1.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/p2_walk06r.png"),
                        ImageController.getInstance().tryGet("/p2_walk05r.png")
                )));
                map1.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/p2_walk06.png"),
                        ImageController.getInstance().tryGet("/p2_walk05.png")
                )));
                return map1;
            case C :
                HashMap<State, Animator> map2 = new HashMap<>();
                map2.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/p1_walk04r.png"))));
                map2.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/p1_walk04.png"))));
                map2.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/p1_walk05r.png"),
                        ImageController.getInstance().tryGet("/p1_walk04r.png")
                )));
                map2.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/p1_walk05.png"),
                        ImageController.getInstance().tryGet("/p1_walk04.png")
                )));
                return map2;
            default :
                return null;
        }
    }
    // 如果要改善效能問題 存圖片路徑 載入時再導入圖片

    public enum State {
        STAND_LEFT,
        STAND_RIGHT,
        WALK_LEFT,
        WALK_RIGHT;

        public boolean checkWalkConflict(State lastState){
            if(lastState != WALK_LEFT && lastState != WALK_RIGHT){
                return false;
            }
            return this == WALK_LEFT || this == WALK_RIGHT;
        }
    }

    private HashMap<State, Animator> stateAnimator; //丟state會給animator
    private State currentState;// 角色動畫狀態
    private Global.Direction verticalDir;// 角色方向, enum包含四方向 這樣做有風險
    private Global.Direction horizontalDir;// 角色方向, enum包含四方向 這樣做有風險
    private AlienType alienType;
    private int ID;
    private Global.Direction tempDir;
    private int num;

    public Alien(int x, int y, int num) {
        super(x, y, 54, 73);
        currentState = State.STAND_RIGHT;
        switch(num){
            case 1:
                this.alienType = AlienType.A;
                break;
            case 2:
                this.alienType = AlienType.B;
                break;
            case 3:
                this.alienType = AlienType.C;
                break;
        }
        stateAnimator = getAnimator(alienType);
        stateAnimator.get(currentState).play();
        horizontalDir = verticalDir = Global.Direction.NO_DIR;
        this.num = num;
    }

    @Override
    public void paintComponent(Graphics g) {
        stateAnimator.get(currentState).paintComponent(g, painter().left(), painter().top(), 54, 73);
    }

    @Override
    public void update() {
        switch (horizontalDir) {
            case LEFT :
                translateX(-2);
                break;
            case RIGHT :
                translateX(2);
                break;
        }
        switch (verticalDir) {
            case UP :
                translateY(-2);
                break;
            case DOWN :
                translateY(2);
                break;
        }
    }

    public void setVerticalDir(Global.Direction dir) {
        this.verticalDir = dir;
        setState();
        tempDir = dir;
    }

    public void setHorizontalDir(Global.Direction dir) {
        this.horizontalDir = dir;
        setState();
        tempDir = dir;
    }

    public Global.Direction getDir(){
        return tempDir;
    }

    public int getNum() {
        return num;
    }

    public State getCurrentState() {
        return currentState;
    }

    private void setState() {
        State lastState = currentState;
        if (horizontalDir != Global.Direction.NO_DIR) {
            switch (horizontalDir) {
                case LEFT :
                    currentState = State.WALK_LEFT;
                    break;
                case RIGHT :
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
        if(currentState != lastState){
            if(!currentState.checkWalkConflict(lastState)){
                stateAnimator.get(lastState).stop();
            }
            stateAnimator.get(currentState).play();
        }
    }
    public void setId(int id){
        this.ID=id;
    }
    public int getId(){
        return this.ID;
    }
}