package application.draw;

import java.awt.Color;
import java.awt.Graphics;

import nodesystem.TemporalyText;

class AnOval extends ALine
{
    public void drawOvalX(Graphics g)
    {
        if(lineWidth<=1) g.drawOval(xView(offX),yView(offY),x2,y2);
        else
        if(lineWidth==2){
            g.drawOval(xView(offX),yView(offY),x2,y2);
            g.drawOval(xView(offX+1),yView(offY+1),x2-2,y2-2);
        }
        else
        if(lineWidth==3)
        {
            g.drawOval(xView(offX),yView(offY),x2,y2);
            g.drawOval(xView(offX+1),yView(offY+1),x2-2,y2-2);
            g.drawOval(xView(offX-1),yView(offY-1),x2+2,y2+2);
        }
        else{
            int d2=lineWidth/2;
            for(int i=0;i<lineWidth;i++){
                g.drawOval(xView(offX+i-d2),yView(offY+i-d2),x2-(i*2-lineWidth),y2-(i*2-lineWidth));
            }
        }
        
    }

    public double ovalTheta(double theta)
    {
        return 0.0d;
    }

    public void showOvalEdges(Graphics g)
    {
        showEdge(g,xView(offX),yView(offY));
        showEdge(g,xView(offX+x2),yView(offY+y2));
        showEdge(g,xView(offX),yView(offY+y2));
        showEdge(g,xView(offX+x2),yView(offY));
        showEdge(g,xView(offX),yView(offY+y2/2));
        showEdge(g,xView(offX+x2),yView(offY+y2/2));
        showEdge(g,xView(offX+x2/2),yView(offY));
        showEdge(g,xView(offX+x2/2),yView(offY+y2));
        showRC(g);
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
        ALine f=(ALine)(super.copyThis2(new AnOval(canvas)));
        f.pointedP=pointedP;
        f.x2=x2;
        f.y2=y2;
        return (AFigure)f;
    }
    //public boolean save(DataOutputStream outS)
    public boolean save(TemporalyText outS)
    {
       if(!saveHeader(outS, "oval")) return false;
       if(!strmWrite(outS, ""+offX+","+offY+","
                            +x2+","+y2+")\n")) return false;

        return true;
    }
    public AnOval(FigCanvas c)
    {
        canvas=c;
        super.init();
        x2=20; y2=20;
    }
    public AnOval()
    {
        super.init();
    }
    public boolean isSelected(int x, int y)
    {
        double a,aa,b,bb,xx,yy;
        if(isPointed(x,y,offX+x2/2,offY)) return true;
        if(isPointed(x,y,offX,offY+y2/2)) return true;
        if(isPointed(x,y,offX+x2/2,offY+y2)) return true;
        if(isPointed(x,y,offX+x2,offY+y2/2)) return true;
        a=x2/2.0; b=y2/2.0; aa=a*a; bb=b*b;
        if(aa<0.05) return false;
        if(bb<0.05) return false;
        xx=x-offX-a; yy=y-offY-b;
        if(Math.abs(xx*xx/aa+yy*yy/bb-1.0)<0.05) return true;
        return false;
    }
    public void drawTemp(Graphics g)
    {
        drawOvalX(g);
        showOvalEdges(g);
    }
    public void draw(Graphics g)
    {
        Color cc=g.getColor();
        g.setColor(color);
        if(isEditing()) drawTemp(g);
        else drawOvalX(g);
        g.setColor(cc);
    }
}

