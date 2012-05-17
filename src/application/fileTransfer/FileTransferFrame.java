/*
		A basic implementation of the JFrame class.
*/
package application.fileTransfer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.*;

import application.autoUpdating.AutoUpdating;

import nodesystem.*;
import util.BinaryData;
import controlledparts.*;
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
public class FileTransferFrame extends ControlledFrame 
implements FrameWithControlledPane, 
             FrameWithControlledTextAreas, 
             FrameWithControlledButton
{
	
	//	public FileFrame fileFrame;
    JFileChooser fromFileChooser=null;
    JFileChooser toFileChooser=null;
    String fileName="";
    Vector panes=null;
    QA qa=null;

	public void enterMouseOnTheText(int id, int x, int y) {
		ControlledTextArea t=(ControlledTextArea)(this.textAreas.elementAt(id));
		t.enterMouse(x,y);		
	}

	public void exitMouseOnTheText(int id, int x, int y) {
		ControlledTextArea t=(ControlledTextArea)(this.textAreas.elementAt(id));
		t.exitMouse();
	}
	
	public void focusButton(int i)
	{
			SelectedButton button=(SelectedButton)(editButtons.elementAt(i));
//			  button.controlledButton_mouseEntered(null);
			button.focus();
	}
	
	public void unfocusButton(int i)
	{
			SelectedButton button=(SelectedButton)(editButtons.elementAt(i));
//			  button.controlledButton_mouseExited(null);
			button.unFocus();
   }
	public void moveMouseOnTheText(int id, int x, int y) {
		ControlledTextArea t=(ControlledTextArea)(this.textAreas.elementAt(id));
		t.moveMouse(x,y);	
	}

	public void pressMouseOnTheText(int id, int p, int x, int y) {
		ControlledTextArea t=(ControlledTextArea)(this.textAreas.elementAt(id));
		t.pressMouse(p,x,y);		
	}

/*
	public void whenActionButtonPressed(EditDialog d) {
		String dname=d.getDialogName();
		String urlHead="file:";
		String separator=""+System.getProperty("file.separator");
		if(separator.equals("\\"))
				urlHead=urlHead+"///";
			else
				urlHead=urlHead+"//";
		String fileName=d.getText();
		if(dname.equals("input common file name:")){
			urlHead=fileName;
			urlField.setText(urlHead);
		}
		else
		if(dname.equals("input user file name:")){
			urlField.setText(urlHead+fileName);
		}
		else
		if(dname.equals("output user file name:")){
			String url="";
//		String fileName=d.getText();
			File userDataPath=communicationNode.userDataDir;   		
			if(!userDataPath.exists()) 
					 userDataPath.mkdir();
			return;
		}
		else
		if(dname.equals("output common file name:")){
			if(!this.communicationNode.getControlMode().equals("teach")) return;
			return;
		}
		else
		if(dname.equals("url:")){
           
//			  String url=d.getText();
			urlField.setText(fileName);
			NetworkReader nr=new NetworkReader(fileName,this.communicationNode.getEncodingCode());
			String readingText=nr.loadAndWait();
	   // 	editdispatch.read(url);
			if(readingText.equals("")) return;
			language.CQueue cq= new language.CQueue();
			JTextArea t=new JTextArea();
			language.ALisp lisp=new language.ALisp(t,null,cq,null);
			HtmlParser hp=new HtmlParser(readingText,lisp);
            
		}
	}
*/

	public void clickMouseOnTheText(int id, int p, int x, int y) {
		ControlledTextArea t=(ControlledTextArea)(this.textAreas.elementAt(id));
		t.clickMouse(p,x,y);	
	}

	public void dragMouseOnTheText(int id, int position, int x, int y) {
		ControlledTextArea t=(ControlledTextArea)(this.textAreas.elementAt(id));
		t.dragMouse(position,x,y);	
	}

	public void releaseMouseOnTheText(int id, int position, int x, int y) {
		ControlledTextArea t=(ControlledTextArea)(this.textAreas.elementAt(id));
		t.releaseMouse(position,x,y);
	}

	public void typeKey(int id, int p, int key) {
		ControlledTextArea t=(ControlledTextArea)(this.textAreas.elementAt(id));
		t.typeKey(p,key);	
	}


	public String toStrConst(String x)
	{
		String sx="";
		int i=0;
		int len=x.length();
		while(i<len){
			char c=x.charAt(i);
//			  System.out.println("c="+c+":"+(int)c);
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
//			  System.out.println(sx);
		}

		String rtn=sx;
		return rtn;
	}
	
	String commandName="transfer";
	public String getCommandName(){
		return commandName;
	}

	public void sendEvent(String s)
	{
		if(this.communicationNode==null) return;
		this.communicationNode.sendEvent(commandName,s);
	}

    public void sendEvent(AMessage m){
		if(this.communicationNode==null) return;
//		System.out.println(commandName+"-"+m.getHead());
		this.communicationNode.sendEvent(commandName,m);		
   	
    }


	public String label;

//---------------------------------------------

	BinaryData bin = null;

	private Object bcount;
	private javax.swing.JLabel JLabel4;
	private ControlledScrollPane controlledScrollPane3=new ControlledScrollPane();
	private ControlledScrollPane controlledScrollPane2=new ControlledScrollPane();
	public ControlledTextArea copyToFileNameField=new controlledparts.ControlledTextArea();//SAKURAGI CHANGED
	private ControlledButton copyToButton;


	//SAKURAGI ADDED START
	boolean jarFlug=false;
	boolean updateFlug=false;
	public void updateFlugChk(){
		updateFlug=true;
	}
	
	AutoUpdating au;
//	CommunicationNode communicationNode;     // this is defined at ControlledFrame, super class of this
	public void sendVerUpBinalyToAll(AutoUpdating au,CommunicationNode communicationNode){
		updateFlug=true;
		this.au=au;
		this.communicationNode=communicationNode;
		sendBinalyToAll();
		this.endVerUp();
	}
	
	//SAKURAGI ADDED END	
	public void sendBinalyToAll(String s){
		if(s.equals("update"))updateFlug=true;
		sendBinalyToAll();
	}
	//SAKURAGI ADDED END
	
    int xBuffSize=1000;
    byte[] buf=new byte[xBuffSize];
    public void sendBinalyToAll()
    {
        if(!this.isControlledByLocalUser()) return;
        this.outputArea.setText("start ");
        this.recordMessage("send preperation");
        String sending="send.start()\n";
        this.sendEvent(sending);
        String path = this.copyFromFileNameField.getText();
        this.recordMessage("\"path\",\""+path+"\"");
    	long fsize=getFileSize(path);
        this.recordMessage("\"data-length\","+fsize);
        this.outputArea.append("sending "+fsize+" bytes.\n");
        this.repaint();
//      copy the file to myself.
        if(!updateFlug)this.startReceiveData();//SAKURAGI CHANGED
		try{
			int data;   
			// ファイル入力ストリームを取得
			FileReader fr = new FileReader(path);

//----------------------------------------
//			DataInputStreamクラス宣言
			DataInputStream dis = null;
			int buffSize=0;
			if(this.qa==null){
		        buffSize=calculateBufferSize(path);
			}
			else{
				if(qa.isAnswer("usingEquation")){
					buffSize=calculateBufferSize(path);
				}
				else{
					int pn=qa.getAnswer("partitioningNumber");
					buffSize=calculateBufferSize2(path,pn);
				}
			}
			this.recordMessage("\"buffersize\","+buffSize);
			if(buffSize>xBuffSize){
			    buf=new byte[buffSize];
			    xBuffSize=buffSize;
			}
//			DataInputStreamクラス生成
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
//			ファイルの読み込み
      		int x;
      		int sentBytes=0;
      		this.recordMessage("\"start-send\"");
      		int startTime=this.getTimeNow();
      		this.startTimeField.setText(""+startTime);
			while ((x = dis.read(buf,0,buffSize))!=-1){
				sending = "send.put(binary)\n";
				AMessage am=new AMessage();
				am.setHead(sending);
				am.setData(buf,x);
				this.sendEvent(am);
				/*
				bin.setLength(x);
				String out = bin.bin2str();
				sending = "send.put("+"\""+out+"\")\n";
				this.sendEvent(sending);
				// copy the file to myself
				 * 
				 */
				if(!updateFlug)fw.write(buf,0,x);//SAKURAGI CHANGED
				sentBytes=sentBytes+x;
				this.outputArea.append(""+sentBytes+"bytes sent.\n");
				this.repaint();
			}
			
//			DataInputStreamクラスを閉じる
			dis.close();
			int endTime=this.getTimeNow();
			this.endTimeField.setText(""+endTime);
			this.durationTimeField.setText(""+(endTime-startTime));
			this.recordMessage("\"end-send\"");

//----------------------------------------
			
		}
		catch(IOException ex)
		{
			System.out.println("入出力エラー！");
			ex.printStackTrace();
		}
        sending="send.end()\n";
		this.sendEvent(sending);
		this.endData();
    }
    
    int defaultMaxBufferSize=20000000; // max 20MB
    public int getMaxBufferSize(){
		int pn=qa.getAnswer("maxBufferSize");
		if(pn>0) return pn;
		else return defaultMaxBufferSize;
    }
    
    public int calculateBufferSize(String path){
    	long fsize=getFileSize(path);
    	//t0=sqrt(8sb(i-2)/(w(db+c)))
    	double w=100.0E6;
    	double dh=2.0E-3;
    	double c=4.0E-3;
    	double b=2.0;
    	int ti=getTreeHeight();
    	double t0=Math.sqrt(8.0*fsize*b*(ti-2)/(w*(b*dh+c)));
    	int t=(int)(Math.round(t0));
    	if(t<1)t=1;
    	int x=(int)(Math.round(fsize/t));
    	int minsize=10;
//    	int maxsize=200000000;
    	int maxsize=this.getMaxBufferSize();
    	if(x<minsize)x=minsize;
    	if(x>maxsize)x=maxsize;
    	this.recordMessage("\"partition number\","+t);
     	return x;
    }
    public int calculateBufferSize2(String path, int t){
    	long fsize=getFileSize(path);
    	if(t<1)t=1;
    	int x=(int)(Math.round(fsize/t));
    	int minsize=10;
//    	int maxsize=200000000;
    	int maxsize=this.getMaxBufferSize();
    	if(x<minsize)x=minsize;
    	if(x>maxsize)x=maxsize;
    	this.recordMessage("\"partition number\","+t);
    	return x;
    }
    public int getTreeHeight(){
    	if(this.communicationNode==null) return 2;
    	if(this.communicationNode.nodeController==null) return 2;
    	int n=this.communicationNode.nodeController.getNodeNumber();
    	double x=Math.log((double)n+1)/Math.log(2.0);
    	if(x<2.0) x=2.0;
    	return (int)x;
    }
    public long getFileSize(String path){
    	long size=0;
    	try{
    		File file = new File(path);
    	    
    	    // Get the number of bytes in the file
    	    size = file.length();
    	}
    	catch(Exception e){
    	}
    	return size;
    }
 
	DataOutputStream fw = null;
	private JTextField endTimeField;
	private JTextField durationTimeField;
	private JLabel jLabel3;
	private JLabel jLabel2;
	private JTextField startTimeField;
	private JLabel jLabel1;

	int receiveStartTime;
	
 	public void startReceiveData()
 	{
 		System.out.println("送信");
		bin = new BinaryData();
		String commondir=this.communicationNode.commonDataDir.toString();
		String separator=""+System.getProperty("file.separator");
		String outputFile=commondir+separator+this.copyToFileNameField.getText();
		if(updateFlug)outputFile=this.copyToFileNameField.getText();//SAKURAGI CHANGED
		System.out.println(outputFile);
		if(!this.isSending()){
		   this.outputArea.setText("start transfer "+outputFile+".\n");
		   this.outputArea.repaint();
		}
 		try{
			fw = new DataOutputStream(new FileOutputStream(outputFile));		
 		}
		catch(IOException ex)
		{
			this.outputArea.append("file open error:"+ex.toString());
		}
		if(!this.isSending()) {
           this.recordMessage("\"receive-start\"");
           this.receiveStartTime=this.getTimeNow();
           this.startTimeField.setText(""+this.receiveStartTime);
		}
 	}
 
	public void receiveEvent(String s)
	{
		// This method is derived from interface Spawnable
		// to do: code goes here
//		if(!this.communicationNode.isReceivingEvents) return;
		if(!this.isReceiving()) return;
		InputQueue iq=null;
		try{
		   BufferedReader dinstream=new BufferedReader(
//				new InputStreamReader(
//				new StringBufferInputStream(s),encodingCode)
				new StringReader(s)
		   );
		   iq=new InputQueue(dinstream);
		}
//		  catch(UnsupportedEncodingException e){
		catch(Exception e){
			System.out.println("exception:"+e);
			return;
		}
		if(iq==null) return;
			ParseFileTransferEvent evParser=new ParseFileTransferEvent(this,iq);
			evParser.parsingString=s;
			evParser.run();
	}
	public void receiveEvent(AMessage m)
	{
		// This method is derived from interface Spawnable
		// to do: code goes here
//		if(!this.communicationNode.isReceivingEvents) return;
		if(!this.isReceiving()) return;
		InputQueue iq=null;
		String s=m.getHead();
		try{
		   BufferedReader dinstream=new BufferedReader(
//				new InputStreamReader(
//				new StringBufferInputStream(s),encodingCode)
				new StringReader(s)
		   );
		   iq=new InputQueue(dinstream);
		}
//		  catch(UnsupportedEncodingException e){
		catch(Exception e){
			System.out.println("exception:"+e);
			return;
		}
		if(iq==null) return;
			ParseFileTransferEvent evParser=new ParseFileTransferEvent(this,iq);
			evParser.setBinary(m.getData());
			evParser.parsingString=s;
			evParser.run();
	} 
	public String parseCommand(String command){
		/*
		communication flow
		Basic at this node
		    --ask information of this node ---> FileTransferFrame ---the data-+
		                                                            <----------+
		Basic at this node
		    --ask infromation of down side nodes ---> FileTransferFrame
		    if there is a down side node 
		      ---> nextNodeController at this node
		      --- to down node -->
		      ---> nextNodeController at down node   ---->  Basic at a down side node
		       --- recursive -->
		     nextNodeController at this node
		       ---requeste data ---> Basic at this node
		      ---- calculation   ---
		      print the result
		     if there is the up side node
     		     Basic at this node -- the result ----> 
     		     nextNodeController at this node ---->
     		     nextNodeController at up side node --->
     		     Basic at up side node
		
		      
		    --ask information of this node ---> FileTransferFrame ---the data-+
		                                                            <----------+
		Basic at this node
		    --ask infromation of down side nodes ---> FileTransferFrame
		    if there is a down side node 
		      ---> nextNodeController this node
		      --- to down node -->
		      ---> nextNodeController at down node   ---->
	    
		       --> FileTransferFrame
		      ---> Basic (communication) 
		      ---> nextNodeController at down node
		 
		 
		command:
		  ... from basic node
		  request start-time
		  request end-time
		  request duration
		  ... from basic interpreter
		  request-down [0/1] <application> start-time
		  request-down [0/1] <application> end-time
		  request-down [0/1] <application> duration
		  
		  ... from down node
		  return-up <application> start-time
		  return-up <application> end-time
		  return-up <application> duration

		*/
		if(command.startsWith("request ")){
			String subc1=command.substring("request ".length());
			if(subc1.startsWith("start-time")){
				String x=this.startTimeField.getText();
				return x;
			}
			else
			if(subc1.startsWith("end-time")){
				String x=this.endTimeField.getText();
				return x;
				
			}
			else
			if(subc1.startsWith("duration")){
				String x=this.durationTimeField.getText();
				return x;
				
			}
		}

		return "na";

	}

	public void receiveData(String s)
 	{
 		bin.string2bin(s);

 		try{
 		
 		fw.write(bin.getByteArray(),0,bin.getLength());
 		}
		catch(IOException ex)
		{
			this.outputArea.setText("write error:"+ex.toString());
		}
 	}
	int receivedsize;
 	public void receiveData(byte[] s){
 		try{
 		    fw.write(s,0,s.length);
 		    this.receivedsize=receivedsize+s.length;
// 		    this.messageArea.append(""+receivedsize+" bytes are received.\n");
// 		    this.repaint();
 		}
		catch(IOException ex)
		{
			this.outputArea.append("write error:"+ex.toString()+"\n");
		}
		this.recordMessage(""+this.receivedsize+",\"bytes received\"");
		
 	} 	
 	public void endVerUp()
 	{
		try{
	 		//SAKURAGI CHANGED START---
			if(fw!=null){
//				fw.close();
				String ver=this.copyToFileNameField.getText();
				ver=ver.substring(ver.lastIndexOf("_")+1,ver.length());
				ver=ver.substring(0,ver.indexOf(".zip"));
				if(!CommunicationNode.thisVer.equals(ver)){
					//バージョンアップ
					AutoUpdating au=new AutoUpdating(communicationNode);
					au.jarFlug=this.jarFlug;
					au.unzip(this.copyToFileNameField.getText());
		        
					//再起動
					/*
					try {
						Process p=Runtime.getRuntime().exec(System.getProperty("user.dir")+File.separator+"student.bat");
					}catch (IOException e){e.printStackTrace();}
					System.exit(0);
					*/
				}
		   }
	        //SAKURAGI CHANGED END------
		}
		catch(Exception ex)
		{
			this.outputArea.append("error:"+ex.toString());
		}
 	}
 	public void endData(){
 		try{
 		if(fw!=null){
 			fw.close();
 		}
 		}
 		catch(IOException ex){
			this.outputArea.append("close error:"+ex.toString());			
 		}
 		
        this.outputArea.append("end transfer.\n");
        if(!this.isSending()){
        	int x=this.getTimeNow();
        	this.endTimeField.setText(""+x);
        	this.durationTimeField.setText(""+(x-this.receiveStartTime));
            this.recordMessage("\"receive-end\"");
        }
 	}
 	
 	void sendint(int x)
 	{
 		
 	}
 
 
	int currentIndex;
	


	File logBasePath;
	String encodingCode;
	

	boolean isBlockOnMemory0Updated;
	boolean isBlockOnMemory1Updated;
	boolean isBlockOnMemory2Updated;
	int blockSize;
	int currentBlockOnMemory0;
	int currentBlockOnMemory1;
	int currentBlockOnMemory2;

	Vector stringsBuffer1;
	Vector stringsBuffer2;
	public Vector stringsBuffer0;


//-----------------------------------------

	public void clickButton(int i) {
		SelectedButton t=(SelectedButton)(editButtons.elementAt(i));
		t.click();
		this.mouseClickedAtButton(i);
	}
	
	public void mouseClickedAtButton(int i) 
	{
		SelectedButton t=(SelectedButton)(editButtons.elementAt(i));

		if(i==this.exitButton.getID()){
			exitThis();
		}
		
		if(i==this.copyFromButton.getID()){
			if(!this.isControlledByLocalUser()) return;
			if(this.fromFileChooser==null){
				this.fromFileChooser=new JFileChooser();
			}
			this.fromFileChooser.show(true);
			this.fromFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = this.fromFileChooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = this.fromFileChooser.getSelectedFile();
				String fn=file.toString();
				fileName=file.getName();
				this.copyFromFileNameField.setText(file.toString());
				this.copyFromFileNameField.ControlledTextArea_textPasted();
				/*
				try{
                    this.copyFromFileNameField.setText(file.getCanonicalPath());
				}
				catch(Exception e){
					this.outputArea.setText(e.toString());
				}
				*/
			}	
			this.fromFileChooser.show(false);    

		}       // fileButton
		if(i==this.copyToButton.getID()){
			if(!this.isControlledByLocalUser()) return;
			if(this.toFileChooser==null){
				this.toFileChooser=new JFileChooser();
			}
			String commondir=this.communicationNode.commonDataDir.toString();
			this.copyToFileNameField.setText(commondir);
			this.toFileChooser.setCurrentDirectory(new File(commondir));
//            this.toFileChooser.setSelectedFile(new File(commondir));
			this.toFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			this.toFileChooser.show(true);
			int returnVal = this.toFileChooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = this.toFileChooser.getSelectedFile();
				String fn=file.toString();
				String fx=file.getName();
				//This is where a real application would open the file.
//				  this.fileNameField.setText(fn);
				String separator=""+System.getProperty("file.separator");
				String relativePath="";
				String common=commondir+separator;
				if(fn.length()>common.length()){
					relativePath=fn.substring(common.length());
				   this.copyToFileNameField.setText(relativePath+separator+fileName);
				}
				else
				   this.copyToFileNameField.setText(fileName);
                this.copyToFileNameField.ControlledTextArea_textPasted();
			}	    
			this.toFileChooser.show(false);
			return;
		}
			
		/*
			fileFrame.setListener(this);
			fileFrame.setCommonPath(
				this.communicationNode.commonDataDir.toString());
			fileFrame.setUserPath(
				this.communicationNode.userDataDir.toString());
			fileFrame.setVisible(true);  return;
	    }       // fileButton
	    */
	    if(i==this.sendButton.getID()){
	    	this.startTimeField.setText("");
	    	this.endTimeField.setText("");
	    	this.durationTimeField.setText("");
	    	sendBinalyToAll();
	    	return;
	    }
	    if(i==this.clearOutputButton.getID()){
	    	this.outputArea.setText("");
	    }
	}
	
	public void exitThis()
	{
		this.recordMessage("0,\"exit copy\"");
		this.setVisible(false);
	}
	
