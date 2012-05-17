/*
		A basic implementation of the JFrame class.
*/
package nodesystem;
 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import nodesystem.applicationmanager.*;
import nodesystem.eventrecorder.*;

import application.draw.NetworkReader;
import controlledparts.*;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;

import util.TraceSW;
/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/ 
public class CommunicationNode extends 
// javax.swing.JFrame 
       ControlledFrame
implements DialogListener, FrameWithControlledButton, java.lang.Runnable, MessageGui 
 
{ 
    public int lastLockTime;
	public ResourceBundle resourceBundle;
    public File mesDataRoot;
    public File logDataRoot;
    public File nodeDataRoot;
    public File commonDataDir;
    public File dsrRoot;
    public int serialNo;
    public InterNodeController nodeController;
    public String tempText;
    public String encodingCode;
    public String thisID;
    public File commonDataImageDir;
    public File commonDataFigDir;
    public File userDataDir;
    
    public static String thisVer="0701231";	//SAKURAGI ADDED
    public static boolean debugFlug=false;	//SAKURAGI ADDED
    public LogControl logControl;				//SAKURAGI ADDED
    Thread me; 
    TraceSW trace;
    QA qa;
	Hashtable returnMessage;

    boolean guiIsOccupiedFlag;

    public void releaseGuiLock(String caller)
    {
        this.guiIsOccupiedFlag=false;
//        System.out.println("releaseGuiLock:false-"+caller);
    }

    public boolean isGuiLockOccupied(String caller)
    {
//        System.out.println("isGuiLockOccupied-");
        if(this.guiIsOccupiedFlag) {
//            notifyAll();
//            System.out.println("guiLock:true-"+caller);
            return true;
        }
        else{
            guiIsOccupiedFlag=true;
//            notifyAll();
//            System.out.println("guiLock:false->true-"+caller);
            return false;
        }
    }

//    public void sendEventFromPlayer(String s)
    public void sendEventFromPlayer(AMessage m)
    {
    	String s=m.getHead();
    	AMessage mx=new AMessage();
    	mx.setData(m.getData(),m.getDataLength());
        if(this.commandTranceiver==null) return;
        String sending=communicationMode+" "+s+commandTranceiver.endOfACommand;
        mx.setHead(sending);
        /*
        if(this.eventRecorder!=null){
            eventRecorder.recordMyOp(sending);
        }
        */
        if(sendEventFlag) {
           if(ebuff!=null){
                ebuff.putS(mx);
           }
        }
        commandTranceiver.receiveEventQueue.put(m);
    }

    public String className;

    void setGroupMngrAddressPort(String a, String p)
    {
        this.groupMngrAddressField.setText(a);
        this.groupMngrPortField.setText(p);
    }

//    NodeSettings settings;

    public void setControlModeLabel(String x)
    {
        this.controlModeLabel.setText(this.getLclTxt(x));
    }

    public String getMyAddress()
    {
        return this.myAddressField.getText();
    }

    public boolean isDirectOperation()
    {
        return this.directEvent;
    }

    public void setEncodingCode(String code)
    {
        this.encodingCode=code;
    }

    public String getEncodingCode()
    {
        return this.encodingCode;
    }

    public void updateTime(int t)
    {
        if(this.isGuiLockOccupied("cn-updateTime")) return;
        timeField.setText(""+t);
        this.releaseGuiLock("cn-updateTime");
//        this.eventRecorder.timeField.setText(""+t*1000);
    }

    public String getSeparator(){
        return ""+System.getProperty("file.separator");    	
    }
    
    public String getIconPlace(){
    	String url="";
    	if(this.isApplet()){
    		url=this.appletBaseDir+"images/";
    		if(url.startsWith("/")){
    	           String urlHead="file:";
    	           String separator=this.getSeparator();
    	           if(separator.equals("\\"))
    	                urlHead=urlHead+"///";
    	            else
    	                urlHead=urlHead+"//";
    	           url=urlHead+this.dsrRoot.getPath()+separator+"images"+separator;    			
    		}
    	}
    	else{
           String urlHead="file:";
           String separator=this.getSeparator();
           if(separator.equals("\\"))
                urlHead=urlHead+"///";
            else
                urlHead=urlHead+"//";
           url=urlHead+this.dsrRoot.getPath()+separator+"images"+separator;
    	}
        return url;    	
    }
    
    public boolean isFocusGained()
    {
        return this.focusGained;
    }

    boolean focusGained;

    public int remainNextDoorRequest;

    public void blinkHeartBeatLamp(int tx)
    {
        if(this.isGuiLockOccupied("cn-blink-lamp")) return;
        Color cc=this.heartBeatButton.getBackground();
               if((tx/20)%2==0)
               {
//                  this.blinkHeartBeatLamp();
                   if(cc!=Color.green)
                   this.heartBeatButton.setBackground(Color.green);
               }
               else
               {
                   if(cc!=Color.red)                
                   this.heartBeatButton.setBackground(Color.red);
               }
        this.releaseGuiLock("cn-blink-lamp");
    }

    public boolean isReadableFromEachFile()
    {
        return this.isReadingDirect;        
    }

    public boolean isKeepingCriticalSection;

    public boolean isReadingDirect;

    public int lastTime;

    public boolean isReceiving(){
    	if(this.eventRecorder!=null)
    	if(this.eventRecorder.isPlayAllChecked()){
//    		if(this.eventRecorder.isPlaying())
    			return true;
    	}
    	return this.isReceivingEvents;
    }

    public boolean isSending()
    {
        return this.sendEventFlag;
    }

    public void recordMessage(String s)
    {
    	if(this.eventRecorder!=null)
        eventRecorder.recordMessage("\"CommunicationNode\","+s);
    }

    public File getDefaultPath()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return null;
    }

    public Vector getDialogs()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return null;
    }

    public boolean isShowingRmouse()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return this.rmouseIsActivated;
    }

    public void sendFileDialogMessage(String m)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

    public void whenActionButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

    public void whenCancelButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

    public int lockNodeID;

    int nodeWithTheLock;

    public int lockTimerMax;

    public int lockTimer;

 
    public int theNodeWithThisLock;

    
    public synchronized boolean lockIsAcquired(int n)
    {
        String b=(String)(this.remoteLock.get(""+n));
        if(b==null) return false;
        if(b.equals("0")) return true;
        else return false;
    }

    public boolean controlIsLocked;

    public Hashtable remoteLock;

    public void unLockTheNode(int n)
    {
        if(this.serialNo==0) return;
        if(this.isKeepingCriticalSection) return;
        this.opNodeField.setText("0");
        this.lockNodeID=0;
        this.applicationManager.setEditable(false);
        AMessage m=new AMessage();
        if(this.serialNo==n){
            String sending=
                "broadcast shell setLock 0"+this.commandTranceiver.endOfACommand;
            m.setHead(sending);
            this.ebuff.putS(m);
        }
        else{
             String sending=
                "node "+n+" shell lockTo 0"+this.commandTranceiver.endOfACommand;
             m.setHead(sending);
            this.ebuff.putS(m);
        }
    }

    
    public boolean getLockOfTheNode(int n)
    {
        /*
           mutual exclusion
        
        
           Lock procedure for the group communication.
           This lock provides the gurantee that the exclusive operation
           of one node at a time for common operation of the group.
           
           - the primary node (usually, node 1) holds the primary lock.
             this node control locks of other nodes.
             If one node (user node) would like to have the exclusive operation,
             the node looks up its lock status. if the lock status
             indicats that there is no other node which have the lock,
             it request the lock to the primary node.
             If there is no other locks (exclusive operation rights),
             the primary node inform the ID of the user node to all
             nodes in the group. at each node, the node keeps the operating
             node ID as its lock status.
             
             int lockNodeID
                 lockNodeID==0 : this group is not locked by any node.
                 lockNodeID>0  : this group is locked by the node of lockNodeID
                 
             n: lock Manager nodes's ID (teacher's node)
             
             
             See also the codes in CommandTranceiver.parseShellCommand()
        
        */
//        System.out.println("getLockOfTheNode("+n+")");
        if(lockNodeID==this.serialNo){   
            //this node already has the right of operation broadcastiong.
            //  extends the right to have the lock. 
            //this.lockTimer=8;
        	this.setLockTimer(8);
            return true;
        }
        if(lockNodeID==0){
            //This group is not operated by any node in this group.
//            System.out.println("try to enter the cs at "+this.eventRecorder.timer.getMilliTime());
        	if(this.eventRecorder!=null)
            this.lastLockTime=this.eventRecorder.timer.getMilliTime();
            
            if(n==this.serialNo){  // if I am the teacher...
                String sending="broadcast shell setLock "+this.serialNo
                               +this.commandTranceiver.endOfACommand;
                AMessage m=new AMessage();
                m.setHead(sending);
                this.ebuff.putS(m);
//                this.lockTimer=5;
                this.setLockTimer(5);
                this.lockNodeID=this.serialNo;
                this.opNodeField.setText(""+this.serialNo);
                this.applicationManager.setEditable(true);
            }
            else
            {  // if I am not the teacher, request the lock to the teacher node.
//                this.lockTimer=5;
                String sending="node "+n+" shell getLock "+this.serialNo
//                               +" shell"+this.commandTranceiver.endOfACommand;
                                 +this.commandTranceiver.endOfACommand;
                AMessage m=new AMessage();
                m.setHead(sending);
                this.ebuff.putS(m);
                /*
                this.lockNodeID=this.serialNo;
                this.opNodeField.setText(""+this.serialNo);
                */
             }
//             this.lockTimer=5;
             return true;
        }
        //
        // if(lockNodeID!=serialNo && lockNodeID!=0)
        //
        //   this node has no right to execute the critical section,
        //      if I'm not the teacher(node 1).
        //   If i'm the teacher, change the critical section(cs) node to this node.
        //
        if(this.serialNo==n){
            // change the operation node(critical section)
            // from the "lockNodeID" node to this node.
            // this node is the teacher node. so this has the right to change the operation node.
//            this.sendEventFlag=false;
            String sending="node "+this.lockNodeID
                              + " shell lockTo " + this.serialNo
                              + this.commandTranceiver.endOfACommand;
            AMessage m=new AMessage();
            m.setHead(sending);
            this.ebuff.putS(m);
            return false;
        }
        return false;
        
    }

    public NodeSettings getNodeSettings(){
    	return this.nodeController.getNodeSettings();
    }
    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledButton
        // to do: code goes here
        String mode=this.getControlMode();
        if(mode==null) {
//        	System.out.println("true @ isControlledByLocalUser()");
        	return true;
        }
        if(mode.equals("common")){
            if(this.serialNo==0) { // local operation, independent
//            	System.out.println("true @ isControlledByLocalUser()");
            	return true;
            }
            if(this.getLockOfTheNode(1)) {
                this.sendEventFlag=true;
//                this.isReceivingEvents=false;
//            	System.out.println("true @ isControlledByLocalUser()");
                return true;
            }
            else {
                this.sendEventFlag=false;
//                this.isReceivingEvents=true;
//            	System.out.println("false @ isControlledByLocalUser()");
                return false; 
            }
        }
        if(mode.equals("withReflector")){
//        	System.out.println("true @ isControlledByLocalUser()");
                return true;
        }
