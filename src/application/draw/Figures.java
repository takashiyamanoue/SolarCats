/*
NetPaint

     by T. Yamanoue,
     Kyushu Institute of Technology, Japan,
     Aug.1, 1997

 
   A Paint tool for the Internet.
   
   Drawing tool on a Web brouser.
   A Co-operative drawing tool.
   Drawing a paint on the Internet by linking parts
   
   
*/
package application.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import nodesystem.TemporalyText;

public class Figures extends AFigure
{
    public void sendFig()
    {
        TemporalyText out=new TemporalyText();
       FigList p;
       p=fp;
       this.canvas.gui.sendEvent("fig.start.\n");
       while(p!=null){
        out.setText("");
        p.fig.save(out);
        String afig=out.getText();
        this.canvas.gui.sendEvent("fig."+afig);
        p=p.next;
       }
       this.canvas.gui.sendEvent("fig.end.\n");
       return;
    }

    public int getXmax(){
        FigList p=fp;
        int rtn=p.fig.getXmax();
        p=p.next;
        while(p!=null){
        	int w=p.fig.getXmax();
        	if(w>rtn){
        		rtn=w;
        	}
            p=p.next;
        }
        return rtn;
    }
    public int getYmax(){
        FigList p=fp;
        int rtn=p.fig.getYmax();
        p=p.next;
        while(p!=null){
        	int w=p.fig.getYmax();
        	if(w>rtn){
        		rtn=w;
        	}
            p=p.next;
        }
        return rtn; 
    }
    public int getXmin(){
        FigList p=fp;
        int rtn=p.fig.getXmin();
        p=p.next;
        while(p!=null){
        	int w=p.fig.getXmin();
        	if(w<rtn){
        		rtn=w;
        	}
            p=p.next;
        }
        return rtn;
    	
    }
    public int getYmin(){
        FigList p=fp;
        int rtn=p.fig.getYmin();
        p=p.next;
        while(p!=null){
        	int w=p.fig.getYmin();
        	if(w<rtn){
        		rtn=w;
        	}
            p=p.next;
        }
        return rtn; 
   	
    }
    public void move(int x, int y)
    {
        FigList p=fp;
        while(p!=null){
            p.fig.offX+=x;
            p.fig.offY+=y;
            p=p.next;
        }
    }

    public void setFont(Font f)
    {
        FigList p=fp;
        while(p!=null){
            p.fig.setFont(f);
            p=p.next;
        }
    }

    public void setWidth(int w)
    {
        FigList p=fp;
        while(p!=null){
            p.fig.lineWidth=w;
            p=p.next;
        }
    }

