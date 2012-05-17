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

import nodesystem.TemporalyText;

public class ALine extends AFigElement
{
    public void drawLine(Graphics g)
    {
        Color cc=g.getColor();
        g.setColor(color);
        if(lineWidth<=1) {
          g.drawLine(xView(offX),yView(offY),
                     xView(offX+x2),yView(offY+y2));
        }
        else
        {
          drawLineW(g,offX, offY, offX+x2, offY+y2,lineWidth);
          g.fillOval(xView(offX+x2-lineWidth/2),yView(offY+y2-lineWidth/2),
                     lineWidth, lineWidth);
          g.fillOval(xView(offX-lineWidth/2),yView(offY-lineWidth/2),
                     lineWidth, lineWidth);
        }
        g.setColor(cc);
    }

    ALine thisCopy;

    public void magnifyXY()
    {
//        int x1,y1,x2,y2;
        double offXw, offYw;
        offXw=thisCopy.offX; offYw=thisCopy.offY;
        magrotateP(dcX,dcY,magRatio,angle,(double)(thisCopy.offX),
                                  (double)(thisCopy.offY));
        offX=(int)newdX; offY=(int)newdY;
        magrotateP(dcX,dcY,magRatio,angle,
                           (double)(thisCopy.x2+offXw),
                           (double)(thisCopy.y2+offYw));
        x2=(int)(newdX-offX); y2=(int)(newdY-offY);
        return;

    }
    public void copy(ALine f)
    {
        offX=f.offX; offY=f.offY; x2=f.x2;y2=f.y2;
    }
    public AFigure copyThis()
    {
        ALine f=(ALine)(super.copyThis2(new ALine(canvas)));
        f.pointedP=pointedP;
        f.x2=x2;
        f.y2=y2;
        return (AFigure)f;
    }
    public boolean isIntheArea(int  wx1, int wy1, int wx2, int  wy2)
    {
        if(!isIntheRectangle(offX,offY,       wx1,wy1,wx2,wy2)) return false;
        if(!isIntheRectangle(offX+x2,offY+y2, wx1,wy1,wx2,wy2)) return false;
        return true;
    }
    public void movePoint(int  x, int y)
    {
        if(pointedP==1){
            int  wx=x2+offX;
            int  wy=y2+offY;
            offX=x; offY=y;
            x2=wx-offX; y2=wy-offY;
            return;
        }
        else
        if(pointedP==2){
            x2=x-offX;  y2=y-offY;
            return;
        }
    }
    int pointedP;
    public boolean  startModifyPoint(int x, int y)
    {
        pointedP=0;
        if(isPointed(x,y,offX,offY))
        {
            pointedP=1;
            return  true;
        }
        else
        if(isPointed(x,y,offX+x2,offY+y2))
        {
            pointedP=2;
            return true;
        }
        else return false;
    }
    public void init()
    {
        super.init();
        x2=20; y2=20;
//        lineWidth=3;
     }
    public void mouseMove(int xx, int yy)
    {
        int x=xLogical(xx);
        int y=yLogical(yy);
        showhide=true;
        if(step==0) // new , setting one edge
        {offX=x; offY=y; return; }
        if(step==1) // setting another edge
        {x2=x-offX; y2=y-offY; return;}
        if(step==40) // moving this line
        {offX=x; offY=y; return;}
        if(step==51) // modifying, the first step
        { movePoint(x,y); return;}
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
        if(step==0){ offX=x; offY=y; step++; return;}
        if(step==1){
            x2=x-offX; y2=y-offY;
            nextNewFig();
            return;
        }
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
            if(startModifyPoint(x,y)){
                step++;
            }
            return;
        }
        if(step==51) // modifying, the second  step
        {
            movePoint(x,y);  step=50; return;
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
            thisCopy=new ALine();
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
        offX=0; offY=0; x2=20; y2=20;
        step=0;
        showhide=false;
        selected=true;
        if(color==null) color=canvas.getForeground();
    }
    //public boolean save(DataOutputStream outS)
    public boolean save(TemporalyText outS)
    {
        if(!saveHeader(outS,"line")) return false;
        if(!strmWrite(outS, ""+offX+","+offY+","
                             +x2+","+y2+")\n")) return false;
        return true;

    }
    public ALine()
    {
        init();
    }
    public ALine(FigCanvas c)
    {
        canvas=c;
        init();
    }
    public void drawTemp(Graphics g)
    {
        Color cx;
        drawLine(g);
        showEdge(g,
          xView(offX),yView(offY));
        showEdge(g,xView(offX+x2),yView(offY+y2));
        showRC(g);
     }
    public void draw(Graphics g)
    {
//        writeMessage("ALine.draw\n");
        Color cc=g.getColor();
        g.setColor(color);
        if(isEditing()) drawTemp(g);
        else
        drawLine(g);
        g.setColor(cc);
    }
    public boolean isSelected(int x, int y)
    {
        if(isPointed(offX,offY,x,y)) return true;
        if(isPointed(offX+x2,offY+y2,x,y)) return true;
        if(isOntheLine(x,y,offX,offY,offX+x2,offY+y2)) return true;
        return false;
    }
    public int y2;
    public int x2;
}

