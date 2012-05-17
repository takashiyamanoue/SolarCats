package application.networktester;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
public class NetworkTesterChildProc extends NetworkTesterClient
{
    public NetworkTesterChildProc()
    {
    }
    public NetworkTesterChildProc(Socket so, NetworkTesterWorkerFrame cbf)
    {
        init();
        gui=cbf;
        sock=so;
        InetAddress ria=sock.getInetAddress();
        int rport=sock.getPort();
        int time=super.gui.communicationNode.eventRecorder.timer.getTime();
        String message=""+time+",\"connected\","+ria.toString()+","+rport;
        super.gui.messages.append(message+"\n");
        try{  in=sock.getInputStream();} catch(IOException e){System.out.println(e);}
        try{  out=sock.getOutputStream();} catch(IOException e){System.out.println(e);}

        this.start();
    }
    public void run()
    {
        while(me!=null){
            if(sock!=null){
                try{
                    int c=super.in.read(inBuff);
                    if(c<0) {stop(); return;}
                    super.out.write(inBuff,0,c);
                    super.out.flush();
                }
                catch(IOException e){
                }
            }
        }
    }
}