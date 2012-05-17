/*
		A basic implementation of the JFrame class.
*/

package application.draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.Vector;

import javax.swing.*;

import language.ALisp;
import language.HtmlParser;
import language.LispObject;
import nodesystem.*;
import application.texteditor.TextEditFrame;
import controlledparts.*;

import java.net.*;

import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


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
// DrawFrame for DSR/Solar-CATS
public class DrawFrame extends ControlledFrame 
implements DialogListener, FrameWithControlledButton, 
FrameWithControlledFigCanvas, // FrameWithControlledPane, 
FrameWithControlledSlider, FrameWithControlledTextAreas

{
    public void setTextOnTheText(int id, int pos, String s)
    {
        ControlledTextArea t=(ControlledTextArea)(this.textAreas.elementAt(id));
		t.setTextAt(pos, s);
    }

    public void changeStateOnTheSlider(int id, int value)
    {
        // This method is derived from interface FrameWithControlledSlider
        // to do: code goes here
        this.editdispatch.setDepth(value);
        this.depthIndicator.setValueX(value);
    }

    public void enterMouseOnTheSlider(int id)
    {
        // This method is derived from interface FrameWithControlledSlider
        // to do: code goes here
        this.depthIndicator.focus();
    }

    public void exitMouseOnTheSlider(int id)
    {
        // This method is derived from interface FrameWithControlledSlider
        // to do: code goes here
        this.depthIndicator.unFocus();
    }

    Vector states;
    DrawPlayState drawPlayState;
    public DrawEditState drawEditState;

    public Color getBackground()
    {
        // This method is derived from interface FrameWithControlledSlider
        // to do: code goes here
        return null;
    }

    public int getTimeNow()
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
        return super.getTimeNow();
    }

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
        return super.isControlledByLocalUser();
    }

    public void dragMouseOnTheFigCanvas(int id, int x, int y)
    {
        canvas.dragMouse(x,y);
    }

    public Vector canvases;

    public void downKeyOnTheFigCanvas(int id, int key)
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
        canvas.downKey(key);
    }

    public void downMouseOnTheFigCanvas(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
        canvas.downMouse(x,y);
    }

    public void enterMouseOnTheFigCanvas(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
        canvas.enterMouse(x,y);
    }

    public void exitMouseOnTheFigCanvas(int id)
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
        canvas.exitMouse();
    }

    public void keyDownedAtFigCanvas(int id, int key)
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
//            this.canvas.keyDown(key);
    }

    public void mouseDownedAtFigCanvas(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
//        canvas.mouseDown(x,y);
    }

    public void mouseDraggedAtFigCanvas(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
//        canvas.mouseDragged(x,y);
    }

    public void mouseEnteredAtFigCanvas(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
//           canvas.mouseEntered(x,y);
    }

    public void mouseExitedAtFigCanvas(int id)
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
//        canvas.mouseExited();
    }

    public void mouseMovedAtFigCanvas(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
//        canvas.mouseMove(x,y);
    }

    public void mouseUpAtFigCanvas(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
//        this.canvas.mouseUp(x,y);
    }

    public void moveMouseOnTheFigCanvas(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
        this.canvas.moveMouse(x,y);
    }

    public void upMouseOnTheFigCanvas(int id, int x, int y)
    {
        // This method is derived from interface FrameWithControlledFigCanvas
        // to do: code goes here
        canvas.releaseMouse(x,y);
    }
