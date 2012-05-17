//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

/**
 * Command to exit the program. This command calls the prompt to exit the 
 * System and therefore requires special permissions - usually not available
 * when running in an Applet.
 */
public class ExitCommand extends BaseCommand {

	ExitCommand() {
		super("exit", 0,
			"\n- closes the prompt and any registered command packages");
		
		// Note - getPrompt().exit() calls System.exit() which requires
		// special permissions if we are running in an applet
		//
		RuntimePermission permToExit = new RuntimePermission("exitVM");
		checkPermission(permToExit);
	}

	public void execute(String args[]) {
		prompt.exit();
	}
}
