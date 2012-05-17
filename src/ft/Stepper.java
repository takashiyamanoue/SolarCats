package ft;
/** influences and displays the state of a motor.
    Digital inputs define home and track current position.
  */
public class Stepper extends StepMotor {
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
  public Stepper (Interface iface,
	int motor, String motorText,
	int home, String homeText, int toHome,
	int step, String stepText, int maxStep) {
    super(iface, motor, motorText, home, homeText, toHome,
						step, stepText, maxStep);
    this.iface = iface; this.motor = motor; this.home = home; this.step = step;
    iface.addObserver(home, new Interface.DigitalObserver() {
      public void edge (Interface iface, int input, boolean val, int count) {
        if (running && goal == -1 && val)
	  synchronized (Stepper.this) {
	    running = false; Stepper.this.notifyAll();
	  }
      }
    });
    iface.addObserver(step, new Interface.DigitalObserver() {
      public void edge (Interface iface, int input, boolean val, int count) {
        if (running && goal > 0 && count >= goal-2 && count <= goal+2) {
	  iface.set(Stepper.this.motor, OFF);
	  synchronized (Stepper.this) {
	    running = false; Stepper.this.notifyAll();
	  }
	}
      }
    });
  }
  protected final Interface iface;
  protected final int motor, home, step;
  protected boolean running;
  protected int goal;
  /** home the motor.
      this is notified once it is no longer running.
    */
  public synchronized void home () {
    if (running) throw new IllegalArgumentException("running");
    if (!iface.isSet(home)) {
      goal = -1; running = true; iface.set(motor, toHome);
    }
  }
  /** position the motor.
      this is notified once it is no longer running.
    */
  public synchronized void position (int goal) {
    if (running || goal <= 0) throw new IllegalArgumentException("running");
    int pos = iface.isAt(step);
    if (pos < goal-1) {
      this.goal = goal; running = true;
      iface.set(motor, toHome == LEFT ? RIGHT : LEFT); 
    } else if (pos > goal+1) {
      this.goal = goal; running = true;
      iface.set(motor, toHome == LEFT ? LEFT : RIGHT);
    }
  }
  /** motor's state.
    */
  public synchronized boolean isRunning () { return running; }

// ------------------------------------------------------------------ properties

}