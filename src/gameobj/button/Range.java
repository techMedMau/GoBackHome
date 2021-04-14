package gameobj.button;

import gameobj.Alien;

public interface Range {
    boolean getState();
    void isTriggered(Alien alien);
}
