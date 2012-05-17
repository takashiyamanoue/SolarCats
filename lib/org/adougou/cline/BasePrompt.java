//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import java.util.StringTokenizer;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Iterator;
import java.awt.Window;
import java.io.*;
import java.security.AccessController;

/**
 * Base class for all command prompts. This class handles the
 * prompt-independent processing, such as the basic user-interaction loop and
 * command execution. 
 *
 * Optionally, System.in, System.err, System.out can be redirected to the
 * prompt's streams.
 *
 * @see ShellPrompt
 * @see TextAreaPrompt
 */
public class BasePrompt extends Thread implements IPrompt {
	
	private String name_;
	private BufferedReader reader_;
	private CommandRegister register_;

	public PrintStream out;
	public PrintStream err;
	public InputStream in;
	
	// If System streams are redirected, a handle to the originals must 
	// be kept so that we can reset them if asked to exit
	//
	private PrintStream defaultOut_;
	private PrintStream defaultErr_;
	private InputStream defaultIn_;

	protected BasePrompt(	String name,
							CommandRegister register,
							PrintStream out,
							PrintStream err,
							InputStream in) {
		name_ = name;

		this.out = out;
		this.err = err;
		this.in = in;

		reader_ = new BufferedReader(new InputStreamReader(in));

		if (register == null) {
			register_ = new CommandRegister();
		} else {
			register_ = register;
		}

		BaseCommand.setPrompt(this);
	}

	public void redirectSystemIOToPrompt() {

		// If we are running in an applet we will require permission to 
		// redirect the standard IO streams
		//
		RuntimePermission setIO = new RuntimePermission("setIO");
		AccessController.checkPermission(setIO);

		// maintain a handle to the default System.out and System.err
		// streams so that we can reset these when we exit
		//
		defaultOut_ = System.out;
		defaultErr_ = System.err;
		defaultIn_ = System.in;
		
		// reassign standard output, error and input to the same as that 
		// being used by the prompt. The whole idea here is to still be 
		// able to write "System.out.println()" in your code and have this
		// automatically redirected to the command prompt.
		//
		System.setOut(out);

		// At one time, I was prefixing all error messages with "Error: ".
		// However, this acts somewhat funny for multi-line error messages.
		// System.setErr(new PrefixPrintStream(error, "Error: "));
		//
		System.setErr(err);

		System.setIn(in);
	}		
	
	/**
	 * This method is called to register a new command to be used with this
	 * prompt. Note, every prompt contains a reference to a CommandRegister 
	 * that keeps track of available commands.
	 */
	public void registerCommand(Command command) {
		register_.add(command);
	}
	
	public void registerPackage(ICLinePackage ci) {
		register_.add(ci);
	}
	
	/**
	 * Iterates through all the registered packages and tells them we are 
	 * exiting - just in case they have resources to free.
	 */
	public void exit() {

		register_.exit();

		// reset the output Streams to the defaults
		// (some messages might be displayed on exit, however these will
		// not be visible if the out and err streams are directed to a
		// TextArea and this is no longer visible)
		//
		System.setOut(defaultOut_);
		System.setErr(defaultErr_);
		System.setIn(defaultIn_);

		System.exit(0);
	}
	
	protected void printPrompt() {
		out.print(name_ + "> "); 
		out.flush();
	}
	
	/**
	 * This method starts the prompt in interactive mode. Note that this is
	 * the starting point for a new thread of execution whcih blocks until
	 * the user enters input into the input stream.
	 */
	public void run() {
		for(;;) {
			printPrompt();
			String command = null;
			try {
				command = reader_.readLine();
				if (command != null && command.length() != 0) {
					register_.execute(command);
				}
			} catch (CommandException e) {
				err.println("CommandException: " + e);
			} catch (IOException e) {
				err.println("could not read line");
			}
		}
	}
	
	/**
	 * Ask the user a boolean question. Eg. "How are you today?"
	 * @return true if the user responds "yes" or "y", false if they respond
	 *  "no" or "n" - all case insensitive.
	 */
	public boolean askBooleanQuestion(String question) {
		String response = askQuestion(question, "n");
		if (response.equalsIgnoreCase("yes") || 
			response.equalsIgnoreCase("y")) {
			return true;
		} 
		return false;
	}
	
	/**
	 * Ask the user a question.
	 * @return a string containing the typed response.
	 */
	public String askQuestion(String question) {
		return askQuestion(question, null);
	}
	
	/**
	 * Ask the user a question and provide a default answer (to save the 
	 * user typing if it is a likely default).
	 * @return a string containing the typed response, or the default answer
	 *  if the user just hits enter.
	 */
	public String askQuestion(String question, String defaultAns) {
		out.print(question);
		if (defaultAns != null) {
			out.print(" (" + defaultAns + ")"); 
		}
		out.print(": ");
		out.flush();
		String response = "";

		try {
			response = reader_.readLine();
			if (defaultAns != null &&
				(response.equalsIgnoreCase("") ||
				response.equalsIgnoreCase("\n"))) {
				return defaultAns;
			}
		} catch (IOException e) {
			err.println("could not read response");
		}
		return response;
	}
}
