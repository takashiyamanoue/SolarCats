/*
 * 作成日: 2005/09/08
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package application.ftcontroller;

/**
 * @author yamachan
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
import controlledparts.*;
import nodesystem.*;
import application.basic.*;
import application.texteditor.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.border.*;

import controlledparts.ControlledButton;
/**
* This code was generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* *************************************
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED
* for this machine, so Jigloo or this code cannot be used legally
* for any corporate or commercial purpose.
* *************************************
*/
public class FtControlMasterFrame extends ControlledFrame
implements FrameWithControlledPane, FrameWithControlledButton,
             FrameWithControlledTextAreas, DialogListener
{
	class Queue{
		Vector q;
		boolean isWaiting=false;
		Queue(){
			q=new Vector();
		}
		void enQueue(String x){
			q.add(x);
		}
		String deQueue(){
			isWaiting=true;
			while(q.size()<=0){
				try{
					Thread.sleep(10);
				}
				catch(InterruptedException e){return null;}
				if(!isWaiting){
					return null;
				}
			}
			isWaiting=false;
			String rtn=(String)(q.remove(0));
			return rtn;
		}
		void reset(){
			isWaiting=false;
			q=new Vector();
		}
	}
	
	
	public void setTextOnTheText(int id, int pos, String s)
	{
		ControlledTextArea t=(ControlledTextArea)(this.texts.elementAt(id));
		t.setTextAt(pos,s); 
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
		if(x.indexOf("getReturn")==0){
			String rtn=this.getLastReturn();
			messagesArea.append(">"+rtn+" is returned to the program for "+x+" command\n");
			return rtn;
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
/*
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
//						String address=((String)((Vector)anotherNode).elementAt(1));                      
//						command="node "+nodeID+" set connect "+address;
					  int serverNode=((Integer)(anotherNode.elementAt(0))).intValue();
					  command="node "+nodeID+" set connectnode "+serverNode;
					  messagesArea.append("-"+command+"\n");
//						script.addElement(command);
					  this.addProgram(command);
		}
		command="waittime 500";
		   messagesArea.append("-"+command+"\n");
//		  script.addElement(command);
		this.addProgram(command);
		command="all enter";
		   messagesArea.append("-"+command+"\n");
//		  script.addElement(command);
		this.addProgram(command);
		command="waittime 500";
		   messagesArea.append("-"+command+"\n");
//		  script.addElement(command);
		this.addProgram(command);
        
		for(int i=0;i<connectedNodeNumber;i+=2){
					Vector theNode=(Vector)(nodes.elementAt(i));
					  int nodeID=((Integer)(theNode.elementAt(0))).intValue();
					  // node i+m set sendreceive-n size 10
					  command="node "+nodeID+" set sendreceive-n "+unitSize+" "+iterNum;
					  messagesArea.append("-"+command+"\n");
//						script.addElement(command);
					  this.addProgram(command);
		}
        
		command="resetResultsCounter";
		messagesArea.append("-"+command+"\n");
//		  script.addElement(command);
		this.addProgram(command);
                
		command="waittime 500";
		  messagesArea.append("-"+command+"\n");
//		  script.addElement(command);
		this.addProgram(command);
		command="all enter";
		   messagesArea.append("-"+command+"\n");
//		  script.addElement(command);
		this.addProgram(command);
		command="waittime 500";
		messagesArea.append("-"+command+"\n");
//		  script.addElement(command);
		this.addProgram(command);
		command="waitallresults "+numberOfOnesideNodes;
		   messagesArea.append("-"+command+"\n");
//		  script.addElement(command);
		this.addProgram(command);
		command="waittime 500";
		   messagesArea.append("-"+command+"\n");
//		  script.addElement(command);
		this.addProgram(command);
		command="all disconnect";
		   messagesArea.append("-"+command+"\n");
//		  script.addElement(command);
		this.addProgram(command);
                
          
//		  }
		messagesArea.append("script2 is generated\n");
	}
	*/
	public void getNodesInfo()
	{         
			nodes=new Vector();
			messagesArea.append("start collect nodes info\n");
//			  sendEvent("broadcast","shell","spawn NetworkTesterWorker");
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
		this.queue.enQueue(""+id+" "+result);
		messagesArea.append("node "+id+":"+result+"\n");
		this.communicationNode.eventRecorder.recordMessage(""+id+","+result);
		this.numberOfResult++;
        
	}
	public String getLastReturn()
	{   
		String last="";
		last=this.queue.deQueue();
		String rtn=last.substring(last.indexOf(" ")+1);
		return rtn;
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
			this.clickButton(this.enterButton.getID());
//			  String x=s.substring("chat ".length());
			return true;
		}
		else
		if(command.indexOf("clear")==0){
			this.clickButton(this.clearButton.getID());
			return true;
		}
		else
		if(command.indexOf("quit")==0){
			this.clickButton(this.exitButton.getID());
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
//			  connectServer(x);
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
			this.queue=new Queue();
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
//			  setTestID((new Integer(x)).intValue());
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
			this.sendEvent("broadcast","shell", "spawn FT-Controller-(worker) remote");
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
//			  connectServer(x);
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
//			  connectServer(x);
			this.sendEvent("node "+nodeID,workerCommand,theCommand);
			return true;
		}
		else
		if(command.indexOf("waittime ")==0){
			String x=command.substring("waittime ".length());
			// wait <millisec>
//			  connectServer(x);
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
	
	String commandName="ftcontrolmaster";
	public String getCommandName(){
		return commandName;
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

    String workerCommand="ftcontrolworker";

	public void sendEvent(String x)
	{
	}
	public void sendEvent(String transmitMode , String command, String subcommand)
	{
		CommandTranceiver ct=this.communicationNode.commandTranceiver;
		if(ct==null) return;
		if(ebuff!=null){
			String sending=transmitMode+" "+command+" "+subcommand+ct.endOfACommand;
			AMessage m=new AMessage();
			m.setHead(sending);
		    this.ebuff.putS(m);
		}
	}
	public ControlledFrame spawnMain(CommunicationNode gui, String args, int pID, String controlMode)
	{
		// This method is derived from interface Spawnable
		// to do: code goes here
//			 NetworkTesterMasterFrame frame=new NetworkTesterMasterFrame("NetworkTester Worker");
		   FtControlMasterFrame frame=this;
		   frame.setTitle("FT Controller Master");
		   frame.communicationNode=gui;
		   frame.pID=pID;
//			 frame.communicationNode.encodingCode=encodingCode;
		   frame.ebuff=gui.commandTranceiver.ebuff;

//			 frame.show();
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
//			close();
			this.exitThis();
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
		if(i==this.enterButton.getID()){
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
			clickButton(this.clearButton.getID()); // clear
			return;
		}
		else
		if(i==this.clearButton.getID()){
		//   buttons.addElement( this.clearButton);
			 this.commandFieldArea.setText("");
			 return;
		}
		else
		if(i==this.collectNodesInformationButton.getID()){
//		buttons.addElement(this.collectNodesInformationButton);
		   this.getNodesInfo();
			return;
		}
		else
		if(i==this.spawnworkerButton.getID()){
//		buttons.addElement(this.spawnworkerButton);
		   this.commandFieldArea.setText("spawnworkers");
		   this.clickButton(this.enterButton.getID());
		   return;
		}
		else
		if(i==this.executeButton.getID()){
//		buttons.addElement(this.executeButton);
		   this.executeScript();
		   return;
		}
		else
		if(i==this.helpButton.getID()){
//		buttons.addElement(this.helpButton);
		   return;
		}
		else
		if(i==this.exitButton.getID()){
//		buttons.addElement(this.exitButton);
//			close();
			this.exitThis();
		}
		else
		if(i==this.editScriptButton.getID()){
//		buttons.addElement(this.editScriptButton);
			this.openTextEditor(this);
			return;
		}
		else
		if(i==this.clearMessageButton.getID()){
//		buttons.addElement(this.clearMessageButton);
			this.messagesArea.setText("");
			return;
		}
		else
		if(i==this.copySelectedMessageButton.getID()){
//		buttons.addElement(this.copySelectedMessageButton);
			this.copyFromTheText(0); // 0:messageArea
			return;
		}
		else
		if(i==this.closeWorkersButton.getID()){
//		buttons.addElement(this.closeWorkersButton);
		   this.commandFieldArea.setText("all quit");
		   this.clickButton(this.enterButton.getID());
		   return;
		}
		if(i==this.resetButton.getID()){
			this.reset();
		}
	}
	
	public void reset(){
		this.parse("all reset");
		this.queue.reset();
		this.mouseClickedAtButton(this.clearButton.getID());
		this.mouseClickedAtButton(this.clearMessageButton.getID());
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
	
	Queue queue;

	public FtControlMasterFrame()
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
		executeButton.setBounds(205, 315, 156, 24);
		spawnworkerButton.setText("spawn workers");
		spawnworkerButton.setActionCommand("spawn workers");
		getContentPane().add(spawnworkerButton);
		spawnworkerButton.setBackground(new java.awt.Color(204,204,204));
		spawnworkerButton.setForeground(java.awt.Color.black);
		spawnworkerButton.setFont(new Font("Dialog", Font.BOLD, 12));
		spawnworkerButton.setBounds(48,288,156,24);
		//$$ emptyBorder1.move(0,410);
		//$$ lineBorder1.move(24,410);
		//$$ titledBorder1.move(48,410);
		//$$ etchedBorder1.move(72,410);
		//$$ etchedBorder2.move(96,410);
		JLabel1.setText("Distributed System Recorder/FisherTechnik Controller Master");
		getContentPane().add(JLabel1);
		JLabel1.setBounds(48,12,400,34);
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
		editScriptButton.setText("edit script");
		editScriptButton.setActionCommand("edit script");
		getContentPane().add(editScriptButton);
		editScriptButton.setBackground(new java.awt.Color(204,204,204));
		editScriptButton.setForeground(java.awt.Color.black);
		editScriptButton.setFont(new Font("Dialog", Font.BOLD, 12));
		editScriptButton.setBounds(48, 315, 156, 24);
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
		{
			resetButton = new ControlledButton();
			this.getContentPane().add(resetButton);
			resetButton.setText("reset");
			resetButton.setBounds(362, 316, 128, 22);
		}
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
		buttons.addElement(this.editScriptButton);
		buttons.addElement(this.clearMessageButton);
		buttons.addElement(this.copySelectedMessageButton);
		buttons.addElement(this.closeWorkersButton);
		buttons.addElement(this.resetButton);
		int numberOfButtons=buttons.size();	
		for(int i=0;i<numberOfButtons;i++){
			SelectedButton b=(SelectedButton)(buttons.elementAt(i));
			b.setFrame(this);
			b.setID(i);
		}	

		texts=new Vector();
		texts.addElement(this.messagesArea);
		texts.addElement(this.commandFieldArea);
		int numberOfTexts=texts.size();
		for(int i=0;i<numberOfTexts;i++){
			ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
			t.setFrame(this);
			t.setID(i);
		}
		nodes=new Vector();
//		random=new Random((long)(this.communicationNode.eventRecorder.timer.getMilliTime()));
		super.registerListeners();
		this.queue=new Queue();
//		textEditFrame=new TextEditFrame();
	}

	public FtControlMasterFrame(String sTitle)
	{
		this();
		setTitle(sTitle);
	}

	static public void main(String args[])
	{
		(new FtControlMasterFrame()).setVisible(true);
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
	controlledparts.ControlledButton exitButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton executeButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton spawnworkerButton = new controlledparts.ControlledButton();
	EmptyBorder emptyBorder1 = new EmptyBorder(new Insets(0,0,0,0));
	LineBorder lineBorder1 = new LineBorder(Color.GRAY);
	TitledBorder titledBorder1 = new TitledBorder(emptyBorder1);
	EtchedBorder etchedBorder1 = new EtchedBorder();
	EtchedBorder etchedBorder2 = new EtchedBorder();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	controlledparts.ControlledButton helpButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton editScriptButton = new controlledparts.ControlledButton();
	private ControlledButton resetButton;
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
	
	/**
	* Auto-generated method for setting the popup menu for a component
	*/
	private void setComponentPopupMenu(
		final java.awt.Component parent,
		final javax.swing.JPopupMenu menu) {
		parent.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent e) {
				if (e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
			public void mouseReleased(java.awt.event.MouseEvent e) {
				if (e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
		});
	}

}