//    	System.out.println(""+(directEvent||echoback)+"@ isControlledByLocalUser()");
        return this.directEvent||this.echoback;
    }

    public void sendEvent(String x)
    {
        this.sendThisEvent(x);
    }
    public void sendEvent(AMessage m){
    	
    }

    public ControlledFrame lookUp(String x)
    {
       ControlledFrame frame=null;
       if(x.equals("node")){
    	   return this.communicationNode;
       }
       ApplicationID id=this.applicationManager.getRunningApplication(x);
       if(id==null){
           frame=(ControlledFrame)(this.applicationManager.spawnApplication2(
           x, this.controlMode));
       }
       else frame=(ControlledFrame)(id.application);
       return frame;
    }

    public String call(String command)
    {
         String appliName=command.substring(0,command.indexOf(" "));
         String arg=command.substring((appliName+" ").length());
         ControlledFrame appli=this.lookUp(appliName);
         String rtn=appli.parseCommand(arg);
         return rtn;
    }

    public void receiveEvent(String s)
    {
//        if(!this.isReceivingEvents) return;
    	if(!this.isReceiving()) return;
        InputQueue iq=null;
        try{
           BufferedReader dinstream=new BufferedReader(
//              new InputStreamReader(
//              new StringBufferInputStream(s),encodingCode)
                new StringReader(s)
           );
           iq=new InputQueue(dinstream);
        }
//        catch(UnsupportedEncodingException e){
        catch(Exception e){
            System.out.println("exception:"+e);
            return;
        }
        if(iq==null) return;
            ParseNodeEvent evParser=new ParseNodeEvent(this,iq);
//            if(this.isGuiLockOccupied()){
               evParser.run();
//               this.releaseGuiLock();
//            }
    }
    public void receiveEvent(AMessage m){
    	String s=m.getHead();
    	if(this.trace.isTracing()){
    		System.out.println("executing-"+s);
    	}
        this.receiveEvent(s);
    }

    public ApplicationsFrame applicationsSelector;
    private JLabel solarcatsicon;
    private JLabel JLabel1;

    public Vector buttons;

    public void sendThisEvent(String x) //synchronized
    {
        sendEvent("cnode",x);
    }

    public void clickButton(int i)
    {
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        b.click();
        this.mouseClickedAtButton(i);
     }

    public void focusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            ControlledButton button=(ControlledButton)(buttons.elementAt(i));
            button.focus();
    }

    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        sendThisEvent("btn.click("+i+")\n");
        if(i==this.spawnButton.getID()){    // spawnButton
//            if(!this.getControlMode().equals("receive"))
             if(this.isDirectOperation())
               this.spawnApplication(this.applicationButton.getActionCommand());
        }
        if(i==this.applicationButton.getID()){    //applicationButton
//            if(!this.getControlMode().equals("receive"))
               this.applicationsSelector.show();
        }
        if(i==this.showRecorderButton.getID()){    //showRecorderButton
        	if(this.eventRecorder!=null){
              if( this.getControlMode().equals("receive") &&
                (! this.eventRecorder.controlMode.equals("remote")) ) return;
              else
                this.eventRecorder.show();
        	}
        }
    }

    public void mouseEnteredAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        sendThisEvent("btn.enter("+i+")\n");
    }

    public void mouseExitedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        sendThisEvent("btn.exit("+i+")\n");
    }

    public void unfocusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//            button.controlledButton_mouseExited(null);
        button.unFocus();
    }

    public MessageQueue receiveEventQueue;

    public void sendEventForRecorder(String eventString)
    {
        /*
        String sending=communicationMode+" eventRecorder "+eventString
                        +this.commandTranceiver.endOfACommand;
       if(this.eventRecorder.sendEventFlag)
          commandTranceiver.sendCommandForRecorders(sending);
          */
    }

    public String getPathSeparator()
    {
	    return ""+System.getProperty("file.separator");
    }

    public String getUserDirPath()
    {
               String url="";
//       		   String fileName=d.getText();
    		   File path=userDataDir;
               if(!path.exists()) path.mkdir();
	    	   String separator=""+System.getProperty("file.separator");
               if(separator.equals("\\"))
            	   url="file:///"+path.getPath();
               else
                   url="file://"+path.getPath();
        return url;
    }

    public String getCommonDirPath()
    {
               String url="";
//       		   String fileName=d.getText();
    		   File path=commonDataFigDir;
               String separator=""+System.getProperty("file.separator");
               if(separator.equals("\\"))
            	   url="file:///"+path.getPath();
               else
                   url="file://"+path.getPath();
        return url;
    }

    public void spawnRemote(String application)
    {
              String remoteControlMode="remote";
//              String remoteControlMode="receive";
              if(getControlMode().equals("teach"))
                       remoteControlMode="receive";
              String sending="spawn "
              + application + " "
              + remoteControlMode;
//              + commandTranceiver.endOfACommand;
              this.sendEvent("shell",sending);
    }
    public EventBuffer ebuff;

    public  // synchronized
//    void sendEvent(String application, String eventString)
    void sendEvent(String application, AMessage event)
    {
        if(this.commandTranceiver==null) return;
        String eventString=event.getHead();
        String command=application+" "+eventString;
        String sending=communicationMode+" "+command
                        +this.commandTranceiver.endOfACommand;
//        System.out.println(sending);
        AMessage cx=new AMessage();
        cx.setHead(command);
        cx.setTime(this.eventRecorder.timer.getTime());
        cx.setData(event.getData());
        cx.setTime(this.getTimeNow());
        event.setHead(sending);
        if(this.eventRecorder!=null){
            eventRecorder.recordMyOp(cx);
        }
                
        if(!sendEventFlag) return;
        
        if(ebuff!=null){
             ebuff.putS(event);
        }
    }
    public void sendEvent(String application, String event){
    	AMessage m=new AMessage();
    	m.setHead(event);
    	sendEvent(application,m);
    }

    public boolean rmouseIsActivated;

    public boolean isReceivingEvents;

    public boolean directEvent;

    public boolean echoback;

    public boolean sendEventFlag;

    public String communicationMode;

