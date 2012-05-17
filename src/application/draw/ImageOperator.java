package application.draw;

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
import java.net.URL;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import util.Parameters;

import nodesystem.AMessage;
import nodesystem.NodeSettings;

public class ImageOperator extends java.lang.Object
                           implements ImageObserver
{
    public int pix36[][];

    public int iY36;
    public int iX36;
    public int heightPer36;
    public int widthPer36;
    public boolean isSent;
    public int sendingH;
    public int sendingW;
    public int sendingY;
    public int sendingX;
    public boolean isSending;

	private SubImageClass subImageArray[][];
	private SubImageClass subImageArray2[][];
	private Object subPixelBuffer[][];
	private Object subImageBuffer[][];
//	private int eWidth=24;
	private int eWidth=128;
    public PixelGrabber pixelGrabber;
    public boolean isCreatingImage;

    public int iY12;
    public int iX12;
    public int iY24;
    public int iX24;
    public int heightPer12;
    public int widthPer12;
    public int heightPer24;
    public int widthPer24;
    public int height;
    public int width;
    public String url;
    public String name;
    public Image image2;
    public Image image;

    private int iXimg;
    private int iYimg;
    private int heightPerImg;
    private int widthPerImg;
	public static float cmpQuality=(float)0.5;//圧縮率(0<x<=1.0)
    
    private NodeSettings settings;
    
    public void grabImage()
    {
        if(width<0) return;
        if(height<0) return;
        if(pixelGrabber!=null) return;
        
        if(!(this.canvas.gui.isSending())) return;
                  pixels=new int[width*height];
                  pixelGrabber=new PixelGrabber(image,0,0,width,height,pixels,0,width);
                  try{
                    pixelGrabber.grabPixels();
                  }
                  catch(InterruptedException e)
                  {
                    System.out.println(e);
                  }
//                }
   }

    public void setMyImage(MyImage myImg)
    {
        this.myImage=myImg;
    }

    public String process36()
    {
        if(width<=0 || height<=0) return null;
        int x=iX36*36;
        int y=iY36*36;
        int pixels[]=new int[1*1];

        this.sendingX=x;
        this.sendingY=y;
        this.sendingW=width;
        this.sendingH=36;
        if((iY36 % 4) ==0) this.canvas.gui.repaint();
        
        String header="name(\""+name+"\").("+
                "width("+36+")."+
               "height("+36+")."+
               "tile.";

        int maxPoints=50;
        int i=0;
        String colors="";

        while(true){
          PixelGrabber pg=new PixelGrabber(image,x,y,1,1,pixels,0,1);
          try{
              pg.grabPixels();
          }
          catch(InterruptedException e)
          {
            System.out.println(e);
          }
//        pix24[iX24][iY24]=pixels[0];
          iX36++;
          colors=colors+"color("+pixels[0]+")";
          if(iX36>widthPer36-1)  {iX36=0; iY36++; break;}
          if(iY36>heightPer36-1) {iX36=0; iY36=0; i24or12=4; break;}
          i++;
          if(i>=maxPoints) break;
          x=iX36*36;
          y=iY36*36;
          colors=colors+"-";
        }
        return header+colors+"\n";
    }

    public void drawRYRect(Graphics g, int x,int y, int w, int h)
    {
            Color cc=g.getColor();
            g.setColor(Color.red);
//            g.fillOval(x,y,10,10);
            g.drawRect(x,y,w,h);
            g.setColor(Color.yellow);
            g.drawRect(x+1,y+1,w-2,h-2);
            g.setColor(cc);
    }

    public boolean loadingflag;

    public int startTime;

    public boolean isUpdated()
    {
        if(width<=0) return false;
        if(height<=0) return false;
        return true;
    }

    public AMessage process6()
    {
    	AMessage rtn=new AMessage();
        if(width<=0 || height<=0) return null;
        if(width<=0 || height<=0) return null;
        int x=iX6*6;
        int y=iY6*6;
        int pixels[]=new int[1*1];

        this.sendingX=x;
        this.sendingY=y;
        this.sendingW=width;
        this.sendingH=6;
//        this.canvas.gui.repaint();
        
        String header="name(\""+name+"\").("+
               x+","+y+")."+
                "width("+6+")."+
               "height("+6+")."+
               "tile.";

        int maxPoints=50;
        int i=0;
        String colors="";

        while(true){
          PixelGrabber pg=new PixelGrabber(image,x,y,1,1,pixels,0,1);
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
        rtn.setHead(header+colors+"\n");
        return rtn;


    }
    public int pix6[][];
    public int widthPer6;
    public int heightPer6;
    public int iY6;
    public int iX6;
    private Graphics g;
    public ImageOperator(FigCanvas canvas,NodeSettings s)
    {
        this.canvas=canvas;
        this.settings=s;
        this.setElementWidth(s.getInt("pictureSegmentWidth"));
        init();
    }
    public MyImage myImage;
    public synchronized void paint(Graphics g, int x, int y)
    {
        boolean rtn=false;
//        if(flags==ALLBITS){
          rtn=g.drawImage(image,x,y,width,height,this);
//        }
/*
          if(this.canvas.gui.getControlMode().equals("teach") && this.i24or12==0 ){
                this.canvas.gui.imageManager.start();
          }
*/            
        if(this.i24or12>0){
            drawRYRect(g,x+sendingX,y+sendingY,sendingW,sendingH);
        }
        if(rtn&&(!loadingflag)){
            int endTime=canvas.gui.getTimeNow();
            canvas.gui.recordMessage(""+startTime+",\"image "+url+" is updated\", "+
                (endTime-startTime)*0.1);
            loadingflag=true;

            
        }
        if(rtn==false){
            int h=image.getHeight(this);
            int w=image.getWidth(this);
            loadingflag=false;
            this.drawRYRect(g,x,y,w,h);
            
        }
//        this.notifyAll();
    }
    public int flags;
    public synchronized boolean imageUpdate(Image img, int infoflags,int x, int y,
    int width, int height) //synchronized
    {
 //        canvas.gui.recordMessage("-"+startTime+
 //               ":image "+url+" is updated, height="+img.getHeight(this));
         flags=infoflags;
         if(isCreatingImage){
            this.width=width;
            this.height=height;
            init2();
        }
         if(myImage!=null){
            if(isCreatingImage){
               myImage.x2=width;
               myImage.y2=height;
//               this.grabImage();
//               return true;
            }
         }
//         this.notifyAll();
         return true;
//         return false;
    }
    public void createNewImage(int width,int height)
    {
        this.width=width;
        this.height=height;
        image=canvas.createImage(width,height);
    }
    public void loadImage(String url)  //synchronized
    {
        this.url=url;
        URL xurl=null;
		try {
		    xurl=new java.net.URL(url);
		}
		catch (java.net.MalformedURLException error) {
		    System.out.println("malformed url exception while opening "+url);
		}
		catch (Exception e){
		    System.out.println("something is wrong while openeing "+url);
		}
		startTime=canvas.gui.getTimeNow();
//		canvas.gui.recordMessage("message: start loading url "+url);
		image = canvas.getToolkit().getImage(xurl);
		width=image.getWidth(this);
		height=image.getHeight(this);
		init();
//		this.notifyAll();
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
           
           widthPerImg=width/eWidth;
           heightPerImg=height/eWidth;

           iX36=0;
           iY36=0;
           iX24=0;
           iY24=0;
           iX12=0;
           iY12=0;
           iX6=0;
           iY6=0;
           iXimg=0;
           iYimg=0;

           i24or12=0;

           pix36=new int[widthPer36][heightPer36];
           pix24=new int[widthPer24][heightPer24];
           pix12=new int[widthPer12][heightPer12];
           pix6= new int[widthPer6 ][heightPer6 ];
  /*         
           subImageArray= new SubImageClass[widthPer12+1][heightPer12+1]; // 2007/1/6, t.yamanoue 
		   subImageArray2=new SubImageClass[widthPer12+1][heightPer12+1]; // 2007/2/22, t.yamanoue
           subImageBuffer= new Object[widthPer12+1][heightPer12+1]; // 2007/2/22, t.yamanoue
           subPixelBuffer = new Object[widthPer12+1][heightPer12+1]; //2007/2/22, t.yamanoue
*/
           subImageArray= new SubImageClass[widthPerImg+1][heightPerImg+1]; // 2007/1/6, t.yamanoue 
		   subImageArray2=new SubImageClass[widthPerImg+1][heightPerImg+1]; // 2007/2/22, t.yamanoue
           subImageBuffer= new Object[widthPerImg+1][heightPerImg+1]; // 2007/2/22, t.yamanoue
           subPixelBuffer = new Object[widthPerImg+1][heightPerImg+1]; //2007/2/22, t.yamanoue
           this.subAreaStatus = new int[widthPerImg+1][heightPerImg+1]; //2009/5/26, 6.yamanoue
           this.sampledImages=null;
           memoryImages=new Hashtable();
        }
   }
    public void loadImage(String url,int w, int h) //synchronized
    {
         this.url=url;
        URL xurl=null;
		try {
		    xurl=new java.net.URL(url);
		}
		catch (java.net.MalformedURLException error) {
		    canvas.gui.recordMessage("error: malformed URL Exception occured.");
		}
		startTime=canvas.gui.getTimeNow();
//		canvas.gui.recordMessage("message: start loading url "+url);
		image = canvas.getToolkit().getImage(xurl);
		if(image==null){
		    canvas.gui.recordMessage("message: loading url "+url+" failed.");
		}
		else{
			canvas.gui.recordMessage("-"+startTime+":url "+url+" loaded.");
	    }
		width=w;
		height=h;
		init();
//		this.notifyAll();
   }
    
    public void updateImage(BufferedImage img, int w){
    	this.image=img;
    	if(this.eWidth==w) return;
    	else this.eWidth=2;
    	this.init2();
    }
    
    public ImageOperator(FigCanvas fc, BufferedImage img, NodeSettings s){
    	canvas=fc;
    	this.image=img;
    	this.width=img.getWidth();
    	this.height=img.getHeight();
    	this.settings=s;
    	this.eWidth=s.getInt("pictureSegmentWidth");
    	init();
    }
    public ImageOperator(FigCanvas fc,MyImage myImage,NodeSettings s)
    {
        canvas=fc;
        this.myImage=myImage;
        this.settings=s;
        this.setElementWidth(s.getInt("pictureSegmentWidth"));
        init();
    }
    public ImageOperator next;
    /*
    public ImageOperator(int w, int h, Color c, FigCanvas fc,MyImage myImage)
    {
        width=w; height=h; canvas=fc;
        init();
        fillRect(0,0,w,h,c);
        this.myImage=myImage;
    }
    */
	boolean restWidth; // 2007/1/6, t.yamanoue
	boolean restHeight; // 2007/1/6
	int[][] subAreaStatus=null; // 2009 5/26, t.yamanoue
	public AMessage processImage()
	{
		if(this.image==null)return null;
		int x=iXimg*eWidth;
		int y=iYimg*eWidth;

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
        sendingW=eWidth;
        sendingH=eWidth;
/*
        System.out.println("iXimg="+iXimg+",iYimg="+iYimg+",sendingX="+sendingX+",sendingY="+sendingY+
        		",sendingW="+sendingW+",sendingH="+sendingH+
        		",hightPerImg="+heightPerImg+",height="+this.height+
        		",widthPerImg="+widthPerImg+",width="+this.width);
        		*/
        int pix[]=(int[])(subPixelBuffer[iXimg][iYimg]);
        if(pix==null){
        	pix=new int[sendingW*sendingH];
//        	pix=new int[eWidth*eWidth];
        }
		PixelGrabber pg=new PixelGrabber(image,sendingX,sendingY,sendingW,sendingH,pix,0,sendingW);
//		PixelGrabber pg=new PixelGrabber(image,sendingX,sendingY,eWidth,eWidth,pix,0,eWidth);
		try{
			pg.grabPixels();
		}
		catch(InterruptedException e)
		{
			System.out.println(e);
		}
		subPixelBuffer[iXimg][iYimg]=pix;

        /*
         * The following if statement is used for the motion
         * detecting. This reduces the traffic.
         */
		SubImageClass newSub=subImageArray2[iXimg][iYimg];
		if(newSub==null){
			newSub=new SubImageClass(sendingX,sendingY,sendingW,sendingH,pix);
			newSub.setStepAndError(this.subImageErrorStep,this.subImageError);
			subImageArray2[iXimg][iYimg]=newSub;
		}
		else{
    		newSub.copyPixels(sendingW,sendingH,pix);
		}
		SubImageClass oldSub=this.subImageArray[iXimg][iYimg];
		if(newSub.isAlmostTheSame(oldSub)) {
//						System.out.println("isAlmostTheSame");
			isNotNeedToSend=true;
			if(this.subAreaStatus[iXimg][iYimg]>0){
				this.subAreaStatus[iXimg][iYimg]--;
			}
		}
		else{
			if(oldSub==null){
				oldSub=new SubImageClass(sendingX,sendingY,sendingW,sendingH,pix);
				oldSub.setStepAndError(this.subImageErrorStep, this.subImageError);
			}
			else{
				oldSub.copyThis(newSub);
			}
			this.subImageArray[iXimg][iYimg]=oldSub;
			this.subAreaStatus[iXimg][iYimg]=4;
		}
		AMessage m=null;
		int sas=0;
		if(this.coding.equals("slowFine")){
		      sas=this.subAreaStatus[iXimg][iYimg];
		      if(sas>0){
   	              m= this.makeImageMessageWithSlowFine(name, sas, iXimg, iYimg, sendingX, sendingY, sendingW, sendingH, pix);		
		      }
		}
		/* */
		else{
    		if(!isNotNeedToSend){
	    	   if(this.coding.equals("raw")){
	   	           m= this.makeImageMessageWithNoCompression(name, iXimg, iYimg, sendingX, sendingY, sendingW, sendingH, pix);
		       }
		       if(this.coding.equals("jpeg")){
	               m= this.makeImageMessageWithJpegCompression(name, iXimg, iYimg, sendingX, sendingY, sendingW, sendingH, pix);   
		       }
		    }
		}
		/* */
	    
	    iXimg++;
        
        // 2007/1/6, t.yamanoue, 送信画像の範囲修正
        if(iXimg>widthPerImg)
        { 	iXimg=0; iYimg++;
            if(iYimg==4){
 //           	System.out.println("iYimg==4");
            }
            this.restWidth=false;
        }
        else
        if(iXimg>widthPerImg-1){
        	this.restWidth=true;
        }
        
        if(iYimg>heightPerImg){
        	i24or12=5;
//            System.out.println("to the next picture");
            this.restHeight=false;
        }
        else
        if(iYimg>heightPerImg-1){
        	this.restHeight=true;
        }	    
//       	if(sas==0) return null;
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
	    	pixByte=new byte[w*h*4];  //4byte per pixel.
	    	subImageBuffer[ix][iy]=pixByte;
	    }
	    int i=0;
	    try{        
	    for(i=0;i<w*h;i++){
	    	pixByte[i*4]=(byte)((pix[i] >>24)& 0x000000ff);
	    	pixByte[i*4+1]=(byte)((pix[i] >>16)& 0x000000ff);
	    	pixByte[i*4+2]=(byte)((pix[i] >>8)& 0x000000ff);
	    	pixByte[i*4+3]=(byte)(pix[i] & 0x000000ff);
	    }
	    }
	    catch(Exception e){
	    	System.out.println("i="+i+",ix="+ix+",iy="+iy+",x="+x+",y="+y+",w="+w+",h="+h);
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
			JPEGImageWriteParam param=new JPEGImageWriteParam(Locale.getDefault());
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(cmpQuality);
//			ByteArrayOutputStream os=new ByteArrayOutputStream(100000);
//			if(baos==null){
				baos=new ByteArrayOutputStream(w*h*4);
				// System.out.println("ByteArrayOutputStreamが取得できません");
//			}
			if(baos==null){
				System.out.println("ByteArrayOutputStreamが取得できません");				
			}
			else{
				ImageOutputStream ios=null;
				try{
					ios=ImageIO.createImageOutputStream(baos);
				}catch(Exception e){ios=null;}
				if(ios==null){
					System.out.println("ImageIOが取得できませんでした");
				}else{
					iw.setOutput(ios);
					try{
						iw.write(null,new IIOImage(bimg,null,null),param);
					}catch(Exception e){
						try{
							baos.close();
							ios.close();
						}catch(Exception e2){}
						System.out.println("bimgの圧縮中にエラーが起きました");
					}
					cmpBuf=baos.toByteArray();
					try{
						baos.close();
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
		String rtn= "name(\""+name+"\").("+
		   x+","+y+").width("+w+").height("+h+").jpeg\n";
        /*   */
//	    System.out.println(rtn);
		AMessage m=new AMessage();       
		m.setHead(rtn);
   		m.setData(cmpBuf,cmpBuf.length); // for jpeg
		return m;
	}
	
    byte[] sampledSubImageArea;
	AMessage makeImageMessageWithSlowFine(String picName, int areaStatus,int ix, int iy, int x, int y, int w, int h, int[] pix){
	    // for raw binary;
		int step=2;
		int aStep=step*step;
		int asr=4-areaStatus;
		int sx=asr%step;
		int sy=asr/step;
		int ssalen=4*h*w/aStep;
//		if(asr!=0)
	    sampledSubImageArea=(byte[])(subImageBuffer[ix][iy]);
	    if(sampledSubImageArea==null){
	    	sampledSubImageArea=new byte[ssalen];
	    	subImageBuffer[ix][iy]=sampledSubImageArea;
	    }
	    int i=0,j=0,ixy=0,pxy=0;
	    try{
	      ixy=0; 
	      for( j=0;j<h-step+1;j+=step)
	    	for( i=0;i<w-step+1;i+=step)
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
	    	System.out.println(e.toString());
			System.out.println("areaStatus="+areaStatus+" ix="+ix+" iy="+iy+" x="+x+" y="+y+" w="+w+" h="+h+" aStep="+aStep+" sx="+sx+" sy="+sy+" asr="+asr);
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
		  PixelGrabber pg=new PixelGrabber(image,x,y,1,1,pixels,0,1);
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
		AMessage rtn=new AMessage();
        if(width<=0 || height<=0) return null;
        int x=iX24*24;
        int y=iY24*24;
//        int w=8;
        int w=1;
//        int h=8;
        int h=1;
        int pixels[]=new int[w*h];
        /*
        PixelGrabber pg=new PixelGrabber(image,x,y,1,1,pixels,0,1);
        this.grabImage();
        */
        this.sendingX=x;
        this.sendingY=y;
        this.sendingW=width;
        this.sendingH=24;
        if((iY24 % 4) ==0) this.canvas.gui.repaint();
        
        String header="name(\""+name+"\").("+
               x+","+y+")."+
                "width("+24+")."+
               "height("+24+")."+
               "tile.";

        int maxPoints=50;
        int i=0;
        String colors="";

        while(true){
            
          PixelGrabber pg=new PixelGrabber(image,x,y,w,h,pixels,0,w);
          try{
              pg.grabPixels();
          }
          catch(InterruptedException e)
          {
            System.out.println(e);
          }
          
//        pix24[iX24][iY24]=pixels[0];
          iX24++;
          colors=colors+"color("+pixels[0]+")";
//          colors=colors+"color("+pixels[width*y+x]+")";

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
        rtn.setHead(header+colors+"\n");
        return rtn;
    }
	public void setName(String x)
    {
        name=x;
    }
	/*
    public String process0_x()
    {
        int currentX=this.myImage.offX;
        int currentY=this.myImage.offY;
        
        if(width<=0 || height <=0) {
            return "name(\""+name+"\").("+currentX+","+currentY+")."+
                   "width("+10+")."+
                   "height("+10+")."+
                   "create."+
                   "color("+(Color.black).getRGB()+")\n";
        }
        

//		try{
//			Thread.sleep(1);
//		}
//		catch(InterruptedException e){
//		  System.out.println(e);
//		}


        pixels=new int[5*5];
        PixelGrabber pg=new PixelGrabber(image,0,0,5,5,pixels,0,5);
        try{
            pg.grabPixels();
        }
        catch(InterruptedException e)
        {
            System.out.println(e);
        }
                
//        i24or12=1;
        i24or12=4;
        iX24=0; iY24=0;
        this.sendingX=0;
        this.sendingY=0;
        this.sendingW=width;
        this.sendingH=height;
        this.canvas.gui.repaint();
        
        
        return "name(\""+name+"\").("+currentX+","+currentY+")."+
               "width("+width+")."+
               "height("+height+")."+
               "create."+
               "color("+pixels[0]+")\n";
    }
*/
	public AMessage process0()
	{
		AMessage rtn=new AMessage();
		int currentX=0;
		int currentY=0;
        if(width<=0 || height <=0) {
        	String h=
             "name(\""+name+"\").("+currentX+","+currentY+")."+
                   "width("+10+")."+
                   "height("+10+")."+
                   "create."+
                   "color("+(Color.black).getRGB()+")\n";
        	rtn.setHead(h);
        	return rtn;
        }		
        pixels=new int[4*4];
        PixelGrabber pg=new PixelGrabber(image,0,0,4,4,pixels,0,4);
        try{
            pg.grabPixels();
        }
        catch(InterruptedException e)
        {
            System.out.println(e);
        }
                
//        i24or12=1;
        i24or12=4;
        iX24=0; iY24=0;
        this.sendingX=0;
        this.sendingY=0;
        this.sendingW=width;
        this.sendingH=height;
        this.canvas.gui.repaint();
        
        /* */
        String h= "name(\""+name+"\").("+currentX+","+currentY+")."+
               "width("+width+")."+
               "height("+height+")."+
               "create."+
               "color("+pixels[0]+")\n";
        rtn.setHead(h);
        /* */
//        rtn.setHead("");                    /////
        return rtn;
	}
	/*
	public String nextImage_x()
    {
        if(!isUpdated()) return null;
        if(i24or12<0)  return null;
        if(i24or12==0) return process0_x();
        if(i24or12==1) return process24_x();
//        if(i24or12==1) return process36();
        if(i24or12==2) return process12_x();
        if(i24or12==3) return process6_x();
        if(i24or12==4) return processImage_x();
        return null;
    }
    */
	public AMessage nextImage()
	{
//		System.out.println("start nextImage.");
//		if(this.sourceImage==null) return null;
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
			System.out.println("iXimg="+this.iXimg+",iYimg="+this.iYimg);
		}
//        if(i24or12==5) return this.sendPaintOffScreenCommand();
		if(i24or12==5){
			i24or12=0; iYimg=0;
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
            this.createNewImage(width,height);
            gx=image.getGraphics();
        }
        if(gx==null){
            gx=image.getGraphics();
        }
        gx.setColor(c);
        gx.fillRect(x,y,w,h);
        canvas.gui.repaint();
    }
    public int i24or12;
    public ImageOperator(String url, FigCanvas fc,MyImage myImage, NodeSettings s)
    {
        canvas=fc;
        this.settings=s;
        this.eWidth=s.getInt("pictureSegmentWidth");
        loadImage(url);
        init();
        this.myImage=myImage;
    }
    public int pix12[][];
    public int pix24[][];
    public FigCanvas canvas;

public char i2a[]={
        '0','1','2','3','4','5','6','7','8','9',
        'a','b','c','d','e','f','g','h','i','j',
        'k','l','m','n','o','p','q','r','s','t',
        'u','v','w','x','y','z','!','#','$','%',
        'A','B','C','D','E','F','G','H','I','J',
        'K','L','M','N','O','P','Q','R','S','T',
        'U','V','W','X'};
    public int a2i[];
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

        init2();
        flags=0;
        isSending=false;
        isSent=false;
        isCreatingImage=true;
    }

    public String image2String(){
    	String rtn="";
    	int ww=eWidth;
	    int[] pix=new int[ww*ww];
    	for(int j=0;j<=(height/ww);j++){
    		for(int i=0;i<=(width/ww);i++){
		       PixelGrabber pg=new PixelGrabber(image,i*ww,j*ww,ww,ww,pix,0,ww);
		       try{
			      pg.grabPixels();
		       }
		       catch(InterruptedException e)
		       {
		       	System.out.println(e);
		       }
   	           rtn=rtn+"subImage("+i*ww+","+j*ww+","+ww+","+ww+"\n"+
   	           subImage2String(i*ww,j*ww,ww,ww,pix)+")\n";
    		}
    	}
    	return rtn;
    }
    public String subImage2String(int x, int y, int w, int h,int pixels[])
    {

		int pixSize=w*h;
       int strSize=pixSize*6;

       char subimg[]=new char[strSize];
       String rtn="";

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
        int rest=strSize;
        int line_length=320*6;
        int line_position=0;
        while(rest>0){
        	if(rest>line_length){
        	   rtn=rtn+"\""+new String(subimg, line_position, line_length)+"\"-\n";
        	   rest=rest-line_length;
        	}
        	else{
        		String last="\""+new String(subimg, line_position, rest)+"\"";
        		rtn=rtn+last;
        		rest=rest-last.length();
        	}
        	line_position=line_position+line_length;
        }
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
  public int[] getSubImgBuffer(int w, int h){
      int pixSize=w*h;
      int pixels[]=new int[pixSize];
      return pixels;	  
  }
  public int setSubImgBufferFromString(int[] pix, int offset, StringBuffer s){
      int ps=0;
//    int imglength=subimg.length();
    int sl=s.length()/6;
    isCreatingImage=false;
    String xs="";
    int i=0;
    for(i=0;i<sl;i++){
        xs=s.substring(ps,ps+6);
        pix[offset+i]=string2Int(xs);
        ps+=6;
    }
	return sl;
  }
  public void subImgDraw(int x, int y, int w, int h, int[] pix){
      Graphics gx=image.getGraphics();
      if(gx==null){
          gx=image.getGraphics();
      }
      MemoryImageSource mis=new MemoryImageSource(w,h,pix,0,w);
      Image ximg=canvas.createImage(mis);
      gx.drawImage(ximg,x,y,w,h,this);  // canvas -> this
      canvas.gui.repaint();	  
  }
  public void string2subImg(int x, int y, int w, int h, String subimg)
    {
        int pixSize=w*h;
        int pixels[]=new int[pixSize];
        int ps=0;
//        int imglength=subimg.length();
        String xs="";
        isCreatingImage=false;
        for(int i=0;i<pixSize;i++){
            xs=subimg.substring(ps,ps+6);
            pixels[i]=string2Int(xs);
            ps+=6;
        }
        Graphics gx=image.getGraphics();
        if(gx==null){
            gx=image.getGraphics();
        }
        MemoryImageSource mis=new MemoryImageSource(w,h,pixels,0,w);
        Image ximg=canvas.createImage(mis);
        gx.drawImage(ximg,x,y,w,h,this);  // canvas -> this
        canvas.gui.repaint();
    }
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
    public boolean isAllTheSameColor(int x, int y, int w, int h,int pixels[])
    {
        int noizeCount=0;
        int pixSize=w*h;
        
        int firstPix=pixels[0];
        for(int i=0;i<pixSize;i++){
            if(firstPix!=pixels[i]) {
                noizeCount++;
                if(noizeCount>2) return false;
            }
        }
        return true;
        /*
        int firstPix=pixels[y*width+x];
        for(int yy=0;yy<h;yy++)
        for(int xx=0;xx<w;xx++)
        {
            int currentPix=pixels[(y+yy)*width+(x+xx)];
            if(firstPix!=currentPix) {
                noizeCount++;
                if(noizeCount>10) return false;
            }
        }
        return true;
        */
    }
	class SubImageClass{
		int x,y,w,h;
		int[] pix;
		int err;
        int step;		
		private void setError(){
//			this.err=w*h*4*3;                       /////               /////
//            this.err=5*4*4;
//			this.err=w*h*2;
			this.err=10;
			this.step=4;
		}
		public void setError(int r){
			this.err=w*h*step*r/(step*step);
		}
		public void setStepAndError(int s, int r){
			if(s==0){
				System.out.println("strange s ="+s);
				s=4;
			}
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
			if(pix==null)
		    	this.pix=new int[p.length];
			else
			if(pix.length<p.length){
				this.pix=new int[p.length];
			}
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
//			int l=w*h;
			int s=0;
			if(x.pix==null) return -1;
//			int sr=settings.getAnswer("diffPixSamplingRate");

			for(int i=0;i<x.h;i+=this.step)
				for(int j=0;j<x.w;j+=this.step){
					int p=i*(x.w)+j;
				s=s+diff1Pix(pix[p],x.pix[p]);
			}

/*
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
    int xpixels[]=null;
    public void setBinaryImage(int x, int y, int w, int h, byte[] img){
    	int nlen=img.length/4;
    	if(xpixels==null){
    		xpixels=new int[w*h];
    	}
    	int xlen=xpixels.length;
    	if(nlen>xlen){
    		xpixels=new int[nlen];
    	}
    	for(int i=0;i<nlen;i++){
    		xpixels[i]=((0x000000ff & img[i*4])<<24) | ((0x000000ff & img[i*4+1])<<16) | ((0x000000ff & img[i*4+2])<<8) | (0x000000ff & img[i*4+3]) ;
    	}
    	setSubImage(x,y,w,h,xpixels,canvas);
    }
    Hashtable memoryImages=new Hashtable();
    XYWH xywh=new XYWH(0,0,0,0);
    ColorModel defaultColorModel = ColorModel.getRGBdefault();
    //	MemoryImageSource mis=new MemoryImageSource(w,h,pix,0,w);	
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
//        this.drawImage(ximg,x,y,w,h,panel);
        Graphics gx=image.getGraphics();
        if(gx==null){
            gx=image.getGraphics();
        }
//        MemoryImageSource mis=new MemoryImageSource(w,h,pixels,0,w);
//        Image ximg=canvas.createImage(mis);
//        System.out.println("x1="+x+" y1="+y+" x2="+(x+w)+" y2="+(y+h));
        gx.drawImage(ximg,x,y,w,h,this);  // canvas -> this
//        gx.drawImage(ximg, x,w,h,panel); 
        canvas.gui.repaint();
//      this.imageUpdate(null,0,0,0,width,height);
    }    
	
	    Hashtable sampledImages;    
	    public void setSlowFineImage(String code, int x, int y, int w, int h, byte[] img){
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
    		int[] pixsXY=(int[])(sampledImages.get(keyXY));
    		if(pixsXY==null){
        		pixsXY=new int[w*h];
    		}
	    	if(position==0){
	    		int p=0;
	    		for(int j=0;j<h-step+1;j+=step)
	    			for(int i=0;i<w-step+1;i+=step)
	    		{ 
	    			int color	=((0x000000ff & img[p*4])<<24) | ((0x000000ff & img[p*4+1])<<16) | ((0x000000ff & img[p*4+2])<<8) | (0x000000ff & img[p*4+3]) ;
	    			pixsXY[i+j*w]=color;
	    			pixsXY[i+1+j*w]=color;
	    			pixsXY[i+(j+1)*w]=color;
	    			pixsXY[i+1+(j+1)*w]=color;
	            	p++;
	    		}
	    	    setSubImage(x,y,w,h,pixsXY,canvas);    	
	    	    sampledImages.put(keyXY, pixsXY);
	    	    return;
	    	}
	    	
//			if(pixsXY==null) return;            

	    	if(position==1){
	    		int p=0;
	    		for(int j=0;j<h-step+1;j+=step)
	    			for(int i=0;i<w-step+1;i+=step)
	    		{ 
	    			int color	=((0x000000ff & img[p*4])<<24) | ((0x000000ff & img[p*4+1])<<16) | ((0x000000ff & img[p*4+2])<<8) | (0x000000ff & img[p*4+3]) ;
	    			pixsXY[i+1+j*w]=color;
	    			pixsXY[i+1+(j+1)*w]=color;
	            	p++;
	    		}
	    	    setSubImage(x,y,w,h,pixsXY,canvas);    	
	    	    sampledImages.put(keyXY, pixsXY);
	    	    return;
	    	}
	    	if(position==2){
	            
	    		int p=0;
	    		for(int j=0;j<h-step+1;j+=step)
	    			for(int i=0;i<w-step+1;i+=step)
	    		{ 
	    			int color	=((0x000000ff & img[p*4])<<24) | ((0x000000ff & img[p*4+1])<<16) | ((0x000000ff & img[p*4+2])<<8) | (0x000000ff & img[p*4+3]) ;
	    			pixsXY[i+(j+1)*w]=color;
	            	p++;
	    		}
	    	    setSubImage(x,y,w,h,pixsXY,canvas);    	
	    	    sampledImages.put(keyXY, pixsXY);
	    	    return;
	    	}
	    	if(position==3){
	            
	    		int p=0;
	    		for(int j=0;j<h-step+1;j+=step)
	    			for(int i=0;i<w-step+1;i+=step)
	    		{ 
	    			int color	=((0x000000ff & img[p*4])<<24) | ((0x000000ff & img[p*4+1])<<16) | ((0x000000ff & img[p*4+2])<<8) | (0x000000ff & img[p*4+3]) ;
	    			pixsXY[i+1+(j+1)*w]=color;
	            	p++;
	    		}
	    	    setSubImage(x,y,w,h,pixsXY,canvas);    	
	    	    sampledImages.put(keyXY, pixsXY);
	    	    return;
	    	}
	    }

	    public void setJpegImage(int x, int y, int w, int h, byte[] img)
	    {
	    	int nlen=img.length/4;
	    	if(xpixels==null){
	    		xpixels=new int[w*h];
	    	}
	    	int xlen=xpixels.length;
	    	if(nlen>xlen){
	    		xpixels=new int[nlen];
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
	    	
	    	setSubImage(x,y,w,h,xpixels,canvas);
//	    	20090207
	    	//プログラム追加→入江健悟
	    	//分割数を指定して描画の最後に時間を表示するプログラム
//			Bunkatsu_Flug++;
//			if(Bunkatsu_Flug==Bunkatsu){
//				System.out.println("E N D"+System.currentTimeMillis());//描画終了時間
//				Bunkatsu_Flug = 0;
//			}
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
	public void setElementWidth(int w){
		this.eWidth=w;
	}
	String coding="slowFine";
	int subImageError=5;
	int subImageErrorStep=4;
    public void setParameters(Parameters p){
    	this.eWidth=p.getInt("pictureSegmentWidth");
    	this.coding=p.getString("pictureCoding");
    	this.subImageError=p.getInt("errorRate");
    	this.subImageErrorStep=p.getInt("errorSamplingWidth");
    	this.cmpQuality=p.getFloat("jpegCompressRate");
    }

}