//	public BlockedFileManager fileManager;
	
	Vector textAreas;
	
	Vector editButtons;
	
	public FileTransferFrame()
	{
		
		panes=new Vector();
		panes.addElement(this.controlledScrollPane1);
		panes.addElement(this.controlledScrollPane2);
		panes.addElement(this.controlledScrollPane3);
		int numberOfPanes=panes.size();
		for(int i=0;i<numberOfPanes;i++){
			ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(i));
			p.setFrame(this);
			p.setID(i);
		}
		
		textAreas=new Vector();
		textAreas.addElement(copyFromFileNameField);
		textAreas.addElement(this.copyToFileNameField);
		textAreas.addElement(this.outputArea);
		int numberOfTextArea=textAreas.size();
		for(int i=0; i<numberOfTextArea;i++) {
			ControlledTextArea t=(ControlledTextArea)(textAreas.elementAt(i));
			t.setFrame(this);
			t.setID(i);
		}		
		
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		this.setSize(400, 433);
		setVisible(false);
		
		sendButton.setText("Send");
		sendButton.setActionCommand("Send");
		getContentPane().add(sendButton);
		sendButton.setBackground(new java.awt.Color(204,204,204));
		sendButton.setForeground(java.awt.Color.black);
		sendButton.setFont(new Font("Dialog", Font.BOLD, 12));
		sendButton.setBounds(18, 224, 70, 28);
		
		controlledScrollPane1.setOpaque(true);
		controlledScrollPane1.setBounds(91, 252, 259, 91);
		controlledScrollPane1.setViewportView(outputArea);
		outputArea.setBackground(java.awt.Color.white);
		outputArea.setForeground(java.awt.Color.black);
		outputArea.setFont(new Font("SansSerif",Font.PLAIN,12));
		outputArea.setBounds(0, 0, 392, 152);
		outputArea.setPreferredSize(new java.awt.Dimension(50, 44));

		JLabel1.setText("Distributed System Recorder/ Copy");
		getContentPane().add(JLabel1);
		JLabel1.setForeground(new java.awt.Color(102,102,153));
		JLabel1.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel1.setBounds(5, 4, 324, 36);
		
		JLabel2.setText("Copy the file from this node to the common dir of all nodes.");
		getContentPane().add(JLabel2);
		JLabel2.setForeground(new java.awt.Color(102,102,153));
		JLabel2.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel2.setBounds(15, 53, 359, 20);
		
		JLabel3.setText("message");
		getContentPane().add(JLabel3);
		JLabel3.setForeground(new java.awt.Color(102,102,153));
		JLabel3.setFont(new Font("Dialog", Font.BOLD, 12));
		JLabel3.setBounds(19, 266, 60, 36);
		
		copyFromFileNameField.setSelectionColor(new java.awt.Color(204,204,255));
		copyFromFileNameField.setSelectedTextColor(java.awt.Color.black);
		copyFromFileNameField.setCaretColor(java.awt.Color.black);
		copyFromFileNameField.setBorder(etchedBorder1);
		copyFromFileNameField.setDisabledTextColor(new java.awt.Color(153,153,153));
		copyFromFileNameField.setBackground(java.awt.Color.white);
		copyFromFileNameField.setForeground(java.awt.Color.black);
		copyFromFileNameField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		copyFromFileNameField.setBounds(3, 4, 280, 46);
        {
		copyFromButton.setActionCommand("from");
		copyFromButton.setToolTipText("choose the from file");
		getContentPane().add(copyFromButton);
		copyFromButton.setBackground(new java.awt.Color(204,204,204));
		copyFromButton.setForeground(java.awt.Color.black);
		copyFromButton.setFont(new Font("Dialog", Font.BOLD, 12));
		copyFromButton.setBounds(15, 111, 74, 47);
		copyFromButton.setText("from");
//		copyFromButton.setIcon(new ImageIcon("images/file-icon.GIF"));
        }
        {
		exitButton.setActionCommand("exit");
		exitButton.setToolTipText("close and exit");
		getContentPane().add(exitButton);
		exitButton.setBackground(new java.awt.Color(204,204,204));
		exitButton.setForeground(java.awt.Color.black);
		exitButton.setFont(new Font("Dialog", Font.BOLD, 12));
		exitButton.setBounds(308, 15, 72, 24);
		exitButton.setText("exit");
		this.exitButton.setIcon(new ImageIcon("images/exit-icon.GIF"));
        }
		
		{
			copyToButton = new ControlledButton();
			this.getContentPane().add(copyToButton);
			copyToButton.setText("To");
			copyToButton.setBounds(17, 160, 72, 46);
		}
		{
			this.getContentPane().add(controlledScrollPane1);
//			controlledScrollPane1.setBounds(91, 258, 279, 56);
			this.getContentPane().add(controlledScrollPane2);
			controlledScrollPane2.setBounds(90, 112, 281, 46);
			controlledScrollPane2.setOpaque(true);
			controlledScrollPane2.getViewport().add(copyFromFileNameField);
		}
		{
			copyFromFileNameField.setBounds(0, 0, 276, 42);
//			copyFromFileNameField.setPreferredSize(new java.awt.Dimension(0, 0));

		}
		{
			controlledScrollPane3.setOpaque(true);
			this.getContentPane().add(controlledScrollPane3);
			controlledScrollPane3.setBounds(91, 162, 279, 44);
			{
//				copyToFileNameField = new ControlledTextField();
				controlledScrollPane3.getViewport().add(copyToFileNameField);
				copyToFileNameField.setBounds(0, 0, 275, 40);
				copyToFileNameField.setBackground(
					new java.awt.Color(255, 255, 255));

//				copyToFileNameField.setPreferredSize(new java.awt.Dimension(276, 41));
			}
		}
		{
			JLabel4 = new JLabel();
			this.getContentPane().add(JLabel4);
			JLabel4.setText("Click the \"from\" and \"to\" button to choose the file and dir.");
			JLabel4.setBounds(15, 81, 347, 22);
			JLabel4.setForeground(new java.awt.Color(102,102,153));
		}
		{
			jLabel1 = new JLabel();
			getContentPane().add(jLabel1);
			jLabel1.setText("start");
			jLabel1.setBounds(21, 359, 77, 21);
		}
		{
			startTimeField = new JTextField();
			getContentPane().add(startTimeField);
			startTimeField.setBounds(56, 359, 77, 21);
		}
		{
			jLabel2 = new JLabel();
			getContentPane().add(jLabel2);
			jLabel2.setText("end");
			jLabel2.setBounds(140, 359, 77, 21);
		}
		{
			endTimeField = new JTextField();
			getContentPane().add(endTimeField);
			endTimeField.setBounds(168, 359, 77, 21);
		}
		{
			jLabel3 = new JLabel();
			getContentPane().add(jLabel3);
			jLabel3.setText("duration");
			jLabel3.setBounds(259, 359, 77, 21);
		}
		{
			durationTimeField = new JTextField();
			getContentPane().add(durationTimeField);
			durationTimeField.setBounds(308, 359, 77, 21);
		}
		{
			getContentPane().add(clearOutputButton);
			clearOutputButton.setText("clear");
			clearOutputButton.setBounds(21, 301, 63, 28);
		}

		//}}
		this.setTitle("CopyFrame");

		editButtons=new Vector();
		editButtons.addElement(exitButton);
		editButtons.addElement(this.copyFromButton);
		editButtons.addElement(sendButton);
		editButtons.addElement(this.copyToButton);
		editButtons.addElement(this.clearOutputButton);
		int numberOfButtons=editButtons.size();
		for(int i=0;i<numberOfButtons;i++){
			SelectedButton button=(SelectedButton)(editButtons.elementAt(i));
			button.setFrame(this);
			button.setID(i);
		}
		
		//{{INIT_MENUS
		//}}
	}

	public FileTransferFrame(String sTitle)
	{
		this();
		setTitle(sTitle);
	}

	public void setVisible(boolean b)
	{
		if (b)
			setLocation(50, 50);
			{
//				controlledScrollPane2 = new ControlledScrollPane();
			}
		super.setVisible(b);
	}
	public void setQA(QA q){
        this.qa=q; 
	}
	static public void main(String args[])
	{
		(new FileTransferFrame()).setVisible(true);
	}
	
	public ControlledFrame spawnMain(CommunicationNode gui, String args, int pID, String controlMode)
	{
		// This method is derived from interface Spawnable
		// to do: code goes here
//			 NetworkTesterMasterFrame frame=new NetworkTesterMasterFrame("NetworkTester Worker");
		   FileTransferFrame frame=this;
		   frame.communicationNode=gui;
		   frame.setTitle("FileTransferFrame");
		   frame.pID=pID;
//			 frame.communicationNode.encodingCode=encodingCode;
		   frame.ebuff=gui.commandTranceiver.ebuff;
		   frame.setQA(this.communicationNode.getQA("partitioning"));
//			 frame.show();
		   frame.setVisible(true);
		   return frame;
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

	public int pID;



	//{{DECLARE_CONTROLS
	controlledparts.ControlledScrollPane controlledScrollPane1 = new controlledparts.ControlledScrollPane();
	controlledparts.ControlledTextArea outputArea = new controlledparts.ControlledTextArea();
	controlledparts.ControlledButton copyFromButton = new controlledparts.ControlledButton();
	public controlledparts.ControlledButton sendButton = new controlledparts.ControlledButton();
	public controlledparts.ControlledButton exitButton = new controlledparts.ControlledButton();
	private controlledparts.ControlledButton clearOutputButton= new controlledparts.ControlledButton();
	
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	public controlledparts.ControlledTextArea copyFromFileNameField = new controlledparts.ControlledTextArea();//SAKURAGI CHANGED
    EtchedBorder etchedBorder1= new EtchedBorder();

	
	/* (非 Javadoc)
	 * @see nodesystem.DialogListener#getDialogs()
	 */
	public Vector getDialogs() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}



	/* (非 Javadoc)
	 * @see nodesystem.DialogListener#getDefaultPath()
	 */
	public File getDefaultPath() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/* (非 Javadoc)
	 * @see nodesystem.DialogListener#whenCancelButtonPressed(nodesystem.EditDialog)
	 */
	public void whenCancelButtonPressed(EditDialog d) {
		// TODO 自動生成されたメソッド・スタブ
		
	}



	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledPane#changeScrollbarValue(int, int, int)
	 */
	public void changeScrollbarValue(int paneID, int barID, int value) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(paneID));
		p.setScrollBarValue(barID,value);
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledPane#showScrollBar(int, int)
	 */
	public void showScrollBar(int paneID, int barID) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(paneID));
		p.showScrollBar(barID);
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledPane#hideScrollBar(int, int)
	 */
	public void hideScrollBar(int paneID, int barID) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(paneID));
		p.hideScrollBar(barID);
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledPane#scrollBarHidden(int, int)
	 */
	public void scrollBarHidden(int paneID, int barId) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledPane#scrollBarShown(int, int)
	 */
	public void scrollBarShown(int paneID, int barID) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledPane#scrollBarValueChanged(int, int, int)
	 */
	public void scrollBarValueChanged(int paneID, int barID, int v) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#setTextOnTheText(int, java.lang.String)
	 */
	public void setTextOnTheText(int i, int pos, String s) {
		// TODO 自動生成されたメソッド・スタブ
        ControlledTextArea t=(ControlledTextArea)(textAreas.elementAt(i));
		t.setTextAt(pos,s);
		
	}





	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mouseExitAtTheText(int, int, int)
	 */
	public void mouseExitAtTheText(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mouseEnteredAtTheText(int, int, int)
	 */
	public void mouseEnteredAtTheText(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}



	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mouseMoveAtTextArea(int, int, int)
	 */
	public void mouseMoveAtTextArea(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}



	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mousePressedAtTextArea(int, int, int, int)
	 */
	public void mousePressedAtTextArea(int i, int p, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}





	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mouseReleasedAtTextArea(int, int, int, int)
	 */
	public void mouseReleasedAtTextArea(int id, int position, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mouseDraggedAtTextArea(int, int, int, int)
	 */
	public void mouseDraggedAtTextArea(int id, int position, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}





	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mouseClickedAtTextArea(int, int, int, int)
	 */
	public void mouseClickedAtTextArea(int i, int p, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#keyIsTypedAtATextArea(int, int, int)
	 */
	public void keyIsTypedAtATextArea(int i, int p, int key) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

    public void keyIsPressedAtATextArea(int i, int p, int key) {
	    // TODO 自動生成されたメソッド・スタブ
		
    }

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledButton#mouseExitedAtButton(int)
	 */
	public void mouseExitedAtButton(int i) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledButton#mouseEnteredAtButton(int)
	 */
	public void mouseEnteredAtButton(int i) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	//}}

	//{{DECLARE_MENUS
	//}}

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
		ControlledTextArea t=(ControlledTextArea)(this.textAreas.elementAt(i));
		t.releaseKey(p, code);	
	}

    public void pressKey(int i, int p, int code) {
	    // TODO 自動生成されたメソッド・スタブ
	    ControlledTextArea t=(ControlledTextArea)(this.textAreas.elementAt(i));
	    t.pressKey(p, code);	
    }
    public void recordMessage(String s)
    {
        if(communicationNode==null) return;
        communicationNode.eventRecorder.recordMessage("\"FileTransferFrame\","+s);
    }
    public int getMiliTime(){
    	return this.getTimeNow();
    }
}