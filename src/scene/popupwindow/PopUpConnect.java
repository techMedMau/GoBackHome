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

    public PopUpConnect() {
        super(485, 276 );
    }

    @Override
    public void sceneBegin() {
        show();
        inputLine = new InputLine(290, 275, 35);
        img = ImageController.getInstance().tryGet("/enterPassword.png");
        cancelButton = new Button(280,340,186,73,ImageController.getInstance().tryGet("/cancel.png"));
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
        return null;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(this.img, (Global.WINDOW_WIDTH - getWidth()) / 2, (Global.WINDOW_HEIGHT - getHeight()) / 2, null);
        inputLine.paint(g);
        cancelButton.paint(g);
    }

    @Override
    public void update() {

    }
}
