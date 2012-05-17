package nodesystem.groupmanager;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;

import util.TraceSW;

import nodesystem.Client;
import nodesystem.ConnectionReceiver;
import nodesystem.Distributor;
import nodesystem.EchobackDistributor;
import nodesystem.eventrecorder.*;

public class MulticastReflectorFrame extends javax.swing.JFrame 
implements java.lang.Runnable, nodesystem.MessageGui, TraceSW

{
    public Thread me;

    public void stop()
    {
        if(me!=null){
            me=null;
        }
    }

    public void start()
    {
        if(me==null){
            me=new Thread(this,"MulticastReflectorFrame");
            me.start();
        }
    }

    public void run()
    {
        // This method is derived from interface java.lang.Runnable
        // to do: code goes here
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

    void acceptStartCS()
    {
        this.disConnect();
        this.clientNo=0;
        Distributor ds=null;
        ds=new EchobackDistributor("",40);
        this.distributor=ds;
		wellKnownPort=(new Integer(listenPort.getText())).intValue();
		connectionReceiver=new ConnectionReceiver(
		                   this,distributor,wellKnownPort,null,this);
    }

    void acceptStartCSB()
    {
        this.disConnect();
        this.clientNo=0;
        Distributor ds=null;
        ds=new Distributor("",40);
        this.distributor=ds;
		wellKnownPort=(new Integer(listenPort.getText())).intValue();
		connectionReceiver=new ConnectionReceiver(
		                   this,distributor,wellKnownPort,null,this);
    }

    void acceptStart()
    {
        if(this.radioButtonMcastCS.isSelected()){
            this.acceptStartMcast();
        }
        if(this.radioButtonCSB.isSelected()){
            this.acceptStartCSB();
        }
        if(this.radioButtonCS.isSelected()){
            this.acceptStartCS();
        }
    }

    ButtonGroup radioButtonGroup;

    int clientNo;

    public Distributor distributor;
    int wellKnownPort;
    ConnectionReceiver connectionReceiver;
    public Timer timer;
    
    void disConnect()
    {
        if(this.connectionReceiver!=null)
           this.connectionReceiver.stop();
    }

   	/**
	 * The entry point for this application.
	 * Sets the Look and Feel to the System Look and Feel.
	 * Creates a new JFrame1 and makes it visible.
	 */
	static public void main(String args[])
	{
		try {
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
			(new MulticastReflectorFrame()).setVisible(true);
		} 
		catch (Throwable t) {
			t.printStackTrace();
			//Ensure the application exits with an error condition.
			System.exit(1);
		}
	}


    void acceptStartMcast()
    {
        this.disConnect();
        this.clientNo=0;
		this.distributor=new MulticastDistributor("",10);
		String mcastAddr=this.mcastAddrNameField.getText();
		int mcastPort=(new Integer(this.mcastPortField.getText())).intValue();
		((MulticastDistributor)distributor).setMulticastAddressPort(mcastAddr,mcastPort);
		wellKnownPort=(new Integer(listenPort.getText())).intValue();
		connectionReceiver=new ConnectionReceiver(
		                   this,distributor,wellKnownPort,null,this);
    }

    public void appendMessage(String s)
    {
        // This method is derived from interface nodesystem.MessageGui
        // to do: code goes here
        this.messageArea.append(s+"\n");
    }

    public String nextDoorControl(String command, Client cl)
    {
        // This method is derived from interface nodesystem.MessageGui
        // to do: code goes here
        /*
          nextDoor command

          nextDoor requestTime
          nextDoor returnTime <time>
          nextDoor requestState                         //not yet.
          nextDoor returnState <state of this node>     //not yet.

        */
//        appendMessage("nextDoor "+command+"\n");
        
        String rtn=null;
        StringTokenizer st=new StringTokenizer(command," ");
        String dmy=st.nextToken();
        String cmd=st.nextToken();
        if(cmd.equals("requestTime")){
            int t=this.timer.getMilliTime();
            String rcmd="nextDoor returnTime "+t;
            try{
                if(cl!=null)
                  cl.sio.writeString(rcmd);
            }
            catch(IOException e){}
        }
        if(cmd.equals("requestNo")){
            this.clientNo++;
            String rcmd="nextDoor returnNo "+this.clientNo;
            try{
                if(cl!=null)
                cl.sio.writeString(rcmd);
            }
            catch(IOException e){
                this.clientNo--;
            }
        }
        if(cmd.equals("setTime")){
            String t=st.nextToken();
            int tx=(new Integer(t)).intValue();
            this.timer.setMilliTime(tx);
        }
        rtn="";
        return rtn;
    }

    public String nodeControl(String s)
    {
        // This method is derived from interface nodesystem.MessageGui
        // to do: code goes here
        return null;
    }

    
	public MulticastReflectorFrame()
	{
		//{{INIT_CONTROLS
		setTitle("Reflector");
		getContentPane().setLayout(null);
		setSize(470,389);
		setVisible(false);
		getContentPane().add(myAddressField);
		myAddressField.setBounds(132,48,240,24);
		getContentPane().add(listenPort);
		listenPort.setBounds(372,48,84,24);
		JLabel1.setText("myAddr, tcp-port");
		getContentPane().add(JLabel1);
		JLabel1.setBounds(12,48,120,24);
		getContentPane().add(mcastAddrNameField);
		mcastAddrNameField.setBounds(132,96,240,24);
		JLabel2.setText("multicast addr, port");
		getContentPane().add(JLabel2);
		JLabel2.setBounds(12,96,132,24);
		getContentPane().add(mcastPortField);
		mcastPortField.setBounds(372,96,84,24);
		JScrollPane1.setOpaque(true);
		getContentPane().add(JScrollPane1);
		JScrollPane1.setBounds(12,192,444,144);
		JScrollPane1.getViewport().add(messageArea);
		messageArea.setBounds(0,0,440,140);
		reStartButton.setText("restart");
		reStartButton.setActionCommand("rs");
		getContentPane().add(reStartButton);
		reStartButton.setBounds(264,12,96,24);
		exitButton.setText("exit");
		exitButton.setActionCommand("ext");
		getContentPane().add(exitButton);
		exitButton.setBounds(360,12,96,24);
		radioButtonCSB.setText("TCP, no echo, Balking");
		radioButtonCSB.setActionCommand("TCP, no echo, Balking");
		getContentPane().add(radioButtonCSB);
		radioButtonCSB.setBounds(12,144,228,24);
		radioButtonCS.setText("TCP, echo back");
		radioButtonCS.setActionCommand("TCP, echo back");
		getContentPane().add(radioButtonCS);
		radioButtonCS.setBounds(240,144,216,24);
		radioButtonMcastCS.setText("Multicast, echo back");
		radioButtonMcastCS.setActionCommand("Multicast, echo back");
		getContentPane().add(radioButtonMcastCS);
		radioButtonMcastCS.setBounds(240,168,216,24);
		getContentPane().add(radioButtonMcastCS);
		timelabel.setText("time");
		getContentPane().add(timelabel);
		timelabel.setBounds(12,336,60,24);
		getContentPane().add(timeField);
		timeField.setBounds(72,336,84,24);
		JLabel3.setText("DSR Reflector");
		getContentPane().add(JLabel3);
		JLabel3.setBounds(12,12,228,24);
		JLabel4.setText("This address is used for the up-link from the clients.");
		getContentPane().add(JLabel4);
		JLabel4.setBounds(36,72,408,24);
		JLabel5.setText("This address is used for the multicast down-link to the clients.");
		getContentPane().add(JLabel5);
		JLabel5.setBounds(36,120,420,24);
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		reStartButton.addActionListener(lSymAction);
		exitButton.addActionListener(lSymAction);
		SymItem lSymItem = new SymItem();
		radioButtonCSB.addItemListener(lSymItem);
		radioButtonCS.addItemListener(lSymItem);
		radioButtonMcastCS.addItemListener(lSymItem);
		radioButtonCSB.addActionListener(lSymAction);
		radioButtonCS.addActionListener(lSymAction);
		radioButtonMcastCS.addActionListener(lSymAction);
		//}}

		//{{INIT_MENUS
		//}}

        this.radioButtonGroup=new ButtonGroup();
        this.radioButtonGroup.add(this.radioButtonCSB);
        this.radioButtonGroup.add(this.radioButtonCS);
        this.radioButtonGroup.add(this.radioButtonMcastCS);

		this.timer=Timer.getTimer();
		timer.start();

        wellKnownPort=22222;

        listenPort.setText(""+wellKnownPort);

	    try{
             String myaddr=(InetAddress.getLocalHost()).toString();
             this.myAddressField.setText(myaddr);
        }
        catch(UnknownHostException e){}
        
        this.mcastAddrNameField.setText("224.0.1.1");
        this.mcastPortField.setText("17320");

        this.radioButtonCSB.setSelected(true);
        
        this.start();

        this.acceptStart();

	}

	public MulticastReflectorFrame(String title)
	{
		this();
		setTitle(title);
	}
	public void setVisible(boolean b)
	{
		if(b)
		{
			setLocation(50, 50);
		}
	super.setVisible(b);
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		Insets ins = getInsets();
		setSize(ins.left + ins.right + d.width, ins.top + ins.bottom + d.height);
		Component components[] = getContentPane().getComponents();
		for (int i = 0; i < components.length; i++)
			{
			Point p = components[i].getLocation();
			p.translate(ins.left, ins.top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
		Object object = event.getSource();
		if (object == MulticastReflectorFrame.this)
			MulticastReflectorFrame_WindowClosing(event);
		}
	}

	void MulticastReflectorFrame_WindowClosing(java.awt.event.WindowEvent event)
	{
		dispose();		 // dispose of the Frame.
	}
	//{{DECLARE_CONTROLS
	javax.swing.JTextField myAddressField = new javax.swing.JTextField();
	javax.swing.JTextField listenPort = new javax.swing.JTextField();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JTextField mcastAddrNameField = new javax.swing.JTextField();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	javax.swing.JTextField mcastPortField = new javax.swing.JTextField();
	javax.swing.JScrollPane JScrollPane1 = new javax.swing.JScrollPane();
	javax.swing.JTextArea messageArea = new javax.swing.JTextArea();
	javax.swing.JButton reStartButton = new javax.swing.JButton();
	javax.swing.JButton exitButton = new javax.swing.JButton();
	javax.swing.JRadioButton radioButtonCSB = new javax.swing.JRadioButton();
	javax.swing.JRadioButton radioButtonCS = new javax.swing.JRadioButton();
	javax.swing.JRadioButton radioButtonMcastCS = new javax.swing.JRadioButton();
	javax.swing.JLabel timelabel = new javax.swing.JLabel();
	javax.swing.JLabel timeField = new javax.swing.JLabel();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel4 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel5 = new javax.swing.JLabel();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == reStartButton)
				reStartButton_actionPerformed(event);
			else if (object == exitButton)
				exitButton_actionPerformed(event);
			else if (object == radioButtonCSB)
				radioButtonCSB_actionPerformed(event);
			else if (object == radioButtonCS)
				radioButtonCS_actionPerformed(event);
			else if (object == radioButtonMcastCS)
				radioButtonMcastCS_actionPerformed(event);
		}
	}

	void reStartButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		reStartButton_actionPerformed_Interaction1(event);
	}

	void reStartButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	    this.messageArea.setText("");
	    this.disConnect();
	    this.timer.setTime(0);
	    this.clientNo=0;
	    this.acceptStart();
	}

	void exitButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		exitButton_actionPerformed_Interaction1(event);
	}

	void exitButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	    this.disConnect();
	    this.stop();
		try {
			this.dispose();
		} catch (java.lang.Exception e) {
		}
		System.exit(0);
	}

	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if (object == radioButtonCSB)
				radioButtonCSB_itemStateChanged(event);
			else if (object == radioButtonCS)
				radioButtonCS_itemStateChanged(event);
			else if (object == radioButtonMcastCS)
				radioButtonMcastCS_itemStateChanged(event);
		}
	}

	void radioButtonCSB_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
	}

	void radioButtonCS_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
	}

	void radioButtonMcastCS_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
	}

	void radioButtonCSB_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		radioButtonCSB_actionPerformed_Interaction1(event);
	}

	void radioButtonCSB_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			radioButtonCSB.setSelected(true);
		} catch (java.lang.Exception e) {
		}
	}

	void radioButtonCS_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		radioButtonCS_actionPerformed_Interaction1(event);
	}

	void radioButtonCS_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			radioButtonCS.setSelected(true);
		} catch (java.lang.Exception e) {
		}
	}

	void radioButtonMcastCS_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		radioButtonMcastCS_actionPerformed_Interaction1(event);
	}

	void radioButtonMcastCS_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			radioButtonMcastCS.setSelected(true);
		} catch (java.lang.Exception e) {
		}
	}

	public boolean isTracing() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	public void setTrace(boolean tf) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}