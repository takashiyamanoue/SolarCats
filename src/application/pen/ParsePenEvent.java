
/*
 * 作成日: 2005/04/26
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package application.pen;

/**
 * @author yamachan
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */


import controlledparts.*;

public class ParsePenEvent extends ParseEvent
{

	public MainGUI gui;



	public boolean parseEvent()
	{
		boolean rtn;
		if( 
			this.parseButtonEvent(gui)                ||
			this.parseSliderEvent(gui)                ||
			this.parseScrollBarEvent(gui)             ||
			this.parseTabbedPaneEvent(gui)            ||
			this.parseTextEvent(gui)                  ||
			parseFileEvent(gui.fileFrame)             ||
			parseFocus(gui)    ||
			parseFrameEvent(gui) ||
			parsePosition(gui)||
			parseMenuEvent(gui)||
			parseMenuItemEvent(gui)
			)
			rtn= true;
		else rtn= false;
		return rtn;

	}
	public void run()
	 {
		  super.run();
	 }
	 
	 public ParsePenEvent(MainGUI f, InputQueue q)
	 {
		 iq=q;
		 gui=f;
		 me=null;
//		   start();
	 }
	 public boolean parsePosition(MainGUI gui){
	        parseB();
	        if(!iq.rString("pos(")) return false;
	 //       Text fig=new Text(gui);
	           if(!parseAttrib());
	        if(!rNumPair()) return false;
	        int i=x1; int p=y1;
	        parseB();
	        if(!iq.rString(")")) return false;
            gui.setPosition(i,p);
//	        fig.font=gui.getFont();
//	        fig.fmetrics=gui.getFontMetrics(fig.font);

//	        afig=fig;
	        return true;
	 }

}