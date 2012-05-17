//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

/**
 * Command to print out the current working directory. Requires special
 * permissions and will likely return an error if executed in an applet.
 */
public class PrintWorkingDirectoryCommand extends BaseCommand {
	
	PrintWorkingDirectoryCommand() {
		super("pwd", 0, 
			"\n- prints out the current working directory");

		// We might want to improve this Command for Applet usage such
		// that it tries to acquire the necessary permissions to properly
		// execute - as is, a SecurityException will probably be thrown
		// See how this is done in ExitCommand
	}

	public void execute(String args[]) {
		try {
			String dir = System.getProperty("user.dir");
			prompt.out.println(dir);
		} catch (SecurityException e) {
			prompt.err.println("System security exception: " + e);
		} catch (NullPointerException e) {
			prompt.err.println("Null system property key: " + e);
		} catch (IllegalArgumentException e) {
			prompt.err.println("Bad system property key: " + e);
		}
	}
}
