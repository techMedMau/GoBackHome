package gameobj.button;

import java.awt.*;

public interface ClickState {
    boolean state(Point point);
    boolean state(int x,int y);

}
