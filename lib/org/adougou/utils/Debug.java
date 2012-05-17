package org.adougou.utils;

/**
 * This class provides methods to assert a runtime condition, and handle
 * critical exceptions.
 */
public class Debug
{
	// disable the default constructor
	private Debug() {}
	
	/**
	 * Print the given message to standard error, print the current stack
	 * trace and exit the program with a return value of 1.
	 */
	public static void EXIT(String message) {
		try {
			throw new RuntimeException(message);
		} catch (RuntimeException e) {
			e.printStackTrace(System.err);
		}
		System.err.println("program exiting");
		System.exit(1);
	}
	
	/**
	 * Assert that a programming statement is true. If false, 
	 * the EXIT(String message) method will be called.
	 * <p>
	 * For example: <PRE>
	 * Debug.ASSERT( noodleFactory.contains("Shrimp flavour"),
	 * 	"Noodle factory does not contain shrimp flavour!");
	 * </PRE>
	 */
	public static void ASSERT(boolean condition, String message) {
		if (condition == false) {
			EXIT("ASSERTION: " + message);
		}

	}

	/**
	 * Same as the ASSERT(boolean, String) method however does not
	 * require the second error message parameter (less informative).
	 */
	public static void ASSERT(boolean condition) {
		ASSERT(condition, "condition not satisfied");
	}

	
	/**
	 * Similar to the ASSERT(boolean, String) method, however asserts 
	 * that the given object is not null.
	 */
	public static void ASSERT(Object object, String message) {
		if (object == null) {
			ASSERT(false, message);
		}
	}
	
	/**
	 * Same as the ASSERT(Object) method however requires no error message.
	 * (Faster to write code, but less informative when there is a problem).
	 */
	public static void ASSERT(Object object) {
		ASSERT(object, "null object");
	}
	
	/**
	 * Calls the EXIT method using the error message and e.toString().
	 */
	public static void EXCEPTION(Exception e, String message) {
		EXIT("EXCEPTION: " + message + ":\n" + e.toString());
	}
}

	
