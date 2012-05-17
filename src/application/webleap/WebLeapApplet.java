/*
	A basic extension of the javax.swing.JApplet class
 */
package application.webleap;
import util.*;
import webleap.Settings;

import javax.swing.JApplet;

import application.draw.DrawFrame;
import application.draw.NetworkReader;
import controlledparts.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import nodesystem.CommunicationNode;
import nodesystem.FileFrame;

public class WebLeapApplet extends JApplet
{
	
	WebLEAPFrame frm;
	String[] messages=new String[]{"Loading the program","Please Wait for a while..."};
    public void paint(Graphics g){
    	for(int i=0;i<2;i++){
    		g.drawString(messages[i],15,20+i*20);
    	}
    }
    public void changeMessage(){
    	messages[0]="loading done.";
    	messages[1]="good luck!";
    	repaint();
    }

	public void init()
	{
		repaint();
//		String figname=this.getParameter("fig");
        NetworkReader nr=new NetworkReader();
//        String baseDir=nr.getBaseDir(this.getCodeBase().getPath(),"/");
//        String baseDir=nr.getBaseDir(this.getCodeBase().toString(),"/");
        String baseDir=this.getParameter("baseDir");
        if(baseDir==null){
        	baseDir= nr.getBaseDir(this.getCodeBase().getPath(),"/");
        }
        String codeDir=(this.getCodeBase()).toString();
		CommunicationNode cn = new CommunicationNode();
		cn.setIsApplet(true);
		cn.setCodeDirForApplet(codeDir);
		cn.setBaseDirForApplet(baseDir);
		Locale locale=new Locale("","");
		/*
		System.out.println("country="+locale.getCountry());
		System.out.println("locale="+locale.toString());
		System.out.println("language="+locale.getLanguage());
		*/
		cn.resourceBundle = ResourceBundle.getBundle("dsrWords" , locale);
	    cn.setTitle("DSR/communication node - teacher");
		cn.className="Communication_Node";
		cn.setWords();
		cn.setIcons(cn.getIconPlace());
		cn.setControlMode("local");
//		cn.spawnApplication("WebLEAPFrame");
		
		WebLEAPFrame wf=new WebLEAPFrame();
		Settings st=new Settings();
		st.setIsApplet(true);
		st.setBaseUrl(baseDir);
		st.initTab();
		wf.setSettings(st);
		UrlTable ut=new UrlTable("url list",cn);
		wf.setUrlTable(ut);
		DrawFrame df= new DrawFrame(); 
		df.setIsApplet(true);
        df.editdispatch.setSeparator("/");
        System.out.println("codeBase="+codeDir);
        System.out.println("baseDir="+baseDir);
//		df.setIcons(baseDir+"images/");
        df.setIcons(cn.getIconPlace());
		df.clearAll();
		df.textEditFrame.setIcons(baseDir+"images/");
//		df.show();
		df.setVisible(true);
		wf.setDraw(df);
//		wf.setIcons(baseDir+"images/");
		wf.setIcons(cn.getIconPlace());
		wf.clearAll();
		wf.setVisible(true);
		this.changeMessage();

	}

}
