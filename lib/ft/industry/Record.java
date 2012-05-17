package ft.industry;
import ft.Constants;
import ft.Interface;
import ft.StepMotor;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/** very rudimentary teach-in panel for fischertechnik interface.
  */
public class Record extends Panel implements Constants {
  /** create recording panel for an Interface.
    */
  public Record (Interface iface) {
    super(new BorderLayout()); setBackground(edge);
	
    Panel panel = new Panel(new GridLayout(1, 0));
    final int pos[] = new int[4];
    for (int n = 1, bit = M1; n <= 4; ++ n, bit <<= Mshift) {
      panel.add(new StepMotor(iface, bit, "M"+n,
		bit & 0x55, "E"+(2*n-1), iface.LEFT,
		bit & 0xaa, "E"+2*n, maxStep));
      final int index = n-1;
      iface.addObserver(bit & 0xaa, new Interface.DigitalObserver() {
	public void edge (Interface iface, int input, boolean val, int count) {
	  pos[index] = count;
	}
      });
    }
    add(panel, BorderLayout.CENTER);

    panel = new Panel();
    Button button;
    panel.add(button = new Button("record"));
    button.addActionListener(new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        System.out.println(pos[0]+"\t"+pos[1]+"\t"+pos[2]+"\t"+pos[3]);
      }
    });
    add(panel, BorderLayout.SOUTH);
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

  /** step limit */		private int maxStep;
  public int getMaxStep () { return maxStep; }
  public void setMaxStep (int maxStep) { this.maxStep = maxStep; }  

  static { Interface.class.getName(); }	// load properties
  { String prefix = getClass().getName();
    edgeWidth = Integer.getInteger(prefix+".edge.width", 2).intValue();
    edge = Color.getColor(prefix+".edge", Color.gray);
    maxStep = Integer.getInteger(prefix+".max.step", 255).intValue();
  }

// ---------------------------------------------------------- record application

  /** <tt>ft.Record commPortId</tt>
    */
  public static void main (String[] args) {
    if (args != null && args.length > 0)
      try {
        Interface iface = Interface.open(args[0]);

        Frame frame = new Frame("Record ["+args[0]+"]");
        frame.add(new Record(iface), BorderLayout.CENTER);
        frame.addWindowListener(new WindowAdapter() {
          public void windowClosing (WindowEvent e) {
	    System.out.flush(); System.exit(0);
	  }
        });
        frame.pack(); frame.show();

        iface.run();
      } catch (Exception e) { System.err.println(e); System.exit(1); }
    else System.err.println("usage: java ft.Record commPortId");
  }
}