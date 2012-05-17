package application.pen;
import java.awt.Color;

public class RunStop {
	public RunStop(){
		MainGUI.MenuBar.MysetEnabled(true);
		MainGUI.edit_area.setEditable(true);
		MainGUI.edit_area.setBackground(new Color(255,255,255));
		MainGUI.numbar_area.setBackground(new Color(255,255,255));
		MainGUI.new_button.setEnabled(true);
//		MainGUI.save_button.setEnabled(true);
		MainGUI.open_button.setEnabled(true);
		if(MainGUI.Stop_flag){
			new RunClean();
		} else {
			if(MainGUI.Bug_flag){
				MainGUI.run_button.setText("é¿çs");
				MainGUI.run_print.breaks();
			} else {
				MainGUI.run_point.insert("\n", 0);
				MainGUI.run_button.setText("énÇﬂÇ©ÇÁé¿çs");
				MainGUI.run_print.end();
			}
			MainGUI.Step_flag	= false;
			MainGUI.Run_flag	= false;
			MainGUI.Suspend_flag	= false;
			MainGUI.Input_flag		= false;
			MainGUI.Stop_flag		= false;
		}
		MainGUI.edit_area.requestFocus();
	}
}
