/*
		A basic implementation of the JFrame class.
*/

package application.basic;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;

import language.CQueue;
import language.LispObject;
import language.ReadLine;
import nodesystem.*;
import application.draw.DrawFrame;
import controlledparts.*;

import javax.swing.*;


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
public class BasicFrame extends controlledparts.ControlledFrame 
implements controlledparts.FrameWithControlledButton, 
            controlledparts.FrameWithControlledCheckBox, 
            controlledparts.FrameWithControlledPane, 
            controlledparts.FrameWithControlledTextAreas, 
             nodesystem.DialogListener,
             FrameWithLanguageProcessor
{
    private Vector checkBoxes;

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

    public void changeStateCheckBox(int i, int x)
    {
        // This method is derived from interface controlledparts.FrameWithControlledCheckBox
        // to do: code goes here
        ControlledCheckBox b=(ControlledCheckBox)(checkBoxes.elementAt(i));
        b.changeState(x);
        this.stateChangedAtCheckBox(i,x);
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

    public void setTextOnTheText(int id, int pos,String s)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
        t.setTextAt(pos,s);
    }

    public boolean stopFlag;

    public void resetStopFlag()
    {
        this.stopFlag=false;
    }

    public boolean stopFlagIsOn()
    {
        // This method is derived from interface language.GuiWithControlFlag
        // to do: code goes here
        return this.stopFlag;
    }

    public boolean traceFlagIsOn()
    {
        // This method is derived from interface language.GuiWithControlFlag
        // to do: code goes here
        return this.traceFlag.isSelected();
    }

    public boolean stopFlagIsSelected()
    {
        // This method is derived from interface language.GuiWithStopFlag
        // to do: code goes here
        return false;
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
        return this.communicationNode.rmouseIsActivated;
    }
*/
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

    public void moveMouseOnTheText(int id, int x, int y)
    {
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
        //   t.rmouse.move(x,y);
        t.moveMouse(x,y);
    }

    public void mouseMoveAtTextArea(int id, int x, int y)
    {
    }
    public void mouseEnteredAtTheText(int id, int x, int y)
    {
    }
    public void mouseExitAtTheText(int id, int x, int y)
    {
    }
    

    public void clearAll()
    {
	    exampleButton.setText(((ControlledButton)(this.basicExampleListFrame.buttons.elementAt(0))).getText());
	    this.currentExample=(String)(basicExampleListFrame.programs.elementAt(0));
	    this.edittingArea.setText("");
	    this.inputArea.setText("");
	    this.outputArea.setText("");
	    
	    this.fileFrame.setCommunicationNode(this.communicationNode);
	    this.fileFrame.setWords();
	    if(!this.isApplet())
	       this.fileFrame.setFileChooser(new JFileChooser());
	    this.enterButton.setToolTipText(getLclTxt("enter"));
    }


    public String parseCommand(String x)
    {
        /*
        command:
           add-program <statement>
           clear-program
           set-command <command>
           clear-command
           clear-output
//           add <i> <string>
//           click <i>
           run
           enter
           exit
           
        */
    	if(x.startsWith("ex ")){
            String st=x.substring("ex ".length());
    		this.inputArea.setText(st);
       		this.basic.setWaitingFunction(st);
    		this.clickButton(this.enterButton.getID());
    		return basic.waitForResult(st);
    	}

        if(x.indexOf("add-program ")==0){
            String st=x.substring("add-program ".length());
            this.edittingArea.append(st+"\n");
            return "";
        }
        if(x.indexOf("clear-program ")==0){
            String st=x.substring("clear-program ".length());
            this.edittingArea.setText("");
            return "";
        }
        if(x.indexOf("set-command ")==0){
            String st=x.substring("add-command ".length());
            this.inputArea.setText(st);
            return "";
        }
        if(x.indexOf("clear-command ")==0){
            String st=x.substring("clear-command ".length());
            this.inputArea.setText("");
            return "";
        }
        if(x.indexOf("run")==0){
            this.clickButton(6);
            return "";
        }
        if(x.indexOf("enter")==0){
            this.clickButton(8);
            return "";
        }
        if(x.indexOf("exit")==0){
            this.clickButton(1);
            return "";
        }
        if(x.indexOf("call ")==0){
        	String f=x.substring("call ".length());
        	String q="? "+f;
        	this.inputArea.setText(q);
        	this.enter();
        	
        }
        return null;
    }

    public boolean traceFlagIsSelected()
    {
        return this.traceFlag.isSelected();
    }

    public void mouseIsMovedAtTheFrame(int x, int y)
    {
       sendEvent("frm.mm("+x+","+y+")\n");
    }

    public void mouseIsExitedAtTheFrame(int x, int y)
    {
   }

    public void mouseIsEnteredAtTheFrame(int x, int y)
    {
         sendEvent("frm.ment("+x+","+y+")\n");
   }

    public boolean graphicsIsShown;

    public void saveText(String urlname)
    {
        ControlledTextArea mes=this.outputArea;
        if(urlname==null) return;
        int startTime=this.communicationNode.eventRecorder.timer.getMilliTime();
        mes.append("saving "+urlname+"\n");

        FileOutputStream fouts=null;

        try{ fouts= new FileOutputStream(new File(urlname));}
        catch(FileNotFoundException e){
            mes.append("wrong directory:"+urlname+" ?\n");
        }
        /*
        catch(IOException e){ mes.append("cannot access"+urlname+".\n");}
        */
        String outx=this.edittingArea.getText();
//        byte[] buff=new byte[outx.length()];
        byte[] buff=null;
        try{
          buff=outx.getBytes(this.communicationNode.encodingCode);
        }
        catch(java.io.UnsupportedEncodingException e){
            mes.append("exception:"+e);
        }
//        outx.getBytes(0,outx.length(),buff,0);

        try{
            fouts.write(buff);
            fouts.flush();
        }
        catch(IOException e)
           { mes.append("save:IOExceptin while flushing.\n");}
           
        try{ fouts.close();  }
        catch(IOException e)
           { mes.append("save:IOException while closing.\n");}

        mes.append("Saving done.\n");
        int endTime=this.communicationNode.eventRecorder.timer.getMilliTime();
        int savingtime=endTime-startTime;
        double length=(double)(outx.length());
        String savingSpeed="";
        if(savingtime>0){
            savingSpeed=""+(length*1000)/savingtime;
        }
        else savingSpeed="(na)";
        this.recordMessage(""+startTime+
        ",\"savefig("+urlname+") \","+savingtime+
        ","+length+","+savingSpeed);
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
//        if(this.sendEventFlag) sendEvent("file."+m);
          sendEvent("file."+m);
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
//        	urlField.setText(fileName);
            this.loadProgram(url);
            return;
        }
        if(dname.equals("input user file name:")){
            String url="";
    		String fileName=d.getText();
    		File userDataPath=draw.communicationNode.userDataDir;
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
//        	urlField.setText(fileName);
        	this.loadProgram(url);
            return;
        }
        if(dname.equals("output user file name:")){
            String url="";
    		String fileName=d.getText();
     		File userDataPath=draw.communicationNode.userDataDir;   		
	    	if(!userDataPath.exists()) 
	    	         userDataPath.mkdir();
//	    	File thePath=new File(userDataPath.getPath(),fileName);
//        	urlField.setText(fileName);
//        	this.saveText(thePath.getPath());
            this.saveText(fileName);
            return;
        }
        if(dname.equals("url:")){
            String url=d.getText();
//        	urlField.setText(url);
        	this.loadProgram(url);
            return;
        }
		else
		if(dname.equals("output common file name:")){
			if(!this.communicationNode.getControlMode().equals("teach")) return;
			String url="";
			String fileName=d.getText();
//			  File commonDataPath=this.communicationNode.commonDataFigDir;
//			File thePath=new File(commonDataPath.getPath(),fileName);
//			System.out.println(thePath.getPath());
//			editdispatch.save(thePath);
			this.saveText(fileName);
			return;
		}    }

    public void whenCancelButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

    public int lastManipulatedTextArea;

    public FileFrame fileFrame;

    public void pasteAtTheText(int i)
    {
            String theText="";
            String newText="";
            int startPosition=0;
            int endPosition=0;
            ControlledTextArea ta=(ControlledTextArea)(this.texts.elementAt(i));
//            ControlledScrollPane p=(ControlledScrollPane)(this.panes.elementAt(i));
//            ControlledScrollBar vs=(ControlledScrollBar)(p.scrollBars.elementAt(0));
//            int vsv=vs.getValue();
//            ControlledScrollBar hs=(ControlledScrollBar)(p.scrollBars.elementAt(1));
//            int hsv=hs.getValue();
            startPosition=ta.getSelectionStart();
            theText=ta.getText();
//            startPosition=ta.getSelectionStart();
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
//            vs.setValue(vsv);
//            hs.setValue(hsv);
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

    public Vector panes;

    public void pressMouseOnTheText(int id, int p, int x, int y)
    {
         ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
         t.pressMouse(p,x,y);
         this.lastManipulatedTextArea=id;
   }

    public void mousePressedAtTextArea(int id, int p, int x, int y)
    {
        this.lastManipulatedTextArea=id;
//        sendEvent("txt.mps("+id+","+p+")\n");
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

    public void scrollBarHidden(int paneID, int barID)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
  //      sendEvent("sb.hidden("+paneID+","+barID+")\n");
    }

    public void scrollBarShown(int paneID, int barID)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
//        sendEvent("sb.shown("+paneID+","+barID+")\n");
    }

    public void scrollBarValueChanged(int paneID, int barID, int v)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
