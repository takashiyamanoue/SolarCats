package application.texteditor;
import nodesystem.*;
import controlledparts.*;

public class ParseTextEditEvent extends ParseEvent
{
    public ParseTextEditEvent()
    {
    }

    public boolean parseEvent()
    {
        if(
           this.parseDTextEditEvent(gui)
        )
        return true;
        /* ||
		   parseFileEvent(gui.fileFrame)             ||
		   parseFocus(gui)    ||
		   parseFrameEvent(gui)
		   ) return true; */
		else
        return false;
    }

    public TextEditFrame gui;

    public ParseTextEditEvent(TextEditFrame f, InputQueue q)
    {
        iq=q;
        gui=f;
        me=null;
    }

}