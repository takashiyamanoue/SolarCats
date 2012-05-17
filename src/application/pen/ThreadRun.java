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
				new ConsoleAppend("### �s���ȕ��������͂���܂���\n");
			}  catch (NullPointerException f) {
				MainGUI.Bug_flag = true;
				new ConsoleAppend("### �z��͈̔͂��m�F���Ă�������\n");
			} catch (ArithmeticException f){
				MainGUI.Bug_flag = true;
				new ConsoleAppend("### ���Z�̃G���[�ł�\n");
				new ConsoleAppend("### �[���ŏ��Z���Ă��܂��񂩁H\n");
			} catch (StringIndexOutOfBoundsException f){
				MainGUI.Bug_flag = true;
				new ConsoleAppend("### �����񑀍�Ɏ��s���܂���\n");
				new ConsoleAppend("### " + f.getLocalizedMessage() + "\n");
			} catch (ArrayIndexOutOfBoundsException f){
				MainGUI.Bug_flag = true;
				new ConsoleAppend("### �z��̃T�C�Y�ȏ���w�肵�܂���\n");
				new ConsoleAppend("### " + f.getLocalizedMessage() + "\n");
			} catch (Exception f) {
				MainGUI.Bug_flag = true;
				new ConsoleAppend("### �����s���̎��s�G���[�ł�\n");
				new ConsoleAppend("### �G���[�R�[�h�F" + f + "\n");
			}
		} catch (ParseException f){
			MainGUI.Bug_flag = true;
			new ConsoleAppend("### �R���p�C���ł��܂���ł���\n");
		} catch (ThreadRunStop f){
			MainGUI.Bug_flag = true;
			if(MainGUI.Run_flag){
				new ConsoleAppend("### �����s���̃G���[�ł�\n");
				new ConsoleAppend("### �G���[�R�[�h�F" + f + "\n");
			}
		} catch (TokenMgrError f){
			MainGUI.Bug_flag = true;
			new ConsoleAppend("### Token�G���[�ł�\n");
			new ConsoleAppend("### �G���[�R�[�h�F" + f + "\n");
		} catch (Exception f) {
			MainGUI.Bug_flag = true;
			new ConsoleAppend("### �����s���̃G���[�ł�\n");
			new ConsoleAppend("### �G���[�R�[�h�F" + f + "\n");
		} catch (Error f){
			MainGUI.Bug_flag = true;
			new ConsoleAppend("### �����s���̃G���[�ł�\n");
			new ConsoleAppend("### �G���[�R�[�h�F" + f + "\n");
		}
		
		SwingUtilities.invokeLater(new MyRunnable());
	}
}