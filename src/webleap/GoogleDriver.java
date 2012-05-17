package webleap;

import java.util.Hashtable;

import com.google.soap.search.GoogleSearch;
import com.google.soap.search.GoogleSearchResult;
import com.google.soap.search.GoogleSearchResultElement;


class GoogleDriver implements java.lang.Runnable, webleap.Aggregate, webleap.SearchEngineDriver 
{
    public int frequency;

    public SearchResultElement getResultElementAt(int i)
    {
        if(this.result==null) return null;
        if(i>this.resultElements.length-1) return null;
        GoogleSearchResultElement e=this.resultElements[i];
        if(e==null) return null;
        SearchResultElement rtn=new SearchResultElement(
               e.getTitle(),
               e.getURL(),
               e.getSnippet()
            
            );
        
        return rtn;
    }

    public Iterator iterator()
    {
        // This method is derived from interface webleap.Aggregate
        // to do: code goes here
        return new GoogleResultIterator(this);
    }

    public GoogleSearchResultElement resultElements[];

    public GoogleSearchResult result;

    public String keyword;

    public void start()
    {
        if(me!=null) me.start();
    }

    public Thread me;

    public void run()
    {
       try {
	    GoogleSearch gs;
	    gs = new GoogleSearch();
	    this.clientKey=this.settings.googleLicenceKeyField.getText();
	    gs.setKey(this.clientKey);
	    gs.setFilter(true);
	    gs.setLanguageRestricts("lang_en");
	    
	    String query="\"" + keyword + "\"" + " filetype:html";
	    if(!this.optionString.equals("")) query=query+" "+this.optionString;
	    gs.setQueryString(query);
	    result = gs.doSearch();
	    frequency=result.getEstimatedTotalResultsCount();
	    this.searchedPhrases.put(keyword,this);
	    this.messageReceiver.receiveMessage("frequency "+frequency);
	    this.resultElements=this.result.getResultElements();
	    
	   }
	   catch(Exception e){System.out.println(e.toString());
	      this.messageReceiver2.receiveMessage(e.toString());
	   }
    }

    public int proxyPort;

    public String proxyName;

    public void setProxy(String pname, int port,String pkind, String password)
    {
        this.proxyName=pname;
        this.proxyPort=port;
    }

    public MessageReceiver messageReceiver2;

    public void setMessageReceiver2(MessageReceiver r)
    {
        this.messageReceiver2=r;
    }

    public SearchEngineDriver copyThis()
    {
        GoogleDriver rtn=new GoogleDriver();
        rtn.clientKey=this.clientKey;
        rtn.setMessageReceiver(this.messageReceiver);
//        rtn.optionString=this.optionString;
        rtn.setQueryOptions(this.optionString);
        rtn.setCache(this.searchedPhrases);
        rtn.setKwicTable(this.kwicTable);
        rtn.setSettings(this.settings);
        rtn.setMessageReceiver2(this.messageReceiver2);
        rtn.setProxy(this.proxyName,this.proxyPort,"webleap","");
        return (SearchEngineDriver)rtn;
    }


    public Settings settings;

    public void setSettings(Settings s)
    {
        this.settings=s;
    }

    public KwicTable kwicTable;

    public void setKwicTable(KwicTable tab)
    {
        this.kwicTable=tab;
        this.kwicTable.setAggregate(this);
        this.kwicTable.setCache(this.searchedPhrases);
        
//        this.qwicTable.setCache(this.searchedPhrases);
    }

    public void setQueryOptions(String opt)
    {
        this.optionString=opt;
    }

    public webleap.MessageReceiver messageReceiver;

    public void setMessageReceiver(MessageReceiver r)
    {
        this.messageReceiver=r;
    }

    public void setCache(Hashtable table)
    {
        this.searchedPhrases=table;
    }

    public KwicTable getKWICtable()
    {
        return this.kwicTable;
    }

    public String optionString;

    public void searchTheEngine(String keyword)
    {
        GoogleDriver r=(GoogleDriver)(this.searchedPhrases.get(keyword));
        if(r!=null){
            int frequency=r.result.getEstimatedTotalResultsCount();
            this.messageReceiver.receiveMessage("frequency "+frequency);
            return;
        }
   
        this.keyword=keyword;
        this.start();
    }


    public String clientKey;

    public GoogleDriver()
    {
      me=new Thread(this,"google driver");
    }

    public Hashtable searchedPhrases;

    public int getFrequency()
    {
        // This method is derived from interface webleap.SearchEngineDriver
        // to do: code goes here
        return this.frequency;
    }

}