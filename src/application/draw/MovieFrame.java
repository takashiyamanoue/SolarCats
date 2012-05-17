package application.draw;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;

import nodesystem.NodeSettings;
import nodesystem.StateContainer;
import controlledparts.ControlledButton;
import controlledparts.ControlledFrame;
import controlledparts.FrameWithControlledButton;
import controlledparts.SelectedButton;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class MovieFrame extends ControlledFrame 
implements FrameWithControlledButton, StateContainer, Runnable
{
	private JLabel jLabel1;
	private ControlledButton startButton;
	private ControlledButton stopButton;
	private DrawFrame gui;
    private Vector buttons;
    Thread me=null;

	public MovieFrame(){
		this.setTitle("sending movie");
	}
	
	public MovieFrame(DrawFrame gui){
		this.setTitle("sending movie");
		this.setEnabled(true);
		this.gui=gui;
		this.initGUI();
		this.buttons=new Vector();
		this.buttons.addElement(this.startButton);
		this.buttons.addElement(this.stopButton);
		for(int i=0;i<this.buttons.size();i++){
			ControlledButton b=(ControlledButton)(buttons.elementAt(i));
			b.setFrame(this);
			b.setID(i);
		}
	}
	
	private void initGUI() {
		try {
			{
				getContentPane().setLayout(null);
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1);
				jLabel1.setText("movie");
				jLabel1.setBounds(6, 25, 164, 28);
			}
			{
				startButton = new ControlledButton();
				getContentPane().add(startButton);
				startButton.setText("start");
				startButton.setBounds(0, 0, 89, 25);
			}
			{
				stopButton = new ControlledButton();
				getContentPane().add(stopButton);
				stopButton.setText("stop");
				stopButton.setBounds(89, 0, 86, 25);
			}
			{
				this.setSize(191, 90);
			}
			 super.registerListeners();
			 
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    AFigure currentBackgroundImage;
    Robot robot=null;
	void getImage(){
    	if(!this.isControlledByLocalUser()) return; 
//      this.communicationNode.setStoppingLockTimer(true);
  	Rectangle ro=this.getBounds();
  	Point p=ro.getLocation();
  	int xo=p.x;
  	int yo=p.y;
  	int width=ro.width;
  	int height=ro.height;
  	int dx=88;
  	int dy=125;                            
  	int xn=xo+dx;
  	int yn=yo+dy;
   	int wn=width-dx-20;
  	int hn=height-dy-20;

  	ro.setLocation(xn,yn);
  	ro.width=wn;
  	ro.height=hn;
//  	this.gui.show(false);
//  	this.setVisible(false);
//  	this.minimizingWindow();
  	try{
  		Thread.sleep(200);
  	}
  	catch(InterruptedException e){}
  	try{
  		if(this.robot==null){
  	       this.robot=new Robot();
  		}
  	}
  	catch(Exception e){
  		System.out.println(e.toString());
  	}
  	BufferedImage bi=robot.createScreenCapture(ro);
  	if(this.currentBackgroundImage==null||
  		this.gui.imageManager.getImageOperator("draw-background-0")==null){
  	    this.currentBackgroundImage=new MyImage(this.gui.canvas,bi,"draw-background-0",
  	    		                 this.gui.communicationNode.getNodeSettings());
          currentBackgroundImage.newFig();
          this.gui.canvas.fs.add(currentBackgroundImage);
          this.gui.canvas.fs.setStep(-10);
          currentBackgroundImage.depth=25;
          this.gui.depthIndicator.setValue(currentBackgroundImage.depth);
  	}
  	else{
  		NodeSettings s=this.gui.communicationNode.getNodeSettings();
  		((MyImage)this.currentBackgroundImage).updateImage(bi,s.getInt("pictureSegmentWidth"));
  	}
//       if(this.currentBackgroundImage!=null){
//      	
//      }
//      else{
//      }
//      this.currentBackgroundImage=newfig;
      if(this.gui.communicationNode.sendEventFlag){
//      	System.out.println("start sending image");
          this.gui.imageManager.start();
      }
    //
//  	this.gui.show(true);		
	}
	
	@Override
	public void clickButton(int i) {
		// TODO Auto-generated method stub
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        b.click();
        this.mouseClickedAtButton(i);
		
	}

	@Override
	public void focusButton(int i) {
		// TODO Auto-generated method stub
        ControlledButton button=(ControlledButton)(buttons.elementAt(i));
//      button.controlledButton_mouseEntered(null);
      button.focus();		
	}

	@Override
	public void mouseClickedAtButton(int i) {
		// TODO Auto-generated method stub
		if(i==this.startButton.getID()){
			this.start();
		}
		if(i==this.stopButton.getID()){
			this.stop();
//			this.gui.setVisible(true);
			this.gui.show(true);
			this.setVisible(false);
		}
	}

	@Override
	public void mouseEnteredAtButton(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExitedAtButton(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEvent(String x) {
		// TODO Auto-generated method stub
        gui.sendEvent("dmovie."+x);		
	}

	@Override
	public void unfocusButton(int i) {
		// TODO Auto-generated method stub
        ControlledButton button=(ControlledButton)(buttons.elementAt(i));
//      button.controlledButton_mouseExited(null);
      button.unFocus();
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int lastTime=this.gui.getTimeNow();
		int stopInterval=120000; // 1 min.
		while(me!=null){
			this.gui.getImageFromDisplay();
//			this.gui.setVisible(false);
//			this.gui.show(false);
			while(!this.gui.isNoMoreSendingImage()){
			   try{
				   Thread.sleep(10);
			   }
			   catch(InterruptedException e){
				   System.out.println(""+e.toString());
			   }
			}
			if(!this.gui.isTeacher()){
				if(!this.gui.isSending()){
					this.stop();
					this.gui.show(true);
					this.setVisible(false);
					return;
				}
			}
			try{
				   Thread.sleep(10);
			}
			catch(InterruptedException e){
				   System.out.println(""+e.toString());
			}
		}
		
	}
    public void start(){
    	if(this.me==null){
    		me=new Thread(this,"movie thread");
    		me.start();
    	}
    }
    public void stop(){
    	if(this.me!=null){
    		me=null;
    	}
    }

	@Override
	public int getState() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setState(int i) {
		// TODO Auto-generated method stub
		
	}
}