//        sendEvent("sb.value("+paneID+","+barID+","+v+")\n");
    }
    public void showhideDraw()
    {
    }

    private String encodingCode;

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
//        sendEvent("txt.mrl("+id+","+position+")\n");
    }

    public void mouseDraggedAtTextArea(int id, int position, int x, int y)
    {
//        sendEvent("txt.mdg("+id+","+position+")\n");
    }

    void enter()
    {
  
		//{{CONNECTION
		// TextArea にﾌｫｰｶｽを要求する
//		textInput.requestFocus();
		//}}
		String input=inputArea.getText();
		outputArea.append(input+"\n");
//		outputArea.setCaretPosition(outputArea.getText().length());
		inqueue.putString(input);
		inputArea.setText("");
		inputArea.repaint();
/*
//		lisp.evals(inqueue);
*/
		LispObject o=((ReadLine)(basic.read)).readProgram(inqueue);
        LispObject p=basic.basicparser.parseBasic(o);
        basic.evalList(p);
//   		outputArea.setCaretPosition(outputArea.getText().length());
//        outputArea.repaint();
  }

    void loadExample()
    {
            String urlname="";
	    	File thePath=new File(getDefaultPath(),currentExample);
            String separator=""+System.getProperty("file.separator");
            if(separator.equals("\\"))
            	urlname="file:///"+thePath.getPath();
            else
                urlname="file://"+thePath.getPath();
        	System.out.println("url="+urlname);

		loadProgram( urlname );

    }

    void programRun()
    {
 		try {
//			edittingArea.show();
		} catch (Exception e) {
		}
		this.stopFlag=false;
		String input=edittingArea.getText();
//		textOutput.appendText("> "+input+"\n");
		inqueue.putString(input);
        basic.clearEnvironment();
//        graphicsArea.clear();
		LispObject o=((ReadLine)(basic.read)).readProgram(inqueue);
        LispObject p=basic.basicparser.parseBasic(o);
        basic.evalList(p);
//        outputArea.repaint();
//        outputArea.setCaretPosition(outputArea.getText().length());
   }

    String commandName="basic";
    public String getCommandName(){
    	return commandName;
    }
    public void sendEvent(String s)
    {
        if(this.communicationNode==null) return;
        this.communicationNode.sendEvent(commandName,s);
    }

    public Vector texts;

    public void clickMouseOnTheText(int i, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        this.lastManipulatedTextArea=i;
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
        t.clickMouse(p,x,y);
    }

    public void keyIsTypedAtATextArea(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
    }
    
    public void keyIsPressedAtATextArea(int i, int p, int key){
    }

    public void mouseClickedAtTextArea(int i, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        this.lastManipulatedTextArea=i;
    }

    public void typeKey(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
        t.typeKey(p,key);
        
    }

    public void clickButton(int i)
    {
        ControlledButton t=(ControlledButton)(buttons.elementAt(i));
        t.click();
        this.mouseClickedAtButton(i);
    }

    public void unfocusButton(int i)
    {
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//            button.controlledButton_mouseExited(null);
            button.unFocus();
   }

    public void focusButton(int i)
    {
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//            button.controlledButton_mouseEntered(null);
            button.focus();
    }

    String currentExample;

    BasicExampleListFrame basicExampleListFrame;

    public File getDefaultPath()
    {
        File rtn=this.communicationNode.dsrRoot;
        return rtn;
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
        // to do: code goes herevoid mouseClickedAtButton(int )
/*
	buttons.addElement( fileButton);
	buttons.addElement( exitButton );
	buttons.addElement( editorClearButton );
	buttons.addElement( this.copyButton );
	buttons.addElement( this.cutButton );
	buttons.addElement( this.pasteButton );
	buttons.addElement( runButton );
	buttons.addElement( loadExampleButton );
	buttons.addElement( enterButton );
	buttons.addElement( exampleButton );
	buttons.addElement( outputClearButton );
	buttons.addElement( inputClearButton );
	buttons.addElement( graphicsButton);
    buttons.addElement( hideButton);
    buttons.addElement( stopButton);
*/
        if(i==0){          
            fileFrame.setListener(this);
            fileFrame.setSeparator(System.getProperty("file.separator"));
            fileFrame.setCommonPath(
               this.communicationNode.commonDataDir.toString());
            fileFrame.setUserPath(
               this.communicationNode.userDataDir.toString());
            fileFrame.setVisible(true); return;} // fileButton
        if(i==1){exitThis(); return;} // exitButton
        if(i==2){edittingArea.setText(""); return;} // editorClearButton
        if(i==3){ this.copyFromTheText(this.lastManipulatedTextArea); return; } // copyButton
        if(i==4){ this.cutAnAreaOfTheText(this.lastManipulatedTextArea); return; } // cutButton
        if(i==5){ this.pasteAtTheText(this.lastManipulatedTextArea); return; } // pasteButton
        if(i==6){programRun(); return; } // runButton
        if(i==7){loadExample(); return;} // loadExampleButton
        if(i==8){enter(); return;} // enterButton
        if(i==9){basicExampleListFrame.setVisible(true); return;} // exampleButton
        if(i==10){outputArea.setText(""); return;} // outputClearButton
        if(i==11){inputArea.setText(""); return;} // inputClearButton
        if(i==12){ 
            draw=(DrawFrame)(this.lookUp("DrawFrame"));
            draw.setVisible(true);
            return;
         } // (show) graphicsButton
        if(i==13){
            if(this.draw!=null) 
            this.draw.setVisible(false);
            return;
        } //  hideButton
        if(i==this.stopButton.getID()){
            this.stopFlag=true;
        }

    }

    public void dispose()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
    }

    public DrawFrame draw;

    private Basic basic;

    String str2saveable(String str)
    {
        StringBuffer s=new StringBuffer(str);
        String sx="";
        int i=0;
        int len=s.length();
        while(i<len){
            char c=s.charAt(i);
            if(c=='\''){
                sx=sx+'\\'; sx=sx+c; i++; }
            else
            if(c=='\"'){
                sx=sx+'\\'; sx=sx+c; i++; }
            else
            if(c=='\\'){sx=sx+'\\'; sx=sx+c; i++;
                        c=s.charAt(i); sx=sx+c; i++; }
            else
            if((int)c==10){
                sx=sx+"\\n"; i++;
                c=s.charAt(i);
                if((int)c==13) i++;
                }
            else
            if((int)c==13){
                sx=sx+"\\n"; i++;
                c=s.charAt(i);
                if((int)c==10) i++;}
            else
            { sx=sx+c; i++; }
//            System.out.println(sx);
        }
/*
        // Unicode を S-JIS Code に変換
	    byte[]  sjisCode = JavaStringToShiftJISString.convertAll( sx.toCharArray());
        String rtn=new String(sjisCode,0);

        return rtn;
*/
        return sx;
    }

    public void toFront()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
        super.toFront();
    }

    public void sendAll()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
        int maxTextNumber=texts.size();
        for(int i=0;i<maxTextNumber;i++){
            ControlledTextArea t=(ControlledTextArea)(this.texts.elementAt(i));
            t.ControlledTextArea_textPasted();
        }
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
            ParseBasicEvent evParser=new ParseBasicEvent(this,iq);
            evParser.parsingString=s;
            evParser.run();
    }

    public void exitThis()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
