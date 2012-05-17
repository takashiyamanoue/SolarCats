package nodesystem.binaryTreeMaker;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketOptions;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import nodesystem.CommunicationNode;
import nodesystem.InterNodeController;
public class InitialRequestClient 
implements Runnable
{
	private int ECHO_PORT = 10007;
	private static final int PACKET_SIZE = 1024;
	private static final String MCAST_ADDRESS = "224.0.1.1";
	private int requestTerm = 200;
	private int waitAfterConnection = 500;
	Thread me;
	public static void main(String args[]) {
		InitialRequestClient client=new InitialRequestClient(null,"g1",null,0, 200, 500, 10007);
	}
	public InitialRequestClient(InterNodeController c,String groupName, 
			String mcastAddr, int mcastPort, int rt, int wt, int ackPort){
		this.controller=c;
		this.requestTerm = rt;
		this.waitAfterConnection = wt;
		this.ECHO_PORT=ackPort;
		System.out.println("start initialRequestClient");
		System.out.println("groupName="+groupName+",mcastAddr="+mcastAddr
				+",mcastPort="+mcastPort+",ackRecPort="+ackPort);
		System.out.println("requestTerm="+rt+",waitAfterConnection="+wt);

		try {
			InetAddress mcastAddress=null;
			if(mcastAddr==null)
		       mcastAddress = InetAddress.getByName(MCAST_ADDRESS);
			else
			   mcastAddress = InetAddress.getByName(mcastAddr);
			/*
			if(mcastPort!=0)
				ECHO_PORT=mcastPort;
				*/
			String message="request join "+groupName;
			request(message,mcastAddress,mcastPort);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	public void request(String message,InetAddress mcastAddress, int mcastPort){
		try{
    		mcastSocket = new MulticastSocket();
    		mcastSocket.setInterface(InetAddress.getLocalHost());
		}
		catch(IOException e){
			System.out.println(e.toString());
		}
	    while(!this.isConnectedToUpperNode()){
		  int randomNumber=(int)(Math.random()*4096.0);
		  String xmessage=message+" "+randomNumber;
		  try{
		    byte[] bytes = xmessage.getBytes();
		    DatagramPacket packet =
				new DatagramPacket(bytes, bytes.length, mcastAddress, mcastPort);
			startReceive();
			System.out.println("send "+xmessage+" to "+mcastAddress+":"+mcastPort);
		    mcastSocket.send(packet);
		  }
		  catch(Exception e){
			  System.out.println(e.toString());
		  }finally {
		  }
		  try{
			Thread.sleep(requestTerm);  
		  }
		  catch(InterruptedException e){
			  
		  }
  	    }
		if (mcastSocket != null) {
			mcastSocket.close();
		}
 
	}
	InterNodeController controller;
	public void setInterNodeControllerNode(InterNodeController c){
		controller=c;
	}
	public void startReceive(){
		this.start();
	}
	boolean thisIsConnected=false;
	public boolean isConnectedToUpperNode(){
        if(this.controller==null) {
        	return thisIsConnected;
        }
		return this.controller.isConnectedToUpperNode();
	}
	public boolean isRunning(){
		return this.me!=null;
	}
    ServerSocket serverSocket = null;
    Socket socket = null;

	public void start(){
		if(me==null){
			me=new Thread(this,"InitialRequestClient-receive-thread");
			me.start();
		}
	}
	public void stop(){
		try{
			Thread.sleep(this.waitAfterConnection);
		}
		catch(InterruptedException e){
			
		}
		me=null;
		try{
    		serverSocket.close();
		}
		catch(Exception e){}
	}
	MulticastSocket mcastSocket = null;
	public void run(){
	  try{
	    serverSocket = new ServerSocket(ECHO_PORT);
	    System.out.println("EchoServerÇ™ãNìÆÇµÇ‹ÇµÇΩ(port=" + serverSocket.getLocalPort() + ")");
	    socket = serverSocket.accept();
	    System.out.println("ê⁄ë±Ç≥ÇÍÇ‹ÇµÇΩ "
	                  + socket.getRemoteSocketAddress() );
	    new Thread(new Runnable(){
	    	public void run(){
	           connectToTheNode(socket);
	    	}
	    }).start();
		while(me!=null){
			   socket = serverSocket.accept();
			      System.out.println("ê⁄ë±Ç≥ÇÍÇ‹ÇµÇΩ "
			                  + socket.getRemoteSocketAddress() );
			      new Thread(new Runnable(){
			    	  public void run(){
			              discardTheConnection(socket);
			    	  }
			      }).start();
		}
	  }
	  catch(Exception e){
		System.out.println(e.toString());
	  }
	}
	public void connectToTheNode(Socket s){
	      String line="";
		  try{
	        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        line = in.readLine();
	        DataOutputStream out = new DataOutputStream(s.getOutputStream());
	        out.writeInt(1);
	        in.close();
	        out.close();
		  }
		  catch(IOException e){
			System.out.println(""+e);
		  }
	      System.out.println("éÛêM: " + line);
	      StringTokenizer st=new StringTokenizer(line);
	      String rtn="";
	      if(st.hasMoreTokens()){
	    	  rtn=st.nextToken(); // must be "return"
	      }
	      String ack="";
	      if(st.hasMoreTokens()){
	    	  ack=st.nextToken(); // must be "ack"
	      }
	      String cmd="";
	      if(st.hasMoreTokens()){
	    	  cmd=st.nextToken(); // must be "connect"
	      }
	      String lr="";
	      if(st.hasMoreTokens()){
	    	  lr=st.nextToken(); // must be "left" or "right"
	      }
	      String addr="";
	      if(st.hasMoreTokens()){
	    	  addr=st.nextToken(); // must be "<address>"
	      }
	      String p="";
	      if(st.hasMoreTokens()){
	    	  p=st.nextToken(); // must be "<port>"
	      }
	      int pn=(new Integer(p)).intValue();
	      if(this.controller!=null){
	    	  this.controller.connectToUpperNode(addr, p);
	      }
	      else{
	    	  this.thisIsConnected=true;
	      }	     	
	}
	public void discardTheConnection(Socket s){
		try{
		      BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		      String line;
		      line = in.readLine();
		      System.out.println("discard éÛêM: " + line);
		      DataOutputStream out = new DataOutputStream(s.getOutputStream());
		      out.writeInt(0);
		      in.close();
		      s.close();
		}
		catch(IOException e){
				System.out.println(e.toString());
		}	
	}

}
