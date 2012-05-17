package controlledparts;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.Vector;

import nodesystem.AMessage;
import nodesystem.CommunicationNode;
import nodesystem.EventBuffer;
import nodesystem.applicationmanager.ApplicationID;

public class ControlledFrame extends javax.swing.JFrame implements FrameWithControlledFocus    
{
	boolean thisIsApplet;
	
	public boolean isApplet(){
		return thisIsApplet;
	}
	
	public void setIsApplet(boolean tf){
		this.thisIsApplet=tf;
	}
	
	public String getLclTxt(String x){
		if(this.communicationNode==null) return x;
		return communicationNode.getLclTxt(x);
	}
	public String getCommandName(){
		return null;
	}
	public void setEditable(boolean x){
	}
	
    public boolean isDirectOperation()
    {
        if(communicationNode==null) return true;
        return communicationNode.isDirectOperation();
    }

    public boolean isSending(){
    	if(communicationNode==null) return true;
    	return communicationNode.isSending();
    }

    public boolean isReceiving(){
    	if(communicationNode==null) return false;
    	return communicationNode.isReceiving();
    }

    public boolean isFocused()
    {
        return this.focusStatus;
    }

    protected boolean focusStatus;

    public boolean isShowingRmouse()
    {
    	if(this.communicationNode!=null)
    	  return this.communicationNode.isShowingRmouse();
    	else
          return false;
    }

    public boolean isControlledByLocalUser()
    {
        if(communicationNode==null) return true;
        return communicationNode.isControlledByLocalUser();
    }
    public void clearAll()
    {
    }

    public ControlledFrame lookUp(String x)
    {
       ControlledFrame frame=null;
       if(this.communicationNode==null){ // this is called from applet ? 
    	   return null;
       }
       if(x.equals("node")){
    	   return this.communicationNode;
       }
       ApplicationID id=this.communicationNode.applicationManager.getRunningApplication(x);
       if(id==null){
           frame=(ControlledFrame)(this.communicationNode.applicationManager.spawnApplication2(
           x, this.communicationNode.getControlMode()));
       }
       else frame=(ControlledFrame)(id.application);
       return frame;
    }
    
    public void closeWindow(){
    	this.exitThis();
    }
    
   public String parseCommand(String x)
    {
        return null;
    }

    public void setVisible(boolean b)
    {
		if (b)
			setLocation(50, 50);
		super.setVisible(b);
    }

    public void focusGained()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
        this.focusStatus=true;
        if(this.isControlledByLocalUser())
           sendEvent("frm.fgain()\n");
    }

    public void focusLost()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
        this.focusStatus=false;
    }

    public void gainFocus()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
        this.requestFocus();
        this.focusStatus=true;
    }

    public void loseFocus()
    {
        // This method is derived from interface FrameWithControlledFocus
        // to do: code goes here
        this.focusStatus=false;
    }

    public void spawnRemote(ControlledFrame f, String applicationFrame)
    {
    }

    public String getControlMode()
    {
        if(this.communicationNode==null) return null;
        return this.communicationNode.getControlMode();
    }

    public String getNodeKind()
    {
    	if(this.communicationNode.className.equals("CommunicationNode")){
    		return "teacher";
    	}
    	else{
    		return "student";
    	}
    }
    public boolean isTeacher(){
    	if(this.getNodeKind().equals("teacher"))
    		return true;
    	else
    		return false;
    }
    public void recordMessage(String message)
    {
    }

    public EventBuffer ebuff;

    // public String encodingCode;

    public int pID;

    public CommunicationNode communicationNode;

    public boolean isReceivingEvents;

    public int getTimeNow()
    {
        if(communicationNode==null) return 0;
       return  communicationNode.eventRecorder.timer.getMilliTime();
    }

    public void dispose()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
//    	System.out.println("frame disposed.");
    	this.exitThis();
        super.dispose();
    }

    public void exitThis()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
        this.setVisible(false);
    }

    public void hide()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
        super.hide();
