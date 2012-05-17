package application.pen;
import java.io.StringReader;
import javax.swing.SwingUtilities;

class ThreadRun extends Thread {
	public void run() {
		String str = MainGUI.edit_area.getText() + "\n";

		try {
			IntVParser parser = new IntVParser(new StringReader(str));
			if(MainGUI.Debug_flag) {
				parser.enable_tracing();
			} else {
				parser.disable_tracing();
			}
			parser.IntVUnit();

			try {
				IntVExecuter visitor = new IntVExecuter();
				if(MainGUI.Debug_flag) { visitor.NodeDump(); }
				parser.jjtree.rootNode().jjtAccept(visitor, null);
			} catch (NumberFormatException f){
				MainGUI.Bug_flag = true;
				new ConsoleAppend("### 不正な文字が入力されました\n");
			}  catch (NullPointerException f) {
				MainGUI.Bug_flag = true;
				new ConsoleAppend("### 配列の範囲を確認してください\n");
			} catch (ArithmeticException f){
				MainGUI.Bug_flag = true;
				new ConsoleAppend("### 演算のエラーです\n");
				new ConsoleAppend("### ゼロで除算していませんか？\n");
			} catch (StringIndexOutOfBoundsException f){
				MainGUI.Bug_flag = true;
				new ConsoleAppend("### 文字列操作に失敗しました\n");
				new ConsoleAppend("### " + f.getLocalizedMessage() + "\n");
			} catch (ArrayIndexOutOfBoundsException f){
				MainGUI.Bug_flag = true;
				new ConsoleAppend("### 配列のサイズ以上を指定しました\n");
				new ConsoleAppend("### " + f.getLocalizedMessage() + "\n");
			} catch (Exception f) {
				MainGUI.Bug_flag = true;
				new ConsoleAppend("### 原因不明の実行エラーです\n");
				new ConsoleAppend("### エラーコード：" + f + "\n");
			}
		} catch (ParseException f){
			MainGUI.Bug_flag = true;
			new ConsoleAppend("### コンパイルできませんでした\n");
		} catch (ThreadRunStop f){
			MainGUI.Bug_flag = true;
			if(MainGUI.Run_flag){
				new ConsoleAppend("### 原因不明のエラーです\n");
				new ConsoleAppend("### エラーコード：" + f + "\n");
			}
		} catch (TokenMgrError f){
			MainGUI.Bug_flag = true;
			new ConsoleAppend("### Tokenエラーです\n");
			new ConsoleAppend("### エラーコード：" + f + "\n");
		} catch (Exception f) {
			MainGUI.Bug_flag = true;
			new ConsoleAppend("### 原因不明のエラーです\n");
			new ConsoleAppend("### エラーコード：" + f + "\n");
		} catch (Error f){
			MainGUI.Bug_flag = true;
			new ConsoleAppend("### 原因不明のエラーです\n");
			new ConsoleAppend("### エラーコード：" + f + "\n");
		}
		
		SwingUtilities.invokeLater(new MyRunnable());
	}
}