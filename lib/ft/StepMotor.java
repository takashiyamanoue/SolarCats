package ft;
import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.Panel;
/** influences and displays the state of a motor.
    Digital inputs define home and track current position.
  */
public class StepMotor extends MotorButton {
  /** initialize to off, arm labels for mouse events.
      @param iface to observe and set motor state.
      @param motor M1.. for motor to be observed.
      @param motorText to be displayed in component; null to suppress.
      @param home E1.. for input defining home position.
      @param homeText to be displayed in component; null to suppress.
      @param toHome LEFT/RIGHT to move to home position.
      @param step E1.. for input counting current position.
      @param stepText to be displayed in component; null to suppress.
      @param maxStep maximum count, 0 unlimited.
    */
  public StepMotor (final Interface iface,
	final int motor, String motorText,
	int home, String homeText, int toHome,
	int step, String stepText, final int maxStep) {
    super(iface, motor, motorText);
    this.step = step; this.toHome = toHome;

    Panel p = new Panel(new BorderLayout());
    add(p, BorderLayout.SOUTH);
    
    { TouchView tv =
	new TouchView(iface, home, homeText == null ? " " : homeText);
      final Label label = toHome == LEFT ? left : right;
      iface.addObserver(home, new Interface.DigitalObserver() {
        public void edge (Interface iface, int input, boolean val, int count) {
          if (val) {
	    label.setEnabled(false);
	    iface.set(motor, OFF); iface.set(StepMotor.this.step, 0, 1);
	  } else label.setEnabled(true);
        }
      });
      p.add(tv, toHome == LEFT ? BorderLayout.WEST : BorderLayout.EAST);
    }

    { EdgeView ev =
	new EdgeView(iface, step, stepText == null ? " " : stepText);
      if (maxStep > 0) {
        final Label label = toHome == LEFT ? right : left;
        iface.addObserver(step, new Interface.DigitalObserver() {
          public void edge (Interface iface, int input, boolean val,
								int count) {
            if (count >= maxStep) {
              if (label.isEnabled()) iface.set(motor, OFF);
              label.setEnabled(false);
            } else label.setEnabled(true);
          }
        });
      }
      p.add(ev, BorderLayout.CENTER);
    }
  }
  protected int toHome, step;
  /** changes step delta in response to the Interface.
    */
  public void edge (Interface iface, int motor, int val) {
    if (val != OFF) iface.setDelta(step, val == toHome ? -1 : 1);
    super.edge(iface, motor, val);
  }

// ------------------------------------------------------------------ properties

}