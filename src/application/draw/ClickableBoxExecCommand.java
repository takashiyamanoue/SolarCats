package application.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import controlledparts.ControlledFrame;

import nodesystem.DialogListener;
import nodesystem.EditDialog;
import nodesystem.TemporalyText;

public class ClickableBoxExecCommand extends SensorBox implements DialogListener  
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

    public ClickableBoxExecCommand(FigCanvas c)
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
        if(!saveHeader(outS,"clickableboxexe")) return false;
        if(!strmWrite(outS, ""+offX+","+offY+","
                             +x2+","+y2+","
                             +this.str2saveable(new StringBuffer(this.command))+"\")\n"))
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
        this.command=d.getText();
    }

    Hashtable applications;
    public void actWhenPlaying()
    {
    	String rtn="";
    	if(applications!=null){
    	     String appliName=command.substring(0,command.indexOf(" "));
    	     String arg=command.substring((appliName+" ").length());
    	     ControlledFrame f=(ControlledFrame)(applications.get(appliName));
    	     if(f!=null){
                  rtn=f.parseCommand(arg);	
    	     }
    	}
    	else{
           rtn=this.canvas.gui.communicationNode.call(command);
    	}
    }
    public void setApplications(Hashtable a){
    	this.applications=a;
    }

    public void mouseDown(int x, int y)
    {
        if(this.step==-10 && isSetCommandRegion(x,y)){
            this.canvas.gui.openTextEditor(this);
            this.canvas.gui.textEditFrame.getTextArea().setText(command);
            return;
        }
        super.mouseDown(x,y);
    }

    public int commandRegionY;

    public int commandRegionX;

    public String command;

    public AFigure copyThis()
    {
        ClickableBoxExecCommand rtn=(ClickableBoxExecCommand)(super.copyThis());
        rtn.command=this.command;
        return (AFigure)rtn;
    }

    public void init(FigCanvas c)
    {
        this.command="";
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
            g.drawString("command: "+this.command,xView(offX),yView(offY-2));
            g.drawRect(xView(offX),yView(offY-commandRegionY), commandRegionX,commandRegionY);
            g.setColor(Color.black);
            super.draw(g);
            g.setColor(cc);
            g.setFont(cf);
        }
    }

    public ClickableBoxExecCommand()
    {
    }

}