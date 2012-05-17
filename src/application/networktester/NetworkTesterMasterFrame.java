/*
		A basic implementation of the JFrame class.
*/
package application.networktester;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import nodesystem.Client;
import nodesystem.CommandTranceiver;
import nodesystem.CommunicationNode;
import nodesystem.DialogListener;
import nodesystem.EditDialog;
import application.basic.BasicFrame;
import application.texteditor.TextEditFrame;
import controlledparts.ControlledEditPane;
import controlledparts.ControlledFrame;
import controlledparts.ControlledScrollPane;
import controlledparts.ControlledTextArea;
import controlledparts.FrameWithControlledButton;
import controlledparts.FrameWithControlledFileDialog;
import controlledparts.FrameWithControlledPane;
import controlledparts.FrameWithControlledTextAreas;
import controlledparts.SelectedButton;

import javax.swing.border.*;

public class NetworkTesterMasterFrame extends ControlledFrame implements DialogListener, FrameWithControlledFileDialog, FrameWithControlledPane, FrameWithControlledTextAreas, FrameWithControlledButton  
{
    public String nextDoorControl(String s, Client c)
    {
        return null;
    }

    public void setTextOnTheText(int id, int pos, String s)
    {
        ControlledTextArea t=(ControlledTextArea)(this.texts.elementAt(id));
		t.setTextAt(pos,s); 
    }

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
        return true;
    }

    public boolean isShowingRmouse()
    {
        return false;
    }

    public void enterMouseOnTheText(int id, int x, int y)
    {
    }
    
    public void exitMouseOnTheText(int id, int x, int y)
    {
    }

    public void moveMouseOnTheText(int id, int x, int y)
    {
    }

    public void mouseMoveAtTextArea(int id, int x, int y)
    {
    }
    public void mouseEnteredAtTheText(int id, int x, int y)
    {
    }
    public void mouseExitAtTheText(int id, int x, int y)
    {
    }
    
    public void exitThis()
    {
        this.setVisible(false);
    }


    public void addProgram(String x)
    {
         this.basic.parseCommand("add-program r=ex(\"NetworkTesterMasterFrame\",\""
            + x + "\")");
    }

    public String parseCommand(String x)
    {
        if(x.indexOf("getNodeNumber")==0){
            return ""+this.nodes.size();
        }
        if(x.indexOf("getNodeIdAt ")==0){
            String xi=x.substring("getNodeInfoAt ".length());
            int i=(new Integer(xi)).intValue();
            Vector v=(Vector)(nodes.elementAt(i));
            int j=((Integer)(v.elementAt(0))).intValue();
            return ""+j;
        }
        if(x.indexOf("getNodeAddressAt ")==0){
            String xi=x.substring("getNodeInfoAt ".length());
            int i=(new Integer(xi)).intValue();
            Vector v=(Vector)(nodes.elementAt(i));
            return (String)(v.elementAt(1));
        }
        messagesArea.append(">"+x+"\n");
        if(!this.parse(x)) return null;
        return "";
    }

    BasicFrame basic;
    TextEditFrame textEditFrame;
    
    public void copyFromTheText(int i)
    {
        String theText="";
        String newText="";
        int startPosition=0;
        int endPosition=0;
       
        ControlledEditPane ta=(ControlledEditPane)(this.texts.elementAt(i));
        /*
            theText=ta.getText();
            startPosition=ta.getSelectionStart();
            if(startPosition<0) startPosition=0;
            endPosition=ta.getSelectionEnd();
            if(endPosition<startPosition){
                int w=startPosition; startPosition=endPosition;
                endPosition=w;
            }
            if(endPosition>theText.length())
               endPosition=theText.length();
               */
         newText=""+ta.getSelectedText();
         this.communicationNode.tempText=newText;
         return;
  }

    public void openTextEditor(DialogListener l)
    {
        this.basic=(BasicFrame)(this.lookUp("BasicFrame"));
        this.basic.setVisible(true);
    }

    public Vector panes;
    public Vector buttons;
    public Vector texts;
    Vector script;
    Random random;
    public int pID;
    int numberOfResult;
    Vector nodes;
    Vector nodeInfo;
    Thread me;

    public void comment()
    {
        /*
        Network Tester Master
        
        1. collect information of candidates for workers.
        2. generate the test scenario.
        3. spawn the workers.
        4. assing the scripts for the test to each workers.
        5. start the scriptes at each workers
        6. collect the results from the workers.
        
        
        
        */
    }
    public void executeScript()
    {
         this.basic.parseCommand("run");
    }
    public void generateScript1(int numberOfCombination, Vector nodes)
    {
        Vector sequences=new Vector();
        int n=nodes.size();
        int maxcombination=numberOfCombination;
        if(n*(n-1)/2<maxcombination) maxcombination=n*(n-1)/2;
        while(sequences.size()<maxcombination){
            int randomSeed=this.communicationNode.eventRecorder.timer.getMilliTime();
            try{Thread.sleep(5);}
            catch(InterruptedException e){}
            RandomSequence aSequence=new RandomSequence(n,randomSeed);
            aSequence.sortMe1();
            boolean news=true;
            for(int j=0;j<sequences.size();j++){
                RandomSequence w=(RandomSequence)(sequences.elementAt(j));
                if(w.equals(aSequence)){ news=false; break;}
            }
            if(news) 
            sequences.addElement(aSequence);
        }
        for(int i=0;i<sequences.size();i++){
           Vector oneSequence=new Vector();
           RandomSequence x=(RandomSequence)(sequences.elementAt(i));
            for(int j=0;j<n;j++){
                oneSequence.addElement( nodes.elementAt(x.numbers[j]));
            }
            generateScript2(oneSequence);
        }
         messagesArea.append("script1 is generated\n");
    }
    public void generateScript2(Vector nodes)
    {
        String command="";
        int connectedNodeNumber=nodes.size();
        connectedNodeNumber=(connectedNodeNumber/2)*2;
        int numberOfOnesideNodes=connectedNodeNumber/2;
        String unitSize=this.unitSizeArea.getText();
        String iterNum=this.timesArea.getText();
        this.addProgram("synctime");
        for(int i=0;i<connectedNodeNumber;i+=2){
                     Vector theNode=(Vector)(nodes.elementAt(i));
                      int nodeID=((Integer)(theNode.elementAt(0))).intValue();
                      // node i+m connect j+m
                     Vector anotherNode=(Vector)(nodes.elementAt(i+1));
//                      String address=((String)((Vector)anotherNode).elementAt(1));                      
//                      command="node "+nodeID+" set connect "+address;
                      int serverNode=((Integer)(anotherNode.elementAt(0))).intValue();
                      command="node "+nodeID+" set connectnode "+serverNode;
                      messagesArea.append("-"+command+"\n");
//                      script.addElement(command);
                      this.addProgram(command);
        }
        command="waittime 500";
           messagesArea.append("-"+command+"\n");
//        script.addElement(command);
        this.addProgram(command);
        command="all enter";
           messagesArea.append("-"+command+"\n");
//        script.addElement(command);
        this.addProgram(command);
        command="waittime 500";
           messagesArea.append("-"+command+"\n");
//        script.addElement(command);
        this.addProgram(command);
        
        for(int i=0;i<connectedNodeNumber;i+=2){
                    Vector theNode=(Vector)(nodes.elementAt(i));
                      int nodeID=((Integer)(theNode.elementAt(0))).intValue();
                      // node i+m set sendreceive-n size 10
                      command="node "+nodeID+" set sendreceive-n "+unitSize+" "+iterNum;
                      messagesArea.append("-"+command+"\n");
//                      script.addElement(command);
                      this.addProgram(command);
        }
        
        command="resetResultsCounter";
        messagesArea.append("-"+command+"\n");
//        script.addElement(command);
        this.addProgram(command);
                
        command="waittime 500";
          messagesArea.append("-"+command+"\n");
//        script.addElement(command);
        this.addProgram(command);
        command="all enter";
           messagesArea.append("-"+command+"\n");
//        script.addElement(command);
        this.addProgram(command);
        command="waittime 500";
        messagesArea.append("-"+command+"\n");
//        script.addElement(command);
        this.addProgram(command);
        command="waitallresults "+numberOfOnesideNodes;
           messagesArea.append("-"+command+"\n");
//        script.addElement(command);
        this.addProgram(command);
        command="waittime 500";
           messagesArea.append("-"+command+"\n");
//        script.addElement(command);
        this.addProgram(command);
        command="all disconnect";
           messagesArea.append("-"+command+"\n");
//        script.addElement(command);
        this.addProgram(command);
                
          
//        }
        messagesArea.append("script2 is generated\n");
    }
    public void getNodesInfo()
    {         
            nodes=new Vector();
            messagesArea.append("start collect nodes info\n");
//            sendEvent("broadcast","shell","spawn NetworkTesterWorker");
            String myaddress=this.communicationNode.getMyAddress();
			int slashPlace=myaddress.indexOf("/");
			String theAddress="";
			if(slashPlace>=1){
    			StringTokenizer st=new StringTokenizer(myaddress,"/");
	    		String dmy=st.nextToken();
		    	theAddress=st.nextToken();
			}
			else{
				theAddress=myaddress;
			}

            sendEvent("broadcast",workerCommand,"getnodeinfo "+this.communicationNode.serialNo);
           
   }
    public void getResultFromNodes(int id, String result)
    {
        messagesArea.append("node "+id+":"+result+"\n");
        this.communicationNode.eventRecorder.recordMessage(""+id+","+result);
        this.numberOfResult++;
        
    }
    public boolean parse(String command)
    {
        /*
        command:
          enter ... meta
          clear ... meta
          quit  ... meta
          set <command> ... meta""
          synctime
          connect <server-address>
          sendreceive <datasize>
          sendreceive-n <datasize> <times>
          send <datasize>
          receive
          load <file-name>
          testid <id>
          returnnodeinfo <return-address>        
        */
        if(command.indexOf("enter")==0){
            this.clickButton(1);
//            String x=s.substring("chat ".length());
            return true;
        }
        else
        if(command.indexOf("clear")==0){
            this.clickButton(1);
            return true;
        }
        else
        if(command.indexOf("quit")==0){
            this.clickButton(2);
            return true;
        }
        else
        if(command.indexOf("set ")==0){
            String x=command.substring("set ".length());
            // set <command>
            this.commandFieldArea.setText(x);
            return true;
        }
        else
        if(command.indexOf("synctime")==0){
            int timenow=this.communicationNode.eventRecorder.timer.getTime();
            this.sendEvent("broadcast","shell", "settime "+timenow);
            return true;
        }
        else
        if(command.indexOf("connect ")==0){
            String x=command.substring("connect ".length());
            // connect <remote-host>
//            connectServer(x);
            return true;
        }
        else
        if(command.indexOf("nodeinfo ")==0){
            BufferedReader dinstream=null;
            String x=command.substring("nodeinfo ".length());
            // nodeinfo <serialNo> <address>
            StringTokenizer st=new StringTokenizer(x," ");
            String serialNo=st.nextToken();
            String address=st.nextToken();
            int id;
            int times;
            try{
               id=(new Integer(serialNo)).intValue();
             }
            catch(Exception e){
                messagesArea.append("command format error\n");
                return false;
            }
            this.recordOneNodeInfo(id,address);
            return true;
        }
        else
        if(command.indexOf("resetResultsCounter")==0){
            this.numberOfResult=0;
            return true;
        }
        else
        if(command.indexOf("list")==0){
            if(script==null) return true;
            int length=script.size();
            for(int i=0;i<length;i++){
                String line=(String)(script.elementAt(i));
                this.messagesArea.append(line+"\n");
            }
            return true;
        }
        else
        if(command.indexOf("resultofnode ")==0){
            String x=command.substring("resultofnode ".length());
            // resultofnode <node-id> <theResult>
            StringTokenizer st=new StringTokenizer(x," ");
            String serialNo=st.nextToken();
            String theResult=st.nextToken();
            while(st.hasMoreTokens()){
                theResult=theResult+" ";
                theResult=theResult+st.nextToken();
            }
            int id=0;
            try{
               id=(new Integer(serialNo)).intValue();
             }
            catch(Exception e){
                messagesArea.append("command format error\n");
                return false;
            }
            this.getResultFromNodes(id,theResult);
            return true;
        }
        else
        if(command.indexOf("testid ")==0){
            String x=command.substring("testid ".length());
            // setTestID <testID>       
//            setTestID((new Integer(x)).intValue());
            return true;
        }
        else
        if(command.indexOf("getnodesinfo")==0){
            // getnodesinfo 
            getNodesInfo();
            return true;
        }
        else
        if(command.indexOf("spawnworkers")==0){
            // spawn workers
            this.sendEvent("broadcast","shell", "spawn NetworkTesterWorkerFrame remote");
            return true;
        }
        else
        if(command.indexOf("receive")==0){
            BufferedReader dinstream=null;
            String x=command.substring("receive".length());
            // receive            
            return true;
        }
        else
        if(command.indexOf("all ")==0){
            String x=command.substring("all ".length());
            // all <workers-command>
//            connectServer(x);
            this.sendEvent("broadcast",workerCommand,x);
            return true;
        }
        else
        if(command.indexOf("node ")==0){
            String x=command.substring("node ".length());
            StringTokenizer st=new StringTokenizer(x," ");
            String nodeID=st.nextToken();
            String theCommand=st.nextToken();
            while(st.hasMoreTokens()){
                theCommand=theCommand+" ";
                theCommand=theCommand+st.nextToken();
            }
            // node <nodeID> <workers-command>
//            connectServer(x);
            this.sendEvent("node "+nodeID,workerCommand,theCommand);
            return true;
        }
        else
        if(command.indexOf("waittime ")==0){
            String x=command.substring("waittime ".length());
            // waittime <millisec>
//            connectServer(x);
            this.waitTime((new Integer(x)).intValue());
            return true;
        }
        else
        if(command.indexOf("waitallresults ")==0){
            String x=command.substring("waitallresults ".length());
            // waitallresults <number>
            this.waitAllResults((new Integer(x)).intValue());
            return true;
        }
        else
        return false;
    }
    public void receiveEvent(String s)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
        if(parse(s)){
        }
        else{
            messagesArea.append("command failed while receive event.\n");
        };
    }
    public void recordOneNodeInfo(int id, String address)
    {
        Vector info=new Vector();
        info.addElement((new Integer(id)));
        info.addElement(address);
        nodes.addElement(info);
        messagesArea.append("node "+id+":"+address+"\n");
    }

    String commandName="networktestermaster";
    String workerCommand="networktesterworker";
    public String getCommandName(){
    	return commandName;
    }

    public void sendEvent(String x)
    {
    }
    public void sendEvent(String transmitMode , String command, String subcommand)
    {
        CommandTranceiver ct=this.communicationNode.commandTranceiver;
        if(ct==null) return;
        if(ebuff!=null)
        this.ebuff.putS(transmitMode+" "+command+" "+subcommand+ct.endOfACommand);
    }
    public ControlledFrame spawnMain(CommunicationNode gui, String args, int pID, String controlMode)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
