//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

/**
 * Package containing all commands that will be automatically registered for
 * use with the command prompt. These include basic commands such as the 
 * HelpCommand, RunScriptCommand etc.
 */
class CLinePackage implements ICLinePackage {

	public Command[] getCommands() {
		Command[] commands = {	
			new HelpCommand(),
			new RunScriptCommand(),
			new PrintWorkingDirectoryCommand(),
			new ListCommand(),
			new ExitCommand(),
			new TimeCommand(),
		};
		return commands;
	}

	public void exit() {
	}
}
