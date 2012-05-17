package ft;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/** diagnostic panel for fischertechnik interface.
  */
public class Diagnose extends Panel {
  /** create diagnostic panel for an Interface.
    */
  public Diagnose (Interface iface) {
    super(new GridLayout(0, 1)); setBackground(edge);
	
    Panel panel = new Panel(new GridLayout(1, 0));
    for (byte n = 1, bit = 1; n <= 4; ++ n, bit <<= 1)
      panel.add(new EdgeButton(iface, bit, "E"+n));
    add(panel);

    panel = new Panel(new GridLayout(1, 0));
    for (byte n = 5, bit = 1 << 4; n <= 8; ++ n, bit <<= 1)
      panel.add(new EdgeButton(iface, bit, "E"+n));
    add(panel);

    panel = new Panel(new GridLayout(1, 0));
    for (byte n = 1, bit = 3; n <= 4; ++ n, bit <<= 2)
      panel.add(new MotorButton(iface, bit, "M"+n));
    add(panel);

    if (analog) {
      panel = new Panel(new GridLayout(1, 0));
      for (byte n = 0, bit = 1; n < 2; ++ n, bit <<= 1) {
        panel.add(new ScaledView(iface, bit, "E"+(char)(n+'X')+": "));
      }
      add(panel);
    }
  }
  /** make room for edge.
    */
  public Insets getInsets () {
    return new Insets(edgeWidth, edgeWidth, edgeWidth, edgeWidth);
  }
  
// ------------------------------------------------------------------ properties

  /** panel edge width */	private int edgeWidth;
  public int getEdgeWidth () { return edgeWidth; }
  public void setEdgeWidth (int edgeWidth) { this.edgeWidth = edgeWidth; }

  /** panel edge */		private Color edge;
  public Color getEdge () { return edge; }
  public void setEdge (Color edge) { this.edge = edge; }  

  /** show analog inputs */	protected boolean analog;
  
  static { Interface.class.getName(); }	// load properties
  { String prefix = getClass().getName();
    edgeWidth = Integer.getInteger(prefix+".edge.width", 2).intValue();
    edge = Color.getColor(prefix+".edge", Color.blue);
    analog = Boolean.getBoolean(prefix+".analog");
  }

// -------------------------------------------------------- diagnose application

  /** <tt>ft.Diagnose commPortId ...</tt>
    */
  public static void main (String[] args) {
    if (args != null && args.length > 0)
      for (int a = 0; a < args.length; ++ a)
        try {
          Interface iface = Interface.open(args[a]);

          Frame frame = new Frame("Diagnose ["+args[a]+"]");
          frame.add(new Diagnose(iface), BorderLayout.CENTER);
          frame.addWindowListener(new WindowAdapter() {
            public void windowClosing (WindowEvent e) { System.exit(0); }
          });
          frame.pack(); frame.show();

          new Thread(iface).start();
        } catch (Exception e) { e.printStackTrace(); System.exit(1); }
    else System.err.println("usage: java ft.Diagose commPortId ...");
  }
}