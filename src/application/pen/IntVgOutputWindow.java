package application.pen;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

/**
 * ���܂��܂ȕ`��������Ȃ�JPanel���g�������N���X�ł�
 * 
 * @author zxc
 */
public class IntVgOutputWindow extends JPanel {
	private JFrame frame = new JFrame("GraphicWindow");
	
	private Color dc = new Color(0, 0, 0);
	private Color bc = new Color(255, 255, 255);
	
	private BufferedImage image;
	private Graphics2D imageGraphics;
	
	/* (�� Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		super.paintComponent(g2);
		g2.drawImage(image, 0, 0, this);
	}
	
	/**
	 * ���̃N���X�̃R���X�g���N�^�ł�
	 */
	public IntVgOutputWindow(){
	}
	
	/**
	 * �`��Window���������A�������s�����\�b�h
	 * 
	 * @param w
	 * Window�̉���
	 * @param h
	 * Window�̏c��
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
	 * �`��Window����郁�\�b�h
	 */
	public void gWindowClose(){
		frame.setVisible(false);
		image = null;
		repaint();

		dc = new Color(0, 0, 0);
		bc = new Color(255, 255, 255);
	}
	
	/**
	 * ���̃O���t�B�b�N�X�R���e�L�X�g�̌��݂̐F���A
	 * �w�肳�ꂽ�F�ɐݒ肵�܂��B
	 * ���̃O���t�B�b�N�X�R���e�L�X�g���g������ȍ~�̃����_�����O����́A
	 * �����Ŏw�肳�ꂽ�F���g�p���܂��B
	 * 
	 * @param r
	 * �ԐF����
	 * @param g
	 * �ΐF����
	 * @param b
	 * �F����
	 */
	public void gColor(int r, int g, int b){
		dc = new Color(r, g, b);
	}
	
	/**
	 * �ȉ~�i���̂݁j��`�悷�郁�\�b�h�B
	 * �F�� gColor �̃��\�b�h�Ŏw�肵�����́B
	 * 
	 * @param x
	 * ������� x ���W�ł��B
	 * @param y
	 * ������� y ���W�ł��B
	 * @param width
	 * ���ł��B
	 * @param height
	 * �����ł��B
	 */
	public void gDrawOval(int x, int y, int width, int height){
		imageGraphics.setColor(dc);
		imageGraphics.draw(new Ellipse2D.Double(x, y, width, height));
		repaint();
	}
	
	/**
	 * �_��`�悷�郁�\�b�h�B
	 * �F�� gColor �̃��\�b�h�Ŏw�肵�����́B
	 * 
	 * @param x
	 * ���S�_ x �̍��W�ł��B
	 * @param y
	 * ���S�_ y �̍��W�ł��B
	 */
	public void gDrawPoint(int x, int y){
		imageGraphics.setColor(dc);
		imageGraphics.draw(new Ellipse2D.Double(x, y, 1, 1));
		repaint();
	}
	
	/**
	 * �ȉ~�i�h��Ԃ��j��`�悷�郁�\�b�h�B
	 * �F�� gColor �̃��\�b�h�Ŏw�肵�����́B
	 * 
	 * @param x
	 * ������� x ���W�ł��B
	 * @param y
	 * ������� y ���W�ł��B
	 * @param width
	 * ���ł��B
	 * @param height
	 * �����ł��B
	 */
	public void gFillOval(int x, int y, int width, int height){
		imageGraphics.setColor(dc);
		imageGraphics.fill(new Ellipse2D.Double(x, y, width, height));
		repaint();
	}
	
	/**
	 * �_��`�悷�郁�\�b�h�B
	 * �F�� gColor �̃��\�b�h�Ŏw�肵�����́B
	 * 
	 * @param x
	 * ���S�_ x �̍��W�ł��B
	 * @param y
	 * ���S�_ y �̍��W�ł��B
	 */	
	public void gFillPoint(int x, int y){
		imageGraphics.setColor(dc);
		imageGraphics.fill(new Ellipse2D.Double(x - 1, y - 1, 2, 2));
		repaint();
	}
	
