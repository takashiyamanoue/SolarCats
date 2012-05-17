package nodesystem;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.JRadioButton;

import controlledparts.ControlledFrame;

import util.TraceSW;

import nodesystem.binaryTreeMaker.InitialRequestClient;
import nodesystem.binaryTreeMaker.InitialRequestServer;
import nodesystem.eventrecorder.EventRecorder;

public class InterNodeController
{
	/*
	 * 上位ノードが切れたとき。
	 *  - 上位ノードが切れたときの認識:
	 *     * 上位ノードに要求した時間が帰ってこないとき
	 *     * isConnected が false のとき
	 *  - 最もserialNo が大きなノードを上位ノードに置き換える。
	 *    * group manager に通知する。
	 *    * group manager が関係するノードに指示を出す。
	 *      - serialNo が最も大きなノードをグループから一度切り離し
	 *      　serialNoを切れたノード(上位ノード) と置き換える
	 *      - そのノードを、切れたノードの上位ノードに接続する。
	 *      - 下位ノードを、そのノードに接続しなおす。
	 *      
	 *      
	 *                  1
	 *               /     \
	 *             2         3 <--- これが切れたとき
	 *           /    \    /    \
	 *          4     5   6      7
	 * 
	 *      
	 *                  1
	 *               /     \
	 *             2         * 
	 *           /    \    /    
	 *          4     5   6      7 <--- group から切り離す。
	 *      
	 *      
	 *                  1
	 *               /     \
	 *             2         *
	 *           /    \    /    
	 *          4     5   6      7 -> 3 にする。
	 *      
	 *      
	 *                  1
	 *               /     \
	 *             2         3
	 *           /    \    /    
	 *          4     5   6      
	 *      
	 *      
	 *      
	 */
	
    int wellKnownPort;
    int groupMngrPort;
    ConnectionReceiver connectionReceiver;
    Client clientToUpperNode;
    CommunicationNode communicationNode;    
    Thread me;
    public int remainNextDoorRequest;
    MulticastClient mcastClient;
    public String nodeKind;
    boolean isJoined;
    public String groupMode;
    NodeSettings settings;
    Distributor distributor;
    CommandTranceiver commandTranceiver;
    EventRecorder eventRecorder;
    int treeHeight;

    public InterNodeController(
    		CommunicationNode cn, Distributor d,
    		CommandTranceiver tx, EventRecorder r){
    	communicationNode=cn;
    	distributor=d;
    	commandTranceiver=tx;
    	eventRecorder=r;
		wellKnownPort=27183;
        groupMngrPort=31415;
//        this.encodingCode="JISAutoDetect";
		this.settings=new NodeSettings(communicationNode);

        communicationNode.listenPort.setText(""+wellKnownPort);
        communicationNode.connectPort.setText(""+wellKnownPort);
        communicationNode.groupMngrPortField.setText(""+groupMngrPort);
        this.settings.groupMngrPortField.setText(""+groupMngrPort);
//        this.groupMode="P2P";
        this.groupMode=this.settings.getString("groupMode");
    }
    
    public boolean isRootNode(){
    	if(this.nodeKind==null) return false;
    	return this.nodeKind.equals("root");
    }
    
    public int getNodeNumber(){
        int serverPort=(new Integer(communicationNode.groupMngrPortField.getText())).intValue();
        String serverAddress=communicationNode.groupMngrAddressField.getText();
        StringIO sio=new StringIO(serverAddress,serverPort,this.getTrace());
        if(sio.sock==null) return 2;
        communicationNode.appendMessage("connected to "+serverAddress+"\n");
        int num=0;
        try{
           sio.writeString("getNodeNumber");
           String snum=(sio.readString()).getHead();
           num=(new Integer(snum)).intValue();
        }
        catch(Exception e){
        	return 2;
        }
    	return num;
    }

    void connect()
    {
		String theAddress=communicationNode.addressOfUpperNode.getText();
		int cport=(new Integer(communicationNode.connectPort.getText())).intValue();
		clientToUpperNode.connect(theAddress,cport);
		communicationNode.setPaths2();
    }

