package application.draw;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Hashtable;

import nodesystem.AMessage;

//import util.VideoOperator.XYWH;
public class ImageManager extends java.lang.Object implements Runnable
{
	int elementWidth;
    public void setImageOperator(String name,ImageOperator img)
    {
        imageOperators.put(name,img);
    }

    public void init()
    {
        imageOperators=new Hashtable();
        imageCount=0;
        first=null;
        last=null;
    }
    public DrawFrame gui; //Draw
    public ImageOperator getImageOperator(String name)
    {
        ImageOperator imgOperator=(ImageOperator)(imageOperators.get(name));
        return imgOperator;
    }
    private synchronized boolean send(ImageOperator p)
    {
        if(gui==null) {
//            this.notifyAll();
            return false;}
//        if(!(this.gui.isControlledByLocalUser())) return false;
        while(!(this.gui.isControlledByLocalUser())){
        	try{
        		Thread.sleep(1);
        	}
        	catch(Exception e){}
        }
        AMessage img=p.nextImage();
        if(img!=null){
            if(img.getHead().equals("")){
            	return true;
            }
        	String h="img("+img.getHead()+")\n";
        	img.setHead(h);
            gui.sendEvent(img);
            return true;
        }
        return false;
//        this.notifyAll();
    }
    public void stop()
    {
        if(me!=null){
//            me.stop();
            me=null;
        }
    }
    public void start()
    {
    	/*
        if(me==null){
            me=new Thread(this,"ImangeManager");
            me.start();
        }
        */
    	run();
    }
    public boolean isRunning(){
    	/*
    	if(this.me==null) return true;
    	return false;
    	*/
        return this.runningFlag;
    }
    boolean runningFlag=false;
    public void run()
    {
        int count=0;
        int maxcount=5;
        if(gui==null) {me=null; return;}
        this.gui.setStoppingLockTimer(true);
//        this.gui.mutex.stop();
//        while(me!=null){
        runningFlag=true;
        while(runningFlag){
            if(gui==null) {me=null; return;}
            ImageOperator p=first;
            if(p==null){
                  try{
                      Thread.sleep(5);
                  }
                  catch(InterruptedException e){
                    System.out.println(e);
                  }              
            }
            boolean cont=false;
            cont=false;
            while(p!=null){
            	boolean wc=send(p);
                cont=cont|wc;
                if(count>maxcount){
                   
                  try{
                      Thread.sleep(1);
                  }
                  catch(InterruptedException e){
                    System.out.println(e);
                  }
                  
                  count=0;
                }
                else count++;
                p=p.next;
            }
            if(!cont){
            	this.gui.setStoppingLockTimer(false);
            	this.me=null;
            	runningFlag=false;
            	return;
            }
        }
    }
    public Thread me;
    public FigCanvas canvas;
    public ImageManager(FigCanvas fc, DrawFrame gui) //Draw gui
    {
        init();
        canvas=fc;
        this.gui=gui;
        this.elementWidth=elementWidth;
    }
    public int imageCount;
    public String newName()
    {
        String rtn= "image"+imageCount;
        imageCount++;
        System.out.println("new image ..name:"+rtn);
        return rtn;
    }
    public void add(ImageOperator io)
    {
    	ImageOperator opr=this.getImageOperator(io.name);
    	if(opr!=null){
    		this.imageOperators.remove(opr);
    	}
        imageOperators.put(io.name,io);
        if(first==null) {
            first=io;
            io.next=null;
            last=io;
            return;
        }
        last.next=io;
        io.next=null;
        last=io;
    }
    public ImageOperator last;
    public ImageOperator first;
    public Hashtable imageOperators;

}

