package application.pen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

/**
 * ���j���[ -> �ҏW -> ���s��ʂ��R�s�[
 * ���������Ă���N���X
 * 
 * @author rn_magi
 */
public class ConsoleCopyButtonListener implements ActionListener {
	private JTextArea console;
	
	public ConsoleCopyButtonListener(JTextArea cn){
		console		= cn;
	}
	
	public void actionPerformed(ActionEvent e) {
		new ClipBoard(console.getText());
	}

}
