package application.pen;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class FileSave {
	String name;
	String file_name;
	
	private String[] obj = {"既にファイルがあります、上書きしますか？"};
	private String[] option = { "上書き", "別名で保存", "取り消し" };
	
	public FileSave(File file, JTextArea ed){
		String file_path  = FilePath(file);
		Save(file_path, ed);
		file_name = name;
	}

	public FileSave(File file, JTextArea ed, JFileChooser fc, JFrame mw){
		int retValue;
		String file_path = FilePath(file);
		if(new File(file_path).canRead()){
			retValue = JOptionPane.showOptionDialog(null, obj,"", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null,option,option[0]);
			if(retValue==JOptionPane.YES_OPTION){
				Save(file_path, ed);
				file_name = name;
			}else if(retValue==JOptionPane.NO_OPTION){
				retValue = fc.showSaveDialog(mw);
				if (retValue == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					file_name = new FileSave(file, ed, fc, mw).file_name;
				}
			}else if(retValue==JOptionPane.CANCEL_OPTION){
			}
		}else{
			Save(file_path, ed);
			file_name = name;
		}
	}
	
	public String FilePath(File file){
		String ext = "";
		
		name = file.getName();
		int i = name.lastIndexOf('.');
		if (i > 0 &&  i < name.length() - 1) {
			ext = name.substring(i + 1).toLowerCase();
		}
		if (!ext.equals("pen") || ext == ""){
			name += ".pen";
		}

		String file_path = file.getParent() + System.getProperty("file.separator") + name;
		return file_path;
	}
	
	public void Save(String file_path, JTextArea ed){
		try{
			FileWriter writer = new FileWriter(file_path);
			ed.write(writer);
			writer.close();
		}catch (FileNotFoundException ex){
			System.out.println(ex);
		}catch (IOException ex){
			System.out.println(ex);
		}
	}
}
