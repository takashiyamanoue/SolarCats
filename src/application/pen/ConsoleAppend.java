package application.pen;
/**
 * �R���\�[����ʂ́u���s��ʁv�Ɓu�����v�ɕ������ǉ�����N���X
 * 
 * @author rn_magi
 */
public class ConsoleAppend {
	
	/**
	 * @param append
	 * �R���\�[����ʂ֒ǉ����镶����
	 */
	public ConsoleAppend(String append){
		MainGUI.console.append(append);
		MainGUI.console.setCaretPosition(MainGUI.console.getText().length());
		MainGUI.console_log.append(append);
		MainGUI.console_log.setCaretPosition(MainGUI.console_log.getText().length());
	}
}
