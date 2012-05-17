package nodesystem;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.StringTokenizer;

import nodesystem.applicationmanager.*;
import nodesystem.eventrecorder.*;
import application.autoUpdating.*;//SAKURAGI ADDED
import application.fileTransfer.*;//SAKURAGI ADDED

import controlledparts.*;

public class CommandTranceiver extends java.lang.Object
   implements Runnable
{
	
	boolean stopFlag=false;
	public boolean otherVerRecognitionFlag=false;//SAKURAGI ADDED
	
    public void stop()
    { 
        me=null;
    }

    public void run()
    {
        while(me!=null){
            if(this.receiveEventQueue!=null){
                AMessage command=this.receiveEventQueue.get();
          
                while(this.gui.isGuiLockOccupied("ctr-run")){
                    try{
                        Thread.sleep(1); // 5
                    }
                    catch(InterruptedException e){
                        System.out.println("error:"+e.toString());
                    }
                }
          
                try{
                   this.parseACommand(command);
                }
                catch(Exception e){
                    System.out.println("parse command error:"+e.toString());
                    e.printStackTrace();
                }
                this.gui.releaseGuiLock("ctr-run");
            }
            /*
            try{
                me.sleep(5);
            }
            catch(InterruptedException e){
                System.out.println("CommandTranceiver thread exception");
            }
            */
        }
    }

    public Thread me;

    public void start()
    {
        if(me==null){
            me=new Thread(this,"CommandTranceiver");
            me.start();
//            System.out.println("CommandTranceiver thread priority:"+me.getPriority());
        }
    }

    public MessageQueue receiveEventQueue;

//    public void parseACommandWithComModeForPlay(String ss)
    public void parseACommandWithComModeForPlay(AMessage m)
    {
    	String ss=m.getHead();
        String aCommand="";
        int markLength=endOfACommand.length();
        sendCommand(m);
        int endOfTheFirstCommand=ss.indexOf(endOfACommand);
        if(endOfTheFirstCommand<0) return;
        aCommand=ss.substring(0,endOfTheFirstCommand);
        ss=ss.substring(endOfTheFirstCommand+markLength);
        String x;
        if(aCommand.indexOf("broadcast ")==0){
               x=aCommand.substring("broadcast ".length());
//               parseACommand(x);
               m.setHead(x);
               this.receiveEventQueue.put(m);
        }
        if(aCommand.indexOf("local ")==0){
               x=aCommand.substring("local ".length());
               m.setHead(x);
//               parseACommand(x);
               this.receiveEventQueue.put(m);
        }
   }

    public void parseACommandWithComModeForPlay(String communicationMode, AMessage command)
    {
           if(communicationMode.indexOf("broadcast")==0){
//               parseACommand(command);
               this.receiveEventQueue.put(command);
               return;
           }
           if(communicationMode.indexOf("local")==0){
               return;
           }
           if(communicationMode.indexOf("node ")==0){
//               parseACommand(command);
               this.receiveEventQueue.put(command);
               return;               
           }
    }

//    public void parseACommandWithComMode(String communicationMode, String command)
    public void parseACommandWithComMode(String communicationMode, AMessage command)
    {
           if(command.getHead().indexOf("eventRecorder ")==0){
           }
           else{
              if(recorder!=null){
//                   recorder.recordOtrOp(communicationMode+" "+command);
              }
           }
           parseACommandWithComModeForPlay(communicationMode, command);
    }

//    public void parseACommandForPlay(String s)
    public void parseACommandForPlay(AMessage m)
    {
    	String s=m.getHead();
        String x;
        ControlledFrame obj=null;
        ApplicationID id=null;
        
        int indexOfFirstWord=s.indexOf(" ");
        String firstWord=s.substring(0,indexOfFirstWord+1);
        
        String appliName=gui.applicationManager.getApplicationFromCommand(firstWord);
        if(appliName!=null){
        	id=gui.applicationManager.getRunningApplication(appliName);
        	if(id==null) return;
        	obj=id.application;
        	if(obj==null) return;
        	x=s.substring(firstWord.length());
        	m.setHead(x);
        	obj.receiveEvent(m);
        	return;
        }
        else
        if(s.indexOf("shell ")==0){
            x=s.substring("shell ".length());
            parseShellCommand(x);
            return;
        }
        else
		if(s.indexOf("cnode ")==0){
			// communication Node... gui is the CommunicationNode
			x=s.substring("cnode ".length());
 //           this.gui.toFront();
			this.gui.receiveEvent(x);
			return;
		}
        /*
		if(s.indexOf("avframe ")==0){
			id=gui.applicationManager.getRunningApplication("AVFrame");
			if(id==null) return;
			obj=id.application;
			if(obj==null) return;
			x=s.substring("avframe ".length());
			obj.receiveEvent(x);
			return;
		}
        if(s.indexOf("drawing ")==0){
            id=gui.applicationManager.getRunningApplication("DrawFrame");
            if(id==null) return;
            obj=id.application;
            if(obj==null) return;
            x=s.substring("drawing ".length());
            obj.receiveEvent(x);
            return;
        }
        if(s.indexOf("tedit ")==0){
            id=gui.applicationManager.getRunningApplication("TextEditFrame");
            if(id==null) return;
            obj=id.application;
            if(obj==null)  return;
            x=s.substring("tedit ".length());
            obj.receiveEvent(x);
            return;
         }
        if(s.indexOf("basic ")==0){
            id=gui.applicationManager.getRunningApplication("BasicFrame");
            if(id==null) return;
            obj=id.application;
            if(obj==null)  return;
            x=s.substring("basic ".length());
            obj.receiveEvent(x);
            return;
         }
        if(s.indexOf("web ")==0){
            id=gui.applicationManager.getRunningApplication("WebFrame");
            if(id==null) return;
            obj=id.application;
            if(obj==null) return;
            x=s.substring("web ".length());
            obj.receiveEvent(x);
            return;
         }
        if(s.indexOf("webleap ")==0){
            id=gui.applicationManager.getRunningApplication("WebLEAPFrame");
            if(id==null) return;
            obj=id.application;
            if(obj==null) return;
             x=s.substring("webleap ".length());
            obj.receiveEvent(x);
            return;
         }
		if(s.indexOf("pen ")==0){
			id=gui.applicationManager.getRunningApplication("PEN");
			if(id==null) return;
			obj=id.application;
			if(obj==null) return;
			 x=s.substring("pen ".length());
			obj.receiveEvent(x);
			return;
		 }
        if(s.indexOf("networktesterworker ")==0){
            // networktesterworker <command>
            obj=gui.applicationManager.getRunningApplication("NetworkTesterWorkerFrame").application;
            if(obj==null){
               return;
            }
          
            x=s.substring("networktesterworker ".length());
            obj.receiveEvent(x);
            return;
        }
        if(s.indexOf("networktestermaster ")==0){
            // networktestermaster <command>
            obj=gui.applicationManager.getRunningApplication("NetworkTesterMasterFrame").application;
            if(obj==null){
                    return;
            }

            x=s.substring("networktestermaster ".length());
            obj.receiveEvent(x);
            return;
        }
		if(s.indexOf("ftcontrolworker ")==0){
			 // networktesterworker <command>
			 obj=gui.applicationManager.getRunningApplication("FT-Controller-(worker)").application;
			 if(obj==null){
				return;
			 }
          
			 x=s.substring("ftcontrolworker ".length());
			 obj.receiveEvent(x);
			 return;
		 }
		 if(s.indexOf("ftcontrolmaster ")==0){
			 // networktestermaster <command>
			 obj=gui.applicationManager.getRunningApplication("FT-Controller-(master)").application;
			 if(obj==null){
					 return;
			 }

			 x=s.substring("ftcontrolmaster ".length());
			 obj.receiveEvent(x);
			 return;
		 }
		 if(s.indexOf("RemoteNodeController ")==0){
            // networktestermaster <command>
            obj=gui.applicationManager.getRunningApplication("RemoteNodeController").application;
            if(obj==null){
                return;
            }
          
            x=s.substring("RemoteNodeController ".length());
 //           obj.toFront();
            obj.receiveEvent(x);
            return;
        }
        if(s.indexOf("rcxcontrolworker ")==0){
            // networktesterworker <command>
            obj=gui.applicationManager.getRunningApplication("RcxControlWorkerFrame").application;
            if(obj==null){
//                  obj=gui.applicationManager.spawnApplication2("RcxControlWorkerFrame","remote",gui.encodingCode);
            }
          
            x=s.substring("rcxcontrolworker ".length());
//            obj.toFront();
            obj.receiveEvent(x);
            return;
        }
        if(s.indexOf("rcxcontrolmaster ")==0){
            // networktestermaster <command>
            obj=gui.applicationManager.getRunningApplication("RcxControlMasterFrame").application;
            if(obj==null){
                    return;
            }

            x=s.substring("rcxcontrolmaster ".length());
            obj.toFront();
            obj.receiveEvent(x);
            return;
        }
		if(s.indexOf("transfer ")==0){
			// communication Node... gui is the CommunicationNode
            id=gui.applicationManager.getRunningApplication("FileTransferFrame");
            if(id==null) return;
            obj=id.application;
            if(obj==null)  return;
            x=s.substring("transfer ".length());
            obj.receiveEvent(x);
			return;
		}
        */
        /*
		*/
   }

    public void sendCommandForRecorders(String s)
    {
    	if(this.stopFlag) return;
        if(s.indexOf("broadcast ")==0){
//            distributor.putS(s,-1);
             distributor.putS(s,-1);
        }
        else
        if(s.indexOf("node ")==0){
//            distributor.putS(s,-1);
             distributor.putS(s,-1);
        }
        else{
             gui.messageArea.append("communicationMode error.\n");
        }
    }


    public String encodingCode;

    public EventBuffer ebuff;
//    public void parseCommandsForPlay(String ss)
    public void parseCommandsForPlay(AMessage m)
    {
    	String ss=m.getHead();
        String aCommand="";
        int markLength=endOfACommand.length();
//        sendCommand(ss);
        sendCommand(m);
        while(true){
           int endOfTheFirstCommand=ss.indexOf(endOfACommand);
           if(endOfTheFirstCommand<0) return;
           aCommand=ss.substring(0,endOfTheFirstCommand);
           ss=ss.substring(endOfTheFirstCommand+markLength);
           String x;
           if(aCommand.indexOf("broadcast ")==0){
               x=aCommand.substring("broadcast ".length());
//               parseACommand(x);
               m.setHead(x);
               this.receiveEventQueue.put(m);
           }
           if(aCommand.indexOf("local ")==0){
               x=aCommand.substring("local ".length());
//               parseACommand(x);
               m.setHead(x);
               this.receiveEventQueue.put(m);
           }
        }
   }
    public String endOfACommand;
    public void parseShellCommand(String s)
    {
        boolean btemp=this.recorder.isSendingEvent;
        this.recorder.isSendingEvent=false;
        if(s.indexOf("settime ")==0){
            String x=s.substring("settime ".length());
            int timenow=(new Integer(x)).intValue();
            this.gui.eventRecorder.timer.setTime(timenow);
        }
        else
        if(s.indexOf("chat ")==0){
            String x=s.substring("chat ".length());
            gui.chatOut.append(x);
            gui.chatOut.setCaretPosition(gui.chatOut.getText().length());
        }
        //SAKURAGI ADDED START---
        else
        if(s.indexOf("vercheck ")==0){
            String x=s.substring("vercheck ".length());
            if(!x.equals(CommunicationNode.thisVer) && gui.serialNo==1 && !otherVerRecognitionFlag){
            	//rootノードが別Verの参加を確認した時
            	//フラグを立て、AutoUpdatingFrameを出す
            	otherVerRecognitionFlag=true;
            	AutoUpdatingFrame auf=new AutoUpdatingFrame(gui,this);
            }
        }
        else
        if(s.indexOf("verup ")==0){
        	String x=s.substring("verup ".length());
        	FileTransferFrame ft=(FileTransferFrame)gui.applicationManager.getRunningApplication("FileTransferFrame").application;
        	ft.setVisible(false);
        	ft.copyToFileNameField.setText(System.getProperty("user.dir")+"\\tmp\\dsr_"+x+".zip");
        	ft.updateFlugChk();
        }
        else
        if(s.indexOf("endVerup")==0){
            	FileTransferFrame ft=(FileTransferFrame)gui.applicationManager.getRunningApplication("FileTransferFrame").application;
            	ft.setVisible(false);
            	ft.endVerUp();
        }
       
        
        //SAKURAGI ADDED END-----
        else
        if(s.indexOf("spawn ")==0){
            int startTime=this.recorder.timer.getMilliTime();
            String x=s.substring("spawn ".length());
            StringTokenizer st=new StringTokenizer(x," ");
            String application=st.nextToken();
            String mode=st.nextToken();
//            this.gui.setControlMode(mode);
//            gui.applicationManager.spawnApplication(application,mode,gui.encodingCode);
            if(gui.isReceiving()) {
              gui.applicationManager.spawnApplication(application,mode);
              int endTime=this.recorder.timer.getMilliTime();
              this.recorder.recordMessage(""+startTime+",\"spawn "+application+"\","+(endTime-startTime));
            }
        }
        else
        if(s.indexOf("settime ")==0){
            String x=s.substring("settime ".length());
            recorder.timer.setTime((new Integer(x)).intValue());
            recorder.recordMessage(""+"0,\"abstime="+recorder.timer.absoluteTime()+"\"");
            
        }
        else
        if(s.indexOf("updatemessages")==0){
            recorder.mes.update();
           
        }
        else
        if(s.indexOf("log ")==0){
            BufferedReader dinstream=null;
            String x=s.substring("log ".length());
//            DataInputStream dinstream=new DataInputStream(new StringBufferInputStream(x));
            try{
              dinstream=new BufferedReader(
//              new InputStreamReader(new StringBufferInputStream(x),encodingCode)
                new StringReader(x)
              );
            }
//            catch(UnsupportedEncodingException e){
            catch(Exception e){
                System.out.println("exception:"+e);
            }
            if(dinstream==null) return;
            InputQueue iq=new InputQueue(dinstream);
            ParseLogEvent evParser=new ParseLogEvent(this.recorder,iq);
            evParser.run();
            
        }
        else
        if(s.indexOf("getnodeID ")==0){
            String returnNode=s.substring("getnodID ".length());
            String myaddress=gui.myAddressField.getText();
            StringTokenizer st=new StringTokenizer(myaddress,"/");
            String theAddress=st.nextToken();
            String sending="node "+returnNode+"shell dnode "+gui.serialNo+" "
            +gui.uName+" "+theAddress+this.endOfACommand;
            AMessage m=new AMessage();
            m.setHead(sending);
            this.ebuff.putS(m);
        }
        else
        if(s.indexOf("dnode ")==0){
            String node=s.substring("dnode ".length());
            gui.messageArea.append("node "+node+" is connected\n");
        }
        else
        if(s.indexOf("setControlMode ")==0){
            String mode=s.substring("setControlMode ".length());
//            gui.applicationManager.changeAllControlMode(mode);
            gui.setControlMode(mode);
            gui.messageArea.append("control mode changed to "+mode+".\n");
//            gui.controlModeLabel.setText(mode);
//            gui.controlMode=mode;
        }
        else
        if(s.indexOf("setRecordMode ")==0){
            String mode=s.substring("setRecordMode ".length());
            gui.eventRecorder.changeControlMode(mode);
            gui.messageArea.append("record control mode changed to "+mode+".\n");
        }
        else
        if(s.indexOf("getNodeInfo ")==0){
            /*
              receive:
                getNodeInfo <returnNode> <receiveApplication>
                    
              send:
                node <returnNode> <receiveApplication> nodeInfo <thisNodeID> <userName> <address>
            */
            String args=s.substring("getNodeInfo ".length());
            StringTokenizer st=new StringTokenizer(args);
            String returnNode=st.nextToken();
            String receiveApplication=st.nextToken();
            String myaddress=gui.myAddressField.getText();
            StringTokenizer st2=new StringTokenizer(myaddress,"/");
            String dmy=st2.nextToken();
            String theAddress=st2.nextToken();
            gui.messageArea.append("returning this node info.\n");
            String sending="node "+returnNode+ " "+ receiveApplication
            +" nodeInfo "+gui.serialNo+" "+gui.uName+" "+theAddress+
            this.endOfACommand;
            AMessage m=new AMessage();
            m.setHead(sending);
            this.ebuff.putS(m);
        }
        else
        
        /* 
          codes for mutual exclusion 
          See also CommunicationNode.getLockOfTheNode(n)
                   CommunicationNode.unLockTheNode(n);
        
        */
        
        if(s.indexOf("getLock ")==0){
            /*
            
              mutual exclusion/          
              this code is executed at the teacher node (node 1).
              this node broadcast the "cs node" information to all nodes.
            
              receive:
                getLock <lockNodeID> 
                    
              send:
                broadcast shell setLock <lockNodeID>
            */
//            System.out.println(s);
            String args=s.substring("getLock ".length());
            StringTokenizer st=new StringTokenizer(args);
            String id=st.nextToken();
             
            if(this.gui.lockNodeID==0){
                this.gui.lockNodeID=(new Integer(id)).intValue();
                this.gui.opNodeField.setText(id);
                String sending="broadcast shell setLock "+id
                               +this.endOfACommand;
                AMessage m=new AMessage();
                m.setHead(sending);
                this.ebuff.putS(m);

            }
            
        }
        else
        if(s.indexOf("setLock ")==0){
            /*
            
              mutual exclusion/
              receive the "cs node" information.
              <lockNodeID>==0 means release the cs.
              
              receive:
                setLock <lockNodeID>
                    
              send:
             
            */
//            System.out.println(s);
            String args=s.substring("setLock ".length());
            StringTokenizer st=new StringTokenizer(args);
            String id=st.nextToken();
            this.gui.lockNodeID=(new Integer(id)).intValue();
            this.gui.opNodeField.setText(id);
            if(this.gui.lockNodeID==this.gui.serialNo){
                // this node is entering the critical section(cs).
                  this.gui.lockTimer=5;
                  this.gui.applicationManager.setEditable(true);
            }            
//
//          the following is the codes for measuring the time to the critical section.
//
            if(this.gui.lockNodeID==this.gui.serialNo){
//                int x=this.gui.eventRecorder.timer.getMilliTime();
//                System.out.println("to cs:"+(x-gui.lastLockTime));
//                System.out.println("enterd the cs at "+x);
            }
       }
        else
        if(s.indexOf("lockTo ")==0){
            /*
            
              mutual exclusion/
              release the critical section.
            
              receive:
                lockTo <newLockNodeID>
                    
              send:
                broadcast shell setLock <newLockNodeID>
 
            */
//            System.out.println(s);
            String args=s.substring("lockTo ".length());
            StringTokenizer st=new StringTokenizer(args);
            String lockNodeID=st.nextToken();
            
            this.gui.lockNodeID=(new Integer(lockNodeID)).intValue();
            this.gui.opNodeField.setText(lockNodeID);
       
            String sending="broadcast shell setLock "+lockNodeID
                               +this.endOfACommand;
            /*
            String sending="node "+lockNodeID+" getLock "+lockNodeID
                               +this.endOfACommand;
            */
            AMessage m = new AMessage();
            m.setHead(sending);
            this.ebuff.putS(m);
            
        }
        else
        if(s.indexOf("reset")==0){
        	this.resetProcedure();
        }
        else
        if(s.startsWith("setFixing ")){
        	String onoff=s.substring("setFixing ".length());
        	if(onoff.startsWith("on")){
        		this.gui.setFixing(true);
        	}
        	else
        	if(onoff.startsWith("off")){
        		this.gui.setFixing(false);
        	}
        }
        /*
        if(s.indexOf("heatbeat")==0){
            this.gui.blinkHeatBeatLamp();
        }
        */
        this.recorder.isSendingEvent=btemp;
    }
    public void resetProcedure(){
    	//stop send/receive
    	//disconnect from the group
//    	this.gui.leaveGroupButton_actionPerformed(null);
    	//if i am the teacher, 
    	if(this.gui.isRoot()){
           	//   reset the group manager
         	this.stopFlag=true;
        	this.stop();
        	this.gui.stop();
        	try{ Thread.sleep(500);}catch(InterruptedException e){}
    		this.gui.nodeController.resetGroupManager();
        	this.stopFlag=false;
        	this.gui.nodeController.initialize();
    	//   wait little bit
        	try{ Thread.sleep(100);}catch(InterruptedException e){}
        	this.start();
    	//   if the group manager is initialized, 
    	//       connect to the group manager
    		this.gui.joinButton_actionPerformed(null);
    	}
    	else
    	//if i am the student
    	{
         	this.stopFlag=true;
        	this.stop();
        	this.gui.stop();
        	this.gui.nodeController.initialize();
    	//   wait much more longer than teacher,
        	try{ Thread.sleep(2000);}catch(InterruptedException e){}
        	this.start();
    	//   connect to the group manager
        	this.stopFlag=false;
        	this.gui.releaseGuiLock("ctr-run");
    		this.gui.joinButton_actionPerformed(null);
    	}
    	//
    	this.gui.start();
    }
    
//    public void parseACommand(String s)
    public void parseACommand(AMessage m)
    {
    	String s=m.getHead();
         String x;
       //
        if(s.indexOf("eventRecorder ")==0){
            // networktestermaster <command>
            if(this.gui.eventRecorder==null)  return;
            x=s.substring("eventRecorder ".length());
            this.gui.eventRecorder.receiveEvent(x);
            return;
        }
        //
        this.gui.eventRecorder.recordOtrOp(m);
        this.parseACommandForPlay(m);
        
    }
    public void comment()
    {
        /*

             <message>=<broadcastmessage> + <localmessage> 
                                          + <nodemessage>;

             <broadcastmessage>= 'broadcast '<command> ;

             <localmessage>= 'local '<command> ;
             
             <nodemessage>='node '<nodeName>' '<command> ;

             <command>=   <application><operation>
                        + 'shell '<shellCommand>;

             <application>=
                             'draw '
                           + 'basic '
                           + 'editor '
                           + 'networktester ';

             <id>= <number> ;

             <shellCommand>= 'chat '<chatmessage>
                           + 'spawn '<application>
                           + 'kill '<application>
                           + 'ps ';

        */
    }
    public EventRecorder recorder;

//    public void parseCommands(String ss)
    public void parseCommands(AMessage m)
    {
/*
        if(recorder!=null){
              recorder.recordOtrOp(ss);
        }
 */
        int markLength=endOfACommand.length();
        String ss=m.getHead();
        while(true){
          int endOfTheFirstCommand=ss.indexOf(endOfACommand);
           if(endOfTheFirstCommand<0) return;
           String s=ss.substring(0,endOfTheFirstCommand);
           ss=ss.substring(endOfTheFirstCommand+markLength);
           String x;
           if(s.indexOf("broadcast ")==0){
               x=s.substring("broadcast ".length());
//               parseACommand(x);
               m.setHead(x);
               parseACommandWithComMode("broadcast",m);
           }
           if(s.indexOf("local ")==0){
               x=s.substring("local ".length());
               m.setHead(x);
               parseACommandWithComMode("local",m);
           }
           if(s.indexOf("node ")==0){
               x=s.substring("node ".length());
               String nodeName=""+this.gui.serialNo+" ";
               if(x.indexOf(nodeName)==0){
                  x=x.substring(nodeName.length());
                  m.setHead(x);
//                  parseACommand(x);
                  parseACommandWithComMode("node "+this.gui.serialNo,m);
               };
               
           }
        }
    }
//    public void sendCommand(String s)
    public void sendCommand(AMessage s)
    {
    	if(this.stopFlag) return;
        if(s.equals("")) return;
        if(s.getHead().indexOf("broadcast ")==0){
             distributor.putS(s,-1);
        }
        else
        if(s.getHead().indexOf("node ")==0){
             distributor.putS(s,-1);
        }
        else{
             gui.messageArea.append("communicationMode error.\n");
        }
    }
    public CommunicationNode gui;
    public Distributor distributor;
    public CommandTranceiver(CommunicationNode gui,Distributor distributor,
            EventRecorder or, String code, MessageQueue q)
    {
        this.distributor=distributor;
        this.gui=gui;
        this.recorder=or;
        this.encodingCode=code;
        this.endOfACommand=";;;\0\n";
        this.ebuff=new EventBuffer(this);
//        this.ebuff.start();
        this.receiveEventQueue=q;
        me=null;
        this.start();
    }
}

