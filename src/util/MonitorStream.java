/*
 * 作成日: 2005/01/17
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package util;

/**
 * @author yamachan
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */

/*
 * Copyright (c) 1996-2001 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.media.Buffer;
import javax.media.control.MonitorControl;
import javax.media.format.VideoFormat;
import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushBufferStream;
import javax.media.util.BufferToImage;

public class MonitorStream
implements PushBufferStream, MonitorControl, BufferTransferHandler 
{

	PushBufferStream actual = null;
	boolean dataAvailable = false;
	boolean terminate = false;
	boolean enabled = false;
	Object bufferLock = new Object();
	Buffer cbuffer = new Buffer();
	BufferTransferHandler transferHandler = null;
	Component component = null;
	MonitorCDS cds;
	BufferToImage bti = null;
	Image currentImage = null;
    
	MonitorStream(PushBufferStream actual, MonitorCDS cds) {
	this.actual = actual;
	actual.setTransferHandler(this);
	this.cds = cds;
	}

	public javax.media.Format getFormat() {
	return actual.getFormat();
	}
	
	public void read(Buffer buffer) throws IOException {
	// Wait for data to be available
	// Doesn't get used much because the transferData
	// call is made when data IS available. And most
	// Processors/Players read the data in the same
	// thread that called transferData, although that's
	// not a safe assumption to make
	if (!dataAvailable) {
		synchronized (bufferLock) {
		while (!dataAvailable && !terminate) {
			try {
			bufferLock.wait(1000);
			} catch (InterruptedException ie) {
			}
		}
		}
	}

	if (dataAvailable) {
		synchronized (bufferLock) {
		// Copy the buffer attributes, but swap the data
		// attributes so that no extra copy is made.
		buffer.copy(cbuffer, true);
		dataAvailable = false;
		}
	}
	return;
	}

	public void transferData(PushBufferStream pbs) {
    	// Get the data from the original source stream
    	synchronized (bufferLock) {
    		try {
    		pbs.read(cbuffer);
    		} catch (IOException ioe) {
    		return;
    		}
    		dataAvailable = true;
    		bufferLock.notifyAll();
    	}

    	// Display data if monitor is active
    	if (isEnabled()) {
    		if (bti == null) {
    		VideoFormat vf = (VideoFormat) cbuffer.getFormat();
    		bti = new BufferToImage(vf);
/*		
		AudioFormat af = (AudioFormat) cbuffer.getFormat();
*/		
    		}
    		if (bti != null && component != null) {
    		Image im = bti.createImage(cbuffer);
    		this.currentImage=im;
    		if(currentImage==null)
    		System.out.println("currentImage(sourceImage)==null @ transferData");
    		Graphics g = component.getGraphics();
    		Dimension size = component.getSize();
    		if (g != null)
    			g.drawImage(im, 0, 0, component);
    		}
    	}

	// Maybe synchronize this with setTransferHandler() ?
    	if (transferHandler != null && cds.delStarted)
    		transferHandler.transferData(this);
	}

/*
 * http://www.developer.com/java/other/article.php/1579071

	Let's recap the steps involved in capturing audio data from 
	a microphone and saving that data in a ByteArrayOutputStream 
	object, as implemented by this program.

	- Identify the available mixers, and save a Mixer.Info 
	   object describing a compatible mixer. 
	- Instantiate an AudioFormat object that specifies a 
	   particular arrangement of audio data bytes in a sound stream.  Many options are available here. 
	- Instantiate a DataLine.Info object that describes an 
	   object of type TargetDataLine set up for the AudioFormat 
	   described above. 
	- Invoke the getMixer method of the AudioSystem class to get 
	   a reference to a Mixer object that matches the Mixer.Info 
	   object saved earlier. 
	- Invoke the getLine method on the Mixer object to get a 
	   TargetDataLine object that matches the characteristics of 
	   the DataLine.Info object instantiated earlier. 
	- Invoke the open method on the TargetDataLine object, 
	   passing the AudioFormat object as a parameter. 
	- Invoke the start method on the TargetDataLine object to 
	   cause the line to start acquiring data from the microphone. 
	- Spawn and start a Thread object to transfer audio data in 
	  real time from the internal buffer of the TargetDataLine 
	  object to a ByteArrayOutputStream object. 
	- When an appropriate amount of audio data has been captured, 
	  cause the Thread object to stop transferring data from the 
	  TargetDataLine object to the ByteArrayOutputStream object. 
	- Optionally invoke the stop method on the TargetDataLine 
	  object to cause it to stop acquiring audio data from the 
	  microphone. 
	Note that it isn't always necessary to explicitly specify a mixer as was done in this program.  A similar program in a previous lesson simply invoked the getLine method of the AudioSystem class to get a TargetDataLine object for a particular data format on a compatible mixer.  I elected to explicitly specify a mixer in this program for illustration purposes only.
*/


	public void setTransferHandler(BufferTransferHandler transferHandler) {
	this.transferHandler = transferHandler;
	}

	public boolean setEnabled(boolean value) {
	enabled = value;
	if (value == false) {
		if (!cds.delStarted) {
		try {
			cds.stopDelegate();
		} catch (IOException ioe) {
		}
		}
	} else {
		// Start the capture datasource if the monitor is enabled
		try {
		cds.startDelegate();
		} catch (IOException ioe) {
		}
	}
	return enabled;
	}

	public boolean isEnabled() {
	return enabled;
	}

	public Component getControlComponent() {
	if (component == null) {
		Dimension size = ((VideoFormat)getFormat()).getSize();
		component = new MyCanvas(size);
		component.setSize(size);
	}
	return component;
	}

	public float setPreviewFrameRate(float rate) {
	System.err.println("TODO");
	return rate;
	}
	
	public ContentDescriptor getContentDescriptor() {
	return actual.getContentDescriptor();
	}

	public long getContentLength() {
	return actual.getContentLength();
	}

	public boolean endOfStream() {
	return actual.endOfStream();
	}

	public Object [] getControls() {
	return new Object[0];
	}

	public Object getControl(String str) {
	return null;
	}

	class MyCanvas extends Canvas {
	Dimension size;
	
	public MyCanvas(Dimension size) {
		super();
		this.size = size;
	}

	public Dimension getPreferredSize() {
		return size;
	}
	}
	
	public int[] getCurrentSound(){
		return null;
	}
	
	public Image getCurrentImage(){
		if(currentImage==null)
    		System.out.println("currentImage(sourceImage)==null @ getCurrentImage");
		return this.currentImage;
	}
}
