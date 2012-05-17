package application.serialcontroller;

import java.awt.Font;
import java.io.File;
import java.util.Vector;

import nodesystem.CommunicationNode;
import nodesystem.EditDialog;
import application.networktester.NetworkTesterWorkerFrame;
import application.texteditor.TextEditFrame;
import controlledparts.ControlledButton;
import controlledparts.ControlledFrame;
import controlledparts.ControlledScrollPane;
import controlledparts.ControlledTextArea;
import controlledparts.SelectedButton;

public class SerialControlWorkerFrame extends NetworkTesterWorkerFrame 
{

    public String propertyFileName;

    public void setPropertyFile(String x)
    {
        this.propertyFileName=x;
    }

/*
      initPort
      f: serial port setting file name
*/
    public void initPort(String f)
    {
    }

    public void mouseClickedAtButton(int i)
    {
        if(i==this.enterButton.getID()) {
        //	 buttons.addElement( this.enterButton);
    		String command=commandField.getText();
	    	if(command.equals("")) return;
	    	int startTime=communicationNode.eventRecorder.timer.getTime();
	    	String theMessage=""+startTime + "," + command;
	        this.messages.append(theMessage+"\n");
            this.communicationNode.eventRecorder.recordMessage(theMessage);
		    if(parse(command)){
	    	}
		    else{
		        this.messages.append("failed\n");
	     	}
	     	clickButton(this.clearButton.getID());
        }
        else
        if(i==this.clearButton.getID()){
	    //   buttons.addElement( this.clearButton);
	         this.commandField.setText("");
	    }
	    else
	    if(i==this.quitButton.getID()){
	    //   buttons.addElement( this.quitButton);
	    //    close();
        	 this.parse("quit");
	    }
	    else
	    if(i==this.messageClearButton.getID()){
	    // messageClearButton
	         this.messages.setText("");
	         messages.repaint();
	    }
	    else
	    if(i==this.propertiesButton.getID()){
	        this.openProperties(this.propertyFileName);
	    }
	    else
	    if(i==this.initButton.getID()){
	        this.init2();
	    }
    }

    public void openProperties(String pfile)
    {
        this.properties=new TextEditFrame();
        this.properties.setControlledFrame(this);
        this.properties.communicationNode=this.communicationNode;
        String url="";
   		File dataPath=this.communicationNode.commonDataDir;
	    File thePath=new File(dataPath.getPath(),pfile);
        String separator=""+System.getProperty("file.separator");
        if(separator.equals("\\"))
            	url="file:///"+thePath.getPath();
        else
                url="file://"+thePath.getPath();
        properties.getUrlField().setText(pfile);
        this.properties.loadText(url);  
        this.properties.show(true);
    }

    public TextEditFrame properties;


    public ControlledFrame spawnMain(CommunicationNode gui, String args, int pID, String controlMode, String encodingCode)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
//           NetworkTesterWorkerFrame frame=new NetworkTesterWorkerFrame("NetworkTester Worker");
           SerialControlWorkerFrame frame=this;
           this.setTitle("Serial Controller Worker");
           frame.communicationNode=gui;
           frame.pID=pID;
           frame.encodingCode=encodingCode;
           frame.ebuff=gui.commandTranceiver.ebuff;

           frame.show();
           return frame;
    }

    public void focusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
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

    public void run()
    {
        // This method is derived from interface java.lang.Runnable
        // to do: code goes here
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

	public SerialControlWorkerFrame()
	{
    	panes=new Vector();
	    panes.addElement(this.messagesPane );
	    int numberOfPanes=panes.size();
	    for(int i=0;i<numberOfPanes;i++){
	        ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(i));
	        p.setFrame(this);
	        p.setID(i);
	    }
//	    super();
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		setSize(499,410);
		setVisible(false);
		propertiesButton.setText("properties");
		propertiesButton.setActionCommand("properties");
		getContentPane().add(propertiesButton);
		propertiesButton.setBackground(new java.awt.Color(204,204,204));
		propertiesButton.setForeground(java.awt.Color.black);
		propertiesButton.setFont(new Font("Dialog", Font.BOLD, 12));
		propertiesButton.setBounds(60,36,108,24);
		JLabel2.setText("command:");
		getContentPane().add(JLabel2);
		JLabel2.setBounds(24,348,84,36);
		initButton.setText("init");
		initButton.setActionCommand("init");
		initButton.setToolTipText("initialize serial port");
		getContentPane().add(initButton);
		initButton.setBackground(new java.awt.Color(204,204,204));
		initButton.setForeground(java.awt.Color.black);
		initButton.setFont(new Font("Dialog", Font.BOLD, 12));
		initButton.setBounds(168,36,108,24);
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
		initButton.addActionListener(lSymAction);
		//}}

		//{{INIT_MENUS
		//}}
		
    	buttons=new Vector();
	    buttons.addElement( this.enterButton);
	    buttons.addElement( this.clearButton);
	    buttons.addElement( this.quitButton);
	    buttons.addElement( this.messageClearButton);
	    buttons.addElement( this.propertiesButton);
	    buttons.addElement( this.initButton);
	
    	int numberOfButtons=buttons.size();
	
	    for(int i=0;i<numberOfButtons;i++){
	        SelectedButton b=(SelectedButton)(buttons.elementAt(i));
	        b.setFrame(this);
	        b.setID(i);
	    }
	
    	texts=new Vector();
	    texts.addElement(this.messages);
	    texts.addElement(this.commandField);
	
    	int numberOfTextAreas=texts.size();
	    for(int i=0;i<numberOfTextAreas;i++){
	        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
	        t.setFrame(this);
	        t.setID(i);
	    }
	}

	public SerialControlWorkerFrame(String title)
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
		if (object == SerialControlWorkerFrame.this)
			SerialControlWorkerFrame_WindowClosing(event);
		}
	}

	void SerialControlWorkerFrame_WindowClosing(java.awt.event.WindowEvent event)
	{
		dispose();		 // dispose of the Frame.
	}
	//{{DECLARE_CONTROLS
	protected ControlledButton propertiesButton = new ControlledButton();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	ControlledButton initButton = new ControlledButton();
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
			else if (object == initButton)
				initButton_actionPerformed(event);
		}
	}

	void propertiesButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		propertiesButton_actionPerformed_Interaction1(event);
	}

	void propertiesButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			propertiesButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void quitButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		quitButton_actionPerformed_Interaction1(event);
	}

	void quitButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			quitButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void messageClearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		messageClearButton_actionPerformed_Interaction1(event);
	}

	void messageClearButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			messageClearButton.show();
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
	//		enterButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void clearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		clearButton_actionPerformed_Interaction1(event);
	}

	void clearButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			clearButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void initButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		initButton_actionPerformed_Interaction1(event);
	}

	void initButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			initButton.requestFocus();
		} catch (java.lang.Exception e) {
		}
	}
}