//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import java.io.InputStream;
import java.io.IOException;
	
/**
 * This is an InputStream that can be assigned to System.in. It is used
 * by the TextAreaPrompt (but could also be used for other purposes).
 *
 * @see TextArea
 * @see TextAreaPrompt
 * @see TextAreaOutputStream
 */
public class TextAreaInputStream extends InputStream {
	
	private TextArea textArea_;
	
	/**
	 * Construct a new TextAreaInputStream to read input from a TextArea.
	 */
	TextAreaInputStream(TextArea textArea) {
		textArea_ = textArea;
	}
	
	/**
	 * @return the number of bytes available to be read.
	 */
	public int available() {
		if (textArea_.eolPosition_ > textArea_.solPosition_) {
			return textArea_.eolPosition_ - textArea_.solPosition_;
		}
		return 0;
	}
	
	/**
	 * Read an array of bytes. This method will be called by methods such
	 * as BufferedReader.readLine() - for example in BasePrompt.
	 * 
	 * @return the number of bytes that were read.
	 */
	public int read(byte[] b, int start, int bLength) throws IOException {
		int byteIndex = 0;
		while (true) {
			if (available() != 0) {
				textArea_.select(	textArea_.solPosition_,
									textArea_.eolPosition_);

				String input = textArea_.getSelectedText();

				for ( ; byteIndex < input.length() && byteIndex < b.length;
						byteIndex++) {
					b[byteIndex] = (byte)input.charAt(byteIndex);
				}

				textArea_.solPosition_+= byteIndex;
				return byteIndex;
			}

			// if nothing was available - wait() on the textArea_
			//
			try {
				synchronized(textArea_) {
					textArea_.wait();
				}
			} catch (InterruptedException e) {
				System.err.println("TextAreaInputStream read interrupted");
				return byteIndex;
			}
		}
	}
	
	/**
	 * Read a single byte from the TextArea. This method makes use of the 
	 * read(byte[], int, int) method which is much more efficient for 
	 * reading more than one byte at a time.
	 */
	public int read() throws IOException {
		byte[] b = new byte[1];
		int bytesRead = read(b,0,1);
		if (bytesRead == 0) {
			return -1;
		}
		return b[0];
	}
}
