package application.draw;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.util.Vector;

import nodesystem.DialogListener;
import nodesystem.EditDialog;
import nodesystem.TemporalyText;

public class UrlBox extends SensorBox
implements DialogListener
{
    public boolean isShowingRmouse()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return this.canvas.gui.isShowingRmouse();
    }

    public boolean isControlledByLocalUser()
    {
        return this.canvas.gui.isControlledByLocalUser();
    }

    public boolean isDirectOperation()
    {
        return this.canvas.gui.isDirectOperation();
    }

    public UrlBox(FigCanvas c)
    {
        init();
        canvas=c;
        init(c);
    }

    public boolean isSetCommandRegion(int x, int y)
    {
            if(x<offX) return false;
            if(offX+commandRegionX<x) return false;
            if(y<offY-commandRegionY) return false;
            if(offY<y) return false;
            return true;
    }

    public boolean isSelected(int x, int y)
    {
        if(super.isSelected(x,y)) return true;
        if(this.isPlaying()) return false;
        return false;
    }

    public boolean save(TemporalyText outS)
    {
        if(!saveHeader(outS,"urlbox")) return false;
        if(!strmWrite(outS, ""+offX+","+offY+","
                             +x2+","+y2+","
                             +this.str2saveable(new StringBuffer(this.url))+"\")\n"))
                             return false;
         return true;
    }

    public void newFig()
    {
        init(canvas);
    }

    public Vector getDialogs()
    {
        return null;
    }

    public File getDefaultPath()
    {
        return null;
    }

    public void sendFileDialogMessage(String s)
    {
    }

    public void whenCancelButtonPressed(EditDialog d)
    {
    }

    public void whenActionButtonPressed(EditDialog d)
    {
        this.url=d.getText();
    }

    public void setUrl(String u){
    	this.url=u;
    }
    public void actWhenPlaying()
    {
       this.canvas.gui.readHtml(this.url);
    }

    public void mouseDown(int x, int y)
    {
        if(this.step==-10 && isSetCommandRegion(x,y)){
            this.canvas.gui.openTextEditor(this);
            this.canvas.gui.textEditFrame.getTextArea().setText(url);
            return;
        }
        super.mouseDown(x,y);
    }

    public int commandRegionY;

    public int commandRegionX;

    public String url;

    public AFigure copyThis()
    {
        UrlBox rtn=(UrlBox)(super.copyThis());
        rtn.url=this.url;
        return (AFigure)rtn;
    }

    public void init(FigCanvas c)
    {
        this.url="";
        this.commandRegionX=20;
        this.commandRegionY=12;
    }


    public void draw(Graphics g)
    {
        if(this.isPlaying()){
            return;
        }
        else{
            Font cf=g.getFont();
            Color cc=g.getColor();
            g.setFont(new Font("Dialog", Font.BOLD, 8));
            g.setColor(Color.black);
            g.drawString("url: "+this.url,xView(offX),yView(offY-2));
            g.drawRect(xView(offX),yView(offY-commandRegionY), commandRegionX,commandRegionY);
            g.setColor(Color.black);
            super.draw(g);
            g.setColor(cc);
            g.setFont(cf);
        }
    }

    public UrlBox()
    {
    }

}

