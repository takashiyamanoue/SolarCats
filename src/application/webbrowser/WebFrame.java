/*
		A basic implementation of the JFrame class.
*/
package application.webbrowser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font; 
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import nodesystem.*;
import application.draw.DrawFrame;
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
public class WebFrame extends controlledparts.ControlledFrame implements 
//  Runnable,
  controlledparts.FrameWithControlledButton, 
  controlledparts.FrameWithControlledEditPane, 
  controlledparts.FrameWithControlledPane, 
  controlledparts.FrameWithControlledTextAreas,
  nodesystem.DialogListener
{

    public int loadingStartTime;

//    boolean pageIsLoading;

    public void pageLoadingDone(int i)
    {
        URL url=this.pageArea.getPage();
        String urlName=url.toString();        
        int endTime=0;
        if(this.communicationNode!=null)
               endTime=this.communicationNode.eventRecorder.timer.getMilliTime();
        String readingSpeed="";
        int time=endTime-this.loadingStartTime;
        this.message.setText(""+time+" msec. for loading "+urlName);
        this.recordMessage(""+this.loadingStartTime+
//            ",\"load("+currentURL+") \""+","+readingtime*0.1+
            ",\"loadingDone("+urlName+") \""+","+time+
            ", ,");
        if(this.communicationNode!=null){
            this.communicationNode.releaseGuiLock("web-pl");
        }
//        this.pageIsLoading=false;
//        this.setEnableToLoad(true);
    }

    public void setKeywordsLabel(String txt)
    {
        this.keywordsLabel.setText(txt);
    }

    public Vector texts2;

    public void clickMouseOnTheText(int i, int p, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts2.elementAt(i));
        t.clickMouse(p,x,y);
    }

    public void dragMouseOnTheText(int id, int position, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts2.elementAt(id));
        t.dragMouse(position,x,y);
    }

    public void enterMouseOnTheText(int id, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts2.elementAt(id));
        t.enterMouse(x,y);
    }

    public void exitMouseOnTheText(int id, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts2.elementAt(id));
        t.exitMouse();
    }

    public boolean isDirectOperation()
    {
        // This method is derived from interface controlledparts.FrameWithControlledButton
        // to do: code goes here
        return super.isDirectOperation();
    }

    public void keyIsTypedAtATextArea(int i, int p, int key)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
    }
	public void keyIsPressedAtATextArea(int i, int p, int key)
	{
		// This method is derived from interface controlledparts.FrameWithControlledTextAreas
		// to do: code goes here
	}

    public void mouseClickedAtTextArea(int i, int p, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseDraggedAtTextArea(int id, int position, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseEnteredAtTheText(int id, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseExitAtTheText(int id, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseMoveAtTextArea(int id, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mousePressedAtTextArea(int i, int p, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void mouseReleasedAtTextArea(int id, int position, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void moveMouseOnTheText(int id, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts2.elementAt(id));
        t.moveMouse(x,y);
    }

    public void pressMouseOnTheText(int i, int p, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts2.elementAt(i));
        t.pressMouse(p,x,y);
    }

    public void releaseMouseOnTheText(int id, int position, int x, int y)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
         ControlledTextArea t=(ControlledTextArea)(texts2.elementAt(id));
        t.releaseMouse(position,x,y);
    }

    public void setTextOnTheText(int i, int pos, String s)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
    }

    public void typeKey(int i, int p, int key)
    {
        // This method is derived from interface controlledparts.FrameWithControlledTextAreas
        // to do: code goes here
        ControlledTextArea t=(ControlledTextArea)(texts2.elementAt(i));
        t.typeKey(p,key);
    }

    // synchronized 
    Vector mqueue=new Vector();
    void loadPage(String newURL)
    {        
       if(this.communicationNode!=null){
        /*
           this.communicationNode.releaseGuiLock();
           while(this.communicationNode.isGuiLockOccupied()){
               try{
                   Thread.sleep(17);
               }
               catch(InterruptedException e){
                     this.message.setText("error.");
               }
           }
         */
       }
//	   this.pageIsLoading=true;

       this.pageArea.init();
//       this.pageArea.setText("<html></html>");
       this.loadingStartTime=0; 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
    	       message.setText("loading now....");
    	   }
       });
       if(this.communicationNode!=null)
           loadingStartTime=this.communicationNode.eventRecorder.timer.getMilliTime();
//       System.out.println("after getting time");
       URL url=null;
       try{ url=new URL(newURL); }
       catch(MalformedURLException e)
       {  
           message.setText("MalformedURLException\n");
           return;
       }
 	   mqueue.add(newURL);
  		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
		           String x=(String)(mqueue.remove(0));
       try{      
//                load(url.openStream(),this.encodingCode);
           pageArea.setContentType(getMimeType(languageCodeButton.getText()));
           pageArea.setEditable(false);
//                pageArea.setPage(currentURL);
//           System.out.println("before");
           pageArea.setPage(x);
//           System.out.println("after");
       }
       catch(java.lang.IllegalArgumentException e){
           pageArea.setText("error:"+e+"\n");
           message.setText("loading failed.");
           pageArea.init();
       }
       catch(Exception e){
//		        this.pageArea.setText("coudn't get "+currentURL+"\n");
	       pageArea.setText("coudn't get "+x+"\n");
           message.setText("loading failed.");
    	   pageArea.init();
       }
			}
   		});
       /*
       while(this.pageIsLoading){
          try{
            Thread.sleep(7);
          }
          catch(InterruptedException e){}
       }
       */
       this.message.setText("loading done.");
  //          stop();
       int endTime=0;
       if(this.communicationNode!=null)
           endTime=this.communicationNode.eventRecorder.timer.getMilliTime();
       int readingtime=endTime-loadingStartTime;
       String readingSpeed="";
            /*
            if(readingtime>0){
                readingSpeed=""+10.0*((double)(tlength))/readingtime;
            }
            else readingSpeed="(na)";
            */
       this.recordMessage(""+loadingStartTime+
//            ",\"load("+currentURL+") \""+","+readingtime*0.1+
            ",\"load("+newURL+") \""+","+readingtime+
            ", ,");
