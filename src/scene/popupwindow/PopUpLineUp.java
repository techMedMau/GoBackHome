package scene.popupwindow;

import controllers.ImageController;
import gameobj.button.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PopUpLineUp extends PopUpTask {
    private Map<Button, Button> lineUp;
    private Button gold ;
    private Button diamond;
    private Button iron;
    private Button ruby;


    public PopUpLineUp(){

        lineUp = new HashMap<>();
        gold = new Button(300,100,48,48,ImageController.getInstance().tryGet("/lineUp/gold.png"));
        diamond = new Button(300,200,48,48,ImageController.getInstance().tryGet("/lineUp/diamond.png"));
        iron = new Button(300,300,48,48,ImageController.getInstance().tryGet("/lineUp/iron.png"));
        ruby =
        lineUp.put(gold
                ,new Button(600,300,48,48,ImageController.getInstance().tryGet("/lineUp/gold.png")));
        lineUp.put(diamond
                ,new Button(600,100,48,48,ImageController.getInstance().tryGet("/lineUp/diamond.png")));
        lineUp.put(iron
                ,new Button(600,400,48,48,ImageController.getInstance().tryGet("/lineUp/iron.png")));
        lineUp.put(new Button(300,400,48,48,ImageController.getInstance().tryGet("/lineUp/ruby.png"))
                ,new Button(600,200,48,48,ImageController.getInstance().tryGet("/lineUp/ruby.png")));
    }



    @Override
    public void paint(Graphics g){
        super.paint(g);
//        lineUp.get(button1);
    }

    @Override
    public void update(){

    }



}
