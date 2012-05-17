/*
		A basic implementation of the JFrame class.
*/
package nodesystem.eventrecorder;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import nodesystem.*;

import controlledparts.ControlledButton;
import controlledparts.ControlledCheckBox;
import controlledparts.ControlledTextArea;
import controlledparts.InputQueue;
import controlledparts.SelectedButton;

import javax.swing.border.BevelBorder; 
import javax.swing.border.TitledBorder; 
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

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
public class EventRecorder extends javax.swing.JFrame implements controlledparts.FrameWithControlledButton, controlledparts.FrameWithControlledCheckBox, controlledparts.FrameWithControlledTextAreas, java.lang.Runnable      
{
    String stateIndicatorValue;

    public void setStateIndicator(String x)
    {
        this.stateIndicatorValue=x;
    }

    public void updateFields()
    {
    	if(this.isControlledByLocalUser()){
          if(this.communicationNode.isGuiLockOccupied("er-ufield")) return;
    		instructionField.setText(this.instructionFieldText);
	    	this.timeField.setText(""+this.timeFieldValue);
		    this.stateIndicatorField.setText(this.stateIndicatorValue);
		    this.communicationNode.releaseGuiLock("er-ufield");
    	} else{
    		instructionField.setText(this.instructionFieldText);
	    	this.timeField.setText(""+this.timeFieldValue);
		    this.stateIndicatorField.setText(this.stateIndicatorValue);
    	}
   }

    public int timeFieldValue;

    public String instructionFieldText;

    public int previousDisplayTime;

    public void startPlay()
    {
    	System.out.println("startPlay");
            this.previousControlState=communicationNode.getControlMode();
//     		stateIndicatorField.setText("play");
            this.setStateIndicator("play");
            this.updateFields();
            endOfTheCommandSequence=(log.maxIndex);
//          this.currentCommandIndex=0;
            int timex=this.getTimeAt(this.currentCommandIndex);
            System.out.println("timer="+timer.getMilliTime());
            if(timex<0) return;
//            this.previousTime=timer.getTime();
            timer.stop();
           this.previousTime=timer.getMilliTime();
            System.out.println("(startPlay)Timer="+previousTime);
            
		    if(endOfTheCommandSequence>0){
//	    	    timer.setTime(timex);
//              timer.setTime(0);
		    	this.updateTime(timex);
                String xtimes=timeField.getText();
//                int xtime=0;
//                this.previousDisplayTime=0;
//                xtime=(new Integer(xtime)).intValue();
//                timer.setTime(xtime);
//                timer.setMilliTime(xtime);
                timer.setMilliTime(timex);
    		    timer.start();
    		    if(communicationNode.isSending()){
    		    	AMessage m=new AMessage();
    		    	m.setHead("broadcast shell settime "+timex //xtime
                      +commandTranceiver.endOfACommand);
//                this.commandTranceiver.ebuff.putS("broadcast shell settime "+xtime //timex
//                      +commandTranceiver.endOfACommand);
    		    	this.commandTranceiver.ebuff.putS(m);
    		    }
                if(!this.communicationNode.nodeController.getGroupMode().equals("P2P")){
                    this.communicationNode.nextDoorControl("nextDoor setUpnodeTime",null);
                }
//                this.previousReceivingEventState=this.communicationNode.isReceivingEvents;
//                this.communicationNode.isReceivingEvents=true;
                if(this.communicationNode.getControlMode().equals("local")){
                	this.communicationNode.setControlMode("receive");
                }
                this.communicationNode.rmouseIsActivated=true;
                if(this.communicationNode.getControlMode().equals("withReflector")){
                    this.communicationNode.directEvent=false;
                }
                else{
                    this.communicationNode.isReceivingEvents=true;
                    this.communicationNode.directEvent=false;
                }
                /*
                this.communicationNode.setControlMode("remote");
                if(this.previousControlState.equals("teach"))
                       communicationNode.setControlMode("receive");
                */
                System.out.println("timer="+timer.getMilliTime());
                this.start();
            }
    }

    public void startRecord()
    {
//    		stateIndicatorField.setText("record");
            this.setStateIndicator("record");
            this.updateFields();
	    	this.previousTime=// timer.getTime();
	    	                   timer.getMilliTime();
	    	timer.stop();
	    	this.stop();
//		    timer.setTime(0);
            timer.setMilliTime(0);
		    timer.start();
		    this.previousDisplayTime=0;
		    currentCommandIndex=0;
		    log.maxIndex=0;
//            int timeNow=timer.getTime();
            int timeNow=timer.getMilliTime();
//            timeField.setText(""+timeNow);
            this.updateTime(timeNow);
//            instructionField.setText(""+timeNow+":"+"");
            this.updateInstructionField(""+timeNow+":"+"");
            if(!this.communicationNode.nodeController.getGroupMode().equals("P2P")){
                    this.communicationNode.nextDoorControl("nextDoor setUpnodeTime",null);
            }
		
//		    timeTable=new Vector(500);
//		    commandSequence=new Vector(500);
//		    myOpCheck.setState(true);
            this.previousControlState=communicationNode.getControlMode();
//	    	myOpCheck.setSelected(true);
    }

    public void updateInstructionField(String x)
    {
        this.instructionFieldText=x;
    }

    public void updateTime(int time)
    {
        this.timeFieldValue=time;
    }

    boolean playing;
    public boolean isPlaying(){
    	return playing;
    }
    public void setPlaying(boolean f){
    	this.playing=f;
    }
    
    void playOneCommand(AMessage m)
    {
    	this.setPlaying(true);
    	String x=m.getHead();
        String xx="";
        if(x.startsWith("#")) return;
        AMessage mx=new AMessage();
        mx.setData(m.getData(),m.getDataLength());
        if(x.indexOf("m-")==0){
            xx=x.substring(2);
            if(this.myOpCheck.isSelected()){
//               commandTranceiver.parseACommandWithComModeForPlay(xx);
            	mx.setHead(xx);
                 this.communicationNode.sendEventFromPlayer(mx);
            }
       }
        if(x.indexOf("o-")==0){
            xx=x.substring(2);
            if(this.otrOpCheck.isSelected()){
//               commandTranceiver.parseACommandWithComModeForPlay(xx);
            	mx.setHead(xx);
                 this.communicationNode.sendEventFromPlayer(mx);
//                 this.commandTranceiver.receiveEventQueue.put(xx);
            }
       }
        this.setPlaying(false);
     }

    Vector checkBoxes;

    public void changeStateCheckBox(int i, int x)
    {
        // This method is derived from interface controlledparts.FrameWithControlledCheckBox
        // to do: code goes here
        ControlledCheckBox b=(ControlledCheckBox)(checkBoxes.elementAt(i));
        b.changeState(x);
        this.stateChangedAtCheckBox(i,x);
    }