//           NetworkTesterMasterFrame frame=new NetworkTesterMasterFrame("NetworkTester Worker");
           NetworkTesterMasterFrame frame=this;
           frame.setTitle("NetworkTester Master");
           frame.communicationNode=gui;
           frame.pID=pID;
//           frame.communicationNode.encodingCode=encodingCode;
           frame.ebuff=gui.commandTranceiver.ebuff;

//           frame.show();
           frame.setVisible(true);
           frame.basic=(BasicFrame)(frame.lookUp("BasicFrame"));
    	   frame.basic.setVisible(true);
           return frame;
    }
    public void waitAllResults(int nodeNumber)
    {
        this.numberOfResult=0;
        while(this.numberOfResult<nodeNumber){
            try{
                Thread.sleep(20);
            }
            catch(InterruptedException e){}
        }
    }
    public void waitTime(int millisec)
    {
        try{
            Thread.sleep((long)millisec);
        }
        catch(InterruptedException e){
        }
    }
    public void changeScrollbarValue(int paneID, int barID, int value)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
    }

    public void clickButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
/*
	    
0		buttons.addElement(this.enterButton);
1		buttons.addElement(this.clearButton);
2		buttons.addElement(this.collectNodesInformationButton);
3		buttons.addElement(this.spawnworkerButton);
4		buttons.addElement(this.executeButton);
5		buttons.addElement(this.helpButton);
6		buttons.addElement(this.exitButton);
7		buttons.addElement(this.generateScript1Button);
8		buttons.addElement(this.generateScript2Button);
9		buttons.addElement(this.editScriptButton);
10		buttons.addElement(this.clearMessageButton);
11		buttons.addElement(this.copySelectedMessageButton);
12      buttons.addElement(this.closeWorkersButton);
	    */
        if(i==0){
        //	 buttons.addElement( this.enterButton);
    		String command=commandFieldArea.getText();
	    	if(command.equals("")) return;
	    	int startTime=communicationNode.eventRecorder.timer.getTime();
	    	String theMessage=""+startTime + "," + command;
	    	this.messagesArea.append(theMessage+"\n");
            this.communicationNode.eventRecorder.recordMessage(theMessage);
		    if(parse(command)){
	    	}
		    else{
		        this.messagesArea.append("failed\n");
	     	}
	     	clickButton(1); // clear
	     	return;
        }
        else
        if(i==1){
	    //   buttons.addElement( this.clearButton);
	         this.commandFieldArea.setText("");
	         return;
	    }
	    else
	    if(i==2){
//		buttons.addElement(this.collectNodesInformationButton);
	       this.getNodesInfo();
	        return;
	    }
	    else
	    if(i==3){
//		buttons.addElement(this.spawnworkerButton);
	       this.commandFieldArea.setText("spawnworkers");
	       this.clickButton(0);
	       return;
	    }
	    else
	    if(i==4){
//		buttons.addElement(this.executeButton);
	       this.executeScript();
	       return;
	    }
	    else
	    if(i==5){
//		buttons.addElement(this.helpButton);
           return;
	    }
	    else
	    if(i==6){
//		buttons.addElement(this.exitButton);
//	        close();
            this.exitThis();
	    }
	    else
	    if(i==7){
//		buttons.addElement(this.generateScript1Button);
	       this.script=new Vector();
	       int combination=0;
	       try{
	          combination=(new Integer(this.combinationNumberArea.getText())).intValue();
	       }
	       catch(Exception e){}
	       this.generateScript1(combination,nodes);
	    }
	    else
	    if(i==8){
	    	    // buttons.addElement( this.script2Button);
	       this.script=new Vector();
	       this.generateScript2(nodes);
	       return;
        }
        else
        if(i==9){
//		buttons.addElement(this.editScriptButton);
            this.openTextEditor(this);
            return;
        }
        else
        if(i==10){
//		buttons.addElement(this.clearMessageButton);
            this.messagesArea.setText("");
            return;
        }
        else
        if(i==11){
//		buttons.addElement(this.copySelectedMessageButton);
            this.copyFromTheText(0); // 0:messageArea
            return;
        }
        else
        if(i==12){
//		buttons.addElement(this.closeWorkersButton);
	       this.commandFieldArea.setText("all quit");
	       this.clickButton(0);
	       return;
        }
    }

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

    public void focusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
    }

    public void focusGained()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
    }

    public void focusLost()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
    }

    public void gainFocus()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
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

    public void hideScrollBar(int paneID, int barID)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
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

    public void loseFocus()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
    }

    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
