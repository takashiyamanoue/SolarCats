/*
		A basic implementation of the JFrame class.
*/
package nodesystem.eventrecorder;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.*;
import java.net.URL;
import java.util.Vector;

import javax.swing.ImageIcon;

import nodesystem.AMessage;
import nodesystem.CommandTranceiver;
import nodesystem.CommunicationNode;
import nodesystem.EditDialog;
import nodesystem.FileFrame;
import nodesystem.JFileDialog;

import controlledparts.ControlledButton;
import controlledparts.ControlledCheckBox;
import controlledparts.ControlledScrollPane;
import controlledparts.ControlledTextArea;
import controlledparts.SelectedButton;

public class Logger extends javax.swing.JFrame implements controlledparts.FrameWithControlledButton, controlledparts.FrameWithControlledCheckBox, controlledparts.FrameWithControlledPane, controlledparts.FrameWithControlledTextAreas, java.lang.Runnable, nodesystem.DialogListener    
{

    public String toStrConst(String x)
    {
        String sx="";
        int i=0;
        int len=x.length();
        while(i<len){
            char c=x.charAt(i);
//            System.out.println("c="+c+":"+(int)c);
            if(c=='\''){
                sx=sx+'\\'; sx=sx+c; i++; }
            else
            if(c=='\"'){
                sx=sx+'\\'; sx=sx+c; i++; }
            else
            if(c=='\\'){sx=sx+'\\'; sx=sx+c; i++;
                        c=x.charAt(i); sx=sx+c; i++; }
            else
            if((int)c==10){
                sx=sx+"\\n"; i++;
                if(i>=len) break;
                c=x.charAt(i);
                if((int)c==13) i++;
                }
            else
            if((int)c==13){
                sx=sx+"\\n"; i++;
                if(i>=len) break;
                c=x.charAt(i);
                if((int)c==10) i++;}
            else
            { sx=sx+c; i++; }
//            System.out.println(sx);
        }

        String rtn=sx;
        return rtn;
    }

    public void sendLogToAll()
    {
        if(!this.isControlledByLocalUser()) return;
        String sending="send.start()";
        this.sendEvent(sending);
        int max=this.fileManager.getMaxIndex();
        for(int i=0;i<max;i++){
            AMessage work= this.fileManager.getMessageAt(i);
            String coms=this.toStrConst(work.getHead());
            sending= "send.put(\""+coms+"\","+i+")";
            work.setHead(sending);
            this.sendEvent(work);
        }
        sending="send.end()";
        this.sendEvent(sending);        
    }

    public void setEventRecorder(EventRecorder r)
    {
        this.eventRecorder=r;
    }

    EventRecorder eventRecorder;

    public Vector checkBoxes;

    public void changeStateCheckBox(int i, int x)
    {
        // This method is derived from interface controlledparts.FrameWithControlledCheckBox
        // to do: code goes here
        ControlledCheckBox b=(ControlledCheckBox)(checkBoxes.elementAt(i));
        b.changeState(x);
        this.stateChangedAtCheckBox(i,x);
    }

    public void focusCheckBox(int i)
    {
        // This method is derived from interface controlledparts.FrameWithControlledCheckBox
        // to do: code goes here
            ControlledCheckBox b=(ControlledCheckBox)(checkBoxes.elementAt(i));
            b.focus();
    }

    public void mouseEnteredAtCheckBox(int i)
    {
        // This method is derived from interface controlledparts.FrameWithControlledCheckBox
        // to do: code goes here
    }

    public void mouseExitedAtCheckBox(int i)
    {
        // This method is derived from interface controlledparts.FrameWithControlledCheckBox
        // to do: code goes here
    }

    public void stateChangedAtCheckBox(int i, int x)
    {
        // This method is derived from interface controlledparts.FrameWithControlledCheckBox
        // to do: code goes here
    }