//        System.out.println("basicframe exithis");
        this.setVisible(false);
//         hide();         // Frame を非表示にする.
    }

    public ControlledFrame spawnMain(CommunicationNode cn,String args,int pID, String controlMode)
    {
           BasicFrame bf=this;
           bf.communicationNode=cn;
           bf.pID=pID;
//           bf.encodingCode=code;
           bf.setTitle("BasicFrame");
		
		// spawn Basic Slaves at other nodes in this group.
           bf.ebuff=cn.commandTranceiver.ebuff;

//           bf.show();
           bf.setVisible(true);
           bf.draw=(DrawFrame)(this.lookUp("DrawFrame"));
           bf.draw.setVisible(false);
		   bf.graphicsIsShown=false;
           return bf;
    }

    void loadProgram(String urlname)
    {
        if(!(this.communicationNode.isControlledByLocalUser()||this.communicationNode.isReadableFromEachFile())) return;
         BufferedReader inputStream=null;
        URL  url;
        String codingCode=this.communicationNode.getEncodingCode();
        ControlledTextArea mes=this.outputArea;

        int startTime=this.communicationNode.eventRecorder.timer.getMilliTime();
        if(urlname==null){
            mes.append("URL Name is expected.\n");
            return;
        }
        mes.append("reading "+urlname+"\n");
        try{ url=new URL(urlname); }
        catch(MalformedURLException e)
        {  mes.append("MalformedURLException\n");
           return;
        }

        try{
            inputStream=//new DataInputStream(url.openStream());
                    new BufferedReader(
                          new InputStreamReader(url.openStream(),codingCode));
        }
        catch(UnsupportedEncodingException e){
             mes.append("encoding unsupported.\n");
            return;
       }
        catch(IOException e){
            mes.append("url open error.\n");
            return;
        }

       this.edittingArea.setText("");
       int tlength=0;
       String x=null;
       do{
          x=null;
          try{
            x=inputStream.readLine();
          }
          catch(java.io.IOException e){  break;}
          if(x!=null){
             x=x+"\n";
             tlength=tlength+x.length();
             this.edittingArea.append(x);
          }
       } while(x!=null);
  
        try{ inputStream.close();  }
        catch(IOException e)
           { mes.append("read:IOException while closing.\n");}
        mes.append("reading done.\n");
        int endTime=this.communicationNode.eventRecorder.timer.getMilliTime();
        int readingtime=endTime-startTime;
        String readingSpeed="";
        if(readingtime>0){
            readingSpeed=""+(((double)(tlength))*1000)/readingtime;
        }
        else readingSpeed="(na)";
//        npMain.recordMessage("-"+startTime+":readfig("+urlname+") done.");
        this.recordMessage(""+startTime+
        ",\"readfig("+urlname+") \""+","+readingtime+
        ","+tlength+","+readingSpeed);

        this.sendAll();

    }

	public BasicFrame()
	{
	    
    	panes=new Vector();
	    panes.addElement(this.editPane);
	    panes.addElement(this.inputPane);
	    panes.addElement(this.outputPane);
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
		setTitle("Basic");
		setResizable(false);
		getContentPane().setLayout(null);
		setSize(467,533);
		setVisible(false);
		editPane.setOpaque(true);
		getContentPane().add(editPane);
		editPane.setBounds(96,36,348,240);
		edittingArea.setSelectionColor(new java.awt.Color(204,204,255));
		edittingArea.setSelectedTextColor(java.awt.Color.black);
		edittingArea.setCaretColor(java.awt.Color.black);
		edittingArea.setDisabledTextColor(new java.awt.Color(153,153,153));
		editPane.getViewport().add(edittingArea);
		edittingArea.setBackground(java.awt.Color.white);
		edittingArea.setForeground(java.awt.Color.black);
		edittingArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		edittingArea.setBounds(0,0,344,236);
		{
		fileButton.setToolTipText("file load, save");
		fileButton.setActionCommand("Load");
		getContentPane().add(fileButton);
		fileButton.setBackground(new java.awt.Color(204,204,204));
		fileButton.setForeground(java.awt.Color.black);
		fileButton.setFont(new Font("Dialog", Font.BOLD, 12));
		fileButton.setBounds(12,36,84,24);
		fileButton.setIcon(new ImageIcon("images/file-icon.GIF"));
		}
		{
		editorClearButton.setToolTipText("clear edit pane");
		editorClearButton.setActionCommand("Clear");
		getContentPane().add(editorClearButton);
		editorClearButton.setBackground(new java.awt.Color(204,204,204));
		editorClearButton.setForeground(java.awt.Color.black);
		editorClearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		editorClearButton.setBounds(12,156,84,24);
		editorClearButton.setIcon(new ImageIcon("images/clear-icon.GIF"));
		}
		{
		runButton.setToolTipText("run this program");
		runButton.setActionCommand("run");
		getContentPane().add(runButton);
		runButton.setBackground(new java.awt.Color(204,204,204));
		runButton.setForeground(java.awt.Color.black);
		runButton.setFont(new Font("Dialog", Font.BOLD, 12));
		runButton.setBounds(12,180,84,24);
		runButton.setIcon(new ImageIcon("images/play.gif"));
		}
		loadExampleButton.setToolTipText("load the following example program");
		loadExampleButton.setText("load ex.");
		loadExampleButton.setActionCommand("load ex.");
		getContentPane().add(loadExampleButton);
		loadExampleButton.setBackground(new java.awt.Color(204,204,204));
		loadExampleButton.setForeground(java.awt.Color.black);
		loadExampleButton.setFont(new Font("Dialog", Font.BOLD, 12));
		loadExampleButton.setBounds(12,228,84,24);
		{
		JLabel1.setText("Distributed System Recorder/ A Procedural Programming Environment");
		getContentPane().add(JLabel1);
		JLabel1.setBackground(new java.awt.Color(204,204,204));
		JLabel1.setForeground(new java.awt.Color(102,102,153));
		JLabel1.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel1.setBounds(12,0,456,36);
		JLabel1.setIcon(new ImageIcon("images/basic-icon.GIF"));
		}
		outputPane.setOpaque(true);
		getContentPane().add(outputPane);
		outputPane.setBounds(96,276,348,108);
		outputArea.setSelectionColor(new java.awt.Color(204,204,255));
		outputArea.setSelectedTextColor(java.awt.Color.black);
		outputArea.setCaretColor(java.awt.Color.black);
		outputArea.setDisabledTextColor(new java.awt.Color(153,153,153));
		outputPane.getViewport().add(outputArea);
		outputArea.setBackground(java.awt.Color.white);
		outputArea.setForeground(java.awt.Color.black);
		outputArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		outputArea.setBounds(0,0,344,104);
		inputPane.setOpaque(true);
		getContentPane().add(inputPane);
		inputPane.setBounds(96,384,276,84);
		inputArea.setSelectionColor(new java.awt.Color(204,204,255));
		inputArea.setSelectedTextColor(java.awt.Color.black);
		inputArea.setCaretColor(java.awt.Color.black);
		inputArea.setDisabledTextColor(new java.awt.Color(153,153,153));
		inputPane.getViewport().add(inputArea);
		inputArea.setBackground(java.awt.Color.white);
		inputArea.setForeground(java.awt.Color.black);
		inputArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		inputArea.setBounds(0,0,272,80);
		JLabel2.setText("Output");
		getContentPane().add(JLabel2);
		JLabel2.setBackground(new java.awt.Color(204,204,204));
		JLabel2.setForeground(new java.awt.Color(102,102,153));
		JLabel2.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel2.setBounds(24,276,84,48);
		JLabel3.setText("Input");
		getContentPane().add(JLabel3);
		JLabel3.setBackground(new java.awt.Color(204,204,204));
		JLabel3.setForeground(new java.awt.Color(102,102,153));
		JLabel3.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel3.setBounds(24,384,84,60);
		{
//		enterButton.setText("Enter");
		enterButton.setActionCommand("Enter");
		getContentPane().add(enterButton);
		enterButton.setBackground(new java.awt.Color(204,204,204));
		enterButton.setForeground(java.awt.Color.black);
		enterButton.setFont(new Font("Dialog", Font.BOLD, 12));
		enterButton.setBounds(372,384,72,84);
		enterButton.setIcon(new ImageIcon("images/enter-icon.GIF"));
		}
		{
		exitButton.setToolTipText("exit and close");
		exitButton.setActionCommand("Exit");
		getContentPane().add(exitButton);
		exitButton.setBackground(new java.awt.Color(204,204,204));
		exitButton.setForeground(java.awt.Color.black);
		exitButton.setFont(new Font("Dialog", Font.BOLD, 12));
		exitButton.setBounds(12,60,84,24);
		exitButton.setIcon(new ImageIcon("images/exit-icon.GIF"));
		}
		exampleButton.setToolTipText("select an example program");
		exampleButton.setText("controlledButton");
		exampleButton.setActionCommand("controlledButton");
		getContentPane().add(exampleButton);
		exampleButton.setBackground(new java.awt.Color(204,204,204));
		exampleButton.setForeground(java.awt.Color.black);
		exampleButton.setFont(new Font("Dialog", Font.BOLD, 12));
		exampleButton.setBounds(12,252,84,24);
		{
		outputClearButton.setActionCommand("clear");
		outputClearButton.setToolTipText("clear output region");
		getContentPane().add(outputClearButton);
		outputClearButton.setBackground(new java.awt.Color(204,204,204));
		outputClearButton.setForeground(java.awt.Color.black);
		outputClearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		outputClearButton.setBounds(12,336,84,24);
		outputClearButton.setIcon(new ImageIcon("images/clear-icon.GIF"));
		}
		{
		inputClearButton.setActionCommand("clear");
		inputClearButton.setToolTipText("clear input region");
		getContentPane().add(inputClearButton);
		inputClearButton.setBackground(new java.awt.Color(204,204,204));
		inputClearButton.setForeground(java.awt.Color.black);
		inputClearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		inputClearButton.setBounds(12,444,84,24);
		inputClearButton.setIcon(new ImageIcon("images/clear-icon.GIF"));
		}
		graphicsButton.setText("Show Graphics");
		graphicsButton.setActionCommand("Show/Hide Graphics Frame");
		getContentPane().add(graphicsButton);
		graphicsButton.setBackground(new java.awt.Color(204,204,204));
		graphicsButton.setForeground(java.awt.Color.black);
		graphicsButton.setFont(new Font("Dialog", Font.BOLD, 12));
		graphicsButton.setBounds(108,468,144,24);
		{
		copyButton.setActionCommand("copy");
		copyButton.setToolTipText("copy from selected(colored) text to temporary place");
		getContentPane().add(copyButton);
		copyButton.setBackground(new java.awt.Color(204,204,204));
		copyButton.setForeground(java.awt.Color.black);
		copyButton.setFont(new Font("Dialog", Font.BOLD, 12));
		copyButton.setBounds(12,84,84,24);
		copyButton.setIcon(new ImageIcon("images/copy-icon.GIF"));
		}
		{
		cutButton.setActionCommand("cut");
		cutButton.setToolTipText("cut the selected(colored) area");
		getContentPane().add(cutButton);
		cutButton.setBackground(new java.awt.Color(204,204,204));
		cutButton.setForeground(java.awt.Color.black);
		cutButton.setFont(new Font("Dialog", Font.BOLD, 12));
		cutButton.setBounds(12,108,84,24);
		cutButton.setIcon(new ImageIcon("images/cut-icon.GIF"));
		}
		{
		pasteButton.setActionCommand("paste");
		pasteButton.setToolTipText("paste from temporary place");
		getContentPane().add(pasteButton);
		pasteButton.setBackground(new java.awt.Color(204,204,204));
		pasteButton.setForeground(java.awt.Color.black);
		pasteButton.setFont(new Font("Dialog", Font.BOLD, 12));
		pasteButton.setBounds(12,132,84,24);
		pasteButton.setIcon(new ImageIcon("images/paste-icon.GIF"));
		}
		hideButton.setText("Hide Grahics");
		hideButton.setActionCommand("Hide Grahics");
		getContentPane().add(hideButton);
		hideButton.setBackground(new java.awt.Color(204,204,204));
		hideButton.setForeground(java.awt.Color.black);
		hideButton.setFont(new Font("Dialog", Font.BOLD, 12));
		hideButton.setBounds(252,468,120,24);
		 {
		stopButton.setActionCommand("stop");
		stopButton.setToolTipText("stop the running program");
		getContentPane().add(stopButton);
		stopButton.setBackground(new java.awt.Color(204,204,204));
		stopButton.setForeground(java.awt.Color.black);
		stopButton.setFont(new Font("Dialog", Font.BOLD, 12));
		stopButton.setBounds(12,204,84,24);
		stopButton.setIcon(new ImageIcon("images/stop2.GIF"));
		 }
		traceFlag.setText("trace");
		traceFlag.setActionCommand("trace");
		getContentPane().add(traceFlag);
		traceFlag.setBackground(new java.awt.Color(204,204,204));
		traceFlag.setForeground(java.awt.Color.black);
		traceFlag.setFont(new Font("Dialog", Font.BOLD, 12));
		traceFlag.setBounds(12,468,84,24);
		//}}

		//{{INIT_MENUS
		//}}
/*	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		editorClearButton.addActionListener(lSymAction);
		loadExampleButton.addActionListener(lSymAction);
		runButton.addActionListener(lSymAction);
		fileButton.addActionListener(lSymAction);
		enterButton.addActionListener(lSymAction);
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		exitButton.addActionListener(lSymAction);
		exampleButton.addActionListener(lSymAction);
		outputClearButton.addActionListener(lSymAction);
		inputClearButton.addActionListener(lSymAction);
		graphicsButton.addActionListener(lSymAction);
		cutButton.addActionListener(lSymAction);
		copyButton.addActionListener(lSymAction);
		pasteButton.addActionListener(lSymAction);
		SymComponent aSymComponent = new SymComponent();
		this.addComponentListener(aSymComponent);
		SymFocus aSymFocus = new SymFocus();
		this.addFocusListener(aSymFocus);
		hideButton.addActionListener(lSymAction);
		stopButton.addActionListener(lSymAction);
		SymItem lSymItem = new SymItem();
		traceFlag.addItemListener(lSymItem);
		//}}
*/
    this.setEnabled(true);
    super.registerListeners();
    inqueue=new CQueue();
	basic=new Basic(inputArea,outputArea,inqueue,this);
		
    basicExampleListFrame=new BasicExampleListFrame();
	basicExampleListFrame.setFrame(this);
		
	fileFrame=new FileFrame("basic file");
	
	buttons=new Vector();
	buttons.addElement( fileButton);
	buttons.addElement( exitButton );
	buttons.addElement( editorClearButton );
	buttons.addElement( this.copyButton );
	buttons.addElement( this.cutButton );
	buttons.addElement( this.pasteButton );
	buttons.addElement( runButton );
	buttons.addElement( loadExampleButton );
	buttons.addElement( enterButton );
	buttons.addElement( exampleButton );
	buttons.addElement( outputClearButton );
	buttons.addElement( inputClearButton );
	buttons.addElement( graphicsButton);
	buttons.addElement( this.hideButton);
	buttons.addElement( this.stopButton);
	
	int numberOfButtons=buttons.size();
	
	for(int i=0;i<numberOfButtons;i++){
	    SelectedButton b=(SelectedButton)(buttons.elementAt(i));
	    b.setFrame(this);
	    b.setID(i);
	}
	
	texts=new Vector();
	texts.addElement(this.edittingArea);
	texts.addElement(this.inputArea);
	texts.addElement(this.outputArea);
	
	int numberOfTextAreas=texts.size();
	for(int i=0;i<numberOfTextAreas;i++){
	    ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
	    t.setFrame(this);
	    t.setID(i);
	}
	this.stopFlag=false;
	
	checkBoxes=new Vector();
	checkBoxes.addElement(this.traceFlag);
	int numberOfCheckBox=checkBoxes.size();
	for(int i=0;i<numberOfCheckBox;i++){
	    ControlledCheckBox t=(ControlledCheckBox)(checkBoxes.elementAt(i));
	    t.setFrame(this);
	    t.setID(i);
	}
	
	
//	    this.setVisible(true);
//		basic.start();
		
	}

	public BasicFrame(String sTitle)
	{
		this();
		setTitle(sTitle);
	}

	static public void main(String args[])
	{
		(new BasicFrame()).setVisible(true);
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
	controlledparts.ControlledScrollPane editPane = new controlledparts.ControlledScrollPane();
	controlledparts.ControlledTextArea edittingArea = new controlledparts.ControlledTextArea();
	controlledparts.ControlledButton fileButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton editorClearButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton runButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton loadExampleButton = new controlledparts.ControlledButton();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	controlledparts.ControlledScrollPane outputPane = new controlledparts.ControlledScrollPane();
	controlledparts.ControlledTextArea outputArea = new controlledparts.ControlledTextArea();
	controlledparts.ControlledScrollPane inputPane = new controlledparts.ControlledScrollPane();
	controlledparts.ControlledTextArea inputArea = new controlledparts.ControlledTextArea();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	controlledparts.ControlledButton enterButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton exitButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton exampleButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton outputClearButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton inputClearButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton graphicsButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton copyButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton cutButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton pasteButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton hideButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton stopButton = new controlledparts.ControlledButton();
	controlledparts.ControlledCheckBox traceFlag = new controlledparts.ControlledCheckBox();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == editorClearButton)
				editorClearButton_actionPerformed(event);
			else if (object == loadExampleButton)
				loadExampleButton_actionPerformed(event);
			else if (object == runButton)
				runButton_actionPerformed(event);
			else if (object == fileButton)
				fileButton_actionPerformed(event);
			else if (object == enterButton)           // *
				enterButton_actionPerformed(event);
			else if (object == exampleButton)
				exampleButton_actionPerformed(event);
			else if (object == outputClearButton)
				outputClearButton_actionPerformed(event);
			else if (object == inputClearButton)
				inputClearButton_actionPerformed(event);
			else if (object == graphicsButton)
				graphicsButton_actionPerformed(event);
			else if (object == cutButton)
				cutButton_actionPerformed(event);
			else if (object == copyButton)
				copyButton_actionPerformed(event);
			else if (object == pasteButton)
				pasteButton_actionPerformed(event);
			else if (object == hideButton)
				hideButton_actionPerformed(event);
			else if (object == stopButton)
				stopButton_actionPerformed(event);
			else if (object == exitButton)
				exitButton_actionPerformed(event);
		}
	}

	void editorClearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		editorClearButton_actionPerformed_Interaction1(event);
	}

	void editorClearButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			edittingArea.setText("");
		} catch (Exception e) {
		}
	}

	void loadExampleButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		loadExampleButton_actionPerformed_Interaction1(event);
	}

	void loadExampleButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	}

	void runButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		runButton_actionPerformed_Interaction1(event);
	}

	void runButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	}

	void fileButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		fileButton_actionPerformed_Interaction1(event);
	}

	void fileButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			edittingArea.show();
		} catch (Exception e) {
		}
	}

	void enterButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		enterButton_actionPerformed_Interaction1(event);
	}

	void enterButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{

	}
    public void recordMessage(String s)
    {
//        String sending="message(\""+str2saveable(s)+"\")";
        /*
        if(ebuff!=null){
            ebuff.putS("local","drawing ",sending, 3);
        }
        */
        if(communicationNode==null) return;
        communicationNode.eventRecorder.recordMessage("\"BasicFrame\","+s);
    }
    private EventBuffer ebuff;
    private int pID;
    private CQueue inqueue;
    private Vector buttons;
