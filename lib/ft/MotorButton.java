package ft;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
/** influences and displays the state of a motor.
    Digital inputs define home and track current position.
  */
public class MotorButton extends MotorView {
  /** initialize to off, arm labels for mouse events.
      @param iface to observe and set motor state.
      @param mask M1.. for motor to be observed.
      @param text to be displayed in component; null to suppress.
    */
  public MotorButton (final Interface iface, final int mask, String text) {
    super(iface, mask, text);
    class Mousy extends MouseAdapter {
      protected int how;
      public Mousy (int how) { this.how = how; }
      public void mouseEntered (MouseEvent e) {
        setBackground(active);
      }
      public void mouseExited (MouseEvent e) {
        setBackground(getEdge());
      }
      public void mousePressed (MouseEvent e) {
        iface.set(mask, how);
      }
      public void mouseReleased (MouseEvent e) {
        iface.set(mask, iface.OFF);
      }
    };
    left.addMouseListener(new Mousy(iface.LEFT));
    right.addMouseListener(new Mousy(iface.RIGHT));
  }
  
// ------------------------------------------------------------------ properties

  /** panel active */		private Color active;
  public Color getActive () { return active; }
  public void setActive (Color active) { this.active = active; }

  static { Interface.class.getName(); }	// load properties
  { String prefix = getClass().getName();
    active = Color.getColor(prefix+".active", Color.red);
  }
}