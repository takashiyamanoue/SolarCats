package ft;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
/** displays the value of an analog input.
  */
public class AnalogView extends Panel implements Interface.AnalogObserver {
  /** initialize to zero.
      @param iface to observe.
      @param mask EX.. for sensor to be observed.
      @param text to be displayed in component; null to suppress.
    */
  public AnalogView (Interface iface, int mask, String text) {
    super(new BorderLayout()); setBackground(edge);
    if (text != null) {
      Label name;
      add(name = new Label(text, Label.RIGHT), BorderLayout.WEST);
        name.setBackground(labelBg); name.setForeground(labelFg);
    }
    add(value = new Label(" 0 ", Label.CENTER), BorderLayout.CENTER);
      value.setBackground(bg); value.setForeground(fg);
    iface.addObserver(mask, this);
  }
  protected final Label value;
  /** make room for edge.
    */
  public Insets getInsets () {
    return new Insets(edgeWidth, edgeWidth, edgeWidth, edgeWidth);
  }
  /** changes text in response to the Interface.
    */
  public void edge (Interface iface, int input, int value) {
    this.value.setText(value+"");
  }

// ------------------------------------------------------------------ properties

  /** panel edge width */	private int edgeWidth;
  public int getEdgeWidth () { return edgeWidth; }
  public void setEdgeWidth (int edgeWidth) { this.edgeWidth = edgeWidth; }

  /** panel edge */		private Color edge;
  public Color getEdge () { return edge; }
  public void setEdge (Color edge) { this.edge = edge; }  

  /** value background */	private Color bg;
  public Color getBg () { return bg; }
  public void setBg (Color bg) { this.bg = bg; }

  /** value foreground */	private Color fg;
  public Color getFg () { return fg; }
  public void setFg (Color fg) { this.fg = fg; }

  /** label background */	private Color labelBg;
  public Color getLabelBg () { return labelBg; }
  public void setLabelBg (Color labelBg) { this.labelBg = labelBg; }

  /** label foreground */	private Color labelFg;
  public Color getLabelFg () { return labelFg; }
  public void setLabelFg (Color labelFg) { this.labelFg = labelFg; }

  static { Interface.class.getName(); }	// load properties
  { String prefix = getClass().getName();
    edgeWidth = Integer.getInteger(prefix+".edge.width", 1).intValue();
    edge = Color.getColor(prefix+".edge", Color.black);
    bg = Color.getColor(prefix+".bg", Color.gray);
    fg = Color.getColor(prefix+".fg", Color.black);
    labelBg = Color.getColor(prefix+".label.bg", Color.gray);
    labelFg = Color.getColor(prefix+".label.fg", Color.black);
  }
}