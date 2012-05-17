/*
		A basic implementation of the JFrame class.
*/
package application.webleap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTextArea;
import javax.swing.ImageIcon;

import nodesystem.*;
import webleap.SearchEngineDriver;
import webleap.Settings;
import application.draw.DrawFrame;
import application.draw.NetworkReader;
import controlledparts.*;


/**
* This code was generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* *************************************
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED
* for this machine, so Jigloo or this code cannot be used legally
* for any corporate or commercial purpose.
* *************************************
*/
public class WebLEAPFrame extends ControlledFrame implements FrameWithControlledButton, FrameWithControlledPane, FrameWithControlledTextAreas, nodesystem.MessageReceiver, webleap.MessageReceiver      
{
	Color backColor=new Color(180,200,250);
    SearchEngineDriver searchEngineDriver;
    String proxyKind;
    String passWord;

    public void setTextOnTheText(int id, int pos, String s)
    {
        ControlledTextArea t=(ControlledTextArea)(this.texts.elementAt(id));
		t.setTextAt(pos,s);
    }

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
        return super.isControlledByLocalUser();
    }
/*
    public boolean isShowingRmouse()
    {
        if(this.communicationNode==null) return false;
        return this.communicationNode.rmouseIsActivated;
    }
*/
    public void mouseEnteredAtTheText(int id, int x, int y)
    {
    }

    public void mouseExitAtTheText(int id, int x, int y)
    {
    }

    public void enterMouseOnTheText(int id, int x, int y)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
        t.enterMouse(x,y);
    }

    public void exitMouseOnTheText(int id, int x, int y)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
        t.exitMouse();

    }

    public void clearAll()
    {
        this.evaluationArea.setText("");
        this.messageArea.setText("");
        this.objectText.setText("");
		this.minPhraseChoice.setSelectedItem("2");
		this.maxPhraseChoice.setSelectedItem("4");
        
    }

    public void receiveMessage(String x)
    {
        this.messageArea.append(x);
    }

    public void pasteAtTheText(int i)
    {
            String theText="";
            String newText="";
            int startPosition=0;
            int endPosition=0;
            ControlledTextArea ta=(ControlledTextArea)(this.texts.elementAt(i));
            ControlledScrollPane p=(ControlledScrollPane)(this.panes.elementAt(i));
            ControlledScrollBar vs=(ControlledScrollBar)(p.scrollBars.elementAt(0));
            int vsv=vs.getValue();
            ControlledScrollBar hs=(ControlledScrollBar)(p.scrollBars.elementAt(1));
            int hsv=hs.getValue();
            startPosition=ta.getSelectionStart();
            theText=ta.getText();
            startPosition=ta.getSelectionStart();
            if(startPosition<0) startPosition=0;
            endPosition=ta.getSelectionEnd();
            if(endPosition<startPosition){
                int w=startPosition; startPosition=endPosition;
                endPosition=w;
            }
            if(endPosition>theText.length())
               endPosition=theText.length();
            newText=theText.substring(0,startPosition);
            newText=newText+ communicationNode.tempText+
                     (ta.getText()).substring(endPosition);
            ta.setText(newText);
            vs.setValue(vsv);
            hs.setValue(hsv);
            return;
    }


    public void cutAnAreaOfTheText(int i)
    {
        String theText="";
        String newText="";
        int startPosition=0;
        int endPosition=0;
        ControlledTextArea ta=(ControlledTextArea)(this.texts.elementAt(i));
            startPosition=ta.getSelectionStart();
            theText=ta.getText();
            if(startPosition<0) startPosition=0;
            endPosition=ta.getSelectionEnd();
            if(endPosition<startPosition){
                int w=startPosition; startPosition=endPosition;
                endPosition=w;
            }
            if(endPosition>theText.length())
               endPosition=theText.length();
            communicationNode.tempText=theText.substring(startPosition,endPosition);
            newText=theText.substring(0,startPosition);
            newText=newText+ theText.substring(endPosition);
            ta.setText(newText);
            return;
    }

    
    public void copyFromTheText(int i)
    {
        String theText="";
        String newText="";
        int startPosition=0;
        int endPosition=0;
        ControlledTextArea ta=(ControlledTextArea)(this.texts.elementAt(i));
            theText=ta.getText();
            startPosition=ta.getSelectionStart();
            if(startPosition<0) startPosition=0;
            endPosition=ta.getSelectionEnd();
            if(endPosition<startPosition){
                int w=startPosition; startPosition=endPosition;
                endPosition=w;
            }
            if(endPosition>theText.length())
               endPosition=theText.length();
            this.communicationNode.tempText=theText.substring(startPosition,endPosition);
            return;
  }

    public void moveMouseOnTheText(int id, int x, int y)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
//        t.rmouse.move(x,y);
        t.moveMouse(x,y);
    }

    public void mouseMoveAtTextArea(int id, int x, int y)
    {
    }

    public String parseCommand(String x)
    {
        /*
        command:
           openUrlListTable <index>
        */
        if(x.indexOf("openUrlListTable ")==0){
            String st=x.substring("openUrlListTable ".length());
            webleap.KwicTable q=this.searchEngineDriver.getKWICtable();
            if(q==null) return null;
            q.showTable(st);
            return "";
        }
        return null;
    }

    public DrawFrame draw;

    void moveDownResults()
    {
		visualizer2.clear();
		
		remove(visualizer2);

		visualizer2=visualizer;
		visualizer2.setBounds(offsetX,
		                      visualizerHeight+offsetY+spaceBetweenVisualizer,
		     visualizerWidth,visualizerHeight);
		this.getContentPane().add(visualizer2);
		visualizer2.repaint();

		remove(visualizer);

        visualizer = new Visualizer();
		visualizer.setBounds(offsetX,offsetY,
		     visualizerWidth, visualizerHeight);
		visualizer.init(this,this.settings);
		this.getContentPane().add(visualizer);
		visualizer.repaint();

    }

