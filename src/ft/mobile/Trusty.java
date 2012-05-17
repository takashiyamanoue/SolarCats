// Trusty.java
package ft.mobile;
import ft.Controller;
import ft.Interface;
/** simple control program for a moving robot that can back off.
  */
public class Trusty extends Controller {
  public Trusty (Interface iface) { super(iface); }
  /** m1, m2 run forward.
      if e3 (or e4) is open, back off, turn away, and go forward again.
    */
  public void run () {
    m1.left(); m2.left();
    for (int count = 0; count < 10; )
      if (!e3.isOn()) { turn(m1, m2, e1); ++ count; }
      else if (!e4.isOn()) { turn(m2, m1, e2); ++ count; }
    m1.off(); m2.off();
  }
  /** use impulse counter to back off, turn away from motor a.
    */
  protected void turn (Output a, Output b, Input t) {
    a.right(); b.right(); t.count(8);
    a.left(); t.count(2);
    b.left();
  }
}
