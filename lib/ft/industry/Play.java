package ft.industry;
import ft.Constants;
import ft.Interface;
import ft.Stepper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
/** very rudimentary play panel for ft.Record.
  */
public class Play extends Panel implements Constants {
  /** create play panel for an Interface.
    */
  public Play (Interface iface) {
    super(new GridLayout(1, 0)); setBackground(edge);

    for (int n = 1, bit = M1; n <= 4; ++ n, bit <<= Mshift)
      add(stepper[n-1] = new Stepper(iface, bit, "M"+n,
		bit & 0x55, "E"+(2*n-1), iface.LEFT,
		bit & 0xaa, "E"+2*n, maxStep));
  }
  protected Stepper stepper [] = new Stepper[4];
  /** wait until all steppers are not running.
    */
  public void await () {
    try {
      for (int n = 0; n < stepper.length; ++ n)
        synchronized (stepper[n]) {
          if (stepper[n].isRunning()) { stepper[n].wait(); continue; }
        }
    } catch (InterruptedException e) { }
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
    maxStep = Integer.getInteger(prefix+".max.step", 0).intValue();
  }

// ---------------------------------------------------------- record application

  /** <tt>ft.Play commPortId</tt>
    */
  public static void main (String[] args) {
    if (args != null && args.length > 0)
      try {
        Interface iface = Interface.open(args[0]);

        Frame frame = new Frame("Play ["+args[0]+"]");
	Play play;
        frame.add(play = new Play(iface), BorderLayout.CENTER);
        frame.addWindowListener(new WindowAdapter() {
          public void windowClosing (WindowEvent e) { System.exit(0); }
        });
        frame.pack(); frame.show();

        new Thread(iface).start();
	
	for (int n = 0; n < play.stepper.length; ++ n) play.stepper[n].home();
	play.await();
	
	String line;
	BufferedReader in =
		new BufferedReader(new InputStreamReader(System.in));
	while ((line = in.readLine()) != null) {
	  StringTokenizer st = new StringTokenizer(line);
	  for (int n = 0; n < play.stepper.length; ++ n)
	    play.stepper[n].position(Integer.parseInt(st.nextToken()));
	  play.await();
	}
	System.exit(0);
      } catch (Exception e) { System.err.println(e); System.exit(1); }
    else System.err.println("usage: java ft.Play commPortId");
  }
}