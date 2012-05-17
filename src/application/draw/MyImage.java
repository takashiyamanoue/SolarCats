package application.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import nodesystem.NodeSettings;
import nodesystem.TemporalyText;
import nodesystem.AMessage;

public class MyImage extends AFigElement
{
	NodeSettings settings;
	
	public int getWidth(){
		return this.imageOperator.height;
	}
	public int getHeight(){
		return this.imageOperator.width;
	}
    public MyImage(FigCanvas fc, ImageOperator io, NodeSettings s)
    {
    	this.settings=s;
        init();
        canvas=fc;
//        imageManager=canvas.gui.imageManager;
        imageOperator=io;
        imageManager=canvas.gui.imageManager ;
    }

    public AFigure copyThis()
    {
        MyImage f=(MyImage)(super.copyThis2(new MyImage(canvas)));
        f.x2=x2;
        f.y2=y2;
        f.imageOperator=imageOperator;
        f.imageManager=imageManager;
        f.imageName=imageName;

        return (MyImage)f;
   }
    public void copy(MyImage f)
    {
        offX=f.offX; offY=f.offY; x2=f.x2; y2=f.y2;
        imageOperator=f.imageOperator;
        imageManager=f.imageManager;
        imageName=f.imageName;
        url=f.url;
    }
    /*
    public void sendBase()
    {
        String sendingString=imageOperator.process0_x();
        imageManager.gui.sendEvent("img("+sendingString+")\n");
    }
    */
    public String imageName;
    public void updateImage()
    {
        imageManager.add(imageOperator);
    }
    public void load(String name,String url)
    {
        ImageOperator imgx=imageManager.getImageOperator(name);
        if(imgx==null){
          imageOperator =new ImageOperator(url,canvas,this,this.settings);
          String myname=imageManager.newName();
          imageOperator.name=myname;
          imageManager.add(imageOperator);
        }
		x2=imageOperator.image.getWidth(imageOperator);
		y2=imageOperator.image.getHeight(imageOperator);

    }
    public ImageOperator imageOperator;
    public ImageManager imageManager;
    public int subImgSize;
    public int mem_pix[];
    int elementWidth=128;

    public void updateImage(BufferedImage img, int eWidth){
    	this.imageOperator.updateImage(img,eWidth);
    }
        
