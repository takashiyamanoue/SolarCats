package nodesystem.binaryTreeMaker;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.io.IOException;
/*
 * multicast パケットが使いたいインターフェースで入出力できない場合がある。
 * この場合、netstat -r で routing を確認後、
 * route delete 224.0.0.0 などで、余分なroute を削除することで
 * 正常に戻る。
 */
public class MulticastReceiver {
  public static final int ECHO_PORT = 10007;
  public static final int PACKET_SIZE = 1024;
  public static final String MCAST_ADDRESS = "224.0.1.1";
  public static void main(String args[]) {
		System.setProperty("java.net.preferIPv4Stack","true");
    MulticastSocket socket = null;
    byte[] buf = new byte[PACKET_SIZE];
    DatagramPacket packet = new DatagramPacket(buf, buf.length);
    try {
       socket = new MulticastSocket(ECHO_PORT);
       InetAddress mcastAddress = InetAddress.getByName(MCAST_ADDRESS);
       socket.joinGroup(mcastAddress);
       System.out.println("MulticastReceiverを起動しました(" + "address=" + mcastAddress
                       + ",port=" + socket.getLocalPort() + ")");
       while (true) {
          socket.receive(packet);
          String message = new String(buf, 0, packet.getLength());
          System.out.println(packet.getSocketAddress()
                    + " : " + message);
          InetAddress addr = packet.getAddress();
          EchoClient ec = new EchoClient(message, addr);

       }
    } catch (IOException e) {
       e.printStackTrace();
    } finally {
       if (socket != null) {
          socket.close();
       }
    }
  }
}