package application.pen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

public class ConsoleCopyButtonListenerX implements ActionListener {
	private JTextArea console;
	
	public ConsoleCopyButtonListenerX(JTextArea cn){
		console		= cn;
	}
	public void actionPerformed(ActionEvent e) {
		new ClipBoard(console.getText());
	}

}