    public void connectToUpperNode(String addr, String port)
    {
		communicationNode.addressOfUpperNode.setText(addr);
		communicationNode.connectPort.setText(port);
		this.connect();
    }
    StringIO getConnectionToTheGroupManager(){
    	JRadioButton b=this.settings.getSelectedButton();
    	if(!((b.getText()).equals("P2P"))){
    		return null;
    	}
    	if(communicationNode.groupMngrAddressField.equals("")) return null;
        int serverPort=(new Integer(communicationNode.groupMngrPortField.getText())).intValue();
        String serverAddress=communicationNode.groupMngrAddressField.getText();
        communicationNode.appendMessage("connected to "+serverAddress+"\n");
        StringIO sio=new StringIO(serverAddress,serverPort,this.getTrace());
        if(!sio.isConnected()) return null;
        sio.setTracing(this.getTrace());
        return sio;	
    }
    
    void disconnect()
    {
    	if(!this.isConnectedToUpperNode()) return;
        StringIO sio=this.getConnectionToTheGroupManager();
        if(!(sio==null)) {
           try{
           	/*
              sio.writeString("delnode "+ communicationNode.listenPort.getText()
                                 +" "+ communicationNode.serialNo
                                 +" "+ this.communicationNode.myAddressField.getText());
           */
              sio.writeString("delnode "+communicationNode.serialNo);
//           String ack=sio.readString();
           //
              sio.close();
           }
           catch(IOException e){}
        }
		if(connectionReceiver!=null) connectionReceiver.stop(); 
        communicationNode.appendMessage("end of disconnecting.\n");
        
        if(clientToUpperNode==null) return;
		if(clientToUpperNode.sio==null) return;
        String myNamex=communicationNode.userName.getText()+"-"+this.communicationNode.serialNo;
        String sending=
         "broadcast shell chat leaving "+myNamex+".\n"+commandTranceiver.endOfACommand;
        AMessage m=new AMessage();
        m.setHead(sending);
        if(communicationNode.ebuff!=null){
             communicationNode.ebuff.putS(m);
        }
        communicationNode.chatIn.setText("");
        //
        // close streams to the upper node
        //
		clientToUpperNode.disconnect();
		if(this.rClient!=null){
			rClient.stop();
		}
		if(this.rServer!=null){
			rServer.stop();
		}
		//
        communicationNode.addressOfUpperNode.setText("");
        /*
		1. connect to the group manager
		2. send my information to the manager
		3. receive the group system information
		4. connect to the upper node in the group
		5. start listening from connections.
		*/
		
/*
        int serverPort=(new Integer(communicationNode.groupMngrPortField.getText())).intValue();
        String serverAddress=communicationNode.groupMngrAddressField.getText();
 		communicationNode.appendMessage("disconnecting from "+serverAddress);
 		
        StringIO sio=new StringIO(serverAddress,serverPort);
        if(!sio.isConnected()) return;
        */

    }

    void joinCSBGroup()
    {
		this.communicationNode.setControlMode("common");
		String sno=this.communicationNode.serialNoField.getText();
		if(sno.equals("")) sno="0";
		try{
           communicationNode.serialNo=(new Integer(sno)).intValue();
        }
        catch(Exception e){
            communicationNode.serialNo=0;
        }
		clientToUpperNode=new Client(communicationNode,
				distributor,
				commandTranceiver,
		        communicationNode.receiveEventQueue,this.getTrace());
		this.connect();
		this.nextDoorControl("nextDoor requestNo",null);
    }

	void joinCSGroup()
	{
		this.communicationNode.setControlMode("withReflector");
		clientToUpperNode=new Client(communicationNode,
				distributor,
				commandTranceiver,
		        communicationNode.receiveEventQueue,this.getTrace());
		
		this.connect();
		this.nextDoorControl("nextDoor requestNo",null);
	}

    void setUpperNodeAddressPort(String a, String p)
    {
        this.communicationNode.addressOfUpperNode.setText(a);
        this.communicationNode.connectPort.setText(p);
    }

	void joinMCASTGroup()
	{
	    this.mcastClient=new MulticastClient(communicationNode,
	    		null,
	    		commandTranceiver,
	            communicationNode.receiveEventQueue,this.getTrace());
		String maddr=this.settings.multicastAddressField.getText();
		int mport=(new Integer(this.settings.multicastPortField.getText())).intValue();
		this.mcastClient.connect(maddr,mport);
		this.communicationNode.setControlMode("withReflector");
		clientToUpperNode=new Client(communicationNode,
				distributor,null,
		        communicationNode.receiveEventQueue,this.getTrace());
		this.connect();
		try{
		    Thread.sleep(10);
		}
		catch(InterruptedException e){}
		this.nextDoorControl("nextDoor requestNo",null);
	}

