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
            case A -> {
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
            }
            case B -> {
                HashMap<State, Animator> map = new HashMap<>();
                map.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/p2_walk05r.png"))));
                map.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/p2_walk05.png"))));
                map.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/p2_walk06r.png"),
                        ImageController.getInstance().tryGet("/p2_walk05r.png")
                )));
                map.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/p2_walk06.png"),
                        ImageController.getInstance().tryGet("/p2_walk05.png")
                )));
                return map;
            }
            case C -> {
                HashMap<State, Animator> map = new HashMap<>();
                map.put(State.STAND_LEFT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/p1_walk04r.png"))));
                map.put(State.STAND_RIGHT, new Animator(1, Arrays.asList(
                        ImageController.getInstance().tryGet("/p1_walk04.png"))));
                map.put(State.WALK_LEFT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/p1_walk05r.png"),
                        ImageController.getInstance().tryGet("/p1_walk04r.png")
                )));
                map.put(State.WALK_RIGHT, new Animator(3, Arrays.asList(
                        ImageController.getInstance().tryGet("/p1_walk05.png"),
                        ImageController.getInstance().tryGet("/p1_walk04.png")
                )));
                return map;
            }
            default -> {
                return null;
            }
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
    private int num;

    public Alien(int x, int y, int width, int height, AlienType alienType) {
        super(x, y, width, height);
        currentState = State.STAND_RIGHT;
        stateAnimator = getAnimator(alienType);
        stateAnimator.get(currentState).play();
        horizontalDir = verticalDir = Global.Direction.NO_DIR;
        this.num = (int)(Math.random()*3+1);
    }

    @Override
    public void paintComponent(Graphics g) {
        stateAnimator.get(currentState).paintComponent(g, painter().left(), painter().top(), 36, 49);
    }

    @Override
    public void update() {
        if(this.touchLeft()){
            this.translateX(1);
            return;
        }
        if(this.touchTop()){
            this.translateY(1);
        }
        if(this.touchBottom()){
            this.translateY(-1);
        }
        if(this.touchRight()){
            this.translateX(-1);
        }
//        待解決
        switch (horizontalDir) {
            case LEFT -> translateX(-1);
            case RIGHT -> translateX(1);
        }
        switch (verticalDir) {
            case UP -> translateY(-1);
            case DOWN -> translateY(1);
        }
    }

    public void setVerticalDir(Global.Direction dir) {
        this.verticalDir = dir;
        setState();
    }

    public void setHorizontalDir(Global.Direction dir) {
        this.horizontalDir = dir;
        setState();
    }

    private void setState() {
        State lastState = currentState;
        if (horizontalDir != Global.Direction.NO_DIR) {
            switch (horizontalDir) {
                case LEFT -> currentState = State.WALK_LEFT;
                case RIGHT -> currentState = State.WALK_RIGHT;
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
}