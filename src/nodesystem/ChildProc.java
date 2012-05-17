package nodesystem;
import java.net.InetAddress;
import java.net.Socket;

import util.TraceSW;
public class ChildProc extends Client
{
    public ChildProc()
    {
    }
    public ChildProc(Socket so,
                     MessageGui cbf,
                     Distributor d,
                     CommandTranceiver cr,
                     TraceSW tr)
    {
        init();
        gui=cbf;
        sock=so;
        distributor=d;
        InetAddress ria=sock.getInetAddress();
        int rport=sock.getPort();
        gui.appendMessage("ChildProc, Connected from "+ria.toString()+":"+rport+"\n");
        sio=new StringIO(sock,tr);
//        sio.setTracing(tr);
        StringIO l=distributor.getIOAt(1);
        if(l==null){
          distributor.setOutputStream(1, sio);
          myID=1;
        }
        else{
          myID=distributor.addOutputStream(sio);
          if(myID<0){
            this.close();
            return;
          }
        }
//
//        sendCurrentState();
        commandTranceiver=cr;
        if(commandTranceiver!=null)
          this.receiveEventQueue=this.commandTranceiver.receiveEventQueue;
        this.start();
        gui.appendMessage(" streamChannelID="+myID+"\n");

//        gui.appendMessage("Connected from "+gui.myAddressField.getText()+"\n");
    }
}

