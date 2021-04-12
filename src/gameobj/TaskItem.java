package gameobj;

import controllers.ImageController;

import java.awt.*;

public class TaskItem extends GameObject{
    private Image img;
    private Image imgS;
    private boolean state;

    public TaskItem(String path, int x, int y) {
        super(x, y, 64, 64);
        this.img = ImageController.getInstance().tryGet(path);
        this.imgS = ImageController.getInstance().tryGet("/shadow.png");
        state = false;
    }

    public TaskItem isTriggered(Alien alien){
        if(Math.sqrt(Math.abs((alien.painter().centerX() - this.painter().centerX())*(alien.painter().centerX() - this.painter().centerX())
                +(alien.painter().centerY() - this.painter().centerY())* (alien.painter().centerY() - this.painter().centerY()))) < 150.0){
            state = true;
        }else{state = false;}

        return this;
    }


    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
        if(state) {
            g.drawImage(imgS, painter().left(), painter().top(), null);
        }
    }

    @Override
    public void update() {
    }
}
