package webleap;


import java.awt.TextArea;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

import language.HtmlTokenizer;
public class GeneralDriver implements webleap.Aggregate, webleap.MessageReceiver, webleap.SearchEngineDriver 
{
    public String html;
    String passWord;

    protected static void showElement(Element e)
    throws IOException, BadLocationException
    {
        String name = e.getName();
        if( name.equals("content")){
            int start = e.getStartOffset();
            int end   = e.getEndOffset();
            Document doc = e.getDocument();
            System.out.print("["+doc.getText( start, end - start) + "]");
        }
        else{
            AttributeSet a = e.getAttributes();
            Enumeration enm = a.getAttributeNames();
            while( enm.hasMoreElements()){
                Object o = enm.nextElement();
                String subName = o.toString();
                Object v = a.getAttribute(o);
                String value = v.toString();
                System.out.print("{ "+subName + ":" + value + " } ");
            }
        }
        System.out.println();
        int children = e.getElementCount();
        for(int i=0; i<children; i++){
            showElement(e.getElement(i));
        }
    }

    public static void showModel(Document doc)
    {
        try{
            Element root = doc.getDefaultRootElement();
            showElement(root);
        }
        catch(IOException e){
            System.out.println("I/O Exception ");
        }
        catch( BadLocationException e){
            System.out.println("bad location exception");
        }
    }

    public String keyword;

    public int frequency;

    public SearchResultElement getResultElementAt(int i)
    {
        return null;
    }

    public Iterator iterator()
    {
        // This method is derived from interface webleap.Aggregate
        // to do: code goes here
        JEditorPane x=new JEditorPane("text/html", this.html);
        Document doc = x.getDocument();
        showModel(doc);
        return new GeneralResultIterator(this);
    }

    Vector resultItems;

    public void setMessageReceiver2(MessageReceiver r2)
    {
        this.messageReceiver2=r2;
    }

    public MessageReceiver messageReceiver2;


    public SearchEngineDriver copyThis()
    {
        GeneralDriver rtn=new GeneralDriver();
        rtn.setMessageReceiver(this.messageReceiver);
        rtn.setSettings(this.settings);
        rtn.setQueryOptions("");
        rtn.setCache(this.searchedPhrases);
        rtn.setKwicTable(this.getKWICtable());
//        rtn.gui=this.gui;
        rtn.proxyName=this.proxyName;
        rtn.proxyPort=this.proxyPort;
        rtn.setMessageReceiver2(this.messageReceiver2);
//        rtn.visualizer=this.visualizer;
        return (SearchEngineDriver)rtn;
    }


    public int getFrequency()
    {
        // This method is derived from interface webleap.SearchEngineDriver
        // to do: code goes here
        return this.frequency;
    }

    public void setQueryOptions(String options)
    {
        // This method is derived from interface webleap.SearchEngineDriver
        // to do: code goes here
    }

    public KwicTable kwicTable;

    public void setKwicTable(KwicTable ktab)
    {
        this.kwicTable=ktab;
        this.kwicTable.setAggregate(this);
        this.kwicTable.setCache(this.searchedPhrases);
//        this.qwicTable.setCache(this.searchedPhrases);
    }

    public KwicTable getKWICtable()
    {
        return this.kwicTable;
    }


    public void setCache(Hashtable c)
    {
        this.searchedPhrases=c;
    }

    public void searchTheEngine(String keyword)
    {
        this.keyword=keyword;
        GeneralDriver searched=(GeneralDriver)(this.searchedPhrases.get(keyword));
        if(searched==null){   // the phrase(x) has not been searched
           this.step1(keyword,0,0);
        }
        else
        {
            this.messageReceiver.receiveMessage("frequency "+
                searched.frequency);
        }
    }

    public void setMessageReceiver(MessageReceiver r)
    {
        this.messageReceiver=r;
    }

    public MessageReceiver messageReceiver;
    public Hashtable searchedPhrases;
    public Settings settings;
    String listItemFormat;
    String parameterPositions;

    public void setSettings(Settings settings)
    {
        this.settings=settings;
        int searchEngineIndex=settings.searchEngineComboBox.getSelectedIndex();
        this.searchEngineURL=(String)(settings.searchEngineSettingTab.getValueAt(
            searchEngineIndex,2));
        this.queryHead=(String)(settings.searchEngineSettingTab.getValueAt(
            searchEngineIndex,3));
        this.queryWordSeparator=(String)(settings.searchEngineSettingTab.getValueAt(
            searchEngineIndex,4));
        this.queryTail=(String)(settings.searchEngineSettingTab.getValueAt(
            searchEngineIndex,5));
        this.frequencyFormat =(String)(settings.searchEngineSettingTab.getValueAt(
            searchEngineIndex,6));
        this.listItemFormat =(String)(settings.searchEngineSettingTab.getValueAt(
        	searchEngineIndex,7));
        this.parameterPositions =(String)(settings.searchEngineSettingTab.getValueAt(
            	searchEngineIndex,8));
    }

    public String frequencyFormat;

    public String queryWordSeparator;

    public String queryTail;

    public String queryHead;

    public String searchEngineURL;

    public int proxyPort;

    public String proxyName;
    public String proxyKind;