    public void unfocusCheckBox(int i)
    {
        // This method is derived from interface controlledparts.FrameWithControlledCheckBox
        // to do: code goes here
            ControlledCheckBox b=(ControlledCheckBox)(checkBoxes.elementAt(i));
            b.unFocus();
    }

    public void saveText(String fileName)
    { 
        File file=new File(fileName);
        FileOutputStream fouts=null;
        try{ fouts= new FileOutputStream(file);}
        catch(FileNotFoundException e){
            System.out.println("wrong directory:"+file.getPath()+" ?");
            return;
        }
        /*
        catch(IOException e){ System.out.println("cannot access"+file.getPath()+".");}
        */
        for(int i=0;i<fileManager.maxIndex;i++){
            AMessage m=this.fileManager.getMessageAt(i);
            String line=m.getHead();
            if(line==null) break;

            byte[] buff=null;
            try{
               buff=line.getBytes(this.communicationNode.encodingCode);
            }
            catch(java.io.UnsupportedEncodingException e){}
            try{
               fouts.write(buff);
            }
            catch(IOException e)
            { System.out.println("save:IOExceptin while writing.");}        
        }
        try{ 
            fouts.close();
            fouts.flush();
        }
        catch(IOException e)
        { System.out.println("save:IOException while closing.");}
        
        System.out.println("Saving done.");
   }

    public boolean isDirectOperation()
    {
        /*
        if(communicationNode==null) return true;
        return this.communicationNode.isDirectOperation();
        */
        if(eventRecorder==null) return true;
        return this.eventRecorder.isDirectOperation();
    }

