package application.webleap;

import java.awt.Color;
import java.util.Vector;

public class Visualizer extends GraphicPlane
{
    public int fBoxTableIndex;

    public Vector fBoxTable;


    public webleap.Settings settings;


    public int minPhraseLength;
    public int maxPhraseLength;
    public void clear()
    {
        maxPhraseLength=gui.maxPhraseLength();
        minPhraseLength=gui.minPhraseLength();
        frequency=new GraphicsPrimitiveList[maxPhraseLength];
        aSentence=new GraphicsPrimitiveList();
        aSentence.init();
        for(int i=0;i<maxPhraseLength;i++){
            frequency[i]=new GraphicsPrimitiveList();
            frequency[i].init();
        }
//        repaint();

    }
    public Thread me;
    public WebLEAPFrame gui;
    public void sortFrequency(int l)
    {
        Vector a=(frequency[l]).primitives;
        if(a==null) return;
        int i;
        int j;
        int max=a.size();
        for(i=0;i<max-1;i++){
            for(j=i+1;j<max;j++){
                application.webleap.FrequencyBox ti=(application.webleap.FrequencyBox)(a.elementAt(i));
                application.webleap.FrequencyBox tj=(application.webleap.FrequencyBox)(a.elementAt(j));
                if(ti.frequency>tj.frequency){
                    a.setElementAt(ti,j);
                    a.setElementAt(tj,i);
                }
            }
        }
        (frequency[l]).primitives=a;
    }
    public int barHeight;
    public Visualizer()
    {
        init(); 
    
		//{{REGISTER_LISTENERS
		SymMouse aSymMouse = new SymMouse();
		this.addMouseListener(aSymMouse);
		//}}
		fBoxTable=new Vector();
		this.fBoxTableIndex=0;
	}
    public void init(WebLEAPFrame g,webleap.Settings s)
    {
        gui=g;
        spaceBetweenWords=10;
        clear();

        barHeight=12;
        settings=s;
    		//{{INIT_CONTROLS
		setSize(0,0);
		//}}
	//	start();
	    this.setVisible(true);
}
    public int spaceBetweenWords;
    public GraphicsPrimitiveList frequency[];
    public GraphicsPrimitiveList  aSentence;
    public void addWord(int no,String x)
    {
        Phrase newWord;
        if(no==0){
            newWord=new Phrase(x,10,15,Color.black,gui);
        }
        else{
            Phrase prevWord=(Phrase)(aSentence.primitives.elementAt(no-1));
//            prevWord.getSize();
            newWord=new Phrase(x,prevWord.x1+prevWord.width+spaceBetweenWords,15,Color.black,gui);
        }
        newWord.getSize();
        aSentence.add(newWord);
        if(no>aSentence.primitives.size()){
            aSentence.primitives.setSize(no);
        }
        /* */
        String toDraw="addfig text("+
             "attrib(\n"+
             "color("+ Color.black.getRGB()+")\n"+
             "depth(0)\n"+
             "width(1)\n"+
             "font(\"Dialog\","+0+","+
                       14+")\n"+
             ")"+
             newWord.x1+","+newWord.y1+","
                             +"\""+x+"\")";
                             
        String rtn=this.gui.draw.parseCommand(toDraw);
        /* */
        /*
        aSentence.primitives.setElementAt(newWord,no);
        this.repaint2();
//        gui.repaint();
        */
    }
    public int cy;
    public int cx;
    public int yoffset;
//    public synchronized void addFrequencyBox(int no, int fx, int length,String u,String html,String phrase)
    public synchronized FrequencyBox addFrequencyBox(int no, int length, String phrase)
    // phrase: the object phrase
    // no: position of this phrase, left from the sentence.
    // fx: frequency of the prhase
    // length: phrase length (number of the words in phrase)
    // html: html text which includes this frequency
    {
        int x1;   // the left side x position of this phrases
        int x2;   // the left side x position of the last word in this phrase
        int w;    // width of this phrase
        int h,h1; // virtical position
        Phrase t=(Phrase)(aSentence.primitives.elementAt(no));
        x1=t.x1;
        t=(Phrase)(aSentence.primitives.elementAt(no+length));
        t.getSize();
        x2=t.x1+t.width;
        w=x2-x1;
        h1=0;
        int dx=3;
        h1=(length+1)*dx+(length+1)*length*barHeight/2;
        
        h=35+h1+(no%(length+1))*barHeight;
        FrequencyBox fbox=new FrequencyBox(x1,h,w,barHeight,phrase);
        fbox.setGui(gui)   ;  
        fbox.setSettings(settings);
        frequency[length].primitives.addElement(fbox);
        this.fBoxTable.addElement(fbox);

        fBoxTableIndex++;
        
        return fbox;
//        String command="btn.click("+14+")";  // set play
//        rtn=this.gui.draw.parseCommand(command);
    }
    
    public int xoffset;
    public int height;
    public int width;
	//{{DECLARE_CONTROLS
	//}}

	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseClicked(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == Visualizer.this)
				Visualizer_MouseClicked(event);
		}
	}

	void Visualizer_MouseClicked(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
			 
		Visualizer_MouseClicked_Interaction1(event);
	}

	void Visualizer_MouseClicked_Interaction1(java.awt.event.MouseEvent event)
	{
		try {
//			this.show();
		} catch (Exception e) {
		}
//		boolean b=this.mouseDown(null,event.getX(),event.getY());
	}
}

