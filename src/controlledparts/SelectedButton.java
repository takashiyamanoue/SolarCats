package controlledparts;

import java.util.Vector;

import nodesystem.AMessage;

public interface SelectedButton
{
    int getID();

    void click();

    void unFocus();

    void focus();

    void controlledButton_mouseExited(java.awt.event.MouseEvent event);

    public void controlledButton_mouseEntered(java.awt.event.MouseEvent event);

    void setFrame(FrameWithControlledButton f);

    void setID(int i);

    Vector<AMessage> getKeyFrame(String commandName);

}