    public void setTextOnTheText(int id, int pos, String s)
    {
        ControlledTextArea t=(ControlledTextArea)(this.texts.elementAt(id));
        t.setTextAt(pos,s);
    }

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
        /*
        if(communicationNode==null) return true;
        return this.communicationNode.isControlledByLocalUser();
        */
        if(eventRecorder==null) return true;
        return this.eventRecorder.isControlledByLocalUser();
    }

    public boolean isShowingRmouse()
    {
        return this.communicationNode.rmouseIsActivated;
    }

    public void moveMouseOnTheText(int id, int x, int y)
    {
        
    }

    public void mouseMoveAtTextArea(int id,int x, int y)
    {
    }
    
    public void exitMouseOnTheText(int id, int x, int y)
    {
    }

    public void enterMouseOnTheText(int id, int x, int y)
    {
    }

    public void mouseExitAtTheText(int id, int x, int y)
    {
    }
    
    public void mouseEnteredAtTheText(int id, int x, int y)
    {
    }
    
    public void changeScrollbarValue(int paneID, int barID, int value)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
    }

    public void hideScrollBar(int paneID, int barID)
    {
        // This method is derived from interface FrameWithControlledPane
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

    public void showScrollBar(int paneID, int barID)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
    }

    public Vector panes;

    public boolean isSendingEvent;

    public void pressMouseOnTheText(int id, int p, int x, int y)
    {
    }

    public void mousePressedAtTextArea(int id, int p, int x, int y)
    {
    }
    public void sendEvent(AMessage m)
    {
        if(communicationNode==null) return;
        if(communicationNode.eventRecorder==null) return;
        CommandTranceiver ct=this.communicationNode.commandTranceiver;
        if(ct==null) return;
        if(this.communicationNode.eventRecorder.sendEventFlag){
        	String s="broadcast shell log "+label+"."+m.getHead()+ ct.endOfACommand;
        	m.setHead(s);
           ct.ebuff.putS(m);
        }
    }
    public void sendEvent(String s){
    	AMessage x=new AMessage();
    	x.setHead(s);
    	sendEvent(x);
    }

    Vector texts;

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

    public void mouseClickedAtTextArea(int i, int p,int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseDraggedAtTextArea(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseReleasedAtTextArea(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void releaseMouseOnTheText(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void typeKey(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public FileFrame fileFrame;

    public void setCommunicationNode(CommunicationNode cn)
    {
        communicationNode=cn;
    }

    public CommunicationNode communicationNode;

    public void read(String urlname)
    {
        /*
        this.fileManager.clearThis();
        this.logArea.setText("");
        
        DataInputStream inputStream;
        URL  url;
        String rl;

        if(urlname==null){
            System.out.println("URL Name is expected.");
            return;
        }
//        System.out.println("reading "+urlname+"\n");
        try{ url=new URL(urlname); }
        catch(MalformedURLException e)
        {  System.out.println("MalformedURLException");
           return;
        }

        try{
            inputStream=new DataInputStream(url.openStream());
        }
        catch(IOException e){
            System.out.println("url open error.\n");
            return;
        }

        int i=0;
        while(true){
           try{ 
//            rl=inputStream.readLine();
              rl=inputStream.readUTF();
           }
           catch(IOException e){ return;}
           catch(NullPointerException e)
           {
              break;
           }
           if(rl==null) break;
           if(rl.length()>0) this.fileManager.putStringAt(rl,i);
           i++;
        }

        try{ inputStream.close();  }
        catch(IOException e)
           { System.out.println("read:IOException while closing.\n");}
        System.out.println("reading done.\n");
        */
        BlockedFileManager source
        =new BlockedFileManager( // file.getPath()
                                 urlname
                                   ,communicationNode.encodingCode);
        int max=source.getMaxIndex();
        for(int i=0;i<max;i++){
            AMessage work= source.getMessageAt(i);
            fileManager.putMessageAt(work,i);
        }
        this.fileManager.setMaxIndex(max,true);
        source.close();
        this.showTheFile();
    }
    
    public void readText(String fileName){
		File file=new File(fileName);
		FileInputStream fins=null;
		try{ fins= new FileInputStream(file);}
		catch(FileNotFoundException e){
			System.out.println("wrong directory:"+file.getPath()+" ?");
			return;
		}
		BufferedReader inputStream=null;
	   String codingCode=this.communicationNode.getEncodingCode();

	   try{
		   inputStream=//new DataInputStream(url.openStream());
				   new BufferedReader(
						 new InputStreamReader(fins,codingCode));
	   }
	   catch(UnsupportedEncodingException e){
			System.out.println("encoding unsupported.\n");
		   return;
	  }
	   catch(IOException e){
		   System.out.println("url open error.\n");
		   return;
	   }

	  String x=null;
	  int i=0;
	  do{
		 x=null;
		 try{
		   x=inputStream.readLine();
		 }
		 catch(java.io.IOException e){  break;}
		 if(x!=null){
			x=x+"\n";
			AMessage m=new AMessage();
			m.setHead(x);
			this.fileManager.putMessageAt(m,i);
			i++;
		 }
	  } while(x!=null);
  
	   try{ inputStream.close();  }
	   catch(IOException e)
		  { System.out.println("read:IOException while closing.");}
		System.out.println("reading done.\n");
		System.out.println("Reading done.");

    }

    public String label;

    public Vector dialogs;

    public JFileDialog fileDialog;

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

    public void sendFileDialogMessage(String m)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        sendEvent("file."+m);
    }

    public void whenActionButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        String dname=d.getDialogName();
        if(dname.equals("input common file name:")){
/*            
            File commonDataLogDir=new File(communicationNode.commonDataDir,label);
            if(!commonDataLogDir.exists())
                     commonDataLogDir.mkdir();
*/                     
            String url="";
    		String fileName=d.getText();
			if(this.textFileCheckBox.isSelected()) 
			   readText(fileName);
			else
        	   read(fileName);
            this.currentFileNameField.setText(fileName);
            return;
        }
        if(dname.equals("input user file name:")){
/*             
            File nodeLogDir=new File(communicationNode.nodeDataDir,label);
            if(!nodeLogDir.exists())
                     nodeLogDir.mkdir();
*/                     
            String url="";
    		String fileName=d.getText();
    		if(this.textFileCheckBox.isSelected())
    		   readText(fileName);
    		else
        	   read(fileName);
            this.currentFileNameField.setText(fileName);
            return;
            
        }
        if(dname.equals("output user file name:")){
/*             
            File nodeLogDir=new File(communicationNode.nodeDataDir,label);
            if(!nodeLogDir.exists())
                     nodeLogDir.mkdir();
*/                     
            String url="";
    		String fileName=d.getText();
    		if(this.textFileCheckBox.isSelected()) 
    		   this.saveText(fileName);
    		else
            	save(fileName);
            this.currentFileNameField.setText(fileName);
            return;
            
        }
        if(dname.equals("output common file name:")){
            if(!this.communicationNode.getControlMode().equals("teach")) return;
            /*
            File commonDataLogDir=new File(communicationNode.commonDataDir,label);
            if(!commonDataLogDir.exists()) commonDataLogDir.mkdir();
              */       
            String url="";
    		String fileName=d.getText();
            this.save(fileName);
            this.currentFileNameField.setText(fileName);
            return;
        }
        if(dname.equals("url:")){
            String url=d.getText();
        	read(url);
            this.currentFileNameField.setText(url);
            return;
        }
    }

    public void whenCancelButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

    public void save(String fileName)
    {
        BlockedFileManager dest
        =new BlockedFileManager(fileName,
                                   communicationNode.encodingCode);
        int max=this.fileManager.getMaxIndex();
        for(int i=0;i<max;i++){
            AMessage work= this.fileManager.getMessageAt(i);
            dest.putMessageAt(work,i);
        }
//        dest.setMaxIndex(max,true);
        dest.close();
        this.showTheFile();
    }

    public void show()
    {
        super.show();
        this.showTheFile();
//        start();
    }

    public Vector buttons;

    public void unfocusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        b.unFocus();
    }

    public void mouseExitedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
    }

    public void mouseEnteredAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
    }

    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
