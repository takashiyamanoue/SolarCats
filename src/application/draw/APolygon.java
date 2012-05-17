package application.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import nodesystem.TemporalyText;

class APolygon extends ALines
{
    public AFigure copyThis()
    {
        APolygon f=(APolygon)(copyThis2(new APolygon(canvas)));
        f.pgon=pgon;
        return (AFigure)f;
    }
    public void init()
    {
        plist=null;
        offX=0; offY=0; x2=20; y2=20;
        lastXY=new Point(offX,offY);
        selected=false;
        xy1=new Point(0,0); xy2=new Point(0,0);
     }
    public Polygon pgon;
    public void mkPolygon()
    {
        int xlist[];
        int ylist[];
        int i,n;
        PointList p;
        p=plist;
        n=1;
        while(p!=null){
            n++;
            p=p.next;
        }

        xlist= new int[n];
        ylist= new int[n];
        p=plist;
        xlist[0]=offX; ylist[0]=offY;
        i=1;
        while(p!=null){
            xlist[i]=p.x+offX; ylist[i]=p.y+offY;
            p=p.next;
            i++;
        }
        pgon=new Polygon(xlist,ylist,n);
        /*
//        if(n>1){
            Color cc=g.getColor();
            g.setColor(color);
            if(n==2)
                drawLines(g);
            else
            g.fillPolygon(xlist,ylist,n);
        */
    }
    public boolean isSelected(int x, int y)
    {
        
        if(super.isSelected(x,y)) return true;
        if(isOntheLine(x,y,offX,offY,x2,y2)) return true;
        return false;
        /*
        mkPolygon();
        return pgon.inside(x,y);
        */
    }
    public void drawPolygon(Graphics g,Color color)
    {
        int xlist[];
        int ylist[];
        int i,n;
        PointList p;
        p=plist;
        n=1;
        while(p!=null){
            n++;
            p=p.next;
        }

        xlist= new int[n];
        ylist= new int[n];
        p=plist;
        xlist[0]=xView(offX); ylist[0]=yView(offY);
        i=1;
        while(p!=null){
            xlist[i]=xView(p.x+offX);
            ylist[i]=yView(p.y+offY);
            p=p.next;
            i++;
        }
//        if(n>1){
            Color cc=g.getColor();
            g.setColor(color);
            if(n==2)
                drawLines(g);
            else
            g.fillPolygon(xlist,ylist,n);
            g.setColor(cc);
//        }

    }
    public void drawTemp(Graphics g)
    {
        drawPolygon(g,color);
        showEdges(g);
    }
    public APolygon()
    {
        plist=null;
        init();
    }
    public APolygon(FigCanvas c)
    {
        canvas=c;
        init();
    }
 // public boolean save(DataOutputStream outS)
    public boolean save(TemporalyText outS)
    {

        PointList p;
        p=plist;
        if(!saveHeader(outS,"polygon")) return false;
        if(!savePoints(outS)) return false;
        return true;
    }

    public void draw(Graphics g)
    {
        if(showhide){
            Color cc=g.getColor();
            g.setColor(color);
            if(isEditing()){
                drawTemp(g);
                g.drawLine(xView(offX+lastXY.x),
                           yView(offY+lastXY.y),
                           xView(x2+offX),
                           yView(y2+offY));
                showEdge(g,xView(x2+offX),
                           yView(y2+offY));
            }
            else
                drawPolygon(g,color);
            g.setColor(cc);
        }
    }

}

