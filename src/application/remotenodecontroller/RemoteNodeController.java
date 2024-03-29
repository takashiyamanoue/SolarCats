package application.remotenodecontroller;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import nodesystem.CommunicationNode;
import nodesystem.EventBuffer;
import controlledparts.ControlledFrame;
import controlledparts.FrameWithControlledButton;


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
public class RemoteNodeController extends ControlledFrame implements FrameWithControlledButton  
{
    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledButton
        // to do: code goes here
        return true;
    }

    public void setGroupControlModeRecorder(String theMode)
    {
		String pmode=(String)(this.controlModeComboBox.getSelectedItem());
		this.setGroupControlModeApplication("teach");
		this.sendEvent("setRecordMode "+theMode);
		if(theMode.equals("local")){
		    this.communicationNode.eventRecorder.changeControlMode("local");
		}
		else
		{
		    this.communicationNode.eventRecorder.changeControlMode("play");
		}
		this.setGroupControlModeApplication(pmode);
		this.messageArea.append("change all node record mode to "+theMode+".\n");
        this.messageArea.setCaretPosition(
             this.messageArea.getText().length());
    }

    public void setGroupControlModeApplication(String theMode)
    {
		String pmode=this.communicationNode.getControlMode();
		this.communicationNode.setControlMode("teach");
		if(theMode.equals("teach")){
		    this.sendEvent("setControlMode receive");
            this.communicationNode.setControlMode(theMode);
		}
		else 
		if(theMode.equals("local")){
		    this.sendEvent("setControlMode "+theMode);
            this.communicationNode.setControlMode(theMode);
		}
		else 
		if(theMode.equals("common")){
    		this.sendEvent("setControlMode "+theMode);
		    this.communicationNode.setControlMode(theMode);
    	}
		else
		if(theMode.equals("remote")){
     		this.sendEvent("setControlMode "+theMode);
            this.communicationNode.setControlMode("broadcast");
 		}
		else
		if(theMode.equals("withReflector")){
     		this.sendEvent("setControlMode "+theMode);
            this.communicationNode.setControlMode("withReflector");
 		}
		this.messageArea.append("change all node mode to "+theMode+".\n");
    }

    public void clickButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
    }

    public void focusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
    }

    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
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

    public void unfocusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
    }

    public JoinedNodeFrame joinedNodeFrame;

    public Vector buttons;

    public void setControlMode(String x)
    {
        this.communicationNode.setControlMode(x);
    }

    public boolean parse(String command)
    {
        if(command.indexOf("nodeInfo ")==0){
            String x=command.substring("nodeInfo ".length());
           // nodeinfo <serialNo> <username> <address>
           this.joinedNodeFrame.addItem(x);
           this.communicationNode.appendMessage("node:"+x+"\n");
           return true;
        }
        return false;
    }

    public void sendEvent(String transmitMode , String command, String subcommand)
    {
        if(ebuff!=null)
        this.ebuff.putS(transmitMode+" "+command+" "+subcommand
           + this.communicationNode.commandTranceiver.endOfACommand);
    }

    public EventBuffer ebuff;

    public int pID;

    public void setComboBox()
    {
        this.controlModeComboBox.addItem("teach");
        this.controlModeComboBox.addItem("common");
        this.controlModeComboBox.addItem("local");
        this.controlModeComboBox.addItem("remote");
        this.controlModeComboBox.addItem("withReflector");
        
        this.recordModeComboBox.addItem("local");
        this.recordModeComboBox.addItem("remote");
        this.recordModeComboBox.addItem("play");
       
        this.selectedNodeControlComboBox.addItem("common");
        this.selectedNodeControlComboBox.addItem("local");
        this.selectedNodeControlComboBox.addItem("remote");
        
//        this.nodeInformationComboBox.addItem("click here to select a node");
        
    }