/*
      setControlMode
         
         setting remote control mode
         mode: "local"  ... local execution, without remote controlled
               "broadcast" ... remote control, sendevent
               "teach"  ... remote control, sendevent, broadcast image.
               "remote" ... remote controlled. (load image directry from the source).
               "receive"... remote controlled, receive broadcasted image
               "common" ... common operation. receive envent and send event
               "withReflector" ... common operation, send event without operation,
                                   receive event.
               "withReflectorNoEcho" ... common operation, send event with operation,
                                   receive event. reflector does not echoback.
               
 */
    public void setControlMode(String mode)
    {
 		this.controlModeLabel.setText(this.getLclTxt(mode));
       if(mode.equals("local")){
            this.controlMode="local";
//            this.communicationMode="broadcast";
            this.communicationMode="local";
            this.isReceivingEvents=false;
            this.sendEventFlag=false;
            this.echoback=true;
            this.directEvent=true;
            this.rmouseIsActivated=false;
            this.isReadingDirect=true;
            this.opNodeField.setText("0");
            this.applicationManager.setEditable(true);
            return;
        }
        if(mode.equals("broadcast")){
            this.controlMode="broadcast";
            this.communicationMode="broadcast";
            this.sendEventFlag=true;
            this.echoback=true;
            this.directEvent=true;
            this.isReceivingEvents=false;
            this.rmouseIsActivated=false;
            this.opNodeField.setText("1");
            this.isReadingDirect=true;
            this.applicationManager.setEditable(true);
            return;
        }
        if(mode.equals("teach")){
            this.controlMode="teach";
            this.communicationMode="broadcast";
            this.sendEventFlag=true;
            this.echoback=true;
            this.directEvent=true;
            this.isReceivingEvents=false;
            this.rmouseIsActivated=false;
            this.isReadingDirect=true;
            this.opNodeField.setText("1");
            this.applicationManager.setEditable(true);
            return;
        }
        if(mode.equals("remote")){
            this.controlMode="remote";
            this.communicationMode="broadcast";
            this.sendEventFlag=false;
            this.echoback=false;
            this.directEvent=false;
            this.rmouseIsActivated=true;
            this.isReceivingEvents=true;
            this.isReadingDirect=true;
            this.opNodeField.setText("1");
            this.applicationManager.setEditable(false);
            return;
        }
        if(mode.equals("receive")){
            this.controlMode="receive";
            this.communicationMode="broadcast";
            this.sendEventFlag=false;
            this.echoback=false;
            this.directEvent=false;
            this.rmouseIsActivated=true;
            this.isReceivingEvents=true;
            this.isReadingDirect=false;
            this.opNodeField.setText("1");
            this.applicationManager.setEditable(false);
            return;
        }
        if(mode.equals("common")){
            this.controlMode="common";
            this.communicationMode="broadcast";
            this.sendEventFlag=true;
            this.echoback=true;
            this.directEvent=true;
            this.rmouseIsActivated=true;
            this.isReceivingEvents=true;
            this.isReadingDirect=false;
            this.opNodeField.setText("0");
            return;
        }
        if(mode.equals("withReflector")){
            this.controlMode="withReflector";
            this.communicationMode="broadcast";
            this.sendEventFlag=true;
            this.echoback=true;
            this.directEvent=false;
            this.rmouseIsActivated=true;
            this.isReceivingEvents=true;
            this.isReadingDirect=true;
//            this.opNodeField.setText("1");
            return;
        }
        /*
        if(mode.equals("withReflectorNoEcho")){
            this.controlMode="common";
            this.communicationMode="broadcast";
            this.sendEventFlag=true;
            this.echoback=true;
            this.directEvent=true;
            this.rmouseIsActivated=true;
            this.isReceivingEvents=true;
            this.isReadingDirect=true;
            this.opNodeField.setText("1");
            return;
        }
        */
    }

    public String getControlMode()
    {
        return this.controlMode;
    }
    
    public String getCommunicationMode(){
    	return this.communicationMode;
    }

    String controlMode;

    boolean isJoined;

    public void spawnApplication(String a)
    {
    	int startTime=0;
    	if(this.eventRecorder!=null)
         startTime=this.eventRecorder.timer.getMilliTime();
        String application;
        if(a.indexOf("*")==0)
             application=a.substring(1);
        else application=a;
        if(application.equals("NetworkTesterWorkerFrame")){
           applicationManager.spawnApplication(application,"remote");
        }
        else
        if(application.equals("NetworkTesterMasterFrame")){
           applicationManager.spawnApplication(application,"local");
        }
        else
        if(application.equals("RemoteNodeController")){
           applicationManager.spawnApplication(application,"local");
        }
        else
        if(application.equals("FT-Controller-(master)")){
        	applicationManager.spawnApplication(application,"local");
        }
        else
        if(application.equals("FT-Controller-(worker)")){
        	applicationManager.spawnApplication(application,"remote");
        }
        else{
            spawnRemote(application);   
            applicationManager.spawnApplication(application,this.getControlMode());
       }
        int endTime=0;
        if(this.eventRecorder!=null)
         endTime=this.eventRecorder.timer.getMilliTime();
        int readingtime=endTime-startTime;
        String readingSpeed="";
        /*
        if(readingtime>0){
            readingSpeed=""+10.0*((double)(tlength))/readingtime;
        }
        else readingSpeed="(na)";
        */
        this.recordMessage(""+startTime+
        ",\"spawn("+a+") \""+","+readingtime+
        ", ,");
    }
