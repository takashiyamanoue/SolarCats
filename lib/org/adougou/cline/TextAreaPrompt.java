//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import java.io.PrintStream;
	
/**
 * This class is used in conjunction with TextArea to provide an AWT-based
 * command prompt. 
 *
 * @see TextArea
 * @see ShellPrompt
 */
public class TextAreaPrompt extends BasePrompt {
	
	TextArea textArea_;
	
	/**
	 * The TextAreaPrompt requires a TextArea. The TextArea should first be 
	 * allocated and configured, and then passed to this constructor. The
	 * second parameter is a command register containing previously registered
	 * commands that will be available to this prompt.
	 */
	public TextAreaPrompt(	String name,
							TextArea textArea,
							CommandRegister register) {
		super(	name, register,
				new PrintStream(new TextAreaOutputStream(textArea)),
				new PrintStream(new TextAreaOutputStream(textArea)),
				new TextAreaInputStream(textArea));
		textArea_ = textArea;
	}
	
	/**
	 * Same idea as the other constructor however will allocate a new 
	 * command register for use by the prompt.
	 */
	public TextAreaPrompt(String name, TextArea textArea) {
		super(	name, null, new PrintStream(new TextAreaOutputStream(textArea)),
				new PrintStream(new TextAreaOutputStream(textArea)),
				new TextAreaInputStream(textArea));
		textArea_ = textArea;
	}
}
