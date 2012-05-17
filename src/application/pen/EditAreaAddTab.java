package application.pen;
import javax.swing.text.BadLocationException;

/**
 * �C���f���g�̌��𐔂��ǉ����ׂ��C���f���g��Ԃ��܂�
 * 
 * @author rn_magi
 */
public class EditAreaAddTab {	
	/**
	 * @param start
	 * �C���f���g�𐔂���s���̃I�t�Z�b�g
	 * @param end
	 * �C���f���g�𐔂���s���̃I�t�Z�b�g
	 * @return
	 * �ǉ����ׂ��C���f���g��Ԃ��܂�
	 */
	public String AddTab(int start, int end){
		String add = "";
		try {
			for(int i = start; i < end; i++){
				String str = MainGUI.edit_area.getText(i,1);
				if(str.equals("�b") || str.equals("|") || str.equals(" ") || str.equals("�@")){
					if(str.equals("�b")){
						add += "  | ";
					}else if(str.equals("|")){
						add += "  | ";
					}
				}else{
					break;
				}
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return add;
	}
}
