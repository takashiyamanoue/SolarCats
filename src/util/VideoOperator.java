/*
 * 作成日: 2005/01/23
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.media.protocol.DataSource;

import nodesystem.AMessage;
import nodesystem.eventrecorder.Timer;

public class VideoOperator extends java.lang.Object
						   implements ImageObserver
{
	private int pix12[][];
	private int pix24[][];
	private int pix6[][];
	private SubImageClass subImageArray[][];
	private SubImageClass subImageArray2[][];
	private Object subPixelBuffer[][];
	private Object subImageBuffer[][];
	 // This subImageArray is used for extracting the motion
	 // on the screen in order to reduce the network traffic.
	 // This array keeps sub-images of the screen of the last time.
	 // The only sub-images which are different from the
	 // latest screen is sent to other nodes.
	private int widthPer6;
	private int heightPer6;
	private int pix36[][];
	private int iY36;
	private int iX36;
	private int heightPer36;
	private int widthPer36;
	private int iY6;
	private int iX6;
	private int iY12;
	private int iX12;
	private int iY24;
	private int iX24;
	private int heightPer12;
	private int widthPer12;
	private int heightPer24;
	private int widthPer24;
	private int height;
	private int width;
	private int eWidth=32;
	public String name;
	private Image sourceImage;
	private Image destinationImage;
	private PixelGrabber pixelGrabber;
	private boolean isCreatingImage;
	private int sendingH;
	private int sendingW;
	private int sendingY;
	private int sendingX;

	private boolean isSending;	
	private boolean loadingflag;
	public VideoOperator next;
	public VideoOperator previous; // 2007/1/7 VideoManager remove追加のため
	private int i24or12;
	private int flags;
	private Graphics offGr; //Graphics for double buffering
	private Image offImg;   //Image for double buffering

	float cmpQuality;
	
	public char i2a[]={
		'0','1','2','3','4','5','6','7','8','9',
		'a','b','c','d','e','f','g','h','i','j',
		'k','l','m','n','o','p','q','r','s','t',
		'u','v','w','x','y','z','!','#','$','%',
		'A','B','C','D','E','F','G','H','I','J',
		'K','L','M','N','O','P','Q','R','S','T',
		'U','V','W','X'};
	public int a2i[];

		
	private Component sourcePanel;
	private Component destinationPanel;
	private MonitorStream monitorStream;
	private SendReceiveAVControl frame;
	private DataSource dataSource;
	private Color defaultBackground;
    int int2StringSubimgSize=6;

//	public VideoOperator(SendReceiveAVControl f, MonitorStream ms, DataSource ds, Component ps, Component pd)
	public VideoOperator(SendReceiveAVControl f, Component pd)
	{
		this.frame=f;
//		this.setMonitorStream(ms);
//		this.setSourcePanel(ps);
		this.setDestinationPanel(pd);
//		this.setDataSource(ds);
		init();
		init2();
		this.prepareOffScreenImage();
		this.int2StringSubimg=new char[int2StringSubimgSize];
	}
	
	String coding="";
	int subImageError=10;
	int subImageErrorStep=4;
	
    public void setParameters(Parameters p){
    	this.eWidth=p.getInt("pictureSegmentWidth");
    	this.coding=p.getString("pictureCoding");
    	this.subImageError=p.getInt("errorRate");
    	this.subImageErrorStep=p.getInt("errorSamplingWidth");
    }
	
   public void setMonitorStream(MonitorStream ms){
//	   System.out.println("setMonitorStream(..)");
   	    this.monitorStream=ms;
   	    /*
   	    if(this.monitorStream==null)
   	    	 System.out.println("monitorStream=null");
   	    else
   	    	 System.out.println("monitorStream---x");
   	   */
   }

   public void setDataSource(DataSource ds){
     	this.dataSource=ds;
   }

    public void setSourcePanel(Component p){
		this.sourcePanel=p;
    	if(p==null) return;
    	this.width=p.getWidth();
    	this.height=p.getHeight();
    	this.createNewSourceImage();
    	System.out.println("set source panel: w="+width+" h="+height);
    }

    public void setDestinationPanel(Component p){
    	this.destinationPanel=p;
    	if(this.destinationPanel==null) return;
    	this.width=destinationPanel.getWidth();
    	this.height=destinationPanel.getHeight();
    	this.createNewDestinationImage();
    	System.out.println("set source panel: w="+width+" h="+height);

    }

    public void prepareOffScreenImage(){
		offImg=destinationPanel.createImage(this.destinationPanel.getWidth(),
		                   this.destinationPanel.getHeight());
		offGr=offImg.getGraphics();    	
    }
    
    public MonitorStream getMonitorStream(){
    	return monitorStream;
    }


	public void init()
	{
		a2i=new int[256];
		a2i[(int)'0']=0;a2i[(int)'1']=1;a2i[(int)'2']=2;a2i[(int)'3']=3; a2i[(int)'4']=4;
		a2i[(int)'5']=5;a2i[(int)'6']=6;a2i[(int)'7']=7;a2i[(int)'8']=8; a2i[(int)'9']=9;

		a2i[(int)'a']=10;a2i[(int)'b']=11;a2i[(int)'c']=12;a2i[(int)'d']=13;a2i[(int)'e']=14;
		a2i[(int)'f']=15;a2i[(int)'g']=16;a2i[(int)'h']=17;a2i[(int)'i']=18;a2i[(int)'j']=19;
		a2i[(int)'k']=20;a2i[(int)'l']=21;a2i[(int)'m']=22;a2i[(int)'n']=23;a2i[(int)'o']=24;
		a2i[(int)'p']=25;a2i[(int)'q']=26;a2i[(int)'r']=27;a2i[(int)'s']=28;a2i[(int)'t']=29;
		a2i[(int)'u']=30;a2i[(int)'v']=31;a2i[(int)'w']=32;a2i[(int)'x']=33;a2i[(int)'y']=34;
		a2i[(int)'z']=35;a2i[(int)'!']=36;a2i[(int)'#']=37;a2i[(int)'$']=38;a2i[(int)'%']=39;

		a2i[(int)'A']=40;a2i[(int)'B']=41;a2i[(int)'C']=42;a2i[(int)'D']=43;a2i[(int)'E']=44;
		a2i[(int)'F']=45;a2i[(int)'G']=46;a2i[(int)'H']=47;a2i[(int)'I']=48;a2i[(int)'J']=49;
		a2i[(int)'K']=50;a2i[(int)'L']=51;a2i[(int)'M']=52;a2i[(int)'N']=53;a2i[(int)'O']=54;
		a2i[(int)'P']=55;a2i[(int)'Q']=56;a2i[(int)'R']=57;a2i[(int)'S']=58;a2i[(int)'T']=59;
		a2i[(int)'U']=60;a2i[(int)'V']=61;a2i[(int)'W']=62;a2i[(int)'X']=63;a2i[(int)'Y']=64;

		flags=0;
		isSending=false;
		isCreatingImage=true;
	}
	
    public void init2()
	{
		if(width>0 && height>0){
			heightPer36=height/36;
			widthPer36=width/36;
		   heightPer24=height/24;
		   widthPer24=width/24;
		   heightPer12=height/12;
		   widthPer12=width/12;
		   heightPer6=height/6;
		   widthPer6=width/6;

		   iX36=0;
		   iY36=0;
		   iX24=0;
		   iY24=0;
		   iX12=0;
		   iY12=0;
		   iX6=0;
		   iY6=0;

		   i24or12=0;

		   pix36=new int[widthPer36][heightPer36];
		   pix24=new int[widthPer24][heightPer24];
		   pix12=new int[widthPer12][heightPer12];
		   pix6= new int[widthPer6 ][heightPer6 ];
//		   subImageArray= new SubImageClass[widthPer24][heightPer24];
           subImageArray= new SubImageClass[widthPer12+1][heightPer12+1]; // 2007/1/6, t.yamanoue
//		   subImageArray= new SubImageClass[widthPer24+1][heightPer24+1]; 
		   subImageArray2=new SubImageClass[widthPer12+1][heightPer12+1]; // 2007/2/22, t.yamanoue
           subImageBuffer= new Object[widthPer12+1][heightPer12+1]; // 2007/2/22, t.yamanoue
           subPixelBuffer = new Object[widthPer12+1][heightPer12+1]; //2007/2/22, t.yamanoue
           subAreaStatus= new int[widthPer12+1][heightPer12+1];

           this.defaultBackground=this.destinationPanel.getBackground(); // 2007/1/7, t.yamanoue
    	    if(pixByte==null)
   		    pixByte=new byte[eWidth*eWidth*4];

		}
   }


	public void setSourceImage(Image x)
	{
		this.sourceImage=x;
//    	System.out.println("sourcImage updated @ setSourceImage(Image)");
//    	if(sourceImage==null){System.out.println("sourceImage<-null");}
//    	else {System.out.println("sourceImage!=null");}
	}
	
	public Image getSourceImage(){
		return sourceImage;
	}

    public void setDestinationImage(Image x){
    	this.destinationImage=x;
    }

    public void setSourceImage(Image x, int w, int h){
    	this.sourceImage=x;
    	this.width=w;
    	this.height=h;
//    	System.out.println("sourcImage updated @ setSourceImage(Image,int,int)");
//    	if(sourceImage==null){System.out.println("sourceImage<-null");}
//    	else {System.out.println("sourceImage!=null");}
    	System.out.println("set source panel: w="+width+" h="+height);
    }

	public void setDestinationImage(Image x, int w, int h){
		this.destinationImage=x;
		this.width=w;
		this.height=h;
    	System.out.println("set source panel: w="+width+" h="+height);
	}

	public void drawRYRect(Graphics g, int x,int y, int w, int h)
	{
			Color cc=g.getColor();
			g.setColor(Color.red);
//			  g.fillOval(x,y,10,10);
			g.drawRect(x,y,w,h);
			g.setColor(Color.yellow);
			g.drawRect(x+1,y+1,w-2,h-2);
			g.setColor(cc);
	}

	public boolean isUpdated()
	{
//		return true;
		
		if(width<=0) return false;
		if(height<=0) return false;
		return true;
		
	}
	public AMessage process6()
	{
		AMessage m=new AMessage();
		if(this.sourceImage==null) return null;
		if(width<=0 || height<=0) return null;
		if(width<=0 || height<=0) return null;
		int x=iX6*6;
		int y=iY6*6;
		int pixels[]=new int[1*1];

		this.sendingX=x;
		this.sendingY=y;
		this.sendingW=width;
		this.sendingH=6;
//		  this.canvas.gui.repaint();
        
		String header="name(\""+name+"\").("+
			   x+","+y+")."+
				"width("+6+")."+
			   "height("+6+")."+
			   "tile.";

		int maxPoints=50;
		int i=0;
		String colors="";

		while(true){
		  PixelGrabber pg=new PixelGrabber(sourceImage,x,y,1,1,pixels,0,1);
		  try{
			  pg.grabPixels();
		  }
		  catch(InterruptedException e)
		  {
			System.out.println(e);
		  }
		  iX6++;
		  colors=colors+"color("+pixels[0]+")";
		  if(iX6>widthPer6-1)  {iX6=0; iY6++; break;}
		  if(iY6>heightPer6-1) {
			iY12=0; iX12=0;
			iX6=0;  iY6=0;
			i24or12=4; break;
		  }
		  i++;
		  if(i>=maxPoints) break;
		  x=iX6*6;
		  y=iY6*6;
		  colors=colors+"-";
		}
		m.setHead( header+colors+"\n");
		return m;
	}

	public synchronized void paint(Graphics g, int x, int y)
	{
		  g.drawImage(destinationImage,x,y,width,height,this);
	}
	
	public synchronized boolean imageUpdate(Image img, int infoflags,int x, int y,
	int width, int height) //synchronized
	{
		 flags=infoflags;
         this.destinationPanel.setSize(width,height);
		 return true;
		 
	}
	public void createNewSourceImage()
	{
		if(this.frame==null) return;
        if((this.frame).isSendingAV()){
//			this.monitorStream.setEnabled(false);
            if(this.monitorStream!=null){
			    this.monitorStream.transferData(monitorStream);
     		    sourceImage=this.monitorStream.getCurrentImage();
//        	    System.out.println("sourcImage updated @ createNewSourceImage()");
//        	    if(sourceImage==null){System.out.println("sourceImage<-null");}
//        	    else {System.out.println("sourceImage!=null");}
//     		this.monitorStream.setEnabled(true);
            }
            else{
            	this.sourceImage=null;
//            	System.out.println("this.monitorStream==null @ createNewSourceImage()");
            }
		}
		else
		{
		    this.sourceImage=null;
//		    System.out.println("souceImage<-null @ createNewSourceImage");
		}
	}

	public void createNewDestinationImage()
	{
		if(destinationPanel!=null){
            if(this.destinationImage==null){
            	this.destinationImage=this.destinationPanel.createImage(this.width,this.height);
            }
       		Graphics gx=destinationPanel.getGraphics();
//            if(!this.frame.isReceiving()) {
            if((this.frame).isSendingAV()){
            	if(this.sourceImage!=null)
				gx.drawImage(sourceImage,0,0,width,height,destinationPanel);
            	return;
            }            
    		else{
    			
    		}
		}
	}
	public void createNewDestinationImage(int w, int h)
	{
		this.width=w;
		this.height=h;
		if(destinationPanel!=null){
			if(this.destinationImage==null){
				this.destinationImage=this.destinationPanel.createImage(w,h);
			}
			Graphics gx=destinationPanel.getGraphics();
            if(this.sourceImage==null) {
//            	System.out.println("sourceImage==null");
            	return;
            } 
            else
            {
            	if((this.frame).isSendingAV())
    			gx.drawImage(sourceImage,0,0,width,height,destinationPanel);
            }
		}
	}

	boolean restWidth; // 2007/1/6, t.yamanoue
	boolean restHeight; // 2007/1/6
	int[][] subAreaStatus=null; // 2009 5/26, t.yamanoue
	public AMessage processImage()
	{
		/*
		try{
			Thread.sleep(1);
		}
		catch(InterruptedException e){}
		*/
		if(this.sourceImage==null)return null;
       	if(!(this.frame).isSendingAV()) return null;
//		int d=24;
//        int d=12;
//		int x=iX24*d;
        int x=iX12*eWidth;
//		int y=iY24*d;
        int y=iY12*eWidth;

		boolean isNotNeedToSend=false;
		String rtn;

    	this.sendingX=x;
    	this.sendingY=y;
    	
    	//2007/1/6,  送信画像の範囲修正, t.yamanoue
        if(restWidth&&!restHeight){
        	this.sendingW=this.width-x;
        	this.sendingH=eWidth;
        }
        else
		if(!restWidth&&restHeight){
        	this.sendingW=eWidth;
        	this.sendingH=this.height-y;			
		}
		else
		if(restWidth&&restHeight){
			this.sendingW=this.width-x;
			this.sendingH=this.height-y;
		}
		else
		{
		    this.sendingX=x;
		    this.sendingY=y;
		    this.sendingW=eWidth;
		    this.sendingH=eWidth;
		}
//		if((iX24 % 8)==0) this.imagePanel.repaint();
        
//		int pixels[]=new int[d*d];
//		this.monitorStream.setEnabled(false);
        int pix[]=(int[])(subPixelBuffer[iX12][iY12]);
        if(pix==null){
        	pix=new int[eWidth*eWidth];
         }
		PixelGrabber pg=new PixelGrabber(sourceImage,x,y,eWidth,eWidth,pix,0,eWidth);
//		PixelGrabber pg=new PixelGrabber(sourceImage,x,y,sendingW,sendingH,pixels,0,d);
		try{
			pg.grabPixels();
		}
		catch(InterruptedException e)
		{
			System.out.println(e);
		}
       	subPixelBuffer[iX12][iY12]=pix;

        /*
         * The following if statement is used for the motion
         * detecting. This reduces the traffic.
         */
		SubImageClass newSub=subImageArray2[iX12][iY12];
		if(newSub==null){
			newSub=new SubImageClass(x,y,eWidth,eWidth,pix);
			subImageArray2[iX12][iY12]=newSub;
		}
		else{
    		newSub.copyPixels(eWidth,eWidth,pix);
		}
//		SubImageClass newSub=new SubImageClass(x,y,eWidth,eWidth,pixels);
//		if(newSub.isAlmostTheSame(this.subImageArray[iX24][iY24])) {
		SubImageClass oldSub=this.subImageArray[iX12][iY12];
		if(newSub.isAlmostTheSame(oldSub)) {
//						System.out.println("isAlmostTheSame");
			isNotNeedToSend=true;
			if(this.subAreaStatus[iX12][iY12]>0){
				this.subAreaStatus[iX12][iY12]--;
			}
		}
		else{
			if(oldSub==null){
				oldSub=new SubImageClass(x,y,eWidth,eWidth,pix);
				oldSub.setStepAndError(this.subImageErrorStep, this.subImageError);
			}
			else{
				oldSub.copyThis(newSub);
			}
			this.subImageArray[iX12][iY12]=oldSub;
			this.subAreaStatus[iX12][iY12]=4;
		}

		AMessage m=null;
		int sas=0;
		if(this.coding.equals("slowFine")){
		      sas=this.subAreaStatus[iX12][iY12];
		      if(sas>0){
   	              m= this.makeImageMessageWithSkippingFastMovie(name, sas, iX12, iY12, x, y, eWidth, eWidth, pix);		
		      }
		}
		/* */
		else{
		if(!isNotNeedToSend){
		   if(this.coding.equals("raw")){
	   	       m= this.makeImageMessageWithNoCompression(name, iX12, iY12, x, y, eWidth, eWidth, pix);
		   }
		   if(this.coding.equals("jpeg")){
	           m= this.makeImageMessageWithJpegCompression(name, iX12, iY12, x, y, eWidth, eWidth, pix);   
		   }
		}
		}
		/* */
        // 2007/1/6, t.yamanoue, 送信画像の範囲修正
    	if(x>=(this.width-eWidth)){
    		iX12=0; iY12++;
    		this.restWidth=false;
    	}
    	else{
    		iX12++;
    		this.restWidth=true;
    	}
    	if(y>=(this.height-eWidth)){
    		iX12=0; iY12=0;
    		this.i24or12=5;
    		this.restHeight=false;
    	}
    	else{
    		this.restHeight=true;
    	}
 		
    	if(this.coding.equals("slowFine")){
        	if(sas==0) return null;
    	}
    	else{
    	   	if(isNotNeedToSend) return null;    		
    	}
        	    
		return m;

    }
	BufferedImage bufferedImage=null;
	BufferedImage bufferedImageBuffer=null;
	ByteArrayOutputStream baOut=null;
	
	/*
	 String picName ... picture name of all of the picture area.
	 int ix ... sub-area id for x axis
	 int iy ... sub-area id for y axis
	 int x ...  x value of the left-upper corner of the sub-area.
	 int y ...  y value of the left-upper corner of the sub-area
     int w ...  width of the sub-area
     int h ...  height of the sub-area
     int[] pix ... pixel of the sub-area	 
	 */
	AMessage makeImageMessageWithNoCompression(String picName, int ix, int iy, int x, int y, int w, int h, int[] pix){
	    // for raw binary
	    pixByte=(byte[])(subImageBuffer[ix][iy]);
	    if(pixByte==null){
	    	pixByte=new byte[w*h*4];
	    	subImageBuffer[ix][iy]=pixByte;
	    }
	            
	    for(int i=0;i<eWidth*eWidth;i++){
	    	pixByte[i*4]=(byte)((pix[i] >>24)& 0x000000ff);
	    	pixByte[i*4+1]=(byte)((pix[i] >>16)& 0x000000ff);
	    	pixByte[i*4+2]=(byte)((pix[i] >>8)& 0x000000ff);
	    	pixByte[i*4+3]=(byte)(pix[i] & 0x000000ff);
	    }
		String rtn="name(\""+picName+"\").("+
		   x+","+y+").width("+w+").height("+h+").binary\n";
		AMessage m=new AMessage();
		m.setHead(rtn);
		m.setData(pixByte,pixByte.length);
		return m;
	}
	
	ByteArrayOutputStream baos=null;
	AMessage makeImageMessageWithJpegCompression(String picName, int ix, int iy, int x, int y, int w, int h, int[] pix){
	    // jpeg encode
    	/* */
		byte[] cmpBuf=null;		
		String cmpType="jpg";//変更することにより圧縮する方を決定する。jpegやtiff
							 //後々ＧＵＩ化
	    
//		ここでint配列をBufferedImageに変換。・・・・(1)
		MemoryImageSource mis=new MemoryImageSource(
				      w,h, defaultColorModel, pix,0,w);
		Image img= Toolkit.getDefaultToolkit().createImage(mis);
		BufferedImage bimg= new BufferedImage(img.getWidth(null),img.getHeight(null),
				      BufferedImage.TYPE_3BYTE_BGR);  // BufferedImageオブジェクトを作成
        Graphics g = bimg.createGraphics();  // Graphicsの取得
        g.drawImage(img,0,0,null);
        g.dispose();
        
		ImageWriter iw=null;
		try{
			//名前付きの形式を符号化できるような、
			//現在登録されているすべての ImageWriter を保持する Iterator を返します。
			iw=(ImageWriter)ImageIO.getImageWritersByFormatName(cmpType).next();
		}catch(Exception e){iw=null;}
		if(iw==null){
			System.out.println("ImageWriterがnullになりました");
		}else{
			JPEGImageWriteParam p=new JPEGImageWriteParam(Locale.getDefault());
			p.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			p.setCompressionQuality(cmpQuality);
			ByteArrayOutputStream os=new ByteArrayOutputStream(100000);
//			if(baos==null){
//				baos=new ByteArrayOutputStream(100000);
//				 System.out.println("ByteArrayOutputStreamが取得できません");
//			}
//			if(baos==null){
			if(os==null){
				System.out.println("ByteArrayOutputStreamが取得できません");				
			}
			else{
				ImageOutputStream ios=null;
				try{
//					ios=ImageIO.createImageOutputStream(baos);
					ios=ImageIO.createImageOutputStream(os);
				}catch(Exception e){ios=null;}
				if(ios==null){
					System.out.println("ImageIOが取得できませんでした");
				}else{
					iw.setOutput(ios);
					try{
						iw.write(null,new IIOImage(bimg,null,null),p);
					}catch(Exception e){
						try{
//							baos.close();
							os.close();
							ios.close();
						}catch(Exception e2){}
						System.out.println("bimgの圧縮中にエラーが起きました");
					}
//					cmpBuf=baos.toByteArray();
					cmpBuf=os.toByteArray();
					try{
//						baos.close();
						os.close();
						ios.close();
					}catch(Exception e){}
				}
			}
		}
		if(cmpBuf==null){
			System.out.println("圧縮ルーチンにエラーが発生しています");
		}
      /*  */
	    
	    // for jpeg
		/* */
		String rtn= "name(\""+picName+"\").("+
		   x+","+y+").width("+w+").height("+h+").jpeg\n";
        /*   */
//	    System.out.println(rtn);
		AMessage m=new AMessage();       
		m.setHead(rtn);
   		m.setData(cmpBuf,cmpBuf.length); // for jpeg
		return m;
	}
	
    byte[] sampledSubImageArea;
	AMessage makeImageMessageWithSkippingFastMovie(String picName, int areaStatus,int ix, int iy, int x, int y, int w, int h, int[] pix){
	    // for raw binary;
		int step=2;
		int aStep=step*step;
		int asr=4-areaStatus;
		int sx=asr%step;
		int sy=asr/step;
//		if(asr!=0)
//		System.out.println("areaStatus="+areaStatus+" ix="+ix+" iy="+iy+" x="+x+" y="+y+" w="+w+" h="+h+" aStep="+aStep+" sx="+sx+" sy="+sy+" asr="+asr);
	    if(sampledSubImageArea==null){
	    	sampledSubImageArea=new byte[4*(h+1)*(w+1)/aStep];
	    }
	    int i=0,j=0,ixy=0,pxy=0;
	    try{
	      ixy=0; 
	      for( j=0;j<h;j+=step)
	    	for( i=0;i<w;i+=step)
	        {
	    		pxy=(i+sx)+(j+sy)*w;
	    	    sampledSubImageArea[ixy*4]=(byte)((pix[pxy] >>24)& 0x000000ff);
	    	    sampledSubImageArea[ixy*4+1]=(byte)((pix[pxy] >>16)& 0x000000ff);
	    	    sampledSubImageArea[ixy*4+2]=(byte)((pix[pxy] >>8)& 0x000000ff);
	    	    sampledSubImageArea[ixy*4+3]=(byte)(pix[pxy] & 0x000000ff);
	    	    ixy++;
	        }
	    }
	    catch(Exception e){
	    	System.out.println("sampledSubImageArea.length="+sampledSubImageArea.length+" pix.length="+pix.length);
	    	System.out.println("i="+i+" j="+j+" pxy="+pxy+" ixy="+ixy);
	    }
		String rtn="name(\""+picName+"\").("+
		   x+","+y+").width("+w+").height("+h+").sImg(\""+step+"-"+asr+"\")\n";
//		if(asr!=0)
//			System.out.println(rtn);
		AMessage m=new AMessage();
		m.setHead(rtn);
		m.setData(sampledSubImageArea,sampledSubImageArea.length);
		return m;
	}
	
	public AMessage process12()
	{
		AMessage m=new AMessage();
		if(this.sourceImage==null)return null;
		if(width<=0 || height<=0) return null;
		if(width<=0 || height<=0) return null;
		int x=iX12*12;
		int y=iY12*12;
		int pixels[]=new int[1*1];

		 this.sendingX=x;
		 this.sendingY=y;
		 this.sendingW=width;
		 this.sendingH=12;
//		 if((iY12 % 8)==0)this.imagePanel.repaint();
         
		String header="name(\""+name+"\").("+
			   x+","+y+").width(12).height(12).tile.";

		int maxPoints=50;
		int i=0;
		String colors="";

		while(true){
		  PixelGrabber pg=new PixelGrabber(sourceImage,x,y,1,1,pixels,0,1);
		  try{
			  pg.grabPixels();
		  }
		  catch(InterruptedException e)
		  {
			System.out.println(e);
		  }
		  iX12++;
		  colors=colors+"color("+pixels[0]+")";
		  if(iX12>widthPer12-1)  {iX12=0; iY12++; break;}
		  if(iY12>heightPer12-1) {
			iY12=0; iX12=0; i24or12=4; break;
		  }
		  i++;
		  if(i>=maxPoints) break;
		  x=iX12*12;
		  y=iY12*12;
		  colors=colors+"-";
		}
		try{
			Thread.sleep(1);
		}
		catch(InterruptedException e){
		}
		m.setHead(header+colors+"\n");
        return m;
	}
	int pixels[]=new int[200];
	byte pixByte[];
	public AMessage process24()
	{
		AMessage m=new AMessage();
		if(this.sourceImage==null) return null;
		if(width<=0 || height<=0) return null;
		int x=iX24*24;
		int y=iY24*24;
//		  int w=8;
		int w=1;
//		  int h=8;
		int h=1;
//		int pixels[]=new int[w*h];
		/*
		PixelGrabber pg=new PixelGrabber(image,x,y,1,1,pixels,0,1);
		this.grabImage();
		*/
		this.sendingX=x;
		this.sendingY=y;
		this.sendingW=width;
		this.sendingH=24;
//		if((iY24 % 4) ==0) this.imagePanel.repaint();
        
		String header="name(\""+name+"\").("+
			   x+","+y+")."+
				"width("+24+")."+
			   "height("+24+")."+
			   "tile.";

		int maxPoints=50;
		int i=0;
		String colors="";

		while(true){
            
		  PixelGrabber pg=new PixelGrabber(sourceImage,x,y,w,h,pixels,0,w);
		  try{
			  pg.grabPixels();
		  }
		  catch(InterruptedException e)
		  {
			System.out.println(e);
		  }
          
//		  pix24[iX24][iY24]=pixels[0];
		  iX24++;
		  colors=colors+"color("+pixels[0]+")";
//			colors=colors+"color("+pixels[width*y+x]+")";

		  if(iX24>widthPer24-1)  {iX24=0; iY24++; break;}
		  if(iY24>heightPer24-1) {iX24=0; iY24=0; i24or12=4; break;}
		  i++;
		  if(i>=maxPoints) break;
		  x=iX24*24;
		  y=iY24*24;
		  colors=colors+"-";
		}
		try{
			Thread.sleep(1);
		}
		catch(InterruptedException e){}
		m.setHead( header+colors+"\n");
		return m;
	}
	public void setName(String x)
	{
		name=x;
	}
	
	public AMessage sendPaintOffScreenCommand(){
		AMessage m=new AMessage();
		this.i24or12=6;
		m.setHead( "name(\""+name+"\").(0,0).width(0).height(0).paint\n");
		return m;
	}
	
	public void paintOffScreen(){
		if(this.destinationPanel==null) return;
		Graphics gx=this.destinationPanel.getGraphics();
		gx.drawImage(this.offImg,0,0,this.destinationPanel);
	}
	
	public AMessage process0()
	{
		AMessage rtn=new AMessage();
		int currentX=0;
		int currentY=0;
		this.createNewSourceImage();
		this.createNewDestinationImage();
		if(this.sourceImage==null) return null;
     
		i24or12=4;
		iX12=0; iY12=0;
//		iX24=0; iY24=0;
		rtn.setHead( "name(\""+name+"\").("+currentX+","+currentY+")."+
			   "width("+width+")."+
			   "height("+height+")."+
			   "create."+
			   "color("+(Color.black).getRGB()+")\n");
		return rtn;
	}
	public AMessage nextImage()
	{
//		System.out.println("start nextImage.");
//		if(this.sourceImage==null) return null;
		if(!(this.frame.isSendingAV())) return null;
//		System.out.println("sourceImage is not null.");
		if(!isUpdated()) return null;
//		System.out.println("isUpdated is true.");
		if(i24or12<0)  return null;
		if(i24or12==0) return process0();
		if(i24or12==1) return process24();
//		  if(i24or12==1) return process36();
		if(i24or12==2) return process12();
		if(i24or12==3) return process6();
		try{
		if(i24or12==4) return processImage();
		}
		catch(Exception e){
			System.out.println(e.toString());
			System.out.println("iX12="+this.iX12+",iY12="+this.iY12);
			
		}
        if(i24or12==5) return this.sendPaintOffScreenCommand();
        if(i24or12==6)
		{
			/*
			 *   Sending Frame Rate
			 */
			try{
   			Thread.sleep(3); // waiting time.
	    	}
		    catch(InterruptedException e){
			   System.out.println(""+e.getStackTrace());
		    }
		    i24or12=0;
		}
		return null;
	}
	public void fillRect(int x, int y, int w, int h, Color c) //synchrnized
	{
		Graphics gx=null;
		isCreatingImage=false;
		if(w>width) width=w;
		if(h>height) {
			isCreatingImage=true;
			height=h;
			if(this.destinationImage==null){
			   this.createNewDestinationImage(width,height);
			   System.out.println("destinationImage was null");
			}
			gx=destinationPanel.getGraphics();
		}
		if(gx==null){
			gx=destinationPanel.getGraphics();
		}
		gx.setColor(c);
//		gx.drawLine(0,50,100,50);
		gx.fillRect(x,y,w,h);
//		this.destinationPanel.repaint();
	}
	public void clearImage(){
		this.fillRect(0,0,this.width,this.height,this.defaultBackground);
	}
	char subimg[]=new char[2000];
	public String image2String(int x, int y, int w, int h,int pixels[])
	{
	   int pixSize=w*h;
	   int strSize=pixSize*6;

//	   char subimg[]=new char[strSize];
	   String rtn;

		int psub=0;
		int indexOfthisSubimg=0;
        
		for(int i=0;i<pixSize;i++){
			int px=pixels[i];
			for(int j=0;j<6;j++){
				  char c=i2a[px & 0x0000003F];
				  subimg[psub]=c; psub++;
				  px=px>>6;
			}
		}
//		rtn=new String(subimg);
		rtn=new String(subimg,0,strSize);
		return rtn;

	}
	  public int string2Int(String x)
	{
		int index;
		int rtn=0;
		int mlt=64;
		int xmlt=1;
		for(int i=0;i<6;i++){
			rtn=rtn | a2i[(int)(x.charAt(i))]*xmlt;
			xmlt=xmlt*mlt;
		}
		return rtn;
	}
    public void string2subImg(int x, int y, int w, int h, String subimg)
	{
		int pixSize=w*h;
		int pixels[]=new int[pixSize];
		int ps=0;
//		  int imglength=subimg.length();
		String xs="";
		isCreatingImage=false;
		for(int i=0;i<pixSize;i++){
			xs=subimg.substring(ps,ps+6);
			pixels[i]=string2Int(xs);
			ps+=6;
		}
		setSubImage(x,y,w,h,pixels,destinationPanel);
	}
    int xpixels[]=new int[1200];
    public void setBinaryImage(int x, int y, int w, int h, byte[] img){
    	for(int i=0;i<(img.length)/4;i++){
    		xpixels[i]=((0x000000ff & img[i*4])<<24) | ((0x000000ff & img[i*4+1])<<16) | ((0x000000ff & img[i*4+2])<<8) | (0x000000ff & img[i*4+3]) ;
    	}
    	setSubImage(x,y,w,h,xpixels,destinationPanel);
    }
    Hashtable memoryImages=new Hashtable();
    XYWH xywh=new XYWH(0,0,0,0);
    ColorModel defaultColorModel = ColorModel.getRGBdefault();
    //	MemoryImageSource mis=new MemoryImageSource(w,h,pix,0,w);	
    Hashtable sampledImages;    
    public void setSampledImage(String code, int x, int y, int w, int h, byte[] img){
    	if(sampledImages==null){
    		sampledImages=new Hashtable();
    	}
    	StringTokenizer st=new StringTokenizer(code,"-");
    	int step=1, position=0;
    	try{
    		String sstep=st.nextToken();
    		String spos=st.nextToken();
    		step=(new Integer(sstep)).intValue();
    		position=(new Integer(spos)).intValue();
    	}
    	catch(Exception e){
    		System.out.println("code="+code);
    		System.out.println(e.toString());
    		Thread.dumpStack();
    	}
    	String keyXY=""+x+"-"+y;
    	if(position==0){
    		int[] pixsXY=(int[])(sampledImages.get(keyXY));
    		if(pixsXY==null){
        		pixsXY=new int[w*h];    			
    		}
    		int p=0;
    		for(int j=0;j<h;j+=step)
    			for(int i=0;i<w;i+=step)
    		{ 
    			int color	=((0x000000ff & img[p*4])<<24) | ((0x000000ff & img[p*4+1])<<16) | ((0x000000ff & img[p*4+2])<<8) | (0x000000ff & img[p*4+3]) ;
    			pixsXY[i+j*w]=color;
    			pixsXY[i+1+j*w]=color;
    			pixsXY[i+(j+1)*w]=color;
    			pixsXY[i+1+(j+1)*w]=color;
            	p++;
    		}
    	    setSubImage(x,y,w,h,pixsXY,destinationPanel);    	
    	    sampledImages.put(keyXY, pixsXY);
    	    return;
    	}
    	
		int[] pixsXY=(int[])(sampledImages.get(keyXY));
		if(pixsXY==null) return;            

    	if(position==1){
    		int p=0;
    		for(int j=0;j<h;j+=step)
    			for(int i=0;i<w;i+=step)
    		{ 
    			int color	=((0x000000ff & img[p*4])<<24) | ((0x000000ff & img[p*4+1])<<16) | ((0x000000ff & img[p*4+2])<<8) | (0x000000ff & img[p*4+3]) ;
    			pixsXY[i+1+j*w]=color;
    			pixsXY[i+1+(j+1)*w]=color;
            	p++;
    		}
    	    setSubImage(x,y,w,h,pixsXY,destinationPanel);    	
    	    return;
    	}
    	if(position==2){
            
    		int p=0;
    		for(int j=0;j<h;j+=step)
    			for(int i=0;i<w;i+=step)
    		{ 
    			int color	=((0x000000ff & img[p*4])<<24) | ((0x000000ff & img[p*4+1])<<16) | ((0x000000ff & img[p*4+2])<<8) | (0x000000ff & img[p*4+3]) ;
    			pixsXY[i+(j+1)*w]=color;
            	p++;
    		}
    	    setSubImage(x,y,w,h,pixsXY,destinationPanel);    	
    	    return;
    	}
    	if(position==3){
            
    		int p=0;
    		for(int j=0;j<h;j+=step)
    			for(int i=0;i<w;i+=step)
    		{ 
    			int color	=((0x000000ff & img[p*4])<<24) | ((0x000000ff & img[p*4+1])<<16) | ((0x000000ff & img[p*4+2])<<8) | (0x000000ff & img[p*4+3]) ;
    			pixsXY[i+1+(j+1)*w]=color;
            	p++;
    		}
    	    setSubImage(x,y,w,h,pixsXY,destinationPanel);    	
    	    return;
    	}
    }

    public void setJpegImage(int x, int y, int w, int h, byte[] img)
    {
    	int xlen=xpixels.length;
//    	int nlen=img.length/4;
//    	if(nlen>xlen){
    	if(xlen<w*h){
    		xpixels=new int[w*h];
    	}
    	/*
    	for(int i=0;i<nlen;i++){
    	xpixels[i]=((0x000000ff & img[i*4])<<24) | ((0x000000ff & img[i*4+1])<<16) | ((0x000000ff & img[i*4+2])<<8) | (0x000000ff & img[i*4+3]) ;
    	*/
    	
    	try {
			BufferedImage buf = ImageIO.read(new ByteArrayInputStream(img));
			PixelGrabber grabber = new PixelGrabber(buf, 0, 0, -1, -1, true);
			if(grabber.grabPixels())
			{
				xpixels = (int[])(grabber.getPixels());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	setSubImage(x,y,w,h,xpixels,destinationPanel);
//    	20090207
    	//プログラム追加→入江健悟
    	//分割数を指定して描画の最後に時間を表示するプログラム
//		Bunkatsu_Flug++;
//		if(Bunkatsu_Flug==Bunkatsu){
//			System.out.println("E N D"+System.currentTimeMillis());//描画終了時間
//			Bunkatsu_Flug = 0;
//		}
    }
    
    public void setSubImage(int x, int y, int w, int h, int[] pix, Component panel){
		xywh.setXYWH(x,y,w,h);
		MemoryImageSource misx=(MemoryImageSource)(memoryImages.get(xywh));
		if(misx==null){
		    MemoryImageSource mis=new MemoryImageSource(w,h,pix,0,w);
		    memoryImages.put(xywh,mis);
		    misx=mis;
		}
		else{
			misx.newPixels(pix,defaultColorModel,0,w);
		}
		Image ximg=panel.createImage(misx);
		// draw image at off screen (double buffering).
        this.offGr.drawImage(ximg,x,y,w,h,panel);
	}
	public class XYWH{
		int x,y,w,h;
		public XYWH(int x, int y, int w, int h){
			this.x=x; this.y=y;this.w=w; this.h=h;
		}
		public void setXYWH(int x, int y, int w, int h){
			this.x=x; this.y=y; this.w=w; this.h=h;
		}
	}
/*	
	public String int2String(int x)
	{
		String rtn;
		rtn="";
		for(int i=0;i<6;i++){
			rtn=rtn+i2a[x & 0x0000003F];
			x=x>>6;
		}
		return rtn;

	}
	*/
	char int2StringSubimg[];
	public String int2String(int x)
	{
//		int strSize=6;

//		char subimg[]=new char[strSize];
	
		String rtn;

		 int psub=0;
		 int indexOfthisSubimg=0;
        
		 int px=x;
		 for(int j=0;j<6;j++){
			   char c=i2a[px & 0x0000003F];
//			   subimg[psub]=c; psub++;
			   this.int2StringSubimg[psub]=c; psub++;
			   px=px>>6;
		 }
//		 rtn=new String(subimg);
		 rtn=new String(int2StringSubimg);
		 return rtn;
		 /*
		String rtn;
		rtn="";
		for(int i=0;i<6;i++){
			rtn=rtn+i2a[x & 0x0000003F];
			x=x>>6;
		}
		return rtn;
         */
	}
	public boolean isAllTheSameColor(int x, int y, int w, int h,int pixels[])
	{
		System.out.println("isAllTheSameColor");
		int noizeCount=0;
		int pixSize=w*h;
        
		int firstPix=pixels[0];
		for(int i=0;i<pixSize;i++){
			if(firstPix!=pixels[i]) {
				noizeCount++;
				if(noizeCount>10) return false;
			}
		}
		return true;

	}

	/*
	 * 
	 * @author yamachan
	 *
	 * Inner Class for a SubImage of the screen.
	 * This class is used for reducing the network traffic,
	 * by detecting the motion on the screen.
	 * Objects of this class is stored in the subImageArray.
	 * 
	 */
	class SubImageClass{
		int x,y,w,h;
		int[] pix;
		int err;
		int step=4;
		
		private void setError(){
//			this.err=w*h*4*3;
            this.err=5*4*4*w*h/(step*step);
		}
		
		public void setError(int r){
			this.err=w*h*step*r/(step*step);
		}
		public void setStepAndError(int s, int r){
			this.step=s;
			this.err=w*h*step*r/(step*step);
		}
		public SubImageClass getCopy(){
			SubImageClass rtn=new SubImageClass(x,y,w,h,pix);
			return rtn;
		}
		public SubImageClass(int x, int y, int w, int h, int[] p){
			this.x=x; this.y=y; this.w=w; this.h=h;
			this.pix=new int[p.length];
			for(int i=0;i<p.length;i++){
				pix[i]=p[i];
			}
//			this.pix=p;
			setError();
		}
		public void setPosition(int x, int y){
			this.x=x; this.y=h;
		}
		public void setSize(int w, int h){
			this.w=w; this.h=h;
			this.setError();
		}
		public void setPixels(int w, int h, int[] p){
			this.w=w; this.h=h;
			this.setError();
			this.pix=p;
		}
		public void copyPixels(int w, int h, int[] p){
			this.w=w; this.h=h;
			this.pix=new int[p.length];
			for(int i=0;i<p.length;i++){
				pix[i]=p[i];
			}
		}
		public void copyThis(SubImageClass xx){
			int[] xpix=xx.pix;
			this.x=xx.x; this.y=xx.y; this.w=xx.w; this.h=xx.h;
			for(int i=0;i<pix.length;i++){
				pix[i]=xpix[i];
			}
//			this.pix=p;
			setError();
			
		}
		public int diff1Pix(int a, int b){
			int a1=a>>24   & 0x000000ff;
			int a2=(a>>16) & 0x000000ff;
			int a3=(a>>8)  & 0x000000ff;
			int a4=a       & 0x000000ff;
			int b1=b>>24   & 0x000000ff;
			int b2=(b>>16) & 0x000000ff;
			int b3=(b>>8)  & 0x000000ff;
			int b4=b       & 0x000000ff;
			return Math.abs(a1-b1)+Math.abs(a2-b2)+Math.abs(a3-b3)+Math.abs(a4-b4);
		}
		public int diffPixels(SubImageClass x){
			if(x==null) return -1;
			if(x.w!=this.w) return -1;
			if(x.h!=this.h) return -1;
			int l=w*h;
			int s=0;
			if(x.pix==null) return -1;
            for(int j=0;j<h;j+=step)
            for(int i=0;i<w;i+=step){
            	s=s+diff1Pix(pix[i+j*w],x.pix[i+j*w]);
            }
            /*
			for(int i=0;i<l;i++){
				s=s+diff1Pix(pix[i],x.pix[i]);
			}

			
            int p1=0; int p2=w-1; int p3=l/2; int p4=l-w; int p5=l-1;
            s=diff1Pix(pix[p1],x.pix[p1]);
            s=s+diff1Pix(pix[p2],x.pix[p2]);
            s=s+diff1Pix(pix[p3],x.pix[p3]);
            s=s+diff1Pix(pix[p4],x.pix[p4]);
            s=s+diff1Pix(pix[p5],x.pix[p5]);
            */
			return s;
		}
		public boolean isAlmostTheSame(SubImageClass s){
//			System.out.println("is almost the same x:"+x+" y:"+y);
			int e=diffPixels(s);
			if(e<0) return false;
			if(e<err) return true;
			else return false;
		}
	}
	SkipControl skipControl;
	public void setSkipParameters(Timer t,Parameters p){
		this.skipControl=new SkipControl(t,p);
	}
	public boolean largeDelay(int s){
		if(skipControl==null) return false;
		return skipControl.largeDelay(s);
	}	
}

