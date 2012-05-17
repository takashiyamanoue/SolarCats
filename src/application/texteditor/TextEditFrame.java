/*
		A basic implementation of the JFrame class.
*/
package application.texteditor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import nodesystem.*;
import controlledparts.*;

public class TextEditFrame extends ControlledFrame implements DialogListener, EditDialog, FrameWithControlledButton, FrameWithControlledPane, FrameWithControlledTextAreas    
{
	public void setEditable(boolean x){
		this.textArea.setEditable(x);
	}
	
    public void clearAll()
    {
        fileFrame=new FileFrame("Text File I/O");
    	this.fileFrame.setCommunicationNode(this.communicationNode);
    	this.fileFrame.setWords();
        this.textArea.setText("");
        if(this.communicationNode!=null)
           this.fileFrame.setSeparator(communicationNode.getSeparator());
        if(!this.isApplet())
           this.fileFrame.setFileChooser(new JFileChooser());
    }

    public void setUrlField(JLabel x)
    {
        this.urlField=x;
    }

    public JLabel getUrlField() 
    {
        return this.urlField;
    }

    public void setTextOnTheText(int id, int pos, String s)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
		t.setTextAt(pos,s);
    }

    public void sendAll()
    {
        this.textArea.ControlledTextArea_textPasted();
    }

    public void receiveEvent(String s)
    {
//        if(!this.communicationNode.isReceivingEvents) return;
    	if(!this.isReceiving()) return;
        InputQueue iq=null;
        try{
           BufferedReader dinstream=new BufferedReader(
                new StringReader(s)
           );
           iq=new InputQueue(dinstream);
        }
        catch(Exception e){
            System.out.println("exception:"+e);
            return;
        }
        if(iq==null) return;
            ParseTextEditEvent evParser=new ParseTextEditEvent(this,iq);
            evParser.parsingString=s;
            evParser.run();
    }

    public ControlledFrame spawnMain(CommunicationNode cn, String args, int pID, String controlMode)
    {
           TextEditFrame f=this;
           f.setTitle("DSR/Text Editor");
           f.setCommunicationNode(cn);
           f.pID=pID;
           f.setIcons(cn.getIconPlace());
		
           f.ebuff=cn.commandTranceiver.ebuff;
//           wf.show();
           f.setControlledFrame(null);
           f.textArea.setText("");
           f.setListener(cn);
           f.setVisible(true);
           return f;

    }

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
        if(this.dialogListener!=null) {
            return this.dialogListener.isControlledByLocalUser();
        }
        if(this.communicationNode!=null){
            return this.communicationNode.isControlledByLocalUser();
        }
        else return true;
    }

    String commandName="tedit";
    public String getCommandName(){
    	return commandName;
    }

    public void sendEvent(String x)
    {
        if(frame==null) {
            if(this.communicationNode==null) return;
            this.communicationNode.sendEvent(commandName,"txtedit."+x);
            return;
        }
        frame.sendEvent("txtedit."+x);
    }
/*
    public boolean isShowingRmouse()
    {
        if(frame!=null){
            if(frame.communicationNode!=null)
               return this.frame.communicationNode.rmouseIsActivated;
        }
        if(this.communicationNode!=null)
          return communicationNode.rmouseIsActivated;
        return false;
    }
*/
    public void moveMouseOnTheText(int id, int x, int y)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
//        t.rmouse.move(x,y);
        t.moveMouse(x,y);
    }

    public void mouseMoveAtTextArea(int id, int x, int y)
    {
    }

    public void exitMouseOnTheText(int id, int x, int y)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
//        t.rmouse.setVisible(false);
        t.exitMouse();
    }
    public void mouseExitAtTheText(int id, int x, int y)
    {
    }
    public void enterMouseOnTheText(int id, int x, int y)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
