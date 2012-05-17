package application.pen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

public class EditAreaUndoableEditListener implements UndoableEditListener,ActionListener {
	private UndoManager undo;
	
	public EditAreaUndoableEditListener(){
		undo = new UndoManager();
	}
	
	public void undoableEditHappened(UndoableEditEvent e) {
		undo.addEdit(e.getEdit());
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem)(e.getSource());
		if(source.getText().equals("���ɖ߂�")){
			if(undo.canUndo()){
				undo.undo();
			}
		} else if(source.getText().equals("��蒼��")){
			if(undo.canRedo()){
				undo.redo();
			}
		}
	}
}
