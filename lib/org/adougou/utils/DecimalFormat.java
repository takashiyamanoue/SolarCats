package org.adougou.utils;

import java.text.NumberFormat;

/**
 * This class provides a static handle to a NumberFormat instance to format
 * decimal numbers for printing. It is simply to prevent multiple calls
 * to NumberFormat.getInstance() and to make formating numbers a little 
 * easier - rather than writing:<p><code>
 * NumberFormat instance = NumberFormat.getInstance();
 * instance.setMaximumFractionDigits(3);
 * String formattedString = instance.format(number);
 * </code><p>
 * you can simply call the DecimalFormat.format() method and pass it the 
 * number with the desired decimal places.
 *
 * TODO: add the ability to change the LOCALE (eg. pour le francais)
 */
public class DecimalFormat {

	private static NumberFormat numberFormat_ = null;

	public static String format(double number, int fractionDigits) {
		if (numberFormat_ == null) {
			numberFormat_ = NumberFormat.getInstance();
		}
		numberFormat_.setMaximumFractionDigits(fractionDigits);
		return numberFormat_.format(number);
	}
}
