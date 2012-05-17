package application.draw;
import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;

import javax.swing.JTextArea;

import controlledparts.*;

class ParseFig extends java.lang.Object
{
	Hashtable applications;
	public void setApplication(String aname,ControlledFrame a){
		if(aname==null) return;
		if(applications!=null){
			this.applications.put(aname,a);
		}
	}
    public boolean parseClickableBoxExec()
    {
        parseB();
        if(!iq.rString("clickableboxexe(")) return false;
        ClickableBoxExecCommand fig=new ClickableBoxExecCommand(canvas);
        fig.setApplications(applications);
        if(!parseAttrib(fig));
//
        if(!rNumPair()) return false;
        int offX=x1; int offY=y1;
        fig.offX=x1; fig.offY=y1;

        if(!iq.rString(",")) return false;
        if(!rNumPair()) return false;
        int x2=x1; int y2=y1;
        fig.x2=x1; fig.y2=y1;
//
        if(!iq.rString(",")) return false;
        StringBuffer str=iq.rStrConst();
        if(str==null) return false;

//
//            byte[] sjisCode = new byte[str.length()];
//            (str.toString()).getBytes(0,str.length(),sjisCode,0);
//*        byte[] sjisCode = (str.toString()).getBytes();
            // S-JIS Code ‚ð Unicode ‚É•ÏŠ·
//            char[] uniCode = ShiftJISStringToJavaString.convertAll(sjisCode);
//

//        fig.text=new StringBuffer(String.valueOf(uniCode));
          fig.command=str.toString();

        parseB();
        if(!iq.rString(")")) return false;
/*
        fig.urlName.font=canvas.getFont();
        fig.urlName.fmetrics=canvas.getFontMetrics(fig.urlName.font);
*/
        afig=fig;
        return true;
    }

    public boolean parseClickableBoxURL()
    {
        parseB();
        if(!iq.rString("clickableboxurl(")) return false;
        ClickableBoxURL fig=new ClickableBoxURL(canvas);
        if(!parseAttrib(fig));
//
        if(!rNumPair()) return false;
        int offX=x1; int offY=y1;
        fig.offX=x1; fig.offY=y1;

        if(!iq.rString(",")) return false;
        if(!rNumPair()) return false;
        int x2=x1; int y2=y1;
        fig.x2=x1; fig.y2=y1;
//
        if(!iq.rString(",")) return false;
        if(!iq.rString("bp(")) return false;
        Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(")")) return false;
        fig.setBasePathBox(c.intValue());
//
        if(!iq.rString(",")) return false;
        StringBuffer str=iq.rStrConst();
        if(str==null) return false;

//
//*            byte[] sjisCode = new byte[str.length()];
//*            (str.toString()).getBytes(0,str.length(),sjisCode,0);
            // S-JIS Code ‚ð Unicode ‚É•ÏŠ·
//            char[] uniCode = ShiftJISStringToJavaString.convertAll(sjisCode);
//

//        fig.text=new StringBuffer(String.valueOf(uniCode));
          fig.urlName=str.toString();

