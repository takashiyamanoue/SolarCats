package webleap;

import java.util.StringTokenizer;
import java.util.Vector;

import language.HtmlTokenizer;
import language.HtmlTokenizerPlus;


class GeneralResultIterator implements webleap.Iterator  
{
    public GeneralDriver driver;
    Vector resultItems;
    HtmlTokenizer htmlTokenizer;
    Vector listTokens;
    Vector itemTokens;
    boolean hasMoreItems=true;
    String itemPattern;
    String parameterPositions;

    public int index;

    public GeneralResultIterator(GeneralDriver driver)
    {
        this.driver=driver;
        this.index=0;
        this.makeResultList();
        this.itemPattern=this.driver.getListItemPattern();
        this.parameterPositions=this.driver.getParameterPositions();
    }
   
    public boolean hasNext()
    {
        // This method is derived from interface webleap.Iterator
        // to do: code goes here
    	if(this.listTokens==null)return false;
    	if(this.itemTokens==null) return false;
    	if(this.index>this.listTokens.size()) return false;
    	if(!this.hasMoreItems) return false;
//        if(driver.getResultElementAt(index)==null) return false;
        return true;
    }

    public Object next()
    {
        // This method is derived from interface webleap.Iterator
        // to do: code goes here
    	Vector results=new Vector();
    	index=this.indexOfThePattern(this.itemTokens,results,index);
    	SearchResultElement elm=makeSearchResultElement(results);
        return (Object)elm;
    }
    public SearchResultElement makeSearchResultElement(Vector r){
    	/*
        SearchResultElement rtn=new SearchResultElement(
                e.getTitle(),
                e.getURL(),
                e.getSnippet()
             
             );
             */
    	StringTokenizer st=new StringTokenizer(this.parameterPositions,",");
    	try{
        	int iurl=(new Integer(st.nextToken())).intValue();
        	int ititle=(new Integer(st.nextToken())).intValue();
    	    int il=(new Integer(st.nextToken())).intValue();
    	    int ix=(new Integer(st.nextToken())).intValue();
    	    int ir=(new Integer(st.nextToken())).intValue();
    	    String snippet=(String)(r.elementAt(il))+"<strong>"+(String)(r.elementAt(ix))+"</strong>"+(String)(r.elementAt(ir));
    	    String xurl=(String)(r.elementAt(iurl));
    	    StringTokenizer urlst=new StringTokenizer(xurl);
    	    String xxurl=urlst.nextToken();
    	    SearchResultElement rtn=new SearchResultElement(
    			(String)(r.elementAt(ititle)),
//    			(String)(r.elementAt(iurl)),
    			xxurl,
    			(String)(snippet)
    			);
    	   	return rtn;
    	}
    	catch(Exception e){
    	    SearchResultElement rtn=new SearchResultElement(
        			"",
        			"",
        			"N/A"
        			);
        	   	return rtn;    		
    	}
    }
    public void makeResultList(){
    	this.resultItems=new Vector();
    	this.htmlTokenizer=new HtmlTokenizer(this.driver.html);
    	this.listTokens=htmlTokenizer.tokens;
    	String listPattern=this.driver.getListItemPattern();
    	HtmlTokenizerPlus listToken=new HtmlTokenizerPlus(listPattern);
    	this.itemTokens=listToken.tokens;
    	index=0;
    }
    public int indexOfThePattern(Vector p, Vector r, int j)
    {
    	if(this.listTokens==null) return -1;
        int i,k,l=0;
        // i... index of p;
        // j+k ... index of wordSequence
        // l ... index of r

        int xl=0;

        int plength=p.size();
        while(true){
            i=0; k=0;
            String x="";
           while(i<plength){
              if(j+k>=this.listTokens.size()) {
                if(i==plength) // pattern is matched
                {
                  xl=l;
                  r.setSize(l);
                }
                else {
                  r.setSize(xl);
                  l=xl;  // back the counter.
                }
                this.hasMoreItems=false;
                return j+k;
              }
               String matcher=(String)(p.elementAt(i));
               String matchee=(String)(this.listTokens.elementAt(j+k));
              if(matcher.equals("*")){
            	  String matcher1=(String)(p.elementAt(i+1));
            	  if(matcher1.equals(matchee)){
            		  r.addElement(x);
            		  i++; l++;
            		  x="";
            	  }
            	  else{
            		  x=x+matchee+" ";
                     k++;
            	  }
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
                return j+k;
              }
            else {
                r.setSize(xl);
                l=xl;  // back the counter.
            }
            j++;
        }
    }
}