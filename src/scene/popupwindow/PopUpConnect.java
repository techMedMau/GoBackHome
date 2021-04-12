package scene.popupwindow;

import controllers.ImageController;
import gameobj.Input;
import gameobj.InputLine;
import gameobj.button.Button;
import internet.server.ClientClass;
import internet.server.Server;
import utils.CommandSolver;
import utils.Global;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PopUpConnect extends PopUpWindows {
    private Image img;
    private Button cancelButton;
    private Button confirmButton;
    private Input input;


    public PopUpConnect() {
        super(485, 276 );
    }

    @Override
    public void sceneBegin() {
        show();
        img = ImageController.getInstance().tryGet("/enterPassword.png");
        cancelButton = new Button(280,340,186,73,ImageController.getInstance().tryGet("/cancel.png"));
        confirmButton=new Button(480,340,186,73,ImageController.getInstance().tryGet("/confirm.png"));
        input=new Input(290,275,37,35,19);
    }

    @Override
    public void sceneEnd() {
        disShow();
        input = null;
        img = null;
        cancelButton = null;

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (state == null) {
                return;
            }
            switch (state) {
                case CLICKED:
                    if (cancelButton.state(e.getPoint())) {
                        sceneEnd();
                    }
                    if (confirmButton.state(e.getPoint())){
                        ArrayList<String> str=new ArrayList<>();


                    }
                    break;
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                switch (commandCode){
                    case 7:
                        if (input.length()==0){return;}
                        input.substring();
                        break;
                }

            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {

            }

            @Override
            public void keyTyped(char c, long trigTime) {
                input.add(c);
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(this.img, (Global.WINDOW_WIDTH - getWidth()) / 2, (Global.WINDOW_HEIGHT - getHeight()) / 2, null);
        cancelButton.paint(g);
        confirmButton.paint(g);
        input.paint(g);

    }

    @Override
    public void update() {
    }
}
