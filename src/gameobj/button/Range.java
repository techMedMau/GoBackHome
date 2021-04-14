package gameobj.button;

import gameobj.Alien;

public interface Range {
    boolean getState();
    boolean isTriggered(Alien alien);
}
