package application.draw;

import java.awt.Color;
import java.awt.Graphics;

import nodesystem.TemporalyText;

class AFreeHandCurve extends ALines
{
    public AFigure copyThis()
    {
        return (AFigure)(copyThis2(new AFreeHandCurve(canvas)));
    }
    public void mouseUp(int x, int y)
    {
         if(step==1){
                nextNewFig();
         }
     }
    public void mouseDrag(int xx, int yy)
    {
        int x=xLogical(xx);
        int y=yLogical(yy);
         xy1.x=x-offX; xy1.y=y-offY;
         x2=xy1.x; y2=xy1.y;
         addPoint(x2,y2);
         showhide=true;
         step=1;
         return;
    }
    public void draw(Graphics g)
    {
        int xp,yp;
        xp=offX+lastXY.x; yp=offY+lastXY.y;
//        writeMessage("NewLines.draw\n");
        if(showhide) {
            Color cc=g.getColor();
            g.setColor(color);
            if(isEditing()){
              drawTemp(g);
              g.drawLine(xView(xp+1), yView(yp-2),
                         xView(xp+11), yView(yp-22));
              g.drawLine(xView(xp+11),yView(yp-22),
                         xView(xp+13),yView( yp-20));
              g.drawLine(xView(xp+13),yView(yp-20),
                         xView(xp+3), yView(yp-1));
              g.drawLine(xView(xp+3),yView(yp-1),
                         xView(xp),yView(yp));
              g.drawLine(xView(xp+1),yView(yp-2),
                         xView(xp),yView(yp));
              g.setColor(cc);
            }
            else
              super.draw(g);
        }
   }
    //public boolean save(DataOutputStream outS)
    public boolean save(TemporalyText outS)
    {

        PointList p;
        p=plist;
        if(!saveHeader(outS,"free")) return false;
        if(!savePoints(outS)) return false;
        return true;
    }
    public void drawTemp(Graphics g)
    {
         PointList pt;
 //      writeMessage("ALines.drawTemp\n");
        pt=plist;
        drawLinesX(g);
        showEdge(g, xView(offX), yView(offY));
        this.showRC(g);
        return;
   }
    public AFreeHandCurve()
    {
    }
    public AFreeHandCurve(FigCanvas c)
    {
         canvas=c;
         /*
        plist=null;
        offX=0; offY=0; x2=20; y2=20;
        lastXY=new Point(0,0);
        selected=false;
        xy1=new Point(0,0); xy2=new Point(0,0);
        */
        init();
   }
    public boolean isSelected(int x, int y)
    {
        PointList p1,p2;;
        p1=plist;
        if(isPointed(offX,offY,x,y)) return true;
        while(p1!=null){
            if(isPointed(p1.x+offX,p1.y+offY,x,y)) return true;
            p1=p1.next;
        }
        return false;
     }
}

