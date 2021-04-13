package gameobj;

import controllers.ImageController;

import java.awt.*;

public class BackgroundItem extends GameObject{
    private Image img;
    public BackgroundItem(String path, int x, int y, int width, int height,
                          int x2, int y2, int width2, int height2) {
        super(x, y, width, height, x2, y2, width2, height2);//先collider後paint
        this.img = ImageController.getInstance().tryGet(path);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}
