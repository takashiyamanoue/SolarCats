/*
		A basic implementation of the JFrame class.
*/
package application.lisp;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
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
public class LispExampleListFrame extends javax.swing.JFrame implements FrameWithControlledButton, StateContainer    
{
    public int getState()
    {
        return this.currentState;
    }

    private int currentState;

    public void setState(int i)
    {
          frame.exampleButton.setText(((ControlledButton)(buttons.elementAt(i))).getText());
          frame.currentExample=(String)(programs.elementAt(i));
          this.currentState=i;
    }

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledButton
        // to do: code goes here
        if(frame==null) return true;
        return frame.isControlledByLocalUser();
    }

    public boolean isDirectOperation()
    {
        // This method is derived from interface FrameWithControlledButton
        // to do: code goes here
        if(frame==null) return true;
        return frame.isDirectOperation();
    }

    public void sendEvent(String x)
    {
        frame.sendEvent("bex."+x);
    }

    public void unfocusButton(int i)
    {
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//            button.controlledButton_mouseExited(null);
            button.unFocus();
    }

    public void focusButton(int i)
    {
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//            button.controlledButton_mouseEntered(null);
            button.focus();
    }

    public void clickButton(int i)
    {
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        b.click();
        this.mouseClickedAtButton(i);
    }

    public void setFrame(LispFrame f)
    {
        frame=f;
    }

    private LispFrame frame;

    public Vector programs;
    public Vector buttons;

    public void mouseExitedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        frame.sendEvent("bex.btn.exit("+i+")\n");
    }

    public void mouseEnteredAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
