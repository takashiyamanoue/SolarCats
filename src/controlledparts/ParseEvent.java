package controlledparts;
import java.awt.Color;
import java.awt.Event;
import java.util.Vector;

import application.draw.DrawFrame;
import application.texteditor.TextEditFrame;
import controlledparts.ControlledFrame;
import controlledparts.FrameWithControlledButton;
import controlledparts.FrameWithControlledCheckBox;
import controlledparts.FrameWithControlledEditPane;
import controlledparts.FrameWithControlledFigCanvas;
import controlledparts.FrameWithControlledPane;
import controlledparts.FrameWithControlledSlider;
import controlledparts.FrameWithControlledTextAreas;
import controlledparts.FrameWithControlledTextField;
import controlledparts.FrameWithControlledTabbedPane;
import nodesystem.*;

public class ParseEvent extends java.lang.Object // implements Runnable
{
    public boolean parseCheckBoxExit(FrameWithControlledCheckBox f)
    {
         parseB();
        if(!iq.rString("exit(")) return false;

        Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(")")) return false;
        f.unfocusCheckBox(c.intValue());

        return true;
     }

    public boolean parseCheckBoxEnter(FrameWithControlledCheckBox f)
    {
         parseB();
        if(!iq.rString("enter(")) return false;

        Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(")")) return false;
        f.focusCheckBox(c.intValue());

        return true;
    }

    public boolean parseCheckBoxStateChanged(FrameWithControlledCheckBox f)
    {
         parseB();
        if(!iq.rString("state(")) return false;

        if(!rNumPair()) return false;
        if(!iq.rString(")")) return false;
        f.changeStateCheckBox(x1,y1);
        return true;
    }

    public boolean parseCheckBoxEvent(FrameWithControlledCheckBox f)
    {
        parseB();
        if(!iq.rString("cbx.")) return false;
        if(parseCheckBoxStateChanged(f) ||
           parseCheckBoxEnter(f)        ||
           parseCheckBoxExit(f)
        ) return true;
        return false;

    }

    public boolean parseTextEnterPress(FrameWithControlledTextField f)
    {
         parseB();
        if(!iq.rString("etr(")) return false;
        Integer x=iq.rInteger();
        if(x==null) return false;
        if(!iq.rString(")")) return false;
        f.enterPressed(x.intValue());
        return true;
    }

    public boolean parseTextField(FrameWithControlledTextField f)
    {
        parseB();
        FrameWithControlledTextAreas g=(FrameWithControlledTextAreas)f;
        if(!iq.rString("fld.")) return false;
          if(parseTextClicked(g) ||
             parseTextKeyType(g) ||
             parseTextMouseEnter(g)||
             parseTextMouseExit(g)||
             parseTextMouseMove(g)||
             parseTextMouseDrag(g)||
             parseTextMouseRelease(g)||
//             parseTextMouseDown(f)||
             parseTextMousePress(g) ||
             parseTextSetText(g)||
             parseTextEnterPress(f)
            ) return true;
        return false;
    }

    public boolean parseTextSetText(FrameWithControlledTextAreas f)
    {
        parseB();
        if(!iq.rString("set(")) return false;
        Integer x=iq.rInteger();
        if(x==null) return false;
        if(!iq.rString(",")) return false;
		Integer y=iq.rInteger();
		if(y==null) return false;
		if(!iq.rString(",")) return false;
        StringBuffer str=iq.rStrConst();
        if(str==null) return false;
        parseB();
        if(!iq.rString(")"))    return false;
        int id=x.intValue();
        int pos=y.intValue();
        f.setTextOnTheText(id,pos,str.toString());
        return true;
    }

    public String parsingString;

    public boolean parseFcSubEvent(FrameWithControlledFigCanvas f, int id)
    {
        if( parseMouseMove(f,id)           ||
            parseMouseDown(f,id)           ||
            parseMouseDrag(f,id)           ||
            parseMouseUp(f,id)             ||
            parseMouseEnter(f,id)          ||
            parseMouseExit(f,id)           ||
            parseKeyDown(f,id)             ||
            parseScrollBarEvent((FrameWithControlledPane)f))
            return true;
        return false;
    }
    public boolean parseFcEvent(FrameWithControlledFigCanvas f)
    {
        int id;
        parseB();
        if(!iq.rString("fc(")) return false;
        Integer x=iq.rInteger();
        if(x==null) return false;
        id=x.intValue();
        if(!iq.rString(").")) return false;        
        if(parseFcSubEvent(f,id)) return true;
        return false;
    }
    public void run()
    {
         parseB();
         int x=1;
         while(!iq.eos()){
             boolean iter=true;
             try{
              iter=parseEvent();
              if(!iter){
                  System.out.println(this.parsingString);
//                  System.out.println(this.iq.toString);
                  Thread.dumpStack();
                  gui.recordMessage(""+ ",\"parseError \",,,");
                 if(!errorRecovery()) return;
              }
             }
             catch(Exception e){
            	 System.out.println("this.parsingString:"+this.parsingString);
            	 Thread.dumpStack();
            	 System.out.println(""+e.toString());
             }
         }
    }
    public boolean parseTextMouseMove(FrameWithControlledTextAreas f)
    {
         parseB();
        if(!iq.rString("mm(")) return false;

        if(!rNumTriple()) return false;
        if(!iq.rString(")")) return false;
        f.moveMouseOnTheText(x1,y1,z1);
        return true;
    }

    public boolean parseTextMouseExit(FrameWithControlledTextAreas f)
    {
         parseB();
        if(!iq.rString("mxit(")) return false;

        if(!rNumTriple()) return false;
        if(!iq.rString(")")) return false;
        f.exitMouseOnTheText(x1,y1,z1);
        return true;
    }

    public boolean parseTextMouseEnter(FrameWithControlledTextAreas f)
    {
         parseB();
        if(!iq.rString("ment(")) return false;

        if(!rNumTriple()) return false;
        if(!iq.rString(")")) return false;
//        f.mouseEnteredAtTheText(x1,y1,z1);
        f.enterMouseOnTheText(x1,y1,z1);
        return true;
    }

    public boolean parseTextUpdateHyperLink(FrameWithControlledEditPane f)
    {
         parseB();
        if(!iq.rString("hpl(")) return false;
         Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(",")) return false;
        StringBuffer str=iq.rStrConst();
        if(str==null) return false;
        if(!iq.rString(")")) return false;
        f.updateHyperLink(c.intValue(),str.toString() );
        return true;
    }

    public boolean parseTextMouseExit(FrameWithControlledEditPane f)
    {
         parseB();
        if(!iq.rString("mxit(")) return false;

        if(!rNumTriple()) return false;
        if(!iq.rString(")")) return false;
        f.exitMouseOnTheEPane(x1,y1,z1);
        return true;
    }

    public boolean parseTextMouseEnter(FrameWithControlledEditPane f)
    {
         parseB();
        if(!iq.rString("ment(")) return false;

        if(!rNumTriple()) return false;
        if(!iq.rString(")")) return false;
//        f.mouseEnteredAtTheText(x1,y1,z1);
        f.enterMouseOnTheEPane(x1,y1,z1);
        return true;
    }

    public boolean parseTextMouseMove(FrameWithControlledEditPane f)
    {
         parseB();
        if(!iq.rString("mm(")) return false;

        if(!rNumTriple()) return false;
        if(!iq.rString(")")) return false;
        f.moveMouseOnTheEPane(x1,y1,z1);
        return true;
    }

    public boolean parseFrameEvent(ControlledFrame f)
    {
        parseB();
        if(!iq.rString("frm.")) return false;
          if(
             parseMouseEnter(f)||
             parseMouseExit(f) ||
             parseMouseMove(f) ||
             parseFocus(f)     ||
             parseMinimize(f)  ||
             parseNormalize(f) ||
             parseClose(f)
            ) return true;
        return false;
    }

    public boolean parseMouseExit(ControlledFrame f)
    {
        int offX, offY;
        parseB();
        if(!iq.rString("mxit(")) return false;
        if(!rNumPair()) return false;
        // message.appendText("parsing polygon(");
        if(!iq.rString(")")) return false;
        Event ev=new Event(gui,(long)0,Event.MOUSE_EXIT,
        x1,y1,0,0);
//        message.appendText("mm("+x1+","+y1+")\n");
//        gui.postEvent(ev);
        f.exitMouseAtTheFrame(x1,y1);
        return true;
    }

    public boolean parseMouseEnter(ControlledFrame f)
    {
        int offX, offY;
        parseB();
        if(!iq.rString("ment(")) return false;
        if(!rNumPair()) return false;
        // message.appendText("parsing polygon(");
        if(!iq.rString(")")) return false;
//        Event ev=new Event(gui,(long)0,Event.MOUSE_ENTER,
//        x1,y1,0,0);
//        message.appendText("mm("+x1+","+y1+")\n");
//        gui.postEvent(ev);
        f.enterMouseAtTheFrame(x1,y1);
        return true;
    }

    public boolean parseMouseMove(ControlledFrame f)
    {
        int offX, offY;
        parseB();
        if(!iq.rString("mm(")) return false;
        if(!rNumPair()) return false;
        // message.appendText("parsing polygon(");
        if(!iq.rString(")")) return false;
//        Event ev=new Event(gui,(long)0,Event.MOUSE_MOVE,
//        x1,y1,0,0);
//        message.appendText("mm("+x1+","+y1+")\n");
//        gui.postEvent(ev);
        f.moveMouseAtTheFrame(x1,y1);
        return true;
    }

    public boolean parseFocus(ControlledFrame f)
    {
        parseB();
        if(!iq.rString("fgain()")) return false;
        f.gainFocus();
        return true;
    }

    public boolean parseMinimize(ControlledFrame f)
    {
        parseB();
        if(!iq.rString("minimize()")) return false;
        f.minimizingWindow();
        return true;
    }
    
    public boolean parseNormalize(ControlledFrame f)
    {
    	parseB();
    	if(!iq.rString("normalize()")) return false;
    	f.normalizingWindow();
    	return true;
    }

    public boolean parseClose(ControlledFrame f)
    {
    	parseB();
    	if(!iq.rString("close()")) return false;
    	f.dispose();
    	return true;
    }

    public boolean parseTextMousePress(FrameWithControlledTextAreas f)
    {
         parseB();
        if(!iq.rString("mps(")) return false;

        if(!rNumPair()) return false;
        if(!iq.rString(",")) return false;
        int id=x1; int p=y1;
        if(!rNumPair()) return false;
        if(!iq.rString(")")) return false;
        f.pressMouseOnTheText(id,p,x1, y1);
        return true;
    }

    public boolean parseTextMousePress(FrameWithControlledEditPane f)
    {
         parseB();
        if(!iq.rString("mps(")) return false;

        if(!rNumPair()) return false;
        if(!iq.rString(",")) return false;
        int id=x1; int p=y1;
        if(!rNumPair()) return false;
        if(!iq.rString(")")) return false;
        f.pressMouseOnTheEPane(id,p,x1, y1);
        return true;
    }

    public boolean parseScrollBarValue(int id, FrameWithControlledPane p)
    {
         parseB();
        if(!iq.rString("sbv(")) return false;
        if(!this.rNumPair()) return false;
        if(!iq.rString(")")) return false;
        p.changeScrollbarValue(id,x1,y1);
        return true;

    }

    public boolean parseScrollBarHidden(int pid,FrameWithControlledPane p)
    {
         parseB();
        if(!iq.rString("sbh(")) return false;
        Integer x=iq.rInteger();
        if(!iq.rString(")")) return false;
        int sid=x.intValue();
        p.hideScrollBar(pid,sid);
        return true;
    }

    public boolean parseScrollBarShown(int pid,FrameWithControlledPane p)
    {
         parseB();
        if(!iq.rString("sbs(")) return false;
        Integer x=iq.rInteger();
        if(!iq.rString(")")) return false;
        int sid=x.intValue();
        p.showScrollBar(pid,sid);
        return true;
    }

    public boolean parseScrollBarEvent(FrameWithControlledPane p)
    {
        parseB();
        if(!iq.rString("pane(")) return false;
        Integer x=iq.rInteger();
        if(!iq.rString(").")) return false;
        if(x==null) return false;
        int pid=x.intValue();
        if(parseScrollBarShown(pid,p) ||
             parseScrollBarHidden(pid,p) ||
             parseScrollBarValue(pid,p)
          ) return true;
        return false;
    }

    public boolean parseLoadEvent(LoadFileFrame f)
    {
        parseB();
        if(!iq.rString("loadfd.")) return false;
        if(this.parseButtonEvent(f)) return true;
        if(parseFileDialogEvent(f)) return true;
        return false;
    }

    public boolean parseFileDialogEvent(DialogListener d)
    {
        parseB();
        if(!iq.rString("fdialog(")) return false;

         Integer c=iq.rInteger();
        if(c==null) return false;
//       StringBuffer str=iq.rStrConst();
//        if(str==null) return false;

        if(!iq.rString(").")) return false;
        Vector dialogs=d.getDialogs();
        JFileDialog fd=(JFileDialog)(dialogs.elementAt(c.intValue()));
        if( this.parseButtonEvent(fd)||
            this.parseTextEvent(fd)  || 
            this.parseScrollBarEvent(fd)) return true;
        return false;
       
    }

    public boolean parseFileEvent(FileFrame ff)
    {
        parseB();
        if(!iq.rString("file.")) return false;
        if(parseFileDialogEvent(ff)) return true;
        if(parseButtonEvent(ff)) return true;;
        return false;
    }


    public boolean parseTextMouseRelease(FrameWithControlledTextAreas f)
    {
         parseB();
        if(!iq.rString("mrl(")) return false;

        if(!rNumPair()) return false;
        if(!iq.rString(",")) return false;
        int id=x1; int p=y1;
        if(!rNumPair()) return false;
        if(!iq.rString(")")) return false;
        f.releaseMouseOnTheText(id,p,x1, y1);
        return true;
    }

    public boolean parseTextMouseRelease(FrameWithControlledEditPane f)
    {
         parseB();
        if(!iq.rString("mrl(")) return false;

        if(!rNumPair()) return false;
        if(!iq.rString(",")) return false;
        int id=x1; int p=y1;
        if(!rNumPair()) return false;
        if(!iq.rString(")")) return false;
        f.releaseMouseOnTheEPane(id,p,x1, y1);
        return true;
    }

    public boolean parseTextMouseDrag(FrameWithControlledTextAreas f)
    {
         parseB();
        if(!iq.rString("mdg(")) return false;

        if(!rNumPair()) return false;
        if(!iq.rString(",")) return false;
        int id=x1; int p=y1;
        if(!rNumPair()) return false;
        if(!iq.rString(")")) return false;
        f.dragMouseOnTheText(id,p,x1, y1);
        return true;
    }

    public boolean parseTextMouseDrag(FrameWithControlledEditPane f)
    {
         parseB();
        if(!iq.rString("mdg(")) return false;

        if(!rNumPair()) return false;
        if(!iq.rString(",")) return false;
        int id=x1; int p=y1;
        if(!rNumPair()) return false;
        if(!iq.rString(")")) return false;
        f.dragMouseOnTheEPane(id,p,x1, y1);
        return true;
    }

	public boolean parseTextKeyPress(FrameWithControlledTextAreas f)
	{
		 parseB();
		if(!iq.rString("kps(")) return false;

		if(!rNumTriple()) return false;
		if(!iq.rString(")")) return false;
		f.pressKey(x1,y1,z1);
		return true;
	}
    public boolean parseTextKeyType(FrameWithControlledTextAreas f)
    {
         parseB();
        if(!iq.rString("kty(")) return false;

        if(!rNumTriple()) return false;
        if(!iq.rString(")")) return false;
        f.typeKey(x1,y1,z1);
        return true;
    }
	public boolean parseTextKeyRelease(FrameWithControlledTextAreas f)
	{
		 parseB();
		if(!iq.rString("rls(")) return false;

		if(!rNumTriple()) return false;
		if(!iq.rString(")")) return false;
		f.releaseKey(x1,y1,z1);
		return true;
	}
    public boolean parseTextKeyType(FrameWithControlledEditPane f)
    {
         parseB();
        if(!iq.rString("kty(")) return false;

        if(!rNumTriple()) return false;
        if(!iq.rString(")")) return false;
        f.typeKeyAtTheEPane(x1,y1,z1);
        return true;
    }
    public boolean parseEditPaneTextKeyType(FrameWithControlledEditPane f)
    {
         parseB();
        if(!iq.rString("kty(")) return false;

        if(!rNumTriple()) return false;
        if(!iq.rString(")")) return false;
        f.typeKeyAtTheEPane(x1,y1,z1);
        return true;
    }

    public boolean rNumTriple()
    {
        parseB();
        Integer x=iq.rInteger();
        if(x==null) return false;
        x1=x.intValue();
        parseB();
        
        if(!iq.rString(",")) return false;
        parseB();
        
        Integer y=iq.rInteger();
        if(y==null) return false;
        y1=y.intValue();
     
        if(!iq.rString(",")) return false;
        parseB();
        
        Integer z=iq.rInteger();
        if(z==null) return false;
        z1=z.intValue();
        return true;
    }

    public int z1;

    public boolean parseTextClicked(FrameWithControlledTextAreas f)
    {
         parseB();
        if(!iq.rString("mdn(")) return false;

        if(!rNumPair()) return false;
        int p=x1; int key=y1;
        if(!iq.rString(",")) return false;
        if(!rNumPair()) return false;
        if(!iq.rString(")")) return false;
        f.clickMouseOnTheText(p,key,x1,y1);
        return true;
    }

    public boolean parseTextClicked(FrameWithControlledEditPane f)
    {
         parseB();
        if(!iq.rString("mdn(")) return false;

        if(!rNumPair()) return false;
        int p=x1; int key=y1;
        if(!iq.rString(",")) return false;
        if(!rNumPair()) return false;
        if(!iq.rString(")")) return false;
        f.clickMouseOnTheEPane(p,key,x1,y1);
        return true;
    }

    public boolean parseTextEvent(FrameWithControlledTextAreas f)
    {
        parseB();
        if(!iq.rString("txt.")) return false;
          if(parseTextClicked(f) ||
             parseTextKeyType(f) ||
             parseTextKeyRelease(f)||
             parseTextKeyPress(f)||
             parseTextMouseEnter(f)||
             parseTextMouseExit(f)||
             parseTextMouseMove(f)||
             parseTextMouseDrag(f)||
             parseTextMouseRelease(f)||
//             parseTextMouseDown(f)||
             parseTextMousePress(f) ||
             parseTextSetText(f)
            ) return true;
        return false;
        
    }

    public boolean parseEditPaneEvent(FrameWithControlledEditPane f)
    {
        parseB();
        if(!iq.rString("etxt.")) return false;
          if(parseTextClicked(f) ||
             parseEditPaneTextKeyType(f) ||
             parseTextMouseDrag(f)||
             parseTextMouseRelease(f)||
//             parseTextMouseDown(f)||
             parseTextMousePress(f)||
             parseTextMouseMove(f) ||
             parseTextMouseEnter(f) ||
             parseTextMouseExit(f)  ||
             parseTextUpdateHyperLink(f)
            ) return true;
        return false;
        
    }

    public boolean parseDTextEditEvent(TextEditFrame f)
    {
        parseB();
        if(!iq.rString("txtedit.")) return false;
        if(parseTextEvent(f)) return true;
        if(this.parseButtonEvent(f)) return true;
        if(this.parseFileEvent(f.fileFrame)) return true;
        if(this.parseScrollBarEvent(f)) return true;
        if(this.parseFocus(f)) return true;
        if(this.parseFrameEvent(f)) return true;
        return false;
    }

    public boolean parseExit(ControlledFrame f)
    {
        /*
        parseB();
        if(!iq.rString("exit()")) return false;
        f.exitThis();
        */
        return true;
    }

    public boolean parseSliderExit(FrameWithControlledSlider f)
    {
         parseB();
        if(!iq.rString("exit(")) return false;
        Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(")")) return false;
		f.exitMouseOnTheSlider(c.intValue());
        return true;
    }

    public boolean parseSliderEnter(FrameWithControlledSlider f)
    {
         parseB();
        if(!iq.rString("enter(")) return false;

        Integer c=iq.rInteger();
        if(c==null) return false;
//        StringBuffer str=iq.rStrConst();
//        if(str==null) return false;


        if(!iq.rString(")")) return false;
//		f.depthIndicator.setBackground(Color.white);
		f.enterMouseOnTheSlider(c.intValue());

        return true;
    }

    public boolean parseSliderStateChange(FrameWithControlledSlider f)
    {
         parseB();
        if(!iq.rString("state(")) return false;
        if(!rNumPair()) return false;
        if(!iq.rString(")")) return false;
	    	f.changeStateOnTheSlider(x1,y1);
        return true;
        
    }
    
	public boolean parseSliderEvent(FrameWithControlledSlider f){
		parseB();
		if(!iq.rString("slid.")) return false;
		if(parseSliderStateChange(f)||
		   parseSliderEnter(f)   ||
		   parseSliderExit(f)
		) return true;
		return false;
	}

	public boolean parseTabbedPaneStateChange(FrameWithControlledTabbedPane f){
		parseB();
	   if(!iq.rString("state(")) return false;
	   if(!rNumPair()) return false;
	   if(!iq.rString(")")) return false;
		   f.changeStateOnTheTabbedPane(x1,y1);
	   return true;
        
	}
    
	public boolean parseTabbedPaneEnter(FrameWithControlledTabbedPane f)
	{
		 parseB(); 
		if(!iq.rString("enter(")) return false;

		Integer c=iq.rInteger();
		if(c==null) return false;
//		  StringBuffer str=iq.rStrConst();
//		  if(str==null) return false;


		if(!iq.rString(")")) return false;
//		f.depthIndicator.setBackground(Color.white);
		f.enterMouseOnTheTabbedPane(c.intValue());

		return true;
	}

	public boolean parseTabbedPaneExit(FrameWithControlledTabbedPane f)
	{
		 parseB();
		if(!iq.rString("exit(")) return false;
		Integer c=iq.rInteger();
		if(c==null) return false;
		if(!iq.rString(")")) return false;
		f.exitMouseOnTheTabbedPane(c.intValue());
		return true;
	}

    public boolean parseTabbedPaneEvent(FrameWithControlledTabbedPane f)
    {
        parseB();
        if(!iq.rString("tbpane.")) return false;
        if(parseTabbedPaneStateChange(f)||
           parseTabbedPaneEnter(f)   ||
           parseTabbedPaneExit(f)
        ) return true;
        return false;
    }

	public boolean parseMenuEvent(FrameWithControlledMenu f){
		parseB();
		if(!iq.rString("menu.")) return false;
		if(parseMenuMouseClicked(f)||
		   parseMenuMouseEntered(f)||
		   parseMenuMouseExit(f)
		) return true;
		return false;
	}
	
	public boolean parseMenuMouseEntered(FrameWithControlledMenu f){
        parseB();
        if(!iq.rString("enter(")) return false;

         Integer c=iq.rInteger();
        if(c==null) return false;

        if(!iq.rString(")")) return false;
        int x=c.intValue();
        f.enterMenu(x);
        return true;
	}

	public boolean parseMenuMouseExit(FrameWithControlledMenu f){
        parseB();
        if(!iq.rString("exit(")) return false;

         Integer c=iq.rInteger();
        if(c==null) return false;

        if(!iq.rString(")")) return false;
        int x=c.intValue();
        f.exitMenu(x);
        return true;
	}

	public boolean parseMenuMouseClicked(FrameWithControlledMenu f){
        parseB();
        if(!iq.rString("click(")) return false;

         Integer c=iq.rInteger();
        if(c==null) return false;

        if(!iq.rString(")")) return false;
        int x=c.intValue();
        f.clickMenu(x);
        return true;
	}

	public boolean parseMenuItemEvent(FrameWithControlledMenuItem f){
		parseB();
		if(!iq.rString("item.")) return false;
		if(parseMenuItemMouseClicked(f)||
		   parseMenuItemMouseEntered(f)||
		   parseMenuItemMouseExit(f)
		) return true;
		return false;
	}
	
	public boolean parseMenuItemMouseEntered(FrameWithControlledMenuItem f){
        parseB();
        if(!iq.rString("enter(")) return false;

         Integer c=iq.rInteger();
        if(c==null) return false;

        if(!iq.rString(")")) return false;
        int x=c.intValue();
        f.enterMenuItem(x);
        return true;
	}

	public boolean parseMenuItemMouseExit(FrameWithControlledMenuItem f){
        parseB();
        if(!iq.rString("exit(")) return false;

         Integer c=iq.rInteger();
        if(c==null) return false;

        if(!iq.rString(")")) return false;
        int x=c.intValue();
        f.exitMenuItem(x);
        return true;
	}

	public boolean parseMenuItemMouseClicked(FrameWithControlledMenuItem f){
        parseB();
        if(!iq.rString("click(")) return false;

         Integer c=iq.rInteger();
        if(c==null) return false;

        if(!iq.rString(")")) return false;
        int x=c.intValue();
        f.clickMenuItem(x);
        return true;
	}

    public boolean parseButtonExit(FrameWithControlledButton f)
    {
         parseB();
        if(!iq.rString("exit(")) return false;

        Integer c=iq.rInteger();
        if(c==null) return false;
//        StringBuffer str=iq.rStrConst();
 //       if(str==null) return false;


        if(!iq.rString(")")) return false;
        f.unfocusButton(c.intValue());

        return true;
    }

    public boolean parseButtonEnter(FrameWithControlledButton f)
    {
         parseB();
        if(!iq.rString("enter(")) return false;

        Integer c=iq.rInteger();
        if(c==null) return false;
//        StringBuffer str=iq.rStrConst();
//        if(str==null) return false;


        if(!iq.rString(")")) return false;
        f.focusButton(c.intValue());

        return true;
    }

    public boolean parseButtonEvent(FrameWithControlledButton f)
    {
        parseB();
        if(!iq.rString("btn.")) return false;
        if(parseButtonClicked(f)||
           parseButtonEnter(f)   ||
           parseButtonExit(f)
        ) return true;
        return false;
    }

    public boolean parseDrawSubEvent(DrawFrame f)
    {
        if( parseButtonEvent(f)  ||
            parseSliderEvent(f) // ||
//            parseExit((ControlledFrame)f)   
           )
            return true;
        return false;
    }

    public boolean parseDrawEvent()
    {
        parseB();
        if(!iq.rString("draw.")) return false;
        if(parseDrawSubEvent(gui)) return true;
        return false;
    }

    public boolean parseOpenFigFile()
    {
        if(!iq.rString("-(")) return false;

        StringBuffer dir=iq.rStrConst();
        if(dir==null) return false;

        if(!iq.rString("-")) return false;

        StringBuffer file=iq.rStrConst();
        if(file==null) return false;

        if(!iq.rString(")")) return false;

        gui.editdispatch.loadFile(dir.toString(),file.toString());
        return true;
    }
    public boolean parseScrollV()
    {
        if(!iq.rString("v.")) return false;
        if(!iq.rString("a()")) {
//            gui.figcanvas.vScrollBar.action(null,null);
        }
        else
        if(!iq.rString("md(")){
           if(!rNumPair()) return false;
           if(!iq.rString(")")) return false;
//           gui.figcanvas.vScrollBar.mouseDrag(null,x1,y1);
           return true;
        }
        else
        if(!iq.rString("mu(")){
           if(!rNumPair()) return false;
           if(!iq.rString(")")) return false;
//           gui.figcanvas.vScrollBar.mouseUp(null,x1,y1);
           return true;
       }
        return false;
    }
    public boolean parseScrollH()
    {
        if(!iq.rString("h.")) return false;
        if(!iq.rString("a()")) {
//            gui.figcanvas.hScrollBar.action(null,null);
        }
        else
        if(!iq.rString("md(")){
           if(!rNumPair()) return false;
           if(!iq.rString(")")) return false;
//           gui.figcanvas.hScrollBar.mouseDrag(null,x1,y1);
           return true;
        }
        else
        if(!iq.rString("mu(")){
           if(!rNumPair()) return false;
           if(!iq.rString(")")) return false;
//           gui.figcanvas.hScrollBar.mouseUp(null,x1,y1);
           return true;
       }
        return false;

    }
    public boolean parseScroll()
    {
        if(!iq.rString("scrl.")) return false;
        if(parseScrollH() || parseScrollV()) return true;
        return false;
    }
    public ParseEvent()
    {
    }
    public boolean parseMessage(DrawFrame f)
    {
        parseB();
        if(!iq.rString("message(")) return false;

        StringBuffer str=iq.rStrConst();
        if(str==null) return false;

        if(!iq.rString(")")) return false;

        return true;
    }
    public boolean parseSystemExit(DrawFrame f)
    {
        /*
        if(!iq.rString("exit()")) return false;
//        gui.close();
        */
        return true;
    }
    public boolean parseSystemSub(DrawFrame f)
    {
        /*
       if( parseSystemExit(f)  )
         return true;
         */
        return false;
     }
    public boolean parseSystem(DrawFrame f)
    {
        /*
        parseB();
        if(!iq.rString("system.")) return false;
        if(parseSystemSub(f)) return true;
        */
        return false;
    }
    public boolean parseKeyDown(FrameWithControlledFigCanvas f, int id)
    {
        if(!iq.rString("kdn(")) return false;
        Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(")")) return false;