/*
    public boolean isShowingRmouse()
    {
        if(communicationNode==null) return false;
        return this.communicationNode.rmouseIsActivated;
    }
*/
    public void enterMouseOnTheText(int id, int x, int y)
    {
//      ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
//      t.rmouseIsShown=true;
//        this.urlField.moveMouse(x,y);
//        t.rmouse.setVisible(true);
//        t.moveMouse(x,y);
        this.urlField.enterMouse(x,y);
    }
    
    public void exitMouseOnTheText(int id, int x, int y)
    {
        this.urlField.exitMouse();
    }

    public void moveMouseOnTheText(int id, int x, int y)
    {
        this.urlField.moveMouse(x,y);
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
    	textEditFrame=new TextEditFrame();
        textEditFrame.setBounds(this.getInsets().left+100, this.getInsets().right+100,
                             500,400);
        textEditFrame.setIsApplet(this.isApplet());
        textEditFrame.clearAll();
        if(this.communicationNode!=null){
             textEditFrame.setIcons(this.communicationNode.getIconPlace());
             textEditFrame.fileFrame.setSeparator(this.communicationNode.getSeparator());
        }
        JFileChooser chooser=null;
        if(!this.isApplet()){
        	chooser=new JFileChooser();
        	System.out.println("this is not applet");
           try{
               textEditFrame.fileFrame.setFileChooser(chooser);
           }
           catch(Exception e){
        	   System.out.println("at clearAll..."+e.toString());
           }
        }
		this.colorSelectFrame.setCommunicationNode(this.communicationNode);
		this.colorSelectFrame.setWords();
		if(fileFrame!=null){
			this.loadFileFrame.setCommunicationNode(this.communicationNode);
			this.loadFileFrame.setWords();
    		this.fileFrame.setCommunicationNode(this.communicationNode);
	    	this.fileFrame.setWords();
		} 
        this.editdispatch.clear();
//        this.drawEditState.setState(eClearButton.getID());
//        editFunField.setText(eClearButton.getText()); 
//		editdispatch.clear();              
		
		this.figSelectFrame.setState(figSelectFrame.lineButton.getID());
//        figSelectorButton.setText(figSelectFrame.lineButton.getText());
//        figSelectorButton.setIcon(figSelectFrame.lineButton.getIcon());
//        figSelectorButton.repaint();
        editdispatch.newFig(figSelectorButton.getText());
        
        this.colorSelectFrame.setState(colorSelectFrame.blackButton.getID());
//        colorSelectButton.setBackground(colorSelectFrame.blackButton.getBackground());
//        colorSelectButton.setBorder1(colorSelectFrame.bevelBorder1);
//        colorSelectButton.setBorder2(colorSelectFrame.etchedBorder1);
//        colorSelectButton.repaint();
//        editdispatch.changeColor();
        
        this.widthSelectFrame.setState(widthSelectFrame.width1.getID());
//        lineWidthSelectButton.setText(widthSelectFrame.width1.getText());
//        lineWidthSelectButton.setIcon(widthSelectFrame.width1.getIcon());
//        lineWidthSelectButton.repaint();
//        editdispatch.changeWidth();
        
        this.fontSizeSelectFrame.setState(fontSizeSelectFrame.size12.getID());
//        fontSizeSelectButton.setFont(fontSizeSelectFrame.size12.getFont());
//        fontSizeSelectButton.setText(fontSizeSelectFrame.size12.getText());
//        fontSizeSelectButton.setIcon(fontSizeSelectFrame.size8.getIcon());
//        fontSizeSelectButton.repaint();
//        editdispatch.changeFontSize();
        
        this.urlField.setText("");

        this.drawPlayState.setState(this.editButton.getID());
//        canvas.setPlayMode(false);
//        this.editButton.setText("editting");
        if((fileFrame!=null)){
        	if(fileFrame.fileDialog!=null){
              this.fileFrame.fileDialog.clickButton(
                 fileFrame.fileDialog.clearButton.getID());
              
              this.loadFileFrame.fileDialog.clickButton(
                 loadFileFrame.fileDialog.clearButton.getID());
        	}
        }
        this.textEditFrame.clickButton(
             textEditFrame.clearButton.getID());
//
        this.eClearButton.setToolTipText(this.getLclTxt("clear"));
		this.eModifyButton.setToolTipText(this.getLclTxt("modify"));
		this.eCutButton.setToolTipText(this.getLclTxt("cut"));
		this.eSetButton.setToolTipText(this.getLclTxt("set"));
		this.eSetButton.setText(this.getLclTxt("set"));
		this.eSelectButton.setToolTipText(this.getLclTxt("select"));
		this.eMoveButton.setToolTipText(this.getLclTxt("move"));
		this.eCutButton.setToolTipText(this.getLclTxt("cut"));
		this.eRotateButton.setToolTipText(this.getLclTxt("rotate_and_expand"));
        this.eCopyButton.setToolTipText(this.getLclTxt("copy"));
		this.editButton.setToolTipText(this.getLclTxt("editting"));
		this.startPlayButton.setToolTipText(this.getLclTxt("play"));

    }

    public String parseCommand(String s, String appliName,ControlledFrame a){
        if(s.indexOf("addfig ")==0){
            String x=s.substring("addfig ".length());
               BufferedReader dinstream=null;
               try{
                 dinstream=new BufferedReader(
                  new StringReader(x)
                 );
               }
               catch(Exception e){
                  System.out.println("exception:"+e);
               }
               if(dinstream==null) return "error";
               InputQueue iq=new InputQueue(dinstream);
               this.canvas.fs.stop(); 
               ParseFig pf=new ParseFig(iq,this.canvas,null);
               pf.setApplication(appliName,a);
            if(x.indexOf("figures(")==0){
               //
               if(!pf.parseFigures()) return "error";
               this.editdispatch.fs.add(pf.figs);
//               this.editdispatch.fs=pf.figs;
               this.canvas.fs.add(pf.figs);
               this.repaint();
               this.canvas.fs.start();
            }
            else{
               if(!pf.parseFig()) return "error";
               this.editdispatch.fs.add(pf.afig);
//               this.editdispatch.fs=pf.figs;
               this.canvas.fs.add(pf.afig);
               this.repaint();
               this.canvas.fs.start();
            }
            return "";
         }
         if(s.indexOf("btn.click(")==0){
            String x=s.substring("btn.click(".length());
            String ix=x.substring(0,x.indexOf(")"));
            int ii=(new Integer(ix)).intValue();
            this.clickButton(ii);
            return "";
         }
         if(s.indexOf("moveFig(")==0){ // moveFig(x,y)
             int sign=1;
             String sx=s.substring("moveFig(".length());
             String sxx=sx.substring(0,sx.indexOf(","));
             if(sxx.indexOf("-")==0){
                 sign=-1;
                 sxx=sxx.substring("-".length());
             }
             int ix=(new Integer(sxx)).intValue(); ix=sign*ix; 
             String sy=sx.substring(",".length()+1);
             String syy=sy.substring(0,sy.indexOf(")"));
             sign=1;
             if(syy.indexOf("-")==0){
                 sign=-1;
                 syy=syy.substring("-".length());
             }
             int iy=(new Integer(syy)).intValue(); iy=sign*iy;
             this.editdispatch.moveFig(ix,iy);
             this.repaint();
             return "";
         }
         return "";
             	
    }
    public String parseCommand(String s)
    {
    	return this.parseCommand(s, null,null);
    	/*
        if(s.indexOf("addfig ")==0){
           String x=s.substring("addfig ".length());
              BufferedReader dinstream=null;
              try{
                dinstream=new BufferedReader(
                 new StringReader(x)
                );
              }
              catch(Exception e){
                 System.out.println("exception:"+e);
              }
              if(dinstream==null) return "error";
              InputQueue iq=new InputQueue(dinstream);
              this.canvas.fs.stop(); 
              ParseFig pf=new ParseFig(iq,this.canvas,null);
           if(x.indexOf("figures(")==0){
              //
              if(!pf.parseFigures()) return "error";
              this.editdispatch.fs.add(pf.figs);
//              this.editdispatch.fs=pf.figs;
              this.canvas.fs.add(pf.figs);
              this.repaint();
              this.canvas.fs.start();
           }
           else{
              if(!pf.parseFig()) return "error";
              this.editdispatch.fs.add(pf.afig);
//              this.editdispatch.fs=pf.figs;
              this.canvas.fs.add(pf.afig);
              this.repaint();
              this.canvas.fs.start();
           }
           return "";
        }
        if(s.indexOf("btn.click(")==0){
           String x=s.substring("btn.click(".length());
           String ix=x.substring(0,x.indexOf(")"));
           int ii=(new Integer(ix)).intValue();
           this.clickButton(ii);
           return "";
        }
        if(s.indexOf("moveFig(")==0){ // moveFig(x,y)
            int sign=1;
            String sx=s.substring("moveFig(".length());
            String sxx=sx.substring(0,sx.indexOf(","));
            if(sxx.indexOf("-")==0){
                sign=-1;
                sxx=sxx.substring("-".length());
            }
            int ix=(new Integer(sxx)).intValue(); ix=sign*ix; 
            String sy=sx.substring(",".length()+1);
            String syy=sy.substring(0,sy.indexOf(")"));
            sign=1;
            if(syy.indexOf("-")==0){
                sign=-1;
                syy=syy.substring("-".length());
            }
            int iy=(new Integer(syy)).intValue(); iy=sign*iy;
            this.editdispatch.moveFig(ix,iy);
            this.repaint();
            return "";
        }
        return "";
        */
        
    }

    public FigCanvas canvas= new FigCanvas();

    public void paint(Graphics g)
    {
         super.paint(g);
    }
/*
    public void focusGained()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
        // if(sendEventFlag) 
        sendEvent("focus.gain()\n");
    }
*/
/*
    public void focusLost()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
    }
*/
/*
    public void gainFocus()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
        this.requestFocus();
    }
*/
/*
    public void loseFocus()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
    }
*/
    public Vector panes;

    public void pressMouseOnTheText(int id, int p, int x, int y)
    {
        this.urlField.pressMouse(p,x,y);
    }

    public void mousePressedAtTextArea(int id, int p, int x, int y)
    {
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
        // if(sendEventFlag)
//           sendEvent("fc.sb.hidden("+paneID+","+barID+")\n");
    }

    public void scrollBarShown(int paneID, int barID)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
        // if(sendEventFlag)
//           sendEvent("fc.sb.shown("+paneID+","+barID+")\n");
    }

    public void scrollBarValueChanged(int paneID, int barID, int v)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
        // if(sendEventFlag)
//           sendEvent("fc.sb.value("+paneID+","+barID+","+v+")\n");
    }

//    public String encodingCode;

    boolean thisIsShowed;

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
        // if(this.sendEventFlag) 
        sendEvent("file."+m);
    }

    public void whenActionButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
