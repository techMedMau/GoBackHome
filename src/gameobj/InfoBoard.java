package gameobj;

import java.awt.*;

public class InfoBoard {
    public enum Item{
        SWORD(50,50),
        REPORT(50,50);

        private int height;
        private int width;

        Item(int height, int width){
            this.height = height;
            this.width = width;
        }
    }

    private int x;
    private int y;
    private int w;
    private int h;

    public InfoBoard(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void paintAlien(Graphics g, Alien aliens){

    }



}
