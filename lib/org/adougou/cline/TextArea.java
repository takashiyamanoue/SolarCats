//
// (c) 2000-2001 david croft. please see the license at:
//
// http://zooid.org/~tforcdivad/license.txt
//
package org.adougou.cline;

import java.awt.event.KeyEvent;
import java.awt.Dimension;

/**
 * This is a special TextArea for use with the TextAreaPrompt. It has the 
 * basic functionality of the java.awt.TextArea, however it has a 
 * KeyListener that controls how the user moves about.
 *
 * @see java.awt.TextArea
 * @see TextAreaPrompt
 */
public class TextArea extends java.awt.TextArea
	implements java.awt.event.KeyListener {

	static final int DEFAULT_COLUMNS=60;
	
	// The following two indices are used to mark the start and end of the
	// current string being composed on the command line. They are used by
	// TextAreaOutputStream and TextAreaInputStream.
	//
	int solPosition_ = 0;
	int eolPosition_ = 0;
	
	/**
	 * Construct a new TextArea for use with the TextAreaPrompt. Uses the 
	 * default number of columns (TextArea.DEFAULT_COLUMNS).
	 * @param numRows the number of lines that will fit into the text area.
	 */
	public TextArea(int numRows) {
		super("", numRows, DEFAULT_COLUMNS, TextArea.SCROLLBARS_VERTICAL_ONLY);
		addKeyListener(this);
	}

	/**
	 * Construct a new TextArea for use with the TextAreaPrompt. 
	 * @param numRows the number of lines that will fit into the text area.
	 * @param numColumns the number of characters that will fit into the 
	 * text area.
	 */
	public TextArea(int numRows, int numColumns) {
		super("", numRows, numColumns, TextArea.SCROLLBARS_VERTICAL_ONLY);
		addKeyListener(this);
	}
	
	/**
	 * The java.awt.TextArea.getPreferredSize() method seems to insist
	 * on making the TextArea slight larger than is required to fit the 
	 * specified number of rows. My solution is to overload the 
	 * getPreferredSize() method and slightly reduce the dimension height.
	 */
	public Dimension getPreferredSize() {
		Dimension superDim = super.getPreferredSize();
		superDim.height-=8;
		return superDim;
	}

	/**
	 * Notes from the KeyEvent API documentation:
	 *
	 * keyTyped events are higher level and do not usually depend on the 
	 * platform or keyboard layout. KeyPressed and KeyReleased events are
	 * lower-level are platform dependent. All key-processing for the
	 * TextArea makes use of keyPressed events since we want to capture
	 * special KeyEvents such as HOME, ENTER, arrow keys etc..
	 */
	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();
		
		// ENTER key - a new line of text is ready to be read by 
		// the TextAreaInputStream which is waiting on this TextArea
		//
		if (keyCode == KeyEvent.VK_ENTER) {

			
			// if the user was not at the end of the current line,
			// (eg. if they had used HOME or arrow keys to go back
			// and edit the text) hitting ENTER will break the line
			// into two pieces. To avoid this, consume the ENTER KeyEvent,
			// and manually append "\n" to the TextArea
			//
			e.consume();
			append("\n");
			
			// We need to find out the size of the string in this TextArea...
			// alas, there is no method to do this easily. We could do:
			//
			// int size = getText().length();
			//
			// however this would mean the entire text represented in the
			// TextArea would be allocated as a new String
			// (I assume a StringBuffer is used to store text for the
			// java.awt.TextArea). Here is the hack around:
			//
			// This will force the marker to the end of the text area range
			// (see the javadocs for setSelectionEnd()):
			//
			setSelectionEnd(Integer.MAX_VALUE);
			//
			// Now, getSelectionEnd() should return size of the entire text
			// string in the TextArea:
			//
			eolPosition_ = getSelectionEnd();

			synchronized(this) {
				notify();
			}

		// BACK_SPACE and LEFT arrow - let the user move around to edit the 
		// current input line
		//
		} else if (	keyCode == KeyEvent.VK_BACK_SPACE ||
					keyCode == KeyEvent.VK_LEFT) {

			if (getCaretPosition() <= solPosition_) {
				e.consume();
			} 

		// HOME key
		//
		} else if (keyCode == KeyEvent.VK_HOME) {
			setCaretPosition(solPosition_);
			e.consume();

		// UP and DOWN arrow keys - don't let the user go up and down 
		//
		} else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
			e.consume();
		
		// PASTE 
		// Now that we have dealt with the difficulty of finding the 
		// endPosition_, we should not need to worry about special paste
		// handling - I have left this code here though in case we want to 
		// do something fancy in the future - for example, doing something
		// with non-string pastes?
		/*
		} else if (keyCode == KeyEvent.VK_PASTE) {

			// we need to get a hold of the clipboard and figure out if this
			// is a string and if so how long it is - the caret will
			// not be moved until later when the java.awt.TextArea processes
			// this event!
			//
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Clipboard clipboard = toolkit.getSystemClipboard();

			// apparently the parameter passed to getContents() does nothing
			// - see the Clipboard javadocs
			//
			Transferable transfer = clipboard.getContents(null);
			String toPaste = null;
			if (transfer != null) {
				// something on the clipboard
				try {
					toPaste = (String)transfer.getTransferData(
						DataFlavor.stringFlavor);
				} catch (UnsupportedFlavorException e2) {
					// not String data on the clipboard
				} catch (IOException e2) {
					// data no longer available
				}
			}
			if (toPaste != null) {
				//eolPosition_ += toPaste.length();
			} else {
				e.consume();
			}
		*/
		
		// CUT - COPY is OK, but don't let the user cut
		//
		} else if (keyCode == KeyEvent.VK_CUT) {
			e.consume();
		
		} 
	}

	public void keyTyped(KeyEvent e) {
		// do nothing
	}
	public void keyReleased(KeyEvent e) {
		// do nothing
	}
}
