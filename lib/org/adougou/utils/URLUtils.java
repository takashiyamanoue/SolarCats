package org.adougou.utils;

import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;

public class URLUtils {

	public static final boolean isURLString(String string) {
		if (string.indexOf("://") != -1) {
			return true;
		} else if (	string.indexOf("/") != -1 ||
					string.indexOf("\\") != -1 ) {

			// this might be a file, this is expensive but we need to try
			URL test = createURL(string);
			if (test != null) {
				return true;
			}
		}
		return false;
	}

	public static final URL createURL(String name) {
		// try and determine if this string is a full URL or just a 
		// partial file name (default protocol if "protocol://" is not 
		// prepended to the string)
		URL url = null;
		if (name.indexOf("://") != -1) {
			try {
				url = new URL(name);
			} catch (MalformedURLException e) {
				System.err.println("Could not create URL from '" +
					name + "'");
				return null;
			}
		} else {
			// else we default to the file protocol
			File file = new File(name);
			try {
				url = file.toURL();
			} catch (MalformedURLException e) {
				// if the command has mixed fileName and integer (or non-file
				// name) arguments, we might be trying to create a URL for
				// an integer argument - just skip this name and return null
				// System.err.println(
				// 	"URL path '" + name + "' is not valid: " + e);
				return null;
			}
		}
		return url;
	}
}
