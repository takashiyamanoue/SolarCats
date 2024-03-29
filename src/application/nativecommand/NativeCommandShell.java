/*
		A basic implementation of the JFrame class.
*/
package application.nativecommand;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import language.CQueue;

import nodesystem.CommunicationNode;

import application.basic.BasicFrame;
import application.basic.ParseBasicEvent;
import application.draw.DrawFrame;

import controlledparts.ControlledButton;
import controlledparts.ControlledFrame;
import controlledparts.ControlledScrollPane;
import controlledparts.ControlledTextArea;
import controlledparts.FrameWithControlledButton;
import controlledparts.FrameWithControlledPane;
import controlledparts.FrameWithControlledTextAreas;
import controlledparts.InputQueue;
import controlledparts.ParseEvent;
import controlledparts.SelectedButton;

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
public class NativeCommandShell extends ControlledFrame
implements FrameWithControlledButton, FrameWithControlledTextAreas, FrameWithControlledPane
{
	Vector buttons;
	Vector textAreas;
	Vector panes;
	
	public NativeCommandShell()
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		this.setEnabled(true);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		this.setSize(498, 433);
		setVisible(false);
		enterButton.setText("Enter");
		enterButton.setActionCommand("Enter");
		getContentPane().add(enterButton);
		enterButton.setBackground(new java.awt.Color(204,204,204));
		enterButton.setForeground(java.awt.Color.black);
		enterButton.setFont(new Font("Dialog", Font.BOLD, 12));
		enterButton.setBounds(399, 259, 77, 42);
//		enterButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent evt) {
//				enterButtonActionPerformed(evt);
//			}
//		});
		outputPane.setOpaque(true);
		getContentPane().add(outputPane);
		outputPane.setBounds(70, 63, 399, 196);
		outputPane.getViewport().add(outputArea);
		outputArea.setBackground(java.awt.Color.white);
		outputArea.setForeground(java.awt.Color.black);
		outputArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		outputArea.setBounds(0,0,392,152);
		JLabel1.setText("Distributed System Recorder/ Native Command Shell");
		getContentPane().add(JLabel1);
		JLabel1.setForeground(new java.awt.Color(102,102,153));
		JLabel1.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel1.setBounds(24,12,324,36);
		JLabel2.setText("input");
		getContentPane().add(JLabel2);
		JLabel2.setForeground(new java.awt.Color(102,102,153));
		JLabel2.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel2.setBounds(7, 259, 63, 28);
		JLabel3.setText("output");
		getContentPane().add(JLabel3);
		JLabel3.setForeground(new java.awt.Color(102,102,153));
		JLabel3.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel3.setBounds(7, 84, 63, 35);
		JLabel4.setText("\"#\" is my  number.");
		getContentPane().add(JLabel4);
		JLabel4.setForeground(new java.awt.Color(102,102,153));
		JLabel4.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel4.setBounds(70, 371, 273, 21);
		{
			exitButton = new ControlledButton();
			getContentPane().add(exitButton);
			exitButton.setText("exit");
			exitButton.setBounds(413, 21, 63, 28);
//			exitButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent evt) {
//					exitButtonActionPerformed(evt);
//				}
//			});
		}
		{
			clearButton = new ControlledButton();
			getContentPane().add(clearButton);
			clearButton.setText("clear");
			clearButton.setBounds(350, 21, 63, 28);
//			clearButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent evt) {
//					clearButtonActionPerformed(evt);
//				}
//			});
		}
		{
			inputPane = new ControlledScrollPane();
			getContentPane().add(inputPane);
			inputPane.setBounds(70, 259, 329, 42);
			{
				inputArea = new ControlledTextArea();
				inputPane.setViewportView(inputArea);
			}
		}
		{
			helpLabel1 = new JLabel();
			getContentPane().add(helpLabel1);
			helpLabel1.setText("Windows... cmd /c <command>   <command>=dir|cd| ...");
			helpLabel1.setBounds(70, 343, 371, 28);
		}
		{
			currentDirLabel = new JLabel();
			getContentPane().add(currentDirLabel);
			currentDirLabel.setText("currentdir :");
			currentDirLabel.setBounds(70, 322, 63, 28);
		}
		{
			currentDirXlabel = new JLabel();
			getContentPane().add(currentDirXlabel);
			currentDirXlabel.setBounds(133, 322, 343, 21);
		}
		//}}
	    super.registerListeners();
	    this.setEnabled(true);
		panes=new Vector();
		panes.addElement(inputPane);
		panes.addElement(outputPane);
		for(int i=0;i<panes.size();i++){
			ControlledScrollPane t=(ControlledScrollPane)(panes.elementAt(i));
			t.setID(i);
			t.setFrame(this);
		}

		//{{INIT_MENUS
		//}}
		buttons=new Vector();
		buttons.addElement(clearButton);
		buttons.addElement(exitButton);
		buttons.addElement(enterButton);
		for(int i=0;i<buttons.size();i++){
			ControlledButton b=(ControlledButton)(buttons.elementAt(i));
			b.setID(i);
			b.setFrame(this);
		}
		
		textAreas=new Vector();
		textAreas.addElement(inputArea);
		textAreas.addElement(outputArea);
		for(int i=0;i<textAreas.size();i++){
			ControlledTextArea t=(ControlledTextArea)(textAreas.elementAt(i));
			t.setID(i);
			t.setFrame(this);
		}

	}
	
	public void clearAll(){
		this.currentDir=this.communicationNode.commonDataDir;
	}
	
	public void exitThis()
	{
		// This method is derived from interface Spawnable
		// to do: code goes here
//		  System.out.println("basicframe exithis");
//		this.endMonitoring();
		this.setVisible(false);
//		   hide();         // Frame を非表示にする.
	}
	public NativeCommandShell(String sTitle)
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
		(new NativeCommandShell()).setVisible(true);
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
	/*
	private void enterButtonActionPerformed(ActionEvent evt) {
		System.out.println("enterButton.actionPerformed, event=" + evt);
		//TODO add your code for enterButton.actionPerformed
	}
	
	private void clearButtonActionPerformed(ActionEvent evt) {
		System.out.println("clearButton.actionPerformed, event=" + evt);
		//TODO add your code for clearButton.actionPerformed
	}
	
	private void exitButtonActionPerformed(ActionEvent evt) {
		System.out.println("exitButton.actionPerformed, event=" + evt);
		//TODO add your code for exitButton.actionPerformed
	}
*/
	// Used by addNotify
	boolean frameSizeAdjusted = false;

	//{{DECLARE_CONTROLS
	controlledparts.ControlledButton enterButton = new controlledparts.ControlledButton();
	controlledparts.ControlledScrollPane outputPane = new controlledparts.ControlledScrollPane();
	controlledparts.ControlledTextArea outputArea = new controlledparts.ControlledTextArea();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	private controlledparts.ControlledTextArea inputArea;
	private controlledparts.ControlledScrollPane inputPane;
	private controlledparts.ControlledButton clearButton;
	private controlledparts.ControlledButton exitButton;
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel4 = new javax.swing.JLabel();
	//}}
