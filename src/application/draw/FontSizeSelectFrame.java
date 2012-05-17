package application.draw;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.util.Vector;

import nodesystem.StateContainer;
import controlledparts.ControlledButton;
import controlledparts.FrameWithControlledButton;
import controlledparts.SelectedButton;


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
public class FontSizeSelectFrame extends javax.swing.JFrame implements FrameWithControlledButton, StateContainer    
{
    public boolean isDirectOperation()
    {
        return draw.isDirectOperation();
    }

    public int getState()
    {
        return this.currentState;
    }

    public int currentState;

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledButton
        // to do: code goes here
        return draw.isControlledByLocalUser();
    }

    public void sendEvent(String x)
    {
        draw.sendEvent("fsize."+x);
    }

    public void aButtonClicked(int i)
    {
//        System.out.println("button "+i+" is clicked");
        hide();
    }

    public void setState(int i)
    {
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        draw.fontSizeSelectButton.setIcon(b.getIcon());
        draw.fontSizeSelectButton.setFont(b.getFont());
        draw.fontSizeSelectButton.setText(b.getText());
        draw.fontSizeSelectButton.repaint();
        draw.editdispatch.changeFontSize();
        this.currentState=i;
//        hide();
    }

    public DrawFrame draw;

    public void setDraw(DrawFrame d)
    {
        this.draw=d;
    }

    public Vector buttons;

	public FontSizeSelectFrame()
	{
		//{{INIT_CONTROLS
		setTitle("Font Size");
		setResizable(false);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		this.setSize(125, 427);
		setVisible(false);
		size8.setText("8");
		size8.setActionCommand("8");
		getContentPane().add(size8);
		size8.setBackground(new java.awt.Color(204,204,204));
		size8.setForeground(java.awt.Color.black);
		size8.setFont(new Font("Dialog", Font.BOLD, 8));
		size8.setBounds(12,12,96,24);
		size9.setText("9");
		size9.setActionCommand("9");
		getContentPane().add(size9);
		size9.setBackground(new java.awt.Color(204,204,204));
		size9.setForeground(java.awt.Color.black);
		size9.setFont(new Font("Dialog", Font.BOLD, 9));
		size9.setBounds(12,36,96,24);
		size10.setText("10");
		size10.setActionCommand("10");
		getContentPane().add(size10);
		size10.setBackground(new java.awt.Color(204,204,204));
		size10.setForeground(java.awt.Color.black);
		size10.setFont(new Font("Dialog", Font.BOLD, 10));
		size10.setBounds(12,60,96,24);
		size11.setText("11");
		size11.setActionCommand("11");
		getContentPane().add(size11);
		size11.setBackground(new java.awt.Color(204,204,204));
		size11.setForeground(java.awt.Color.black);
		size11.setFont(new Font("Dialog", Font.BOLD, 11));
		size11.setBounds(12,84,96,24);
		size12.setText("12");
		size12.setActionCommand("12");
		getContentPane().add(size12);
		size12.setBackground(new java.awt.Color(204,204,204));
		size12.setForeground(java.awt.Color.black);
		size12.setFont(new Font("Dialog", Font.BOLD, 12));
		size12.setBounds(12,108,96,24);
		size14.setText("14");
		size14.setActionCommand("14");
		getContentPane().add(size14);
		size14.setBackground(new java.awt.Color(204,204,204));
		size14.setForeground(java.awt.Color.black);
		size14.setFont(new Font("Dialog", Font.BOLD, 14));
		size14.setBounds(12,132,96,24);
		size16.setText("16");
		size16.setActionCommand("16");
		getContentPane().add(size16);
		size16.setBackground(new java.awt.Color(204,204,204));
		size16.setForeground(java.awt.Color.black);
		size16.setFont(new Font("Dialog", Font.BOLD, 16));
		size16.setBounds(12,156,96,36);
		size18.setText("18");
		size18.setActionCommand("18");
		getContentPane().add(size18);
		size18.setBackground(new java.awt.Color(204,204,204));
		size18.setForeground(java.awt.Color.black);
		size18.setFont(new Font("Dialog", Font.BOLD, 18));
		size18.setBounds(12,192,96,36);
		size20.setText("20");
		size20.setActionCommand("20");
		getContentPane().add(size20);
		size20.setBackground(new java.awt.Color(204,204,204));
		size20.setForeground(java.awt.Color.black);
		size20.setFont(new Font("Dialog", Font.BOLD, 20));
		size20.setBounds(12,228,96,36);
		size24.setText("24");
		size24.setActionCommand("24");
		getContentPane().add(size24);
		size24.setBackground(new java.awt.Color(204,204,204));
		size24.setForeground(java.awt.Color.black);
		size24.setFont(new Font("Dialog", Font.BOLD, 24));
		size24.setBounds(12,264,96,36);
		size30.setText("30");
		size30.setActionCommand("30");
		getContentPane().add(size30);
		size30.setBackground(new java.awt.Color(204,204,204));
		size30.setForeground(java.awt.Color.black);
		size30.setFont(new Font("Dialog", Font.BOLD, 30));
		size30.setBounds(12,300,96,48);
		closeButton.setText("close");
		closeButton.setActionCommand("close");
		getContentPane().add(closeButton);
		closeButton.setBackground(new java.awt.Color(204,204,204));
		closeButton.setForeground(java.awt.Color.black);
		closeButton.setFont(new Font("Dialog", Font.BOLD, 12));
		closeButton.setBounds(12,360,96,24);
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		size8.addActionListener(lSymAction);
		size9.addActionListener(lSymAction);
		size10.addActionListener(lSymAction);
		size11.addActionListener(lSymAction);
		size12.addActionListener(lSymAction);
		size14.addActionListener(lSymAction);
		size16.addActionListener(lSymAction);
		size18.addActionListener(lSymAction);
		size20.addActionListener(lSymAction);
		size24.addActionListener(lSymAction);
		size30.addActionListener(lSymAction);
		//}}

		//{{INIT_MENUS
		//}}
		buttons=new Vector();
		buttons.addElement(size8);
		buttons.addElement(size9);
		buttons.addElement(size10);
		buttons.addElement(size11);
		buttons.addElement(size12);
		buttons.addElement(size14);
		buttons.addElement(size16);
		buttons.addElement(size18);
		buttons.addElement(size20);
		buttons.addElement(size24);
		buttons.addElement(size30);
		buttons.addElement(this.closeButton);
		int bsize=buttons.size();
		
		for(int i=0;i<bsize;i++){
		    ControlledButton b=(ControlledButton)(buttons.elementAt(i));
		    b.setID(i);
		    b.setFrame(this);
		}

	}

	public FontSizeSelectFrame(String title)
	{
		this();
		setTitle(title);
	}
	public void setVisible(boolean b)
	{
		if(b)
		{
			setLocation(50, 50);
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
		Insets ins = getInsets();
		setSize(ins.left + ins.right + d.width, ins.top + ins.bottom + d.height);
		Component components[] = getContentPane().getComponents();
		for (int i = 0; i < components.length; i++)
			{
			Point p = components[i].getLocation();
			p.translate(ins.left, ins.top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
		Object object = event.getSource();
		if (object == FontSizeSelectFrame.this)
			FontSizeSelectFrame_WindowClosing(event);
		}
	}

	void FontSizeSelectFrame_WindowClosing(java.awt.event.WindowEvent event)
	{
		dispose();		 // dispose of the Frame.
	}
	//{{DECLARE_CONTROLS
	controlledparts.ControlledButton size8 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton size9 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton size10 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton size11 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton size12 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton size14 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton size16 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton size18 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton size20 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton size24 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton size30 = new controlledparts.ControlledButton();
	controlledparts.ControlledButton closeButton = new controlledparts.ControlledButton();
	//}}

	//{{DECLARE_MENUS
	//}}

    public void unfocusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//            button.controlledButton_mouseEntered(null);
            button.unFocus();
        
    }

    public void mouseExitedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        draw.sendEvent("fsize.btn.exit("+i+")\n");
    }

    public void mouseEnteredAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        draw.sendEvent("fsize.btn.enter("+i+")\n");
    }

    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        if(closeButton.getID()==i){
            hide();
            return;
        }
 		this.setState(i);
 		this.hide();
        
    }

    public void focusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//            button.controlledButton_mouseEntered(null);
            button.focus();
    }

    public void clickButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        b.click();
        this.mouseClickedAtButton(i);
    }


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			/*
			if (object == size8)
				size8_actionPerformed(event);
			*/
            int buttonNumber=buttons.size();

            for(int i=0;i<buttonNumber;i++){
                ControlledButton b=(ControlledButton)(buttons.elementAt(i));
                if(object == b){ 
                   aButtonClicked(i);
                }
            }
            
		}
	}

	void size8_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		size8_actionPerformed_Interaction1(event);
	}

	void size8_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			size8.show();
		} catch (Exception e) {
		}
	}
}