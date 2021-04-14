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
    private Nums traitorNums;
    private Nums playNums;
    private Button cancelButton;
    private Button confirmButton;
    public PopUpCreateRoom() {
        super(486,486);
    }

    @Override
    public void sceneBegin() {
        img= ImageController.getInstance().tryGet("/popupcreat.png");
        show();
        traitorNums=new Nums(2,380,110);
        playNums=new Nums(8,280,230);
        cancelButton = new Button(280,300,186,73,ImageController.getInstance().tryGet("/button/cancel.png"));
        confirmButton=new Button(480,300,186,73,ImageController.getInstance().tryGet("/button/confirm.png"));
    }

    @Override
    public void sceneEnd() {
        disShow();

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
                            if (!Global.WAIT_SCENES.containsKey(str)){
                                break;
                            }
                        }
                        if (traitorNums.getTarget()<=playNums.getTarget()&&traitorNums.getTarget()!=0){
                            sceneEnd();
                            WaitingScene waitingScene=new WaitingScene(str,traitorNums.getTarget(),playNums.getTarget(),ClientClass.getInstance().getID());
                            Global.WAIT_SCENES.put(str,waitingScene);
                            ArrayList<String> strCreat=new ArrayList<>();
                            strCreat.add(str);
                            strCreat.add(String.valueOf(traitorNums.getTarget()));
                            strCreat.add(String.valueOf(playNums.getTarget()));
                            strCreat.add(String.valueOf(ClientClass.getInstance().getID()));
                            ClientClass.getInstance().sent(Global.InternetCommand.CREAT,strCreat);
                            SceneController.getInstance().changeScene(waitingScene);
                        }
                        break;
                    }
                    traitorNums.show(e.getPoint());
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
        traitorNums.paint(g);
        playNums.paint(g);
    }

    @Override
    public void update() {

    }
}
