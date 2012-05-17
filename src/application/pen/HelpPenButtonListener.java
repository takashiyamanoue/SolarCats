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
				"初学者向けのプログラミング学習環境 PEN " + ver + "\n" +
				"" + "\n" +
				"PENは初学者向けのプログラミング学習環境です。" + "\n" +
				"PENの記述言語は、" + "\n" +
				"  大学入試センターのアルゴリズム記述言語 DNCL および" + "\n" +
				"  東京農工大の入試用アルゴリズム記述言語 TUATLE に" + "\n" +
				"準拠しています。" + "\n" +
				"" + "\n" +
				"連絡先 : pen@s.osaka-gu.ac.jp" + "\n" +
				"URL    : http://www.media.osaka-cu.ac.jp/PEN/" + "\n" +
				"" + "\n" +
				"Copyright 2003-2005" + "\n" +
				"中村 亮太 : 大阪市立大学大学院 創造都市研究科" + "\n" +
				"西田 知博 : 大阪学院大学 情報学部" + "\n" +
				"松浦 敏雄 : 大阪市立大学大学院 創造都市研究科" + "\n" +
				"</pre></html>";
		
		info = new MyJLabel(str);
		// 2006 3.23
		gui=g;
	}
	
	public void actionPerformed(ActionEvent e) {
//		JOptionPane.showMessageDialog(null, info, "PEN について", JOptionPane.INFORMATION_MESSAGE);
		gui.showHelpPage();
	}
}
