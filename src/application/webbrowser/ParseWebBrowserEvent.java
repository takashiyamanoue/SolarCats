package application.webbrowser;

import controlledparts.*;

public class ParseWebBrowserEvent extends ParseEvent
{
    public boolean parseCodeButton(WebFrame f)
    {
        parseB();
        if(!iq.rString("code.")) return false;
        if(parseButtonEvent(f.languageCodeFrame)) return true;
        return false;
    }


    public boolean parseEvent()
    {   boolean rtn,btemp,btemp2,btemp3;
        if( parseButtonEvent(gui)     ||
            parseTextEvent(gui)       ||
            parseEditPaneEvent(gui)   ||
            parseFileEvent(gui.fileFrame) ||
            super.parseScrollBarEvent(gui)||
            super.parseFocus(gui)     ||
            super.parseFrameEvent(gui)||
            parseCodeButton(gui)
          )
            rtn= true;
        else rtn= false;
        return rtn;
    }

    public WebFrame gui;
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
    public ParseWebBrowserEvent(WebFrame f, InputQueue q)
    {
        iq=q;
        gui=f;
        me=null;
//        start();
    }
}