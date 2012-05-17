package application.draw;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;

import nodesystem.NodeSettings;

import language.ALisp;
import language.LispObject;

public class HtmlEnvironment
implements   AppletStub,AppletContext
{
	Figures figs;
	int x,y;
	int width,height;
	FigCanvas canvas;
	int currentX;
	int currentY;
	int currentLeftMergine=10;
	int currentHeadMergine=10;
	boolean settingCenter;

	String html;
	LispObject htmlObject;
	String codebase;
	String url;
	Vector subEnvs;
	ALisp lisp;
	DrawFrame gui;
	
	double sizeOfH1=24.0;
	double sizeOfH2=18.0;
	double sizeOfH3=14.0;
	double sizeOfNormal=11.0;
	double scale=1.0;
	Color fgColor=Color.black;
	Color bgColor=Color.white;
	int tabSize=10;	
	int vSpace=15;
	int itemSymbolSize=10;
	AnOval itemSymbol;
	Font currentFont;
	Font previousFont;
	
	Font h1Font=new Font("Dialog", Font.BOLD, 24);
	Font h2Font=new Font("Dialog", Font.PLAIN, 22);
	Font h3Font =new Font("Dialog",Font.PLAIN, 18);
	Font h4Font = new Font("Dialog",Font.PLAIN, 14);
	Font normalFont = new Font("Dialog",Font.PLAIN, 11);
	Font smallFont = new Font("Dialog",Font.PLAIN, 9);
	Font italicFont = new Font("Dialog",Font.ITALIC,11);
	String currentHyperLink;
	String charSet = "utf-8";
	
	HtmlEnvironment superEnv;
	
	public HtmlEnvironment(DrawFrame g,LispObject o,String u,ALisp l){
		gui=g;
		this.canvas=g.canvas;
		this.width=canvas.getWidth();
		this.height=canvas.getHeight();
		this.x=0;
		this.y=0;
		currentX=this.x+this.currentLeftMergine;
		currentY=this.y+this.currentHeadMergine;
		this.htmlObject=o;
		this.url=u;
		this.codebase=makeCodebase(u);
		this.lisp=l;
		this.applets=new Hashtable();
		this.settingCenter=false;
		this.currentFont=this.normalFont;
		this.previousFont=this.normalFont;
		this.currentHyperLink=null;
	}
	
	public HtmlEnvironment(HtmlEnvironment sEnv){
		gui=sEnv.gui;
		this.canvas=sEnv.canvas;
		this.width=canvas.getWidth();
		this.height=canvas.getHeight();
		this.x=0;
		this.y=0;
		currentX=sEnv.currentX;
		currentY=sEnv.currentY;
		this.htmlObject=sEnv.htmlObject;
		this.url=sEnv.url;
		this.codebase=sEnv.codebase;
		this.lisp=sEnv.lisp;
		this.applets=new Hashtable();
		this.superEnv=sEnv;
		this.settingCenter=sEnv.settingCenter;
		this.currentFont=sEnv.currentFont;
		this.previousFont=sEnv.previousFont;
		this.currentHyperLink=sEnv.currentHyperLink;
	}
	  public void eval(){
		   eval(htmlObject);
		   this.canvas.setPreferredSize(new Dimension(this.currentX,this.currentY));
		   this.canvas.revalidate();
		   this.canvas.vscrollbar.setMinimum(0);
		   this.canvas.vscrollbar.setMaximum(this.currentY);
		   this.canvas.vscrollbar.setValue(0);
		   this.canvas.hscrollbar.setMinimum(0);
		   this.canvas.hscrollbar.setMaximum(this.width);
		   this.canvas.hscrollbar.setValue(0);
		   if(this.currentY>this.height){
			   canvas.showScrollBar(canvas.vscrollbar.getID());
		   }
		   else{
			   canvas.hideScrollBar(canvas.vscrollbar.getID());
		   }
		   this.canvas.fs.setStep(-10);
		   
	   }
	   public void eval(LispObject x){
//		   lisp.plist2("eval ",x);
		   if(x==null) return;
		   while(!lisp.Null(x)){
			   LispObject w=lisp.car(x);
    		   if(lisp.atom(w)){
	    		   return;
		       }
    		   else
        	   if(isSingleTag(w)){
        		   LispObject tagName=lisp.car(lisp.cdr(w));
        		   LispObject clause=lisp.cdr(lisp.cdr(w));
        		   applySingleTag(tagName,w,clause); 
        		   return;
        	   }
        	   else
        	   if(isString(w)){
//        		   writeString(w);
        	   }
        	   else
    		   if(isComment(w)){
    			   System.out.println("comment");
    		   }
    		   else
    		   if(isXmlDeclaration(w)){
                   System.out.println("xml-declaration");
        		   LispObject tagName=lisp.car(lisp.cdr(w));
        		   LispObject clause=lisp.cdr(lisp.cdr(w));
        		   applySingleTag(tagName,w,clause);     			   
    		   }
    		   else
    		   if(isTag(w)){
//    			       return;   
    			   LispObject first=w;
    			   LispObject tagName=lisp.car(lisp.cdr(first));
    			   LispObject clause=lisp.cdr(x);
    			   apply(tagName,first,clause);
    			   return;
    		   }
    		  
    		   /*
    		   else
    		   if(firstIsString(x)){
    			   LispObject s=lisp.car(x);
    			   writeString(s);
    		   }
    		   */
    		   
		       else{
		    	   eval(w);
		       }
		       
    		   x=lisp.cdr(x);
		   }
	   }
	   public void evalHtml(LispObject x){
		   if(x==null) return;
//		   while(!lisp.Null(x)){
//			   LispObject w=lisp.car(x);
    		   if(lisp.atom(x)){
	    		   return;
		       }
    		   else
        	   if(isSingleTag(x)){
        		   LispObject first=lisp.car(x);
        		   LispObject tagName=lisp.car(lisp.cdr(x));
        		   LispObject clause=lisp.cdr(lisp.cdr(x));
        		   applySingleTag(tagName,x,clause);    			   
        	   }
        	   else
        	   if(isString(x)){
        		   writeString(x);
        	   }
        	   else
    		   if(isComment(x)){
    			   System.out.println("comment");
    		   }
    		   else
    		   if(firstIsTag(x)){
//    			       return;   
    			   LispObject first=lisp.car(x);
    			   LispObject tagName=lisp.car(lisp.cdr(first));
    			   LispObject clause=lisp.cdr(x);
    			   apply(tagName,first,clause);
    		   }
    		   /*
    		   else
    		   if(firstIsString(x)){
    			   LispObject s=lisp.car(x);
    			   writeString(s);
    		   }
    		   */
		       else{
		    	   LispObject a=lisp.car(x);
		    	   evalHtml(a);
		       }
//    		   x=lisp.cdr(x);
//		   }
		   
	   }
	   public void evalHead(LispObject x){
		   if(x==null) return;
//		   while(!lisp.Null(x)){
//			   LispObject w=lisp.car(x);
    		   if(lisp.atom(x)){
	    		   return;
		       }
    		   else
        	   if(isSingleTag(x)){
        		   LispObject first=lisp.car(x);
        		   LispObject tagName=lisp.car(lisp.cdr(x));
        		   LispObject clause=lisp.cdr(lisp.cdr(x));
        		   applySingleTag(tagName,x,clause);    			   
        	   }
        	   else
        	   if(isString(x)){
//        		   writeString(x);
        	   }
        	   else
    		   if(isComment(x)){
    			   System.out.println("comment");
    		   }
    		   else
    		   if(firstIsTag(x)){
//    			       return;   
    			   LispObject first=lisp.car(x);
    			   LispObject tagName=lisp.car(lisp.cdr(first));
    			   LispObject clause=lisp.cdr(x);
    			   apply(tagName,first,clause);
    		   }
    		   /*
    		   else
    		   if(firstIsString(x)){
    			   LispObject s=lisp.car(x);
    			   writeString(s);
    		   }
    		   */
		       else{
		    	   LispObject a=lisp.car(x);
		    	   evalHead(a);
		       }
//    		   x=lisp.cdr(x);
//		   }
		   
	   }
	   public boolean firstIsTag(LispObject x){
		   if(x==null) return false;
		   if(lisp.Null(x)) return false;
		   if(lisp.atom(x)) return false;
		   LispObject first=lisp.car(x);
		   if(!isTag(first)) return false;
		   return true;
	   }
	   public boolean firstIsComment(LispObject x){
		   if(x==null) return false;
		   if(lisp.Null(x)) return false;
		   if(lisp.atom(x)) return false;
		   LispObject first=lisp.car(x);
		   if(!isComment(first)) return false;
		   return true;
	   }
	   public boolean firstIsXmlDeclaration(LispObject x){
		   if(x==null) return false;
		   if(lisp.Null(x)) return false;
		   if(lisp.atom(x)) return false;
		   LispObject first=lisp.car(x);
		   if(!isXmlDeclaration(first)) return false;
		   return true;
	   }
	   
	   public boolean writeString(LispObject x){
		   if(x==null) return false;
		   if(lisp.Null(x)) return false;
		   if(lisp.atom(x)) return false;
		   if(!isString(x)) return false;
		   LispObject s=lisp.car(lisp.cdr(x));
		   String w=s.toString();
		   if(w.equals("\n")) return true;
		   if(w.equals("\r")) return true;
		   if(w.equals("\t")) return true;
		   if(w.equals(" ")) return true;
		   if(w.equals("　")) return true;
		   Vector txt=this.getFixedWidthText(this.width,this.currentFont,w);
		   if(txt.size()<=0) return false;
		   for(int i=0;i<txt.size();i++){
			   String line=(String)(txt.elementAt(i));
			   setStringOnTheCanvas(line);
			   //lines=lines+(String)(txt.elementAt(i));
		   }
		   return true;
	   }
	   
	   public void writeTextBox(LispObject x, Hashtable h){
		   if(x==null) return;
		   if(lisp.Null(x)) return;
		   if(lisp.atom(x)) return;
		   
		   Vector txt=this.getFixedWidthText(this.width,this.currentFont,"A");
		   int ts=txt.size();
	       FontMetrics fmetrics=canvas.getFontMetrics(this.currentFont);
	       int height=fmetrics.getMaxAscent()+fmetrics.getMaxDescent();
		   String ws=((String)(h.get("value")));
		   int w=(new Integer(ws)).intValue()*ts;
		   
		   TextBox t=new TextBox(this.canvas);
		   t.setSize(w,height);
           t.setFont(this.currentFont);
           t.color=this.fgColor;
           t.offX=this.currentX;
           t.offY=this.currentY;
           t.step=-10;
           t.getSize();
           if(this.settingCenter){
        	   t.offX=this.currentLeftMergine+(this.width/2)-(t.x2/2);
           }
           t.showhide=true;
           t.selected=false;
           this.canvas.fs.add(t);
          this.previousFont=this.currentFont;
	   }
	   public void setStringOnTheCanvas(String line){
           Text t=new Text(this.canvas);
           t.setFont(this.currentFont);
           t.setText(line);
           t.color=this.fgColor;
           t.offX=this.currentX;
           t.offY=this.currentY;
           t.step=-10;
           t.getSize();
           if(this.settingCenter){
        	   t.offX=this.currentLeftMergine+(this.width/2)-(t.x2/2);
           }
           t.showhide=true;
           t.selected=false;
           this.canvas.fs.add(t);
           if(this.currentHyperLink!=null){
        	   UrlBox box=new UrlBox(this.canvas);
        	   box.offX=t.offX; box.offY=t.offY;
        	   box.x2=t.x2; box.y2=t.y2;
        	   box.setUrl(this.currentHyperLink);
        	   box.showhide=true;
        	   box.selected=false;
        	   box.setPlayMode();
        	   this.canvas.fs.add(box);
           }
           this.currentX=this.getTextWidth(this.currentFont,line)+10;
           if(line.endsWith("\n")){
               this.currentY=this.currentY+t.y2+vSpace;
           }
          System.out.println("writeString "+line);
          this.previousFont=this.currentFont;
		   
	   }

	   public Vector getFixedWidthText(int width, Font f,String x){
		   Vector text=new Vector();
		   String line="";
		   /*
		   while(getTextWidth(f,line+" ")<preX){
			   line=line+" ";
		   }
		   */
		   StringTokenizer st=new StringTokenizer(x);
		   while(st.hasMoreTokens()){
			   String w=st.nextToken();
			   if(getTextWidth(f,line+w)+this.currentX>width){ // string x が幅を超えている場合
			       char c=w.charAt(0);
				   if((c & 0x8000)!=0x0000){ // 多バイトコードの場合
    				   for(int i=0;i<w.length();i++){
        				   c=w.charAt(i);
		    			   if(getTextWidth(f,line+c)<width){
			    			   line=line+c;
			    			   c=w.charAt(i+1);
			    			   i++;
				    	   }
		    			   else{ //幅を超えた場合
		    				   int p=text.size();
		    				   if(p>0){ // 前の行に改行を加える。
		    				      String pl=(String)(text.elementAt(p-1));
		    				      if(p>0)  pl=pl+"\n";
		    				      text.setElementAt(pl,p-1);
		    				   }
		    				   text.addElement(line);
		    				   line="";
		    				   this.currentX=0;
		    			   }
	    			   }
				   }
				   else{ //１バイトコードの場合
    				   int p=text.size();
    				   if(p>0){ // 前の行に改行を加える。
    				      String pl=(String)(text.elementAt(p-1));
    				      pl=pl+"\n";
    				      text.setElementAt(pl,p-1);
    				   }
    				   text.addElement(line);
  				      line="";
					   line=line+" "+w;
					   this.currentX=0;
				   }
			   }
			   else{ // string x が幅を超えていない場合
				   line=line+" "+w;
			   }
		   } // while 終了
		   if(!line.equals("")){
			   text.addElement(line);
		   }
		   return text;
	   }
	    public int getTextWidth(Font f,String x){
	        StringBuffer s=new StringBuffer(x);
	        int height;
	        int lines;
	        int n;
	        int p1,p2;
	        int maxc;
	        int maxl;
	        int x2,y2;
	        int maxlength=256;
	        char buff[];
	        buff= new char[maxlength];
	        if(x.length()>maxlength){
	        	System.out.println("too long string");
	        	return this.width+1;
	        }
	        FontMetrics fmetrics=canvas.getFontMetrics(f);
	        height=fmetrics.getMaxAscent()+fmetrics.getMaxDescent();
	        lines=0;
	        n=0;
	        p1=0; p2=0;
	        maxl=0; maxc=0;
	        while(s.length()>n){
	            if(s.charAt(n)=='\n'){
	                lines++;
	                if(maxc<p2) {maxl=p1; maxc=p2-1;}
	                p1=n+1; p2=-1;
	            }
	            p2++;
	            n++;
	        }
	        if(maxc<p2) maxc=p2;
	        try{
	           s.getChars(maxl,maxl+maxc,buff,0);
	        }
	        catch(StringIndexOutOfBoundsException e){}
	        x2=fmetrics.charsWidth(buff,0,maxc);

	        // the following part needs an improvement.
	        if(maxc>0 && x2==0) x2=maxc*height;

	        y2=(lines+1)*height;
	        return x2;
	 }
	    
	   public boolean isXTag(String key, LispObject x){
		   if(!(isTag(x)||isSingleTag(x))) return false;
		   LispObject name=lisp.car(lisp.cdr(x));
		   if(lisp.eq(name,lisp.recSymbol(key)))
			   return true;
		   return false;
	   }
	   public boolean isTag(LispObject x){
		   if(lisp.Null(x)) return false;
		   if(lisp.atom(x)) return false;
		   LispObject first=lisp.car(x);
		   if(lisp.eq(first,lisp.recSymbol("tag"))) return true;
		   return false;
	   }
	   public boolean isString(LispObject x){
		   if(lisp.Null(x)) return false;
		   if(lisp.atom(x)) return false;
		   LispObject first=lisp.car(x);
		   if(lisp.eq(first,lisp.recSymbol("string"))) return true;
		   return false;
	   }
	   
	   public boolean firstIsString(LispObject x){
		   if(lisp.atom(x)) return false;
		   LispObject a=lisp.car(x);
		   return isString(a);
	   }
	   
	   public boolean isSingleTag(LispObject x){
		   if(lisp.Null(x)) return false;
		   if(lisp.atom(x)) return false;
		   LispObject first=lisp.car(x);
		   if(lisp.eq(first,lisp.recSymbol("stag"))) return true;
		   return false;
	   }
	   public boolean isComment(LispObject x){
		   if(lisp.Null(x)) return false;
		   if(lisp.atom(x)) return false;
		   LispObject first=lisp.car(x);
		   if(lisp.eq(first,lisp.recSymbol("!"))) return true;
		   return false;
	   }
	   public boolean isXmlDeclaration(LispObject x){
		   if(lisp.Null(x)) return false;
		   if(lisp.atom(x)) return false;
		   LispObject first=lisp.car(x);
		   if(lisp.eq(first,lisp.recSymbol("xml-dcl"))) return true;
		   return false;
	   }
	   public boolean firstIsSingleTag(LispObject x){
		   if(lisp.atom(x)) return false;
		   LispObject a=lisp.car(x);
		   return isSingleTag(a);
	   }
	   public boolean firstIsHtml(LispObject x){
		   if(lisp.atom(x)) return false;
		   LispObject a=lisp.car(x);
		   if(!isTag(a)) return false;
		   if(lisp.equal(lisp.car(lisp.cdr(x)), lisp.recSymbol("html"))) return true;
		   if(lisp.equal(lisp.car(lisp.cdr(x)), lisp.recSymbol("HTML"))) return true;
		   return false;
	   }
	   public void apply(LispObject tagName, LispObject tag, LispObject clause){
		   System.out.println("apply "+tagName.toString());
//		   lisp.plist2("---",clause);
		   if(lisp.equal(tagName,lisp.recSymbol("html"))||
				   lisp.equal(tagName,lisp.recSymbol("HTML"))){
			   while(!lisp.Null(clause)){
//				   LispObject w=lisp.car(clause);
			       this.evalHtml(clause);
			       clause=lisp.cdr(clause);
			   }
			   
//			   this.evalHtml(clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("head"))||
				   lisp.equal(tagName,lisp.recSymbol("HEAD"))){
			   
			   while(!lisp.Null(clause)){
//				   LispObject w=lisp.car(clause);
			       this.evalHead(clause);
			       clause=lisp.cdr(clause);
			   }
			   
//			   this.evalHtml(clause);
//			   lisp.plist2("head ",clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("title"))||
				   lisp.equal(tagName,lisp.recSymbol("TITLE"))){
			   lisp.plist2("title ",clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("body"))||
				   lisp.equal(tagName,lisp.recSymbol("BODY"))){
//			   lisp.plist2("body ",clause);
			   
			   while(!lisp.Null(clause)){
//				   LispObject x=lisp.car(clause);
			       this.evalHtml(clause);
			       clause=lisp.cdr(clause);
			   }
			   
//			   this.evalHtml(clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("a"))||
				   lisp.equal(tagName,lisp.recSymbol("A"))){
			   Vector attr=this.getTagAttributes(tag);
			   String urlx="";
			   if(attr!=null){
			   int maxattr=attr.size();
			   for(int i=0;i<maxattr;i++){
				   StringPair p=(StringPair)(attr.elementAt(i));
				   if(p.l.equals("href")||p.l.equals("HREF")){
					   String url=p.r;
					   if(url.startsWith("\"")){
						   url=url.substring(1,url.length());
						   if(url.endsWith("\"")){
							   url=url.substring(0,url.length()-1);
						   }
					   }
					   urlx=url;
				   }
			   }
			   }
			   HtmlEnvironment env=new HtmlEnvironment(this);
			   env.currentHyperLink=urlx;
//			   lisp.plist2("ul ",clause);
			   Color cc=this.fgColor;
			   env.fgColor=Color.blue;
			   env.evalHtml(clause);
			   env.currentHyperLink=null;
			   this.fgColor=cc;
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("p"))||
				   lisp.equal(tagName,lisp.recSymbol("P"))){
			   this.currentX=0;
			   this.currentY=currentY+this.vSpace+this.previousFont.getSize();
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("br"))||
				   lisp.equal(tagName,lisp.recSymbol("BR"))){
			   this.currentX=0;
			   this.currentY=currentY+this.vSpace+this.previousFont.getSize();
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("ul"))||
				   lisp.equal(tagName,lisp.recSymbol("UL"))){
//			   lisp.plist2("ul ",clause);
			   evalUl(tag,clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("ol"))||
				   lisp.equal(tagName,lisp.recSymbol("OL"))){
//			   lisp.plist2("ol ",clause);
			   evalOl(tag,clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("li"))||
				   lisp.equal(tagName,lisp.recSymbol("LI"))){
//			   lisp.plist2("li ",clause);
			   setItemSymbol();
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("hr"))||
				   lisp.equal(tagName,lisp.recSymbol("HR"))){
//			   lisp.plist2("hr",clause);
			   holizontalLine();
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("script"))||
				   lisp.equal(tagName,lisp.recSymbol("SCRIPT"))){
			   evalScript(tag,clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("applet"))||
				   lisp.equal(tagName,lisp.recSymbol("APPLET"))){
			   evalApplet(tag,clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("h1"))||
				   lisp.equal(tagName,lisp.recSymbol("H1"))){
			   HtmlEnvironment env=new HtmlEnvironment(this);
			   env.currentFont=this.h1Font;
			   
			   while(!lisp.Null(clause)){
//			      LispObject x=lisp.car(clause);
			      env.evalHtml(clause);
			      clause=lisp.cdr(clause);
			   }
			   
//			   env.evalHtml(clause);
			   this.currentY=env.currentY;
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("center"))||
				   lisp.equal(tagName,lisp.recSymbol("CENTER"))){
			   HtmlEnvironment env=new HtmlEnvironment(this);
			   env.settingCenter=true;
			   
			   while(!lisp.Null(clause)){
//				   LispObject w=lisp.car(clause);
    			   env.evalHtml(clause);
    			   clause=lisp.cdr(clause);
			   }
			   
//			   env.evalHtml(clause);
			   this.currentY=env.currentY;
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("img"))||
				   lisp.equal(tagName,lisp.recSymbol("IMG"))){
			   evalImage(tag,clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("address"))||
				   lisp.equal(tagName,lisp.recSymbol("ADDRESS"))){
			   HtmlEnvironment env=new HtmlEnvironment(this);
			   env.currentFont=this.italicFont;
			   this.currentY=currentY+this.vSpace;
			   
			   while(!lisp.Null(clause)){
//				   LispObject w=lisp.car(clause);
    			   env.evalHtml(clause);
    			   clause=lisp.cdr(clause);
			   }
			   
//			   env.evalHtml(clause);
			   this.currentY=env.currentY;
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("form"))||
				   lisp.equal(tagName,lisp.recSymbol("FORM"))){
			   evalForm(tag,clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("!"))){
			   
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("xml"))||
				   lisp.equal(tagName,lisp.recSymbol("XML"))){
			   
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("?xml"))||
				   lisp.equal(tagName,lisp.recSymbol("?XML"))){
			   
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("style"))||
				   lisp.equal(tagName,lisp.recSymbol("STYLE"))){
			   
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("div"))||
				   lisp.equal(tagName,lisp.recSymbol("DIV"))){
			   Hashtable p=this.getTagAttributes2(tag);
			   String name=(String)(p.get("id"));
			   HtmlEnvironment env=new HtmlEnvironment(this);
			   
			   while(!lisp.Null(clause)){
//				   LispObject w=lisp.car(clause);
    			   env.evalHtml(clause);
    			   clause=lisp.cdr(clause);
			   }
			   
//			   env.evalHtml(clause);
			   return;
			
		   }
		   else
		   if(lisp.equal(tagName, lisp.recSymbol("input"))||
				   lisp.equal(tagName, lisp.recSymbol("INPUT"))){
			   applyFormInputTag(clause);
		   }
		   else
		   if(lisp.equal(tagName, lisp.recSymbol("label"))||
				   lisp.equal(tagName, lisp.recSymbol("LABEL"))){
			   applyFormLabelTag(clause);
		   }
		   else
		   if(lisp.equal(tagName, lisp.recSymbol("textarea"))||
				   lisp.equal(tagName, lisp.recSymbol("TEXTAREA"))){
			   applyFormTextAreaTag(clause);
		   }
		   else{
			   System.out.println("no definition for "+tagName.toString());
		   }
		   
	   }
	   public void applySingleTag(LispObject tagName, LispObject tag, LispObject clause){
		   System.out.println("apply SingleTag "+tagName.toString());
//		   lisp.plist2("---",clause);
		   if(lisp.equal(tagName,lisp.recSymbol("p"))||
				   lisp.equal(tagName,lisp.recSymbol("P"))){
			   this.currentX=0;
			   this.currentY=currentY+this.vSpace+this.previousFont.getSize();
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("br"))||
				   lisp.equal(tagName,lisp.recSymbol("BR"))){
			   this.currentX=0;
			   this.currentY=currentY+this.vSpace+this.previousFont.getSize();
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("ul"))||
				   lisp.equal(tagName,lisp.recSymbol("UL"))){
//			   lisp.plist2("ul ",clause);
			   evalUl(tag,clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("ol"))||
				   lisp.equal(tagName,lisp.recSymbol("OL"))){
//			   lisp.plist2("ol ",clause);
			   evalOl(tag,clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("li"))||
				   lisp.equal(tagName,lisp.recSymbol("LI"))){
//			   lisp.plist2("li ",clause);
			   setItemSymbol();
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("hr"))||
				   lisp.equal(tagName,lisp.recSymbol("HR"))){
//			   lisp.plist2("hr",clause);
			   holizontalLine();
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("script"))||
				   lisp.equal(tagName,lisp.recSymbol("SCRIPT"))){
			   evalScript(tag,clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("applet"))||
				   lisp.equal(tagName,lisp.recSymbol("APPLET"))){
			   evalApplet(tag,clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("img"))||
				   lisp.equal(tagName,lisp.recSymbol("IMG"))){
			   evalImage(tag,clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("form"))||
				   lisp.equal(tagName,lisp.recSymbol("FORM"))){
			   evalForm(tag,clause);
			   return;
		   }
		   if(lisp.equal(tagName, lisp.recSymbol("input"))||
				   lisp.equal(tagName, lisp.recSymbol("INPUT"))){
			   applyFormInputTag(tag);
			   return;
		   }
		   if(lisp.equal(tagName, lisp.recSymbol("label"))||
				   lisp.equal(tagName, lisp.recSymbol("LABEL"))){
			   applyFormLabelTag(tag);
			   return;
		   }
		   if(lisp.equal(tagName, lisp.recSymbol("textarea"))||
				   lisp.equal(tagName, lisp.recSymbol("TEXTAREA"))){
			   applyFormTextAreaTag(tag);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("!"))){
			   
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("xml"))||
				   lisp.equal(tagName,lisp.recSymbol("XML"))){
			   
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("xml-dcl"))||
				   lisp.equal(tagName,lisp.recSymbol("XML-DCL"))){
			   applyXmlDeclaration(tag,clause);
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("div"))||
				   lisp.equal(tagName,lisp.recSymbol("DIV"))){
			   
			   return;
		   }
		   if(lisp.equal(tagName,lisp.recSymbol("meta"))||
				   lisp.equal(tagName, lisp.recSymbol("META"))){
			   evalMeta(tag,clause);
		   }
		   else{
			   System.out.println("no definition for "+tagName.toString()+" of single tag.");
		   }
		   
	   }
	   
	   public void setItemSymbol(){
		   if(this.previousFont==null){
			   this.previousFont=this.currentFont;
		   }
//		   this.currentY=this.currentY+vSpace+this.previousFont.getSize();
		   this.itemSymbol=new AnOval();
		   int fontSize=this.currentFont.getSize();
		   itemSymbol.x2=(int)(fontSize*0.5);
		   itemSymbol.y2=(int)(fontSize*0.5);
		   itemSymbol.offX=this.currentX-itemSymbol.x2-5;
		   itemSymbol.offY=this.currentY+(int)(itemSymbol.y2*0.2);
		   itemSymbol.showhide=true;
		   itemSymbol.selected=false;
		   itemSymbol.step=-10;
		   this.canvas.fs.add(itemSymbol);
	   }
	   public void holizontalLine(){
		   ALine line=new ALine();
		   line.offX=this.currentX;
		   line.offY=this.currentY;
		   line.x2=this.width;
		   line.y2=0;
		   line.step=-10;
		   this.currentY=this.currentY+vSpace+this.previousFont.getSize();
		   this.currentX=this.currentLeftMergine;
		   line.showhide=true;
		   line.selected=false;
		   this.canvas.fs.add(line);
	   }
	   public void evalScript(LispObject tag, LispObject clause){
		   lisp.plist2("script ",clause);
	   }
	   public void evalForm(LispObject tag, LispObject clause){
		   Hashtable p=getTagAttributes2(tag);   
		   String method=(String)(p.get("method"));
		   String action=(String)(p.get("action"));
		   String name=(String)(p.get("name"));
		   while(!lisp.Null(clause)){
			   LispObject t=lisp.car(clause);
			   this.evalHtml(t);
			   clause=lisp.cdr(clause);
		   }
	   }
	   public void applyFormInputTag(LispObject t){
		   Hashtable p=getTagAttributes2(t);
		   String type=(String)(p.get("type"));
		   String name=(String)(p.get("name"));
		   String value=(String)(p.get("value"));
		   String size=(String)(p.get("size"));
		   String id=(String)(p.get("id"));
		   if(type==null) return;
		   if(type.equals("text")||type.equals("TEXT")){
			   this.writeTextBox(t, p);
			   return;
		   }
		   else
		   if(type.equals("password")||type.equals("PASSWORD")){
			   
		   }
		   else
		   if(type.equals("hidden")||type.equals("HIDDEN")){
			   
		   }
		   else
		   if(type.equals("submit")||type.equals("SUBMIT")){
			   
		   }
		   else
		   if(type.equals("reset")||type.equals("RESET")){
			   
		   }
		   else
		   if(type.equals("button")||type.equals("BUTTON")){
			   
		   }
	   }
	   public void applyFormLabelTag(LispObject t){
		   Hashtable p=getTagAttributes2(t);
		   String lfor=(String)(p.get("for"));
		   if(lfor==null) return;
		   
	   }
	   public void applyFormTextAreaTag(LispObject t){
		   Hashtable p=getTagAttributes2(t);
		   String type=(String)(p.get("type"));
		   String name=(String)(p.get("name"));
		   String value=(String)(p.get("value"));
		   String size=(String)(p.get("size"));
		   if(type==null){
			   type=(String)(p.get("TYPE"));
		   }
		   if(type.equals("text")||type.equals("TEXT")){
			   
		   }
		   else
		   if(type.equals("password")||type.equals("PASSWORD")){
			   
		   }
		   else
		   if(type.equals("hidden")||type.equals("HIDDEN")){
			   
		   }
		   else
		   if(type.equals("submit")||type.equals("SUBMIT")){
			   
		   }
		   else
		   if(type.equals("reset")||type.equals("RESET")){
			   
		   }
		   else
		   if(type.equals("button")||type.equals("BUTTON")){
			   
		   }
		   
	   }
	   public String getAttributeValue(String key, Vector v){
		   int nv=v.size();
		   for(int i=0;i<nv;i++){
			   StringPair p=(StringPair)(v.elementAt(i));
				String name=p.l; String value=p.r;
				if(name.equals(key)){
					return value;
				}
			}
		   return "";
	   }
	   int xmlDecTimes=0;
	   public void applyXmlDeclaration(LispObject tag, LispObject clause){
		   if(xmlDecTimes>0) return;
		   Hashtable p=getTagAttributes2(tag);   
		   String method=(String)(p.get("method"));
		   this.charSet=(String)(p.get("charset"));
		   xmlDecTimes++;
	   }

	   public boolean isParam(LispObject x){
		   if(lisp.Null(x)) return false;
		   if(lisp.atom(x)) return false;
		   LispObject first=lisp.car(x);
		   if(lisp.eq(first,lisp.recSymbol("param"))) return true;
		   return false;		   
	   }
	   public void evalApplet(LispObject tag, LispObject clause){
		   while(!lisp.Null(clause)){
			   LispObject t=lisp.car(clause);
			   if(isParam(t)){
				   Vector p=getTagAttributes(t);
				   if(p!=null){
					   StringPair px=(StringPair)(p.elementAt(0));
					   if(px.l.equals("name")||px.l.equals("NAME")){
						   
					   }
				   }
			   }
			   clause=lisp.cdr(clause);
		   }
		   String appletName="";
		   String codebase="";
		   int width=0; int height=0;
		   Vector a=this.getTagAttributes(tag);
		   if(a==null) return;
		   int na=a.size();
		   for(int i=0;i<na;i++){
			   StringPair p=(StringPair)(a.elementAt(i));
				String name=p.l; String value=p.r;
				if(name.equals("code")||name.equals("CODE")){
					appletName=value;
				}
				if(name.equals("codebase")||name.equals("CODEBASE")){
					codebase=value;
				}
				if(name.equals("width")||name.equals("WIDTH")){
					width=new Integer(value).intValue();
				}
				if(name.equals("height")||name.equals("HEIGHT")){
					height=new Integer(value).intValue();
				}
			}
		    Applet ai=loadApplet(appletName,codebase,width,height);
		    ai.setStub(this);
  		    ai.init();
  		    AppletBox ab=new AppletBox(this.canvas,ai);
  		    ab.offX=this.currentX;
  		    ab.offY=this.currentY;
  		    this.canvas.fs.add(ab);
	   }
		public Applet loadApplet(String appletName, String codebase, int w, int h) {
		      // appletName = ... extract from the HTML CODE= somehow ...;
		      // width =       ditto
		      // height =       ditto
			  Applet ai=null;
		      try {
		         // get a Class object for the Applet subclass
		    	  if(!(codebase.endsWith("/"))){
		    		  codebase=codebase+"/";
		    	  }
		          URLClassLoader loader = new URLClassLoader(new URL[] { new URL(codebase) });
		          
		          // Load class from class loader. argv[0] is the name of the class to be loaded
		          
		          if(appletName.endsWith(".class")){
		        	  appletName=appletName.substring(0,appletName.indexOf(".class"));
		          }
		          if(appletName.endsWith(".CLASS")){
		        	  appletName=appletName.substring(0,appletName.indexOf(".CLASS"));	        	  
		          }
				  System.out.println("codebase="+codebase);
				  System.out.println("width="+w+",height="+h);
				  System.out.println("appletName="+appletName);
		         Class ac = Class.forName(appletName,false,loader);
		         // Construct an instance (as if using no-argument constructor)
		         ai = (Applet) ac.newInstance();
		      } catch(ClassNotFoundException e) {
		    	  System.out.println(e);
		          System.out.println("Applet subclass " + appletName + " did not load");
		      } catch (Exception e ){
		    	  System.out.println(e);
		          System.out.println("Applet " + appletName + " did not instantiate");
		         return null;
		      }
		      ai.setSize(w, h);
		      return ai;
		   }	   
	   public void evalImage(LispObject tag,LispObject clause){
		   Vector attr=this.getTagAttributes(tag);
		   String xurl="";
		   String width="";
		   String height="";
		   if(attr!=null){
		   int maxattr=attr.size();
		   for(int i=0;i<maxattr;i++){
			   StringPair p=(StringPair)(attr.elementAt(i));
			   if(p.l.equals("src")||p.l.equals("SRC")){
				   xurl=p.r;
				   xurl=this.removeDoubleQuotes(xurl);
			   }
			   else
			   if(p.l.equals("width")||p.l.equals("WIDTH")){
				   width=p.r;
				   width=this.removeDoubleQuotes(width);
			   }
			   else
			   if(p.l.equals("height")||p.l.equals("HEIGHT")){
				   height=p.r;
				   height=this.removeDoubleQuotes(height);
			   }
		   }
		   
		   }
//		   lisp.plist2("ul ",clause);
		   MyImage img=this.loadImage(xurl);
		   if(!width.equals("")){
			   int x= new Integer(width).intValue();
			   img.setWidth(x);
		   }
		   if(!height.equals("")){
			   int y=new Integer(height).intValue();
			   img.setHeight(y);
		   }
		   int wimg=img.getWidth();
		   int himg=img.getHeight();
		   img.setX(this.currentX);
		   img.setY(this.currentY);
		   this.currentY=this.currentY+himg;
		   return;		   
	   }
	   String removeDoubleQuotes(String x){
		   if(x.startsWith("\"")){
			   x=x.substring(1,x.length());
			   if(x.endsWith("\"")){
				   x=x.substring(0,x.length()-1);
				   return x;
			   }
		   }
		   return x;
	   }
		public MyImage loadImage(String url) {
		      // appletName = ... extract from the HTML CODE= somehow ...;
		      // width =       ditto
		      // height =       ditto
			  MyImage ai=null;
			  String imgurl=null;
			  try{
			   imgurl=this.makeUrl(new URL(this.codebase),url);
			  }
			  catch(Exception e){
				  return null;
			  }
			  MyImage img=new MyImage(this.canvas,imgurl,this.getNodeSettings());
	          img.color=this.fgColor;
	          img.offX=this.currentX;
	          img.offY=this.currentY;
	           this.currentY=this.currentY+img.y2+vSpace;
	           if(this.settingCenter){
	        	   img.offX=this.currentLeftMergine+(this.width/2)-(img.x2/2);
	           }
	           img.showhide=true;
	           img.selected=false;
	           this.canvas.fs.add(img);
	           if(this.currentHyperLink!=null){
	        	   UrlBox box=new UrlBox(this.canvas);
	        	   box.offX=img.offX; box.offY=img.offY;
	        	   box.x2=img.x2; box.y2=img.y2;
	        	   box.setUrl(this.currentHyperLink);
	        	   box.showhide=true;
	        	   box.selected=false;
	        	   this.canvas.fs.add(box);
	           }              
	           return img;
	   }
	   public void evalOl(LispObject tag, LispObject clause){	  
		   HtmlEnvironment env=new HtmlEnvironment(this);
		   env.currentX=env.currentX+this.tabSize;
		   while(!lisp.Null(clause)){
			   LispObject x=lisp.car(clause);
              if(isLi(x)){
		    	  setItemSymbol();
		      }
		      else{
	             env.eval(clause);
		      }
	          clause=lisp.cdr(clause);
	      }
	      this.currentY=env.currentY+this.vSpace;
	   }
	   public void evalUl(LispObject tag, LispObject clause){
		   lisp.plist2("eval-ui",clause);
		   HtmlEnvironment env=new HtmlEnvironment(this);
		   env.currentX=env.currentX+this.tabSize;
		   int xx=env.currentX;
	      while(!lisp.Null(clause)){
			   LispObject x=lisp.car(clause);
		      if(isLi(x)){
		    	  setItemSymbol();
		    	  env.currentX=xx;
		    	  this.currentX=xx;
		      }
		      else{
		    	 env.currentY=this.currentY;
	             env.evalHtml(clause);
		      }
	    	  this.currentY=this.currentY+this.vSpace;
	          clause=lisp.cdr(clause);
	      }
	   }
	   public boolean isLi(LispObject x){
		   if(lisp.Null(x)) return false;
		   if(lisp.atom(x)) return false;
		   LispObject first=lisp.car(x);
		   if(!lisp.eq(first,lisp.recSymbol("tag"))) return false;
		   LispObject c=lisp.car(lisp.cdr(x));
		   if(lisp.equal(c,lisp.recSymbol("li"))||
				   lisp.equal(c,lisp.recSymbol("LI"))) return true;
		   return false;
       }
       public Vector getTagAttributes(LispObject x){
    	   if(!(isTag(x)||isSingleTag(x))) return null;
    	   Vector rtn=new Vector();
    	   LispObject w=lisp.cdr(x); //x=(tag <1> <2> <3> ...), w=(<1> <2> <3> ...)
    	   if(lisp.Null(w)) return null;
    	   w=lisp.cdr(w);            // w=(<2> <3> ...)
    	   while(!lisp.Null(w)){
    		   LispObject u=lisp.car(w);
    		   if(isString(u)){
    			   LispObject s=lisp.car(lisp.cdr(u));
    			   String attrs=s.toString();
    			   if(attrs.indexOf(" ")!=0){
    				   StringTokenizer st=new StringTokenizer(attrs," =",true);
    				   int lr=0;
    				   StringPair pair=new StringPair();
    				   while(st.hasMoreElements()){
    					   String xx=st.nextToken();
    					   if(!xx.equals("=")){
    						   if(xx.equals("\n")){}
    						   else
    						   if(xx.equals("\t")){}
    						   else
    						   if(xx.equals("\r")){}
    						   if(xx.equals("\f")){}
    						   else
    						   if(xx.equals(" ")){}
    						   else{
    						      if(lr==0)
    							     pair.l=xx;
    						      if(lr==1){
    						    	  pair.r=xx;
    						    	  rtn.add(pair);
    						    	  lr=0;
    						      }
    						   }
    					   }
    					   else
    					   {
    						   lr=1;
    					   }
    				   }
    			   }
    		   }
    		   w=lisp.cdr(w);
    	   }
    	   return rtn;
       }
       public Hashtable getTagAttributes2(LispObject x){
    	   if(!(isTag(x)||isSingleTag(x))) return null;
    	   Hashtable rtn=new Hashtable();
    	   LispObject w=lisp.cdr(x); //x=(tag <1> <2> <3> ...), w=(<1> <2> <3> ...)
    	   if(lisp.Null(w)) return null;
    	   w=lisp.cdr(w);            // w=(<2> <3> ...)
    	   while(!lisp.Null(w)){
    		   LispObject u=lisp.car(w);
    		   if(isString(u)){
    			   LispObject s=lisp.car(lisp.cdr(u));
    			   String attrs=s.toString();
    			   if(attrs.indexOf(" ")!=0){
    				   StringTokenizer st=new StringTokenizer(attrs," =",true);
    				   int lr=0;
    				   StringPair pair=new StringPair();
    				   while(st.hasMoreElements()){
    					   String xx=st.nextToken();
    					   if(!xx.equals("=")){
    						   if(xx.equals("\n")){}
    						   else
    						   if(xx.equals("\t")){}
    						   else
    						   if(xx.equals("\r")){}
    						   if(xx.equals("\f")){}
    						   else
    						   if(xx.equals(" ")){}
    						   else{
    						      if(lr==0)
    							     pair.l=xx.toLowerCase();
    						      if(lr==1){
    						    	  pair.r=this.removeDoubleQuotes(xx);
    						    	  rtn.put(pair.l, pair.r);
    						    	  lr=0;
    						      }
    						   }
    					   }
    					   else
    					   {
    						   lr=1;
    					   }
    				   }
    			   }
    		   }
    		   w=lisp.cdr(w);
    	   }
    	   return rtn;
       }
   public void evalMeta(LispObject tag,LispObject clause){
	   Vector attr=this.getTagAttributes(tag);
	   String httpEquiv="";
	   String content="";
	   if(attr!=null){
		   int maxattr=attr.size();
		   for(int i=0;i<maxattr;i++){
			   StringPair p=(StringPair)(attr.elementAt(i));
			   if(p.l.equals("http-equiv")||p.l.equals("HTTP-EQUIV")){
				   httpEquiv=p.r;
				   if(httpEquiv.startsWith("\"")){
					   httpEquiv=httpEquiv.substring(1,httpEquiv.length());
					   if(httpEquiv.endsWith("\"")){
						   httpEquiv=httpEquiv.substring(0,httpEquiv.length()-1);
					   }
				   }
			   }
			   if(p.l.equals("content")||p.l.equals("CONTENT")){
				   content=p.r;
				   if(content.startsWith("\"")){
					   content=content.substring(1,content.length());
					   if(content.endsWith("\"")){
						   content=content.substring(0,content.length()-1);
					   }
				   }
				   StringTokenizer st=new StringTokenizer(content," =;");
                   String charset="";
                   while(st.hasMoreTokens()){
                	   String t=st.nextToken();
                	   if(t.equals("charset")){
                		   String eq=st.nextToken(); // eq must be "="
                		   charset=st.nextToken();
                		   break;
                	   }
                   }
                   this.charSet=charset;
			   }
		   }
		   
	    }
//		   lisp.plist2("ul ",clause);
		return;		   
	}
    public class StringPair{
    	   String l,r;
       }
       	
    public String makeUrl(URL base, String value){
	  String sBase=base.toString();
//	  System.out.println("base="+sBase);
	  try{
	  if(value.startsWith("http:")||value.startsWith("HTTP:")){
		  return value;  
	  }
	  else
	  if(sBase.endsWith(".html")||sBase.endsWith(".htm")||
		 sBase.endsWith(".HTML")||sBase.endsWith(".HTM")){
			  URL u=new URL(base,value);
    		  return u.toString();
		  
	  }
	  else
	  if((!sBase.endsWith("/")) &&
		 (!value.startsWith("/"))){
           return sBase+"/"+value;
	  }
	  else{
			  URL u=new URL(base,value);
    		  return u.toString();
	  }
	  }
	  catch(MalformedURLException ex){
		  System.out.println("attribute base="+sBase);
		  System.out.println("attribute value="+value);
		  return "";
	  }
    }

	public boolean isActive() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	public URL getDocumentBase() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public URL getCodeBase() {
		// TODO 自動生成されたメソッド・スタブ
		URL rtn=null;
		try{
		   rtn=new URL(this.url);
		}
		catch(Exception e){}
		return rtn;
	}

	public String getParameter(String arg0) {
		// TODO 自動生成されたメソッド・スタブ
		AppletAndBase ab=(AppletAndBase)this.applets.get(arg0);
		if(ab==null) return null;
		MutableAttributeSet p=ab.param;
		if(p==null) return null;
		Enumeration e = p.getAttributeNames();
		while (e.hasMoreElements()){
			Object name = e.nextElement();
			String value = (String) p.getAttribute(name);
			String xname=(String)name;
			if(xname.equals(arg0)){
				return value;
			}
		}
		return null;
	}

	public AppletContext getAppletContext() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public void appletResize(int arg0, int arg1) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public AudioClip getAudioClip(URL arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public Image getImage(URL arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public Applet getApplet(String arg0) {
		// TODO 自動生成されたメソッド・スタブ
		AppletAndBase ab=(AppletAndBase)applets.get(arg0);
		Applet rtn=ab.applet;
		return rtn; 
	}

	public Enumeration getApplets() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public void showDocument(URL arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void showDocument(URL arg0, String arg1) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void showStatus(String arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void setStream(String arg0, InputStream arg1) throws IOException {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public InputStream getStream(String arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public Iterator getStreamKeys() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
	
    Hashtable applets;
    
    class AppletAndBase{
    	public String appletName;
    	public Applet applet;
    	public String codeBase;
    	public MutableAttributeSet param;
    	public int position;
    	public AppletAndBase(String n, Applet x, String b, MutableAttributeSet p,int pos){
    		appletName=n; applet=x; codeBase=b; param=p; position=pos;
    	}
    }
    
    public void addApplet(String name, Applet x,String base, MutableAttributeSet p,int pos){
        AppletAndBase ab=new AppletAndBase(name,x,base,p,pos); 
        applets.put(name,ab);
    }
    
    public void stopApplets(){
    	if(applets==null) return;
    	Enumeration el=applets.elements();
    	while(el.hasMoreElements()){
    		AppletAndBase ab=(AppletAndBase)el.nextElement();
    		Applet a=ab.applet;
    		a.stop();
    	}
    	applets.clear();
    }
    String makeCodebase(String url){
		String rtn = url;
		if(rtn.endsWith(".htm")||rtn.endsWith(".html")){
			rtn = rtn.substring(0,rtn.lastIndexOf('/'));
		}
		return rtn;

    }
    public NodeSettings getNodeSettings(){
    	return this.gui.getNodeSettings();
    }
    public String getCharSet(){
    	return this.charSet;
    }
}

