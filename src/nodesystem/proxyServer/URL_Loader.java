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

import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;

/**
* A class to demonstrate simple programmatic access of
* Web content using the URL class.
*/
public class URL_Loader
{
	public     boolean    fDebugOn = true; //toggles debugging

	protected TextField fInputField;//user input field
	protected URL       fDefaultURL = null;//used as default URL
	protected Hashtable   cashTable = new Hashtable();//URL Å‡ hashCode's Name File
	protected String pHostName = "";
	
	/**
	* Build the appropriate UI elements for punching in a URL.
	*/
	public URL_Loader() {

	  	//build the default base URL
	  	try { fDefaultURL = new URL("http://www.google.co.jp/");}
	  	catch (MalformedURLException ex) {
	  		System.err.println("build of fDefaultURL failed: " + ex);
	  	}

	}//URL_Loader()

 
/**
* Load a document given a URL string
* @param specStr A String containing the URL to
* load: i.e. "http://www.rawthought.com/"
*/
public void loadURL(String AccessHost,String specStr, Hashtable mainCash, URL hostName)
{
	URL tempURL;//the URL to be loaded
	InputStream tempInputStream;//stream from which to read data
	int curByte; //used for copying from input stream to a file
	cashTable = mainCash;
	//fDefaultURL = hostName;
	System.out.println("Default:   " + fDefaultURL);
	String extension;
	extension = this.addExtension(specStr);
	

 
   	 try {
   	 	//create new URL using fDefaultURL as the base or default URL
   	 	if(AccessHost.equalsIgnoreCase("")){
   	 		tempURL = new URL(fDefaultURL, specStr);
   	 		System.out.println(fDefaultURL +"  "+ specStr);
   	 	}else{
   	 		tempURL = new URL(AccessHost + specStr);
   	 		System.out.println(AccessHost + "  "+ specStr);
   	 		}
        if (fDebugOn) {
        	try{System.out.println(
        		    "My address=" + java.net.InetAddress.getLocalHost().toString());}catch(UnknownHostException e){}
            System.out.println("protocol: " + tempURL.getProtocol());
            System.out.println("host: " + tempURL.getHost());
            fDefaultURL= new URL("http://" + tempURL.getHost());
            System.out.println("port: " + tempURL.getPort());
            System.out.println("filename: " + tempURL.getFile());
        }

    	try {
    		if (fDebugOn) System.out.println("Opening input stream...");
    		//open the connection, get InputStream from which to read
    		//content data
    		tempInputStream = tempURL.openStream();
    		int hashFile = tempURL.hashCode();

    		//if we get to this point without an exception being thrown,
    		//then we've connected to a valid Web server,
    		// requested a valid URL, and there's content
    		//data waiting for us on the InputStream
    		try {
    			//use URL.hashCode() to generate a unique filename
    			String newFileName =
    			    String.valueOf(hashFile) + extension;

    			if (fDebugOn) System.out.println(
    			    "Opening output file: " + newFileName);
                //open output file
                FileOutputStream outstream =
		            new FileOutputStream(newFileName);

    			if (fDebugOn)
    			    System.out.println("Copying Data...");
				try {
			        while ( (curByte = tempInputStream.read()) != -1 ) {
                        //simple byte-for-byte copy...could be improved!
                        outstream.write(curByte);
			        }
			        if (fDebugOn)
			            System.out.println("Done Downloading Content!");
			        cashTable.put(specStr,newFileName);
			        if(fDebugOn)System.out.println(specStr+": "+cashTable.get(specStr));
					//we're done writing to the local file, so close it
			        outstream.flush();
			        outstream.close();
			    }
			    catch(IOException copyEx) {
                    System.err.println("copyEx: " + copyEx);
			    }
    		}
    		catch (Exception fileOpenEx) {
                System.err.println("fileOpenEx: " + fileOpenEx);
    		}
    	}
    	catch (IOException retrieveEx) {
            System.err.println("retrievEx: " + retrieveEx);
    	}
   	 }
   	 catch (MalformedURLException murlEx) {
   	 	System.err.println("new URL threw ex: " + murlEx);
   	 }//  case: nothing htmlFile.
} // loadURL
public Hashtable setHash(){
	return cashTable;
	}
protected String addExtension(String orgAdr){
	if(orgAdr.endsWith(".gif")){return ".gif";}
	else if(orgAdr.endsWith(".html")){return ".html";}
	else if(orgAdr.endsWith(".jpg")){return ".jpg";}
	else if(orgAdr.endsWith(".wmv")){return ".wmv";}
	else{return ".html";}
	}
public URL ResDefaultHost(){
	return fDefaultURL;
	}

} //class  URL_Loader


