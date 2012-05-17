//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import org.adougou.utils.URLUtils;
import java.util.Iterator;
import java.util.Vector;
import java.net.URL;
import java.io.*;

/**
 * Any command that loads a URL (eg. file) for input should extend this 
 * command. It basically checks that the Strings representing the URLs
 * are valid and returns URL instances as an enumeration (takes some of
 * the pain out of creating URLs). It is used by the RunScriptCommand.
 *
 * @see RunScriptCommand
 */
public abstract class URLCommand extends BaseCommand {

	protected URLCommand(String name, int numArgs, String help) {
		super(name, numArgs, help);
	}
	
	/**
	 * Given an array of Strings, try and create a URL from each String.
	 * If a valid URL can be created from the string, it will be added to 
	 * a list, and the iterator for this list is returned.
	 */
	public Iterator getURLs(String args[]) {
		Vector urls = new Vector(args.length);

		// go through each string in args and try to create a URL
		for (int i=0; i<args.length; i++) {
			URL url = URLUtils.createURL(args[i]);
			if (url != null) {
				urls.add(url);
			}
		}
		return urls.iterator();
	}
	
	/**
	 * Utility method that given a URL, allocates a BufferedReader and
	 * opens the URL for reading data.
	 */
	public BufferedReader getBufferedReader(URL url) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
				new InputStreamReader(url.openStream()));
		} catch (IOException e) {
			prompt.err.println("error opening url: " + url);
		}
		return reader;
	}
}
