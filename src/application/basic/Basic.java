package application.basic;

import java.awt.Color;
import java.util.Hashtable;

import javax.swing.JTextArea;

import language.ALisp;
import language.CQueue;
import language.LispObject;
import language.MyNumber;
import language.MyString;
import application.draw.ALine;
import application.draw.DrawFrame;
import controlledparts.ControlledFrame;
import controlledparts.FrameWithLanguageProcessor;

public class Basic extends language.ABasic
{
	Hashtable waitingTable;
    public LispObject evaluatingList;
    String results="";
    String waitingFunction="";
    
    public void setWaitingFunction(String s){
    	this.waitingFunction=s;
    }

    public void run()
    {
        while(!Null(evaluatingList)){
            LispObject s=car(evaluatingList);
            try{
               LispObject r=preEval(s,environment); //eval the S expression
//               this.results=print.print(r);
//               System.out.println("results="+results);
            }
            catch(Exception e){
               if(this.gui.stopFlagIsOn()){
                   this.gui.resetStopFlag();
                   printArea.append("STOP\n");
                   break;
               }
            }
            evaluatingList=cdr(evaluatingList);
        }
        printArea.append("OK\n");
        printArea.setCaretPosition(printArea.getText().length());
        me=null;
    }

    public synchronized void evalList(LispObject x)
    {
        this.evaluatingList=x;
        start();
    }

    public String getResult(){
    	return results;
    }
    public void setResult(String x){
    	this.results=x;
        synchronized(waitingTable){
     	   this.waitingTable.put(waitingFunction,results);
        }
        this.waitingFunction="";
//    	System.out.println("results="+results);
    }
    public LispObject applyMiscOperation(LispObject proc, LispObject argl)
    {
        LispObject x=super.applyMiscOperation(proc,argl);
        if(x!=null) return x;
        if(eq(proc,recSymbol("ex"))){
                String appliname=print.print(car(argl));
                String command=print.print(second(argl));
                ControlledFrame appli=this.basicFrame.lookUp(appliname);
                String rtn=appli.parseCommand(command);
                MyString val=new MyString(rtn);
                return val;
        }

        return null;
    }

    FrameWithLanguageProcessor basicFrame;
    
    public Basic(JTextArea inputArea, JTextArea outputArea, 
              CQueue q, FrameWithLanguageProcessor bf)
   {
       super(inputArea, outputArea, q, bf);
       basicFrame=bf;
       this.waitingTable=new Hashtable();
   }    
    
    int maxwait=100;
	public String waitForResult(String x)
	{   
		System.out.println("...waiting "+x);
		for(int i=0;i<maxwait;i++){
			synchronized(waitingTable){
			    String rtn=(String)(this.waitingTable.get(x));
			    System.out.println(" ... return "+rtn);
			    if(rtn!=null) {
				   this.waitingTable.remove(x);
				   return rtn;
			    }
			}
			try{
				Thread.sleep(10);
			}
			catch(Exception e){}
		}
		System.out.println("time out");
		return "time-out";
	}
    public LispObject applyGraphicsOperation(LispObject proc, LispObject argl)
    {
        /*
             graphics functions
        */
          // (line x1 y1 x2 y2 color)
          //
             if(eq(proc,recSymbol("line"))){
                int x1,y1,x2,y2,cx;
                Color cc,color;
                x1=(int)(((MyNumber)(ALisp.car(argl))).getInt());
                y1=(int)(((MyNumber)(ALisp.second(argl))).getInt());
                x2=(int)(((MyNumber)(ALisp.third(argl))).getInt());
                y2=(int)(((MyNumber)(ALisp.fourth(argl))).getInt());
                previousX=x2; previousY=y2;
                cx=(int)(((MyNumber)(ALisp.fifth(argl))).getInt());
 
                drawLine(x1,y1,x2,y2,cx);
 
                LispObject x=cons(proc,argl);

                return x;

             }
             
             // (lineto x2 y2 color)
             //
             if(eq(proc,recSymbol("lineto"))){
                
               int x1,y1,x2,y2,cx;
               Color cc,color;

               x1=previousX;
               y1=previousY;
               x2=(int)(((MyNumber)(ALisp.car(argl))).getInt());
               y2=(int)(((MyNumber)(ALisp.second(argl))).getInt());
               previousX=x2; previousY=y2;
               cx=(int)(((MyNumber)(ALisp.third(argl))).getInt());

               drawLine(x1,y1,x2,y2,cx);
               
                LispObject x=cons(proc,argl);
                return x;

             }
             
             
             //  (pset x y color)
             //
              if(eq(proc,recSymbol("pset"))){
                  int x1,y1,x2,y2,cx;
                  Color cc,color;

                  x2=(int)(((MyNumber)(ALisp.car(argl))).getInt());
                  y2=(int)(((MyNumber)(ALisp.second(argl))).getInt());
                  previousX=x2; previousY=y2;
                  x1=x2; y1=y2;
                  cx=(int)(((MyNumber)(ALisp.third(argl))).getInt());

                  drawLine(x1,y1,x2,y2,cx);
                  

                LispObject x=cons(proc,argl);
                return x;

             }
        return null;
    }
    public void drawLine(int x1, int y1, int x2, int y2, int cx)
    {
    	/*
            if(this.basicFrame.draw==null) {
                DrawFrame d=(DrawFrame)((this.basicFrame.communicationNode.applicationManager.getRunningApplication("DrawFrame")).application);
                if(d==null) return;
                this.basicFrame.draw=d;
            }
            */
    	       DrawFrame draw=basicFrame.getDrawFrame();

                ALine aline=new ALine(draw.canvas);
                aline.offX=x1;  aline.offY=y1;
                aline.x2=x2-x1; aline.y2=y2-y1;
                aline.color=colors[cx];
                aline.step=-10;
                aline.showhide=true;
                aline.selected=false;
                draw.canvas.fs.add(aline);
                draw.repaint();
    }

}