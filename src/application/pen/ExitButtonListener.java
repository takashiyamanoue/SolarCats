package application.pen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import controlledparts.*;

public class ExitButtonListener  implements ActionListener{
	private JFileChooser file_c;
//	private JFrame window;
	private ControlledFrame window;
	private JTextArea edit_area;
	
	private String[] obj = {"終了する前にプログラムを保存しますか？"};
	private String[] option = { "保存", "破棄", "取り消し" };
	
	public ExitButtonListener(JFileChooser fc, //JFrame mw, 
			ControlledFrame mw,
			JTextArea ea){
		file_c		= fc;
		window		= mw;
		edit_area	= ea;
	}

	public void actionPerformed(ActionEvent e) {
		/*
		String window_name = window.getTitle();
		if(window_name.substring(0, 1).equals("*")){
			java.awt.Toolkit.getDefaultToolkit().beep();
			int retValue = JOptionPane.showOptionDialog(window, obj,"", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null,option,option[0]);
			if(retValue==JOptionPane.YES_OPTION){
				int returnVal = MainGUI.fc.showSaveDialog(window);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					new FileSave(file_c.getSelectedFile(), edit_area);
					System.exit(0);
				}
			}else if(retValue==JOptionPane.NO_OPTION){
				System.exit(0);
			}
		}else{
			System.exit(0);
		}
		*/
		window.exitThis();
	}
}
