/*
		A basic implementation of the JDialog class.
*/
package nodesystem;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

public class JAboutGroupManager extends javax.swing.JDialog
{
	public JAboutGroupManager(Frame parentFrame)
	{
		super(parentFrame);
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		setTitle("JFC Application - About");
		setModal(true);
		getContentPane().setLayout(null);
		setSize(248,94);
		setVisible(false);
		okButton.setText("OK");
		okButton.setActionCommand("OK");
		okButton.setOpaque(false);
		okButton.setMnemonic((int)'O');
		getContentPane().add(okButton);
		okButton.setFont(new Font("Dialog", Font.BOLD, 12));
		okButton.setBounds(98,59,51,25);
		aboutLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		aboutLabel.setText("A JFC Application");
		getContentPane().add(aboutLabel);
		aboutLabel.setForeground(new java.awt.Color(102,102,153));
		aboutLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		aboutLabel.setBounds(0,0,248,59);
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		okButton.addActionListener(lSymAction);
		//}}
	}

	public void setVisible(boolean b)
	{
	    if (b)
	    {
    		Rectangle bounds = (getParent()).getBounds();
    		Dimension size = getSize();
    		setLocation(bounds.x + (bounds.width - size.width)/2,
    			        bounds.y + (bounds.height - size.height)/2);
	    }

		super.setVisible(b);
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;
		// Adjust components according to the insets
		Insets insets = getInsets();
		setSize(insets.left + insets.right + d.width, insets.top + insets.bottom + d.height);
		Component components[] = getContentPane().getComponents();
		for (int i = 0; i < components.length; i++)
		{
			Point p = components[i].getLocation();
			p.translate(insets.left, insets.top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	//{{DECLARE_CONTROLS
	javax.swing.JButton okButton = new javax.swing.JButton();
	javax.swing.JLabel aboutLabel = new javax.swing.JLabel();
	//}}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == JAboutGroupManager.this)
				jAboutDialog_windowClosing(event);
		}
	}

	void jAboutDialog_windowClosing(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		jAboutDialog_windowClosing_Interaction1(event);
	}

	void jAboutDialog_windowClosing_Interaction1(java.awt.event.WindowEvent event) {
		try {
			// JAboutDialog Hide the JAboutDialog
			this.setVisible(false);
		} catch (Exception e) {
		}
	}
	
	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == okButton)
				okButton_actionPerformed(event);
		}
	}

	void okButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		okButton_actionPerformed_Interaction1(event);
	}

	void okButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event) {
		try {
			// JAboutDialog Hide the JAboutDialog
			this.setVisible(false);
		} catch (Exception e) {
		}
	}
}