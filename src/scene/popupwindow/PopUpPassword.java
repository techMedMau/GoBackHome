package scene.popupwindow;

import controllers.ImageController;
import utils.CommandSolver;
import gameobj.button.Button;

import java.awt.*;
import java.util.ArrayList;

public class PopUpPassword extends PopUpTask {
    private Image img;
    private ArrayList<Button> buttons;

    public PopUpPassword(){
        this.img = ImageController.getInstance().tryGet("/password/sheet.png");
        buttons = new ArrayList<>();
        buttons.add(new Button(350,100, 32,32
                ,ImageController.getInstance().tryGet("/password/0.png")));
        buttons.add(new Button(350,100, 32,32
                ,ImageController.getInstance().tryGet("/password/1.png")));
        buttons.add(new Button(350,100, 32,32
                ,ImageController.getInstance().tryGet("/password/2.png")));
        buttons.add(new Button(350,100, 32,32
                ,ImageController.getInstance().tryGet("/password/3.png")));
        buttons.add(new Button(350,100, 32,32
                ,ImageController.getInstance().tryGet("/password/4.png")));
        buttons.add(new Button(350,100, 32,32
                ,ImageController.getInstance().tryGet("/password/5.png")));
        buttons.add(new Button(350,100, 32,32
                ,ImageController.getInstance().tryGet("/password/6.png")));
        buttons.add(new Button(350,100, 32,32
                ,ImageController.getInstance().tryGet("/password/7.png")));
        buttons.add(new Button(350,100, 32,32
                ,ImageController.getInstance().tryGet("/password/8.png")));
        buttons.add(new Button(350,100, 32,32
                ,ImageController.getInstance().tryGet("/password/9.png")));
        
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