    public void focusCheckBox(int i)
    {
        // This method is derived from interface controlledparts.FrameWithControlledCheckBox
        // to do: code goes here
            ControlledCheckBox b=(ControlledCheckBox)(checkBoxes.elementAt(i));
            b.focus();
    }

    public void mouseEnteredAtCheckBox(int i)
    {
        // This method is derived from interface controlledparts.FrameWithControlledCheckBox
        // to do: code goes here
    }

    public void mouseExitedAtCheckBox(int i)
    {
        // This method is derived from interface controlledparts.FrameWithControlledCheckBox
        // to do: code goes here
    }

    public void stateChangedAtCheckBox(int i, int x)
    {
        // This method is derived from interface controlledparts.FrameWithControlledCheckBox
        // to do: code goes here
    }

    public void unfocusCheckBox(int i)
    {
        // This method is derived from interface controlledparts.FrameWithControlledCheckBox
        // to do: code goes here
            ControlledCheckBox b=(ControlledCheckBox)(checkBoxes.elementAt(i));
            b.unFocus();
    }

    public boolean isDirectOperation()
    {
        if(this.controlMode.equals("local")) return true;
        return false;
    }

    public boolean isRecording()
    {
        String state=stateIndicatorValue;
        boolean rtn=state.equals("record");
        return rtn;
    }

    public Vector textFields;
//    private JCheckBox playAllCheck;
    controlledparts.ControlledCheckBox playAllCheck = new controlledparts.ControlledCheckBox();

