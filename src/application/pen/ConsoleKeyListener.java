package application.pen;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Position;

/**
 * プログラム実行時の入力処理を行うクラス
 * 
 * @author rn_magi
 */
class ConsoleKeyListener implements KeyListener{
	static String input_line;
	static int start_offs = 0;
		
	public void keyPressed(KeyEvent e){
		if(MainGUI.Run_flag){
			switch(e.getKeyCode()){
				case KeyEvent.VK_ENTER :
					try {
						int get_line	= MainGUI.console.getLineCount();
						//int start_offs	= MainGUI.console.getLineStartOffset(get_line - 1);
						int end_offs	= MainGUI.console.getLineEndOffset(get_line - 1);
						input_line		= MainGUI.console.getText(start_offs , end_offs - start_offs);
						MainGUI.Input_flag = false;
					} catch (BadLocationException f) {
						f.printStackTrace();
					}
					break;
				default:
					try {
						int console_line = MainGUI.console.getLineCount();
						int key_line = MainGUI.console.getLineOfOffset(MainGUI.console.getSelectionStart()) + 1;
						if(console_line != key_line){
							Document doc		= MainGUI.console.getDocument();
							Position EndPos	= doc.getEndPosition();
							int pos		= EndPos.getOffset();
							MainGUI.console.getCaret().setDot(pos);
							MainGUI.console.requestFocus();
						}
					} catch (BadLocationException f) {
						f.printStackTrace();
					}
					break;
			}
		}
	}
	public void keyReleased(KeyEvent e) {
		
	}
	public void keyTyped(KeyEvent e) {		
	}
}