    void joinP2PGroup(String nodeKind) //nodeKinde="teacerh"|"student"
    {
        /*
	    Class thisClass=this.getClass();
	    String thisName=thisClass.getName();
	    if(thisName.equals("nodesystem.CommunicationNode")){
	    */
	    clientToUpperNode=new Client(communicationNode,
	    		distributor,
	    		commandTranceiver,
		        communicationNode.receiveEventQueue,this.getTrace());
	    if(this.communicationNode.className.equals("Communication_Node")){
	        joinTheGroup(nodeKind);
	        return;
	    }
	    if(!isJoined){
    	    if(joinTheGroup(nodeKind)) 
    	          isJoined=true;
    	    else isJoined=false;
    	}
    }
    boolean fixing=false;
    public void setFixing(boolean on){
    	this.fixing=on;
    }
    public void setFixingTree(boolean on){
    	String f="off";
    	if(on) f="on";
        String sending=
            "broadcast shell setFixing "+f+".\n"+commandTranceiver.endOfACommand;
           AMessage m=new AMessage();
           m.setHead(sending);
           if(communicationNode.ebuff!=null){
                communicationNode.ebuff.putS(m);
           }
    	
    }

    void acceptStart()
    {
		wellKnownPort=(new Integer(communicationNode.listenPort.getText())).intValue();
		connectionReceiver=new ConnectionReceiver(
		                   communicationNode,
		                   distributor,
		                   wellKnownPort,
		                   commandTranceiver,this.getTrace());
    }
   
    
    void joinGroupWithMode(String nodeKind)
    {
    	String mode=this.getGroupMode();
//        this.setGroupMode(x);
    	this.communicationNode.appendMessage("groupMode="+this.groupMode+"\n");
        if(mode.equals("P2P")){
            joinP2PGroup(nodeKind);
            return;
        }
        if(mode.equals("CSB")){
            joinCSBGroup();
            return;
        }
        if(mode.equals("CS")){
            joinCSGroup();
            return;
        }
        if(mode.equals("MCAST")){
            joinMCASTGroup();
            return;
        }
        if(mode.equals("P2Pa")){
            startMakeGroup(nodeKind);
            return;
        }
    }
     public void startMakeGroup(String kind){
    	int nodePort=this.settings.getInt("MakeBinaryTree-waitingNodePort");
        communicationNode.listenPort.setText(""+nodePort);
    	if(kind.equals("teacher")){
    		this.nodeKind="root";
            communicationNode.serialNo=1;
            communicationNode.appendMessage("my serial no="+communicationNode.serialNo+"\n");
            communicationNode.serialNoField.setText(""+communicationNode.serialNo);
			int wth=new Integer(this.settings.getString("MakeBinaryTree-waitToHeight")).intValue();
			int it=new Integer(this.settings.getString("MakeBinaryTree-ServerRepTerm")).intValue();
            int ap=this.settings.getInt("MakeBinaryTree-ackReceivePort");
            boolean parallel=this.settings.getBoolean("MakeBinaryTree-parallel-processing");
            this.acceptStart();
    		this.rServer=new InitialRequestServer(1,this,
    				this.settings.getString("groupName"),
    				this.settings.getString("groupMcastAddr"),
    				this.settings.getInt("groupMcastPort"),
    				nodePort,
    				wth,it,ap,1,parallel
    		);
    		this.rServer.start();
    		communicationNode.start();
    	}
    	else
    	if(kind.equals("student")){
    	    clientToUpperNode=new Client(communicationNode,
    	    		distributor,
    	    		commandTranceiver,
    		        communicationNode.receiveEventQueue,this.getTrace());
			int rt=new Integer(this.settings.getString("MakeBinaryTree-requestRepTerm")).intValue();
			int wt=new Integer(this.settings.getString("MakeBinaryTree-waitAfterConnect")).intValue();
            int ap=this.settings.getInt("MakeBinaryTree-ackReceivePort");
    		this.rClient=new InitialRequestClient(
    				this,
    				this.settings.getString("groupName"),
    				this.settings.getString("groupMcastAddr"),
    				this.settings.getInt("groupMcastPort"), rt, wt,ap); 
    		int height=0;
            communicationNode.start();
    		while(height<=1){
    			try{
    				Thread.sleep(100);
    			}
    			catch(InterruptedException e){}
    			height=this.getNodeDepth();
    		}
//    		this.nodePort=this.nodePort+this.communicationNode.serialNo;
            communicationNode.listenPort.setText(""+nodePort);
			int wht=new Integer(this.settings.getString("MakeBinaryTree-waitToHeight")).intValue();
			int it=new Integer(this.settings.getString("MakeBinaryTree-ServerRepTerm")).intValue();
            ap=this.settings.getInt("MakeBinaryTree-ackReceivePort");
            boolean parallel=this.settings.getBoolean("MakeBinaryTree-parallel-processing");
    		this.acceptStart();
    		this.communicationNode.sendChatMessage("joined.");
    		this.rServer=new InitialRequestServer(height,this,
    				this.settings.getString("groupName"),
    				this.settings.getString("groupMcastAddr"),
    				this.settings.getInt("groupMcastPort"),
    				nodePort,
    				wht,it,ap,communicationNode.serialNo,parallel
    				);
    		this.rServer.start();
    		this.rClient.stop();
    	}
    	
    }

