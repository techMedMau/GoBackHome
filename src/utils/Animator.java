package utils;

import java.awt.*;
import java.util.List;

public class Animator {
    private int count;
    private Delay delay;
    private List<Image> imgs;

    public Animator(int delayCount, List<Image> imgs) {
        this.imgs = imgs;
        delay = new Delay(delayCount);
    }

    public void play() {
        delay.loop();
    }

    public void pause() {
        delay.pause();
    }

    public void stop() {
        delay.stop();
    }
    public Delay getDelay(){
        return delay;
    }
    public List<Image> getImgs(){
        return this.imgs;
    }

    public void paintComponent(Graphics g, int left, int top, int right, int bottom) {
        if (delay.count()) {
            count = ++count % imgs.size();
        }
        g.drawImage(imgs.get(count), left, top, right, bottom, null);
    }
}