//    public BasicCanvas graphicsArea;

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowActivated(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == BasicFrame.this)
				BasicFrame_windowActivated(event);
		}
/*
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == BasicFrame.this)
				BasicFrame_windowClosing(event);
		}
		*/
	}

	void BasicFrame_windowClosing(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		BasicFrame_windowClosing_Interaction1(event);
	}

	void BasicFrame_windowClosing_Interaction1(java.awt.event.WindowEvent event)
	{
		try {
			this.exitThis();
		} catch (Exception e) {
		}
	}

	void exampleButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		exampleButton_actionPerformed_Interaction1(event);
	}

	void exampleButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			exampleButton.show();
		} catch (Exception e) {
		}
//		basicExampleListFrame.show();
	}

	void outputClearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		outputClearButton_actionPerformed_Interaction1(event);
	}

	void outputClearButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			outputArea.setText("");
		} catch (Exception e) {
		}
	}

	void inputClearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		inputClearButton_actionPerformed_Interaction1(event);
	}

	void inputClearButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			inputArea.setText("");
		} catch (Exception e) {
		}
	}

	void graphicsButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		graphicsButton_actionPerformed_Interaction1(event);
	}

	void graphicsButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// graphicsButton Show the ControlledButton
//			graphicsButton.setVisible(true);
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
			// cutButton Show the ControlledButton
