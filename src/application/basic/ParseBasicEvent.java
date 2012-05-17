package application.basic;

import controlledparts.*;

public class ParseBasicEvent extends ParseEvent
{
    public boolean parseBexButton(BasicFrame gui)
    {
        parseB();
        if(!iq.rString("bex.")) return false;
        if(parseButtonEvent(gui.basicExampleListFrame)) return true;
        return false;
    }

    public boolean parseEvent()
    {   boolean rtn,btemp,btemp2,btemp3;
    /*
        */
        if( parseButtonEvent(gui)     ||
            parseTextEvent(gui)       ||
            parseBexButton(gui)       ||
            parseFileEvent(gui.fileFrame) ||
            super.parseScrollBarEvent(gui)||
            super.parseFrameEvent(gui)||
            super.parseFocus(gui) ||
            super.parseCheckBoxEvent(gui)
          )
            rtn= true;
        else rtn= false;
        /*
        */
//        gui.communicationNode.setControlMode(previousControlMode);
        return rtn;
    }

   private BasicFrame gui;
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
    public ParseBasicEvent(BasicFrame f, InputQueue q)
    {
        iq=q;
        gui=f;
        me=null;
//        start();
    }
}

