package application.pen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

class NewFileButtonListener implements ActionListener{
	private JFrame window;
	private JTextArea edit_area;
	private String WindowName;
	
	private String[] obj		= {"編集画面を初期化しますか？"};
	private String[] obj2		= {"初期化する前にプログラムを保存しますか？"};

	private String[] option	= { "初期化", "取り消し"};
	private String[] option2	= { "保存", "破棄", "取り消し" };
	
	public NewFileButtonListener(JFrame mw, JTextArea ea, String wn){
		window	= mw;
		edit_area	= ea;
		WindowName	= wn;
	}
	
	public void actionPerformed(ActionEvent e) {
		/*
		java.awt.Toolkit.getDefaultToolkit().beep();
		int retValue = JOptionPane.showOptionDialog(window, obj,"", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null,option,option[0]);
		if(retValue==JOptionPane.YES_OPTION){
			String window_name = window.getTitle();
			if(window_name.substring(0, 1).equals("*")) {
				java.awt.Toolkit.getDefaultToolkit().beep();
				retValue = JOptionPane.showOptionDialog(window, obj2,"", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null,option2,option2[0]);
				if(retValue==JOptionPane.YES_OPTION){
					int returnVal = MainGUI.fc.showSaveDialog(window);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						new FileSave(MainGUI.fc.getSelectedFile(), edit_area, MainGUI.fc, window);
						NewFile();
					}else{
					}
				}else if(retValue==JOptionPane.NO_OPTION){
					NewFile();
				}else if(retValue==JOptionPane.CANCEL_OPTION){
				}
			}else{
				NewFile();
			}
		}else if(retValue==JOptionPane.NO_OPTION){
		}
		*/
		NewFile();
		edit_area.requestFocus();
	}
	
	public void NewFile(){
		new RunClean();
		edit_area.setText("");
		window.setTitle("NewFile - " + WindowName);
	}
}