//        if(this.controlMode.equals("receive")) return;
        String dname=d.getDialogName();
        String urlHead="file:";
        String separator=""+System.getProperty("file.separator");
    	editdispatch.setSeparator(separator);
        if(separator.equals("\\"))
                urlHead=urlHead+"///";
            else
                urlHead=urlHead+"//";
        String fileName=d.getText();
        if(dname.equals("input common file name:")){
//               String separator=""+System.getProperty("file.separator");
//               String url=this.communicationNode.getCommonDirPath();
//               url=url+separator+fileName;
//        	   editdispatch.read(url);
               editdispatch.read(urlHead+fileName);
        }
        else
        if(dname.equals("input user file name:")){
//            String separator=""+System.getProperty("file.separator");
//            String url=this.communicationNode.getUserDirPath();
//            url=url+separator+fileName;
//        	editdispatch.read(url);
            editdispatch.read(urlHead+fileName);
        }
        else
        if(dname.equals("output user file name:")){
            String url="";
//    		String fileName=d.getText();
     		File userDataPath=communicationNode.userDataDir;   		
	    	if(!userDataPath.exists()) 
	    	         userDataPath.mkdir();
//	    	File thePath=new File(userDataPath.getPath(),fileName);
//        	editdispatch.save(thePath);
            editdispatch.save(new File(fileName));
            return;
        }
        else
        if(dname.equals("output common file name:")){
            if(!this.communicationNode.getControlMode().equals("teach")) return;
//            File commonDataPath=this.communicationNode.commonDataFigDir;
//	    	File thePath=new File(commonDataPath.getPath(),fileName);
//	    	System.out.println(thePath.getPath());
//        	editdispatch.save(thePath);
            editdispatch.save(new File(fileName));
            return;
        }
        else
        if(dname.equals("url:")){
           
        	int p=fileName.length();
        	String x="";
        	if(fileName.charAt(p-1)=='\\'){
        		x=fileName.substring(0,p-1);
        	}
        	else
        	if(fileName.endsWith("//")){
        		x=fileName.substring(0,p-2);
        	}
        	else{
        		x=fileName;
        	}
        	urlField.setText(x);
        	this.readHtml(x);
//            String url=d.getText();
//        	editdispatch.baseDir=d.getFilePath();
//        	editdispatch.setSeparator("/");
//            editdispatch.read(fileName);
/*
        	NetworkReader nr=new NetworkReader(fileName,this.communicationNode.getEncodingCode());
        	String readingText=nr.loadAndWait();
       // 	editdispatch.read(url);
        	if(readingText.equals("")) return;
        	language.CQueue cq= new language.CQueue();
        	
        	JTextArea t=new JTextArea();
        	language.ALisp lisp=new language.ALisp(t,null,cq,null);
            HtmlParser hp=new HtmlParser(readingText,lisp);
            */
        	
        }
        //
    }

    public void readHtml(String x){
    	NetworkReader nr=null;
    	String code="UTF-8";
    	if(this.communicationNode!=null)
    		code=this.communicationNode.getEncodingCode();
    	nr=new NetworkReader(x,code);
    	String readingText=nr.loadAndWait();
   // 	editdispatch.read(url);
    	if(readingText.equals("")) return;
    	language.CQueue cq= new language.CQueue();
    	JTextArea t=new JTextArea();
    	ALisp lisp=new language.ALisp(t,null,cq,null);
        HtmlParser hp=new HtmlParser(readingText,lisp);
        LispObject htmlObject=hp.getResult();
        if(htmlObject==null) return;
        this.clearAll();
        HtmlEnvironment htmlEnv=new HtmlEnvironment(this,htmlObject,x,lisp);
        htmlEnv.eval();
        String codeX=htmlEnv.getCharSet();
        if(!(codeX.toUpperCase()).equals(code)){
        	nr.stop();
        	nr=new NetworkReader(x,codeX);
        	readingText=nr.loadAndWait();
        	if(readingText.equals("")) return;
        	cq=new language.CQueue();
        	t.setText("");
        	lisp=new language.ALisp(t,null,cq,null);
        	hp=new HtmlParser(readingText, lisp);
        	htmlObject=hp.getResult();
        	if(htmlObject==null) return;
        	this.clearAll();
        	htmlEnv=new HtmlEnvironment(this,htmlObject,x,lisp);
        	htmlEnv.eval();
        }
        this.repaint();
    	this.mouseClickedAtButton(this.startPlayButton.getID());
    }
    
    public String htmlToPage(String newURL)
    {        
       if(this.communicationNode!=null){

       }
       URL url=null;
       try{ url=new URL(newURL); }
       catch(MalformedURLException e)
       {  
//           message.setText("MalformedURLException\n");
           return "MalformedURLException";
       }
       Object x=null;
       try{
           x=url.getContent();
       }
       catch(Exception e){
    	   return e.toString();
       }
       Class xclass=null;
       if(x!=null){
    	   xclass=x.getClass();
       }
       if(xclass!=null){
    	   
       }
       else{
    	   
       }
       return "";
    }
    
    public void whenCancelButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

    public Vector fileDialogs;

    public FileFrame fileFrame;
    public JFileChooser fileChooser;

    public Vector textAreas;

    public void clickMouseOnTheText(int i, int p,int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        this.urlField.clickMouse(p,x,y);
    }

    public void dragMouseOnTheText(int id, int position,int x, int y)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        this.urlField.dragMouse(position,x,y);
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
        this.urlField.releaseMouse(position,x,y);
    }

    public void typeKey(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledTextAreas
        // to do: code goes here
        this.urlField.typeKey(p,key);
    }

    FontSizeSelectFrame fontSizeSelectFrame;

    public void clickButton(int i)
    {
        SelectedButton t=(SelectedButton)(editButtons.elementAt(i));
        t.click();
        boolean tmp=this.communicationNode.directEvent;
        this.communicationNode.directEvent=false;
        this.mouseClickedAtButton(i);
        this.communicationNode.directEvent=tmp;
    }

    public void sliderStateChanged(int id, int v)
    {
        // if(sendEventFlag) 
		this.editdispatch.setDepth(v);
    }

    public void sliderMouseExited(int i)
    {
    }

    public void sliderMouseEntered(int i)
    {
    }

    public void unfocusButton(int i)
    {
            SelectedButton button=(SelectedButton)(editButtons.elementAt(i));
//            button.controlledButton_mouseExited(null);
            button.unFocus();
    }

    public void focusButton(int i)
    {
            SelectedButton button=(SelectedButton)(editButtons.elementAt(i));
//            button.controlledButton_mouseEntered(null);
             button.focus();
    }

    public Vector<SelectedButton> editButtons;

    public void toFront()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
    }

    public void mouseExitedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        // if(sendEventFlag) 
//        sendEvent("draw.btn.exit("+i+")\n");
    }

    public void mouseEnteredAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        // if(sendEventFlag) 
//        sendEvent("draw.btn.enter("+i+")");
    }

    MovieFrame movieFrame=null; 
    
    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        SelectedButton t=(SelectedButton)(editButtons.elementAt(i));
        t.click();
		    if(i==this.figSelectorButton.getID()){ 
		    	if(figSelectFrame==null) return;
		        figSelectFrame.setVisible(true);   return; }                      // figSelectButton
		    if(i==this.colorSelectButton.getID()){
		    	if(colorSelectFrame==null) return;
		    	colorSelectFrame.setVisible(true); return; }                      // colorSelectButton
		    if(i==this.lineWidthSelectButton.getID()){ widthSelectFrame.setVisible(true); return; }                      // lineWidthSelectButton
            if(i==this.fontSizeSelectButton.getID()){fontSizeSelectFrame.setVisible(true); return;}                     // fontSizeSelectButton
            if(i==this.eSetButton.getID()){ this.drawEditState.setState(i);
                      return; }                           // eSetButton
		    if(i==this.eClearButton.getID()){ this.editdispatch.clear();
		              return; }                          // eClearButton
		    if(i==this.eRotateButton.getID()){ this.drawEditState.setState(i);
		              return; }            // eRotateButton
		    if(i==this.eCopyButton.getID()){ this.drawEditState.setState(i);
		              return; }                          // eCopyButton
		    if(i==this.eModifyButton.getID()){ this.drawEditState.setState(i);
		              return; }                     // eModifyButton
		    if(i==this.eMoveButton.getID()){ this.drawEditState.setState(i); 
		              return; }                      // eMoveButton
		    if(i==this.eCutButton.getID()){ this.drawEditState.setState(i);
		               return; }                         // eCutButton
		    if(i==this.eSelectButton.getID()){ this.drawEditState.setState(i);
		               return; }                        // eSelectButton
		    if(i==this.fileButton.getID()){
		    	if(fileFrame==null) return;
		        fileFrame.setCommonPath(
		          new File(this.communicationNode.commonDataDir,"figs").toString());
		        fileFrame.setUserPath(
		          this.communicationNode.userDataDir.toString());
		        fileFrame.setVisible(true);  return;    }       // fileButton
		    if(i==this.closeButton.getID()){                                                 // closeButton
		    
		    //    if(this.thisIsShowed){
		    //        thisIsShowed=false;
		            exitThis(); 
		    //    }
		    //    return; 
		    } 
