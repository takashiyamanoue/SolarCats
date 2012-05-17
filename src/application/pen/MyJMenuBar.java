package application.pen;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.text.DefaultEditorKit.CopyAction;
import javax.swing.text.DefaultEditorKit.CutAction;
import javax.swing.text.DefaultEditorKit.PasteAction;
import controlledparts.*;

public class MyJMenuBar extends JMenuBar{
	private JFileChooser file_c;
//	private JFrame window;
	private MainGUI window;
	private JTextArea edit_area;
	private JTextArea console;
	private JTable var_table;
	private String WindowName;
	private String SystemName;
	private String Version;
	private EditAreaUndoableEditListener undo;
	
	private JMenuBar MenuBar = new JMenuBar();
	
//	private JMenu FileMenu = new MyJMenu("ファイル");
	public JMenu FileMenu = new MyJMenu("ファイル");
//	private JMenu EditMenu = new MyJMenu("編集");
	public JMenu EditMenu = new MyJMenu("編集");
//	private JMenu HelpMenu = new MyJMenu("ヘルプ");
	public JMenu HelpMenu = new MyJMenu("ヘルプ");
	
//	private JMenuItem NewFileMenuItem		= new MyJMenuItem("新規");
	public ControlledMenuItem NewFileMenuItem		= new MyJMenuItem("新規");
//	private JMenuItem FileOpenMenuItem		= new MyJMenuItem("開く");
//	public JMenuItem FileOpenMenuItem		= new MyJMenuItem("開く");
	public ControlledMenuItem FileOpenMenuItem		= new MyJMenuItem("開く/保存する");
//	private JMenuItem FileSaveMenuItem		= new MyJMenuItem("上書き保存");
//	private JMenuItem FileReSaveMenuItem	= new MyJMenuItem("名前を付けて保存");
//	public JMenuItem FileReSaveMenuItem	= new MyJMenuItem("名前を付けて保存");
//	private JMenuItem ExitMenuItem			= new MyJMenuItem("PENを終了する");
	public ControlledMenuItem ExitMenuItem			= new MyJMenuItem("PENを終了する");
	
//	private JMenuItem UndoMenuItem			= new MyJMenuItem("元に戻す");
	public ControlledMenuItem UndoMenuItem			= new MyJMenuItem("元に戻す");
//	private JMenuItem RedoMenuItem			= new MyJMenuItem("やり直し");
	public ControlledMenuItem RedoMenuItem			= new MyJMenuItem("やり直し");
//	private JMenuItem CutMenuItem			= new MyJMenuItem("切り取り"	, new CutAction());
	public ControlledMenuItem CutMenuItem			= new MyJMenuItem("切り取り"	, new CutAction());
//	private JMenuItem CopyMenuItem			= new MyJMenuItem("コピー"	, new CopyAction());
	public ControlledMenuItem CopyMenuItem			= new MyJMenuItem("コピー"	, new CopyAction());
//	private JMenuItem PasteMenuItem		= new MyJMenuItem("貼り付け"	, new PasteAction());
	public ControlledMenuItem PasteMenuItem		= new MyJMenuItem("貼り付け"	, new PasteAction());
//	private JMenuItem ConsoleCopyMenuItem	= new MyJMenuItem("実行画面をコピー");
	public ControlledMenuItem ConsoleCopyMenuItem	= new MyJMenuItem("実行画面をコピー");
//	private JMenuItem VarCopyMenuItem		= new MyJMenuItem("変数画面をコピー");
	public ControlledMenuItem VarCopyMenuItem		= new MyJMenuItem("変数画面をコピー");
	
//	private JMenuItem HelpPenMenuItem			= new MyJMenuItem("PENについて");
	public ControlledMenuItem HelpPenMenuItem			= new MyJMenuItem("PENについて");
	
	public MyJMenuBar(JFileChooser fc, MainGUI mw, JTextArea ea, JTextArea cs, JTable vt, String sn, String ver, EditAreaUndoableEditListener ud){
		file_c		= fc;
		window		= mw;
		edit_area	= ea;
		console		= cs;
		var_table	= vt;
		SystemName	= sn;
		Version		= ver;
		WindowName	= SystemName + " " + Version;
		undo		= ud;
	}
	
	public JMenuBar createMenuBar(){
//		NewFileMenuItem.addActionListener(new NewFileButtonListener(window,edit_area,WindowName));
		FileOpenMenuItem.addActionListener(new FileOpenButtonListener(file_c, window, edit_area, WindowName));
//		FileReSaveMenuItem.addActionListener(new FileSaveButtonListener(file_c, window, edit_area, WindowName));
		ExitMenuItem.addActionListener(new ExitButtonListener(file_c, window, edit_area));
		
		UndoMenuItem.addActionListener(undo);
		RedoMenuItem.addActionListener(undo);
		
		ConsoleCopyMenuItem.addActionListener(new ConsoleCopyButtonListener(console));
		VarCopyMenuItem.addActionListener(new VarCopyButtonListener(var_table));

		HelpPenMenuItem.addActionListener(new HelpPenButtonListener(SystemName, Version, window));// add window, 2006 3.23
		
		FileMenu.add(NewFileMenuItem);
		FileMenu.add(FileOpenMenuItem);
		//FileMenu.add(FileSaveMenuItem);
//		FileMenu.add(FileReSaveMenuItem);
		FileMenu.add(new JSeparator());
		FileMenu.add(ExitMenuItem);

		EditMenu.add(UndoMenuItem);
		EditMenu.add(RedoMenuItem);
		EditMenu.add(new JSeparator());
		EditMenu.add(CutMenuItem);
		EditMenu.add(CopyMenuItem);
		EditMenu.add(PasteMenuItem);
		EditMenu.add(new JSeparator());
		EditMenu.add(ConsoleCopyMenuItem);
		EditMenu.add(VarCopyMenuItem);
		
		HelpMenu.add(HelpPenMenuItem);
		
		MenuBar.add(FileMenu);
		MenuBar.add(EditMenu);
		MenuBar.add(HelpMenu);

		return MenuBar;
	}

	public void MysetEnabled(boolean f){
		NewFileMenuItem.setEnabled(f);
		FileOpenMenuItem.setEnabled(f);
//		FileReSaveMenuItem.setEnabled(f);
		ExitMenuItem.setEnabled(f);
		
		UndoMenuItem.setEnabled(f);
		RedoMenuItem.setEnabled(f);
		CutMenuItem.setEnabled(f);
		CopyMenuItem.setEnabled(f);
		PasteMenuItem.setEnabled(f);
		ConsoleCopyMenuItem.setEnabled(f);
		VarCopyMenuItem.setEnabled(f);
	}
	
	public void LogCopy(boolean f){
		ConsoleCopyMenuItem.setEnabled(f);
		VarCopyMenuItem.setEnabled(f);
	}
}
