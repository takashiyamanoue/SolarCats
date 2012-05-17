package ft.pneumatic;
import ft.Controller;
import ft.Interface;
/** control program for the sorter model in pneumatic robots.<p>
    M1 uses two diodes(!) to control two pneumatic valves which move
	the slider left and right.<br>
    M2 powers the compressor and the lamp for the photo cell.<br>
    E1 reads the photo cell.<br>
    M3 controls the pneumatic valve which centers the slider.<br>
    M4 controls the motor which transports the black and white wheels;
	it's direction is critical: to avoid jamming it must press against
	the tile next to the piston.<br>
    E2 senses if the piston is completely withdrawn.<p>
    If the diodes are not available, M1 and M2 are each connected to a valve
	and the compressor and lamp are left on permanently, see ft.properties.
  */
public class Sorter extends Controller {
  public Sorter (Interface iface) { super(iface); }
  /** process about 5 wheels.
    */
  public void run () {
    if (diodes) left(M2);		// start compressor and lamp
    if (isOff(E2)) {		// retract piston: wait 1 edge
      left(M4); e2.count(1); off(M4);
    }
    sleep(recover);		// additional pressure recovery period
    moveSlider(OFF);		// center slider

    for (int wheel = 0; wheel < 7; ++ wheel) {
      boolean white = isOn(E1);
      cyclePiston();
      moveSlider(white ? LEFT : RIGHT); moveSlider(OFF);
    }
    off(Mall);
  }
  /** run piston back and forth once -- E2 must go off and back on.
    */
  protected void cyclePiston () { right(M4); e2.count(2); off(M4); }
  /** move slider.
    */
  protected void moveSlider (int how) {
    sleep(recover);
    switch (how) {
    case LEFT:	left(M1); break;
    case RIGHT:	if (diodes) right(M1); else right(M2); break;
    case OFF:	left(M3);
    }
    sleep(press);
    off(M1|M3); if (!diodes) off(M2);
  }
  /** sleep in tenths of seconds.
    */
  protected void sleep (int tsec) {
    try { Thread.sleep(tsec*100); } catch (InterruptedException e) { }
  }
  /** true if wired with diodes */	protected boolean diodes;
  /** pressure time for slider */	protected int press;
  /** recovery time for compressor */	protected int recover;
  
  static { Interface.class.getName(); }	// load properties
  { String prefix = getClass().getName();
    press = Integer.getInteger(prefix+".press", 10).intValue();
    recover = Integer.getInteger(prefix+".recover", 20).intValue();
    diodes = Boolean.getBoolean(prefix+".diodes");
  }
}
