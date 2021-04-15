package scene.popupwindow;

import controllers.ImageController;
import utils.CommandSolver;

import java.awt.*;

public class PopUpPassword extends PopUpTask {
    private Image img;

    public PopUpPassword(){
        this.img = ImageController.getInstance().tryGet("/password/sheet.png");
        
    }

    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(img, 208,90,null);
    }


    @Override
    public void update(){

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {

        return (e, state, trigTime) -> {
            switch (state) {
                case CLICKED:
                    super.mouseListener().mouseTrig(e, state, trigTime);
            }
        };
    }
}
