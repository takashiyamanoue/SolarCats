package nodesystem.SearchEngineProxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import nodesystem.AMessage;
import nodesystem.MessageReceiver;
import nodesystem.StringIO;

import util.TraceSW;

public class ClientConnector implements Runnable
{

    public Vector cqueue;
    TraceSW tracing;
    Thread me;
    MessageReceiver gui;
    Socket sock;
    public StringIO sio;
    public String command;
    public InetAddress ria;
    String passWord;


    public void start()
    {
        if(this.me==null){
            me=new Thread(this,"ClientConnector");
            me.start();
        }
    }

    public void stop()
    {
        if(me!=null){
            me=null;
        }
    }

    public void run()
    {
    	String c="";
        while(me!=null){
            try{
            	AMessage m=this.sio.readString();
                c=m.getHead();
            }
            catch(IOException e){
                this.close();
                this.stop();
                me=null;
                return;
            }
            this.processTheRequest(c);
        }
    }


    public void close()
    {
        try{Thread.sleep(100);} catch(InterruptedException e){}
        if(this.sio!=null){
          try{
               sio.in.close();
               sio.out.close();
          }
          catch(IOException e){
          }
        }
        if(this.sock!=null){
            try{
                sock.close();
            }
            catch(IOException e){
            }
        }
        this.stop();
    }


    public synchronized void processTheRequest(String command)
    {
//    	System.out.println(command);
    	gui.receiveMessage("processing the '"+command+"'...");
        if(command==null) {
            gui.receiveMessage("i/o stream closing.\n");
            this.close();
            gui.receiveMessage("end of closing.\n");
            return;
        }        
        if(command.indexOf("get-"+passWord+" ")==0){
            ProxySearchEngineDriver driver=new ProxySearchEngineDriver(sio,gui);
            String url=command.substring(("get-"+passWord+" ").length());
            driver.loadURL(url);
            this.close();
        	return;
        	
        }
        else{
        	this.close();
        }

    }
    
    public ClientConnector( Socket ns, ProxyServer g, String pass)
    {
        /*
        
        */
        gui=g;   
        sock=ns;
        ria=sock.getInetAddress();
        cqueue=new Vector();
        int rport=sock.getPort();
        gui.receiveMessage("Connected from "+ria.toString()+":"+rport+"\n");
        sio=new StringIO(sock,tracing);
        this.passWord=pass;
//        sio.setTracing(true);
        this.start();
	}
    public void comment()
    {
        /*
        1. receive requests from CommunicationNodes and manage the group of
           the nodes. Nodes of the group are connected to form a tree structure.
        
        2. receive requests from GroupManagers and connect the groups of the nodes.
        
        Each request is the one of the followings
        
           - addnode
           - delnode
           - gettime
           - 
        
        
        1. receive the node information
        2. construct the tree (system structure) infomation
        3. send the connection infomation to the node.
        */
    }
	//{{DECLARE_CONTROLS
	//}}

    /**
	 * @param args
	 */
}
	

