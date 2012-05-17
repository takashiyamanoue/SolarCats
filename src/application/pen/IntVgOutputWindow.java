package application.pen;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

/**
 * さまざまな描画をおこなうJPanelを拡張したクラスです
 * 
 * @author zxc
 */
public class IntVgOutputWindow extends JPanel {
	private JFrame frame = new JFrame("GraphicWindow");
	
	private Color dc = new Color(0, 0, 0);
	private Color bc = new Color(255, 255, 255);
	
	private BufferedImage image;
	private Graphics2D imageGraphics;
	
	/* (非 Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		super.paintComponent(g2);
		g2.drawImage(image, 0, 0, this);
	}
	
	/**
	 * このクラスのコンストラクタです
	 */
	public IntVgOutputWindow(){
	}
	
	/**
	 * 描画Windowを初期化、生成を行うメソッド
	 * 
	 * @param w
	 * Windowの横幅
	 * @param h
	 * Windowの縦幅
	 */
	public void gWindowOpen(int w, int h){
		w += 10;
		h += 30;
		setBackground(bc);
		setSize(w,h);
		
		image  = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		imageGraphics = (Graphics2D)image.createGraphics();
		imageGraphics.setColor(bc);
		imageGraphics.fillRect(0,0, w,h);
		
		frame.setSize(w, h);
		frame.getContentPane().add(this);
		frame.setVisible(true);
	}
	
	/**
	 * 描画Windowを閉じるメソッド
	 */
	public void gWindowClose(){
		frame.setVisible(false);
		image = null;
		repaint();

		dc = new Color(0, 0, 0);
		bc = new Color(255, 255, 255);
	}
	
	/**
	 * このグラフィックスコンテキストの現在の色を、
	 * 指定された色に設定します。
	 * このグラフィックスコンテキストを使うこれ以降のレンダリング操作は、
	 * ここで指定された色を使用します。
	 * 
	 * @param r
	 * 赤色成分
	 * @param g
	 * 緑色成分
	 * @param b
	 * 青色成分
	 */
	public void gColor(int r, int g, int b){
		dc = new Color(r, g, b);
	}
	
	/**
	 * 楕円（淵のみ）を描画するメソッド。
	 * 色は gColor のメソッドで指定したもの。
	 * 
	 * @param x
	 * 左上隅の x 座標です。
	 * @param y
	 * 左上隅の y 座標です。
	 * @param width
	 * 幅です。
	 * @param height
	 * 高さです。
	 */
	public void gDrawOval(int x, int y, int width, int height){
		imageGraphics.setColor(dc);
		imageGraphics.draw(new Ellipse2D.Double(x, y, width, height));
		repaint();
	}
	
	/**
	 * 点を描画するメソッド。
	 * 色は gColor のメソッドで指定したもの。
	 * 
	 * @param x
	 * 中心点 x の座標です。
	 * @param y
	 * 中心点 y の座標です。
	 */
	public void gDrawPoint(int x, int y){
		imageGraphics.setColor(dc);
		imageGraphics.draw(new Ellipse2D.Double(x, y, 1, 1));
		repaint();
	}
	
	/**
	 * 楕円（塗りつぶし）を描画するメソッド。
	 * 色は gColor のメソッドで指定したもの。
	 * 
	 * @param x
	 * 左上隅の x 座標です。
	 * @param y
	 * 左上隅の y 座標です。
	 * @param width
	 * 幅です。
	 * @param height
	 * 高さです。
	 */
	public void gFillOval(int x, int y, int width, int height){
		imageGraphics.setColor(dc);
		imageGraphics.fill(new Ellipse2D.Double(x, y, width, height));
		repaint();
	}
	
	/**
	 * 点を描画するメソッド。
	 * 色は gColor のメソッドで指定したもの。
	 * 
	 * @param x
	 * 中心点 x の座標です。
	 * @param y
	 * 中心点 y の座標です。
	 */	
	public void gFillPoint(int x, int y){
		imageGraphics.setColor(dc);
		imageGraphics.fill(new Ellipse2D.Double(x - 1, y - 1, 2, 2));
		repaint();
	}
	
	/**
	 * 線を描画するメソッド。
	 * 色は gColor のメソッドで指定したもの。
	 * 
	 * @param x1
	 * 始点 x の座標です。
	 * @param y1
	 * 始点 y の座標です。
	 * @param x2
	 * 終点 x の座標です。
	 * @param y2
	 * 終点 y の座標です。
	 */
	public void gDrawLine(int x1, int y1, int x2, int y2){
		imageGraphics.setColor(dc);
		imageGraphics.draw(new Line2D.Double( x1, y1, x2, y2));
		repaint();
	}
	
	/**
	 * 矩形[四角]（淵のみ）を描画するメソッド。
	 * 色は gColor のメソッドで指定したもの。
	 * 
	 * @param x
	 * 左上隅の x 座標です。
	 * @param y
	 * 左上隅の y 座標です。
	 * @param width
	 * 幅です。
	 * @param height
	 * 高さです。
	 */
	public void gDrawBox(int x, int y, int width, int height){
		imageGraphics.setColor(dc);
		imageGraphics.draw(new Rectangle2D.Double(x, y, width, height));
		repaint();
	}

	/**
	 * 矩形[四角]（塗りつぶし）を描画するメソッド。
	 * 色は gColor のメソッドで指定したもの。
	 * 
	 * @param x
	 * 左上隅の x 座標です。
	 * @param y
	 * 左上隅の y 座標です。
	 * @param width
	 * 幅です。
	 * @param height
	 * 高さです。
	 */
	public void gFillBox(int x, int y, int width, int height){
		imageGraphics.setColor(dc);
		imageGraphics.fill(new Rectangle2D.Double(x, y, width, height));
		repaint();
	}
	
	/**
	 * 円弧（淵のみ）を描画するメソッド。
	 * 色は gColor のメソッドで指定したもの。
	 * 
	 * @param x
	 * 弧の左上隅の x 座標です。
	 * @param y
	 * 弧の左上隅の y 座標です。
	 * @param w
	 * 楕円の幅です (角の大きさは考慮しない)。
	 * @param h
	 * 楕円の高さです (角の大きさは考慮しない)。
	 * @param start
	 * 弧の始角 (度単位) です。
	 * @param extent
	 * 弧の角の大きさ (度単位) です。
	 * @param type
	 * 弧の閉じ方の種類 (OPEN=0、CHORD=1、または PIE=2)
	 */
	public void gDrawArc(int x, int y, int w, int h, int start, int extent, int type){
		imageGraphics.setColor(dc);
		imageGraphics.draw(new Arc2D.Double(x, y, w, h, start, extent, type));
		repaint();
	}
	
	/**
	 * 円弧（塗りつぶし）を描画するメソッド。
	 * 色は gColor のメソッドで指定したもの。
	 * 
	 * @param x
	 * 弧の左上隅の x 座標です。
	 * @param y
	 * 弧の左上隅の y 座標です。
	 * @param w
	 * 楕円の幅です (角の大きさは考慮しない)。
	 * @param h
	 * 楕円の高さです (角の大きさは考慮しない)。
	 * @param start
	 * 弧の始角 (度単位) です。
	 * @param extent
	 * 弧の角の大きさ (度単位) です。
	 * @param type
	 * 弧の閉じ方の種類 (OPEN=0、CHORD=1、または PIE=2)
	 */
	public void gFillArc(int x, int y, int w, int h, int start, int extent, int type){
		imageGraphics.setColor(dc);
		imageGraphics.fill(new Arc2D.Double(x, y, w, h, start, extent, type));
		repaint();
	}
}
