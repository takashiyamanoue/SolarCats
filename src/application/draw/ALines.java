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
import java.awt.Graphics;
import java.awt.Point;

import nodesystem.TemporalyText;

public class ALines extends AFigElement
{
    public void drawLinesX(Graphics g)
    {
        PointList pt;
        int x1,y1,x2,y2;
        pt=plist;
        x1=0;y1=0;
        if(lineWidth>1)
              g.fillOval(xView(offX-lineWidth/2),yView(offY-lineWidth/2),
                         lineWidth, lineWidth);
        while(pt!=null){
            x2=pt.x; y2=pt.y;
            /*
            g.drawLine(xView(x1+offX),
                       yView(y1+offY),
                       xView(x2+offX),
                       yView(y2+offY));
            */
            if(lineWidth<=1) {
              g.drawLine(xView(x1+offX),yView(y1+offY),
                         xView(x2+offX),yView(y2+offY));
            }
            else
            {
              drawLineW(g,x1+offX, y1+offY, x2+offX, y2+offY,lineWidth);
              g.fillOval(xView(offX+x2-lineWidth/2),yView(offY+y2-lineWidth/2),
                         lineWidth, lineWidth);
            }
            x1=x2; y1=y2;
            pt=pt.next;
        }
        return;

   }

    public AFigure copyThis2(ALines f)
    {
        ALines ls=(ALines)(super.copyThis2(f));
        ls.angle=angle;
        ls.ddX=ddX;
        ls.lastXY=lastXY;
        ls.magRatio=magRatio;
        ls.magRatioR=magRatioR;
        ls.magRatioX=magRatioX;
        ls.newdX=newdX;
        ls.newdY=newdY;
        ls.x2=x2;
        ls.y2=y2;
        ls.xy1=xy1;
        ls.xy2=xy2;
        ls.copy(this);
        return (AFigure)ls;
   }
    public AFigure copyThis()
    {
        return (AFigure)(this.copyThis2(new ALines(canvas)));
    }
    public boolean isIntheArea(int wx1,int wy1,int wx2,int  wy2)
    {
        PointList pt,ptc;
        if(!super.isIntheArea(wx1,wy1,wx2,wy2)) return false;
        pt=plist;
        while(pt!=null){
            if(!isIntheRectangle(offX+pt.x,offY+pt.y,
                                 wx1,wy1,wx2,wy2)) return false;
            pt=pt.next;
        }
        return true;

    }
    public ALines thisCopy;
    public void magnifyP(double cx, double cy, double r, double x, double y)
    {
        double wx, wy;
        wx=x-cx; wy=y-cy;
        newdX=wx*r+cx; newdY=wy*r+cy;
    }
    public void magnifyXY()
    {
        PointList pt,ptc;
//        int x1,y1,x2,y2;
        double offXw, offYw;
        offXw=thisCopy.offX; offYw=thisCopy.offY;
        pt=plist;
        ptc=thisCopy.plist;
        while(ptc!=null){
//            x2=ptc.x; y2=ptc.y;
//            x2=pt.x; y2=pt.y;
            double wx=(double)(ptc.x)+offXw;
            double wy=(double)(ptc.y)+offYw;
//            magnifyP(dcX,dcY,magRatio,wx,wy);
            magrotateP(dcX,dcY,magRatio,angle,wx,wy);
            pt.x=(int)newdX;
            pt.y=(int)newdY;
            pt=pt.next; ptc=ptc.next;
        }
//        magnifyP(dcX,dcY,magRatio,(double)(thisCopy.offX),
//                                  (double)(thisCopy.offY));
        magrotateP(dcX,dcY,magRatio,angle,(double)(thisCopy.offX),
                                  (double)(thisCopy.offY));
        offX=(int)newdX; offY=(int)newdY;
        pt=plist;
        while(pt!=null){
            pt.x=pt.x-offX;
            pt.y=pt.y-offY;
            pt=pt.next;
        }
        return;

    }
    public boolean savePoints(TemporalyText outS)
    {
        PointList p;
        p=plist;
        if(!strmWrite(outS,""+offX+","+offY+"\n")) return false;
        while(p!=null){
            if(!strmWrite(outS, " "+p.x+","+p.y+"\n"))
                          return false;
            p=p.next;
        }
        if(!strmWrite(outS,")\n")) return false;
        return true;
    }
    public void init()
    {
        super.init();
        plist=null;
        x2=20; y2=20;
        lastXY=new Point(0,0);
        xy1=new Point(0,0); xy2=new Point(0,0);
//        lineWidth=3;
   }
    public void mouseMove(int xx, int yy)
    {
        int x=xLogical(xx);
        int y=yLogical(yy);
        showhide=true;
        if(step==0) // new, setting first point
        { offX=x; offY=y; return;}
        if(step==1) // new, setting other points
        { x2=x-offX; y2=y-offY; return;}
        if(step==40)// moving
        { offX=x; offY=y; return;}
        if(step==51)// modifying, the first step
        { movePoint(x,y); return; }
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
       xy1.x=x-offX; xy1.y=y-offY;
        if(step==-10) //
        {
            if(isSelected(x,y)){
                if(isPlaying()){
                    actWhenPlaying();
                    return;
                }
                step=10;
                selected=true;
                canvas.ftemp.add(this);
                canvas.fs.remove(this);
                this.start();
            }
            return;
        }
        if(step==0) // new, setting the first
        { offX=x; offY=y; step++; return;}
        if(step==1) // new, setting others
        {
            if(isPointed(x,y,lastXY.x+offX,lastXY.y+offY)){
                nextNewFig();
                return;
            } else{
               x2=xy1.x; y2=xy1.y;
               addPoint(x2,y2);
               showhide=true;
               return;
            }
         }
         if(step==10) // selecting
         {
            if(isSelected(x,y)){
            }
            else
            {
                step=-10;
                selected=false;
                canvas.fs.add(this);
                canvas.ftemp.remove(this);
                this.start();
            }
            return;
         }
         if(step==40) // moving
         {
            offX=x; offY=y;
            step=-10;
            selected=false;
            canvas.fs.add(this);
            canvas.ftemp.remove(this);
            this.start();
            canvas.editdispatch.select();
            return;
         }
         if(step==50) // modifying, the first step
         {
            if(startModifyPoint(x,y)){
                step++;
            }
            return;
         }
         if(step==51) // modifying, the second step
         {
            endModifyPoint(); step=50; return;
         }
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
            thisCopy=new ALines();
            thisCopy.copy(this);
            step++;
            return;
         }
         if(step==62) // magnifying, the third step
         {
            thisCopy=null;
            step=-10;
            selected=false;
            canvas.fs.add(this);
            canvas.ftemp.remove(this);
            this.start();
            canvas.editdispatch.select();
            return;
         }
    }
    public void newFig()
    {
        offX=0; offY=0;
        x2=20;  y2=20;
        step=0;
        plist=null;
        lastXY=new Point(offX,offY);
        showhide=false;
        selected=true;
        xy1=new Point(0,0); xy2=new Point(0,0);
        start();

    }
    public void endModifyPoint()
    {
        ppoint=null;
    }
    public void movePoint(int x, int y)
    {
        if(ppoint!=null){
            ppoint.x=x-offX;
            ppoint.y=y-offY;
        }
    }
    public boolean startModifyPoint(int x, int y)
    {
        ppoint=pointedPoint(x,y);
        if(ppoint==null) return false;
        else return true;
    }
    public PointList ppoint;
    public PointList pointedPoint(int x, int y)
    {
        PointList p;
        p=plist;
        if(isPointed(x,y,offX,offY)){
            writeMessage("This point can't move.\n");
            return null;
        }
        while(p!=null){
            if(isPointed(x,y,offX+p.x,offY+p.y)) return p;
            p=p.next;
        }
        return null;
    }
    public void showEdges(Graphics g)
    {
        PointList pt;
        Color cx;
 //       writeMessage("ALines.drawTemp\n");
        pt=plist;
        showEdge(g, xView(offX), yView(offY));
        Color cc=g.getColor();
        if(cc==Color.black) cx=Color.yellow;
        else cx=Color.black;
        g.setColor(cx);
        g.drawLine(xView(offX-4),yView(offY-4),
                   xView(offX+4),yView(offY+4));
        g.drawLine(xView(offX+4),yView(offY-4),
                   xView(offX-4),yView(offY+4));
        g.setColor(cc);
        xy1.x=0; xy1.y=0;
        while(pt!=null){
            xy2.x=pt.x; xy2.y=pt.y;
            showEdge(g, xView(xy2.x+offX), yView(xy2.y+offY));
            xy1.x=xy2.x; xy1.y=xy2.y;
            pt=pt.next;
        }
        if(step==1) showEdge(g,xView(offX+x2),yView(offY+y2));
        showRC(g);
     }
    //public boolean save(DataOutputStream outS)
    public boolean save(TemporalyText outS)
    {


        if(!saveHeader(outS,"lines")) return false;
        if(!savePoints(outS)) return false;
        return true;

    }
    public boolean isSelected(int x, int y)
    {
        PointList p1,p2;
        int x1,y1;
        p1=plist;
        x1=0; y1=0;
        if(isPointed(offX,offY,x,y)) return true;
        while(p1!=null){
            if(isOntheLine(x,y,x1+offX,y1+offY,p1.x+offX,p1.y+offY)) return true;
            x1=p1.x; y1=p1.y; p1=p1.next;
        }
        if(isPointed(offX+x1,offY+y1,x,y)) return true;
        return false;
    }
    public Point xy2;
    public Point xy1;
    public int y2;
    public int x2;
    public Point lastXY;

    public void addPoint(int xa, int ya)
    {
        PointList p1,p2;
 //       writeMessage("ALines.addPoint("+xa+","+ya+")\n");
        p1=plist;
        lastXY.x=xa; lastXY.y=ya;
        if(p1==null){
            p1=new PointList();
            p1.x=xa; p1.y=ya;
            p1.next=null;
            plist=p1;
            return;
        } else
        {
           p2=p1;
           p1=p1.next;
           while(p1!=null){ p1=p1.next; p2=p2.next;}
           p1=new PointList();
           p1.x=xa; p1.y=ya;
           p1.next=null;
           p2.next=p1;
           return;
        }
    }
     public void copy(ALines lines)
    {
        offX=lines.offX; offY=lines.offY;
        PointList p;
        plist=null;
        p=lines.plist;
        while(p!=null){
            addPoint(p.x,p.y);x2=p.x; y2=p.y;
            p=p.next;
        }
    }
    public void drawTemp(Graphics g)
    {
        PointList pt;
 //       writeMessage("ALines.drawTemp\n");
        drawLinesX(g);

        drawLineW(g,lastXY.x+offX,lastXY.y+offY,offX+x2,offY+y2,lineWidth);
        showEdges(g);

        return;

    }
    public void drawLines(Graphics g)
    {
        PointList pt;
        int x1,y1,x2,y2;
        pt=plist;
        x1=0;y1=0;
        if(lineWidth>1)
              g.fillOval(xView(offX-lineWidth/2),yView(offY-lineWidth/2),
                         lineWidth, lineWidth);
        while(pt!=null){
            x2=pt.x; y2=pt.y;
            /*
            g.drawLine(xView(x1+offX),
                       yView(y1+offY),
                       xView(x2+offX),
                       yView(y2+offY));
            */
            if(lineWidth<=1) {
              g.drawLine(xView(x1+offX),yView(y1+offY),
                         xView(x2+offX),yView(x2+offY));
            }
            else
            {
              drawLineW(g,x1+offX, y1+offY, x2+offX, y2+offY,lineWidth);
              g.fillOval(xView(offX+x2-lineWidth/2),yView(offY+y2-lineWidth/2),
                         lineWidth, lineWidth);
            }
            x1=x2; y1=y2;
            pt=pt.next;
        }
        return;

    }
    public void draw(Graphics g)
    {
        Color cc=g.getColor();
        g.setColor(color);
        if(isEditing()) drawTemp(g);
        else drawLinesX(g);
        g.setColor(cc);
    }
    public ALines(FigCanvas c)
    {
        canvas=c;
        init();
    }
    public ALines()
    {
        super.init();
        plist=null;
        plist=null;
        lastXY=new Point(0,0);
    }
    public PointList plist;
}

