/*
NetPaint

     by T. Yamanoue,
     Kyushu Institute of Technology, Japan,
     Aug.1, 1997


   A Paint tool for the Internet.

   Drawing tool on a Web brouser.
   A Co-operative drawing tool.
   Drawing a paint on the Internet by linking parts


*/
package application.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Vector;

import javax.swing.JTextArea;

import nodesystem.*;
import controlledparts.*;

public class EditDispatcher implements DialogListener  
{
	String separator="/";
    public String baseDir;
	
    public void setSeparator(String x){
    	this.separator=x;
    }
    
    public boolean isDirectOperation()
    {
        return this.npMain.isControlledByLocalUser();
    }

    public boolean isShowingRmouse()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return this.npMain.isShowingRmouse();
    }

    public boolean isControlledByLocalUser()
    {
        return this.npMain.isControlledByLocalUser();
    }

    public void moveFig(int x, int y)
    {
        fc.fs.move(x,y);
    }

    public void moveSelectedFig(int x, int y)
    {
        fc.ftemp.move(x,y);
    }

    public void selectAll()
    {
        changeMode("all");
    }


    public void save(File file)
    {
        FileOutputStream fouts;
        DataOutputStream outs;
        URL url;
        InetAddress serv;
        Socket      sock;



        if(file==null) return;
        int startTime=npMain.getTimeNow();
        fouts=null;

        try{ fouts= new FileOutputStream(file);}
        catch(FileNotFoundException e){
            npMain.recordMessage(""+ ",\"exception(FileNotFound) \",,,");
        }
        /*
        catch(IOException e){ 
            npMain.recordMessage(""+ ",\"exception(IO) \",,,");
        }
        */

//        DataOutputStream outputStream=new DataOutputStream(fouts);
        TemporalyText outS=new TemporalyText();

        if(!(fc.fs.save(outS))){
            npMain.recordMessage(""+ ",\"error while saving \",,,");
        }
        String outx=outS.getText();
//        byte[] buff=new byte[outx.length()];
        byte[] buff=null;
        try{
          buff=outx.getBytes(npMain.communicationNode.getEncodingCode());
        }
        catch(java.io.UnsupportedEncodingException e){
            npMain.recordMessage(""+ ",\"exception(UnsupportedEncoding) \",,,");
        }
//        outx.getBytes(0,outx.length(),buff,0);

        try{
            fouts.write(buff);
            fouts.flush();
        }
        catch(IOException e)
        { 
             npMain.recordMessage(""+ ",\"exception(IO) \",,,");           
        }
           
        try{ fouts.close();  }
        catch(IOException e)
           {  npMain.recordMessage(""+ ",\"exception(IO) \",,,");}

        int endTime=npMain.getTimeNow();
        int savingtime=endTime-startTime;
        String savingSpeed="";
        if(savingtime>0){
            savingSpeed=""+10.0*((double)(outx.length()))/savingtime;
        }
        else savingSpeed="(na)";
        npMain.setTitle("Common/Communicative Browser - "+file);
        npMain.recordMessage(""+startTime+
        ",\"savefig("+file.getPath()+") \","+savingtime*0.1+
        ","+outx.length()+","+savingSpeed);
        this.npMain.urlField.setText(file.getPath());
    }

    public void read(String urlname)
    {
//        DataInputStream inputStream;
    	
    	if(urlname.endsWith("html")||urlname.endsWith("HTML")||urlname.endsWith("htm")||urlname.endsWith("HTM")){
    		this.npMain.readHtml(urlname);
    		return;
    	}
    	
        BufferedReader inputStream=null;
        URL  url;
        String encodingCode="JIS";
        this.npMain.urlField.setText(urlname);
        if(this.npMain.communicationNode!=null){
           if(!(this.npMain.isControlledByLocalUser()||this.npMain.communicationNode.isReadableFromEachFile())) return;
           encodingCode=this.npMain.communicationNode.getEncodingCode();
        }
        
        NetworkReader nr=new NetworkReader();
//        baseDir=nr.getBaseDir(urlname,npMain.communicationNode.getPathSeparator());
        baseDir=nr.getBaseDir(urlname,separator);
        if(baseDir.startsWith("http:/")){
        	if(baseDir.charAt(6)!='/'){
        		String x=baseDir.substring(6);
        		baseDir="http://"+x;
        	}
        }

//        if((this.npMain.getControlMode()).equals("receive")) return;
//        if(!(this.npMain.communicationNode.isSending())) return;
/*
        if( !(  this.npMain.getControlMode().equals("teach") ||
                 ( this.npMain.getControlMode().equals("common") &&
                   this.npMain.isControlledByLocalUser())
             )
          )  return;
          */

        
        int startTime=npMain.getTimeNow();
        if(urlname==null){
            npMain.recordMessage(""+ ",\"urlNameError() \",,,");
            return;
        }
//        mes.append("reading "+urlname+"\n");
        try{ url=new URL(urlname); }
        catch(MalformedURLException e)
        {
           npMain.recordMessage(""+ ",\"exception(MalformedURL) \",,,");
           return;
        }
        
        try{
            inputStream=//new DataInputStream(url.openStream());
                    new BufferedReader(
                          new InputStreamReader(url.openStream(),encodingCode));
        }
        catch(UnsupportedEncodingException e){
             npMain.recordMessage(""+ ",\"exception(UnsupportedEncoding) \",,,");
            return;
       }
        catch(IOException e){
             npMain.recordMessage(""+ ",\"exception(IOException) \",,,");
            return;
        }

        inQ= new InputQueue(inputStream);

        ParseFig pfig=new ParseFig(inQ, fc, mes);
        if(!pfig.parseFigures()){
             npMain.recordMessage(""+ ",\"parseError \",,,");
        }

        fs=pfig.figs;
        fc.fs=pfig.figs;
        npMain.repaint();
        try{ inputStream.close();  }
        catch(IOException e)
        {
            npMain.recordMessage(""+ ",\"exception(IO) \",,,");
        }
        int endTime=npMain.getTimeNow();
        int readingtime=endTime-startTime;
        String readingSpeed="";
        if(readingtime>0){
            readingSpeed=""+10.0*((double)(inQ.length()))/readingtime;
        }
        else readingSpeed="(na)";
//        npMain.recordMessage("-"+startTime+":readfig("+urlname+") done.");
        npMain.recordMessage(""+startTime+
        ",\"readfig("+urlname+") \""+","+readingtime*0.1+
        ","+inQ.length()+","+readingSpeed);
         if(this.npMain.isSending())
               this.npMain.sendAll();

    }
    public void read3(String fig){
        String encodingCode="UTF-8";
        BufferedReader inputStream=null;
        try{
            inputStream=//new DataInputStream(url.openStream());
                    new BufferedReader(new StringReader(fig),8000);
        }
        catch(Exception e){
            return;
       }

        inQ= new InputQueue(inputStream);

        ParseFig pfig=new ParseFig(inQ, fc, mes);
        if(!pfig.parseFigures()){
             System.out.println(""+ ",\"parseError \",,,");
        }

        fs=pfig.figs;
        fc.fs=pfig.figs;
        npMain.repaint();
    	
    }

    public Vector getDialogs()
    {
        return npMain.fileDialogs;
    }

    public void sendFileDialogMessage(String m)
    {
//        npMain.sendEvent(m);
          npMain.sendFileDialogMessage(m);
    }

    public void changeFontSize()
    {
  //       ((JLabel)(npMain.colorSelector.getSelectedItem())).repaint();
        Font f=npMain.fontSizeSelectButton.getFont();
        if(fc.ftemp.fp!=null) fc.ftemp.setFont(f);
        npMain.repaint();
    }

    public void changeWidth()
    {
  //       ((JLabel)(npMain.colorSelector.getSelectedItem())).repaint();
        String ws=npMain.lineWidthSelectButton.getText();
        int width=(new Integer(ws)).intValue();
        if(fc.ftemp.fp!=null) fc.ftemp.setWidth(width);
        npMain.repaint();
   }

    public File getDefaultPath()
    {
        File rtn=this.fc.gui.communicationNode.dsrRoot;
        return rtn;
    }


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
        String subname=d.getSubName();
        AFigure newfig;
        if(dname.equals("imageDialog")){
            if(
            // !npMain.getControlMode().equals("receive")
            //     npMain.communicationNode.sendEventFlag
//               !npMain.communicationNode.isReceivingEvents
            		!npMain.isReceiving()
              ) {
              String url="";
    		  String fileName=d.getText();
//              String separator=""+System.getProperty("file.separator");
              if(subname.equals("input common file name:")){
//        		File commonDataImagePath=this.npMain.communicationNode.commonDataImageDir;
//	        	File thePath=new File(commonDataImagePath.getPath(),fileName);
                if(this.separator.equals("\\"))
//                	url="file:///"+thePath.getPath();
                    url="file:///"+fileName;
                else
//                    url="file://"+thePath.getPath();
                    url="file://"+fileName;
              }
              else
              if(subname.equals("input user file name:")){
        		File userDataPath=this.npMain.communicationNode.userDataDir;
	        	if(!userDataPath.exists()) 
	     	         userDataPath.mkdir();
//                File thePath=new File(userDataPath.getPath(),fileName);
                if(separator.equals("\\"))
//                	url="file:///"+thePath.getPath();
                    url="file:///"+fileName;
                else
//                   url="file://"+thePath.getPath();
                    url="file://"+fileName;
              }
              else
              if(subname.equals("url:")){
                   url=d.getText();
              }
              //
              if(npMain.communicationNode.sendEventFlag)
                    npMain.imageManager.start();
              //
              newfig=new MyImage(fc,url,this.getNodeSettings());
              newfig.newFig();
              fc.ftemp.add(newfig);
              newfig.depth=0;
              npMain.depthIndicator.setValue(newfig.depth);
            }
            else
            {  // receive image;
            }
            return;
        }
        else
        if(dname.equals("textEditor")){
    		String theText=d.getText();
            newfig=new Text(fc);
            newfig.newFig();
            ((Text)newfig).setText(theText);
            fc.ftemp.add(newfig);
            newfig.depth=0;
            npMain.depthIndicator.setValue(newfig.depth);
            return;
        }
    }

    public String getSeparator(){
    	return this.separator;
    }
    
    public void loadFile(String dir,String file)
    {


        if(file==null) return;

        int astindex=file.indexOf("*.*");

        if(astindex>0) {
            String fx=file.substring(0,file.indexOf("*.*"));
            file=fx;
        }
//        mes.append("loading "+file+"\n");
        int startTime=npMain.getTimeNow();
        npMain.recordMessage("message: loading "+file+".");

        File inputfile=new File(dir,file);

        FileInputStream infstream=null;
        BufferedReader inputStream=null;
        try{
            infstream=new FileInputStream(inputfile);
        }
        catch(FileNotFoundException e){
//            mes.append(dir+file+" not found.\n");
            npMain.recordMessage("error: "+dir+file+" not found.");
        }
        /*
        catch(IOException e){
//            mes.append("cannot access "+dir+file+" .\n");
            npMain.recordMessage("error: cannot access "+dir+file+".");
        }
        */

//        inputStream=new DataInputStream(infstream);
        try{
        inputStream=new BufferedReader(new InputStreamReader(infstream,npMain.communicationNode.getEncodingCode()));
        }
        catch(UnsupportedEncodingException e){
            mes.append("unsupported encoding exception.\n");
        }
        inQ= new InputQueue(inputStream);

        ParseFig pfig=new ParseFig(inQ, fc, mes);

        if(!pfig.parseFigures()){
//            mes.append("parse error while loading.\n");
            npMain.recordMessage("error: parse error while loading.");
        }
        else{
            npMain.setTitle("Common/Communicative Browser - "+file);
        }

//        mes.append("reading "+file+" done.\n");
        npMain.recordMessage("-"+startTime+":reading "+file+" done.");
        fs=pfig.figs;
        fc.fs=pfig.figs;
        npMain.repaint();

        try{ inputStream.close();  }
        catch(IOException e)
           {
//            mes.append("read:IOException while closing.\n");
             npMain.recordMessage("error: read:IOException while closing.");
           }
//        mes.append("reading done.\n");
        npMain.recordMessage("message: reading done.");

     }
    public String getCurrentState()
    {
        String rtn="";
        int statesNumber=npMain.states.size();
        for(int i=0;i<statesNumber;i++){
            StateContainer c=(StateContainer)(npMain.states.elementAt(i));
            rtn=rtn+"stat("+i+","+c.getState()+")\n";
        }
        return rtn;
    }
    public void setDepth(int d)
    {
        if(fc.ftemp.fp!=null)
            fc.ftemp.setDepth(d);
    }
    public void copy()
    {
        changeMode("copy");
        npMain.editFunField.setText("select");
//        npMain.figNamField.setText("");
        npMain.repaint();
    }
    public void draw(Graphics g)
    {
        if(selectAreaState==1){
            Color  cc=g.getColor();
            g.setColor(Color.blue);
            g.drawRect(aX1,aY1,aX2-aX1,aY2-aY1);
            g.setColor(cc);
        }
    }
    public int aY2;
    public int aY1;
    public int aX2;
    public int aX1;
    public int selectAreaState;
    public void set()
    {
        fc.ftemp.setStep(-10);
        fc.fs.add(fc.ftemp);
        fc.ftemp.stop();
        fc.ftemp.fp=null;
        select();
    }
    public void save2(String dir,String file)
    {
        FileOutputStream fouts;
        DataOutputStream outs;
        URL url;
        InetAddress serv;
        Socket      sock;



        if(file==null) return;

        int astindex=file.indexOf("*.*");
        if(astindex>0) {
           String fx=file.substring(0,file.indexOf("*.*"));
            file=fx;
        }
//        mes.append("saving "+file+"\n");

        fouts=null;

        try{ fouts= new FileOutputStream(dir+file);}
        catch(FileNotFoundException e){
//            mes.append("wrong directory:"+dir+" ?\n");
        }
        /*
        catch(IOException e){ 
//            mes.append("cannot access"+dir+file+".\n");
        }
        */

//        DataOutputStream outputStream=new DataOutputStream(fouts);
        TemporalyText outS=new TemporalyText();

        if(!(fc.fs.save(outS))){
//            mes.append("error while saving\n");
        }

        String outx=outS.getText();
//        byte[] buff=new byte[outx.length()];
//        outx.getBytes(0,outx.length(),buff,0);
        byte[] buff=null;
        try{        
           buff=outx.getBytes(npMain.communicationNode.getEncodingCode());
        }
        catch(java.io.UnsupportedEncodingException e){}
        try{
            fouts.write(buff);
            fouts.flush();
        }
        catch(IOException e)
        { 
//            mes.append("save:IOExceptin while flushing.\n");
        }
           
        try{ fouts.close();  }
        catch(IOException e)
        { 
            // mes.append("save:IOException while closing.\n");
        }

//        mes.append("Saving done.\n");
        npMain.setTitle("Common/Communicative Browser - "+file);

   }
    public void read2()
    {
        BufferedReader inputStream;
        URL  url;

        String fname=npMain.urlField.getText();

//        String fname=null;
//        mes.append("reading "+fname+"\n");
        if(fname==null){
//            mes.append("URL Name is expected.\n");
            return;
        }
        try{ url=new URL(fname); }
        catch(MalformedURLException e)
        {
//            mes.append("MalformedURLException\n");
           return;
        }

        try{
            inputStream=new BufferedReader(
             new InputStreamReader(url.openStream(),npMain.communicationNode.getEncodingCode()));
        }
        catch(UnsupportedEncodingException e){
//            mes.append("exception:"+e+"\n");
            return;
        }
        catch(IOException e){
//            mes.append("url open error.\n");
            return;
        }

        inQ= new InputQueue(inputStream);

        ParseFig pfig=new ParseFig(inQ, fc, mes);
        if(!pfig.parseFigures()){
//            mes.append("parse error while loading.\n");
        }

//        mes.append("reading "+fname+" done.\n");
        fs=pfig.figs;
        fc.fs=pfig.figs;
        npMain.repaint();
        try{ inputStream.close();  }
        catch(IOException e)
           { 
            // mes.append("read:IOException while closing.\n");
            }
//        mes.append("reading done.\n");

    }

    public void modifyFig()
    {
//        npMain.editFunField.setText("Modify");
        changeMode("modify");
    }
    public void moveFig()
    {
//        npMain.editFunField.setText("Move");
        changeMode("move");

    }
    public void clear()
    {
        changeMode("clear");
        if(fc.fs!=null)
        fc.fs.stop();
        if(fc.fs!=null)
        fc.fs.fp=null;
        npMain.urlField.setText("");
        npMain.repaint();
        npMain.imageManager.init();
        changeMode("select");
    }
    public void bufferClear(){
//        changeMode("clear");
        if(fc.fs!=null)
        fc.fs.stop();
        if(fc.fs!=null)
        fc.fs.fp=null;
//        npMain.urlField.setText("");
//        npMain.repaint();
//        npMain.imageManager.init();
//        changeMode("select");
    	
    }
    public void changeColor()
    {
 //       ((JLabel)(npMain.colorSelector.getSelectedItem())).repaint();
        color=npMain.colorSelectButton.getBackground();
        if(fc.ftemp.fp!=null) fc.ftemp.setColor(color);
        npMain.repaint();
    }
    public Color color;
    public InputQueue inQ;
    public void keyDown(java.awt.event.KeyEvent e, int key)
    {
        if(fc.isPlaying()){
        	fc.fs.keyDown(key);
        }
        else{
          if(state.equals("new")){
              fc.ftemp.keyDown(key);
              return;
              }
          if(state.equals("modify")){
              fc.ftemp.keyDown(key);
              return;
              }
          return;
        }
    }
    public void save()
    {
        FileOutputStream fouts;
        DataOutputStream outputStream;
        File file;
        TemporalyText outS=new TemporalyText();

        if(!(fc.fs.save(outS))){
//        mes.append("error while saving\n");
        }

    }
    public String getFigString(){
        TemporalyText outS=new TemporalyText();

        if(!(fc.fs.save(outS))){
//        mes.append("error while saving\n");
        }
    	return outS.getText();
    }
    public void select()
    {
        changeMode("select");
    }
    public DrawFrame npMain;  //Draw
    public void rmFig()
    {
//            npMain.editFunField.setText("Cut");
            changeMode("cut");
            if(fc.ftemp!=null) {
  //              fc.fs.remove(fc.ftemp);
                fc.ftemp.stop();
                fc.ftemp.fp=null;
             }
//             fc.repaint();
             this.npMain.repaint();
             select();
    }
    public AFigure ftemp;
    public void newFig(String ft)
    {
//        npMain.editFunField.setText(npMain.communicationNode.getLclTxt("new"));
//        npMain.figNamField.setText(ft);
        changeMode("new");
  //      changeColor();
        Color  cc=color;

        if(fc.ftemp.fp!=null) {
    //        fc.fs.add(fc.ftemp);
            fc.ftemp.stop();
            fc.ftemp.fp=null;
        }
        npMain.repaint();
//        mes.append("new fig = "+ft+"\n");
        if(ft.equals("line")){
 //           mes.append("edispatch, Line is selected\n");
            newfig=new ALine(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);
        }
        else
        if(ft.equals("box")){
  //          mes.append("edispatch, Rectangle is selected\n");
            newfig=new ARectangle(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);
        }
        else
        if(ft.equals("fillBox")){
  //          mes.append("Fill Rectangle is selected\n");
            newfig=new FillRect(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);
        }
        else
        if(ft.equals("fillOval")){
 //           mes.append("Fill Oval is selected\n");
            newfig=new FillOval(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);
        }
        else
        if(ft.equals("lines")){
 //           mes.append("edispatch, Lines is selected\n");
            newfig=new ALines(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);
        }
        else
        if(ft.equals("curve")){
 //           mes.append("edispatch, Lines is selected\n");
            newfig=new ACurve(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);
        }
        else
        if(ft.equals("text")){
 //           mes.append("edispatch, Text is selected\n");

            newfig=new Text(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);

       }
       else
       if(ft.equals("loadText")){
       		 npMain.openTextEditor(this);
    		 return;
       }
        else
       if(ft.equals("loadText-r")){
            newfig=new Text(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);
       }
        else
        if(ft.equals("free")){
 //           mes.append("edispatch, Free is selected\n");
            newfig=new AFreeHandCurve(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);
        }
        else
        if(ft.equals("oval")){
 //           mes.append("edispatch, Oval is selected\n");
            newfig=new AnOval(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);
        }
        else
        if(ft.equals("polygon")){
//            mes.append("edispatch, Polygon is selected\n");
            newfig=new APolygon(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);
        }
        else
        if(ft.equals("r-rect")){
 //           mes.append("edispatch, RainbowRect is selected\n");
            newfig=new FillRainbowRect(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);
        }
        else
        if(ft.equals("r-poly")){
//            mes.append("RainbowPolygon is selected\n");
            newfig=new RainbowPolygon(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);
            }
        else
        if(ft.equals("image")){
            npMain.openImageInputDialog(this);
//            mes.append("Image is selected\n");
//    		 npMain.newImageDialog.show();
    		 return;
        }
        if(ft.equals("urlBox")){
            newfig=new ClickableBoxURL(fc);
            newfig.newFig();
            fc.ftemp.add(newfig);
        }
        newfig.color=cc;
        newfig.depth=0;
        npMain.depthIndicator.setValue(newfig.depth);
        return;
     }
    public AFigure newfig;
    public Figures fs;
    public void changeMode(String mode)
    {
    	String m=npMain.getLclTxt(mode);
        npMain.editFunField.setText(m);
        if(fc.fs==null) return;
        state=mode;
 //       mes.append("Edit mode to "+state+"\n");
        if(state.equals("select")){
            fc.ftemp.setStep(-10);
            fc.fs.add(fc.ftemp);
            fc.ftemp.stop();
            fc.ftemp.fp=null;
//            fc.fs.setStep(-10);
            npMain.repaint();
   //         return;
        }
        if(state.equals("all")){
            fc.ftemp.setStep(-10);
            fc.fs.add(fc.ftemp);
            fc.ftemp.stop();
            fc.ftemp.fp=fc.fs.fp;
            fc.ftemp.setStep(10);
            fc.fs.fp=null;
            npMain.repaint();
        }
        else
        if(state.equals("cut")){ // return;
        }
        else
        if(state.equals("copy")){
            Figures fsc=(Figures)(fc.ftemp.copyThis());
            fc.ftemp.setStep(-10);
            fc.fs.add(fc.ftemp);
  //          fc.ftemp.stop();
            fc.ftemp.fp=null;
            fc.ftemp.add(fsc);
            fc.ftemp.setStep(10);
        }
        else
        if(state.equals("move")){
            fc.ftemp.setStep(40); // return;
        }
        else
        if(state.equals("modify")){
            fc.ftemp.setStep(50); // return;
        }
        else
        if(state.equals("rotate")){
//            mes.append("Please Set the Center of Rotate first.\n");
            fc.ftemp.setStep(60); // return;
        }
        if(fc.fs!=null)
            fc.fs.start();
    }
    public EditDispatcher()
    {
    }
    public JTextArea mes;
    public FigCanvas fc;
    public EditDispatcher(FigCanvas f, JTextArea m, DrawFrame np) //Draw np
    {
        fc=f; mes=m; 
   //     mes.append("EditDispatcher starting\n");
        state="new";
   //     f.fs=new Figures(f);
        fs=f.fs;
   //     f.ftemp=null;
        f.editdispatch=this;
        ftemp=f.ftemp;
        npMain=np;
        selectAreaState=0;
        color=Color.black;
    }
   /*
     state= : new
            : select
            : cut
   */
    public String state;
    public void mouseUp(java.awt.event.MouseEvent e, int x, int y)
    {
 //       mes.append("edispatch mouseUp("+x+","+y+")\n");
       if(fc.isPlaying()){
       }
       else{
          if(state.equals("select")) {
          if(selectAreaState==1){
            aX2=x; aY2=y;
            fc.fs.selectArea(aX1,aY1,aX2,aY2);
            selectAreaState=0;
            if(fc.ftemp.fp!=null)
              npMain.depthIndicator.setValue(
                 fc.ftemp.fp.fig.depth);

            }
          }
          fc.ftemp.mouseUp(x,y);
       }       
  }
    public void mouseMove(java.awt.event.MouseEvent e, int x, int y)
    {
        // mes.append("edispatch mouseDown("+x+","+y+")\n");{
        if(fc.isPlaying()){
        }
        else{
          if(state.equals("move")) fc.ftemp.mouseMove2(x,y);
          else                     fc.ftemp.mouseMove(x,y);
        }
    }
    public void mouseExit(java.awt.event.MouseEvent e)
    {
  //      mes.append("edispatch mouseExit\n");
         fc.ftemp.mouseExit();
     }
    public void mouseEnter(java.awt.event.MouseEvent e, int x, int y)
    {
  //       mes.append("edispatch mouseEnter("+x+","+y+")\n");
         fc.ftemp.mouseEnter(x,y);
    }
    public void mouseDrag(java.awt.event.MouseEvent e, int x, int y)
    {
        // mes.append("edispatch mouseDrag("+x+","+y+")\n");
        if(fc.isPlaying()){
        }
        else{
          if(state.equals("new")){
              fc.ftemp.mouseDrag(x,y);
              return;
              }
          if(state.equals("select")){
              if(selectAreaState==1){
                  if(x>aX1) aX2=x;
                  if(y>aY1) aY2=y;
              }
              else mouseDown(e,x,y);
              return;
          }
          return;
        }
     }
    public void mouseDown(java.awt.event.MouseEvent e, int x, int y)
    {
 //       mes.append("edispatch mouseDown("+x+","+y+")\n");
      if(fc.isPlaying()){
           fc.fs.mouseDown(x,y);
      }
      else{
          if(state.equals("select")) {
            fc.fs.mouseDown(x,y);
            selectAreaState=1;
            aX1=x; aY1=y;
            if(fc.ftemp.fp!=null)
              npMain.depthIndicator.setValue(
                 fc.ftemp.fp.fig.depth);
          }
          if(state.equals("move")) fc.ftemp.mouseDown2(x,y);
          else fc.ftemp.mouseDown(x,y);
      }
     }
    public NodeSettings getNodeSettings(){
    	return this.npMain.getNodeSettings();
    }
}

