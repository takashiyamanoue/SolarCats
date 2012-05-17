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
 * ���͎x���{�^����`�t�@�C��������͎x���{�^����z�u���邽�߂̃N���X
 * 
 * @author rn_magi
 */
public class ButtonEdit {
	JPanel edit_area_panel		= new JPanel();
	JPanel edit_button_area_panel		= new JPanel();

	/**
	 * <code>edit_button_area_layout</code> �̃R�����g
	 * ���̃C���X�^���X�ɐ������ꂽ���͎x���{�^�����i�[�����
	 */
	JToolBar edit_button_area_toolbar	= new JToolBar();
	 
	GridBagLayout edit_button_area_layout	= new GridBagLayout();
	GridBagConstraints gbc					= new GridBagConstraints();
	
	/**
	 * <code>edit_button</code> �̃R�����g
	 * ���͎x���{�^����`�t�@�C�������݂��Ȃ������ꍇ�A
	 * ���̃C���X�^���X���Ɋi�[����Ă���l��
	 * ���͎x���{�^�����쐬����悤�ɂȂ��Ă���
	 */
	EditButtonList[] edit_button = {
			new EditButtonList(
				"����" ,
				"<html><pre>�������������Ȃ��\n  | ��������\n�����s����</pre></html>" ,
				new String[] {"���� ��������� �Ȃ��\n" , "  | \n" , "�����s����"} ,
				130 , 25 , new Color(140,220,200)
			),
			new EditButtonList(
				"�����`�����łȂ����" ,
				"<html><pre>�������������Ȃ��\n  | ��������\n�����s���C�����łȂ����\n  | ��������\n�����s����</pre></html>" ,
				new String[] {"���� ��������� �Ȃ��\n" , "  | \n" , "�����s���C�����łȂ����\n" , "  | \n","�����s����"} ,
				130 , 25 , new Color(140,220,200)
			),
			new EditButtonList(
				"���₵�Ȃ���J��Ԃ�" ,
				"<html><pre>��ϐ��� �� �ᐔ�l�� ���� �ᐔ�l�� �܂� 1 �����₵�Ȃ���C\n  | ��������\n���J��Ԃ�</pre></html>" ,
				new String[] {"��ϐ��� �� �ᐔ�l�� ���� �ᐔ�l�� �܂� 1 �����₵�Ȃ���C\n" , "  | \n"  ,"���J��Ԃ�"} ,
				130 , 25 , new Color(160,220,200)
			),
			new EditButtonList(
				"���炵�Ȃ���J��Ԃ�" ,
				"<html><pre>��ϐ��� �� �ᐔ�l�� ���� �ᐔ�l�� �܂� 1 �����炵�Ȃ���C\n  | ��������\n���J��Ԃ�</pre></html>" ,
				new String[] {"��ϐ��� �� �ᐔ�l�� ���� �ᐔ�l�� �܂� 1 �����炵�Ȃ���C\n" , "  | \n" , "���J��Ԃ�"}  ,
				130 , 25 , new Color(160,220,200)
			),
			new EditButtonList(
				"�`�̊ԁC�J��Ԃ�" ,
				"<html><pre>��������� �̊ԁC\n  | ��������\n���J��Ԃ�</pre></html>" ,
				new String[] {"��������� �̊ԁC\n" , "  | \n" , "���J��Ԃ�"} ,
				130 , 25 , new Color(180,220,200)
			),
			new EditButtonList(
				"�J��Ԃ��`�ɂȂ�܂Ŏ��s" ,
				"<html><pre>�J��Ԃ��C\n  | ��������\n���C ��������� �ɂȂ�܂Ŏ��s����</pre></html>" ,
				new String[] {"�J��Ԃ��C\n" , "  | \n" , "���C ��������� �ɂȂ�܂Ŏ��s����"} ,
				130 , 25 , new Color(180,220,200)
			),
			new EditButtonList(
				"����" ,
				"<html><pre>��ϐ��� �� input()</pre></html>" ,
				new String[] {"��ϐ��� �� input()"} ,
				65 , 25 , new Color(255,255,204)
			),
			new EditButtonList(
				"�o��" ,
				"<html><pre>��o�͕��� ���������</pre></html>" ,
				new String[] {"��o�͕��� ���������"} ,
				65 , 25 , new Color(255,255,204)
			),
			new EditButtonList(
				"���s���o��" ,
				"<html><pre>��o�͕��� �����s�Ȃ��ň������</pre></html>" ,
				new String[] {"��o�͕��� �����s�Ȃ��ň������"} ,
				65 , 25 , new Color(255,255,204)
			),
			new EditButtonList(
				"���",
				"<html><pre>��ϐ��� �� �Ꭾ��</pre></html>" ,
				new String[] {"��ϐ��� �� �Ꭾ��"} ,
				65 , 25 , new Color(255,255,204)
			),
			new EditButtonList(
				"����" ,
				"<html><pre>���� ��ϐ���</pre></html>" ,
				new String[] {"���� ��ϐ���"} ,
				65 , 25 , new Color(255,204,255)
			),
			new EditButtonList(
				"��" ,
				"<html><pre>�Ꭾ�� �� �Ꭾ��</pre></html>" ,
				new String[] {"�Ꭾ�� �� �Ꭾ��"} ,
				32 , 25 , new Color(204,204,204)
			),
			new EditButtonList(
				"��" ,
				"<html><pre>�Ꭾ�� �� �Ꭾ��</pre></html>" ,
				new String[] {"�Ꭾ�� �� �Ꭾ��"} ,
				32 , 25 , new Color(204,204,204)
			),
			new EditButtonList(
				"��" ,
				"<html><pre>�Ꭾ�� �� �Ꭾ��</pre></html>" ,
				new String[] {"�Ꭾ�� �� �Ꭾ��"} ,
				32 , 25 , new Color(204,204,204)
			),
			new EditButtonList(
				"��" ,
				"<html><pre>�Ꭾ�� �� �Ꭾ��</pre></html>" ,
				new String[] {"�Ꭾ�� �� �Ꭾ��"} ,
				32 , 25 , new Color(204,204,204)
			),
			new EditButtonList(
				"��" ,
				"<html><pre>�Ꭾ�� �� �Ꭾ��</pre></html>" ,
				new String[] {"�Ꭾ�� �� �Ꭾ��"} ,
				32 , 25 , new Color(204,204,204)
			),
			new EditButtonList(
				"��" ,
				"<html><pre>�Ꭾ�� �� �Ꭾ��</pre></html>" ,
				new String[] {"�Ꭾ�� �� �Ꭾ��"} ,
				32 , 25 , new Color(204,204,204)
			),
			new EditButtonList(
				"����" ,
				"<html><pre>����</pre></html>" ,
				new String[] {"����"} ,
				65 , 25 , new Color(190,190,190)
			),
			new EditButtonList(
					"�܂���" ,
					"<html><pre>�܂���</pre></html>" ,
					new String[] {"�܂���"} ,
					65 , 25 , new Color(190,190,190)
			),
			new EditButtonList(
					"�łȂ�" ,
					"<html><pre>�łȂ�</pre></html>" ,
					new String[] {"�łȂ�"} ,
					65 , 25 , new Color(190,190,190)
			),
			new EditButtonList(
					"�u�v" ,
					"<html><pre>�u�ᕶ�����v</pre></html>" ,
					new String[] {"�u�ᕶ�����v"} ,
					65 , 25 , new Color(180,180,180)
			)
		};
	
	MainGUI gui; // added by t.yamanoue, 2005 aug.

	/**
	 * ���͎x���{�^�����i�[����c�[���o�[�̏����ݒ�����߂�
	 * 
	 * @param ListFile
	 * ���͎x���{�^����`�t�@�C���̐�΃p�X
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
	 * ���͎x���{�^����`�t�@�C����ǂݍ��݃c�[���o�[�ɔz�u����
	 * ��`�t�@�C���̓ǂݍ��݂Ɏ��s�����ꍇ�A
	 * �G���[�����V�X�e���R���\�[���ɏo�͂��A
	 * �f�t�H���g�̓��͎x���{�^����z�u����
	 * 
	 * @param list
	 * ���͎x���{�^����`�t�@�C���̐�΃p�X
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
		//	edit_button_area_toolbar.add(new MyJLabel("�v���O�������͎x���{�^��"));
			edit_button_area_toolbar.add(edit_button_area_panel);
		} catch (Exception ex){
			System.out.println(ex);
			EditButtonDefault();
		}
	}
	
	/**
	 * �f�t�H���g�̓��͎x���{�^���̔z�u
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
		edit_button_area_toolbar.add(new MyJLabel("�i�v���O�������͎x���{�^���j"));
		edit_button_area_toolbar.add(edit_button_area_panel);
	}
}
