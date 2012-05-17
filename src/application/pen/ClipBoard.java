package application.pen;
import java.awt.*;
import java.awt.datatransfer.*;

/**
 *�V�X�e���̃N���b�v�{�[�h�ɓn���ꂽ��������i�[����N���X
 *
 * @author rn_magi
 */
public class ClipBoard implements ClipboardOwner {
	
	/**
	 * <code>clipboard</code> �̃R�����g
	 * �N���b�v�{�[�h�N���X�̏�����
	 */
	private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

	/**
	 * @param data
	 * �N���b�v�{�[�h�Ɋi�[���镶����
	 */
	public ClipBoard(String data){
		StringSelection contents = new StringSelection(data);
		clipboard.setContents(contents, this);
	}

	public void lostOwnership(Clipboard clipboard, Transferable contents) {
	}
}