//    UrlTable urlTable=null;
    
    void evaluateText()
    {
		messageArea.setText("");
		
		if(pg==null){
            pg=new PatternGenerator1();
		}
        this.proxyKind=this.settings.getProxyKind();
        this.passWord=this.settings.getPassWord();
//        pg.setProxyKind(this.proxyKind);
        
        /*
        int selectedEngine=this.settings.searchEngineComboBox.getSelectedIndex();
        String driver=this.settings.searchEngineSettingTab.getValueAt(selectedEngine,2);
        */
        if(urlTable==null){
             urlTable=new UrlTable("",this.communicationNode);
        }
        urlTable.setSettings(this.settings);
        this.settings.setKwicTable(urlTable);
        this.searchEngineDriver=this.settings.getSearchEngineDriver();
        this.searchEngineDriver.setSettings(this.settings);
        this.searchEngineDriver.setMessageReceiver2((webleap.MessageReceiver)this);
        if(!this.settings.noProxyButton.isSelected() )
            pg.setProxy(this.settings.proxyNameField.getText(),
                        (new Integer(this.settings.proxyPortField.getText())).intValue(),this.proxyKind, this.passWord);
        else
            pg.setProxy(null,0,this.proxyKind,"");

        visualizer.clear();
//        System.out.println("visualizer is visible? : "+visualizer.isVisible());
        String theText=(objectText.getText()).replace('\n',' ');
        pg.init(theText," \"(){}[]+*/!#$%&=:",this,settings);

        int nwords=pg.sequenceLength;
        for(int i=0;i<nwords;i++){
            visualizer.addWord(i,(String)(pg.wordSequence.elementAt(i)));
        }
        visualizer.repaint();

        Vector results=new Vector();
        pg.setSearchEngineDriver(this.searchEngineDriver);
        pg.setWords(results);
//        pg.generate(results);
        pg.start();

    }

    public ParseEvent parser;

    String commandName="webleap";
    public String getCommandName(){
    	return commandName;
    }

    public void sendEvent(String s)
    {
        if(this.communicationNode==null) return;
        this.communicationNode.sendEvent(commandName,s);
    }

    public void changeScrollbarValue(int paneID, int barID, int value)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
        ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(paneID));
        p.setScrollBarValue(barID,value);
    }

    public void hideScrollBar(int paneID, int barID)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
        ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(paneID));
        p.hideScrollBar(barID);
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
         ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(paneID));
         p.showScrollBar(barID);
    }

    public int lastManipulatedTextArea;

    public Vector panes;

    public void dispose()
    {
        super.dispose();
    }

    public void exitThis()
    {
//        System.out.println("webleapframe exithis");
//         hide();         // Frame を非表示にする.
          this.setVisible(false);
         if(this.communicationNode==null) {
//            this.dispose();
            System.exit(0);
            return;
         }
//         this.communicationNode.sendEventFlag=false;
//        communicationNode.applicationManager.kill(pID);
//        dispose();      // ｼｽﾃﾑﾘｿｰｽを開放する.
    }


    public void receiveEvent(String s)
    {
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
            ParseWebLEAPEvent evParser=new ParseWebLEAPEvent(this,iq);
            evParser.parsingString=s;
            evParser.run();
    }

    public void recordMessage(String s)
    {
        communicationNode.eventRecorder.recordMessage("\"WebLEAPFrame\","+s);
    }

    public ControlledFrame spawnMain(CommunicationNode cn, String args, int pID, String controlMode)
    {
           WebLEAPFrame wf=this;
           wf.setTitle("DSR/WebLEAP");
           wf.communicationNode=cn;
           wf.setIcons(cn.getIconPlace());
		   wf.urlTable=new UrlTable("url list",cn);
           wf.pID=pID;
//           wf.communicationNode.encodingCode=encodingCode;
           wf.setControlMode(controlMode);
           Settings s=new Settings();
           s.initTab();
           wf.setSettings(s);
           wf.draw=(DrawFrame)(wf.lookUp("DrawFrame"));
           wf.draw.setVisible(true);
           wf.draw.setIcons(cn.getIconPlace());
		
		// spawn Basic Slaves at other nodes in this group.
           wf.ebuff=cn.commandTranceiver.ebuff;
           wf.setVisible(true);
           return wf;
    }

    private Vector texts;

    private Vector buttons;

    public void setUrlTable(UrlTable ut){
    	this.urlTable=ut;
    }
    public void setDraw(DrawFrame df){
    	this.draw=df;
    }
    
    public void clickButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        SelectedButton b=(SelectedButton)(buttons.elementAt(i));
        b.click();
        this.mouseClickedAtButton(i);
    }

    public void clickMouseOnTheText(int i, int p,int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
        t.clickMouse(p,x,y);
    }

    public void dragMouseOnTheText(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
         ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
        t.dragMouse(position,x,y);
       
    }

    public void enterMouseAtTheFrame(int x, int y)
    {
        // This method is derived from interface FrameWithControlledFrame
        // to do: code goes here
    }

    public void focusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            ControlledButton button=(ControlledButton)(buttons.elementAt(i));
