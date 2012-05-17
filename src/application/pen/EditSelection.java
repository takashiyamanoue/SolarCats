package application.pen;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class EditSelection {
	private String l = "á";
	private String r = "â";
	private JTextArea edit_area;
	
	public EditSelection(JTextArea ea){	
		edit_area = ea;
		int pos = edit_area.getCaretPosition();
		selection(pos);
	}
	
	public EditSelection(JTextArea ea, int move){	
		edit_area = ea;
		int pos = edit_area.getCaretPosition() + move;
		if(pos > 0){
			selection(pos);
		}
	}
	
	public void selection(int pos){
		int l_pos = -1;
		int r_pos = -1;
		int end_pos = 0;
		String temp;
		
		try {
			end_pos = edit_area.getLineEndOffset(edit_area.getLineCount() - 1);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		
		for(int x = 1; x < 5 && pos - x >= 0; x++){
			try {
			    temp = edit_area.getText(pos - x, 1);
				if(l.equals(temp)){
					l_pos = pos - x;
					break;
				}else if(r.equals(temp)){
					break;
				}
			} catch (BadLocationException f) {
				f.printStackTrace();
			}
		}
		if(l_pos != -1){
			for(int x = 0; x < 4 && pos + x <= end_pos; x++){
				try {
					if(r.equals(edit_area.getText(pos + x, 1))){
						r_pos = pos + x + 1;
						break;
					}
				} catch (BadLocationException f) {
					f.printStackTrace();
				}
			}

			if(r_pos != -1){
				edit_area.setSelectionStart(l_pos);
				edit_area.setSelectionEnd(r_pos);
			}
		}
	}
}
