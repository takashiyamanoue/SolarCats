package webleap;

import java.awt.TextArea;
import java.util.StringTokenizer;
import java.util.Vector;

import language.HtmlTokenizer;
import language.HtmlTokenizerPlus;

public class PatternMatcher 
{
	HtmlTokenizerPlus htmlTokenizer;
    public void init(Vector wordSequence)
    {
        this.wordSequence=wordSequence;
        this.sequenceLength=wordSequence.size();
    }

    Vector tokens;
    Vector getTokens(){
    	return this.tokens;
    }
    
    public void init(String s)
    {
//        init(s,"\t\r\n" );
    	this.htmlTokenizer=new HtmlTokenizerPlus(s);
    	this.init(this.htmlTokenizer.tokens);
    	this.tokens=this.htmlTokenizer.tokens;
    }
    public void pattern(String p, Vector r)
    {
    	/*
    	StringTokenizer st=new StringTokenizer(p," \n\r\t");
        Vector pv=new Vector();
        while (st.hasMoreTokens()) {
            try{
                String next=st.nextToken();
                pv.addElement(next);
            }
            catch(ArrayIndexOutOfBoundsException e){
                System.out.println(e);
            }
        }
        */
    	HtmlTokenizerPlus st=new HtmlTokenizerPlus(p);
    	Vector pv=st.tokens;
        pattern(pv,r);
    }
    public TextArea output;
    public void pattern(Vector p, Vector r)
    {
        int i,j=0,k,l=0;
        // i... index of p;
        // j+k ... index of wordSequence
        // l ... index of r

        int xl=0;

        int plength=p.size();
        while(true){
            i=0; k=0;
            while(i<plength){
              if(j+k>=sequenceLength) {
                if(i==plength) // pattern is matched
                {
                  xl=l;
                  r.setSize(l);
                }
                else {
                  r.setSize(xl);
                  l=xl;  // back the counter.
                }
                return;
              }
               String matcher=(String)(p.elementAt(i));
               String matchee=(String)(wordSequence.elementAt(j+k));
              if(matcher.equals("*")){
                  r.addElement(matchee);
                  l++; i++; k++;
              }
              else
              if(matcher.equals(matchee)){
                i++; k++;
              }
              else break;
            }

            if(i==plength) // pattern is matched
              {
                xl=l;
                r.setSize(l);
                return;
              }
            else {
                r.setSize(xl);
                l=xl;  // back the counter.
            }
            j++;
        }
    }

    public int sequenceLength;
    public void comment()
    {
        /*
        Sentence pattern matcher.

        ex.

          wordSequence = { "this" "is" "a" "pen" "." "this" "is" "an" "apple" "." ...}

          pattern({"this" "is" "*" "*"},results)

          results={"a" "pen" "an" "apple"}




        */

    }
    /*
    public void init(String matched,String breakSymbols)
    {
        tokenizer=new StringTokenizer(matched,breakSymbols);
        wordSequence=new Vector();
        sequenceLength=0;
        while (tokenizer.hasMoreTokens()) {
            try{
                String next=tokenizer.nextToken();
                wordSequence.addElement(next);
            }
            catch(ArrayIndexOutOfBoundsException e){
                System.out.println(e);
            }
                sequenceLength++;
                if((sequenceLength%100)==0){
                    if(output!=null){
                        output.setText(""+sequenceLength+" words were tokenized.\n");
                    }
                }
        }
        if(output!=null){
            output.setText(""+sequenceLength+" workds were tokenized.\n");
        }

    }
    */
    public Vector wordSequence;
    public StringTokenizer tokenizer;
}

