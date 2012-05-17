package application.draw;

import java.applet.Applet;
import controlledparts.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import nodesystem.CommunicationNode;
import nodesystem.FileFrame;

public class DrawApplet extends Applet 
//implements ActionListener
{
	DrawFrame frm;
	CommunicationNode cnode;
	public void init() {
		String figname=this.getParameter("fig");
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
		if(figname==null) System.out.println("null");
		else {
			frm.editdispatch.read(figname);
		}
		frm.show();

	}
}
