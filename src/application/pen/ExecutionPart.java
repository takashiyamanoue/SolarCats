
/*
 * �쐬��: 2005/04/25
 *
 * ���̐������ꂽ�R�����g�̑}�������e���v���[�g��ύX���邽��
 * �E�B���h�E > �ݒ� > Java > �R�[�h���� > �R�[�h�ƃR�����g
 */
package application.pen;

/**
 * @author yamachan
 *
 * ���̐������ꂽ�R�����g�̑}�������e���v���[�g��ύX���邽��
 * �E�B���h�E > �ݒ� > Java > �R�[�h���� > �R�[�h�ƃR�����g
 */

// this part is transformed from RunButtonListener.java
// t.yamanoue, 2005

import java.awt.*;

public class ExecutionPart {

	ThreadRun RUN;
	private boolean run_flag = true;

// source.getText() -> MainGUI.playButton.getToolTipText()
    public void playButtonClicked(){
    	if((MainGUI.run_button.getToolTipText()).equals("���s")|| 
    	     (MainGUI.run_button.getToolTipText()).equals("�n�߂�����s")){
    		MyRun();
//	    	run_flag = false;    	// commented out by t.yamanoue, 2005 7/18
    	}
    	else
    	if((MainGUI.run_button.getToolTipText()).equals("�ꎞ��~")){
			MainGUI.Suspend_flag = true;
			if(!MainGUI.Input_flag){ MainGUI.run_print.stop(); }
//			MainGUI.MenuBar.LogCopy(true);
			MainGUI.run_button.setToolTipText("�ĊJ");
//			MainGUI.run_button.setIcon(MainGUI.playIcon);

    	}
	    else 
	    if(MainGUI.run_button.getToolTipText().equals("�ĊJ")){
			MainGUI.Step_flag = false;
			MainGUI.Suspend_flag = false;
			if(!MainGUI.Input_flag){ MainGUI.run_print.run(); }
//			MainGUI.MenuBar.LogCopy(false);
			MainGUI.run_button.setToolTipText("�ꎞ��~");
//			MainGUI.run_button.setIcon(MainGUI.pauseIcon);

	    }
    	
    }
    
    public void stepRunButtonClicked(){
    	/*
    	System.out.println("step-flag="+MainGUI.Step_flag);
    	System.out.println("Run-flag="+MainGUI.Run_flag);
    	System.out.println("Suspend-flag="+MainGUI.Suspend_flag);
    	System.out.println("Input-flag="+MainGUI.Input_flag);
    	System.out.println("stop-flag="+MainGUI.Stop_flag);
 */  	
//			if(!MainGUI.run_button.getText().equals("�n�߂�����s")){
		if(!(MainGUI.run_button.getToolTipText()).equals("�n�߂�����s")){
			MainGUI.Step_flag = true;
			MainGUI.Suspend_flag = false;
			if(!MainGUI.Run_flag){
				MyRun();
				MainGUI.run_print.stop();
			}
//			MainGUI.MenuBar.LogCopy(true); // deleted 7/19, 2005
			if(!MainGUI.Input_flag){ MainGUI.run_print.stop(); }
//			MainGUI.run_button.setText("�ĊJ");
			MainGUI.run_button.setToolTipText("�ĊJ"); // substituted
//			MainGUI.playButton.setIcon(MainGUI.playIcon);
		}
    }

    public void toHead(){
		if(MainGUI.Run_flag){
			MainGUI.Run_flag		= false;
			MainGUI.Step_flag		= false;
			MainGUI.Suspend_flag	= false;
			MainGUI.Input_flag		= false;
			MainGUI.Stop_flag		= true; // 2005 7/18
		} else {
//			Reset();         
			new RunClean(); // substituted 2005 7/18

		}
    }

	
	public void MyRun(){
		MainGUI.run_print.run();
		MainGUI.run_point.setText("��");
		MainGUI.console.setText("");

		int x = MainGUI.vt_model.getRowCount();
		for(int i = 1; i <= x; i++){
			MainGUI.vt_model.removeRow(0);
		}
//		MainGUI.MenuBar.MysetEnabled(false);
		MainGUI.edit_area.setEditable(false);
		MainGUI.edit_area.setBackground(new Color(240, 240, 240));
		MainGUI.numbar_area.setBackground(new Color(240, 240, 240));
//		MainGUI.new_button.setEnabled(false);
//		MainGUI.save_button.setEnabled(false);
//		MainGUI.open_button.setEnabled(false);
		MainGUI.run_button.setText("�ꎞ��~");
//        MainGUI.playButton.setToolTipText("�ꎞ��~");
//        MainGUI.playButton.setIcon(MainGUI.pauseIcon);
		MainGUI.Run_flag = true;
		MainGUI.Bug_flag = false;
		RUN = new ThreadRun();
		RUN.start();

	}
	
	public void MySuspend(){
//		RUN.suspend();
		MainGUI.Suspend_flag = true;
	}

    public boolean isRunning(){
    	return run_flag;
    }
	
	public void Reset(){
		MainGUI.run_point.setText("��");
		MainGUI.run_print.breaks();
		MainGUI.run_button.setEnabled(true);
//        MainGUI.playButton.setEnabled(true);
		MainGUI.step_button.setEnabled(true);
		MainGUI.edit_area.requestFocus();

	}

}