package application.pen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class HelpPenButtonListener implements ActionListener {
	private String str;
	private JLabel info;
    private MainGUI gui;	
	public HelpPenButtonListener(String sn, String ver, MainGUI g){
		str =	"<html><pre>" +
				"���w�Ҍ����̃v���O���~���O�w�K�� PEN " + ver + "\n" +
				"" + "\n" +
				"PEN�͏��w�Ҍ����̃v���O���~���O�w�K���ł��B" + "\n" +
				"PEN�̋L�q����́A" + "\n" +
				"  ��w�����Z���^�[�̃A���S���Y���L�q���� DNCL �����" + "\n" +
				"  �����_�H��̓����p�A���S���Y���L�q���� TUATLE ��" + "\n" +
				"�������Ă��܂��B" + "\n" +
				"" + "\n" +
				"�A���� : pen@s.osaka-gu.ac.jp" + "\n" +
				"URL    : http://www.media.osaka-cu.ac.jp/PEN/" + "\n" +
				"" + "\n" +
				"Copyright 2003-2005" + "\n" +
				"���� ���� : ���s����w��w�@ �n���s�s������" + "\n" +
				"���c �m�� : ���w�@��w ���w��" + "\n" +
				"���Y �q�Y : ���s����w��w�@ �n���s�s������" + "\n" +
				"</pre></html>";
		
		info = new MyJLabel(str);
		// 2006 3.23
		gui=g;
	}
	
	public void actionPerformed(ActionEvent e) {
//		JOptionPane.showMessageDialog(null, info, "PEN �ɂ���", JOptionPane.INFORMATION_MESSAGE);
		gui.showHelpPage();
	}
}
