package application.draw;

import java.util.Vector;

public class HtmlElements extends HtmlElement{
	Vector elements;
	public HtmlElements(){
		elements=new Vector();
	}
    public void add(HtmlElement ele){
    	elements.add(ele);
    }
    public void remove(HtmlElement ele){
    	elements.remove(ele);
    }
    public void refresh(){
    	
    }
}