    public boolean joinTheGroup(String opKind)
    {
    	this.communicationNode.stop();
        this.remainNextDoorRequest=0;
        int serverPort=(new Integer(communicationNode.groupMngrPortField.getText())).intValue();
        String serverAddress=communicationNode.groupMngrAddressField.getText();
        return joinTheGroup(serverAddress,serverPort,opKind);
    }
    public boolean joinTheGroup(String a, int port, String opKind)
    {
    	this.communicationNode.stop();
        this.remainNextDoorRequest=0;
        int serverPort=port;
        String serverAddress=a;
        boolean contWait=false;
        StringIO sio=null;
        do{
           sio=new StringIO(serverAddress,serverPort,this.getTrace());
           communicationNode.appendMessage("connected to "+serverAddress+"\n");
           String wx="";
           try{
               wx=(sio.readString()).getHead();
           }
           catch(IOException e){}
    	   if(wx.startsWith("wait")){
    		   try{
        		   sio.close();
    		   }
    		   catch(IOException e){}
    		   try{
    			   Thread.sleep(1000);
    		   }
    		   catch(InterruptedException e){}
    		   contWait=true;
    	   }
    	   else{
    		   contWait=false;
    	   }
        }
        while(contWait);
        
        try{
           sio.writeString("addnode-"+opKind);
           //nodeKind=sio.readString();
           nodeKind=(sio.readString()).getHead();
 
           int xport=0;

           if(nodeKind.equals("root")){
              communicationNode.appendMessage("I'm the root\n");
              /*
              try{
                  Thread.sleep(100);
              }
              catch(InterruptedException e){}
              */
//              String xps=sio.readString();
              String xps=(sio.readString()).getHead();
              xport=(new Integer(xps)).intValue();
              communicationNode.appendMessage("my listen port="+xport+"\n");
            //
              communicationNode.listenPort.setText(""+xport);
//              String sno=sio.readString();
              String sno=(sio.readString()).getHead();
              communicationNode.serialNo=(new Integer(sno)).intValue();
              communicationNode.appendMessage("my serial no="+communicationNode.serialNo+"\n");
              communicationNode.serialNoField.setText(""+communicationNode.serialNo);
           }
           else
           if(nodeKind.equals("leaf")) {
              communicationNode.appendMessage("I'm a leaf node.\n");
//              String tmp=sio.readString();
              String tmp=(sio.readString()).getHead();
              communicationNode.appendMessage(tmp+"\n");
              /*
              try{
                  Thread.sleep(100);
              }
              catch(InterruptedException e){}
              */
              String upnode=(sio.readString()).getHead();
              communicationNode.appendMessage(upnode+":");
              String upport=(sio.readString()).getHead();
              communicationNode.appendMessage(upport+"\n");
              String listenport=(sio.readString()).getHead();
              communicationNode.appendMessage("listen port="+listenport+"\n");
            //
              String sno=(sio.readString()).getHead();
              communicationNode.serialNo=(new Integer(sno)).intValue();
              communicationNode.appendMessage("my serial no="+communicationNode.serialNo+"\n");
              communicationNode.serialNoField.setText(""+communicationNode.serialNo);
              StringTokenizer st=new StringTokenizer(upnode,"/");
              String dmy="";
              String theAddress="";
              if(st.hasMoreTokens()) {
                 dmy=st.nextToken();
                 if(st.hasMoreTokens()) {
                     dmy=st.nextToken();
                     if(st.hasMoreTokens()) dmy=st.nextToken();
                     else theAddress=dmy;
                 }
                 else theAddress=dmy;
              }
              else  theAddress=st.nextToken();
              communicationNode.addressOfUpperNode.setText(theAddress);
              communicationNode.connectPort.setText(""+upport);
              communicationNode.listenPort.setText(listenport);
              connect();
            //
           }
           else{ // none
           	  communicationNode.appendMessage("failed to join the group.\n");
           	  return false;
           }
          //
          // receive the time now from the group manager
		  acceptStart();
		  communicationNode.thisID=communicationNode.uName+"-"+communicationNode.serialNo;
		  String stime=(sio.readString()).getHead();
		  int t=(new Integer(stime)).intValue();
		  eventRecorder.timer.setTime(t);
		  sio.writeString("ack:node "+communicationNode.serialNo+" started.");
		  sio.close();
		  communicationNode.appendMessage("time from the group manager is "+stime+"\n");

  // announce to the group.
  //
	      String sending=communicationNode.thisID+"(Ver."+communicationNode.thisVer+")"+" joined the group.\n";//SAKURAGI CHANGED
          communicationNode.chatOut.append(sending);
          String s="broadcast shell chat "+sending+communicationNode.commandTranceiver.endOfACommand;
          AMessage m=new AMessage();
          m.setHead(s);
          communicationNode.commandTranceiver.sendCommand(
        		  m);
          //SAKURAGI ADDED START---
          m.setHead("broadcast shell vercheck "+CommunicationNode.thisVer+communicationNode.commandTranceiver.endOfACommand);
          communicationNode.commandTranceiver.sendCommand(
        		  m
        		  );
          //SAKURAGI ADDED END-----
          communicationNode.chatIn.setText("");
        }
        catch(IOException e){}
        communicationNode.start();
        return true;
    }
    public void resetGroupManager(){
    	this.communicationNode.stop();
        this.remainNextDoorRequest=0;
        try{
           if(this.isRootNode()){
               int serverPort=(new Integer(communicationNode.groupMngrPortField.getText())).intValue();
               String serverAddress=communicationNode.groupMngrAddressField.getText();
               StringIO sio=new StringIO(serverAddress,serverPort,this.getTrace());
               communicationNode.appendMessage("connected to "+serverAddress+"\n");
               sio.writeString("reset");
               String xps=(sio.readString()).getHead();
               sio.close();
           }
        }
        catch(IOException e){}
        communicationNode.start();
    	
    }
    public void initialize(){
    	this.communicationNode.stop();
//    	this.disconnect();
		clientToUpperNode.disconnect();
		clientToUpperNode=null;
		this.connectionReceiver.stop();
		this.communicationNode.chatOut.setText("");
		this.communicationNode.messageArea.setText("");
		this.isJoined=false;
//		this.communicationNode.start();
    }
    
