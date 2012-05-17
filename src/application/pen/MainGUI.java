package application.pen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.TooManyListenersException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import java.awt.event.*;
import javax.swing.event.*;

import application.webbrowser.WebFrame;

import nodesystem.CommunicationNode;
import nodesystem.DialogListener;
import nodesystem.EditDialog;
import nodesystem.FileFrame;
import nodesystem.LoadFileFrame;

import controlledparts.*;
/**
 * GUIの基礎部分です。
 * 
 * @author rn_magi
 *
 */
public class MainGUI extends ControlledFrame
implements FrameWithControlledTextAreas,
FrameWithControlledButton,
FrameWithControlledSlider,
FrameWithControlledPane,
FrameWithControlledTabbedPane,
FrameWithControlledMenu,
FrameWithControlledMenuItem,
DialogListener

{
	String Version			= "ver1.09_7";
	String SystemName		= "PEN";
	String WindowName		= SystemName + " " + Version;
//	JFrame main_window		= new JFrame("NewFile - " + WindowName);
	ControlledFrame main_window = new ControlledFrame("NewFile - "+ WindowName);
	JPanel menu_panel		= new JPanel();
	JPanel run_time_panel		= new JPanel();
	JPanel edit_panel		= new JPanel();
	JPanel edit_area_panel		= new JPanel();
//	JPanel run_panel		= new JPanel();
	JPanel console_panel		= new JPanel();
	JPanel var_panel		= new JPanel();
	JSplitPane main_splitpane;
	JSplitPane run_splitpane;
	JScrollPane edit_JSP;
	JScrollPane console_JSP;
	JScrollPane console_log_JSP;
	JScrollPane var_JSP;

	DropTarget edit_area_drop	= new DropTarget();
	
	Font font			= new Font("ＭＳ ゴシック", 0, 14);
	String ButtonListFile		= "";
	Document edit_doc		= new PlainDocument();
	EditAreaUndoableEditListener undo	= new EditAreaUndoableEditListener();
	
	String[] columnNames		= {"型" , "変数名" , "値"};
	int[] COLSIZE			= {45,80,100};
	
	static DefaultTableModel vt_model;
	static MyJMenuBar MenuBar;
	static MyRunJLabel run_print	= new MyRunJLabel();
/*
	static JButton new_button	= new MenuButton("新規", 60, 30);
	static JButton open_button	= new MenuButton("開く", 60, 30);
	static JButton save_button	= new MenuButton("保存", 60, 30);
	static JButton run_button	= new MenuButton("実行", 80, 30);
	static JButton step_button	= new MenuButton("一行実行", 80, 30);
	static JButton stop_button	= new MenuButton("始めに戻る", 80, 30);
	static JTabbedPane console_tab	= new JTabbedPane();
	static JTextArea run_point	= new JTextArea(1,1);
	static JTextArea breakpoint	= new JTextArea(1,1);
	static JTextArea numbar_area	= new JTextArea(1,2);
	static JTextArea edit_area	= new JTextArea(0,0);
	static JTextArea console	= new JTextArea(0,0);
	static JTextArea console_log	= new JTextArea(13,0);
	static JTable var_table		= new JTable();
	static JSlider run_time		= new JSlider(SwingConstants.HORIZONTAL,0,2000,0);
	*/
	static JFileChooser fc		= new JFileChooser("./");
	static RunButtonListener RunButton	= new RunButtonListener();
	
	static IntVgOutputWindow gDrowWindow	= new IntVgOutputWindow();

	
//	static JButton new_button			= new MenuButton("新規", 60, 30);
	static ControlledButton new_button = new MenuButton("新規", 60, 30);
//	static JButton open_button			
//	static ControlledButton open_button = new MenuButton("開く", 60, 30);
	static ControlledButton open_button = new MenuButton("開く／保存", 75, 30);	
//	static JButton save_button			
//	static ControlledButton save_button = new MenuButton("保存", 60, 30);
//	static JButton run_button			
	static ControlledButton run_button  = new MenuButton("実行", 75, 30);
//	static JButton step_button			
	static ControlledButton step_button = new MenuButton("一行実行", 75, 30);
//	static JButton stop_button			
	static ControlledButton stop_button = new MenuButton("始めに戻る", 75, 30);
//	static JTabbedPane console_tab		= new JTabbedPane();
	static ControlledTabbedPane console_tab = new ControlledTabbedPane();
//	static JTextArea run_point			= new JTextArea(1,1);
	static ControlledTextArea run_point = new ControlledTextArea(1,1);
//	static JTextArea breakpoint			= new JTextArea(1,1);
	static ControlledTextArea breakpoint = new ControlledTextArea(1,1);
//	static JTextArea numbar_area		= new JTextArea(1,2);
	static ControlledTextArea numbar_area = new ControlledTextArea(1,2);
//	static JTextArea edit_area			= new JTextArea(0,0);
	static ControlledTextArea edit_area = new ControlledTextArea(0,0);
//	static JTextArea console			= new JTextArea(10,0);
	static ControlledTextArea console  = new ControlledTextArea(10,0);
//	static JTextArea console_log		= new JTextArea(10,0);
	static ControlledTextArea console_log = new ControlledTextArea(10,0);
	static JTable var_table				= new JTable();
//	static JSlider run_time				= new JSlider(SwingConstants.HORIZONTAL,0,2000,0);
	static ControlledSlider run_time   = new ControlledSlider(SwingConstants.HORIZONTAL,0,2000,0);

	ControlledButton helpButton= new ControlledButton();
	
	static boolean Run_flag	= false;
	static boolean Step_flag	= false;
	static boolean Suspend_flag	= false;
	static boolean Input_flag	= false;
	static boolean Stop_flag	= false;
	static boolean Bug_flag	= false;
	static boolean Debug_flag	= false;


	PenFileFilter filter[] = {
			new PenFileFilter("txt" , "テキストファイル （*.txt）") ,
			new PenFileFilter("pen" , "PENファイル （*.pen）")
		};

//	public MainGUI(){
//		GUI();
//	}
	
	
// fields for dsr
	Vector texts=new Vector();
	Vector buttons=new Vector();
	Vector sliders=new Vector();
	Vector panes=new Vector();
	Vector tabbedPanes=new Vector();
	Vector menus=new Vector();
	Vector menuItems=new Vector();
	Hashtable editButtons = new Hashtable();
	WebFrame browser;
	
	static Object isAlreadyConstructed=null;
    
	FileFrame fileFrame;
//	static ExecutionPart executionPart;
	int lastManipulatedTextArea;

	
    //
//	NewFileButtonListener newFileButtonListener=new NewFileButtonListener(main_window,edit_area,WindowName);// t.yamanoue
//	FileOpenButtonListener fileOpenButtonListener = new FileOpenButtonListener(fc, main_window, edit_area, WindowName); // t.yamanoue
//	FileSaveButtonListener fileSaveButtonListener = new FileSaveButtonListener(fc, main_window, edit_area, WindowName); // t.yamanoue
	NewFileButtonListener newFileButtonListener=new NewFileButtonListener(this,edit_area,WindowName);// t.yamanoue
	FileOpenButtonListener fileOpenButtonListener = new FileOpenButtonListener(fc, this, edit_area, WindowName); // t.yamanoue
	FileSaveButtonListener fileSaveButtonListener = new FileSaveButtonListener(fc, this, edit_area, WindowName); // t.yamanoue
	ExitButtonListener exitButtonListener = new ExitButtonListener(fc, this, edit_area); // t.yamanoue
		
	ConsoleKeyListener consoleKeyListener=new ConsoleKeyListener();	
	EditAreaKeyListener editAreaKeyListener=new EditAreaKeyListener(edit_area);
	EditAreaMouseListener editAreaMouseListener=new EditAreaMouseListener(edit_area);
//	EditAreaDocumentListener editAreaDocumetnListener=new EditAreaDocumentListener(main_window, edit_area, numbar_area);
	EditAreaDocumentListener editAreaDocumetnListener=new EditAreaDocumentListener(this, edit_area, numbar_area);
//	UndoMenuItem.addActionListener(undo);
//	RedoMenuItem.addActionListener(undo);
	EditAreaUndoableEditListener editAreaUndoAbleListner =new EditAreaUndoableEditListener();
	
//	ConsoleCopyMenuItem.addActionListener(new ConsoleCopyButtonListener(console));
	ConsoleCopyButtonListener consoleButtonListener= new ConsoleCopyButtonListener(console);
//	VarCopyMenuItem.addActionListener(new VarCopyButtonListener(var_table));
	VarCopyButtonListener varCopyButtonListener=new VarCopyButtonListener(var_table);

//	HelpPenMenuItem.addActionListener(new HelpPenButtonListener(SystemName, Version, window));// add window, 2006 3.23

	public MainGUI(){
		// 編集ボタン設定ファイルの場所:
		//   dsr/commondata/pen/ButtonList.ini		
		String currentDir=System.getProperty("user.dir");
		File dsrRoot=new File(currentDir);
		File commonDataDir=new File(dsrRoot,"commondata");
		if(!commonDataDir.exists())
					 commonDataDir.mkdir();
		ButtonListFile=commonDataDir.getPath()+
		   System.getProperty("file.separator")+
		   "pen"+
		   System.getProperty("file.separator")+		   
		   "ButtonList.ini";   
	    GUI();
	}
	/**
	 * 実行時の引数の解析を行い GUIメソッド を呼び出す。
	 */
	public MainGUI(String argv[]){
		File path = new File(System.getProperty("java.class.path"));
		if(path.isDirectory()){
			ButtonListFile = path.getPath() + System.getProperty("file.separator") + "ButtonList.ini";
		} else {
			ButtonListFile = path.getParent() + System.getProperty("file.separator") + "ButtonList.ini";
		}
		
		for(int i = 0; i < argv.length; i++) {
			if(argv[i].equals("-teacher") || argv[i].equals("-t")){
				COLSIZE[0] = 80;
				font = new Font("ＭＳ ゴシック", 0, 22);
			} else if(argv[i].equals("-debug") || argv[i].equals("-d")) {
				Debug_flag = true;
			} else if(argv[i].equals("-b")){
				ButtonListFile = argv[++i];
			}
		}
		this.GUI();
	}
	
	/**
	 * 各コンポーネントの初期化や配置を行う
	 */
	public void GUI(){
		/*
		JFrame.setDefaultLookAndFeelDecorated(true);
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		}catch(Exception ex){}
		*/
		
		for(int i = 0 ; i < filter.length ; i++)
			fc.addChoosableFileFilter(filter[i]);

// 2006 3/22 t.yamanoue
		/*
//		main_window.addWindowListener(new MyWindowAdapter(main_window, edit_area));
		main_window.setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("pen.png")));
//		main_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_window.setSize(800,600);
		main_window.setResizable(false); // uncomment by t. yamanoue, 2006 3/19
		main_window.setLocation(100,100);
		MenuBar = new MyJMenuBar(fc, main_window, edit_area, console, var_table, SystemName, Version, undo);
		main_window.setJMenuBar(MenuBar.createMenuBar());
		*/
		
		// 2006 3.22 t.yamanoue
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("pen.png")));
//		main_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800,600);
		this.setResizable(false); // uncomment by t. yamanoue, 2006 3/19
		this.setLocation(100,100);
		
		MenuBar = new MyJMenuBar(fc, this, edit_area, console, var_table, SystemName, Version, undo);
		this.setJMenuBar(MenuBar.createMenuBar());

		// added by 2006 3/9 t.yamanoue
		this.menus.addElement(MenuBar.FileMenu);
		this.menus.addElement(MenuBar.EditMenu);
		this.menus.addElement(MenuBar.HelpMenu);
		int maxMenu=menus.size();
		for(int i=0;i<maxMenu;i++){
			ControlledMenu m=(ControlledMenu)(menus.elementAt(i));
			m.setFrame(this);
			m.setID(i);
			m.initGUI();
		}
		this.menuItems.addElement(MenuBar.ConsoleCopyMenuItem);
		this.menuItems.addElement(MenuBar.CopyMenuItem);
		this.menuItems.addElement(MenuBar.CutMenuItem);
		this.menuItems.addElement(MenuBar.ExitMenuItem);
		this.menuItems.addElement(MenuBar.FileOpenMenuItem);
