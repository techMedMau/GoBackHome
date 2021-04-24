package scene;

import utils.CommandSolver;
import utils.GameKernel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//場景抽象類-->
  //介面的實作推遲給子類場景實現
  //並增加兩個抽象方法給子類實現-->場景初始化和場景結束(釋放資源用)
public abstract class Scene implements GameKernel.PaintInterface,GameKernel.UpdateInterface{
    public abstract void sceneBegin();
    public abstract void sceneEnd();
    //因為滑鼠和鍵盤每個場景依據自己的需求才會用到，所以做成抽象方法給子場景決定是否要監聽滑鼠和鍵盤；
    //假如子類不要，就空方法體即可
    public abstract CommandSolver.MouseListener mouseListener();
    public abstract CommandSolver.KeyListener keyListener();
    public static class WindowClose extends WindowAdapter {
        private static Close closeAction;

        @Override
        public void windowClosing(WindowEvent e) {
            if (closeAction!=null){
                closeAction.CloseAction();
            }
        }

        public static void setCloseAction(Close closeAction){
            WindowClose.closeAction=closeAction;
        }

        public interface Close{
            void CloseAction();
        }
    }
}