/*
    public void setChoiceBox()
    {
		applicationChoiceBox.addItem("DrawFrame");
		applicationChoiceBox.addItem("BasicFrame");
		applicationChoiceBox.addItem("WebFrame");
		applicationChoiceBox.addItem("WebLEAPFrame");
		applicationChoiceBox.addItem("NetworkTesterWorkerFrame");		
		applicationChoiceBox.addItem("NetworkTesterMasterFrame");
		applicationChoiceBox.addItem("RemoteNodeController");
		applicationChoiceBox.addItem("RcxControlWorkerFrame");
		applicationChoiceBox.addItem("RcxControlMasterFrame");
    }
*/
    public void stop()
    {
        if(me!=null){
//            me.stop();
            me=null;
        }
    }

    public void start()
    {
        if(me==null){
            me=new Thread(this,"CommunicationNode");
            me.start();
//            System.out.println("CommunicationNode thread priority:"+me.getPriority());
        }
    }

    public void run()
    {
        int i=0;
        lockTimer=0;
        int heartbeatTimer=25;
        while(me!=null){
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){
            }
            int tx=eventRecorder.timer.getTime();
            if(this.nodeController.isConnectedToUpperNode() &&
                  this.remainNextDoorRequest<2 ){
                      this.blinkHeartBeatLamp(tx);
            }
            else{
            	if(this.nodeController.isRootNode()){
            	   this.blinkHeartBeatLamp(tx);
            	}
            }
            if((i%10)==0){
               
               int t=tx/10;
//               timeField.setText(""+t);
               this.updateTime(t);
            }
            if(this.getControlMode().equals("common")){
                if(this.lockNodeID==this.serialNo){
//                	System.out.println("lockTimer="+this.getLockTimer());
                	if(isStoppingLockTimer()){}
                	else
                    if(lockTimer>0) //lockTimer--;
                    	this.decreaseLockTimer();
                    else {
                        unLockTheNode(1);
                        lockTimer=0;
                        this.sendEventFlag=false;
//                        this.directEvent=false;
                    }
                }    
            }
            if(i%heartbeatTimer==0){ // send heart beat for maintain the group.
            	if(me!=null)
                this.nextDoorControl("nextDoor startAdjustTimer",null);
            	this.nextDoorControl("nextDoor start-requestNo", null);
            }
            i++;
        }
    }

    boolean stoppingLockTimer;
    public boolean isStoppingLockTimer(){
    	return stoppingLockTimer;
    }
    public void setStoppingLockTimer(boolean b){
    	this.stoppingLockTimer=b;
    }
    public synchronized void setLockTimer(int t){
    	this.lockTimer=t;
    }
    public int getLockTimer(){
    	return lockTimer;
    }
    public synchronized void decreaseLockTimer(){
    	this.lockTimer--;
    }
    
    public void setPaths2()
    {
        thisID=uName+"-"+serialNo;
        nodeDataDir=new File(nodeDataRoot,thisID);
//        System.out.println("nodeDataDir="+nodeDataDir.getPath());
	    if(!nodeDataDir.exists()) 
	    	         nodeDataDir.mkdir();
        userDataDir=new File(nodeDataDir,"data");
//        System.out.println("nodeDataDir="+nodeDataDir.getPath());
	    if(!userDataDir.exists()) 
	    	         userDataDir.mkdir();
//
	    File userDataLogDir=new File(userDataDir,"Logs");
	    if(!userDataLogDir.exists())
	                 userDataLogDir.mkdir();
//	                 
        logDataRoot=new File(nodeDataDir,"Logs");
	    if(!logDataRoot.exists()) 
	    	         logDataRoot.mkdir();
//        System.out.println("logDataRoot="+logDataRoot.getPath());
        logDataDir=new File(logDataRoot,"tempLog");
	    if(!logDataDir.exists()) 
	    	         logDataDir.mkdir();
//        System.out.println("logDataDir="+logDataDir.getPath());
        mesDataRoot=new File(nodeDataDir,"Messages");
	    if(!mesDataRoot.exists()) 
	    	         mesDataRoot.mkdir();
//        System.out.println("mesDataRoot="+mesDataRoot.getPath());
        mesDataDir=new File(mesDataRoot,"tempMes");
	    if(!mesDataDir.exists()) 
	    	         mesDataDir.mkdir();
        commonDataDir=new File(dsrRoot,"commondata");
	    
        eventRecorder.initLog();
   }

    public File nodeDataDir;

    public File mesDataDir;

    public File logDataDir;

    public String currentDir;

    public String uName;

    public String appletBaseDir;
    
    public String getAppletBase(){
    	return appletBaseDir;
    }
    
    public void setCodeDirForApplet(String codeDir){
    	this.appletBaseDir=codeDir;
    }
    public void setBaseDirForApplet(String baseDir){
//    public void setPathForApplet(String baseDir){
        uName="applet user";
        userName.setText(uName);
//        System.out.println("userName="+uName);
        currentDir=baseDir;
//        appletBaseDir=baseDir;
//        System.out.println("currentDir="+currentDir);
//        dsrRoot=new File((new File(currentDir)).getParent());
        try{
        
        dsrRoot=new File(currentDir);
//        System.out.println("dsrRoot="+dsrRoot.getPath());
        }
        catch(Exception e){
        }
        try{
        nodeDataRoot=new File(dsrRoot,"nodedata");
	    if(!nodeDataRoot.exists()) 
	    	         nodeDataRoot.mkdir();
        }
        catch(Exception e){
        }
	    thisID=uName+"-0";
	    try{
        nodeDataDir=new File(nodeDataRoot,thisID);
//        System.out.println("nodeDataDir="+nodeDataDir.getPath());
	    if(!nodeDataDir.exists()) 
	    	         nodeDataDir.mkdir();
        userDataDir=new File(nodeDataDir,"data");
//        System.out.println("nodeDataDir="+nodeDataDir.getPath());
	    if(!userDataDir.exists()) 
	    	         userDataDir.mkdir();
        logDataRoot=new File(nodeDataDir,"Logs");
	    if(!logDataRoot.exists()) 
	    	         logDataRoot.mkdir();
//        System.out.println("logDataRoot="+logDataRoot.getPath());
        logDataDir=new File(logDataRoot,"tempLog");
	    if(!logDataDir.exists()) 
	    	         logDataDir.mkdir();
//        System.out.println("logDataDir="+logDataDir.getPath());
        mesDataRoot=new File(nodeDataDir,"Messages");
	    if(!mesDataRoot.exists()) 
	    	         mesDataRoot.mkdir();
//        System.out.println("mesDataRoot="+mesDataRoot.getPath());
        mesDataDir=new File(mesDataRoot,"tempMes");
	    if(!mesDataDir.exists()) 
	    	         mesDataDir.mkdir();
        commonDataDir=new File(dsrRoot,"commondata");
        if(!commonDataDir.exists())
                     commonDataDir.mkdir();
        commonDataFigDir=new File(commonDataDir,"figs");
        if(!commonDataFigDir.exists())
                     commonDataFigDir.mkdir();
        commonDataImageDir=new File(commonDataDir,"images");
        if(!commonDataFigDir.exists())
                     commonDataImageDir.mkdir();    
        }
        catch(Exception e){
        	
        }
    }
    
    public void setPaths()
    {
        System.out.println("osName="+System.getProperty("os.name"));
        System.out.println("fileSeparator="+System.getProperty("file.separator"));
        uName=System.getProperty("user.name");
        userName.setText(uName);
//        System.out.println("userName="+uName);
        currentDir=System.getProperty("user.dir");
//        System.out.println("currentDir="+currentDir);
//        dsrRoot=new File((new File(currentDir)).getParent());
        dsrRoot=new File(currentDir);
//        System.out.println("dsrRoot="+dsrRoot.getPath());
        nodeDataRoot=new File(dsrRoot,"nodedata");
	    if(!nodeDataRoot.exists()) 
	    	         nodeDataRoot.mkdir();
	    thisID=uName+"-0";
        nodeDataDir=new File(nodeDataRoot,thisID);
//        System.out.println("nodeDataDir="+nodeDataDir.getPath());
	    if(!nodeDataDir.exists()) 
	    	         nodeDataDir.mkdir();
        userDataDir=new File(nodeDataDir,"data");
//        System.out.println("nodeDataDir="+nodeDataDir.getPath());
	    if(!userDataDir.exists()) 
	    	         userDataDir.mkdir();
        logDataRoot=new File(nodeDataDir,"Logs");
	    if(!logDataRoot.exists()) 
	    	         logDataRoot.mkdir();
//        System.out.println("logDataRoot="+logDataRoot.getPath());
        logDataDir=new File(logDataRoot,"tempLog");
	    if(!logDataDir.exists()) 
	    	         logDataDir.mkdir();
//        System.out.println("logDataDir="+logDataDir.getPath());
        mesDataRoot=new File(nodeDataDir,"Messages");
	    if(!mesDataRoot.exists()) 
	    	         mesDataRoot.mkdir();
//        System.out.println("mesDataRoot="+mesDataRoot.getPath());
        mesDataDir=new File(mesDataRoot,"tempMes");
	    if(!mesDataDir.exists()) 
	    	         mesDataDir.mkdir();
        commonDataDir=new File(dsrRoot,"commondata");
        if(!commonDataDir.exists())
                     commonDataDir.mkdir();
        commonDataFigDir=new File(commonDataDir,"figs");
        if(!commonDataFigDir.exists())
                     commonDataFigDir.mkdir();
        commonDataImageDir=new File(commonDataDir,"images");
        if(!commonDataFigDir.exists())
                     commonDataImageDir.mkdir();
//
    }

    public void setWords(){
		// プロパティ名から値を取得して表示
//			this.spawnButton.setText(resourceBundle.getString("spawn"));
            this.spawnButton.setToolTipText(getLclTxt("spawn"));
//			  this.exitButton.setText(resourceBundle.getString("exit"));
			this.exitButton.setToolTipText(this.getLclTxt("exit"));
//			this.localButton.setText(this.getLclTxt("local"));
            this.localButton.setToolTipText(this.getLclTxt("local"));
//			this.settingButton.setText(this.getLclTxt("setting"));
            this.settingButton.setToolTipText(this.getLclTxt("setting"));
//			this.sendButton.setText(this.getLclTxt("Send"));
            this.sendButton.setToolTipText(this.getLclTxt("Send"));
			this.joinButton.setToolTipText(this.getLclTxt("join_the_group"));
			this.JLabel1.setText("Distributed System Recorder/"+ this.getLclTxt(this.className));
			this.leaveGroupButton.setToolTipText(this.getLclTxt("leave_the_group"));    	
			this.JLabel2.setText(this.getLclTxt("Group_Manager"));
			this.JLabel9.setText(this.getLclTxt("My_Address"));
			this.JLabel3.setText(this.getLclTxt("Name_No"));
			this.JLabel5.setText(this.getLclTxt("Chat_Output"));
			this.JLabel6.setText(this.getLclTxt("Chat_Input"));
			this.JLabel7.setText(this.getLclTxt("Applications"));
			this.timeLabel.setText(this.getLclTxt("Time"));
			this.JLabel11.setText(this.getLclTxt("op_node"));
			this.JLabel10.setText(this.getLclTxt("Sys_Message"));
			this.JLabel12.setText(this.getLclTxt("takashi_yamanoue"));
			this.showRecorderButton.setToolTipText(this.getLclTxt("show_recorder"));
			this.applicationButton.setToolTipText(this.getLclTxt("select_application"));
			
		    applicationManager=new Applications(this);
		    this.applicationsSelector=new ApplicationsFrame(this);
	        this.applicationsSelector.setIcons(this.getIconPlace());
	    	this.applicationsSelector.setState(0);
   }

    public boolean isRoot(){
    	return this.nodeController.isRootNode();
    }
    
    public String getLclTxt(String key){
    	String x=key;
    	try{
    	  x= resourceBundle.getString(key);
    	}
    	catch(Exception e){
    		return key;
    	}
    	return x;
    }

    public Applications applicationManager;
    public EventRecorder eventRecorder;
    Distributor distributor;
    public CommandTranceiver commandTranceiver;
    
	public CommunicationNode()
	{
		//SAKURAGI ADDED デバッグ用
		//application.autoUpdating.AutoUpdatingFrame auf=new application.autoUpdating.AutoUpdatingFrame(this);
		
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		setTitle("CommunicationNode");
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		setSize(464,529);
		setVisible(false);
		{
//		JLabel1.setText("Distributed System Recorder/ Communication Node");
		groupMngrAddressField.setSelectionColor(new java.awt.Color(204,204,255));
		groupMngrAddressField.setSelectedTextColor(java.awt.Color.black);
		groupMngrAddressField.setCaretColor(java.awt.Color.black);
		groupMngrAddressField.setDisabledTextColor(new java.awt.Color(153,153,153));
		groupMngrAddressField.setToolTipText("group manager\'s name/address(input)");
		getContentPane().add(groupMngrAddressField);
		groupMngrAddressField.setBackground(java.awt.Color.white);
		groupMngrAddressField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		groupMngrAddressField.setBounds(108,60,212,24);
		groupMngrAddressField.setEditable(true);
		groupMngrAddressField.setEnabled(true);
        }
        {
		groupMngrPortField.setSelectionColor(new java.awt.Color(204,204,255));
		groupMngrPortField.setSelectedTextColor(java.awt.Color.black);
		groupMngrPortField.setCaretColor(java.awt.Color.black);
		groupMngrPortField.setDisabledTextColor(new java.awt.Color(153,153,153));
		getContentPane().add(groupMngrPortField);
		groupMngrPortField.setBackground(java.awt.Color.white);
		groupMngrPortField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		groupMngrPortField.setBounds(320,60,60,24);
		}
		{
		    joinButton.setActionCommand("Join");
//		joinButton.setToolTipText("Join the group");
		    getContentPane().add(joinButton);
		    joinButton.setBackground(new java.awt.Color(204,204,204));
		    joinButton.setFont(new Font("Dialog", Font.BOLD, 12));
		    joinButton.setBounds(380,60,70,24);
//		    joinButton.setIcon(new ImageIcon("images/join-icon.GIF"));
		}
		{
//		JLabel2.setText("Group Manager");
		getContentPane().add(JLabel2);
		JLabel2.setBackground(new java.awt.Color(204,204,204));
		JLabel2.setForeground(new java.awt.Color(102,102,153));
		JLabel2.setFont(new Font("Dialog", Font.BOLD, 10));
		JLabel2.setBounds(12,60,108,24);
		}
		{
//		JLabel3.setText("Your Name, No.");
		getContentPane().add(JLabel3);
		JLabel3.setBackground(new java.awt.Color(204,204,204));
		JLabel3.setForeground(new java.awt.Color(102,102,153));
		JLabel3.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel3.setBounds(12,132,108,24);
		}
		{
		userName.setSelectionColor(new java.awt.Color(204,204,255));
		userName.setSelectedTextColor(java.awt.Color.black);
		userName.setCaretColor(java.awt.Color.black);
		userName.setDisabledTextColor(new java.awt.Color(153,153,153));
		userName.setToolTipText("input your name here");
		getContentPane().add(userName);
		userName.setBackground(java.awt.Color.white);
		userName.setFont(new Font("SansSerif", Font.PLAIN, 12));
		userName.setBounds(108,132,212,24);
		}
		{
		serialNoField.setSelectionColor(new java.awt.Color(204,204,255));
		serialNoField.setSelectedTextColor(java.awt.Color.black);
		serialNoField.setCaretColor(java.awt.Color.black);
		serialNoField.setDisabledTextColor(new java.awt.Color(153,153,153));
		getContentPane().add(serialNoField);
		serialNoField.setBackground(java.awt.Color.white);
		serialNoField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		serialNoField.setBounds(320,132,60,24);
		}
		{
    		exitButton.setActionCommand("Exit");
	    	exitButton.setToolTipText("exit");
		    getContentPane().add(exitButton);
		    exitButton.setBackground(new java.awt.Color(204,204,204));
		    exitButton.setFont(new Font("Dialog", Font.BOLD, 12));
		    exitButton.setBounds(380,132,70,24);
//		    exitButton.setIcon(new ImageIcon("images/exit-icon.GIF"));
		}
		{
		JScrollPane1.setOpaque(true);
		getContentPane().add(JScrollPane1);
		JScrollPane1.setBounds(108,156,272,84);
		}
		{
		chatOut.setSelectionColor(new java.awt.Color(204,204,255));
		chatOut.setSelectedTextColor(java.awt.Color.black);
		chatOut.setCaretColor(java.awt.Color.black);
		chatOut.setDisabledTextColor(new java.awt.Color(153,153,153));
		chatOut.setEditable(false);
		JScrollPane1.getViewport().add(chatOut);
		chatOut.setFont(new Font("SansSerif", Font.PLAIN, 12));
		chatOut.setBounds(0,0,248,80);
		}
		{
//		JLabel5.setText("Chat/ Output");
		getContentPane().add(JLabel5);
		JLabel5.setBackground(new java.awt.Color(204,204,204));
		JLabel5.setForeground(new java.awt.Color(102,102,153));
		JLabel5.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel5.setBounds(12,180,96,24);
		}
		{
		JScrollPane2.setOpaque(true);
		getContentPane().add(JScrollPane2);
		JScrollPane2.setBounds(108,240,272,48);
		}
		{
		chatIn.setSelectionColor(new java.awt.Color(204,204,255));
		chatIn.setSelectedTextColor(java.awt.Color.black);
		chatIn.setCaretColor(java.awt.Color.black);
		chatIn.setDisabledTextColor(new java.awt.Color(153,153,153));
		chatIn.setToolTipText("input chat message here");
		JScrollPane2.getViewport().add(chatIn);
		chatIn.setFont(new Font("SansSerif", Font.PLAIN, 12));
		chatIn.setBounds(0,0,248,44);
		}
		{
//		JLabel6.setText("Chat/ Input");
		getContentPane().add(JLabel6);
		JLabel6.setBackground(new java.awt.Color(204,204,204));
		JLabel6.setForeground(new java.awt.Color(102,102,153));
		JLabel6.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel6.setBounds(12,240,96,24);
		}
		{
//		sendButton.setText("Send");
		    sendButton.setActionCommand("Send");
		    sendButton.setToolTipText("send the left text(input) to all members");
		    getContentPane().add(sendButton);
		    sendButton.setBackground(new java.awt.Color(204,204,204));
		    sendButton.setFont(new Font("Dialog", Font.BOLD, 12));
		    sendButton.setBounds(380,240,70,24);
//		    sendButton.setIcon(new ImageIcon("images/send-icon.GIF"));
        }
        {
//		JLabel7.setText("Applications");
		getContentPane().add(JLabel7);
		JLabel7.setBackground(new java.awt.Color(204,204,204));
		JLabel7.setForeground(new java.awt.Color(102,102,153));
		JLabel7.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel7.setBounds(12,288,96,24);
        }
        {
//		JLabel8.setText("Connect To:");
		getContentPane().add(JLabel8);
		JLabel8.setBackground(new java.awt.Color(204,204,204));
		JLabel8.setForeground(new java.awt.Color(102,102,153));
		JLabel8.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel8.setBounds(12,84,96,24);
        }
		{
		addressOfUpperNode.setSelectionColor(new java.awt.Color(204,204,255));
		addressOfUpperNode.setSelectedTextColor(java.awt.Color.black);
		addressOfUpperNode.setCaretColor(java.awt.Color.black);
		addressOfUpperNode.setDisabledTextColor(new java.awt.Color(153,153,153));
		getContentPane().add(addressOfUpperNode);
		addressOfUpperNode.setBackground(java.awt.Color.white);
		addressOfUpperNode.setFont(new Font("SansSerif", Font.PLAIN, 12));
		addressOfUpperNode.setBounds(108,84,212,24);
		}
		{
		connectPort.setSelectionColor(new java.awt.Color(204,204,255));
		connectPort.setSelectedTextColor(java.awt.Color.black);
		connectPort.setCaretColor(java.awt.Color.black);
		connectPort.setDisabledTextColor(new java.awt.Color(153,153,153));
		getContentPane().add(connectPort);
		connectPort.setBackground(java.awt.Color.white);
		connectPort.setFont(new Font("SansSerif", Font.PLAIN, 12));
		connectPort.setBounds(320,84,60,24);
		}
		{
//		JLabel9.setText("My Address");
		getContentPane().add(JLabel9);
		JLabel9.setBackground(new java.awt.Color(204,204,204));
		JLabel9.setForeground(new java.awt.Color(102,102,153));
		JLabel9.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel9.setBounds(12,108,96,24);
		}
		{
		myAddressField.setSelectionColor(new java.awt.Color(204,204,255));
		myAddressField.setSelectedTextColor(java.awt.Color.black);
		myAddressField.setCaretColor(java.awt.Color.black);
		myAddressField.setDisabledTextColor(new java.awt.Color(153,153,153));
		myAddressField.setEditable(false);
		getContentPane().add(myAddressField);
		myAddressField.setBackground(java.awt.Color.white);
		myAddressField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		myAddressField.setBounds(108,108,212,24);
		}
		{
		listenPort.setSelectionColor(new java.awt.Color(204,204,255));
		listenPort.setSelectedTextColor(java.awt.Color.black);
		listenPort.setCaretColor(java.awt.Color.black);
		listenPort.setDisabledTextColor(new java.awt.Color(153,153,153));
		listenPort.setEditable(false);
		getContentPane().add(listenPort);
		listenPort.setBackground(java.awt.Color.white);
		listenPort.setFont(new Font("SansSerif", Font.PLAIN, 12));
		listenPort.setBounds(320,108,60,24);
		}
		{
		JScrollPane3.setOpaque(true);
		getContentPane().add(JScrollPane3);
		JScrollPane3.setBounds(108, 336, 272, 84);
		messageArea.setSelectionColor(new java.awt.Color(204,204,255));
		messageArea.setSelectedTextColor(java.awt.Color.black);
		messageArea.setCaretColor(java.awt.Color.black);
		messageArea.setDisabledTextColor(new java.awt.Color(153,153,153));
		messageArea.setEditable(false);
		JScrollPane3.getViewport().add(messageArea);
		messageArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		messageArea.setBounds(0,0,248,104);
		}
		{
//		JLabel10.setText("Sys. Message");
		getContentPane().add(JLabel10);
		JLabel10.setBackground(new java.awt.Color(204,204,204));
		JLabel10.setForeground(new java.awt.Color(102,102,153));
		JLabel10.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel10.setBounds(10, 342, 96, 24);
		}
		{
//		timeLabel.setText("Time:");
		getContentPane().add(timeLabel);
		timeLabel.setBackground(new java.awt.Color(204,204,204));
		timeLabel.setForeground(new java.awt.Color(102,102,153));
		timeLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		timeLabel.setBounds(200, 312, 50, 24);
		}
		{
		controlModeLabel.setText("controlMode");
		getContentPane().add(controlModeLabel);
		controlModeLabel.setBounds(192,36,156,24);
		}
		{
		    getContentPane().add(applicationButton);
		    applicationButton.setBackground(new java.awt.Color(204,204,204));
		    applicationButton.setForeground(java.awt.Color.black);
		    applicationButton.setFont(new Font("Dialog", Font.BOLD, 12));
		    applicationButton.setBounds(108,288,272,24);
		}
		{
//     		spawnButton.setText("spawn");
		    spawnButton.setActionCommand("spawn");
		    getContentPane().add(spawnButton);
		    spawnButton.setBackground(new java.awt.Color(204,204,204));
		    spawnButton.setForeground(java.awt.Color.black);
		    spawnButton.setFont(new Font("Dialog", Font.BOLD, 12));
		    spawnButton.setBounds(380,288,70,24);
//		    spawnButton.setIcon(new ImageIcon("images/play.gif"));
        }
        {
//		    showRecorderButton.setText("show recorder");
		    showRecorderButton.setActionCommand("show recorder");
		    getContentPane().add(showRecorderButton);
		    showRecorderButton.setBackground(new java.awt.Color(204,204,204));
		    showRecorderButton.setForeground(java.awt.Color.black);
		    showRecorderButton.setFont(new Font("Dialog", Font.BOLD, 12));
		    showRecorderButton.setBounds(380, 312, 70, 24);
//		    showRecorderButton.setIcon(new ImageIcon("images/recorder-icon.GIF"));
        }
        {
//    		localButton.setText("local");
	    	localButton.setActionCommand("local");
		    getContentPane().add(localButton);
		    localButton.setBounds(380,36,70,24);
//		    localButton.setIcon(new ImageIcon("images/local-icon.GIF"));
        }
        {
//    		JLabel11.setText("op node");
	    	JLabel11.setToolTipText("operating(broadcastiong) node");
		    JLabel11.setBackground(new java.awt.Color(204,204,204));
			JLabel11.setForeground(new java.awt.Color(102,102,153));
			getContentPane().add(JLabel11);
			JLabel11.setBounds(11, 313, 96, 24);
        }
        {
		opNodeField.setEditable(false);
		getContentPane().add(opNodeField);
		opNodeField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		opNodeField.setBounds(108, 312, 68, 24);
		opNodeField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
        }
		{
		    heartBeatButton.setActionCommand("heatbeat");
		    heartBeatButton.setToolTipText("heart beat");
		    getContentPane().add(heartBeatButton);
		    heartBeatButton.setBounds(318, 312, 62, 24);
//		    heartBeatButton.setIcon(new ImageIcon("images/heart-icon.GIF"));
		}
		{
		//$$ joinIcon.move(396,480);
		getContentPane().add(timeField);
		timeField.setBounds(250, 312, 68, 24);
		timeField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		}
		{
//		JLabel12.setText("Takashi Yamanoue, Kagoshima University");
		getContentPane().add(JLabel12);
		JLabel12.setBounds(24,420,420,24);
		}
		{
		JLabel13.setText("yamanoue@cc.kagoshima-u.ac.jp");
		getContentPane().add(JLabel13);
		JLabel13.setBounds(24,444,420,24);
		}
		{
		JLabel14.setText("http://yama-linux.cc.kagoshima-u.ac.jp/~dsr");
		getContentPane().add(JLabel14);
		JLabel14.setBounds(24,468,420,24);
		}
		{//SAKURAGI ADDED
			JLabel15.setText("DSR Version: "+thisVer);
			getContentPane().add(JLabel15);
			JLabel15.setBounds(24,492,420,24);
		}
		//$$ disConnectIcon.move(348,504);
		{
		    settingButton.setActionCommand("setting");
		    getContentPane().add(settingButton);
		    settingButton.setBounds(380,108,70,24);
//		    settingButton.setIcon(new ImageIcon("images/setting.gif"));
		}
		{
		    getContentPane().add(leaveGroupButton);
    		leaveGroupButton.setBounds(380,84,70,24);
//    		leaveGroupButton.setIcon(new ImageIcon("images/dis-connect-icon.GIF"));
		}
		{
			solarcatsicon = new JLabel();
			this.getContentPane().add(solarcatsicon);
			solarcatsicon.setBounds(11, 5, 94, 57);
//			solarcatsicon.setIcon(new ImageIcon("images/solar-cats-1-img.GIF"));
		}
		//}}

		//{{INIT_MENUS
		//}}
	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		joinButton.addActionListener(lSymAction);
		exitButton.addActionListener(lSymAction);
		sendButton.addActionListener(lSymAction);
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		localButton.addActionListener(lSymAction);
		SymFocus aSymFocus = new SymFocus();
		this.addFocusListener(aSymFocus);
		settingButton.addActionListener(lSymAction);
		leaveGroupButton.addActionListener(lSymAction);
		//}}
		
		this.receiveEventQueue=new MessageQueue(1000);
//		this.setChoiceBox();
		
        this.encodingCode="JIS";

		distributor=new Distributor(encodingCode,5);
		try{
            setPaths();
		}
		catch(Exception e){
			
		}
		try{
        eventRecorder=new EventRecorder();
		}
		catch(Exception e){
			eventRecorder=null;
		}
		
		commandTranceiver=new 
		  CommandTranceiver(this,distributor,eventRecorder,encodingCode,
		                    receiveEventQueue);
		if(eventRecorder!=null)
            eventRecorder.init(commandTranceiver,this);
        this.ebuff=commandTranceiver.ebuff;

        this.nodeController=new InterNodeController(
        		this,distributor,commandTranceiver,eventRecorder);
        this.trace=this.nodeController.getTrace();
        this.qa=this.nodeController.getQA();
        /*
		clientToUpperNode=new Client(this,distributor,commandTranceiver,
		                    receiveEventQueue);
	    this.mcastClient=new MulticastClient(this,null,commandTranceiver,
	                        receiveEventQueue);
	    */
//	    try{
//           String myaddr=(InetAddress.getLocalHost()).getHostName();
//             String myaddr=(InetAddress.getLocalHost()).toString();
	    	String myaddr=this.getMyAddress1();
             myAddressField.setText(myaddr);
//        }
//        catch(UnknownHostException e){}

//        applicationsSelector.setFrame(this);
//        applicationsSelector.setApplicationPath();
        
        setTitle("Communication Node");
        tempText="";
        isJoined=false;
        
    	buttons=new Vector();
	    buttons.addElement( this.spawnButton);
	    buttons.addElement( this.applicationButton );
	    buttons.addElement( this.showRecorderButton );
	
    	int numberOfButtons=buttons.size();
	
	    for(int i=0;i<numberOfButtons;i++){
	        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
	        b.setFrame(this);
	        b.setID(i);
    		b.addActionListener(lSymAction);
	    }
	    this.serialNo=0;
	    this.remoteLock=new Hashtable();
	    this.controlIsLocked=false;
	    this.theNodeWithThisLock=0;
	    this.lockTimerMax=5;
	    this.isKeepingCriticalSection=false;
	    
		this.returnMessage=new Hashtable();	    
//	    if(!this.isApplet())
//	          init();
		this.setEnabled(true);
    }

    public boolean isGlobal(InetAddress a){
    	if(this.isLocal(a)) return false;
    	return true;
    }
 
    public boolean isLocal(InetAddress a){
    	byte x[]=a.getAddress();
    	if(x[0]==(byte)192 && x[1]==(byte)168) return true;
    	if(x[0]==(byte)172) {
    		if((byte)16<=x[1] && x[1]<=(byte)31)
    		return true;
    	}
    	if(x[0]==(byte)10) return true;
    	return false;
    }
    public boolean isV6(InetAddress a){
    	byte x[]=a.getAddress();
    	if(x.length>4)
    		return true;
    	else
    		return false;
    }
    
    public String getMyAddress1(){
    	String myAddress="";
	    try{
            String addr=(InetAddress.getLocalHost()).getHostName();
            StringTokenizer st=new StringTokenizer(addr,"/");
            String name=st.nextToken();
	    	InetAddress ia[]=InetAddress.getAllByName(name);
            for(int i=0;i<ia.length;i++){
            	InetAddress a=ia[i];
            	if(isV6(a)){
            		
            	}
            	else
            	if(isGlobal(a)){
            		myAddress=a.getHostAddress();
            		return myAddress;
            	}
 
            }
            for(int i=0;i<ia.length;i++){
            	InetAddress a=ia[i];
            	if(isV6(a)){
            		
            	}
            	else
            	if(isLocal(a)){
            		myAddress=a.getHostAddress();
            		if(!myAddress.startsWith("127.0.0"))
            		return myAddress;
            	}            	
            }
            return "127.0.0.1";
       }
       catch(UnknownHostException e){
    	   return "127.0.0.1";
       }
    }
	
	//SAKURAGI ADDED
	public void init(){
		InitSetting is=new InitSetting(this);
	    logControl=new LogControl(this);
		is.setInit();
	    logControl.init();
	}
    public void setIcons(String iconPlace){
		try{
//			ImageIcon ic=new ImageIcon("images/rotate-icon.GIF");
//			ImageIcon ic=new ImageIcon(new URL(iconPlace+"rotate-icon.GIF"));
			ImageIcon ic=new ImageIcon(new URL(iconPlace+"exit-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			    exitButton.setIcon(ic);
//			eRotateButton.setIcon(ic);
			else
				exitButton.setText("exit");
//			ic=new ImageIcon(new URL(iconPlace+"clear-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"send-icon.GIF"));
	    	if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			    sendButton.setIcon(ic);
//		     eClearButton.setIcon(ic);
		    else
			 sendButton.setText("send");
//	  		ic=new ImageIcon(new URL(iconPlace+"modyfy-icon.GIF"));
	    	ic=new ImageIcon(new URL(iconPlace+"play.gif"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
//			     eModifyButton.setIcon(ic);
		    spawnButton.setIcon(ic);
			else spawnButton.setText("play");
//			ic=new ImageIcon(new URL(iconPlace+"copy-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"recorder-icon.GIF"));
//		    showRecorderButton.setIcon(new ImageIcon("images/recorder-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
//			  eCopyButton.setIcon(new ImageIcon("images/copy-icon.GIF"));
//		    showRecorderButton.setIcon(new ImageIcon("images/recorder-icon.GIF"));
				showRecorderButton.setIcon(ic);
			else showRecorderButton.setText("recorder");
			ic=new ImageIcon(new URL(iconPlace+"local-icon.GIF"));
//		    localButton.setIcon(new ImageIcon("images/local-icon.GIF"));
		    if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
		      localButton.setIcon(ic);
		    else localButton.setText("local");
//		    heartBeatButton.setIcon(new ImageIcon("images/heart-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"heart-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			heartBeatButton.setIcon(ic);
			else heartBeatButton.setText("o");
//		    settingButton.setIcon(new ImageIcon("images/setting.gif"));
			ic=new ImageIcon(new URL(iconPlace+"setting.gif"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			settingButton.setIcon(ic);
			else settingButton.setText("setting");
//			solarcatsicon.setIcon(new ImageIcon("images/solar-cats-1-img.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"solar-cats-1-img.GIF"));
			solarcatsicon.setIcon(ic);
//    		leaveGroupButton.setIcon(new ImageIcon("images/dis-connect-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"dis-connect-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			leaveGroupButton.setIcon(ic);
			else leaveGroupButton.setText("leave");
//		    joinButton.setIcon(new ImageIcon("images/join-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"join-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			joinButton.setIcon(ic);
			else joinButton.setText("join");

		}
		catch(Exception e){
		}
    }
    
	public CommunicationNode(String sTitle)
	{
		this();
		setTitle(sTitle);
	}

	public void setVisible(boolean b)
	{
		if (b)
			setLocation(50, 50);
			{
				JLabel1 = new JLabel();
				this.getContentPane().add(JLabel1);
//				JLabel1.setIcon(new ImageIcon("images/solar-cats-1-img.GIF"));
				JLabel1.setBackground(new java.awt.Color(204,204,204));
				JLabel1.setFont(new Font("Dialog",Font.BOLD,12));
				JLabel1.setForeground(new java.awt.Color(102,102,153));
				JLabel1.setBounds(108, 9, 344, 24);
			}
		super.setVisible(b);
	}

	public void mainx(String arg){
		StringTokenizer st=new StringTokenizer(arg);
		int narg=st.countTokens();
		String args[]=new String[narg];
		int j=0;
		while(st.hasMoreTokens()){
			args[j]=st.nextToken(); j++;
		}
		Locale locale=new Locale("","");
		this.resourceBundle = ResourceBundle.getBundle("dsrWords" , locale);
	    this.setTitle("DSR/communication node - teacher");
		this.setVisible(true);
		this.className="Communication_Node";
		this.controlMode="teach";
		this.setWords();
        int i=0; // args[i]
        while(i<args.length){
    		if(args[i].equals("-h")){
    		    i++;
    	    	String groupManagerAddress=args[i];
    	    	if(groupManagerAddress==null){
    	    	    System.out.println("usage: CommunicationNode [ -h <host address>] ");
    	    	    return;
    	    	}
	    	    this.groupMngrAddressField.setText(groupManagerAddress);
    		    this.joinButton_actionPerformed_Interaction1(null);
	    	}
	    	if(args[i].equals("-m")){
	    		i++;
	    		String controlMode=args[i];
	    		if(controlMode==null){
	    			System.out.println("usage: CommunicationNode [-m common]");
	    			return;
	    		}
	    		this.setControlMode(controlMode);
	    	}
	    	if(args[i].equals("-locale")){
	    		i++;
	    		String localeName=args[i];
	    		int sepPos=localeName.indexOf("_");
	    		if(sepPos<=0){
	    			System.out.println("usage: CommunicationNode [-locale <language>_<country>]");
	    			return;
	    		}
	    		else{
     	    		String language=localeName.substring(0,sepPos);
		    		String country=localeName.substring(sepPos+1,localeName.length());
		   // ロケールを生成
			        locale = new Locale(language,country);
		   // 指定されたロケールのリソースバンドルを取得
			        this.resourceBundle = ResourceBundle.getBundle("dsrWords" , locale);
	    		}
	    	}
	    	i++;
		}
		//
		this.setWords();
		this.setControlMode(this.controlMode);
		this.serialNo=1;
		this.start();
				
	}
	
	static public void main(String args[])
	{
	    CommunicationNode cn=new CommunicationNode();
	    cn.setIsApplet(false);
		Locale locale=new Locale("","");
		/*
		System.out.println("country="+locale.getCountry());
		System.out.println("locale="+locale.toString());
		System.out.println("language="+locale.getLanguage());
		*/
		cn.resourceBundle = ResourceBundle.getBundle("dsrWords" , locale);
	    cn.setTitle("DSR/communication node - teacher");
		cn.setVisible(true);
		cn.className="Communication_Node";
		cn.controlMode="teach";
		cn.setWords();
        int i=0; // args[i]
        while(i<args.length){
    		if(args[i].equals("-h")){
    		    i++;
    	    	String groupManagerAddress=args[i];
    	    	if(groupManagerAddress==null){
    	    	    System.out.println("usage: CommunicationNode [ -h <host address>] ");
    	    	    return;
    	    	}
	    	    cn.groupMngrAddressField.setText(groupManagerAddress);
    		    cn.joinButton_actionPerformed_Interaction1(null);
	    	}
	    	if(args[i].equals("-m")){
	    		i++;
	    		String controlMode=args[i];
	    		if(controlMode==null){
	    			System.out.println("usage: CommunicationNode [-m common]");
	    			return;
	    		}
	    		cn.setControlMode(controlMode);
	    	}
	    	if(args[i].equals("-locale")){
	    		i++;
	    		String localeName=args[i];
	    		int sepPos=localeName.indexOf("_");
	    		if(sepPos<=0){
	    			System.out.println("usage: CommunicationNode [-locale <language>_<country>]");
	    			return;
	    		}
	    		else{
     	    		String language=localeName.substring(0,sepPos);
		    		String country=localeName.substring(sepPos+1,localeName.length());
		   // ロケールを生成
			        locale = new Locale(language,country);
		   // 指定されたロケールのリソースバンドルを取得
			        cn.resourceBundle = ResourceBundle.getBundle("dsrWords" , locale);
	    		}
	    	}
	    	i++;
		}
		//
		cn.setWords();
		cn.setControlMode(cn.controlMode);
		cn.serialNo=1;
		cn.init(); // sakuragi added
        cn.setIcons(cn.getIconPlace());
		cn.start();
		
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension size = getSize();

		super.addNotify();

		if (frameSizeAdjusted)
			return;
		frameSizeAdjusted = true;

		// Adjust size of frame according to the insets and menu bar
		Insets insets = getInsets();
		javax.swing.JMenuBar menuBar = getRootPane().getJMenuBar();
		int menuBarHeight = 0;
		if (menuBar != null)
			menuBarHeight = menuBar.getPreferredSize().height;
		setSize(insets.left + insets.right + size.width, insets.top + insets.bottom + size.height + menuBarHeight);
	}

	// Used by addNotify
	boolean frameSizeAdjusted = false;

	//{{DECLARE_CONTROLS
	javax.swing.JTextField groupMngrAddressField = new javax.swing.JTextField();
	javax.swing.JTextField groupMngrPortField = new javax.swing.JTextField();
	javax.swing.JButton joinButton = new javax.swing.JButton();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	javax.swing.JTextField userName = new javax.swing.JTextField();
	javax.swing.JTextField serialNoField = new javax.swing.JTextField();
	javax.swing.JButton exitButton = new javax.swing.JButton();
	javax.swing.JScrollPane JScrollPane1 = new javax.swing.JScrollPane();
	public javax.swing.JTextArea chatOut = new javax.swing.JTextArea();
	javax.swing.JLabel JLabel5 = new javax.swing.JLabel();
	javax.swing.JScrollPane JScrollPane2 = new javax.swing.JScrollPane();
	javax.swing.JTextArea chatIn = new javax.swing.JTextArea();
	javax.swing.JLabel JLabel6 = new javax.swing.JLabel();
	javax.swing.JButton sendButton = new javax.swing.JButton();
	javax.swing.JLabel JLabel7 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel8 = new javax.swing.JLabel();
	javax.swing.JTextField addressOfUpperNode = new javax.swing.JTextField();
	javax.swing.JTextField connectPort = new javax.swing.JTextField();
	javax.swing.JLabel JLabel9 = new javax.swing.JLabel();
	javax.swing.JTextField myAddressField = new javax.swing.JTextField();
	javax.swing.JTextField listenPort = new javax.swing.JTextField();
	javax.swing.JScrollPane JScrollPane3 = new javax.swing.JScrollPane();
	public javax.swing.JTextArea messageArea = new javax.swing.JTextArea();
	javax.swing.JLabel JLabel10 = new javax.swing.JLabel();
	javax.swing.JLabel timeLabel = new javax.swing.JLabel();
	javax.swing.JLabel controlModeLabel = new javax.swing.JLabel();
	public controlledparts.ControlledButton applicationButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton spawnButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton showRecorderButton = new controlledparts.ControlledButton();
	javax.swing.JButton localButton = new javax.swing.JButton();
	javax.swing.JLabel JLabel11 = new javax.swing.JLabel();
	javax.swing.JTextField opNodeField = new javax.swing.JTextField();
	javax.swing.JButton heartBeatButton = new javax.swing.JButton();
	javax.swing.JLabel timeField = new javax.swing.JLabel();
	javax.swing.JLabel JLabel12 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel13 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel14 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel15 = new javax.swing.JLabel();//SAKURAGI ADDED
	javax.swing.JButton settingButton = new javax.swing.JButton();
	javax.swing.JButton leaveGroupButton = new javax.swing.JButton();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == joinButton)
				joinButton_actionPerformed(event);
			else if (object == exitButton)
				exitButton_actionPerformed(event);
			else if (object == sendButton)
				sendButton_actionPerformed(event);
			else if (object == localButton)
				localButton_actionPerformed(event);
			if (object == settingButton)
				settingButton_actionPerformed(event);
			else if (object == leaveGroupButton)
				leaveGroupButton_actionPerformed(event);
			
			
		}
	}

	void joinButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		joinButton_actionPerformed_Interaction1(event);
	}

	void exitButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.		 
		exitButton_actionPerformed_Interaction1(event);
	}

	void sendButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		sendButton_actionPerformed_Interaction1(event);
	}

	void sendButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		
		String chatMessage=chatIn.getText();
		
