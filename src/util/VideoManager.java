/*
 * 作成日: 2005/01/23
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package util;
import java.util.Hashtable;

import nodesystem.AMessage;

import controlledparts.ControlledFrame;
public class VideoManager extends java.lang.Object implements Runnable
{
	public ControlledFrame frame;
	public Thread me;
	public int imageCount;
	public VideoOperator last;
	public VideoOperator first;
	public Hashtable videoOperators;
	private int sendInterval;
	
	public VideoManager(){
	}
	
	public void setSendInterval(int t){
		if(t<0) this.sendInterval=1;
		else
		this.sendInterval=t;
	}
	
	public void setFrame(ControlledFrame f){
		this.frame=f;
	}
	public void setImageOperator(String name,VideoOperator img)
	{
		videoOperators.put(name,img);
	}
	public void init()
	{
		videoOperators=new Hashtable();
		imageCount=0;
		first=null;
		last=null;
	}
	
	public VideoOperator getVideoOperator(String name)
	{
		VideoOperator imgOperator=(VideoOperator)(videoOperators.get(name));
		return imgOperator;
	}
	public synchronized void send(VideoOperator p)
	{
		if(frame==null) {
//			  this.notifyAll();
			return;}
		if(this.frame.isControlledByLocalUser()){
//			p.createNewSourceImage();
    		AMessage am=p.nextImage();
	    	if(am!=null){
	    		String sendingString=am.getHead();
//	    		System.out.println("vm-s "+sendingString);
	    		am.setHead("img("+sendingString+")\n");
		    	frame.sendEvent(am);
		    }
	    	/*
			try{
	   			Thread.sleep(1); // waiting time.
		    }
			catch(InterruptedException e){
				   System.out.println(""+e.getStackTrace());
			}
			*/
		    return;
		}
		else{
//			p.setSourceImage(null);
//			p.setMonitorStream(null);
		}
//		  this.notifyAll();
	}
	public void stop()
	{
		if(me!=null){
//			  me.stop();
			me=null;
		}
	}
	public void start()
	{
		if(me==null){
			me=new Thread(this,"VideoManager");
			me.start();
		}
	}
	public void run()
	{
		int count=0;
		int maxcount=5;
		VideoOperator p=null;
		while(me!=null){
			if(frame==null) {me=null; return;}
			if(p==null){
				  p=first;
				  p.createNewSourceImage();
		   }
			while(p!=null){
				send(p);
				/*
				if(count>maxcount){                  
				  count=0;
				}
				else count++;
				*/
				p=p.next;
			}
		    try{
			   Thread.sleep(this.sendInterval); //!!!  video sampling interval
		    }
		    catch(InterruptedException e){
		       System.out.println(e);
	        }
		}
	}
	public VideoManager(ControlledFrame f) //Draw gui
	{
		init();
		frame=f;
	}
	public String newName()
	{
		String rtn= "image"+imageCount;
		imageCount++;
		return rtn;
	}
	public void add(String name,VideoOperator io)
	{
		VideoOperator lastOperator=(VideoOperator)(videoOperators.get(name));
		if(lastOperator!=null) {
//			lastOperator.setDestinationImage(null);
//			lastOperator.setDestinationPanel(null);
			lastOperator.setSourceImage(null);
			VideoOperator pv=lastOperator.previous;
			if(pv!=null){
			    pv.next=null;
			}
//			lastOperator.setSourcePanel(null);
//			lastOperator.setMonitorStream(null);
			videoOperators.remove(lastOperator);
		} 
		io.setName(name);
        this.setImageOperator(name,io);
		if(first==null) {
			first=io;
			io.next=null;
			io.previous=null; // 2007/01/07
			last=io;
			return;
		}
		last.next=io;
		io.next=null;
		io.previous=last; // 2007/01/07
		last=io;
	}
	/*
	public void startVideoSend(){
		// Get the processor's output, create a DataSink and connect the two.
		DataSource outputDS = processor.getDataOutput();
		try {
			MediaLocator ml = new MediaLocator("file:capture." +
					 (outputType.equals("video.x_msvideo")? "avi" : "mov"));
			datasink = Manager.createDataSink(outputDS, ml);
			datasink.open();
			datasink.start();
		} catch (Exception e) {
			System.err.println(e);
		}
		processor.start();
		System.out.println("Started saving...");		
	}
	*/
	
	// 2007/1/7, 画像パネル削除 ... 同じ画面が２枚以上移るバグ対策
	public void remove(String name)
	{
		VideoOperator lastOperator=(VideoOperator)(videoOperators.get(name));
		if(lastOperator!=null){
			lastOperator.clearImage();
	    	videoOperators.remove(name);
	    	lastOperator.setName("*");
	    	VideoOperator pv=lastOperator.previous;
	    	if(pv!=null)
    	    	pv.next=lastOperator.next;
		}

	}
}

