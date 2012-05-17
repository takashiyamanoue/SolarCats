package application.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

import nodesystem.TemporalyText;

public class TextBox extends AFigElement
{
    public String attribute;
    int x1, y1;
    int width, height;
    
    public String getAttribute()
    {
        return attribute;
    }

    public void setAttribute(String a)
    {
        attribute=a;
    }

    public void draw(Graphics g, int x, int y)
    {
        x1=x; y1=y;
        this.draw(g);
    }

    public boolean isInside(int x, int y)
    {
        setSize();
        if(x<x1) return false;
        if(x1+width<x) return false;
        if(y<y1) return false;
        if(y1+height<y) return false;
        return true;
    }

    public void setLabel(String l)
    {
        label=l;
    }

    public void setColor(Color c)
    {
        color=c;
    }

    public void setSize()
    {

        height=fmetrics.getMaxAscent()+fmetrics.getMaxDescent()+2;
        width=fmetrics.stringWidth(label)+2;
    }

    public void setSize(int w, int h)
    {

        height=w;
        width=h;
    }

    public FigCanvas canvas;

    public void setCanvas(FigCanvas c)
    {
        canvas=c;
    }

    public FontMetrics fmetrics;

    public Font font;

    public boolean getCheckState()
    {
        return isChecked;
    }

    public void setCheckState(boolean b)
    {
        isChecked=b;
    }

    public boolean isChecked;

    public void setPosition(int x, int y)
    {
        x1=x; y1=y;
    }

    public String label;

    public TextBox(String l, FigCanvas c)
    {
        label=l;
        canvas=c;
        setFont(new Font("Dialog", Font.BOLD, 8));
    }
	String charSet;
	public void setCharSet(String c){
		this.charSet=c;
	}
    public void immediateActionAfterSetStep()
    {
         if(step==50) // modifying
        {
            
            this.canvas.gui.openTextEditor(this.canvas.editdispatch);
            String s=""+this.text.toString();
            this.canvas.gui.textEditFrame.getTextArea().setText(s);
             selected=false;
            canvas.ftemp.remove(this);
//            canvas.editdispatch.select();
            step=-10;
            return;
        }
    }

    public boolean saveAttrib(TemporalyText outs)
    {
         if(color==null) color=Color.black;
        if(!strmWrite(outs,"attrib(\n")) return false;
        if(!strmWrite(outs,"color("+color.getRGB()+")\n")) return false;
        if(!strmWrite(outs,"depth("+depth+")\n")) return false;
        if(!strmWrite(outs,"width("+lineWidth+")\n")) return false;
        if(!strmWrite(outs,"font(\""+font.getName()+"\","+font.getStyle()+","+font.getSize()+")\n")) return false;
        if(!strmWrite(outs,")\n")) return false;
        return true;
   }

    public void setFont(Font f)
    {
        canvas.setFont(f);
        this.font=f;
        fmetrics=canvas.getFontMetrics(font);
    }

