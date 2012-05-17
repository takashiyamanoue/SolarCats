package application.draw;
import java.awt.Color;

import nodesystem.*;
import controlledparts.*;

public class ParseDrawEvent extends ParseEvent
{
    byte[] binary;
    public void setBinary(byte[] d){
    	binary=d;
    }
    public boolean parseFig(DrawFrame f)
    {
        parseB();
        if(!iq.rString("fig.")) return false;
        if(iq.rString("start.")){
            f.editdispatch.clear();
        }
        else
        if(iq.rString("end.")){
            f.editdispatch.fs=f.canvas.fs;
            f.repaint();
        }
        else{
            ParseFig pf=new ParseFig(iq,f.canvas,null);
            if(!pf.parseFig()) return false;
            AFigure fig=pf.afig;
            f.canvas.fs.add(fig);
        }
        return true;

    }

    public boolean parseState()
    {
        parseB();
        if(!iq.rString("stat(")) return false;
        if(!rNumPair()) return false;
        if(!iq.rString(")")) return false;
        StateContainer c=(StateContainer)(gui.states.elementAt(x1));
        c.setState(y1);
        return true;
    }

    public DrawFrame gui;
    public boolean parseDFigEvent(FigSelectFrame f)
    {
        parseB();
        if(!iq.rString("dfig.")) return false;
        if(parseButtonEvent(f)) return true;
        return false;
    }
    public boolean parseDFSizeEvent(FontSizeSelectFrame f)
    {
        parseB();
        if(!iq.rString("fsize.")) return false;
        if(parseButtonEvent(f)) return true;
        return false;
    }
    public boolean parseDColorEvent(ColorSelectFrame f)
    {
        parseB();
        if(!iq.rString("dcolor.")) return false;
        if(parseButtonEvent(f)) return true;
        return false;
    }
    public boolean parseDMovieEvent(MovieFrame f)
    {
        parseB();
        if(!iq.rString("dmovie.")) return false;
        if(parseButtonEvent(f)) return true;
        return false;
    }
    public boolean parseImageRec(DrawFrame f)
    {
        parseB();
        boolean bgflag;
//        iq.printLine();
        if(iq.rString("img(")) {
           bgflag=false;
        }
        else
        if(iq.rString("bgimg(")){
        	bgflag=true;
        }
        else{
        	return false;
        }
        parseB();
        if(!iq.rString("name(")) return false;
        StringBuffer str=iq.rStrConst();
        if(!iq.rString(").")) return false;  //name(<string>)
        if(str==null) return false;
        String name=str.toString();


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
        ImageOperator img=f.imageManager.getImageOperator(name);
        if(iq.rString("create.")){
           if(img==null){
               isFirstTime=true;
               img=new ImageOperator(f.canvas,gui.communicationNode.getNodeSettings());
               f.imageManager.setImageOperator(name,img);
           }
           if(offx==0 && offy==0 && w==10)
           {
//            System.out.println("xxx");
           }
           if(!iq.rString("color(")) return false;
           Integer ci=iq.rInteger();
           if(!iq.rString(")")) return false;  // color(<number>)
           if(hi==null) return false;
           int cx=ci.intValue();
           Color color=new Color(cx);

//
           if(isFirstTime){
               img.createNewImage(w,h);
               try{
                  img.fillRect(0,0,w,h,color);
               }
               catch(NullPointerException e){
                  System.out.println("img-create");
               }
              FigCanvas fc=f.canvas;
              MyImage newfig=new MyImage(fc,img, this.getNodeSettings());
              img.setMyImage(newfig);
              newfig.imageName=name;
              newfig.newFig();
              newfig.offX=offx;
              newfig.offY=offy;
              if(bgflag){
                  fc.fs.add(newfig);
                  newfig.depth=25;
                  fc.fs.setStep(-10);
//                  f.depthIndicator.setValue(newfig.depth);
            	  
              }
              else{
                 fc.ftemp.add(newfig);
                 newfig.depth=0;
                 f.depthIndicator.setValue(newfig.depth);
              }
           }
           else{
              img.isCreatingImage=true;
              img.imageUpdate(null,0,offx,offy,w,h);
           }
           parseB();
           
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

               img=gui.imageManager.getImageOperator(name);
               if(img==null) return false;
               img.fillRect(offx,offy,w,h,color);
               if(!iq.rString("-")) break;        // -
               offx=offx+w;
            }
            parseB();
            if(!iq.rString(")")) return false;     // img( .... )
            return true;
       }
        else
        if(iq.rString("binary")){
			img=gui.imageManager.getImageOperator(name);
			if(img==null) return false;
			img.setBinaryImage(offx,offy,w,h,this.binary);
//		   parseB();
		   if(!iq.rString(")")) return false;     // img( .... )
		   return true;        	
        }
		else
		if(iq.rString("jpeg")){
				img=gui.imageManager.getImageOperator(name);
				if(img==null) return false;
				img.setJpegImage(offx,offy,w,h,this.binary);
				parseB();
			   if(!iq.rString(")")) return false;     // img( .... )
			   return true;			
		}
		else
		if(iq.rString("sImg(")){
				StringBuffer sb=iq.rStrConst();
				if(sb==null) return false;
				if(!iq.rString(")")) return false;
				img=gui.imageManager.getImageOperator(name);
				if(img==null) return false;			
				img.setSlowFineImage(sb.toString(), offx, offy, w, h, this.binary);
				parseB();
				if(!iq.rString(")")) return false; // img( ...)
				return true;
			}
        else
        if(iq.rString("bmp.")){
           str=iq.rStrConst();
           if(str==null) return false;

           img=gui.imageManager.getImageOperator(name);
           if(img==null) return false;
           img.string2subImg(offx,offy,w,h,str.toString());
           parseB();
           if(!iq.rString(")")) return false;     // img( .... )

           return true;
        }
        else return false;
//        gui.canvas.repaint();
//        return true;
    }
    public boolean parseFigs(DrawFrame f)
    {
        parseB();
        if(!iq.rString("cfigs(")) return false;
 //       message.appendText("reading figs.\n");
        f.canvas.fs.stop();
        ParseFig pf=new ParseFig(iq,f.canvas,null);
        if(!pf.parseFigures()) return false;
        parseB();
        if(!iq.rString(")")) return false;
 //       message.appendText("reading figs done.\n");
        f.editdispatch.fs=pf.figs;
        f.canvas.fs=pf.figs;
        f.repaint();
        return true;

    }
    public boolean parseTFigs(DrawFrame f)
    {
        parseB();
        if(!iq.rString("tfigs(")) return false;
 //       message.appendText("reading figs.\n");
        f.canvas.fs.stop();
        ParseFig pf=new ParseFig(iq,f.canvas,null);
        if(!pf.parseFigures()) return false;
        parseB();
        if(!iq.rString(")")) return false;
 //       message.appendText("reading figs done.\n");
        f.editdispatch.ftemp=pf.figs;
        f.canvas.ftemp=pf.figs;
        f.repaint();
        return true;

    }
    public boolean parseDWFrameEvent(WidthSelectFrame f)
    {
        parseB();
        if(!iq.rString("dwidth.")) return false;
        if(parseButtonEvent(f)) return true;
        return false;
    }
    public boolean parseEvent()
    {
        boolean rtn;
        if( parseFcEvent(gui)                         ||
            this.parseButtonEvent(gui)                ||
            this.parseSliderEvent(gui)                ||
            this.parseTextEvent(gui)                  ||
            parseDWFrameEvent(gui.widthSelectFrame)   ||
            parseDColorEvent(gui.colorSelectFrame)    ||
            parseDFigEvent(gui.figSelectFrame)        ||
            parseDFSizeEvent(gui.fontSizeSelectFrame) ||
            parseDTextEditEvent(gui.textEditFrame)    ||
            parseDMovieEvent(gui.movieFrame)          ||
            parseFileEvent(gui.fileFrame)             ||
            parseLoadEvent(gui.loadFileFrame)         ||
            parseFig(gui)                             ||
            parseFigs(gui)                            ||
            parseTFigs(gui)                           ||
            parseImageRec(gui)    ||
            parseFocus(gui)    ||
            parseMessage(gui)  ||
            parseState()       ||
            parseFrameEvent(gui)
            )
            rtn= true;
        else rtn= false;
        return rtn;

    }

    public ParseDrawEvent(DrawFrame f, InputQueue q)
    {
        iq=q;
        gui=f;
        me=null;
//        start();
    }
    public NodeSettings getNodeSettings(){
    	return this.gui.getNodeSettings();
    }
}