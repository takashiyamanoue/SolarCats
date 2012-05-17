package application.draw;
import nodesystem.StateContainer;
import controlledparts.ControlledButton;
public class DrawEditState implements StateContainer 
{
    public int currentState;

    public DrawFrame draw;

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
        ControlledButton t=(ControlledButton)(draw.editButtons.elementAt(i));
        this.draw.editFunField.setText(t.getText());
        this.currentState=i;
        if(i==4){ draw.editdispatch.set(); return; }                   // eSetButton
        if(i==5){ /* draw.editdispatch.clear(); */ return; }                  // eClearButton
	    if(i==6){ draw.editdispatch.changeMode("rotate"); return; }    // eRotateButton
	    if(i==7){ draw.editdispatch.copy(); return; }                  // eCopyButton
	    if(i==8){ draw.editdispatch.modifyFig(); return; }             // eModifyButton
	    if(i==9){ draw.editdispatch.moveFig();  return; }              // eMoveButton
	    if(i==10){ draw.editdispatch.rmFig(); return; }                // eCutButton
	    if(i==11){ draw.editdispatch.select();return; }                // eSelectButton
        if(i==this.draw.eSelectAllButton.getID()){
		        draw.editdispatch.selectAll();
		        return;
        }

    }

	public DrawEditState()
	{

	}

	public DrawEditState(DrawFrame f)
	{
	    draw=f;
	}


}