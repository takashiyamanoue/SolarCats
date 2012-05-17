package nodesystem.proxyServer;
import java.io.*;
import java.net.*;
import java.util.*;

public class ProxyServer implements Runnable
{
	public final static int DEFAULT_PORT = 8089;

	public boolean fDebugOn = true;

	protected int 			fPort; //which port we actually end up using
	protected ServerSocket 	fMainListenSocket = null; //main server socket
	public boolean 		fContinueListening = true;
	public PageCache pageCache;
	public static boolean rootExistence = true;
	public static String parentAdress;
	Thread me;


	/**
	*Instantiate and init
	*/
	public ProxyServer(int port) {

		if (port == 0) port = DEFAULT_PORT;
		fPort = port;
		this.pageCache=new PageCache(this);

	    if (fDebugOn) System.out.println("done instantiating...");
	    this.start();

	}//HttpdMulti


	/**
	*	This method waits for an incoming connection,
	*	opens input and output streams on that connection,
	*	then uses HttpTransactionHandler to complete the request.
	*	Once the request has been completed, shuts down the connection
	* 	and begins waiting for a new connection.
	*/
	public void run()
	{

		//create a new ServerSocket
		try {
			if (fDebugOn) System.out.println("building fMainListenSocket...");
			fMainListenSocket = new  ServerSocket(fPort);
		}
		catch (Exception e) {
			System.err.println("build fMainListenSocket threw: " + e);
			return;
		}
		finally {
			if (fMainListenSocket == null) {
				System.err.println("Couldn't create a new ServerSocket!");
				return;
			}

			if (fDebugOn)
				System.out.println("fMainListenSocket initialized on port..." + fPort);
		}


	  	try {
	  	while (fContinueListening ) {

			if (fDebugOn) System.out.println("server accepting...");
	     	Socket clientSocket = fMainListenSocket.accept( );//this blocks!

	     	if (clientSocket != null) {
	     		HttpConnectionMgr mgr = new HttpConnectionMgr(clientSocket,pageCache,parentAdress);
//	     		cashTable = mgr.getHash();
			}

	 	}  //while fContinueListening
	    } //try
	    catch (Exception loopEx) {
	    	System.err.println("main loop ex: " + loopEx);
	    }

	} // HttpdMulti.run
	public static void getParentInfo(boolean isRoot, String parentAdr){
		rootExistence = isRoot;
		parentAdress = parentAdr;
			System.out.println("Root by HttpdMulti");
			if(rootExistence)System.out.print("Root is true");
			else System.out.print("Root is false.");
			System.out.println(parentAdress + "etc...");
		}

	public void start(){
		if(me==null){
			me=new Thread(this,"proxyServer");
			me.start();
		}
	}

	public static void main(String args[]){
		ProxyServer proxyServer = new ProxyServer(0);
		proxyServer.start();//start it up!
	 }

	}/* class HttpdMulti */



