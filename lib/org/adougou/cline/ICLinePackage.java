//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

/**
 * Any Java package that is intended to be used with the cline package needs
 * to implement this interface and return an array of Commands that it would
 * like to use with the command prompt.
 *
 * @see BasePrompt#registerPackage(ICLinePackage)
 */
public interface ICLinePackage {
	public Command[] getCommands();
	public void exit();
}