    public void receiveTime()
    {
//		if(client.sock==null) return;
//		client.disconnect();
//        addressOfUpperNode.setText("");
        /*
		1. connect to the group manager
		2. send gettime request
		3. receive the time
		4. close
		*/
		
        int serverPort=(new Integer(communicationNode.groupMngrPortField.getText())).intValue();
        String serverAddress=communicationNode.groupMngrAddressField.getText();
 		communicationNode.appendMessage("getting time from "+serverAddress+"\n");
 		
        StringIO sio=new StringIO(serverAddress,serverPort,this.getTrace());
        communicationNode.appendMessage("connected to "+serverAddress+"\n");
        try{
        sio.writeString("gettime");
        String stime=(sio.readString()).getHead();
        //
        
        sio.close();
        communicationNode.appendMessage("end of disconnecting.\n");
        eventRecorder.timer.setTime((new Integer(stime)).intValue());
        }
        catch(IOException e){}
    }

    public String getGroupMode()
    {
        this.groupMode=this.settings.getString("groupMode");
        return this.groupMode;
    }

    public void setGroupMode(String x)
    {
        this.groupMode=x;
    }

    
    public void fixNodeConnectionAtChildNode(){
    	System.out.println("fixNodeConnectionAtChildNode");
        this.remainNextDoorRequest=0;
        StringIO sio=this.getConnectionToTheGroupManager();
        if(sio==null){
        	JRadioButton b=this.settings.getSelectedButton();
        	if((b.getText()).equals("P2Pa")){
        		if(this.rClient==null) return;
        		if(!(this.rClient.isRunning())){
        			if(this.rServer!=null){
                       this.rServer.stop();
        			}
        			this.setFixingTree(true);
        			int rt=new Integer(this.settings.getString("MakeBinaryTree-requestRepTerm")).intValue();
        			int wt=new Integer(this.settings.getString("MakeBinaryTree-waitAfterConnect")).intValue();
                    int ap=this.settings.getInt("MakeBinaryTree-ackReceivePort");
            		this.rClient=new InitialRequestClient(
        				this,
        				this.settings.getString("groupName"),
        				this.settings.getString("groupMcastAddr"),
        				this.settings.getInt("groupMcastPort"), rt, wt,ap); 
            		this.rClient.stop();
                    communicationNode.start();
            		this.setFixingTree(false);

        		}
        	}
        }
        else{
          try{
            communicationNode.appendMessage("fixing node.\n");
            sio.writeString("delupper "+this.communicationNode.serialNo);
//            String ack=sio.readString();
            AMessage ack=sio.readString();
            sio.close();
          }
          catch(IOException e){}
        }
    	
    }
    
    
    public String nextDoorControl(String command, Client cl)
    {
        /*
          nextDoor command

          nextDoor requestTime
          nextDoor returnTime <time>
          nextDoor requestState                         //not yet.
          nextDoor returnState <state of this node>     //not yet.
          nextDoor requestNo
          nextDoor returnNo <serial No>
          nextDoor setTime <time>
        */
//        appendMessage("nextDoor "+command+"\n");
//        System.out.println(command);
        String rtn=null;
        StringTokenizer st=new StringTokenizer(command," ");
        String dmy=st.nextToken();
        String cmd=st.nextToken();
        int timeNow=this.eventRecorder.timer.getMilliTime();
        if(cmd.equals("requestTime")){
            int t=timeNow;
            String rcmd="nextDoor returnTime "+t;
            try{
                if(cl!=null)
                  cl.sio.writeString(rcmd);
            }
            catch(IOException e){}
            return rtn;
        }
        else
        if(cmd.equals("returnTime")){
            String t=st.nextToken();
            int tx=(new Integer(t)).intValue();
            int dt=timeNow-communicationNode.lastTime;
            this.eventRecorder.timer.setMilliTime(tx+dt/2);
            this.remainNextDoorRequest--;
            return rtn;
        }
        else
        if(cmd.equals("startAdjustTimer")){
            if(this.clientToUpperNode==null) return null;
            if(!this.clientToUpperNode.isConnected()) {
            	if(!this.isRootNode())
            	this.fixNodeConnectionAtChildNode();
                return null;
            }
            if(this.remainNextDoorRequest>1){
                this.communicationNode.appendMessage("t="+timeNow+" !No response from the upper node("+this.remainNextDoorRequest+" times).\n");
                if(this.remainNextDoorRequest>20)
                this.fixNodeConnectionAtChildNode();
            }
            String scmd="nextDoor requestTime";
            this.communicationNode.lastTime=this.eventRecorder.timer.getMilliTime();
            try{
              this.clientToUpperNode.sio.writeString(scmd);
            }
            catch(IOException e){}
            this.remainNextDoorRequest++;
            return rtn;
        }
        else
        if(cmd.equals("setUpnodeTime")){
            if(this.clientToUpperNode==null) return null;
            if(!this.clientToUpperNode.isConnected()) {
                return null;
            }
            String scmd="nextDoor setTime ";
            this.communicationNode.lastTime=this.eventRecorder.timer.getMilliTime();
            try{
              this.clientToUpperNode.sio.writeString(scmd+this.communicationNode.lastTime);
            }
            catch(IOException e){}
            return rtn;
        }
        else
        if(cmd.equals("setTime")){
            String t=st.nextToken();
            int tx=(new Integer(t)).intValue();
            this.eventRecorder.timer.setMilliTime(tx);
            return rtn;
        }
        else
        if(cmd.equals("requestNo")){
            if(this.clientToUpperNode==null) return null;
            if(!this.clientToUpperNode.isConnected()) {
                return null;
            }
            if(this.remainNextDoorRequest>1){
                this.communicationNode.appendMessage("t="+timeNow+" !No response from the upper node.\n");
            }
            String scmd="nextDoor requestNo";
            try{
              this.clientToUpperNode.sio.writeString(scmd);
            }
            catch(IOException e){}
            rtn="ok";
            return rtn;
        }
        else
        if(cmd.equals("start-requestNo")){
            if(this.clientToUpperNode==null) return null;
            if(!this.clientToUpperNode.isConnected()) {
               return null;
            }
            String scmd="nextDoor requestNo2";
            try{
               this.clientToUpperNode.sio.writeString(scmd);
            }
            catch(IOException e){}
            rtn="ok";
            return rtn;
        }
        else
        if(cmd.equals("requestNo2")){
            if(this.remainNextDoorRequest>1){
                this.communicationNode.appendMessage("t="+timeNow+" !No response from the upper node.\n");
            }
            if(this.clientToUpperNode==null){
//                communicationNode.appendMessage("my No is "+1+"\n");
                this.communicationNode.serialNoField.setText("1");
                this.communicationNode.serialNo=1;            	
            }
            String lr="l";
            if(cl.distributor.getIOAt(1)==cl.sio){
            	lr="l";
            }
            else
            if(cl.distributor.getIOAt(2)==cl.sio){
              	lr="r";
            }            
            String scmd="nextDoor returnNo2 "+this.communicationNode.serialNo+" "+lr;
            try{
            	if(cl!=null)
                  cl.sio.writeString(scmd);
            }
            catch(IOException e){}
            rtn="ok";
            return rtn;
        }
        else
        if(cmd.equals("returnNo")){
            String myNo =st.nextToken();
            communicationNode.appendMessage("my No is "+myNo+"\n");
            this.communicationNode.serialNoField.setText(myNo);
            this.communicationNode.serialNo=(new Integer(myNo)).intValue();
		    rtn="ok";
		    return rtn;
         }
        else
        if(cmd.equals("returnNo2")){
            String upperNodeNumber =st.nextToken();
            String lr=st.nextToken();
            int nx=(new Integer(upperNodeNumber)).intValue();
            int n=nx*2;
            if(!lr.equals("l")){
            	n=n+1;
            }
//            communicationNode.appendMessage("my No is "+n+"\n");
            this.communicationNode.serialNoField.setText(""+n);
            this.communicationNode.serialNo=n;
  		    rtn="ok";
  		    return rtn;
        }
        else
        if(cmd.equals("ex")){
        	System.out.println(command);
        	String subc=command.substring("nextDoor ex".length());
        	String application=st.nextToken();
        	String param=command.substring(("nextDoor ex "+application+" ").length());
        	WaitForTheReturn wftr=new WaitForTheReturn(
        			this.communicationNode,application,param,this.communicationNode,"nop");
//        	ControlledFrame appli=this.communicationNode.lookUp(application);
//        	rtn=appli.parseCommand(param);
        	rtn="ok";
        	return rtn;
        }
        else
        if(cmd.equals("ex-r")){
        	System.out.println(command);
        	String subc=command.substring("nextDoor ex-r ".length());
        	String waitingKey=st.nextToken();
        	String application=st.nextToken();
        	String param=command.substring(("nextDoor ex-r "+waitingKey+" "+application+" ").length());
        	String ulr="u";
        	if(cl.sio==(this.getLinkToUp()).sio){
        		ulr="u";
        	}
        	else
        	if(cl.sio==this.communicationNode.getLeftLink()){
        		ulr="l";
        	}
        	else
        	if(cl.sio==this.communicationNode.getRightLink()){
        		ulr="r";
        	}
        	/*
        	ControlledFrame appli=this.communicationNode.lookUp(application);
        	String xx=appli.parseCommand(param);
        	try{
                cl.sio.writeString("nextDoor return-ex-r "+waitingKey+" "+xx);
        	}
        	catch(Exception e){
        		
        	}
        	*/
        	WaitForTheReturn wftr=new WaitForTheReturn(
        			this.communicationNode,application,param,
        			this.communicationNode,"nextDoor return-ex-r "+ulr+" "+waitingKey);
        	rtn="ok";
        	return rtn;
        }
        else
        if(cmd.equals("return-ex-r")){
        	String subc=command.substring("nextDoor return-ex-r ".length());
        	StringTokenizer xst=new StringTokenizer(subc);
        	String waitingKey=xst.nextToken();
        	String application=xst.nextToken();
        	String param=command.substring(("nextDoor return-ex-r "+waitingKey+" ").length());
        	String dmyx=this.communicationNode.parseCommand("returnFromNext "+waitingKey+" "+param);
        	return rtn;
        }
        else {        	
          rtn="unsupported command";
          return rtn;
        }
     }