//        Event ev=new Event(gui,(long)0,Event.KEY_PRESS,
//        x1,y1,0,0);
//        message.appendText("mdn("+x1+","+y1+")\n");
//        gui.postEvent(ev);
//        gui.canvas.keyDown(c.intValue());
        f.downKeyOnTheFigCanvas(id,c.intValue());
        return true;

    }
    public boolean parseMouseExit(FrameWithControlledFigCanvas f, int id)
    {
        int offX, offY;
        parseB();
        if(!iq.rString("mxit(")) return false;
        if(!rNumPair()) return false;
        // message.appendText("parsing polygon(");
        if(!iq.rString(")")) return false;
//        Event ev=new Event(gui,(long)0,Event.MOUSE_EXIT,
//        x1,y1,0,0);
//        message.appendText("mm("+x1+","+y1+")\n");
//        gui.postEvent(ev);
        f.exitMouseOnTheFigCanvas(id);
        return true;
    }
    public boolean parseMouseEnter(FrameWithControlledFigCanvas f, int id)
    {
        int offX, offY;
        parseB();
        if(!iq.rString("ment(")) return false;
        if(!rNumPair()) return false;
        // message.appendText("parsing polygon(");
        if(!iq.rString(")")) return false;
//        Event ev=new Event(gui,(long)0,Event.MOUSE_ENTER,
//        x1,y1,0,0);
//        message.appendText("mm("+x1+","+y1+")\n");
//        gui.postEvent(ev);
//        gui.canvas.mouseEntered(x1,y1);
        f.enterMouseOnTheFigCanvas(id,x1,y1);
        return true;
    }
    public boolean parseMouseUp(FrameWithControlledFigCanvas f, int id)
    {
        int offX, offY;
        parseB();
        if(!iq.rString("mup(")) return false;
        if(!rNumPair()) return false;
        // message.appendText("parsing polygon(");
        if(!iq.rString(")")) return false;
//        Event ev=new Event(gui,(long)0,Event.MOUSE_UP,
//        x1,y1,0,0);
//        message.appendText("mm("+x1+","+y1+")\n");
//        gui.postEvent(ev);
//        gui.canvas.mouseUp(x1,y1);
        f.upMouseOnTheFigCanvas(id,x1,y1);
        return true;
    }
    public boolean parseEColor()
    {
        if(!iq.rString("color(")) return false;
        Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(")")) return false;
