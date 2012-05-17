package application.pen;
/**
 * コンソール画面の「実行画面」と「履歴」に文字列を追加するクラス
 * 
 * @author rn_magi
 */
public class ConsoleAppend {
	
	/**
	 * @param append
	 * コンソール画面へ追加する文字列
	 */
	public ConsoleAppend(String append){
		MainGUI.console.append(append);
		MainGUI.console.setCaretPosition(MainGUI.console.getText().length());
		MainGUI.console_log.append(append);
		MainGUI.console_log.setCaretPosition(MainGUI.console_log.getText().length());
	}
}
