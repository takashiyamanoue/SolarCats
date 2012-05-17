//SAKURAGI DEVELOPED

//log.txtの管理を行う

package nodesystem;

import java.io.*;

public class LogControl {
	
	static String logFileName="log.txt";
	static String cvsFileName="log.csv"; 
	CommunicationNode cn;
	String sendTo="";
	String buf="";
	String lf="\n";
	boolean debug=false;
	boolean initLog=false;
	
	LogControl(CommunicationNode cn){
		this.cn=cn;
	}
	
	public void init(){
		if(CommunicationNode.debugFlug){
			lf=System.getProperty("line.separator");
			debug=true;
			addBuf("------------");
			addBuf("VER:"+CommunicationNode.thisVer);
			addBuf("WakeUp:"+gettime());
			printBuf();
			initLog=false;
		}
	}
	public void addBuf(String s){
		buf=buf+s+lf;
	}
	public void printBuf(){
		print(buf);
		buf="";
	}
	public void printtime(){
		if(debug)write(gettime());
	}
	public void printtimelf(String s){
		if(debug)write(s+":"+gettime()+lf);
	}
	public void println(String s){
		if(debug)write(s+lf);
	}
	public void print(String s){
		if(debug)write(s);
	}
	public void setSendTo(String s){
		if(CommunicationNode.debugFlug)sendTo=s;
	}
	public String gettime(){
		return Long.toString(System.currentTimeMillis());
	}

	public void write(String s){
		try{
			BufferedWriter br=new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(logFileName,!initLog)));
			br.write(s);
			br.close();
		}catch(Exception e){
			System.out.println("ログファイルの書き込みエラーです");
		}
	}
	public boolean sendLog(){
		if(!sendTo.equals("")){
			System.out.println("ログ送信開始");
       		try{
       			String com="cmd /c copy "+logFileName+" "+sendTo+CommunicationNode.thisVer+"_"+cn.serialNo;
       			System.out.println(com);
       			Process p=Runtime.getRuntime().exec(com);
       			p.waitFor();
       		}catch(Exception e){
       			System.out.println("ログを送信出来ませんでした");
       			e.printStackTrace();
       		}
		}
		return true;
	}
	public boolean cleanup(String ver,String id){
		if(!sendTo.equals("")){
			int max=0;
			try{
				max=Integer.parseInt(id);
			}catch(Exception e){
				System.out.println("CLEAN_UPの書式エラーです");
			}
			String fileName="";
			for(int i=2;i<=max;i++){
				String line;
				fileName=sendTo+ver+"_"+id;
				long startTime=0;
				long endTime=0;
				
				try{
					BufferedReader br=new BufferedReader(new FileReader(fileName));
					while((line=br.readLine())!=null){
						if(line.indexOf("RECEIVE_START:")==0){
							line=line.substring("RECEIVE_START:".length());
							try{
								startTime=Long.parseLong(line);
							}catch(Exception e3){
								System.out.println("不正なLOGファイルです");
							}
						}else if(line.indexOf("RECEIVE_END:")==0){
							line=line.substring("RECEIVE_END:".length());
							try{
								endTime=Long.parseLong(line);
							}catch(Exception e3){
								System.out.println("不正なLOGファイルです");
							}
						}

					}
					br.close();
					addBuf(i+","+(endTime-startTime));
					
				}catch(FileNotFoundException e){
					System.out.println(fileName+"が読み込めませんでした");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			String tmp=logFileName;
			logFileName=cvsFileName;
			printBuf();
			logFileName=tmp;
		}
		return true;
	}
	public static String getLogfileName(){
		return logFileName;
	}
}
