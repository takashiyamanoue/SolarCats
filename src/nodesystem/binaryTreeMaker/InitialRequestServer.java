package nodesystem.binaryTreeMaker;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import nodesystem.InterNodeController;
public class InitialRequestServer 
implements Runnable
{
	 int ECHO_PORT = 10007;
	 public static final int PACKET_SIZE = 1024;
	 Thread me;
	 int height;
	 int waitTime;
	 int idleTime=1000;
	 String groupName;
	 String mcastAddr;
	 int mcastPort;
	 String nodeAddr;
	 int nodePort;
	 int nodeID;
	 boolean parallelFlag;
	 boolean leftIsOcupied,rightIsOcupied;
	 public void start(){
		 if(me==null){
		   me=new Thread(this,"InitialRequestServer");
		   me.start();
		 }
	 }
	 public void stop(){
		 me=null;
		 
	 }

	 public static void main(String args[]) {
		 InitialRequestServer s=new InitialRequestServer(100,null,"g1",null,2020, 3030, 200, 200,10007,0,true);
		 s.start();
	}
	public InitialRequestServer(int h,InterNodeController c,String name,String addr,int p,
		int nport, int wt, int it, int ackPort, int id, boolean pf
	){
		this.height=h;
		this.waitTime=h*wt;
		this.groupName=name;
		this.mcastAddr=addr;
		this.mcastPort=p;
		this.nodePort=nport;
		this.controller=c;
		this.idleTime=it;
		this.ECHO_PORT=ackPort;
		this.nodeID=id;
		this.parallelFlag=pf;
		System.out.println("start InitialRequestServer");
		System.out.println("height="+h+",waitTime="+h*wt);
		System.out.println("groupName="+name+",mcastAddr="+addr
				+",mcastPort="+p+"ackRecPort="+ackPort);
		System.out.println("nodePort="+nport+",idleTime="+it);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		 MulticastSocket mcastSocket=null;
		 InetAddress mcastAddress=null;
		 InetAddress myAddress=null;
		 try{
		     myAddress=InetAddress.getLocalHost();
		 }
		 catch(Exception e){
			 System.out.println(""+e);
		 }
		 String maddr=myAddress.getHostAddress();
		 StringTokenizer st=new StringTokenizer(maddr,"/");
		 String dmy="";
		 String localAddr="";
		 if(st.hasMoreTokens())
		    dmy=st.nextToken();
		 if(st.hasMoreTokens())
		    localAddr=st.nextToken();
		 if(localAddr.equals(""))
		  	 localAddr=dmy;
		 byte[] buf = new byte[PACKET_SIZE];
		 DatagramPacket packet = new DatagramPacket(buf, buf.length);
		 while (me!=null) {
			 int ack=0;
			 if(!this.isAllOccupied()){
		    	 if(mcastSocket==null){
	    			 try{
    					 mcastSocket = new MulticastSocket(mcastPort);
			             mcastSocket.setInterface(myAddress);
					     mcastAddress = InetAddress.getByName(mcastAddr);
				         System.out.println("MulticastReceiverÇãNìÆÇµÇ‹ÇµÇΩ("
				    		 + "address=" + mcastAddress
			    			 + ",port=" + mcastSocket.getLocalPort() + ")");
	    			 }
    				 catch(Exception e){
    					 System.out.println("error, InitialRequestServer, run, new MulticastSocket");
					     System.out.println(""+e.toString());
				     }				 
			     }
			 }
			 if(this.isLeftEmpty()){
				 try {
					 System.out.println("requestServer, waiting at left side.");
//				     InetAddress remoteAddress=mcastSocket.getInetAddress();
	    		     mcastSocket.joinGroup(mcastAddress);
				     mcastSocket.receive(packet);			 
				     mcastSocket.leaveGroup(mcastAddress);
				     String message = new String(buf, 0, packet.getLength());
				     System.out.println(packet.getSocketAddress()
						 + " : " + message);
				     if(message.startsWith("request join "+groupName)){
				    	 if(this.parallelFlag){
				    	    String snum=message.substring(("request join "+groupName+" ").length());
				    	    int num=0;
				    	    try{
    				    	    num=(new Integer(snum)).intValue();
				    	    }
				    	    catch(Exception e){
				    		    num=0; 
				    	    }
				    	    if(!isBinaryMatchFromTail(height,nodeID,num)){
				    		    continue;
				    	    }
				    	 }
					     try{
						     Thread.sleep(waitTime);
					     }
					     catch(InterruptedException e){	 }

				         InetAddress addr = packet.getAddress();
				         if(!this.controller.isFixing())
				             ack=returnAck(addr,"return ack connect left "+localAddr+" "+nodePort);
					     if(ack==1){
	    				     for(int i=0;i<1000;i++){
		    			    	 if(this.isLeftEmpty()){
			    		    		 try{
				    	    			 Thread.sleep(100);
					        		 }
					        		 catch(InterruptedException e){}
					    	     }
					    	     else{
					    		     break;
					    	     }
					         }
					     }
				     }
				  } catch (IOException e) {
 					 System.out.println("error, InitialRequestServer, run, left");
					  e.printStackTrace();
				  } 
			 }
			 else
			 if(this.isRightEmpty()){
				 try {
					 System.out.println("requestServer, waiting at right side.");
	    		     mcastSocket.joinGroup(mcastAddress);
				     mcastSocket.receive(packet);
				     mcastSocket.leaveGroup(mcastAddress);
//				     InetAddress remoteAddress=mcastSocket.getInetAddress();
				     String message = new String(buf, 0, packet.getLength());
				     System.out.println(packet.getSocketAddress()
						 + " : " + message);
				     if(message.startsWith("request join "+groupName)){
				    	 if(this.parallelFlag){
				    	    String snum=message.substring(("request join "+groupName+" ").length());
				    	    int num=0;
				    	    try{
    				    	     num=(new Integer(snum)).intValue();
				    	    }
				    	    catch(Exception e){
				    		    num=0; 
				    	    }
				    	    if(!isBinaryMatchFromTail(height,nodeID,num)){
				    		    continue;
				    	    }
				    	 }
					     try{
						     Thread.sleep(waitTime);
					     }
					     catch(InterruptedException e){	 }
				         InetAddress addr = packet.getAddress();
				         if(!this.controller.isFixing())
				             ack=returnAck(addr,"return ack connect right "+localAddr+" "+nodePort);
						 if(ack==1){
						        for(int i=0;i<1000;i++){
						    	   if(this.isRightEmpty()){
						    		   try{
						    			   Thread.sleep(100);
						    		   }
						    		   catch(InterruptedException e){}
						    	    }
						    	    else{
						    		    break;
						    	    }
						        }
						 }
				     }
				  } catch (IOException e) {
				      System.out.println("error, InitialRequestServer, run, right");
					  e.printStackTrace();
				  } 
			 }
			 else
			 if(this.isAllOccupied()){
				 try{
				     Thread.sleep(idleTime);
				 }
				 catch(InterruptedException e){	 }					
				 if(mcastSocket!=null){
				   try{
//				     mcastSocket.leaveGroup(mcastAddress);
				     mcastSocket.close();
				     mcastSocket=null;
				   }
				   catch(Exception e){	 
					   System.out.println("error, InitialRequestServer, run, all ocupied");
					   System.out.println(""+e.toString());
				   }
				 }
			 }
		  }
		
	}
	boolean isBinaryMatchFromTail(int h, int x, int y){
		System.out.println("isBinaryMatchFromTail, h="+h+",x="+x+",y="+y);
		if(h<=1) return true;
		h=h-1;
		for(int i=0;i<h;i++){
			int xi1=x & 0x00000001;
			int yi1=y & 0x00000001;
			if(xi1!=yi1)
				return false;
			x=x>>1;
		    y=y>>1;
		}
		return true;
	}
	boolean leftIsEmpty=true;
	boolean rightIsEmpty=true;
	public boolean isLeftEmpty(){
		boolean rtn;
		if(controller==null){
			rtn= this.leftIsEmpty;
		}
		rtn= !this.controller.isLeftConnected();
//		System.out.println(" isLeftEmpty()=="+rtn);
		return rtn;
	}
	public boolean isRightEmpty(){
		boolean rtn;
		if(controller==null){
			rtn= this.rightIsEmpty;
		}
		rtn= !this.controller.isRightConnected();
//		System.out.println(" isRightEmpty()=="+rtn);
		return rtn;
	}
	public boolean isAllOccupied(){
		return !(this.isLeftEmpty()) && !(this.isRightEmpty());
	}
	public int returnAck(InetAddress addr, String x) {
		Socket socket = null;
		if(this.controller==null){
			if(this.leftIsEmpty){
				this.leftIsEmpty=false;
			}
			else
			if(this.rightIsEmpty){
				this.rightIsEmpty=false;
			}
		}
		try {
			System.out.println("connecting to "+addr+":"+ECHO_PORT);
			System.out.println("returing "+x);
		    socket = new Socket(addr, ECHO_PORT);
		    System.out.println("ê⁄ë±ÇµÇ‹ÇµÇΩ"
		                   + socket.getRemoteSocketAddress());
		    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    out.println(x);
		    DataInputStream in = new DataInputStream(socket.getInputStream());
		    int ack=in.readInt();
		    socket.close();
		    System.out.println("êÿífÇ≥ÇÍÇ‹ÇµÇΩ "
		            + socket.getRemoteSocketAddress());
		    return ack;
		} catch (Exception e) {
			System.out.println("error, InitialRequestServer, returnAck");
		    System.out.println(""+e.toString());
		    /*
		    try{
		    	if(socket!=null)
		    	socket.close();
			    System.out.println("êÿífÇ≥ÇÍÇ‹ÇµÇΩ with error, "
			            + socket.getRemoteSocketAddress());
		    }
		    catch(IOException ee){}
		    */
		    return 0;
		} 
	}
	InterNodeController controller;
	public void setInterNodeControllerNode(InterNodeController c){
		controller=c;
	}
}