/*            
       try{
          Thread.sleep(150);
       }
       catch(InterruptedException e){}
*/       
    }

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledEditPane
        // to do: code goes here
        return super.isControlledByLocalUser();
    }

    public void clearAll()
    {
//        this.stop();
//        this.setEnableToLoad(true);
		this.languageCodeButton.setText(
		  ((ControlledButton)(this.languageCodeFrame.buttons.elementAt(3))).getText()
		); //JISAutoDetect

//        this.urlArea.setText("");
//        this.urlArea.setEditable(true);
//        this.pageArea.setEditable(false);
//        this.pageArea.setText("");
//        try{
//           this.pageArea.setEditable(false);
           String cdd=this.communicationNode.commonDataDir.toString();
           String sep=""+System.getProperty("file.separator");
           String initPage="";
           String urlHead="file:";
           if(sep.equals("\\")){
                urlHead=urlHead+"///";
           }
           else {
                urlHead=urlHead+"//";
           }
           initPage=urlHead+cdd+sep+"html"+sep+"dsr.html";
           /*
           this.pageArea.setText(
           "<html><body bgcolor=\"white\" text=\"black\"></body></html>");
           */
//           this.pageArea.setPage(initPage);
           this.startLoading(initPage);
//        }
//        catch(Exception e){
//                this.message.setText("initialize failed.");
//        }
        this.pointerToNextUrl=0;
		//
		
    }