/*
	    
0		buttons.addElement(this.enterButton);
1		buttons.addElement(this.clearButton);
2		buttons.addElement(this.collectNodesInformationButton);
3		buttons.addElement(this.spawnworkerButton);
4		buttons.addElement(this.executeButton);
5		buttons.addElement(this.helpButton);
6		buttons.addElement(this.exitButton);
7		buttons.addElement(this.generateScript1Button);
8		buttons.addElement(this.generateScript2Button);
9		buttons.addElement(this.editScriptButton);
10		buttons.addElement(this.clearMessageButton);
11		buttons.addElement(this.copySelectedMessageButton);
12      buttons.addElement(this.closeWorkersButton);
	    */
        if(i==0){
        //	 buttons.addElement( this.enterButton);
    		String command=commandFieldArea.getText();
	    	if(command.equals("")) return;
	    	int startTime=communicationNode.eventRecorder.timer.getTime();
	    	String theMessage=""+startTime + "," + command;
	    	this.messagesArea.append(theMessage+"\n");
	    	this.messagesArea.setCaretPosition(
                this.messagesArea.getText().length());

            this.communicationNode.eventRecorder.recordMessage(theMessage);
		    if(parse(command)){
	    	}
		    else{
		        this.messagesArea.append("failed\n");
                this.messagesArea.setCaretPosition(
                   this.messagesArea.getText().length());

	     	}
	     	clickButton(1); // clear
	     	return;
        }
        else
        if(i==1){
	    //   buttons.addElement( this.clearButton);
	         this.commandFieldArea.setText("");
	         return;
	    }
	    else
	    if(i==2){
//		buttons.addElement(this.collectNodesInformationButton);
	       this.getNodesInfo();
	        return;
	    }
	    else
	    if(i==3){
//		buttons.addElement(this.spawnworkerButton);
	       this.commandFieldArea.setText("spawnworkers");
	       this.clickButton(0);
	       return;
	    }
	    else
	    if(i==4){
//		buttons.addElement(this.executeButton);
	       this.executeScript();
	       return;
	    }
	    else
	    if(i==5){
//		buttons.addElement(this.helpButton);
           return;
	    }
	    else
	    if(i==6){
//		buttons.addElement(this.exitButton);
//	        close();
            this.exitThis();
	    }
	    else
	    if(i==7){
//		buttons.addElement(this.generateScript1Button);
	       this.script=new Vector();
	       int combination=0;
	       try{
	          combination=(new Integer(this.combinationNumberArea.getText())).intValue();
	       }
	       catch(Exception e){}
	       this.generateScript1(combination,nodes);
	    }
	    else
	    if(i==8){
	    	    // buttons.addElement( this.script2Button);
	       this.script=new Vector();
	       this.generateScript2(nodes);
	       return;
        }
        else
        if(i==9){
//		buttons.addElement(this.editScriptButton);
            this.openTextEditor(this);
            return;
        }
        else
        if(i==10){
//		buttons.addElement(this.clearMessageButton);
            this.messagesArea.setText("");
            return;
        }
        else
        if(i==11){
//		buttons.addElement(this.copySelectedMessageButton);
            this.copyFromTheText(0); // 0:messageArea
            return;
        }
        else
        if(i==12){
//		buttons.addElement(this.closeWorkersButton);
	       this.commandFieldArea.setText("all quit");
	       this.clickButton(0);
	       return;
        }
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

    public void mouseEnteredAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
    }

    public void mouseExitedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
    }

    public void mousePressedAtTextArea(int i, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseReleasedAtTextArea(int id, int position,int x, int y)
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

    public void scrollBarHidden(int paneID, int barId)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
    }

    public void scrollBarShown(int paneID, int barID)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
    }

    public void scrollBarValueChanged(int paneID, int barID, int v)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
    }

    public void sendFileDialogMessage(String m)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

    public void showScrollBar(int paneID, int barID)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
    }

    public void typeKey(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void unfocusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
    }

    public void whenActionButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        String dname=d.getDialogName();
        String subname=d.getSubName();
        if(dname.equals("textEditor")){
    		String theText=d.getText();
/*            AFigure   newfig=new Text(fc);
            newfig.newFig();
            ((Text)newfig).setText(theText);
            fc.ftemp.add(newfig);
            newfig.depth=0;
            npMain.depthIndicator.setValue(newfig.depth);
            */
            this.script=new Vector();
            StringTokenizer st=new StringTokenizer(theText,"\n");
            while(st.hasMoreTokens()){
                script.addElement(st.nextToken());
            }
            return;
        }
    }

    public void whenCancelButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

	public NetworkTesterMasterFrame()
	{
	    panes=new Vector();
	    panes.addElement(this.messagesPane);
	    panes.addElement(this.commandFieldPane);
	    int numberOfPanes=panes.size();
	    for(int i=0;i<numberOfPanes;i++){
	        ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(i));
	        p.setFrame(this);
	        p.setID(i);
	    }
	    
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		setSize(545,450);
		setVisible(false);
		messagesPane.setOpaque(true);
		getContentPane().add(messagesPane);
		messagesPane.setBounds(48,48,396,192);
		messagesPane.getViewport().add(messagesArea);
		messagesArea.setBackground(java.awt.Color.white);
		messagesArea.setForeground(java.awt.Color.black);
		messagesArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		messagesArea.setBounds(0,0,392,188);
		commandFieldPane.setOpaque(true);
		getContentPane().add(commandFieldPane);
		commandFieldPane.setBounds(96,240,264,36);
		commandFieldPane.getViewport().add(commandFieldArea);
		commandFieldArea.setBackground(java.awt.Color.white);
		commandFieldArea.setForeground(java.awt.Color.black);
		commandFieldArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		commandFieldArea.setBounds(0,0,260,32);
		enterButton.setText("enter");
		enterButton.setActionCommand("enter");
		getContentPane().add(enterButton);
		enterButton.setBackground(new java.awt.Color(204,204,204));
		enterButton.setForeground(java.awt.Color.black);
		enterButton.setFont(new Font("Dialog", Font.BOLD, 12));
		enterButton.setBounds(360,240,84,36);
		clearButton.setText("clear");
		clearButton.setActionCommand("clear");
		getContentPane().add(clearButton);
		clearButton.setBackground(new java.awt.Color(204,204,204));
		clearButton.setForeground(java.awt.Color.black);
		clearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		clearButton.setBounds(444,240,84,36);
		collectNodesInformationButton.setText("collect node info.");
		collectNodesInformationButton.setActionCommand("collect node info.");
		getContentPane().add(collectNodesInformationButton);
		collectNodesInformationButton.setBackground(new java.awt.Color(204,204,204));
		collectNodesInformationButton.setForeground(java.awt.Color.black);
		collectNodesInformationButton.setFont(new Font("Dialog", Font.BOLD, 12));
		collectNodesInformationButton.setBounds(204,288,156,24);
		generateScript1Button.setText("script 1");
		generateScript1Button.setActionCommand("script 1");
		getContentPane().add(generateScript1Button);
		generateScript1Button.setBackground(new java.awt.Color(204,204,204));
		generateScript1Button.setForeground(java.awt.Color.black);
		generateScript1Button.setFont(new Font("Dialog", Font.BOLD, 12));
		generateScript1Button.setBounds(48,312,156,24);
		exitButton.setText("exit");
		exitButton.setActionCommand("exit");
		getContentPane().add(exitButton);
		exitButton.setBackground(new java.awt.Color(204,204,204));
		exitButton.setForeground(java.awt.Color.black);
		exitButton.setFont(new Font("Dialog", Font.BOLD, 12));
		exitButton.setBounds(444,48,84,24);
		executeButton.setText("execute script");
		executeButton.setActionCommand("execute the script");
		getContentPane().add(executeButton);
		executeButton.setBackground(new java.awt.Color(204,204,204));
		executeButton.setForeground(java.awt.Color.black);
		executeButton.setFont(new Font("Dialog", Font.BOLD, 12));
		executeButton.setBounds(204,360,156,24);
		spawnworkerButton.setText("spawn workers");
		spawnworkerButton.setActionCommand("spawn workers");
		getContentPane().add(spawnworkerButton);
		spawnworkerButton.setBackground(new java.awt.Color(204,204,204));
		spawnworkerButton.setForeground(java.awt.Color.black);
		spawnworkerButton.setFont(new Font("Dialog", Font.BOLD, 12));
		spawnworkerButton.setBounds(48,288,156,24);
		generateScript2Button.setText("script 2");
		generateScript2Button.setActionCommand("script 2");
		getContentPane().add(generateScript2Button);
		generateScript2Button.setBackground(new java.awt.Color(204,204,204));
		generateScript2Button.setForeground(java.awt.Color.black);
		generateScript2Button.setFont(new Font("Dialog", Font.BOLD, 12));
		generateScript2Button.setBounds(48,336,156,24);
		combinationNumberArea.setText("5");
		combinationNumberArea.setBorder(etchedBorder1);
		getContentPane().add(combinationNumberArea);
		combinationNumberArea.setBackground(java.awt.Color.white);
		combinationNumberArea.setForeground(java.awt.Color.black);
		combinationNumberArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		combinationNumberArea.setBounds(324,312,60,24);
		//$$ emptyBorder1.move(0,410);
		//$$ lineBorder1.move(24,410);
		//$$ titledBorder1.move(48,410);
		//$$ etchedBorder1.move(72,410);
		unitSizeArea.setText("500000");
		unitSizeArea.setBorder(etchedBorder1);
		getContentPane().add(unitSizeArea);
		unitSizeArea.setBackground(java.awt.Color.white);
		unitSizeArea.setForeground(java.awt.Color.black);
		unitSizeArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		unitSizeArea.setBounds(324,336,60,24);
		//$$ etchedBorder2.move(96,410);
		timesArea.setText("10");
		timesArea.setBorder(etchedBorder1);
		getContentPane().add(timesArea);
		timesArea.setBackground(java.awt.Color.white);
		timesArea.setForeground(java.awt.Color.black);
		timesArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		timesArea.setBounds(456,336,72,24);
		JLabel1.setText("Distributed System Recorder/Network Tester Master");
		getContentPane().add(JLabel1);
		JLabel1.setBounds(48,12,336,36);
		JLabel2.setText("command:");
		getContentPane().add(JLabel2);
		JLabel2.setBounds(24,240,72,36);
		helpButton.setText("help");
		helpButton.setActionCommand("help");
		getContentPane().add(helpButton);
		helpButton.setBackground(new java.awt.Color(204,204,204));
		helpButton.setForeground(java.awt.Color.black);
		helpButton.setFont(new Font("Dialog", Font.BOLD, 12));
		helpButton.setBounds(444,72,84,24);
		JLabel3.setText("combination:");
		getContentPane().add(JLabel3);
		JLabel3.setBounds(216,312,96,24);
		JLabel4.setText("unit size(byte):");
		getContentPane().add(JLabel4);
		JLabel4.setBounds(216,336,108,24);
		JLabel5.setText("times:");
		getContentPane().add(JLabel5);
		JLabel5.setBounds(396,336,60,24);
		editScriptButton.setText("edit script");
		editScriptButton.setActionCommand("edit script");
		getContentPane().add(editScriptButton);
		editScriptButton.setBackground(new java.awt.Color(204,204,204));
		editScriptButton.setForeground(java.awt.Color.black);
		editScriptButton.setFont(new Font("Dialog", Font.BOLD, 12));
		editScriptButton.setBounds(48,360,156,24);
		clearMessageButton.setText("clear");
		clearMessageButton.setActionCommand("clear");
		getContentPane().add(clearMessageButton);
		clearMessageButton.setBackground(new java.awt.Color(204,204,204));
		clearMessageButton.setForeground(java.awt.Color.black);
		clearMessageButton.setFont(new Font("Dialog", Font.BOLD, 12));
		clearMessageButton.setBounds(444,96,84,24);
		copySelectedMessageButton.setText("copy");
		copySelectedMessageButton.setActionCommand("copy");
		getContentPane().add(copySelectedMessageButton);
		copySelectedMessageButton.setBackground(new java.awt.Color(204,204,204));
		copySelectedMessageButton.setForeground(java.awt.Color.black);
		copySelectedMessageButton.setFont(new Font("Dialog", Font.BOLD, 12));
		copySelectedMessageButton.setBounds(444,120,84,24);
		closeWorkersButton.setText("close workers");
		closeWorkersButton.setActionCommand("close workers");
		getContentPane().add(closeWorkersButton);
		closeWorkersButton.setBackground(new java.awt.Color(204,204,204));
		closeWorkersButton.setForeground(java.awt.Color.black);
		closeWorkersButton.setFont(new Font("Dialog", Font.BOLD, 12));
		closeWorkersButton.setBounds(360,288,144,24);
		//}}

		//{{INIT_MENUS
		//}}
	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		clearMessageButton.addActionListener(lSymAction);
		copySelectedMessageButton.addActionListener(lSymAction);
		enterButton.addActionListener(lSymAction);
		clearButton.addActionListener(lSymAction);
		collectNodesInformationButton.addActionListener(lSymAction);
		spawnworkerButton.addActionListener(lSymAction);
		helpButton.addActionListener(lSymAction);
		exitButton.addActionListener(lSymAction);
		generateScript1Button.addActionListener(lSymAction);
		generateScript2Button.addActionListener(lSymAction);
		editScriptButton.addActionListener(lSymAction);
		executeButton.addActionListener(lSymAction);
		closeWorkersButton.addActionListener(lSymAction);
		//}}
		
		buttons=new Vector();
		buttons.addElement(this.enterButton);
		buttons.addElement(this.clearButton);
		buttons.addElement(this.collectNodesInformationButton);
		buttons.addElement(this.spawnworkerButton);
		buttons.addElement(this.executeButton);
		buttons.addElement(this.helpButton);
		buttons.addElement(this.exitButton);
		buttons.addElement(this.generateScript1Button);
		buttons.addElement(this.generateScript2Button);
		buttons.addElement(this.editScriptButton);
		buttons.addElement(this.clearMessageButton);
		buttons.addElement(this.copySelectedMessageButton);
		buttons.addElement(this.closeWorkersButton);
    	int numberOfButtons=buttons.size();	
	    for(int i=0;i<numberOfButtons;i++){
	        SelectedButton b=(SelectedButton)(buttons.elementAt(i));
	        b.setFrame(this);
	        b.setID(i);
	    }	

		texts=new Vector();
		texts.addElement(this.messagesArea);
		texts.addElement(this.commandFieldArea);
		texts.addElement(this.combinationNumberArea);
		texts.addElement(this.unitSizeArea);
		texts.addElement(this.timesArea);
		int numberOfTexts=texts.size();
	    for(int i=0;i<numberOfTexts;i++){
	        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
	        t.setFrame(this);
	        t.setID(i);
	    }
	    nodes=new Vector();
