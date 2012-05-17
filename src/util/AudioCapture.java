/*
 * 作成日: 2005/08/09
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
The original form of the following code is in the
http://www.developer.com/java/other/article.php/1579071

Java Sound, Getting Started, Part 2, Capture Using Specified Mixer
By Richard G. Baldwin

modified by t.yamanoue aug 2005

*/
/*File AudioCapture02.java
This program demonstrates the capture and 
subsequent playback of audio data.

A GUI appears on the screen containing the 
following buttons:
Capture
Stop
Playback

Input data from a microphone is captured and 
saved in a ByteArrayOutputStream object when the
user clicks the Capture button.

Data capture stops when the user clicks the Stop 
button.

Playback begins when the user clicks the Playback
button.

This version of the program gets and  displays a
list of available mixers, producing the following
output:

Available mixers:
Java Sound Audio Engine
Microsoft Sound Mapper
Modem #0 Line Record
ESS Maestro

Thus, this machine had the four mixers listed 
above available at the time the program was run.

Then the program gets and uses one of the 
available mixers instead of simply asking for a 
compatible mixer as was the case in a previous 
version of the program.

Either of the following two mixers can be used in
this program:

Microsoft Sound Mapper
ESS Maestro

Neither of the following two mixers will work in
this program.  The mixers fail at runtime for 
different reasons:

Java Sound Audio Engine
Modem #0 Line Record

The Java Sound Audio Engine mixer fails due to a 
data format compatibility problem.

The Modem #0 Line Record mixer fails due to an 
"Unexpected Error"

Tested using SDK 1.4.0 under Win2000
************************************************/

import java.io.*;
import java.util.Vector;

import javax.sound.sampled.*;
import javax.media.GainControl;

