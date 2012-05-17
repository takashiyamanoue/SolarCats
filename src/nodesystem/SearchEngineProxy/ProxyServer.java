package nodesystem.SearchEngineProxy;

import java.awt.event.ActionEvent;

import nodesystem.MessageReceiver;

import webleap.Settings;

public class ProxyServer implements MessageReceiver

{
	ProxySearchEngineDriver driver;
	String passWord;
	String proxyPort;
	ProxyServerGui gui;
	boolean noWindow;

	public ProxyServer(){
			driver=new ProxySearchEngineDriver(null,this);
			this.proxyPort=Settings.port;
			this.passWord=Settings.passWord;
			this.receiveMessage("proxtPort="+this.proxyPort);
			this.receiveMessage("passWord="+this.passWord);
			ProxyConnectionReceiver connectionReceiver=new ProxyConnectionReceiver(this,(new Integer(proxyPort)).intValue(),passWord);		
	}
	
	public void setGui(ProxyServerGui gui){
		this.gui=gui;
	}
	public void receiveMessage(String s) {
		// TODO 自動生成されたメソッド・スタブ
		if(this.noWindow){
			System.out.println(s);
		}
		else{
			if(gui==null){
				System.out.println(s);
				return;
			}
			if(this.gui.messageArea!=null)
        		this.gui.messageArea.append(s+"\n");
			else
				System.out.println(s);
		}
		
	}
	
	public void setPassword(String pass){
		this.passWord=pass;
	}
	
	public void testSearchEngineAccess(String x){
		this.driver.loadURL(x);
		this.receiveMessage(this.driver.page);
	}	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		String opt="";
		boolean noWindow=false;
		for(int i=0;i<args.length;i++){
		   opt=args[i];
		   if(opt.startsWith("-nw")){
			   System.out.println("no window");
			   noWindow=true;
		   }
		}
        ProxyServer s=new ProxyServer();
        if(!noWindow){
        	ProxyServerGui g=new ProxyServerGui();
        	g.setProxyServer(s);
        	s.setGui(g);
        	System.out.println("window");
            g.setVisible(true);
            g.setNoWindow(false);
            g.initGui();
        }
        else{
        }
	}

}