//	    random=new Random((long)(this.communicationNode.eventRecorder.timer.getMilliTime()));
        super.registerListeners();
		
//		textEditFrame=new TextEditFrame();
	}

	public NetworkTesterMasterFrame(String sTitle)
	{
		this();
		setTitle(sTitle);
	}

	static public void main(String args[])
	{
		(new NetworkTesterMasterFrame()).setVisible(true);
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
	controlledparts.ControlledScrollPane messagesPane = new controlledparts.ControlledScrollPane();
	controlledparts.ControlledTextArea messagesArea = new controlledparts.ControlledTextArea();
	controlledparts.ControlledScrollPane commandFieldPane = new controlledparts.ControlledScrollPane();
	public controlledparts.ControlledTextArea commandFieldArea = new controlledparts.ControlledTextArea();
	controlledparts.ControlledButton enterButton = new controlledparts.ControlledButton();
	public controlledparts.ControlledButton clearButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton collectNodesInformationButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton generateScript1Button = new controlledparts.ControlledButton();
	controlledparts.ControlledButton exitButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton executeButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton spawnworkerButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton generateScript2Button = new controlledparts.ControlledButton();
	controlledparts.ControlledTextArea combinationNumberArea = new controlledparts.ControlledTextArea();
	EmptyBorder emptyBorder1 = new EmptyBorder(new Insets(0,0,0,0));
	LineBorder lineBorder1 = new LineBorder(Color.GRAY);
	TitledBorder titledBorder1 = new TitledBorder(emptyBorder1);
	EtchedBorder etchedBorder1 = new EtchedBorder();
	EtchedBorder etchedBorder2 = new EtchedBorder();
	controlledparts.ControlledTextArea unitSizeArea = new controlledparts.ControlledTextArea();
	controlledparts.ControlledTextArea timesArea = new controlledparts.ControlledTextArea();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	controlledparts.ControlledButton helpButton = new controlledparts.ControlledButton();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel4 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel5 = new javax.swing.JLabel();
	controlledparts.ControlledButton editScriptButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton clearMessageButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton copySelectedMessageButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton closeWorkersButton = new controlledparts.ControlledButton();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == clearMessageButton)
				clearMessageButton_actionPerformed(event);
			else if (object == copySelectedMessageButton)
				copySelectedMessageButton_actionPerformed(event);
			else if (object == enterButton)
				enterButton_actionPerformed(event);
			else if (object == clearButton)
				clearButton_actionPerformed(event);
			else if (object == collectNodesInformationButton)
				collectNodesInformationButton_actionPerformed(event);
			else if (object == spawnworkerButton)
				spawnworkerButton_actionPerformed(event);
			else if (object == helpButton)
				helpButton_actionPerformed(event);
			else if (object == exitButton)
				exitButton_actionPerformed(event);
			else if (object == generateScript1Button)
				generateScript1Button_actionPerformed(event);
			else if (object == generateScript2Button)
				generateScript2Button_actionPerformed(event);
			else if (object == editScriptButton)
				editScriptButton_actionPerformed(event);
			else if (object == executeButton)
				executeButton_actionPerformed(event);
			else if (object == closeWorkersButton)
				closeWorkersButton_actionPerformed(event);
		}
	}

	public void clearMessageButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		clearMessageButton_actionPerformed_Interaction1(event);
	}

	public void clearMessageButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			clearMessageButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	public void copySelectedMessageButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		copySelectedMessageButton_actionPerformed_Interaction1(event);
	}

	public void copySelectedMessageButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			copySelectedMessageButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void enterButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		enterButton_actionPerformed_Interaction1(event);
	}

	void enterButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			enterButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	public void clearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		clearButton_actionPerformed_Interaction1(event);
	}

	public void clearButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			clearButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	public void collectNodesInformationButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		collectNodesInformationButton_actionPerformed_Interaction1(event);
	}

	public void collectNodesInformationButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			collectNodesInformationButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void spawnworkerButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		spawnworkerButton_actionPerformed_Interaction1(event);
	}

	void spawnworkerButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			spawnworkerButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void helpButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		helpButton_actionPerformed_Interaction1(event);
	}

	void helpButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			helpButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void exitButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		exitButton_actionPerformed_Interaction1(event);
	}

	void exitButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			exitButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void generateScript1Button_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		generateScript1Button_actionPerformed_Interaction1(event);
	}

	void generateScript1Button_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			generateScript1Button.show();
		} catch (java.lang.Exception e) {
		}
	}

	void generateScript2Button_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		generateScript2Button_actionPerformed_Interaction1(event);
	}

	void generateScript2Button_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			generateScript2Button.show();
		} catch (java.lang.Exception e) {
		}
	}

	void editScriptButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		editScriptButton_actionPerformed_Interaction1(event);
	}

	void editScriptButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			editScriptButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void executeButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		executeButton_actionPerformed_Interaction1(event);
	}

	void executeButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			executeButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	public void closeWorkersButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		closeWorkersButton_actionPerformed_Interaction1(event);
	}

	public void closeWorkersButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			closeWorkersButton.show();
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
	public void pressKey(int i, int p, int code) {
		// TODO 自動生成されたメソッド・スタブ

	}

}