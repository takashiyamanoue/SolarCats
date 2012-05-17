/*
 * 作成日: 2005/08/15
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package util;

import nodesystem.AMessage;
import controlledparts.*;

/**
 * @author yamachan
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
public class AudioTransmitter implements Runnable
{
	BinaryData binaryData;
	ControlledFrame frame;
	Thread me=null;
	
	BinaryData queue[];
	int queueMax=5;
	int pq1;
	int pq2;
	int rest;
	
	Indicator indlsn;
	String lineID;
	EnDecode codec;
	byte[] encoded;

	public AudioTransmitter(ControlledFrame x){
		queue= new BinaryData[queueMax];
		pq1=0;
		pq2=0;
		rest=0;
		codec=new EnDecode();
		encoded=new byte[500];
		setFrame(x);
	}
	public void setindicatorListener(Indicator x){
		indlsn=x;
	}
	
    public void setLineID(String x){
    	lineID=x;
    }
	
	public int indicate(byte[] x, int count){
		int compBoxMax =0;
		for(int i=0;i<count;i=i+2){
			byte btmBox = x[i];
			byte topBox = x[i+1];
//			int checker=0;
			int compBox =topBox<<8;
			compBox = compBox | (0xff & (int)btmBox);
			compBox=Math.abs(compBox);
			if(compBoxMax < compBox)compBoxMax =compBox;
			//System.out.println(compBox);				//for check
			//System.out.println(compBoxMax);				//for check
			indlsn.setLevel(compBox);			
			
/*			int checkBox=compBox;
			for(int j=0;j<15;j++){
				if(checkBox%2!=0)checker=j;
				checkBox=checkBox/2;
				}
			System.out.println(checker);
			indlsn.setLevel(checker);
*/		
			}
		return compBoxMax;
	}
	public void send(byte[] x,int count)  // this is called from AudioCapture
	{
//		if(me==null) return;
		if(lineID==null) return;
		int compBoxMax = indicate(x,count);
		//System.out.print(compBoxMax+" "+indlsn.getLimit());	//for check
		if(!this.frame.isControlledByLocalUser()) return;
		if(indlsn.getLimit()<compBoxMax){
			/*
//			System.out.println("count="+count);
    		BinaryData bdata=new BinaryData();
    		// t.yamanoue, 2006 12/2 for G711 encode
    		encodeG711(x,encoded); //for G711
//	    	bdata.setDataDeep(x);
    		bdata.setDataDeep(encoded); // for G711
//    		bdata.setDataShallow(encoded); //for G711
	    	bdata.setLength(count/2);
	    	queue[pq1]=bdata;
	    	rest++;
	    	pq1++;
	    	if(pq1>=queueMax) pq1=0;
	    	*/
			this.transmit(x,count);
		}
	}
	public void setFrame(ControlledFrame f){
		this.frame=f;
	}
	public void setIndicator(Indicator x){
		this.indlsn=x;
	}
	public void transmit(byte[] data,int length){
		if(frame==null) return;
		if(!(frame.isControlledByLocalUser())) return;
	       AMessage am=new AMessage();
		       am.setHead("audio(\""+lineID+"\",\"binary\")\n");
		       am.setData(data,length);
//	       frame.sendEvent("audio(\""+lineID+"\",\""+s+"\")\n");
		       frame.sendEvent(am);		
	}
    public void run(){
    	while(me!=null){
    		while(rest>0){
    		   BinaryData d=queue[pq2];
    		   int count=d.getLength();
    		   int xmtu=800; //xmtu*1.5<1500
		
			   int sentCount=0;
//			binaryData.setLength(x.length);
			   while(sentCount<count){
			   	   int xlen=0;
			   	   if(sentCount+xmtu<count) xlen=xmtu;
			   	   else xlen= xmtu=count-sentCount;
//  			       String s=d.bin2str(sentCount,xlen);
  			       AMessage am=new AMessage();
  			       am.setHead("audio(\""+lineID+"\",\"binary\")\n");
  			       am.setData(d.getByteArray(),d.getLength());
//			       frame.sendEvent("audio(\""+lineID+"\",\""+s+"\")\n");
  			       frame.sendEvent(am);
			       sentCount+=xmtu;
			       try{
			    	   Thread.sleep(1);
			       }
			       catch(InterruptedException e){
			    	   
			       }
			   }
			   pq2++;
			   if(pq2>=queueMax) pq2=0;
			   rest--;
			   if(me==null) return;
    		}
			if(rest==0){
			   try{
				 Thread.sleep(1);
			   }
			   catch(InterruptedException e){
			   }
			}
    	}
    }

    public void start(){
    	if(me==null){
    		me=new Thread(this,"audio transmitter");
    		me.start();
    	}
    }
    public void stop(){
    	if(me!=null){
        	me=null;
    	}
    }
    /*  
     * added by t.yamanoue, 2006 12/2, G711 enocde/decode
     */
    public void encodeG711(byte[] x,byte[] y){
//    	byte[] rtn=new byte[x.length/2];
    	if(x.length>y.length*2) return;
    	for(int i=0;i<x.length/2;i++){
    		short c=(short)(x[i*2]<<8|(x[i*2+1]&0x00ff));
    		y[i]=codec.encode(c);
    	}
//    	return rtn;
    }

}
