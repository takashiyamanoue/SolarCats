package application.pen;
import java.awt.Font;
import javax.swing.JMenu;
import controlledparts.*;

//public class MyJMenu extends JMenu{
public class MyJMenu extends ControlledMenu{
	public MyJMenu(String str){
		Font font = new Font("", 0, 12);
		setText(str);
		setFont(font);
	}
}
