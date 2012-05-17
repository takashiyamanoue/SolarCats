//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import java.util.Iterator;
import java.net.URL;
import java.io.*;

/**
 * This command reads in a text file (or URL) containing a list of commands.
 * It is basically a utility for batch executions. Each line of the file 
 * should contain a command to be executed. Comment lines can be inserted
 * by placing a '#' at the start of the line. By convention, I name all my 
 * scripts with a .scr ending (good old ms-dos syntax).
 */
public class RunScriptCommand extends URLCommand {

	RunScriptCommand() {
		super("rs", 1, "url" +
			"\n- reads the specified url containing a list of commands");
	}

	public void execute(String args[]) {
		Iterator urls = getURLs(args);
		if (urls == null || ( ! urls.hasNext()) ) {
			prompt.err.println("must specify valid script URL");
			return;
		}

		URL url = (URL)urls.next();
		BufferedReader in = getBufferedReader(url);
		if (in == null) {
			return;
		}

		String nextCommand = null;
		int lineNumber=0;
		try {
			while ((nextCommand=in.readLine()) != null) {
				lineNumber++;
				if (nextCommand.equals("") || nextCommand.startsWith("#")) {
					continue;
				}
				try {
					register_.execute(nextCommand);
				} catch (CommandException e) {
					prompt.err.println("while reading script file '" +
						url + "' on line " + lineNumber);
					prompt.err.println(e);
					return;
				}
			}
			in.close();
		} catch (IOException e) {
			prompt.err.println("IOException reading script from: " + 
				url);
		}
	}
}
