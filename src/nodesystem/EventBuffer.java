package nodesystem;

import nodesystem.applicationmanager.*;

public class EventBuffer 
// implements java.lang.Runnable
{
    public int lastTime;

    public void eventFlowLimitter(int l)
    {
        // l: length of the event for put().
        //
        int maxRate=80000; // 80000 bytes/sec.
        
        this.eventLength+=l;
        if(this.eventLength<maxRate/40) return;
        
        int t1=this.lastTime;
        int t2=this.tranceiver.gui.eventRecorder.timer.getTime(); // unit:0.1sec

        int dt=t2-t1;
        int wt=eventLength*1000/maxRate; // 
        if(dt==0){
            try{
                Thread.sleep(wt);
            }
            catch(InterruptedException e){}
        }
        else
        if(wt>dt*100){
            try{
                Thread.sleep(wt-dt*100);
            }
            catch(InterruptedException e){}
        }
        this.lastTime=this.tranceiver.gui.eventRecorder.timer.getTime(); 
        this.eventLength=0;
        return;
    }

    public int eventLength;

    private boolean lockValueForBuffer;

    public void unlockBuff()
    {
        lockValueForBuffer=false;
    }

    public synchronized void lockBuff()
    {
        while(lockValueForBuffer){
            try{Thread.sleep(20);}
            catch(InterruptedException e){}
        }
        lockValueForBuffer=true;
        
    }

//    public synchronized void putS(String sending, int priority)
    public synchronized void putS(AMessage sending, int priority)
    {
        /*
        if(priority>1){
                tranceiver.sendCommand(sending);
 //             ebuff.putS(s);
        }
        else 
        */
        putS(sending);
    }
    public EventBuffer()
    {
    }
    public boolean flushFlag;
    public synchronized void sendAll()
    {

          flushFlag=true;

          Applications applis=tranceiver.gui.applicationManager;
          applis.sendAll();
          flushFlag=false;
    }
//    public ComBrowFrame gui;
    public Thread me;
    public String buff;
    /*
    public synchronized void putS(String command)
    {
//        this.eventFlowLimitter(command.length());
        tranceiver.sendCommand(command);
    }
    */
    public synchronized void putS(String c){
    	AMessage m=new AMessage();
    	m.setHead(c);
    	this.putS(c);
    }
    public synchronized void putS(AMessage command)
    {
//        this.eventFlowLimitter(command.length());
        tranceiver.sendCommand(command);
    }
    
    public EventBuffer(CommandTranceiver ct)
    {
        tranceiver=ct;
        buff="";
//        gui=cbf;
        flushFlag=false;
        lockValueForBuffer=false;
//        start();
        this.eventLength=0;
    }
    public CommandTranceiver tranceiver;
    /*
    public void run()
    {
        int c=0;
        int flushCount=50;
        int i;
        i=flushCount;
        while(me!=null){
//            if(c==-1){stop(); return;}
//            text.appendText(""+(char)c);
            try{ me.sleep(100);}
            catch(InterruptedException e) {}
            if(!buff.equals(""))
                 {
                    lockBuff();
                    // ***
                    // distributor.putS(buff);
//                    tranceiver.sendCommand(buff);
                    buff="";
                    unlockBuff();
                  }
            if(i<=0){
//                sendAll();
                i=flushCount;
            }
            i--;
        }
    }
    */
    public void stop()
    {
        if(me!=null){
//            me.stop();
            me=null;
        }
    }

    public void start()
    {
        /*
        if(me==null){
            me= new Thread(this,"EventBuffer");
            me.start();
        }
        */
    }
}

