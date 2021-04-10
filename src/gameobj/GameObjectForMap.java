package gameobj;

import controllers.ImageController;

import java.awt.*;

public class GameObjectForMap extends GameObject{
    private Image img;
    public GameObjectForMap(String path, int x, int y, int width, int height) {
        super(x, y, width, height );
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
