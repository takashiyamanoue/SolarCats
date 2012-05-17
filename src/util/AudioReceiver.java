/*
 * 作成日: 2005/08/15
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package util;
import controlledparts.*;
import java.io.*;
import java.util.Hashtable;

import javax.sound.sampled.*;

import nodesystem.eventrecorder.Timer;

/**
 * @author yamachan
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
public class AudioReceiver {
	BinaryData binaryData;
	ControlledFrame frame;
	Thread me=null;
	
	boolean stopCapture = false;
	ByteArrayOutputStream byteArrayOutputStream;
	AudioFormat audioFormat;
	TargetDataLine targetDataLine;
	AudioInputStream audioInputStream;
	SourceDataLine sourceDataLine;
	AudioCapture capture;
	DataLine.Info dataLineInfo;
	Hashtable sdLines;
	Mixer mixer;
	int linesMax;
	EnDecode codec;
	byte[] decoded;
	
	public AudioReceiver(AudioCapture x){
		this.capture=x;
		byteArrayOutputStream =
						 new ByteArrayOutputStream();
		sdLines=new Hashtable();
		try{
		  //Get everything set up for playback.
		  //Get the previously-saved data into a byte
		  // array object.
//		  byte audioData[] = byteArrayOutputStream.
//									   toByteArray();
		  //Get an input stream on the byte array
		  // containing the data
//		  InputStream byteArrayInputStream =
//				 new ByteArrayInputStream(audioData);
		  audioFormat = capture.getAudioFormat();
		  /*
		  audioInputStream = new AudioInputStream(
						byteArrayInputStream,
						audioFormat,
						audioData.length/audioFormat.
									 getFrameSize());
									 */
		  dataLineInfo = 
								new DataLine.Info(
								SourceDataLine.class,
								audioFormat);
		  /*
		  sourceDataLine = (SourceDataLine)
				   AudioSystem.getLine(dataLineInfo);
				   */
		  Mixer.Info[] ifs=AudioSystem.getMixerInfo();
		  mixer=AudioSystem.getMixer(ifs[0]);
		  linesMax=mixer.getMaxLines(dataLineInfo);
		  Line[] slines=mixer.getSourceLines();
		  /*
		  sourceDataLine.open(audioFormat);
		  sourceDataLine.start();
		  slines=mx.getSourceLines();
		  Line l2=mx.getLine(dataLineInfo);
		  l2.open();
		  slines=mx.getSourceLines();
		  Line l3=mx.getLine(dataLineInfo);
		  l3.open();
		  slines=mx.getSourceLines();
		  System.out.println("source line number="+slines.length);
		  */
		  /*
		  sourceDataLine=(SourceDataLine)mx.getLine(dataLineInfo);
		  sourceDataLine.open(audioFormat);
		  sourceDataLine.start();
		  */
		  //Create a thread to play back the data and
		  // start it  running.  It will run until
		  // all the data has been played back.
		} catch (Exception e) {
		  System.out.println(e);
		  this.sourceDataLine=null;
//		  System.exit(0);
		}//end catch
		
		// for G711 codec
		codec=new EnDecode();
		decoded=new byte[1000];
		this.skipControllers=new Hashtable();
	  }//end playAudio
	
	public void receive(String x)  // this is called from AudioCapture
	{
		if(sourceDataLine==null) return;
//		BinaryData bdata=new BinaryData();
		bdata.string2bin(x); 
		this.decodeG711(bdata.getByteArray(),bdata.getLength(),decoded); // for G711
        try{
//			sourceDataLine.write(bdata.getByteArray(),0,bdata.getLength()); // for G711
        	sourceDataLine.write(decoded,0,(bdata.getLength())*2);
	    }
	    catch (Exception e) {
		    System.out.println(e);
//		    System.exit(0);
	    }//end catch
	}//end run	
	public void receive(String name, String x){
		SourceDataLine l=(SourceDataLine)(sdLines.get(name));
		if(l==null) return;
//		BinaryData bdata=new BinaryData();
		bdata.string2bin(x);
		this.decodeG711(bdata.getByteArray(),bdata.getLength(),decoded); // for G711
        try{
//			l.write(bdata.getByteArray(),0,bdata.getLength());
        	l.write(decoded,0,(bdata.getLength())*2);
	    }
	    catch (Exception e) {
		    System.out.println(e);
//		    System.exit(0);
	    }//end catch
		
	}
	BinaryData bdata=new BinaryData();
	public void receive(SourceDataLine l, String x){
		if(l==null) return;
//		BinaryData bdata=new BinaryData();
		bdata.string2bin(x);
		this.decodeG711(bdata.getByteArray(),bdata.getLength(),decoded); // for G711
        try{
//			l.write(bdata.getByteArray(),0,bdata.getLength());
        	l.write(decoded,0,(bdata.getLength())*2);
	    }
	    catch (Exception e) {
		    System.out.println(e);
//		    System.exit(0);
	    }//end catch
		
	}
	public void receive(SourceDataLine l, byte[] x){
		if(l==null) return;
//		System.out.println("receive audio "+ x.length+" byte");
//		BinaryData bdata=new BinaryData();
//		this.decodeG711(x,x.length,decoded); // for G711
        try{
//			l.write(bdata.getByteArray(),0,bdata.getLength());
//        	l.write(decoded,0,x.length*2);
        	l.write(x,0,x.length);
	    }
	    catch (Exception e) {
		    System.out.println(e);
//		    System.exit(0);
	    }//end catch
				
	}
	public SourceDataLine getSourceLine(String name){
		SourceDataLine l=(SourceDataLine)(sdLines.get(name));
        return l;		
	}
	
	public void addNewSourceLine(String name){
		try{
		  SourceDataLine l=(SourceDataLine)mixer.getLine(dataLineInfo);
		  l.open(audioFormat);
		  l.start();
		  sdLines.put(name,l);
		}
		catch(Exception e){
		    System.out.println(e);			
		}
	}
	
	public void removeSourceLine(String name){
		SourceDataLine l=(SourceDataLine)sdLines.get(name);
		try{
			  l.stop();
			  l.close();
			  sdLines.remove(name);
			}
			catch(Exception e){
			    System.out.println(e);			
			}		
	}
	
	public void stopReceive()
	{
		if(sourceDataLine==null) return;
		try{
			sourceDataLine.drain();
			sourceDataLine.close();
		}
		catch(Exception e){
			System.out.println(e);
		}	  	
	}
	// for G711 decode
	public void decodeG711(byte[] x,int l, byte[] y){
//		byte[] rtn=new byte[x.length*2];
		if(l*2>y.length) return;
		for(int i=0;i<l;i++){
			short c=codec.decode(x[i]);
			byte c0=(byte)(0x00ff & (c>>8));
			byte c1=(byte)(0x00ff & c);
			y[i*2]=c0;
			y[i*2+1]=c1;
		}
//		return rtn;
	}
	Parameters skipParameters;
	public void setSkipParameters(Parameters p){
		this.skipParameters=p;
	}
	Hashtable skipControllers;
	Timer timer=Timer.getTimer();
	public boolean largeDelay(String name, int sentTime){
		SkipControl c=(SkipControl)(skipControllers.get(name));
		if(c==null){
			c=new SkipControl(this.timer,this.skipParameters);
			skipControllers.put(name, c);
            return false;			
		}	
		return c.largeDelay(sentTime);
	}	
}