//            button.controlledButton_mouseEntered(null);
            button.focus();
    }

    public void gainFocus()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
        this.requestFocus();
    }

    public void keyIsTypedAtATextArea(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
//        sendEvent("txt.kdn("+i+","+p+","+key+")\n");
    }

	public void keyIsPressedAtATextArea(int i, int p, int key)
	{
		// This method is derived from interface FrameWithControlledTextAreas
		// to do: code goes here
//		  sendEvent("txt.kdn("+i+","+p+","+key+")\n");
	}

    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        sendEvent("btn.click("+i+")\n");
        /*
		buttons.addElement(this.evaluateButton);
		buttons.addElement(this.objectClearButton);
		buttons.addElement(this.stopButton);
		buttons.addElement(this.exitButton);
		buttons.addElement(this.moveButton);
		buttons.addElement(this.resultClearButton);
		buttons.addElement(this.clearSearchedButton);
		buttons.addElement(this.showSettingsButton);
		buttons.addElement(this.showGraphicsButton);
		buttons.addElement(this.hideGraphicsButton);
		buttons.addElement(this.copyButton);
		buttons.addElement(this.cutButton);
		buttons.addElement(this.pasteButton);
		buttons.addElement(this.messageClearButton);
		buttons.addElement(this.evaluationClearButton);
		*/
		if(i==this.evaluateButton.getID()){ // evaluateButton
		    this.evaluateText();
		    return;
		}
		if(i==this.objectClearButton.getID()){ // objectClearButton
		    objectText.setText("");
            return;
		}
		if(i==this.stopButton.getID()){ // stopButton
		    if(pg!=null) pg.stop();
		    return;
		}
		if(i==this.exitButton.getID()){ // exitButton
		    this.exitThis();
		    return;
		}
		/*
		if(i== 4){ // moveButton
		    this.moveDownResults();
		    return;
		}
		*/
		if(i==this.resultClearButton.getID()){ // resultClearButton
		     String downcommand="btn.click(5)";
             String rtn1=this.draw.parseCommand(downcommand);
//		    visualizer.clear();
//		    visualizer2.clear();
//		    repaint();
		    return;
		}
		if(i==this.clearSearchedButton.getID()){ // clearSearchedButton
		    searchedPhrases=new Hashtable();
		    return;
		}
		if(i==this.showSettingsButton.getID()){ // showSettingsButton
		    this.settings.setVisible(true);
		    return;
		}
		if(i==this.showGraphicsButton.getID()){ // showGraphicsButton
			if(this.draw==null){
               draw=(DrawFrame)(this.lookUp("DrawFrame"));
			}
            if(draw==null){ // applet ...
        		String figname=null;
        		draw = new DrawFrame();
                draw.editdispatch.setSeparator("/");
                NetworkReader nr=new NetworkReader();
//                String baseDir=nr.getBaseDir(this.getCodeBase().getPath(),"/");
        		draw.setIcons(this.baseDir+"images/");
        		draw.textEditFrame.setIcons(this.baseDir+"images/");
        		draw.clearAll();
        		if(figname==null) System.out.println("null");
        		else {
        			draw.editdispatch.read(figname);
        		}
            	
            }
            draw.setVisible(true); return;
		}
		if(i==this.hideGraphicsButton.getID()){ // hideGraphicsButton
            if(this.draw!=null) 
            this.draw.setVisible(false); 
            return;
		}
		if(i==this.copyButton.getID()){ // copyButton
    		this.copyFromTheText(this.lastManipulatedTextArea); return; 
		}
		if(i==this.cutButton.getID()){ // cutButton
    		this.cutAnAreaOfTheText(this.lastManipulatedTextArea); return;
		}
		if(i==this.pasteButton.getID()){ // pasteButton
    		this.pasteAtTheText(this.lastManipulatedTextArea); return;
		}
		if(i==this.messageClearButton.getID()){ // messageClearButton
		    this.messageArea.setText(""); return;
		}
		if(i==this.evaluationClearButton.getID()){ // evaluationClearButton
		    this.evaluationArea.setText(""); return;
		}
		/*
		if(i== 15){ // clearResultTextButton
		    this.resultArea.setText(""); return;
		}
		*/
		if(i== this.downButton.getID()){ // downButton
		     String downcommand="moveFig(0,100)";
             String rtn1=this.draw.parseCommand(downcommand);
		}
		if(i== this.upButton.getID()){ // upButton
		     String upcommand="moveFig(0,-100)";
             String rtn2=this.draw.parseCommand(upcommand);
		}
		if(i== this.pasteFromSystemButton.getID()){
          if(this.isControlledByLocalUser()){
             this.objectText.paste();
//    	     this.sendEvent("txt.set(0,"+this.textArea.getStrConst());
             this.objectText.ControlledTextArea_textPasted();
		  }
		}
    }

    public void mouseClickedAtTextArea(int i, int p,int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        this.lastManipulatedTextArea=i;
    }

    public void mouseDraggedAtTextArea(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseEnteredAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        sendEvent("btn.enter("+i+")\n");
    }

    public void mouseExitedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        sendEvent("btn.exit("+i+")\n");
    }

    public void mouseIsEnteredAtTheFrame(int x, int y)
    {
        // This method is derived from interface FrameWithControlledFrame
        // to do: code goes here
         sendEvent("frm.ment("+x+","+y+")\n");
    }

    public void mouseIsExitedAtTheFrame(int x, int y)
    {
        // This method is derived from interface FrameWithControlledFrame
        // to do: code goes here
//         sendEvent("frm.mxit("+x+","+y+")\n");
    }

    public void mouseIsMovedAtTheFrame(int x, int y)
    {
        // This method is derived from interface FrameWithControlledFrame
        // to do: code goes here
//       sendEvent("frm.mm("+x+","+y+")\n");
    }

    public void mousePressedAtTextArea(int id, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        this.lastManipulatedTextArea=id;
//        sendEvent("txt.mps("+id+","+p+")\n");
    }

    public void mouseReleasedAtTextArea(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
//        sendEvent("txt.mrl("+id+","+position+")\n");
    }

    public void moveMouseAtTheFrame(int x, int y)
    {
        // This method is derived from interface FrameWithControlledFrame
        // to do: code goes here
    }

    public void pressMouseOnTheText(int id, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
 //       System.out.println("press mouse on the text, id="+id+" p="+p);
        t.pressMouse(p,x,y);
    }

    public void releaseMouseOnTheText(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
        t.releaseMouse(position,x,y);
        
    }

    public void typeKey(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
        t.typeKey(p,key);
    }

    public void unfocusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            ControlledButton button=(ControlledButton)(buttons.elementAt(i));
