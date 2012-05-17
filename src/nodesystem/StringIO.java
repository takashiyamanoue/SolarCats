package nodesystem;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import util.TraceSW;
public class StringIO extends java.lang.Object
{
	TraceSW tracing;
	public void setTracing(TraceSW x){
		tracing=x;
	}
	public String getRemoteAddress(){
		if(sock==null) return "";
		return sock.getInetAddress().toString();
	}
	public String getRemotePort(){
		if(sock==null) return "";
		return ""+sock.getPort();
	}
	public boolean isTracing(){
		if(tracing==null) 
			return false;
		else
			return tracing.isTracing();
	}
    public boolean isConnected()
    {
        return this.sock!=null;
    }

    public InetAddress myAddress;

    public InetAddress remoteAddress;

    public StringIO(Socket sock, TraceSW tr)
    {
        maxbuff=1024;
        buffer=new byte[maxbuff];
        dbuffer=new byte[maxbuff];
        this.sock=sock;
        this.tracing=tr;
        try{
            /*
          in=sock.getInputStream();
          out=sock.getOutputStream();
            */
          in=new DataInputStream(
             //new BufferedInputStream(
             sock.getInputStream()
             //)
             );
          out=new DataOutputStream(
                //new BufferedOutputStream(
                sock.getOutputStream()
                //)
                );
        }
        catch(IOException e){
            System.out.println("faild at in/out stream making\n");
            return;
        }
        remoteAddress=sock.getInetAddress();
//        myID=distributor.addOutputStream(out);
        
/*
    try{
//            sock.setTcpNoDelay(true);
            System.out.println("SoLinger:"+this.sock.getSoLinger());
            System.out.println("SoTimeout:"+this.sock.getSoTimeout());
            System.out.println("sendBuffersize:"+this.sock.getSendBufferSize());
            System.out.println("readBuffersize:"+this.sock.getReceiveBufferSize());
            System.out.println("nodelay:"+this.sock.getTcpNoDelay());
        }
        catch(IOException e){
        }
*/
    }
    public void close() throws IOException
    {
        InetAddress ria=sock.getInetAddress();
        int rport=sock.getPort();
        String discmessage="disconnecting from "+ria.toString()+":"+rport+"\n";
        //    gui.appendMessage(discmessage);
//        System.out.println(discmessage);
        this.out.close();
        this.in.close();
        if(this.sock!=null)  {
        	this.sock.close();
        	this.sock=null;
        }
    }

    public Socket sock;

    public StringIO(String address, int port, TraceSW tr)
    {
        maxbuff=1024;
        buffer=new byte[maxbuff];
        dbuffer=new byte[maxbuff];
        this.tracing=tr;
        this.connect(address,port);
    }

    public int serverPort;

    public void connect(String serverAddress,int wellKnownPort)
    {
        serverPort=wellKnownPort;
        int maxtry=10;
        sock=null;
        while(sock==null){
           try{
               this.sock=new Socket(serverAddress,serverPort);
               this.sock.setSoTimeout(0);
           }
           catch(IOException e){
               this.sock=null;
               this.out=null;
           }
           if(sock==null){
              try{
        	      Thread.sleep(300);
              }
              catch(InterruptedException e){}
              if(maxtry<=0) return;
              maxtry--;
           }
        }
        try{
          in=new DataInputStream(
             this.sock.getInputStream()
             );
          out=new DataOutputStream(
                this.sock.getOutputStream()
                );
        }
        catch(IOException e){
        	System.out.println(""+e.toString()+"at new StriongIO");
            return;
        }
        remoteAddress=sock.getInetAddress();
        return;

    }

    public int maxbuff;
    byte buffer[];
    byte dbuffer[];
    /*
    public String readString() throws IOException
    {
          String rtn=in.readUTF();
//          out.writeUTF("ack");
//          out.flush();
//          System.out.println("read "+rtn);
          return rtn;
    }
    */
    
    public AMessage readString() throws IOException
    {
    	AMessage rtn=new AMessage();
    	String adr=this.sock.getRemoteSocketAddress().toString();
    	String h="";
    	//    	try{
    	synchronized(in){
         	  if(this.sock.isClosed()){
      	    	  System.out.println("StringIO socket closed");
      	    	  this.sock=null;
                  return null;
      	      }
//      	  if(in.available()>0){
         	  int time=in.readInt();
              int headLen=in.readInt();
              int buflen=this.buffer.length;
              if(headLen>buflen){
            	  this.buffer=new byte[headLen];
              }
              if(isTracing())
                  System.out.print(""+time+":"+this.serverPort+">>(headlen) "+headLen+" from "+adr);
              if(headLen<0){
//            	  sock.close();
//            	  System.out.println("...");
            	  throw new IOException("Stream in seems closed.");
//            	  return null;
              }
              try{
                  in.readFully(buffer,0,headLen);
              }
              catch(ArrayIndexOutOfBoundsException e){
            	  System.out.println("excepton ..."+e.toString());
            	  System.out.println("...reading head, len="+headLen);
            	  throw new IOException("strange headLen");
              }
              String head=new String(buffer,0,headLen);
              if(isTracing())
                  System.out.print(" >> "+head);
              int dataLen=in.readInt();
              if(isTracing())
                  System.out.println(" >>(len) "+dataLen);
//              int dlen=this.dbuffer.length;
//              if(dataLen>dlen){
            	  dbuffer=new byte[dataLen];
//              }
//              byte[] data=new byte[dataLen];             
              try{
                 if(dataLen>0)
                    in.readFully(dbuffer,0,dataLen);
              }
              catch(ArrayIndexOutOfBoundsException e){
            	  System.out.println(e.toString());
            	  System.out.println("...head="+head);
            	  throw new IOException("strange datalen");
              }
    	      /*
              for(int i=0;i<dataLen;i++){
            	  data[i]=buffer[i];
              }
              */
              rtn.setTime(time);
              rtn.setHead(head);
              rtn.setData(dbuffer);
    	}
        return rtn;

    }
    public void writeString(String s) throws IOException
    {
    	/*
            if(out==null) return;
//            System.out.println("write "+s);
            out.writeUTF(s);
            out.flush();
//            String ack=this.in.readUTF();
       */
    	AMessage x=new AMessage();
    	x.setHead(s);
    	writeString(x);
    }
    public void writeString(AMessage m) throws IOException
    {
    	String adr=this.sock.getRemoteSocketAddress().toString();
        if(out==null) return;
        if(isTracing())
        System.out.print(""+m.getTime()+":"+this.serverPort+"<< "+m.getHead()+" to "+adr);
        synchronized(out){
        	String h=m.getHead();
            byte[] hb=h.getBytes();
            out.writeInt(m.getTime());
            out.writeInt(hb.length);
            out.write(hb,0,hb.length);
            if(isTracing())
                System.out.println(" "+this.serverPort+"<< "+m.getDataLength());
            out.writeInt(m.getDataLength());
            if(m.getDataLength()>0)
                out.write(m.getData(),0,m.getDataLength());
            out.flush();
        }    	
    }
    public DataOutputStream out;
    public DataInputStream in;
    public StringIO(DataInputStream in,DataOutputStream out,TraceSW tr)
    {
        maxbuff=1024;
        buffer=new byte[maxbuff];
        dbuffer=new byte[maxbuff];
        this.in=in;
        this.out=out;
        this.tracing=tr;
    }
}

