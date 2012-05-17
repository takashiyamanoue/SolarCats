package application.lisp;

import java.awt.Color;
import java.util.Hashtable;

import javax.swing.JTextArea;

import language.ArrayManager;
import language.BasicParser;
import language.CQueue;
import language.LispObject;
import language.ListCell;
import language.Parser;
import language.PrintS;
import language.ReadLine;
import language.ReadS;
import language.SourceManager;
import controlledparts.FrameWithLanguageProcessor;
import application.basic.Basic;

public class Lisp extends Basic {

    public Lisp(JTextArea inputArea, JTextArea outputArea, 
            CQueue q, FrameWithLanguageProcessor bf)
 {
     super(inputArea, outputArea, q, bf);
 }    
    public void init(JTextArea rarea, JTextArea parea,CQueue iq, FrameWithLanguageProcessor g)
    {
         me=null;
         inqueue=iq;
        symbolTable=new Hashtable();
        nilSymbol  = recSymbol("nil");
        environment=cons(nilSymbol,nilSymbol);
        tSymbol    = recSymbol("t");
        initSymbols();
        initFunctionDispatcher();
    //    inqueue=iq;
    //    outqueue=oq;
        readArea=rarea;
        printArea=parea;

        read=new ReadS(inqueue,this);
        print=new PrintS(this);
        gui=g;
    }
    public void run()
    {
            try{
               LispObject r=preEval(evaluatingList,environment); //eval the S expression
               String o=print.print(r);
               printArea.append(o+"\n");               
            }
            catch(Exception e){
               if(this.gui.stopFlagIsOn()){
                   this.gui.resetStopFlag();
                   printArea.append("STOP\n");
                   
               }
            }
        printArea.append("OK\n");
        printArea.setCaretPosition(printArea.getText().length());
        me=null;

    }
    public LispObject evalAtomForm(LispObject form, LispObject env){
    	LispObject rtn=null;
        if(numberp(form)) rtn= form;
        else
        if(eq(tSymbol,form)) rtn= tSymbol;
        else
        if(eq(nilSymbol,form)) rtn= nilSymbol;
        else{
           LispObject w=assoc(form,((ListCell)env).a);
           if(Null(w)){
             plist("can not find out ",form);
             return nilSymbol;
           }
           /*
           if(!atom(second(w))){
              rtn=nilSymbol;
              if(eq(recSymbol("dimension"),car(second(w)))){
                 rtn= form;
              }
           }
           else
           */
           rtn= second(w);
        }    	
        return rtn;
    }
    public void clearEnvironment()
    {
        environment=cons(nilSymbol,nilSymbol);
    }

}