    public void clickMouseOnTheText(int i, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void dragMouseOnTheText(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void enterMouseOnTheText(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void enterPressed(int id)
    {
        // This method is derived from interface FrameWithControlledTextField
        // to do: code goes here
    }

    public void exitMouseOnTheText(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public boolean isShowingRmouse()
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        return false;
    }

    public void keyIsTypedAtATextArea(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

	public void keyIsPressedAtATextArea(int i, int p, int key)
	{
		// This method is derived from interface FrameWithControlledTextAreas
		// to do: code goes here
	}

    public void mouseClickedAtTextArea(int i, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseDraggedAtTextArea(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseEnteredAtTheText(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseExitAtTheText(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseMoveAtTextArea(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mousePressedAtTextArea(int i, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseReleasedAtTextArea(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void moveMouseOnTheText(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void pressMouseOnTheText(int i, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void releaseMouseOnTheText(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void setTextOnTheText(int i, int pos, String s)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void typeKey(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledButton
        // to do: code goes here
//        return true;
        if(this.controlMode.equals("local")) return true;
        return false;
    }
    
    public boolean isPlayAllChecked(){
    	if(this.playAllCheck.isSelected()) return true;
    	return false;
    }

    public int previousTime;

    public String previousControlState;

    boolean previousReceivingEventState;

    public String communicationMode;

    public boolean isReceivingEvents;

    public boolean sendEventFlag;

    public String controlMode;


    public void changeControlMode(String mode)
    {
 /*
      setMode
         
         setting remote control mode
         mode: "local"  ... local execution, without remote controlled
               "broadcast" ... sendevent
               "remote" ... remote controlled (passive)
               "play"   ... remote control    (active)
                
 */
         this.controlModeLabel.setText(mode);
         if(mode.equals("local")){
            this.communicationMode="local";
            this.controlMode="local";
             this.sendEventFlag=false;
            this.isReceivingEvents=false;
             return;
        }
        if(mode.equals("remote")){
            this.communicationMode="local";
            this.controlMode="remote";
            this.sendEventFlag=false;
            this.isReceivingEvents=true;
            return;
        }
         if(mode.equals("play")){
            this.communicationMode="broadcast";
            this.sendEventFlag=true;
            this.isReceivingEvents=false;
            return;
        }

    }
    
    public void receiveEvent(String s)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
        if(!this.isReceivingEvents) return;
        InputQueue iq=null;
        try{
           BufferedReader dinstream=new BufferedReader(
//              new InputStreamReader(
//               new StringBufferInputStream(s),this.communicationNode.encodingCode)
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
        ParseEventRecorderEvent evParser=new ParseEventRecorderEvent(this,iq);
        evParser.parsingString=s;
        evParser.run();
    }
    public void sendEvent(String s)
    {
        if(!sendEventFlag) return;
        if(this.communicationNode!=null){
            /*
             this.communicationNode.commandTranceiver.ebuff.putS(
               communicationMode,"eventRecorder",s);
            */
//            this.communicationNode.sendEventForRecorder(s);
            String sending=this.communicationMode+" eventRecorder "+s
                        +this.communicationNode.commandTranceiver.endOfACommand;
            this.communicationNode.commandTranceiver.sendCommandForRecorders(sending);
        }
    }

    public boolean isSendingEvent;

    public void closeMessage()
    {
        mes.close();
    }

    void stopRecord()
    {
        stop();
// 		stateIndicatorField.setText("stop");
        this.setStateIndicator("stop");
        this.updateFields();
 		int timex=0;
		AMessage x=null;
		this.log.update();
        endOfTheCommandSequence=currentCommandIndex;
		if(currentCommandIndex>0){
//		    currentCommandIndex--;
		    timex=this.getTimeAt(currentCommandIndex-1);
		    x=this.getDataAt(currentCommandIndex-1);
	    }
//	    timeField.setText(""+timex);
        this.updateTime(timex);
        this.updateInstructionField(""+timex+":"+x.getHead());
//	    instructionField.setText(""+timex+":"+x);
	    timer.stop();
        timer.setMilliTime(this.previousTime);
        System.out.println("time="+timer.getMilliTime());
        timer.start();
// 		timer.setTime(0);
    }

    void stopPlay()
    {
        stop();
        this.setStateIndicator("stop");
        this.updateFields();
 		stateIndicatorField.setText("stop");
	    timer.stop();
        if(this.communicationNode.sendEventFlag){
        	AMessage m=new AMessage();
        	m.setHead("broadcast shell updatemessages"
             + commandTranceiver.endOfACommand);
//          this.commandTranceiver.ebuff.putS("broadcast shell updatemessages"
//             + commandTranceiver.endOfACommand);
        	this.commandTranceiver.ebuff.putS(m);
        }
//         this.communicationNode.isReceivingEvents=this.previousReceivingEventState;
        communicationNode.commandTranceiver.receiveEventQueue.flush();
        communicationNode.setControlMode(this.previousControlState);
//        timer.setTime(this.previousTime);
        timer.setMilliTime(this.previousTime);
        timer.start();
   }

    public int theLastTime;

    public Vector buttons;

    public void unfocusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
            button.unFocus();
    }

    public void mouseExitedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        sendEvent("btn.exit("+i+")\n");
    }

    public void mouseEnteredAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        sendEvent("btn.enter("+i+")\n");
    }

    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        /*
		buttons.addElement( toHeadButton);
		buttons.addElement(	stepBackButton);
		buttons.addElement(	stopButton);
		buttons.addElement(	pauseButton);
		buttons.addElement(	recordButton);
		buttons.addElement(	playButton);
		buttons.addElement(	stepButton);
		buttons.addElement(	toEndButton);
		buttons.addElement(	previousButton);
		buttons.addElement(	nextButton);
		buttons.addElement( showMessage);
		buttons.addElement( showRecordMessage);
		*/
		if(i==this.toHeadButton.getID()){ //buttons.addElement( toHeadButton);
//    		stateIndicatorField.setText("rewind");
            this.setStateIndicator("rewind");
//	    	repaint();
            this.updateFields();
		    try{
		        Thread.sleep(100);
		    }
		    catch(InterruptedException e){}
//		    stateIndicatorField.setText("stop");
            this.setStateIndicator("stop");
            this.updateFields();
		    stop();
		    currentCommandIndex=0;
            int timex=getTimeAt(currentCommandIndex);
            AMessage x=this.getDataAt(currentCommandIndex);
//            timeField.setText(""+0);
            this.updateTime(0);
//		    instructionField.setText(""+timex+":"+x);
            this.updateInstructionField(""+timex+":"+x.getHead());
            this.updateFields();
            this.stop();
            this.timer.stop();
            this.timer.setMilliTime(timex);
		    return;
		}
		else
		if(i==this.stepBackButton.getID()){ //buttons.addElement(	stepBackButton);
    		if(currentCommandIndex<1) return;
            int px=this.searchPreviousKeyIndex(currentCommandIndex);
            this.renderKeyFrame(currentCommandIndex);
            currentCommandIndex=px;
            int timex=getTimeAt(currentCommandIndex);
            AMessage m=getDataAt(currentCommandIndex);
            String x=m.getHead();
//	        timeField.setText(""+timex);
            this.updateTime(timex);
//	        instructionField.setText(""+timex+":"+x);
            this.updateInstructionField(""+timex+":"+x);
            this.updateFields();
//        commandTranceiver.parseCommandsForPlay(x);
//            currentCommandIndex++;
//            timer.setTime(timex);
            this.stop();
            timer.stop();
            timer.setMilliTime(timex);
            System.out.println("timer="+timer.getMilliTime());
            return;
        }
		else
		if(i==this.stopButton.getID()){ //buttons.addElement(	stopButton);
	    	if(this.stateIndicatorValue.equals("play")){
		        stopPlay();
		    }
		    if(this.stateIndicatorValue.equals("record")){
		        stopRecord();
		    }
		    this.setStateIndicator("stop");
		    this.updateFields();
		    return;
		}
		else
		if(i==this.pauseButton.getID()){ //buttons.addElement(	pauseButton);
//    		stateIndicatorField.setText("pause");
            this.setStateIndicator("pause");
	    	timer.stop();
		    stop();
		    this.updateFields();
		    return;
		}
		else
		if(i==this.recordButton.getID()){ //buttons.addElement(	recordButton);
		    startRecord();
	    	return;
		}
		else
		if(i==this.playButton.getID()){ //buttons.addElement(	playButton);
			System.out.println("playButton timer="+timer.getMilliTime());
		    startPlay();
            return;
		}	
		else
		if(i==this.stepButton.getID()){ //buttons.addElement(	stepButton);
            int px=this.searchNextKeyIndex(currentCommandIndex);
            currentCommandIndex=px;
            this.renderKeyFrame(currentCommandIndex);
            int timex=getTimeAt(currentCommandIndex);
            AMessage m=getDataAt(currentCommandIndex);
            String x=(getDataAt(currentCommandIndex)).getHead();
//	        timeField.setText(""+timex);
            this.updateTime(timex);
//	        instructionField.setText(""+timex+":"+x);
            this.updateInstructionField(""+timex+":"+x);
//          commandTranceiver.parseCommandsForPlay(x);
//            currentCommandIndex++;
//            timer.setTime(timex);
            this.stop();
            timer.setMilliTime(timex);
            System.out.println("(step button)Timer="+timex);
            this.timer.stop();
            System.out.println("(step button 1)timer="+timer.getMilliTime());
            this.updateFields();
            System.out.println("(step button2)Timer="+timer.getMilliTime());

		}
		else
		if(i==this.toEndButton.getID()){ //buttons.addElement(	toEndButton);
		}
		else
		if(i==this.previousButton.getID()){ //buttons.addElement(	previousButton);
            currentCommandIndex--;
            this.updateFields();
		}
		else
		if(i==this.nextButton.getID()){ //buttons.addElement(	nextButton);
//          String x=getDataAt(currentCommandIndex);
			AMessage x=getDataAt(currentCommandIndex);
            int timex=getTimeAt(currentCommandIndex);
//		    timeField.setText(""+timex);
            this.updateTime(timex);
//		    instructionField.setText(""+timex+":"+x);
            this.updateInstructionField(""+timex+":"+x.getHead());
//            x.setHead(x.getHead()+this.communicationNode.commandTranceiver.endOfACommand);
            this.updateFields();
//            commandTranceiver.parseCommandsForPlay(x);
            this.playOneCommand(x);
            return;
		}
		else
		if(i==this.showMessage.getID()){ //buttons.addElement( showMessage);
	    	if(stateIndicatorValue.equals("play")) return;
		    if(stateIndicatorValue.equals("record")) return;
		    messageFrame.show();
		    return;
		}
		else
		if(i==this.showRecordMessage.getID()){ //buttons.addElement( showRecordMessage);
		    if(stateIndicatorValue.equals("play")) return;
		    if(stateIndicatorValue.equals("record")) return;
		    recordFrame.show();
		    return;
		}
		else
		if(i==this.closeButton.getID()){ // close button
		    this.hide();
		}
    }
    public int searchPreviousKeyIndex(int index){
    	index--;index--;
    	if(index<=0) return 0;
    	for(int i=index;i>=0;i--){
            AMessage m=getDataAt(i);
            String h=m.getHead();
            if(h.startsWith("#KeyFrame")){
            	return i;
            }
    	}
    	return 0;
    }
    public int searchNextKeyIndex(int index){
    	index++;
    	for(int i=index;i<log.getMaxIndex();i++){
            AMessage m=getDataAt(i);
            String h=m.getHead();
            if(h.startsWith("#KeyFrame")){
            	return i;
            }
    	}
    	return 0;
    }

    public void renderKeyFrame(int index){
	   String fileName="KeyFrame-"+index;
       File keyFrameDir=new File(communicationNode.logDataRoot,fileName);
	    if(!keyFrameDir.exists()) 
	    	         keyFrameDir.mkdir();
	   String filePath=keyFrameDir.getPath();
	   BlockedFileManager fm=new BlockedFileManager(filePath,
			   communicationNode.encodingCode);
	   int max=fm.getMaxIndex();
	   for(int i=0;i<max;i++){
		   AMessage m=fm.getMessageAt(i);
           this.communicationNode.sendEventFromPlayer(m);
	   }
    }

    public void focusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            ControlledButton button=(ControlledButton)(buttons.elementAt(i));
            button.focus();
    }

    public void clickButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        ControlledButton t=(ControlledButton)(buttons.elementAt(i));
        t.click();
        System.out.println("clickButton Timer="+timer.getMilliTime());
        this.mouseClickedAtButton(i);
    }

    public CommunicationNode communicationNode;

    public int endOfTheCommandSequence;

    public int currentCommandIndex;

    public Logger messageFrame;

    public Logger recordFrame;

    public Date date;

    public Timer timer;

    public Thread me;

//    public Timer timerForPlay;

    public CommandTranceiver commandTranceiver;
    public void init(CommandTranceiver ct, CommunicationNode cn)
    {
        commandTranceiver=ct;
        communicationNode=cn;
//        date=new Date();
//        date.setTime((long)0);
        initLog();
//        timeTable=new Vector();
//		commandSequence=new Vector();
//		timer=new Timer();
        timer=Timer.getTimer();
		this.currentCommandIndex=0;
		AMessage m=this.getDataAt(currentCommandIndex);
		String x="";
		if(m!=null){
		    x=m.getHead();		    
		}
		if(!(x.equals(""))){
		    int timex=this.getTimeAt(currentCommandIndex);
//		    timeField.setText(""+timex);
            this.updateTime(timex);
//		    instructionField.setText(""+timex+":"+x);
            this.updateInstructionField(""+timex+":"+x);
		    this.endOfTheCommandSequence=this.log.maxIndex-1;
		    this.updateFields();
		}
    }
/*
    public synchronized void recordMessage(String s)
    {
//        int timeNow=timer.getTime();
            int timeNow=timer.getMilliTime();
//            messageFrame.logArea.appendText("\n"+timeNow+":"+s);
            putTimeAndMessageAt(timeNow,s+"\n",currentMessageIndex);
            currentMessageIndex++;
            notifyAll();

    }
    */
    int keyFrameTerm=60*1000;
    int lastKeyFrameTime;
    private void recordMessage(AMessage m)
    {
        int timeNow=timer.getTime();
        	m.setHead(m.getHead()+"\n");
//            messageFrame.logArea.appendText("\n"+timeNow+":"+s);
//            putTimeAndMessageAt(timeNow,s+"\n",currentMessageIndex);
            putTimeAndMessageAt(timeNow, m,currentMessageIndex);
            currentMessageIndex++;
//            notifyAll();

    }
    public void recordMessage(String s)
    {
//        int timeNow=timer.getTime();
    	AMessage m=new AMessage();
    	m.setHead(s);
            int timeNow=timer.getMilliTime();
            this.recordMessage(m);
//            messageFrame.logArea.appendText("\n"+timeNow+":"+s);
//            putTimeAndMessageAt(timeNow,s+"\n",currentMessageIndex);
//            putTimeAndMessageAt(timeNow,m,currentMessageIndex);
//           currentMessageIndex++;
//            notifyAll();

    }    
    public int currentMessageIndex;
   // synchronized 
   void putTimeAndMessageAt(int time,AMessage m,int index)
    {
        /*
         mes.putStringAt(""+time,index*2);
         mes.putStringAt(command,index*2+1);
         */
	   String command=m.getHead();
         String x="\""+this.communicationNode.thisID+"\","+time+",";
         x=x+command;
         m.setHead(x);
         mes.putMessageAt(m,index);
    }
   Vector<String> keyFrames;
   void putTimeAndKeyFrameAt(int time,Vector<AMessage> m,int index)
   {
	   if(keyFrames==null){
		   keyFrames=new Vector();
	   }
	   String fileName="KeyFrame-"+index;
	   
	   keyFrames.addElement(fileName);
       File keyFrameDir=new File(communicationNode.logDataRoot,fileName);
	    if(!keyFrameDir.exists()) 
	    	         keyFrameDir.mkdir();
	   String fpath=keyFrameDir.getPath();
	   BlockedFileManager fm=new BlockedFileManager(fpath,communicationNode.encodingCode);
	   for(int i=0;i<m.size();i++){
		   fm.putMessageAt(m.elementAt(i), i);
	   }
	   fm.update();
       /*
        mes.putStringAt(""+time,index*2);
        mes.putStringAt(command,index*2+1);
        */
	   /*
	   String command=m.getHead();
        String x="\""+this.communicationNode.thisID+"\","+time+",";
        x=x+command;
        m.setHead(x);
        mes.putMessageAt(m,index);
        */
   }
   /*
   void putTimeAndMessageAt(int time, AMessage command,int index){
       String x="\""+this.communicationNode.thisID+"\","+time+",";
       mes.putMessageAt(x,command,index);	   
   }
   */
    public BlockedFileManager mes;
    public BlockedFileManager log;
    public void closeLog()
    {
        log.close();
    }
    public void initLog()
    {
        log=new BlockedFileManager((communicationNode.logDataDir).getPath()
                                   ,communicationNode.encodingCode);
        mes=new BlockedFileManager((communicationNode.mesDataDir).getPath()
                                   ,communicationNode.encodingCode);
		recordFrame=new Logger("Records","Logs",log);
		recordFrame.setIcons(communicationNode.getIconPlace());
		recordFrame.setCommunicationNode(this.communicationNode);
        recordFrame.fileFrame.setSeparator(this.communicationNode.getSeparator());
		recordFrame.setEventRecorder(this);
		messageFrame=new Logger("Messages","Messages",mes);
		messageFrame.setIcons(communicationNode.getIconPlace());
		messageFrame.setCommunicationNode(this.communicationNode);
        recordFrame.fileFrame.setSeparator(this.communicationNode.getSeparator());
		messageFrame.setEventRecorder(this);
    }
    
//    String getDataAt(int index)
    AMessage getDataAt(int index)
    {
    	AMessage m=log.getMessageAt(index);
    	if(m==null) return null;
    	AMessage mx=new AMessage();
    	mx.setData(m.getData(),m.getDataLength());
        String record=m.getHead();
        if(record==null) return null;
        if(record.equals("")) return null;
        int p=(record.substring(0,10)).indexOf(":");
        if(p<0) return m;
        String recordData=record.substring(p+1);
        mx.setHead(recordData);
        return mx;
    }
    int getTimeAt(int index)
    {
//    	if(index==1801){
//    		System.out.println("x");
//    	}
    	try{
    	AMessage m=log.getMessageAt(index);
    	if(m==null) {
    		this.updateTime(0);
    		return theLastTime;
    	}
        String record=m.getHead();
        if(record==null) return theLastTime;
        if(record.equals("")) return theLastTime;
        int p=(record.substring(0,10)).indexOf(":");
        if(p<0) return theLastTime;
        String recordTime=record.substring(0,p);
        theLastTime=(new Integer(recordTime)).intValue();
    	}
    	catch(Exception e){
    		System.out.println(e.toString());
    		System.out.println("index="+index);
    		e.printStackTrace();
    	}
        return theLastTime;
    }
    // synchronized 
    void putTimeAndDataAt(int time,AMessage command,int index)
    {
        /*
         log.putStringAt(""+time,index*2);
         log.putStringAt(command,index*2+1);
         */
//         log.putStringAt(""+time+":"+command,index);
    	String c=command.getHead();
    	/*
    	if(c.indexOf("img(")>=0){
    		System.out.println("img(");
    	}
    	*/
    	String s=""+time+":"+c;
    	command.setHead(s);

    	log.putMessageAt(command,index);
    }
    void saveFile(String dir, String file, String text)
    {

    }
    
    public void recordMyOp(AMessage s)
    {
//        System.out.println("recording-"+s);
        if(!this.myOpCheck.isSelected()) return;
        AMessage m=new AMessage();
        String xs="m-"+s.getHead();
//        int timeNow=timer.getTime();
        int timeNow=timer.getMilliTime();
//        timeField.setText(""+timeNow);
        this.updateTime(timeNow);
//        instructionField.setText(""+timeNow+":"+xs);
        this.updateInstructionField(""+timeNow+":"+xs);
        String state=stateIndicatorValue;
        if(state.equals("record")){
        	m.setHead(xs);
        	m.copyData(s.getData(),s.getDataLength());
            if(timeNow>lastKeyFrameTime+keyFrameTerm){
            	Vector<AMessage> currentStatus=communicationNode.getKeyFrame();
            	this.putTimeAndKeyFrameAt(timeNow, currentStatus, currentCommandIndex);
//            	log.putMessageAt(new AMessage(sx+"#KeyFrame-"+currentMessageIndex), currentMessageIndex);
            	putTimeAndDataAt(timeNow,new AMessage("#KeyFrame-"+currentCommandIndex),currentCommandIndex);
            	currentCommandIndex++;
            	lastKeyFrameTime=timeNow;
            }
            putTimeAndDataAt(timeNow,m,currentCommandIndex);
            currentCommandIndex++;
        }
//        notifyAll();
    }

//    public synchronized void recordOtrOp(String s)
    public void recordOtrOp(AMessage s)
    {
        if(!this.otrOpCheck.isSelected()) return;
        AMessage m=new AMessage();
        String xs="o-"+s.getHead();
//        int timeNow=timer.getTime();
        int timeNow=timer.getMilliTime();
//        timeField.setText(""+timeNow);
        this.updateTime(timeNow);
//        instructionField.setText(""+timeNow+":"+xs);
        this.updateInstructionField(""+timeNow+":"+xs);
        String state=stateIndicatorValue;
        if(state.equals("record")){            
//                putTimeAndDataAt(timeNow,xs,currentCommandIndex);
        	m.setHead(xs);
        	m.setData(s.getData());
        	m.setDataLength(s.getDataLength());
       	    putTimeAndDataAt(timeNow,m,currentCommandIndex);
            currentCommandIndex++;
         }
//         notifyAll();
    }

    public void start()
    {
        if(me==null){
            me=new Thread(this,"EventRecorder");
            me.start();
//            System.out.println("EventRecorder thread priority:"+me.getPriority());
        }
    }

    public void stop()
    {
        if(me!=null){
//            me.stop();
            me=null;
        }
//        log.close();
//        mes.close();
//        Timer.stop();
    }

    public void run()
    {
        int loopCounter=0;
        System.out.println("run Timer="+timer.getMilliTime());
         while(me!=null){
            try{ Thread.sleep(5);} catch(InterruptedException e){}
            advance();
            if(loopCounter%20==0) this.updateFields();
            loopCounter++;
            if(loopCounter>50) loopCounter=0;
        }
   }

    void advance()
    {
         if(currentCommandIndex>=endOfTheCommandSequence){
            // when reached the end
//            stopButton_Clicked(null);
            stopPlay();
            return;
        }

        if(currentCommandIndex<0) {
               stopPlay();
               return;
        }
//        int timeNow=timer.getTime();
        int timeNow=timer.getMilliTime();
// 		timeField.setText(""+timeNow);
        this.updateTime(timeNow);
//        int timex=((Integer)(timeTable.elementAt(currentCommandIndex))).intValue();
        int timex=getTimeAt(currentCommandIndex);
        if(timeNow>=timex){
//		    String x=(String)(commandSequence.elementAt(currentCommandIndex));
            AMessage m=getDataAt(currentCommandIndex);
               if(m!=null){
               String x=m.getHead();
//		    timeField.setText(""+timex);
               this.updateTime(timex);
		       String dx="";
		       if(x.length()>50){
		           dx=x.substring(0,49);
		       }
		       else{
		           dx=x;
		       }
		       /*
		       if(x.indexOf("img(")>=0){
		    	   System.out.println("img(");
		       }
		       */
//		       instructionField.setText(""+timex+":"+dx);
               this.updateInstructionField(""+timex+":"+dx);
		       this.playOneCommand(m);
            }
            currentCommandIndex++;
        }
   }
	public EventRecorder()
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		setSize(467,290);
		setVisible(false);
		{
		toHeadButton.setActionCommand("<<");
		toHeadButton.setToolTipText("go to the first");
		getContentPane().add(toHeadButton);
		toHeadButton.setBackground(new java.awt.Color(204,204,204));
		toHeadButton.setForeground(java.awt.Color.black);
		toHeadButton.setFont(new Font("Dialog", Font.BOLD, 12));
		toHeadButton.setBounds(24,84,48,36);
		toHeadButton.setIcon(new ImageIcon("images/rewind.GIF"));
		}
		{
		stepBackButton.setActionCommand("|<");
		stepBackButton.setToolTipText("step back");
		getContentPane().add(stepBackButton);
		stepBackButton.setBackground(new java.awt.Color(204,204,204));
		stepBackButton.setForeground(java.awt.Color.black);
		stepBackButton.setFont(new Font("Dialog", Font.BOLD, 12));
		stepBackButton.setBounds(72,84,48,36);
		stepBackButton.setIcon(new ImageIcon("images/stepback.GIF"));
		}
		{
		stopButton.setActionCommand("[]");
		stopButton.setToolTipText("stop");
		getContentPane().add(stopButton);
		stopButton.setBackground(new java.awt.Color(204,204,204));
		stopButton.setForeground(java.awt.Color.black);
		stopButton.setFont(new Font("Dialog", Font.BOLD, 12));
		stopButton.setBounds(120,84,48,36);
		stopButton.setIcon(new ImageIcon("images/stop2.GIF"));
		}
		{
		pauseButton.setActionCommand("||");
		pauseButton.setToolTipText("pose");
		getContentPane().add(pauseButton);
		pauseButton.setBackground(new java.awt.Color(204,204,204));
		pauseButton.setForeground(java.awt.Color.black);
		pauseButton.setFont(new Font("Dialog", Font.BOLD, 12));
		pauseButton.setBounds(168,84,48,36);
		pauseButton.setIcon(new ImageIcon("images/pause.GIF"));
		}
		{
		recordButton.setText("rec");
		recordButton.setActionCommand("rec");
		recordButton.setToolTipText("record");
		getContentPane().add(recordButton);
		recordButton.setBackground(new java.awt.Color(204,204,204));
		recordButton.setForeground(java.awt.Color.red);
		recordButton.setFont(new Font("Dialog", Font.BOLD, 12));
		recordButton.setBounds(216,84,60,36);
		}
		{
		playButton.setActionCommand(">");
		playButton.setToolTipText("play");
		getContentPane().add(playButton);
		playButton.setBackground(new java.awt.Color(204,204,204));
		playButton.setForeground(java.awt.Color.black);
		playButton.setFont(new Font("Dialog", Font.BOLD, 12));
		playButton.setBounds(276,84,48,36);
		playButton.setIcon(new ImageIcon("images/play.gif"));
		}
		{
		stepButton.setActionCommand(">|");
		stepButton.setToolTipText("step forward");
		getContentPane().add(stepButton);
		stepButton.setBackground(new java.awt.Color(204,204,204));
		stepButton.setForeground(java.awt.Color.black);
		stepButton.setFont(new Font("Dialog", Font.BOLD, 12));
		stepButton.setBounds(324,84,48,36);
		stepButton.setIcon(new ImageIcon("images/step.GIF"));
		}
		{
		toEndButton.setActionCommand(">>");
		toEndButton.setToolTipText("go to the end");
		getContentPane().add(toEndButton);
		toEndButton.setBackground(new java.awt.Color(204,204,204));
		toEndButton.setForeground(java.awt.Color.black);
		toEndButton.setFont(new Font("Dialog", Font.BOLD, 12));
		toEndButton.setBounds(372,84,48,36);
		toEndButton.setIcon(new ImageIcon("images/forward.GIF"));
		}
		{
		stateIndicatorField.setSelectionColor(new java.awt.Color(204,204,255));
		stateIndicatorField.setSelectedTextColor(java.awt.Color.black);
		stateIndicatorField.setCaretColor(java.awt.Color.black);
		stateIndicatorField.setBorder(titledBorder3);
		stateIndicatorField.setDisabledTextColor(new java.awt.Color(153,153,153));
		stateIndicatorField.setEditable(false);
		getContentPane().add(stateIndicatorField);
		stateIndicatorField.setBackground(java.awt.Color.lightGray);
		stateIndicatorField.setForeground(java.awt.Color.black);
		stateIndicatorField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		stateIndicatorField.setBounds(228,120,192,24);
		}
		{
		timeField.setSelectionColor(new java.awt.Color(204,204,255));
		timeField.setSelectedTextColor(java.awt.Color.black);
		timeField.setCaretColor(java.awt.Color.black);
		timeField.setBorder(titledBorder1);
		timeField.setDisabledTextColor(new java.awt.Color(153,153,153));
		timeField.setDoubleBuffered(true);
		timeField.setEditable(false);
		getContentPane().add(timeField);
		timeField.setBackground(java.awt.Color.lightGray);
		timeField.setForeground(java.awt.Color.black);
		timeField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		timeField.setBounds(24,144,72,48);
		}
		{
		instructionField.setSelectionColor(new java.awt.Color(204,204,255));
		instructionField.setSelectedTextColor(java.awt.Color.black);
		instructionField.setCaretColor(java.awt.Color.black);
		instructionField.setBorder(titledBorder2);
		instructionField.setDisabledTextColor(new java.awt.Color(153,153,153));
		instructionField.setDoubleBuffered(true);
		instructionField.setEditable(false);
		getContentPane().add(instructionField);
		instructionField.setBackground(java.awt.Color.lightGray);
		instructionField.setForeground(java.awt.Color.black);
		instructionField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		instructionField.setBounds(96,144,324,48);
		}
		{
		previousButton.setText("previous");
		previousButton.setActionCommand("previous");
		getContentPane().add(previousButton);
		previousButton.setBackground(new java.awt.Color(204,204,204));
		previousButton.setForeground(java.awt.Color.black);
		previousButton.setFont(new Font("Dialog", Font.BOLD, 12));
		previousButton.setBounds(24,192,84,24);
		}
		{
		nextButton.setText("next");
		nextButton.setActionCommand("next");
		getContentPane().add(nextButton);
		nextButton.setBackground(new java.awt.Color(204,204,204));
		nextButton.setForeground(java.awt.Color.black);
		nextButton.setFont(new Font("Dialog", Font.BOLD, 12));
		nextButton.setBounds(108,192,84,24);
		}
		{
		showRecordMessage.setText("showRecord");
		showRecordMessage.setActionCommand("showRecord");
		getContentPane().add(showRecordMessage);
		showRecordMessage.setBackground(new java.awt.Color(204,204,204));
		showRecordMessage.setForeground(java.awt.Color.black);
		showRecordMessage.setFont(new Font("Dialog", Font.BOLD, 12));
		showRecordMessage.setBounds(192,192,108,24);
		}
		{
		showMessage.setText("show Message");
		showMessage.setActionCommand("show Message");
		getContentPane().add(showMessage);
		showMessage.setBackground(new java.awt.Color(204,204,204));
		showMessage.setForeground(java.awt.Color.black);
		showMessage.setFont(new Font("Dialog", Font.BOLD, 12));
		showMessage.setBounds(300,192,120,24);
		}
		{
		JLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		JLabel1.setText("Distributed System Recorder/ Event Recorder");
		getContentPane().add(JLabel1);
		JLabel1.setBackground(new java.awt.Color(204,204,204));
		JLabel1.setForeground(new java.awt.Color(102,102,153));
		JLabel1.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel1.setBounds(0,12,372,36);
		JLabel1.setIcon(new ImageIcon("images/recorder-icon.GIF"));		
		}
		{
		controlModeLabel.setText("controlMode");
		getContentPane().add(controlModeLabel);
		controlModeLabel.setBounds(204,48,108,24);
		}
		{
		closeButton.setActionCommand("close");
		closeButton.setToolTipText("close, exit");
		getContentPane().add(closeButton);
		closeButton.setBackground(new java.awt.Color(204,204,204));
		closeButton.setForeground(java.awt.Color.black);
		closeButton.setFont(new Font("Dialog", Font.BOLD, 12));
		closeButton.setBounds(24,60,96,24);
		closeButton.setIcon(new ImageIcon("images/exit-icon.GIF"));
		}
		myOpCheck.setText("my opr");
		myOpCheck.setActionCommand("my opr");
		getContentPane().add(myOpCheck);
		myOpCheck.setBackground(new java.awt.Color(204,204,204));
		myOpCheck.setForeground(java.awt.Color.black);
		myOpCheck.setFont(new Font("Dialog", Font.BOLD, 12));
		myOpCheck.setBounds(24,120,96,24);
		otrOpCheck.setText("other\'s opr");
		otrOpCheck.setActionCommand("other\'s opr");
		getContentPane().add(otrOpCheck);
		otrOpCheck.setBackground(new java.awt.Color(204,204,204));
		otrOpCheck.setForeground(java.awt.Color.black);
		otrOpCheck.setFont(new Font("Dialog", Font.BOLD, 12));
		otrOpCheck.setBounds(120,120,108,24);
		{
//			playAllCheck = new JCheckBox();
			getContentPane().add(getPlayAllCheck());
			playAllCheck.setBackground(new java.awt.Color(204,204,204));
			playAllCheck.setForeground(java.awt.Color.black);
			playAllCheck.setFont(new Font("Dialog",Font.BOLD,12));
			playAllCheck.setText("play all");
			playAllCheck.setBounds(28, 217, 98, 28);
		}
		//$$ imageIcon3.move(204,288);
		//}}

		//{{INIT_MENUS
		//}}
	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		toHeadButton.addActionListener(lSymAction);
		stepBackButton.addActionListener(lSymAction);
		stopButton.addActionListener(lSymAction);
		pauseButton.addActionListener(lSymAction);
		recordButton.addActionListener(lSymAction);
		playButton.addActionListener(lSymAction);
		stepButton.addActionListener(lSymAction);
		toEndButton.addActionListener(lSymAction);
		previousButton.addActionListener(lSymAction);
		nextButton.addActionListener(lSymAction);
		showRecordMessage.addActionListener(lSymAction);
		showMessage.addActionListener(lSymAction);
		closeButton.addActionListener(lSymAction);
		SymItem lSymItem = new SymItem();
		myOpCheck.addItemListener(lSymItem);
		otrOpCheck.addItemListener(lSymItem);
		//}}
		stateIndicatorField.setText("stop");
		this.stateIndicatorValue="stop";
		currentCommandIndex=0;
		currentMessageIndex=0;
		
		buttons=new Vector();
		buttons.addElement( toHeadButton);
		buttons.addElement(	stepBackButton);
		buttons.addElement(	stopButton);
		buttons.addElement(	pauseButton);
		buttons.addElement(	recordButton);
		buttons.addElement(	playButton);
		buttons.addElement(	stepButton);
		buttons.addElement(	toEndButton);
		buttons.addElement(	previousButton);
		buttons.addElement(	nextButton);
		buttons.addElement( showMessage);
		buttons.addElement( showRecordMessage);
		buttons.addElement( this.closeButton);
	
	    int numberOfButtons=buttons.size();
	    for(int i=0;i<numberOfButtons;i++){
	        SelectedButton b=(SelectedButton)(buttons.elementAt(i));
	        b.setFrame(this);
	        b.setID(i);
	    }
	    
	    textFields=new Vector();
	    textFields.addElement(this.timeField);
	    textFields.addElement(this.stateIndicatorField);
	    textFields.addElement(this.instructionField);
	    int numberOfField=textFields.size();
	    for(int i=0;i<numberOfField;i++){
	        ControlledTextArea f=(ControlledTextArea)(textFields.elementAt(i));
	        f.setFrame(this);
	        f.setID(i);
	    }
	    
    	checkBoxes=new Vector();
	    checkBoxes.addElement(this.myOpCheck);
	    checkBoxes.addElement(this.otrOpCheck);
	    checkBoxes.addElement(this.playAllCheck);
	    int numberOfCheckBox=checkBoxes.size();
	    for(int i=0;i<numberOfCheckBox;i++){
	        ControlledCheckBox t=(ControlledCheckBox)(checkBoxes.elementAt(i));
	        t.setFrame(this);
	        t.setID(i);
	    }

	    isSendingEvent=true;
	    this.setTitle("EventRecorder");
	    this.changeControlMode("local");
	    this.myOpCheck.setSelected(true);
	}

	public EventRecorder(String sTitle)
	{
		this();
		setTitle(sTitle);
	}

	public void setVisible(boolean b)
	{
		if (b)
			setLocation(50, 50);
		super.setVisible(b);
	}

	static public void main(String args[])
	{
		(new EventRecorder()).setVisible(true);
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
	controlledparts.ControlledButton toHeadButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton stepBackButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton stopButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton pauseButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton recordButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton playButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton stepButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton toEndButton = new controlledparts.ControlledButton();
	controlledparts.ControlledTextArea stateIndicatorField = new controlledparts.ControlledTextArea();
	controlledparts.ControlledTextArea timeField = new controlledparts.ControlledTextArea();
	controlledparts.ControlledTextArea instructionField = new controlledparts.ControlledTextArea();
	controlledparts.ControlledButton previousButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton nextButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton showRecordMessage = new controlledparts.ControlledButton();
	controlledparts.ControlledButton showMessage = new controlledparts.ControlledButton();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JLabel controlModeLabel = new javax.swing.JLabel();
	controlledparts.ControlledButton closeButton = new controlledparts.ControlledButton();
	BevelBorder bevelBorder1 = new BevelBorder(BevelBorder.LOWERED);
	TitledBorder titledBorder1 = new TitledBorder(bevelBorder1);
	TitledBorder titledBorder2 = new TitledBorder(bevelBorder1);
	TitledBorder titledBorder3 = new TitledBorder(bevelBorder1);
	controlledparts.ControlledCheckBox myOpCheck = new controlledparts.ControlledCheckBox();
	controlledparts.ControlledCheckBox otrOpCheck = new controlledparts.ControlledCheckBox();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == toHeadButton)
				toHeadButton_actionPerformed(event);
			else if (object == stepBackButton)
				stepBackButton_actionPerformed(event);
			else if (object == stopButton)
				stopButton_actionPerformed(event);
			else if (object == pauseButton)
				pauseButton_actionPerformed(event);
			else if (object == recordButton)
				recordButton_actionPerformed(event);
			else if (object == playButton)
				playButton_actionPerformed(event);
			else if (object == stepButton)
				stepButton_actionPerformed(event);
			else if (object == toEndButton)
				toEndButton_actionPerformed(event);
			else if (object == previousButton)
				previousButton_actionPerformed(event);
			else if (object == nextButton)
				nextButton_actionPerformed(event);
			else if (object == showRecordMessage)
				showRecordMessage_actionPerformed(event);
			else if (object == showMessage)
				showMessage_actionPerformed(event);
			else if (object == closeButton)
				closeButton_actionPerformed(event);
		}
	}

	void toHeadButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		toHeadButton_actionPerformed_Interaction1(event);
	}

	void toHeadButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
		/*
		stateIndicatorField.setText("rewind");
		repaint();
		try{
		    Thread.sleep(500);
		}
		catch(InterruptedException e){}
		stateIndicatorField.setText("stop");
		stop();
//		stopButton_actionPerformed_Interaction1(null);
//		stopButton_Clicked(null);
		currentCommandIndex=0;
        int timex=getTimeAt(currentCommandIndex);
        String x=this.getDataAt(currentCommandIndex);
	    timeField.setText(""+timex);
		instructionField.setText(""+timex+":"+x);
		*/
	}

	void stepBackButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		stepBackButton_actionPerformed_Interaction1(event);
	}

	void stepBackButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
	}

	void stopButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		stopButton_actionPerformed_Interaction1(event);
	}

	void stopButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
		/*
		if(this.stateIndicatorField.getText().equals("play")){
		    stopPlay();
		}
		if(this.stateIndicatorField.getText().equals("record")){
		    stopPlay();
		}
		*/
	}