    public int getNodeDepth(){
    	int rtn=1;
    	int i=this.communicationNode.serialNo;
    	while(i>1){
    		i=i>>1;
    		rtn++;
    	}
    	return rtn;
    }
    public boolean isConnectedToUpperNode(){
    	if(this.clientToUpperNode==null) return false;
    	return this.clientToUpperNode.isConnected();
    }
    
    public int getRemainNextDoorRequest(){
    	return remainNextDoorRequest;
    }

    public String nodeControl(String command)
    {
        /*
          node control command

          nodeControl redirect <new up node> <new up port>
          nodeControl getTime <returnNodeChannel>
          nodeControl receiveTime <time>
          nodeControl setserial <serial no>

        */
//    	String command=c.getHead();
        communicationNode.appendMessage(command+"\n");
        
        String rtn=null;
        StringTokenizer st=new StringTokenizer(command," ");
        String dmy=st.nextToken();
        String cmd=st.nextToken();
        if(cmd.equals("redirect")){
        	this.communicationNode.stop();
            if(this.clientToUpperNode.isConnected())
        		clientToUpperNode.disconnect();
            String theAddress =st.nextToken();
    		communicationNode.addressOfUpperNode.setText(theAddress);
    		//
    		String thePort=st.nextToken();
    		communicationNode.connectPort.setText(thePort);

            communicationNode.appendMessage("connection redirecting to "
                             +theAddress+":"+thePort+"\n");

	    	int cport=(new Integer(communicationNode.connectPort.getText())).intValue();
	    	//
		    clientToUpperNode.connect(theAddress,cport);
		    this.communicationNode.start();
		    rtn="ok";
		    return rtn;
        }
        if(cmd.equals("discon")){
            String dir=st.nextToken();
            
            if(dir.equals("upper")){
            	if(this.clientToUpperNode.isConnected())
                  this.clientToUpperNode.disconnect();
                return "ok";
            }
            if(dir.equals("left")){
        	  StringIO left=this.distributor.streams[0];
        	  try{
        	   left.close();
        	  }
        	  catch(Exception e){}
        	  this.distributor.remove(0);
        	  return "ok";
            }
            if(dir.equals("right")){
        	  StringIO right=this.distributor.streams[1];
        	  try{
        	   right.close();
        	  }
        	  catch(Exception e){}
        	  this.distributor.remove(1);
        	  return "ok";
           }
        }
        if(cmd.equals("setserial")){
        	String sno=st.nextToken();
        	this.communicationNode.serialNo=(new Integer(sno)).intValue();
        	this.communicationNode.serialNoField.setText(sno);
        	return "ok";
        }
        rtn="unsupported command";
        return rtn;
    }
    public int getAnswer(String x){
    	return this.settings.getAnswer(x);
    }
    public TraceSW getTrace(){
    	return (TraceSW)(this.settings);
    }
    public boolean isTracing(){
    	return this.settings.isTracing();
    }
    public QA getQA(){
    	return this.settings;
    }
    public Client getLinkToUp(){
    	return this.clientToUpperNode;
    }
    public NodeSettings getNodeSettings(){
    	return this.settings;
    }

    InitialRequestClient rClient;
    InitialRequestServer rServer;
    public void setInitialRequestClient(InitialRequestClient c){
    	this.rClient=c;
    }
    public void setInitialRequestserver(InitialRequestServer s){
    	this.rServer=s;
    }
    public boolean isLeftConnected(){
    	if(this.communicationNode==null) return false;
    	StringIO sio=this.communicationNode.getLeftLink();
    	if(sio!=null) return true;
    	return false;
    }
    public boolean isRightConnected(){
    	if(this.communicationNode==null) return false;
    	StringIO sio=this.communicationNode.getRightLink();
    	if(sio!=null) return true;
    	return false;
    }
    public boolean isFixing(){
    	return fixing;
    }
}
