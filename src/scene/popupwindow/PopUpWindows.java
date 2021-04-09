package scene.popupwindow;

import scene.Scene;

public abstract class PopUpWindows extends Scene {
    private int width;
    private int height;
    private boolean show;
    public PopUpWindows(int width,int height){
        this.width=width;
        this.height=height;
        show=false;
    }
    public int getWidth(){
        return width;
    }

    public int getHeight() {
        return height;
    }
    public void show(){
        show=true;
    }
    public void disShow(){
        show=false;
    }
    public boolean isShow(){
        return show;
    }
}
