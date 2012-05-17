package application.av;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.JLabel;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import util.Parameters;

public class AudioSkipParametersFrame extends JPanel
implements Parameters
{
	private JLabel minimumIntervalLabel;
	private JLabel initialIntervalLabel;
	private JLabel initialSkiptermLabel;
	private JLabel updDecFactLabel;
	private JLabel updIncFactLabel;
	private JLabel skipIncFactLabel;
	private JLabel gapLabel;
	private JTextField minimumIntervalField;
	private JTextField initialIntervalField;
	private JTextField initialSkiptermField;
	private JTextField updDecFactField;
	private JTextField updIncFactField;
	private JTextField skipIncFactField;
	private JTextField gapField;
	private JLabel jLabel1;
	private JButton cancelButton;
	private JButton saveButton;
	
	int minimumInterval=1000; // ì«Ç›îÚÇŒÇµä‘äuÇÃç≈è¨íl(msec)
	int initialInterval=10000; //ì«Ç›îÚÇŒÇµä‘äuÇÃèâä˙íl
	double updateDecreasingFactor=2.0; //ì«Ç›îÚÇŒÇµä‘äuÇíZÇ≠Ç∑ÇÈäÑçá
	double updateIncreasingFactor=2.0; //ì«Ç›îÚÇŒÇµä‘äuÇí∑Ç≠Ç∑ÇÈäÑçá
	double skiptermIncreasingFactor=2.0;//ì«Ç›îÚÇŒÇµéûä‘Çí∑Ç≠Ç∑ÇÈäÑçá
	int initialSkipterm=30; //ì«Ç›îÚÇŒÇµéûä‘ÇÃèâä˙íl
	int gapBetweenDecreaseToIncrease=10; //ì«Ç›îÚÇŒÇµä‘äuÇí∑Ç≠ÇµÇƒÇ¢ÇÈÇ∆Ç´Ç©ÇÁ
	                                     //íZÇ≠Ç∑ÇÈÇ∆Ç´ÇÃÅAíxâÑÇÃÉMÉÉÉbÉv
	
	Properties setting;
	String fileName="audio-skip-setting.properties";

//	AVGui avgui;
    public AudioSkipParametersFrame(){
//    	this.avgui=g;
    	this.initGUI();
    }
	
	//@Override
	public boolean getBoolean(String x) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public int getInt(String x) {
		// TODO Auto-generated method stub
		if(x.equals("minimumInterval"))
		return this.minimumInterval;
		if(x.equals("initialInterval"))
			return this.initialInterval;
		if(x.equals("initialSkipterm"))
			return this.initialSkipterm;
		return 0;
	}
	/*
	double updateDecreasingFactor=2.0; //ì«Ç›îÚÇŒÇµä‘äuÇíZÇ≠Ç∑ÇÈäÑçá
	double updateIncreasingFactor=2.0; //ì«Ç›îÚÇŒÇµä‘äuÇí∑Ç≠Ç∑ÇÈäÑçá
	double skiptermDecreasingFactor=2.0;//ì«Ç›îÚÇŒÇµéûä‘Çí∑Ç≠Ç∑ÇÈäÑçá
	*/
	public double getDouble(String x){
		if(x.equals("updateIncreasingFactor"))
			return this.updateIncreasingFactor;
		if(x.equals("updateDecreasingFactor"))
			return this.updateDecreasingFactor;
		if(x.equals("skiptermIncreasingFactor"))
			return this.skiptermIncreasingFactor;
		return 1.0;
	}
	private void initGUI() {
		try {
			{
				this.setPreferredSize(new java.awt.Dimension(320, 249));
				this.setLayout(null);
				this.addComponentListener(new ComponentAdapter() {
					public void componentShown(ComponentEvent evt) {
						thisComponentShown(evt);
					}
				});
				{
					jLabel1 = new JLabel();
					this.add(jLabel1);
					jLabel1.setText("Audio Skip Setting");
					jLabel1.setBounds(71, 7, 107, 19);
				}
				{
					minimumIntervalLabel = new JLabel();
					this.add(minimumIntervalLabel);
					minimumIntervalLabel.setText("minimum Interval");
					minimumIntervalLabel.setBounds(12, 43, 93, 19);
				}
				{
					initialIntervalLabel = new JLabel();
					this.add(initialIntervalLabel);
					initialIntervalLabel.setText("initial Interval");
					initialIntervalLabel.setBounds(12, 65, 93, 19);
				}
				{
					initialSkiptermLabel = new JLabel();
					this.add(initialSkiptermLabel);
					initialSkiptermLabel.setText("init. skip");
					initialSkiptermLabel.setBounds(12, 93, 93, 19);
				}
				{
					minimumIntervalField = new JTextField();
					this.add(minimumIntervalField);
					minimumIntervalField.setBounds(120, 40, 155, 25);
				}
				{
					initialIntervalField = new JTextField();
					this.add(initialIntervalField);
					initialIntervalField.setBounds(120, 65, 155, 25);
				}
				{
					initialSkiptermField = new JTextField();
					this.add(initialSkiptermField);
					initialSkiptermField.setBounds(120, 90, 155, 25);
				}
				{
					saveButton = new JButton();
					this.add(saveButton);
					saveButton.setText("save");
					saveButton.setBounds(78, 224, 78, 25);
					saveButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							saveButtonActionPerformed(evt);
						}
					});
				}
				{
					cancelButton = new JButton();
					this.add(cancelButton);
					cancelButton.setText("cancel");
					cancelButton.setBounds(156, 224, 90, 25);
				}
				{
					updDecFactLabel = new JLabel();
					this.add(updDecFactLabel);
					updDecFactLabel.setText("intrvl dec. fact.");
					updDecFactLabel.setBounds(18, 118, 90, 19);
				}
				{
					updIncFactLabel = new JLabel();
					this.add(updIncFactLabel);
					updIncFactLabel.setText("intrvl inc. fact.");
					updIncFactLabel.setBounds(18, 143, 90, 19);
				}
				{
					skipIncFactLabel = new JLabel();
					this.add(skipIncFactLabel);
					skipIncFactLabel.setText("skip inc. fact.");
					skipIncFactLabel.setBounds(18, 167, 90, 19);
				}
				{
					gapLabel = new JLabel();
					this.add(gapLabel);
					gapLabel.setText("gap");
					gapLabel.setBounds(18,190, 90,19);
				}
				{
					updDecFactField = new JTextField();
					this.add(updDecFactField);
					updDecFactField.setText("");
					updDecFactField.setBounds(120, 115, 155, 25);
				}
				{
					updIncFactField = new JTextField();
					this.add(updIncFactField);
					updIncFactField.setText("");
					updIncFactField.setBounds(120, 140, 155, 25);
				}
				{
					skipIncFactField= new JTextField();
					this.add(skipIncFactField);
					skipIncFactField.setText("");
					skipIncFactField.setBounds(120, 164, 155, 25);
				}
				{
					gapField=new JTextField();
					this.add(gapField);
					gapField.setText("");
					gapField.setBounds(120, 188, 155, 25);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.loadProperties();
		this.thisComponentShown(null);
	}
	
	private void thisComponentShown(ComponentEvent evt) {
//		System.out.println("this.componentShown, event="+evt);
		//TODO add your code for this.componentShown
		this.minimumIntervalField.setText(""+this.minimumInterval);
		this.initialIntervalField.setText(""+this.initialInterval);
		this.initialSkiptermField.setText(""+this.initialSkipterm);
		this.gapField.setText(""+this.gapBetweenDecreaseToIncrease);
		this.updDecFactField.setText(""+this.updateDecreasingFactor);
		this.updIncFactField.setText(""+this.updateIncreasingFactor);
		this.skipIncFactField.setText(""+this.skiptermIncreasingFactor);
		this.repaint();
	}
	private void saveButtonActionPerformed(ActionEvent evt) {
//		System.out.println("saveButton.actionPerformed, event="+evt);
		//TODO add your code for saveButton.actionPerformed
		
		this.minimumInterval=(new Integer(this.minimumIntervalField.getText())).intValue();
		this.initialInterval=(new Integer(this.initialIntervalField.getText())).intValue();
		this.initialSkipterm=(new Integer(this.initialSkiptermField.getText())).intValue();
		this.gapBetweenDecreaseToIncrease=(new Integer(this.gapField.getText())).intValue();
		this.updateDecreasingFactor=(new Double(this.updDecFactField.getText())).doubleValue();
		this.updateIncreasingFactor=(new Double(this.updIncFactField.getText())).doubleValue();
		this.skiptermIncreasingFactor=(new Double(this.skipIncFactField.getText())).doubleValue();
		
		this.saveProperties();
	}
	public void saveProperties(){
		if(this.setting==null){
			setting=new Properties();
		}
        this.setting.setProperty("minimumInterval",this.minimumIntervalField.getText());
        this.setting.setProperty("initialInterval",this.initialIntervalField.getText());
        this.setting.setProperty("initialSkipterm",this.initialSkiptermField.getText());
		this.setting.setProperty("gap", this.gapField.getText());
		this.setting.setProperty("updDecFact", this.updDecFactField.getText());
		this.setting.setProperty("updIncFact",this.updIncFactField.getText());
		this.setting.setProperty("skipIncFact", this.skipIncFactField.getText());
	       try {
	           FileOutputStream saveS = new FileOutputStream(this.fileName);
	           setting.store(saveS,"--- audio skip settings ---");

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
	        	String p1=this.setting.getProperty("minimumInterval");
	        	int x=(new Integer(p1)).intValue();
	            this.minimumInterval=x;
	            p1=this.setting.getProperty("initialInterval");
	            x=(new Integer(p1)).intValue();
    	        this.initialInterval=x;
    	        p1=this.setting.getProperty("initialSkipterm");
	        	x=(new Integer(p1)).intValue();
	            this.initialSkipterm=x;
	            p1=this.setting.getProperty("gap");
	            x=(new Integer(p1)).intValue();
	            this.gapBetweenDecreaseToIncrease=x;
	            p1=this.setting.getProperty("updDecFact");
	            double d=(new Double(p1)).doubleValue();
	            this.updateDecreasingFactor=d;
	            d=(new Double(this.setting.getProperty("updIncFact"))).doubleValue();
	            this.updateIncreasingFactor=d;
	            d=(new Double(this.setting.getProperty("skipIncFact"))).doubleValue();
	            this.skiptermIncreasingFactor=d;
	        }
	        catch(Exception e){
	        	e.printStackTrace();
	        }
	        this.thisComponentShown(null);
	}

//	@Override
	public String getString(String x) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
	public float getFloat(String x) {
		// TODO Auto-generated method stub
		return 0;
	}


}