/*
    public boolean isShowingRmouse()
    {
        return this.communicationNode.rmouseIsActivated;
    }
*/
    DrawFrame draw;
    Graphics offScreenGraphics;
    Image offScreenImage;

    public void updateHyperLink(int id, String url)
    {
          this.startLoading(url);
    }

    public String str2saveable(String s)
    {
        String sx="";
        int i=0;
        int len=s.length();
        while(i<len){
            char c=s.charAt(i);
//            System.out.println("c="+c+":"+(int)c);
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

        // Unicode を S-JIS Code に変換
//	    byte[]  sjisCode = JavaStringToShiftJISString.convertAll( sx.toCharArray());
//        String rtn=new String(sjisCode,0);
        String rtn=sx;

        return rtn;
    }

    public String mimeType;

    public String getMimeType(String type)
    {
        this.mimeType="text/html; charset="+type;
        return this.mimeType;
    }

    public LanguageCodeFrame languageCodeFrame;

    public void hyperLinkUpdate(int id, String urlName)
    {
//           System.out.println("url="+urlName);
           this.urlArea.setText(urlName);
           this.startLoading(urlName);
//           this.loadPage(urlName);
    }

    public void moveMouseOnTheEPane(int id, int x, int y)
    {
        ControlledEditPane t=(ControlledEditPane)(texts.elementAt(id));
        t.moveMouse(x,y);
    }

    public void mouseExitAtTheEPane(int id, int x, int y)
    {
//        sendEvent("txt.mxit("+id+","+x+","+y+")\n");
    }

    public void mouseEnteredAtTheEPane(int id, int x, int y)
    {
//        sendEvent("txt.ment("+id+","+x+","+y+")\n");
    }

    public void enterMouseOnTheEPane(int id, int x, int y)
    {
        ControlledEditPane t=(ControlledEditPane)(texts.elementAt(id));
//        t.rmouseIsShown=true;
//        t.rmouse.setVisible(true);
//        t.moveMouse(x,y);
        t.enterMouse(x,y);
    }

    public void exitMouseOnTheEPane(int id, int x, int y)
    {
        ControlledEditPane t=(ControlledEditPane)(texts.elementAt(id));
//        t.rmouse.setVisible(false);
//        t.rmouseIsShown=false;
        t.exitMouse();
    }
    public void mouseMovedAtTheEPane(int id, int x, int y)
    {
//        sendEvent("txt.mm("+id+","+x+","+y+")\n");
    }

/*
    public void mouseIsEnteredAtTheFrame(int x, int y)
    {
        // This method is derived from interface FrameWithControlledFrame
        // to do: code goes here
        
        sendEvent("frm.ment("+x+","+y+")\n");
   }
*/
    public void mouseIsExitedAtTheFrame(int x, int y)
    {
        // This method is derived from interface FrameWithControlledFrame
        // to do: code goes here
        sendEvent("frm.mxit("+x+","+y+")\n");
    }
/*
    public void mouseIsMovedAtTheFrame(int x, int y)
    {
        // This method is derived from interface FrameWithControlledFrame
        // to do: code goes here
       sendEvent("frm.mm("+x+","+y+")\n");
    }
*/
/*
    void load(InputStream ins, String coding)
    {
            int buffersize=50000;
	    	BufferedReader dinstream=null;
            String page="";
            try{
               dinstream=new BufferedReader(
                              new InputStreamReader(ins,this.languageCodeButton.getText() )
//                              new InputStreamReader(ins)
                              ,buffersize);
            }
            catch(Exception e){
                message.setText(""+e);
            }
            String line=null;
            try{
                line=dinstream.readLine();
            }
            catch(IOException e){
                message.setText("error at readLine.");
            }
            catch(NullPointerException e){
                message.setText("null pointer exception.");
            }
            while(line!=null)
            {
                if(this.me==null) return;
                page=page+line+"\n";
//                pageArea.setText(page);
//                pageArea.repaint();
                try{
                    line=dinstream.readLine();
                }
                catch(IOException e){
                    message.setText("error at readLine.");
                    break;
                }
                catch(ArrayIndexOutOfBoundsException e){
                    message.setText("error at readLine, arrayIndexOutOf bounds.");
                    message.setText("buffer size "+buffersize+"seems small.");
                    break;
                }
                catch(NullPointerException e){
                    message.setText("error at readLine, null pointer exception.");
                    break;
                }
                catch(Exception e){
                    message.setText("error at readLine, something wrong:"+e);
                    break;
                }
            }
            pageArea.setContentType(this.getMimeType(this.languageCodeButton.getText())) ;
            pageArea.setText(page);
            pageArea.repaint();
            
  //      }
//          resultArea.repaint();
            message.setText("connection closed.");
        
            try{
               dinstream.close();
            }
            catch(IOException e) {message.setText("close error.");}
//          WebPagePane.setText(page);
    }
*/

    public int startPosition;

    public int endPosition;

    public void recordMessage(String s)
    {
        if(communicationNode==null) return;
        communicationNode.eventRecorder.recordMessage("\"WebFrame\","+s);
    }

     public void pasteAtTheText(int i)
    {
            String theText="";
            String newText="";
            int startPosition=0;
            int endPosition=0;
            ControlledEditPane ta=(ControlledEditPane)(this.texts.elementAt(i));
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
        ControlledEditPane ta=(ControlledEditPane)(this.texts.elementAt(i));
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
       
        ControlledEditPane ta=(ControlledEditPane)(this.texts.elementAt(i));
        /*
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
               */
         newText=""+ta.getSelectedText();
         this.communicationNode.tempText=newText;
         return;
  }

    public int lastManipulatedTextArea;

    String commandName="web";
    public String getCommandName(){
    	return commandName;
    }
    public void sendEvent(String s)
    {
        /*
        if(!sendEventFlag) return;
        if(ebuff!=null){
             ebuff.putS(communicationMode,"web",s);
        }
        */
        if(this.communicationNode==null) return;
        this.communicationNode.sendEvent(commandName,s);
    }

    public int pointerToNextUrl;

    public Vector urls;

    public void saveText(String urlname)
    {
        if(urlname==null) return;
        int startTime=this.communicationNode.eventRecorder.timer.getMilliTime();
        message.setText("saving "+urlname+"\n");

        FileOutputStream fouts=null;

        try{ fouts= new FileOutputStream(new File(urlname));}
        catch(FileNotFoundException e){
            message.setText("wrong directory:"+urlname+" ?\n");
        }
        catch(IOException e){ message.setText("cannot access"+urlname+".\n");}

        String outx=this.pageArea.getText();
//        byte[] buff=new byte[outx.length()];
        byte[] buff=null;
        try{
          buff=outx.getBytes(communicationNode.getEncodingCode());
        }
        catch(java.io.UnsupportedEncodingException e){
            System.out.println("exception:"+e);
        }
//        outx.getBytes(0,outx.length(),buff,0);

        try{
            fouts.write(buff);
            fouts.flush();
        }
        catch(IOException e)
           { message.setText("save:IOExceptin while flushing.\n");}
           
        try{ fouts.close();  }
        catch(IOException e)
           { message.setText("save:IOException while closing.\n");}

        message.setText("Saving done.\n");
        int endTime=communicationNode.eventRecorder.timer.getMilliTime();
        int savingtime=endTime-startTime;
        double length=(double)(outx.length());
        String savingSpeed="";
        if(savingtime>0){
            savingSpeed=""+(length*1000.0)/savingtime;
        }
        else savingSpeed="(na)";
        this.recordMessage(""+startTime+
        ",\"savefig("+urlname+") \","+savingtime+
        ","+length+","+savingSpeed);
    }

    public EventBuffer ebuff;

    public String encodingCode;

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

    public void scrollBarHidden(int paneID, int barID)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
//        sendEvent("sb.hidden("+paneID+","+barID+")\n");
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

    public void showScrollBar(int paneID, int barID)
    {
        // This method is derived from interface FrameWithControlledPane
        // to do: code goes here
         ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(paneID));
         p.showScrollBar(barID);
    }

    void loadPageFromProxy(String targetURL, String proxyAddress, int proxyPort)
    {
       int maxbuff=1024;
        byte buff[]=new byte[maxbuff];;
        String dataLine;
        String page="";
        this.pageArea.setText("");
        int bl=0;
		URL url=null;
		InputStream instream=null;
		BufferedReader dinstream=null;
//		CQueue inqueue;
		int rl=0;
		int x=0;
		
		Socket proxySocket=null;
		try{
		    proxySocket=new Socket(proxyAddress,proxyPort);
		}
		catch(UnknownHostException e){
		    this.message.setText("proxy host unknown:"+proxyAddress+":"+proxyPort);
		}
		catch(IOException e){
		    message.setText("IOException while connecting the proxy:"+proxyAddress);
		}
		BufferedOutputStream outs=null;
		InputStream ins=null;
		try{
    		outs=new BufferedOutputStream(proxySocket.getOutputStream());
		    ins=new BufferedInputStream(proxySocket.getInputStream());
    	}
    	catch(IOException e){
    	    message.setText("IOException while getting output stream");
    	}
		
		try{
		    byte b[]=(new String("GET "+targetURL+" HTTP/1.1\n\n")).getBytes();
    		outs.write(b);
    		outs.flush();
		}
		catch(IOException e){
		    message.setText("IOException while output the GET command");
		}
		

       int buffersize=50000;

              dinstream=new BufferedReader(new InputStreamReader(ins),buffersize);

            String line=null;
            try{
            line=dinstream.readLine();
            }
            catch(IOException e){
                message.setText("error at readLine.");
            }
            catch(NullPointerException e){
                message.setText("null pointer exception.");
            }
            while(line!=null)
            {
                page=page+line+"\n";
//                pageArea.setText(page);
//                pageArea.repaint();
                try{
                line=dinstream.readLine();
                }
                catch(IOException e){
                    message.setText("error at readLine.");
                    break;
                }
                catch(ArrayIndexOutOfBoundsException e){
                    message.setText("error at readLine, arrayIndexOutOf bounds.");
                    message.setText("buffer size "+buffersize+"seems small.");
                    break;
                }
                catch(NullPointerException e){
                    message.setText("error at readLine, null pointer exception.");
                    break;
                }
                catch(Exception e){
                    message.setText("error at readLine, something wrong:"+e);
                    break;
                }
            }
            pageArea.setText(page);
            pageArea.repaint();
            
  //      }
//        resultArea.repaint();
        message.setText("connection closed.");
        
        try{
           dinstream.close();
           outs.close();
           proxySocket.close();
        }
        catch(IOException e) {message.setText("close error.");}
//        WebPagePane.setText(page);
     }