//        t.rmouseIsShown=true;
//        t.rmouse.setVisible(true);
//        t.moveMouse(x,y);
        t.enterMouse(x,y);      
    }
    
    public void mouseEnteredAtTheText(int id, int x, int y)
    {
    }

    public void gainFocus()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
        this.requestFocus();
    }

    public void loseFocus()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
    }

    public void pressMouseOnTheText(int id, int p, int x, int y)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
        t.pressMouse(p,x,y);
    }

    public void mousePressedAtTextArea(int id, int p, int x, int y)
    {
        if(frame==null) return;
    }
    public void showScrollBar(int paneID, int barID)
    {
         ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(paneID));
         p.showScrollBar(barID);
   }

    public void hideScrollBar(int paneID, int barID)
    {
        ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(paneID));
        p.hideScrollBar(barID);
    }

    public void changeScrollbarValue(int paneID, int barID, int value)
    {
        ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(paneID));
        p.setScrollBarValue(barID,value);
     }

    Vector panes;

    public void scrollBarHidden(int paneID, int barID)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
    }

    public void scrollBarShown(int paneID, int barID)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
        /*
        if(frame==null) return;
        if(frame.communicationNode.sendEventFlag)
           frame.sendEvent("txtedit.sb.shown("+paneID+","+barID+")\n");
           */
    }

    public void scrollBarValueChanged(int paneID, int barID, int v)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
        /*
        if(frame==null) return;
        if(frame.communicationNode.sendEventFlag)
           frame.sendEvent("txtedit.sb.value("+paneID+","+barID+","+v+")\n");
           */
    }

    String tempText;

    public void saveText(String urlname)
    {
//        JTextArea mes=frame.messages;
        if(urlname==null) return;
//        int startTime=frame.getTimeNow();
        int startTime=this.getTimeNow();
        this.messageLabel.setText("saving "+urlname+"...");
//        mes.append("saving "+urlname+"\n");

        FileOutputStream fouts=null;

        try{ fouts= new FileOutputStream(new File(urlname));}
        catch(FileNotFoundException e){
            messageLabel.setText("wrong directory:"+urlname+" ?");
        }
        catch(IOException e){ 
            messageLabel.setText("cannot access"+urlname+".");
        }

        String outx=this.textArea.getText();
//        byte[] buff=new byte[outx.length()];
        byte[] buff=null;
        try{
//          buff=outx.getBytes(frame.communicationNode.encodingCode);
            buff=outx.getBytes(communicationNode.getEncodingCode());
        }
        catch(java.io.UnsupportedEncodingException e){
            messageLabel.setText("exception:"+e);
        }
//        outx.getBytes(0,outx.length(),buff,0);

        try{
            fouts.write(buff);
            fouts.flush();
        }
        catch(IOException e)
           { messageLabel.setText("save:IOExceptin while flushing.");}
           
        try{ fouts.close();  }
        catch(IOException e)
           { messageLabel.setText("save:IOException while closing.");}

        messageLabel.setText("Saving done.");
        int endTime=// frame.getTimeNow();
                    this.getTimeNow();
        int savingtime=endTime-startTime;
        double length=(double)(outx.length());
        String savingSpeed="";
        if(savingtime>0){
            savingSpeed=""+10.0*length/savingtime;
        }
        else savingSpeed="(na)";
        this.recordMessage(""+startTime+
        ",\"savefig("+urlname+") \","+savingtime*0.1+
        ","+length+","+savingSpeed);
    }

    public void loadText(String urlname)
    {
        if(!(this.isControlledByLocalUser()||this.communicationNode.isReadableFromEachFile())) return;
        BufferedReader inputStream=null;
        URL  url;
        
//        JTextArea mes=draw.messages;

        int startTime=this.getTimeNow();
        if(urlname==null){
            messageLabel.setText("URL Name is expected.");
            return;
        }
        messageLabel.setText("reading "+urlname+"...");
        try{ url=new URL(urlname); }
        catch(MalformedURLException e)
        {  messageLabel.setText("MalformedURLException.");
           return;
        }

        try{
            InputStream is=url.openStream();
            InputStreamReader isr=new InputStreamReader(is,
                      this.communicationNode.getEncodingCode());
            inputStream = new BufferedReader( isr);
        }
        catch(UnsupportedEncodingException e){
             messageLabel.setText("encoding unsupported.");
            return;
       }
        catch(IOException e){
            messageLabel.setText("url open error.");
            return;
        }

       this.textArea.setText("");
       int tlength=0;
       String x=null;
       do{
          x=null;
          try{
            x=inputStream.readLine();
          }
          catch(java.io.IOException e){  break;}
          if(x!=null){
             tlength=tlength+x.length();
             this.textArea.append(x+"\n");
          }
       } while(x!=null);
  
        try{ inputStream.close();  }
        catch(IOException e)
           { messageLabel.setText("read:IOException while closing.");}
        messageLabel.setText("reading done.\n");
        int endTime=this.getTimeNow();
        int readingtime=endTime-startTime;
        String readingSpeed="";
        if(readingtime>0){
            readingSpeed=""+10.0*((double)(tlength))/readingtime;
        }
        else readingSpeed="(na)";
//        npMain.recordMessage("-"+startTime+":readfig("+urlname+") done.");
        this.recordMessage(""+startTime+
        ",\"readfig("+urlname+") \""+","+readingtime*0.1+
        ","+tlength+","+readingSpeed);
        
         if(this.communicationNode.isSending())
               this.sendAll();
        

   }
    public void setDialogName(String s)
    {
    }

    public void setSubName(String n)
    {
    }

    public String getSubName()
    {
        return null;
    }

    public Vector getDialogs()
    {
        return null;
    }

    public void sendFileDialogMessage(String s)
    {
        sendEvent("file."+s);
    }

    public void releaseMouseOnTheText(int id, int position, int x, int y)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
        t.releaseMouse(position,x,y);
    }

    public void dragMouseOnTheText(int id, int position, int x, int y)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
        t.dragMouse(position,x,y);
    }

    public void mouseReleasedAtTextArea(int id, int position, int x, int y)
    {
    }

    public void mouseDraggedAtTextArea(int id, int position, int x, int y)
    {
    }

    public void clickMouseOnTheText(int i, int p, int x, int y)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
        t.clickMouse(p,x,y);
    }

    public void mouseClickedAtTextArea(int i, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void typeKey(int i,int p, int key)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
        /*
        String s=t.getText();
        String pre="";
        String post="";
        if(p>0) pre=s.substring(0,p); else pre="";
        if(p<s.length()) post=s.substring(p,s.length());
        t.setText(pre+(char)key+post);
        t.setCaretPosition(p);
        t.repaint();
        */
        t.typeKey(p,key);
    }

    Vector texts;

    public void keyIsTypedAtATextArea(int i,int p,int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
//        System.out.println("mouse clicked at TextEdit frame");
        if(frame==null) return;
//        frame.sendEvent("txtedit.txt.kdn("+i+","+p+","+key+")\n");
    }
	public void keyIsPressedAtATextArea(int i,int p,int key)
	{
		// This method is derived from interface FrameWithControlledTextAreas
		// to do: code goes here
//		  System.out.println("mouse clicked at TextEdit frame");
		if(frame==null) return;
//		  frame.sendEvent("txtedit.txt.kdn("+i+","+p+","+key+")\n");
	}

    public void setControlledFrame(ControlledFrame d)
    {
        this.frame=d;
        if(frame==null) return;
        if(this.frame.communicationNode!=null)
        this.communicationNode=frame.communicationNode;
        

    }

    public ControlledFrame frame;

    public void unfocusButton(int i)
    {
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//            button.controlledButton_mouseExited(null);
            button.unFocus();
    }

    public void mouseExitedAtButton(int i)
    {
//       frame.sendEvent("txtedit.btn.exit("+i+")\n");
    }

    public void mouseEnteredAtButton(int i)
    {
//       frame.sendEvent("txtedit.btn.enter("+i+")\n");
    }

    public void mouseClickedAtButton(int i)
    {
        String theText="";
        String newText="";
        int startPosition=0;
        int endPosition=0;
       if(i==0){
//             loadButton_actionPerformed_Interaction1(null);       

//            fileFrame.setDialogName("file dialog:");
            fileFrame.setListener(this);
            fileFrame.setCommonPath(
                this.communicationNode.commonDataDir.toString());
            fileFrame.setUserPath(
                this.communicationNode.userDataDir.toString());
            fileFrame.show();
            return;

       }
       else if(i==1){
 //            okButton_actionPerformed_Interaction1(null);
    		if(this.dialogListener!=null) 
	        	this.dialogListener.whenActionButtonPressed(this);
		    this.hide();
		    return;
       }
       else if(i==2){
//             clearButton_actionPerformed_Interaction1(null);
			textArea.setText("");
			return;
       }
       else if(i==3){
 //            cancelButton_actionPerformed_Interaction(null);
            if(this.dialogListener!=null)
    	    	this.dialogListener.whenCancelButtonPressed(this);
		    this.hide();
		    return;
       }
       else if(i==4){
//             copyButton_actionPerformed_Interaction1(null);
//			textArea.setText("");
            theText=textArea.getSelectedText();
            this.communicationNode.tempText=theText;
            return;
       }
       else if(i==5){
 //            cutButton_actionPerformed_Interaction(null);
            startPosition=this.textArea.getSelectionStart();
            theText=this.textArea.getText();
            if(startPosition<0) startPosition=0;
            endPosition=this.textArea.getSelectionEnd();
            if(endPosition<startPosition){
                int w=startPosition; startPosition=endPosition;
                endPosition=w;
            }
            if(endPosition>theText.length())
               endPosition=theText.length();
            this.communicationNode.tempText=theText.substring(startPosition,endPosition);
            newText=theText.substring(0,startPosition);
            newText=newText+ theText.substring(endPosition);
            this.textArea.setText(newText);
            return;
       }
       else if(i==6){
//             pasteButton_actionPerformed_Interaction1(null);
            theText=this.textArea.getText();
            startPosition=this.textArea.getSelectionStart();
            if(startPosition<0) startPosition=0;
            endPosition=this.textArea.getSelectionEnd();
            if(endPosition<startPosition){
                int w=startPosition; startPosition=endPosition;
                endPosition=w;
            }
            if(endPosition>theText.length())
               endPosition=theText.length();
            newText=theText.substring(0,startPosition);
            newText=newText+ this.communicationNode.tempText+
                     (this.textArea.getText()).substring(endPosition);
            this.textArea.setText(newText);
            return;
       }
       else if(i==7){
 //            searchButton_actionPerformed_Interaction(null);
//	    	this.dialogListener.whenCancelButtonPressed(this);
//		    this.hide();
       }
        else if(i==8){
 //            replaceButton_actionPerformed_Interaction(null);
//	    	this.dialogListener.whenCancelButtonPressed(this);
//		    this.hide();
       }
       else if(i==this.paste2Button.getID()){
         if(this.isControlledByLocalUser()){
            this.textArea.paste();
//    	    this.sendEvent("txt.set(0,"+this.textArea.getStrConst());
            this.textArea.ControlledTextArea_textPasted();
         }
       }
    }

    public void focusButton(int i)
    {
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//            button.controlledButton_mouseEntered(null);
            button.focus();
   }

    public void clickButton(int i)
    {
        SelectedButton b=(SelectedButton)(buttons.elementAt(i));
        b.click();
        this.mouseClickedAtButton(i);
   }

    JFileDialog fileNameDialog;
    Vector buttons;

    public void setListener(DialogListener l)
    {
        dialogListener=l;
    }

    DialogListener dialogListener;

    public void whenCancelButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

    public void whenActionButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        String dname=d.getDialogName();
        if(dname.equals("input common file name:")){
            String url="";
    		String fileName=d.getText();
//    		File commonDataPath=communicationNode.commonDataDir;
//	    	File thePath=new File(commonDataPath.getPath(),fileName);
            String separator=""+System.getProperty("file.separator");
            if(separator.equals("\\"))
//            	url="file:///"+thePath.getPath();
                url="file:///"+fileName;
            else
//                url="file://"+thePath.getPath();
                url="file://"+fileName;
        	urlField.setText(fileName);
            this.loadText(url);
            return;
        }
        if(dname.equals("input user file name:")){
            String url="";
    		String fileName=d.getText();
    		File userDataPath=communicationNode.userDataDir;
	    	if(!userDataPath.exists()) 
	    	         userDataPath.mkdir();
//            File thePath=new File(userDataPath.getPath(),fileName);
            String separator=""+System.getProperty("file.separator");
            if(separator.equals("\\"))
//            	url="file:///"+thePath.getPath();
                url="file:///"+fileName;
            else
//                url="file://"+thePath.getPath();
                url="file://"+fileName;
        	urlField.setText(fileName);
        	this.loadText(url);
            return;
        }
        if(dname.equals("output user file name:")){
            String url="";
    		String fileName=d.getText();
     		File userDataPath=communicationNode.userDataDir;   		
	    	if(!userDataPath.exists()) 
	    	         userDataPath.mkdir();
//	    	File thePath=new File(userDataPath.getPath(),fileName);
        	urlField.setText(fileName);
//        	this.saveText(thePath.getPath());
            this.saveText(fileName);
            return;
        }
        if(dname.equals("output common file name:")){
    		String fileName=d.getText();
            if(!this.communicationNode.getControlMode().equals("teach")) return;
//            File commonDataPath=this.communicationNode.commonDataDir;
//	    	File thePath=new File(commonDataPath.getPath(),fileName);
//	    	System.out.println(thePath.getPath());
//        	this.saveText(thePath.getPath());
            this.saveText(fileName);
//        	editdispatch.save(thePath);
            return;
        }
        if(dname.equals("url:")){
            String url=d.getText();
        	urlField.setText(url);
        	this.loadText(url);
            return;
        }
    }

    public File getDefaultPath()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return dialogListener.getDefaultPath();
    }

    public String getText()
    {
        // This method is derived from interface EditDialog
        // to do: code goes here
        return this.textArea.getText();
    }

    public String getDialogName()
    {
        // This method is derived from interface EditDialog
        // to do: code goes here
        return "textEditor";
    }

    public FileFrame fileFrame;

    public ControlledTextArea getTextArea(){
        return this.textArea;
    }
	
    public void setIcons(String iconPlace){
		try{
		     this.copyButton.setIcon(new ImageIcon(new URL(iconPlace+"copy-icon.GIF")));
	    	 this.cutButton.setIcon(new ImageIcon(new URL(iconPlace+"cut-icon.GIF")));
			 this.pasteButton.setIcon(new ImageIcon(new URL(iconPlace+"paste-icon.GIF")));
			 this.fileButton.setIcon(new ImageIcon(new URL(iconPlace+"file-icon.GIF")));
			 this.cancelButton.setIcon(new ImageIcon(new URL(iconPlace+"exit-icon.GIF")));
			 this.clearButton.setIcon(new ImageIcon(new URL(iconPlace+"clear-icon.GIF")));
			 this.JLabel1.setIcon(new ImageIcon(new URL(iconPlace+"editIcon.GIF")));
			 this.paste2Button.setIcon(new ImageIcon(new URL(iconPlace+"paste-icon.GIF")));
		}
		catch(Exception e){
		     this.copyButton.setText("copy");
	    	 this.cutButton.setText("cut");
			 this.pasteButton.setText("paste");
			 this.fileButton.setText("file");
			 this.cancelButton.setText("cancel");
			 this.clearButton.setText("clear");
//			 this.JLabel1.setText("edit");
			 this.paste2Button.setText("copy-os");
			
		}
    }

    public TextEditFrame()
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		setResizable(false);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		setSize(572,478);
		setVisible(false);
		okButton.setText("OK");
		okButton.setActionCommand("OK");
		getContentPane().add(okButton);
		okButton.setBackground(new java.awt.Color(204,204,204));
		okButton.setForeground(java.awt.Color.black);
		okButton.setFont(new Font("Dialog", Font.BOLD, 12));
		okButton.setBounds(108,72,84,24);
		JLabel1.setText("Distributed System Recorder/ Draw - Text Editor");
		getContentPane().add(JLabel1);
		JLabel1.setBackground(new java.awt.Color(204,204,204));
		JLabel1.setForeground(new java.awt.Color(102,102,153));
		JLabel1.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel1.setBounds(24,12,408,36);
		fileButton.setActionCommand("load");
		getContentPane().add(fileButton);
		fileButton.setBackground(new java.awt.Color(204,204,204));
		fileButton.setForeground(java.awt.Color.black);
		fileButton.setFont(new Font("Dialog", Font.BOLD, 12));
		fileButton.setBounds(24,72,84,24);
		clearButton.setActionCommand("clear");
		clearButton.setToolTipText("clear");
		getContentPane().add(clearButton);
		clearButton.setBackground(new java.awt.Color(204,204,204));
		clearButton.setForeground(java.awt.Color.black);
		clearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		clearButton.setBounds(192,72,84,24);
		textAreaPane.setOpaque(true);
		getContentPane().add(textAreaPane);
		textAreaPane.setBounds(24,132,528,264);
		textAreaPane.getViewport().add(textArea);
		textArea.setBackground(java.awt.Color.white);
		textArea.setForeground(java.awt.Color.black);
		textArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		textArea.setBounds(0,0,524,260);
		cancelButton.setText("cancel/exit");
		cancelButton.setActionCommand("cancel");
		getContentPane().add(cancelButton);
		cancelButton.setBackground(new java.awt.Color(204,204,204));
		cancelButton.setForeground(java.awt.Color.black);
		cancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
		cancelButton.setBounds(372,72,180,24);
		copyButton.setActionCommand("copy");
		copyButton.setToolTipText("copy");
		getContentPane().add(copyButton);
		copyButton.setBackground(new java.awt.Color(204,204,204));
		copyButton.setForeground(java.awt.Color.black);
		copyButton.setFont(new Font("Dialog", Font.BOLD, 12));
		copyButton.setBounds(24,96,84,24);
		cutButton.setActionCommand("cut");
		cutButton.setToolTipText("cut");
		getContentPane().add(cutButton);
		cutButton.setBackground(new java.awt.Color(204,204,204));
		cutButton.setForeground(java.awt.Color.black);
		cutButton.setFont(new Font("Dialog", Font.BOLD, 12));
		cutButton.setBounds(108,96,84,24);
		pasteButton.setActionCommand("paste");
		pasteButton.setToolTipText("paste");
		getContentPane().add(pasteButton);
		pasteButton.setBackground(new java.awt.Color(204,204,204));
		pasteButton.setForeground(java.awt.Color.black);
		pasteButton.setFont(new Font("Dialog", Font.BOLD, 12));
		pasteButton.setBounds(192,96,84,24);
		searchButton.setText("search");
		searchButton.setActionCommand("search");
		getContentPane().add(searchButton);
		searchButton.setBackground(new java.awt.Color(204,204,204));
		searchButton.setForeground(java.awt.Color.black);
		searchButton.setFont(new Font("Dialog", Font.BOLD, 12));
		searchButton.setBounds(372,96,96,24);
		replaceButton.setText("replace");
		replaceButton.setActionCommand("replace");
		getContentPane().add(replaceButton);
		replaceButton.setBackground(new java.awt.Color(204,204,204));
		replaceButton.setForeground(java.awt.Color.black);
		replaceButton.setFont(new Font("Dialog", Font.BOLD, 12));
		replaceButton.setBounds(468,96,84,24);
		getContentPane().add(urlField);
		urlField.setBounds(24,48,468,24);
		getContentPane().add(messageLabel);
		messageLabel.setBounds(24,396,456,24);
		paste2Button.setText("S");
		paste2Button.setActionCommand("paste2");
		paste2Button.setToolTipText("paste from system copy");
		getContentPane().add(paste2Button);
		paste2Button.setBackground(new java.awt.Color(204,204,204));
		paste2Button.setForeground(java.awt.Color.black);
		paste2Button.setFont(new Font("Dialog", Font.BOLD, 12));
		paste2Button.setBounds(276,96,96,24);
		/*
	     this.copyButton.setIcon(new ImageIcon("images/copy-icon.GIF"));
    	 this.cutButton.setIcon(new ImageIcon("images/cut-icon.GIF"));
		 this.pasteButton.setIcon(new ImageIcon("images/paste-icon.GIF"));
		 this.fileButton.setIcon(new ImageIcon("images/file-icon.GIF"));
		 this.cancelButton.setIcon(new ImageIcon("images/exit-icon.GIF"));
		 this.clearButton.setIcon(new ImageIcon("images/clear-icon.GIF"));
		 JLabel1.setIcon(new ImageIcon("images/editIcon.GIF"));
		paste2Button.setIcon(new ImageIcon("images/paste-icon.GIF"));
		*/
		//}}

		//{{INIT_MENUS
		//}}