    public MyImage(FigCanvas fc, BufferedImage img,String myname,NodeSettings s){
        canvas=fc;
        this.settings=s;
        this.elementWidth=s.getInt("pictureSegmentWidth");
        init();
        imageManager=canvas.gui.imageManager ;
        if(!(this.imageManager.gui.isControlledByLocalUser())) return;
    	imageOperator=new ImageOperator(fc,img,s);
    	imageOperator.setParameters(s);
        imageOperator.name=myname;
        imageOperator.setMyImage(this);
        imageManager.add(imageOperator);
        imageName=myname;

        AMessage sendingString=imageOperator.process0();
        if(sendingString!=null){
           String h=sendingString.getHead();
           if(!h.equals("")){
              sendingString.setHead("bgimg("+h+")\n");
              imageManager.gui.sendEvent(sendingString);
           }
        }

		x2=imageOperator.image.getWidth(imageOperator);
		y2=imageOperator.image.getHeight(imageOperator);
    }
    public MyImage(FigCanvas fc)
    {
        init();
        canvas=fc;
        this.settings=canvas.gui.getNodeSettings();
        imageOperator=new ImageOperator(fc,this,this.settings);
        imageManager=canvas.gui.imageManager ;
   }
    public String url;
    public void setWidth(int x){
    	if(this.imageOperator==null) return;
    	this.imageOperator.width=x;
    }
    public void setHeight(int y){
    	if(this.imageOperator==null) return;
    	this.imageOperator.height=y;
    }
    public void nextNewFig()
    {
           step=-10;
           this.selected=false;
           showhide=true;
           canvas.fs.add(this);
           canvas.ftemp.remove(this);
           this.start();
    }
    public int a2i[];
    public char i2a[]={
        '0','1','2','3','4','5','6','7','8','9',
        'a','b','c','d','e','f','g','h','i','j',
        'k','l','m','n','o','p','q','r','s','t',
        'u','v','w','x','y','z','!','#','$','%',
        'A','B','C','D','E','F','G','H','I','J',
        'K','L','M','N','O','P','Q','R','S','T',
        'U','V','W','X'};
    public boolean save(TemporalyText outS)
    {
         if(url==null) {
             if(!saveHeader(outS,"image")) return false;
             if(!strmWrite(outS,"name("+"\""+imageOperator.name+"\")\n")) return false;
             if(!strmWrite(outS, ""+offX+","+offY+","+x2+","+y2+"\n")) return false;
             if(!strmWrite(outS,"strImg("+this.imageOperator.image2String()+")\n")) return false;
             if(!strmWrite(outS,")\n")) return false;
        	 return true;
         }
         else{
         StringBuffer urlbuff=new StringBuffer(url);
         if(!saveHeader(outS,"image")) return false;
         if(!strmWrite(outS,"name("+"\""+imageOperator.name+"\")\n")) return false;
         if(!strmWrite(outS, ""+offX+","+offY+","+x2+","+y2+"\n")) return false;
         if(!strmWrite(outS,"url(\""+str2saveable(urlbuff)+"\")\n")) return false;
         if(!strmWrite(outS,")\n")) return false;
         return true;
         }
    }
    public boolean isSelected(int x, int y)
    {
        /*
        if(xView(offX)>x) return false;
        if(yView(offY)>y) return false;
        if(xView(offX+x2)<x) return false;
        if(yView(offY+y2)<y) return false;
        return true;
        */
        if(isPointed(offX,offY,x,y)) return true;
        if(isPointed(offX+x2,offY+y2,x,y)) return true;
        if(isPointed(offX+x2,offY,x,y)) return true;
        if(isPointed(offX,offY+y2,x,y)) return true;
        if(isOntheLine(x,y,offX,offY,offX+x2,offY)) return true;
        if(isOntheLine(x,y,offX+x2,offY,offX+x2,offY+y2)) return true;
        if(isOntheLine(x,y,offX+x2,offY+y2,offX,offY+y2)) return true;
        if(isOntheLine(x,y,offX,offY+y2,offX,offY)) return true;
        return false;
    }
    public void mouseMove(int xx, int yy)
    {
        int x=xLogical(xx);
        int y=yLogical(yy);
        showhide=true;
        if(step==0) // new , setting one edge
        { offX=x; offY=y;
          return; }
        if(step==1) // setting another edge
        { //x2=x-offX; y2=y-offY;
          return;}
        if(step==40) // moving this line
        {offX=x; offY=y; return;}
        if(step==51) // modifying, the first step
        { //movePoint(x,y);
        return;}
        if(step==60) // magnify, the first step... setting center
        {
          //  dcX=x; dcY=y;
        }
        if(step==61)// magnify, the second step
        {
        //    dcX2=x; dcY2=y;
            return;
        }
        if(step==62)// magnify, the third step
        {
//            dcX2=x; dcY2=y;
//            ddX=dcX2-dcX; ddY=dcY2-dcY;
//            magRatioX=Math.sqrt((dcX-dcX2)*(dcX-dcX2)+(dcY-dcY2)*(dcY-dcY2));
//            magRatio=magRatioX/magRatioR;
//            angle=Math.atan2(ddX,ddY);
//            magnifyXY();
            return;
        }

    }