        parseB();
        if(!iq.rString(")")) return false;
/*
        fig.urlName.font=canvas.getFont();
        fig.urlName.fmetrics=canvas.getFontMetrics(fig.urlName.font);
*/
        afig=fig;
        return true;
    }

    public boolean parseFont(AFigure fig)
    {   parseB();
        if(!iq.rString("font(")) return false;
        StringBuffer str=iq.rStrConst();
        if(str==null) return false;
        String fname=str.toString();
        if(!iq.rString(","))     return false;
        Integer c=iq.rInteger();
        if(c==null) return false;
        int fstyle=c.intValue();
        if(!iq.rString(","))     return false;
        c=iq.rInteger();
        if(c==null) return false;
        int fsize=c.intValue();
        if(!iq.rString(")")) return false;
        ((Text)fig).setFont(new Font(fname,fstyle,fsize));

        return true;
    }

    public boolean parseWidth(AFigure fig)
    {   parseB();
        if(!iq.rString("width(")) return false;
        Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(")")) return false;
        fig.lineWidth=c.intValue();

        return true;
    }

    public boolean parseCurve()
    {
        int offX,offY;
        ACurve fig=null;
        parseB();
        if(!iq.rString("curve(")) return false;
        fig=new ACurve(canvas);
        if(!parseAttrib(fig));
        // message.appendText("parsing lines(");
        if(!parsePoints(fig)) return false;
        afig=fig;
        return true;
    }

    public ParseFig()
    {
    	this.applications=new Hashtable();
    }

    public boolean parseLoadText()
    {
        return false;
    }
    public boolean parseImage()
    {
        int offX, offY;
        parseB();
        if(!iq.rString("image(")) return false;
        MyImage fig=new MyImage(canvas);

        // message.appendText("parsing polygon(");

        if(!parseAttrib(fig));

        if(!iq.rString("name(")) return false;
        StringBuffer str=iq.rStrConst();
        if(str==null) return false;
        if(!iq.rString(")")) return false;
        String name=str.toString();

        parseB();

        if(!rNumPair()) return false;
        offX=x1; offY=y1;
        fig.offX=x1; fig.offY=y1;

        if(!iq.rString(",")) return false;

        if(!rNumPair()) return false;
        int w=x1; int h =y1;
        fig.x2=w; fig.y2=h;
        

        parseB();

        if(iq.rString("url(")) {
           str=iq.rStrConst();
           if(str==null) return false;
           if(!iq.rString(")")) return false;
           String url=str.toString();
           parseB();
           if(!iq.rString(")")) return false;

           fig.url=url;
           fig.imageManager=canvas.gui.imageManager;
           ImageOperator img=fig.imageManager.getImageOperator(name);
           if(img==null) {
               fig.imageOperator.loadImage(url,w,h);
               fig.imageManager.setImageOperator(name,fig.imageOperator);
           }
           else{
               fig.imageOperator=img;
           }
        }
        else
        if(iq.rString("strImg(")){
            fig.imageManager=canvas.gui.imageManager;
            ImageOperator img=fig.imageManager.getImageOperator(name);
            if(img==null) {
                img=new ImageOperator(canvas,canvas.gui.communicationNode.getNodeSettings());
                img.createNewImage(w,h);
                fig.imageOperator=img;
                fig.imageManager.setImageOperator(name,img);
            }
            else{
                fig.imageOperator=img;
            }

            parseB();
            int[] pix;
            while(iq.rString("subImage(")) {
               if(!rNumPair()) return false;
               int xo=x1; int yo=y1;
               if(!iq.rString(",")) return false;
               if(!rNumPair()) return false;
               int we=x1; int he =y1;
               
               pix=fig.imageOperator.getSubImgBuffer(we, he);
               int offset=0;
        	   str=iq.rStrConst();
        	   int ssize= fig.imageOperator.setSubImgBufferFromString(pix, offset, str);
        	   offset=offset+ssize;
        	   while(iq.rString("-")){
        		   str=iq.rStrConst();
            	   ssize= fig.imageOperator.setSubImgBufferFromString(pix, offset, str);
            	   offset=offset+ssize;
        	   }
               if(str==null) return false;
               if(!iq.rString(")")) return false;
               /*
               String subimg=str.toString();
               parseB();
               fig.imageOperator.string2subImg(xo, yo, we, he, subimg);
               */
               fig.imageOperator.subImgDraw(xo, yo, we, he, pix);
            }
            if(!iq.rString(")")) return false;
        	
        }
        else{
        	return false;
        }
        if(!iq.rString(")")) return false;
        afig=fig;
        fig.imageOperator.name=name;
        fig.updateImage();
//        fig.sendBase();

        return true;
    }
    public void dmy()
    {
        int x=0;
    }
    public void parseB()
    {
        int x;
        while(iq.rString(" ")){
            x=0;
        };
    }
    public boolean parseRainbowRectangle()
    {
        parseB();
        if(!iq.rString("rainbowrect(")) return false;
        FillRainbowRect fig=new FillRainbowRect(canvas);
                 if(!parseAttrib(fig));
       if(!rNumPair()) return false;
        int offX=x1; int offY=y1;

        if(!iq.rString(",")) return false;
        if(!rNumPair()) return false;
        int x2=x1; int y2=y1;
        fig.offX=offX; fig.offY=offY;
        fig.x2=x2;     fig.y2=y2;

        parseB();
        if(!iq.rString(")")) return false;
        afig=fig;
        return true;
    }
    public boolean parsePoints(AFigure fig)
    {
       int offX, offY;
       if(!rNumPair()) return false;
        offX=x1; offY=y1;
        fig.offX=x1; fig.offY=y1;

   //     message.appendText(""+offX+","+offY+"...\n");

        while(rNumPair()){
            ((ALines)fig).addPoint(x1,y1);
//            message.appendText(" "+x1+","+y1+"\n");
        }
        parseB();
//        message.appendText(")\n");
        if(!iq.rString(")")) return false;
        return true;
  }
    public boolean parseRainbowPolygon()
    {
        int offX, offY;
        parseB();
        if(!iq.rString("rainbowpolygon(")) return false;
        RainbowPolygon fig=new RainbowPolygon(canvas);
        fig.stop();
 //       message.appendText("parsing lines(");
        if(!parseAttrib(fig));
        if(!parsePoints(fig)) return false;
   //     fig.start();
        afig=fig;
        afig.start();
        return true;
   }
    public boolean parseFillOval()
    {
        parseB();
        if(!iq.rString("filloval(")) return false;
        FillOval fig=new FillOval(canvas);
          if(!parseAttrib(fig));
      if(!rNumPair()) return false;

        int offX=x1; int offY=y1;
        if(!iq.rString(",")) return false;
        if(!rNumPair()) return false;
        int x2=x1; int y2=y1;
        fig.offX=offX; fig.offY=offY;
        fig.x2=x2;     fig.y2=y2;

        parseB();
        if(!iq.rString(")")) return false;
        afig=fig;
        return true;
    }
    public boolean parseFillRect()
    {
        parseB();
        if(!iq.rString("fillrect(")) return false;
        FillRect fig=new FillRect(canvas);
                 if(!parseAttrib(fig));
       if(!rNumPair()) return false;
        int offX=x1; int offY=y1;

        if(!iq.rString(",")) return false;
        if(!rNumPair()) return false;
        int x2=x1; int y2=y1;
        fig.offX=offX; fig.offY=offY;
        fig.x2=x2;     fig.y2=y2;

        parseB();
        if(!iq.rString(")")) return false;
        afig=fig;
        return true;
    }
    public boolean parseDepth(AFigure fig)
    {
        parseB();
        if(!iq.rString("depth(")) return false;
        Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(")")) return false;
        fig.depth=c.intValue();

        return true;

    }
    public boolean parseColor(AFigure fig)
    {   parseB();
        if(!iq.rString("color(")) return false;
        Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(")")) return false;
        fig.color=new Color(c.intValue());

        return true;

    }
    public boolean parseStep(AFigure fig)
    {   parseB();
        if(!iq.rString("step(")) return false;
        Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(")")) return false;
        fig.step=c.intValue();

        return true;

    }
    public boolean attribElement(AFigure fig)
    {
         if(parseColor(fig)) return true;
         if(parseDepth(fig)) return true;
         if(parseWidth(fig)) return true;
         if(parseFont(fig))  return true;
         if(parseStep(fig)) return true;
         return false;
    }
    public boolean parseAttrib(AFigure fig)
    {
        parseB();
        if(!iq.rString("attrib(")) return false;

        // message.appendText("parsing attrib(");

        while(attribElement(fig)) dmy();

        parseB();
        // message.appendText(")\n");
        if(!iq.rString(")")) return false;

        return true;
    }
    public boolean parsePolygon()
    {
        int offX, offY;
        parseB();
        if(!iq.rString("polygon(")) return false;
        APolygon fig=new APolygon(canvas);

        // message.appendText("parsing polygon(");

        if(!parseAttrib(fig));
        if(!parsePoints(fig)) return false;
        afig=fig;
        return true;
    }
    public JTextArea message;
    public boolean parseText()
    {

        parseB();
        if(!iq.rString("text(")) return false;
        Text fig=new Text(canvas);
           if(!parseAttrib(fig));
    if(!rNumPair()) return false;
        int offX=x1; int offY=y1;
        fig.offX=x1; fig.offY=y1;

        if(!iq.rString(",")) return false;
        StringBuffer str=iq.rStrConst();
        if(str==null) return false;

//
//*            byte[] sjisCode = new byte[str.length()];
//*            (str.toString()).getBytes(0,str.length(),sjisCode,0);
            // S-JIS Code ‚ð Unicode ‚É•ÏŠ·
//            char[] uniCode = ShiftJISStringToJavaString.convertAll(sjisCode);
//

//        fig.text=new StringBuffer(String.valueOf(uniCode));
          fig.text=new StringBuffer(String.valueOf(str));

        parseB();
        if(!iq.rString(")")) return false;

        fig.font=canvas.getFont();
        fig.fmetrics=canvas.getFontMetrics(fig.font);

        afig=fig;
        return true;
    }
    public boolean parseFree()
    {
        parseB();
        if(!iq.rString("free(")) return false;
        AFreeHandCurve fig=new AFreeHandCurve(canvas);
        if(!parseAttrib(fig));
        if(!parsePoints(fig)) return false;
        afig=fig;
        return true;
    }
    public boolean parseAnOval()
    {
        parseB();
        if(!iq.rString("oval(")) return false;
        AnOval fig=new AnOval(canvas);
        if(!parseAttrib(fig));
        if(!rNumPair()) return false;

        int offX=x1; int offY=y1;
        if(!iq.rString(",")) return false;
        if(!rNumPair()) return false;
        int x2=x1; int y2=y1;
        fig.offX=offX; fig.offY=offY;
        fig.x2=x2;     fig.y2=y2;

        parseB();
        if(!iq.rString(")")) return false;
        afig=fig;
        return true;
    }
    public boolean parseARectangle()
    {
        parseB();
        if(!iq.rString("rectangle(")) return false;
        ARectangle fig=new ARectangle(canvas);
                 if(!parseAttrib(fig));
       if(!rNumPair()) return false;
        int offX=x1; int offY=y1;

        if(!iq.rString(",")) return false;
        if(!rNumPair()) return false;
        int x2=x1; int y2=y1;
        fig.offX=offX; fig.offY=offY;
        fig.x2=x2;     fig.y2=y2;

        parseB();
        if(!iq.rString(")")) return false;
        afig=fig;
        return true;
    }
    public boolean parseLines()
    {
        int offX,offY;
        parseB();
        if(!iq.rString("lines(")) return false;
         ALines fig=new ALines(canvas);
        if(!parseAttrib(fig));
        // message.appendText("parsing lines(");
        if(!parsePoints(fig)) return false;
        afig=fig;
        return true;
    }
    public AFigure afig;
    public FigCanvas canvas;
    public boolean parseALine()
    {

        parseB();
        if(!iq.rString("line(")) return false;
        ALine fig=new ALine(canvas);
        if(!parseAttrib(fig));

        // message.appendText("parsing line(");

        if(!rNumPair()) return false;
        int offX=x1; int offY=y1;


        parseB();
        if(!iq.rString(",")) return false;
        if(!rNumPair()) return false;
        int x2=x1; int y2=y1;
        fig.offX=offX; fig.offY=offY;
        fig.x2=x2;     fig.y2=y2;

        parseB();
        if(!iq.rString(")")) return false;
        afig=fig;

        // message.appendText(""+offX+","+offY+","+x2+","+y2+")\n");

        return true;
    }
    public boolean parseFig()
    {
        if( parseALine()           ||
            parseLines()           ||
            parseCurve()           ||
            parseARectangle()      ||
            parseFillRect()        ||
            parseRainbowRectangle()||
            parseAnOval()          ||
            parseFillOval()        ||
            parseFree()            ||
            parseText()            ||
            parsePolygon()         ||
            parseFigures()         ||
            parseRainbowPolygon()  ||
            parseImage()           ||
            parseClickableBoxURL() ||
            parseClickableBoxExec() ||
            parseLoadText()
            )
            {
                afig.selected=false;
                afig.step=-10;
                afig.showhide=true;
                afig.start();
                return true;
            }
        return false;
    }
    public boolean rNumPair()
    {
        parseB();
        Integer x=iq.rInteger();
        if(x==null) return false;
        x1=x.intValue();
        parseB();
        if(!iq.rString(",")) return false;
        parseB();
        Integer y=iq.rInteger();
        if(y==null) return false;
        y1=y.intValue();
        return true;
    }
    public int y1;
    public int  x1;
    public synchronized boolean parseFigures()
    {
         Figures f=new Figures(canvas);
         parseB();
         if(!iq.rString("figures(")) return false;
//         int x=1;
         while(parseFig()){
            f.add(afig);
         };
         parseB();
         figs=f;
         if(!iq.rString(")")) return false;
         return true;
    }
    public ParseFig(InputQueue q, FigCanvas c, JTextArea m)
    {
        iq=q;
        canvas=c;
        message=m;
        this.applications=new Hashtable();
    }
    public Figures figs;
    public InputQueue iq;
}

