//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import java.io.PrintStream;

/**
 * This class is a slight modification of the standard PrintStream (which 
 * is derived from a FilterStream) to append a prefix to any line message 
 * (eg. println(String)) sent to the stream. By reassigning
 * System.err to an instantiation of this class, all error messages can have 
 * an "Error: " prefix automatically inserted.
 */
public class PrefixPrintStream extends PrintStream {

	private String prefix_;
	
	PrefixPrintStream(PrintStream printStream, String prefix) {
		super(printStream);	// construct the ErrorPrintStream using the 
							// OutputStream from the given PrintStream
		prefix_ = prefix;
	}
	
	/**
	 * Slight modification of the PrintStream.println(String) method -
	 * a prefix will be inserted at the front of data being streamed.
	 */
	public void println(Object x) {
		super.println(prefix_ + x);
	}
	
	/**
	 * Same idea as the println(String) method.
	 */
	public void println(String x) {
		super.println(prefix_ + x);
	}
}