/*	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		fileButton.addActionListener(lSymAction);
		okButton.addActionListener(lSymAction);
		clearButton.addActionListener(lSymAction);
		cancelButton.addActionListener(lSymAction);
		copyButton.addActionListener(lSymAction);
		cutButton.addActionListener(lSymAction);
		pasteButton.addActionListener(lSymAction);
		searchButton.addActionListener(lSymAction);
		replaceButton.addActionListener(lSymAction);
		SymFocus aSymFocus = new SymFocus();
		this.addFocusListener(aSymFocus);
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		paste2Button.addActionListener(lSymAction);
		//}}
 */
		super.registerListeners();
		
        buttons=new Vector();
		buttons.addElement(this.fileButton);
		buttons.addElement(this.okButton);
		buttons.addElement(this.clearButton);
		buttons.addElement(this.cancelButton);
		buttons.addElement(this.copyButton);
		buttons.addElement(this.cutButton);
		buttons.addElement(this.pasteButton);
		buttons.addElement(this.searchButton);
		buttons.addElement(this.replaceButton);
		buttons.addElement(this.paste2Button);
		int numberOfButtons=buttons.size();
		for(int i=0;i<numberOfButtons;i++){
		    SelectedButton b=(SelectedButton)(buttons.elementAt(i));
		    b.setFrame(this);
		    b.setID(i);
		}
		
		texts=new Vector();
		texts.addElement(this.textArea);
		for(int i=0;i<1;i++){
		    ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
		    t.setFrame(this);
		    t.setID(i);
		}
		
        this.panes=new Vector();
        panes.addElement(this.textAreaPane);
        for(int i=0;i<1;i++){
            ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(i));
            p.setFrame(this);
            p.setID(i);
        }
        
        

	}

	public TextEditFrame(String sTitle)
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
		(new TextEditFrame()).setVisible(true);
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
	controlledparts.ControlledButton okButton = new controlledparts.ControlledButton();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	controlledparts.ControlledButton fileButton = new controlledparts.ControlledButton();
	public controlledparts.ControlledButton clearButton = new controlledparts.ControlledButton();
	controlledparts.ControlledScrollPane textAreaPane = new controlledparts.ControlledScrollPane();
	controlledparts.ControlledTextArea textArea = new controlledparts.ControlledTextArea();
	controlledparts.ControlledButton cancelButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton copyButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton cutButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton pasteButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton searchButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton replaceButton = new controlledparts.ControlledButton();
	javax.swing.JLabel urlField = new javax.swing.JLabel();
	javax.swing.JLabel messageLabel = new javax.swing.JLabel();
	controlledparts.ControlledButton paste2Button = new controlledparts.ControlledButton();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == fileButton)
				fileButton_actionPerformed(event);
			else if (object == okButton)
				okButton_actionPerformed(event);
			else if (object == clearButton)
				clearButton_actionPerformed(event);
			else if (object == cancelButton)
				cancelButton_actionPerformed(event);
			else if (object == copyButton)
				copyButton_actionPerformed(event);
			else if (object == cutButton)
				cutButton_actionPerformed(event);
			else if (object == pasteButton)
				pasteButton_actionPerformed(event);
			else if (object == searchButton)
				searchButton_actionPerformed(event);
			else if (object == replaceButton)
				replaceButton_actionPerformed(event);
			else if (object == paste2Button)
				paste2Button_actionPerformed(event);
		}
	}

	void fileButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		fileButton_actionPerformed_Interaction1(event);
	}

	void fileButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
