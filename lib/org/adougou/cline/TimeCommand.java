//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import java.util.Date;

/**
 * Simple command to prints out the current date/time.
 */
public class TimeCommand extends BaseCommand {

	public TimeCommand() {
		super("time", 0,
			"\n- prints out the current date and time");
	}

	public void execute(String args[]) {
		prompt.out.println("Current time is: " + (new Date()).toString());
	}
}
