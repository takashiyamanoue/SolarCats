package nodesystem.eventrecorder;
import nodesystem.AMessage;
import controlledparts.*;

public class ParseLogEvent extends ParseEvent
{
    public boolean parseLogSendEnd(Logger l)
    {
        if(!iq.rString("end()")) return false;
        if(l!=null){
            if(l.fileManager!=null){
               l.showTheFile();
            }
        }
        return true;
    }

    public boolean parseLogSendStart(Logger l)
    {
        if(!iq.rString("start()")) return false;
        if(l!=null){
            if(l.fileManager!=null)
               l.fileManager.clearThis();
        }
        return true;
    }

    public boolean parseLogSendPut(Logger l)
    {
        if(!iq.rString("put(")) return false;
        StringBuffer x=this.iq.rStrConst();
        if(!iq.rString(",")) return false;
        Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(")")) return false;
        int i=c.intValue();
        if(l!=null){
            if(l.fileManager!=null){
            	AMessage m=new AMessage();
            	m.setHead(x.toString());
               l.fileManager.putMessageAt(m,i);
            }
        }
        
        return true;
    }

    public boolean parseDataTransfer(Logger l)
    {
        parseB();
        if(!iq.rString("send.")) return false;
        if(parseLogSendStart(l) ||
           parseLogSendPut(l)  ||
           parseLogSendEnd(l)
          )
         return true;
         else return false;
    }

    public boolean parseParts(Logger l)
    {
        boolean rtn;
        if( parseButtonEvent(l)     ||
            parseCheckBoxEvent(l)   ||
            parseTextEvent(l)       ||
            parseFileEvent(l.fileFrame) ||
            super.parseScrollBarEvent(l)||
//            super.parseFrameEvent(gui.recordFrame)||
//            super.parseFocus(gui.recordFrame) ||
            parseFileDialogEvent(l.fileFrame)||
            parseDataTransfer(l)
          )
             rtn= true;
        else rtn= false;
        return rtn;
    }

    public boolean parseMes(EventRecorder gui)
    {
        parseB();
        if(!iq.rString(gui.messageFrame.label+".")) return false;
        //
        if(parseParts(gui.messageFrame)) return true;
        return false;
    }

    public boolean parseLog(EventRecorder gui)
    {
        parseB();
        if(!iq.rString(gui.recordFrame.label+".")) return false;
        //
        if(parseParts(gui.recordFrame)) return true;
        return false;
     }

    public boolean parseEvent()
    {
        boolean rtn;
        if( parseLog(gui)     ||
            parseMes(gui)  
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

    public EventRecorder gui;

    public ParseLogEvent(EventRecorder f, InputQueue q)
    {
        iq=q;
        gui=f;
        me=null;
//        start();
    }

}