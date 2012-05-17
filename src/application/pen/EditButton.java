package application.pen;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;

import controlledparts.*;

public class EditButton extends //JButton{
ControlledButton{
	private Font font = new Font("", 0, 10);
	
	public EditButton(EditButtonList ebl){
		setMargin(new Insets(0,0,0,0));
		setText(ebl.ButtonText);
		setFont(font);
		setToolTipText(ebl.TipText);
//		addActionListener(new EditButtonListener(ebl.AppendText));
//         comment outed by t. yamanoue for dsr 2006 3.21
		Dimension size = new Dimension(ebl.Width, ebl.Height);
		setMaximumSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
		
		setBackground(ebl.Color);
	}
}