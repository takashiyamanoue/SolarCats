package application.rcxcontroller;

import java.awt.Panel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

import nodesystem.CommunicationNode;
import rcx.RCXOpcode;
import rcx.RCXPort;
import application.serialcontroller.SerialControlWorkerFrame;
import controlledparts.ControlledFrame;

public class RcxControlWorkerFrame extends SerialControlWorkerFrame implements rcx.RCXListener
{
    public void init2()
    {
	       this.setPropertyFile("rcx-parameters.txt");
           this.initPort(this.propertyFileName);
    }


    public void initPort(String pfile)
    {
   		File dataPath=this.communicationNode.commonDataDir;
	    File f=new File(dataPath.getPath(),pfile);
//    	File f = new File("rcx-parameters.txt");
    	if (!f.exists()) {
    	    f = new File(System.getProperty("user.dir")
    			 + System.getProperty("path.separator")
    			 + "parameters.txt");
    	}
    	if (f.exists()) {
    	    try {
        		FileInputStream fis = new FileInputStream(f);
        		parameters = new Properties();
        		parameters.load(fis);
        		fis.close();
            	portName = parameters.getProperty("port");
    	    } catch (IOException e) { }
    	}

        rcxPort = new RCXPort(portName);
        rcxPort.addRCXListener(this);
//        tableButton.setEnabled(true);        
        
        if(rcxPort.isOpen()) {
            messages.append("RCXPort initialized.\n");
            String lasterror = rcxPort.getLastError();
            if(lasterror!=null)
                messages.append(lasterror+"\n");
            messages.setEditable(true);
            messages.setEnabled(true);
            messages.requestFocus();            
        }
        else {
            if(portName!=null) {
                messages.append("Failed to create RCXPort with "+portName+"\n");            
                messages.append("Port "+portName+" is invalid or may be ");
                messages.append("currently used.\nTry another port.\n");
                messages.append("Edit or create a file named parameters.txt ");
                messages.append("that has:\nport=COM1\n(replace COM1 with ");
                messages.append("the correct port name)\n");
                messages.repaint();
            }
            else
                messages.append("Please specify a port in parameters.txt\n");
                messages.append("Create a file that has:\nport=COM1\n");
                messages.append("(replace COM1 with the correct port name)\n");
                messages.repaint();
        }

    }

    public boolean parse(String command)
    {
        /*
        command:
          enter ... meta
          clear ... meta
          quit  ... meta
          set <command> ... meta
          send <rcx code>
          testid <id>
          returnnodeinfo <return-address>        
        */
        if(command.indexOf("enter")==0){
            this.clickButton(0);
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
//            this.clickButton(2);
//            this.close();
            this.exitThis();
            return true;
        }
        else
        if(command.indexOf("set ")==0){
            String x=command.substring("set ".length());
            // set <command>
            this.commandField.setText(x);
            return true;
        }
        else
        if(command.indexOf("send ")==0){
            BufferedReader dinstream=null;
            String rcxcode=command.substring("send ".length());
            // send <rcxcode>
            this.sendCodeToRcx(rcxcode);
            return true;
        }
        else
        if(command.indexOf("getnodeinfo ")==0){
            // getnodeinfo <return node id>       
            masterNode=command.substring("getnodeinfo ".length());
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
            String myName=this.communicationNode.uName;
            this.sendEvent("node "+masterNode, 
             "rcxcontrolmaster", "nodeinfo "+this.communicationNode.serialNo+" "+myName+" "+theAddress);
            messages.append("return nodeinfo "+this.communicationNode.serialNo+" "+myName+" "+theAddress+ " to the node "+masterNode+"\n");
            return true;
        }
        else
        if(command.indexOf("returninfo ")==0){
            // returninfo <return node id>       
            String theNode=command.substring("returninfo ".length());
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
            this.sendEvent("node "+theNode, 
             "rcxcontrolworker", "nodeinfo "+this.communicationNode.serialNo+" "+theAddress);
            messages.append("return nodeinfo "+this.communicationNode.serialNo+" "+theAddress+ " to the node "+theNode+"\n");
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
                messages.append("command format error\n");
                return false;
            }
            this.connectServer(address);
            return true;
        }
        return false;
    }

    public void sendCodeToRcx(String x)
    {
        /*
            String strInput = textField.getText();
            textField.setText("");
            textArea.append("> "+strInput+"\n");
            */
            this.commandField.setText("");
            this.messages.append("> "+x+"\n");
            byteArray = RCXOpcode.parseString(x);
            if(byteArray==null) {
                messages.append("Error: illegal hex character or length\n");
                return;
            }
            if(rcxPort!=null) {
                if(!rcxPort.write(byteArray)) {
                    messages.append("Error: writing data to port "+portName+"\n");
                }
            }
        }
    


    private String      portName;
    private RCXPort     rcxPort;
    private Panel       textPanel;
    private Panel       topPanel;
    /*
    private TextArea    textArea;
    private TextField   textField;
    private Button      tableButton;
    */
    private Properties  parameters;
    private int inByte;
    private int charPerLine = 48;
    private int lenCount;
    private StringBuffer sbuffer;
    private byte[] byteArray;

    public void receivedMessage(byte[] responseArray) {
        if(responseArray==null)
            return;
        sbuffer=new StringBuffer("");
        for(int loop=0;loop<responseArray.length;loop++) {       
            int newbyte = (int)responseArray[loop];
            if(newbyte<0) newbyte=256+newbyte;
            sbuffer = new StringBuffer(Integer.toHexString(newbyte));
            
            if(sbuffer.length()<2)
                sbuffer.insert(0,'0');
            messages.append(sbuffer+" ");
            lenCount+=3;
            if(lenCount==charPerLine) {
                lenCount=0;
                messages.append("\n");
            }
        }
        if(lenCount!=charPerLine)
            messages.append("\n");        
        this.sendEvent("node "+masterNode,"rcxcontrolmaster", 
               "receive "+this.communicationNode.serialNo+" "+sbuffer.toString());
     }
    public void receivedError(String error) {
        messages.append(error+"\n");
        this.sendEvent("node "+masterNode,"rcxcontrolmaster", 
               "error "+this.communicationNode.serialNo);
    }
        
    public ControlledFrame spawnMain(CommunicationNode gui, String args, int pID, String controlMode, String encodingCode)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
