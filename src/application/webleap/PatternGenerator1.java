package application.webleap;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import webleap.MessageReceiver;
import webleap.SearchEngineDriver;
public class PatternGenerator1 implements java.lang.Runnable, MessageReceiver    
{
	String passWord;
    public SearchEngineDriver searchEngineDriver;
    String proxyKind;

    public void setSearchEngineDriver(SearchEngineDriver driver)
    {
        this.searchEngineDriver=driver;
    }

    public void receiveMessage(String s)
    {
        // This method is derived from interface MessageReceiver
        // to do: code goes here
        if(s.indexOf("done ")==0){
            String phrs=s.substring("done ".length());
            this.numberOfUnProcessedKey--;
            if(this.numberOfUnProcessedKey<=0){
                gui.addMessage("Done. All key words have been processed.\n");
                Date date=new Date(); // Calendar.getInstance();
                endTime=date.getTime(); //(new Date()).getTime();
                double spendTime=(endTime-startTime)/1000.0;
                gui.addMessage(date.toString()+"\n"+searchCount+"searches in "
                    +spendTime+"sec. "+spendTime/searchCount+" sec./search\n");
            }
        }
    }

    public int searchCount;

    public long endTime;

    public long startTime;

    public Date date;

    public webleap.Settings settings;

    public int proxyPort;

    public String proxyName;

    public void setProxy(String proxyName, int proxyPort, String k, String pw)
    {
        this.proxyName=proxyName;
        this.proxyPort=proxyPort;
        this.proxyKind=k;
        this.passWord=pw;
    }

    public PatternGenerator1()
    {
        me=null;
        startTime=(new Date()).getTime();
    }

    public synchronized void notifyOneKeyProcessed()
    {
        this.numberOfUnProcessedKey--;
        if(this.numberOfUnProcessedKey<=0){
            gui.addMessage("Done. All key words have been processed.\n");
            Date date=new Date(); // Calendar.getInstance();
            endTime=date.getTime(); //(new Date()).getTime();
            double spendTime=(endTime-startTime)/1000.0;
            gui.addMessage(date.toString()+"\n"+searchCount+"searches in "
                +spendTime+"sec. "+spendTime/searchCount+" sec./search\n");
        }
    }
    
    public int numberOfUnProcessedKey;

    public Visualizer visualizer;

	public Thread me;
    public void stop()
    {
        if(me!=null){
//            me.destroy();
//            me=null;
//            me.stop();
            me=null;
        }
    }
    public void start()
    {
        if(me==null){
            me=new Thread(this);
            me.start();
        }
    }
    public void run()
    {
        if(me!=null){
        int i,j,k;
        String x;
        visualizer=gui.getVisualizer();
        int plength=gui.maxPhraseLength();
        int minlength=gui.minPhraseLength();
        searchCount=0;
        numberOfUnProcessedKey=0;
        Hashtable sp=gui.getSearchedPhrases();
//        this.searchEngineDriver.setProxy(this.proxyName,this.proxyPort,proxyKind,passWord);
        this.searchEngineDriver.setCache(sp);
        String qoption=this.settings.searchOptionField.getText();
        this.searchEngineDriver.setQueryOptions(qoption);
        this.searchEngineDriver.setKwicTable(this.settings.kwicTable);
        for(i=0;i<sequenceLength;i++){
           x="";
           for(j=0;j<plength;j++){
              if(i+j<sequenceLength){

                if(me==null){stop(); return;} // quit when interrupted

                 x=x+(String)(wordSequence.elementAt(i+j));

                 if(j>=minlength-1){

                 gui.addMessage("evaluating "+x+"\n");
                 String html;
                 numberOfUnProcessedKey++;
                    searchCount++;
                    gui.addMessage("searching "+x+"\n");
                    SearchEngineDriver driver=this.searchEngineDriver.copyThis();
                    driver.setProxy(this.proxyName,this.proxyPort,proxyKind,passWord);
                    FrequencyBox fb=visualizer.addFrequencyBox(i,j,x);
                    fb.setMessageReceiver(this);
                    driver.setMessageReceiver(fb);
                    driver.searchTheEngine(x);
                    results.addElement(x);
                 } // end of if(i+j>minlength-1)
                 x=x+" ";
             } // end of if(i+j<sequenceLength)
           } // end of j-loop

        } // end of i-loop
        } // end of if(me!=null)
        System.out.println("end of patternGenerator1-run");
        // stop();
    }
    public Vector results;
    public void setWords(Vector r)
    {
        results=r;
    }
    public WebLEAPFrame gui;
    public TextArea output;
    public int sequenceLength;
    public Vector wordSequence;
    public StringTokenizer tokenizer;
    public void init(String s, String breakSymbols, WebLEAPFrame g,webleap.Settings settings)
    {
        tokenizer=new StringTokenizer(s,breakSymbols);
        wordSequence=new Vector();
        sequenceLength=0;
        gui=g;

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
        this.settings=settings;
   }
    public void comment()
    {
        /*

        ex.

        wordSequence={"this" "is" "a" "pen"}

        results={"this is a",
                 "this is",
                 "this",
                 "is a pen"
                 "is a"
                 "is"
                 "a pen"
                 "a"
                 "pen"}


        */
    }
}

