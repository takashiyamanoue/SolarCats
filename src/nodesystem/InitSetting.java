//SAKURAGI DEVELOPED
            	
//dsr.ini��ǂݍ��݁C�ݒ���s��


package nodesystem;

import java.io.*;

public class InitSetting {
	
	final static String initFileName="dsr.ini";
	
	CommunicationNode cn;
	boolean autoConnectFlug=false;
	
	InitSetting(CommunicationNode cn){
		this.cn=cn;
	}
	public void setInit(){
		String line;
		try{
			FileReader fr=new FileReader(initFileName);
			BufferedReader br=new BufferedReader(fr);
			while((line=br.readLine())!=null){
				try{
					if(parseSetting(line))break;
				}catch(Exception e2){
					System.out.println("InitError: "+line+" �͕s���ȏ����ł�");
				}
			}
			fr.close();
			br.close();
		}catch(FileNotFoundException e){
			System.out.println(initFileName+"���ǂݍ��߂܂���ł���");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(autoConnectFlug)autoConnect();
	}
	public boolean parseSetting(String s) throws Exception{
		/*
		 * �t�H�[�}�b�g
		 * 
		 * # (�s��)					�R�����g�s
		 * VER=<string>				�o�[�W�������
		 * AUTOCONNECT=<boolean>	�N�����C�����ŃO���[�v�}�l�[�W���ւ̐ڑ����s����
		 * GM=<string>				�O���[�v�}�l�[�W���̃A�h���X
		 * PORT=<string>			�O���[�v�}�l�[�W���̃|�[�g
		 * DEBUG=<boolean>			�f�o�b�O���[�h�ŋN�����邩
		 * INITLOG=<boolean>		���O�𖈉�㏑�����邩
		 * SENDLOGTO=<string>		log.txt�̑��M��(�w�肵�Ȃ��ꍇ��f�o�b�O���[�h��ON�łȂ��ꍇ�͑��M���Ȃ�)
		 * PRINT=<string>			<string>��W���o��
		 * END						�ȉ��̍s��ǂݍ��܂Ȃ�
		 * 
		 */
		if(s.indexOf("#")==0);
		
		else if(s.indexOf("VER=")==0)
		{
			CommunicationNode.thisVer=s.substring("VER=".length());
			cn.JLabel15.setText("DSR Version: "+CommunicationNode.thisVer);
		}
		else if(s.indexOf("AUTOCONNECT=")==0)
		{
			s=s.substring("AUTOCONNECT=".length());
			s=s.toLowerCase();
			if(s.equals("true"))autoConnectFlug=true;
			else if(s.equals("false"))autoConnectFlug=false;
			else throw null;
		}
		else if(s.indexOf("GM=")==0)
		{
			cn.groupMngrAddressField.setText(s.substring("GM=".length()));
		}
		else if(s.indexOf("PORT=")==0)
		{
			cn.groupMngrPortField.setText(s.substring("PORT=".length()));
		}
		else if(s.indexOf("DEBUG=")==0)
		{
			s=s.substring("DEBUG=".length());
			s=s.toLowerCase();
			if(s.equals("true"))CommunicationNode.debugFlug=true;
			else if(s.equals("false"))CommunicationNode.debugFlug=false;
			else throw null;
		}
		else if(s.indexOf("INITLOG=")==0)
		{
			s=s.substring("INITLOG=".length());
			s=s.toLowerCase();
			if(s.equals("true"))cn.logControl.initLog=true;
			else if(s.equals("false"))cn.logControl.initLog=false;
			else throw null;
		}
		else if(s.indexOf("SENDLOGTO=")==0)
		{
			cn.logControl.setSendTo(s.substring("SENDLOGTO=".length()));
		}
		else if(s.indexOf("PRINT=")==0)
		{
			System.out.println(s.substring("PRINT=".length()));
		}
		else if(s.indexOf("END")==0)
		{
			return true;
		}
		return false;
	}
	public void autoConnect(){
		cn.joinButton_actionPerformed_Interaction1(null);
	}
}
