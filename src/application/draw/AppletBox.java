package application.draw;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.Color;
import java.awt.Graphics;

public class AppletBox extends ARectangle {
	   Applet applet;
	   AppletContext context;
	   AppletStub stub;
	   public AppletBox()
	    {
	    }
	    public AppletBox(FigCanvas c,Applet a)
	    {
	    	this.canvas=c;
	        this.applet=a;
//	        this.context=c;
//	        this.stub=s;
	    }
	    public void drawTemp(Graphics g)
	    {
	        drawRectX(g);
	        showEdge(g,xView(offX),yView(offY));
	        showEdge(g,xView(offX+x2),yView(offY));
	        showEdge(g,xView(offX+x2),yView(offY+y2));
	        showEdge(g,xView(offX),yView(offY+y2));
	        showRC(g);
	        this.applet.setLocation(xView(offX),yView(offY));
	        this.applet.paint(g);
	    }
	    public void draw(Graphics g)
	    {
	        Color cc=g.getColor();
	        g.setColor(color);
	        if(isEditing()) drawTemp(g);
	        else {
		        this.applet.setLocation(xView(offX),yView(offY));
	        	this.applet.paint(g);
	        }
	        g.setColor(cc);
	    }
}
