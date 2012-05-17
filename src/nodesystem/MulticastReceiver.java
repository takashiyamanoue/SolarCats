package nodesystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver extends java.lang.Object
                               implements Runnable
{
    public MessageReceiver mreceiver;


    protected Thread me;
    MulticastSocket msocket = null;
    byte[] buff = new byte[1024];
    DatagramPacket packet = new DatagramPacket(buff, buff.length);
    String mcastAddress; // Multicastアドレスの指定
    
    public MulticastReceiver(String maddr, int mcport, MessageReceiver mr){

        mcastAddress=maddr;
        this.mreceiver=mr;
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
        
    }
    
    public void start(){
        if(me==null){
            me=new Thread(this,"MulticastReceiver");
            me.start();
        }
    }
    public void stop(){
        me=null;
    }
    
    public void run() {
        while(me!=null) {
            try{
                msocket.receive(packet);
                String message = new String(buff, 0, packet.getLength());
                this.mreceiver.receiveMessage(message);
                /*
                System.out.println(packet.getSocketAddress()
                    + " 受信: " + message);
                    */
            } catch (IOException e ) {
                e.printStackTrace();
            } 
        }
    }
    
}