//          super.setVisible(false);
    }

    public void receiveEvent(String s)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
    }
    public void receiveEvent(AMessage m)
    {
         String s=m.getHead();
         receiveEvent(s);
    }

    public void sendAll()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
    }

    public void sendEvent(String s)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
    }
    public void sendEvent(AMessage m){
    	
    }

    public ControlledFrame spawnMain(CommunicationNode gui, String args, int pID, String controlMode)
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
        return null;
    }

    public void toFront()
    {
        // This method is derived from interface Spawnable
        // to do: code goes here
    }

    String communicationMode;


 /*
      setMode
         
         setting remote control mode
         mode: "local"  ... local execution, without remote controlled
               "broadcast" ... remote control, sendevent
               "teach"  ... remote control, sendevent, broadcast image.
               "remote" ... remote controlled. (load image directry from the source).
               "receive"... remote controlled, receive broadcasted image
               "common" ... common operation. receive envent and send event
               
 */
    public void setControlMode(String mode)
    {
    	this.communicationMode=mode;
    }
    public void setCommunicationNode(CommunicationNode cn){
    	this.communicationNode=cn;
    }

    public void mouseIsMovedAtTheFrame(int x, int y)
    {
    }

    public void mouseIsExitedAtTheFrame(int x, int y)
    {
    }

    public void mouseIsEnteredAtTheFrame(int x, int y)
    {
    }

    public void exitMouseAtTheFrame(int x, int y)
    {
//        this.rmouseIsShown=false;
          this.rmouse.setVisible(false);
    }

    public void enterMouseAtTheFrame(int x, int y)
    {
//        this.rmouseIsShown=true;
        this.rmouse.setVisible(true);
        this.moveMouseAtTheFrame(x,y);
    }

    public void moveMouseAtTheFrame(int x, int y)
    {
         rmouse.move(x,y);
         repaint();
   }

    public void activateRemoteMouse(boolean f)
    {
        if(this.communicationNode==null) return;
//        this.communicationNode.rmouseIsActivated =f;
        this.communicationNode.setRmouseActivate(f);
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        if(this.communicationNode==null) return;
        /*
        if(this.communicationNode.rmouseIsActivated && 
           this.rmouseIsShown) this.rmouse.paint(g);
        */
        this.rmouse.paint(g);
    }

    RemoteMouse rmouse;
    boolean rmouseIsShown;

    public void registerListeners()
    {
		this.enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK
		                | java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK);
		                
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymMouse aSymMouse = new SymMouse();
		this.addMouseListener(aSymMouse);
		SymMouseMotion aSymMouseMotion = new SymMouseMotion();
		this.addMouseMotionListener(aSymMouseMotion);
		
		rmouse=new RemoteMouse();
		this.rmouse.setVisible(false);
    }

	public ControlledFrame()
	{
		//{{INIT_CONTROLS
		getContentPane().setLayout(new BorderLayout(0,0));
		setSize(0,0);
		setVisible(false);
		//}}

		//{{REGISTER_LISTENERS
//		SymWindow aSymWindow = new SymWindow();
//		this.addWindowListener(aSymWindow);
//		SymMouse aSymMouse = new SymMouse();
//		this.addMouseListener(aSymMouse);
//		SymMouseMotion aSymMouseMotion = new SymMouseMotion();
//		this.addMouseMotionListener(aSymMouseMotion);
		//}}

		//{{INIT_MENUS
		//}}
        this.rmouse=new RemoteMouse();
	}

	public ControlledFrame(String title)
	{
		this();
		setTitle(title);
	}
