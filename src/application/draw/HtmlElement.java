package application.draw;

import java.util.Vector;

public class HtmlElement {
    int x,y;
    int width,height;
    FigCanvas canvas;
    AFigure fig;
    HtmlEnvironment env;
    String html;
    public HtmlElement(){
    	
    }
    public HtmlElement(HtmlEnvironment e,AFigure f,String h){
    	this.env=e; this.fig=f; this.html=h;
    }
    public int getX(){
    	return this.x;
    }
    public int getY(){
    	return this.y;
    }
    public int getWidth(){
    	return this.width;
    }
    public int getHeight(){
    	return this.height;
    }
    public void setX(int x){
    	
    }
    public void setY(int y){
    	
    }
}