//		this.menuItems.addElement(MenuBar.FileReSaveMenuItem);
		this.menuItems.addElement(MenuBar.HelpPenMenuItem);
		this.menuItems.addElement(MenuBar.NewFileMenuItem);
		this.menuItems.addElement(MenuBar.PasteMenuItem);
		this.menuItems.addElement(MenuBar.RedoMenuItem);
		this.menuItems.addElement(MenuBar.UndoMenuItem);
		this.menuItems.addElement(MenuBar.VarCopyMenuItem);
		int maxMenuItems=menuItems.size();
		for(int i=0;i<maxMenuItems;i++){
			ControlledMenuItem m=(ControlledMenuItem)(menuItems.elementAt(i));
			m.setFrame(this);
			m.setID(i);
			m.initGUI();
		}
		
		menu_panel.setLayout(new BoxLayout(menu_panel,BoxLayout.X_AXIS));
//			new_button.addActionListener(new NewFileButtonListener(main_window,edit_area,WindowName)); // t.yamanoue
			menu_panel.add(new_button);
//			open_button.addActionListener(new FileOpenButtonListener(fc, main_window, edit_area, WindowName)); // t.yamanoue
			menu_panel.add(open_button);
//			save_button.addActionListener(new FileSaveButtonListener(fc, main_window, edit_area, WindowName)); // t.yamanoue
//			menu_panel.add(save_button);
			menu_panel.add(new MyJLabel(new Dimension(50,30)));