	void pauseButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		pauseButton_actionPerformed_Interaction1(event);
	}

	void pauseButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
		/*
		stateIndicatorField.setText("pause");
		timer.stop();
		stop();
		*/
	}

	void recordButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		recordButton_actionPerformed_Interaction1(event);
	}

	void recordButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
	}

	void playButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		playButton_actionPerformed_Interaction1(event);
	}

	void playButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
	}

	void stepButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		stepButton_actionPerformed_Interaction1(event);
	}

	void stepButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
		/*
            String x=getDataAt(currentCommandIndex);
            int timex=getTimeAt(currentCommandIndex);
		    timeField.setText(""+timex);
		    instructionField.setText(""+timex+":"+x);
            commandTranceiver.parseCommandsForPlay(x);
            currentCommandIndex++;
            */
	}

	void toEndButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		toEndButton_actionPerformed_Interaction1(event);
	}

	void toEndButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
	}

	void previousButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		previousButton_actionPerformed_Interaction1(event);
	}

	void previousButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
		/*
		if(currentCommandIndex<1) return;
        currentCommandIndex=currentCommandIndex-1;
//        int timex=((Integer)(timeTable.elementAt(currentCommandIndex))).intValue();
        int timex=getTimeAt(currentCommandIndex);
//	    String x=(String)(commandSequence.elementAt(currentCommandIndex));
        String x=getDataAt(currentCommandIndex);
	    timeField.setText(""+timex);
	    instructionField.setText(""+timex+":"+x);
//        commandTranceiver.parseCommandsForPlay(x);
        currentCommandIndex++;
        timer.setTime(timex);
        */
	}

	void nextButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		nextButton_actionPerformed_Interaction1(event);
	}

	void nextButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
		/*
        int timex=getTimeAt(currentCommandIndex);
//	    String x=(String)(commandSequence.elementAt(currentCommandIndex));
        String x=getDataAt(currentCommandIndex);
	    timeField.setText(""+timex);
	    instructionField.setText(""+timex+":"+x);
//        commandTranceiver.parseCommandsForPlay(x);
        currentCommandIndex++;
        timer.setTime(timex);
        */
	}

	void showRecordMessage_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		showRecordMessage_actionPerformed_Interaction1(event);
	}

	void showRecordMessage_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
		/*
		if((stateIndicatorField.getText()).equals("play")) return;
		if((stateIndicatorField.getText()).equals("record")) return;
		recordFrame.show();
		*/
	}

	void showMessage_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		showMessage_actionPerformed_Interaction1(event);
	}

	void showMessage_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
		/*
		if((stateIndicatorField.getText()).equals("play")) return;
		if((stateIndicatorField.getText()).equals("record")) return;
		messageFrame.show();
		*/
	}

	void closeButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		closeButton_actionPerformed_Interaction1(event);
	}

	void closeButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			closeButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if (object == myOpCheck)
				myOpCheck_itemStateChanged(event);
			else if (object == otrOpCheck)
				otrOpCheck_itemStateChanged(event);
		}
	}

	void myOpCheck_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
			 
		myOpCheck_itemStateChanged_Interaction1(event);
	}

	void myOpCheck_itemStateChanged_Interaction1(java.awt.event.ItemEvent event)
	{
		try {
//			myOpCheck.show();
		} catch (java.lang.Exception e) {
		}
	}

	void otrOpCheck_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
			 
		otrOpCheck_itemStateChanged_Interaction1(event);
	}

	void otrOpCheck_itemStateChanged_Interaction1(java.awt.event.ItemEvent event)
	{
		try {
//			otrOpCheck.show();
		} catch (java.lang.Exception e) {
		}
	}
	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#keyIsReleasedAtTextArea(int, int)
	 */
	public void keyIsReleasedAtTextArea(int id, int p, int code) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#releaseKey(int, int)
	 */
	public void releaseKey(int i, int p, int code) {
		// TODO 自動生成されたメソッド・スタブ

	}
	
	public void pressKey(int i, int p, int code){
		
	}
	
	public JCheckBox getPlayAllCheck() {
		return playAllCheck;
	}

}