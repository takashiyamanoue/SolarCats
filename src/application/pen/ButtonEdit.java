package application.pen;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * 入力支援ボタン定義ファイルから入力支援ボタンを配置するためのクラス
 * 
 * @author rn_magi
 */
public class ButtonEdit {
	JPanel edit_area_panel		= new JPanel();
	JPanel edit_button_area_panel		= new JPanel();

	/**
	 * <code>edit_button_area_layout</code> のコメント
	 * このインスタンスに生成された入力支援ボタンが格納される
	 */
	JToolBar edit_button_area_toolbar	= new JToolBar();
	 
	GridBagLayout edit_button_area_layout	= new GridBagLayout();
	GridBagConstraints gbc					= new GridBagConstraints();
	
	/**
	 * <code>edit_button</code> のコメント
	 * 入力支援ボタン定義ファイルが存在しなかった場合、
	 * このインスタンス内に格納されている値で
	 * 入力支援ボタンを作成するようになっている
	 */
	EditButtonList[] edit_button = {
			new EditButtonList(
				"もし" ,
				"<html><pre>もし≪条件式≫ならば\n  | ＜処理＞\nを実行する</pre></html>" ,
				new String[] {"もし ≪条件式≫ ならば\n" , "  | \n" , "を実行する"} ,
				130 , 25 , new Color(140,220,200)
			),
			new EditButtonList(
				"もし～そうでなければ" ,
				"<html><pre>もし≪条件式≫ならば\n  | ＜処理＞\nを実行し，そうでなければ\n  | ＜処理＞\nを実行する</pre></html>" ,
				new String[] {"もし ≪条件式≫ ならば\n" , "  | \n" , "を実行し，そうでなければ\n" , "  | \n","を実行する"} ,
				130 , 25 , new Color(140,220,200)
			),
			new EditButtonList(
				"増やしながら繰り返す" ,
				"<html><pre>≪変数≫ を ≪数値≫ から ≪数値≫ まで 1 ずつ増やしながら，\n  | ＜処理＞\nを繰り返す</pre></html>" ,
				new String[] {"≪変数≫ を ≪数値≫ から ≪数値≫ まで 1 ずつ増やしながら，\n" , "  | \n"  ,"を繰り返す"} ,
				130 , 25 , new Color(160,220,200)
			),
			new EditButtonList(
				"減らしながら繰り返す" ,
				"<html><pre>≪変数≫ を ≪数値≫ から ≪数値≫ まで 1 ずつ減らしながら，\n  | ＜処理＞\nを繰り返す</pre></html>" ,
				new String[] {"≪変数≫ を ≪数値≫ から ≪数値≫ まで 1 ずつ減らしながら，\n" , "  | \n" , "を繰り返す"}  ,
				130 , 25 , new Color(160,220,200)
			),
			new EditButtonList(
				"～の間，繰り返す" ,
				"<html><pre>≪条件式≫ の間，\n  | ＜処理＞\nを繰り返す</pre></html>" ,
				new String[] {"≪条件式≫ の間，\n" , "  | \n" , "を繰り返す"} ,
				130 , 25 , new Color(180,220,200)
			),
			new EditButtonList(
				"繰り返し～になるまで実行" ,
				"<html><pre>繰り返し，\n  | ＜処理＞\nを， ≪条件式≫ になるまで実行する</pre></html>" ,
				new String[] {"繰り返し，\n" , "  | \n" , "を， ≪条件式≫ になるまで実行する"} ,
				130 , 25 , new Color(180,220,200)
			),
			new EditButtonList(
				"入力" ,
				"<html><pre>≪変数≫ ← input()</pre></html>" ,
				new String[] {"≪変数≫ ← input()"} ,
				65 , 25 , new Color(255,255,204)
			),
			new EditButtonList(
				"出力" ,
				"<html><pre>≪出力文≫ を印刷する</pre></html>" ,
				new String[] {"≪出力文≫ を印刷する"} ,
				65 , 25 , new Color(255,255,204)
			),
			new EditButtonList(
				"改行無出力" ,
				"<html><pre>≪出力文≫ を改行なしで印刷する</pre></html>" ,
				new String[] {"≪出力文≫ を改行なしで印刷する"} ,
				65 , 25 , new Color(255,255,204)
			),
			new EditButtonList(
				"代入",
				"<html><pre>≪変数≫ ← ≪式≫</pre></html>" ,
				new String[] {"≪変数≫ ← ≪式≫"} ,
				65 , 25 , new Color(255,255,204)
			),
			new EditButtonList(
				"整数" ,
				"<html><pre>整数 ≪変数≫</pre></html>" ,
				new String[] {"整数 ≪変数≫"} ,
				65 , 25 , new Color(255,204,255)
			),
			new EditButtonList(
				"＝" ,
				"<html><pre>≪式≫ ＝ ≪式≫</pre></html>" ,
				new String[] {"≪式≫ ＝ ≪式≫"} ,
				32 , 25 , new Color(204,204,204)
			),
			new EditButtonList(
				"＞" ,
				"<html><pre>≪式≫ ＞ ≪式≫</pre></html>" ,
				new String[] {"≪式≫ ＞ ≪式≫"} ,
				32 , 25 , new Color(204,204,204)
			),
			new EditButtonList(
				"≧" ,
				"<html><pre>≪式≫ ≧ ≪式≫</pre></html>" ,
				new String[] {"≪式≫ ≧ ≪式≫"} ,
				32 , 25 , new Color(204,204,204)
			),
			new EditButtonList(
				"＜" ,
				"<html><pre>≪式≫ ＜ ≪式≫</pre></html>" ,
				new String[] {"≪式≫ ＜ ≪式≫"} ,
				32 , 25 , new Color(204,204,204)
			),
			new EditButtonList(
				"≦" ,
				"<html><pre>≪式≫ ≦ ≪式≫</pre></html>" ,
				new String[] {"≪式≫ ≦ ≪式≫"} ,
				32 , 25 , new Color(204,204,204)
			),
			new EditButtonList(
				"≠" ,
				"<html><pre>≪式≫ ≠ ≪式≫</pre></html>" ,
				new String[] {"≪式≫ ≠ ≪式≫"} ,
				32 , 25 , new Color(204,204,204)
			),
			new EditButtonList(
				"かつ" ,
				"<html><pre>かつ</pre></html>" ,
				new String[] {"かつ"} ,
				65 , 25 , new Color(190,190,190)
			),
			new EditButtonList(
					"または" ,
					"<html><pre>または</pre></html>" ,
					new String[] {"または"} ,
					65 , 25 , new Color(190,190,190)
			),
			new EditButtonList(
					"でない" ,
					"<html><pre>でない</pre></html>" ,
					new String[] {"でない"} ,
					65 , 25 , new Color(190,190,190)
			),
			new EditButtonList(
					"「」" ,
					"<html><pre>「≪文字列≫」</pre></html>" ,
					new String[] {"「≪文字列≫」"} ,
					65 , 25 , new Color(180,180,180)
			)
		};
	