//			run_button.addActionListener(RunButton); // t.yamanoue
			menu_panel.add(run_button);
//			step_button.addActionListener(RunButton); // t.yamanoue
			menu_panel.add(step_button);
//			stop_button.addActionListener(RunButton); // t.yamanoue
			menu_panel.add(stop_button);
			menu_panel.add(new MyJLabel(new Dimension(20,30)));
			run_time_panel.setLayout(new BoxLayout(run_time_panel,BoxLayout.Y_AXIS));
				run_time_panel.add(new MyJLabel("(速)　　←　　実行速度　　→　　(遅)"));
				run_time.addMouseListener(new RunTimeMouseListener(run_time));
				run_time.addChangeListener(new RunTimeChangeListener(run_time));
				run_time.setPaintTicks(true);
				run_time.setMajorTickSpacing(200);
				run_time.setMinorTickSpacing(100);
				run_time_panel.add(run_time);
			menu_panel.add(run_time_panel);
			menu_panel.add(new MyJLabel(new Dimension(10,30)));
			menu_panel.add(run_print);
			menu_panel.add(new MyJLabel(new Dimension(10,30)));

//		main_window.getContentPane().add(menu_panel,BorderLayout.NORTH);
			this.getContentPane().add(menu_panel,BorderLayout.NORTH);

		edit_panel.setLayout(new BoxLayout(edit_panel,BoxLayout.Y_AXIS));
			edit_area_panel.setLayout(new BoxLayout(edit_area_panel,BoxLayout.X_AXIS));
				breakpoint.setEditable(false);
				breakpoint.setBackground(new Color(220, 220, 220));
				breakpoint.setFont(font);
				breakpoint.addMouseListener(new BreakPointMouseListener(breakpoint));
				breakpoint.append("　");

				run_point.setEditable(false);
				run_point.setBackground(new Color(220, 220, 220));
				run_point.setForeground(new Color(255, 0 ,0));
				run_point.setFont(font);
				run_point.append("●");
				
				numbar_area.setEditable(false);
				numbar_area.setFont(font);
				numbar_area.append("  1:" + "\n");
				
//				edit_doc.addDocumentListener(new EditAreaDocumentListener(main_window, edit_area, numbar_area));
				edit_doc.addDocumentListener(new EditAreaDocumentListener(this, edit_area, numbar_area));
				edit_doc.addUndoableEditListener(undo);
				edit_area.setDocument(edit_doc);
				edit_area.setTabSize(4);
				edit_area.setFont(font);
//				edit_area.addKeyListener(new EditAreaKeyListener(edit_area)); // t.yamanoue
				edit_area.addMouseListener(new EditAreaMouseListener(edit_area)); // t.yamanoue
				
				try {
//					edit_area_drop.addDropTargetListener(new FileDropOpen(fc, main_window, edit_area, WindowName));
					edit_area_drop.addDropTargetListener(new FileDropOpen(fc, this, edit_area, WindowName));
					edit_area.setDropTarget(edit_area_drop);
				} catch (TooManyListenersException e) {
					e.printStackTrace();
				}
				
				edit_area_panel.add(breakpoint);
				edit_area_panel.add(run_point);
				edit_area_panel.add(numbar_area);
				edit_JSP = //new JScrollPane(edit_area);
					   new ControlledScrollPane(edit_area);
				edit_JSP.setRowHeaderView(edit_area_panel);
				edit_JSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

			edit_panel.add(new MyJLabel("編集画面"));
			edit_panel.add(edit_JSP);