/*
    public void startLoading()
    {
//        start();
          this.loadPage(this.currentURL);
    }
*/
    String currentURL;
/*
    public Thread me;

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
           me=new Thread(this,"WebFrame");
           me.start();
        }
    }

    public void run()
    {
//        if(me!=null){
            this.loadPage(currentURL);
//        }   
    }

    synchronized boolean isAbleToLoad(){
    	if(pageIsLoading) return false;
		System.out.println("isAble:pageIsLoading:true->true");
    	pageIsLoading=true;
    	return true;
    }
    
    void setEnableToLoad(boolean f){
    	pageIsLoading=!f;
    	System.out.println("set:pageIsLoading:"+pageIsLoading);
    }
*/
    public void startLoading(String url)
    {
    	/*
    	try{
    		do {
    			Thread.sleep(50);
    		} while(!this.isAbleToLoad());
    	}
    	catch(InterruptedException e){}
    	*/
//		this.pageIsLoading=true;    	
		this.urlArea.setText("");
		this.urlArea.setEditable(true);
		this.pageArea.setEditable(false);
    	this.setVisible(true);
    	this.requestFocus();
    	this.requestFocusInWindow();
	    this.urlArea.setText(url);
	    currentURL=url;
        pointerToNextUrl++;
	    if(pointerToNextUrl>=urls.size()){
	        urls.addElement(url);
	    }
	    else {
	        urls.setElementAt(url,pointerToNextUrl);
	    }
//	    start();
        this.loadPage(currentURL);
   }
    public FileFrame fileFrame;

    Vector buttons;
    Vector texts;
    Vector panes;

    public void clickButton(int i)
    {
//        System.out.println("webframe.clickbutton("+i+")");
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        b.click();
        this.mouseClickedAtButton(i);
    }

    public void clickMouseOnTheEPane(int i, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledEditPane
        // to do: code goes here
        ControlledEditPane t=(ControlledEditPane)(texts.elementAt(i));
        t.clickMouse(p,x,y);
    }

    public void dispose()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
        super.dispose();
    }

    public void dragMouseOnTheEPane(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledEditPane
        // to do: code goes here
        ControlledEditPane t=(ControlledEditPane)(texts.elementAt(id));
        t.dragMouse(position,x,y);
    }

    public void exitThis()
    {
         this.setVisible(false);         // Frame を非表示にする.
//         this.show(false);
//         this.stop();
         this.recordMessage("0,\"exit webframe\"");
         /*
         this.communicationNode.applicationManager.deleteRunningApplication("WebFrame");
         this.dispose();
         */
    }

    public void focusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//            button.controlledButton_mouseEntered(null);
            button.focus();
    }

    public File getDefaultPath()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        File rtn=this.communicationNode.dsrRoot;
        return rtn;
    }

    public Vector getDialogs()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return null;
    }

    public void keyIsTypedAtTheEPane(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledEditPane
        // to do: code goes here
//         sendEvent("txt.kdn("+i+","+p+","+key+")\n");
   }

    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        /*
		buttons.addElement(this.fileButton);
		buttons.addElement(this.backButton);
		buttons.addElement(this.forwardButton);
		buttons.addElement(this.loadButton);
		buttons.addElement(this.copyButton);
		buttons.addElement(this.cutButton);
		buttons.addElement(this.pasteButton);
		buttons.addElement(this.searchButton);
		buttons.addElement(this.replaceButton);
		buttons.addElement(this.exitButton);
		buttons.addElement(this.languageCodeButton);
		buttons.addElement(this.captureButton);
		*/
       if(i==this.fileButton.getID()){
//            fileFrame.setDialogName("file dialog:");
            fileFrame.setListener(this);
            fileFrame.setCommonPath(
               this.communicationNode.commonDataDir.toString());
            fileFrame.setUserPath(
               this.communicationNode.userDataDir.toString());
            fileFrame.show();
            return;

       }
       else if(i==this.backButton.getID()){
//		buttons.addElement(this.backButton);
//                this.stop();
                pointerToNextUrl--;
                if(this.pointerToNextUrl<=0)  {
                     pointerToNextUrl=0;
                }
                String theUrl=(String)(urls.elementAt(pointerToNextUrl));
	            this.urlArea.setText(theUrl);
//    	        currentURL=theUrl;
                int id=1;
//                sendEvent("txt.hpl("+id+",\""+theUrl+"\")\n");
//                this.start();            
//                this.loadPage(currentURL);
                this.startLoading(theUrl);
		    return;
       }
       else if(i==this.forwardButton.getID()){
//		buttons.addElement(this.forwardButton);
                if(this.pointerToNextUrl>=urls.size()-1)  {
                    pointerToNextUrl=urls.size()-1; return;
                }
                pointerToNextUrl++;
                String theUrl=(String)(urls.elementAt(pointerToNextUrl));
//                stop();
	            this.urlArea.setText(theUrl);
     	        currentURL=theUrl;
                int id=1;
//                sendEvent("txt.hpl("+id+",\""+theUrl+"\")\n");
//                this.start();
               this.loadPage(currentURL);
               return;
       }
       else if(i==this.loadButton.getID()){
 //		buttons.addElement(this.loadButton);
                String theURL=this.urlArea.getText();
                this.startLoading(theURL);
                int id=1;
//                sendEvent("txt.hpl("+id+",\""+theURL+"\")\n");
    		    return;
       }
       else if(i==this.copyButton.getID()){
//		buttons.addElement(this.copyButton);
            this.copyFromTheText(this.lastManipulatedTextArea);
            return;
       }
       else if(i==this.cutButton.getID()){
 //		buttons.addElement(this.cutButton);
            this.cutAnAreaOfTheText(this.lastManipulatedTextArea); 
            return;
       }
       else if(i==this.pasteButton.getID()){
//		buttons.addElement(this.pasteButton);
            this.pasteAtTheText(this.lastManipulatedTextArea); 
            return;
       }
       else if(i==this.searchButton.getID()){
// 		buttons.addElement(this.searchButton);
      }
        else if(i==this.replaceButton.getID()){
//		buttons.addElement(this.replaceButton);
       }
        else if(i==this.exitButton.getID()){
//		buttons.addElement(this.exitButton);
            this.exitThis();
            return;
       }
       else if(i==this.languageCodeButton.getID()){
        // languageCodeButton
            this.languageCodeFrame.show();
            return;
       }
       else if(i==this.captureButton.getID()){
        // caputreButton
            Image img=this.pageArea.getImage();
            if(this.draw==null){
        		   ControlledFrame d=communicationNode.applicationManager.spawnApplication2("DrawFrame",communicationNode.getCommunicationMode());
		           draw=(DrawFrame)d;
            }
            this.draw.show();
            return;
       }
       else if(i==this.reloadButton.getID()){
                String theURL=this.urlArea.getText();
                this.startLoading(theURL);
                int id=1;
//                sendEvent("txt.hpl("+id+",\""+theURL+"\")\n");
            return;
       }
    }

    public void mouseClickedAtTheEPane(int i, int p,int x,int y)
    {
        // This method is derived from interface FrameWithControlledEditPane
        // to do: code goes here
        this.lastManipulatedTextArea=i;
//        sendEvent("txt.mdn("+i+","+p+")\n");
    }

    public void mouseDraggedAtTheEPane(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledEditPane
        // to do: code goes here
//        sendEvent("txt.mdg("+id+","+position+")\n");
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

    public void mousePressedAtTheEPane(int id, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledEditPane
        // to do: code goes here
        this.lastManipulatedTextArea=id;
//        sendEvent("txt.mps("+id+","+p+")\n");
    }

    public void mouseReleasedAtTheEPane(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledEditPane
        // to do: code goes here
//        sendEvent("txt.mrl("+id+","+position+")\n");
    }

    public void pressMouseOnTheEPane(int id, int p, int x, int y)
    {
        // This method is derived from interface FrameWithControlledEditPane
        // to do: code goes here
        ControlledEditPane t=(ControlledEditPane)(texts.elementAt(id));
        t.pressMouse(p,x,y);
    }

    public void receiveEvent(String s)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
//        if(!this.communicationNode.isReceivingEvents) return;
    	if(!this.isReceiving()) return;
        //
        //
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
            ParseWebBrowserEvent evParser=new ParseWebBrowserEvent(this,iq);
            evParser.parsingString=s;
            try{
            evParser.run();
            }
            catch(Exception e){}
    }

    public void releaseMouseOnTheEPane(int id, int position, int x, int y)
    {
        // This method is derived from interface FrameWithControlledEditPane
        // to do: code goes here
         ControlledEditPane t=(ControlledEditPane)(texts.elementAt(id));
        t.releaseMouse(position,x,y);
   }

    public void sendAll()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
    }

    public void sendFileDialogMessage(String m)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        // if(this.sendEventFlag) 
        sendEvent("file."+m);
    }

    public ControlledFrame spawnMain(CommunicationNode cn, String args, int pID, String controlMode)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
           WebFrame wf=this;
           /*
           int i=wf.pageArea.id;
           wf.pageArea.setText("");
           wf.pageArea=new ControlledEditPane();
		   wf.pagePane.getViewport().add(pageArea);
           wf.pageArea.setBounds(0,0,633,352);
           wf.texts.setElementAt(wf.pageArea,i);
           wf.pageArea.setFrame(wf);
           wf.pageArea.setID(i);
           */
           wf.setTitle("DSR/Web Browser");
           wf.communicationNode=cn;
           wf.pID=pID;
