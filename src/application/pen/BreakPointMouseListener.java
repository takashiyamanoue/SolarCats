package application.pen;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextArea;

/**
 * JTextArea内 の何行でマウスイベントが発生したかを調べるクラス
 * 
 * @author rn_magi
 */
public class BreakPointMouseListener implements MouseListener {
	JTextArea breakpoint;
	
	public BreakPointMouseListener(JTextArea bp){
		breakpoint = bp;
	}
	
	public void mouseClicked(MouseEvent e) {
	//	System.out.print("Click:");
	//	System.out.println(e.getY() / 16 + 1);
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
