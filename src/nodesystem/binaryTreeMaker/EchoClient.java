package nodesystem.binaryTreeMaker;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
public class EchoClient {
  public static final int ECHO_PORT = 10008;
  public EchoClient(String x, InetAddress addr) {
    Socket socket = null;
    try {
      socket = new Socket(addr, ECHO_PORT);
      System.out.println("ê⁄ë±ÇµÇ‹ÇµÇΩ"
                   + socket.getRemoteSocketAddress());
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

      out.println(x);
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
       if (socket != null) {
         socket.close();
       }
    } catch (IOException e) {}
       System.out.println("êÿífÇ≥ÇÍÇ‹ÇµÇΩ "
            + socket.getRemoteSocketAddress());
    }
  }
}