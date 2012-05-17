package application.pen;
import java.awt.Font;

import javax.swing.Action;
import javax.swing.JMenuItem;
import controlledparts.*;

public class MyJMenuItem extends ControlledMenuItem{
	
	public MyJMenuItem(String str){
		Font font = new Font("", 0, 12);
		setText(str);
		setFont(font);
	}
	
	public MyJMenuItem(String str, Action ac){
		Font font = new Font("", 0, 12);
		setFont(font);
		setAction(ac);
		setText(str);
	}
}
