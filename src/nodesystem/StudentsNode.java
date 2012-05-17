package nodesystem;

import java.awt.Dimension;
import java.util.Locale;
import java.util.ResourceBundle;


public class StudentsNode extends CommunicationNode
{

	public void spawnApplication(String application)
    {
        if(application.indexOf("*")==0)
             return;
        String modeOfThisNode=super.getControlMode();
//        String modeOfThisNode=this.commandTranceiver.getOperationMode();
        if(modeOfThisNode.equals("local") ||
           modeOfThisNode.equals("common")) {
            applicationManager.spawnApplication(application,controlMode);
            this.spawnRemote(application);
           }
    }

	static public void main(String args[])
	{
	    StudentsNode sn=new StudentsNode();
		Locale locale=new Locale("","");
		sn.resourceBundle = ResourceBundle.getBundle("dsrWords" , locale);
	    sn.setTitle("DSR/communication node - student");
		sn.setVisible(true);
		sn.className="Students_Node";
		sn.controlMode="receive";
		sn.setWords();
//		sn.commandTranceiver.setOperationMode("remote");
        int i=0; // args[i]
        while(i<args.length){
    		if(args[i].equals("-h")){
    		    i++;
    	    	String groupManagerAddress=args[i];
    	    	if(groupManagerAddress==null){
    	    	    System.out.println("usage: StudentsNode [ -h <host address>] ");
    	    	}
	    	    sn.groupMngrAddressField.setText(groupManagerAddress);
    		    sn.joinButton_actionPerformed_Interaction1(null);
	    	}
			if(args[i].equals("-m")){
				i++;
				String controlMode=args[i];
				if(controlMode==null){
					System.out.println("usage: CommunicationNode [-m common]");
				}
				sn.setControlMode(controlMode);
			}
			if(args[i].equals("-locale")){
				i++;
				String localeName=args[i];
				int sepPos=localeName.indexOf("_");
				if(sepPos<=0){
					System.out.println("usage: CommunicationNode [-locale <language>_<country>]");
					return;
				}
				else{
					String language=localeName.substring(0,sepPos);
					String country=localeName.substring(sepPos+1,localeName.length());
		   // ロケールを生成
					locale = new Locale(language,country);
		   // 指定されたロケールのリソースバンドルを取得
					sn.resourceBundle = ResourceBundle.getBundle("dsrWords" , locale);
				}
			}
		    i++;
		}
		sn.setWords();
		sn.setControlMode(sn.controlMode);
		sn.serialNo=1;
		sn.init(); // sakuragi
        sn.setIcons(sn.getIconPlace());
		sn.start();
	}

/*
    public void setChoiceBox()
    {
		applicationChoiceBox.addItem("DrawFrame");
		applicationChoiceBox.addItem("BasicFrame");
		applicationChoiceBox.addItem("WebFrame");
		applicationChoiceBox.addItem("WebLEAPFrame");
    }
*/
	public StudentsNode()
	{
	    super();
		//{{INIT_CONTROLS
		setTitle("Communication Node");
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		setSize(462,526);
		setVisible(false);
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		//}}

		//{{INIT_MENUS
		//}}
//        this.setControlMode("receive");
	}

	public void joinButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		this.nodeController.joinGroupWithMode("student");
	}

	public StudentsNode(String title)
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
//		Insets ins = getInsets();
//		setSize(ins.left + ins.right + d.width, ins.top + ins.bottom + d.height);
//		Component components[] = getContentPane().getComponents();
//		for (int i = 0; i < components.length; i++)
//			{
//			Point p = components[i].getLocation();
//			p.translate(ins.left, ins.top);
//			components[i].setLocation(p);
//		}
		fComponentsAdjusted = true;
	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
		Object object = event.getSource();
		if (object == StudentsNode.this)
			StudentsNode_WindowClosing(event);
		}
	}

	void StudentsNode_WindowClosing(java.awt.event.WindowEvent event)
	{
		dispose();		 // dispose of the Frame.
	}
	//{{DECLARE_CONTROLS
	//}}

	//{{DECLARE_MENUS
	//}}

}