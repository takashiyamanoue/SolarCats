/*
NetPaint

     by T. Yamanoue,
     Kyushu Institute of Technology, Japan,
     Aug.1, 1997


   A Paint tool for the Internet.

   Drawing tool on a Web brouser.
   A Co-operative drawing tool.
   Drawing a paint on the Internet by linking parts


*/
package application.draw;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JScrollBar;

import nodesystem.AMessage;
import nodesystem.TemporalyText;

import controlledparts.ControlledScrollBar;
import controlledparts.ControlledScrollPane;
import controlledparts.FrameWithControlledFigCanvas;
import controlledparts.PaneWithControlledScrollBar;
import controlledparts.RemoteMouse;

public class FigCanvas extends 
//javax.swing.JComponent
ControlledScrollPane
implements PaneWithControlledScrollBar
//ControlledScrollPane
{
	
//	ControlledScrollBar hscrollbar;
//	ControlledScrollBar vscrollbar;
//	public Vector scrollBars;
    public void setDrawFrame(DrawFrame g)
    {
        gui=g;
    }

    public DrawFrame gui;

    public void setEditdispatch(EditDispatcher e)
    {
        editdispatch=e;
    }

    int id;

    public FrameWithControlledFigCanvas frame;

    public void setFrame(FrameWithControlledFigCanvas f)
    {
        this.frame=f;
//        super.setFrame(f);
    }

    public int getID()
    {
        return id;
    }

    public void setID(int i)
    {
        this.id=i;
    }

    public boolean isPlaying()
    {
        if(fs==null) return false;
        return fs.isPlaying();
    }

    public void setPlayMode(boolean x)
    {
        this.fs.setPlayMode(x);
    }

    public //synchronized 
    void downKey(int key)
    {
        int startTime=frame.getTimeNow();
//        int key=(int)(event.getKeyChar());
//        System.out.println("keyTyped in figcanvas:"+key);
//         gui.editdispatch.keyDown(null,key);
          this.editdispatch.keyDown(null,key);
          this.repaint();
//        if(gui.sendEventFlag) gui.sendEvent("fc.kdn("+key+")\n");
//       gui.repaint();
        int endTime=frame.getTimeNow();
        if(endTime>startTime)
        frame.recordMessage(""+startTime+",\"fc("+id+").kdn("+key+")\"");
//        this.notifyAll();
    }

    public void exitMouse()
    {
        int startTime=frame.getTimeNow();
        /*
        if(gui.communicationNode.echoback) 
//           gui.communicationNode.rmouseIsActivated=false;
             this.showRMouse=false;
        */
        rmouse.setVisible(false);
//        gui.editdispatch.mouseExit(null);
        this.editdispatch.mouseExit(null);
        this.repaint();
//        gui.repaint();
        int endTime=frame.getTimeNow();
        if(endTime>startTime)
        frame.recordMessage(""+startTime+",\"fc("+id+").mexit()\"");
    }

    public void enterMouse(int x, int y)
    {
        
        int startTime=frame.getTimeNow();
        /*
        if(gui.communicationNode.echoback) {
//            gui.communicationNode.rmouseIsActivated=true; 
            this.showRMouse=true;
            rmouse.move(x,y);
        }
        */
//        if(gui.communicationNode.rmouseIsActivated) rmouse.setVisible(true);
//        else rmouse.setVisible(false);
        if(this.frame.isShowingRmouse()) rmouse.setVisible(true);
        else rmouse.setVisible(false);
        rmouse.move(x,y);
//        gui.editdispatch.mouseEnter(null,x,y);
        this.editdispatch.mouseEnter(null,x,y);
        this.repaint();
//        gui.requestFocus();
//        gui.repaint();
        int endTime=frame.getTimeNow();
        if(endTime>startTime)
        frame.recordMessage(""+startTime+",\"fc("+id+").ment("+x+","+y+")\"");
   }

    public //synchronized 
    void dragMouse(int x, int y)
    {
        int startTime=frame.getTimeNow();
//        if(gui.communicationNode.echoback) rmouse.move(x,y);
        rmouse.move(x,y);
//        gui.editdispatch.mouseDrag(null,x,y);
        this.editdispatch.mouseDrag(null,x,y);
        this.repaint();
//        gui.repaint();
         int endTime=frame.getTimeNow();
        if(endTime>startTime)
        frame.recordMessage(""+startTime+",\"fc.mdg("+x+","+y+")\"");
//        this.notifyAll();
    }

    public //synchronized 
    void moveMouse(int x, int y)
    {
        int startTime=frame.getTimeNow();
//        if(gui.communicationNode.echoback) rmouse.move(x,y);
        rmouse.move(x,y);
//        gui.editdispatch.mouseMove(null,x,y);
        this.editdispatch.mouseMove(null,x,y);
        this.repaint();
//        gui.repaint();
        int endTime=frame.getTimeNow();
        if(endTime>startTime)
        frame.recordMessage(""+startTime+",\"fc("+id+").mm("+x+","+y+")\"");
//        this.notifyAll();
   }

    public //synchronized 
    void downMouse(int x, int y)
    {
        this.rmouse.setColor(Color.blue);
        int startTime=frame.getTimeNow();
//        if(gui.communicationNode.echoback) rmouse.move(x,y);
        rmouse.move(x,y);
        this.editdispatch.mouseDown(null,x,y);
        this.repaint();
//        gui.repaint();
        int endTime=frame.getTimeNow();
        if(endTime>startTime)
        frame.recordMessage(""+startTime+",\"fc("+id+").mdn("+x+","+y+")\"");
        /*
        try{
            Thread.sleep(80);
        }
        catch(InterruptedException e){}
        this.rmouse.resetColor();
        */
//        this.notifyAll();
    }

    public //synchronized 
    void releaseMouse(int x, int y)
    {
        int startTime=frame.getTimeNow();
//        gui.editdispatch.mouseUp(null,x,y);
        this.editdispatch.mouseUp(null,x,y);
        this.rmouse.resetColor();
        this.repaint();
//        gui.repaint();
        int endTime=frame.getTimeNow();
        if(endTime>startTime)
        frame.recordMessage(""+startTime+",\"fc("+id+").mup("+x+","+y+")\"");
//        System.out.println("mouse released,");
//        this.notifyAll();
    }

    public void paint(Graphics g)
    {
    	if(g==null) return;
        if(fs==null) return;
        Color cc=g.getColor();
        g.setColor(this.getBackground());
        g.fillRect(0,0,this.getSize().width, this.getSize().height);
        g.setColor(cc);
        if(fs==null) return;
        fs.draw(g);
        if(ftemp!=null) 
             ftemp.draw(g);
        if(editdispatch==null) return;
        editdispatch.draw(g);
        this.rmouse.paint(g);
        /*
        if(
        gui.communicationNode.rmouseIsActivated &&
                this.showRMouse) rmouse.paint(g);
         */
    }

    public Figures ftemp;
    public Figures fs;
    public EditDispatcher editdispatch;
    public RemoteMouse rmouse;

	public FigCanvas()
	{
		this.setBackground(new Color(204,220,250));
		//{{INIT_CONTROLS
		setSize(0,0);
		//}}
		
//		this.enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK
//		                | java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK
//		                | java.awt.AWTEvent.KEY_EVENT_MASK);
//		this.requestFocus();

		//{{REGISTER_LISTENERS
		SymMouse aSymMouse = new SymMouse();
		this.addMouseListener(aSymMouse);
		SymMouseMotion aSymMouseMotion = new SymMouseMotion();
		this.addMouseMotionListener(aSymMouseMotion);
		SymKey aSymKey = new SymKey();
		this.addKeyListener(aSymKey);
		//}}
		
		this.addNotify();
		this.setEnabled(true);
        fs=new Figures(this);
        ftemp=new Figures(this);
        rmouse=new RemoteMouse();
        rmouse.setVisible(false);
//        this.createHorizontalScrollBar();
        this.createVerticalScrollBar();
        
	}

	//{{DECLARE_CONTROLS
	//}}


	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mousePressed(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == FigCanvas.this)
				FigCanvas_mousePressed(event);
		}
		public void mouseReleased(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == FigCanvas.this)
				FigCanvas_mouseReleased(event);
		}

		public void mouseExited(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == FigCanvas.this)
				FigCanvas_mouseExited(event);
		}

		public void mouseEntered(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == FigCanvas.this)
				FigCanvas_mouseEntered(event);
		}

	}

	public void FigCanvas_mouseEntered(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		FigCanvas_mouseEntered_Interaction1(event);
	}

	void FigCanvas_mouseEntered_Interaction1(java.awt.event.MouseEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
		int x=event.getX(); int y=event.getY();
        // if(gui.sendEventFlag) 
        if(this.frame.isControlledByLocalUser()){
            frame.sendEvent("fc("+id+").ment("+x+","+y+")\n");
            if(frame.isDirectOperation()){
              frame.mouseEnteredAtFigCanvas(id,x,y);
              this.enterMouse(x,y);
            }
        }
	}

	public void FigCanvas_mouseExited(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		FigCanvas_mouseExited_Interaction1(event);
	}

	void FigCanvas_mouseExited_Interaction1(java.awt.event.MouseEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
		int x=event.getX(); int y=event.getY();
        // if(gui.sendEventFlag) 
        if(this.frame.isControlledByLocalUser()){
            frame.sendEvent("fc("+id+").mxit("+x+","+y+")\n");
            if(this.frame.isDirectOperation()){
              frame.mouseExitedAtFigCanvas(id);
              this.exitMouse();
            }
        }
	}

	class SymMouseMotion extends java.awt.event.MouseMotionAdapter
	{
		public void mouseMoved(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == FigCanvas.this)
				FigCanvas_mouseMoved(event);
		}

		public void mouseDragged(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == FigCanvas.this)
				FigCanvas_mouseDragged(event);
		}
	}

	public void FigCanvas_mouseDragged(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		FigCanvas_mouseDragged_Interaction1(event);
	}

	void FigCanvas_mouseDragged_Interaction1(java.awt.event.MouseEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
        int x=event.getX(); int y=event.getY();
        if(frame.isControlledByLocalUser()){
          frame.sendEvent("fc("+id+").mdg("+x+","+y+")\n");
          if(frame.isDirectOperation()){
              frame.mouseDraggedAtFigCanvas(id,x,y);
              this.dragMouse(x,y);
          }
        }
	}

	public void FigCanvas_mouseMoved(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		FigCanvas_mouseMoved_Interaction1(event);
	}

	void FigCanvas_mouseMoved_Interaction1(java.awt.event.MouseEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
        int x=event.getX(); int y=event.getY();
//        if(gui.echoback) rmouse.move(x,y);
        if(this.frame.isControlledByLocalUser()){
            frame.sendEvent("fc("+id+").mm("+x+","+y+")\n");
            if(this.frame.isDirectOperation()){
              frame.mouseMovedAtFigCanvas(id,x,y);
              this.moveMouse(x,y);
            }
        }
	}

	public void FigCanvas_mouseReleased(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		FigCanvas_mouseReleased_Interaction1(event);
	}

	void FigCanvas_mouseReleased_Interaction1(java.awt.event.MouseEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
        int startTime=gui.getTimeNow();
        int x=event.getX(); int y=event.getY();
        if(this.frame.isControlledByLocalUser()){
            frame.sendEvent("fc("+id+").mup("+x+","+y+")\n");
            if(this.frame.isDirectOperation()){
              frame.mouseUpAtFigCanvas(id,x,y);
              this.releaseMouse(x,y);
            }
        }
	}

	class SymKey extends java.awt.event.KeyAdapter
	{
		public void keyPressed(java.awt.event.KeyEvent event)
		{
			Object object = event.getSource();
			if (object == FigCanvas.this)
				FigCanvas_keyPressed(event);
		}

		public void keyTyped(java.awt.event.KeyEvent event)
		{
			Object object = event.getSource();
			if (object == FigCanvas.this)
				FigCanvas_keyTyped(event);
		}
	}

	public void FigCanvas_keyTyped(java.awt.event.KeyEvent event)
	{
		// to do: code goes here.
			 
		FigCanvas_keyTyped_Interaction1(event);
	}

	void FigCanvas_keyTyped_Interaction1(java.awt.event.KeyEvent event)
	{
	    
//	 	System.out.println("key typed-"+event.getKeyChar());
        int key=(int)(event.getKeyChar());
        if(this.frame.isControlledByLocalUser()){
           frame.sendEvent("fc("+id+").kdn("+key+")\n");
           if(this.frame.isDirectOperation()){
             frame.keyDownedAtFigCanvas(id,key);
             this.downKey(key);
           }
        }
	}

	public void FigCanvas_keyPressed(java.awt.event.KeyEvent event)
	{
		// to do: code goes here.
			 
		FigCanvas_keyPressed_Interaction1(event);
	}

	void FigCanvas_keyPressed_Interaction1(java.awt.event.KeyEvent event)
	{
		try {
//			this.FigCanvas_keyTyped_Interaction1(event);
		} catch (Exception e) {
		}
//		System.out.println("key pressed-"+event.getKeyChar());
	}

	public void FigCanvas_mousePressed(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
        int x=event.getX(); int y=event.getY();
//        System.out.println(event.toString());
        if(this.frame.isControlledByLocalUser()){  
        	boolean rf=this.requestFocusInWindow();
           frame.sendEvent("fc("+id+").mdn("+x+","+y+")\n");
           if(this.frame.isDirectOperation()){
   	         frame.mouseDownedAtFigCanvas(id,x,y);
	         this.downMouse(x,y);
	       }
	    }
	}

	public boolean isDirectOperation() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	public void sendEvent(String x) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public boolean isControlledByLocalUser() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

    public void scrollBarIsHidden(int barID)
    {
        // This method is derived from interface PaneWithControlledScrollBar
        // to do: code goes here
//        ControlledScrollBar bar=(ControlledScrollBar)(this.scrollBars.elementAt(barID));
//        bar.setVisible(false);
    }

    public void scrollBarIsShown(int barID)
    {
        // This method is derived from interface PaneWithControlledScrollBar
        // to do: code goes here
//        ControlledScrollBar bar=(ControlledScrollBar)(this.scrollBars.elementAt(barID));
//        bar.setVisible(true);
    }

    public void scrollBarValueChanged(int barID, int v)
    {
        // This method is derived from interface PaneWithControlledScrollBar
        // to do: code goes here
        ControlledScrollBar bar=(ControlledScrollBar)(this.scrollBars.elementAt(barID));
        if(bar.getID()==this.vscrollbar.getID()){
        	int maxY=this.fs.getYmax();
        	int minY=this.fs.getYmin();
        	int smax=vscrollbar.getMaximum();
        	int smin=vscrollbar.getMinimum();
        	int m=(int)((minY-maxY)*((double)v)/(smin-smax));
        	this.fs.move(0,m);
        }
        else{
        	int maxX=this.fs.getXmax();
        	int minX=this.fs.getXmin();
        	int smax=hscrollbar.getMaximum();
        	int smin=hscrollbar.getMinimum();
        	int m=(int)((minX-maxX)*((double)v)/(smin-smax));
        	this.fs.move(m,0);
        }
    }

    public void setScrollBarValue(int barID, int v)
    {
        // This method is derived from interface PaneWithControlledScrollBar
        // to do: code goes here
        ControlledScrollBar bar=(ControlledScrollBar)(this.scrollBars.elementAt(barID));
        bar.setValue(v);
    }

    int scrollBarWidth=10;
    public JScrollBar createVerticalScrollBar()
    {
//        return new ScrollBar(JScrollBar.VERTICAL);
//       return new ControlledScrollBar(JScrollBar.VERTICAL);
          if(this.vscrollbar==null){
              if(this.scrollBars==null){
        		 vscrollbar=new ControlledScrollBar(JScrollBar.VERTICAL);
		         hscrollbar=new ControlledScrollBar(JScrollBar.HORIZONTAL);
		         vscrollbar.setBounds(this.getWidth()-scrollBarWidth,0,scrollBarWidth,this.getHeight());
		         hscrollbar.setBounds(this.getHeight()-scrollBarWidth,0,this.getWidth()-scrollBarWidth,scrollBarWidth);
		         scrollBars=new Vector();
		         scrollBars.addElement(vscrollbar);
		         scrollBars.addElement(hscrollbar);
		         int numberOfScrollBars=scrollBars.size();	
		         for(int i=0;i<numberOfScrollBars;i++){
		            ControlledScrollBar bar=(ControlledScrollBar)(scrollBars.elementAt(i));
		            bar.setID(i);
		            bar.setPane(this);
		         }
              }
          }
         return this.vscrollbar;
    }

    public JScrollBar createHorizontalScrollBar()
    {
//        return new ScrollBar(JScrollBar.HORIZONTAL);
//        return new ControlledScrollBar(JScrollBar.HORIZONTAL);
           if(this.hscrollbar==null){
              if(this.scrollBars==null){
        		 vscrollbar=new ControlledScrollBar(JScrollBar.VERTICAL);
		         hscrollbar=new ControlledScrollBar(JScrollBar.HORIZONTAL);
		         vscrollbar.setBounds(this.getWidth()-scrollBarWidth,0,scrollBarWidth,this.getHeight());
		         hscrollbar.setBounds(this.getHeight()-scrollBarWidth,0,this.getWidth()-scrollBarWidth,scrollBarWidth);
		         scrollBars=new Vector();
		         scrollBars.addElement(vscrollbar);
		         scrollBars.addElement(hscrollbar);
		         int numberOfScrollBars=scrollBars.size();	
		         for(int i=0;i<numberOfScrollBars;i++){
		            ControlledScrollBar bar=(ControlledScrollBar)(scrollBars.elementAt(i));
		            bar.setID(i);
		            bar.setPane(this);
		         }
              }
          }
          return this.hscrollbar;
    }
    public void hideScrollBar(int barID)
    {
        ControlledScrollBar bar=(ControlledScrollBar)(this.scrollBars.elementAt(barID));
//        bar.hide();
        bar.setVisible(false);
    }

    public void showScrollBar(int barID)
    {
        ControlledScrollBar bar=(ControlledScrollBar)(this.scrollBars.elementAt(barID));
        bar.setVisible(true);
    }
    Vector<AMessage> keyFrame;
	public Vector<AMessage> getKeyFrame(String commandName){
		keyFrame=new Vector();
        TemporalyText outS=new TemporalyText();

        if(!(this.fs.save(outS))){
            gui.recordMessage(""+ ",\"error while saving \",,,");
        }
        String outx1=outS.getText();
        String header1=commandName+" cfigs(";
        AMessage m1=new AMessage();
        m1.setHead(header1+outx1+")\n");
        keyFrame.add(m1);

        outS=new TemporalyText();

        if(!(this.ftemp.save(outS))){
            gui.recordMessage(""+ ",\"error while saving \",,,");
        }
        String outx2=outS.getText();
        String header2=commandName+" tfigs(";
        AMessage m2=new AMessage();
        m2.setHead(header2+outx2+")\n");
        keyFrame.add(m2);
		return keyFrame;
	}
	public void setCurrentFig(AMessage fig){
		
	}
	public void setCurrentEditingFig(AMessage fig){
		
	}

}