//            button.controlledButton_mouseExited(null);
            button.unFocus();
    }

    PatternGenerator1 pg;

    public int spaceBetweenVisualizer;

    webleap.Settings settings;

    public int visualizerWidth;

    public int visualizerHeight;

    public Visualizer visualizer2;

    public Visualizer visualizer;

     public UrlTable urlTable;

    public int offsetY;

    public int offsetX;

    public Hashtable searchedPhrases;

   public void repaint()
    {
        // This method is derived from interface MyGui
        // to do: code goes here
//        Graphics g=this.getGraphics();
//        visualizer.repaint();
//        visualizer2.repaint();
    }

    public int minPhraseLength()
    {
        // This method is derived from interface MyGui
        // to do: code goes here
        String s=(String)(this.minPhraseChoice.getSelectedItem());
        int x=(new Integer(s)).intValue();
        return x;
    }

    public int maxPhraseLength()
    {
        // This method is derived from interface MyGui
        // to do: code goes here
        String s=(String)(this.maxPhraseChoice.getSelectedItem());
        int x=(new Integer(s)).intValue();
        return x;
    }

    public Visualizer getVisualizer()
    {
        // This method is derived from interface MyGui
        // to do: code goes here
        return visualizer;
    }

    public UrlTable getUrlTable()
    {
        // This method is derived from interface MyGui
        // to do: code goes here
        return urlTable;
    }

    public Hashtable getSearchedPhrases()
    {
        // This method is derived from interface MyGui
        // to do: code goes here
        return searchedPhrases;
    }

    public JTextArea getResultArea()
    {
        // This method is derived from interface MyGui
        // to do: code goes here
        return null;
    }

    public JTextArea getEvaluationArea()
    {
        // This method is derived from interface MyGui
        // to do: code goes here
        return evaluationArea;
    }

    public void eraseTheHtml()
    {
        // This method is derived from interface MyGui
        // to do: code goes here
    }

    public void addMessage(String s)
    {
        // This method is derived from interface MyGui
        // to do: code goes here
        messageArea.append(s);
        messageArea.setCaretPosition(messageArea.getText().length());
    }
    
	public WebLEAPFrame()
	{
	    panes=new Vector();
	    panes.addElement(this.objectPane);
	    panes.addElement(this.messagePane);
//	    panes.addElement(this.resultPane);
	    panes.addElement(this.evaluationPane);
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
		setTitle("WebLEAP");
		setResizable(false);
		getContentPane().setLayout(null);
		getContentPane().setFont(new Font("SansSerif", Font.PLAIN, 12));
		getContentPane().setBackground(backColor);
		setSize(687,365);
		setVisible(false);
		evaluateButton.setText("eval");
		evaluateButton.setActionCommand("evaluate");
		getContentPane().add(evaluateButton);
		evaluateButton.setBackground(new java.awt.Color(204,220,250));
		evaluateButton.setForeground(java.awt.Color.black);
		evaluateButton.setFont(new Font("Dialog", Font.BOLD, 12));
		evaluateButton.setBounds(12,36,96,24);
		objectClearButton.setActionCommand("clear");
		getContentPane().add(objectClearButton);
		objectClearButton.setBackground(new java.awt.Color(204,220,250));
		objectClearButton.setForeground(java.awt.Color.black);
		objectClearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		objectClearButton.setBounds(204,36,84,24);
		stopButton.setActionCommand("stop");
		getContentPane().add(stopButton);
		stopButton.setBackground(new java.awt.Color(204,220,250));
		stopButton.setForeground(java.awt.Color.black);
		stopButton.setFont(new Font("Dialog", Font.BOLD, 12));
		stopButton.setBounds(108,36,96,24);
		exitButton.setActionCommand("exit");
		getContentPane().add(exitButton);
		exitButton.setBackground(new java.awt.Color(204,220,250));
		exitButton.setForeground(java.awt.Color.black);
		exitButton.setFont(new Font("Dialog", Font.BOLD, 12));
		exitButton.setBounds(564,36,96,24);
		objectPane.setOpaque(true);
		getContentPane().add(objectPane);
		objectPane.setBounds(12,60,648,48);
		objectPane.getViewport().add(objectText);
		objectText.setBackground(java.awt.Color.white);
		objectText.setForeground(java.awt.Color.black);
		objectText.setFont(new Font("SansSerif", Font.PLAIN, 12));
		objectText.setBounds(0,0,644,44);
		getContentPane().add(minPhraseChoice);
		minPhraseChoice.setBounds(528,108,48,24);
		getContentPane().add(maxPhraseChoice);
		maxPhraseChoice.setBounds(612,108,48,24);
		JLabel1.setText("to");
		getContentPane().add(JLabel1);
		JLabel1.setBounds(588,108,24,24);
		resultClearButton.setActionCommand("clear");
		getContentPane().add(resultClearButton);
		resultClearButton.setBackground(new java.awt.Color(204,220,250));
		resultClearButton.setForeground(java.awt.Color.black);
		resultClearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		resultClearButton.setBounds(576,156,84,24);
		downButton.setText("down");
		downButton.setActionCommand("down");
		getContentPane().add(downButton);
		downButton.setBackground(new java.awt.Color(204,220,250));
		downButton.setForeground(java.awt.Color.black);
		downButton.setFont(new Font("Dialog", Font.BOLD, 12));
		downButton.setBounds(300,156,108,24);
		upButton.setText("up");
		upButton.setActionCommand("up");
		getContentPane().add(upButton);
		upButton.setBackground(new java.awt.Color(204,220,250));
		upButton.setForeground(java.awt.Color.black);
		upButton.setFont(new Font("Dialog", Font.BOLD, 12));
		upButton.setBounds(408,156,96,24);
		copyButton.setActionCommand("copy");
		copyButton.setToolTipText("copy");
		getContentPane().add(copyButton);
		copyButton.setBackground(new java.awt.Color(204,220,250));
		copyButton.setForeground(java.awt.Color.black);
		copyButton.setFont(new Font("Dialog", Font.BOLD, 12));
		copyButton.setBounds(12,108,96,24);
		cutButton.setActionCommand("delete");
		cutButton.setToolTipText("cut");
		getContentPane().add(cutButton);
		cutButton.setBackground(new java.awt.Color(204,220,250));
		cutButton.setForeground(java.awt.Color.black);
		cutButton.setFont(new Font("Dialog", Font.BOLD, 12));
		cutButton.setBounds(108,108,96,24);
		pasteButton.setActionCommand("paste");
		pasteButton.setToolTipText("paste");
		getContentPane().add(pasteButton);
		pasteButton.setBackground(new java.awt.Color(204,220,250));
		pasteButton.setForeground(java.awt.Color.black);
		pasteButton.setFont(new Font("Dialog", Font.BOLD, 12));
		pasteButton.setBounds(204,108,96,24);
		clearSearchedButton.setText("clr cache");
		clearSearchedButton.setActionCommand("clr cache");
		getContentPane().add(clearSearchedButton);
		clearSearchedButton.setBackground(new java.awt.Color(204,220,250));
		clearSearchedButton.setForeground(java.awt.Color.black);
		clearSearchedButton.setFont(new Font("Dialog", Font.BOLD, 10));
		clearSearchedButton.setBounds(288,36,96,24);
		JLabel2.setText("message");
		getContentPane().add(JLabel2);
		JLabel2.setBounds(12,192,72,36);
		JLabel3.setText("phrase length");
		getContentPane().add(JLabel3);
		JLabel3.setFont(new Font("Dialog", Font.PLAIN, 10));
		JLabel3.setBounds(444,108,84,24);
		messagePane.setOpaque(true);
		getContentPane().add(messagePane);
		messagePane.setBounds(12,228,336,84);
		messagePane.getViewport().add(messageArea);
		messageArea.setBackground(java.awt.Color.white);
		messageArea.setForeground(java.awt.Color.black);
		messageArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		messageArea.setBounds(0,0,332,80);
		evaluationPane.setOpaque(true);
		getContentPane().add(evaluationPane);
		evaluationPane.setBounds(348,228,312,84);
		evaluationPane.getViewport().add(evaluationArea);
		evaluationArea.setBackground(java.awt.Color.white);
		evaluationArea.setForeground(java.awt.Color.black);
		evaluationArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		evaluationArea.setBounds(0,0,308,80);
		JLabel4.setText("WebLEAP");
		getContentPane().add(JLabel4);
		JLabel4.setBounds(12,0,420,36);
//		JLabel4.setIcon(new ImageIcon("images/webleap-icon.GIF"));
		showSettingsButton.setText("settings");
		showSettingsButton.setActionCommand("settings");
		getContentPane().add(showSettingsButton);
		showSettingsButton.setBackground(new java.awt.Color(204,220,250));
		showSettingsButton.setForeground(java.awt.Color.black);
		showSettingsButton.setFont(new Font("Dialog", Font.BOLD, 10));
		showSettingsButton.setBounds(456,36,108,24);
		showGraphicsButton.setText("show graphics");
		showGraphicsButton.setActionCommand("show graphics");
		getContentPane().add(showGraphicsButton);
		showGraphicsButton.setBackground(new java.awt.Color(204,220,250));
		showGraphicsButton.setForeground(java.awt.Color.black);
		showGraphicsButton.setFont(new Font("Dialog", Font.BOLD, 10));
		showGraphicsButton.setBounds(12,156,144,24);
		hideGraphicsButton.setText("hide graphics");
		hideGraphicsButton.setActionCommand("hide graphics");
		getContentPane().add(hideGraphicsButton);
		hideGraphicsButton.setBackground(new java.awt.Color(204,220,250));
		hideGraphicsButton.setForeground(java.awt.Color.black);
		hideGraphicsButton.setFont(new Font("Dialog", Font.BOLD, 10));
		hideGraphicsButton.setBounds(156,156,144,24);
		messageClearButton.setActionCommand("clear");
		getContentPane().add(messageClearButton);
		messageClearButton.setBackground(new java.awt.Color(204,220,250));
		messageClearButton.setForeground(java.awt.Color.black);
		messageClearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		messageClearButton.setBounds(264,204,84,24);
		evaluationClearButton.setActionCommand("clear");
		getContentPane().add(evaluationClearButton);
		evaluationClearButton.setBackground(new java.awt.Color(204,220,250));
		evaluationClearButton.setForeground(java.awt.Color.black);
		evaluationClearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		evaluationClearButton.setBounds(576,204,84,24);
//		 this.cutButton.setIcon(new ImageIcon("images/cut-icon.GIF"));
//		 this.pasteButton.setIcon(new ImageIcon("images/paste-icon.GIF"));
//		 this.copyButton.setIcon(new ImageIcon("images/copy-icon.GIF"));
		JLabel5.setText("The KWIC(Keyword in context) table is shown when a colored bar in the graphics area is clicked.");
		getContentPane().add(JLabel5);
		JLabel5.setBounds(12,180,636,24);
		pasteFromSystemButton.setText("system");
		pasteFromSystemButton.setActionCommand("paste (system)");
		getContentPane().add(pasteFromSystemButton);
		pasteFromSystemButton.setBackground(new java.awt.Color(204,220,250));
		pasteFromSystemButton.setForeground(java.awt.Color.black);
		pasteFromSystemButton.setFont(new Font("Dialog", Font.BOLD, 10));
		pasteFromSystemButton.setBounds(300,108,132,24);
//		pasteFromSystemButton.setIcon(new ImageIcon("images/paste-icon.GIF"));
//		 this.exitButton.setIcon(new ImageIcon("images/exit-icon.GIF"));
//		 ImageIcon clearIcon=new ImageIcon("images/clear-icon.GIF");
//		ImageIcon clearIcon=new ImageIcon();
//		 this.objectClearButton.setIcon(clearIcon);
//		 this.resultClearButton.setIcon(clearIcon);
//		 this.messageClearButton.setIcon(clearIcon);
//		 this.stopButton.setIcon(new ImageIcon("images/stop2.GIF"));
		//}}

		//{{INIT_MENUS
		//}}
	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		evaluateButton.addActionListener(lSymAction);
		stopButton.addActionListener(lSymAction);
		objectClearButton.addActionListener(lSymAction);
		exitButton.addActionListener(lSymAction);
		clearSearchedButton.addActionListener(lSymAction);
		showSettingsButton.addActionListener(lSymAction);
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		messageClearButton.addActionListener(lSymAction);
		evaluationClearButton.addActionListener(lSymAction);
		showGraphicsButton.addActionListener(lSymAction);
		hideGraphicsButton.addActionListener(lSymAction);
		resultClearButton.addActionListener(lSymAction);
		copyButton.addActionListener(lSymAction);
		cutButton.addActionListener(lSymAction);
		pasteButton.addActionListener(lSymAction);
		downButton.addActionListener(lSymAction);
		upButton.addActionListener(lSymAction);
		SymFocus aSymFocus = new SymFocus();
		this.addFocusListener(aSymFocus);
		pasteFromSystemButton.addActionListener(lSymAction);
		//}}
		
        this.setEnabled(true);
        super.registerListeners();		
		
		this.minPhraseChoice.addItem("1");
		this.minPhraseChoice.addItem("2");
		this.minPhraseChoice.addItem("3");
		this.maxPhraseChoice.addItem("3");
		this.maxPhraseChoice.addItem("4");
		this.maxPhraseChoice.addItem("5");
		this.maxPhraseChoice.addItem("6");
		this.minPhraseChoice.setSelectedItem("2");
		this.maxPhraseChoice.setSelectedItem("4");
		
        offsetY=86;
        offsetX=104;
        spaceBetweenVisualizer=10;

//		settings=new webleap.Settings();
		
		
		buttons=new Vector();
		buttons.addElement(this.evaluateButton);
		buttons.addElement(this.objectClearButton);
		buttons.addElement(this.stopButton);
		buttons.addElement(this.exitButton);
		buttons.addElement(this.resultClearButton);
		buttons.addElement(this.clearSearchedButton);
		buttons.addElement(this.showSettingsButton);
		buttons.addElement(this.showGraphicsButton);
		buttons.addElement(this.hideGraphicsButton);
		buttons.addElement(this.copyButton);
		buttons.addElement(this.cutButton);
		buttons.addElement(this.pasteButton);
		buttons.addElement(this.messageClearButton);
		buttons.addElement(this.evaluationClearButton);
//		buttons.addElement(this.resultTextClearButton);
		buttons.addElement(this.downButton);
		buttons.addElement(this.upButton);
		buttons.addElement(this.pasteFromSystemButton);
		int numberOfButtons=buttons.size();
		for(int i=0;i<numberOfButtons;i++){
		    ControlledButton b=(ControlledButton)(buttons.elementAt(i));
		    b.setFrame(this);
		    b.setID(i);
		}
		
		texts=new Vector();
		texts.addElement(this.objectText);
		texts.addElement(this.messageArea);
//		texts.addElement(this.resultArea);
		texts.addElement(this.evaluationArea);
		int numberOfTexts=texts.size();
		for(int i=0;i<numberOfTexts;i++){
		    ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
		    t.setFrame(this);
		    t.setID(i);
		}

        visualizerWidth=500;
        visualizerHeight=250;

        visualizer = new Visualizer();
//		visualizer.setBounds(offsetX,offsetY,
//		       visualizerWidth,visualizerHeight);
		visualizer.init(this,this.settings);
//		this.getContentPane().add(visualizer);
//        visualizer.repaint();
/*
		visualizer2 = new Visualizer();
		visualizer2.setBounds(offsetX,
		                      visualizerHeight+offsetY+spaceBetweenVisualizer,
		       visualizerWidth, visualizerHeight);
		visualizer2.init(this,this.settings);
		this.getContentPane().add(visualizer2);
*/
		searchedPhrases=new Hashtable();
//		settings.setSearchEngineDriver();
	}
	public void setSettings(Settings s){
		this.settings=s;
	}

	public WebLEAPFrame(String sTitle)
	{
		this();
		setTitle(sTitle);
	
	}

	static public void main(String args[])
	{
	    /*
           DrawFrame draw=new DrawFrame();
           draw.setTitle("WebLEAP/visualizer");
           draw.clearAll();
           draw.setVisible(true);
	       WebLEAPFrame wf=new WebLEAPFrame();
	       wf.clearAll();
	       wf.setVisible(true);
           wf.setTitle("WebLEAP");
           wf.communicationNode=null;
		   wf.urlTable=new UrlTable("url list",null);
           wf.pID=0;
           wf.draw=draw;
           */
           CommunicationNode cn=new CommunicationNode();
           cn.setVisible(true);
           cn.spawnApplication("WebLEAPFrame");
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
//		setSize(insets.left + insets.right + size.width, insets.top + insets.bottom + size.height + menuBarHeight);
	}

	// Used by addNotify
	boolean frameSizeAdjusted = false;

	//{{DECLARE_CONTROLS
	controlledparts.ControlledButton evaluateButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton objectClearButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton stopButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton exitButton = new controlledparts.ControlledButton();
	controlledparts.ControlledScrollPane objectPane = new controlledparts.ControlledScrollPane();
	controlledparts.ControlledTextArea objectText = new controlledparts.ControlledTextArea();
	javax.swing.JComboBox minPhraseChoice = new javax.swing.JComboBox();
	javax.swing.JComboBox maxPhraseChoice = new javax.swing.JComboBox();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	controlledparts.ControlledButton resultClearButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton downButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton upButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton copyButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton cutButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton pasteButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton clearSearchedButton = new controlledparts.ControlledButton();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	controlledparts.ControlledScrollPane messagePane = new controlledparts.ControlledScrollPane();
	controlledparts.ControlledTextArea messageArea = new controlledparts.ControlledTextArea();
	controlledparts.ControlledScrollPane evaluationPane = new controlledparts.ControlledScrollPane();
	controlledparts.ControlledTextArea evaluationArea = new controlledparts.ControlledTextArea();
	javax.swing.JLabel JLabel4 = new javax.swing.JLabel();
	controlledparts.ControlledButton showSettingsButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton showGraphicsButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton hideGraphicsButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton messageClearButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton evaluationClearButton = new controlledparts.ControlledButton();
	javax.swing.JLabel JLabel5 = new javax.swing.JLabel();
	controlledparts.ControlledButton pasteFromSystemButton = new controlledparts.ControlledButton();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == evaluateButton)
				evaluateButton_actionPerformed(event);
			else if (object == stopButton)
				stopButton_actionPerformed(event);
			else if (object == objectClearButton)
				objectClearButton_actionPerformed(event);
			else if (object == exitButton)
				exitButton_actionPerformed(event);
			if (object == clearSearchedButton)
				clearSearchedButton_actionPerformed(event);
			else if (object == showSettingsButton)
				showSettingsButton_actionPerformed(event);
			else if (object == messageClearButton)
				messageClearButton_actionPerformed(event);
			else if (object == evaluationClearButton)
				evaluationClearButton_actionPerformed(event);
			else if (object == showGraphicsButton)
				showGraphicsButton_actionPerformed(event);
			else if (object == hideGraphicsButton)
				hideGraphicsButton_actionPerformed(event);
			else if (object == resultClearButton)
				resultClearButton_actionPerformed(event);
			else if (object == copyButton)
				copyButton_actionPerformed(event);
			else if (object == cutButton)
				cutButton_actionPerformed(event);
			else if (object == pasteButton)
				pasteButton_actionPerformed(event);
			else if (object == downButton)
				downButton_actionPerformed(event);
			else if (object == upButton)
				upButton_actionPerformed(event);
			else if (object == pasteFromSystemButton)
				pasteFromSystemButton_actionPerformed(event);
		}
	}

	void evaluateButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		evaluateButton_actionPerformed_Interaction1(event);
	}

	void evaluateButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			objectText.show();
		} catch (Exception e) {
		}
	}

	void stopButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		stopButton_actionPerformed_Interaction1(event);
	}

	void stopButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			objectText.show();
		} catch (Exception e) {
		}
