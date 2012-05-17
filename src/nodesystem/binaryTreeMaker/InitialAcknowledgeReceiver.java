package nodesystem.binaryTreeMaker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import nodesystem.ChildProc;
import nodesystem.CommandTranceiver;
import nodesystem.Distributor;
import nodesystem.MessageGui;

import util.TraceSW;

public class InitialAcknowledgeReceiver 
implements Runnable
{
	public static final int PACKET_SIZE = 1024;

    public InitialAcknowledgeReceiver()
    {
    }
    public CommandTranceiver commandTranceiver;
    public Vector childprocesses;
    public void comment()
    {
        /*


          +--------|-------------------------------+
          |                                        |
          |      client                            |
          |                 distributor            |
          |                                        |
          |                      connectionReceiver|
          |                                        |
          | childproc   childproc ...              |
          |     |           |                      |
          +-----|-----------|----------------------+
                |           |



        */
    }
    public Socket ns;
    public Distributor distributor;
    public int wellKnownPort;
    public int maxConnection;
    public ServerSocket ss;
    public int serverPort;
    public String serverAddress;
    TraceSW trace;
    public void stop()
    {
        if(me!=null){
//            me.stop();
            me=null;
        }
        if(this.childprocesses==null) return;
        for(int i=0;i<childprocesses.size();i++){
            ChildProc cp= (ChildProc)(childprocesses.elementAt(i));
            if(cp!=null)  cp.stop();
        }
        try{
          if(ss!=null) ss.close();
        }
        catch(java.io.IOException e){
            System.out.println("exception:"+e+" occured while closing server socket.");
        }
    }
    public void start()
    {
        if(me==null){
            me=new Thread(this,"ConnectionReceiver");
            me.start();
//            System.out.println("ConnectionReceiver thread priority:"+me.getPriority());
        }
    }
    public Thread me;
    byte receiveBuffer[];
    public void run()
    {
	    // listen loop
	    while(me!=null){
	        try{
	            if(ss==null){
	                this.gui.appendMessage("socket is not created in the connectionReceiver\n");
	                stop();
	                return;
	            }
	            ns=ss.accept();
	            /*
	            ns.setSoTimeout(0);
	            ChildProc cp=new ChildProc(ns,gui,distributor,
	                                       commandTranceiver,this.trace);
	            childprocesses.addElement(cp);
	            */
	            InputStream is=ns.getInputStream();
	            is.read();
	        }
	        catch(IOException e){System.out.println(e);}
	    }
    }
    public OutputStream out;
    public InputStream in;
    public Socket sock;
    public MessageGui gui;
    public InitialAcknowledgeReceiver(MessageGui cc,
                              Distributor d,
                              int wellKnownPort,
                              CommandTranceiver cr, TraceSW tr)
    {
        gui=cc;

	    maxConnection=50;
	    distributor=d;
	    commandTranceiver=cr;

        try{
    	    if(ss!=null)
    	    	ss.close();
            ss=null;
    	    ss=new ServerSocket(wellKnownPort);
    	    ss.setReuseAddress(true);
    	    ss.setSoTimeout(0);
    	}
    	catch(IOException e){
    	    System.out.println(e);
    	    gui.appendMessage("failed to create the serverSocket\n");
    	    return;
    	}
	    gui.appendMessage("Starting listen at port:"+wellKnownPort+"\n");
        childprocesses=new Vector();
        this.trace=tr;

        start();
    }
    public boolean isTracing(){
    	return this.isTracing();
    }

}
