package application.draw;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class SensorBoxWithLabel extends Box
{
    public String attribute;

    public String getAttribute()
    {
        return attribute;
    }

    public void setAttribute(String a)
    {
        attribute=a;
    }

    public void draw(Graphics g, int x, int y)
    {
        x1=x; y1=y;
        this.draw(g);
    }

    public boolean isInside(int x, int y)
    {
        setSize();
        if(x<x1) return false;
        if(x1+width<x) return false;
        if(y<y1) return false;
        if(y1+height<y) return false;
        return true;
    }

    public void setLabel(String l)
    {
        label=l;
    }

    public void setColor(Color c)
    {
        color=c;
    }

    public void setSize()
    {

        height=fmetrics.getMaxAscent()+fmetrics.getMaxDescent()+2;
        width=fmetrics.stringWidth(label)+2;
    }

    public FigCanvas canvas;

    public void setCanvas(FigCanvas c)
    {
        canvas=c;
    }

    public void setFont(Font f)
    {
        font=f;
        fmetrics=canvas.getFontMetrics(f);
    }

    public FontMetrics fmetrics;

    public Font font;

    public boolean getCheckState()
    {
        return isChecked;
    }

    public void setCheckState(boolean b)
    {
        isChecked=b;
    }

    public boolean isChecked;

    public void draw(Graphics g)
    {
        Color cc=g.getColor();
        Font cf=g.getFont();
        g.setFont(this.font);
        this.setSize();
        if(this.isChecked){
             g.setColor(color);
             g.fillRect(x1,y1,width,height);
             g.setColor(Color.black);
             g.drawString(label,x1,y1+height-fmetrics.getMaxDescent()-1);
        }
        else{
             g.setColor(Color.white);
             g.fillRect(x1,y1,width,height);
             g.setColor(color);
             g.drawRect(x1,y1,width,height);
             g.drawString(label,x1,y1+height-fmetrics.getMaxDescent()-1);
        }
        g.setFont(cf);
        g.setColor(cc);
    }

    public void setPosition(int x, int y)
    {
        x1=x; y1=y;
    }

    public String label;

    public SensorBoxWithLabel(String l, FigCanvas c)
    {
        label=l;
        canvas=c;
        setFont(new Font("Dialog", Font.BOLD, 8));
    }

}