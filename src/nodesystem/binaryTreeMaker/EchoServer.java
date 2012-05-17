package nodesystem.binaryTreeMaker;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

public class EchoServer 
implements Runnable
{
public static final int ECHO_PORT = 10008;

  Thread me;
  public EchoServer() {
	  this.start();
  }
  public void start(){
	  if(me==null){
		  me=new Thread(this);
		  me.start();
	  }
  }
  public void stop(){
	  me=null;
  }

//@Override
public void run() {
	// TODO Auto-generated method stub
    ServerSocket serverSocket = null;
    Socket socket = null;
    try {
      serverSocket = new ServerSocket(ECHO_PORT);
      System.out.println("EchoServerÇ™ãNìÆÇµÇ‹ÇµÇΩ(port=" + serverSocket.getLocalPort() + ")");
      socket = serverSocket.accept();
      System.out.println("ê⁄ë±Ç≥ÇÍÇ‹ÇµÇΩ "
                  + socket.getRemoteSocketAddress() );
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      String line;
      line = in.readLine();
      System.out.println("éÛêM: " + line);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (socket != null) {
          socket.close();
        }
      } catch (IOException e) {}
      try {
        if (serverSocket != null) { 
           serverSocket.close();
        }
      } catch (IOException e) {}
    }
	
}

}