    public void mouseDown(int xx, int yy)
    {
        int x=xLogical(xx);
        int y=yLogical(yy);
        if(step==-10){
            if(isSelected(x,y)){
                if(isPlaying()){
                    actWhenPlaying();
                    return;
                }
                step=10;
                selected=true;
                canvas.ftemp.add(this);
                canvas.fs.remove(this);
            }
        }
        if(step==0) // new fig
        { offX=x; offY=y;
          nextNewFig();//step++;
          canvas.editdispatch.newFig("Line");
          return;}
/*
        if(step==1){
//            x2=x-offX; y2=y-offY;
            nextNewFig();
            return;
        }
*/
        if(step==10){
            if(isSelected(x,y)){
            }
            else
            {
                step=-10;
                selected=false;
                canvas.fs.add(this);
                canvas.ftemp.remove(this);
            }
            return;
        }
        if(step==40) //moving
        {
            offX=x; offY=y;
            step=-10;
            selected=false;
            canvas.fs.add(this);
            canvas.ftemp.remove(this);
            canvas.editdispatch.select();
            return;
        }
        if(step==50) // modifying
        {
//            if(startModifyPoint(x,y)){
//                step++;
//            }
            return;
        }
        if(step==51) // modifying, the second  step
        {
 //           movePoint(x,y);  step=50; return;
        }
        if(step==60) // magnifying, the first step
        {
            return;
        }
        if(step==61) // magnifying, the second step
        {
//            dcX2=x; dcY2=y;
//            magRatioR=Math.sqrt((dcX-dcX2)*(dcX-dcX2)+(dcY-dcY2)*(dcY-dcY2));
//            if(magRatioR==0.0) return;
//            thisCopy=new ALine();
//            thisCopy.copy(this);
//            canvas.gui.messages.appendText("Please enlarge and Rotate it.\n");
//            step++;
            return;
        }
        if(step==62) // magnifying, the third step
        {
//            thisCopy=null;
//            step=-10;
//            selected=false;
//            canvas.fs.add(this);
//            canvas.ftemp.remove(this);
//            this.start();
//            canvas.editdispatch.select();
            return;
        }

    }
    public void drawTemp(Graphics g)
    {
        if(imageOperator==null) return;
        if(imageOperator.image==null) return;
        /*
		x2=imageOperator.image.getWidth(canvas);
		y2=imageOperator.image.getHeight(canvas);
		imageOperator.width=x2;
		imageOperator.height=y2;
		imageOperator.init2();
        g.drawImage(imageOperator.image,xView(offX),yView(offY),canvas);
        */
        imageOperator.paint(g,xView(offX),yView(offY));
        showEdge(g,xView(offX),yView(offY));
        showEdge(g,xView(offX+x2),yView(offY));
        showEdge(g,xView(offX+x2),yView(offY+y2));
        showEdge(g,xView(offX),yView(offY+y2));
        showRC(g);
  }
    public void draw(Graphics g)
    {
        Color cc=g.getColor();
        g.setColor(color);
        if(imageOperator==null) return;
        if(imageOperator.image==null) return;

        /*
		x2=imageOperator.image.getWidth(canvas);
		y2=imageOperator.image.getHeight(canvas);
		imageOperator.width=x2;
		imageOperator.height=y2;
        imageOperator.init2();
        */
        if(isEditing()) drawTemp(g);
        else
          imageOperator.paint(g,xView(offX),yView(offY));
//          g.drawImage(imageOperator.image,xView(offX),yView(offY),canvas);
        g.setColor(cc);
    }
    public void init()
    {
        super.init();
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
        subImgSize=256;

    }
    public MyImage(FigCanvas fc, String url, NodeSettings s)
    {
    	this.settings=s;
    	if(this.imageManager!=null){
            if(!(this.imageManager.gui.isControlledByLocalUser())) return;
    	}
//        this.imageManager.gui.mutex.setTimeOut(0,100);
         canvas=fc;
        init();
        imageManager=canvas.gui.imageManager ;
//        imageOperator=new ImageOperator(url,fc,this);
        imageOperator=new ImageOperator(fc,this.settings);
        String myname=imageManager.newName();
        imageOperator.name=myname;
        imageOperator.setMyImage(this);
        imageManager.add(imageOperator);
        imageName=myname;

        AMessage sendingString=imageOperator.process0();
        if(sendingString!=null){
//        imageManager.gui.sendEvent("img("+sendingString+")\n",2);
        	String h=sendingString.getHead();
        	sendingString.setHead("img("+h+")\n");
          imageManager.gui.sendEvent(sendingString);
        }
/*
        if((imageManager.gui.getControlMode()).equals("teach"))
            imageManager.start();
 */
        imageOperator.loadImage(url);

        this.url=url;

		x2=imageOperator.image.getWidth(imageOperator);
		y2=imageOperator.image.getHeight(imageOperator);
   }
    public int y2;
    public int x2;
    public void newFig()
    {
        offX=0; offY=0;
		x2=imageOperator.image.getWidth(canvas);
		y2=imageOperator.image.getHeight(canvas);
        step=0;
        showhide=false;
        selected=true;
        if(color==null) color=canvas.getForeground();
   }
}

