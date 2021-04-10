package gameobj;

import controllers.ImageController;

import java.awt.*;

public class tile extends GameObject{
    private Image img;
    public tile(int x, int y) {
        super(x, y, 64, 64);
        this.img = ImageController.getInstance().tryGet("/tile_12.png");
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}