    public void setDepth(int depth)
    {
        FigList p=fp;
        if(fp==null) return;
        int df=depth-p.fig.depth;
        while(p!=null){
            if(p.fig.depth+df>=0&&
               p.fig.depth+df<=30)
            p.fig.depth+=df;
            p=p.next;
        }
    }
    public void drawDepthITemp(Graphics g, int i)
    {
       FigList p;
        p=fp;
        while(p!=null){
            if(p.fig.depth==i) p.fig.drawTemp(g);
            p=p.next;
        }
    }
    public AFigure copyThis()
    {
        Figures fs;
        fs=(Figures)(super.copyThis2(new Figures(canvas)));
        fs.fp=null;
        FigList p1=fp;
        if(fp==null) return fs;
        FigList p2=new FigList();
        p2.fig=p1.fig.copyThis();
        p2.fig.offX=p2.fig.offX+20;
        p2.fig.offY=p2.fig.offY+20;
        p2.next=null;
        fs.fp=p2;
        p1=p1.next;
        while(p1!=null){
          AFigure f=p1.fig.copyThis();
          FigList p3=new FigList(); p3.fig=f; p3.next=null;
          p3.fig.offX=p3.fig.offX+20;
          p3.fig.offY=p3.fig.offY+20;
          p2.next=p3;
          p2=p3;
          p1=p1.next;
        }
        return fs;
    }
    public void mouseDown2(int x, int y)
    {
        FigList p=fp;
        if(p==null) return;
        int xx=p.fig.offX;
        int yy=p.fig.offY;
        p.fig.mouseDown(x,y);
        p=p.next;
        while(p!=null){
            int xxx=p.fig.offX;
            int yyy=p.fig.offY;
            p.fig.mouseDown(x+xxx-xx,y+yyy-yy);
            p=p.next;
        }
    }
    public void mouseMove2(int  x, int y)
    {
        FigList p=fp;
        if(p==null) return;
        int xx=p.fig.offX;
        int yy=p.fig.offY;
        p.fig.mouseMove(x,y);
        p=p.next;
        while(p!=null){
            int xxx=p.fig.offX;
            int yyy=p.fig.offY;
            p.fig.mouseMove(x+xxx-xx,y+yyy-yy);
            p=p.next;
        }
    }
    public void selectArea(int wx1, int wy1, int wx2, int wy2)
    {
           FigList p=fp;
           while(p!=null){
              p.fig.selectArea(wx1,wy1,wx2,wy2);
              p=p.next;
           }
    }
    public void stop()
    {
        FigList p=fp;
        while(p!=null){
            p.fig.stop();
            p=p.next;
        }
    }
    public void start()
    {
        FigList p=fp;
        while(p!=null){
            p.fig.start();
            p=p.next;
        }
    }
    public void setStep(int s)
    {
        FigList p=fp;
        while(p!=null){
            p.fig.step=s;
            p.fig.immediateActionAfterSetStep();
            p=p.next;
        }
    }
    public void mouseUp(int x, int y)
    {
        FigList p=fp;
        while(p!=null){
            p.fig.mouseUp(x,y);
            p=p.next;
        }
    }
    public void mouseExit()
    {
        FigList p=fp;
        while(p!=null){
            p.fig.mouseExit();
            p=p.next;
        }
    }
    public void mouseEnter(int x, int y)
    {
        FigList p=fp;
        while(p!=null){
            p.fig.mouseEnter(x,y);
            p=p.next;
        }
    }
    public void mouseDrag(int x, int y)
    {
        FigList p=fp;
        while(p!=null){
            p.fig.mouseDrag(x,y);
            p=p.next;
        }
    }
    public void keyDown(int key)
    {
        FigList p=fp;
        while(p!=null){
            p.fig.keyDown(key);
            p=p.next;
        }
    }
    public void mouseMove(int x, int y)
    {
        FigList p=fp;
        while(p!=null){
            p.fig.mouseMove(x,y);
            p=p.next;
        }
    }
    public void mouseDown(int x, int y)
    {
        FigList p=fp;
        while(p!=null){
            p.fig.mouseDown(x,y);
            p=p.next;
        }
    }
    public void setColor(Color cc)
    {
        FigList p=fp;
        while(p!=null){
            p.fig.color=cc;
            p=p.next;
        }
    }
    public void add(Figures fs)
    {
        FigList p=fs.fp;
        while(p!=null){
            add(p.fig);
            p=p.next;
        }
    }
    public void drawDepthI(Graphics g, int i)
    {
        FigList p;
        p=fp;
        while(p!=null){
            if(p.fig.depth==i) p.fig.draw(g);
            p=p.next;
        }

    }
 /*
    public boolean save(DataOutputStream outS)
*/
    public synchronized boolean save(TemporalyText outS)
    {
       FigList p;
       p=fp;
       if (!strmWrite(outS,"figures(\n")){
         // this.notifyAll();
         return false;}
       while(p!=null){
        if(p.fig.save(outS)) p=p.next;
        else return false;
       }
       if(!strmWrite(outS,")\n")) {
        // this.notifyAll();
        return false;}
//       this.notifyAll();
       return true;
    }
    public void draw(Graphics g)
    {
    	if(g==null) return;
    	if(canvas==null) return;
    	if(canvas.editdispatch==null) return;
        int max=canvas.editdispatch.npMain.depthIndicator.getMaximum();
        int i;
        for(i=0;i<max+1;i++){
            drawDepthI(g,max-i);
        }
    }
    public void drawTemp(Graphics g)
    {
        int max=canvas.editdispatch.npMain.maxDepth;
        int i;
        for(i=0;i<max+1;i++){
            drawDepthITemp(g,max-i);
        }
    }
    public void remove(AFigure f)
    {
        FigList p1,p2;
        p1=fp;
        if(p1==null) return;
        if(p1.fig==f) {p1.fig.stop(); fp=p1.next; return;}
        p2=fp;
        p1=p1.next;
        while(p1!=null){
            if(p1.fig==f){p1.fig.stop(); p2.next=p1.next; return;}
            p1=p1.next; p2=p2.next;
        }
    }
    public AFigure select(int x, int y)
    {
        FigList p;
        AFigure fs;
        p=fp;
        fs=null;
        while(p!=null) {
            if(p.fig==null) writeMessage("Figures.select, p.fig is null\n");
            if(p.fig.isSelected(x,y)&&(fs==null)){
                p.fig.selected=true;
                fs=p.fig;}
            else p.fig.selected=false;
            p=p.next;
        }
        return fs;
    }
    public void add(AFigure f)
    {
        FigList p,p1,p2;
        p=new FigList();
        p.fig=f; p.next=null;
        p2=fp;
        if(p2==null) {fp=p; return;}
        p1=p2.next;
        while(p1!=null){p2=p2.next; p1=p1.next;}
        p2.next=p;
    }
    public Figures(FigCanvas f)
    {
        fp=null;
        canvas=f;
    }
    public FigList fp   ;
}

