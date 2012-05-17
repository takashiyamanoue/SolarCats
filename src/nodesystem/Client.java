package nodesystem;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import util.TraceSW;
public class Client extends java.lang.Object implements  Runnable
{
	TraceSW tracing;
    public boolean isConnected()
    {
        if(this.me==null) return false;
        else return true;
    }

    public boolean isNextDoorControl()
    {
       /*
          next door communication command

          nextDoor <command> <argment>

        */
        //
    	/*
        int pos=commandbuffer.indexOf("nextDoor ");
        if(pos!=0) return false;
        String rtn=gui.nextDoorControl(commandbuffer,this);
        return true;
        */
    	String h=this.commandbuffer.getHead();
    	if(h.startsWith("nextDoor ")){
    		String rtn=gui.nextDoorControl(h,this);
    		return true;
    	}
    	else{
    		return false;
    	}
    }

    public StringIO sio;

    public boolean getMessage()
    {
        if(this.sio==null) return false;
        this.commandbuffer=null;
        try{
          this.commandbuffer=sio.readString();
          if(this.isNodeControl()||this.isNextDoorControl())
              return false; 
          else
          if(this.commandbuffer==null){
        	  StringIO up=this.distributor.getIOAt(0);
        	  StringIO left=this.distributor.getIOAt(1);
        	  StringIO right=this.distributor.getIOAt(2);
        	  if(up==sio){
        		  this.distributor.setOutputStream(0,null);
        		  return false;
        	  }
        	  if(left==sio){
        		  this.distributor.setOutputStream(1, null);
        		  return false;
        	  }
        	  if(right==sio){
        		  this.distributor.setOutputStream(2, null);
        		  return false;
        	  }
        	  return false;
          }
          else
          {
             if(this.distributor==null) return false;
             this.distributor.putS(this.commandbuffer,this.myID);
             return true;
          }
          
        }
        catch(IOException e){
            stop();
            return false;
        }
    }

    public byte[] outBuffer;

    public MessageQueue receiveEventQueue;


    public byte dmy[];
    public boolean isNodeControl()
    {
        /*
          node control command

          nodeControl redirect <new up node> <new up port>

        */
        //
    	/*
        int pos=commandbuffer.indexOf("nodeControl ");
        if(pos!=0) return false;
        String rtn=gui.nodeControl(commandbuffer);
        this.stop();
        return true;
       */
    	String h=this.commandbuffer.getHead();
    	if(h.startsWith("nodeControl ")){
    		String rtn=gui.nodeControl(h);
    		this.stop();
    		return true;
    	}
    	else
    		return false;
    }
    public void disconnect()
    {
        stop();
    }
    public CommandTranceiver commandTranceiver;
    public int myID;
    public Distributor distributor;
    public Client()
    {
        init();
    }
    public int maxbuff;
    public byte[] outBuff;
    public byte[] inBuff;
    public void init()
    {
        maxbuff=256;
        inBuff=new byte[maxbuff];
        outBuff=new byte[maxbuff];
        me=null;
        sio=null;
        dmy=new byte[4];
    }
    public synchronized void putS(String s)
    {
             distributor.putS(s,myID);

 }
//    public String commandbuffer;
    public AMessage commandbuffer;
    public DataInputStream dinstream;
    public void connect(String serverAddress,
                        int    wellKnownPort)
    {
        sio=new StringIO(serverAddress, wellKnownPort,this.tracing);
        if(distributor==null) myID=0;
        else{
//          myID=distributor.addOutputStream(sio);
        	myID=0;
        	distributor.setOutputStream(myID, sio);
        }
        start();
        gui.appendMessage("connected to "+serverAddress+":"+wellKnownPort+
        		 ", streamChannel="+myID+"\n");
    }
    public int serverPort;
    public String serverAddress;
    public void stop()
    {
        if(me!=null){
            me=null;
            /*
            if(sock!=null){
               InetAddress ria=sock.getInetAddress();
               int rport=sock.getPort();
               String discmessage="disconnecting from "+ria.toString()+":"+rport+"\n";
               gui.appendMessage(discmessage);
            }
            */
//            putS(discmessage);
            if(distributor!=null)
                distributor.remove(myID);

//            me.stop();
            close();
        }

   }
    public void start()
    {
        if(me==null){
            me=new Thread(this,"Client");
            me.start();
//            System.out.println("Client thread priority:"+me.getPriority());
        }
    }
    public Thread me;
    public void run()
    {
        while(me!=null){
            if(getMessage()){
                if(!commandbuffer.equals(""))
                    if(commandTranceiver!=null)
                    commandTranceiver.parseCommands(commandbuffer);
//                     this.receiveEventQueue.put(commandbuffer);
            }            
            try{
                Thread.sleep(5);
            }
            catch(InterruptedException e){System.out.println(e);}            
       }

    }
    public void close()
    {
        try{
           sio.close();
        }
        catch(IOException e){}
    }
    //public OutputStream out;
    public DataOutputStream out;   // public InputStream in;
   public DataInputStream in;    public Socket sock;
    public MessageGui gui;
    public Client(MessageGui cc,Distributor distributor,CommandTranceiver cr,
                  MessageQueue q, TraceSW tr)
    {
        gui=cc;
        this.distributor=distributor;
        commandTranceiver=cr;
        this.receiveEventQueue=q;
        this.tracing=tracing;
        init();

   }
}

