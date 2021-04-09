package scene.popupwindow;

import controllers.ImageController;
import gameobj.InputLine;
import gameobj.button.Button;
import utils.CommandSolver;
import utils.Global;

import java.awt.*;

public class PopUpConnect extends PopUpWindows {
    private Image img;
    private InputLine inputLine;
    private Button cancelButton;
    private Button confirmButton;
    private String num;

    public PopUpConnect() {
        super(485, 276 );
    }

    @Override
    public void sceneBegin() {
        show();
        inputLine = new InputLine(290, 275, 35);
        img = ImageController.getInstance().tryGet("/enterPassword.png");
        cancelButton = new Button(280,340,186,73,ImageController.getInstance().tryGet("/cancel.png"));
        confirmButton=new Button(480,340,186,73,ImageController.getInstance().tryGet("/confirm.png"));
        num=new String();
    }

    @Override
    public void sceneEnd() {
        disShow();
        inputLine = null;
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
                        if (num.length()==0){return;}
                        num=num.substring(0,num.length()-1);
                        break;
                }

            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {

            }

            @Override
            public void keyTyped(char c, long trigTime) {
                num+=c;
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(this.img, (Global.WINDOW_WIDTH - getWidth()) / 2, (Global.WINDOW_HEIGHT - getHeight()) / 2, null);
        inputLine.paint(g);
        cancelButton.paint(g);
        confirmButton.paint(g);
        g.drawString(num,290,280);
    }

    @Override
    public void update() {
    }
}
