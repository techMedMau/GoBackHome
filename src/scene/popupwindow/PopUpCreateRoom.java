package scene.popupwindow;

import controllers.ImageController;
import controllers.SceneController;
import gameobj.Nums;
import gameobj.button.Button;
import internet.server.ClientClass;
import scene.WaitingScene;
import utils.CommandSolver;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;

public class PopUpCreateRoom extends PopUpWindows{
    private Image img;
    private Nums playNums;
    private Button cancelButton;
    private Button confirmButton;
    public PopUpCreateRoom() {
        super(486,486);
    }

    @Override
    public void sceneBegin() {
        img= ImageController.getInstance().tryGet("/openScene/popupcreate.png");
        show();
        //顯示數字的地方
        playNums=new Nums(6,225,210);
        cancelButton = new Button(415,300,186,73,ImageController.getInstance().tryGet("/button/cancel.png"));
        confirmButton=new Button(615,300,186,73,ImageController.getInstance().tryGet("/button/confirm.png"));
    }

    @Override
    public void sceneEnd() {
        disShow();
        img=null;
        playNums=null;
        cancelButton=null;
        confirmButton=null;

    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (state == null) {
                return;
            }
            switch (state) {
                case CLICKED:
                    if (cancelButton.state(e.getPoint())){
                        sceneEnd();
                        break;
                    }
                    if (confirmButton.state(e.getPoint())){
                        String str;
                        while (true){
                            str=new String();
                            for (int i=0;i<5;i++){
                                str+=Global.random(0,9);
                            }
                            //不用懂
                            if (!Global.WAIT_SCENES.containsKey(str)){
                                break;
                            }
                        }

                        if (playNums.getTarget() != 0){
                            WaitingScene waitingScene=new WaitingScene(str,playNums.getTarget(),ClientClass.getInstance().getID());
                            Global.WAIT_SCENES.put(str,waitingScene);
                            ArrayList<String> strCreat=new ArrayList<>();
                            strCreat.add(str);
                            strCreat.add(String.valueOf(playNums.getTarget()));
                            strCreat.add(String.valueOf(ClientClass.getInstance().getID()));
                            ClientClass.getInstance().sent(Global.InternetCommand.CREATE,strCreat);
                            SceneController.getInstance().changeScene(waitingScene);
                            sceneEnd();
                            return;
                        }
                        break;
                    }

                    playNums.show(e.getPoint());
                    break;
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,(Global.WINDOW_WIDTH - getWidth()) / 2, (Global.WINDOW_HEIGHT - getHeight()) / 2, null);
        cancelButton.paint(g);
        confirmButton.paint(g);
        playNums.paint(g);
    }

    @Override
    public void update() {

    }
}