;
	public RemoteNodeController()
	{
		//{{INIT_CONTROLS
		setTitle("A Simple Frame");
		getContentPane().setLayout(null);
		this.setSize(449, 335);
		setVisible(false);
		JLabel1.setText("Distributed System Recorder/Remote Node controller");
		getContentPane().add(JLabel1);
		JLabel1.setBounds(12,12,324,36);
		getContentPane().add(controlModeComboBox);
		controlModeComboBox.setBounds(168, 84, 182, 21);
		JLabel2.setText("node control mode");
		getContentPane().add(JLabel2);
		JLabel2.setBounds(14, 84, 154, 21);
		getContentPane().add(recordModeComboBox);
		recordModeComboBox.setBounds(168, 105, 182, 21);
		JLabel3.setText("node record mode");
		getContentPane().add(JLabel3);
		JLabel3.setBounds(14, 105, 154, 21);
		JLabel4.setText("selected node control");
		getContentPane().add(JLabel4);
		JLabel4.setBounds(14, 126, 154, 21);
		getContentPane().add(selectedNodeControlComboBox);
		selectedNodeControlComboBox.setBounds(168, 126, 182, 21);
		messagePane.setOpaque(true);
		getContentPane().add(messagePane);
		messagePane.setBounds(84, 175, 350, 119);
		messagePane.getViewport().add(messageArea);
		messageArea.setBounds(0,0,344,116);
		messageArea.setPreferredSize(new java.awt.Dimension(345, 117));
		JLabel5.setText("message");
		getContentPane().add(JLabel5);
		JLabel5.setBounds(14, 175, 70, 21);
		JLabel6.setText("node selecter");
		getContentPane().add(JLabel6);
		JLabel6.setBounds(14, 147, 154, 21);
		exitButton.setActionCommand("exit");
		getContentPane().add(exitButton);
		exitButton.setBounds(350, 14, 84, 35);
		sendControlModeButton.setText("send");
		sendControlModeButton.setActionCommand("send");
		getContentPane().add(sendControlModeButton);
		sendControlModeButton.setBounds(350, 84, 84, 21);
		sendRecordModeButton.setText("send");
		sendRecordModeButton.setActionCommand("send");
		getContentPane().add(sendRecordModeButton);
		sendRecordModeButton.setBounds(350, 105, 84, 21);
		sendSelectedControlButton.setText("send");
		sendSelectedControlButton.setActionCommand("send");
		getContentPane().add(sendSelectedControlButton);
		sendSelectedControlButton.setBounds(350, 126, 84, 21);
		getNodeInfoButton.setText("get");
		getNodeInfoButton.setActionCommand("get");
		getNodeInfoButton.setToolTipText("get Node Info in the Group");
		getContentPane().add(getNodeInfoButton);
		getNodeInfoButton.setBounds(350, 147, 84, 21);
		showNodesButton.setText("selected node");
		showNodesButton.setActionCommand("selected node");
		getContentPane().add(showNodesButton);
		showNodesButton.setBackground(new java.awt.Color(204,204,204));
		showNodesButton.setForeground(java.awt.Color.black);
		showNodesButton.setFont(new Font("Dialog", Font.BOLD, 12));
		showNodesButton.setBounds(168, 147, 182, 21);
		exitButton.setIcon(new ImageIcon("images/exit-icon.GIF"));
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		controlModeComboBox.addActionListener(lSymAction);
		recordModeComboBox.addActionListener(lSymAction);
		selectedNodeControlComboBox.addActionListener(lSymAction);
		exitButton.addActionListener(lSymAction);
		sendControlModeButton.addActionListener(lSymAction);
		sendRecordModeButton.addActionListener(lSymAction);
		sendSelectedControlButton.addActionListener(lSymAction);
		getNodeInfoButton.addActionListener(lSymAction);
		SymItem lSymItem = new SymItem();
		showNodesButton.addActionListener(lSymAction);
		//}}

		//{{INIT_MENUS
		//}}
		
        this.setComboBox();
        buttons = new Vector();
        buttons.addElement(this.showNodesButton);
        this.showNodesButton.setID(0);
        this.showNodesButton.setFrame(this);
		{
			resetButton = new JButton();
			getContentPane().add(resetButton);
			resetButton.setText("reset");
			resetButton.setBounds(350, 49, 84, 28);
			resetButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					resetButtonActionPerformed(evt);
				}
			});
		}
        this.showNodesButton.addActionListener(lSymAction);

        this.joinedNodeFrame = new JoinedNodeFrame();
        this.joinedNodeFrame.setFrame(this);
        super.registerListeners();
        
	}

	public RemoteNodeController(String title)
	{
		this();
		setTitle(title);
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;
	private JButton resetButton;

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
		Object object = event.getSource();
		if (object == RemoteNodeController.this)
			RemoteNodeController_WindowClosing(event);
		}
	}

	void RemoteNodeController_WindowClosing(java.awt.event.WindowEvent event)
	{
//		dispose();		 // dispose of the Frame.
        this.exitThis();
	}
	//{{DECLARE_CONTROLS
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JComboBox controlModeComboBox = new javax.swing.JComboBox();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	javax.swing.JComboBox recordModeComboBox = new javax.swing.JComboBox();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel4 = new javax.swing.JLabel();
	javax.swing.JComboBox selectedNodeControlComboBox = new javax.swing.JComboBox();
	javax.swing.JScrollPane messagePane = new javax.swing.JScrollPane();
	javax.swing.JTextArea messageArea = new javax.swing.JTextArea();
	javax.swing.JLabel JLabel5 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel6 = new javax.swing.JLabel();
	javax.swing.JButton exitButton = new javax.swing.JButton();
	javax.swing.JButton sendControlModeButton = new javax.swing.JButton();
	javax.swing.JButton sendRecordModeButton = new javax.swing.JButton();
	javax.swing.JButton sendSelectedControlButton = new javax.swing.JButton();
	javax.swing.JButton getNodeInfoButton = new javax.swing.JButton();
	controlledparts.ControlledButton showNodesButton = new controlledparts.ControlledButton();
	//}}

	//{{DECLARE_MENUS
	//}}

    public void exitThis()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
        System.out.println("remote node controller exithis");
