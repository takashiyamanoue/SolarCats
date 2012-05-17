package nodesystem.groupmanager;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import util.TraceSW;

import nodesystem.Client;
import nodesystem.Distributor;
import nodesystem.MessageGui;
import nodesystem.NodeSettings;
import nodesystem.StringIO;
import nodesystem.eventrecorder.*;
import nodesystem.groupmanager.GroupManagerLogger;

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
/**
 * A basic JFC based application.
 */
public class GroupManager extends javax.swing.JFrame 
implements java.lang.Runnable, MessageGui, TraceSW 

{
    public String nextDoorControl(String s, Client c)
    {
        return null;
    }

    public int upperRootPort;

    public String upperRootAddress;

    int serialNo;

    public void decreaseSerialNumber(){
    	this.serialNo--;
    }
    
    public int getNewSerialNumber()
    {
        if(this.upperSio==null){
            this.serialNo++;
            return this.serialNo;
        }
        else{
            try{
               upperSio.writeString("getSerialNo");
               String x=(upperSio.readString()).getHead();
               return (new Integer(x)).intValue();
            }
            catch(IOException e){}
            return -1;
        }
    }
    public int getSerialNumber(){
    	return this.serialNo;
    }
    public void setSerialNumber(int x){
    	if(this.upperSio==null){
    		this.serialNo=x;
    	}
    	else{
    		try{
    			upperSio.writeString("setSerialNo "+x);
    		}
    		catch(IOException e){}
    	}
    }

    StringIO upperSio;

    DataOutputStream upperOut;

    DataInputStream upperIn;

    public Socket upperSocket;


 
    public Thread me;

    public void run()
    {
        while(me!=null){
            try{
                Thread.sleep(1000);
            }
            catch(InterruptedException e){
            }
            int timeNow=(timer.getTime())/10;
            timeField.setText(""+timeNow);
            timeField.repaint();
        }
    }

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
            me=new Thread(this,"GroupManager");
            me.start();
        }
    }

    public Timer timer;
    private JButton showConnectionButton;
    private JTextField nodePortNumber;
    private JRadioButton fixedPort;
    private JRadioButton portFloatingButton;
    private JCheckBox traceCheckBox;
    private ButtonGroup portManageButtonGroup;

    public String nodeControl(String command)
    {
        /*
          node control command

          nodeControl returnTime

        */
        String rtn=null;
        StringTokenizer st=new StringTokenizer(command," ");
        String token=st.nextToken();
        token=st.nextToken();
        if(token.equals("returnTime")){
            int t=timer.getTime();
            rtn=""+t;
            return rtn;
        }
        if(token.equals("getSerialNo")){
            rtn=""+this.getNewSerialNumber();
            return rtn;
        }
        if(token.equals("setSerialNo")){
        	String no=st.nextToken();
            int n=(new Integer(no)).intValue();
        	this.setSerialNumber(n);
        	return "";
        }
        if(token.equals("getRoot")){
            String rootAddr=this.treeManager.root.nodeInfo.inetAddr;
            String rootPort=""+this.treeManager.root.nodeInfo.port;
            rtn=rootAddr+":"+rootPort;
            return rtn;
        }
        if(token.equals("init")){
        	this.initButton_actionPerformed_Interaction1(null);
        	return "";
        }
        rtn="";
        return rtn;
    }

    Vector messageQueue=new Vector();
    public void appendMessage(String s)
    {
    	messageQueue.add(s);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					String sx=(String)(messageQueue.remove(0));
                  messageArea.append(sx);
                  messageArea.setCaretPosition(
                     messageArea.getText().length());
				}
			});
		if(this.logger!=null){
			this.logger.info(s);
		}
    }

    public ConnectionReceiver2 connectionReceiver;

    public TreeManager treeManager;

    public Distributor distributor;

    public int wellKnownPort;

    final GroupManagerLogger logger = new GroupManagerLogger(); 

	public GroupManager()
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		this.setSize(554, 384);
		setVisible(false);
		JPanel1.setLayout(null);
		getContentPane().add(JPanel1);
		JPanel1.setBackground(new java.awt.Color(204,204,204));
		JPanel1.setBounds(0,12,552,324);
		JScrollPane1.setOpaque(true);
		JPanel1.add(JScrollPane1);
		JScrollPane1.setBounds(85, 138, 455, 102);
		messageArea.setSelectionColor(new java.awt.Color(204,204,255));
		messageArea.setSelectedTextColor(java.awt.Color.black);
		messageArea.setCaretColor(java.awt.Color.black);
		messageArea.setDisabledTextColor(new java.awt.Color(153,153,153));
		JScrollPane1.getViewport().add(messageArea);
		messageArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		messageArea.setBounds(0,0,452,128);
		myAddressField.setSelectionColor(new java.awt.Color(204,204,255));
		myAddressField.setSelectedTextColor(java.awt.Color.black);
		myAddressField.setCaretColor(java.awt.Color.black);
		myAddressField.setDisabledTextColor(new java.awt.Color(153,153,153));
		JPanel1.add(myAddressField);
		myAddressField.setBackground(java.awt.Color.white);
		myAddressField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		myAddressField.setBounds(84,60,168,24);
		JLabel1.setText("my address");
		JPanel1.add(JLabel1);
		JLabel1.setBackground(new java.awt.Color(204,204,204));
		JLabel1.setForeground(new java.awt.Color(102,102,153));
		JLabel1.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel1.setBounds(12,60,72,24);
		myPortField.setSelectionColor(new java.awt.Color(204,204,255));
		myPortField.setSelectedTextColor(java.awt.Color.black);
		myPortField.setCaretColor(java.awt.Color.black);
		myPortField.setDisabledTextColor(new java.awt.Color(153,153,153));
		JPanel1.add(myPortField);
		myPortField.setBackground(java.awt.Color.white);
		myPortField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		myPortField.setBounds(252,60,72,24);
		quitButton.setText("Quit");
		quitButton.setActionCommand("Quit");
		JPanel1.add(quitButton);
		quitButton.setBackground(new java.awt.Color(204,204,204));
		quitButton.setFont(new Font("Dialog", Font.BOLD, 12));
		quitButton.setBounds(432,60,108,24);
		JLabel2.setText("Distributed System Recorder/ Group Manager");
		JPanel1.add(JLabel2);
		JLabel2.setBackground(new java.awt.Color(204,204,204));
		JLabel2.setForeground(new java.awt.Color(102,102,153));
		JLabel2.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel2.setBounds(24,12,276,36);
		timeField.setSelectionColor(new java.awt.Color(204,204,255));
		timeField.setSelectedTextColor(java.awt.Color.black);
		timeField.setCaretColor(java.awt.Color.black);
		timeField.setDisabledTextColor(new java.awt.Color(153,153,153));
		JPanel1.add(timeField);
		timeField.setBackground(java.awt.Color.white);
		timeField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		timeField.setBounds(84,252,168,25);
		JLabel3.setText("Time:");
		JPanel1.add(JLabel3);
		JLabel3.setBackground(new java.awt.Color(204,204,204));
		JLabel3.setForeground(new java.awt.Color(102,102,153));
		JLabel3.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel3.setBounds(12,240,72,24);
		JPanel1.add(upManagerField);
		upManagerField.setBounds(84,84,168,24);
		JPanel1.add(upPortField);
		upPortField.setBounds(252,84,72,24);
		JLabel4.setText("up manager");
		JPanel1.add(JLabel4);
		JLabel4.setBounds(12,84,72,24);
		connectButton.setText("connect");
		connectButton.setActionCommand("connect");
		JPanel1.add(connectButton);
		connectButton.setBounds(324,84,108,24);
		disconnectButton.setText("disconnect");
		disconnectButton.setActionCommand("disconnect");
		JPanel1.add(disconnectButton);
		disconnectButton.setBounds(432,84,108,24);
		initButton.setText("init");
		initButton.setActionCommand("init");
		JPanel1.add(initButton);
		initButton.setBounds(324,60,108,24);
		{
			portFloatingButton = new JRadioButton();
			JPanel1.add(portFloatingButton);
			portFloatingButton.setText("Floating Port");
			portFloatingButton.setBounds(85, 109, 120, 23);
		}
		{
			fixedPort = new JRadioButton();
			JPanel1.add(fixedPort);
			fixedPort.setText("Fixed Port");
			fixedPort.setBounds(213, 112, 108, 22);
		}
		{
			nodePortNumber = new JTextField();
			JPanel1.add(nodePortNumber);
			nodePortNumber.setBounds(322, 112, 63, 21);
			nodePortNumber.setText("27182");
		}
		{
			showConnectionButton = new JButton();
			JPanel1.add(showConnectionButton);
			showConnectionButton.setText("show Connection");
			showConnectionButton.setBounds(392, 112, 147, 21);
			showConnectionButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					showConnectionButtonActionPerformed(evt);
				}
			});
		}
		{
			traceCheckBox = new JCheckBox();
			JPanel1.add(traceCheckBox);
			traceCheckBox.setText("trace");
			traceCheckBox.setBounds(287, 252, 77, 28);
		}
		//}}

		//{{INIT_MENUS
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
//		openItem.addActionListener(lSymAction);
//		saveItem.addActionListener(lSymAction);
//		exitItem.addActionListener(lSymAction);
//		aboutItem.addActionListener(lSymAction);
		quitButton.addActionListener(lSymAction);
		connectButton.addActionListener(lSymAction);
		initButton.addActionListener(lSymAction);
		disconnectButton.addActionListener(lSymAction);
		//}}
		//
		//
		wellKnownPort=31415;
        myPortField.setText(""+wellKnownPort);
        this.upPortField.setText(""+wellKnownPort);
        
		this.portManageButtonGroup = new ButtonGroup();
		this.portManageButtonGroup.add(this.portFloatingButton);
		this.portManageButtonGroup.add(this.fixedPort);
        this.fixedPort.setSelected(true);
        
        this.initButton_actionPerformed_Interaction1(null);
	}

    /**
     * Creates a new instance of JFrame1 with the given title.
     * @param sTitle the title for the new frame.
     * @see #JFrame1()
     */
	public GroupManager(String sTitle)
	{
		this();
		setTitle(sTitle);
	}
	
	/**
	 * The entry point for this application.
	 * Sets the Look and Feel to the System Look and Feel.
	 * Creates a new JFrame1 and makes it visible.
	 */
	static public void main(String args[])
	{
		try{
		    // Add the following code if you want the Look and Feel
		    // to be set to the Look and Feel of the native system.
		    /*
		    try {
		        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    } 
		    catch (Exception e) { 
		    }
		    */

			//Create a new instance of our application's frame, and make it visible.
			(new GroupManager()).setVisible(true);
		} 
		catch (Throwable t) {
			t.printStackTrace();
			//Ensure the application exits with an error condition.
			System.exit(1);
		}
	}

    /**
     * Notifies this component that it has been added to a container
     * This method should be called by <code>Container.add</code>, and 
     * not by user code directly.
     * Overridden here to adjust the size of the frame if needed.
     * @see java.awt.Container#removeNotify
     */
	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension size = getSize();
		
		super.addNotify();
		
		if (frameSizeAdjusted)
			return;
		frameSizeAdjusted = true;
		
		// Adjust size of frame according to the insets and menu bar
		javax.swing.JMenuBar menuBar = getRootPane().getJMenuBar();
		int menuBarHeight = 0;
		if (menuBar != null)
		    menuBarHeight = menuBar.getPreferredSize().height;
		Insets insets = getInsets();
		setSize(insets.left + insets.right + size.width, insets.top + insets.bottom + size.height + menuBarHeight);
	}

	// Used by addNotify
	boolean frameSizeAdjusted = false;

	//{{DECLARE_CONTROLS
	javax.swing.JPanel JPanel1 = new javax.swing.JPanel();
	javax.swing.JScrollPane JScrollPane1 = new javax.swing.JScrollPane();
	javax.swing.JTextArea messageArea = new javax.swing.JTextArea();
	javax.swing.JTextField myAddressField = new javax.swing.JTextField();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JTextField myPortField = new javax.swing.JTextField();
	javax.swing.JButton quitButton = new javax.swing.JButton();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	javax.swing.JTextField timeField = new javax.swing.JTextField();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	javax.swing.JTextField upManagerField = new javax.swing.JTextField();
	javax.swing.JTextField upPortField = new javax.swing.JTextField();
	javax.swing.JLabel JLabel4 = new javax.swing.JLabel();
	javax.swing.JButton connectButton = new javax.swing.JButton();
	javax.swing.JButton disconnectButton = new javax.swing.JButton();
	javax.swing.JButton initButton = new javax.swing.JButton();
	//}}

	//{{DECLARE_MENUS
	//}}

	void exitApplication()
	{
		try {
	    	// Beep
	    	Toolkit.getDefaultToolkit().beep();
	    	// Show a confirmation dialog
	    	int reply = JOptionPane.showConfirmDialog(this, 
	    	                                          "Do you really want to exit?", 
	    	                                          "JFC Application - Exit" , 
	    	                                          JOptionPane.YES_NO_OPTION, 
	    	                                          JOptionPane.QUESTION_MESSAGE);
			// If the confirmation was affirmative, handle exiting.
			if (reply == JOptionPane.YES_OPTION)
			{
			    this.stop();
		    	this.setVisible(false);    // hide the Frame
		    	this.dispose();            // free the system resources
		    	
		    	if(this.upperIn!=null){
		    	    this.upperIn.close();
		    	}
		    	if(this.upperOut!=null){
		    	    this.upperOut.close();
		    	}
		    	if(this.upperSio!=null){
		    	    this.upperSio=null;
		    	}
		    	if(this.upperSocket!=null){
		    	    this.upperSocket.close();
		    	}
			    this.connectionReceiver.stop();
		    	System.exit(0);            // close the application
			}
		} catch (Exception e) {
		}
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == GroupManager.this)
				GroupManager_windowClosing(event);
		}
	}

	void GroupManager_windowClosing(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		GroupManager_windowClosing_Interaction1(event);
	}

	void GroupManager_windowClosing_Interaction1(java.awt.event.WindowEvent event) {
		try {
			this.exitApplication();
		} catch (Exception e) {
		}
	}

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
//			if (object == openItem)
//				openItem_actionPerformed(event);
//			else if (object == saveItem)
//				saveItem_actionPerformed(event);
//			else if (object == exitItem)
//				exitItem_actionPerformed(event);
//			else if (object == aboutItem)
//				aboutItem_actionPerformed(event);
//			else 
			if (object == quitButton)
				quitButton_actionPerformed(event);
			else if (object == connectButton)
				connectButton_actionPerformed(event);
			else if (object == initButton)
				initButton_actionPerformed(event);
			else if (object == disconnectButton)
				disconnectButton_actionPerformed(event);
		}
	}

	void openItem_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		openItem_actionPerformed_Interaction1(event);
	}

	void openItem_actionPerformed_Interaction1(java.awt.event.ActionEvent event) {
		try {
			// openFileDialog Show the FileDialog
//			openFileDialog.setVisible(true);
		} catch (Exception e) {
		}
	}

	void saveItem_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		saveItem_actionPerformed_Interaction1(event);
	}

	void saveItem_actionPerformed_Interaction1(java.awt.event.ActionEvent event) {
		try {
			// saveFileDialog Show the FileDialog
//			saveFileDialog.setVisible(true);
		} catch (Exception e) {
		}
	}

	void exitItem_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		exitItem_actionPerformed_Interaction1(event);
	}

	void exitItem_actionPerformed_Interaction1(java.awt.event.ActionEvent event) {
		try {
			this.exitApplication();
		} catch (Exception e) {
		}
	}

	void aboutItem_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		aboutItem_actionPerformed_Interaction1(event);
	}

	void aboutItem_actionPerformed_Interaction1(java.awt.event.ActionEvent event) {
	}

	void quitButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		quitButton_actionPerformed_Interaction1(event);
	}

	void quitButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// GroupManager Hide the GroupManager
			this.setVisible(false);
		} catch (Exception e) {
		}
		this.exitApplication();
	}

	void connectButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		connectButton_actionPerformed_Interaction1(event);
	}

	void connectButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	    /*
	        connect to the upper group manager, in order to pass through a fire wall.
	        
	            1. connect to the upper group manager.
	            2. get the information of the root node of the upper group.
	        
	        when the root node of this group is joining this group,
    	        1. the node connect to this group manager. 
	            2. redirect the node to the root node of the upper group.
	    */
		
        int serverPort=(new Integer(this.upPortField.getText())).intValue();
        String serverAddress=this.upManagerField.getText();
        /*
        try{
            this.upperSocket=new Socket(serverAddress,serverPort);
        }
        catch(IOException e){
            appendMessage("up GroupManager is down?, connection failed.\n");
            upperSocket=null;
            upperOut=null;
        }
        try{
            upperIn=new DataInputStream(
                        //new BufferedInputStream(
                        upperSocket.getInputStream()
                        //)
                        );
            upperOut=new DataOutputStream(
                        // new BufferedOutputStream(
                        upperSocket.getOutputStream()
                        //)
                        );
        }
        catch(IOException e){
            appendMessage("faild at in/out stream making\n");
        }
        */
        upperSio=new StringIO(serverAddress,serverPort,this);
        upperSio.setTracing(this);
        appendMessage("connected to "+serverAddress+"\n");
