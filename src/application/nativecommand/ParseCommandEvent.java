package application.nativecommand;
import controlledparts.*;

public class ParseCommandEvent extends ParseEvent
{
    public boolean parseEvent()
    {   boolean rtn,btemp,btemp2,btemp3;
    /*
        */
        if( parseButtonEvent(gui)     ||
            parseTextEvent(gui)       ||
            super.parseScrollBarEvent(gui)||
            super.parseFrameEvent(gui)||
            super.parseFocus(gui) 
          )
            rtn= true;
        else rtn= false;
        /*
        */
//        gui.communicationNode.setControlMode(previousControlMode);
        return rtn;
    }

   private NativeCommandShell gui;
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
    public ParseCommandEvent(NativeCommandShell f, InputQueue q)
    {
        iq=q;
        gui=f;
        me=null;
//        start();
    }
}

