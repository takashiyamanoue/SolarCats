package nodesystem;
import java.io.IOException;
import java.util.Vector;
public class Distributor extends java.lang.Object
{
    public byte buff[];

    public int smax;

    public void comment()
    {
        /*
        
               |         |
               v         v
              putS       |
               |         |
               +---------+
                         |
                         v
                     putBuffer
                         |
                         v
            +---------------+
            | outputQueue   |
            |               |
            +---------------+
                    |
                    v
             +--------------+
             |  distribute  |
             |     write    |
             +--------------+
            
          */        
        
        
    }

    public synchronized void putBinary(byte[] x, int myID)
    {
        /*
         int length=x.length;
         byte[] tbuff=new byte[length];
         OutputStream o;
         InputStream in;
         int c=x.length;
         for(int j=0;j<smax;j++){
              if(j!=myID){
                    o=streams[j];                    
//                    o=(OutputStream)(streams.elementAt(j));
//                    in=(InputStream)(inStreams.elementAt(j));
                    if(o!=null){
                           for(int it=0;it<length;it++)  // copy buffer, for sending
                              tbuff[it]=x[it];        //
                          try{
                              o.write(c);
                              o.write(tbuff);
                              o.flush();
                          }
                          catch(IOException e){
                            System.out.println("IOEXception occured at Distributor.putBinary");
                          }
                    }
                }

          }
          */
    }

    public String encodingCode;

    public Vector inStreams;
    public synchronized void putBuffer(byte[] buff,int myID)
    {
        /*
         OutputStream o;
//         System.out.println("put buffer except out strm "+myID+", max:"+max);
         int c;
         for(int j=0;j<smax;j++){
              if(j!=myID){
                    o=streams[j];
                    if(o!=null){
                        try{
                          o.write(buff);
//                          o.flush();
                        }
                        catch(IOException e){
                            System.out.println("IOException occurd. at Distributor.putBuffer");
                        }
                    }
                }

          }
          */

    }
    public synchronized void putS(String s,int myID)
    {
//        DataOutputStream o=null;
        StringIO o=null;
        for(int j=0;j<smax;j++){
              if(j!=myID){
                    o=streams[j];
                    if(o!=null){
                        try{
                           o.writeString(s);
                        }
                        catch(IOException e){
                            System.out.println(e.toString()
                               +" while distributor.puts(s,id)");
                        }
                        
                    }
                }

          }
    }
    public synchronized void putS(AMessage s,int myID)
    {
//        DataOutputStream o=null;
        StringIO o=null;
        for(int j=0;j<smax;j++){
              if(j!=myID){
                    o=streams[j];
                    if(o!=null){
                        try{
                           o.writeString(s);
                        }
                        catch(IOException e){
                            System.out.println(e.toString()
                               +" while distributor.puts(s,id)");
                        }
                        
                    }
                }

          }
    }
    public int maxbuff;
    public void remove(int i)
    {
        streams[i]=null;
    }
//    public Vector streams;
//    public DataOutputStream[] streams;  
      public StringIO[] streams;
    
    public synchronized void put(int c)
    {
        /*
        byte dmy[]=new byte[4];
        OutputStream o;
        InputStream in;
        int i;
        int cx;
        for(i=0;i<smax;i++){
              o=streams[i];
              if(o!=null){
                 try{
                    o.write(c);
                    o.flush();
                 }
                 catch(IOException e){
                    System.out.println("IOException occured at Distributor.put");
                 }
              }

        }
        */
    }
//    public int addOutputStream(OutputStream o)
    public int addOutputStream(StringIO o)
    {
    	int rtn=-1;
//        inStreams.addElement(i);
        for(int i=1;i<this.smax;i++){
            if(streams[i]==null){
                streams[i]=o;
                rtn= i;
                break;
            }
        }
        System.out.println("addOutputStream()="+rtn);
        return rtn;
    }
    public void setOutputStream(int i,StringIO o)
    {
    	if(i<0) return;
    	if(i>streams.length) return;
        streams[i]=o;
        System.out.println("setOutputStream("+i+",o)");
    }
    public Distributor(String code, int sm)
    {
//        text=a;
//        streams=new  Vector();
//        inStreams=new Vector();
        smax=sm; // max stream number
//        streams=new DataOutputStream[smax];
        streams=new StringIO[smax];
        for(int i=0;i<smax;i++){
            streams[i]=null;
        }
        maxbuff=256;
        this.buff=new byte[maxbuff];
//        maxbuff=1024;
        encodingCode=code;
    }
    public StringIO getIOAt(int x){
//    	System.out.println("getIoAt("+x+")");
    	try{
        	StringIO rtn=streams[x];
        	return rtn;
    	}
    	catch(Exception e){
    		return null;
    	}
    }
}

