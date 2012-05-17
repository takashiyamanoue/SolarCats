package application.draw;
import java.awt.Color;
import java.awt.Graphics;
public class BoxSensor extends Box
{

    public void paint(Graphics g)
    {
        Color cc=g.getColor();
        g.setColor(color);
        g.drawRect(x1,y1,x1+width,y1+height);
        g.setColor(cc);
    }
}

