package application.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.util.Vector;

import nodesystem.DialogListener;
import nodesystem.EditDialog;
import nodesystem.TemporalyText;

public class ClickableBoxURL extends SensorBox implements DialogListener   
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

    public void setBasePathBox(int i)
    {
        this.selectedBasePathBox=i;
        for(int k=0;k<paths.size();k++){
             SensorBoxWithLabel sb=(SensorBoxWithLabel)(paths.elementAt(k));
            if(k!=i) sb.setCheckState(false);
           
        }
        SensorBoxWithLabel sb=(SensorBoxWithLabel)(paths.elementAt(i));
        sb.setCheckState(true);
        
    }

    public int selectedBasePathBox;


    public void selectBoxSens(int x, int y)
    {
        if(this.isPlaying()) return;
        int num=paths.size();
        int w=0;
        int selectedPos=-1;
        for(int i=0;i<num;i++){
            SensorBoxWithLabel sb=(SensorBoxWithLabel)(paths.elementAt(i));
            if(sb.isInside(x,y)){
                selectedPos=i;
                sb.setCheckState(true);
            }
        }
        if(selectedPos==-1) return;
        for(int i=0;i<num;i++){
            SensorBoxWithLabel sb=(SensorBoxWithLabel)(paths.elementAt(i));
            if(i!=selectedPos) sb.setCheckState(false);
        }
        this.selectedBasePathBox=selectedPos;
    }

    public void drawSelectBoxs(Graphics g)
    {
        int num=paths.size();
        int w=0;
        for(int i=0;i<num;i++){
            SensorBoxWithLabel sb=(SensorBoxWithLabel)(paths.elementAt(i));
            sb.setSize();
            sb.setPosition(offX+w,offY-15-sb.height);
            sb.draw(g);
            w=w+sb.width+1;
        }
    }

    public Vector paths;

    public File getDefaultPath()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return null;
    }

    public Vector getDialogs()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return null;
    }

    public void sendFileDialogMessage(String m)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

    public void whenActionButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        this.urlName=d.getText();
    }

    public void whenCancelButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

    public void init(FigCanvas c)
    {
        this.urlName="";
        this.urlRegionX=20;
        this.urlRegionY=12;
        this.paths=new Vector();
        SensorBoxWithLabel sb1=new SensorBoxWithLabel("current", c);
        sb1.setAttribute("current");
        sb1.setCheckState(true);
        sb1.setFont(new Font("Dialog", Font.BOLD, 8));
        sb1.setColor(Color.red);
        SensorBoxWithLabel sb2=new SensorBoxWithLabel("common", c);
        sb2.setAttribute(this.getCommonDirPath());
        sb2.setCheckState(false);
        sb2.setFont(new Font("Dialog", Font.BOLD, 8));
        sb2.setColor(Color.green);
        SensorBoxWithLabel sb3=new SensorBoxWithLabel("user",c);
        sb3.setAttribute(this.getUserDirPath());
        sb3.setCheckState(false);
        sb3.setFont(new Font("Dialog", Font.BOLD, 8));
        sb3.setColor(Color.blue);
        SensorBoxWithLabel sb4=new SensorBoxWithLabel("net",c);
        sb4.setAttribute("");
        sb4.setCheckState(false);
        sb4.setFont(new Font("Dialog", Font.BOLD, 8));
        sb4.setColor(Color.red);
        paths.addElement(sb1);
        paths.addElement(sb2);
        paths.addElement(sb3);
        paths.addElement(sb4);
        this.selectedBasePathBox=0;
    }

    public int urlRegionY;

    public int urlRegionX;

    public boolean isSetURLRegion(int x, int y)
    {
            if(x<offX) return false;
            if(offX+urlRegionX<x) return false;
            if(y<offY-urlRegionY) return false;
            if(offY<y) return false;
            return true;
    }

    // public int editState;

    public AFigure copyThis()
    {
        ClickableBoxURL rtn=new ClickableBoxURL(this.canvas);
        rtn.offX=this.offX;
        rtn.offY=this.offY;
        rtn.angle=this.angle;
        rtn.color=this.color;
        rtn.dcX=this.dcX;
        rtn.dcY=this.dcY;
        rtn.dcX2=this.dcX2;
        rtn.dcY2=this.dcY2;
        rtn.ddX=this.ddX;
        rtn.ddY=this.ddY;
        rtn.depth=this.depth;
        rtn.direction=this.direction;
        rtn.x2=this.x2;
        rtn.y2=this.y2;
        rtn.urlName=this.urlName;
        rtn.urlRegionX=this.urlRegionX;
        rtn.urlRegionY=this.urlRegionY;
        return (AFigure)rtn;
    }

    public void keyDown(int k)
    {
//        this.urlName.keyDown(k);
    }

    public void mouseMove(int x, int y)
    {
        super.mouseMove(x,y);
//        this.urlName.mouseMove(x,y);
    }

    public void mouseDown(int x, int y)
    {
        if(this.step==-10 && isSetURLRegion(x,y)){
            this.canvas.gui.openTextEditor(this);
            this.canvas.gui.textEditFrame.getTextArea().setText(urlName);
            return;
        }
        else
        if(this.step==-10) {
            this.selectBoxSens(x,y); 
        }
        super.mouseDown(x,y);
    }

    public boolean isSelected(int x, int y)
    {
        if(super.isSelected(x,y)) return true;
        if(this.isPlaying()) return false;
        return false;
    }

    public void newFig()
    {
        super.newFig();
        /*
        this.urlName=new Text(canvas);
        urlName.newFig();
        */
//        editState=0;
        init(canvas);
    }

    public boolean save(TemporalyText outS)
    {
        if(!saveHeader(outS,"clickableboxurl")) return false;
        if(!strmWrite(outS, ""+offX+","+offY+","
                             +x2+","+y2+",bp("+selectedBasePathBox+"),\""
                             +this.str2saveable(new StringBuffer(this.urlName))+"\")\n"))
                             return false;
         return true;
    }

    public ClickableBoxURL()
    {
    }

    public ClickableBoxURL(FigCanvas c)
    {
        init();
        canvas=c;
        init(c);
//        this.urlName=new Text(c);
    }

    public String urlName;


    public void actWhenPlaying()
    {
        SensorBoxWithLabel sb=(SensorBoxWithLabel)(paths.elementAt(this.selectedBasePathBox));
        String defaultPath=sb.getAttribute();
        if(defaultPath.equals("current")) {
//            this.canvas.editdispatch.clear();
        	this.canvas.editdispatch.bufferClear();
            this.canvas.editdispatch.read(this.canvas.editdispatch.baseDir+urlName);
        }
        else
        if(defaultPath.equals("net")){
//            this.canvas.editdispatch.clear();
        	this.canvas.editdispatch.bufferClear();
            this.canvas.editdispatch.read(urlName);
       	
        }
        else{
//            this.canvas.editdispatch.clear();
        	this.canvas.editdispatch.bufferClear();
            this.canvas.editdispatch.read(defaultPath + System.getProperty("file.separator") + urlName);
        }
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
            g.drawString("URL: "+this.urlName,xView(offX),yView(offY-2));
            g.drawRect(xView(offX),yView(offY-urlRegionY), urlRegionX,urlRegionY);
            g.setColor(Color.black);
            super.draw(g);
            g.setColor(cc);
            g.setFont(cf);
            this.drawSelectBoxs(g);
        }
    }
}
