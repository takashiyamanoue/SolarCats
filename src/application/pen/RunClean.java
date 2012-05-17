package application.pen;
public class RunClean {
	public RunClean(){
		MainGUI.console.setText("");
	//	int x = MainGUI.vt_model.getRowCount();
	//	for(int i = 0; i < x; i++){
	//		MainGUI.vt_model.removeRow(0);
	//	}
		MainGUI.vt_model.setRowCount(0);
		MainGUI.run_print.breaks();
		MainGUI.Stop_flag = false;
		MainGUI.run_button.setText("ŽÀs");
		MainGUI.run_point.setText("œ");
		MainGUI.edit_area.requestFocus();
		MainGUI.gDrowWindow.gWindowClose();
	}
}
