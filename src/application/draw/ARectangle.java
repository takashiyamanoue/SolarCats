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

class ARectangle extends ALine
{
    public void drawRectX(Graphics g)
    {
        if(lineWidth<=1)
              g.drawRect(xView(offX),yView(offY),
                           x2,y2);
        else{
              drawLineW(g,offX, offY, x2+offX, offY,lineWidth);
              drawLineW(g,offX+x2, offY, x2+offX, y2+offY,lineWidth);
              drawLineW(g,offX+x2, offY+y2, offX, y2+offY,lineWidth);
              drawLineW(g,offX, offY+y2, offX, offY,lineWidth);
        }
       return;
    }

    public void magnifyXY()
    {
//        int x1,y1,x2,y2;
        double offXw, offYw;
        offXw=thisCopy.offX; offYw=thisCopy.offY;
        magrotateP(dcX,dcY,magRatio,angle,
                       (double)(thisCopy.offX-thisCopy.x2/2),
                       (double)(thisCopy.offY-thisCopy.y2/2));
        x2=(int)(thisCopy.x2*magRatio);
        y2=(int)(thisCopy.y2*magRatio);
        offX=(int)newdX+x2/2; offY=(int)newdY+y2/2;
        return;

    }
    public AFigure copyThis()
    {
        ALine f=(ALine)(super.copyThis2(new ARectangle(canvas)));
        f.pointedP=pointedP;
        f.x2=x2;
        f.y2=y2;
        return (AFigure)f;
    }
    //public boolean save(DataOutputStream outS)
    public boolean save(TemporalyText outS)
    {
        if(!saveHeader(outS,"rectangle")) return false;
        if(!strmWrite(outS, ""+offX+","+offY+","
                             +x2+","+y2+")\n")) return false;
         return true;
    }
    public boolean isSelected(int x, int y)
    {
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
    public ARectangle()
    {
        super.init();
    }
    public ARectangle(FigCanvas c)
    {
        canvas=c;
        super.init();
    }
    public void drawTemp(Graphics g)
    {
        drawRectX(g);
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
        if(isEditing()) drawTemp(g);
        else    drawRectX(g);
        g.setColor(cc);
    }
}

