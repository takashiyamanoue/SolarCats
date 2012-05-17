//SAKURAGI DEVELOPED

package application.autoUpdating;

import java.awt.Font;

import javax.swing.*;
import nodesystem.CommunicationNode;
import nodesystem.CommandTranceiver;

import java.awt.event.*;

public class AutoUpdatingFrame extends JFrame implements ActionListener{
	
	CommunicationNode gui;
	CommandTranceiver ct;
	
	public AutoUpdatingFrame(CommunicationNode gui,CommandTranceiver ct) {
		this.gui=gui;
		this.ct=ct;
		
		setTitle("Recognized other version of DSR");
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		setSize(500,300);
		icon.setBounds(20,5,36,36);
		icon.setIcon(new ImageIcon("images/attention.gif"));
		getContentPane().add(icon);
		setFont(new Font("Dialog", Font.BOLD, 12));
		label1.setForeground(new java.awt.Color(102,102,153));
		label1.setText("Other version of DSR is connected!");
		getContentPane().add(label1);
		label1.setBounds(120,10,400,30);
		label2.setText("If you desire to mach the version of all DSR, please push the under button after all");
		getContentPane().add(label2);
		label2.setBounds(10,50,500,30);	
		label3.setText("node connected.");
		getContentPane().add(label3);
		label3.setBounds(10,70,500,30);
		
		checkbox.setBounds(10,110,160,30);
		getContentPane().add(checkbox);
		checkbox2.setBounds(10,130,160,30);
		getContentPane().add(checkbox2);
		checkbox3.setBounds(10,150,160,30);
		getContentPane().add(checkbox3);
		
		cancelButton.setActionCommand("cancel");
		getContentPane().add(cancelButton);
		cancelButton.setBackground(new java.awt.Color(204,204,204));
		cancelButton.setForeground(java.awt.Color.black);
		cancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
		cancelButton.setBounds(390, 200, 80, 24);
		cancelButton.setText("Cancel");
		
		okButton.setActionCommand("ok");
		getContentPane().add(okButton);
		okButton.setBackground(new java.awt.Color(204,204,204));
		okButton.setForeground(java.awt.Color.black);
		okButton.setFont(new Font("Dialog", Font.BOLD, 12));
		okButton.setBounds(300, 200, 80, 24);
		okButton.setText("OK");
		
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setVisible(false);
			}
		});
		
		okButton.addActionListener(this);
		
		setVisible(true);
	}
	
	AutoUpdating au=null;
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("ok"))
			au=new AutoUpdating(gui,checkbox.isSelected(),checkbox2.isSelected(),checkbox3.isSelected());
		setVisible(false);
		ct.otherVerRecognitionFlag=false;
	}
	
	JLabel icon=new JLabel();
	JLabel label1=new JLabel();
	JLabel label2=new JLabel();
	JLabel label3=new JLabel();
	JCheckBox checkbox=new JCheckBox("Use this node's cache.",true);
	JCheckBox checkbox2=new JCheckBox("Use jar command.",true);
	JCheckBox checkbox3=new JCheckBox("Hi-Speed sending.",false);
	JButton cancelButton = new JButton();
	JButton okButton = new JButton();

}
