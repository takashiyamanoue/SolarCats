/*
NetPaint

     by T. Yamanoue,
     Kyushu Institute of Technology, Japan,
     Aug.1, 1997


   A Paint tool for the Internet.
   
   Drawing tool on a Web brouser.
   A Co-operative drawing tool.
   Drawing a paint on the Internet by linking parts
   
   
*/
package application.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.io.DataOutputStream;

import nodesystem.TemporalyText;

public class AFigure extends java.lang.Object
{

    public void actWhenEditing()
    {
    }

    public void actWhenPlaying()
    {
    }
    
    public int getXmin(){
    	return offX;
    }
    public int getYmin(){
    	return offY;
    }
    public int getXmax(){
    	return 0;
    }
    public int getYmax(){
    	return 0;
    }

    public void setX(int x){
    	this.offX=x;
    }
    
    public void setY(int y){
    	this.offY=y;
    }
    
    public static boolean playModeFlag;

    public void setPlayMode()
    {
        playModeFlag=true;
    }

    public void setEditMode()
    {
        playModeFlag=false;
    }

    public void setPlayMode(boolean x)
    {
        playModeFlag=x;
    }

    public boolean isPlaying()
    {
        return playModeFlag;
    }

    public void immediateActionAfterSetStep()
    {
    }

    public void setFont(Font f)
    {
    }

    public void drawLineW(Graphics g,int x1, int y1, int x2, int y2, int w)
    {
        Equation2D eq=new Equation2D((double)x1,(double)y1,(double)x2,(double)y2);
        double dx=(eq.vdx()*w)/2.0;
        double dy=(eq.vdy()*w)/2.0;

        int xlist[];
        int ylist[];
        int i,n;

        xlist= new int[4];
        ylist= new int[4];
        
        xlist[0]=xView((int)(x1+dx));  ylist[0]=yView((int)(y1+dy));
        xlist[1]=xView((int)(x1-dx));  ylist[1]=yView((int)(y1-dy));
        xlist[2]=xView((int)(x2-dx));  ylist[2]=yView((int)(y2-dy));
        xlist[3]=xView((int)(x2+dx));  ylist[3]=yView((int)(y2+dy));
        
//        if(n>1){
            Color cc=g.getColor();
            g.setColor(color);
            g.fillPolygon(xlist,ylist,4);
            g.setColor(cc);
//        }

    }

    int lineWidth;