//           wf.encodingCode=encodingCode;
		
           wf.ebuff=cn.commandTranceiver.ebuff;
           wf.setIcons(cn.getIconPlace());
//           wf.show();
           wf.setVisible(true);
           return wf;
    }

    public void toFront()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
    }

    public void typeKeyAtTheEPane(int i, int p, int key)
    {
        // This method is derived from interface FrameWithControlledEditPane
        // to do: code goes here
        ControlledEditPane t=(ControlledEditPane)(texts.elementAt(i));
        t.typeKey(p,key);
    }

    public void unfocusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
            button.unFocus();
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
            this.startLoading(url);
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
        	this.urlArea.setText(fileName);
        	this.startLoading(url);
            return;
        }
        if(dname.equals("output user file name:")){
            String url="";
    		String fileName=d.getText();
     		File userDataPath=communicationNode.userDataDir;   		
	    	if(!userDataPath.exists()) 
	    	         userDataPath.mkdir();
//	    	File thePath=new File(userDataPath.getPath(),fileName);
//        	this.saveText(thePath.getPath());
            this.saveText(fileName);
            return;
        }
        if(dname.equals("url:")){
            String url=d.getText();
        	this.startLoading(url);
            return;
        }
    }

    public void whenCancelButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

	public WebFrame()
	{
		//
		panes=new Vector();
		panes.addElement(this.urlPane);
		panes.addElement(this.pagePane);
		int numberOfPanes=panes.size();
		for(int i=0;i<numberOfPanes;i++){
		    ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(i));
		    p.setID(i);
		    p.setFrame(this);
		}
		
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		setResizable(false);
		getContentPane().setLayout(null);
