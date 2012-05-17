package nodesystem.eventrecorder;
import java.util.Date;
public class Timer extends java.lang.Object
                   implements Runnable
{
    public static Timer getTimer()
    {
        return timer;
    }

    private static Timer timer=new Timer();

    long offsetTime;
    long markedTime;
    boolean running;

    public Date date;
    Thread me;
    int time;
    public int previousMilliTime=0;


    public void setMilliTime(int mtime)
    {
          long timeNow=System.currentTimeMillis();
          markedTime=timeNow-(long)mtime;
    }

    public int getMilliTime()
    {
 //       if(this.me==null)
 //       if(!this.running)
 //        return this.previousMilliTime;
        return (int)(System.currentTimeMillis()-markedTime);
    }

    public void resetMilliTimer()
    {
        this.markedTime= System.currentTimeMillis();
    }

    public long absoluteTime()
    {
        return System.currentTimeMillis();
    }

    public void setTime(int x)
    {
//          time=x;
//          System.out.println("markedTime="+markedTime+",x="+x+"\n");
//          long timeNow=(new Date()).getTime();
          long timeNow=System.currentTimeMillis();
          markedTime=timeNow-(long)(x*100);
//          System.out.println("timeNow="+timeNow+",markedTime="+markedTime+"\n");
    }
    public int getTime()
    {
//          int time=(int)(date.getTime())/100;
//          System.out.println("time="+time);
//          return time;
            return this.getMilliTime()/100;
    }
    public void stop()
    {
    	/*
        if(me!=null){
            me.stop();
            me=null;
        }*/
        this.running=false;
        this.previousMilliTime=this.getMilliTime();
    }
    public void start()
    {
    	/*
        if(me==null){
            me=new Thread(this, "Timer");
            me.start();
        }*/
        this.running=true;
        this.markedTime= System.currentTimeMillis()-(long)(this.previousMilliTime);
    }
    public void run()
    {/*
        while(me!=null){
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){}
//            Date d=new Date();
//            long timeSinceMarked=d.getTime()-markedTime;
            long timeSinceMarked=System.currentTimeMillis()-markedTime;
            time=(int)(timeSinceMarked/100);
        }
        */
    }
    private Timer()
    {
        date=new Date();
//        markedTime=date.getTime();
        markedTime=System.currentTimeMillis();
        time=0;
        me=null;
        start();
    }
}