//			added the following line by t.yamanoue, 24aug2005 for pen 1.08
			edit_panel.add(new ButtonEdit(ButtonListFile,this).edit_button_area_toolbar);
			
	//	run_panel.setLayout(new BoxLayout(run_panel,BoxLayout.Y_AXIS));
	//	run_panel.setPreferredSize(new Dimension(250,500));
			console_panel.setLayout(new BoxLayout(console_panel,BoxLayout.Y_AXIS));
//				console.addKeyListener(new ConsoleKeyListener());
				console.setEditable(false);
				console.setLineWrap(true);
				console.setFont(font);
				console.setMargin(new Insets(1,5,1,1));
				console.setBackground(new Color(240, 240, 240));
				console_JSP = //new JScrollPane(console);
					 new ControlledScrollPane(console);
				console_JSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				
				console_log.setEditable(false);
				console_log.setLineWrap(true);
				console_log.setFont(font);
				console_log.setMargin(new Insets(1,5,1,1));
				console_log.setBackground(new Color(240, 240, 240));
				console_log_JSP = //new JScrollPane(console_log);
					new ControlledScrollPane(console_log);
				console_log_JSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				
				console_tab.setFont(new Font("", 0, 12));
				console_tab.addTab("実行画面", console_JSP);
				console_tab.addTab("履歴", console_log_JSP);
				
				console_panel.add(new MyJLabel("コンソール画面"));
				console_panel.add(console_tab);
				
			var_panel.setLayout(new BoxLayout(var_panel,BoxLayout.Y_AXIS));
				vt_model= new DefaultTableModel(columnNames,0);		
				var_table.setPreferredScrollableViewportSize(new Dimension(250,200));
				var_table.setModel(vt_model);
				var_table.setFont(font);
				for(int i=0; i < COLSIZE.length; i++)
					var_table.getColumnModel().getColumn(i).setPreferredWidth(COLSIZE[i]);
				var_table.setRowHeight(20);
				var_JSP = //new JScrollPane(var_table);
					  new ControlledScrollPane(var_table);
				var_JSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				var_panel.add(new MyJLabel("変数表示画面"));
				var_panel.add(var_JSP);

		run_splitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, console_panel, var_panel);
		run_splitpane.setPreferredSize(new Dimension(250,500));
		run_splitpane.setDividerSize(3);

		//	run_panel.add(console_panel);
		//	run_panel.add(var_panel);
		
		main_splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, edit_panel, run_splitpane);
		main_splitpane.setDividerSize(3);
		
//		main_window.getContentPane().add(main_splitpane,BorderLayout.CENTER);
		this.getContentPane().add(main_splitpane,BorderLayout.CENTER);

	//	main_window.getContentPane().add(edit_panel,BorderLayout.CENTER);
	//	main_window.getContentPane().add(run_panel,BorderLayout.EAST);
		
		
		// added by t. yamanoue, 2006 2/17
		if(isAlreadyConstructed!=null) return;
		isAlreadyConstructed=new Integer(1);
		this.setTitle("NewFile - " + WindowName); 
		
		// added by t. yamanoue, 2006 11/29
		super.registerListeners();
         
		this.texts.addElement(edit_area);
		this.texts.addElement(breakpoint);
		this.texts.addElement(console);
		this.texts.addElement(console_log);
		this.texts.addElement(numbar_area);
		this.texts.addElement(run_point);
		int numberOfTexts=texts.size();
		for(int i=0;i<numberOfTexts;i++){
			ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
			t.setFrame(this);
			t.setID(i);
		}
        
		buttons.addElement(new_button);
		buttons.addElement(open_button);
//		buttons.addElement(save_button);
		buttons.addElement(run_button);
		buttons.addElement(step_button);
		buttons.addElement(stop_button);
		int numberOfButtons=buttons.size();
		for(int i=0;i<numberOfButtons;i++){
			ControlledButton b=(ControlledButton)(buttons.elementAt(i));
			b.setFrame(this);
			b.setID(i);
		}

		this.panes.addElement(this.console_JSP);
		this.panes.addElement(this.console_log_JSP);
		this.panes.addElement(this.edit_JSP);
		int numberOfPanes=panes.size();
		for(int i=0;i<numberOfPanes;i++){
			ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(i));
			p.setFrame(this);
			p.setID(i);
		}

		this.tabbedPanes.addElement(console_tab);
		console_tab.setFrame(this);
		console_tab.setID(0);

//		fileFrame=new FileFrame("pen file");

		this.sliders.addElement(run_time);
		run_time.setFrame(this);
		run_time.setID(0);

//		main_window.setVisible(true);
		this.setVisible(true);
		edit_area.requestFocus();
	
	}
	
    public void setFileFrame(FileFrame f){
    	this.fileFrame=f;
    	f.setTitle("pen file");
     }
    
	public ControlledFrame spawnMain(CommunicationNode cn,String args,int pID, String controlMode)
	{
//		   MainGUI pen= new MainGUI();
		   MainGUI pen=this;
		   pen.setCommunicationNode(cn);
		   pen.pID=pID;
//			 bf.encodingCode=code;
		   pen.setTitle("PEN");
		
		// spawn Basic Slaves at other nodes in this group.
		   pen.ebuff=cn.commandTranceiver.ebuff;
           FileFrame fileFrame=new FileFrame();
           fileFrame.setFileChooser(new JFileChooser());
           fileFrame.setSeparator(cn.getSeparator());
           fileFrame.setListener(pen);
           pen.setFileFrame(fileFrame);

//			 bf.show();
		   pen.setVisible(true);
		   return pen;
	}
	
	
	public void setTextOnTheText(int i,int pos, String s){
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
		t.setTextAt(pos,s);
	}

	public void clearAll(){
		/*
		int maxTextNumber=texts.size();
		for(int i=0;i<maxTextNumber;i++){
			ControlledTextArea t=(ControlledTextArea)(this.texts.elementAt(i));
			t.setText("");
		}
		*/
		edit_area.setText("");
		edit_area.setCaretPosition(0);
		console.setText("");
		console.setCaretPosition(0);
		console_log.setText("");
	    
		this.mouseClickedAtButton(stop_button.getID());
		run_time.setValue(0);
		this.fileFrame.setCommunicationNode(this.communicationNode);
		this.fileFrame.setWords();
	}

	public void sendAll()
	{
		// This method is derived from interface Spawnable
		// to do: code goes here
		int maxTextNumber=texts.size();
		for(int i=0;i<maxTextNumber;i++){
			ControlledTextArea t=(ControlledTextArea)(this.texts.elementAt(i));
			t.ControlledTextArea_textPasted();
		}
	}

	String commandName="pen";
	public String getCommandName(){
		return commandName;
	}

	public void sendEvent(String x){
//		System.out.println(x);
//		if(this.communicationNode==null) return;
//		this.communicationNode.sendEvent("pen",x);
		if(this.communicationNode==null) return;
		this.communicationNode.sendEvent(commandName,x);
	}