//		getContentPane().setBackground(java.awt.Color.lightGray);
		getContentPane().setBackground(new Color(204,220,250));
		setSize(664,600);
		setVisible(false);
		pagePane.setOpaque(true);
		getContentPane().add(pagePane);
		pagePane.setBounds(12,144,636,372);
		pagePane.getViewport().add(pageArea);
		pageArea.setBounds(0,0,636,352);
		titleLabel.setText("Distributed System Recorder/ Web Browser");
		getContentPane().add(titleLabel);
		titleLabel.setBounds(12,12,384,36);
		urlPane.setOpaque(true);
		getContentPane().add(urlPane);
		urlPane.setBounds(60,96,504,48);
		urlPane.getViewport().add(urlArea);
		urlArea.setBackground(java.awt.Color.white);
		urlArea.setBounds(0,0,500,44);
		urlArea.setForeground(java.awt.Color.black);
		urlArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		JLabel1.setText("url:");
		getContentPane().add(JLabel1);
		JLabel1.setBounds(12,108,48,36);
		loadButton.setText("load");
		loadButton.setActionCommand("load");
		getContentPane().add(loadButton);
		loadButton.setBackground(new java.awt.Color(204,204,204));
		loadButton.setForeground(java.awt.Color.black);
		loadButton.setFont(new Font("Dialog", Font.BOLD, 12));
		loadButton.setBounds(564,96,84,48);
		fileButton.setActionCommand("file");
		fileButton.setToolTipText("file");
		getContentPane().add(fileButton);
		fileButton.setBackground(new java.awt.Color(204,204,204));
		fileButton.setForeground(java.awt.Color.black);
		fileButton.setFont(new Font("Dialog", Font.BOLD, 12));
		fileButton.setBounds(12,48,84,24);
		backButton.setActionCommand("back");
		backButton.setToolTipText("back");
		getContentPane().add(backButton);
		backButton.setBackground(new java.awt.Color(204,204,204));
		backButton.setForeground(java.awt.Color.black);
		backButton.setFont(new Font("Dialog", Font.BOLD, 12));
		backButton.setBounds(96,48,84,24);
		forwardButton.setActionCommand("forward");
		forwardButton.setToolTipText("forward");
		getContentPane().add(forwardButton);
		forwardButton.setBackground(new java.awt.Color(204,204,204));
		forwardButton.setForeground(java.awt.Color.black);
		forwardButton.setFont(new Font("Dialog", Font.BOLD, 12));
		forwardButton.setBounds(180,48,84,24);
		copyButton.setActionCommand("copy");
		copyButton.setToolTipText("copy");
		getContentPane().add(copyButton);
		copyButton.setBackground(new java.awt.Color(204,204,204));
		copyButton.setForeground(java.awt.Color.black);
		copyButton.setFont(new Font("Dialog", Font.BOLD, 12));
		copyButton.setBounds(12,72,84,24);
		cutButton.setActionCommand("cut");
		cutButton.setToolTipText("cut");
		getContentPane().add(cutButton);
		cutButton.setBackground(new java.awt.Color(204,204,204));
		cutButton.setForeground(java.awt.Color.black);
		cutButton.setFont(new Font("Dialog", Font.BOLD, 12));
		cutButton.setBounds(96,72,84,24);
		pasteButton.setActionCommand("paste");
		pasteButton.setToolTipText("paste");
		getContentPane().add(pasteButton);
		pasteButton.setBackground(new java.awt.Color(204,204,204));
		pasteButton.setForeground(java.awt.Color.black);
		pasteButton.setFont(new Font("Dialog", Font.BOLD, 12));
		pasteButton.setBounds(180,72,84,24);
		searchButton.setText("search");
		searchButton.setActionCommand("search");
		getContentPane().add(searchButton);
		searchButton.setBackground(new java.awt.Color(204,204,204));
		searchButton.setForeground(java.awt.Color.black);
		searchButton.setFont(new Font("Dialog", Font.BOLD, 12));
		searchButton.setBounds(264,72,84,24);
		replaceButton.setText("replace");
		replaceButton.setActionCommand("replace");
		getContentPane().add(replaceButton);
		replaceButton.setBackground(new java.awt.Color(204,204,204));
		replaceButton.setForeground(java.awt.Color.black);
		replaceButton.setFont(new Font("Dialog", Font.BOLD, 12));
		replaceButton.setBounds(348,72,96,24);
		getContentPane().add(message);
		message.setBounds(12,516,636,36);
		exitButton.setActionCommand("exit");
		exitButton.setToolTipText("exit");
		getContentPane().add(exitButton);
		exitButton.setBackground(new java.awt.Color(204,204,204));
		exitButton.setForeground(java.awt.Color.black);
		exitButton.setFont(new Font("Dialog", Font.BOLD, 12));
		exitButton.setBounds(552,48,96,24);
		languageCodeButton.setText("code");
		languageCodeButton.setActionCommand("code");
		languageCodeButton.setToolTipText("select language character code");
		getContentPane().add(languageCodeButton);
		languageCodeButton.setBackground(new java.awt.Color(204,204,204));
		languageCodeButton.setForeground(java.awt.Color.black);
		languageCodeButton.setFont(new Font("Dialog", Font.BOLD, 12));
		languageCodeButton.setBounds(348,48,96,24);
		captureButton.setText("capture");
		captureButton.setActionCommand("capture");
		captureButton.setToolTipText("Image Capture");
		getContentPane().add(captureButton);
		captureButton.setBackground(new java.awt.Color(204,204,204));
		captureButton.setForeground(java.awt.Color.black);
		captureButton.setFont(new Font("Dialog", Font.BOLD, 12));
		captureButton.setBounds(444,72,96,24);
		getContentPane().add(keywordsLabel);
		keywordsLabel.setBounds(348,12,300,36);
