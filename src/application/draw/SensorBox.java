package application.draw;
import java.awt.Graphics;

public class SensorBox extends ARectangle
{
    public AFigure copyThis(){
        SensorBox rtn=(SensorBox)(super.copyThis());
        return (AFigure)rtn;
    }
	
	public String getUserDirPath(){
    	if(canvas==null) return "";
    	if(canvas.gui==null) return "";
    	if(canvas.gui.communicationNode==null) return "";
    	return this.canvas.gui.communicationNode.getUserDirPath();
    }
    
    public String getCommonDirPath(){
    	if(canvas==null) return "";
    	if(canvas.gui==null) return "";
    	if(canvas.gui.communicationNode==null) return "";
        return this.canvas.gui.communicationNode.getCommonDirPath();
    }
    public void actWhenEditing()
    {
    }

    public void mouseMove(int x, int y)
    {
            if(this.isPlaying()){
             }
            else{
                super.mouseMove(x,y);
            }
        
    }

    public void actWhenPlaying()
    {
    }

    public void mouseDown(int x, int y)
    {
        if(this.isPlaying()){
            if(this.isSelected(x,y)){
                actWhenPlaying();
            }
        }
        else{
            if(this.isSelected(x,y)){
                actWhenEditing();
            }
            super.mouseDown(x,y);
        }
        
    }

    public void draw(Graphics g)
    {
        if(this.isPlaying()){
        }
        else{
            super.draw(g);
        }
    }

    public boolean isSelected(int x, int y)
    {
        if(isIntheRectangle(x,y,offX,offY,offX+x2,offY+y2))
        return true;
        else return false;
    }

}