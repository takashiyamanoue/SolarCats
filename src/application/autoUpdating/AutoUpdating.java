//SAKURAGI DEVELOPED
            	
//自身を圧縮する
//全体に送信
//新バージョンを受け取ったものは、jarを展開して再起動

package application.autoUpdating;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import application.fileTransfer.*;
import nodesystem.AMessage;
import nodesystem.LogControl;

import javax.swing.*;

import nodesystem.CommunicationNode;

public class AutoUpdating extends JFrame implements ActionListener{
	
	Thread th=null;
	Thread uz=null;
	File dir;
	String name;
	String rootDir;
	String zipFileName;
	CommunicationNode communicationNode;
	
	List zipList=new ArrayList();
	
	public AutoUpdating(CommunicationNode gui){
		this.communicationNode=gui;
	}
	boolean cacheUseFlug=false;
	public boolean jarFlug=false;
	public boolean hispeedFlug=false;
	public AutoUpdating(CommunicationNode gui,boolean cacheUseFlug,boolean jarFlug,boolean hispeedFlug){
		this.communicationNode=gui;
		this.cacheUseFlug=cacheUseFlug;
		this.jarFlug=jarFlug;
		this.hispeedFlug=hispeedFlug;
		rootDir=System.getProperty("user.dir");
		rootDir=rootDir.substring(rootDir.indexOf(':')+1);
		
		setTitle("Automatic Updating");
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		setSize(400,200);
		
		setFont(new Font("Dialog", Font.BOLD, 12));
		label1.setForeground(new java.awt.Color(102,102,153));
		label1.setBounds(10,10,400,30);
		label1.setText("Process");
		getContentPane().add(label1);
		
		label2.setBounds(20,30,350,30);
		label2.setText("");
		getContentPane().add(label2);
		
		button.setBounds(300,140,80,24);
		button.setActionCommand("close");
		button.setText("CLOSE");
		button.setEnabled(false);
		getContentPane().add(button);
		
		button.addActionListener(this);
		
		setVisible(true);
		th=new MainThread();
		th.start();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("close"))setVisible(false);
	}
	
	JButton button=new JButton();
	JLabel label1=new JLabel();
	public JLabel label2=new JLabel();
	
	//圧縮メインルーチン
	long zipsize;
	public void zip(){
		//テンポラリフォルダ作成
		File tmpDir=new File(System.getProperty("user.dir")+File.separator+"tmp");
		if(!tmpDir.exists())tmpDir.mkdir();
		
		zipFileName="tmp"+File.separator+"dsr_"+nodesystem.CommunicationNode.thisVer+".zip";
		File zipFile=new File(zipFileName);
		zipsize=zipFile.length();
		if(!zipFile.exists() || !cacheUseFlug){ 
			communicationNode.logControl.printtimelf("ZIP_START");
			dir=new File(rootDir);
			ZipOutputStream zos=null;
			try {
				zos=new ZipOutputStream(new FileOutputStream(zipFile));
			}catch(FileNotFoundException e){
				if(zos!=null){
					try{
						zos.close();
					}catch(Exception e2){}
				}
				e.printStackTrace();
			}
			getFilesName(dir);
			for(int i=0;i<zipList.size();i++){
				File file=new File((String)zipList.get(i));
				addZip(zos,file);
			}
			try{
				zos.close();
			}catch(Exception e){e.printStackTrace();}
			communicationNode.logControl.printtimelf("ZIP_END");
		}
		else System.out.println("Zip Skip");
	}
	
	//ファイル名の取得
	public void getFilesName(File dir){
		try{
			String[] fileNameList=dir.list();
			File[] files=new File[fileNameList.length];
			String logFileName=LogControl.getLogfileName();
			for(int i=0;i<fileNameList.length;i++){
				String path=dir.getPath()+File.separator+fileNameList[i];
				files[i]=new File(path);
				//ファイルの処理
				if(files[i].isFile()){
					String file;
					file=files[i].getPath();
					//TODO:Winowsのみの処理
					if(fileNameList[i].indexOf("Thumbs.db")!=-1);
					else if(path.equals(dir.getPath()+File.separator+logFileName));
					else zipList.add(file);
				}
				//ディレクトリの処理
				else if(files[i].isDirectory() &&
						!files[i].getPath().equals(rootDir+File.separator+"tmp") &&
						!files[i].getPath().equals(rootDir+File.separator+"commondata") &&
						!files[i].getPath().equals(rootDir+File.separator+"nodedata")){
					//System.out.println(" "+files[i].getPath());
					getFilesName(new File(files[i].getPath()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//アーカイブ追加
	private void addZip(ZipOutputStream zos,File file){
		BufferedInputStream bis=null;
		try{
			bis=new BufferedInputStream(new FileInputStream(file));
			name=file.getPath().substring(rootDir.length()+1);
			ZipEntry target=new ZipEntry(name);
			//System.out.println(name);
			label2.setText("Compressing: "+name);
			Thread.sleep(1);
			zos.putNextEntry(target);
			byte buf[]=new byte[1024];
			int count;
			while((count=bis.read(buf,0,1024))!=-1){
				zos.write(buf,0,count);
			}
			bis.close();
			zos.closeEntry();
		}catch(Exception e){
			e.printStackTrace();
			if(bis!=null){
				try{
					bis.close();
				}catch(Exception e2){}
			}
		}
	}
	
	
	public void sendFile(){
		FileTransferFrame ft;
		communicationNode.spawnApplication("FileTransferFrame");
		ft=(FileTransferFrame)communicationNode.applicationManager.getRunningApplication("FileTransferFrame").application;
		ft.setVisible(false);
		ft.copyFromFileNameField.setText(System.getProperty("user.dir")+File.separator+"tmp"+File.separator+"dsr_"+nodesystem.CommunicationNode.thisVer+".zip");
		//全ノードにVerNo.を伝え、それぞれのFileTrasferFrameのメソッドを実行
		String sending="broadcast shell verup "+CommunicationNode.thisVer+communicationNode.commandTranceiver.endOfACommand;
		AMessage m=new AMessage();
		m.setHead(sending);
		communicationNode.commandTranceiver.sendCommand(m);
		
//		ft.sendBinalyToAll("update");
		ft.sendVerUpBinalyToAll(this,communicationNode);
		sending="broadcast shell endVerup"+communicationNode.commandTranceiver.endOfACommand;
		m.setHead(sending);
		communicationNode.commandTranceiver.sendCommand(m);
		
	}
	
	//解凍
	public void unzip(String s){
		if(jarFlug){
           	try{
           		communicationNode.logControl.printtimelf("UNZIP_START");
           		String command="jar xvf "+s;
           		communicationNode.setVisible(false);
           		Process p=Runtime.getRuntime().exec(command);
           		InputStream is=p.getInputStream();
           		BufferedReader br=new BufferedReader(new InputStreamReader(is));
           		String line;
           		while((line=br.readLine())!=null){
           			System.out.println(line);
           		}
           		communicationNode.logControl.printtimelf("UNZIP_END");
           		
           		communicationNode.logControl.sendLog();
           		
				//再起動
				try {
					Process p2=Runtime.getRuntime().exec(System.getProperty("user.dir")+File.separator+"student.bat");
					System.exit(0);
				}catch (Exception e2){e2.printStackTrace();}
           		
           	}catch(Exception e){
           		System.out.println("コマンドが不正です");
           	}
		}
		else{
			uz=new UnzipThread(s);
			uz.start();
		}
	}
	
	class MainThread extends Thread{
		public void run(){
			zip();
			label2.setText("Compressing: COMPLEATED!!");
			label2.setText("Sending..."+"0/"+zipsize);
			communicationNode.logControl.printtimelf("SEND_START");
			sendFile();
			communicationNode.logControl.printtimelf("SEND_END");
			button.setEnabled(true);
			label2.setText("Sending: COPLEATED!!");
			th.stop();
		}
	}
	class UnzipThread extends Thread{
		String s="";
		public UnzipThread(String s){
			this.s=s;
		}
		public void run(){
			communicationNode.logControl.printtimelf("UNZIP_START");
			ZipInputStream zis=null;
			FileOutputStream fos=null;
			int entrysize,entrycount=0;
			try{
				
				String sending=communicationNode.thisID+"(Ver."+communicationNode.thisVer+")"+" versionup start\n";
				communicationNode.chatOut.append(sending);
				sending="broadcast shell chat "+sending+communicationNode.commandTranceiver.endOfACommand;
				AMessage m=new AMessage();
				m.setHead(sending);
			    communicationNode.commandTranceiver.sendCommand(m);
				
				System.out.println(s);
				ZipFile zf=new ZipFile(s);
				entrysize=zf.size();
				ProgressWindow pw=new ProgressWindow(entrysize);
				zis=new ZipInputStream(new FileInputStream(s));
				ZipEntry ze=null;
				String fileName=null;
				while((ze=zis.getNextEntry())!=null){
					//アーカイブ情報取得
					fileName=ze.getName();
					//fileName=fileName.substring(fileName.lastIndexOf(File.separator)+1,fileName.length());
					byte[] buf=new byte[1000000];
					BufferedInputStream bis=new BufferedInputStream(zis);
					int size=bis.read(buf,0,1000000);
					zis.closeEntry();
					//書き出し
					//TODO:Windowsのみのやり方
					if(!fileName.substring(fileName.lastIndexOf(File.separator)+1,fileName.length()).equals("Thumbs.db")){
						fos=new FileOutputStream(fileName);
						if(size<0)size=0;
						fos.write(buf,0,size);
					}
					entrycount++;
					pw.pb.setValue(entrycount);
					
				}
		        
				communicationNode.logControl.printtimelf("UNZIP_END");
				communicationNode.logControl.sendLog();
				
				//再起動
				try {
					sending=communicationNode.thisID+"(Ver."+communicationNode.thisVer+")"+" versionup compleated!\n";
					communicationNode.chatOut.append(sending);
					sending="broadcast shell chat "+sending+communicationNode.commandTranceiver.endOfACommand;
					AMessage mx=new AMessage();
					mx.setHead(sending);
				    communicationNode.commandTranceiver.sendCommand(mx );
					
					Process p=Runtime.getRuntime().exec(System.getProperty("user.dir")+File.separator+"student.bat");
					p.waitFor();
					System.exit(0);
				}catch (IOException e2){e2.printStackTrace();}
				
			}catch(Exception e){e.printStackTrace();
			}finally{
				try{
					zis.close();
					if(fos!=null)fos.close();
				}catch(Exception e2){
					
					String sending=communicationNode.thisID+"(Ver."+communicationNode.thisVer+")"+" versionup failed!\n";
					communicationNode.chatOut.append(sending);
					sending="broadcast shell chat "+sending+communicationNode.commandTranceiver.endOfACommand;
					AMessage mx=new AMessage();
					mx.setHead(sending);
				    communicationNode.commandTranceiver.sendCommand(mx);
				
					e2.printStackTrace();
					System.out.println("解凍に失敗しました");
				}
			}
			
		}
	}
	public class ProgressWindow extends JFrame{
		JProgressBar pb;
		ProgressWindow(int max){
			getContentPane().setLayout(new java.awt.FlowLayout());
			pb=new JProgressBar(0,max);
			pb.setValue(0);
			getContentPane().add(pb);
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("Please wait...");
			setSize(200,80);
			setVisible(true);
		}
	}
	
}
