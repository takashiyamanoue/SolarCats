package application.pen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.text.BadLocationException;

import controlledparts.ControlledTextArea;

class EditButtonListener implements ActionListener{
	private String[] add;
	
	public EditButtonListener(String[] apend){
		add = apend;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(!MainGUI.Run_flag){
			String tab = Tab();
			String str = "";
			for(int i = 0; i < add.length; i++){
				if(i == 0){
					str += add[i];
				}else{
					str += tab + add[i];
				}
			}
			// modified by . yamanoue, 23apr2006.
			ControlledTextArea t=MainGUI.edit_area;
			String tx=t.getText();
			int start=t.getSelectionStart();
			int end=t.getSelectionEnd();
			if(tx.length()<end)
				end=tx.length();
			t.replaceRange(str,start, end);
			t.requestFocus();
		}
	}
	
	public String Tab(){
		String add_tab = "";
		// modified by t. yamanoue, 23 Apr. 2006
		int pos=0; 
		int line=0;
		try {
			pos		= MainGUI.edit_area.getCaretPosition();
		}
		catch(Exception e){
		}
		try{
			line	= MainGUI.edit_area.getLineOfOffset(pos);
		}
		catch(BadLocationException e1){
		}
		try{
			add_tab = new EditAreaAddTab().AddTab(MainGUI.edit_area.getLineStartOffset(line), MainGUI.edit_area.getLineEndOffset(line) - 1);
		} 
		catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		return add_tab;
	}
}