//        frame.sendEvent("bex.btn.enter("+i+")\n");
    }

    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        if(b.getText().equals("cancel")) {
            this.hide();
            return;
        }
    	if(frame!=null){
            this.setState(i);
        }
	    this.hide();
    }

	public LispExampleListFrame()
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		setSize(180,334);
		setVisible(false);
		setButton.setText("set");
		setButton.setActionCommand("set");
		getContentPane().add(setButton);
		setButton.setBackground(new java.awt.Color(204,204,204));
		setButton.setForeground(java.awt.Color.black);
		setButton.setFont(new Font("Dialog", Font.BOLD, 12));
		setButton.setBounds(12,12,156,24);
		fibonacciButton.setText("fibonacci");
		fibonacciButton.setActionCommand("fibonacci");
		getContentPane().add(fibonacciButton);
		fibonacciButton.setBackground(new java.awt.Color(204,204,204));
		fibonacciButton.setForeground(java.awt.Color.black);
		fibonacciButton.setFont(new Font("Dialog", Font.BOLD, 12));
		fibonacciButton.setBounds(12,36,156,24);
		lines1Button.setText("lines1");
		lines1Button.setActionCommand("lines1");
		getContentPane().add(lines1Button);
		lines1Button.setBackground(new java.awt.Color(204,204,204));
		lines1Button.setForeground(java.awt.Color.black);
		lines1Button.setFont(new Font("Dialog", Font.BOLD, 12));
		lines1Button.setBounds(12,60,156,24);
		sincurveButton.setText("sincurve");
		sincurveButton.setActionCommand("sincurve");
		getContentPane().add(sincurveButton);
		sincurveButton.setBackground(new java.awt.Color(204,204,204));
		sincurveButton.setForeground(java.awt.Color.black);
		sincurveButton.setFont(new Font("Dialog", Font.BOLD, 12));
		sincurveButton.setBounds(12,84,156,24);
		gaussButton.setText("gauss");
		gaussButton.setActionCommand("gauss");
		getContentPane().add(gaussButton);
		gaussButton.setBackground(new java.awt.Color(204,204,204));
		gaussButton.setForeground(java.awt.Color.black);
		gaussButton.setFont(new Font("Dialog", Font.BOLD, 12));
		gaussButton.setBounds(12,108,156,24);
		hanoiButton.setText("hanoi");
		hanoiButton.setActionCommand("hanoi");
		getContentPane().add(hanoiButton);
		hanoiButton.setBackground(new java.awt.Color(204,204,204));
		hanoiButton.setForeground(java.awt.Color.black);
		hanoiButton.setFont(new Font("Dialog", Font.BOLD, 12));
		hanoiButton.setBounds(12,132,156,24);
		sieveButton.setText("sieve");
		sieveButton.setActionCommand("eratos");
		getContentPane().add(sieveButton);
		sieveButton.setBackground(new java.awt.Color(204,204,204));
		sieveButton.setForeground(java.awt.Color.black);
		sieveButton.setFont(new Font("Dialog", Font.BOLD, 12));
		sieveButton.setBounds(12,156,156,24);
		s2iButton.setText("s2i");
		s2iButton.setActionCommand("s2i");
		getContentPane().add(s2iButton);
		s2iButton.setBackground(new java.awt.Color(204,204,204));
		s2iButton.setForeground(java.awt.Color.black);
		s2iButton.setFont(new Font("Dialog", Font.BOLD, 12));
		s2iButton.setBounds(12,180,156,24);
		mathButton.setText("math-fun");
		mathButton.setActionCommand("math-fun");
		getContentPane().add(mathButton);
		mathButton.setBackground(new java.awt.Color(204,204,204));
		mathButton.setForeground(java.awt.Color.black);
		mathButton.setFont(new Font("Dialog", Font.BOLD, 12));
		mathButton.setBounds(12,204,156,24);
		associateButton.setText("associate");
		associateButton.setActionCommand("associate");
		getContentPane().add(associateButton);
		associateButton.setBackground(new java.awt.Color(204,204,204));
		associateButton.setForeground(java.awt.Color.black);
		associateButton.setFont(new Font("Dialog", Font.BOLD, 12));
		associateButton.setBounds(12,228,156,24);
		cancelButton.setText("cancel");
		cancelButton.setActionCommand("cancel");
		getContentPane().add(cancelButton);
		cancelButton.setBackground(new java.awt.Color(204,204,204));
		cancelButton.setForeground(java.awt.Color.black);
		cancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
		cancelButton.setBounds(12,288,156,24);
		while1Button.setText("while-ex1");
		while1Button.setActionCommand("associate");
		getContentPane().add(while1Button);
		while1Button.setBackground(new java.awt.Color(204,204,204));
		while1Button.setForeground(java.awt.Color.black);
		while1Button.setFont(new Font("Dialog", Font.BOLD, 12));
		while1Button.setBounds(12,252,156,24);
		//}}

		//{{INIT_MENUS
		//}}
		
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
//		factorialButton.addActionListener(lSymAction);
		//}}
		
		programs=new Vector();
		buttons=new Vector();
		buttons.addElement(this.setButton);  
		programs.addElement("commondata/lisp/set.lsp");
		buttons.addElement(this.fibonacciButton);  
		programs.addElement("commondata/lisp/fibonacci.lsp");
		buttons.addElement(this.lines1Button);     
		programs.addElement("commondata/lisp/lines-1.lsp");
		buttons.addElement(this.sincurveButton);     
		programs.addElement("commondata/lisp/sin-curve.lsp");
		buttons.addElement(this.gaussButton);  
		programs.addElement("commondata/lisp/gauss.lsp");
		buttons.addElement(this.hanoiButton);     
		programs.addElement("commondata/lisp/hanoi.lsp");
		buttons.addElement(this.sieveButton);     
		programs.addElement("commondata/lisp/sieve.lsp");
		buttons.addElement(this.s2iButton);
		programs.addElement("commondata/lisp/s2i.lsp");
		buttons.addElement(this.mathButton);
		programs.addElement("commondata/lisp/math-fun.lsp");
		buttons.addElement(this.associateButton);
		programs.addElement("commondata/lisp/associate.lsp");
		buttons.addElement(this.while1Button);
		programs.addElement("commondata/lisp/while-ex1.lsp");
		buttons.addElement(this.cancelButton);
		
		int numberOfButtons=buttons.size();
		for(int i=0;i<numberOfButtons;i++){
		    ControlledButton b=(ControlledButton)(buttons.elementAt(i));
		    b.setFrame(this);
		    b.setID(i);
    		b.addActionListener(lSymAction);
		}
		this.setTitle("LispExampleList");
	
	}

	public LispExampleListFrame(String sTitle)
	{
		this();
		setTitle(sTitle);
	}

	public void setVisible(boolean b)
	{
		if (b)
			setLocation(50, 50);
		super.setVisible(b);
	}

	static public void main(String args[])
	{
		(new LispExampleListFrame()).setVisible(true);
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

	//{{DECLARE_CONTROLS
	controlledparts.ControlledButton setButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton fibonacciButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton lines1Button = new controlledparts.ControlledButton();
	controlledparts.ControlledButton sincurveButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton gaussButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton hanoiButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton sieveButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton s2iButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton mathButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton associateButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton cancelButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton while1Button = new controlledparts.ControlledButton();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
//			if (object == factorialButton)
//				factorialButton_actionPerformed(event);
//			Object object = event.getSource();
//			if (object == whiteButton)
//				whiteButton_actionPerformed(event);
            int buttonNumber=buttons.size();

            for(int i=0;i<buttonNumber;i++){
                ControlledButton b=(ControlledButton)(buttons.elementAt(i));
                if(object == b){ 
                    mouseClickedAtButton(i);
                }
            }
            
		}
	}

	void factorialButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		factorialButton_actionPerformed_Interaction1(event);
	}

	void factorialButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
	}
}