//        fig.color=new Color(c.intValue());
		  gui.colorSelectButton.setBackground(new Color(c.intValue()));
		gui.editdispatch.changeColor();
        return true;

    }
    public boolean parseEdit()
    {
        if(!iq.rString("edit(")) return false;

        StringBuffer str=iq.rStrConst();
        if(str==null) return false;


        if(!iq.rString(")")) return false;
        String ecommand=str.toString();
        if(ecommand.equals("select"))  gui.editdispatch.select();
        else
        if(ecommand.equals("set"))     gui.editdispatch.set();
        else
        if(ecommand.equals("cut"))     gui.editdispatch.rmFig();
        else
        if(ecommand.equals("copy"))    gui.editdispatch.copy();
        else
        if(ecommand.equals("move"))    gui.editdispatch.moveFig();
        else
        if(ecommand.equals("rotate")) gui.editdispatch.changeMode("rotate");
        else
        if(ecommand.equals("modify"))  gui.editdispatch.modifyFig();
        else
        if(ecommand.equals("clear"))   gui.editdispatch.clear();
        else
        if(ecommand.equals("openFig")){
            if(!parseOpenFigFile()) return false;
        }

        return true;    
    }
    public boolean parseNewFig()
    {
        if(!iq.rString("newFig(")) return false;

        StringBuffer str=iq.rStrConst();
        if(str==null) return false;


        if(!iq.rString(")")) return false;
        gui.editdispatch.newFig(str.toString());

        return true;
    }
    public boolean parseButtonClicked(FrameWithControlledButton f)
    {
         parseB();
        if(!iq.rString("click(")) return false;

         Integer c=iq.rInteger();
        if(c==null) return false;

        if(!iq.rString(")")) return false;
        int x=c.intValue();
        f.clickButton(x);
        return true;
    }
    public boolean parseEvent()
    {   boolean rtn,btemp,btemp2,btemp3;
        return false;
    }
    public Thread me;
    public boolean errorRecovery()
    {
        boolean dmy;
        parseB();
        while(iq.rNot(")"))
        {
        }
        if(iq.rString(")")) return true;
        return false;
    }
    public boolean parseMouseDrag(FrameWithControlledFigCanvas f, int id)
    {
        int offX, offY;
        parseB();
        if(!iq.rString("mdg(")) return false;
        if(!rNumPair()) return false;
        // message.appendText("parsing polygon(");
        if(!iq.rString(")")) return false;
//        Event ev=new Event(gui,(long)0,Event.MOUSE_DRAG,
//        x1,y1,0,0);
//        message.appendText("mdg("+x1+","+y1+")\n");
//        gui.postEvent(ev);
//        gui.canvas.mouseDragged(x1,y1);
        f.dragMouseOnTheFigCanvas(id,x1,y1);
        
        return true;
    }
    public boolean parseMouseMove(FrameWithControlledFigCanvas f, int id)
    {
        int offX, offY;
        parseB();
        if(!iq.rString("mm(")) return false;
        if(!rNumPair()) return false;
        // message.appendText("parsing polygon(");
        if(!iq.rString(")")) return false;
//        Event ev=new Event(gui,(long)0,Event.MOUSE_MOVE,
//        x1,y1,0,0);
//        message.appendText("mm("+x1+","+y1+")\n");
//        gui.postEvent(ev);
        f.moveMouseOnTheFigCanvas(id,x1,y1);
//        gui.canvas.mouseMove(x1,y1);
        return true;
    }
    public boolean parseMouseDown(FrameWithControlledFigCanvas f, int id)
    {
        int offX, offY;
        parseB();
        if(!iq.rString("mdn(")) return false;
        if(!rNumPair()) return false;
        if(!iq.rString(")")) return false;
//        Event ev=new Event(gui,(long)0,Event.MOUSE_DOWN,
//        x1,y1,0,0);
//        message.appendText("mdn("+x1+","+y1+")\n");
//        gui.postEvent(ev);
//        gui.canvas.mouseDown(x1,y1);
        f.downMouseOnTheFigCanvas(id,x1,y1);
        return true;

    }
    public ParseEvent(DrawFrame f, InputQueue q) //Draw f
    {
        iq=q;
        gui=f;
        me=null;
//        start();
    }
     public void dmy()
    {
        int x=0;
    }
    public void parseB()
    {
        int x;
        while(iq.rString(" ")){
            x=0;
        };
    }
    public boolean parsePoints()
    {
       int offX, offY;
       if(!rNumPair()) return false;
        offX=x1; offY=y1;
//        fig.offX=x1; fig.offY=y1;

   //     message.appendText(""+offX+","+offY+"...\n");

        while(rNumPair()){
//            ((ALines)fig).addPoint(x1,y1);
//            message.appendText(" "+x1+","+y1+"\n");
        }
        parseB();
//        message.appendText(")\n");
        if(!iq.rString(")")) return false;
        return true;
  }
    public boolean parseDepth()
    {
        parseB();
        if(!iq.rString("depth(")) return false;
        Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(")")) return false;
