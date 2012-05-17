package nodesystem.groupmanager;
import java.util.Vector;

public class ManagersProcessQueue extends java.lang.Object implements java.lang.Runnable
{
    public void closeAll()
    {
        while(requests.size()>0){
            ManagersCommandProcessor p=(ManagersCommandProcessor)(requests.elementAt(0));
            p.close();
            p.stop();
            requests.remove(0);
        }
    }

    public int getSize(){
    	return this.requests.size();
    }
    public void processARequest()
    {
        if(requests.size()<=0){
            try{
                Thread.sleep(50);
            }
            catch(InterruptedException e){}
            return;
        }
        ManagersCommandProcessor p=null;
        synchronized(requests){
           p=(ManagersCommandProcessor)(requests.elementAt(0));
        }
//        p.gui.appendMessage(p.command+"\n");
        p.processTheRequest();
//        p.treeManager.showCurrentConnection(p.gui);
        if(requests.size()>0)
        synchronized(requests){
            requests.remove(0);
        }
        /*
        if(headPointer>=requests.size()) {
            try{
                Thread.sleep(50);
            }
            catch(InterruptedException e){}
            return;
        }
        ChildProc2 p=(ChildProc2)(requests.elementAt(headPointer));
        p.processTheRequest();
        headPointer++;
        */
    }

    public synchronized void addRequest(ManagersCommandProcessor p)
    {
        requests.addElement(p);
    }

    public int headPointer;

    public Vector requests;

    public void stop()
    {
        if(me!=null){
//            me.stop();
            me=null;
        }
    }

    public void start()
    {
        if(me==null){
            me=new Thread(this,"ManagerProcessQueue");
            me.start();
        }
    }

    public Thread me;

	public ManagersProcessQueue()
	{
        requests=new Vector();
        headPointer=0;
        start();
	}


    public void run()
    {
        // This method is derived from interface java.lang.Runnable
        // to do: code goes here
        while(me!=null){
            try{
                Thread.sleep(200);
            }
            catch(InterruptedException e){}
            processARequest();
        }
    }

}