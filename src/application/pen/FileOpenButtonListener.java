package application.pen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class FileOpenButtonListener implements ActionListener{
	private FileOpen file_open;
	MainGUI gui;
	public FileOpenButtonListener(JFileChooser fc, MainGUI mw, JTextArea ea, String wn){
//		file_open = new FileOpen(fc, mw, ea, wn);
		gui=mw;
	}
	
	public void actionPerformed(ActionEvent e) {
		/*
		if(file_open.FileOpenConfirm()){
			file_open.FileChooser();
		}
		*/
		gui.fileOpenClose();
	}
}
