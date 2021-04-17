import controllers.SceneController;
import gameobj.Alien;
import scene.GameScene;
import scene.OpenScene;
import scene.VoteScene;
import scene.WaitingScene;
import utils.CommandSolver;
import utils.GameKernel;
import utils.Global;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
//        ArrayList<Alien> aliens = new ArrayList<>();
//        aliens.add(new Alien(500,500,1));
        JFrame jframe = new JFrame();
        SceneController sceneController=SceneController.getInstance(); //取得單例模式的控場實體
        sceneController.changeScene(new OpenScene()); //一開始使用開場畫面
        GameKernel gameKernel = new GameKernel.Builder().input(  //創建遊戲核心
                new CommandSolver.BuildStream().mouseTrack().subscribe(sceneController).keyboardTrack()
                        .add(KeyEvent.VK_ENTER, 0) //設置ENTER按鍵為 -1
                        .add(KeyEvent.VK_LEFT,Global.Direction.LEFT.getValue())//設置左箭頭為2
                        .add(KeyEvent.VK_RIGHT,Global.Direction.RIGHT.getValue()) //設置右箭頭為3
                        .add(KeyEvent.VK_UP,Global.Direction.UP.getValue())
                        .add(KeyEvent.VK_DOWN,Global.Direction.DOWN.getValue())
                        .add(KeyEvent.VK_X,4)
                        .add(KeyEvent.VK_SPACE,5)
                        .add(KeyEvent.VK_A,6)
                        .add(KeyEvent.VK_BACK_SPACE,7)
                        .next().keyCleanMode().trackChar().subscribe(sceneController)
        ).paint(sceneController).update(sceneController).gen();
        jframe.setSize(Global.WINDOW_WIDTH,Global.WINDOW_HEIGHT);
        jframe.setTitle("Return Home");
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //設置關閉時結束程式
        jframe.add(gameKernel);
        jframe.setVisible(true);
        gameKernel.run();
    }
}
