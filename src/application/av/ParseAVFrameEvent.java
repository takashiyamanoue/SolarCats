/*
 * 作成日: 2005/01/23
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package application.av;

import java.awt.Color;

import javax.sound.sampled.SourceDataLine;

import controlledparts.*;
import util.VideoManager;
import util.VideoOperator;
import util.AudioReceiver;

/**
 * @author yamachan
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
public class ParseAVFrameEvent extends ParseEvent {

    private AVFrame frame;
    private VideoOperator videoOperator;
	byte[] bin=null;
	public void setBinary(byte[] x){
		this.bin=x;
	}
    
	public boolean parseImageRec(VideoManager im)
	{
		parseB();
		if(!iq.rString("img(")) return false;
		parseB();
		if(!iq.rString("name(")) return false;
		StringBuffer str=iq.rStrConst();
		if(!iq.rString(").")) return false;  //name(<string>)
		if(str==null) return false;
		String name=str.toString();
		if(name.equals("*")){
			System.out.println("name="+name);
		}


		if(!iq.rString("(")) return false;
		if(!rNumPair()) return false;
		if(!iq.rString(").")) return false; //(<number>,<number).
		int offx=x1; int offy=y1;


		if(!iq.rString("width(")) return false;
		Integer wi=iq.rInteger();
		if(!iq.rString(").")) return false;  //width(<number>).
		if(wi==null) return false;
		int w=wi.intValue();

		if(!iq.rString("height(")) return false; 
		Integer hi=iq.rInteger();
		if(!iq.rString(").")) return false;//height(<number>).
		if(hi==null) return false;
		int h=hi.intValue();

		boolean isFirstTime=false;
		videoOperator=im.getVideoOperator(name);
		/*
		if(videoOperator.largeDelay(this.sentTime)) {
			this.skipRest();
			return true;
		}
		*/
		if(iq.rString("create.")){
//			videoOperator.getMonitorStream().setEnabled(false);
           im.stop();
		   if(videoOperator==null){
		   	   System.out.println("parseAVFrame, videoOperator==null");
			   isFirstTime=true;
//			   videoOperator=new VideoOperator(frame,null,null, null, this.frame.avPanel);
			   videoOperator=new VideoOperator(frame,this.frame.avPanel);
			   this.videoOperator.setSkipParameters(this.frame.getTimer(), this.frame.getVideoSkipParameters());
			   im.add(name,videoOperator);
		   }
		   videoOperator.setMonitorStream(null);
		   if(offx==0 && offy==0 && w==10)
		   {
//			  System.out.println("xxx");
		   }
		   if(!iq.rString("color(")) return false;
		   Integer ci=iq.rInteger();
		   if(!iq.rString(")")) return false;  // color(<number>)
		   if(hi==null) return false;
		   int cx=ci.intValue();
		   Color color=new Color(cx);

 		  if(!iq.rString(")")) return false;     // img( .... )
      
	//
		   return true;
		}
		else
		if(iq.rString("tile.")){
			while(true){
			   if(!iq.rString("color(")) return false;
			   Integer ci=iq.rInteger();
			   if(!iq.rString(")")) return false;  // color(<number>)
			   if(hi==null) return false;
			   int cx=ci.intValue();
			   Color color=new Color(cx);

			   videoOperator=im.getVideoOperator(name);
			   if(videoOperator==null) return false;
			   videoOperator.fillRect(offx,offy,w,h,color);
			   if(!iq.rString("-")) break;        // -
			   offx=offx+w;
			}
			if(!iq.rString(")")) return false;     // img( .... )
			return true;
	   }
		else
		if(iq.rString("bmp.")){
		   str=iq.rStrConst();
		   if(str==null) return false;

		   videoOperator=im.getVideoOperator(name);
		   if(videoOperator==null) return false;
		   videoOperator.string2subImg(offx,offy,w,h,str.toString());
//		   this.frame.avPanelFrame.repaint();
		   if(!iq.rString(")")) return false;     // img( .... )

		   return true;
		}
		else
			if(iq.rString("binary")){
				videoOperator=im.getVideoOperator(name);
				if(videoOperator==null) return false;
				videoOperator.setBinaryImage(offx,offy,w,h,this.bin);
			   if(!iq.rString(")")) return false;     // img( .... )
			   return true;
			}
		else
		if(iq.rString("jpeg")){
						videoOperator=im.getVideoOperator(name);
						if(videoOperator==null) return false;
						videoOperator.setJpegImage(offx,offy,w,h,this.bin);
					   if(!iq.rString(")")) return false;     // img( .... )
					   return true;			
		}
		else
		if(iq.rString("sImg(")){
						StringBuffer sb=iq.rStrConst();
						if(sb==null) return false;
						if(!iq.rString(")")) return false;
						videoOperator=im.getVideoOperator(name);
						if(videoOperator==null) return false;			
						videoOperator.setSampledImage(sb.toString(), offx, offy, w, h, this.bin);
						if(!iq.rString(")")) return false; // img( ...)
						return true;
		}
		else
		if(iq.rString("paint")){
			videoOperator=im.getVideoOperator(name);
			if(videoOperator==null) return false;
			videoOperator.paintOffScreen();
			if(!iq.rString(")")) return false;
			return true;
		}
		else return false;
//		  gui.canvas.repaint();
//		  return true;
	}
/*
    public boolean parseAudio(AudioReceiver r){
		if(!iq.rString("audio(")) return false;
		StringBuffer x=this.iq.rStrConst();
		if(!iq.rString(")")) return false;
//		int i=c.intValue();
		if(r!=null){
			   r.receive(x.toString());
		}
        
		return true;
     }
     */
    public boolean parseAudio(AudioReceiver r){
		if(!iq.rString("audio(")) return false;
		StringBuffer id=this.iq.rStrConst();
		if(!iq.rString(",")) return false;
		StringBuffer x=this.iq.rStrConst();
		if(!iq.rString(")")) return false;
//		int i=c.intValue();
		if(r!=null){
//			   r.receive(id.toString(),x.toString());
			String name=id.toString();
			if(r.largeDelay(name,this.sentTime)) {
				this.skipRest();
				return true;
			}
			SourceDataLine l=r.getSourceLine(name);
			if(l==null){
				r.addNewSourceLine(name);
			}
			if(x.indexOf("binary")==0){
				r.receive(l,bin);
			}
			else
			r.receive(l,x.toString());
		}
        
		return true;
     }

	public boolean parseEvent()
	{
		boolean rtn;
		if( this.parseButtonEvent(frame)                ||
			this.parseTextField(frame)                  ||
			parseImageRec(frame.videoManager)    ||
			parseAudio(frame.areceiver) ||
			parseFocus(frame)    ||
			parseFrameEvent(frame)
			)
			rtn= true;
		else rtn= false;
		return rtn;

	}

	public ParseAVFrameEvent(AVFrame f, InputQueue q)
	{
		iq=q;
		frame=f;
		me=null;
//		  start();
	}
    int sentTime;
    public void setSentTime(int t){
    	sentTime=t;
    }
}