//        loadTextFileDialog.promptLabel.setText("text file name:");
//        loadTextFileDialog.titleLabel.setText("input text File Name");
//        loadTextFileDialog.actionButton.setText("open");
//        loadTextFileDialog.setListener(this);
//        loadTextFileDialog.show();
	}

	void okButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
//		okButton_actionPerformed_Interaction1(event);
	}

	void clearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		clearButton_actionPerformed_Interaction1(event);
	}

	void clearButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
	//		textArea.setText("");
		} catch (Exception e) {
		}
	}

	void cancelButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		cancelButton_actionPerformed_Interaction1(event);
	}

	void cancelButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.dispose();
		} catch (Exception e) {
		}
//		this.dialogListener.whenCancelButtonPressed(this);
//		this.hide();
	}

	void copyButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
//		copyButton_actionPerformed_Interaction1(event);
	}

	void cutButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
//		cutButton_actionPerformed_Interaction1(event);
	}

	void pasteButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
//		pasteButton_actionPerformed_Interaction1(event);
	}

	void searchButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		searchButton_actionPerformed_Interaction1(event);
	}

	void searchButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// searchButton Request the focus
			searchButton.requestFocus();
		} catch (java.lang.Exception e) {
		}
	}

	void replaceButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		replaceButton_actionPerformed_Interaction1(event);
	}

	void replaceButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// replaceButton Request the focus