//           NetworkTesterWorkerFrame frame=new NetworkTesterWorkerFrame("NetworkTester Worker");
           RcxControlWorkerFrame frame=this;
           this.setTitle("Rcx Controller Worker");
           frame.communicationNode=gui;
           frame.pID=pID;
           frame.encodingCode=encodingCode;
           frame.ebuff=gui.commandTranceiver.ebuff;

           frame.show();
           return frame;
    }

	public RcxControlWorkerFrame()
	{
	    super();
	    /*
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		setSize(499,410);
		setVisible(false);
		clearButton.setText("clr");
		clearButton.setActionCommand("enter");
		getContentPane().add(clearButton);
		clearButton.setBackground(new java.awt.Color(204,204,204));
		clearButton.setForeground(java.awt.Color.black);
		clearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		clearButton.setBounds(420,348,72,36);
		quitButton.setText("quit");
		quitButton.setActionCommand("quit");
		getContentPane().add(quitButton);
		quitButton.setBackground(new java.awt.Color(204,204,204));
		quitButton.setForeground(java.awt.Color.black);
		quitButton.setFont(new Font("Dialog", Font.BOLD, 12));
		quitButton.setBounds(276,36,108,24);
		enterButton.setText("enter");
		enterButton.setActionCommand("enter");
		getContentPane().add(enterButton);
		enterButton.setBackground(new java.awt.Color(204,204,204));
		enterButton.setForeground(java.awt.Color.black);
		enterButton.setFont(new Font("Dialog", Font.BOLD, 12));
		enterButton.setBounds(336,348,84,36);
		JLabel1.setText("Distributed System Recorder/RCX controller, worker");
		getContentPane().add(JLabel1);
		JLabel1.setBounds(24,12,384,24);
		messagesPane.setOpaque(true);
		getContentPane().add(messagesPane);
		messagesPane.setBounds(24,60,468,288);
		messagesPane.getViewport().add(messages);
		messages.setBackground(java.awt.Color.white);
		messages.setForeground(java.awt.Color.black);
		messages.setFont(new Font("SansSerif", Font.PLAIN, 12));
		messages.setBounds(0,0,464,284);
		getContentPane().add(commandField);
		commandField.setBackground(java.awt.Color.white);
		commandField.setForeground(java.awt.Color.black);
		commandField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		commandField.setBounds(108,348,228,36);
		JLabel2.setText("command:");
		getContentPane().add(JLabel2);
		JLabel2.setBounds(24,348,84,36);
		messageClearButton.setText("clear");
		messageClearButton.setActionCommand("clear");
		getContentPane().add(messageClearButton);
		messageClearButton.setBackground(new java.awt.Color(204,204,204));
		messageClearButton.setForeground(java.awt.Color.black);
		messageClearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		messageClearButton.setBounds(384,36,108,24);
		propertiesButton.setText("properties");
		getContentPane().add(propertiesButton);
		propertiesButton.setBackground(new java.awt.Color(204,204,204));
		propertiesButton.setForeground(java.awt.Color.black);
		propertiesButton.setFont(new Font("Dialog", Font.BOLD, 12));
		propertiesButton.setBounds(168,36,108,24);
		//}}

		//{{REGISTER_LISTENERS
//		SymWindow aSymWindow = new SymWindow();
//		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		propertiesButton.addActionListener(lSymAction);
		quitButton.addActionListener(lSymAction);
		messageClearButton.addActionListener(lSymAction);
		enterButton.addActionListener(lSymAction);
		clearButton.addActionListener(lSymAction);
		//}}

		//{{INIT_MENUS
		//}}
	    super.registerListeners();
	    */
        this.JLabel1.setText("Rcx Controller, worker");
	}

	public RcxControlWorkerFrame(String title)
	{
		this();
		setTitle(title);
	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
		Object object = event.getSource();
		if (object == RcxControlWorkerFrame.this)
			RcxControlWorkerFrame_WindowClosing(event);
		}
	}

	void RcxControlWorkerFrame_WindowClosing(java.awt.event.WindowEvent event)
	{
		dispose();		 // dispose of the Frame.
	}
	//{{DECLARE_CONTROLS
	//javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == propertiesButton)
				propertiesButton_actionPerformed(event);
			else if (object == quitButton)
				quitButton_actionPerformed(event);
			else if (object == messageClearButton)
				messageClearButton_actionPerformed(event);
			else if (object == enterButton)
				enterButton_actionPerformed(event);
			else if (object == clearButton)
				clearButton_actionPerformed(event);
		}
	}

	void propertiesButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
//		propertiesButton_actionPerformed_Interaction1(event);
	}

	void quitButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
//		quitButton_actionPerformed_Interaction1(event);
	}

	void messageClearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
//		messageClearButton_actionPerformed_Interaction1(event);
	}

	void enterButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
//		enterButton_actionPerformed_Interaction1(event);
	}

	void clearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
//		clearButton_actionPerformed_Interaction1(event);
	}
}