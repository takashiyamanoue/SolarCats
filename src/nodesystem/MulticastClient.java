package nodesystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import util.TraceSW;

public class MulticastClient extends nodesystem.Client
{
    byte[] buff = new byte[1024];
    DatagramPacket packet; 
    public MulticastSocket msocket=null;
    public String mcastAddress;
    TraceSW tracing;

    public void connect(String maddr, int mcport)
    {
        mcastAddress=maddr;
        try {
            msocket = new MulticastSocket(mcport);
            InetAddress ma = InetAddress.getByName(mcastAddress);
            msocket.joinGroup(ma);
            
            System.out.println("running MulticastReceiver ...."
                + "address = " + mcastAddress + ", port = " 
                + msocket.getLocalPort());
                
        } catch (IOException e ) {
            e.printStackTrace();
        } 
        this.start();
    }

    public MulticastClient(MessageGui cc, Distributor distributor,
               CommandTranceiver cr, MessageQueue q,TraceSW tr)
    {
        super(cc, distributor, cr, q,tr);
        this.tracing=tr;
        packet = new DatagramPacket(buff, buff.length);
    }

    public boolean getMessage()
    {
        this.commandbuffer=null;
        try{
            msocket.receive(packet);
//            commandbuffer = new String(buff, 0, packet.getLength());
            String ps= new String(buff, 0, packet.getLength());
            commandbuffer=new AMessage();
            commandbuffer.setHead(ps);
//            System.out.println( "receive: " + commandbuffer);
            return true;
                                 
            } catch (IOException e ) {
                e.printStackTrace();
            } 
        return false;
    }

    public void comment()
    {
        /*
        
                +-----------+
                | reflector |
                +-----------+
                   ^  |  ^
       unicast(tcp)|  |  | unicast(tcp)
             +-----+  |  +----+
             |        v       |
        -----|--+-------------|--+------- multicast
             |  |             |  |
             |  v             |  v
         +-----------+     +---------+
         |client     | ... |client   |
         +-----------+     +---------+
              |                 |
      CommunicationNode    CommunicationNode
        
        */
    }

}