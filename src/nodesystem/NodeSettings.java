package nodesystem;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import util.Parameters;
import util.TraceSW;


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
public class NodeSettings extends javax.swing.JFrame
implements TraceSW, QA, Parameters
{
    CommunicationNode communicationNode;

    public JRadioButton getSelectedButton()
    {
        if(this.radioButtonP2P.isSelected())
          return this.radioButtonP2P;
        if(this.radioButtonCSB.isSelected()) 
          return this.radioButtonCSB;
        if(this.radioButtonCS.isSelected()) 
          return this.radioButtonCS;
        if(this.radioButtonMcast.isSelected())
          return this.radioButtonMcast;
        if(this.radioButtonP2Pa.isSelected())
            return this.radioButtonP2Pa;
        return null;
    }

    public ButtonGroup radioButtonGroup;

	public NodeSettings(CommunicationNode cn)
	{
		setTitle("setting");
		initGUI();
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		radioButtonP2P.addActionListener(lSymAction);
		radioButtonCS.addActionListener(lSymAction);
		radioButtonMcast.addActionListener(lSymAction);
		exitButton.addActionListener(lSymAction);
		SymItem lSymItem = new SymItem();
		radioButtonCSB.addItemListener(lSymItem);
		radioButtonCSB.addActionListener(lSymAction);

        this.radioButtonGroup=new ButtonGroup();
        this.radioButtonGroup.add(radioButtonP2P);
        this.radioButtonGroup.add(radioButtonCSB);
        this.radioButtonGroup.add(radioButtonCS);
        this.radioButtonGroup.add(radioButtonMcast);
        this.radioButtonGroup.add(radioButtonP2Pa);
        this.communicationNode=cn;
        this.loadProperties();
	}

	private void initGUI(){
		{
			waitToHeightLabel = new JLabel();
			getContentPane().add(waitToHeightLabel);
			waitToHeightLabel.setText("wait to height:");
			waitToHeightLabel.setBounds(94, 289, 93, 19);
		}
		{
			requestRepTermLabel = new JLabel();
			getContentPane().add(requestRepTermLabel);
			requestRepTermLabel.setText("request reputation term:");
			requestRepTermLabel.setBounds(275, 289, 146, 19);
		}
		   getContentPane().setLayout(null);
		   this.setSize(550, 716);
		   setVisible(false);
		{
			JLabel1.setText("Node connection settings");
		    getContentPane().add(JLabel1);
			JLabel1.setBounds(21, 24, 189, 27);
		}
		{
    		JLabel2.setText("group manager addr,port");
	    	getContentPane().add(JLabel2);
		    JLabel2.setBounds(110, 96, 162, 24);
		}
		{
			getContentPane().add(groupMngrAddressField);
		    groupMngrAddressField.setBounds(275, 98, 192, 22);
		}
		{
    		getContentPane().add(groupMngrPortField);
	    	groupMngrPortField.setBounds(467, 98, 59, 23);
		}
		{
		    JLabel4.setText("reflector addr, port (up/down link)");
		    getContentPane().add(JLabel4);
		    JLabel4.setBounds(71, 49, 304, 26);
		}
		{
		    getContentPane().add(reflectorAddressField);
		    reflectorAddressField.setBounds(276, 190, 192, 24);
		}
		{
		    getContentPane().add(reflectorPortField);
		    reflectorPortField.setBounds(468, 190, 60, 24);
		}
		{
    		JLabel5.setText("multicast addr, port(down link)");
	    	getContentPane().add(JLabel5);
	    	JLabel5.setBounds(71, 212, 205, 27);
		}
		{
		    multicastAddressField.setText("224.0.1.1");
		    getContentPane().add(multicastAddressField);
		    multicastAddressField.setBounds(276, 212, 191, 24);
		}
			
		{
		    multicastPortField.setText("17320");
		    getContentPane().add(multicastPortField);
		    multicastPortField.setBounds(467, 211, 60, 27);
		}
		{
		    radioButtonP2P.setText("P2P");
		    radioButtonP2P.setActionCommand("p2p using the group manager");
		    getContentPane().add(radioButtonP2P);
		    radioButtonP2P.setBounds(71, 68, 50, 24);
		    radioButtonP2P.setSelected(true);

		}
		{
		    radioButtonP2Pa.setText("P2Pa");
		    radioButtonP2Pa.setActionCommand("p2p using autonomous grouping");
		    getContentPane().add(radioButtonP2Pa);
		    radioButtonP2Pa.setBounds(72, 239, 67, 25);
		    radioButtonP2Pa.setSelected(true);

		}
		{
		radioButtonCS.setText("CS");
		radioButtonCS.setActionCommand("client-server using the reflector");
		getContentPane().add(radioButtonCS);
		radioButtonCS.setBounds(71, 146, 48, 24);
		}
		{
		radioButtonMcast.setText("MCAST");
		radioButtonMcast.setActionCommand("multicast using the multicast reflector");
		getContentPane().add(radioButtonMcast);
		radioButtonMcast.setBounds(71, 169, 60, 26);
		}
		{
		exitButton.setText("exit");
		exitButton.setActionCommand("exit");
		getContentPane().add(exitButton);
		exitButton.setBounds(364, 4, 119, 20);
		}
		{
		JLabel6.setText("peer to peer using the group manager");
		getContentPane().add(JLabel6);
		JLabel6.setBounds(125, 69, 301, 25);
		}
		{
		JLabel7.setText("TCP client-server, echo back, using the reflector");
		getContentPane().add(JLabel7);
		JLabel7.setBounds(152, 146, 314, 24);
		}
		{
		JLabel8.setText("Multicast client-server, no echo back, using the reflector.");
		getContentPane().add(JLabel8);
		JLabel8.setBounds(152, 166, 376, 24);
		}
		{
		radioButtonCSB.setText("CSB");
		radioButtonCSB.setActionCommand("CSB");
		getContentPane().add(radioButtonCSB);
		radioButtonCSB.setBounds(71, 126, 60, 22);
		}
		{
		JLabel9.setText("TCP client-server, no echo back, Balking pattern, using the reflector");
		getContentPane().add(JLabel9);
		JLabel9.setBounds(152, 126, 374, 24);
		}
		{
		JLabel10.setText("Element Width when transmitting an imange");
		getContentPane().add(JLabel10);
		JLabel10.setBounds(56, 428, 273, 18);
		}
		{
		elementWidthField.setText("128");
		getContentPane().add(elementWidthField);
		elementWidthField.setBounds(356, 430, 42, 18);
		}
		{
		    fileTransferSettingLabel = new JLabel();
			getContentPane().add(fileTransferSettingLabel);
			fileTransferSettingLabel.setBounds(23, 536, 210, 27);
			fileTransferSettingLabel.setText("File Trasfer Settings");
		}
		{
			useEquationButton = new JRadioButton();
			useEquationButton.setText("use the Equation");
			useEquationButton.setBounds(54, 564, 133, 22);
			useEquationButton.setSelected(true);
			getContentPane().add(useEquationButton);
		}
		{
			useSettingButton = new JRadioButton();
			useSettingButton.setText("use Setting");
			useSettingButton.setBounds(54, 590, 113, 29);
			getContentPane().add(useSettingButton);
		}
		{
			partitioningButtonGroup = new ButtonGroup();
			partitioningButtonGroup.add(this.useEquationButton);
			partitioningButtonGroup.add(this.useSettingButton);
		}
		{
			maxBufferSizeLabel.setText("maxBufferSize:");
			getContentPane().add(maxBufferSizeLabel);
			maxBufferSizeLabel.setBounds(261, 586, 113, 31);
		}
		{
			maxBufferSizeField = new JTextField();
			getContentPane().add(maxBufferSizeField);
			maxBufferSizeField.setBounds(370, 587, 75, 30);
			maxBufferSizeField.setText("10000000");
		}
		{
			partitioningNumberField = new JTextField();
			getContentPane().add(partitioningNumberField);
			partitioningNumberField.setBounds(167, 590, 71, 30);
			partitioningNumberField.setText("10");
		}
		{
			traceCheckBox = new JCheckBox();
			getContentPane().add(traceCheckBox);
			traceCheckBox.setText("trace");
			traceCheckBox.setBounds(20, 622, 66, 27);
		}
		{
			saveButton = new JButton();
			getContentPane().add(saveButton);
			saveButton.setText("save");
			saveButton.setBounds(275, 3, 89, 21);
			saveButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					saveButtonActionPerformed(evt);
				}
			});
		}
		{
			traceMessagesCheckBox = new JCheckBox();
			getContentPane().add(traceMessagesCheckBox);
			traceMessagesCheckBox.setText("trace messages");
			traceMessagesCheckBox.setBounds(54, 647, 126, 28);
			traceMessagesCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					traceMessagesCheckButtonActionPerformed(evt);
				}
			});
		}
		{
			diffPixLabel = new JLabel();
			getContentPane().add(diffPixLabel);
			diffPixLabel.setText("Diff pixel sampling rate");
			diffPixLabel.setBounds(57, 455, 163, 19);
		}
		
		{
			jLabel1 = new JLabel();
			getContentPane().add(jLabel1);
			jLabel1.setText("Image Transfer Coding");
			jLabel1.setBounds(56, 404, 134, 20);
		}
		{
			ComboBoxModel codingComboBoxModel = 
				new DefaultComboBoxModel(
						new String[] { "raw", "slowFine","jpeg" });
			codingComboBox = new JComboBox();
			getContentPane().add(codingComboBox);
			codingComboBox.setModel(codingComboBoxModel);
			codingComboBox.setBounds(239, 404, 137, 19);
			codingComboBox.setSelectedIndex(1);
		}
		{
			jLabel2 = new JLabel();
			getContentPane().add(jLabel2);
			jLabel2.setText("Diff Pixcel Error Rate");
			jLabel2.setBounds(55, 480, 164, 21);
		}
		{
			ComboBoxModel diffPixErrorComboBoxModel = 
				new DefaultComboBoxModel(
						new String[] { "0","1", "2","5","10","15","20" });
			diffPixErrorComboBox = new JComboBox();
			getContentPane().add(diffPixErrorComboBox);
			diffPixErrorComboBox.setModel(diffPixErrorComboBoxModel);
			diffPixErrorComboBox.setBounds(239, 479, 136, 25);
			diffPixErrorComboBox.setSelectedIndex(1);
		}
		{
			ComboBoxModel diffPixSampleComboBoxModel = 
				new DefaultComboBoxModel(
						new String[] { "1", "2","3","4","5","6","7","8","16" });
			diffPixSampleComboBox = new JComboBox();
			getContentPane().add(diffPixSampleComboBox);
			diffPixSampleComboBox.setModel(diffPixSampleComboBoxModel);
			diffPixSampleComboBox.setBounds(239, 454, 135, 25);
			diffPixSampleComboBox.setSelectedIndex(3);
		}
		{
			jLabel3 = new JLabel();
			getContentPane().add(jLabel3);
			jLabel3.setText("Image Transfer Setting");
			jLabel3.setBounds(28, 382, 152, 18);
		}
		{
			jpegCompressRateLabel = new JLabel();
			getContentPane().add(jpegCompressRateLabel);
			jpegCompressRateLabel.setText("jpeg compression rate");
			jpegCompressRateLabel.setBounds(54, 506, 168, 18);
		}
		{
			ComboBoxModel jpegCompressComboBoxModel = 
				new DefaultComboBoxModel(
						new String[] { "0.9", "0.8","0.7", "0.6", 
								       "0.5", "0.4", "0.3", "0.2"});
			jpegCompressComboBox = new JComboBox();
			getContentPane().add(jpegCompressComboBox);
			jpegCompressComboBox.setModel(jpegCompressComboBoxModel);
			jpegCompressComboBox.setBounds(239, 503, 136, 23);
		}
		{
			p2pALabel = new JLabel();
			getContentPane().add(p2pALabel);
			p2pALabel.setText("P2P using autonomous grouping");
			p2pALabel.setBounds(156, 242, 185, 19);
		}
		{
			groupMcastPortField = new JTextField();
			getContentPane().add(groupMcastPortField);
			groupMcastPortField.setBounds(467, 262, 60, 21);
			groupMcastPortField.setText("10007");
		}
		{
			groupMcastAddrField = new JTextField();
			getContentPane().add(groupMcastAddrField);
			groupMcastAddrField.setText("224.0.0.7");
			groupMcastAddrField.setBounds(393, 262, 73, 21);
		}
		{
			mcast2Label = new JLabel();
			getContentPane().add(mcast2Label);
			mcast2Label.setText("mcast Addr/Port ");
			mcast2Label.setBounds(275, 263, 116, 21);
		}
		{
			groupNameField = new JTextField();
			getContentPane().add(groupNameField);
			groupNameField.setText("g1");
			groupNameField.setBounds(192, 264, 74, 19);
		}
		{
			groupNameLabel = new JLabel();
			getContentPane().add(groupNameLabel);
			groupNameLabel.setText("group name");
			groupNameLabel.setBounds(98, 264, 92, 19);
		}
		{
			requestServerRepTermLabel = new JLabel();
			getContentPane().add(requestServerRepTermLabel);
			requestServerRepTermLabel.setText("server reputation term:");
			requestServerRepTermLabel.setBounds(94, 313, 152, 19);
		}
		{
			ComboBoxModel waitToHeightComboBoxModel = 
				new DefaultComboBoxModel(
						new String[] { "10", "20", "50","100","200","500","1000","2000","5000" });
			waitToHeightComboBox = new JComboBox();
			getContentPane().add(waitToHeightComboBox);
			waitToHeightComboBox.setModel(waitToHeightComboBoxModel);
			waitToHeightComboBox.setBounds(199, 288, 67, 21);
			waitToHeightComboBox.setFont(new java.awt.Font("メイリオ",0,10));
			waitToHeightComboBox.setSelectedItem("1000");
		}
		{
			ComboBoxModel requestRepTermComboBoxModel = 
				new DefaultComboBoxModel(
						new String[] {"10", "20", "50","100","200","500","1000","2000","5000"  });
			requestRepTermComboBox = new JComboBox();
			getContentPane().add(requestRepTermComboBox);
			requestRepTermComboBox.setModel(requestRepTermComboBoxModel);
			requestRepTermComboBox.setBounds(433, 288, 76, 20);
			requestRepTermComboBox.setFont(new java.awt.Font("メイリオ",0,10));
			requestRepTermComboBox.setSelectedItem("200");
		}
		{
			ComboBoxModel serverRepTermComboBoxModel = 
				new DefaultComboBoxModel(
						new String[] { "10", "20", "50","100","200","500","1000","2000","5000" });
			serverRepTermComboBox = new JComboBox();
			getContentPane().add(serverRepTermComboBox);
			serverRepTermComboBox.setModel(serverRepTermComboBoxModel);
			serverRepTermComboBox.setBounds(244, 312, 70, 23);
			serverRepTermComboBox.setFont(new java.awt.Font("メイリオ",0,10));
			serverRepTermComboBox.setSelectedItem("200");
		}
		{
			waitTimeAfterConnectedLabel = new JLabel();
			getContentPane().add(waitTimeAfterConnectedLabel);
			waitTimeAfterConnectedLabel.setText("wait after connected:");
			waitTimeAfterConnectedLabel.setBounds(326, 313, 129, 19);
		}
		{
			ComboBoxModel waitAfterConComboBoxModel = 
				new DefaultComboBoxModel(
						new String[] {"10", "20", "50","100","200","500","1000","2000","5000" });
			waitAfterConComboBox = new JComboBox();
			getContentPane().add(waitAfterConComboBox);
			waitAfterConComboBox.setModel(waitAfterConComboBoxModel);
			waitAfterConComboBox.setBounds(460, 312, 62, 20);
			waitAfterConComboBox.setFont(new java.awt.Font("メイリオ",0,10));
			serverRepTermComboBox.setSelectedItem("500");
		}
		{
			waitingNodePortLabel = new JLabel();
			getContentPane().add(waitingNodePortLabel);
			waitingNodePortLabel.setText("waiting node port:");
			waitingNodePortLabel.setBounds(94, 338, 116, 19);
		}
		{
			waitingNodePortField = new JTextField();
			getContentPane().add(waitingNodePortField);
			waitingNodePortField.setBounds(214, 338, 67, 19);
		}
		{
			ackReceivePortLabel = new JLabel();
			getContentPane().add(ackReceivePortLabel);
			ackReceivePortLabel.setText("ack receive port:");
			ackReceivePortLabel.setBounds(304, 338, 117, 19);
		}
		{
			ackReceivePortField = new JTextField();
			getContentPane().add(ackReceivePortField);
			ackReceivePortField.setBounds(414, 335, 106, 22);
		}
		{
			parallelLabel = new JLabel();
			getContentPane().add(parallelLabel);
			parallelLabel.setText("parallel processing: ");
			parallelLabel.setBounds(94, 358, 126, 19);
		}
		{
			ComboBoxModel parallelProcessiongComboBoxModel = 
				new DefaultComboBoxModel(
						new String[] { "true", "false" });
			parallelProcessingComboBox = new JComboBox();
			getContentPane().add(parallelProcessingComboBox);
			parallelProcessingComboBox.setModel(parallelProcessiongComboBoxModel);
			parallelProcessingComboBox.setBounds(220, 357, 61, 20);
		}
		{
			autoConnectionFrameOpenButton = new JButton();
			getContentPane().add(autoConnectionFrameOpenButton);
			autoConnectionFrameOpenButton.setText("autoConnection");
			autoConnectionFrameOpenButton.setBounds(304, 358, 129, 19);
			autoConnectionFrameOpenButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					autoConnectionFrameOpenButtonActionPerformed(evt);
				}
			});
		}

	}
	
	public NodeSettings(String title)
	{
		this((CommunicationNode)null);
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
		for (int i = 0; i < components.length; i++){
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
		if (object == NodeSettings.this)
			NodeSettings_WindowClosing(event);
		}
	}

	void NodeSettings_WindowClosing(java.awt.event.WindowEvent event)
	{
		dispose();		 // dispose of the Frame.
	}
	
	public javax.swing.JTextField groupMngrAddressField = new javax.swing.JTextField();
	private javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	private javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	public javax.swing.JTextField groupMngrPortField = new javax.swing.JTextField();
	private javax.swing.JLabel JLabel4 = new javax.swing.JLabel();
	public javax.swing.JTextField reflectorAddressField = new javax.swing.JTextField();
	public javax.swing.JTextField reflectorPortField = new javax.swing.JTextField();
	private javax.swing.JLabel JLabel5 = new javax.swing.JLabel();
	public  javax.swing.JTextField multicastAddressField = new javax.swing.JTextField();
	public javax.swing.JTextField multicastPortField = new javax.swing.JTextField();
	private javax.swing.JRadioButton radioButtonP2P = new javax.swing.JRadioButton();
	private javax.swing.JRadioButton radioButtonP2Pa = new javax.swing.JRadioButton();
	private javax.swing.JRadioButton radioButtonCS = new javax.swing.JRadioButton();
	private javax.swing.JRadioButton radioButtonMcast = new javax.swing.JRadioButton();
	private javax.swing.JButton exitButton = new javax.swing.JButton();
	private javax.swing.JLabel JLabel6 = new javax.swing.JLabel();
	private javax.swing.JLabel JLabel7 = new javax.swing.JLabel();
	private javax.swing.JLabel JLabel8 = new javax.swing.JLabel();
	private javax.swing.JRadioButton radioButtonCSB = new javax.swing.JRadioButton();
	private JCheckBox traceCheckBox;
	private javax.swing.JLabel JLabel9 = new javax.swing.JLabel();
	private javax.swing.JLabel JLabel10 = new javax.swing.JLabel();
	private javax.swing.JLabel maxBufferSizeLabel = new javax.swing.JLabel();
	private javax.swing.JTextField elementWidthField = new javax.swing.JTextField();
	private JTextField groupMcastPortField=new JTextField();
	private JLabel p2pALabel;
	private JComboBox jpegCompressComboBox;
	private JLabel jpegCompressRateLabel;
	private JLabel jLabel3;
	private JComboBox diffPixSampleComboBox;
	private JComboBox diffPixErrorComboBox;
	private JLabel jLabel2;
	private JComboBox codingComboBox;
	private JLabel jLabel1;
	private JLabel diffPixLabel;
	private JCheckBox traceMessagesCheckBox;
	private JButton saveButton;
	private javax.swing.JLabel fileTransferSettingLabel = new JLabel();
	private JTextField partitioningNumberField;
	private JTextField maxBufferSizeField;
	private JRadioButton useEquationButton;
	private JButton autoConnectionFrameOpenButton;
	private JComboBox parallelProcessingComboBox;
	private JLabel parallelLabel;
	private JTextField ackReceivePortField;
	private JLabel ackReceivePortLabel;
	private JTextField waitingNodePortField;
	private JLabel waitingNodePortLabel;
	private JComboBox waitAfterConComboBox;
	private JLabel waitTimeAfterConnectedLabel;
	private JComboBox serverRepTermComboBox;
	private JComboBox requestRepTermComboBox;
	private JComboBox waitToHeightComboBox;
	private JLabel requestServerRepTermLabel;
	private JLabel groupNameLabel=new JLabel();
	private JTextField groupNameField=new JTextField();
	private JLabel mcast2Label=new JLabel();
	private JTextField groupMcastAddrField=new JTextField();
	private JRadioButton useSettingButton;
	private ButtonGroup partitioningButtonGroup;
	private JLabel requestRepTermLabel;
	private JLabel waitToHeightLabel;


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == radioButtonP2P)
				radioButtonP2P_actionPerformed(event);
			else if (object == radioButtonCS)
				radioButtonCS_actionPerformed(event);
			else if (object == radioButtonMcast)
				radioButtonMcast_actionPerformed(event);
			else if (object == exitButton)
				exitButton_actionPerformed(event);
			else if (object == radioButtonCSB)
				radioButtonCSB_actionPerformed(event);
		}
	}

	void radioButtonP2P_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		radioButtonP2P_actionPerformed_Interaction1(event);
	}

	void radioButtonP2P_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			radioButtonP2P.setSelected(true);
		} catch (java.lang.Exception e) {
		}
	}

	void radioButtonCS_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		radioButtonCS_actionPerformed_Interaction1(event);
	}

	void radioButtonCS_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			radioButtonCS.setSelected(true);
		} catch (java.lang.Exception e) {
		}
		
	}

	void radioButtonMcast_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		radioButtonMcast_actionPerformed_Interaction1(event);
	}

	void radioButtonMcast_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			radioButtonMcast.setSelected(true);
		} catch (java.lang.Exception e) {
		}
	}

	void exitButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		exitButton_actionPerformed_Interaction1(event);
	}

	void exitButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			this.hide();
		} catch (java.lang.Exception e) {
		}
		JRadioButton b=this.getSelectedButton();
		if(b==null) return;
		String gmode=b.getText();
		if(gmode.equals("P2P")){
		    this.communicationNode.setGroupMngrAddressPort(
		       this.groupMngrAddressField.getText() ,
		       this.groupMngrPortField.getText());
		}
		if(gmode.equals("CSB")){
		    this.communicationNode.nodeController.setUpperNodeAddressPort(
		       this.reflectorAddressField.getText(),
		       this.reflectorPortField.getText());

		}
		if(gmode.equals("CS")){
		    this.communicationNode.nodeController.setUpperNodeAddressPort(
		       this.reflectorAddressField.getText(),
		       this.reflectorPortField.getText());
		}
		if(gmode.equals("MCAST")){
		    this.communicationNode.nodeController.setUpperNodeAddressPort(
		       this.reflectorAddressField.getText(),
		       this.reflectorPortField.getText());
		}
		if(gmode.equals("P2Pa")){
			/*
		    this.communicationNode.nodeController.setRClient(new InitialRequestClient());
		    this.communicationNode.nodeController.setRServer(new InitialRequestServer());
		    */
		}
		this.communicationNode.nodeController.setGroupMode(gmode);
	}

	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if (object == radioButtonCSB)
				radioButtonCSB_itemStateChanged(event);
		}
	}

	void radioButtonCSB_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
	}

	void radioButtonCSB_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		radioButtonCSB_actionPerformed_Interaction1(event);
	}

	void radioButtonCSB_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			radioButtonCSB.setSelected(true);
		} catch (java.lang.Exception e) {
		}
	}
	/*
	public int getElementWidth(){
		String xs=this.elementWidthField.getText();
		int rtn=128;
		try{
		   rtn=(new Integer(xs)).intValue();
		}
		catch(Exception e){
			rtn=128;
		}
		return rtn;
	}
	*/
	public boolean isTracing(){
		if(this.traceCheckBox==null) return false;
		return this.traceCheckBox.isSelected();
	}
    public boolean isTracing(String x){
    	if(x.equals("messages")){
    		return this.traceMessagesCheckBox.isSelected();
    	}
    	return false;
    }
	
	public void setTrace(boolean tf) {
		// TODO 自動生成されたメソッド・スタブ
	}
	
	public boolean isAnswer(String x) {
		// TODO 自動生成されたメソッド・スタブ
		if(x.equals("usingEquation")){
			return this.useEquationButton.isSelected();
		}
		return false;
	}

	public int getAnswer(String x) {
		// TODO 自動生成されたメソッド・スタブ
		if(x.equals("partitioningNumber")){
			String xx=this.partitioningNumberField.getText();
			int xi=0;
			try{
				xi=(new Integer(xx)).intValue();
			}
			catch(Exception e){
				xi=10;
			}
			return xi;
			
		}
		if(x.equals("maxBufferSize")){
			String xx=this.maxBufferSizeField.getText();
			int xi=0;
			try{
				xi=(new Integer(xx)).intValue();
			}
			catch(Exception e){
				xi=10;
			}
			return xi;
			
		}
		if(x.equals("diffPixSamplingRate")){
//			String xx=this.diffPixSampField.getText();
			String xx=(String)(this.diffPixSampleComboBox.getSelectedItem());
			int xi=4;
			try{
				xi=(new Integer(xx)).intValue();
			}
			catch(Exception e){
				xi=4;
			}
			return xi;
			
		}
		if(x.equals("pixImageWidthElement")){
			String xs=this.elementWidthField.getText();
			int rtn=128;
			try{
			   rtn=(new Integer(xs)).intValue();
			}
			catch(Exception e){
				rtn=128;
			}
			return rtn;
		}
		System.out.println("could not found out : "+x);
		return 0;
	}
	
	private void saveButtonActionPerformed(ActionEvent evt) {
		/*
		{
			components.setBounds(-8, -30, 534, 644);
			components.setPreferredSize(new java.awt.Dimension(550, 700));
		}
		*/
		//		System.out.println("saveButton.actionPerformed, event="+evt);
		//TODO add your code for saveButton.actionPerformed
		this.saveProperties();
		this.setVisible(false);
	}
	
	private void traceMessagesCheckButtonActionPerformed(ActionEvent evt) {
		System.out.println("traceMessagesCheckButton.actionPerformed, event="+evt);
		//TODO add your code for traceMessagesCheckButton.actionPerformed
	}
	
	Properties setting;
	String fileName="dsr-settings.properties";
	public void saveProperties(){
		this.setting.setProperty("partitioningNumber", this.partitioningNumberField.getText());
		this.setting.setProperty("maxBufferSize",this.maxBufferSizeField.getText());
		this.setting.setProperty("multicastAddress", this.multicastAddressField.getText());
		this.setting.setProperty("multicastPort", this.multicastPortField.getText());
		this.setting.setProperty("reflectorAddress", this.reflectorAddressField.getText());
		this.setting.setProperty("reflectorPort",this.reflectorPortField.getText());
		this.setting.setProperty("groupManagerAddress", this.groupMngrAddressField.getText());
		this.setting.setProperty("groupManagerPort", this.groupMngrPortField.getText());
		this.setting.setProperty("radioButtonCS", ""+this.radioButtonCS.isSelected());
		this.setting.setProperty("radioButtonCSB", ""+this.radioButtonCSB.isSelected());
        this.setting.setProperty("radioButtonMcast", ""+this.radioButtonMcast.isSelected());
        this.setting.setProperty("radioButtonP2P", ""+this.radioButtonP2P.isSelected());
        this.setting.setProperty("radioButtonP2Pa", ""+this.radioButtonP2Pa.isSelected());
        this.setting.setProperty("traceCheckBox", ""+this.traceCheckBox.isSelected());
        this.setting.setProperty("traceMessagesCheckBox", ""+this.traceMessagesCheckBox.isSelected());
		this.setting.setProperty("useEquation", ""+this.useEquationButton.isSelected());
		this.setting.setProperty("useSetting", ""+this.useSettingButton.isSelected());
		this.setting.setProperty("diffPixSample",(String)(this.diffPixSampleComboBox.getSelectedItem()));
		this.setting.setProperty("diffPixErrorRate", (String)(this.diffPixErrorComboBox.getSelectedItem()));
		this.setting.setProperty("diffPixSamplingWidth", (String)(this.diffPixSampleComboBox.getSelectedItem()));
	    this.setting.setProperty("pictureCoding",(String)(this.codingComboBox.getSelectedItem()));
		this.setting.setProperty("pictureElementWidth", this.elementWidthField.getText());
		this.setting.setProperty("jpegCompressRate", (String)(this.jpegCompressComboBox.getSelectedItem()));
		this.setting.setProperty("groupName", (String)(this.groupNameField.getText()));
		this.setting.setProperty("groupMcastAddr", (String)(this.groupMcastAddrField.getText()));
		this.setting.setProperty("groupMcastPort", (String)(this.groupMcastPortField.getText()));
		this.setting.setProperty("MakeBinaryTree-ServerRepTerm", (String)(this.serverRepTermComboBox.getSelectedItem()));
		this.setting.setProperty("MakeBinaryTree-requestRepTerm", (String)(this.requestRepTermComboBox.getSelectedItem()));
		this.setting.setProperty("MakeBinaryTree-waitToHeight", (String)(this.waitToHeightComboBox.getSelectedItem()));
		this.setting.setProperty("MakeBinaryTree-waitAfterConnect", (String)(this.waitAfterConComboBox.getSelectedItem()));
		this.setting.setProperty("MakeBinaryTree-waitingNodePort", (String)(this.waitingNodePortField.getText()));
		this.setting.setProperty("MakeBinaryTree-ackReceivePort", (String)(this.ackReceivePortField.getText()));
		this.setting.setProperty("MakeBinaryTree-parallel-processing", (String)(this.parallelProcessingComboBox.getSelectedItem()));
			
		try{
			FileOutputStream saveS = new FileOutputStream(fileName);
			setting.store(saveS,"--- dsr settings ---");
		}
		catch(Exception e){
			System.err.println(e);
		}
		
	}
	public void loadProperties(){
		try{
			setting=new Properties();
			FileInputStream appS = new FileInputStream(fileName);
			setting.load(appS);
		}
		catch(Exception e){
			this.saveProperties();
			return;
		}
		try{
		this.partitioningNumberField.setText(this.setting.getProperty("partitioningNumber"));
		String w=this.setting.getProperty("pictureElementWidth");
		this.elementWidthField.setText(w);
		this.maxBufferSizeField.setText(this.setting.getProperty("maxBufferSize"));
		this.multicastAddressField.setText(this.setting.getProperty("multicastAddress"));
		this.multicastPortField.setText(this.setting.getProperty("multicastPort"));
		this.reflectorAddressField.setText(this.setting.getProperty("reflectorAddress"));
		this.reflectorPortField.setText(this.setting.getProperty("reflectorPort"));
		this.groupMngrAddressField.setText(this.setting.getProperty("groupManagerAddress"));
		this.groupMngrPortField.setText(this.setting.getProperty("groupManagerPort"));
		this.radioButtonCS.setSelected(this.setting.getProperty("radioButtonCS").equals("ture"));
		this.radioButtonCSB.setSelected(this.setting.getProperty("radioButtonCSB").equals("ture"));
        this.radioButtonMcast.setSelected(this.setting.getProperty("radioButtonMcast").equals("true"));
        this.radioButtonP2P.setSelected(this.setting.getProperty("radioButtonP2P").equals("true"));
        this.radioButtonP2Pa.setSelected(this.setting.getProperty("radioButtonP2Pa").equals("true"));
        this.traceCheckBox.setSelected(this.setting.getProperty("traceCheckBox").equals("true"));
        this.traceMessagesCheckBox.setSelected(this.setting.getProperty("traceMessagesCheckBox").equals("true"));
		this.useEquationButton.setSelected(this.setting.getProperty("useEquation").equals("true"));
		this.useSettingButton.setSelected(this.setting.getProperty("useSetting").equals("true"));
		this.diffPixSampleComboBox.setSelectedItem(this.setting.getProperty("diffPixSamplingWidth"));
		
       String p1=this.setting.getProperty("pictureCoding");
        this.codingComboBox.setSelectedItem(p1);

        p1=this.setting.getProperty("diffPixErrorRate");
        this.diffPixErrorComboBox.setSelectedItem(p1);
        p1=this.setting.getProperty("jpegCompressRate");
		this.jpegCompressComboBox.setSelectedItem(p1);
		w=this.setting.getProperty("groupName");
		if(w==null) w="g1";
		this.groupNameField.setText(w);
		w=this.setting.getProperty("groupMcastAddr");
		if(w==null) w="224.0.0.7";
		this.groupMcastAddrField.setText(w);
		w=this.setting.getProperty("groupMcastPort");
		if(w==null) w="10007";
		this.groupMcastPortField.setText(w);
		
		w=this.setting.getProperty("MakeBinaryTree-ServerRepTerm");
		if(w!=null) this.serverRepTermComboBox.setSelectedItem(w);
		w=this.setting.getProperty("MakeBinaryTree-requestRepTerm");
		if(w!=null) this.requestRepTermComboBox.setSelectedItem(w);
		w=this.setting.getProperty("MakeBinaryTree-waitToHeight");
		if(w!=null) this.waitToHeightComboBox.setSelectedItem(w);
		w=this.setting.getProperty("MakeBinaryTree-waitAfterConnect");
		if(w!=null) this.waitAfterConComboBox.setSelectedItem(w);

		w= this.setting.getProperty("MakeBinaryTree-waitingNodePort");
		if(w==null) w="2785";
		this.waitingNodePortField.setText(w);
		w= this.setting.getProperty("MakeBinaryTree-ackReceivePort");
		if(w==null) w="4095";
		this.ackReceivePortField.setText(w);
		w= this.setting.getProperty("MakeBinaryTree-parallel-processing");
		if(w==null) w="true";
		this.parallelProcessingComboBox.setSelectedItem(w);

		}
		catch(Exception e){
			System.out.println(""+e.toString());
		}
	}

	@Override
	public boolean getBoolean(String x) {
		// TODO Auto-generated method stub
		if(x.equals("MakeBinaryTree-parallel-processing")){
		   String w=(String)(this.parallelProcessingComboBox.getSelectedItem());
		   if(w==null)
			   return true;
		   if(w.equals("true")){
			   return true;
		   }
		   else{
			   return false;
		   }
		}

		return false;
	}

	@Override
	public double getDouble(String x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloat(String x) {
		// TODO Auto-generated method stub
		if(x.equals("jpegCompressRate")){
			String rs=(String)(this.jpegCompressComboBox.getSelectedItem());
			float rtn=(float)(new Float(rs)).floatValue();
			return rtn;
		}
		return 1.0f;
	}

//	@Override
	public int getInt(String x) {
		// TODO Auto-generated method stub
		/*
		if(x.equals("sendInterval"))
		return this.sendInterval;
		if(x.equals("videoInterval"))
			return this.videoInterval;
			*/
		if(x.equals("pictureSegmentWidth")){
			String y=this.elementWidthField.getText();
			return (new Integer(y)).intValue();
		}
		else
		if(x.equals("errorRate")){
			String y=(String)(this.diffPixErrorComboBox.getSelectedItem());
			return (new Integer(y)).intValue();
		}
		else
		if(x.equals("errorSamplingWidth")){
			String y=(String)(this.diffPixSampleComboBox.getSelectedItem());
			return (new Integer(y)).intValue();
		}
		else
		if(x.equals("groupMcastPort")){
			String y=this.groupMcastPortField.getText();
			return (new Integer(y)).intValue();
		}
		else
		if(x.equals("nodePort")){
			String y=this.groupMcastPortField.getText();
			return (new Integer(y)).intValue();
		}
		else{
			String y=this.setting.getProperty(x);
			int rtn=0;
			try{ rtn=(new Integer(y)).intValue();}
			catch(Exception e){rtn=0;}
			return rtn;
		}
	}

//	@Override
	public String getString(String x) {
		// TODO Auto-generated method stub
		if(x.equals("pictureCoding")){
			return (String)(this.codingComboBox.getSelectedItem());
		}
		else
		if(x.equals("groupName")){
			return this.groupNameField.getText();
		}
		else
		if(x.equals("groupMcastAddr")){
			return this.groupMcastAddrField.getText();
		}
		else
		if(x.equals("groupMode")){
			JRadioButton b=this.getSelectedButton();
			if(b==null) return null;
			String gmode=b.getText();
			return gmode;
		}
		else{
			String rtn=this.setting.getProperty(x);
			return rtn;
		}
	}
    private AutoConnectionFrame autoConnectionFrame;
	private void autoConnectionFrameOpenButtonActionPerformed(ActionEvent evt) {
		System.out.println("autoConnectionFrameOpenButton.actionPerformed, event="+evt);
		//TODO add your code for autoConnectionFrameOpenButton.actionPerformed
		if(autoConnectionFrame==null){
			autoConnectionFrame=new AutoConnectionFrame(this.communicationNode);
		}
		autoConnectionFrame.setVisible(true);
	}

}