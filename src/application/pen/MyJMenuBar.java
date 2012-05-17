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
	
//	private JMenu FileMenu = new MyJMenu("�t�@�C��");
	public JMenu FileMenu = new MyJMenu("�t�@�C��");
//	private JMenu EditMenu = new MyJMenu("�ҏW");
	public JMenu EditMenu = new MyJMenu("�ҏW");
//	private JMenu HelpMenu = new MyJMenu("�w���v");
	public JMenu HelpMenu = new MyJMenu("�w���v");
	
//	private JMenuItem NewFileMenuItem		= new MyJMenuItem("�V�K");
	public ControlledMenuItem NewFileMenuItem		= new MyJMenuItem("�V�K");
//	private JMenuItem FileOpenMenuItem		= new MyJMenuItem("�J��");
//	public JMenuItem FileOpenMenuItem		= new MyJMenuItem("�J��");
	public ControlledMenuItem FileOpenMenuItem		= new MyJMenuItem("�J��/�ۑ�����");
//	private JMenuItem FileSaveMenuItem		= new MyJMenuItem("�㏑���ۑ�");
//	private JMenuItem FileReSaveMenuItem	= new MyJMenuItem("���O��t���ĕۑ�");
//	public JMenuItem FileReSaveMenuItem	= new MyJMenuItem("���O��t���ĕۑ�");
//	private JMenuItem ExitMenuItem			= new MyJMenuItem("PEN���I������");
	public ControlledMenuItem ExitMenuItem			= new MyJMenuItem("PEN���I������");
	
//	private JMenuItem UndoMenuItem			= new MyJMenuItem("���ɖ߂�");
	public ControlledMenuItem UndoMenuItem			= new MyJMenuItem("���ɖ߂�");
//	private JMenuItem RedoMenuItem			= new MyJMenuItem("��蒼��");
	public ControlledMenuItem RedoMenuItem			= new MyJMenuItem("��蒼��");
//	private JMenuItem CutMenuItem			= new MyJMenuItem("�؂���"	, new CutAction());
	public ControlledMenuItem CutMenuItem			= new MyJMenuItem("�؂���"	, new CutAction());
//	private JMenuItem CopyMenuItem			= new MyJMenuItem("�R�s�["	, new CopyAction());
	public ControlledMenuItem CopyMenuItem			= new MyJMenuItem("�R�s�["	, new CopyAction());
//	private JMenuItem PasteMenuItem		= new MyJMenuItem("�\��t��"	, new PasteAction());
	public ControlledMenuItem PasteMenuItem		= new MyJMenuItem("�\��t��"	, new PasteAction());
//	private JMenuItem ConsoleCopyMenuItem	= new MyJMenuItem("���s��ʂ��R�s�[");
	public ControlledMenuItem ConsoleCopyMenuItem	= new MyJMenuItem("���s��ʂ��R�s�[");
//	private JMenuItem VarCopyMenuItem		= new MyJMenuItem("�ϐ���ʂ��R�s�[");
	public ControlledMenuItem VarCopyMenuItem		= new MyJMenuItem("�ϐ���ʂ��R�s�[");
	
//	private JMenuItem HelpPenMenuItem			= new MyJMenuItem("PEN�ɂ���");
	public ControlledMenuItem HelpPenMenuItem			= new MyJMenuItem("PEN�ɂ���");
	
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