public class AudioCapture
// extends JFrame
{

  ByteArrayOutputStream byteArrayOutputStream;
  AudioFormat audioFormat;
  TargetDataLine targetDataLine;
  AudioInputStream audioInputStream;
  SourceDataLine sourceDataLine;

// added by t. yamanoue , 15 aug 2005
  public AudioTransmitter transmitter;
  CaptureThread captureThread=null;
  
  public void setAudioTransmitter(AudioTransmitter obj){
  	transmitter=obj;
  }

boolean stopCapture = false;

/*
  public static void main(String args[]){
	new AudioCapture();
  }//end main


  public AudioCapture(){//constructor
	final JButton captureBtn = 
						  new JButton("Capture");
	final JButton stopBtn = new JButton("Stop");
	final JButton playBtn = 
						 new JButton("Playback");

	captureBtn.setEnabled(true);
	stopBtn.setEnabled(false);
	playBtn.setEnabled(false);

	//Register anonymous listeners
	captureBtn.addActionListener(
	  new ActionListener(){
		public void actionPerformed(
							 ActionEvent e){
		  captureBtn.setEnabled(false);
		  stopBtn.setEnabled(true);
		  playBtn.setEnabled(false);
		  //Capture input data from the
		  // microphone until the Stop button is
		  // clicked.
		  captureAudio();
		}//end actionPerformed
	  }//end ActionListener
	);//end addActionListener()
	getContentPane().add(captureBtn);

	stopBtn.addActionListener(
	  new ActionListener(){
		public void actionPerformed(
							 ActionEvent e){
		  captureBtn.setEnabled(true);
		  stopBtn.setEnabled(false);
		  playBtn.setEnabled(true);
		  //Terminate the capturing of input data
		  // from the microphone.
		  stopCapture = true;
		}//end actionPerformed
	  }//end ActionListener
	);//end addActionListener()
	getContentPane().add(stopBtn);

	playBtn.addActionListener(
	  new ActionListener(){
		public void actionPerformed(
							 ActionEvent e){
		  //Play back all of the data that was
		  // saved during capture.
		  playAudio();
		}//end actionPerformed
	  }//end ActionListener
	);//end addActionListener()
	getContentPane().add(playBtn);

	getContentPane().setLayout(new FlowLayout());
	setTitle("Capture/Playback Demo");
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setSize(250,70);
	setVisible(true);
  }//end constructor
*/ // commented out by t.yamanoue,16aug2005
  // added by t.yamanoue, 15aug2005
  public AudioCapture(AudioTransmitter x){
  	this.setAudioTransmitter(x);
  }

  public Vector getAvailableInputMixers(){
	  Vector rtn=new Vector();
		try{
			  //Get and display a list of
			  // available mixers.
			  Mixer.Info[] mixerInfo = 
							  AudioSystem.getMixerInfo();
			  System.out.println("Available mixers:");
			  int cnt;
			  for(cnt = 0; cnt < mixerInfo.length; cnt++){
				System.out.println(mixerInfo[cnt].  getName());
			  }//end for loop

			  audioFormat = getAudioFormat();
			  DataLine.Info dataLineInfo=new DataLine.Info(
						TargetDataLine.class,
						audioFormat); // 18Nov2005
			  Mixer mixer=null;
			  int selectedLineNo=0;
		      for(cnt=0;cnt < mixerInfo.length;cnt++){
		    	try{
		    		  //Select one of the available mixers.
		    		  mixer = AudioSystem.getMixer(mixerInfo[cnt]); // original:[3]
		    		  //Get everything set up for capture
		    		  //Get a TargetDataLine on the selected
		    		  // mixer.
		    	    		  
		    		  targetDataLine = (TargetDataLine)
		    	    						 mixer.getLine(dataLineInfo);
		    	      System.out.println("mixer-"+cnt+":"+
		    	    		  mixerInfo[cnt].getName()+" is available for input");
		    	      rtn.add(new Integer(cnt));
		    	}
		    	catch(Exception e){
		    		continue;
		    	}
		      }
		      
			} 
			catch (Exception e) {
			  System.out.println(e);
//			  System.exit(0);
			}//end catch	  
	  return rtn;
  }
  
  public void captureAudio(int i){
	  try{
		  Mixer.Info[] mixerInfo = 
			  AudioSystem.getMixerInfo();
		  audioFormat = getAudioFormat();
		  DataLine.Info dataLineInfo=new DataLine.Info(
					TargetDataLine.class,
					audioFormat); // 18Nov2005
		  Mixer mixer=null;
	      mixer = AudioSystem.getMixer(mixerInfo[i]); // original:[3]
	      /*
	      FloatControl vol=(FloatControl)mixer.getControl(FloatControl.Type.VOLUME);
	      float maxvol=vol.getMaximum();
	      vol.setValue(maxvol);
	      */
		  targetDataLine = (TargetDataLine)mixer.getLine(dataLineInfo);
		  // prepare the line for use.    		
		  targetDataLine.open(audioFormat);
		  targetDataLine.start();
	      float gain=targetDataLine.getLevel();
	    	      
	      System.out.println("mixer-"+i+":"+
	    	    		  mixerInfo[i].getName()+" is selected, gain:"+gain+"\n");
	    	      
		  //Create a thread to capture the microphone
		  // data and start it running.  It will run
		  // until the Stop button is clicked.
	      this.startCapture();		  
	  }
	  catch(Exception e){
		  System.out.println(e);		  
	  }
  }
  
  //This method captures audio input from a
  // microphone and saves it in a
  // ByteArrayOutputStream object.
  public void captureAudio(){
	try{
	  //Get and display a list of
	  // available mixers.
	  Mixer.Info[] mixerInfo = 
					  AudioSystem.getMixerInfo();
	  System.out.println("Available mixers:");
	  int cnt;
	  for(cnt = 0; cnt < mixerInfo.length; cnt++){
		System.out.println(mixerInfo[cnt].  getName());
	  }//end for loop

	  audioFormat = getAudioFormat();
	  DataLine.Info dataLineInfo=new DataLine.Info(
				TargetDataLine.class,
				audioFormat); // 18Nov2005
	  Mixer mixer=null;
	  int selectedLineNo=0;
      for(cnt=0;cnt < mixerInfo.length;cnt++){
    	try{
    		  //Select one of the available mixers.
    		  mixer = AudioSystem.getMixer(mixerInfo[cnt]); // original:[3]
    		  //Get everything set up for capture
    		  //Get a TargetDataLine on the selected
    		  // mixer.
    	    		  
    		  targetDataLine = (TargetDataLine)
    	    						 mixer.getLine(dataLineInfo);
    	      System.out.println("mixer-"+cnt+":"+
    	    		  mixerInfo[cnt].getName()+" is available for input");
    	      selectedLineNo=cnt;
    	}
    	catch(Exception e){
    		continue;
    	}
      }
      
      mixer = AudioSystem.getMixer(mixerInfo[selectedLineNo]); // original:[3]
      /*
      FloatControl vol=(FloatControl)mixer.getControl(FloatControl.Type.VOLUME);
      float maxvol=vol.getMaximum();
      vol.setValue(maxvol);
      */
	  targetDataLine = (TargetDataLine)mixer.getLine(dataLineInfo);
	  // prepare the line for use.    		
	  targetDataLine.open(audioFormat);
	  targetDataLine.start();
      float gain=targetDataLine.getLevel();
    	      
      System.out.println("mixer-"+selectedLineNo+":"+
    	    		  mixerInfo[selectedLineNo].getName()+" is selected, gain:"+gain+"\n");
    	      
	  //Create a thread to capture the microphone
	  // data and start it running.  It will run
	  // until the Stop button is clicked.
//      this.startCapture();

	} 
	catch (Exception e) {
	  System.out.println(e);
//	  System.exit(0);
	}//end catch
  }//end captureAudio method

  public void startCapture(){
	  captureThread = new CaptureThread();
	  captureThread.start();
	  
  }
  public void stopCapture(){
  	if(this.captureThread!=null)
  		this.stopCapture=true;
//  	 this.captureThread.stop();
  }

  //This method plays back the audio data that
  // has been saved in the ByteArrayOutputStream
  private void playAudio() {
	try{
	  //Get everything set up for playback.
	  //Get the previously-saved data into a byte
	  // array object.
	  byte audioData[] = byteArrayOutputStream.
								   toByteArray();
	  //Get an input stream on the byte array
	  // containing the data
	  InputStream byteArrayInputStream =
			 new ByteArrayInputStream(audioData);
	  AudioFormat audioFormat = getAudioFormat();
	  audioInputStream = new AudioInputStream(
					byteArrayInputStream,
					audioFormat,
					audioData.length/audioFormat.
								 getFrameSize());
	  DataLine.Info dataLineInfo = 
							new DataLine.Info(
							SourceDataLine.class,
							audioFormat);
	  sourceDataLine = (SourceDataLine)
			   AudioSystem.getLine(dataLineInfo);
	  sourceDataLine.open(audioFormat);
	  sourceDataLine.start();

	  //Create a thread to play back the data and
	  // start it  running.  It will run until
	  // all the data has been played back.
	  Thread playThread = new PlayThread();
	  playThread.start();
	} catch (Exception e) {
	  System.out.println(e);
//	  System.exit(0);
	}//end catch
  }//end playAudio

  //This method creates and returns an
  // AudioFormat object for a given set of format
  // parameters.  If these parameters don't work
  // well for you, try some of the other
  // allowable parameter values, which are shown
  // in comments following the declartions.
  public AudioFormat getAudioFormat(){
	float sampleRate = 8000.0F;
	//8000,11025,16000,22050,44100
	int sampleSizeInBits = 16;
	//8,16
	int channels = 1;
	//1,2
	boolean signed = true;
	//true,false
	boolean bigEndian = true; //false
	//true,false
	int frameSize = 2;
	//
	float frameRate = 8000.0F;
	//
	AudioFormat rtn=
		new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				sampleRate,
				sampleSizeInBits,
				channels,
				frameSize,  
				frameRate, 
				bigEndian
				);
	return rtn;
	/*
	return new AudioFormat(
					  sampleRate,
					  sampleSizeInBits,
					  channels,
					  signed,
					  bigEndian); //bigEndian
	*/
  }//end getAudioFormat