//		 this.copyButton.setIcon(new ImageIcon("images/copy-icon.GIF"));
//		 this.cutButton.setIcon(new ImageIcon("images/cut-icon.GIF"));
//		 this.pasteButton.setIcon(new ImageIcon("images/paste-icon.GIF"));
		reloadButton.setText("reload");
		reloadButton.setActionCommand("reload");
		getContentPane().add(reloadButton);
		reloadButton.setBackground(new java.awt.Color(204,204,204));
		reloadButton.setForeground(java.awt.Color.black);
		reloadButton.setFont(new Font("DialogInput", Font.BOLD, 12));
		reloadButton.setBounds(264,48,84,24);
//		 this.fileButton.setIcon(new ImageIcon("images/file-icon.GIF"));
//		 this.exitButton.setIcon(new ImageIcon("images/exit-icon.GIF"));
//		 this.forwardButton.setIcon(new ImageIcon("images/next-Icon.GIF"));
//		 this.backButton.setIcon(new ImageIcon("images/stepback.gif"));
//		 this.titleLabel.setIcon(new ImageIcon("images/www-icon.GIF"));
		//}}

		//{{INIT_MENUS
		//}}
	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		fileButton.addActionListener(lSymAction);
		backButton.addActionListener(lSymAction);
		forwardButton.addActionListener(lSymAction);
		loadButton.addActionListener(lSymAction);
		copyButton.addActionListener(lSymAction);
		cutButton.addActionListener(lSymAction);
		pasteButton.addActionListener(lSymAction);
		searchButton.addActionListener(lSymAction);
		replaceButton.addActionListener(lSymAction);
		exitButton.addActionListener(lSymAction);
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		languageCodeButton.addActionListener(lSymAction);
		captureButton.addActionListener(lSymAction);
		SymFocus aSymFocus = new SymFocus();
		this.addFocusListener(aSymFocus);
		reloadButton.addActionListener(lSymAction);
		//}}
	
	    super.registerListeners();
	
		//
		fileFrame=new FileFrame("Web/HTML File");
		this.languageCodeFrame=new LanguageCodeFrame("language code selection");
		this.languageCodeFrame.setFrame(this);
		
		//
		buttons=new Vector();
		buttons.addElement(this.fileButton);
		buttons.addElement(this.backButton);
		buttons.addElement(this.forwardButton);
		buttons.addElement(this.loadButton);
		buttons.addElement(this.copyButton);
		buttons.addElement(this.cutButton);
		buttons.addElement(this.pasteButton);
		buttons.addElement(this.searchButton);
		buttons.addElement(this.replaceButton);
		buttons.addElement(this.exitButton);
		buttons.addElement(this.languageCodeButton);
		buttons.addElement(this.captureButton);
		buttons.addElement(this.reloadButton);
		int numberOfButtons=buttons.size();
		for(int i=0;i<numberOfButtons;i++){
		    ControlledButton b=(ControlledButton)(buttons.elementAt(i));
		    b.setID(i);
		    b.setFrame(this);
		}
		
        //
		texts=new Vector();
		texts.addElement(this.pageArea);
		int numberOfTexts=texts.size();
		for(int i=0;i<numberOfTexts;i++){
		    ControlledEditPane e=(ControlledEditPane)(texts.elementAt(i));
		    e.setID(i);
		    e.setFrame(this);
		}
		texts2=new Vector();
		texts2.addElement(this.urlArea);
		int numberOfTexts2=texts2.size();
		for(int i=0;i<numberOfTexts2;i++){
		    ControlledTextArea e=(ControlledTextArea)(texts2.elementAt(i));
		    e.setID(i);
		    e.setFrame(this);
		}
		//
		urls=new Vector();
		this.pointerToNextUrl=0;
		//
		
		this.languageCodeButton.setText(
		  ((ControlledButton)(this.languageCodeFrame.buttons.elementAt(3))).getText()
		); // JISAutoDetect
		
	}

	public WebFrame(String sTitle)
	{
		this();
		setTitle(sTitle);
	}

	static public void main(String args[])
	{
		(new WebFrame()).setVisible(true);
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension size = getSize();

		super.addNotify();

		int numberOfTexts=texts.size();
		for(int i=0;i<numberOfTexts;i++){
		    ControlledEditPane e=(ControlledEditPane)(texts.elementAt(i));
            e.init();
		}

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
	controlledparts.ControlledScrollPane pagePane = new controlledparts.ControlledScrollPane();
	public controlledparts.ControlledEditPane pageArea = new controlledparts.ControlledEditPane();
	javax.swing.JLabel titleLabel = new javax.swing.JLabel();
	controlledparts.ControlledScrollPane urlPane = new controlledparts.ControlledScrollPane();
	public controlledparts.ControlledTextArea urlArea = new controlledparts.ControlledTextArea();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	controlledparts.ControlledButton loadButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton fileButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton backButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton forwardButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton copyButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton cutButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton pasteButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton searchButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton replaceButton = new controlledparts.ControlledButton();
	javax.swing.JLabel message = new javax.swing.JLabel();
	controlledparts.ControlledButton exitButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton languageCodeButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton captureButton = new controlledparts.ControlledButton();
	javax.swing.JLabel keywordsLabel = new javax.swing.JLabel();
	controlledparts.ControlledButton reloadButton = new controlledparts.ControlledButton();
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
			else if (object == backButton)
				backButton_actionPerformed(event);
			else if (object == forwardButton)
				forwardButton_actionPerformed(event);
			else if (object == loadButton)
				loadButton_actionPerformed(event);
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
			else if (object == exitButton)
				exitButton_actionPerformed(event);
			else if (object == languageCodeButton)
				languageCodeButton_actionPerformed(event);
			else if (object == captureButton)
				captureButton_actionPerformed(event);
			else if (object == reloadButton)
				reloadButton_actionPerformed(event);
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
			// fileButton Show the ControlledButton
//			fileButton.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	void backButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		backButton_actionPerformed_Interaction1(event);
	}

	void backButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// backButton Show the ControlledButton
