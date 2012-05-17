package application.draw;
import java.applet.AppletContext;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JEditorPane;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import language.HtmlTokenizer;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;


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
public class SaveButtonDebuggFrame extends JFrame
{
	private JButton saveButton;
	private String baseUrl;
	private String pageName;
	private String plugInName;
	private String pageCharSet;
	private String charset;

	private void initGUI() {
		try {
			{
				getContentPane().setLayout(null);
			}
			{
				saveButton = new JButton();
				getContentPane().add(saveButton);
				saveButton.setText("save");
				saveButton.setBounds(0, 0, 75, 31);
				saveButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						saveButtonActionPerformed(evt);
					}
				});
			}
			{
				clearButton = new JButton();
				getContentPane().add(clearButton);
				clearButton.setText("clear message");
				clearButton.setBounds(75, 0, 132, 31);
				clearButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						clearButtonActionPerformed(evt);
					}
				});
			}
			{
				messagePane = new JScrollPane();
				getContentPane().add(messagePane);
				messagePane.setBounds(5, 107, 527, 502);
				{
					messageTextArea = new JTextArea();
					messagePane.setViewportView(messageTextArea);
					messageTextArea.setBounds(236, 172, 61, 12);
					messageTextArea.setPreferredSize(new java.awt.Dimension(521, 501));
					messagePane.setAutoscrolls(true);
				}
			}
			{
				inputField = new JTextField();
				getContentPane().add(inputField);
				inputField.setBounds(4, 66, 455, 39);
			}
			{
				sendButton = new JButton();
				getContentPane().add(sendButton);
				sendButton.setText("send");
				sendButton.setBounds(459, 66, 73, 38);
				sendButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						sendButtonActionPerformed(evt);
					}
				});
			}
			{
				urlField = new JTextField();
				getContentPane().add(urlField);
				urlField.setBounds(5, 32, 454, 34);
			}
			{
				connectButton = new JButton();
				getContentPane().add(connectButton);
				connectButton.setText("con");
				connectButton.setBounds(459, 31, 74, 36);
				connectButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						connectButtonActionPerformed(evt);
					}
				});
			}
			{
				editPageButton = new JButton();
				getContentPane().add(editPageButton);
				editPageButton.setText("edit");
				editPageButton.setBounds(285, 0, 75, 31);
				editPageButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						editPageButtonActionPerformed(evt);
					}
				});
			}
			{
				updateButton = new JButton();
				getContentPane().add(updateButton);
				updateButton.setText("update");
				updateButton.setBounds(360, 0, 84, 31);
				updateButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						updateButtonActionPerformed(evt);
					}
				});
			}
			{
				paramButton = new JButton();
				getContentPane().add(paramButton);
				paramButton.setText("params");
				paramButton.setBounds(444, 0, 91, 31);
				paramButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						paramButtonActionPerformed(evt);
					}
				});
			}
			{
				readFigButton = new JButton();
				getContentPane().add(readFigButton);
				readFigButton.setText("read");
				readFigButton.setBounds(207, 1, 78, 30);
				readFigButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						readFigButtonActionPerformed(evt);
					}
				});
			}
			{
				this.setSize(551, 650);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveButtonActionPerformed(ActionEvent evt) {
//		System.out.println("saveButton.actionPerformed, event="+evt);
		//TODO add your code for saveButton.actionPerformed
		this.editPageButtonActionPerformed(null);
		this.updateButtonActionPerformed(null);
	}
	PukiwikiApplet applet;
	private JButton clearButton;
	private JTextArea messageTextArea;
	private JButton readFigButton;
	private JButton paramButton;
	private JButton updateButton;
	private JScrollPane messagePane;
	private JButton editPageButton;
	private JButton connectButton;
	private JTextField urlField;
	private JButton sendButton;
	private JTextField inputField;
	public SaveButtonDebuggFrame(PukiwikiApplet a){
//		System.out.println("y1");
		this.applet=a;
//		this.messageTextArea.append("documentBase="+this.applet.getDocumentBase().toString()+"\n");
//		System.out.println("y2");
		this.initGUI();
		this.setName("saveButtonFrame");
		try{
			StringTokenizer st=new StringTokenizer(applet.param1,"=");
    		String dmy=st.nextToken();
	    	this.plugInName=st.nextToken();
		    this.messageTextArea.append("plugInName="+plugInName+"\n");
		}
		catch(Exception e){
			this.plugInName="";
		}
/*			
		HttpClient client=new HttpClient();
		PostMethod post=new PostMethod(applet.returnUrl);
*/
		
		String dbase=this.applet.getDocumentBase().toString();	
		try{
		   StringTokenizer st=new StringTokenizer(dbase,"?");
		   baseUrl=st.nextToken();
		   pageName=st.nextToken();
		}
		catch(Exception e){
		}
        this.charset=applet.charset;
        if(this.charset==null){
        	this.charset="UTF-8";
        }
        if(this.charset.equals("")){
        	this.charset="UTF-8";
        }

	}
		
	private void clearButtonActionPerformed(ActionEvent evt) {
		System.out.println("clearButton.actionPerformed, event="+evt);
		//TODO add your code for clearButton.actionPerformed
		this.messageTextArea.setText("");
	}
	
	HttpClient client=null;
	
	private void connectButtonActionPerformed(ActionEvent evt) {
		System.out.println("connectButton.actionPerformed, event="+evt);
		//TODO add your code for connectButton.actionPerformed
		
		client=new HttpClient();
		String url=this.urlField.getText();
		this.messageTextArea.append(url+"\n");
		try{
    		HttpMethod method = new GetMethod(url);
//	    	method.getParams().setContentCharset("UTF-8");
			int status=client.executeMethod(method);
		    if (status != HttpStatus.SC_OK) {
		          System.err.println("Method failed: " + method.getStatusLine());
		    }
		    else{
//    		String txt=method.getResponseBodyAsString();
     		    InputStream is=method.getResponseBodyAsStream();
     		    InputStreamReader isr=new InputStreamReader(is,this.charset);
//	     	    InputStreamReader isr=new InputStreamReader(is,"UTF-8");
//     		    InputStreamReader isr=new InputStreamReader(is);
		         BufferedReader br=new BufferedReader(isr);
		        String line="";
		        while(true){
		    	   line=br.readLine();
		        	if(line==null) break;
		    	    this.messageTextArea.append(line+"\n");
//		    	    System.out.println(line);
		        }
		    }
		}
		catch(Exception e){
			this.messageTextArea.append(e.toString()+"\n");
			e.printStackTrace();
		}
		
	}
	
	private void sendButtonActionPerformed(ActionEvent evt) {
		System.out.println("sendButton.actionPerformed, event="+evt);
		//TODO add your code for sendButton.actionPerformed
	}
	String updateText;
	String actionUrl;
	String editCmd;
	String editPage;
	String editDigest;
	String editWriteValue;
	String editEncodeHint;

	private void editPageButtonActionPerformed(ActionEvent evt) {
		System.out.println("editPageButton.actionPerformed, event="+evt);
		//TODO add your code for editPageButton.actionPerformed
		   String editUrl=baseUrl+"?cmd=edit&page="+pageName;
		this.messageTextArea.append(baseUrl+"\n");
		this.messageTextArea.append(editUrl+"\n");
		this.urlField.setText(editUrl);
		this.connectButtonActionPerformed(null);
		String x=this.messageTextArea.getText();
		
		/* get the first form from the url*/
		String form=this.getBetween(x,"<form", "</form>");
		if(form==null) return;
		System.out.println("form="+form);
		/* get the head part in the text area from the form*/
		int i=form.indexOf("<textarea ");
		if(i<0) return;
		this.messageTextArea.setText("");
		this.messageTextArea.append("i="+i+"\n");
		String y="";
		String z="";
		try{
		    y=form.substring(i);
		    z=y.substring(y.indexOf(">")+1);		
		}
		catch(Exception e){
			
		}
		System.out.println("z="+z);
		int j=z.indexOf("#"+this.plugInName);
		int k=j+("#"+this.plugInName).length();
		//String head=x.substring(0,k-1);
		System.out.println("j="+j+" k="+k);
		// has the command #netpaint argument? 
		String dataStart=z.substring(j);
		System.out.println("dataStart="+dataStart);
		if(dataStart.startsWith("#"+this.plugInName+"(")){
			String arg="";
			arg=this.getBetween(dataStart, "(", ")");
			String theCommand="#"+this.plugInName+"("+arg+")" ;
			System.out.println("theCommand="+theCommand);
			k=j+theCommand.length();
		}
		System.out.println("j="+j+" k="+k);
		
		String head=z.substring(0,k);
		String w=z.substring(head.length());
		int l=w.indexOf("</textarea");
		String tail=w.substring(l);
		String body=w.substring(0,l-1);
		System.out.println("head="+head);
		System.out.println("body="+body);
		System.out.println("tail="+tail);
		this.updateText=head;
		String actionwork1=form.substring(0,form.indexOf(">"));
		System.out.println("actionwork1="+actionwork1);
		this.actionUrl=this.getTagProperty(actionwork1, "action");
		System.out.println("action url="+this.actionUrl);
		
		/* 
		 getting input properties
		 */
		HtmlTokenizer htmlt=new HtmlTokenizer(form);
		while(htmlt.hasMoreTokens()){
			String t=htmlt.nextToken();
			if(t.startsWith("<input")){
				String ttype=getTagProperty(t,"type");
				if(ttype.equals("hidden")){
				   String tname=getTagProperty(t,"name");
				   String tvalue=getTagProperty(t,"value");
				   System.out.println(" "+tname+"="+tvalue);
				   if(tname.equals("cmd")){
					   this.editCmd=tvalue;
				   }
				   if(tname.equals("page")){
					   this.editPage=tvalue;
				   }
				   if(tname.equals("digest")){
				   		this.editDigest=tvalue;
				   }
				   if(tname.equals("encode_hint")){
					   this.editEncodeHint=tvalue;
				   }
				}
				if(ttype.equals("submit")){
				   String tname=getTagProperty(t,"name");
				   String tvalue=getTagProperty(t,"value");
				   System.out.println(" "+tname+"="+tvalue);
				   if(tname.equals("write")){
						this.editWriteValue=tvalue;
			       }
				}
			}
		}
		
	}
	String insertSpaceAfterNewLine(String x){
		StringTokenizer st=new StringTokenizer(x,"\n",true);
		String rtn="";
		while(st.hasMoreTokens()){
			String t=st.nextToken();
			if(t.equals("\n")){
				rtn=rtn+t+" ";
			}
			else{
				rtn=rtn+t;
			}
		}
		return rtn;
	}
	
	private String getTagProperty(String tag, String key){
//		System.out.println("tag="+tag);
//		System.out.println("key="+key);
		StringTokenizer st=new StringTokenizer(tag," =",true);
		String t=st.nextToken();
//		System.out.println(" first token="+t);
		while(st.hasMoreTokens()){
			t=st.nextToken();
			if(t.equals(" ")){        // skip space
				while(t.equals(" ")){
					if(!st.hasMoreTokens()) return "";
					t=st.nextToken();
				}
			}
			String keyx=t;
//			System.out.println(" key?="+keyx);
			if(!st.hasMoreTokens()) return "";
			t=st.nextToken();
			if(t.equals(" ")){        // skip space
				while(t.equals(" ")){
					if(!st.hasMoreTokens()) return "";
					t=st.nextToken();
				}
			}
			if(t.equals("=")){
//				System.out.println("...=");
				if(!st.hasMoreTokens()) return "";
				t=st.nextToken();
				if(t.equals(" ")){
					if(!st.hasMoreTokens()) return "";
					t=st.nextToken();
				}
			    if(keyx.equals(key)){
//			    	System.out.println(" keyx="+key+" t="+t);
			    	if(t.startsWith("\"")){
			    			t=t.substring(1);
			    	}
			    	if(t.endsWith("\"")){
			    		t=t.substring(0,t.length()-1);
			    	}
			    	return t;
			    }
			}
			
		}
		return "";
	}
	
	private void updateButtonActionPerformed(ActionEvent evt) {
		// System.out.println("updateButton.actionPerformed, event="+evt);
		//TODO add your code for updateButton.actionPerformed
		
		/* make the body (fig) */
		String fig=this.applet.frm.editdispatch.getFigString();
		/*
		try{
			fig=new String(fig.getBytes("UTF-8"),  "UTF-8");
		}
		catch(Exception e){
			return;
		}
		*/
		this.updateText=this.updateText+"\n "+insertSpaceAfterNewLine(fig);

		
		this.urlField.setText(this.actionUrl);
		String url=this.urlField.getText();
		this.messageTextArea.setText(url+"\n");
//		this.messageTextArea.append(this.updateText);
		   BufferedReader br = null;
//		System.out.println("updateText="+this.updateText);
		System.out.println("editWriteValue="+this.editWriteValue);
		System.out.println("editCmd="+this.editCmd);
		System.out.println("editPage="+this.editPage);
		System.out.println("digest="+this.editDigest);
		try{
    		PostMethod method = new PostMethod(url);
    		if(this.client==null) return;
    		method.getParams().setContentCharset(this.pageCharSet);
    		method.setParameter("msg",this.updateText);
//    		method.setParameter("encode_hint",this.editEncodeHint);
    		method.addParameter("write",this.editWriteValue);
    		method.addParameter("cmd",this.editCmd);
    		method.addParameter("page",this.editPage);
    		method.addParameter("digest",this.editDigest);
			int status=client.executeMethod(method);
		    if (status != HttpStatus.SC_OK) {
		          System.err.println("Method failed: " + method.getStatusLine());
		          method.getResponseBodyAsString();
		    }
		    else {
		          br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
		          String readLine;
		          while(((readLine = br.readLine()) != null)) {
		            System.err.println(readLine);
		          }
		   }
		    method.releaseConnection();
		}
		catch(Exception e){
//			this.messageTextArea.append(e.toString()+"\n");
			System.out.println(""+e);
			e.printStackTrace();
		}
		
	}
	
	private void paramButtonActionPerformed(ActionEvent evt) {
		System.out.println("paramButton.actionPerformed, event="+evt);
		//TODO add your code for paramButton.actionPerformed
		if(this.applet!=null){
			this.messageTextArea.append("documentBase="+
					this.applet.getDocumentBase().toString()+"\n");			
			this.messageTextArea.append("action="+applet.action+"\n");
//			this.messageTextArea.append("enctype="+applet.enctype+"\n");
//			this.messageTextArea.append("form1="+applet.form1+"\n");
//			this.messageTextArea.append("form2="+applet.form2+"\n");
			this.messageTextArea.append("param1="+applet.param1+"\n");
//			this.messageTextArea.append("param2="+applet.param2+"\n");
//			this.messageTextArea.append("param3="+applet.param3+"\n");
//			this.messageTextArea.append("param4="+applet.param4+"\n");
			this.messageTextArea.append("charset="+applet.charset+"\n");
//			this.messageTextArea.append("returnUrl="+applet.returnUrl+"\n");
			System.out.println("updateText="+this.updateText);
		}

	}
	
	boolean isInStringConst(String x, int p){
		
		int px=0;
		int py=0;
		boolean isIn=false;
		while(px<x.length()){
			if(px>p) return false;
			char cx=x.charAt(px);
			char cy=0;
			py=px+1;
			if(cx=='"'){
				isIn=true;
				while(py<x.length()){
					cy=x.charAt(py);
					if(cy=='"'){
						if(px<p && p<py)
							return true;
						else{
							isIn=false;
							px=py;
							break;
						}
					}
					if(cy=='\\'){
						py=py+1;
					}
					py=py+1;
				}
				if(isIn)
					return true;
			}
			if(cx=='\\'){
				px=px+1;
			}
			px=px+1;
		}
		return false;
	}
	
	public void readFigButtonActionPerformed(ActionEvent evt) {
//		System.out.println("readFigButton.actionPerformed, event="+evt);
		//TODO add your code for readFigButton.actionPerformed
//		    System.out.println("isInStringConst"+isInStringConst("abc\"defg\"hil",2));
//		    System.out.println("isinStringConst"+isInStringConst("abc\"defg\"hil",5));
		   String editUrl=this.applet.getDocumentBase().toString();
			System.out.println("editUrl="+editUrl+"\n");
			this.urlField.setText(editUrl);
			this.connectButtonActionPerformed(null);
			String x=this.messageTextArea.getText();
			
			// extract charSet from <?xml= ...?> which contains charSet;
			String firstXmltag=getBetween(x,"<?xml","?>");
			if(firstXmltag==null) return;
			this.pageCharSet=this.getTagProperty(firstXmltag,"encoding");
			if(this.pageCharSet==null)
				this.pageCharSet="UTF-8";
			System.out.println("pageCharSet="+this.pageCharSet);
			
			// exclude until <applet
			int i=x.indexOf("<applet");
			x=x.substring(i);
			
			// extract <pre>...</pre> where the figure is.
			String figw=getBetween(x,"<pre>","</pre>");
			/*
			String fig="";
			try{
    			fig=new String(figw.getBytes(this.pageCharSet),"UTF-8");
			}
			catch(Exception e){
				System.out.println("decoding error..."+e.toString());
				fig=figw;
			}
			*/
			String fig=figw;
			if(fig==null) return;
            fig=fig.replaceAll("&quot;", "\"");
            fig=fig.replaceAll("&lt;", "<");
            fig=fig.replaceAll("&gt;", ">");
            this.applet.frm.editdispatch.read3(fig+"\n");
	}
	
	/*
	 *  get the first string which is in from l to r in the x
	 */
	String getBetween(String x, String l, String r){
//		System.out.println("x="+x);
		System.out.println("l="+l);
		System.out.println("r="+r);
		int i=0;
		while(i<=0){
			i=x.indexOf(l,i);
    		if(i<0) return null;
	    	if(isInStringConst(x,i)){
		    	i=i+l.length();
		    }
		}
		
		i=i+l.length();
		int j=i;
		while(j<=i){
		    j=x.indexOf(r,j);
		    if(j<0) return null;
		    if(isInStringConst(x,j)){
		    	j=j+r.length();
		    }
		}
		String rtn="";
		try{
			rtn=x.substring(i,j);
		}
		catch(Exception e){
			return null;
		}
//		System.out.println("rtn="+rtn);
		return rtn;
	}

}
