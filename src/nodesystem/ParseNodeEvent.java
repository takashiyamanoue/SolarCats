package nodesystem;
import controlledparts.*;
public class ParseNodeEvent extends ParseEvent
{
    public CommunicationNode gui;

    public boolean parseEvent()
    {boolean rtn,btemp,btemp2,btemp3;
        if( parseButtonEvent(gui)     ||
            parseApfButton(gui)       
          )
            rtn= true;
        else rtn= false;
        return rtn;
    }

    public boolean parseApfButton(CommunicationNode gui)
    {
        parseB();
        if(!iq.rString("apf.")) return false;
        if(parseButtonEvent(gui.applicationsSelector)) return true;
        return false;
    }

    public ParseNodeEvent(CommunicationNode f, InputQueue q)
    {
        iq=q;
        gui=f;
        me=null;
    }

}