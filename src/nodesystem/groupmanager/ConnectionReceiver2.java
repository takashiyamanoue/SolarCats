package nodesystem.groupmanager;
import java.io.IOException;
import java.net.ServerSocket;

import nodesystem.ConnectionReceiver;
import nodesystem.MessageGui;

public class ConnectionReceiver2 extends ConnectionReceiver
{
	GroupManager gui;
    public void stop()
    {
        if(me!=null){
            me=null;
            this.managersProcessQueue.closeAll();
            try{
              this.ss.close();
            }
            catch(IOException e){}
        }
    }

    public ManagersProcessQueue managersProcessQueue;


    public TreeManager gMngr;

    public void run()
    {
	    // listen loop
	    while(me!=null){
	        try{
	            
	            ns=ss.accept();
	            ManagersCommandProcessor cp=new ManagersCommandProcessor(ns,gui,gMngr,managersProcessQueue,this.gui);
	            
//	            ChildProc2 cp=new ChildProc2(ss,gui,gMngr);
//	            childprocesses.addElement(cp);
	        }
	        catch(java.io.IOException e){
	        	gui.appendMessage(""+e.toString());
	        }
	    }
    }

    public ConnectionReceiver2(GroupManager cc,
                              int wellKnownPort, TreeManager tm) 
    {
        
        gui=cc;
        gMngr=tm;

	    maxConnection=50;
	    

        try{
    	    if(ss!=null){  ss.close();  }
            ss=null;
    	    ss=new ServerSocket(wellKnownPort);
    	}
    	catch(IOException e){
    	    gui.appendMessage("failed to start accepting at the port:"+wellKnownPort+"\n");
    	    return;
    	}
	    gui.appendMessage("Starting listen at port:"+wellKnownPort+"\n");
//        childprocesses=new Vector();
        managersProcessQueue=new ManagersProcessQueue();

        start();
    }
}

