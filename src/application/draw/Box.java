package application.draw;
import java.awt.Color;
import java.awt.Graphics;
public class Box extends Line
{
    public Box()
    {
    }
    public Box(int x, int y, int w, int h,Color c)
    {
        this.x1=x; this.y1=y; this.width=w; this.height=h;
        this.color=c;
    }
    public void paint(Graphics g)
    {
        Color cc=g.getColor();
        g.setColor(color);
        g.fillRect(x1,y1,width,height);
        g.setColor(cc);
    }
}

