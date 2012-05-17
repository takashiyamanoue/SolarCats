package webleap;

import java.util.Hashtable;

import com.google.soap.search.GoogleSearch;
import com.google.soap.search.GoogleSearchResult;
import com.google.soap.search.GoogleSearchResultElement;

public class GoogleAjaxSearchDriver 
implements java.lang.Runnable, webleap.Aggregate, webleap.SearchEngineDriver 
{
    public GoogleSearchResultElement resultElements[];
    public GoogleSearchResult result;
    public Settings settings;
    public String clientKey;
    public String keyword;
    public int frequency;
    public String optionString;
    public Hashtable searchedPhrases;
    public webleap.MessageReceiver messageReceiver;
    public MessageReceiver messageReceiver2;
    public int proxyPort;
    public String proxyName;
    public KwicTable kwicTable;

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
	
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
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

	public Iterator iterator() {
		// TODO 自動生成されたメソッド・スタブ
        return new GoogleAjaxSearchResultIterator(this);
	}

	public void setProxy(String pname, int port, String pkind, String password) {
		// TODO 自動生成されたメソッド・スタブ
        this.proxyName=pname;
        this.proxyPort=port;		
	}

	public void setMessageReceiver2(MessageReceiver receiver) {
		// TODO 自動生成されたメソッド・スタブ
        this.messageReceiver2=receiver;		
	}

	public SearchEngineDriver copyThis() {
		// TODO 自動生成されたメソッド・スタブ
        GoogleAjaxSearchDriver rtn=new GoogleAjaxSearchDriver();
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

	public void setKwicTable(KwicTable tab) {
		// TODO 自動生成されたメソッド・スタブ
        this.kwicTable=tab;
        this.kwicTable.setAggregate(this);
        this.kwicTable.setCache(this.searchedPhrases);
		
	}

	public void setSettings(Settings settings) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void setMessageReceiver(MessageReceiver receiver) {
		// TODO 自動生成されたメソッド・スタブ
        this.messageReceiver=receiver;		
	}

	public void setQueryOptions(String options) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public KwicTable getKWICtable() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public void setCache(Hashtable cache) {
		// TODO 自動生成されたメソッド・スタブ
        this.searchedPhrases=cache;		
	}

	public void searchTheEngine(String keyword) {
		// TODO 自動生成されたメソッド・スタブ
        GoogleDriver r=(GoogleDriver)(this.searchedPhrases.get(keyword));
        if(r!=null){
            int frequency=r.result.getEstimatedTotalResultsCount();
            this.messageReceiver.receiveMessage("frequency "+frequency);
            return;
        }
   
        this.keyword=keyword;
        this.start();		
	}

	public int getFrequency() {
		// TODO 自動生成されたメソッド・スタブ
        return this.frequency;
	}
	Thread me;
    public void start()
    {
        if(me!=null) me.start();
    }

}