//			cutButton.setVisible(true);
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
			// copyButton Show the ControlledButton
//			copyButton.setVisible(true);
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
			// pasteButton Show the ControlledButton
			pasteButton.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	class SymComponent extends java.awt.event.ComponentAdapter
	{
		public void componentHidden(java.awt.event.ComponentEvent event)
		{
			Object object = event.getSource();
			if (object == BasicFrame.this)
				BasicFrame_componentHidden(event);
		}

		public void componentResized(java.awt.event.ComponentEvent event)
		{
			Object object = event.getSource();
			if (object == BasicFrame.this)
				BasicFrame_componentResized(event);
		}
	}

	void BasicFrame_componentResized(java.awt.event.ComponentEvent event)
	{
		// to do: code goes here.
			 
		BasicFrame_componentResized_Interaction1(event);
	}

	void BasicFrame_componentResized_Interaction1(java.awt.event.ComponentEvent event)
	{
		try {
			// BasicFrame Show the BasicFrame
//			this.setVisible(true);
		} catch (java.lang.Exception e) {
		}
//		System.out.println("basic frame resized");
	}

	void BasicFrame_componentHidden(java.awt.event.ComponentEvent event)
	{
		// to do: code goes here.
			 
		BasicFrame_componentHidden_Interaction1(event);
	}

	void BasicFrame_componentHidden_Interaction1(java.awt.event.ComponentEvent event)
	{
		try {
			// BasicFrame Show the BasicFrame
//			this.setVisible(true);
		} catch (java.lang.Exception e) {
		}
//		System.out.println("basic frame hidden");
	}

	class SymFocus extends java.awt.event.FocusAdapter
	{
		public void focusLost(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == BasicFrame.this)
				BasicFrame_focusLost(event);
		}

		public void focusGained(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == BasicFrame.this)
				BasicFrame_focusGained(event);
		}
	}

	void BasicFrame_focusGained(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
			 
		BasicFrame_focusGained_Interaction1(event);			 
	}

	void BasicFrame_focusGained_Interaction1(java.awt.event.FocusEvent event)
	{
		try {
			// BasicFrame Show the BasicFrame
//			this.setVisible(true);
		} catch (java.lang.Exception e) {
		}
//		System.out.println("basic frame focus gained");
        this.focusGained();
	}

	void BasicFrame_focusLost(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
			 
		BasicFrame_focusLost_Interaction1(event);
	}

	void BasicFrame_focusLost_Interaction1(java.awt.event.FocusEvent event)
	{
		try {
			// BasicFrame Show the BasicFrame
//			this.setVisible(true);
		} catch (java.lang.Exception e) {
		}
//		System.out.println("basic frame focus lost");
	}

	void BasicFrame_windowActivated(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		BasicFrame_windowActivated_Interaction1(event);
	}

	void BasicFrame_windowActivated_Interaction1(java.awt.event.WindowEvent event)
	{
		try {
			this.focusGained();
		} catch (java.lang.Exception e) {
		}
	}

	void hideButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		hideButton_actionPerformed_Interaction1(event);
	}

	void hideButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			hideButton.setVisible(true);
		} catch (java.lang.Exception e) {
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
			// stopButton Show the ControlledButton
//			stopButton.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	void exitButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		exitButton_actionPerformed_Interaction1(event);
	}

	void exitButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			exitButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if (object == traceFlag)
				traceFlag_itemStateChanged(event);
		}
	}

	void traceFlag_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
			 
		traceFlag_itemStateChanged_Interaction1(event);
	}

	void traceFlag_itemStateChanged_Interaction1(java.awt.event.ItemEvent event)
	{
		try {
//			traceFlag.show();
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

	public DrawFrame getDrawFrame() {
        if(this.draw==null) {
            DrawFrame d=(DrawFrame)((this.communicationNode.applicationManager.getRunningApplication("DrawFrame")).application);
            if(d==null) return null;
            this.draw=d;
        }
		return this.draw;		// TODO 自動生成されたメソッド・スタブ
	}

}