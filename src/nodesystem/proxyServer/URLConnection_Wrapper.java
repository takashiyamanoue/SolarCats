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
//import java.util.*;

/**
* Class which exports some private methods of URLConnection in
* a "safe" manner.
*/
public abstract class URLConnection_Wrapper extends URLConnection
{
	URL fRealURL;
	URLConnection fRealURLConnection;


/**
* This is the main method we wanted to make public instead of private.
* @note We don't even need to instantiate this class-- this is a public
* static method!
* @param fileName The filename from which to guess the content type.
*/
public static String guessContentTypeFromName(String fileName)
{
	//fortunately the URLConnection method we use is static as well
	return URLConnection.guessContentTypeFromName(fileName);
}

/**
* This is the other protected method we wanted to export. This subclass
* method makes the URLConnection method public, essentially
* @note There's no need to instantiate this class just to access this
* method-- it's a public static method!
*/
public static String guessContentTypeFromStream(InputStream inStream)
{
	String typeStr = null;
	try {
		//fortunately the URLConnection method we use is static as well
		typeStr = URLConnection.guessContentTypeFromStream(inStream);
	}
	catch (IOException ex) {
		System.err.println("guessContentTypeFromStream threw: " + ex);
	}
	return typeStr;
}

//== Stuff below here was only included to make the Java compiler happy...

//this makes the compiler happy about the constructor
public URLConnection_Wrapper(URL sourceURL) throws Exception
{
  super(sourceURL);
  fRealURL = sourceURL;
  fRealURLConnection = fRealURL.openConnection();

}


} /* class URLConnection_Wrapper */