/*
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
*/
	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	class SymWindow extends java.awt.event.WindowAdapter
	{
		
		
		public void windowClosing(java.awt.event.WindowEvent event)
		{
		Object object = event.getSource();
		if (object == ControlledFrame.this)
			ControlledFrame_WindowClosing(event);
		}
		public void windowIconified(java.awt.event.WindowEvent event)
		{
			Object object=event.getSource();
			if(object == ControlledFrame.this)
				ControlledFrame_WindowMinimizing(event);
		}
		public void windowDeiconified(java.awt.event.WindowEvent event)
		{
			Object object=event.getSource();
			if(object == ControlledFrame.this)
				ControlledFrame_WindowUnMinimizing(event);
		}
		
	}

	void ControlledFrame_WindowClosing(java.awt.event.WindowEvent event)
	{
		if(this.isControlledByLocalUser()){
			this.dispose(); // dispose of the Frame.
    		if(this.communicationNode==null) return;
	    	if(this.communicationNode.sendEventFlag){
		    	this.sendEvent("frm.close()\n");
		    }
		}
	}
	//{{DECLARE_CONTROLS
	//}}

	//{{DECLARE_MENUS
	//}}
	
	void ControlledFrame_WindowMinimizing(java.awt.event.WindowEvent event){
		if(this.isControlledByLocalUser()){
			this.minimizingWindow();
    		if(this.communicationNode==null) return;
	    	if(this.communicationNode.sendEventFlag){
		    	this.sendEvent("frm.minimize()\n");
		    }
		}
	}
	
	public void minimizingWindow(){
		this.setExtendedState(ICONIFIED);
		
	}
	
	void ControlledFrame_WindowUnMinimizing(java.awt.event.WindowEvent event){
		if(this.isControlledByLocalUser()){
			this.normalizingWindow();
    		if(this.communicationNode==null) return;
	    	if(this.communicationNode.sendEventFlag){
		    	this.sendEvent("frm.normalize()\n");
		    }
		}
	}

	public void normalizingWindow(){
		this.setExtendedState(NORMAL);		
	}
	

	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseExited(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledFrame.this)
				ControlledFrame_mouseExited(event);
		}

		public void mouseEntered(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledFrame.this)
				ControlledFrame_mouseEntered(event);
		}
	}

	void ControlledFrame_mouseEntered(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledFrame_mouseEntered_Interaction1(event);
	}

	void ControlledFrame_mouseEntered_Interaction1(java.awt.event.MouseEvent event)
	{
        int x=event.getX(); int y=event.getY();
        if(this.communicationNode!=null){
           if(!this.communicationNode.isShowingRmouse()) this.rmouse.setVisible(true);
           this.rmouse.setVisible(false);
          rmouse.move(x,y);
	    }	
	}

	void ControlledFrame_mouseExited(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledFrame_mouseExited_Interaction1(event);
	}

	void ControlledFrame_mouseExited_Interaction1(java.awt.event.MouseEvent event)
	{
        int x=event.getX(); int y=event.getY();
		this.rmouse.setVisible(false);
	}

	class SymMouseMotion extends java.awt.event.MouseMotionAdapter
	{
		public void mouseMoved(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ControlledFrame.this)
				ControlledFrame_mouseMoved(event);
		}
	}

	void ControlledFrame_mouseMoved(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		ControlledFrame_mouseMoved_Interaction1(event);
	}

	void ControlledFrame_mouseMoved_Interaction1(java.awt.event.MouseEvent event)
	{
		try {
//			this.show();
		} catch (java.lang.Exception e) {
		}
        int x=event.getX(); int y=event.getY();
        if(this.communicationNode!=null){
           if(this.communicationNode.isShowingRmouse()) mouseIsMovedAtTheFrame(x,y);
           else this.moveMouseAtTheFrame(x,y);
        }
	}
	public Vector<AMessage> getKeyFrame(){
		return null;
	}
    public void addKeyFrames(Vector<AMessage> x, Vector<AMessage> y){
    	if(x==null) return;
    	if(y==null) return;
    	for(int i=0;i<y.size();i++){
    		x.add(y.elementAt(i));
    	}
    }
}