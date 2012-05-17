package nodesystem.eventrecorder;
import controlledparts.*;
public class ParseEventRecorderEvent extends ParseEvent
{
    public boolean parseEvent()
    {   boolean rtn,btemp,btemp2,btemp3;
         btemp2=gui.sendEventFlag;
         gui.sendEventFlag=false;
         if( this.parseButtonEvent(gui)     ||
             this.parseTextEvent(gui)       ||
             super.parseCheckBoxEvent(gui)
          )
            rtn= true;
        else rtn= false;
        gui.sendEventFlag=btemp2;
        return rtn;
    }

    public EventRecorder gui;
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
                if(!errorRecovery()) return;
              }
 //         }
         }
         */
         super.run();
    }
    public ParseEventRecorderEvent(EventRecorder f, InputQueue q)
    {
        iq=q;
        gui=f;
        me=null;
//        start();
    }
}