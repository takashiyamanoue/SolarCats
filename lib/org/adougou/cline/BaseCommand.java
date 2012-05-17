//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import java.security.Permission;
import java.security.AccessController;
import java.security.AccessControlException;
import java.util.Iterator;
import java.util.Vector;

/**
 * Base class for creating named commands. The BaseCommand constructor should
 * be called from the constructor of a derived class that is the actual 
 * command. For example:
 * <p><pre>
 * class ExitCommand extends BaseCommand { 
 *  ExitCommand() { 
 *    super("exit", 0, "calls the System to exit");
 *  }
 * }
 * </pre>
 * <p>
 * For a complete example of creating a command, see the RunScriptCommand.
 * @see RunScriptCommand
 */
public abstract class BaseCommand implements Command {

	protected String name_;
	protected int numArgs_;
	protected CommandRegister register_;
	protected String help_;

	// Some commands require special permissions in order to execute.
	// When a command is initialized, it should acquire the necessary
	// permission and call the checkPermission() method. If the 
	// permission fails, it will be added to the list of failed permissions.
	// For an example see ExitCommand
	//
	private Vector failedPermissions_ = null;
	
	// a static reference is maintained so that all commands can access
	// the command prompt (eg. getPrompt.askQuestion()...)
	//
	protected static BasePrompt prompt = null;
	
	/**
	 * All inheriting classes are required to call this constructor using 
	 * <pre>super(name, numArgs, help)</pre>.
	 *
	 * @param name the name of the command - an exception will be thrown if 
	 *  a command already exists with this name
	 * @param numArgs the number of arguments this command takes.
	 * -1 can be specified if the command takes a variable number of 
	 * arguments, in which case it is up to the derived command to carefully
	 * check the arguments.
	 * @param help this string is returned to the user when they request 
	 * help about a command. It should include a brief description of what
	 * the command accomplishes and what each of the required arguments is.
	 */
	protected BaseCommand(String name, int numArgs, String help) {
		name_ = name;
		numArgs_ = numArgs;
		help_ = help;
	}
	
	/**
	 * This method is called by the CommandRegister whenever a new command
	 * is registered - this gives commands access to help information for 
	 * other commands - eg. see the HelpCommand.
	 */
	public void set(CommandRegister cregister) {
		register_ = cregister;
	}

	CommandRegister getRegister() {
		return register_;
	}
	
	/**
	 * Dervied classes that require a special permission (for example, to 
	 * call the VM to exit as in the ExitCommand) should call this method to
	 * check the java.security.Permission. If permission is not granted, 
	 * this permission will be added to a list of failed permissions for this
	 * command, the command will be disabled, and a message describing the 
	 * permission failure will be displayed to the user if they attempt to
	 * execute the command.
	 */
	protected void checkPermission(Permission permission) {
		try {
			AccessController.checkPermission(permission);
		} catch (AccessControlException e) {
			// if there was an exception, set a failed permission
			// so we can print an intelligent message whenever the user
			// attempts to execute this command
			//
			if (failedPermissions_ == null) {
				failedPermissions_ = new Vector(3);
			}
			failedPermissions_.add(permission);
		}
	}

	static void setPrompt(BasePrompt prompt) {
		BaseCommand.prompt = prompt;
	}

	protected BasePrompt getPrompt() {
		if (prompt == null) {
			System.err.println("cannot access prompt as it has not been set");
		}
		return prompt;
	}

	public String getName() { return name_; }
	
	/**
	 * This method enables a command to be used independently of the 
	 * command prompt. You instantiate a command, and then execute it 
	 * by passing a string containing the arguments. If the arguments are
	 * not properly formatted, an error will be printed.
	 */
	public void execute(String args)
	{
		if (register_ == null) {
			prompt.err.println("Warning: command '" +
				getName() + "' not registered: can't execute");
			return;
		}
		execute(register_.parseCommand(args));
	}
	
	/**
	 * This method should be overloaded by each command that is defined -
	 * it will be called by the CommandRegister after the number of
	 * arguments have been checked.
	 */
	public abstract void execute(String args[]);
	
	/**
	 * This method is called internally whenever a command is executed to 
	 * check the arguments. After checking arguments, it will call the 
	 * execute(String[]) method for the specific command.
	 */
	public void execute(Iterator iter) {

		if (failedPermissions_ != null) {
			Iterator perms = failedPermissions_.iterator();
			prompt.err.println("Inadequate permissions to execute command:");
			while (perms.hasNext()) {
				prompt.err.println("  " + perms.next());
			}
			// don't continue with regular execution
			return;
		}

		String args[] = null;
		if (numArgs_ != -1) {
			args = new String[numArgs_];
			for (int i=0; i<numArgs_; i++) {
				if (!iter.hasNext()) {
					prompt.out.println(getHelp());
					return;
				}
				args[i] = (String)iter.next();
			}
			// check that there are not extra arguments
			if (iter.hasNext()) {
				prompt.err.println("Too many arguments in command");
				prompt.out.println(getHelp());
				return;
			}
		// unknown number of arguments indicated by -1
		} else {	
			Vector vector = new Vector(10);
			while(iter.hasNext()) {
				vector.add(iter.next());
			}
			args = new String[vector.size()];
			vector.toArray(args);
		}
		execute(args);
	}
	
	/**
	 * Return a the help string for this command. This is basically the help
	 * string required in the constructor for all BaseCommands.
	 */
	public String getHelp() { 
		String className = this.getClass().getName();
		return "(" + className + ") usage:\n" + name_ + " " + help_;
	}
}
