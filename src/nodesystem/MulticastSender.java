package nodesystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSender extends Object
{
    MulticastSocket msocket = null;
    InetAddress mcastAddress;
    int mcastPort;
    String mcastAddressName;

    public void sendMessage(String x)
    {
        try{
            byte[] bytes = x.getBytes();
            DatagramPacket packet = 
                new DatagramPacket(bytes, bytes.length, 
                                mcastAddress, mcastPort);
            msocket.send(packet);
        } catch (IOException e ) {
            e.printStackTrace();
        } 
    }
    
	public MulticastSender(String addr, int port)
	{
		this.mcastAddressName=addr;
		this.mcastPort=port;
		try{
            mcastAddress = InetAddress.getByName(mcastAddressName);
            if(msocket==null)
                msocket = new MulticastSocket();
            else{
                msocket.close();
                msocket = new MulticastSocket();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
	}
}