//			backButton.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	void forwardButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		forwardButton_actionPerformed_Interaction1(event);
	}

	void forwardButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// forwardButton Show the ControlledButton
//			forwardButton.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	void loadButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		loadButton_actionPerformed_Interaction1(event);
	}

	void loadButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// loadButton Show the ControlledButton
//			loadButton.setVisible(true);
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

	void pasteButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		pasteButton_actionPerformed_Interaction1(event);
	}

	void pasteButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// pasteButton Show the ControlledButton
//			pasteButton.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	void searchButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		searchButton_actionPerformed_Interaction1(event);
	}

	void searchButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// searchButton Show the ControlledButton
//			searchButton.setVisible(true);
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
			// replaceButton Show the ControlledButton
//			replaceButton.setVisible(true);
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
			// exitButton Show the ControlledButton
//			exitButton.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == WebFrame.this)
				WebFrame_windowClosing(event);
		}
	}

	void WebFrame_windowClosing(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		WebFrame_windowClosing_Interaction1(event);
	}

	void WebFrame_windowClosing_Interaction1(java.awt.event.WindowEvent event)
	{
		try {
//			this.exitThis();
		} catch (java.lang.Exception e) {
		}
	}

	void languageCodeButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		languageCodeButton_actionPerformed_Interaction1(event);
	}

	void languageCodeButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			languageCodeButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void captureButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		captureButton_actionPerformed_Interaction1(event);
	}

	void captureButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			captureButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	class SymFocus extends java.awt.event.FocusAdapter
	{
		public void focusGained(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == WebFrame.this)
				WebFrame_focusGained(event);
		}
	}

	void WebFrame_focusGained(java.awt.event.FocusEvent event)
	{
		// to do: code goes here.
			 
		WebFrame_focusGained_Interaction1(event);
	}

	void WebFrame_focusGained_Interaction1(java.awt.event.FocusEvent event)
	{
		try {
			// WebFrame Request the focus
//			this.requestFocus();
		} catch (java.lang.Exception e) {
		}
		this.focusGained();
	}

	void reloadButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		reloadButton_actionPerformed_Interaction1(event);
	}

	void reloadButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			reloadButton.show();
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
		ControlledTextArea t=(ControlledTextArea)(texts2.elementAt(i));
		t.releaseKey(p, code);

	}
	public void pressKey(int i, int p, int code) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledTextArea t=(ControlledTextArea)(texts2.elementAt(i));
		t.pressKey(p, code);

	}
    public void setIcons(String iconPlace){
		try{
			ImageIcon ic=new ImageIcon(new URL(iconPlace+"copy-icon.GIF"));
//			 this.copyButton.setIcon(new ImageIcon("images/copy-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
				this.copyButton.setIcon(ic);
			else
				this.copyButton.setText("copy");
//			 this.cutButton.setIcon(new ImageIcon("images/cut-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"cut-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
				this.cutButton.setIcon(ic);
			else
				this.cutButton.setText("cut");
//			 this.pasteButton.setIcon(new ImageIcon("images/paste-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"paste-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
				this.pasteButton.setIcon(ic);
			else
				this.pasteButton.setText("paste");
//			 this.fileButton.setIcon(new ImageIcon("images/file-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"file-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
				this.fileButton.setIcon(ic);
			else
				this.fileButton.setText("cut");
//			 this.exitButton.setIcon(new ImageIcon("images/exit-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"exit-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
				this.exitButton.setIcon(ic);
			else
				this.exitButton.setText("cut");
//			 this.forwardButton.setIcon(new ImageIcon("images/next-Icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"next-Icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
				this.forwardButton.setIcon(ic);
			else
				this.forwardButton.setText("forward");
//			 this.backButton.setIcon(new ImageIcon("images/stepback.gif"));
			ic=new ImageIcon(new URL(iconPlace+"stepback.gif"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
				this.backButton.setIcon(ic);
			else
				this.backButton.setText("back");
//			 this.titleLabel.setIcon(new ImageIcon("images/www-icon.GIF"));
			ic=new ImageIcon(new URL(iconPlace+"www-icon.GIF"));
			if(ic.getImageLoadStatus()==MediaTracker.COMPLETE)
				this.titleLabel.setIcon(ic);
			else
				this.titleLabel.setText("www");			

	    }
		catch(Exception e){
				this.copyButton.setText("copy");
				this.cutButton.setText("cut");
				this.pasteButton.setText("paste");
				this.fileButton.setText("cut");
				this.exitButton.setText("cut");
				this.forwardButton.setText("forward");
				this.backButton.setText("back");
				this.titleLabel.setText("www");			

		}

//		this.figSelectFrame.setIcons(iconPlace);
//        this.widthSelectFrame.setIcons(iconPlace);
    }
}