//		String sending=this.thisID+": "+chatMessage+"    \n";
        /*
           // Unicode を S-JIS Code に変換
	    byte[]  sjisCode = JavaStringToShiftJISString.convertAll( sending.toCharArray());
	    */
		/*
		String cmd="broadcast shell chat "+sending+commandTranceiver.endOfACommand;
		AMessage m=new AMessage();
		m.setHead(cmd);
        commandTranceiver.sendCommand(m );
        chatIn.setText("");
        if(!this.getControlMode().equals("withReflector")){
           chatOut.append(sending);
           this.chatOut.setCaretPosition(this.chatOut.getText().length());
        }
        */
		this.sendChatMessage(chatMessage);
	}
	public void sendChatMessage(String x){
		String namex=this.uName+"-"+this.serialNo;
		String sending=namex+": "+x+" \n";
        /*
           // Unicode を S-JIS Code に変換
	    byte[]  sjisCode = JavaStringToShiftJISString.convertAll( sending.toCharArray());
	    */
		String cmd="broadcast shell chat "+sending+commandTranceiver.endOfACommand;
		AMessage m=new AMessage();
		m.setHead(cmd);
        commandTranceiver.sendCommand(m );
        chatIn.setText("");
        if(!this.getControlMode().equals("withReflector")){
           chatOut.append(sending);
           this.chatOut.setCaretPosition(this.chatOut.getText().length());
        }
		
	}
    public void appendMessage(String s)
    {
        while(this.isGuiLockOccupied("cn-appendMessage")){
            try{
              Thread.sleep(10); //10
            }
            catch(InterruptedException e){}
        }
         messageArea.append(s);
         System.out.println(s);
//         messageArea.setCaretPosition(messageArea.getText().length());
        this.releaseGuiLock("cn-appendMessage");
   }
   
 
	class SymWindow extends java.awt.event.WindowAdapter
	{

		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == CommunicationNode.this)
				CommunicationNode_windowClosing(event);
		}
	}

	void CommunicationNode_windowClosing(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		CommunicationNode_windowClosing_Interaction1(event);
	}

	void CommunicationNode_windowClosing_Interaction1(java.awt.event.WindowEvent event)
	{
		this.exitButton_actionPerformed_Interaction1(null);
	}

	void localButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		localButton_actionPerformed_Interaction1(event);
	}

	void localButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		this.setControlMode("local");
	}

	class SymFocus extends java.awt.event.FocusAdapter
	{
		public void focusLost(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == CommunicationNode.this)
				CommunicationNode_focusLost(event);
		}

		public void focusGained(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == CommunicationNode.this)
				CommunicationNode_focusGained(event);
		}
	}

	void CommunicationNode_focusGained(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
			 
		CommunicationNode_focusGained_Interaction1(event);
	}

	void CommunicationNode_focusGained_Interaction1(java.awt.event.FocusEvent event)
	{
	    /*
		try {
			this.show();
		} catch (java.lang.Exception e) {
		}
		*/
		this.focusGained=true;
	}

	void CommunicationNode_focusLost(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
			 
		CommunicationNode_focusLost_Interaction1(event);
	}

	void CommunicationNode_focusLost_Interaction1(java.awt.event.FocusEvent event)
	{
	    /*
		try {
			this.show();
		} catch (java.lang.Exception e) {
		}
		*/
		this.focusGained=false;
	}

	void settingButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		settingButton_actionPerformed_Interaction1(event);
	}

	void settingButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		String groupMode=this.nodeController.getGroupMode();
		if(this.nodeController==null)return;
	    if(groupMode.equals("P2P")){
	        this.nodeController.settings.groupMngrAddressField.setText(
	            this.groupMngrAddressField.getText());
	        this.nodeController.settings.groupMngrPortField.setText(
	            this.groupMngrPortField.getText());
	    }
	    if(groupMode.equals("CS")||groupMode.equals("CSB")||
	       groupMode.equals("MCAST")){
	        this.nodeController.settings.reflectorAddressField.setText(
	            this.addressOfUpperNode.getText());
	        this.nodeController.settings.reflectorPortField.setText(
	            this.connectPort.getText());
	    }
	    this.nodeController.settings.show(true);
	}

	public void joinButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			joinButton.show();
		} catch (java.lang.Exception e) {
		}
		this.nodeController.joinGroupWithMode("teacher");
	}

	void exitButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		if(eventRecorder!=null){
		   eventRecorder.stop();
		   eventRecorder.closeLog();
		   eventRecorder.closeMessage();
		}
		this.nodeController.disconnect();
		hide();
		dispose();
		System.exit(0);
	}

	void leaveGroupButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		leaveGroupButton_actionPerformed_Interaction1(event);
	}

	void leaveGroupButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		if(eventRecorder!=null){
		   eventRecorder.stop();
		   eventRecorder.closeLog();
		   eventRecorder.closeMessage();
		}
		nodeController.disconnect();
	}
	
	public void setRmouseActivate(boolean f){
		this.rmouseIsActivated=f;
	}
	
	public String nextDoorControl(String cmd, Client clt) {
		// TODO 自動生成されたメソッド・スタブ
		return this.nodeController.nextDoorControl(cmd,clt);
	}

	public String nodeControl(String s) {
		// TODO 自動生成されたメソッド・スタブ
		return this.nodeController.nodeControl(s);
	}
	public int getAnswer(String x){
		return this.nodeController.getAnswer(x);
	}
	public boolean isTracing(){
		if(trace==null){
			return false;
		}
		return this.trace.isTracing();
	}
	public TraceSW getTrace(){
		return this.trace;
	}
	
	public QA getQA(String x){
		if(x==null) return null;
		if(x.equals("partitioning")){
			return this.nodeController.getQA();
		}
		else return null;
	}
	public Client getLinkToUp(){
		if(this.nodeController==null)
    		return null;
		return this.nodeController.getLinkToUp();
	}
    public StringIO getLeftLink(){
    	if(this.distributor==null)
    		return null;
    	StringIO rtn=this.distributor.getIOAt(1);
    	return rtn;
    }
    public StringIO getRightLink(){
    	if(this.distributor==null)
    		return null;
    	StringIO rtn=this.distributor.getIOAt(2);
    	return rtn;
    }	
    
	int maxwait=10;
	public String getLastReturn(String x)
	{   
		System.out.println("...waiting "+x);
		for(int i=0;i<maxwait;i++){
			synchronized(returnMessage){
			    String rtn=(String)(this.returnMessage.get(x));
			    System.out.println(" ... return "+rtn);
			    if(rtn!=null) {
				   this.returnMessage.remove(x);
				   return rtn;
			    }
			}
			try{
				Thread.sleep(50);
			}
			catch(Exception e){}
		}
		System.out.println("time out");
		return null;
	}
	public String parseCommand(String command){
		System.out.println(command);
		if(command.startsWith("sendToUp ")){
			String x=command.substring("sendToUp ".length());
			String sending="nextDoor ex "+x;
			Client c=this.getLinkToUp();
			StringIO sio=c.sio;
			try{
				sio.writeString(sending);
			}
			catch(Exception e){
				return "na";
			}
			return "ok";
		}
		if(command.startsWith("sendToDown ")){ // sendToDown [l/r] command
			String x=command.substring("sendToDown ".length());
			String lr=x.substring(0,1);
			x=x.substring(2);
			StringIO sio=null;
			if(lr.equals("l")){
				sio=this.getLeftLink();
			}
			else
			if(lr.equals("r")){
				sio=this.getRightLink();
			}
			if(sio==null){
				return "na";
			}
			
			String sending="nextDoor ex "+x;
			Client c=this.getLinkToUp();
			try{
				sio.writeString(sending);
			}
			catch(Exception e){
				return "na";
			}
			
			return "ok";
		}
		if(command.startsWith("askToUp ")){
			String x=command.substring("askToUp ".length());
			String sending="nextDoor ex-r askToUp "+x;
			Client c=this.getLinkToUp();
			StringIO sio=c.sio;
			try{
				sio.writeString(sending);
			}
			catch(Exception e){
				return "na";
			}
			return this.getLastReturn("askToUp");
		}

		if(command.startsWith("askToDown ")){ // askToDown [l/r] command
			String x=command.substring("askToDown ".length());
			String lr=x.substring(0,1);
			x=x.substring(2);
			StringIO sio=null;
			if(lr.equals("l")){
				sio=this.getLeftLink();
			}
			else
			if(lr.equals("r")){
				sio=this.getRightLink();
			}
			if(sio==null){
				return "na";
			}
			
			String sending="nextDoor ex-r askToDown "+x;
			Client c=this.getLinkToUp();
			try{
				sio.writeString(sending);
			}
			catch(Exception e){
				return "na";
			}
			
			return this.getLastReturn("askToDown");
		}
		if(command.startsWith("askToUp-nw ")){ // askToUp-nw waitingKeyword command
			String x=command.substring("askToUp-nw ".length());
			StringTokenizer st=new StringTokenizer(x);
			String waitingKey=st.nextToken();
			String sending="nextDoor ex-r "+waitingKey+" "+x;
			Client c=this.getLinkToUp();
			StringIO sio=c.sio;
			try{
				sio.writeString(sending);
			}
			catch(Exception e){
				return "na";
			}
			return "ok";
		}
		if(command.startsWith("askToDown-nw ")){ // askToDown-nw [l/r] waitingKey command
			String x=command.substring("askToDown-nw ".length());
			String lr=x.substring(0,1);
			x=x.substring(2);
			StringTokenizer st=new StringTokenizer(x);
			String waitingKey=st.nextToken();
			StringIO sio=null;
			if(lr.equals("l")){
				sio=this.getLeftLink();
			}
			else
			if(lr.equals("r")){
				sio=this.getRightLink();
			}
			if(sio==null){
				return "na";
			}
			String xx=x.substring(waitingKey.length()+1);
			String sending="nextDoor ex-r "+waitingKey+" "+xx;
			Client c=this.getLinkToUp();
			try{
				sio.writeString(sending);
			}
			catch(Exception e){
				return "na";
			}
			
			return "ok";
		}
		if(command.startsWith("returnToUp ")){
			String subc=command.substring("returnToUp ".length());
			String x=command.substring("returnToUp ".length());
			String sending="nextDoor ex-r "+x;
			Client c=this.getLinkToUp();
			StringIO sio=c.sio;
			try{
				sio.writeString(sending);
			}
			catch(Exception e){
				return "na";
			}
			return "ok";
		}
		if(command.startsWith("returnFromNext ")){
			String x=command.substring("returnFromNext ".length());
			StringTokenizer st=new StringTokenizer(x);
			String waitingKey=st.nextToken();
			System.out.println(" ... waitingKey="+waitingKey);
			String rx=command.substring(("returnFromNext "+waitingKey).length());
			synchronized(returnMessage){
				returnMessage.put(waitingKey,rx);
			}
			return "ok";
		}
		if(command.startsWith("waitForReturn ")){
			String x=command.substring("waitForReturn ".length());
			StringTokenizer st=new StringTokenizer(x);
			String waitingKey=st.nextToken();
			return this.getLastReturn(waitingKey);
		}
    	
    	if(command.startsWith("nextDoor ")){
    		String x=command.substring("nextDoor ".length());
    		StringTokenizer st=new StringTokenizer(x);
    		String subc=st.nextToken();
    		if(subc.equals("return-ex-r")){
    			String ulr=st.nextToken();
    			String rest=command.substring("nextDoor return-ex-r * ".length());
    			if(ulr.equals("u")){
    				StringIO sio=(this.getLinkToUp()).sio;
    				try{
    					if(sio!=null)
    					sio.writeString("nextDoor return-ex-r "+rest);
    				}
    				catch(Exception e){}
    			}
    			else
    			if(ulr.equals("l")){
    				StringIO sio=this.getLeftLink();
    				try{
    					if(sio!=null)
    					sio.writeString("nextDoor return-ex-r "+rest);
    				}
    				catch(Exception e){}
    			}
    			else
        		if(ulr.equals("r")){
        			StringIO sio=this.getRightLink();
        			try{
        				if(sio!=null)
        				sio.writeString("nextDoor return-ex-r "+rest);
        			}
        			catch(Exception e){}
        		}
    		}
    	}
    	/*
    	WaitForTheReturn wftr=new WaitForTheReturn(
    			this.communicationNode,application,param,
    			"node","nextDoor return-ex-r "+ulr+" "+waitingKey);
        */
		return null;
	}
	public void setFixing(boolean x){
		this.nodeController.setFixing(x);
	}
	Vector<AMessage> keyFrame;
	public Vector<AMessage> getKeyFrame(){
		keyFrame=new Vector();
	    for(int i=0;i<buttons.size();i++){
	        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
	        this.addKeyFrames(keyFrame, b.getKeyFrame("cnode"));
	    }
		Vector<AMessage> kf=this.applicationManager.getKeyFrame();
		this.addKeyFrames(keyFrame, kf);
		return keyFrame;
	}

}
