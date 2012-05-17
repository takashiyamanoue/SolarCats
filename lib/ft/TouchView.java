package ft;
import java.awt.Color;
import java.awt.Label;
/** displays the state of a digital input with a background color.
  */
public class TouchView extends Label implements Interface.DigitalObserver {
  /** initialize to open.
      @param iface to observe.
      @param mask E1.. for sensor to be observed.
      @param text to be displayed in component.
    */
  public TouchView (Interface iface, int mask, String text) {
    super(text, CENTER);
    setBackground(open); setForeground(openText);
    iface.addObserver(mask, this);
  }
  /** changes color in response to the Interface.
    */
  public void edge (Interface iface, int input, boolean val, int count) {
    setBackground(val ? closed : open);
    setForeground(val ? closedText : openText);
  }

// ------------------------------------------------------------------ properties

  /** sensor open */	private Color open;
  public Color getOpen () { return open; }
  public void setOpen (Color open) { this.open = open; }
  
  /** sensor text */	private Color openText;
  public Color getOpenText () { return openText; }
  public void setOpenText (Color openText) { this.openText = openText; }
  
  /** sensor closed */	private Color closed;
  public Color getClosed () { return closed; }
  public void setClosed (Color closed) { this.closed = closed; }
  
  /** sensor text */	private Color closedText;
  public Color getClosedText () { return closedText; }
  public void setClosedText (Color closedText) { this.closedText = closedText; }
  
  static { Interface.class.getName(); }	// load properties
  { String prefix = getClass().getName();
    open = Color.getColor(prefix+".open", Color.black);
    openText = Color.getColor(prefix+".open.text", Color.white);
    closed = Color.getColor(prefix+".closed", Color.green);
    closedText = Color.getColor(prefix+".closed.text", openText);
  }
}