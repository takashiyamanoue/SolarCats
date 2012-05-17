package application.pen;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import controlledparts.*;

class RunButtonListener implements ActionListener{
	ThreadRun RUN;

	public void actionPerformed(ActionEvent e) {
//		System.out.println("RunButtonListener.actionPerformed-start");
//		JButton source = (JButton)(e.getSource());
		ControlledButton source =(ControlledButton)(e.getSource());
		if(source.getText().equals("実行") || source.getText().equals("始めから実行")){
			MyRun();
		}else if(source.getText().equals("一時停止")){
				MainGUI.Suspend_flag = true;
				if(!MainGUI.Input_flag){ MainGUI.run_print.stop(); }
				MainGUI.MenuBar.LogCopy(true);
				MainGUI.run_button.setText("再開");
		}else if(source.getText().equals("再開")){
			MainGUI.Step_flag = false;
			MainGUI.Suspend_flag = false;
			if(!MainGUI.Input_flag){ MainGUI.run_print.run(); }
			MainGUI.MenuBar.LogCopy(false);
			MainGUI.run_button.setText("一時停止");
		}else if(source.getText().equals("一行実行")){
			if(!MainGUI.run_button.getText().equals("始めから実行")){
				MainGUI.Step_flag = true;
				MainGUI.Suspend_flag = false;
				if(!MainGUI.Run_flag){
					MyRun();
					MainGUI.run_print.stop();
				}
				MainGUI.MenuBar.LogCopy(true);
				if(!MainGUI.Input_flag){ MainGUI.run_print.stop(); }
				MainGUI.run_button.setText("再開");
			}
		}else if(source.getText().equals("始めに戻る")){
			if(MainGUI.Run_flag){
				MainGUI.Run_flag	= false;
				MainGUI.Step_flag	= false;
				MainGUI.Suspend_flag	= false;
				MainGUI.Input_flag	= false;
				MainGUI.Stop_flag	= true;
				MainGUI.gDrowWindow.gWindowClose();
			} else {
				new RunClean();
			}
		}
//		System.out.println("RunButtonLister.actionPerformed-end");
	}
	
	public void MyRun(){
		MainGUI.run_print.run();
		MainGUI.run_point.setText("●");
		MainGUI.console.setText("");

//		int x = MainGUI.vt_model.getRowCount();
//		for(int i = 1; i <= x; i++){
//			MainGUI.vt_model.removeRow(0);
//		}
		MainGUI.vt_model.setRowCount(0);
		MainGUI.MenuBar.MysetEnabled(false);
		MainGUI.edit_area.setEditable(false);
		MainGUI.edit_area.setBackground(new Color(240, 240, 240));
		MainGUI.numbar_area.setBackground(new Color(240, 240, 240));
		MainGUI.new_button.setEnabled(false);
//		MainGUI.save_button.setEnabled(false);
		MainGUI.open_button.setEnabled(false);
		MainGUI.run_button.setText("一時停止");
		MainGUI.Run_flag = true;
		MainGUI.Bug_flag = false;
		MainGUI.gDrowWindow.gWindowClose();
		RUN = new ThreadRun();
		RUN.start();
	}
	
	public void MySuspend(){
		MainGUI.Suspend_flag = true;
	}
}
