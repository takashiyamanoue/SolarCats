package nodesystem;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

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
public class AutoConnectionFrame extends JFrame
implements Runnable
{
	private Thread me;
	private JTextField currentTimeField;
	private JLabel currentTimeLabel;
	private long systemTime;
	private JLabel clickAtLabel;
	private JCheckBox enablingGetConnectedTimeCheckBox;
	private JCheckBox enablingAutoClickCheckBox;
	private JButton clearButton;
	private JTextField clickTimeField;
	private JButton closeButton;
	private long systemTimeInSecond;
	private JLabel connectedTimeLabel;
	private JTextField connectionTermField;
	private JLabel connectionTermLabel;
	private JTextField connectedTimeField;
	private CommunicationNode node;
	private InterNodeController controller;

	public AutoConnectionFrame(CommunicationNode n){
		node=n;
		this.initGUI();
		systemTime=System.currentTimeMillis();
		systemTimeInSecond=systemTime/1000;
		this.currentTimeField.setText(""+systemTimeInSecond);
		this.clearButtonActionPerformed(null);
		this.controller=node.nodeController;
		this.start();
	}
	private void initGUI() {
		try {
			{
				getContentPane().setLayout(null);
			}
			{
				currentTimeField = new JTextField();
				getContentPane().add(currentTimeField, "Center");
				currentTimeField.setBounds(172, 26, 132, 23);
			}
			{
				currentTimeLabel = new JLabel();
				getContentPane().add(currentTimeLabel);
				currentTimeLabel.setText("current system time");
				currentTimeLabel.setBounds(26, 29, 144, 17);
			}
			{
				closeButton = new JButton();
				getContentPane().add(closeButton);
				closeButton.setText("close");
				closeButton.setBounds(337, 0, 68, 21);
				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						closeButtonActionPerformed(evt);
					}
				});
			}
			{
				clickAtLabel = new JLabel();
				getContentPane().add(clickAtLabel);
				clickAtLabel.setText("click join button when:");
				clickAtLabel.setBounds(19, 82, 152, 19);
			}
			{
				clickTimeField = new JTextField();
				getContentPane().add(clickTimeField);
				clickTimeField.setBounds(172, 81, 132, 22);
			}
			{
				clearButton = new JButton();
				getContentPane().add(clearButton);
				clearButton.setText("clear");
				clearButton.setBounds(304, 80, 79, 24);
				clearButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						clearButtonActionPerformed(evt);
					}
				});
			}
			{
				enablingAutoClickCheckBox = new JCheckBox();
				getContentPane().add(enablingAutoClickCheckBox);
				enablingAutoClickCheckBox.setText("enabling auto click");
				enablingAutoClickCheckBox.setBounds(0, 56, 156, 23);
			}
			{
				enablingGetConnectedTimeCheckBox = new JCheckBox();
				getContentPane().add(enablingGetConnectedTimeCheckBox);
				enablingGetConnectedTimeCheckBox.setText("enabling get connected time.");
				enablingGetConnectedTimeCheckBox.setBounds(0, 113, 214, 23);
			}
			{
				connectedTimeLabel = new JLabel();
				getContentPane().add(connectedTimeLabel);
				connectedTimeLabel.setText("connected time:");
				connectedTimeLabel.setBounds(19, 139, 151, 19);
			}
			{
				connectedTimeField = new JTextField();
				getContentPane().add(connectedTimeField);
				connectedTimeField.setBounds(172, 136, 132, 22);
			}
			{
				connectionTermLabel = new JLabel();
				getContentPane().add(connectionTermLabel);
				connectionTermLabel.setText("connection term:");
				connectionTermLabel.setBounds(19, 158, 127, 19);
			}
			{
				connectionTermField = new JTextField();
				getContentPane().add(connectionTermField);
				connectionTermField.setBounds(172, 158, 132, 20);
			}
			{
				this.setSize(421, 292);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void start(){
		if(me==null){
			me=new Thread(this,"autoConnectionFrame");
			me.start();
		}
	}
	public void stop(){
		if(me!=null){
			me=null;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(me!=null){
			systemTime=System.currentTimeMillis();
			systemTimeInSecond=systemTime/1000;
			this.currentTimeField.setText(""+systemTimeInSecond);
			if(this.enablingAutoClickCheckBox.isSelected()){
				String t2s=this.clickTimeField.getText();
				long t2=(new Long(t2s)).longValue();
				if(this.node!=null){
					if(t2<=systemTimeInSecond){
						this.node.joinButton_actionPerformed(null);
						this.enablingAutoClickCheckBox.setSelected(false);
						this.enablingGetConnectedTimeCheckBox.setSelected(true);
					}
				}
			}
			if(this.enablingGetConnectedTimeCheckBox.isSelected()){
				if(this.controller.isConnectedToUpperNode()){
					systemTime=System.currentTimeMillis();
					systemTimeInSecond=systemTime/1000;
					this.currentTimeField.setText(""+systemTimeInSecond);
					this.connectedTimeField.setText(""+systemTimeInSecond);
					String t2s=this.clickTimeField.getText();
					long t2=(new Long(t2s)).longValue();
					long tx=systemTimeInSecond-t2;
					this.connectionTermField.setText(""+tx);
					this.enablingGetConnectedTimeCheckBox.setSelected(false);
				}
			}
			try{
				Thread.sleep(100);
			}
			catch(InterruptedException e){}
		}
		
	}
	
	private void closeButtonActionPerformed(ActionEvent evt) {
		System.out.println("closeButton.actionPerformed, event="+evt);
		//TODO add your code for closeButton.actionPerformed
		this.setVisible(false);
	}
	
	private void clearButtonActionPerformed(ActionEvent evt) {
		System.out.println("clearButton.actionPerformed, event="+evt);
		//TODO add your code for clearButton.actionPerformed
		String t=this.currentTimeField.getText();
		try{
		    long ti=(new Long(t)).longValue();
    		this.clickTimeField.setText(""+(ti+3600));
    		this.connectedTimeField.setText("");
    		this.enablingGetConnectedTimeCheckBox.setSelected(false);
		}
		catch(Exception e){
			this.clickTimeField.setText(""+0);
		}
	}

}
