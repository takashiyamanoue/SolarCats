package nodesystem;

import java.awt.Font;
import java.util.Vector;

import javax.swing.JFileChooser;

import nodesystem.JFileDialog.SymAction;
import nodesystem.JFileDialog.SymWindow;
import controlledparts.ControlledScrollPane;
import controlledparts.SelectedButton;

public class UrlDialog extends JFileDialog {

	public UrlDialog(){
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		setSize(633,120);
		setVisible(false);
		actionButton.setText("action");
		actionButton.setActionCommand("action");
		actionButton.setSelected(true);
		getContentPane().add(actionButton);
		actionButton.setBackground(new java.awt.Color(204,204,204));
		actionButton.setForeground(java.awt.Color.black);
		actionButton.setFont(new Font("Dialog", Font.BOLD, 12));
		actionButton.setBounds(252,84,108,24);
		promptLabel.setText("prompt");
		getContentPane().add(promptLabel);
		promptLabel.setBackground(new java.awt.Color(204,204,204));
		promptLabel.setForeground(new java.awt.Color(102,102,153));
		promptLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		promptLabel.setBounds(12,48,120,24);
		titleLabel.setText("title of this dialog");
		getContentPane().add(titleLabel);
		titleLabel.setBackground(new java.awt.Color(204,204,204));
		titleLabel.setForeground(new java.awt.Color(102,102,153));
		titleLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		titleLabel.setBounds(72,12,168,24);
		cancelButton.setText("cancel");
		cancelButton.setActionCommand("cancel");
		cancelButton.setSelected(true);
		getContentPane().add(cancelButton);
		cancelButton.setBackground(new java.awt.Color(204,204,204));
		cancelButton.setForeground(java.awt.Color.black);
		cancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
		cancelButton.setBounds(360,84,132,24);
		clearButton.setText("clear");
		clearButton.setActionCommand("clear");
		getContentPane().add(clearButton);
		clearButton.setBackground(new java.awt.Color(204,204,204));
		clearButton.setForeground(java.awt.Color.black);
		clearButton.setFont(new Font("Dialog", Font.BOLD, 12));
		clearButton.setBounds(144,84,108,24);
/*
		chooserButton.setText("chooser");
		chooserButton.setActionCommand("chooser");
		chooserButton.setToolTipText("open file chooser");
		getContentPane().add(chooserButton);
		chooserButton.setBackground(new java.awt.Color(204,204,204));
		chooserButton.setForeground(java.awt.Color.black);
		chooserButton.setFont(new Font("Dialog", Font.BOLD, 12));
		chooserButton.setBounds(492,84,132,24);
		*/
		controlledScrollPane1.setOpaque(true);
		getContentPane().add(controlledScrollPane1);
		controlledScrollPane1.setBounds(360,36,264,48);
		/*
		fileNameField.setSelectionColor(new java.awt.Color(204,204,255));
		fileNameField.setSelectedTextColor(java.awt.Color.black);
		fileNameField.setCaretColor(java.awt.Color.black);
		fileNameField.setDisabledTextColor(new java.awt.Color(153,153,153));
		controlledScrollPane1.getViewport().add(fileNameField);
		fileNameField.setBackground(java.awt.Color.lightGray);
		fileNameField.setForeground(java.awt.Color.black);
		fileNameField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		fileNameField.setBounds(0,0,260,44);
		*/
		controlledScrollPane2.setOpaque(true);
		getContentPane().add(controlledScrollPane2);
		controlledScrollPane2.setBounds(144,36,216,48);
		controlledScrollPane2.getViewport().add(filePathField);
		filePathField.setBackground(java.awt.Color.white);
		filePathField.setForeground(java.awt.Color.black);
		filePathField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		filePathField.setBounds(0,0,212,44);
		//}}

		//{{INIT_MENUS
		//}}
	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		actionButton.addActionListener(lSymAction);
		cancelButton.addActionListener(lSymAction);
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		clearButton.addActionListener(lSymAction);
		chooserButton.addActionListener(lSymAction);
		//}}
		
		buttons=new Vector();
		buttons.addElement(this.actionButton);
		buttons.addElement(this.cancelButton);
		buttons.addElement(this.clearButton);
		buttons.addElement(this.chooserButton);
		int numberOfButtons=buttons.size();
		for(int i=0;i<numberOfButtons;i++){
		    SelectedButton b=(SelectedButton)(buttons.elementAt(i));
		    b.setFrame(this);
//    		b.addActionListener(lSymAction);		    
		    b.setID(i);
		}
		
		texts=new Vector();
		texts.addElement(this.fileNameField);
		fileNameField.setFrame(this);
		fileNameField.setID(0);
		
		this.panes=new Vector();
        panes.addElement(this.controlledScrollPane1);
        for(int i=0;i<1;i++){
            ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(i));
            p.setFrame(this);
            p.setID(i);
        }

		
//		this.fileChooser= new JFileChooser();
        this.setFileChooser(new JFileChooser());
		
	}
	
}