	MainGUI gui; // added by t.yamanoue, 2005 aug.

	/**
	 * 入力支援ボタンを格納するツールバーの初期設定を決める
	 * 
	 * @param ListFile
	 * 入力支援ボタン定義ファイルの絶対パス
	 */
	public ButtonEdit(String ListFile, MainGUI g){
		gui=g;
		edit_button_area_toolbar.setLayout(new BoxLayout(edit_button_area_toolbar,BoxLayout.Y_AXIS));
		edit_button_area_toolbar.setMaximumSize(new Dimension(550, 550));
		edit_button_area_panel.setLayout(edit_button_area_layout);
		gbc.insets = new Insets(0, 0, 0, 0);
		
		ButtonCustom(ListFile);
	}
	
	/**
	 * 入力支援ボタン定義ファイルを読み込みツールバーに配置する
	 * 定義ファイルの読み込みに失敗した場合、
	 * エラー文をシステムコンソールに出力し、
	 * デフォルトの入力支援ボタンを配置する
	 * 
	 * @param list
	 * 入力支援ボタン定義ファイルの絶対パス
	 */
	public void ButtonCustom(String list) {
		int y = 0;

		for(int i = 0; i < 48; i++){
			JLabel nu = new JLabel();
			Dimension size = new Dimension(10 + i % 2, 0);
			nu.setPreferredSize(size);
			gbc.gridx = i;
			gbc.gridy = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			edit_button_area_layout.setConstraints(nu, gbc);
			edit_button_area_panel.add(nu);
		}
		
		try{
			File edit_button_file = new File(list);
			BufferedReader edit_reader = new BufferedReader(new FileReader(edit_button_file));
			String edit_read;

			while(true){
				edit_read = edit_reader.readLine();
				if(edit_read != null){
					EditButtonList ebl = new EditButtonList(edit_read);
					if( y / 48 != (y + ebl.TextWidth - 1) / 48){
						y = ( y / 48 + 1) * 48;
						gbc.gridx = y % 48;
						gbc.gridy = ( y + ebl.TextWidth ) / 48+ 1;
					} else {
						gbc.gridx = y % 48;
						gbc.gridy = y / 48 + 1;
					}
					gbc.gridwidth = ebl.TextWidth;
					gbc.gridheight = 1;
					y = y + ebl.TextWidth;

					if( !ebl.TipText.equals("") ) {
						EditButton eb = new EditButton(ebl);
						edit_button_area_layout.setConstraints(eb, gbc);
						edit_button_area_panel.add(eb);

						// add the following lines for DSR 24Apr2005
						gui.buttons.addElement(eb);
						gui.editButtons.put(eb,new EditButtonListener(ebl.AppendText));
					} else {
						MyJLabel eb = new MyJLabel(ebl);
						edit_button_area_layout.setConstraints(eb, gbc);
						edit_button_area_panel.add(eb);
					}
					
				}else{
					edit_reader.close();
					break;
				}
			}
		//	edit_button_area_toolbar.add(new MyJLabel("プログラム入力支援ボタン"));
			edit_button_area_toolbar.add(edit_button_area_panel);
		} catch (Exception ex){
			System.out.println(ex);
			EditButtonDefault();
		}
	}
	
	/**
	 * デフォルトの入力支援ボタンの配置
	 */
	public void EditButtonDefault(){
		int y = 0;
		for(int i = 0; i < edit_button.length; i++){
			EditButton eb = new EditButton(edit_button[i]);
			gbc.gridx = y % 16;
			gbc.gridy = y / 16 + 1;
			gbc.gridwidth = edit_button[i].Width / 32;
			gbc.gridheight = 1;
			y = y + edit_button[i].Width / 32;
			edit_button_area_layout.setConstraints(eb, gbc);
			edit_button_area_panel.add(eb);
		}
		edit_button_area_toolbar.add(new MyJLabel("（プログラム入力支援ボタン）"));
		edit_button_area_toolbar.add(edit_button_area_panel);
	}
}
