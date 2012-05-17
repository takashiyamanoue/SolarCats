package application.av;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import util.Parameters;

public class VideoSettingFrame extends JFrame 
implements Parameters
{
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel11;
	private JComboBox errorRateComboBox;
	private JLabel jLabel10;
	private JComboBox pictureCodingComboBox;
	private JLabel jLabel9;
	private JComboBox pictureSegmentSizeCombox;
	private JLabel jLabel8;
	private JLabel jLabel7;
	private JButton cancelButton;
	private JButton saveButton;
	private JComboBox errorSamplingWidthComboBox;
	private JLabel jLabel6;
	private JTextField videoIntervalField;
	private JTextField captureFrameRateField;
	private JTextField sendIntervalField;

	int sendInterval=5;
	float captureFrameRate=5f; //ì«Ç›îÚÇŒÇµä‘äuÇÃèâä˙íl
	int videoInterval=200;
	int pictureSegmentWidth=32;
	String pictureCoding="slowFine";
	int errorRate=10;
	int errorSamplingWidth=4;
	
	Properties setting;
	String fileName="video-setting.properties";

	private void initGUI() {
		try {
			{
				getContentPane().setLayout(null);
				{
					jLabel1 = new JLabel();
					getContentPane().add(jLabel1);
					jLabel1.setText("send interval");
					jLabel1.setBounds(26, 23, 116, 19);
				}
				{
					jLabel2 = new JLabel();
					getContentPane().add(jLabel2);
					jLabel2.setText("capture frame rate");
					jLabel2.setBounds(26, 48, 123, 19);
				}
				{
					jLabel3 = new JLabel();
					getContentPane().add(jLabel3);
					jLabel3.setText("video interval");
					jLabel3.setBounds(26, 73, 108, 19);
				}
				{
					sendIntervalField = new JTextField();
					getContentPane().add(sendIntervalField);
					sendIntervalField.setText("5");
					sendIntervalField.setBounds(160, 20, 103, 25);
				}
				{
					captureFrameRateField = new JTextField();
					getContentPane().add(captureFrameRateField);
					captureFrameRateField.setText("5f");
					captureFrameRateField.setBounds(161, 45, 102, 25);
				}
				{
					jLabel4 = new JLabel();
					getContentPane().add(jLabel4);
					jLabel4.setText("frame/sec.");
					jLabel4.setBounds(275, 48, 87, 19);
				}
				{
					jLabel5 = new JLabel();
					getContentPane().add(jLabel5);
					jLabel5.setText("msec.");
					jLabel5.setBounds(275, 23, 75, 19);
				}
				{
					videoIntervalField = new JTextField();
					getContentPane().add(videoIntervalField);
					videoIntervalField.setText("250");
					videoIntervalField.setBounds(161, 70, 102, 25);
				}
				{
					jLabel6 = new JLabel();
					getContentPane().add(jLabel6);
					jLabel6.setText("msec.");
					jLabel6.setBounds(275, 73, 63, 19);
				}
				{
					saveButton = new JButton();
					getContentPane().add(saveButton);
					saveButton.setText("save");
					saveButton.setBounds(77, 241, 65, 25);
					saveButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							saveButtonActionPerformed(evt);
						}
					});
				}
				{
					cancelButton = new JButton();
					getContentPane().add(cancelButton);
					cancelButton.setText("cancel");
					cancelButton.setBounds(162, 241, 81, 25);
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							cancelButtonActionPerformed(evt);
						}
					});
				}
				{
					jLabel7 = new JLabel();
					getContentPane().add(jLabel7);
					jLabel7.setText("Size of a picture segment width(=height)");
					jLabel7.setBounds(29, 119, 241, 19);
				}
				{
					jLabel8 = new JLabel();
					getContentPane().add(jLabel8);
					jLabel8.setText("pixcels");
					jLabel8.setBounds(392, 119, 45, 19);
				}
				{
					ComboBoxModel pictureSegmentSizeComboxModel = 
						new DefaultComboBoxModel(
								new String[] { "8", "16","20", "32","40", "64"  });
					pictureSegmentSizeCombox = new JComboBox();
					getContentPane().add(pictureSegmentSizeCombox);
					pictureSegmentSizeCombox.setModel(pictureSegmentSizeComboxModel);
					pictureSegmentSizeCombox.setBounds(288, 116, 92, 25);
					pictureSegmentSizeCombox.setSelectedIndex(3);
				}
				{
					jLabel9 = new JLabel();
					getContentPane().add(jLabel9);
					jLabel9.setText("picture coding");
					jLabel9.setBounds(29, 144, 81, 19);
				}
				{
					ComboBoxModel pictureCodingComboBoxModel = 
						new DefaultComboBoxModel(
								new String[] { "raw", "slowFine","jpeg" });
					pictureCodingComboBox = new JComboBox();
					getContentPane().add(pictureCodingComboBox);
					pictureCodingComboBox.setModel(pictureCodingComboBoxModel);
					pictureCodingComboBox.setBounds(240, 141, 140, 25);
					pictureCodingComboBox.setSelectedIndex(1);
				}
				{
					jLabel10 = new JLabel();
					getContentPane().add(jLabel10);
					jLabel10.setText("error rate for sending a segment");
					jLabel10.setBounds(26, 175, 202, 19);
				}
				{
					ComboBoxModel errorRateComboBoxModel = 
						new DefaultComboBoxModel(
								new String[] { "0","1","2","5","7","10", "20", "30" });
					errorRateComboBox = new JComboBox();
					getContentPane().add(errorRateComboBox);
					errorRateComboBox.setModel(errorRateComboBoxModel);
					errorRateComboBox.setBounds(240, 166, 140, 25);
				}
				{
					jLabel11 = new JLabel();
					getContentPane().add(jLabel11);
					jLabel11.setText("error sampling width(=hight)");
					jLabel11.setBounds(26, 200, 170, 19);
				}
				{
					ComboBoxModel errorSamplingWidthComboBoxModel = 
						new DefaultComboBoxModel(
								new String[] { "2", "4", "8", "12" });
					errorSamplingWidthComboBox = new JComboBox();
					getContentPane().add(errorSamplingWidthComboBox);
					errorSamplingWidthComboBox.setModel(errorSamplingWidthComboBoxModel);
					errorSamplingWidthComboBox.setBounds(240, 191, 140, 25);
					errorSamplingWidthComboBox.setSelectedIndex(1);
				}
			}
			{
				this.setSize(476, 315);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	AVFrame avgui;
    public VideoSettingFrame(AVFrame g){
    	this.avgui=g;
    	this.initGUI();
    	this.loadProperties();
    	this.thisComponentShown(null);
    }
	
	//@Override
	public boolean getBoolean(String x) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public int getInt(String x) {
		// TODO Auto-generated method stub
		if(x.equals("sendInterval"))
		return this.sendInterval;
		if(x.equals("videoInterval"))
			return this.videoInterval;
		if(x.equals("pictureSegmentWidth"))
			return this.pictureSegmentWidth;
		if(x.equals("errorRate"))
			return this.errorRate;
		if(x.equals("errorSamplingWidth"))
			return this.errorSamplingWidth;
		return 0;
	}
	/*
	double updateDecreasingFactor=2.0; //ì«Ç›îÚÇŒÇµä‘äuÇíZÇ≠Ç∑ÇÈäÑçá
	double updateIncreasingFactor=2.0; //ì«Ç›îÚÇŒÇµä‘äuÇí∑Ç≠Ç∑ÇÈäÑçá
	double skiptermDecreasingFactor=2.0;//ì«Ç›îÚÇŒÇµéûä‘Çí∑Ç≠Ç∑ÇÈäÑçá
	*/
	public double getDouble(String x){
		/*
		if(x.equals("updateIncreasingFactor"))
			return this.updateIncreasingFactor;
		if(x.equals("updateDecreasingFactor"))
			return this.updateDecreasingFactor;
		if(x.equals("skiptermIncreasingFactor"))
			return this.skiptermIncreasingFactor;
			*/
		return 1.0;
	}
	public float getFloat(String x){
		if(x.equals("captureFrameRate"))
			return this.captureFrameRate;
		return 1.0f;
	}
	private void thisComponentShown(ComponentEvent evt) {
//		System.out.println("this.componentShown, event="+evt);
		//TODO add your code for this.componentShown
		this.sendIntervalField.setText(""+this.sendInterval);
		this.captureFrameRateField.setText(""+this.captureFrameRate);
		this.videoIntervalField.setText(""+this.videoInterval);
		this.repaint();
	}
	private void saveButtonActionPerformed(ActionEvent evt) {
//		System.out.println("saveButton.actionPerformed, event="+evt);
		//TODO add your code for saveButton.actionPerformed
		
		this.sendInterval=(new Integer(this.sendIntervalField.getText())).intValue();
		this.captureFrameRate=(new Float(this.captureFrameRateField.getText())).floatValue();
		this.videoInterval=(new Integer(this.videoIntervalField.getText())).intValue();
		int pictureSegmentWidth=(new Integer((String)(this.pictureSegmentSizeCombox.getSelectedItem()))).intValue();
		String pictureCoding=(String)(this.pictureCodingComboBox.getSelectedItem());
		int errorRate=(new Integer((String)(this.errorRateComboBox.getSelectedItem()))).intValue();
		int errorSamplingWidth=(new Integer((String)(this.errorSamplingWidthComboBox.getSelectedItem()))).intValue();
		
		this.saveProperties();
		this.setVisible(false);
	}
	public void saveProperties(){
		if(this.setting==null){
			setting=new Properties();
		} 
        this.setting.setProperty("sendInterval",this.sendIntervalField.getText());
        this.setting.setProperty("captureFrameRate",this.captureFrameRateField.getText());
        this.setting.setProperty("videoInterval",this.videoIntervalField.getText());
		this.setting.setProperty("pictureSegmentWidth", (String)(this.pictureSegmentSizeCombox.getSelectedItem()));
		this.setting.setProperty("pictureCoding", (String)(this.pictureCodingComboBox.getSelectedItem()));
		this.setting.setProperty("errorRate", (String)(this.errorRateComboBox.getSelectedItem()));
		this.setting.setProperty("errorSamplingWidth", (String)(this.errorSamplingWidthComboBox.getSelectedItem()));
	       try {
	           FileOutputStream saveS = new FileOutputStream(this.fileName);
	           setting.store(saveS,"--- video settings ---");

	        } catch( Exception e){
	           System.err.println(e);
	        } 
	}
	public void loadProperties(){
	       try {
	           setting = new Properties() ;
	           FileInputStream appS = new FileInputStream( fileName);
	           setting.load(appS);

	        } catch( Exception e){
//	           System.err.println(e);
	        	this.saveProperties();
	        	return;
	        } 
	        try{
	        	String p1=this.setting.getProperty("sendInterval");
	        	int x=(new Integer(p1)).intValue();
	            this.sendInterval=x;
	            this.sendIntervalField.setText(p1);
	            p1=this.setting.getProperty("captureFrameRate");
	            this.captureFrameRateField.setText(p1);
	            float y=(new Float(p1)).floatValue();
    	        this.captureFrameRate=y;
    	        p1=this.setting.getProperty("videoInterval");
    	        this.videoIntervalField.setText(p1);
	        	x=(new Integer(p1)).intValue();
	            this.videoInterval=x;
	            
	            p1=this.setting.getProperty("pictureSegmentWidth");
	            this.pictureSegmentSizeCombox.setSelectedItem(p1);
	            x=(new Integer(p1)).intValue();
	            this.pictureSegmentWidth=x;
	            p1=this.setting.getProperty("pictureCoding");
	            this.pictureCodingComboBox.setSelectedItem(p1);
	            this.pictureCoding=p1;
	            p1=this.setting.getProperty("errorRate");
	            this.errorRateComboBox.setSelectedItem(p1);
	            this.errorRate=(new Integer(p1)).intValue();
	            p1=this.setting.getProperty("errorSamplingWidth");
	            this.errorSamplingWidthComboBox.setSelectedItem(p1);
	            this.errorSamplingWidth=(new Integer(p1)).intValue();
	        }
	        catch(Exception e){
	        	e.printStackTrace();
	        }
	        this.thisComponentShown(null);
	}

//	@Override
	public String getString(String x) {
		// TODO Auto-generated method stub
		if(x.equals("pictureCoding")){
			return this.pictureCoding;
		}
		return null;
	}

	private void cancelButtonActionPerformed(ActionEvent evt) {
//		System.out.println("cancelButton.actionPerformed, event="+evt);
		//TODO add your code for cancelButton.actionPerformed
		this.setVisible(false);
	}
}