//=============================================//

//Inner class to capture data from microphone
byte tempBuffer[] = new byte[800]; //10000 -> 
                                     // sampling rate: 16000 byte/sec
                                     //   (8000 sampling, 16bit /sec)
                                     // 800(50msec delay) ->400(25msec delay) 
class CaptureThread extends Thread{
  //An arbitrary-size temporary holding buffer
  public void run(){
	byteArrayOutputStream =
					 new ByteArrayOutputStream();
	stopCapture = false;
	try{//Loop until stopCapture is set by
		// another thread that services the Stop
		// button.
	  while(!stopCapture){
		//Read data from the internal buffer of
		// the data line.
		int cnt = targetDataLine.read(tempBuffer,
							  0,
							  tempBuffer.length);
		if(cnt > 0){
		  /*
		  //Save data in output stream object.
		  byteArrayOutputStream.write(tempBuffer,
									  0,
									  cnt);
	      System.out.println("capt..cnt="+cnt);
	      */
	      // modified by t. yamanoue, 15Aug2005
	      transmitter.send(tempBuffer,cnt);
	      /*// 2007 4/6, yamanoue */
		}
		else{
	        try{
	    	  Thread.sleep(1);
	        }
	        catch(InterruptedException e){}
	      /* */
		}//end if
	  }//end while
	  byteArrayOutputStream.close();
	}catch (Exception e) {
	  System.out.println(e);
//	  System.exit(0);
	}//end catch
  }//end run
  
}//end inner class CaptureThread
//===================================//
//Inner class to play back the data
// that was saved.
class PlayThread extends Thread{
  byte tempBuffer[] = new byte[1600];

  public void run(){
	try{
	  int cnt;
	  //Keep looping until the input read method
	  // returns -1 for empty stream.
	  while((cnt = audioInputStream.read(
					  tempBuffer, 0,
					  tempBuffer.length)) != -1){
		if(cnt > 0){
		  //Write data to the internal buffer of
		  // the data line where it will be
		  // delivered to the speaker.
		  sourceDataLine.write(tempBuffer,0,cnt);
		}//end if
	  }//end while
	  //Block and wait for internal buffer of the
	  // data line to empty.
	  sourceDataLine.drain();
	  sourceDataLine.close();
	}catch (Exception e) {
	  System.out.println(e);
//	  System.exit(0);
	}//end catch
  }//end run
}//end inner class PlayThread
//=============================================//

}//end outer class AudioCapture02.java