    public void setText(String s)
    {
        text=new StringBuffer(s);
    }
    public void setText(String s, String c)
    {
    	this.charSet=c;
    	String sc=null;
    	try{
        	 sc=new String(s.getBytes(),this.charSet);
    	}
    	catch(Exception e){
    		text=new StringBuffer(s);
    		return;
    	}
        text=new StringBuffer(sc);
    }
    public void drawString2(Graphics g,StringBuffer s)
    {
        int height;
        int lines;
        int n;
        int p1,p2;
        int maxc;
        int maxl;
        int x2,y2;
        char buff[];
        buff= new char[256];
        height=fmetrics.getMaxAscent()+fmetrics.getMaxDescent();
        lines=0;
        n=0;
        p1=0; p2=0;
        maxl=0; maxc=0;
        String aLine="";
        g.drawRect(xView(offX), yView(offY+height), width, height);
        while(s.length()>n){
            if(s.charAt(n)=='\n'|| s.charAt(n)==(char)13){
                g.drawString(aLine,xView(offX),yView(offY+height*lines+fmetrics.getMaxAscent()));
                aLine="";
                lines++;
                p1=n+1; p2=-1;
            }
            if(s.charAt(n)!='\n') aLine=aLine+s.charAt(n);
            p2++;
            n++;
        }
        g.drawString(aLine,xView(offX),yView(offY+height*lines+fmetrics.getMaxAscent()));
   }
    public int getCursorPosition(int x, int y)
    {
        if(x<xView(offX)) return 0;
        int len=text.length();
        getSize();
        if(x>xView(offX)+x2) return len;
        return (int)((((double)x-(double)(xView(offX)))/(double)x2)*len);

    }
    public int oldY;
    public int oldX;
    public void magnifyXY()
    {
//        int x1,y1,x2,y2;
        double offXw, offYw;
        offXw=oldX; offYw=oldY;
        magrotateP(dcX,dcY,magRatio,angle,(double)oldX,
                                  (double)oldY);
        offX=(int)newdX; offY=(int)newdY;
        return;

    }
    public int rightMost;
    public Point getSize(StringBuffer s)
    {
        int height;
        int lines;
        int n;
        int p1,p2;
        int maxc;
        int maxl;
        int x2,y2;
        char buff[];
        buff= new char[256];
        height=fmetrics.getMaxAscent()+fmetrics.getMaxDescent();
        lines=0;
        n=0;
        p1=0; p2=0;
        maxl=0; maxc=0;
        while(s.length()>n){ // must be corrected by using fmetrics
            if(s.charAt(n)=='\n'){
                lines++;
                if(maxc<p2) {maxl=p1; maxc=p2;} //
                p1=n+1; p2=-1;
            }
            p2++;
            n++;
        }
        if(maxc<p2) maxc=p2;
        try{
           text.getChars(maxl,maxl+maxc,buff,0);
        }
        catch(StringIndexOutOfBoundsException e){}
        x2=fmetrics.charsWidth(buff,0,maxc);

        // the following part needs an improvement.
        if(maxc>0 && x2==0) x2=maxc*height;

        y2=(lines+1)*height;
        return new Point(x2,y2);
 }
    public void drawCursor(Graphics g)
    {
       StringBuffer sb=new StringBuffer("");
       int l=text.length();
       int i;
       for(i=0;i<cursorPosition;i++) {
          if(i>l) break;
          sb.append(text.charAt(i));
       }
       Point pwh=getSize(sb);
       g.drawLine(xView(offX)+pwh.x,yView(offY)+pwh.y, xView(offX)+pwh.x, yView(offY));
    }
    public String str2saveable(StringBuffer s)
    {
        String sx="";
        int i=0;
        int len=s.length();
        while(i<len){
            char c=s.charAt(i);
//            System.out.println("c="+c+":"+(int)c);
            if(c=='\''){
                sx=sx+'\\'; sx=sx+c; i++; }
            else
            if(c=='\"'){
                sx=sx+'\\'; sx=sx+c; i++; }
            else
            if(c=='\\'){sx=sx+'\\'; sx=sx+c; i++;
//                if(i<len){
//                        c=s.charAt(i); sx=sx+c; i++;
//                }
            }
			else
			if(c=='\b'){
					sx=sx+'\\'; sx=sx+'b'; i++;
			}
			else
			if(c=='\f'){
					sx=sx+'\\'; sx=sx+'f'; i++;
			}
            else
            if((int)c==10){
                sx=sx+"\\n"; i++;
//                if(i<len){
//                  c=s.charAt(i);
//                  if((int)c==13) i++;
//                }
            }
            else
            if((int)c==13){
                sx=sx+"\\n"; i++;
//                if(i<len){
//                  c=s.charAt(i);
//                  if((int)c==10) i++;
//                  }
            }
            else
			if(c=='\t'){
				sx=sx+'\\'; sx=sx+"t"; i++;
			}
            else
            { sx=sx+c; i++; }
//            System.out.println(sx);
        }

        // Unicode ‚ð S-JIS Code ‚É•ÏŠ·
//	    byte[]  sjisCode = JavaStringToShiftJISString.convertAll( sx.toCharArray());
//        String rtn=new String(sjisCode,0);
        String rtn=sx;

        return rtn;
    }
    public AFigure copyThis()
    {
        Text t=(Text)(super.copyThis2(new Text(canvas)));
        t.text=new StringBuffer(text.toString());
        t.cursorPosition=cursorPosition;
        t.fmetrics=fmetrics;
        t.font=font;
        t.x2=x2;
        t.y2=y2;
        return (AFigure)t;
    }
    public int y2;
    public int x2;
    public void mouseMove(int xx, int yy)
    {
        int x=xLogical(xx);
        int y=yLogical(yy);
         showhide=true;
        if(step==0) // new, setting potison.
        {
            offX=x; offY=y-fmetrics.getMaxAscent();
            return;
        }
        if(step==40) // moving
        {offX=x;  offY=y; return;}
         if(step==51) // modifying, the first step
        {  return;}
        if(step==60) // magnify, the first step... setting center
        {
            dcX=x; dcY=y;
        }
        if(step==61)// magnify, the second step
        {
            dcX2=x; dcY2=y;
            return;
        }
        if(step==62)// magnify, the third step
        {
            dcX2=x; dcY2=y;
            ddX=dcX2-dcX; ddY=dcY2-dcY;
            magRatioX=Math.sqrt((dcX-dcX2)*(dcX-dcX2)+(dcY-dcY2)*(dcY-dcY2));
            magRatio=magRatioX/magRatioR;
            angle=Math.atan2(ddX,ddY);
            magnifyXY();
            return;
        }
  }
    public void mouseDown(int xx, int yy)
    {
        int x=xLogical(xx);
        int y=yLogical(yy);
        if(step==-10){
            if(isSelected(x,y)){
                if(isPlaying()) {
                    actWhenPlaying();
                    return; //
                }
                step=10;
                selected=true;
                canvas.ftemp.add(this);
                canvas.fs.remove(this);
//                this.start();
                cursorPosition=text.length();
            }
            return;
        }
        if(step==0){ // new
            offX=x; offY=y-fmetrics.getMaxAscent();
            step++; return;
        }
        if(step==1){
            nextNewFig();
            return;
        }
        if(step==10) //  selecting
        {
            if(isSelected(x,y)){
            }
            else
            {
                step=-10;
                selected=false;
                canvas.fs.add(this);
                canvas.ftemp.remove(this);
  //              this.start();
            }
            return;
        }
        if(step==40) // moving
        {
            offX=x;  offY=y;
            step=-10;
            selected=false;
            canvas.fs.add(this);
            canvas.ftemp.remove(this);
  //          this.start();
            canvas.editdispatch.select();
            return;
        }
        /*
         if(step==50) // modifying
        {
            // immidiateActionAfterSetStep()
        }
        if(step==51) // modifying, the second  step
        {
            selected=false;
            canvas.ftemp.remove(this);
            canvas.editdispatch.select();
           return;
        }
        */
//
        if(step==60) // magnifying, the first step
        {
            dcX=x; dcY=y;
            step++;
            return;
        }
        if(step==61) // magnifying, the second step
        {
            dcX2=x; dcY2=y;
            magRatioR=Math.sqrt((dcX-dcX2)*(dcX-dcX2)+(dcY-dcY2)*(dcY-dcY2));
            if(magRatioR==0.0) return;
            oldX=offX; oldY=offY;
            step++;
            return;
        }
        if(step==62) // magnifying, the third step
        {
            selected=false;
            canvas.fs.add(this);
            canvas.ftemp.remove(this);
            canvas.editdispatch.select();
            return;
        }
    }
    public void keyDown(int key)
    {
        rightMost=text.length();
       if(key==8) {
            text.setLength(text.length()-1);
            cursorPosition--;
            rightMost--;
        }
        else
        if(key==1006) { // left arrow key
           if(cursorPosition>0) cursorPosition--;
        }
        else
        if(key==1007) { // right arrow key
           if(cursorPosition<rightMost) cursorPosition++;
        }
        else
        if(key==1005) { // down arrow key
        }
        else
        if(key==1004) { // up arrow key
        }
/*
        else
        if(key==(int)'"')
        {
            text.append((char)'\\');
            text.append('"');
            cursorPosition++;
        }
        else
        if(key==(int)'\'')
        {
            text.append((char)'\\');
            text.append('\"');
            cursorPosition++;
        }
*/
        else
        {
//            System.out.println("key="+key);
            text.insert(cursorPosition,(char)key);
            cursorPosition++;
        }
     }
    public void newFig()
    {
        offX=0; offY=0;
        step=0;
        cursorPosition=0;
        text=new StringBuffer("");
        showhide=false;
        font=canvas.gui.fontSizeSelectButton.getFont();
        canvas.setFont(font);
        fmetrics=canvas.getFontMetrics(font);
        selected=true;

        writeMessage("NetText done\n");
    }
  //  public boolean save(DataOutputStream outS)
  public boolean save(TemporalyText outS)
  {
        if(!saveHeader(outS,"text")) return false;
        String saving=""+offX+","+offY+","
                             +"\""+str2saveable(text)+"\")\n";
        if(!strmWrite(outS, ""+saving)) return false;
        return true;
    }
    public boolean isSelected(int x, int y)
    {
         getSize();
 //        writeMessage("Text.isSelected, x,y="+x+","+y+"\n");
 //        writeMessage(" ...offX,offY="+offX+","+offY+"\n");
 //        writeMessage(" ...x2,y2="+x2+","+y2+"\n");
         if(x<offX) return false;
         if(x>(offX+x2)) return false;
         if(y<offY) return false;
         if(y>(offY+y2)) return false;
         return true;
    }
    public TextBox()
    {
    }
    public TextBox(FigCanvas c)
    {
        canvas=c;
        super.init();
        x2=20; y2=5;
        text= new StringBuffer("");
        cursorPosition=0;
        rightMost=0;
    }
    public void getSize()
    {
        Point pwh=getSize(text);
        x2=pwh.x;
        y2=pwh.y;
    }
    public void drawTemp(Graphics g)
    {
//        g.setFont(font);
        /*
        g.drawString(text.toString(),
          xView(offX),
          yView(offY+fmetrics.getMaxAscent()));
          */
        drawString2(g,text);
        getSize();
        g.drawRect(xView(offX),yView(offY),
                   x2,y2);
        drawCursor(g);
        showRC(g);
    }
    public void draw(Graphics g)
    {
        if(showhide){
          Color cc=g.getColor();
          g.setColor(color);
          g.setFont(font);
          fmetrics=canvas.getFontMetrics(font);
          if(isEditing()) drawTemp(g);
          else
          /*
            g.drawString(text.toString(),
               xView(offX),
               yView(offY+fmetrics.getMaxAscent()));
               */
          drawString2(g,text);
          g.setColor(cc);
        }
    }
    public int cursorPosition;
    public StringBuffer text;

}