/*
	public boolean isShowingRmouse(){
		return super.isShowingRmouse();
	}
*/
	public void exitMouseOnTheText(int id, int x, int y){
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
		t.exitMouse();		
	}

	public void enterMouseOnTheText(int id, int x, int y){
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
		t.enterMouse(x,y);
	}

	public void mouseExitAtTheText(int id, int x, int y){
	}

	public void mouseEnteredAtTheText(int id, int x, int y){
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));

	}

	public void moveMouseOnTheText(int id, int x, int y){
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
		t.moveMouse(x,y);	
	}

	public void mouseMoveAtTextArea(int id, int x, int y){
	}

	public void pressMouseOnTheText(int i, int p, int x, int y){
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
		t.pressMouse(p,x,y);
	}

	public void mousePressedAtTextArea(int i,int p, int x, int y){
	}

	public void releaseMouseOnTheText(int id, int position, int x, int y){
	}

	public void dragMouseOnTheText(int id, int position, int x, int y){
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
		t.dragMouse(position,x,y);
	}

	public void mouseReleasedAtTextArea(int id, int position, int x, int y){
	}

	public void mouseDraggedAtTextArea(int id, int position, int x, int y){
	}

	public void clickMouseOnTheText(int i, int p, int x, int y){
		this.lastManipulatedTextArea=i;
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
		MouseEvent e=new MouseEvent(t,0,0,0,x,y,1,true);
		t.clickMouse(p,x,y);
		this.editAreaMouseListener.mouseClicked(e);
	}

	public void typeKey(int i, int p, int key){
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
//		t.requestFocus();
		KeyEvent e=new KeyEvent(t, 0, 0, 0, key);
//		t.setCaretPosition(p);
		e.setKeyChar((char)key);
	
		if(i==console.getID()){
			t.typeKey(p,key);
			consoleKeyListener.keyTyped(e);
		}
		else
		if(i==edit_area.getID()){
			if(key!=9)
			t.typeKey(p,key);
			editAreaKeyListener.keyTyped(e);
		}
		
	}

	public void mouseClickedAtTextArea(int i,int p, int x, int y){
		this.lastManipulatedTextArea=i;
		this.clickMouseOnTheText(i,p,x,y);
	}

	public void keyIsTypedAtATextArea(int i,int p,int key){
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
		t.requestFocus();
		KeyEvent e=new KeyEvent(t, 0, 0, 0, key);
//		t.setCaretPosition(p);
		e.setKeyChar((char)key);
		if(i==console.getID()){
			consoleKeyListener.keyTyped(e);
//			  this.typeKey(i,p,key);
		}
		if(i==edit_area.getID()){
/*			int ss=edit_area.getSelectionStart();
			int se=edit_area.getSelectionEnd();
			if(ss<se) this.typeKey(i,p,key);
			*/
			editAreaKeyListener.keyTyped(e);
		}
	}
	
	public void keyIsPressedAtATextArea(int i,int p,int key){
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
//		t.setCaretPosition(p);
		KeyEvent e=new KeyEvent(t, 0, 0, 0, key);
		e.setKeyCode(key);
		if(i==console.getID()){
			consoleKeyListener.keyPressed(e);
//			  this.typeKey(i,p,key);
		}
		if(i==edit_area.getID()){
			this.editAreaKeyListener.keyPressed(e);
		}
	}
	
	public void clickButton(int i){
		ControlledButton t=(ControlledButton)(buttons.elementAt(i));
		t.click();
//		if(i==10){
//			System.out.println("click button 10");
//		}
		this.mouseClickedAtButton(i);
	}

	public void unfocusButton(int i){
		SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//		  button.controlledButton_mouseExited(null);
		button.unFocus();
	}

	public void focusButton(int i){
		SelectedButton button=(SelectedButton)(buttons.elementAt(i));
//		  button.controlledButton_mouseEntered(null);
		button.focus();
	}

	public void mouseClickedAtButton(int i){
//		System.out.println("mouseClickedAtButton "+i);
		ControlledButton b=(ControlledButton)(buttons.elementAt(i));

		if(i==open_button.getID()){
			/*
			String separator=""+System.getProperty("file.separator");
			fileFrame.setListener(this);
			fileFrame.setCommonPath(
			   this.communicationNode.commonDataDir.toString()+
			   separator+"pen");
			fileFrame.setUserPath(
			   this.communicationNode.userDataDir.toString()+
			   separator+"pen");
			fileFrame.setVisible(true); 
			*/
			this.fileOpenClose();
			return;			
		}
		else
		if(i==new_button.getID()){
			ActionEvent e=new ActionEvent(b,0,"");
			this.newFileButtonListener.actionPerformed(e);
		}
		/*
		else
		if(i==save_button.getID()){
			ActionEvent e=new ActionEvent(b,0,"");
			this.newFileButtonListener.actionPerformed(e);
			
		}
		*/
		else
		if(i==run_button.getID()){
			ActionEvent e=new ActionEvent(b,0,"");
			this.RunButton.actionPerformed(e);
		}
		else
		if(i==step_button.getID()){
			ActionEvent e=new ActionEvent(b,0,"");
			this.RunButton.actionPerformed(e);			
		}
		else
		if(i==stop_button.getID()){
			ActionEvent e=new ActionEvent(b,0,"");
			this.RunButton.actionPerformed(e);			
		}
		else
		{
		   EditButtonListener l=(EditButtonListener)(editButtons.get(b));
		   if(l!=null){
			  l.actionPerformed(null);	
//			  sendPosition(edit_area.getID(),edit_area.getCaretPosition());
		   }           
		   
		}
		
	}

	public void mouseExitedAtButton(int i){
	}

	public void mouseEnteredAtButton(int i){
	}

	public void changeStateOnTheSlider(int id, int value){
		ControlledSlider s=(ControlledSlider)(this.sliders.elementAt(id));
		s.setValueX(value);
	}

	public void exitMouseOnTheSlider(int id){
	}

	public void enterMouseOnTheSlider(int id){
	}

	public void sliderStateChanged(int id, int value){
	}

	public void sliderMouseExited(int id){
	}

	public void sliderMouseEntered(int id){
	}

	public void changeScrollbarValue(int paneID, int barID, int value){
		ControlledScrollPane p=(ControlledScrollPane)(panes.elementAt(paneID));
		if(paneID==console.getID()||paneID==console_log.getID()){
//			if(executionPart.isRunning()) return;
		}
		p.setScrollBarValue(barID,value);
	}

	public void showScrollBar(int paneID, int barID){
	}

	public void hideScrollBar(int paneID, int barID){
	}

	public void scrollBarHidden(int paneID, int barId){
	}

	public void scrollBarShown(int paneID, int barID){
	}

	public void scrollBarValueChanged(int paneID, int barID, int v){
	}

	public void changeStateOnTheTabbedPane(int id, int value){
		ControlledTabbedPane p=(ControlledTabbedPane)(tabbedPanes.elementAt(id));
		p.setSelectedIndexX(value);
	}

	public void exitMouseOnTheTabbedPane(int id){
	}

	public void enterMouseOnTheTabbedPane(int id){
	}

	public void stateChangedAtTabbedPane(int id, int value){
	}

	public void mouseExitedAtTabbedPane(int id){
	}

	public void mouseEnteredAtTabbedPane(int id){
	}
	
	public Vector getDialogs(){
		return null;
	}

	public void sendFileDialogMessage(String m){
		sendEvent("file."+m);
	}

	public File getDefaultPath(){
		return null;
	}
	
	public void fileOpenClose(){
		String separator=""+System.getProperty("file.separator");
		fileFrame.setListener(this);
		fileFrame.setCommonPath(
		   this.communicationNode.commonDataDir.toString()+
		   separator+"pen");
		fileFrame.setUserPath(
		   this.communicationNode.userDataDir.toString()+
		   separator+"pen");
		fileFrame.setVisible(true); 
		
	}

	public void whenCancelButtonPressed(EditDialog d){
	}

	public void whenActionButtonPressed(EditDialog d){
		String dname=d.getDialogName();
		if(dname.equals("input common file name:")){
			String url="";
			String fileName=d.getText();
//			File commonDataPath=communicationNode.commonDataDir;
//			File thePath=new File(commonDataPath.getPath(),fileName);
			String separator=""+System.getProperty("file.separator");
			if(separator.equals("\\"))
//				url="file:///"+thePath.getPath();
				url="file:///"+fileName;
			else
//				  url="file://"+thePath.getPath();
				url="file://"+fileName;
//			urlField.setText(fileName);
			this.loadProgram(url);
			return;
		}
		if(dname.equals("input user file name:")){
			String url="";
			String fileName=d.getText();
			File userDataPath=this.communicationNode.userDataDir;
			if(!userDataPath.exists()) 
					 userDataPath.mkdir();
//			  File thePath=new File(userDataPath.getPath(),fileName);
			String separator=""+System.getProperty("file.separator");
			if(separator.equals("\\"))
//				url="file:///"+thePath.getPath();
				url="file:///"+fileName;
			else
//				  url="file://"+thePath.getPath();
				url="file://"+fileName;
//			urlField.setText(fileName);
			this.loadProgram(url);
			return;
		}
		if(dname.equals("output user file name:")){
			String url="";
			String fileName=d.getText();
			File userDataPath=this.communicationNode.userDataDir;   		
			if(!userDataPath.exists()) 
					 userDataPath.mkdir();
//			File thePath=new File(userDataPath.getPath(),fileName);
//			urlField.setText(fileName);
//			this.saveText(thePath.getPath());
			this.saveText(fileName);
			return;
		}
		if(dname.equals("url:")){
			String url=d.getText();
//			urlField.setText(url);
			this.loadProgram(url);
			return;
		}
	}
    
	void loadProgram(String urlname)
	{
		if(!this.communicationNode.isReadableFromEachFile()) return;
		BufferedReader inputStream=null;
		URL  url;
//		String codingCode=this.communicationNode.getEncodingCode();
		ControlledTextArea mes=console;

		int startTime=this.communicationNode.eventRecorder.timer.getMilliTime();
		if(urlname==null){
			mes.append("URL Name is expected.\n");
			return;
		}
//		mes.append("reading "+urlname+"\n");
		try{ url=new URL(urlname); }
		catch(MalformedURLException e)
		{  mes.append("MalformedURLException\n");
		return;
		}

		try{
			inputStream=//new DataInputStream(url.openStream());
				new BufferedReader(
					  new InputStreamReader(url.openStream()));
		}
		catch(UnsupportedEncodingException e){
			mes.append("encoding unsupported.\n");
			return;
		}
		catch(IOException e){
			mes.append("url open error.\n");
			return;
		}

		edit_area.setText("");
		int tlength=0;
		String x=null;
		do{
		   x=null;
		   try{
			 x=inputStream.readLine();
		   }
		   catch(java.io.IOException e){  break;}
		   if(x!=null){
			 x=x+"\n";
			 tlength=tlength+x.length();
			 edit_area.append(x);
		   }
		} while(x!=null);
  
		try{ inputStream.close();  }
		catch(IOException e)
		   { mes.append("read:IOException while closing.\n");}
//		mes.append("reading done.\n");
		int endTime=this.communicationNode.eventRecorder.timer.getMilliTime();
		int readingtime=endTime-startTime;
		String readingSpeed="";
		if(readingtime>0){
			readingSpeed=""+(((double)(tlength))*1000)/readingtime;
		}
		else readingSpeed="(na)";
//	  npMain.recordMessage("-"+startTime+":readfig("+urlname+") done.");
		this.recordMessage(""+startTime+
		",\"readfig("+urlname+") \""+","+readingtime+
		","+tlength+","+readingSpeed);

		this.sendAll();
	}
	public void saveText(String urlname)
	{
		ControlledTextArea mes=console;
		if(urlname==null) return;
		int startTime=this.communicationNode.eventRecorder.timer.getMilliTime();
//		mes.append("saving "+urlname+"\n");

		FileOutputStream fouts=null;

		try{ fouts= new FileOutputStream(new File(urlname));}
		catch(FileNotFoundException e){
			mes.append("wrong directory:"+urlname+" ?\n");
		}
		/*
		catch(IOException e){ mes.append("cannot access"+urlname+".\n");}
		*/
		String outx=edit_area.getText();
//		  byte[] buff=new byte[outx.length()];
		byte[] buff=null;
		buff=outx.getBytes();
//		  outx.getBytes(0,outx.length(),buff,0);

		try{
			fouts.write(buff);
			fouts.flush();
		}
		catch(IOException e)
		{ mes.append("save:IOExceptin while flushing.\n");}
           
		try{ fouts.close();  }
		catch(IOException e)
		{ mes.append("save:IOException while closing.\n");}

//		mes.append("Saving done.\n");
		int endTime=this.communicationNode.eventRecorder.timer.getMilliTime();
		int savingtime=endTime-startTime;
		double length=(double)(outx.length());
		String savingSpeed="";
		if(savingtime>0){
			savingSpeed=""+(length*1000)/savingtime;
		}
		else savingSpeed="(na)";
		this.recordMessage(""+startTime+
		",\"savefig("+urlname+") \","+savingtime+
		","+length+","+savingSpeed);
	}

	public void pasteAtTheText(int i)
	{
		String theText="";
		String newText="";
		int startPosition=0;
		int endPosition=0;
		ControlledTextArea ta=(ControlledTextArea)(this.texts.elementAt(i));
		startPosition=ta.getSelectionStart();
		theText=ta.getText();
//			  startPosition=ta.getSelectionStart();
		if(startPosition<0) startPosition=0;
		endPosition=ta.getSelectionEnd();
		if(endPosition<startPosition){
			int w=startPosition; startPosition=endPosition;
			endPosition=w;
		}
		if(endPosition>theText.length())
			   endPosition=theText.length();
		newText=theText.substring(0,startPosition);
		newText=newText+ communicationNode.tempText+
					 (ta.getText()).substring(endPosition);
		ta.setText(newText);
		return;
	}

	public void cutAnAreaOfTheText(int i)
	{
		String theText="";
		String newText="";
		int startPosition=0;
		int endPosition=0;
		ControlledTextArea ta=(ControlledTextArea)(this.texts.elementAt(i));
		startPosition=ta.getSelectionStart();
		theText=ta.getText();
		if(startPosition<0) startPosition=0;
		endPosition=ta.getSelectionEnd();
		if(endPosition<startPosition){
				int w=startPosition; startPosition=endPosition;
				endPosition=w;
		}
		if(endPosition>theText.length())
			   endPosition=theText.length();
		communicationNode.tempText=theText.substring(startPosition,endPosition);
		newText=theText.substring(0,startPosition);
		newText=newText+ theText.substring(endPosition);
		ta.setText(newText);
		return;
	}

	public void copyFromTheText(int i)
	{
		String theText="";
		String newText="";
		int startPosition=0;
		int endPosition=0;
		ControlledTextArea ta=(ControlledTextArea)(this.texts.elementAt(i));
			theText=ta.getText();
			startPosition=ta.getSelectionStart();
			if(startPosition<0) startPosition=0;
			endPosition=ta.getSelectionEnd();
			if(endPosition<startPosition){
				int w=startPosition; startPosition=endPosition;
				endPosition=w;
			}
			if(endPosition>theText.length())
			   endPosition=theText.length();
			this.communicationNode.tempText=theText.substring(startPosition,endPosition);
			return;
  }

  public void receiveEvent(String s)
  {
	  if(this.communicationNode==null) return;
//	  if(!this.communicationNode.isReceivingEvents) return;
	  if(!this.isReceiving()) return;
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
		  ParsePenEvent evParser=new ParsePenEvent(this,iq);
		  evParser.parsingString=s;
		  evParser.run();
  }
  
	public void exitThis()
	{
		this.mouseClickedAtButton(stop_button.getID());
		this.recordMessage("0,\"exit pen\"");
		this.setVisible(false);
	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#keyIsReleasedAtTextArea(int, int)
	 */
	public void keyIsReleasedAtTextArea(int id, int p, int code) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(id));
		t.setCaretPosition(p);
		KeyEvent e=new KeyEvent(t, 0, 0, 0, code);
		e.setKeyCode(code);
		if(id==console.getID()){
			consoleKeyListener.keyReleased(e);
//			  this.typeKey(i,p,key);
		}
		if(id==edit_area.getID()){
			this.editAreaKeyListener.keyReleased(e);
		}

	}

	/* (非 Javadoc)
	 * @see controlledparts.FrameWithControlledTextAreas#releaseKey(int, int)
	 */
	public void releaseKey(int i, int p, int code) {
		// TODO 自動生成されたメソッド・スタブ
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
		t.setCaretPosition(p);
		KeyEvent e=new KeyEvent(t, 0, 0, 0, code);
		e.setKeyCode(code);
		if(i==console.getID()){
			consoleKeyListener.keyReleased(e);
		}
		else
		if(i==edit_area.getID()){
			editAreaKeyListener.keyReleased(e);
		}

	}
	public void pressKey(int i, int p, int code){
		ControlledTextArea t=(ControlledTextArea)(texts.elementAt(i));
//		t.setCaretPosition(p);
        t.requestFocus();
		KeyEvent e=new KeyEvent(t, 0, 0, 0, code);
		e.setKeyCode(code);
		if(i==console.getID()){
			consoleKeyListener.keyPressed(e);
//			t.pressKey(p,code);			
		}
		else
		if(i==edit_area.getID()){
			editAreaKeyListener.keyPressed(e);
//			t.pressKey(p,code);
		}
	}

	public void showHelpPage(){	
		browser=(WebFrame)
			(this.communicationNode.applicationManager.spawnApplication2("WebFrame",
		  "local"));
		if(browser==null) return;

//		 this.pageArea.setEditable(false);
		   String cdd=this.communicationNode.commonDataDir.toString();
		   String sep=""+System.getProperty("file.separator");
		   String initPage="";
		   String urlHead="file:";
		   if(sep.equals("\\")){
				urlHead=urlHead+"///";
		   }
		   else {
				urlHead=urlHead+"//";
		   }
		   initPage=urlHead+cdd+sep+"html"+sep+"pen.html";
		   browser.startLoading(initPage);
	}

	public void clickMenu(int i) {
		// TODO 自動生成されたメソッド・スタブ
		   ControlledMenu m=(ControlledMenu)(menus.elementAt(i));
		   m.click();
		   /*
		   ActionEvent e=new ActionEvent(m,0,"");
		   ActionListener[] ls=m.getActionListeners();
		   int asize=ls.length;
		   for(int j=0;j<asize;j++){
			   ActionListener l=ls[j];
			   l.actionPerformed(e);
		   }
		   */
	}

	public void enterMenu(int i) {
		// TODO 自動生成されたメソッド・スタブ
		   ControlledMenu m=(ControlledMenu)(menus.elementAt(i));
		   m.focus();
		
	}

	public void exitMenu(int i) {
		// TODO 自動生成されたメソッド・スタブ
		   ControlledMenu m=(ControlledMenu)(menus.elementAt(i));
		   m.unFocus();
		
	}

	public void mouseClickedAtMenu(int i) {
		// TODO 自動生成されたメソッド・スタブ
		/*
		   ControlledMenu m=(ControlledMenu)(menus.elementAt(i));
		   ActionEvent e=new ActionEvent(m,0,"");
		   ActionListener[] ls=m.getActionListeners();
		   int asize=ls.length;
		   for(int j=0;j<asize;j++){
			   ActionListener l=ls[j];
			   l.actionPerformed(e);
		   }
		*/
	}

	public void mouseEnteredAtMenu(int i) {
		// TODO 自動生成されたメソッド・スタブ
		/*
		   ControlledMenu m=(ControlledMenu)(menus.elementAt(i));
		   m.focus();
		   */
	}

	public void mouseExitedAtMenu(int i) {
		// TODO 自動生成されたメソッド・スタブ
		/*
		   ControlledMenu m=(ControlledMenu)(menus.elementAt(i));
		   m.unFocus();
		   */
	}

	public void clickMenuItem(int i) {
		// TODO 自動生成されたメソッド・スタブ
		   ControlledMenuItem m=(ControlledMenuItem)(menuItems.elementAt(i));
		   m.click();
//    		this.mouseClickedAtMenuItem(i);
	}

	public void enterMenuItem(int i) {
		// TODO 自動生成されたメソッド・スタブ
		   ControlledMenuItem m=(ControlledMenuItem)(menuItems.elementAt(i));
           m.focus();		
	}

	public void exitMenuItem(int i) {
		// TODO 自動生成されたメソッド・スタブ
		   ControlledMenuItem m=(ControlledMenuItem)(menuItems.elementAt(i));
           m.unFocus();		
	}

	public void mouseClickedAtMenuItem(int i) {
		// TODO 自動生成されたメソッド・スタブ
		/*
		   ControlledMenuItem m=(ControlledMenuItem)(menuItems.elementAt(i));
		*/
		/*
		   ControlledMenuItem m=(ControlledMenuItem)(menuItems.elementAt(i));
		      ActionEvent e=new ActionEvent(m,0,"");
		      ActionListener[] ls=m.getActionListeners();
		      int asize=ls.length;
		      for(int j=0;j<asize;j++){
			     ActionListener l=ls[j];
			     if(m.getMyActionListener()!=l)
			     l.actionPerformed(e);
		      }
		      */
	}

	public void mouseEnteredAtMenuItem(int i) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void mouseExitedAtMenuItem(int i) {
		// TODO 自動生成されたメソッド・スタブ
		
	}	
	public void sendPosition(int i,int x){
		this.sendEvent("pos("+i+","+x+")\n");
	}
	public void setPosition(int i, int x){
		ControlledTextArea t=(ControlledTextArea)(this.texts.elementAt(i));
		t.setCaretPosition(x);
	}
		
}