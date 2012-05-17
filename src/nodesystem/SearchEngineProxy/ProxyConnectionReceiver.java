package nodesystem.SearchEngineProxy;

import java.io.IOException;
import java.net.ServerSocket;
import nodesystem.ConnectionReceiver;

public class ProxyConnectionReceiver extends ConnectionReceiver
{
	ProxyServer gui;
	String passWord;
    public void stop()
    {
        if(me!=null){
            me=null;
            try{
            	if(ss!=null)
                  this.ss.close();
            }
            catch(IOException e){}
        }
    }

    public void run()
    {
	    // listen loop
	    while(me!=null){
	        try{
	            
	            ns=ss.accept();
	            ClientConnector cc=new ClientConnector(ns,gui,passWord);
	            
//	            ChildProc2 cp=new ChildProc2(ss,gui,gMngr);
//	            childprocesses.addElement(cp);
	        }
	        catch(java.io.IOException e){}
	    }
    }

    public ProxyConnectionReceiver(ProxyServer g, int wellKnownPort, String pass) 
    {
        
        gui=g;

	    maxConnection=50;
	    

        try{
    	    if(ss!=null){  ss.close();  }
            ss=null;
    	    ss=new ServerSocket(wellKnownPort);
    	}
    	catch(IOException e){
    	    gui.receiveMessage("failed to start accepting at the port:"+wellKnownPort+"\n");
    	    return;
    	}
	    gui.receiveMessage("Starting listen at port:"+wellKnownPort+"\n");
	    this.passWord=pass;
//        childprocesses=new Vector();
        start();
    }
    
	/**
	 * @param args
	 */


}