    public String str2saveable(StringBuffer s)
    {
        String sx="";
        int i=0;
        int len=s.length();
        while(i<len){
            char c=s.charAt(i);
            if(c=='\''){
                sx=sx+'\\'; sx=sx+c; i++; }
            else
            if(c=='\"'){
                sx=sx+'\\'; sx=sx+c; i++; }
            else
            if(c=='\\'){sx=sx+'\\'; sx=sx+c; i++;
                        c=s.charAt(i); sx=sx+c; i++; }
            else       { sx=sx+c; i++; }
//            System.out.println(sx);
        }
/*
        // Unicode ‚ð S-JIS Code ‚É•ÏŠ·
	    byte[]  sjisCode = JavaStringToShiftJISString.convertAll( sx.toCharArray());
        String rtn=new String(sjisCode,0);

        return rtn;
 */
        return sx;
    }
    public int yLogical(int y)
    {
//        return canvas.vScrollBar.getValue()+y;
          return y;
    }
    public int xLogical(int x)
    {
//        return canvas.hScrollBar.getValue()+x;
          return x;
    }
    public int yView(int y)
    {
//        return y-canvas.vScrollBar.getValue();
          return y;
    }
    public int xView(int x)
    {
//        return x-canvas.hScrollBar.getValue();
          return x;
    }
    public void showRC(Graphics g)
    {
        Color cx;
        Color cc=g.getColor();
        if(cc==Color.black) cx=Color.yellow;
        else cx=Color.black;
        g.setColor(cx);
        if(step==60){
            g.drawOval(
              xView((int)(dcX-4)),
              yView((int)(dcY-4)),8,8);
        }
        else
        if(step==61||step==62){
            g.drawOval(
              xView((int)(dcX-4)),
              yView((int)(dcY-4)),8,8);
            g.drawOval(
              xView((int)(dcX2-4)),
              yView((int)(dcY2-4)),8,8);
        }
        g.setColor(cc);

    }
    double angle;
    double ddY;
    double ddX;
    public void magrotateP(double cx, double cy, double r, double t, double x, double y)
    {
         double wx, wy, wx2,wy2;
        wx=x-cx; wy=y-cy;
        wx2=wx*(Math.cos(t))+wy*(Math.sin(t));
        wy2=-wx*(Math.sin(t))+wy*(Math.cos(t));
        newdX=wx2*r+cx; newdY=wy2*r+cy;
    }
    double newdY;
    double newdX;
    double magRatioX;
    double magRatioR;
    double magRatio;
    public AFigure copyThis()
    {
        return copyThis2(new AFigure(canvas));
    }
    public AFigure copyThis2(AFigure f)
    {
        f.color=color;
        f.dcX=dcX; f.dcX2=dcX2;
        f.dcY=dcY; f.dcY2=dcY2;
        f.depth=depth; f.direction=direction;
        f.magnification=magnification;
        f.offX=offX; f.offY=offY;
        f.selected=selected;
        f.showhide=showhide;
        f.step=step;
        return f;
    }
    public boolean isIntheArea(int  wx1, int wy1, int wx2, int wy2)
    {
        if(isIntheRectangle(offX,offY,wx1,wy1,wx2,wy2)) return true;
        return false;
    }
    public boolean isIntheRectangle(int x, int y, int rx1, int ry1, int rx2, int ry2)
    {
        if(rx1>rx2) return false;
        if(ry1>ry2) return false;
        if(x<rx1) return false;
        if(rx2<x) return false;
        if(y<ry1) return false;
        if(ry2<y) return false;
        return true;
    }
    public void selectArea(int x1, int y1, int x2, int y2)
    {
        if(isIntheArea(x1,y1,x2,y2)){
                step=10;
                selected=true;
                canvas.ftemp.add(this);
                canvas.fs.remove(this);
                this.start();
        }
    }
    double dcY2;
    double dcX2;
    double dcY;
    double dcX;
    public void nextNewFig()
    {
           step=-10;
           this.selected=false;
           showhide=true;
           canvas.fs.add(this);
           canvas.ftemp.remove(this);
           this.start();
           Class cl=this.getClass();
           AFigure f=null;
           try{
               f=(AFigure)(cl.newInstance());
           }
           catch(InstantiationException e){System.out.println(e);}
           catch(IllegalAccessException e){System.out.println(e);}
//           f.color=canvas.editdispatch.color;
           canvas.editdispatch.changeColor();
           canvas.editdispatch.changeWidth();
           f.canvas=canvas;
           f.init();
           f.newFig();
   //        f.color=canvas.gui.CurrentColor.getBackground();
           canvas.ftemp.add(f);
    }
    public boolean isEditing()
    {
        if(step>=0) return true;
        return false;
    }
    public void newFig()
    {
    }
    public void start()
    {
    }
    public void stop()
    {
    }
    public boolean save(TemporalyText outS)
    {
        return true;
    }
   // public boolean saveHeader(DataOutputStream outs, String fname)
    public boolean saveHeader(TemporalyText outs, String fname)
    {
        if(!strmWrite(outs,fname+"(\n")) return false;
        if(!saveAttrib(outs)) return false;
        return true;
    }
    //public boolean saveAttrib(DataOutputStream outs)
    public boolean saveAttrib(TemporalyText outs)
    {
        if(color==null) color=Color.black;
        if(!strmWrite(outs,"attrib(\n")) return false;
        if(!strmWrite(outs,"color("+color.getRGB()+")\n")) return false;
        if(!strmWrite(outs,"depth("+depth+")\n")) return false;
        if(!strmWrite(outs,"width("+lineWidth+")\n")) return false;
        if(!strmWrite(outs,"step("+step+")\n")) return false; 
        if(!strmWrite(outs,")\n")) return false;
        return true;
    }
    double magnification;
    double direction;
    public void init()
    {
         offX=0; offY=0;
         selected=false;
         depth=0;
         int width=1;
         if(canvas!=null) {
            color=canvas.gui.colorSelectButton.getBackground();
            String ws=canvas.gui.lineWidthSelectButton.getText();
            try{
               width=(new Integer(ws)).intValue();
            }
            catch(Exception e){
                width=1;
            }
            lineWidth=width;    
         }
         else {
            color=Color.black;
            lineWidth=1;
         }
//        color=Color.black;
    }
    public int depth;
    public void movePoint(int x, int y)
    {
    }
    public boolean startModifyPoint(int x, int y)
    {
        return false;
    }
    public void endModifyPoint()
    {
    }
    public Color  color;
/*
    public boolean strmWrite(DataOutputStream outS, String s)
    */
    public boolean strmWrite(TemporalyText outS, String s)
    {
        outS.append(s);
        return true;
    }
    public boolean save(DataOutputStream outs)
    {
        return true;
    }
    public int step;
/*
      if step== -10 :  normal
         step==   0 :  editing, first  step
         step==   1 :  editing, second step
*/
    /*
    if step == -10 then normal state
            ==   0 then new
            ==   1 then setting second position in new
            ==  10 then selecting
            ==  11
    */
    public void writeMessage(String s)
    {
        canvas.gui.recordMessage(""+ ",\""+ s +"\",,,");
    }
/*
  ln_f3
     vertical line
     argument
              (x0,y0)
                 |
                 |---
                 |   |
     (x1,y1)-----------------(x2,y2)
               (x,y)

        input    x0,y0, x1,y1, x2,y2,
        output   (x,y)

*/
    public Point vLine1(double x0, double y0,
                       double x1, double y1, double x2, double y2)
 {
   double d,x,y,a,b,c;
   Equation2D eq=new Equation2D();
   Point p;
   p=null;
   eq.x1=x1; eq.y1=y1; eq.x2=x2; eq.y2=y2;
   if(!eq.edges2abc()) return p;
   p=new Point(0,0);
   a=eq.a; b=eq.b; c=eq.c;
   d=a*a+b*b;

   x=(b*b*x0+a*c-a*b*y0)/d;  p.x=(int)x;
   y=(a*a*y0+b*c-a*b*x0)/d;  p.y=(int)y;
   return p;
 }
    public boolean isOntheLine(int xr, int yr, int x1, int y1, int x2, int y2)
    {
        double w1,w2,d,dxr,dyr,dx1,dx2,dy1,dy2;
        double error=3.0;
        Point p;
        p=vLine1((double)xr,(double)yr,(double)x1,(double)y1,
                                     (double)x2,(double)y2);
        if(p==null) return false;
        if((p.x-x1)*(p.x-x2)>0) return false;
        if((p.y-y1)*(p.y-y2)>0) return false;
        d=Math.sqrt(((double)xr-(double)p.x)*((double)xr-(double)p.x)
                +   ((double)yr-(double)p.y)*((double)yr-(double)p.y));

        if(d>error) return false;
        return true;

    }
    public boolean selected;
    public AFigure()
    {
        init();
    }
    public boolean showhide;
    public FigCanvas canvas;
    public AFigure(FigCanvas c)
    {
         this.canvas=c;
         init();
    }
    public void drawTemp(Graphics g)
    {
    }
    public void showEdge(Graphics g,int x, int y)
    {
        Color cc=g.getColor();
        if(cc!=Color.black) g.setColor(Color.black);
        else                g.setColor(Color.yellow);
        g.fillRect(x-2,y-2,4,4);
        g.setColor(Color.blue);
        g.drawRect(x-2,y-2,4,4);
        g.setColor(cc);
    }
    public int offY;
    public int offX;
    public void draw(Graphics g)
    {
        if(selected) drawTemp(g);
    }
    public boolean isPointed(int x, int y, int x2, int y2)
    {
        int error=3;
        if(!(Math.abs(x-x2)<error)) return false;
        if(!(Math.abs(y-y2)<error)) return false;
        return true;
    }
    public boolean isSelected(int x, int y)
    {
        return false;
    }
    public void mouseEnter(int x, int y)
    {
  //      offX=x; offY=y;
  //      showhide=true;
    }
    public void mouseExit()
    {
      //  showhide=false;
    }
    public void mouseDrag(int x, int y)
    {
    }
    public void mouseMove(int x, int y)
    {
    }
    public void mouseUp(int x, int y)
    {
    }
    public void mouseDown(int x, int y)
    {
        if(!isSelected(x,y)) return;
        if(this.isPlaying()){
            actWhenPlaying();
            return;
        }
        actWhenEditing();
        if(step==-10){
              selected=true;
              step=10;
              canvas.ftemp.add(this);
              canvas.fs.remove(this);
        }
        
    }
    public void keyDown(int key)
    {
    }

}