//		    if(i==14){ return; } 
		    if(i==this.startPlayButton.getID()){
		        this.drawPlayState.setState(i);
		        return;
		    }
		    if(i==this.editButton.getID()){
		        this.editdispatch.changeMode("select");
		        this.drawPlayState.setState(i);
                return;
		    }
		    if(i==this.eSelectAllButton.getID()){  //selectAll
		        this.drawEditState.setState(i);  return;
		    }
		    if(i==this.getImageButton.getID()){
//		    	if(this.isControlledByLocalUser()){
		    	if(this.communicationNode.directEvent){
		    	  this.show(false);
		        	try{
		        		Thread.sleep(200);
		        	}
		        	catch(InterruptedException e){}
		    	  this.getImageFromDisplay();
		    	  this.show(true);
		    	}
		    	return;
		    }
		    if(i==this.sendMovieButton.getID()){
//		    	if(this.isControlledByLocalUser()){
		    	   if(this.movieFrame==null){
		    		   this.movieFrame=new MovieFrame(this);
		    	   }
		    	if(this.communicationNode.directEvent){
		    	   this.show(false);
		    	   this.movieFrame.setVisible(true);
		    	   this.movieFrame.start();
		    	   this.setVisible(false);
		    	}
		    	return;
		    }
    }
    
    AFigure currentBackgroundImage;
    Robot robot=null;
    public void getImageFromDisplay(){
//    	this.editdispatch.clear();
    	/*
        if(!this.communicationNode.sendEventFlag) return;
        */
    	if(!this.isControlledByLocalUser()) return; 
//        this.communicationNode.setStoppingLockTimer(true);
    	Rectangle ro=this.getBounds();
    	Point p=ro.getLocation();
    	int xo=p.x;
    	int yo=p.y;
    	int width=ro.width;
    	int height=ro.height;
    	int dx=88;
    	int dy=125;                            
    	int xn=xo+dx;
    	int yn=yo+dy;
     	int wn=width-dx-20;
    	int hn=height-dy-20;
 
    	ro.setLocation(xn,yn);
    	ro.width=wn;
    	ro.height=hn;
//    	this.show(false);
//    	this.setVisible(false);

    	try{
    		Thread.sleep(10);
    	}
    	catch(InterruptedException e){}
    	try{
    		if(this.robot==null){
    	       this.robot=new Robot();
    		}
    	}
    	catch(Exception e){
    		System.out.println(e.toString());
    	}
    	BufferedImage bi=robot.createScreenCapture(ro);
    	if(this.currentBackgroundImage==null||
    		this.imageManager.getImageOperator("draw-background-0")==null){
    	    this.currentBackgroundImage=new MyImage(this.canvas,bi,"draw-background-0",
    	    		                 this.communicationNode.getNodeSettings());
            currentBackgroundImage.newFig();
            this.canvas.fs.add(currentBackgroundImage);
            this.canvas.fs.setStep(-10);
            currentBackgroundImage.depth=25;
            this.depthIndicator.setValue(currentBackgroundImage.depth);
    	}
    	else{
    		NodeSettings s=this.communicationNode.getNodeSettings();
    		((MyImage)this.currentBackgroundImage).updateImage(bi,s.getInt("pictureSegmentWidth"));
    	}
        if(this.communicationNode.sendEventFlag){
//        	System.out.println("start sending image");
            this.imageManager.start();
        }
//    	this.show(true);
    }
    public boolean isNoMoreSendingImage(){
    	if(this.imageManager.isRunning()){
    		return false;
    	}
    	return true;
    }
    public int getAnswer(String x){
    	return this.communicationNode.getAnswer(x);
    }

    public void dispose()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
        super.dispose();
    }

    public WidthSelectFrame widthSelectFrame;

    public ColorSelectFrame colorSelectFrame;

    public FigSelectFrame figSelectFrame;

    public void openTextEditor(DialogListener l)
    {
        textEditFrame.setListener(l);
        textEditFrame.setVisible(true);
        textEditFrame.setControlledFrame(this);
    }

    public void openImageInputDialog(DialogListener l)
    {
        this.loadFileFrame.setDialogName("imageDialog");
        this.loadFileFrame.setListener(l);
        this.loadFileFrame.setSeparator(this.editdispatch.getSeparator());
        this.loadFileFrame.setFileChooser(this.fileChooser);
        this.loadFileFrame.setCommonPath(
           new File(this.communicationNode.commonDataDir,"images").toString());
        this.loadFileFrame.setUserPath(
           this.communicationNode.userDataDir.toString());
        this.loadFileFrame.setVisible(true);
    }
/*
    public String str2saveable(String str)
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
            else       { sx=sx+c; i++; }
//            System.out.println(sx);
        }
        */
