package nodesystem.applicationmanager;
import controlledparts.ControlledFrame;
import java.util.*;
import nodesystem.AMessage;

public class ApplicationID extends java.lang.Object
{
    public ApplicationID next;
    public String command;
    public ApplicationID(String name,String command, String mode, int ID,ControlledFrame obj)
    {
        controlMode=mode;
        this.name=name;
        this.command=command;
        this.procID=ID;
        this.application=obj;
    }
    public int procID;
    public String name;
    public ControlledFrame application;
    public String controlMode;
    public Vector<AMessage> getKeyFrame(){
    	return application.getKeyFrame();
    }
}

