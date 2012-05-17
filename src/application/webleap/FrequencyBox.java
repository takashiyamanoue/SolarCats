package application.webleap;
import java.awt.Color;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.JTextArea;

import webleap.MessageReceiver;
public class FrequencyBox extends application.draw.Box implements webleap.MessageReceiver 
{
    public MessageReceiver messageReceiver;

    public void setMessageReceiver(MessageReceiver r)
    {
        this.messageReceiver=r;
    }

    public int height;

    public int width;

    public int y;

    public int x;

    public void setBorder(int x, int y, int w, int h)
    {
    }

    public void receiveMessage(String s)
    {
        // This method is derived from interface MessageReceiver
        // to do: code goes here
        String fs="";
        if(s.indexOf("frequency ")==0){
            fs=s.substring("frequency ".length());
            frequency=(new Integer(fs)).intValue();
            Color color=Color.white;
            if(frequency<11) color=Color.pink;
            else if(frequency<101) color=Color.orange;
            else if(frequency<1001) color=Color.yellow;
            else if(frequency<10001) color=Color.green;
            else color=Color.cyan;

            String frect="addfig fillrect("+
               "attrib(\n"+
               "color("+ color.getRGB()+")\n"+
               "depth(1)\n"+
               "width(1)\n"+
               ")"+
            this.x1+","+this.y1+","+this.width+","+this.height+")";
            String rtn=this.gui.draw.parseCommand(frect);
       
            int x=this.x1+this.width/2;
            int y=this.y1-this.height/2+5;
            String ftext="addfig text("+
                 "attrib(\n"+
                 "color("+ Color.black.getRGB()+")\n"+
                 "depth(0)\n"+
                 "width(1)\n"+
                 "font(\"Dialog\","+0+","+10+")\n"+
                 ")"+
                 x+","+y+"," +"\""+fs+"\")";
            rtn=this.gui.draw.parseCommand(ftext,"WebLEAPFrame",this.gui);
/*         */   
            String rect="addfig rectangle("+
                 "attrib(\n"+
                 "color("+ Color.black.getRGB()+")\n"+
                 "depth(0)\n"+
                 "width(1)\n"+
                 ")"+
                 this.x1+","+this.y1+","+this.width+","+this.height+")";
            rtn=this.gui.draw.parseCommand(rect);
            /* */
            String cbox="addfig clickableboxexe("+
//             "attrib(\n"+
//             "color("+ Color.black.getRGB()+")\n"+
//             "depth(0)\n"+
//             "width(1)\n"+
//             ")"+
                 this.x1+","+this.y1+","+this.width+","+this.height+","+
                 "\"WebLEAPFrame openUrlListTable "+phrase+"\""+
                 ")";
            rtn=this.gui.draw.parseCommand(cbox,"WebLEAPFrame",this.gui);
            String command="btn.click("+14+")";  // set play
            rtn=this.gui.draw.parseCommand(command,"WebLEAPFrame",this.gui);
            this.messageReceiver.receiveMessage("done "+phrase);
        }
    }

    public void openTable()
    {
            gui.addMessage("opening the URL list now. please wait for a moment.\n");
//            gui.repaint();
            /*
            try{ theUrl = new URL(url);}
            catch(MalformedURLException e){
                System.out.println("Bad URL: "+url);
            }
            */
            JTextArea ra=gui.getResultArea();
            ra.setText("frequency= "+frequency+"\n\n");
            gui.addMessage("opening the URL list now. please wait for a moment.\n");
            UrlTable urlTab=gui.getUrlTable();
            urlTab.initTab();
            urlTab.setSettings(settings);
            urlTab.keyWordField.setText(phrase);
            urlTab.setHtml(html);
            urlTab.setFrequency(frequency);
            urlTab.show();
            urlTab.setTable();
            gui.addMessage("the URL list is opend.\n");
    }

