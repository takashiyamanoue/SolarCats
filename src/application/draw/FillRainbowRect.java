package application.draw;
import java.awt.Color;
import java.awt.Graphics;

import nodesystem.TemporalyText;
public class FillRainbowRect extends FillRect implements Runnable
{
    public AFigure copyThis()
    {
        FillRainbowRect f=
          (FillRainbowRect)(copyThis2(new FillRainbowRect(canvas)));
        f.cColor=cColor;
        f.hue=hue;
        f.me=me;
        return (AFigure)f;
    }
    public boolean save(TemporalyText outS)
    {
        if(!saveHeader(outS,"rainbowrect")) return false;
        if(!strmWrite(outS, ""+offX+","+offY+","
                             +x2+","+y2+")\n")) return false;
         return true;
    }
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
            me=new Thread(this,"FillRainbowRect");
            me.start();
        }
    }
    public volatile Thread me;
    public void run()
    {
        Thread thisThread = Thread.currentThread( );
          while(me==thisThread){
            draw(canvas.getGraphics());
            advance();
            try{ thisThread.sleep(100);}
            catch(InterruptedException e) {}
          }
    }
    public int hue;
    public void advance()
    {
        hue +=3;
        if(hue >=360) hue=0;
        cColor = Color.getHSBColor((float)(hue/360.0),
                                   (float)1.0, (float)1.0);
    }
    public Color cColor;
    public void draw(Graphics g)
    {
       Color cc=g.getColor();
       g.setColor(cColor);
       if(isEditing()) drawTemp(g);
       else g.fillRect(xView(offX),yView(offY),x2,y2);
       g.setColor(cc);
    }
    public FillRainbowRect()
    {
        init();
        cColor=Color.blue;
        start();
    }
    public FillRainbowRect(FigCanvas c)
    {
        canvas=c;
        init();
        x2=20; y2=20;
        cColor=Color.blue;
        start();
    }
}

