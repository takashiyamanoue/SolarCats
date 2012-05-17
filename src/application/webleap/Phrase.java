package application.webleap;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import application.draw.GraphicsPrimitives;
public class Phrase extends GraphicsPrimitives
{
    public void getSize(String t)
    {
        height=fmetrics.getMaxAscent()+fmetrics.getMaxDescent();
        width=fmetrics.stringWidth(t);
    }


    public void getSize()
    {
        height=fmetrics.getMaxAscent()+fmetrics.getMaxDescent();
        width=fmetrics.stringWidth(text);
    }

    public void paint()
    {
        /*
        String toDraw="addfig text("+
             "attrib(\n"+
             "color("+ Color.black.getRGB()+")\n"+
             "depth(0)\n"+
             "width(1)\n"+
             "font(\"Dialog\","+ //this.font.getStyle()+
                               + 0+ ","+
                                 // this.font.getSize()
                               + 14 +")\n"+
             ")"+
             this.x1+","+this.y1+","
                             +"\""+text+"\")";
        String rtn=this.gui.draw.parseCommand(toDraw);
        */
    }

    public WebLEAPFrame gui;
    public int height;
    public int width;
    public Visualizer canvas;
    public Font font;
    public FontMetrics fmetrics;
    public String text;
    public Phrase(String s, int x, int y, Color c,WebLEAPFrame g)
    {
         x1=x; y1=y; color=c;
         gui=g;
         canvas=gui.getVisualizer();
         font=new Font("Dialog",0,14);
         fmetrics=canvas.getFontMetrics(font);
         text=s;
    }
    public Color color;
    public int y1;
    public int x1;
    public void paint(Graphics g)
    {
        /*
        Color cc=g.getColor();
        g.setColor(color);
        g.drawString(text,x1,y1);
        g.setColor(cc);
        */
        /*
        String toDraw="addfig text("+
             "attrib(\n"+
             "color("+ this.color.getRGB()+")\n"+
             "depth(0)\n"+
             "width(1)\n"+
             "font(\""+this.font.getName()+"\","+this.font.getStyle()+","+
                       this.font.getSize()+")\n"+
             ")"+
             this.x1+","+this.y1+","
                             +"\""+text+"\")";
        String rtn=this.gui.draw.parseCommand(toDraw);
        */
    }
}

