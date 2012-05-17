package application.pen;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class FileDropOpen implements DropTargetListener {
	private FileOpen file_open;
	
	public FileDropOpen(JFileChooser fc, JFrame mw, JTextArea ea, String wn){
		file_open = new FileOpen(fc, mw, ea, wn);
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void dragOver(DropTargetDragEvent dtde) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void drop(DropTargetDropEvent dtde) {
		dtde.acceptDrop(dtde.getDropAction());
		try {
			Transferable trans = dtde.getTransferable();
			DataFlavor flavors[] = trans.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; ++i) {
				if (flavors[i].isFlavorJavaFileListType()) {
					List list = (List) trans.getTransferData(flavors[i]);
				//	for (int j = 0; j < list.size(); ++j) {
				//		File file = (File) list.get(j);
				//		System.out.println(file.getAbsolutePath());
				//	}
				//	System.out.println("---------------------");
					if(file_open.FileOpenConfirm()){
						File file = (File) list.get(0);
						file_open.FileOpenToEditArea(file);
					}
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (UnsupportedFlavorException ufe) {
			ufe.printStackTrace();
		} finally {
			dtde.dropComplete(true);
		}
	}

	public void dragExit(DropTargetEvent dte) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
