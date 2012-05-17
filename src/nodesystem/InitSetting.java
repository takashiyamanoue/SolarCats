//SAKURAGI DEVELOPED
            	
//dsr.iniを読み込み，設定を行う


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
					System.out.println("InitError: "+line+" は不正な書式です");
				}
			}
			fr.close();
			br.close();
		}catch(FileNotFoundException e){
			System.out.println(initFileName+"が読み込めませんでした");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(autoConnectFlug)autoConnect();
	}
	public boolean parseSetting(String s) throws Exception{
		/*
		 * フォーマット
		 * 
		 * # (行頭)					コメント行
		 * VER=<string>				バージョン情報
		 * AUTOCONNECT=<boolean>	起動時，自動でグループマネージャへの接続を行うか
		 * GM=<string>				グループマネージャのアドレス
		 * PORT=<string>			グループマネージャのポート
		 * DEBUG=<boolean>			デバッグモードで起動するか
		 * INITLOG=<boolean>		ログを毎回上書きするか
		 * SENDLOGTO=<string>		log.txtの送信先(指定しない場合やデバッグモードがONでない場合は送信しない)
		 * PRINT=<string>			<string>を標準出力
		 * END						以下の行を読み込まない
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