//        upperSio=new StringIO(upperIn,upperOut);

        try{
           this.upperSio.writeString("getRoot");
           String uAddrP=(this.upperSio.readString()).getHead();
           StringTokenizer st=new StringTokenizer(uAddrP,":");
           String uaddrx=st.nextToken();
           String uport=st.nextToken();
        /*
        st=new StringTokenizer(uaddrx,"/");
        String uaddr=st.nextToken();
        */
           this.appendMessage(uaddrx+":"+uport+"\n");
           this.upperRootAddress=uaddrx;
           this.upperRootPort=(new Integer(uport)).intValue();
        
           String stime=(upperSio.readString()).getHead();
           this.timer.setTime((new Integer(stime)).intValue());
           this.appendMessage("time from upper group manager is "+stime+"\n");
        }
        catch(IOException e){
        }
	}

	public void initButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		initButton_actionPerformed_Interaction1(event);
	}

	void initButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	    this.stop();
	    
	    this.messageArea.setText("");
	    this.upManagerField.setText("");
		distributor=new Distributor("",5);

        treeManager=new TreeManager(this);
        if(this.connectionReceiver!=null) {
            this.connectionReceiver.stop();
        }
		connectionReceiver=new ConnectionReceiver2(
		                   this,wellKnownPort,treeManager);
	    try{
             String myaddr=(InetAddress.getLocalHost()).toString();
             myAddressField.setText(myaddr);
        }
        catch(UnknownHostException e){}	
		this.setTitle("GroupManager");
		timer=Timer.getTimer();
		this.serialNo=0;
		if(this.portFloatingButton.isSelected()){
			this.treeManager.setPortMode("floating");
		}
		else{
			this.treeManager.setPortMode(this.nodePortNumber.getText());
		}
		
		start();
	}

	void disconnectButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		disconnectButton_actionPerformed_Interaction1(event);
	}

	void disconnectButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	    if(this.upperSocket==null) return;
        try{
            this.upperIn.close();
            this.upperOut.close();
            this.upperSocket.close();
        }
        catch(IOException e){ return ; }
        this.upperSio=null;
        this.upperSocket=null;
	}
	
	private void showConnectionButtonActionPerformed(ActionEvent evt) {
		//TODO add your code for showConnectionButton.actionPerformed
		this.treeManager.showCurrentConnection(this);
	}
	public boolean isTracing(){
		boolean rtn=this.traceCheckBox.isSelected();
		return rtn;
	}

	public void setTrace(boolean tf) {
		// TODO 自動生成されたメソッド・スタブ
		this.traceCheckBox.setSelected(tf);
	}
}
