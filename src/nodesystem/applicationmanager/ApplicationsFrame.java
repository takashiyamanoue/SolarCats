/*
		A basic implementation of the JFrame class.
*/
package nodesystem.applicationmanager;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.net.URL;
import java.util.Vector;

import nodesystem.CommunicationNode;
import nodesystem.StateContainer;

import controlledparts.ControlledButton;
import controlledparts.FrameWithControlledButton;
import controlledparts.SelectedButton;

import javax.swing.*;

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
public class ApplicationsFrame extends javax.swing.JFrame implements FrameWithControlledButton, StateContainer    
{
    public boolean isDirectOperation()
    {
        if(frame==null) return true;
        return this.frame.isDirectOperation();
    }

    public void setApplicationPath()
    {
    	{
			penButton.setText(this.frame.getLclTxt("PEN"));
			penButton.setActionCommand("PEN");
//			controlledButton14.setIcon(new ImageIcon("images/pen-icon.GIF"));
        	this.frame.applicationManager.addApplication(
    	   "PEN",          "application.pen.MainGUI","pen ");
    	}
    	{
 		   drawButton.setText(this.frame.getLclTxt("DrawFrame"));
		   drawButton.setActionCommand("DrawFrame");
//		   controlledButton1.setIcon(new ImageIcon("images/draw-icon.GIF")); 
		   this.frame.applicationManager.addApplication(
		   "DrawFrame",      "application.draw.DrawFrame","drawing ");
    	}
    	{
			basicButton.setText(this.frame.getLclTxt("BasicFrame"));
			basicButton.setActionCommand("BasicFrame");
//			controlledButton2.setIcon(new ImageIcon("images/basic-icon.GIF"));
		    this.frame.applicationManager.addApplication(
		    "BasicFrame",     "application.basic.BasicFrame","basic ");
    	}
    	{
			lispButton.setText(this.frame.getLclTxt("LispFrame"));
			lispButton.setActionCommand("LispFrame");
//			controlledButton17.setIcon(new ImageIcon("images/basic-icon.GIF"));
		    this.frame.applicationManager.addApplication(
		    "LispFrame",     "application.lisp.LispFrame","lisp ");
    	}
    	{
			teditButton.setText(this.frame.getLclTxt("TextEditFrame"));
			teditButton.setActionCommand("TextEditFrame");
			teditButton.setToolTipText("text editor ");
//			controlledButton11.setIcon(new ImageIcon("images/editIcon.GIF"));
		    this.frame.applicationManager.addApplication(
		    "TextEditFrame",  "application.texteditor.TextEditFrame","editor ");
    	}
    	{
			webButton.setText(this.frame.getLclTxt("WebFrame"));
			webButton.setActionCommand("WebFrame");
//			controlledButton3.setIcon(new ImageIcon("images/www-icon.GIF"));
    		this.frame.applicationManager.addApplication(
		   "WebFrame",       "application.webbrowser.WebFrame","browser ");
    	}
    	{
			webleapButton.setText(this.frame.getLclTxt("WebLEAPFrame"));
			webleapButton.setActionCommand("WebLEAPFrame");
//			controlledButton9.setIcon(new ImageIcon("images/webleap-icon.GIF"));
    		this.frame.applicationManager.addApplication(
		   "WebLEAPFrame",   "application.webleap.WebLEAPFrame","webleap ");
    	}
    	{
			avframeButton.setText(this.frame.getLclTxt("AVFrame"));
			avframeButton.setActionCommand("AVFrame");
//			controlledButton12.setIcon(new ImageIcon("images/av-icon.GIF"));
    		this.frame.applicationManager.addApplication(
		   "AVFrame",      "application.av.AVFrame","avframe ");
    	}
    	{
			fileTransferButton.setText(this.frame.getLclTxt("FileTransferFrame"));
			fileTransferButton.setActionCommand("FileTransferFrame");
//			controlledButton13.setIcon(new ImageIcon("images/filetransfer-icon.GIF"));
			this.frame.applicationManager.addApplication(
				  "FileTransferFrame", "application.fileTransfer.FileTransferFrame","transfer ");    
		}
    	{
			commandButton.setText(this.frame.getLclTxt("NativeCommandShell"));
			commandButton.setActionCommand("NativeCommandShell");
//			controlledButton13.setIcon(new ImageIcon("images/filetransfer-icon.GIF"));
			this.frame.applicationManager.addApplication(
				  "NativeCommandShell", "application.nativecommand.NativeCommandShell","command ");    
		}
    	{
			controlledButton6.setText(this.frame.getLclTxt("*RemoteNodeController"));
			controlledButton6.setActionCommand("*RemoteNodeController");
//			controlledButton6.setIcon(new ImageIcon("images/remote-control.GIF"));
		this.frame.applicationManager.addApplication(
		   "RemoteNodeController", "application.remotenodecontroller.RemoteNodeController","RemoteNodeController ");
    	}
    	{
			controlledButton5.setText("*NetworkTesterMasterFrame");
			controlledButton5.setActionCommand("NetworkTesterMasterFrame");
		this.frame.applicationManager.addApplication(
		   "NetworkTesterMasterFrame", "application.networktester.NetworkTesterMasterFrame","networktestermaster ");
    	}
    	{
			controlledButton4.setText("*NetworkTesterWorkerFrame");
			controlledButton4.setActionCommand("NetworkTesterWorkerFrame");
		this.frame.applicationManager.addApplication(
		   "NetworkTesterWorkerFrame", "application.networktester.NetworkTesterWorkerFrame","networktesterworker ");
    	}
    	{
			controlledButton8.setText("*RcxControlMasterFrame");
			controlledButton8.setActionCommand("RcxControlMasterFrame");
        this.frame.applicationManager.addApplication(
           "RcxControlMasterFrame", "application.rcxcontroller.RcxControlMasterFrame","rcxcontrolmaster ");
    	}
    	{
			controlledButton7.setText("*RcxControlWorkerFrame");
			controlledButton7.setActionCommand("RcxControlWorkerFrame");
        this.frame.applicationManager.addApplication(
           "RcxControlWorkerFrame", "application.rcxcontroller.RcxControlWorkerFrame","rcxcontrolworker ");
    	}
    	{
			controlledButton15.setText("*FT-Controller-(worker)");
			controlledButton15.setActionCommand("*FT-Controller-(worker)");
	    this.frame.applicationManager.addApplication(
	              "FT-Controller-(worker)", "application.ftcontroller.FtControlWorkerFrame","ftcontrolworker ");
    	}
    	{
			controlledButton16.setText("*FT-Controller-(master)");
			controlledButton16.setActionCommand("*FT-Controller-(master)");
	    this.frame.applicationManager.addApplication(
	              "FT-Controller-(master)", "application.ftcontroller.FtControlMasterFrame","ftcontrolmaster ");
    	}
    }