/*
	public boolean isDirectOperation() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
*/
	public boolean isControlledByLocalUser() {
		// TODO 自動生成されたメソッド・スタブ
        return super.isControlledByLocalUser();
	}
    String commandName="command";
    public String getCommandName(){
    	return commandName;
    }

	public void sendEvent(String x) {
		// TODO 自動生成されたメソッド・スタブ
        if(this.communicationNode==null) return;
        this.communicationNode.sendEvent(commandName,x);		
	}

	public void clickButton(int i) {
		// TODO 自動生成されたメソッド・スタブ
        ControlledButton t=(ControlledButton)(buttons.elementAt(i));
        t.click();
        this.mouseClickedAtButton(i);
		
	}

	public void unfocusButton(int i) {
		// TODO 自動生成されたメソッド・スタブ
        SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//      button.controlledButton_mouseExited(null);
      button.unFocus();
		
	}

	public void focusButton(int i) {
		// TODO 自動生成されたメソッド・スタブ
        SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//      button.controlledButton_mouseEntered(null);
      button.focus();
		
	}

	public void mouseClickedAtButton(int i) {
		// TODO 自動生成されたメソッド・スタブ
		if(i==this.exitButton.getID()){
			this.exitThis();
			return;
		}
		if(i==this.clearButton.getID()){
			this.outputArea.setText("");
			return;
		}
		if(i==this.enterButton.getID()){
			this.execCommand(this.inputArea.getText());
			return;
		}
	}
	
	private Vector StreamToVector(InputStream is) throws Exception {
		  Vector tb = new Vector();
		  BufferedReader br = new BufferedReader(new InputStreamReader(is));
		  String s;
          while ((s = br.readLine()) != null) {
		         tb.add(s);
		  }
		  return tb;
	}
	
	File currentDir;

	public void execCommand(String x){
		this.currentDirXlabel.setText(this.currentDir.getName());
	    this.outputArea.append(x+"\n");
	    this.inputArea.setText("");
	    try{
	           Runtime	rt = Runtime.getRuntime();
	           /*
	           String env[]={"",""};
	           env[0]="Path="+System.getenv("path");
	           env[1]="PATHEXT="+System.getenv("PATHEXT");
	           */
//	           Process	pr = rt.exec(x,env,this.communicationNode.commonDataDir);
//           Process pr=rt.exec(x,null,new File(this.currentDir));
	           Process pr=rt.exec(x,null,currentDir);
	           InputStream is = pr.getInputStream();
	           Vector vecOsOutput = StreamToVector(is);
	           pr.waitFor();
	           for(int i = 0; i < vecOsOutput.size(); i++ ) {
	              String s = (String)vecOsOutput.elementAt(i);
	              this.outputArea.append(s+"\n");
               }
	           /*
	           OutputStream os=pr.getOutputStream();
	           BufferedOutputStream obs=new BufferedOutputStream(os);
	           obs.write("cmd /c cd\n".getBytes());
	           */
	           pr=rt.exec("cmd /c cd",null,null);
	           pr.waitFor();
	           for(int i = 0; i < vecOsOutput.size(); i++ ) {
	              String s = (String)vecOsOutput.elementAt(i);
	              this.outputArea.append(s+"\n");
               }
	           	           
	           String cdname=System.getProperty("CWD"); // windows
	           this.currentDir=new File(cdname);
    	   		this.currentDirXlabel.setText(cdname);
		 }
		 catch(Exception e){
			   System.out.println(e);
		 }
	}

	public void mouseExitedAtButton(int i) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void mouseEnteredAtButton(int i) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void setTextOnTheText(int i, int pos, String s) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public boolean isShowingRmouse() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	public void exitMouseOnTheText(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void enterMouseOnTheText(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void mouseExitAtTheText(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void mouseEnteredAtTheText(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void moveMouseOnTheText(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void mouseMoveAtTextArea(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void pressMouseOnTheText(int i, int p, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void mousePressedAtTextArea(int i, int p, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void releaseMouseOnTheText(int id, int position, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void dragMouseOnTheText(int id, int position, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void mouseReleasedAtTextArea(int id, int position, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void mouseDraggedAtTextArea(int id, int position, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	int lastManipulatedTextArea;
	private JLabel currentDirLabel;
	private JLabel currentDirXlabel;
	private JLabel helpLabel1;
	public void clickMouseOnTheText(int i, int p, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
        this.lastManipulatedTextArea=i;
        ControlledTextArea t=(ControlledTextArea)(textAreas.elementAt(i));
        t.clickMouse(p,x,y);
		
	}

	public void typeKey(int i, int p, int key) {
		// TODO 自動生成されたメソッド・スタブ
        ControlledTextArea t=(ControlledTextArea)(textAreas.elementAt(i));
        t.typeKey(p,key);
		
	}

	public void pressKey(int i, int p, int code) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void releaseKey(int i, int p, int code) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void mouseClickedAtTextArea(int i, int p, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void keyIsTypedAtATextArea(int i, int p, int key) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void keyIsPressedAtATextArea(int i, int p, int key) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void keyIsReleasedAtTextArea(int id, int p, int key) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void changeScrollbarValue(int paneID, int barID, int value) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void showScrollBar(int paneID, int barID) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void hideScrollBar(int paneID, int barID) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void scrollBarHidden(int paneID, int barId) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void scrollBarShown(int paneID, int barID) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void scrollBarValueChanged(int paneID, int barID, int v) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
    public void receiveEvent(String s)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
//        if(!this.communicationNode.isReceivingEvents) return;
    	if(!this.isReceiving()) return;
        InputQueue iq=null;
        try{
           BufferedReader dinstream=new BufferedReader(
//              new InputStreamReader(
//              new StringBufferInputStream(s),encodingCode)
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
            ParseCommandEvent evParser=new ParseCommandEvent(this,iq);
            evParser.parsingString=s;
            evParser.run();
    }

    public ControlledFrame spawnMain(CommunicationNode cn,String args,int pID, String controlMode)
    {
           NativeCommandShell bf=this;
           bf.communicationNode=cn;
           bf.pID=pID;
//           bf.encodingCode=code;
           bf.setTitle("NativeCommandShell");
		
		// spawn Basic Slaves at other nodes in this group.
           bf.ebuff=cn.commandTranceiver.ebuff;

//           bf.show();
           bf.setVisible(true);

           return bf;
    }
    //{{DECLARE_MENUS
	//}}

}