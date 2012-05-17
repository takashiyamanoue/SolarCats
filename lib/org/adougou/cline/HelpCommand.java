//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import java.util.Iterator;
import java.util.Set;

/**
 * This command is used to query information about any command that has
 * been registered with the prompt.
 */
public class HelpCommand extends BaseCommand {

	HelpCommand() {
		super("help", 1, "command" +
			"\n- prints out help information for a command");
	}

	public void execute(String args[]) {
		String topic = args[0];
		Command command = getRegister().getCommand(topic);
		if (command == null) {
			prompt.out.println("No help available for " + topic);
		} else {
			prompt.out.println(command.getHelp());
		}
	}
	
	/**
	 * This method overloads the BaseCommand help function - the HelpCommand
	 * is the only class where this makes sense.
	 */
	public String getHelp() {

		String help = super.getHelp();

		// create a list of the command topics available
		help += "\n  Commands available are: ";
		
		Set keySet = getRegister().keySet();
		Iterator iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String commandName = (String)iterator.next();
			help += "\n    " + commandName;
		}
		return help;
	}
}