	/**
	 * ����`�悷�郁�\�b�h�B
	 * �F�� gColor �̃��\�b�h�Ŏw�肵�����́B
	 * 
	 * @param x1
	 * �n�_ x �̍��W�ł��B
	 * @param y1
	 * �n�_ y �̍��W�ł��B
	 * @param x2
	 * �I�_ x �̍��W�ł��B
	 * @param y2
	 * �I�_ y �̍��W�ł��B
	 */
	public void gDrawLine(int x1, int y1, int x2, int y2){
		imageGraphics.setColor(dc);
		imageGraphics.draw(new Line2D.Double( x1, y1, x2, y2));
		repaint();
	}
	
	/**
	 * ��`[�l�p]�i���̂݁j��`�悷�郁�\�b�h�B
	 * �F�� gColor �̃��\�b�h�Ŏw�肵�����́B
	 * 
	 * @param x
	 * ������� x ���W�ł��B
	 * @param y
	 * ������� y ���W�ł��B
	 * @param width
	 * ���ł��B
	 * @param height
	 * �����ł��B
	 */
	public void gDrawBox(int x, int y, int width, int height){
		imageGraphics.setColor(dc);
		imageGraphics.draw(new Rectangle2D.Double(x, y, width, height));
		repaint();
	}

	/**
	 * ��`[�l�p]�i�h��Ԃ��j��`�悷�郁�\�b�h�B
	 * �F�� gColor �̃��\�b�h�Ŏw�肵�����́B
	 * 
	 * @param x
	 * ������� x ���W�ł��B
	 * @param y
	 * ������� y ���W�ł��B
	 * @param width
	 * ���ł��B
	 * @param height
	 * �����ł��B
	 */
	public void gFillBox(int x, int y, int width, int height){
		imageGraphics.setColor(dc);
		imageGraphics.fill(new Rectangle2D.Double(x, y, width, height));
		repaint();
	}
	
	/**
	 * �~�ʁi���̂݁j��`�悷�郁�\�b�h�B
	 * �F�� gColor �̃��\�b�h�Ŏw�肵�����́B
	 * 
	 * @param x
	 * �ʂ̍������ x ���W�ł��B
	 * @param y
	 * �ʂ̍������ y ���W�ł��B
	 * @param w
	 * �ȉ~�̕��ł� (�p�̑傫���͍l�����Ȃ�)�B
	 * @param h
	 * �ȉ~�̍����ł� (�p�̑傫���͍l�����Ȃ�)�B
	 * @param start
	 * �ʂ̎n�p (�x�P��) �ł��B
	 * @param extent
	 * �ʂ̊p�̑傫�� (�x�P��) �ł��B
	 * @param type
	 * �ʂ̕����̎�� (OPEN=0�ACHORD=1�A�܂��� PIE=2)
	 */
	public void gDrawArc(int x, int y, int w, int h, int start, int extent, int type){
		imageGraphics.setColor(dc);
		imageGraphics.draw(new Arc2D.Double(x, y, w, h, start, extent, type));
		repaint();
	}
	
	/**
	 * �~�ʁi�h��Ԃ��j��`�悷�郁�\�b�h�B
	 * �F�� gColor �̃��\�b�h�Ŏw�肵�����́B
	 * 
	 * @param x
	 * �ʂ̍������ x ���W�ł��B
	 * @param y
	 * �ʂ̍������ y ���W�ł��B
	 * @param w
	 * �ȉ~�̕��ł� (�p�̑傫���͍l�����Ȃ�)�B
	 * @param h
	 * �ȉ~�̍����ł� (�p�̑傫���͍l�����Ȃ�)�B
	 * @param start
	 * �ʂ̎n�p (�x�P��) �ł��B
	 * @param extent
	 * �ʂ̊p�̑傫�� (�x�P��) �ł��B
	 * @param type
	 * �ʂ̕����̎�� (OPEN=0�ACHORD=1�A�܂��� PIE=2)
	 */
	public void gFillArc(int x, int y, int w, int h, int start, int extent, int type){
		imageGraphics.setColor(dc);
		imageGraphics.fill(new Arc2D.Double(x, y, w, h, start, extent, type));
		repaint();
	}
}
