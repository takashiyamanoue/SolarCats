package application.draw;

import java.awt.Color;
import java.awt.Graphics;

import nodesystem.TemporalyText;
class FillRect extends ARectangle
{
    public AFigure copyThis()
    {
        ALine f=(ALine)(super.copyThis2(new FillRect(canvas)));
        f.pointedP=pointedP;
        f.x2=x2;
        f.y2=y2;
        return (AFigure)f;
    }
    public boolean save(TemporalyText outS)
    {
        if(!saveHeader(outS,"fillrect")) return false;
        if(!strmWrite(outS, ""+offX+","+offY+","
                             +x2+","+y2+")\n")) return false;
         return true;
    }
    public FillRect(FigCanvas c)
    {
        canvas=c;
        init();
    }
    public FillRect()
    {
        init();
    }
    public void drawTemp(Graphics g)
    {
        g.fillRect(xView(offX),yView(offY),
                   x2,y2);
        showEdge(g,xView(offX),yView(offY));
        showEdge(g,xView(offX+x2),yView(offY));
        showEdge(g,xView(offX+x2),yView(offY+y2));
        showEdge(g,xView(offX),yView(offY+y2));
    }
    public void draw(Graphics g)
    {
        Color cc=g.getColor();
        g.setColor(color);
        if(isEditing()) drawTemp(g);
        else    g.fillRect(xView(offX),yView(offY),
                           x2,y2);
        g.setColor(cc);
    }

}

