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
* A handler for http transactions
*/
public class HttpTransactionHandler
{
//response status constants
public final static int
    REPLY_ERROR_NO_ERROR 	= 200,
    REPLY_ERROR_NO_SUCH_FILE = 404;

public final static String
    REPLY_EXPLANATION_OK = " OK ",//no problem
    REPLY_EXPLANATION_ERROR = " ERROR ";//error of some sort

//debug
public static boolean fDebugOn = true;

//hooks connecting us to the client
DataInputStream 	fClientInputStream;
DataOutputStream	fClientOutputStream;

//info about the content file
DataInputStream		fContentInputStream;
long				fContentLength = 0;
String				fContentPath;
String				fContentFilename;
long				fContentLastModified = 0;
int                 fReplyErrorCode =
                        REPLY_ERROR_NO_ERROR; //no error by default
String				fReplyErrorExplanation = REPLY_EXPLANATION_OK;
protected PageCache cashTable;
public URL defaultHost;
URL_Loader download;
public int judge;
boolean isRoot;
String parentAddress;


/**
* Instantiate a new transaction handler.
* @param clientInputStream The stream where the client sends us
*   its request
* @param clientOutputStream The stream where we return content to
*   the client
*/
public HttpTransactionHandler(
    DataInputStream clientInputStream,
    DataOutputStream clientOutputStream,
	PageCache mainCash, String parentAdr)
{
	cashTable = mainCash;
	parentAddress = parentAdr;
	System.out.println(cashTable.getSize());
	System.out.println(parentAddress + " is parentAdress.");
	setIOStreams(clientInputStream, clientOutputStream);
}//HttpTransactionHandler

/**
* Set the input and output streams for the transaction.
* @param clientInputStream The stream where the
*   client sends us its request
* @param clientOutputStream The stream where we return
*   content to the client
*/
public void setIOStreams(
    DataInputStream clientInputStream,
    DataOutputStream clientOutputStream)
{
	fClientInputStream = clientInputStream;
	fClientOutputStream = clientOutputStream;
}//setIOStreams

/**
* Handle the current requested transaction.
*/
public void handleTransaction() throws Exception
{

    fContentPath = this.getRequestedFilename(fClientInputStream);

    try {
    	fContentPath = catchContents(fContentPath);
		/*if (fContentPath.startsWith("/")) {
		    //prepend a "current directory" dot
			fContentPath = "." + fContentPath;
		}

		if (fContentPath.endsWith("/")) {
		    //append the default doc name...
			fContentPath += "index.html";
		}
	/**/	
        //try to find this file...will throw if not found

	//	URL_Loader download = new URL_Loader();
	//	download.loadURL(fContentPath,cashTable);//"http://www.google.co.jp"
//**		String judgePath = "./"+(String)cashTable.get(fContentPath);//get file in cashserver
//    	File contentFile = new File(judgePath);


        //get the length of the file
//		fContentLength = contentFile.length();
		//get the mod date of the file
//		fContentLastModified = contentFile.lastModified();
		//strips directory stuff from the filename
//		fContentFilename = contentFile.getName();

//		fContentInputStream =
//		    new DataInputStream(new FileInputStream(judgePath));
//		if(fDebugOn)System.out.println("hash.get: "+judgePath);
	}
	catch (Exception couldNotOpenFileEx) {
		if (fDebugOn) System.err.println(
		    "threw opening url " + fContentPath +
		    " (" + couldNotOpenFileEx + ")");
		buildErrorReplyInfo();
	}

	if (fDebugOn) System.out.println("starting reply...");

	/*
	* We build a StringBufferInputStream here because
	* the more obvious implementation:
	* fClientOutputStream.writeBytes(getReplyHeader());
    *
	* ...is EXTREMELY slow. Basically, it sends a series of TCP
	* packets out containing single-byte data.  The
	* DataOutputStream.writeChars method is so slow because it
	* writes out data on the output stream a single byte at a time
	* instead of sending the data in the big blocks that TCP loves.
    *
	* Using our "block move" copySrcToSink method, in combination
	* with the StringBufferInputStream, improves the efficiency
	* tremendously.
	*/
	StringBufferInputStream headerStream =
	    new StringBufferInputStream(getReplyHeader());
	copySrcToSink(  (InputStream)headerStream,
	                (OutputStream)fClientOutputStream);
	headerStream = null;

	if (fDebugOn)
	    System.out.println("done with reply header...");

    //save the content file....
	copySrcToSink(  (InputStream)fContentInputStream,
	                (OutputStream)fClientOutputStream);
	if (fDebugOn)
	    System.out.println("done copying content data...");
//	cashTable.put(fContentPath,fContentFilename);
}/* handleTransaction */

/**
* Take a source data input stream and a sink data ouput stream
* copy input to output
*/
public static void copySrcToSink(InputStream src, OutputStream sink)
{
	byte[] tempBuf = new byte[1024];
	int bytesRead = 1;
	int debugKCount = 0;
	try {
		do {
			bytesRead = src.read(tempBuf,0,1024);

			if (bytesRead > 0) {
				if (fDebugOn){
					if(bytesRead==1024)debugKCount ++;
					else System.out.println("bytesRead: " + debugKCount + "K " + bytesRead);
				}
				sink.write(tempBuf,0,bytesRead);
				sink.flush();
			}
		} while (bytesRead >= 0);
	}
	catch (IOException ioEx) {
		System.err.println("copySrcToSink failed with: " + ioEx);
	}

} /* copySrcToSink */


/**
* Given an HTTP 1.0 request on the srcStream,
* find the requested filename and return it as a string
*/
protected String getRequestedFilename(DataInputStream srcStream)
{
	boolean foundUrlname = false;
	String retVal = "/"; //default document?

	try {

        //gets first line, should contain
        //"GET foo.html HTTP/1.0"
		BufferedReader br=new BufferedReader(new InputStreamReader(srcStream));
//		String firstLineStr=srcStream.readLine();
		String firstLineStr = br.readLine();
		
		///this part is retright part
		if(fDebugOn)System.out.println("read1: " + firstLineStr);
//		String secondLineStr = srcStream.readLine();//this Line read 2nd Line.
		String secondLineStr = br.readLine();
//		secondLineStr = srcStream.readLine();//this Line read 3rd Line.
		secondLineStr = br.readLine();
		if(fDebugOn)System.out.println("read3: "+ secondLineStr );
		StringTokenizer tokSource2 =							//about debug
		    new StringTokenizer(secondLineStr);					//delete no proglem
		boolean foundUrlname2 = false;							//
		while (tokSource2.hasMoreTokens()) {								//
            //should return a string for the next token			//
			String curTok2 = tokSource2.nextToken();			//
			if (fDebugOn)										//
			    System.out.println("curTok2: " + curTok2);		//
				//retVal = curTok2;								//
			if(curTok2.equals("Referer:")){					//
					curTok2 = tokSource2.nextToken();			//
					System.out.println("request:" + curTok2);	//
					foundUrlname2 = true;
					break;
			}						//
//			else
//				foundUrlname2 = true;						//
		}
		//part end.
/*		if(fDebugOn)System.out.println("read3: "+ srcStream.readLine() );
		if(fDebugOn)System.out.println("read4: "+ srcStream.readLine() );
		if(fDebugOn)System.out.println("read5: "+ srcStream.readLine() );
		if(fDebugOn)System.out.println("read6: "+ srcStream.readLine() );
		if(fDebugOn)System.out.println("read7: "+ srcStream.readLine() );
		if(fDebugOn)System.out.println("read8: "+ srcStream.readLine() );*/
		boolean foundUrlName=false;
		StringTokenizer tokSource =
		    new StringTokenizer(firstLineStr);

		while (tokSource.hasMoreTokens()) {
            //should return a string for the next token
			String curTok = tokSource.nextToken();
			if (fDebugOn)
			    System.out.println("curTok: " + curTok);

			if (curTok.equals("GET")) {
				curTok = tokSource.nextToken();
				retVal = curTok;
				foundUrlname = true;
				break;
			}
		}
	}
	catch (Exception ex) {
		if (fDebugOn) System.err.println(
		                "getRequestedFilename threw: " + ex);
	}

	if (fDebugOn) System.out.println("retVal: " + retVal);
	StringBuffer cheep = new StringBuffer(retVal);
	char saps = cheep.charAt(2);
	if(saps==0){
		judge = 0;
		cheep.delete(1,2);
	}else if(saps == 1){
		judge = 1;
		cheep.delete(1,2);
		}else judge = 2;
	retVal = cheep.toString();
	if (fDebugOn) System.out.println("retVal: " + retVal);
	return retVal;
}//getRequestedFilename


/**
* Build the HTTP reply header
* This includes things like the MIME content type,
* length, mod date, and so on.
*/
protected String getReplyHeader()
{

    if (fDebugOn) System.out.println("getting content type...");

    String contentTypeStr =
        URLConnection_Wrapper.guessContentTypeFromName(
                                            fContentFilename);

    if (fDebugOn)
        System.out.println("building header str...");

    String headerStr =

    	"HTTP/1.0 " + fReplyErrorCode + fReplyErrorExplanation + "\n" +
    	"Date: " + (new Date()).toGMTString() + "\n" +
    	"Server: Todd's_Skanky_Web_Server/1.0\n" +
    	"MIME-version: 1.0\n" +
    	"Content-type: " + contentTypeStr + "\n" +
    	"Last-modified: " +
    	    (new Date(fContentLastModified)).toGMTString() + "\n" +
    	"Content-length: " + fContentLength +
    	"\n\n"; //this last seq terminates the header

    if (fDebugOn) System.out.println("reply header: " + headerStr);

    return headerStr;

}//getReplyHeader

/**
* We couldn't find the content file the client requested
* Return a proper error message to the client.
*/
protected void buildErrorReplyInfo()
{
	String errorContentStr =
	    "Could not find the requested file: " + fContentPath + "\n";
	StringBufferInputStream localStream =
	    new StringBufferInputStream(errorContentStr);

    //get the length of the content
	fContentLength = localStream.available();
	//get the modification date of the file (or at least fake it)
	fContentLastModified = (new Date()).getTime();
	fContentFilename = "error.html";
	fContentInputStream = new DataInputStream(localStream);

	fReplyErrorCode = REPLY_ERROR_NO_SUCH_FILE;
	fReplyErrorExplanation = REPLY_EXPLANATION_ERROR;

}//buildErrorReplyInfo

public String catchContents(String contentPath){
	/*
	int pathLength = contentPath.length();
	StringBuffer path = new StringBuffer(contentPath);
	path = path.delete(0,1);
	contentPath = path.toString();
	*/
	boolean hap = true;
	
	if(fDebugOn)System.out.println("catchContent");
	if(fDebugOn)System.out.println("contentpath1: "+contentPath);
	
	if(contentPath.equalsIgnoreCase("index.html")||contentPath.equalsIgnoreCase("")){
	
	    	//prepend a "current directory" dot and slush
		contentPath = "/" + contentPath;
		contentPath = "." + contentPath;
		if(fDebugOn)System.out.println("contentpath2: "+contentPath);
		

		if (fContentPath.endsWith("/")) {
	    	//append the default doc name...
			contentPath += "index.html";
			if(fDebugOn)System.out.println("contentpath3: "+contentPath);
		}
	}
	else{
		if(fDebugOn)System.out.println("elsecase1: "+contentPath);
		//contentPath = "h" + contentPath;
	 	if(cashTable.containsKey(contentPath)){
	  		System.out.println("Already It is.");
	  	}// found htmlFile.
	 	else{
 			String AccessHost = "";
 			if(!parentAddress.equalsIgnoreCase("")){//if this Node has Parent or etc.
 				AccessHost = parentAddress ; //
 			}//Arrange contentPath to ParentNode's proxyServer.
 			
	 		if(contentPath.startsWith("0")){//request is localhost
	 			if(fDebugOn)System.out.println("this is 0");
	 			//judge root or else. Arange this contentPath Number change 1.
	 			download = new URL_Loader();

	 			System.out.println(AccessHost + contentPath + "    how about(0) ?");//debug is root?with contentPath
//	 			download.loadURL(AccessHost,contentPath,cashTable,defaultHost);//"http://www.google.co.jp"
//	 			cashTable = download.setHash();
	 			defaultHost = download.ResDefaultHost();
	 			
	 		}else if(contentPath.startsWith("1")){// repuest is underNode
	 			//wait this Node get Content.
	 			System.out.println("sarkit in 1!");
//	 			contentPath = this.ToPath(contentPath);
	 			System.out.println("1's cPath: "+ contentPath);
	 			while(hap){
	 				
	 				if(cashTable.containsKey(contentPath)){
	 					System.out.println("Already is 1!");
	 					hap = false;
	 					}
	 				}
	 			
	 		}else{
	 			if(fDebugOn)System.out.println("this is Defarence");
	 			download = new URL_Loader();
	 			System.out.println(AccessHost + contentPath + "    how about(else)?");
//	 			download.loadURL( AccessHost, contentPath, cashTable, defaultHost);
//	 			cashTable = download.setHash();
	 			}
	 		}
//	 	System.out.println("  " + cashTable.size());
	}
	
	return contentPath;
	}//method catchContents end.


}//class HttpTransactionHandler


