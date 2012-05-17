package nodesystem.groupmanager;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;

import util.TraceSW;

import nodesystem.Client;
import nodesystem.ConnectionReceiver;
import nodesystem.Distributor;
import nodesystem.EchobackDistributor;
import nodesystem.eventrecorder.*;

public class ReflectorFrame extends javax.swing.JFrame 
implements nodesystem.MessageGui, TraceSW
{
    public int clientNo;

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
			(new ReflectorFrame()).setVisible(true);
		} 
		catch (Throwable t) {
			t.printStackTrace();
			//Ensure the application exits with an error condition.
			System.exit(1);
		}
	}


    int wellKnownPort;

    ConnectionReceiver connectionReceiver;

    public Timer timer;


    void acceptStart()
    {
        this.disConnect();
        this.clientNo=0;
        Distributor ds=null;
        if(this.echoBackCheckBox.isSelected()){
            ds=new EchobackDistributor("",40);
        }
        else{
            ds=new Distributor("",40);
        }
//		this.distributor=new EchobackDistributor("",10);
        this.distributor=ds;
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
        rtn="";
        return rtn;
    }

    public String nodeControl(String s)
    {
        // This method is derived from interface nodesystem.MessageGui
        // to do: code goes here
        return null;
    }

    public Distributor distributor;
    
	public ReflectorFrame()
	{
		//{{INIT_CONTROLS
		setTitle("A Simple Frame");
		getContentPane().setLayout(null);
		setSize(385,344);
		setVisible(false);
		JScrollPane1.setOpaque(true);
		getContentPane().add(JScrollPane1);
		JScrollPane1.setBounds(12,96,360,204);
		JScrollPane1.getViewport().add(messageArea);
		messageArea.setBounds(0,0,356,200);
		getContentPane().add(listenPort);
		listenPort.setBounds(216,48,60,24);
		getContentPane().add(myAddressField);
		myAddressField.setBounds(48,48,168,24);
		JLabel1.setText("Reflector for experiments");
		getContentPane().add(JLabel1);
		JLabel1.setBounds(72,12,276,36);
		JLabel2.setText("addr");
		getContentPane().add(JLabel2);
		JLabel2.setBounds(12,48,36,24);
		reStartButton.setText("re-start");
		reStartButton.setActionCommand("rs");
		getContentPane().add(reStartButton);
		reStartButton.setBounds(276,48,96,24);
		getContentPane().add(exitButton);
		exitButton.setBounds(276,72,96,24);
		exitButton.setIcon(new ImageIcon("images/exit-icon.GIF"));
		echoBackCheckBox.setText("with echo back");
		echoBackCheckBox.setActionCommand("with echo back");
		getContentPane().add(echoBackCheckBox);
		echoBackCheckBox.setBounds(48,72,228,24);
		JLabel3.setText("time");
		getContentPane().add(JLabel3);
		JLabel3.setBounds(12,300,48,24);
		getContentPane().add(timeLabel);
		timeLabel.setBounds(72,300,84,24);
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		reStartButton.addActionListener(lSymAction);
		exitButton.addActionListener(lSymAction);
		//}}

		//{{INIT_MENUS
		//}}
		
		this.timer=Timer.getTimer();
		timer.start();

        wellKnownPort=22222;

        
        listenPort.setText(""+wellKnownPort);

	    try{
             String myaddr=(InetAddress.getLocalHost()).toString();
             this.myAddressField.setText(myaddr);
        }
        catch(UnknownHostException e){}

        this.acceptStart();

	}

	public ReflectorFrame(String title)
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
		if (object == ReflectorFrame.this)
			ReflectorFrame_WindowClosing(event);
		}
	}

	void ReflectorFrame_WindowClosing(java.awt.event.WindowEvent event)
	{
		dispose();		 // dispose of the Frame.
	}
	//{{DECLARE_CONTROLS
	javax.swing.JScrollPane JScrollPane1 = new javax.swing.JScrollPane();
	javax.swing.JTextArea messageArea = new javax.swing.JTextArea();
	javax.swing.JTextField listenPort = new javax.swing.JTextField();
	javax.swing.JTextField myAddressField = new javax.swing.JTextField();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	javax.swing.JButton reStartButton = new javax.swing.JButton();
	javax.swing.JButton exitButton = new javax.swing.JButton();
	javax.swing.JCheckBox echoBackCheckBox = new javax.swing.JCheckBox();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	javax.swing.JLabel timeLabel = new javax.swing.JLabel();
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
		}
	}

	void reStartButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		reStartButton_actionPerformed_Interaction1(event);
	}

	void reStartButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			reStartButton.show();
		} catch (java.lang.Exception e) {
		}
		this.acceptStart();
	}

	void exitButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		exitButton_actionPerformed_Interaction1(event);
	}

	void exitButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	    this.show(false);
		hide();
		dispose();
        System.exit(0);
	}

	public boolean isTracing() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	public void setTrace(boolean tf) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}