package application.draw;
import java.awt.Color;
import java.awt.Graphics;

import nodesystem.TemporalyText;
public class RainbowPolygon extends APolygon implements Runnable
{
    public AFigure copyThis()
    {
        RainbowPolygon f=(RainbowPolygon)(copyThis2(new RainbowPolygon(canvas)));
        f.cColor=cColor;
        f.hue=hue;
        f.me=me;
        return (AFigure)f;
    }
    public boolean save(TemporalyText outS)
    {
        if(!saveHeader(outS,"rainbowpolygon")) return false;
        if(!savePoints(outS)) return false;
        return true;
    }
    public Thread me;
    public int hue;
    public Color cColor;
    public void stop()
    {
        if(me!=null){
//            me.stop();
            me=null;
        }
    }
    public void start()
    {
        if(me==null){
            me=new Thread(this);
            me.start();
        }
    }
    public void run()
    {
          while(me!=null){
            draw(canvas.getGraphics());
            advance();
            try{ Thread.sleep(100);}
            catch(InterruptedException e) {}
          }
    }
    public void draw(Graphics g)
    {
   //    if(showhide)
   //    {
          Color cc=g.getColor();
          g.setColor(cColor);
          if(isEditing()){
            drawTemp(g);
            g.drawLine(offX+lastXY.x, offY+lastXY.y,
                       x2+offX, y2+offY);
          }
          else     drawPolygon(g,cColor);
          g.setColor(cc);
   //    }
    }
    public void advance()
    {
        hue +=3;
        if(hue >=360) hue=0;
        cColor = Color.getHSBColor((float)(hue/360.0),
                                   (float)1.0, (float)1.0);
    }
    public RainbowPolygon(FigCanvas c)
    {
        canvas=c;
        init();
        start();
    }
    public RainbowPolygon()
    {
        init();
        start();
    }
}