//        fig.depth=c.intValue();

        return true;

    }
    public boolean parseColor()
    {   parseB();
        if(!iq.rString("dcolor(")) return false;
        Integer c=iq.rInteger();
        if(c==null) return false;
        if(!iq.rString(")")) return false;
//        fig.color=new Color(c.intValue());

        return true;

    }
    public boolean attribElement()
    {
         if(parseColor()) return true;
         if(parseDepth()) return true;
         return false;
    }
    public boolean parseAttrib()
    {
        parseB();
        if(!iq.rString("attrib(")) return false;

        // message.appendText("parsing attrib(");

        while(attribElement()) dmy();

        parseB();
        // message.appendText(")\n");
        if(!iq.rString(")")) return false;

        return true;
    }
    public javax.swing.JTextArea message;
    public boolean parseText()
    {

        parseB();
        if(!iq.rString("text(")) return false;
 //       Text fig=new Text(gui);
           if(!parseAttrib());
    if(!rNumPair()) return false;
        int offX=x1; int offY=y1;
//        fig.offX=x1; fig.offY=y1;

        if(!iq.rString(",")) return false;
        StringBuffer str=iq.rStrConst();
        if(str==null) return false;

//        fig.text=str;

        parseB();
        if(!iq.rString(")")) return false;

//        fig.font=gui.getFont();
//        fig.fmetrics=gui.getFontMetrics(fig.font);

//        afig=fig;
        return true;
    }
    public DrawFrame gui;  //Draw
    public boolean rNumPair()
    {
        parseB();
        Integer x=iq.rInteger();
        if(x==null) return false;
        x1=x.intValue();
        parseB();
        if(!iq.rString(",")) return false;
        parseB();
        Integer y=iq.rInteger();
        if(y==null) return false;
        y1=y.intValue();
        return true;
    }
    public int y1;
    public int  x1;
    public InputQueue iq;
    public void skipRest(){
    	if(this.iq==null) return;
    	this.iq.skipRest();
    }
   
}

