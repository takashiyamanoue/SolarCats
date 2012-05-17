package application.pen;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class FileOpen {
	private JFileChooser file_c;
	private JFrame window;
	private JTextArea edit_area;
	private String WindowName;
	
	private String[] obj = {"ファイルを開く前に保存しますか？"};
	private String[] option = { "保存", "破棄", "取り消し" };
	
	private String file_name = "";
	
	private FileOpen file_open;
	
	public FileOpen(JFileChooser fc, JFrame mw, JTextArea ea, String wn){
		file_c		= fc;
		window		= mw;
		edit_area	= ea;
		WindowName	= wn;
	}
	
	public boolean FileOpenConfirm() {
		String window_name = window.getTitle();
		if(window_name.substring(0, 1).equals("*")) {
			java.awt.Toolkit.getDefaultToolkit().beep();
			int retValue = JOptionPane.showOptionDialog(window, obj,"", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null,option,option[0]);
			if(retValue==JOptionPane.YES_OPTION){
				int returnVal = MainGUI.fc.showSaveDialog(window);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file_name = new FileSave(MainGUI.fc.getSelectedFile(), edit_area, MainGUI.fc, window).file_name;
					if(file_name != null){
						return true;
					}
				}else{
				}
			}else if(retValue==JOptionPane.NO_OPTION){
				return true;
			}else if(retValue==JOptionPane.CANCEL_OPTION){ }
		}else{
			return true;
		}
		return false;
	}
	

	
	public void FileChooser(){
		int retValue = file_c.showOpenDialog(window);
		if (retValue == JFileChooser.APPROVE_OPTION) {
			FileOpenToEditArea(file_c.getSelectedFile());
		}else{
			if(!file_name.equals("")){
				new RunClean();
				window.setTitle(file_name + " - " + WindowName);
			}
		}
	}
	
	public void FileOpenToEditArea(File file){
		String code = getCharSet(file);
		
		try{
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), code);
			BufferedReader reader = new BufferedReader(isr);
			String read = "";
			String add	= "";
			while(true){
				read = reader.readLine();
				if(read != null){
					add += read + "\n";
				}else{
					reader.close();
					break;
				}
			}
			new RunClean();
			edit_area.setText(add);
			edit_area.requestFocus();
			window.setTitle(file.getName() + " - " + WindowName);
		}catch (FileNotFoundException ex){
			String messege = "ファイル \"" + file.getName() + "\" が見つかりません";
			JOptionPane.showMessageDialog(null, messege, "エラー", JOptionPane.ERROR_MESSAGE);
		}catch (IOException ex){
		}catch (ClassCastException ex){
		}
	}
	
	public static String getCharSet(File file) {
		String code = "EUC_JP";
		FileInputStream in = null;
		int nch;

		try {
			in = new FileInputStream(file);
			while((nch=in.read())!=-1){
				if(nch==0x1B){
					code = "JIS";
					break;
				} else if(nch>0x80 && nch<0xA1){
					code = "Shift_JIS";
					break;
				}
			}
			in.close();
		} catch(IOException ex){
		} 
		return code;
	}
}