    public void setState(int i)
    {
            ControlledButton b=(ControlledButton)(this.buttons.elementAt(i));
    	    String application=b.getText();
    	    frame.applicationButton.setText(application);
    	    frame.applicationButton.setIcon(b.getIcon());
    	    String actCommand=b.getActionCommand();
    	    frame.applicationButton.setActionCommand(actCommand);
    	    this.currentState=i;
    }

    public int getState()
    {
        return this.currentState;
    }

    public int currentState;

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledButton
        // to do: code goes here
        if(frame==null) return true;
        return this.frame.isControlledByLocalUser();
    }

    public void sendEvent(String x)
    {
        frame.sendThisEvent("apf."+x);
    }

    public CommunicationNode frame;

    public void setFrame(CommunicationNode f)
    {
        frame=f;
    }

    public Vector buttons;

    public void clickButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        b.click();
        this.mouseClickedAtButton(i);
    }

    public void focusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            ControlledButton button=(ControlledButton)(buttons.elementAt(i));
//            button.controlledButton_mouseEntered(null);
            button.focus();
    }

    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        if(i==this.cancelButton.getID()) {
            this.hide();
            return;
        }
    	if(frame!=null){
//    	    String application=((ControlledButton)(buttons.elementAt(i))).getText();
//    	    frame.applicationButton.setText(application);
//            if(application.indexOf("*")==0)
//          frame.currentExample=(String)(programs.elementAt(i));
            this.setState(i);
        }
	    this.hide();
    }

    public void mouseEnteredAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        frame.sendThisEvent("apf.btn.enter("+i+")\n");
    }

    public void mouseExitedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        frame.sendThisEvent("apf.btn.exit("+i+")\n");
    }

    public void unfocusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//            button.controlledButton_mouseExited(null);
            button.unFocus();
    }

	public ApplicationsFrame(CommunicationNode cn)
	{
		this.setFrame(cn);
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		this.setSize(267, 461);
		setVisible(false);
		{
		cancelButton.setText(this.frame.getLclTxt("cancel"));
		cancelButton.setActionCommand("cancel");
//		cancelButton.setIcon(new ImageIcon("images/cancel.GIF"));
		getContentPane().add(cancelButton);
		cancelButton.setBackground(new java.awt.Color(204,204,204));
		cancelButton.setForeground(java.awt.Color.black);
		cancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
		cancelButton.setBounds(14, 392, 231, 21);
		}
		{
		JLabel1.setText(this.frame.getLclTxt("Applications"));
		getContentPane().add(JLabel1);
		JLabel1.setBounds(14, 7, 217, 21);
		}
		{
//		controlledButton1.setText("DrawFrame");
//		controlledButton1.setActionCommand("DrawFrame");
		getContentPane().add(drawButton);
		drawButton.setBackground(new java.awt.Color(204,204,204));
		drawButton.setForeground(java.awt.Color.black);
		drawButton.setFont(new Font("Dialog", Font.BOLD, 12));
		drawButton.setBounds(14, 56, 231, 21);
		}
		{
		getContentPane().add(basicButton);
		basicButton.setBackground(new java.awt.Color(204,204,204));
		basicButton.setForeground(java.awt.Color.black);
		basicButton.setFont(new Font("Dialog", Font.BOLD, 12));
		basicButton.setBounds(14, 119, 231, 21);
		}
		{
		getContentPane().add(webButton);
		webButton.setBackground(new java.awt.Color(204,204,204));
		webButton.setForeground(java.awt.Color.black);
		webButton.setFont(new Font("Dialog", Font.BOLD, 12));
		webButton.setBounds(14, 161, 231, 21);
		}
		{
		getContentPane().add(controlledButton4);
		controlledButton4.setBackground(new java.awt.Color(204,204,204));
		controlledButton4.setForeground(java.awt.Color.black);
		controlledButton4.setFont(new Font("Dialog", Font.BOLD, 12));
		controlledButton4.setBounds(14, 245, 231, 21);
		}
		{
		getContentPane().add(controlledButton5);
		controlledButton5.setBackground(new java.awt.Color(204,204,204));
		controlledButton5.setForeground(java.awt.Color.black);
		controlledButton5.setFont(new Font("Dialog", Font.BOLD, 12));
		controlledButton5.setBounds(14, 266, 231, 21);
		}
		{
		getContentPane().add(controlledButton6);
		controlledButton6.setBackground(new java.awt.Color(204,204,204));
		controlledButton6.setForeground(java.awt.Color.black);
		controlledButton6.setFont(new Font("Dialog", Font.BOLD, 12));
		controlledButton6.setBounds(14, 224, 231, 21);
		}
		{
		getContentPane().add(controlledButton7);
		controlledButton7.setBackground(new java.awt.Color(204,204,204));
		controlledButton7.setForeground(java.awt.Color.black);
		controlledButton7.setFont(new Font("Dialog", Font.BOLD, 12));
		controlledButton7.setBounds(14, 287, 231, 21);
		}
		{
		getContentPane().add(controlledButton8);
		controlledButton8.setBackground(new java.awt.Color(204,204,204));
		controlledButton8.setForeground(java.awt.Color.black);
		controlledButton8.setFont(new Font("Dialog", Font.BOLD, 12));
		controlledButton8.setBounds(14, 308, 231, 21);
		}
		{
		getContentPane().add(webleapButton);
		webleapButton.setBackground(new java.awt.Color(204,204,204));
		webleapButton.setForeground(java.awt.Color.black);
		webleapButton.setFont(new Font("Dialog", Font.BOLD, 12));
		webleapButton.setBounds(14, 182, 231, 21);
		}
		{
		getContentPane().add(teditButton);
		teditButton.setBackground(new java.awt.Color(204,204,204));
		teditButton.setForeground(java.awt.Color.black);
		teditButton.setFont(new Font("Dialog", Font.BOLD, 12));
		teditButton.setBounds(14, 77, 231, 21);
		}
{
			fileTransferButton = new ControlledButton();
			this.getContentPane().add(fileTransferButton);
			fileTransferButton.setBounds(14, 203, 231, 21);
		}
		{
			penButton = new ControlledButton();
			this.getContentPane().add(penButton);
			penButton.setBounds(14, 98, 231, 21);
		}
		{
			controlledButton15 = new ControlledButton();
			this.getContentPane().add(controlledButton15);
			controlledButton15.setBounds(14, 329, 231, 21);
		}
		{
			controlledButton16 = new ControlledButton();
			this.getContentPane().add(controlledButton16);
			controlledButton16.setBounds(14, 350, 231, 21);
		}
		{
			avframeButton = new ControlledButton();
			getContentPane().add(avframeButton);
			avframeButton.setBounds(14, 35, 231, 21);
		}
		{
			getContentPane().add(lispButton);
			lispButton.setBackground(new java.awt.Color(204,204,204));
			lispButton.setForeground(java.awt.Color.black);
			lispButton.setFont(new Font("Dialog", Font.BOLD, 12));
			lispButton.setBounds(14, 140, 231, 21);
		}
		{
			commandButton = new ControlledButton();
			getContentPane().add(commandButton);
			commandButton.setText("NativeCommandShell");
			commandButton.setBounds(14, 371, 231, 21);
		}
	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		//}}
		
		buttons=new Vector();
		buttons.addElement(this.drawButton);  
		buttons.addElement(this.basicButton);  
		buttons.addElement(this.webButton);     
		buttons.addElement(this.controlledButton4);     
		buttons.addElement(this.controlledButton5);  
		buttons.addElement(this.controlledButton6);     
		buttons.addElement(this.controlledButton7);     
		buttons.addElement(this.controlledButton8);
		buttons.addElement(this.webleapButton);
		buttons.addElement(this.cancelButton);
		buttons.addElement(this.teditButton);
		buttons.addElement(this.avframeButton);
		buttons.addElement(this.fileTransferButton);
		buttons.addElement(this.penButton);
		buttons.addElement(this.controlledButton15);
		buttons.addElement(this.controlledButton16);
		buttons.addElement(this.lispButton);
		buttons.addElement(this.commandButton);
		
		int numberOfButtons=buttons.size();
		for(int i=0;i<numberOfButtons;i++){
		    ControlledButton b=(ControlledButton)(buttons.elementAt(i));
		    b.setFrame(this);
		    b.setID(i);
    		b.addActionListener(lSymAction);
		}
				
		this.setTitle("Applications");
		this.setApplicationPath();
	}

    public void setIcons(String iconPlace){
		try{
//			ImageIcon ic=new ImageIcon("images/rotate-icon.GIF");
//			cancelButton.setIcon(new ImageIcon("images/cancel.GIF"));
			ImageIcon ic=new ImageIcon(new URL(iconPlace+"cancel.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			cancelButton.setIcon(ic);
			else
				cancelButton.setText("exit");
//			controlledButton14.setIcon(new ImageIcon("images/pen-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"pen-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			penButton.setIcon(ic);
			else
		    penButton.setText("pen");
//			   controlledButton1.setIcon(new ImageIcon("images/draw-icon.GIF")); 
			ic=new ImageIcon(new URL(iconPlace+"draw-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			drawButton.setIcon(ic);
			else
		    drawButton.setText("draw");
//			controlledButton2.setIcon(new ImageIcon("images/basic-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"basic-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			basicButton.setIcon(ic);
			else
		    basicButton.setText("basic");		
			ic=new ImageIcon(new URL(iconPlace+"lisp-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			lispButton.setIcon(ic);
			else
		    lispButton.setText("lisp");		//			controlledButton11.setIcon(new ImageIcon("images/editIcon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"editIcon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			teditButton.setIcon(ic);
			else
		    teditButton.setText("editor");
//			controlledButton3.setIcon(new ImageIcon("images/www-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"www-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			webButton.setIcon(ic);
			else
		    webButton.setText("www");
//			controlledButton9.setIcon(new ImageIcon("images/webleap-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"webleap-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			webleapButton.setIcon(ic);
			else
		    webleapButton.setText("webleap");
//			controlledButton12.setIcon(new ImageIcon("images/av-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"av-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			avframeButton.setIcon(ic);
			else
		    avframeButton.setText("av");
//			controlledButton13.setIcon(new ImageIcon("images/filetransfer-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"filetransfer-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			fileTransferButton.setIcon(ic);
			else
		    fileTransferButton.setText("file trans");
//			controlledButton6.setIcon(new ImageIcon("images/remote-control.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"remote-control.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			controlledButton6.setIcon(ic);
			else
		    controlledButton6.setText("file trans");			
		}
		catch(Exception e){
		    cancelButton.setText("cancel");

		}
    }
    
    public void setVisible(boolean b)
	{
		if (b)
			setLocation(50, 50);
		super.setVisible(b);
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

	// Used by addNotify
	boolean frameSizeAdjusted = false;

	//{{DECLARE_CONTROLS
	public controlledparts.ControlledButton drawButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton basicButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton webButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton controlledButton4 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton controlledButton5 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton controlledButton6 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton controlledButton7 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton controlledButton8 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton webleapButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton cancelButton = new controlledparts.ControlledButton();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	private ControlledButton controlledButton15;
	private ControlledButton controlledButton16;
	private ControlledButton penButton;
	private ControlledButton fileTransferButton;
	private ControlledButton lispButton = new controlledparts.ControlledButton();;
	ControlledButton avframeButton;
	private ControlledButton commandButton;
	controlledparts.ControlledButton teditButton = new controlledparts.ControlledButton();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
            int buttonNumber=buttons.size();

            for(int i=0;i<buttonNumber;i++){
                ControlledButton b=(ControlledButton)(buttons.elementAt(i));
                if(object == b){ 
                    mouseClickedAtButton(i);
                }
            }
	    }
	}
}