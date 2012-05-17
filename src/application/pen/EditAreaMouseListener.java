package application.pen;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextArea;


public class EditAreaMouseListener implements MouseListener {
	private JTextArea edit_area;
	
	public EditAreaMouseListener(JTextArea ea){
		edit_area = ea;
	}
	
	public void mouseClicked(MouseEvent e) {
		new EditSelection(edit_area);
	}

	public void mouseEntered(MouseEvent e) {
	}
	
	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
