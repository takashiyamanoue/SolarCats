package controlledparts;

import application.draw.DrawFrame;

public interface FrameWithLanguageProcessor {
    DrawFrame getDrawFrame();
    ControlledFrame lookUp(String name);
    void resetStopFlag();
    boolean stopFlagIsOn();
    boolean traceFlagIsOn();

}
