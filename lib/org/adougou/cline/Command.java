//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import java.util.Iterator;

/**
 * Interface implemented by all commands. Note that this interface is
 * implemented by BaseCommand.
 *
 * @see BaseCommand
 */
public interface Command {
	String getName();
	String getHelp();
	void execute(String args[]);
	void execute(Iterator iterator);
	void set(CommandRegister cregister);
}
