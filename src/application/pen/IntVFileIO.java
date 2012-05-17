package application.pen;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

public class IntVFileIO {
	private Hashtable OpenFileTable = new Hashtable();	// �J���Ă���t�@�C�����Ǘ�����e�[�u��
	private String LineSeparator =System.getProperty("line.separator");	// System �Ɏg�p����Ă�����s�R�[�h�̎擾
	
	public IntVFileIO(){
		// �t�@�C���e�[�u���̏�����
		
		OpenFileTable.put(new Integer(0),  new Object[]{"input", "�W������"});
		OpenFileTable.put(new Integer(1),  new Object[]{"output", "�W���o��"});
		OpenFileTable.put(new Integer(2),  new Object[]{"error", "�G���[�o��"});
	}
	
	public Integer FileOpenRead(String FilePath){
		// �ǂݍ��݃��[�h �Ńt�@�C�����J��
		// �t�@�C�������݂��Ȃ������ꍇ�A�G���[��f����~
		
		File file = new File(FilePath);
		try {
			// �t�@�C���e�[�u���ɒǉ�
			Integer ID = FileID();
			
			String code = getCharSet(file);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), code);
			BufferedReader reader = new BufferedReader(isr);
			
			OpenFileTable.put(ID, new Object[]{ "Read", reader });
			return ID;
		} catch (FileNotFoundException e) {
			// �w�肳�ꂽ�p�X���Ŏ������t�@�C�����J���Ȃ��������̏���
			// e.printStackTrace();
			Error(e.getLocalizedMessage());
			return null;
		} catch (UnsupportedEncodingException e) {
			// �G���R�[�h�����ł��Ȃ������ꍇ
			// e.printStackTrace();
			Error(e.getLocalizedMessage());
			return null;
		}
	}
	
	public Integer FileOpenWrite(String FilePath){
		// �������݃��[�h �Ńt�@�C�����I�[�v������
		// �t�@�C�������݂����ꍇ�A�I�[�o�[���C�g����
		
		File file = new File(FilePath);
		try {
			// �t�@�C���e�[�u���ɒǉ�
			Integer ID = FileID();
			OpenFileTable.put(ID, new Object[]{ "Write", new BufferedWriter(new FileWriter(file))});
			return ID;
		} catch (IOException e) {
			// ���o�͏����̎��s�A�܂��͊��荞�݂̔���
			// e.printStackTrace();
			Error(e.getLocalizedMessage());
			return null;
		}
	}
	
	public Integer FileOpenAppend(String FilePath){
		// �ǋL���[�h �Ńt�@�C�����I�[�v������
		// �t�@�C�������݂��Ȃ������ꍇ�A�t�@�C�����쐬����
		
		File file = new File(FilePath);
		try {
			BufferedWriter bf;
			if( file.isFile() ) {
				// �t�@�C���̓��e�� add �ɑޔ�
				String code = getCharSet(file);
				InputStreamReader isr = new InputStreamReader(new FileInputStream(file), code);
				BufferedReader reader = new BufferedReader(isr);
				String read = "";
				String add	= "";
				while(true){
					read = reader.readLine();
					if(read != null){
						add += read + LineSeparator;
					}else{
						reader.close();
						break;
					}
				}
				
				// FileWriter �Ńt�@�C�����J���A�ޔ��������f�[�^�̏�������
				bf = new BufferedWriter(new FileWriter(file));
				bf.write(add);
				bf.flush();
			} else {
				bf = new BufferedWriter(new FileWriter(file));
			}
			// �t�@�C���e�[�u���ɒǉ�
			Integer ID = FileID();
			OpenFileTable.put(ID, new Object[]{ "Append", bf});
			return ID;
		} catch (UnsupportedEncodingException e) {
			// �G���R�[�h�����ł��Ȃ������ꍇ
			// e.printStackTrace();
			Error(e.getLocalizedMessage());
			return null;
		} catch (IOException e) {
			// ���o�͏����̎��s�A�܂��͊��荞�݂̔���
			// e.printStackTrace();
			Error(e.getLocalizedMessage());
			return null;
		}
	}
	
	public String FileIsFile(String FilePath){
		// �t�@�C�������݂���ꍇ	: true
		// �t�@�C�������݂��Ȃ��ꍇ	: false
		
		File file = new File(FilePath);
		Boolean flag = new Boolean(file.isFile());
		
		return flag.toString();
	}
	
	public void FileReName(String FilePath1, String FilePath2){
		// �t�@�C���̃��l�[��
		
		File file1 = new File(FilePath1);
		File file2 = new File(FilePath2);
		if( file1.isFile() ){
			if( file2.isFile() ){
				// ���l�[����Ƀt�@�C�������݂���ꍇ
				Error("���l�[����Ƀt�@�C�������݂��܂�");
			} else {
				if( file1.renameTo(file2) ){
					// �t�@�C���̃��l�[�������������ꍇ
				} else {
					// �t�@�C���̃��l�[�������s�����ꍇ
					Error("�t�@�C���̃��l�[�������s���܂���");
				}
			}
		} else {
			// ���l�[�����̃t�@�C�������݂��Ȃ������ꍇ
			Error("���l�[�����̃t�@�C�������݂��܂���");
		}
	}
	
	public void FileRemove(String FilePath){
		// �t�@�C���̍폜
		
		File file = new File(FilePath);
		if( file.isFile() ){
			if( file.delete() ) {
				// �t�@�C���̍폜�ɐ��������ꍇ
				
			} else {
				// �t�@�C���̍폜�����s�����ꍇ
				Error("�t�@�C���̍폜�Ɏ��s���܂���");
			}
		} else {
			// ���l�[�����̃t�@�C�������݂��Ȃ������ꍇ
			Error("�폜����t�@�C�������݂��܂���");
		}
	}
	
	public String FileGetStr(Integer ID, Integer n){
		// ID �Ŏw�肳�ꂽ�t�@�C������f�[�^�� n���� �ǂݍ���
		// Windows �̏ꍇ�A���s�R�[�h�ɒ��� -> "\r\n" �Ȃ̂� [ 2���� ] �Ƃ��Ĉ�����
		
		if( OpenFileTable.get(ID) != null){
			Object[] obj = (Object[]) OpenFileTable.get(ID);
			if( obj[0].equals("Read") ){
				BufferedReader bf = (BufferedReader) obj[1];
				int c;
				String str = "";
				try {
					// n�������̃f�[�^�ǂݍ���
					for( int i = 0; i < n.intValue(); i++) {
						c = bf.read();
						if( c != -1){
							str += new Character((char) c).toString();
						} else if( str.equals("")){
							return new String(new Character((char) c).toString());
						} else {
							break;
						}
					}
					return str;
				} catch (IOException e) {
					// ���o�͏����̎��s�A�܂��͊��荞�݂̔���
					// e.printStackTrace();
					Error(e.getLocalizedMessage());
					return null;
				}
			} else if( obj[0].equals("input") ){
				// �R���\�[����ʂ�����͂��鏈���������ɒǉ�
				return null;
			} else {
				// �t�@�C����Ԃ� [ Write ] �ȃt�@�C����ǂݍ������Ƃ������̏���
				Error("�t�@�C��ID [ " + ID + " ] �� WriteOnly �ł�");
				return null;
			}
		} else {
			// �w�肳�ꂽID�����݂��Ȃ��ꍇ�̏���
			Error("�t�@�C��ID [ " + ID + " ] �͑��݂��܂���");
			return null;
		}
	}
	
	public String FileGetLine(Integer ID){
		// ID �Ŏw�肳�ꂽ�t�@�C������f�[�^��1�s�ǂݍ���
		// �Ȃ��A���s�R�[�h�� [ �t�����ĂȂ� ] ��ԂŕԂ��Ă��܂�
		
		if( OpenFileTable.get(ID) != null){
			Object[] obj = (Object[]) OpenFileTable.get(ID);
			if( obj[0].equals("Read") ){
				BufferedReader bf = (BufferedReader) obj[1];
				try {
					// ��s���̃f�[�^��ǂݍ���
					String str = bf.readLine();
					if( str == null ){
						int c = -1;
						return new String(new Character((char) c).toString());
					}
					return str;
				} catch (IOException e) {
					// ���o�͏����̎��s�A�܂��͊��荞�݂̔���
					// e.printStackTrace();
					Error(e.getLocalizedMessage());
					return null;
				}
			} else if( obj[0].equals("input") ){
				// �R���\�[����ʂ�����͂��鏈���������ɒǉ�
				return null;
			} else {
				// �t�@�C����Ԃ� [ Write ] �ȃt�@�C����ǂݍ������Ƃ������̏���
				Error("�t�@�C��ID [ " + ID + " ] �� WriteOnly �ł�");
				return null;
			}
		} else {
			// �w�肳�ꂽID�����݂��Ȃ��ꍇ�̏���
			Error("�t�@�C��ID [ " + ID + " ] �͑��݂��܂���");
			return null;
		}
	}
	
	public void FilePutLine(Integer ID, String str){
		// ID �Ŏw�肳�ꂽ�t�@�C���� �f�[�^(str) �ɉ��s���݂ŏ�������
		
		if( OpenFileTable.get(ID) != null){
			Object[] obj = (Object[]) OpenFileTable.get(ID);
			if( obj[0].equals("Write") || obj[0].equals("Append") ){
				BufferedWriter bf = ( BufferedWriter ) obj[1];
				try {
					// �t�@�C���ւ̏�������
					bf.write(str);
					bf.newLine();
				} catch (IOException e) {
					// ���o�͏����̎��s�A�܂��͊��荞�݂̔���
					// e.printStackTrace();
					Error(e.getLocalizedMessage());
				}
			} else if( obj[0].equals("output") ){
				// �R���\�[����ʂ֏o�͂��鏈���������ɒǉ�
			} else if( obj[0].equals("error") ){
				// �R���\�[����ʂ֏o�͂��鏈���������ɒǉ�
			} else {
				// �t�@�C����Ԃ� [ Read ] �ȃt�@�C���ɏ����������Ƃ������̏���
				Error("�t�@�C��ID [ " + ID + " ] �� ReadOnly �ł�");
			}
		} else {
			// �w�肳�ꂽID�����݂��Ȃ��ꍇ�̏���
			Error("�t�@�C��ID [ " + ID + " ] �͑��݂��܂���");
		}
	}
	
	public void FilePutString(Integer ID, String str){
		// ID �Ŏw�肳�ꂽ�t�@�C���� �f�[�^(str) ����������
		
		if( OpenFileTable.get(ID) != null){
			Object[] obj = (Object[]) OpenFileTable.get(ID);
			if( obj[0].equals("Write") || obj[0].equals("Append") ){
				BufferedWriter bf = ( BufferedWriter ) obj[1];
				try {
					// �t�@�C���ւ̏�������
					bf.write(str);
				} catch (IOException e) {
					// e.printStackTrace();
					Error(e.getLocalizedMessage());
					e.printStackTrace();
				}
			} else if( obj[0].equals("output") ){
				// �R���\�[����ʂ֏o�͂��鏈���������ɒǉ�
			} else if( obj[0].equals("error") ){
				// �R���\�[����ʂ֏o�͂��鏈���������ɒǉ�
			} else {
				// �t�@�C����Ԃ� [ Read ] �ȃt�@�C���ɏ����������Ƃ������̏���
				Error("�t�@�C��ID [ " + ID + " ] �� ReadOnly �ł�");
			}
		} else {
			// �w�肳�ꂽID�����݂��Ȃ��ꍇ�̏���
			Error("�t�@�C��ID [ " + ID + " ] �͑��݂��܂���");
		}
	}
	
	public void FileFlush(Integer ID){
		// �X�g���[�����t���b�V��

		if( OpenFileTable.get(ID) != null){
			Object[] obj = (Object[]) OpenFileTable.get(ID);
			if( obj[0].equals("Write") || obj[0].equals("Append") ){
				BufferedWriter bf = ( BufferedWriter ) obj[1];
				try {
					// �X�g���[�����t���b�V��
					bf.flush();
				} catch (IOException e) {
					// ���o�͏����̎��s�A�܂��͊��荞�݂̔���
					// e.printStackTrace();
					Error(e.getLocalizedMessage());
				}
			} else {
				// �t�@�C����Ԃ� [ Read ] �ȃt�@�C���ɏ����������Ƃ������̏���
				Error("�t�@�C��ID [ " + ID + " ] �� ReadOnly �ł�");
			}
		} else {
			// �w�肳�ꂽID�����݂��Ȃ��ꍇ�̏���
			Error("�t�@�C��ID [ " + ID + " ] �͑��݂��܂���");
		}
	}
	
	public void FileClose(Integer ID){
		// �t�@�C���̃N���[�Y����
		// FileIDRemove(ID) �ŕ����t�@�C���� FileTable ����폜
		
		if( OpenFileTable.get(ID) != null){
			Object[] obj = (Object[]) OpenFileTable.get(ID);
			try {
				// �t�@�C���̃N���[�Y����
				// �t�@�C���e�[�u������ID���폜
				if( obj[0].equals("Read") ){
					BufferedReader bf = ( BufferedReader ) obj[1];
					bf.close();
					FileIDRemove(ID);
				} else if( obj[0].equals("Write") || obj[0].equals("Append")){
					BufferedWriter bf = ( BufferedWriter ) obj[1];
					bf.flush();
					bf.close();
					FileIDRemove(ID);
				} else {
					FileIDRemove(ID);
				}
			} catch (IOException e) {
				// ���o�͏����̎��s�A�܂��͊��荞�݂̔���
				// e.printStackTrace();
				Error(e.getLocalizedMessage());
			}
		} else {
			// �w�肳�ꂽID�����݂��Ȃ��ꍇ�̏���
			Error("�t�@�C��ID [ " + ID + " ] �͑��݂��܂���");
		}
	}
	
	public Integer FileID(){
		// �󂢂Ă��� FileID �ԍ��̎Ⴂ�����璲�ׂ�

		int i;
		for( i = 3; true; i++){
			if( OpenFileTable.get(new Integer(i)) == null ){
				break;
			}
		}
		return new Integer(i);
	}
	
	public void FileIDRemove(Integer ID){
		// FileTable ���� FileID ���J�����鏈��

		OpenFileTable.remove(ID);
	}
	
	public void FileAllClose(){
		// �I�[�v������Ă���t�@�C�������ׂĕ���
		
		Object[] obj = OpenFileTable.keySet().toArray();
		
		for(int i = 0; i < obj.length; i++){
			Integer ID = ( Integer ) obj[i];
			FileClose(ID);
		}
	}
	
	public void Error(String msg){
		MainGUI.Run_flag = false;
		new ConsoleAppend("### " + MainGUI.run_point.getLineCount() + "�s�ڂŃG���[�ł�\n");
		new ConsoleAppend("### " + msg + "\n");
		FileAllClose();
		throw new ThreadRunStop();
	}

	public static String getCharSet(File file) {
		String code = "EUC_JP";
		FileInputStream in = null;
		int nch;

		try {
			in = new FileInputStream(file);
			while((nch=in.read())!=-1){
				if(nch==0x1B){
					code = "JIS";
					break;
				} else if(nch>0x80 && nch<0xA1){
					code = "Shift_JIS";
					break;
				}
			}
			in.close();
		} catch(IOException ex){
		} 
		return code;
	}
}
