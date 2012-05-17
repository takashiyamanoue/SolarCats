package application.networktester;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
public class NetworkTesterClient implements java.lang.Runnable 
{
    int dataBufferMax;

    public void lock()
    {
        while(lockFlag){
            try{Thread.sleep(2);}
            catch(InterruptedException e){}
        }
        lockFlag=true;
    }

    public void unlock()
    {
        this.lockFlag=false;
    }

    boolean lockFlag;

    boolean canPut;
    int dataBufferLength;
    byte[] dataBuffer;


    byte dmy[];
    public void disconnect()
    {
        stop();
        if(in!=null){
            try{
              in.close();
            }
           catch(Exception e){
              gui.appendMessage("Exception "+e+"\n");
           }
        }
        if(out!=null){
          try{
            out.close();
          }
          catch(Exception e){
              gui.appendMessage("Exception "+e+"\n");
          }
        }
    }
    int myID;
    public NetworkTesterClient()
    {
        init();
    }
    int maxbuff;
    byte[] outBuff;
    byte[] inBuff;
    public void init()
    {
        maxbuff=1400;
        inBuff=new byte[maxbuff];
        outBuff=new byte[maxbuff];
        dataBufferMax=2000000;
        me=null;
        sock=null;
        dmy=new byte[4];
        dataBuffer=new byte[dataBufferMax]; // 2Mbyte
        canPut=true;
    }
    public synchronized void putData(int size, byte[] data)
    {
        byte dmy[]=new byte[4];

        int n,i,j,k,len,nsent,p;
        int c;
        nsent=0;
          try{
              out.write(data);
              out.flush();
          }
          catch(IOException e){
             gui.messages.append(""+e);
          }
          catch(Exception e){
             gui.messages.append(""+e);
          }
    }
  public boolean getData()
  {

        int c=0;
        int i,j,r,niter;
        int reclen,lb,hb;
        String xs;
        if(sock==null) {stop(); return false;}
        try{
             c=in.read(this.dataBuffer);
        }
        catch(IOException e){
                System.out.println(""+e);
                stop(); return false;
        }
        if(c==-1) {stop(); return false; }

            //
        reclen=0;
        for(int ii=0;ii<4;ii++){
            reclen=(reclen<<8)|(dataBuffer[ii] & 0x000000ff);
        }
        if(reclen>dataBuffer.length) {
            System.out.println("too big data");
            return false;
        }
        int nread=c;
         
        while(nread<reclen){
            int restsize=reclen-nread;
            try{
                c=in.read(dataBuffer,nread,restsize);
            }
            catch(IOException e){
                System.out.println("IOException occured at NetworkTesterClient.dataBuffer");
                stop(); // unlock(); 
                return false;
            }
            if(c<0) {close(); stop(); dataBufferLength=nread;  return false; }

            nread+=c;
        }
        dataBufferLength=nread; 
        return true;           
 }
    DataInputStream dinstream;
    public void connect(String serverAddress,
                        int    wellKnownPort)
    {
        serverPort=wellKnownPort;
        try{
            sock=new Socket(serverAddress,serverPort);
        }
        catch(IOException e){
            gui.appendMessage("Server is down?, connection failed.\n");
            sock=null;
            out=null;
            return;
        }
        try{
            in=sock.getInputStream();
            out=sock.getOutputStream();
        }
        catch(IOException e){
            gui.appendMessage("faild at in/out stream making\n");
            return;
        }
        InetAddress ria=sock.getInetAddress();
        start();
        gui.appendMessage("connected to "+serverAddress+"\n");
    }
    int serverPort;
    String serverAddress;
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
            me=new Thread(this,"tester client");
            me.start();
        }
    }
    Thread me;
    public void run()
    {
        while(me!=null){
            if(sock!=null){
                 if(getData())  // receive the data
                 gui.receiveData(this.dataBufferLength,this.dataBuffer);
                 else return;
//                 putData(dataBufferLength,dataBuffer); //echo back the data
//                 gui.appendMessage("return "+dataBufferLength+"\n");
            }
        }
    }
    public void close()
    {
 //       me.stop();
        try{
            if(in!=null)  in.close();
            if(out!=null) out.close();
//            if(sock!=null) sock.close();
        }
        catch(Exception e){
            gui.appendMessage("Exception "+e+"\n");
        }
        sock=null;
    }
    OutputStream out;
    InputStream in;
    Socket sock;
    NetworkTesterWorkerFrame gui;
    public NetworkTesterClient(NetworkTesterWorkerFrame cc)
    {
        gui=cc;
        init();

   }
}