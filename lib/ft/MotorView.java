package ft;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
/** displays the state of a motor.
    Could observe more than one motor but will effectively only display
    the highest one.
  */
public class MotorView extends Panel implements Interface.MotorObserver {
  /** initialize to off.
      @param iface to observe.
      @param mask M1.. for motor to be observed.
      @param text to be displayed in component; null to suppress.
    */
  public MotorView (Interface iface, int mask, String text) {
    super(new BorderLayout()); setBackground(edge);
    add(left = new Label("<", Label.CENTER), BorderLayout.WEST);
      left.setBackground(off); left.setForeground(offText);
    if (text != null) {
      Label name;
      add(name = new Label(text, Label.CENTER), BorderLayout.CENTER);
        name.setBackground(bg); name.setForeground(fg);
    }
    add(right = new Label(">", Label.CENTER), BorderLayout.EAST);
      right.setBackground(off); right.setForeground(offText);
    iface.addObserver(mask, this);
  }
  protected final Label left, right;
  /** make room for edge.
    */
  public Insets getInsets () {
    return new Insets(edgeWidth, edgeWidth, edgeWidth, edgeWidth);
  }
  /** changes color in response to the Interface.
    */
  public void edge (Interface iface, int motor, int val) {
    switch (val) {
    case LEFT:
      left.setBackground(on); left.setForeground(onText);
      right.setBackground(off); right.setForeground(offText);
      break;
    case RIGHT:
      left.setBackground(off); left.setForeground(offText);
      right.setBackground(on); right.setForeground(onText);
      break;
    default:
      left.setBackground(off); left.setForeground(offText);
      right.setBackground(off); right.setForeground(offText);
    }
  }

// ------------------------------------------------------------------ properties

  /** panel edge width */	private int edgeWidth;
  public int getEdgeWidth () { return edgeWidth; }
  public void setEdgeWidth (int edgeWidth) { this.edgeWidth = edgeWidth; }

  /** panel edge */		private Color edge;
  public Color getEdge () { return edge; }
  public void setEdge (Color edge) { this.edge = edge; }  

  /** label background */	private Color bg;
  public Color getBg () { return bg; }
  public void setBg (Color bg) { this.bg = bg; }

  /** label foreground */	private Color fg;
  public Color getFg () { return fg; }
  public void setFg (Color fg) { this.fg = fg; }

  /** motor off */		private Color off;
  public Color getOff () { return off; }
  public void setOff (Color off) { this.off = off; }

  /** motor off text */		private Color offText;
  public Color getOffText () { return offText; }
  public void setOffText (Color offText) { this.offText = offText; }

  /** motor on */		private Color on;
  public Color getOn () { return on; }
  public void setOn (Color on) { this.on = on; }

  /** motor on text */		private Color onText;
  public Color getOnText () { return onText; }
  public void setOnText (Color onText) { this.onText = onText; }

  static { Interface.class.getName(); }	// load properties
  { String prefix = getClass().getName();
    edgeWidth = Integer.getInteger(prefix+".edge.width", 1).intValue();
    edge = Color.getColor(prefix+".edge", Color.black);
    bg = Color.getColor(prefix+".bg", Color.gray);
    fg = Color.getColor(prefix+".fg", Color.black);
    off = Color.getColor(prefix+".off", Color.gray);
    offText = Color.getColor(prefix+".off.text", Color.gray);
    on = Color.getColor(prefix+".on", Color.green);
    onText = Color.getColor(prefix+".on.text", Color.white);
  }
}