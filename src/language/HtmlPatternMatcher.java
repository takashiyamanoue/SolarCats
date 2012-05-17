package language;
import java.util.Vector;

import application.webleap.WebLEAPFrame;
public class HtmlPatternMatcher extends webleap.PatternMatcher
{

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
              }
            else {
                r.setSize(xl);
                l=xl;  // back the counter.
            }
            j++;
        }
    }
    public void pattern(String p, Vector r)
    {
        Vector pv=new Vector();
        tokenize(p,pv);
        pattern(pv,r);
    }
    public WebLEAPFrame gui;
    public HtmlPatternMatcher(WebLEAPFrame g)
    {
        gui=g;
    }
    public void tokenize(String input, Vector tokend)
    {

        HtmlTokenizer ht=new HtmlTokenizer(input);
        int l=0;
        while (ht.hasMoreTokens()) {
            try{
                String next=ht.nextToken();
                tokend.addElement(next);
            }
            catch(ArrayIndexOutOfBoundsException e){
                System.out.println(e);
            }
                l++;
                if((l%100)==0){
                    /*
                    if(gui.messageArea!=null){
                        gui.messageArea.setText(""+l+" words were tokenized.\n");
                        
                    }*/
                        gui.addMessage(""+l+" words were tokenized.\n");
                }
        }
        /*
        if(gui.messageArea!=null){
           gui.messageArea.setText(""+l+" workds were tokenized.\n");
        }
        */
        gui.addMessage(""+l+" words were tokenized.\n");

    }
    public Vector tokendPattern;
    public Vector tokendHtml;
    public String theText;
    public void comment()
    {
        /*

        input
          text  ... html format
                    ex.
                        <a href=" ... "> ... </a>


                        "<a" "href" "=" "\" ... \"" ">" ... "</a" ">"
        */
    }

     public void init(String matched)
    {
        tokendHtml=new Vector();
        tokenize(matched,tokendHtml);
    }

}