/*
        // Unicode を S-JIS Code に変換
	    byte[]  sjisCode = JavaStringToShiftJISString.convertAll( sx.toCharArray());
        String rtn=new String(sjisCode,0);

        return rtn;
*/
    /*
        return sx;
    }
*/
//t
    public ControlledFrame spawnMain(CommunicationNode cn, String args,int pID,String controlMode)
    {

//           DrawFrame cbf=new DrawFrame("Draw");
           DrawFrame cbf= this;
           cbf.communicationNode=cn;
           cbf.setIcons(cn.getIconPlace());
           FileFrame fileFrame=new FileFrame();
           JFileChooser fc=new JFileChooser();
           fileFrame.setFileChooser(fc);
           fileFrame.setSeparator(cn.getSeparator());
           fileFrame.setListener(cbf);
           cbf.setFileFrame(fileFrame);
           cbf.setFileChooser(fc);
           cbf.pID=pID;
//           cbf.communicationNode.encodingCode=code;
           cbf.setTitle("DrawFrame");
           cbf.ebuff=cn.commandTranceiver.ebuff;
//           cbf.show();
           cbf.setVisible(true);
           cbf.thisIsShowed=true;
           return cbf;

    }
    
    public void setFileChooser(JFileChooser fc){
    	this.fileChooser=fc;
    }
    
    public boolean isSending(){
    	if(this.communicationNode!=null)
    		return this.communicationNode.isSending();
    	else
    		return false;
    }
    
    public void sendAll()
    {
           if(this.communicationNode==null) return;
           if(!communicationNode.isSending()) return;
           TemporalyText tt=new TemporalyText();
           if(canvas.fs!=null)
           if((canvas.fs.fp)!=null){ 
               canvas.fs.sendFig();
           }
           this.sendEvent(editdispatch.getCurrentState());
    }
    public void recordMessage(String s)
    {
        if(communicationNode==null) return;
        communicationNode.eventRecorder.recordMessage("\"DrawFrame\","+s);
    }

    public void receiveEvent(String s)
    {
        if(this.communicationNode==null) return;
//        if(!this.communicationNode.isReceivingEvents) return;
        if(!this.isReceiving()) return;
        BufferedReader dinstream=null;
        try{
            dinstream=new BufferedReader(
               new StringReader(s)
            );
        }
        catch(Exception e){
            System.out.println("exception:"+e);
        }
        if(dinstream==null) return;
            InputQueue iq=new InputQueue(dinstream);
            ParseDrawEvent evParser=new ParseDrawEvent(this,iq);
            evParser.parsingString=s;
            evParser.run();
    }
    Vector mqueue=new Vector();
    public void receiveEvent(AMessage m)
    {
    	String s=m.getHead();
        if(this.communicationNode==null) return;
//        if(!this.communicationNode.isReceivingEvents) return;
        if(!this.isReceiving()) return;
        BufferedReader dinstream=null;
        try{
            dinstream=new BufferedReader(
               new StringReader(s)
            );
        }
        catch(Exception e){
            System.out.println("exception:"+e);
        }
        if(dinstream==null) return;
            InputQueue iq=new InputQueue(dinstream);
            ParseDrawEvent evParser=new ParseDrawEvent(this,iq);
            evParser.setBinary(m.getData());
            evParser.parsingString=s;
            mqueue.add(evParser);
            /*
            if(s.startsWith("img(")){
            	System.out.println("img...");
            }
            */
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
			        ParseDrawEvent p=(ParseDrawEvent)(mqueue.remove(0));
					p.run();
				}
			});
    }    
    public void exitThis()
    {
        this.recordMessage("0,\"exit draw\"");
        this.setVisible(false);
    }
    
    String commandName="drawing";
    public String getCommandName(){
    	return commandName;
    }
    /*
    public void sendEvent(String s)
    {
        if(this.communicationNode==null) return;
        this.communicationNode.sendEvent(commandName,s);
    }
    */
    public void sendEvent(String s)
    {
        if(this.communicationNode==null) return;
        if(!this.isControlledByLocalUser()) return;
//        this.node.sendEvent(commandName,s);
        AMessage m=new AMessage();
		m.setHead(s);
		this.communicationNode.sendEvent(commandName,m);
    }
    public void sendEvent(AMessage m)
    {
    	/*
    	String h=commandName+m.getHead();
		m.setHead(h);
		*/
        if(this.communicationNode==null) return;
        if(!this.isControlledByLocalUser()) return;
		this.communicationNode.sendEvent(commandName,m);    	
    }
    public LoadFileFrame loadFileFrame;

    public Figures ftemp;

    public Image offImg;

    public Figures fs;

    public Graphics offGr;
    
    
    public int wellKnownPort;
    public TextEditFrame textEditFrame;
    public boolean showRMouse;
    public RemoteMouse rmouse;
    public int maxDepth;
    public ImageManager imageManager;
    public EditDispatcher editdispatch;
    public String currentFile;
    public String currentDir;
    public ConnectionReceiver conreceiver;    
    
    public void setIcons(String iconPlace){
		try{
//			ImageIcon ic=new ImageIcon("images/rotate-icon.GIF");
			ImageIcon ic=new ImageIcon(new URL(iconPlace+"rotate-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			eRotateButton.setIcon(ic);
			else
				eRotateButton.setText("rotate");
			ic=new ImageIcon(new URL(iconPlace+"clear-icon.GIF"));
	    	if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
		     eClearButton.setIcon(ic);
		    else
			 eClearButton.setText("clear");
	  		ic=new ImageIcon(new URL(iconPlace+"modyfy-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			     eModifyButton.setIcon(ic);
			else eModifyButton.setText("modify");
			ic=new ImageIcon(new URL(iconPlace+"copy-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			  eCopyButton.setIcon(new ImageIcon("images/copy-icon.GIF"));
			else eCopyButton.setText("copy");
			ic=new ImageIcon(new URL(iconPlace+"move-icon.GIF"));
		    if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
		      eMoveButton.setIcon(ic);
		    else eMoveButton.setText("move");
			ic=new ImageIcon(new URL(iconPlace+"select-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			eSelectButton.setIcon(ic);
			else eSelectButton.setText("select");
			ic=new ImageIcon(new URL(iconPlace+"cut-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			eCutButton.setIcon(ic);
			else eCutButton.setText("cut");
			ic=new ImageIcon(new URL(iconPlace+"draw-icon.GIF"));
			JLabel3.setIcon(ic);
			ic=new ImageIcon(new URL(iconPlace+"file-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			fileButton.setIcon(ic);
			else fileButton.setText("file");
			ic=new ImageIcon(new URL(iconPlace+"play.GIF"));
			startPlayButton.setIcon(ic);
			ic=new ImageIcon(new URL(iconPlace+"exit-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
			 closeButton.setIcon(ic);
			else closeButton.setText("exit");
			ic=new ImageIcon(new URL(iconPlace+"editIcon.GIF"));
			editButton.setIcon(ic);
		}
		catch(Exception e){
			eRotateButton.setText("rotate");
			eClearButton.setText("clear");
			eModifyButton.setText("modify");
 			eCopyButton.setText("copy");
			eMoveButton.setText("move");
			eSelectButton.setText("select");
			eCutButton.setText("cut");
    		fileButton.setText("file");
			closeButton.setText("exit");
		}

		this.figSelectFrame.setIcons(iconPlace);
        this.widthSelectFrame.setIcons(iconPlace);
    }
    
    public void setFileFrame(FileFrame f){
    	this.fileFrame=f;
        if(this.fileFrame!=null)
            loadFileFrame=new LoadFileFrame();
        else
        	loadFileFrame=null;
    }
    
	public DrawFrame()
	{

		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(204,220,250));
		setSize(809,615);
		setVisible(false);
		{
		editFunField.setSelectionColor(new java.awt.Color(204,204,255));
		editFunField.setSelectedTextColor(java.awt.Color.black);
		editFunField.setDisabledTextColor(new java.awt.Color(153,153,153));
		editFunField.setEditable(false);
		editFunField.setCaretColor(java.awt.Color.black);
//		editFunField.setBorder(etchedBorder2);
		getContentPane().add(editFunField);
		editFunField.setBackground(java.awt.Color.lightGray);
		editFunField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		editFunField.setBounds(12,120,72,24);
		}
		{
		JLabel1.setText(" Address(URL/FILE):");
		getContentPane().add(JLabel1);
		JLabel1.setBackground(new java.awt.Color(204,204,204));
		JLabel1.setForeground(new java.awt.Color(102,102,153));
		JLabel1.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel1.setBounds(12,48,144,24);
		}
		{
		eSetButton.setText("set");
		eSetButton.setActionCommand("set");
		eSetButton.setToolTipText("set");
		getContentPane().add(eSetButton);
		eSetButton.setBackground(new java.awt.Color(204,204,204));
		eSetButton.setForeground(java.awt.Color.black);
		eSetButton.setFont(new Font("Dialog", Font.BOLD, 12));
		eSetButton.setBounds(12,336,72,24);
		}
		{
		this.getImageButton.setText("img");
		this.getImageButton.setActionCommand("getImage");
		this.getImageButton.setToolTipText("get image from the display");
		getContentPane().add(this.getImageButton);
		this.getImageButton.setBackground(new Color(204,204,204));
		this.getImageButton.setForeground(Color.black);
		this.getImageButton.setFont(new Font("Dialog",Font.BOLD, 12));
		this.getImageButton.setBounds(12,360,72,24);
		}
		{
			this.sendMovieButton.setText("mov");
			this.sendMovieButton.setActionCommand("sendMovie");
			this.sendMovieButton.setToolTipText("sendMovie from the display");
			getContentPane().add(this.sendMovieButton);
			this.sendMovieButton.setBackground(new Color(204,204,204));
			this.sendMovieButton.setForeground(Color.black);
			this.sendMovieButton.setFont(new Font("Dialog",Font.BOLD, 12));
			this.sendMovieButton.setBounds(12,384,72,24);
			}
		{
		eClearButton.setActionCommand("clear");
//		eClearButton.setToolTipText("clear");
        eClearButton.setToolTipText(this.getLclTxt("clear"));
		getContentPane().add(eClearButton);
		eClearButton.setBackground(new java.awt.Color(204,204,204));
		eClearButton.setForeground(java.awt.Color.black);
		eClearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		eClearButton.setBounds(12,312,72,24);
    	}
		{
		eRotateButton.setActionCommand("rotate");
		eRotateButton.setToolTipText("rotate and expand");
		getContentPane().add(eRotateButton);
		eRotateButton.setBackground(new java.awt.Color(204,204,204));
		eRotateButton.setForeground(java.awt.Color.black);
		eRotateButton.setFont(new Font("Dialog", Font.BOLD, 12));
		eRotateButton.setBounds(12,288,72,24);
		}
		{
		eModifyButton.setActionCommand("modify");
//		eModifyButton.setToolTipText("modify");
        eModifyButton.setToolTipText(this.getLclTxt("modify"));
		getContentPane().add(eModifyButton);
		eModifyButton.setBackground(new java.awt.Color(204,204,204));
		eModifyButton.setForeground(java.awt.Color.black);
		eModifyButton.setFont(new Font("Dialog", Font.BOLD, 12));
		eModifyButton.setBounds(12,264,72,24);
//		ImageIcon ic=new ImageIcon("images/modyfy-icon.GIF");
		}
		{
		eCopyButton.setActionCommand("copy");
//		eCopyButton.setToolTipText("copy");
        eCopyButton.setToolTipText(this.getLclTxt("copy"));
		getContentPane().add(eCopyButton);
		eCopyButton.setBackground(new java.awt.Color(204,204,204));
		eCopyButton.setForeground(java.awt.Color.black);
		eCopyButton.setFont(new Font("Dialog", Font.BOLD, 12));
		eCopyButton.setBounds(12,240,72,24);
		}
		{
		eMoveButton.setActionCommand("move");
//		eMoveButton.setToolTipText("move");
        eMoveButton.setToolTipText(this.getLclTxt("move"));
		getContentPane().add(eMoveButton);
		eMoveButton.setBackground(new java.awt.Color(204,204,204));
		eMoveButton.setForeground(java.awt.Color.black);
		eMoveButton.setFont(new Font("Dialog", Font.BOLD, 12));
		eMoveButton.setBounds(12,216,72,24);
		}
		{
		eCutButton.setActionCommand("cut");
		eCutButton.setToolTipText("cut");
		getContentPane().add(eCutButton);
		eCutButton.setBackground(new java.awt.Color(204,204,204));
		eCutButton.setForeground(java.awt.Color.black);
		eCutButton.setFont(new Font("Dialog", Font.BOLD, 12));
		eCutButton.setBounds(12,192,72,24);
		}
		{
		eSelectButton.setActionCommand("select");
		eSelectButton.setToolTipText("select");
		getContentPane().add(eSelectButton);
		eSelectButton.setBackground(new java.awt.Color(204,204,204));
		eSelectButton.setForeground(java.awt.Color.black);
		eSelectButton.setFont(new Font("Dialog", Font.BOLD, 12));
		eSelectButton.setBounds(12,144,72,24);
		}
		{
		JLabel2.setText("depth:");
		getContentPane().add(JLabel2);
		JLabel2.setBackground(new java.awt.Color(204,204,204));
		JLabel2.setForeground(new java.awt.Color(102,102,153));
		JLabel2.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel2.setBounds(528,72,60,24);
		}
		{
		figSelectorButton.setToolTipText("Select Figure-Primitive");
		getContentPane().add(figSelectorButton);
		figSelectorButton.setBackground(new java.awt.Color(204,204,204));
		figSelectorButton.setForeground(java.awt.Color.black);
		figSelectorButton.setFont(new Font("Dialog", Font.BOLD, 12));
		figSelectorButton.setBounds(84,72,144,24);
		}
		{
		colorSelectButton.setToolTipText("Select Color");
		getContentPane().add(colorSelectButton);
		colorSelectButton.setBackground(new java.awt.Color(204,204,204));
		colorSelectButton.setForeground(java.awt.Color.black);
		colorSelectButton.setFont(new Font("Dialog", Font.BOLD, 12));
		colorSelectButton.setBounds(228,72,96,24);
		}
		{
		lineWidthSelectButton.setToolTipText("Select Line Width");
		getContentPane().add(lineWidthSelectButton);
		lineWidthSelectButton.setBackground(new java.awt.Color(204,204,204));
		lineWidthSelectButton.setForeground(java.awt.Color.black);
		lineWidthSelectButton.setFont(new Font("Dialog", Font.BOLD, 12));
		lineWidthSelectButton.setBounds(324,72,108,24);
		}
		{
		JLabel3.setText("Distributed System Recorder/Draw");
		getContentPane().add(JLabel3);
		JLabel3.setBackground(new java.awt.Color(204,204,204));
		JLabel3.setForeground(new java.awt.Color(102,102,153));
		JLabel3.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel3.setBounds(24,12,396,36);
		}
		{
		fontSizeSelectButton.setToolTipText("select font size");
		getContentPane().add(fontSizeSelectButton);
		fontSizeSelectButton.setBackground(new java.awt.Color(204,204,204));
		fontSizeSelectButton.setForeground(java.awt.Color.black);
		fontSizeSelectButton.setFont(new Font("Dialog", Font.BOLD, 12));
		fontSizeSelectButton.setBounds(432,72,84,24);
		}
		{
		urlField.setSelectionColor(new java.awt.Color(204,204,255));
		urlField.setSelectedTextColor(java.awt.Color.black);
		urlField.setCaretColor(java.awt.Color.black);
		urlField.setBorder(etchedBorder1);
		urlField.setDisabledTextColor(new java.awt.Color(153,153,153));
		getContentPane().add(urlField);
		urlField.setBackground(java.awt.Color.white);
		urlField.setForeground(java.awt.Color.black);
		urlField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		urlField.setBounds(156,48,444,24);
		}
		{
		fileButton.setActionCommand("file");
		fileButton.setToolTipText("file");
		getContentPane().add(fileButton);
		fileButton.setBackground(new java.awt.Color(204,204,204));
		fileButton.setForeground(java.awt.Color.black);
		fileButton.setFont(new Font("Dialog", Font.BOLD, 12));
		fileButton.setBounds(12,72,72,24);
		}
		{
		closeButton.setActionCommand("close");
		closeButton.setToolTipText("close and exit");
		getContentPane().add(closeButton);
		closeButton.setBackground(new java.awt.Color(204,204,204));
		closeButton.setForeground(java.awt.Color.black);
		closeButton.setFont(new Font("Dialog", Font.BOLD, 12));
		closeButton.setBounds(12,96,72,24);
		}
		{
		startPlayButton.setActionCommand("play");
		getContentPane().add(startPlayButton);
		startPlayButton.setBackground(new java.awt.Color(204,204,204));
		startPlayButton.setForeground(java.awt.Color.black);
		startPlayButton.setFont(new Font("Dialog", Font.BOLD, 12));
		startPlayButton.setBounds(600,48,96,24);
		}
		{
		editButton.setActionCommand("edit");
		getContentPane().add(editButton);
		editButton.setBackground(new java.awt.Color(204,204,204));
		editButton.setForeground(java.awt.Color.black);
		editButton.setFont(new Font("Dialog", Font.BOLD, 12));
		editButton.setBounds(696,48,96,24);
		}
		{
		depthIndicator.setToolTipText("Depth");
		getContentPane().add(depthIndicator);
		depthIndicator.setBackground(new java.awt.Color(204,204,204));
		depthIndicator.setForeground(new java.awt.Color(153,153,204));
		depthIndicator.setBounds(588,72,204,24);
		}
		{
		eSelectAllButton.setText("all");
		eSelectAllButton.setActionCommand("select");
		eSelectAllButton.setToolTipText("select all");
		getContentPane().add(eSelectAllButton);
		eSelectAllButton.setBackground(new java.awt.Color(204,204,204));
		eSelectAllButton.setForeground(java.awt.Color.black);
		eSelectAllButton.setFont(new Font("Dialog", Font.BOLD, 12));
		eSelectAllButton.setBounds(12,168,72,24);
		}
		/*
		*/
		//}}

		//{{INIT_MENUS
		//}}

//			this.enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK
//		                | java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK
//		                | java.awt.AWTEvent.KEY_EVENT_MASK);		
	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		fileButton.addActionListener(lSymAction);
		closeButton.addActionListener(lSymAction);
		eSelectButton.addActionListener(lSymAction);
		eCutButton.addActionListener(lSymAction);
		eMoveButton.addActionListener(lSymAction);
		eCopyButton.addActionListener(lSymAction);
		eModifyButton.addActionListener(lSymAction);
		eRotateButton.addActionListener(lSymAction);
		eClearButton.addActionListener(lSymAction);
		eSetButton.addActionListener(lSymAction);
		figSelectorButton.addActionListener(lSymAction);
		colorSelectButton.addActionListener(lSymAction);
		lineWidthSelectButton.addActionListener(lSymAction);
		fontSizeSelectButton.addActionListener(lSymAction);
		startPlayButton.addActionListener(lSymAction);
		editButton.addActionListener(lSymAction);
		this.getImageButton.addActionListener(lSymAction);
		this.sendMovieButton.addActionListener(lSymAction);
		SymChange lSymChange = new SymChange();
		depthIndicator.addChangeListener(lSymChange);
		SymMouse aSymMouse = new SymMouse();
		depthIndicator.addMouseListener(aSymMouse);
		SymKey aSymKey = new SymKey();
		this.addKeyListener(aSymKey);
		eSelectAllButton.addActionListener(lSymAction);
		SymFocus aSymFocus = new SymFocus();
		this.addFocusListener(aSymFocus);
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		//}}

        this.setEnabled(true);
        super.registerListeners();
	           
	    this.drawEditState=new DrawEditState(this);
	    this.drawPlayState=new DrawPlayState(this);

		getContentPane().add(canvas);
		canvas.setBounds(84,98,700,500);
//		canvas.myInit(this);		
        canvas.setFrame(this);
//        canvas.addKeyListener(aSymKey);
		/*
		JLabel dummyLabel2= new JLabel();
		dummyLabel2.setText("jlabel");
		getContentPane().add(dummyLabel2);
		dummyLabel2.setBounds(0,0,800,650);
		dummyLabel2.setVisible(false);
        */
		int w=getSize().width;
		int h=getSize().height;
		                 
        imageManager=new ImageManager(canvas,this);

/*
        textEditFrame=new TextEditFrame();
        textEditFrame.setBounds(this.getInsets().left+100, this.getInsets().right+100,
                             500,400);
*/
        editdispatch=new EditDispatcher(canvas,null,this);
//        editdispatch.newFig("Line");        
        
        figSelectFrame=new FigSelectFrame();
        figSelectFrame.setDraw(this);
        
        colorSelectFrame=new ColorSelectFrame();
        colorSelectFrame.setDraw(this);
        colorSelectButton.setFrame(colorSelectFrame);
        
        widthSelectFrame=new WidthSelectFrame();
        widthSelectFrame.setDraw(this);
        
        fontSizeSelectFrame=new FontSizeSelectFrame();
        fontSizeSelectFrame.setDraw(this);
        
//        fileFrame=new FileFrame();
//        fileFrame.setListener(this);
//        fileFrame.setDraw(this);
        
        editButtons=new Vector();
        editButtons.addElement(figSelectorButton);
        editButtons.addElement(colorSelectButton);
        editButtons.addElement(lineWidthSelectButton);
        editButtons.addElement(fontSizeSelectButton);
        
        editButtons.addElement(eSetButton);
        editButtons.addElement(eClearButton);
        editButtons.addElement(eRotateButton);
        editButtons.addElement(eCopyButton);
        editButtons.addElement(eModifyButton);
        editButtons.addElement(eMoveButton);
        editButtons.addElement(eCutButton);
        editButtons.addElement(eSelectButton);
        editButtons.addElement(fileButton);
        editButtons.addElement(closeButton);
        editButtons.addElement(this.startPlayButton);
        editButtons.addElement(this.editButton);
        editButtons.addElement(this.eSelectAllButton);
        editButtons.addElement(this.getImageButton);
        editButtons.addElement(this.sendMovieButton);
        int numberOfButtons=editButtons.size();
        
        for(int i=0;i<numberOfButtons;i++){
            SelectedButton button=(SelectedButton)(editButtons.elementAt(i));
            button.setFrame(this);
            button.setID(i);
        }
        
        textAreas=new Vector();
        textAreas.addElement(urlField);
        urlField.setFrame(this);
        urlField.setID(0);
        
        panes=new Vector();
        panes.addElement(this.canvas);
        canvases=new Vector();
        canvases.addElement(this.canvas);
        canvas.setFrame(this);
        canvas.setID(0);
        canvas.setPlayMode(false);
        canvas.setEditdispatch(this.editdispatch);
//        this.editButton.setText("editting");
        canvas.setDrawFrame(this);
        
        this.states=new Vector();
        states.addElement(this.drawPlayState);
        states.addElement(this.drawEditState);
        states.addElement(this.figSelectFrame);
        states.addElement(this.colorSelectFrame);
        states.addElement(this.widthSelectFrame);
        states.addElement(this.fontSizeSelectFrame);
        
        this.setVisible(false);
        thisIsShowed=false;
        
        
        DefaultBoundedRangeModel m=new DefaultBoundedRangeModel(0,0,0,30);
        depthIndicator.setModel(m);
	    depthIndicator.setFrame(this);
	    depthIndicator.setID(0);
//	    BoundedRangeModel x=depthIndicator.getModel();
	    
		depthIndicator.setToolTipText("Depth of the figures");
//	    depthIndicator.setMinimum(0);
//		depthIndicator.setMaximum(30);
//		depthIndicator.setValueIsAdjusting(true);
//		depthIndicator.setValue(0);
		
        maxDepth=depthIndicator.getMaximum();
        String separator=""+System.getProperty("file.separator");
    	editdispatch.setSeparator(separator);

        this.show(true);
    	
	}

	public DrawFrame(String sTitle)
	{
		this();
		setTitle(sTitle); 
	}

	static public void main(String args[])
	{
		(new DrawFrame()).setVisible(true);
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
	javax.swing.JTextField editFunField = new javax.swing.JTextField();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	controlledparts.ControlledButton eSetButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton eClearButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton eRotateButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton eModifyButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton eCopyButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton eMoveButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton eCutButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton eSelectButton = new controlledparts.ControlledButton();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	controlledparts.ControlledButton figSelectorButton = new controlledparts.ControlledButton();
	public controlledparts.ControlledButton2 colorSelectButton = new controlledparts.ControlledButton2();
	controlledparts.ControlledButton lineWidthSelectButton = new controlledparts.ControlledButton();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	controlledparts.ControlledButton fontSizeSelectButton = new controlledparts.ControlledButton();
	controlledparts.ControlledTextArea urlField = new controlledparts.ControlledTextArea();
	controlledparts.ControlledButton fileButton = new controlledparts.ControlledButton();
	public controlledparts.ControlledButton closeButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton startPlayButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton editButton = new controlledparts.ControlledButton();
	controlledparts.ControlledSlider depthIndicator = new controlledparts.ControlledSlider();
	controlledparts.ControlledButton eSelectAllButton = new controlledparts.ControlledButton();
    javax.swing.border.EtchedBorder etchedBorder1= new javax.swing.border.EtchedBorder();
    javax.swing.border.EtchedBorder etchedBorder2= new javax.swing.border.EtchedBorder();
	//}}

	//{{DECLARE_MENUS
	//}}

    ControlledButton getImageButton = new ControlledButton();
    ControlledButton sendMovieButton = new ControlledButton();
    

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == fileButton)
				fileButton_actionPerformed(event);
			else if (object == closeButton)
				closeButton_actionPerformed(event);
			else if (object == eSelectButton)
				eSelectButton_actionPerformed(event);
			else if (object == eCutButton)
				eCutButton_actionPerformed(event);
			else if (object == eMoveButton)
				eMoveButton_actionPerformed(event);
			else if (object == eCopyButton)
				eCopyButton_actionPerformed(event);
			else if (object == eModifyButton)
				eModifyButton_actionPerformed(event);
			else if (object == eRotateButton)
				eRotateButton_actionPerformed(event);
			else if (object == eClearButton)
				eClearButton_actionPerformed(event);
			else if (object == eSetButton)
				eSetButton_actionPerformed(event);
			else if (object == figSelectorButton)
				figSelectorButton_actionPerformed(event);
			else if (object == colorSelectButton)
				colorSelectButton_actionPerformed(event);
			else if (object == lineWidthSelectButton)
				lineWidthSelectButton_actionPerformed(event);
			else if (object == fontSizeSelectButton)
				fontSizeSelectButton_actionPerformed(event);
			else if (object == startPlayButton)
				startPlayButton_actionPerformed(event);
			else if (object == editButton)
				editButton_actionPerformed(event);
			else if (object == eSelectAllButton)
				eSelectAllButton_actionPerformed(event);
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
//			fileButton.show();
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
//			closeButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void eSelectButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		eSelectButton_actionPerformed_Interaction1(event);
	}

	void eSelectButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			eSelectButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void eCutButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		eCutButton_actionPerformed_Interaction1(event);
	}

	void eCutButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			eCutButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void eMoveButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		eMoveButton_actionPerformed_Interaction1(event);
	}

	void eMoveButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			eMoveButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void eCopyButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		eCopyButton_actionPerformed_Interaction1(event);
	}

	void eCopyButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			eCopyButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void eModifyButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		eModifyButton_actionPerformed_Interaction1(event);
	}

	void eModifyButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			eModifyButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void eRotateButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		eRotateButton_actionPerformed_Interaction1(event);
	}

	void eRotateButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			eRotateButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void eClearButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		eClearButton_actionPerformed_Interaction1(event);
	}

	void eClearButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			eClearButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void eSetButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		eSetButton_actionPerformed_Interaction1(event);
	}

	void eSetButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			eSetButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void figSelectorButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		figSelectorButton_actionPerformed_Interaction1(event);
	}

	void figSelectorButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			figSelectorButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void colorSelectButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		colorSelectButton_actionPerformed_Interaction1(event);
	}

	void colorSelectButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			colorSelectButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void lineWidthSelectButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		lineWidthSelectButton_actionPerformed_Interaction1(event);
	}

	void lineWidthSelectButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			lineWidthSelectButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void fontSizeSelectButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		fontSizeSelectButton_actionPerformed_Interaction1(event);
	}

	void fontSizeSelectButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			fontSizeSelectButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void startPlayButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		startPlayButton_actionPerformed_Interaction1(event);
	}

	void startPlayButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			startPlayButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void editButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		editButton_actionPerformed_Interaction1(event);
	}

	void editButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			editButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	class SymChange implements javax.swing.event.ChangeListener
	{
		public void stateChanged(javax.swing.event.ChangeEvent event)
		{
			Object object = event.getSource();
			if (object == depthIndicator)
				depthIndicator_stateChanged(event);
		}
	}

	void depthIndicator_stateChanged(javax.swing.event.ChangeEvent event)
	{
		// to do: code goes here.
			 
		depthIndicator_stateChanged_Interaction1(event);
	}

	void depthIndicator_stateChanged_Interaction1(javax.swing.event.ChangeEvent event)
	{
		try {
//			depthIndicator.show();
            depthIndicator.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseExited(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == depthIndicator)
				depthIndicator_mouseExited(event);
		}

		public void mouseEntered(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == depthIndicator)
				depthIndicator_mouseEntered(event);
		}
	}

	void depthIndicator_mouseEntered(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		depthIndicator_mouseEntered_Interaction1(event);
	}

	void depthIndicator_mouseEntered_Interaction1(java.awt.event.MouseEvent event)
	{
		try {
//			depthIndicator.show();
		} catch (java.lang.Exception e) {
		}
	}

	void depthIndicator_mouseExited(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		depthIndicator_mouseExited_Interaction1(event);
	}

	void depthIndicator_mouseExited_Interaction1(java.awt.event.MouseEvent event)
	{
		try {
//			depthIndicator.show();
		} catch (java.lang.Exception e) {
		}
	}

	class SymKey extends java.awt.event.KeyAdapter
	{
		public void keyTyped(java.awt.event.KeyEvent event)
		{
			Object object = event.getSource();
			if (object == DrawFrame.this)
				DrawFrame_keyTyped(event);
		}

		public void keyPressed(java.awt.event.KeyEvent event)
		{
			Object object = event.getSource();
			if (object == DrawFrame.this)
				DrawFrame_keyPressed(event);
		}
	}

	public void DrawFrame_keyPressed(java.awt.event.KeyEvent event)
	{
		// to do: code goes here.
			 
		DrawFrame_keyPressed_Interaction1(event);
	}

	void DrawFrame_keyPressed_Interaction1(java.awt.event.KeyEvent event)
	{
		try {
//			this.setVisible(true);
		} catch (java.lang.Exception e) {
		}
		this.canvas.FigCanvas_keyPressed(event);
	}

	public void DrawFrame_keyTyped(java.awt.event.KeyEvent event)
	{
		// to do: code goes here.
			 
		DrawFrame_keyTyped_Interaction1(event);
	}

	void DrawFrame_keyTyped_Interaction1(java.awt.event.KeyEvent event)
	{
		try {
//			this.setVisible(true);
		} catch (java.lang.Exception e) {
		}
//		System.out.println("key-time-"+event.getKeyChar());
		this.canvas.FigCanvas_keyTyped(event);
	}

	void eSelectAllButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		eSelectAllButton_actionPerformed_Interaction1(event);
	}

	void eSelectAllButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			eSelectAllButton.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	class SymFocus extends java.awt.event.FocusAdapter
	{
		public void focusGained(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == DrawFrame.this){
				DrawFrame_focusGained(event);
				canvas.requestFocusInWindow();
			}
		}
	}

	public void DrawFrame_focusGained(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
			 
		DrawFrame_focusGained_Interaction1(event);
	}

	void DrawFrame_focusGained_Interaction1(java.awt.event.FocusEvent event)
	{
		try {
			// DrawFrame Request the focus
//			this.requestFocus();
		} catch (java.lang.Exception e) {
		}
		this.focusGained();
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowActivated(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == DrawFrame.this)
				DrawFrame_windowActivated(event);
		}
	}

	public void DrawFrame_windowActivated(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		DrawFrame_windowActivated_Interaction1(event);
	}

	void DrawFrame_windowActivated_Interaction1(java.awt.event.WindowEvent event)
	{
		try {
			// DrawFrame Request the focus
//			this.requestFocus();
		} catch (java.lang.Exception e) {
		}
		this.focusGained();
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
		this.urlField.releaseKey(p, code);

	}
	
	public void pressKey(int i, int p, int code){
	}
	
	public void setStoppingLockTimer(boolean b){
		if(communicationNode!=null)
		this.communicationNode.setStoppingLockTimer(b);
	}
    public NodeSettings getNodeSettings(){
    	return this.communicationNode.getNodeSettings();
    }
    Vector<AMessage> keyFrame;
	public Vector<AMessage> getKeyFrame(){
		keyFrame=new Vector();
	    for(int i=0;i<editButtons.size();i++){
	    	try{
	            SelectedButton b=(editButtons.elementAt(i));
	            if(b!=null){
		          Vector<AMessage> ms=b.getKeyFrame(commandName);
		          addKeyFrames(keyFrame, ms);
		        }
	        }
	    	catch(Exception e){
	    		System.out.println(e.toString());
	    	}

	    }
	    Vector<AMessage> canvasKeyFrame=this.canvas.getKeyFrame(this.commandName);
	    addKeyFrames(keyFrame,canvasKeyFrame);
		return keyFrame;
	}
}