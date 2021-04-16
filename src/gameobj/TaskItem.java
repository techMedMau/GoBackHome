package gameobj;

import controllers.ImageController;
import controllers.TaskController;
import gameobj.button.Button;
import gameobj.button.Range;

import java.awt.*;

public class TaskItem extends Button implements Range {
    private Image imgS;
    private boolean state;
    private TaskController.Task task;

    public TaskItem(String path, int x, int y, TaskController.Task task) {
        super(x, y, 60, 70, ImageController.getInstance().tryGet(path));
        this.imgS = ImageController.getInstance().tryGet("/taskBox/shadow.png");
        this.state = false;
        this.task = task;
    }


    public boolean isTriggered(Alien alien){
        if(Math.sqrt(Math.abs((alien.painter().centerX() - this.painter().centerX())*(alien.painter().centerX() - this.painter().centerX())
                +(alien.painter().centerY() - this.painter().centerY())* (alien.painter().centerY() - this.painter().centerY()))) < 90.0){
            state = true;
        } else {
            state = false;
        }
        return state;
    }
    public boolean getState() {
        return state;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (state) {
            g.drawImage(imgS, painter().left(), painter().top(), null);
        }
    }

    @Override
    public void update() {
    }

    public TaskController.Task getTask() {
        return task;
    }
}
