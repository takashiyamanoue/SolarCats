//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import java.util.StringTokenizer;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Vector;

/**
 * This class is used internally by the BasePrompt to maintain a table
 * of registered commands. You should never be required to interact with it
 * using the cline package.
 */
public class CommandRegister extends Hashtable {

	private Hashtable packages_ = new Hashtable(20);

	public CommandRegister() {
		super(100);

		// always register the basic CLinePackage
		add(new CLinePackage());
	}

	public final void add(Command command) {
		command.set(this);
		put(command.getName(), command);
	}

	/**
	 * Registers an entire Java package for use with the command line. All
	 * commands returned by this packages getCommands() method will be 
	 * registerred when this method is called.
	 */
	public void add(ICLinePackage ci) {
		String key = ci.getClass().getName();
		if (packages_.contains(key)) {
			System.err.println("attempt to register duplicate package");
			return;
		}
		packages_.put(key, ci);

		// now register all the commands from this package
		Command[] commands = ci.getCommands();
		for (int i=0; i<commands.length; i++) {
			add(commands[i]);
		}
	}

	public void exit() {
		Iterator iter = packages_.values().iterator();
		while (iter.hasNext()) {
			ICLinePackage ci = (ICLinePackage)iter.next();
			ci.exit();
		}
	}
	
	/**
	 * This method returns the tokens from a String that does not contain
	 * any quotations.
	 */
	private final Iterator parseCommandNoQuotes(String input) {
		StringTokenizer tokenizer = new StringTokenizer(input, " \t");

		// convert from an Enumeration to an Iterator to try and be
		// consistent with our use of Collections
		// 
		Vector tokens = new Vector();
		while (tokenizer.hasMoreElements()) {
			tokens.add(tokenizer.nextElement());
		}
		return tokens.iterator();
	}
	
	/**
	 * This command returns the tokens from a string. If the string contains
	 * a quote, the words between the quotes are returned as a single token.
	 */
	final Iterator parseCommand(String input) {
		if (input.indexOf('"') == -1) {
			return parseCommandNoQuotes(input);
		}

		// else we might have to parse between quotations
		// For example, the string below: 
		// question "how are you" 2
		// contains three tokens (question, how are you, 2)

		int curIndex = 0;
		int endIndex = input.length();

		// used to hold the arguments of the command
		Vector vector = new Vector(4);	// 4 args is a large command

		while (curIndex < endIndex) {
			int spaceIndex = input.indexOf(' ', curIndex);
			int quotationIndex = input.indexOf('"', curIndex);

			//System.out.println("spaceIndex: [" + spaceIndex +
			//	"] quotationIndex: [" + quotationIndex +
			//	"] curIndex: [" + curIndex + "]");

			String next = null;
			if (quotationIndex == -1) {
				Iterator iter = parseCommandNoQuotes(
					input.substring(curIndex, endIndex));
				while (iter.hasNext()) {
					vector.add(iter.next());
				}
				break;
			} else if (spaceIndex != -1 && spaceIndex < quotationIndex) {
				next = input.substring(curIndex, spaceIndex);
				curIndex = spaceIndex+1;
			} else {	// quotationIndex != -1
				// we have to extract a string between quotations
				int matchingQuote =	input.indexOf('"', quotationIndex+1);
				if (matchingQuote == -1) {
					throw new CommandException("quotations do not match");
				}
				next = input.substring(quotationIndex+1, matchingQuote);

				// this assumes that an end quote will always be followed
				// by a space
				curIndex = matchingQuote+2;
			}
			vector.add(next);
		}
		return vector.iterator();
	}

	final Command getCommand(String name) {
		if (containsKey(name)) {
			return (Command)get(name);
		} 
		return null;
	}
	
	/**
	 * Parses a string containing a command name and arguments and then
	 * executes the command if it was well structured (the string 'command'
	 * will be parsed - looking for the name of the command and any 
	 * arguments required for that command).
	 */
	public void execute(String command) {
		executeCommand(parseCommand(command));
	}
	
	/**
	 * Execute a command - the first String in the iterator should be the 
	 * name of the command, the following strings are the parameters that 
	 * will be passed to the command.
	 */
	private final void executeCommand(Iterator iter) {
		if (!iter.hasNext()) {
			throw new CommandException("no command specified");
		}
		String name = (String)iter.next();
		Command command = getCommand(name);
		if (command != null) {
			command.execute(iter);
		} else {
			throw new CommandException("\"" + name + "\" does not compute");
		}
	}
}
