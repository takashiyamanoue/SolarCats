//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import java.util.Iterator;
import java.awt.Window;
import java.awt.Cursor;

/**
 * This class is not currently used, but I have included it to remind me 
 * of the idea and maybe one day I will finish implementing it. The basic 
 * idea is to have a command that enables the user to access AWT-based
 * frames that were launched from the program. For example, the program
 * might maintain a org.adougou.symblnet.AWTListSymblDecoder to display symbl
 * associations. The window command would provide a means for the user to 
 * control this window (hide, change the size, move to a different window...).
 *
 * The CommandRegister would be extended to maintain a table of windows.
 */
public abstract class WindowCommand extends BaseCommand {
	
	private Window window_; 
	
	protected WindowCommand(String name, int numArgs, String help,
							Window window) {
		super(name, numArgs, help);
		window_ = window;
		//register_.registerWindow(window);
	}

	public void execute(Iterator iter) {

		// all WindowCommands should make sure their corresponding Window
		// is visible
		//
		if (! window_.isShowing()) {
			window_.show();
			window_.toFront();
		}
		window_.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// call BaseCommand to execute the command
		super.execute(iter);

		window_.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
