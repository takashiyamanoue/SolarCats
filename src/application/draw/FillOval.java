package application.draw;

import java.awt.Color;
import java.awt.Graphics;

import nodesystem.TemporalyText;

public class FillOval extends AnOval
{
    public AFigure copyThis()
    {
        ALine f=(ALine)(super.copyThis2(new FillOval(canvas)));
        f.pointedP=pointedP;
        f.x2=x2;
        f.y2=y2;
        return (AFigure)f;
    }
    public boolean save(TemporalyText outS)
    {
       if(!saveHeader(outS, "filloval")) return false;
       if(!strmWrite(outS, ""+offX+","+offY+","
                            +x2+","+y2+")\n")) return false;

        return true;
    }
    public void drawTemp(Graphics g)
    {
        g.fillOval(xView(offX),yView(offY),
                   x2,y2);
        showOvalEdges(g);
    }
    public FillOval(FigCanvas c)
    {
        canvas=c;
        init();
        x2=20; y2=20;
    }
    public FillOval()
    {
        init();
    }
    public void draw(Graphics g)
    {
        Color cc=g.getColor();
        g.setColor(color);
        if(isEditing()) drawTemp(g);
        else g.fillOval(xView(offX),yView(offY),
                        x2,y2);
        g.setColor(cc);
    }
}

