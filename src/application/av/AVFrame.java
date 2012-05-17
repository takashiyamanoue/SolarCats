/*
 * 作成日: 2005/01/16
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package application.av;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Vector;

import javax.media.DataSink;
import javax.media.Format;
import javax.media.Manager;
import javax.media.Processor;
import javax.media.ProcessorModel;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;

import application.draw.ParseDrawEvent;

import nodesystem.AMessage;
import nodesystem.CommunicationNode;
import nodesystem.eventrecorder.Timer;
import util.AudioCapture;
import util.AudioReceiver;
import util.AudioTransmitter;
import util.CaptureUtil;
import util.Indicator;
import util.MonitorStream;
import util.Parameters;
import util.SendReceiveAVControl;
import util.VideoManager;
import util.VideoOperator;
import controlledparts.ControlledButton;
import controlledparts.ControlledFrame;
import controlledparts.ControlledTextField;
import controlledparts.FrameWithControlledButton;
import controlledparts.FrameWithControlledTextField;
import controlledparts.InputQueue;
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
/**
 * @author yamachan
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
public class AVFrame extends ControlledFrame 
implements ItemListener, ActionListener, FrameWithControlledTextField,
             FrameWithControlledButton, SendReceiveAVControl, Indicator 
{

    // GUI parts
    private JPanel subAvPanel4;
    private ControlledTextField userNameField1;
    private ControlledTextField myNameField;
    private ControlledButton endButton;
    private ControlledButton startButton;
    private JLabel jLabel1;
    private ControlledButton exitButton;
    private ControlledTextField userNameField4;
    private ControlledTextField userNameField3;
    private ControlledTextField userNameField2;
    private JPanel subAvPanel3;
    private JPanel subAvPanel2;
    private JPanel subAvPanel1;
    private JPanel avPanelFrame;
    public Panel avPanel;
    private ControlledTextField userNameField0;
    private ControlledButton sendReceiveButton;
    private ControlledButton receiveButton;
    private Panel myPicturePanel;
    private JPanel myPictureFramePanel;
    
	private JSlider volLevSldr1;
	private JProgressBar sendBar1;
	private JLabel volLevel;
	private JLabel barText;    
    
    private Vector buttons;
    private Vector texts;
    private boolean sending;

	// JMF objects
	private Processor processor = null;
	private DataSink datasink = null;
	private Component monitor = null;
	private DataSource datasource = null;
	private String outputType = "video.quicktime";
	private MonitorStream monitorStream;

    VideoManager videoManager;
    VideoOperator myface=null;
    
    AudioCapture acapture;
    AudioTransmitter transmitter;
    private JButton videoSettingsButton;
    private JButton skipControlButton;
    AudioReceiver areceiver;
    
    ParseAVFrameEvent parser;
    private Color sendButtonColor;
	VideoSettingFrame videoSetting;

    public AVFrame() 
    {

		setTitle("AVFrame");
		setResizable(false);
		getContentPane().setLayout(null);
		this.setSize(369, 330);
	
	    this.setEnabled(true);
	    
	    {
	    	sendReceiveButton = new ControlledButton();
	    	this.getContentPane().add(sendReceiveButton);
	    	sendReceiveButton.setBounds(182, 205, 80, 20);
	    	sendReceiveButton.setText("send");
	    	this.sendButtonColor=sendReceiveButton.getBackground();
	    }
	    {
	    	receiveButton = new ControlledButton();
	    	this.getContentPane().add(receiveButton);
	    	receiveButton.setBounds(264, 205, 80, 20);
	    	receiveButton.setText("receive");
//	    	this.sendButtonColor=sendReceiveButton.getBackground();
	    }
	    
	    /*
		{
			userNameField1 = new ControlledTextField();
			this.getContentPane().add(userNameField1);
			userNameField1.setText("user1");
			userNameField1.setBounds(16, 287, 79, 16);
		}
		{
			userNameField2 = new ControlledTextField();
			this.getContentPane().add(userNameField2);
			userNameField2.setText("user2");
			userNameField2.setBounds(98, 287, 81, 16);
		}
		{
			userNameField3 = new ControlledTextField();
			this.getContentPane().add(userNameField3);
			userNameField3.setText("user3");
			userNameField3.setBounds(182, 287, 77, 16);
		}
		{
			userNameField4 = new ControlledTextField();
			this.getContentPane().add(userNameField4);
			userNameField4.setText("user4");
			userNameField4.setBounds(263, 287, 79, 15);
		}
		*/
         
	    {
	    	userNameField0= new ControlledTextField();
	    	this.getContentPane().add(userNameField0);
	    	userNameField0.setLayout(null);
	    	userNameField0.setBounds(15, 185, 165, 20);
	    	userNameField0.setEditable(false);
	    	userNameField0.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED, null, null));
	    	userNameField0.setText("Remote node");

	    }
	    
	    /*
	    {
		  subAvPanel1 = new JPanel();
		  this.getContentPane().add(subAvPanel1);
		  subAvPanel1.setLayout(null);
		  subAvPanel1.setBounds(15, 225, 82, 62);
		  subAvPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
	    }
	    */
	    /*
	    {
		  subAvPanel2 = new JPanel();
		  this.getContentPane().add(subAvPanel2);
		  subAvPanel2.setBounds(97, 225, 83, 62);
		  subAvPanel2.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
	    }
	    */
	    /*
	    {
	  	  subAvPanel3 = new JPanel();
		  this.getContentPane().add(subAvPanel3);
		  subAvPanel3.setLayout(null);
		  subAvPanel3.setBounds(180, 225, 82, 62);
	  	  subAvPanel3.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
	    }
	    */
	    /*
	    {
		  subAvPanel4 = new JPanel();
		  this.getContentPane().add(subAvPanel4);
		  subAvPanel4.setLayout(null);
		  subAvPanel4.setBounds(262, 225, 83, 62);
		  subAvPanel4.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
	    }
	    */
		{
		  avPanelFrame = new JPanel();
		  this.getContentPane().add(avPanelFrame);
		  avPanelFrame.setLayout(null);
		  avPanelFrame.setBounds(15, 60, 165, 125);
		  avPanelFrame.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		}
		{
		  avPanel = new Panel();
		  avPanelFrame.add(avPanel);
		  avPanel.setLayout(null);
		  avPanel.setBounds(3, 3, 160, 120);
		}
		{
			myPictureFramePanel = new JPanel();
			this.getContentPane().add(myPictureFramePanel);
			myPictureFramePanel.setLayout(null);
			myPictureFramePanel.setBounds(180, 60, 165, 125);
			myPictureFramePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		}
        {
        	myPicturePanel=new Panel();
        	myPictureFramePanel.add(myPicturePanel);
        	myPicturePanel.setLayout(null);
        	myPicturePanel.setBounds(3,3,160,120);        	
        }
        
		{
			myNameField = new ControlledTextField();
			this.getContentPane().add(myNameField);
			myNameField.setText("This node");
			myNameField.setBounds(180, 185, 165, 20);
		}
		
		{
			exitButton = new ControlledButton();
			this.getContentPane().add(exitButton);
			exitButton.setText("exit");
			exitButton.setBounds(284, 31, 59, 22);
		}
		{
			jLabel1 = new JLabel();
			this.getContentPane().add(jLabel1);
			jLabel1.setText("DSR/Audio Video Communication Frame");
			jLabel1.setBounds(5, 6, 274, 21);
		}
		/*
		{
			startButton = new ControlledButton();
			this.getContentPane().add(startButton);
			startButton.setText("start");
			startButton.setBounds(145, 31, 73, 22);
			
		}*/
		/*
		{
			endButton = new ControlledButton();
			this.getContentPane().add(endButton);
			endButton.setText("end");
			endButton.setBounds(218, 31, 66, 22);
		}
		*/
		
		{

			final int PROGRESS_MAX =10000;
			final int PROGRESS_MIN = 0;
			sendBar1 = new JProgressBar(PROGRESS_MIN,PROGRESS_MAX);
			this.getContentPane().add(sendBar1);
			sendBar1.setBounds(215, 261, 120, 15);
		}
		{
			int max = sendBar1.getMaximum();
			int min = sendBar1.getMinimum();
			volLevSldr1 = new JSlider(min,max,min);
			this.getContentPane().add(volLevSldr1);
			volLevSldr1.setBounds(216, 231, 120, 20);
		}
		{
			barText = new JLabel();
			this.getContentPane().add(barText);
			barText.setText("squelch:");
			barText.setBounds(133, 231, 70, 21);
		}
		{
			volLevel = new JLabel();
			this.getContentPane().add(volLevel);
			volLevel.setText("SendVol:");
			volLevel.setBounds(132, 261, 56, 15);
		}
		{
			sendingIndicatorLabel = new JLabel();
			getContentPane().add(sendingIndicatorLabel);
			sendingIndicatorLabel.setText("sending");
			sendingIndicatorLabel.setBounds(77, 203, 63, 21);
		}
		{
			skipControlButton = new JButton();
			getContentPane().add(skipControlButton);
			skipControlButton.setText("skip control");
			skipControlButton.setBounds(12, 230, 110, 25);
			skipControlButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					skipControlButtonActionPerformed(evt);
				}
			});
		}
		{
			videoSettingsButton = new JButton();
			getContentPane().add(videoSettingsButton);
			videoSettingsButton.setText("video setting");
			videoSettingsButton.setBounds(12, 254, 110, 24);
			videoSettingsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					videoSettingsButtonActionPerformed(evt);
				}
			});
		}

	    super.registerListeners();
	    this.setEnabled(true);
	    
	    FrameAction fAction=new FrameAction();
	    this.exitButton.addActionListener(fAction);
		FrameWindow fWindow = new FrameWindow();
		this.addWindowListener(fWindow);
	    
	    this.buttons=new Vector();
