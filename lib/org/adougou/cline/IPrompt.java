//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

/**
 * Command prompt interface.
 *
 * @see ShellPrompt
 * @see TextAreaPrompt
 */
public interface IPrompt {

	public void registerCommand(Command command);

	public void registerPackage(ICLinePackage ci);

	public void start();

	public void exit();

	public boolean askBooleanQuestion(String question);

	public String askQuestion(String question);

	public String askQuestion(String question, String defaultAns);
}
