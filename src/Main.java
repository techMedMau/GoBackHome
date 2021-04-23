import controllers.SceneController;
import scene.OpenScene;

import utils.CommandSolver;
import utils.GameKernel;
import utils.Global;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {
        JFrame jframe = new JFrame();
        SceneController sceneController=SceneController.getInstance(); //取得單例模式的控場實體
        sceneController.changeScene(new OpenScene()); //一開始使用開場畫面
        GameKernel gameKernel = new GameKernel.Builder().input(  //創建遊戲核心
                new CommandSolver.BuildStream().mouseTrack().subscribe(sceneController).keyboardTrack()
                        .add(KeyEvent.VK_LEFT,Global.Direction.LEFT.getValue())//設置左箭頭為2
                        .add(KeyEvent.VK_RIGHT,Global.Direction.RIGHT.getValue()) //設置右箭頭為3
                        .add(KeyEvent.VK_UP,Global.Direction.UP.getValue())
                        .add(KeyEvent.VK_DOWN,Global.Direction.DOWN.getValue())
                        .add(KeyEvent.VK_SHIFT, Global.KeyCommand.SHIFT.getValue())
                        .add(KeyEvent.VK_BACK_SPACE,Global.KeyCommand.BACK_SPACE.getValue())
                        .add(KeyEvent.VK_CAPS_LOCK,Global.KeyCommand.CAPS_LOCK.getValue())
                        .next().keyCleanMode().trackChar().subscribe(sceneController)
        ).paint(sceneController).update(sceneController).gen();
        jframe.setSize(Global.WINDOW_WIDTH,Global.WINDOW_HEIGHT+20);
        jframe.setTitle("Return Home");
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //設置關閉時結束程式
        jframe.add(gameKernel);
        jframe.setVisible(true);
        gameKernel.run();
    }
}
