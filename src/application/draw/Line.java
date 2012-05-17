package application.draw;
import java.awt.Color;
import java.awt.Graphics;
public class Line extends GraphicsPrimitives
{
    public Line()
    {
    }
    public Line(int x1, int y1, int w, int h, Color c)
    {
        this.x1=x1; this.y1=y1; this.width=w; this.height=h;
        this.color=c;
    }
    public Color color;
    public int width;
    public int y1;
    public int height;
    public int x1;
    public void paint(Graphics g)
    {
        Color cc=g.getColor();
        g.setColor(color);
        g.drawLine(x1,y1,x1+width,y1+height);
        g.setColor(cc);
    }
}

