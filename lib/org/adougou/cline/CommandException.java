//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

/**
 * If an unrecoverable error occurs when executing a command (eg. invalid 
 * parameters etc.), a CommandException should be thrown that will 
 * cleanly be handled by the prompt. The user can then retry the command
 * with the correct parameters, or atleast they will have an idea why the 
 * command is failing.
 */
public class CommandException extends RuntimeException {

	String error_;

	public CommandException(String error) {
		error_ = error;
	}

	public String toString() {
		return error_;
	}
}