//			replaceButton.requestFocus();
		} catch (java.lang.Exception e) {
		}
	}

	class SymFocus extends java.awt.event.FocusAdapter
	{
		public void focusLost(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == TextEditFrame.this)
				TextEditFrame_focusLost(event);
		}

		public void focusGained(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == TextEditFrame.this)
				TextEditFrame_focusGained(event);
		}
	}

	void TextEditFrame_focusGained(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
			 
		TextEditFrame_focusGained_Interaction1(event);
	}

	void TextEditFrame_focusGained_Interaction1(java.awt.event.FocusEvent event)
	{
		try {
			// TextEditFrame Request the focus
//			this.requestFocus();
		} catch (java.lang.Exception e) {
		}
		this.focusGained();
	}

	void TextEditFrame_focusLost(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
			 
		TextEditFrame_focusLost_Interaction1(event);
	}

	void TextEditFrame_focusLost_Interaction1(java.awt.event.FocusEvent event)
	{
		try {
			// TextEditFrame Request the focus
//			this.requestFocus();
		} catch (java.lang.Exception e) {
		}
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowActivated(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == TextEditFrame.this)
				TextEditFrame_windowActivated(event);
		}
	}

	void TextEditFrame_windowActivated(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		TextEditFrame_windowActivated_Interaction1(event);
	}

	void TextEditFrame_windowActivated_Interaction1(java.awt.event.WindowEvent event)
	{
		try {
			// TextEditFrame Request the focus
//			this.requestFocus();
		} catch (java.lang.Exception e) {
		}
		this.focusGained();
	}

	void paste2Button_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		paste2Button_actionPerformed_Interaction1(event);
	}

	void paste2Button_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			paste2Button.show();
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
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
		t.releaseKey(p, code);

	}
	
	public void pressKey(int i, int p, int code){
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
		t.pressKey(p, code);
		
	}

	public String getFilePath() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public String getFileName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}