//		if(pg!=null) pg.stop();
	}

	void objectClearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		objectClearButton_actionPerformed_Interaction1(event);
	}

	void exitButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		exitButton_actionPerformed_Interaction1(event);
	}

	void exitButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.dispose();
		} catch (Exception e) {
		}
//		System.exit(0);
	}

	void clearSearchedButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		clearSearchedButton_actionPerformed_Interaction1(event);
	}

	void clearSearchedButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// clearSearchedButton Show the JButton
//			clearSearchedButton.setVisible(true);
		} catch (Exception e) {
		}
//		searchedPhrases=new Hashtable();
	}

	void showSettingsButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		showSettingsButton_actionPerformed_Interaction1(event);
	}

	void showSettingsButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// SearchEngineSettings Create and show the SearchEngineSettings
//			settings.setVisible(true);
		} catch (Exception e) {
		}
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == WebLEAPFrame.this)
				WebLEAPFrame_windowClosing(event);
		}

	}

	void WebLEAPFrame_windowClosing(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		WebLEAPFrame_windowClosing_Interaction1(event);
	}

	void WebLEAPFrame_windowClosing_Interaction1(java.awt.event.WindowEvent event)
	{
		try {
//			this.show();
		} catch (java.lang.Exception e) {
		}
		this.exitThis();
	}

	void objectClearButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			objectClearButton.show();
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
//			messageArea.setText("");
		} catch (java.lang.Exception e) {
		}
	}

	void evaluationClearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		evaluationClearButton_actionPerformed_Interaction1(event);
	}

	void evaluationClearButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			evaluationArea.setText("");
		} catch (java.lang.Exception e) {
		}
	}

	void showGraphicsButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		showGraphicsButton_actionPerformed_Interaction1(event);
	}

	void showGraphicsButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			java.awt.Frame.getFrames();
		} catch (java.lang.Exception e) {
		}
	}

	void hideGraphicsButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		hideGraphicsButton_actionPerformed_Interaction1(event);
	}

	void hideGraphicsButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			java.awt.Frame.getFrames();
		} catch (java.lang.Exception e) {
		}
	}

	void resultClearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		resultClearButton_actionPerformed_Interaction1(event);
	}

	void resultClearButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			java.awt.Frame.getFrames();
		} catch (java.lang.Exception e) {
		}
	}

	void copyButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		copyButton_actionPerformed_Interaction1(event);
	}

	void copyButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			objectText.getText();
		} catch (java.lang.Exception e) {
		}
	}

	void cutButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		cutButton_actionPerformed_Interaction1(event);
	}

	void cutButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			objectText.getText();
		} catch (java.lang.Exception e) {
		}
	}

	void pasteButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		pasteButton_actionPerformed_Interaction1(event);
	}

	void pasteButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			objectText.getText();
		} catch (java.lang.Exception e) {
		}
	}

	void downButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		downButton_actionPerformed_Interaction1(event);
	}

	void downButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			downButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void upButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		upButton_actionPerformed_Interaction1(event);
	}

	void upButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			upButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	class SymFocus extends java.awt.event.FocusAdapter
	{
		public void focusGained(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == WebLEAPFrame.this)
				WebLEAPFrame_focusGained(event);
		}
	}

	void WebLEAPFrame_focusGained(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
			 
		WebLEAPFrame_focusGained_Interaction1(event);
	}

	void WebLEAPFrame_focusGained_Interaction1(java.awt.event.FocusEvent event)
	{
		try {
			// WebLEAPFrame Request the focus
//			this.requestFocus();
		} catch (java.lang.Exception e) {
		}
		this.focusGained();
	}

	void pasteFromSystemButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		pasteFromSystemButton_actionPerformed_Interaction1(event);
	}

	void pasteFromSystemButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			pasteFromSystemButton.show();
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
	public void pressKey(int i, int p, int code) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
		t.pressKey(p, code);

	}
	String baseDir;
	public void setBaseDir(String d){
		this.baseDir=d;
	}
    public void setIcons(String iconPlace){
		try{
			ImageIcon ic=new ImageIcon(new URL(iconPlace+"clear-icon.GIF"));
	    	if(ic.getImageLoadStatus()==MediaTracker.COMPLETE){
		         this.clearSearchedButton.setIcon(ic);
				 this.objectClearButton.setIcon(ic);
				 this.resultClearButton.setIcon(ic);
				 this.messageClearButton.setIcon(ic);
				 this.evaluationClearButton.setIcon(ic);
		    }
		    else
		    {
			     this.clearSearchedButton.setText("clear");
				 this.objectClearButton.setText("clear");
				 this.resultClearButton.setText("clear");
				 this.messageClearButton.setText("clear");
				 this.evaluationClearButton.setText("clear");
		    }
			ic=new ImageIcon(new URL(iconPlace+"copy-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			  this.copyButton.setIcon(ic);
			else this.copyButton.setText("copy");

			ic=new ImageIcon(new URL(iconPlace+"cut-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			 this.cutButton.setIcon(ic);
			else this.cutButton.setText("cut");
			
            ic=new ImageIcon(new URL(iconPlace+"paste-icon.GIF"));
            if(ic.getImageLoadStatus()==MediaTracker.COMPLETE){
            	this.pasteFromSystemButton.setIcon(ic);
            	this.pasteButton.setIcon(ic);
            }
            else{
            	this.pasteFromSystemButton.setText("paste system");
            	this.pasteButton.setText("paste");
            }
            
            ic=new ImageIcon(new URL(iconPlace+"stop.GIF"));
            if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
            	this.stopButton.setIcon(ic);
            else
            	this.stopButton.setText("stop");
            
			ic=new ImageIcon(new URL(iconPlace+"exit-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			 this.exitButton.setIcon(ic);
			else this.exitButton.setText("exit");

			ic=new ImageIcon(new URL(iconPlace+"webleap-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
				this.JLabel4.setIcon(ic);
			else
				this.JLabel4.setText("webleap");
	    }
		catch(Exception e){
			this.clearSearchedButton.setText("clear");
			this.objectClearButton.setText("clear");
			this.resultClearButton.setText("clear");
			this.messageClearButton.setText("clear");
            this.evaluationClearButton.setText("clear");
		    this.copyButton.setText("copy");
            this.cutButton.setText("cut");
        	this.pasteButton.setText("paste");
            this.pasteFromSystemButton.setText("paste system");
	        this.stopButton.setText("stop");
            this.exitButton.setText("exit");
            this.JLabel4.setText("webleap");
		}

//		this.figSelectFrame.setIcons(iconPlace);
//        this.widthSelectFrame.setIcons(iconPlace);
    }
}