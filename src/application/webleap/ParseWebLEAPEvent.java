package application.webleap;
import controlledparts.*;

public class ParseWebLEAPEvent extends ParseEvent
{
    public ParseWebLEAPEvent(WebLEAPFrame f, InputQueue q)
    {
        iq=q;
        gui=f;
        me=null;
    }

    public WebLEAPFrame gui;
    public boolean parseEvent()
    {
        boolean rtn,btemp,btemp2,btemp3;
        
        if( parseButtonEvent(gui)     ||
            parseTextEvent(gui)       ||
            super.parseScrollBarEvent(gui)||
            super.parseFocus(gui)     ||
            super.parseFrameEvent(gui)
          )
            rtn= true;
        else rtn= false;
        return rtn;
    }

   public void run()
    {
        /*
         parseB();
         int x=1;
         while(!iq.eos()){
             boolean iter=true;
 //          while(iter){
              iter=parseEvent();
              if(!iter){
                gui.recordMessage("error: event parse error");
                if(!errorRecovery()) return;
              }
 //         }
         }
         */
         super.run();
    }

}