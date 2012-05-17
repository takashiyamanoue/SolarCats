package nodesystem.proxyServer;
/**
* This file contains example code from the book
* JAVA NETWORKING AND COMMUNICATIONS
* by Todd Courtois
* ISBN 0-13-850454-7
*
* Purchasers of the book may reuse this sample code
* for any purpose whatsoever as long as this header
* is included. Use of this example code is subject
* to the license agreement printed in the book.
*
* For updates see the web site
* http://www.rawthought.com/JNC/
*/

import java.io.*;
import java.net.*;
import java.util.*;


/**
* A class that manages a single HTTP connection
* We've migrated all the connection-oriented stuff
* from HttpdAsync into this class.
*/
class HttpConnectionMgr extends Thread
{
public boolean fDebugOn = true;

protected Socket fClientSocket = null;
protected DataInputStream 	fClientInputStream;
protected DataOutputStream	fClientOutputStream;
protected HttpTransactionHandler fTransactionHandler;
public String parentAdress;
public PageCache cashTable;

public HttpConnectionMgr(Socket clientSocket, PageCache mainCash, String parentAdr)
{
	fClientSocket = clientSocket;
	System.out.println("YourAdress: " + clientSocket.getLocalAddress().toString());
	System.out.println("YourPort  : " + clientSocket.getPort());
	cashTable = mainCash;
	parentAdress = parentAdr;
	this.start();
}//HttpConnectionMgr

/**
* This method executes the core connection-mgmt stuff
*/
public void run()
{
	try {
		

		if (fDebugOn) System.out.println("building iostreams...");
	 	fClientInputStream =
			new DataInputStream(fClientSocket.getInputStream());
		fClientOutputStream =
	  		new DataOutputStream(fClientSocket.getOutputStream());

	    if ((fClientOutputStream != null ) && (fClientInputStream != null)){
			//now, handle the transaction
			fTransactionHandler =
				new HttpTransactionHandler(fClientInputStream,fClientOutputStream,cashTable,parentAdress);
			try { 
				if(!this.passAuthentication()){
					this.fClientInputStream.close();
					this.fClientOutputStream.close();
					return;
				}
				fTransactionHandler.handleTransaction(); 
//				cashTable = fTransactionHandler.getHash();
			}
			catch (Exception handleTransEx) {
				System.err.println("handleTransaction ex: " + handleTransEx);
			}
			//we no longer need the transaction handler
			fTransactionHandler = null;
			//we no longer need the client input stream!
			fClientInputStream = null;
			//we no longer need the client output stream!
			fClientOutputStream = null;
	     }
	    else {
			if (fClientOutputStream == null)
				System.err.println("fClientOutputStream null!");
			if (fClientInputStream == null)
				System.err.println("fClientInputStream null!");
	    }

		if (fDebugOn) System.out.println("closing fClientSocket...");
		try { fClientSocket.close();  }
		catch (Exception clientSocketCloseEx) {
			System.err.println("fClientSocket.close() threw: " + clientSocketCloseEx);
		};

		fClientSocket = null;
		if (fDebugOn) System.out.println("done with cleanup...");
	}
	catch (IOException ioEx) {
		System.err.println("HttpConnectionMgr run ioEx: " + ioEx);
	}

  }//run

  boolean passAuthentication(){
	  return true;
  }
  /*
  protected Hashtable getHash()
  {
	return cashTable;
  }
*/
}

/* class HttpConnectionMgr */