//         hide();         // Frame を非表示にする.
          this.setVisible(false);
//        communicationNode.applicationManager.kill(pID);
//        dispose();      // ｼｽﾃﾑﾘｿｰｽを開放する.
    }

    public void receiveEvent(String s)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
        if(parse(s)){
        }
        else{
            this.communicationNode.appendMessage("command failed while receive event.\n");
        };
    }

    public void sendAll()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
    }

    String commandName="RemoteNodeController";
    public String getCommandName(){
    	return commandName;
    }

    public void sendEvent(String s)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
           /*
           if(ebuff!=null)
              ebuff.putS("broadcast shell "+s+
           this.communicationNode.commandTranceiver.endOfACommand);
           */
        this.communicationNode.sendEvent("shell",s);
       
    }

    public ControlledFrame spawnMain(CommunicationNode gui, String args, int pID, String controlMode)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
        RemoteNodeController r=this;        
        r.communicationNode=gui;
        r.pID=pID;
        r.ebuff=r.communicationNode.commandTranceiver.ebuff;
//        r.setControlMode(controlMode);
        r.setVisible(true);
        r.setTitle("DSR/remote node controller");
        
        return r;
    }

    public void toFront()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
    }


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == controlModeComboBox)
				controlModeComboBox_actionPerformed(event);
			else if (object == recordModeComboBox)
				recordModeComboBox_actionPerformed(event);
			else if (object == selectedNodeControlComboBox)
				selectedNodeControlComboBox_actionPerformed(event);
			if (object == exitButton)
				exitButton_actionPerformed(event);
			else if (object == sendControlModeButton)
				sendControlModeButton_actionPerformed(event);
			else if (object == sendRecordModeButton)
				sendRecordModeButton_actionPerformed(event);
			else if (object == sendSelectedControlButton)
				sendSelectedControlButton_actionPerformed(event);
			else if (object == getNodeInfoButton)
				getNodeInfoButton_actionPerformed(event);
			else if (object == showNodesButton)
				showNodesButton_actionPerformed(event);
		}
	}

	void controlModeComboBox_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
	}

	void recordModeComboBox_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
	}

	void selectedNodeControlComboBox_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
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
		exitThis();
	}

	void sendControlModeButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		sendControlModeButton_actionPerformed_Interaction1(event);
	}

	void sendControlModeButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			sendControlModeButton.show();
		} catch (java.lang.Exception e) {
		}
		String theMode=(String)(this.controlModeComboBox.getSelectedItem());
		this.setGroupControlModeApplication(theMode);
	}

	void sendRecordModeButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		sendRecordModeButton_actionPerformed_Interaction1(event);
	}

	void sendRecordModeButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		String theMode=(String)(this.recordModeComboBox.getSelectedItem());
		this.setGroupControlModeRecorder(theMode);
	}

	void sendSelectedControlButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		sendSelectedControlButton_actionPerformed_Interaction1(event);
	}

	void sendSelectedControlButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			sendSelectedControlButton.show();
		} catch (java.lang.Exception e) {
		}
		String nodeInfo=this.showNodesButton.getText();
		String theMode=(String)(this.selectedNodeControlComboBox.getSelectedItem());
		StringTokenizer st=new StringTokenizer(nodeInfo);
		String nodeID=st.nextToken();
		this.sendEvent("node "+nodeID,"shell","setControlMode "+theMode);
		this.communicationNode.setControlMode("common");
		this.communicationNode.setControlModeLabel("common with node "+nodeID);
	}

	void getNodeInfoButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		getNodeInfoButton_actionPerformed_Interaction1(event);
	}

	void getNodeInfoButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
        this.joinedNodeFrame.removeAll();
//		selectedNodeControlComboBox.setBounds(168,108,180,24);
        this.sendEvent("broadcast","shell","getNodeInfo "+communicationNode.serialNo+" RemoteNodeController");
	}

	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
		}
	}

	void nodeInformationComboBox_itemStateChanged_Interaction1(java.awt.event.ItemEvent event)
	{
		try {
//			nodeInformationComboBox.validate();
		} catch (java.lang.Exception e) {
		}
	}

	void showNodesButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		showNodesButton_actionPerformed_Interaction1(event);
	}

	void showNodesButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// JoinedNodeFrame Create and show the JoinedNodeFrame
		} catch (java.lang.Exception e) {
		}
		this.joinedNodeFrame.setVisible(true);
	}
	
	private void resetButtonActionPerformed(ActionEvent evt) {
//		System.out.println("resetButton.actionPerformed, event=" + evt);
		//TODO add your code for resetButton.actionPerformed
        this.sendEvent("broadcast","shell","reset");
        try{Thread.sleep(100);}catch(InterruptedException e){}
		this.communicationNode.commandTranceiver.resetProcedure();
	}
}