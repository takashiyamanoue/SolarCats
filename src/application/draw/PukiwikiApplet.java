package application.draw;

import java.applet.Applet;
import java.awt.Graphics;

import nodesystem.CommunicationNode;

public class PukiwikiApplet extends Applet
{
	public DrawFrame frm;
	CommunicationNode cnode;
	SaveButtonDebuggFrame saveButtonDebugg;
	SaveButtonFrame saveButton;
	
	public String action="";
//	public String enctype="";
//	public String form1="";
//	public String form2="";
	public String param1="";
//	public String param2="";
//	public String param3="";
//	public String param4="";
//	public String returnUrl="";
	public String charset="";
	
	public String figname="";
	
	public void init() {
//		figname=this.getParameter("fig");
		
		action=this.getParameter("action");
//		enctype=this.getParameter("enctype");
//		form1=this.getParameter("form1");
//		form2=this.getParameter("form2");
//		returnUrl=this.getParameter("return.URL");
		param1=this.getParameter("param1");
//		param2=this.getParameter("param2");
//		param3=this.getParameter("param3");
//		param4=this.getParameter("param4");
		charset=this.getParameter("charset");
		
		frm = new DrawFrame();
		frm.setIsApplet(true);
        frm.editdispatch.setSeparator("/");
        NetworkReader nr=new NetworkReader();
//        String baseDir=nr.getBaseDir(this.getCodeBase().getPath(),"/");
        String codeBase=this.getCodeBase().toString();
        System.out.println("codeBase="+codeBase);
        String baseDir=nr.getBaseDir(codeBase,"/");
        System.out.println("baseDir="+baseDir);
		frm.setIcons(baseDir+"images/");
		frm.clearAll();
		frm.textEditFrame.setIcons(baseDir+"images/");
		/*
		if(figname==null) System.out.println("null");
		else {
			frm.editdispatch.read(figname);
		}
		*/
		frm.show();
//		System.out.println("x1");
        saveButtonDebugg=new SaveButtonDebuggFrame(this);
//        System.out.println("x2");
        saveButtonDebugg.setVisible(false);
//        System.out.println("x3");
        saveButtonDebugg.readFigButtonActionPerformed(null);
        saveButton=new SaveButtonFrame(saveButtonDebugg, this);
        saveButton.setVisible(true);
	}
}
