package ft;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.TextField;
/** displays the edge count of a digital input.
  */
public class EdgeView extends Panel implements Interface.DigitalObserver {
  /** initialize to zero, counting up.
      @param iface to observe.
      @param mask E1.. for sensor to be observed.
      @param text to be displayed in component; null to suppress.
    */
  public EdgeView (Interface iface, int mask, String text) {
    super(new BorderLayout()); setBackground(edge);
    if (text != null)
      add(new TouchView(iface, mask, text), BorderLayout.WEST);
    add(count = new TextField("   0", 4), BorderLayout.CENTER);
      count.setEditable(false);
      count.setBackground(bg); count.setForeground(fg);
    iface.addObserver(mask, this); iface.set(mask, 0, 1);
  }
  protected final TextField count;
  /** make room for edge.
    */
  public Insets getInsets () {
    return new Insets(edgeWidth, edgeWidth, edgeWidth, edgeWidth);
  }
  /** changes text in response to the Interface.
    */
  public void edge (Interface iface, int input, boolean val, int count) {
    String text = count+"";
    while (text.length() < 4) text = " "+text;
    this.count.setText(text);
  }

// ------------------------------------------------------------------ properties

  /** panel edge width */	private int edgeWidth;
  public int getEdgeWidth () { return edgeWidth; }
  public void setEdgeWidth (int edgeWidth) { this.edgeWidth = edgeWidth; }

  /** panel edge */		private Color edge;
  public Color getEdge () { return edge; }
  public void setEdge (Color edge) { this.edge = edge; }  

  /** count background */	private Color bg;
  public Color getBg () { return bg; }
  public void setBg (Color bg) { this.bg = bg; }

  /** count foreground */	private Color fg;
  public Color getFg () { return fg; }
  public void setFg (Color fg) { this.fg = fg; }

  static { Interface.class.getName(); }	// load properties
  { String prefix = getClass().getName();
    edgeWidth = Integer.getInteger(prefix+".edge.width", 1).intValue();
    edge = Color.getColor(prefix+".edge", Color.black);
    bg = Color.getColor(prefix+".bg", Color.gray);
    fg = Color.getColor(prefix+".fg", Color.black);
  }
}