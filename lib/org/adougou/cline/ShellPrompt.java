//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.io.*;

/**
 * Command prompt to be used within a xterm or DOS-shell. 
 *
 * @see BasePrompt
 * @see TextAreaPrompt
 */
public class ShellPrompt extends BasePrompt {
	
	/**
	 * The shell prompt is an easy one - it just makes use of System.out,
	 * System.err and System.in. The second parameter is a register of 
	 * commands - which might have been created before we needed to allocate
	 * a ShellPrompt.
	 */
	public ShellPrompt(String name, CommandRegister register) {
		super(name, register, System.out, System.err, System.in);
	}

	/**
	 * Same idea as the other constructor, however will construct a new
	 * CommandRegister for use with the prompt.
	 */
	public ShellPrompt(String name) {
		super(name, null, System.out, System.err, System.in);
	}
}