    public void setProxy(String pname, int pport, String pkind, String pw)
    {
        proxyName=pname;
        proxyPort=pport;
        this.proxyKind=pkind;
        this.passWord=pw;
    }

//    public PatternGenerator1 pgenerator;

    public String targetKeyWords;

//    public GeneralDriver(WebLEAPFrame g,Visualizer v,PatternGenerator1 pg, Settings settings)
    public GeneralDriver()
    {
//        gui=g;
//        visualizer=v;
        resultArea=new TextArea();
        messageArea=new TextArea();
//        searchedPhrases=gui.getSearchedPhrases();
//        pgenerator=pg;
        
    }

    public String targetURL;

//    public WebLEAPFrame gui;

//    public Visualizer visualizer;

//    public synchronized void pageLoadingDone(String s)
    public  synchronized
    void receiveMessage(String s)
    {
        // This method is derived from interface AckReceiver
        // to do: code goes here
        html=s;
//        gui.addMessage("tokenizing ....\n");
        step2(s);
        step3();

        String frequency=resultArea.getText();
//        JTextArea ea=gui.getEvaluationArea();
//        ea.append(targetKeyWords+":"+frequency);

        StringTokenizer st=new StringTokenizer(frequency,", \n\r\t");
        String w="";
        while(st.hasMoreElements()){
              w=w+st.nextToken();
        }
        int ifreq=(new Integer(w)).intValue();
        this.frequency=ifreq;
        searchedPhrases.put(keyword,this);
//        visualizer.addFrequency(position,ifreq,tokenSize,targetURL,html,targetKeyWords);
//        visualizer.repaint();
//        gui.addMessage("the frequency of \""+targetKeyWords+"\" has been gotten.\n");
//        pgenerator.notifyOneKeyProcessed();
        this.messageReceiver.receiveMessage("frequency "+w);
//        this.qwicTable.setCache(this.searchedPhrases);
     }

    public String makeSearchURL(String key)
    {
//	    String searchURL="http://www.altavista.com/cgi/query/query?pg=q&kl=XX&q=%22";
        String searchURL=this.searchEngineURL+this.queryHead;
	    StringTokenizer st = new StringTokenizer(key);
        if(st.hasMoreTokens()) {
                searchURL=searchURL+st.nextToken();
        }
        while (st.hasMoreTokens()) {
//                searchURL=searchURL+"+";
                searchURL=searchURL+this.queryWordSeparator;
                searchURL=searchURL+st.nextToken();
        }
//        searchURL=searchURL+"%22&search=Search";
        searchURL=searchURL+this.queryTail;
/*
		gui.messageArea.append("search "+searchURL+"\n");
		gui.urlField.setText(searchURL);
*/
        return searchURL;
    }

    public void step1(String key,int i, int j)
    {
        position=i;
        tokenSize=j;
        targetKeyWords=key;
        targetURL=makeSearchURL(key);
        this.messageReceiver2.receiveMessage("getting frequency of "+key+"....\n");
//        gui.addMessage("getting frequency of "+key+"....\n");
        PageLoader1 ploader=null;
        if(proxyName==null||this.proxyKind.equals("no"))
            ploader=new PageLoader1(targetURL,(MessageReceiver)this,messageReceiver2);
        else{
            ploader=new PageLoader1(targetURL,(MessageReceiver)this,messageReceiver2,proxyName,proxyPort,proxyKind,passWord);
        }
    }

    public int tokenSize;

    public int position;

    public TextArea resultArea;

    public TextArea messageArea;

    public PatternMatcher patternMatcher;

    public void step3()
    {
    	if(patternMatcher==null) return;

//        gui.addMessage(targetKeyWords+ ":pattern mattching of the frequency.....\n");
		Vector result=new Vector();
        patternMatcher.pattern("* pages found",result);
		int l=result.size();
		String frequency="";
		if(l!=0) frequency=(String)(result.elementAt(0));
		if(l==0||frequency.equals("html")) {
//           patternMatcher.pattern("AltaVista found about * Web",result);
//           patternMatcher.pattern("About * pages found",result);
           patternMatcher.pattern(this.frequencyFormat,result);
           l=result.size();
           if(l!=0) frequency=(String)(result.elementAt(0));
           else     frequency="0";
		}
		else frequency=(String)(result.elementAt(0));
		resultArea.setText("");
		resultArea.append(frequency+"\n");

//		messageArea.appendText(""+frequency+" web pages.\n");

   }

    Vector tokens;
    
    public void step2(String theHTML)
    {
//        gui.addMessage(targetKeyWords+":initializing the pattern matcher.....\n");
        this.messageReceiver2.receiveMessage(targetKeyWords+":initializing the pattern matcher.....\n");
	    patternMatcher=new PatternMatcher();
		patternMatcher.output=messageArea;
//		patternMatcher.init(theHTML,".:;<> !#$%&()=^+*/\"@[]{}\n\r");
		patternMatcher.init(theHTML);
		this.tokens=patternMatcher.getTokens();
		int i;
		int l=patternMatcher.sequenceLength;
//		resultArea.setText("contents of this page was tokenized.\n");
    }
    public String getListItemPattern(){
    	return this.listItemFormat;
    }
    public String getParameterPositions(){
    	return this.parameterPositions;
    }
}