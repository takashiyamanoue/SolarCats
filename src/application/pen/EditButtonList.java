package application.pen;
import java.awt.Color;

public class EditButtonList {
	String ButtonText;
	String TipText;
	String[] AppendText;
	int TextWidth;
	int Width;
	int Height;
	Color Color;
	
	public EditButtonList(
			String ButtonText,
			String TipText,
			String[] AppendText,
			int Width,
			int Height,
			Color Color)
	{
		this.ButtonText	= ButtonText;
		this.TipText	= TipText;
		this.AppendText	= AppendText;
		this.Width		= Width;
		this.Height		= Height;
		this.Color		= Color;
	}
	
	public EditButtonList(String button)
	{
		String[] split = button.split("@");
		this.ButtonText	= split[0];
		this.TextWidth	= new Integer(split[1]).intValue();
		String[] color	= split[2].split(",");
		this.Color		= new Color(new Integer(color[0]).intValue(),new Integer(color[1]).intValue(),new Integer(color[2]).intValue());
		this.Width		= (int) Math.round(this.TextWidth * 10.83);
		this.Height		= 25;

		if( split.length >= 4 ){
			this.TipText	= "<html><pre>" + split[3].replaceAll("<br>", "\n") + "</pre></html>";
			this.AppendText	= split[3].split("<br>");
			for(int i = 0; i < this.AppendText.length - 1; i++)
				this.AppendText[i] += "\n";
		} else {
			String str[] = {""};
			this.TipText = "";
			this.AppendText = str;
		}
	}
}