    public void paint()
    {
    	/*
        if(frequency<4) color=Color.pink;
        else if(frequency<11) color=Color.orange;
        else if(frequency<31) color=Color.yellow;
        else if(frequency<91) color=Color.green;
        else color=Color.cyan;
        */
        if(frequency<21) color=Color.pink;
        else if(frequency<101) color=Color.orange;
        else if(frequency<501) color=Color.yellow;
        else if(frequency<2501) color=Color.green;
        else color=Color.cyan;        
        /*
        g.setColor(color);
        g.fillRect(x1,y1,width,height);
        g.setColor(Color.black);
        g.drawString(""+frequency,x1+width/2,y1+height/2+5);
        g.drawRect(x1,y1,width,height);
        g.setColor(cc);
        */
        String frect="addfig fillrect("+
             "attrib(\n"+
             "color("+ this.color.getRGB()+")\n"+
             "depth(1)\n"+
             "width(1)\n"+
             ")"+
             this.x1+","+this.y1+","+this.width+","+this.height+")";
        String rtn=this.gui.draw.parseCommand(frect);
       
        int x=this.x1+this.width/2;
        int y=this.y1-this.height/2+5;
        String ftext="addfig text("+
             "attrib(\n"+
             "color("+ Color.black.getRGB()+")\n"+
             "depth(0)\n"+
             "width(1)\n"+
             "font(\"Dialog\","+0+","+10+")\n"+
             ")"+
             x+","+y+"," +"\""+this.frequency+"\")";
        rtn=this.gui.draw.parseCommand(ftext);
        
        String rect="addfig rectangle("+
             "attrib(\n"+
             "color("+ Color.black.getRGB()+")\n"+
             "depth(1)\n"+
             "width(1)\n"+
             ")"+
             this.x1+","+this.y1+","+this.width+","+this.height+")";
        rtn=this.gui.draw.parseCommand(rect);
    }

    public void setSettings(webleap.Settings s)
    {
        settings=s;
    }

    public webleap.Settings settings;

    public String phrase;

    public WebLEAPFrame gui;

    public void setGui(WebLEAPFrame gui)
    {
        this.gui=gui;
    }

    public String html;
    public void mouseDown(int x, int y)
    {
        URL theUrl=null; 
        if(isIn(x,y)){
            this.openTable();
        }
    }
    public boolean isIn(int x, int y)
    {
        if(x<x1) return false;
        if(y<y1) return false;
        if(x1+width<x) return false;
        if(y1+height<y) return false;
        return true;
    }
    public String url;
    public FrequencyBox(int x, int y, int w, int h,String phrase)
    { 
        this.x1=x; this.y1=y; this.width=w; this.height=h;
        this.phrase=phrase;
    }
    public void paint(Graphics g)
    {
        Color cc=g.getColor();
        /*
        if(frequency<4) color=Color.pink;
        else if(frequency<11) color=Color.orange;
        else if(frequency<31) color=Color.yellow;
        else if(frequency<91) color=Color.green;
        else color=Color.cyan;
        */
        if(frequency<21) color=Color.pink;
        else if(frequency<101) color=Color.orange;
        else if(frequency<501) color=Color.yellow;
        else if(frequency<2501) color=Color.green;
        else color=Color.cyan;        
        /*
        g.setColor(color);
        g.fillRect(x1,y1,width,height);
        g.setColor(Color.black);
        g.drawString(""+frequency,x1+width/2,y1+height/2+5);
        g.drawRect(x1,y1,width,height);
        g.setColor(cc);
        */
        String frect="addfig fillrect("+
             "attrib(\n"+
             "color("+ this.color.getRGB()+")\n"+
             "depth(1)\n"+
             "width(1)\n"+
             ")"+
             this.x1+","+this.y1+","+this.width+","+this.height+")";
        String rtn=this.gui.draw.parseCommand(frect);
       
        int x=this.x1+this.width/2;
        int y=this.y1-this.height/2+5;
        String ftext="addfig text("+
             "attrib(\n"+
             "color("+ Color.black.getRGB()+")\n"+
             "depth(0)\n"+
             "width(1)\n"+
             "font(\"Dialog\","+0+","+10+")\n"+
             ")"+
             x+","+y+"," +"\""+this.frequency+"\")";
        rtn=this.gui.draw.parseCommand(ftext);
        
        String rect="addfig rectangle("+
             "attrib(\n"+
             "color("+ Color.black.getRGB()+")\n"+
             "depth(1)\n"+
             "width(1)\n"+
             ")"+
             this.x1+","+this.y1+","+this.width+","+this.height+")";
        rtn=this.gui.draw.parseCommand(rect);
    }
    public int frequency;
}

