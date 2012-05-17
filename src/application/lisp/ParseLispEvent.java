package application.lisp;

import controlledparts.*;

public class ParseLispEvent extends ParseEvent
{
    public boolean parseBexButton(LispFrame gui)
    {
        parseB();
        if(!iq.rString("bex.")) return false;
        if(parseButtonEvent(gui.lispExampleListFrame)) return true;
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

   private LispFrame gui;
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
    public ParseLispEvent(LispFrame f, InputQueue q)
    {
        iq=q;
        gui=f;
        me=null;
//        start();
    }
}

