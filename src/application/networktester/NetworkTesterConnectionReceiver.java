package application.networktester;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class NetworkTesterConnectionReceiver implements java.lang.Runnable 
{
    public NetworkTesterConnectionReceiver()
    {
    }
    Vector childprocesses;
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
          | childproc                 |
          |     |           |                      |
          +-----|-----------|----------------------+
                |           |



        */
    }
    Socket ns;
    int wellKnownPort;
    ServerSocket ss;
    int serverPort;
    String serverAddress;
    public void stop()
    {
        try{
          if(ss!=null) ss.close();  
//          if(in!=null) this.in.close();
//          if(out!=null) this.out.close();
        }
        catch(Exception e){
            gui.messages.append(""+e+"..ServerSocket Closed\n");
        }
        try{
          for(int i=0;i<childprocesses.size();i++){
	            NetworkTesterChildProc cp=
	              (NetworkTesterChildProc)(childprocesses.elementAt(i));
	            cp.close();
	            cp.stop();
          }
          
        }
        catch(Exception e){
            gui.messages.append(""+e+"..stream closed\n");
        }
        if(me!=null){
//            me.stop();
            me=null;
        }
//        for(int i=0;i<childprocesses.size();i++){
//            ChildProc cp= (ChildProc)(childprocesses.elementAt(i));
//            if(cp!=null)  cp.stop();
//        }
    }
    public void start()
    {
        if(me==null){
            me=new Thread(this);
            me.start();
        }
    }
    Thread me;
    public void run()
    {
	    // listen loop
	    while(me!=null){
	        try{
	            if(ss==null){
	                this.gui.appendMessage("socket is not created in the connectionReceiver\n");
	                me=null;
	                return;
	            }
	            ns=ss.accept();
	            NetworkTesterChildProc cp=new NetworkTesterChildProc(ns,gui);
	            childprocesses.addElement(cp);
	        }
	        catch(IOException e){System.out.println(e);}
	    }
    }
    OutputStream out;
    InputStream in;
    Socket sock;
    NetworkTesterWorkerFrame gui;
    public NetworkTesterConnectionReceiver(NetworkTesterWorkerFrame cc,
                              int wellKnownPort)
    {
        gui=cc;

	    //Start listening connections

//	    maxConnection=50;
//	    distributor=d;

//        wellKnownPort=gui.wellKnownPort;
        ss=null;

        try{
    	    ss=new ServerSocket(wellKnownPort);
    	}
    	catch(IOException e){
    	    System.out.println(e);
    	    gui.messages.append("failed to create the serverSocket\n");
    	    return;
    	}
	    gui.appendMessage("Starting listen at port:"+wellKnownPort+"\n");

        childprocesses=new Vector();

        start();
    }
}