//        this.sendEvent("btn.click("+i+")\n");
       if(i==0){ // nextPageButton
            stop();
    		this.currentIndex=this.currentIndex+this.linesOfPage;
            if(currentIndex>(fileManager.maxIndex-linesOfPage/2)) currentIndex=fileManager.maxIndex-linesOfPage/2;
            showTheFile();
            start();
            return;
       }
        else
        if(i==1){ // previousPageButton
            stop();
    		this.currentIndex=this.currentIndex-this.linesOfPage;
            if(currentIndex<0) currentIndex=0;
            showTheFile();
            start();
            return;
        }
        else
        if(i==2){ // clearButton
            fileManager.maxIndex=0;
//    		fileManager.clearThis();
			logArea.setText("");
			repaint();
			return;
        }
        else
        if(i==3){ // fileButton
            stop();
            String separator=""+System.getProperty("file.separator");
            fileFrame.setListener(this);
            String commonPath=this.communicationNode.commonDataDir.toString()+separator+"Logs";
            fileFrame.setCommonPath(commonPath);
            File commonPathFile=new File(commonPath);
            if(!commonPathFile.exists())
                commonPathFile.mkdir();
            String userPath=this.communicationNode.userDataDir.toString()+separator+"Logs";
            fileFrame.setUserPath(userPath);
            File userPathFile=new File(userPath);
            if(!userPathFile.exists())
                userPathFile.mkdir();
            fileFrame.setVisible(true);
            start();
            return;
        }
        else
        if(i==4){ // closeButton
            stop();
            this.hide();
            return;
        }
        else
        if(i==this.toAllButton.getID()){ // toAllButton
            sendLogToAll();
            return;
        }
    }

    public void focusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        b.focus();
    }

    public void clickButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        /*
		buttons.addElement(	nextPageButton);      0
		buttons.addElement(	previousPageButton);  1
		buttons.addElement(	clearButton);         2
		buttons.addElement( this.fileButton);     3
		buttons.addElement( this.closeButton);    4
		*/
		ControlledButton b=(ControlledButton)(buttons.elementAt(i));
		b.click();
		this.mouseClickedAtButton(i);
    }

    int linesOfPage;
    public int currentIndex;
    
    public void setIcons(String iconPlace){
    	try{
    		nextPageButton.setIcon(new ImageIcon(new URL(iconPlace+"next-icon.GIF")));
    		previousPageButton.setIcon(new ImageIcon(new URL(iconPlace+"stepback.GIF")));
    		clearButton.setIcon(new ImageIcon(new URL(iconPlace+"clear-icon.GIF")));
    		fileButton.setIcon(new ImageIcon(new URL(iconPlace+"file-icon.GIF")));
    		closeButton.setIcon(new ImageIcon(new URL(iconPlace+"exit-icon.GIF")));
    		
    	}
    	catch (Exception e){
    		nextPageButton.setText("next");
    		previousPageButton.setText("stepback");
    		clearButton.setText("clear");
    		fileButton.setText("file");
    		closeButton.setText("exit");
    		
    	}
   }
	public Logger()
	{
    	panes=new Vector();
	    panes.addElement(this.logAreaPane);
	    panes.addElement(this.fileNamePane);
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
		setTitle("Logger");
		setResizable(false);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		setSize(443,422);
		setVisible(false);
		logAreaPane.setOpaque(true);
		getContentPane().add(logAreaPane);
		logAreaPane.setBounds(12,108,420,276);
		logArea.setSelectionColor(new java.awt.Color(204,204,255));
		logArea.setSelectedTextColor(java.awt.Color.black);
		logArea.setCaretColor(java.awt.Color.black);
		logArea.setDisabledTextColor(new java.awt.Color(153,153,153));
		logAreaPane.getViewport().add(logArea);
		logArea.setBackground(java.awt.Color.white);
		logArea.setForeground(java.awt.Color.black);
		logArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		logArea.setBounds(0,0,416,272);
		loggerLabel.setText("log");
		getContentPane().add(loggerLabel);
		loggerLabel.setBackground(new java.awt.Color(204,204,204));
		loggerLabel.setForeground(new java.awt.Color(102,102,153));
		loggerLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		loggerLabel.setBounds(24,12,300,24);
		{
		nextPageButton.setActionCommand("Next Page");
		getContentPane().add(nextPageButton);
		nextPageButton.setBackground(new java.awt.Color(204,204,204));
		nextPageButton.setForeground(java.awt.Color.black);
		nextPageButton.setFont(new Font("Dialog", Font.BOLD, 12));
		nextPageButton.setBounds(324,84,48,24);
	    }
	    {
		previousPageButton.setActionCommand("Previous Page");
		getContentPane().add(previousPageButton);
		previousPageButton.setBackground(new java.awt.Color(204,204,204));
		previousPageButton.setForeground(java.awt.Color.black);
		previousPageButton.setFont(new Font("Dialog", Font.BOLD, 12));
		previousPageButton.setBounds(276,84,48,24);
	    }
	    {
		clearButton.setActionCommand("clear");
		getContentPane().add(clearButton);
		clearButton.setBackground(new java.awt.Color(204,204,204));
		clearButton.setForeground(java.awt.Color.black);
		clearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		clearButton.setBounds(216,84,60,24);
	    }
	    {
		fileButton.setActionCommand("file");
		getContentPane().add(fileButton);
		fileButton.setBackground(new java.awt.Color(204,204,204));
		fileButton.setForeground(java.awt.Color.black);
		fileButton.setFont(new Font("Dialog", Font.BOLD, 12));
		fileButton.setBounds(12,84,72,24);
	    }
	    {
		closeButton.setActionCommand("x");
		getContentPane().add(closeButton);
		closeButton.setBackground(new java.awt.Color(204,204,204));
		closeButton.setForeground(java.awt.Color.black);
		closeButton.setFont(new Font("Dialog", Font.BOLD, 12));
		closeButton.setBounds(156,84,60,24);
	    }
		textFileCheckBox.setText("text file");
		textFileCheckBox.setActionCommand("text file");
		getContentPane().add(textFileCheckBox);
		textFileCheckBox.setBackground(new java.awt.Color(204,204,204));
		textFileCheckBox.setForeground(java.awt.Color.black);
		textFileCheckBox.setFont(new Font("Dialog", Font.BOLD, 12));
		textFileCheckBox.setBounds(84,84,72,24);
		fileNamePane.setOpaque(true);
		getContentPane().add(fileNamePane);
		fileNamePane.setBounds(12,36,420,48);
		fileNamePane.getViewport().add(currentFileNameField);
		currentFileNameField.setBackground(java.awt.Color.white);
		currentFileNameField.setForeground(java.awt.Color.black);
		currentFileNameField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		currentFileNameField.setBounds(0,0,416,44);
		toAllButton.setText("toAll");
		toAllButton.setActionCommand("toAll");
		toAllButton.setToolTipText("send this log to all nodes");
		getContentPane().add(toAllButton);
		toAllButton.setBackground(new java.awt.Color(204,204,204));
		toAllButton.setForeground(java.awt.Color.black);
		toAllButton.setFont(new Font("Dialog", Font.BOLD, 12));
		toAllButton.setBounds(372,84,60,24);
		//}}

		//{{INIT_MENUS
		//}}
	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		nextPageButton.addActionListener(lSymAction);
		previousPageButton.addActionListener(lSymAction);
		clearButton.addActionListener(lSymAction);
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		fileButton.addActionListener(lSymAction);
		closeButton.addActionListener(lSymAction);
		SymItem lSymItem = new SymItem();
		textFileCheckBox.addItemListener(lSymItem);
		toAllButton.addActionListener(lSymAction);
		//}}
		
		buttons=new Vector();
		buttons.addElement(	nextPageButton);
		buttons.addElement(	previousPageButton);
		buttons.addElement(	clearButton);
		buttons.addElement( this.fileButton);
		buttons.addElement( this.closeButton);
		buttons.addElement( this.toAllButton);
		
		int numberOfButtons=buttons.size();
		for(int i=0;i<numberOfButtons;i++){
		    SelectedButton b=(SelectedButton)(buttons.elementAt(i));
		    b.setFrame(this);
		    b.setID(i);
		}
//		this.linesOfPage=this.logArea.getRows();
        linesOfPage=20;
		
        fileFrame=new FileFrame();
        fileFrame.setListener(this);
        /*                     
        this.dialogs=new Vector();
        dialogs.addElement(fileDialog);
        fileDialog.setID(0);
        fileDialog.setVector(dialogs);
        */
		texts=new Vector();
		texts.addElement(this.logArea);
		texts.addElement(this.currentFileNameField);
		for(int i=0;i<1;i++){
		    ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
		    t.setFrame(this);
		    t.setID(i);
		}

    	checkBoxes=new Vector();
	    checkBoxes.addElement(this.textFileCheckBox);
	    int numberOfCheckBox=checkBoxes.size();
	    for(int i=0;i<numberOfCheckBox;i++){
	        ControlledCheckBox t=(ControlledCheckBox)(checkBoxes.elementAt(i));
	        t.setFrame(this);
	        t.setID(i);
	    }
        
        this.setTitle("Logger");
        
	}

	public Logger(String sTitle)
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
		(new Logger()).setVisible(true);
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
	controlledparts.ControlledScrollPane logAreaPane = new controlledparts.ControlledScrollPane();
	controlledparts.ControlledTextArea logArea = new controlledparts.ControlledTextArea();
	javax.swing.JLabel loggerLabel = new javax.swing.JLabel();
	controlledparts.ControlledButton nextPageButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton previousPageButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton clearButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton fileButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton closeButton = new controlledparts.ControlledButton();
	controlledparts.ControlledCheckBox textFileCheckBox = new controlledparts.ControlledCheckBox();
	controlledparts.ControlledScrollPane fileNamePane = new controlledparts.ControlledScrollPane();
	controlledparts.ControlledTextArea currentFileNameField = new controlledparts.ControlledTextArea();
	controlledparts.ControlledButton toAllButton = new controlledparts.ControlledButton();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == nextPageButton)
				nextPageButton_actionPerformed(event);
			else if (object == previousPageButton)
				previousPageButton_actionPerformed(event);
			else if (object == clearButton)
				clearButton_actionPerformed(event);
			else if (object == fileButton)
				fileButton_actionPerformed(event);
			else if (object == closeButton)
				closeButton_actionPerformed(event);
			else if (object == toAllButton)
				toAllButton_actionPerformed(event);
			
		}
	}

	void nextPageButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		nextPageButton_actionPerformed_Interaction1(event);
	}

	void nextPageButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	}

	void previousPageButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		previousPageButton_actionPerformed_Interaction1(event);
	}

	void previousPageButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	}
    public void showTheFile()
    {
        if(fileManager==null) return;
        int ifrom=currentIndex-linesOfPage/2-1;
        if(ifrom<0) ifrom=0;
        int ito=currentIndex+linesOfPage/2;
        if(ito>fileManager.maxIndex) ito=fileManager.maxIndex;
        int i;
        String currentMessages="";
        for(i=ifrom;i<ito;i++){
//            String theTime=fileManager.getStringAt(i);
              AMessage theMessage=fileManager.getMessageAt(i);
//            currentMessages=currentMessages+theTime+":"+theMessage+"\n";
              currentMessages=currentMessages+theMessage.getHead();
        }
        logArea.setText(currentMessages);
//        Point p=new Point(0,0);
//        logArea.viewToModel(p);
        logArea.repaint();
    }
    public void run()
    {
        while(me!=null){
            showTheFile();
            try{
                Thread.sleep(20000);
            }
            catch(InterruptedException e){
            }
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
        /*
        if(me==null){
            me=new Thread(this,"Logger");
            me.start();
        }
        */
    }
    public Thread me;
    public BlockedFileManager fileManager;
    public Logger(String title, String label, BlockedFileManager fm)
    {
	    this();
	    setTitle(title);
	    this.loggerLabel.setText(label);
	    this.label=label;
	    fileManager =fm;
	    this.showTheFile();
    }


	void clearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		clearButton_actionPerformed_Interaction1(event);
	}

	void clearButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == Logger.this)
				Logger_windowClosing(event);
		}
	}

	void Logger_windowClosing(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
		this.mouseClickedAtButton(4);
//			 stop();
//			 this.hide();
	}

	void fileButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		fileButton_actionPerformed_Interaction1(event);
	}

	void fileButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// fileButton Show the ControlledButton
//			fileButton.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	void closeButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		closeButton_actionPerformed_Interaction1(event);
	}

	void closeButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// closeButton Show the ControlledButton
//			closeButton.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if (object == textFileCheckBox)
				textFileCheckBox_itemStateChanged(event);
		}
	}

	void textFileCheckBox_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
			 
		textFileCheckBox_itemStateChanged_Interaction1(event);
	}

	void textFileCheckBox_itemStateChanged_Interaction1(java.awt.event.ItemEvent event)
	{
		try {
//			textFileCheckBox.show();
		} catch (java.lang.Exception e) {
		}
	}

	void toAllButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		toAllButton_actionPerformed_Interaction1(event);
	}

	void toAllButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			toAllButton.show();
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
	public void pressKey(int i, int p, int code){
	}

}