//	    buttons.add(this.speakOutRequestButton);
	    buttons.add(this.exitButton);
//	    buttons.add(this.startButton);
//	    buttons.add(this.endButton);
	    buttons.add(this.sendReceiveButton);
	    buttons.add(this.receiveButton);
	    int numberOfButtons=buttons.size();
	    for(int i=0;i<numberOfButtons;i++){
	    	ControlledButton b=(ControlledButton)(buttons.elementAt(i));
			b.addActionListener(this);
	    	b.setFrame(this);
	    	b.setID(i);
	    }
	    this.texts=new Vector();
	    this.texts.add(this.userNameField0);
	    /*
	    this.texts.add(this.userNameField1);
	    this.texts.add(this.userNameField2);
	    this.texts.add(this.userNameField3);
	    this.texts.add(this.userNameField4);
	    */
	    this.texts.add(this.myNameField);
	    int numberOfTexts=texts.size();
	    for(int i=0;i<numberOfTexts;i++){
	    	ControlledTextField t=(ControlledTextField)(texts.elementAt(i));
	    	t.setFrame(this);
	    	t.setID(i);
	    }
		setVisible(true);
	    
	    videoManager= new VideoManager(this);
	    this.setSendingAV(false);
	    this.transmitter=new AudioTransmitter(this);
	    this.transmitter.setIndicator(this);
	    this.acapture=new AudioCapture(transmitter);
	    this.areceiver=new AudioReceiver(this.acapture);
        this.skipFrame=new AVSkipSettingFrame();
        this.videoSetting=new VideoSettingFrame(this);
        this.areceiver.setSkipParameters(this.getAudioSkipParameters());
    }

    public void setLevel(int x){
    	 
    	sendBar1.setValue(x);
    }
    
    public int getLimit(){
    	return volLevSldr1.getValue();
    }

    public void clearAll(){
//		start av capture
			 this.videoManager.stop();
			 this.startMonitoring();
			 this.setSendingAV(false);
//			 VideoOperator myface=new VideoOperator(this,null,null, null, this.avPanel);
			 myface=new VideoOperator(this,this.avPanel);
			 myface.setParameters(this.videoSetting);
			 myface.setMonitorStream(this.monitorStream);
			 this.videoManager.add("receivingFace",myface);
   	
    }
    
	class FrameAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object==exitButton) {
//			   exit();
			}
		}
	}

	/*
	private void videoSettingButtonActionPerformed(ActionEvent evt) {
//		System.out.println("videoSettingButton.actionPerformed, event="+evt);
		//TODO add your code for videoSettingButton.actionPerformed
		this.videoSetting.setVisible(true);
	}
*/
	class FrameWindow extends java.awt.event.WindowAdapter
	{
		public void windowActivated(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == AVFrame.this)
				This_windowActivated(event);
		}

		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == AVFrame.this)
				This_windowClosing(event);
		}
	}

	void This_windowActivated(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		This_windowActivated_Interaction1(event);
	}

	void This_windowActivated_Interaction1(java.awt.event.WindowEvent event)
	{
		try {
			this.focusGained();
		} catch (java.lang.Exception e) {
		}
	}
    
	void This_windowClosing(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		This_windowClosing_Interaction1(event);
	}

	void This_windowClosing_Interaction1(java.awt.event.WindowEvent event)
	{
		try {
//			this.exit();
		} catch (Exception e) {
		}
	}
	VideoFormat[] vfs;
	AudioFormat[] afs;
	int maxAfs;
	int maxVfs;
	private float captureFrameRate=2f;
	private JLabel sendingIndicatorLabel;

	private void setFormats(){
		AudioFormat af=null;
		VideoFormat vf=null;
		if(captureFrameRate==0f){
			captureFrameRate=2f;
		}

		// Need audio
       int samplingRate=8000;
//		int samplingSize = radio8bit.getState() ? 8:16;
        int samplingSize=8;
//		int channels = radioMono.getState()? 1:2;
        int channels = 1;
//		af = new AudioFormat(AudioFormat.LINEAR, samplingRate, samplingSize,
//				 channels);

//	if (checkVideo.getState()) {
//		String encoding = comboEncoding.getSelectedItem();
        String encoding = "RGB";
//		String strSize = comboSize.getSelectedItem();
//		StringTokenizer st = new StringTokenizer(strSize, "x");
//		int sizeX = Integer.parseInt(st.nextToken());
//		int sizeY = Integer.parseInt(st.nextToken());
        int sizeX = 160; //160
        int sizeY = 120; //120
		Dimension size = new Dimension(sizeX, sizeY);

/*
		vf = new VideoFormat(encoding, size, Format.NOT_SPECIFIED,
				 null, 15f);
				 */
		vf = new VideoFormat(encoding, size, Format.NOT_SPECIFIED,
				 null, this.captureFrameRate); // 2007/1/7 t.yamanoue
		afs=new AudioFormat[10];
		vfs=new VideoFormat[10];
		afs[0]=af;
		vfs[0]=vf;
		encoding="YUV";
		vf = new VideoFormat(encoding, size, Format.NOT_SPECIFIED,
				 null, this.captureFrameRate); // 2007/1/7 t.yamanoue
        vfs[1]=vf;		
        maxAfs=1;
        maxVfs=2;
		
	}
	private void startMonitoring() {
	// Close the previous processor, which in turn closes the capture device

		if (processor != null) {
			processor.stop();
			processor.close();
		}
	
		// Remove the previous monitor
		if (monitor != null) {
			myPicturePanel.remove(monitor);
			monitor = null;
		}

        this.setFormats();
		VideoFormat vf=null;
		AudioFormat af=null;
        for(int i=0;i<maxVfs;i++){
    	// Use CaptureUtil to create a monitored capture datasource
        	vf=vfs[i];
	       datasource = CaptureUtil.getCaptureDS(vf, null);
	       if(datasource!=null) break;
        }
        
	    if (datasource != null) {
		    // Set the preferred content type for the Processor's output
//		    outputType = "video.quicktime";
//		    if (comboFileType.getSelectedItem().equals("AVI"))
		    outputType = "video.x_msvideo";
		    FileTypeDescriptor ftd = new FileTypeDescriptor(outputType);
		    Format [] formats = null;

    		if (af != null && vf != null) {
        		formats = new Format[] { new AudioFormat(null),
		    			 new VideoFormat(null) };
//			    		 System.out.println("datasource-af and vf !=null");
		    }

		    if (af == null)
    		    formats = new Format[] {new VideoFormat(null)};

        	ProcessorModel pm = new ProcessorModel(datasource, formats, ftd);
		    try {
    		    processor = Manager.createRealizedProcessor(pm);
		    } catch (Exception me) {
    		    System.err.println(me);
    		    // Make sure the capture devices are released
    		    datasource.disconnect();
    		    return;
		    }

		// Get the monitor control:
		// Since there are more than one MonitorControl objects
		// exported by the DataSource, we get the specific one
		// that is also the MonitorStream object.
		    MonitorStream ms = (MonitorStream)datasource.getControl("javax.media.control.MonitorControl");
		    if (ms != null) {
    		    monitor = ms.getControlComponent();
    		    this.myPicturePanel.add(monitor);
    		    if(monitor==null) System.out.println("monitor=null");
    		    // Make sure the monitor is enabled
    		    ms.setEnabled(true);
    		    monitorStream=ms;
		    }
		    // audio part
//		    acapture.captureAudio();
	    } // end of if(deatsource!=null) ..
	}

	void endMonitoring(){
		if (processor != null)
			processor.close();
		// Remove the previous monitor
		if (monitor != null) {
			myPicturePanel.remove(monitor);
			monitor = null;
		}
		if(acapture!=null)
		     acapture.stopCapture();
		if(transmitter!=null)
		    transmitter.stop();
	}
 
	/* (非 Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextField#enterPressed(int)
	 */
	public void enterPressed(int id) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#setTextOnTheText(int, java.lang.String)
	 */
	public void setTextOnTheText(int i, int pos,String s) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#exitMouseOnTheText(int, int, int)
	 */
	public void exitMouseOnTheText(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledTextField t=(ControlledTextField)(texts.elementAt(id));
		t.exitMouse();
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#enterMouseOnTheText(int, int, int)
	 */
	public void enterMouseOnTheText(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledTextField t=(ControlledTextField)(texts.elementAt(id));
		t.enterMouse(x,y);		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mouseExitAtTheText(int, int, int)
	 */
	public void mouseExitAtTheText(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mouseEnteredAtTheText(int, int, int)
	 */
	public void mouseEnteredAtTheText(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#moveMouseOnTheText(int, int, int)
	 */
	public void moveMouseOnTheText(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledTextField t=(ControlledTextField)(texts.elementAt(id));
		//   t.rmouse.move(x,y);
		t.moveMouse(x,y);		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mouseMoveAtTextArea(int, int, int)
	 */
	public void mouseMoveAtTextArea(int id, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#pressMouseOnTheText(int, int, int, int)
	 */
	public void pressMouseOnTheText(int i, int p, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mousePressedAtTextArea(int, int, int, int)
	 */
	public void mousePressedAtTextArea(int i, int p, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#releaseMouseOnTheText(int, int, int, int)
	 */
	public void releaseMouseOnTheText(int id, int position, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledTextField t=(ControlledTextField)(texts.elementAt(id));
		t.releaseMouse(position,x,y);		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#dragMouseOnTheText(int, int, int, int)
	 */
	public void dragMouseOnTheText(int id, int position, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mouseReleasedAtTextArea(int, int, int, int)
	 */
	public void mouseReleasedAtTextArea(int id, int position, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mouseDraggedAtTextArea(int, int, int, int)
	 */
	public void mouseDraggedAtTextArea(int id, int position, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#clickMouseOnTheText(int, int, int, int)
	 */
	 
	int lastManipulatedTextField;
	public void clickMouseOnTheText(int i, int p, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		this.lastManipulatedTextField=i;
		ControlledTextField t=(ControlledTextField)(texts.elementAt(i));
		t.clickMouse(p,x,y);		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#typeKey(int, int, int)
	 */
	public void typeKey(int i, int p, int key) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledTextField t=(ControlledTextField)(texts.elementAt(i));
		t.typeKey(p,key);		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#mouseClickedAtTextArea(int, int, int, int)
	 */
	public void mouseClickedAtTextArea(int i, int p, int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#keyIsTypedAtATextArea(int, int, int)
	 */
	public void keyIsTypedAtATextArea(int i, int p, int key) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	public void keyIsPressedAtATextArea(int i, int p, int key){
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledButton#clickButton(int)
	 */
	public void clickButton(int i) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledButton t=(ControlledButton)(buttons.elementAt(i));
		t.click();
		this.mouseClickedAtButton(i);		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledButton#unfocusButton(int)
	 */
	public void unfocusButton(int i) {
		// TODO 自動生成されたメソッド・スタブ
	   	SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//	 	   button.controlledButton_mouseExited(null);
		 button.unFocus();
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledButton#focusButton(int)
	 */
	public void focusButton(int i) {
		// TODO 自動生成されたメソッド・スタブ
		SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//		  button.controlledButton_mouseEntered(null);
		button.focus();
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledButton#mouseClickedAtButton(int)
	 */
	Color buttonColor;
	public void mouseClickedAtButton(int i) {
		// TODO 自動生成されたメソッド・スタブ
//		if(!this.isControlledByLocalUser()) return;
/*
		if(i==this.startButton.getID()){
			this.videoManager.stop();
			this.startMonitoring();
			this.setSendingAV(false);
			VideoOperator myface=new VideoOperator(this,null,null, null, this.avPanel);
			this.videoManager.add("receivingFace",myface);
			this.sendReceiveButton.setText("send/receive-r");
			return;
		}
		if(i==this.endButton.getID()){
			this.endMonitoring();
			this.sendReceiveButton.setText("send/receive");
			this.videoManager.stop();
//			this.audioSourceStream.stop();
		}
		*/
		if(i==this.exitButton.getID()){
			// AV capture end.
			this.acapture.stopCapture();
			this.videoManager.stop();
			this.videoManager.remove("receivingFace");
			this.endMonitoring();
//			this.sendReceiveButton.setText("send/receive");
			
			this.exitThis();
		}
		if(i==this.sendReceiveButton.getID()){
//			if(!this.isReceiving()){
//            if(!this.isSendingAV()){
//    			if(this.isControlledByLocalUser()){
					this.setSendingAV(true);
//	    			VideoOperator myface=new VideoOperator(this,this.monitorStream, this.datasource, this.monitor,this.avPanel);
					myface.setDataSource(this.datasource);
    				myface.setMonitorStream(this.monitorStream);
    				myface.setSourcePanel(this.monitor);
		    		this.videoManager.add("receivingFace",myface);	
			    	this.videoManager.start();
//			    	this.transmitter.start();
			    	this.acapture.transmitter.setLineID("aline");
			    	this.acapture.captureAudio();
			    	this.acapture.startCapture();
//                    this.sendReceiveButton.setText("send");
//                    this.sendReceiveButton.setBackground(Color.red);
//			    	this.sendingIndicatorLabel.setBackground(Color.red);
			    	this.sendingIndicatorLabel.setForeground(Color.red);
                    /*
			    }
    			else{
    				this.setSendingAV(false);
    				this.videoManager.stop();
    				this.transmitter.stop();
					VideoOperator myface=new VideoOperator(this,null,null, null, this.avPanel);
					this.videoManager.add("receivingFace",myface);
                    this.sendReceiveButton.setText("send/receive-r");
    			}
    			*/
//            }
//			else{
//				if(this.isControlledByLocalUser()){
				/*
					this.setSendingAV(false);
					this.videoManager.stop();
					this.transmitter.stop();
					VideoOperator myface=new VideoOperator(this,null,null, null, this.avPanel);
					this.videoManager.add("receivingFace",myface);
//					this.sendReceiveButton.setBackground(buttonColor);
                    this.sendReceiveButton.setText("send/receive-r");
                    this.sendReceiveButton.setBackground(this.sendButtonColor);
                    */
//				}

				
//			}
		}
		if(i==this.receiveButton.getID()){
			this.setSendingAV(false);
			this.videoManager.stop();
//			this.transmitter.stop();
			this.acapture.stopCapture();
//			VideoOperator myface=new VideoOperator(this,null,null, null, this.avPanel);
//			VideoOperator myface=new VideoOperator(this,this.avPanel);
//			this.videoManager.add("receivingFace",myface);
			this.videoManager.remove("receivingFace");
//			this.sendReceiveButton.setBackground(buttonColor);
//            this.sendReceiveButton.setText("receive-r");
//            this.sendReceiveButton.setBackground(this.sendButtonColor);
			this.sendingIndicatorLabel.setForeground(Color.black);
			
		}
	}

   public boolean isSendingAV(){
   	   if(this.sending){
   	   	  return this.isControlledByLocalUser();
   	   }
   	   return this.sending;
   }

   public void setSendingAV(boolean tf){
   	   this.sending=tf;
   }

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledButton#mouseExitedAtButton(int)
	 */
	public void mouseExitedAtButton(int i) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledButton#mouseEnteredAtButton(int)
	 */
	public void mouseEnteredAtButton(int i) {
		// TODO 自動生成されたメソッド・スタ

		
	}
	
	String commandName="avframe"; // command
	public String getCommandName(){
		return commandName;
	}
	
	public void sendEvent(String x){
		if(this.communicationNode==null) return;
		this.communicationNode.sendEvent(commandName,x);		
	}
	
	public void sendEvent(AMessage m){
		if(this.communicationNode==null) return;
//		System.out.println(commandName+"-"+m.getHead());
		this.communicationNode.sendEvent(commandName,m);		
	}
	
	public static void main(String[] args) {
	   AVFrame frame = new AVFrame();
	}
	public void receiveEvent(String s)
	{
		// This method is derived from interface Spawnable
		// to do: code goes here
//		if(!this.communicationNode.isReceivingEvents) return;
        if(!this.isReceiving()) return;
		InputQueue iq=null;
		try{
		   BufferedReader dinstream=new BufferedReader(
//				new InputStreamReader(
//				new StringBufferInputStream(s),encodingCode)
				new StringReader(s)
		   );
		   iq=new InputQueue(dinstream);
		}
//		  catch(UnsupportedEncodingException e){
		catch(Exception e){
			System.out.println("exception:"+e);
			return;
		}
		if(iq==null) return;
			ParseAVFrameEvent evParser=new ParseAVFrameEvent(this,iq);
			evParser.parsingString=s;
			evParser.run();
	}
    public void receiveEvent(AMessage m)
    {
    	String s=m.getHead();
        if(this.communicationNode==null) return;
//        if(!this.communicationNode.isReceivingEvents) return;
        if(!this.isReceiving()) return;
//        System.out.println(this.commandName+"-"+m.getHead());
        BufferedReader dinstream=null;
        try{
            dinstream=new BufferedReader(
               new StringReader(s)
            );
        }
        catch(Exception e){
            System.out.println("exception:"+e);
        }
        if(dinstream==null) return;
            InputQueue iq=new InputQueue(dinstream);
			ParseAVFrameEvent evParser=new ParseAVFrameEvent(this,iq);
            evParser.setBinary(m.getData());
			evParser.setSentTime(m.getTime());
            evParser.parsingString=s;
            evParser.run();
    }    
	public void exitThis()
	{
		// This method is derived from interface Spawnable
		// to do: code goes here
//		  System.out.println("basicframe exithis");
//		this.endMonitoring();
		this.setVisible(false);
//		   hide();         // Frame を非表示にする.
	}

	public ControlledFrame spawnMain(CommunicationNode cn,String args,int pID, String controlMode)
	{
		   AVFrame bf=this;
		   bf.communicationNode=cn;
		   bf.pID=pID;
//			 bf.encodingCode=code;
		   bf.setTitle("AVFrame");
		
		// spawn Basic Slaves at other nodes in this group.
		   bf.ebuff=cn.commandTranceiver.ebuff;

//			 bf.show();
		   bf.setVisible(true);

		   return bf;
	}
	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#keyIsReleasedAtTextArea(int, int)
	 */
	public void keyIsReleasedAtTextArea(int id, int p, int code) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#releaseKey(int, int)
	 */
	public void releaseKey(int i, int p, int code) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledTextField t=(ControlledTextField)(texts.elementAt(i));
//		t.releaseKey(p);		

	}
	public void pressKey(int i, int p, int code){
		ControlledTextField t=(ControlledTextField)(texts.elementAt(i));
//		t.releaseKey(p);				
	}
	
	AVSkipSettingFrame skipFrame;
	private void skipControlButtonActionPerformed(ActionEvent evt) {
//		System.out.println("skipControlButton.actionPerformed, event="+evt);
		//TODO add your code for skipControlButton.actionPerformed
		if(this.skipFrame==null){
			this.skipFrame=new AVSkipSettingFrame();
		}
		skipFrame.setVisible(true);
	}
    public Parameters getVideoSkipParameters(){
    	return this.skipFrame.getVideoSkipParameters();
    }
    public Parameters getAudioSkipParameters(){
    	return this.skipFrame.getAudioSkipParameters();
    }
    
    private void videoSettingsButtonActionPerformed(ActionEvent evt) {
//    	System.out.println("videoSettingsButton.actionPerformed, event="+evt);
    	//TODO add your code for videoSettingsButton.actionPerformed
		this.videoSetting.setVisible(true);
    }
    public Timer getTimer(){
    	if(this.communicationNode==null) return null;
    	if(this.communicationNode.eventRecorder==null) return null;
    	return this.communicationNode.eventRecorder.timer;
    }

}
