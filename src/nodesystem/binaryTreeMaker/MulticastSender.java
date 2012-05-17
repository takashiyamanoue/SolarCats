package nodesystem.binaryTreeMaker;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class MulticastSender {

  public static final int ECHO_PORT = 10007;
  public static final int PACKET_SIZE = 1024;
  public static final String MCAST_ADDRESS = "224.0.0.7";
  public static void main(String args[]) {
    MulticastSocket socket = null;
    try {
      InetAddress mcastAddress = InetAddress.getByName(MCAST_ADDRESS);
      BufferedReader keyIn =
        new BufferedReader(new InputStreamReader(System.in));
      socket = new MulticastSocket();
      //socket.joinGroup(mcastAddress);
      String message;
      message = keyIn.readLine();
      byte[] bytes = message.getBytes();
      DatagramPacket packet =
      new DatagramPacket(bytes, bytes.length, mcastAddress, ECHO_PORT);
      EchoServer es = new EchoServer();
      socket.send(packet);
      socket.close();

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (socket != null) {
         socket.close();
      }
    }
  }
}