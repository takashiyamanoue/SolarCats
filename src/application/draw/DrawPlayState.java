package application.draw;
import nodesystem.StateContainer;
public class DrawPlayState implements StateContainer
{
    public int currentState;

    public DrawFrame draw;

    public DrawPlayState(DrawFrame f)
    {
        draw=f;
    }

    public int getState()
    {
        // This method is derived from interface StateContainer
        // to do: code goes here
        return this.currentState;
    }

    public void setState(int i)
    {
        // This method is derived from interface StateContainer
        // to do: code goes here
        this.currentState=i;
		    if(i==draw.startPlayButton.getID()){
		        draw.canvas.setPlayMode(true);
		        draw.startPlayButton.setText("playing");
		        draw.editButton.setText("edit");
		        draw.canvas.repaint();
		        return;
		    }
		    if(i==draw.editButton.getID()){
		        draw.canvas.setPlayMode(false);
		        draw.editButton.setText("editting");
		        draw.startPlayButton.setText("play");
//		        draw.editdispatch.changeMode("Select");
		        draw.canvas.repaint();
                return;
		    }
    }

}