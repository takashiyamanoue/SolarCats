package ft;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/** abstract base class for control programs.
  */
public abstract class Controller implements Constants, Runnable {
  /** creates e[1..8] alias e1..e8 to sense digital inputs
      and m[1..4] alias m1..m4 to run outputs.
    */ 
  public Controller (Interface iface) {
    this.iface = iface;
    iface.addObserver(E1, e1);
    iface.addObserver(E2, e2);
    iface.addObserver(E3, e3);
    iface.addObserver(E4, e4);
    iface.addObserver(E5, e5);
    iface.addObserver(E6, e6);
    iface.addObserver(E7, e7);
    iface.addObserver(E8, e8);
  }
  protected Interface iface;
  
  /** represent an output as an object.
    */
  protected class Output {
    /** @param mask M1..
      */
    Output (int mask) { this.mask = mask; }
    protected final int mask;
    /** manipulate output.
      */
    public void set (int how) { iface.set(mask, how); }
    public void left () { set(LEFT); }
    public void right () { set(RIGHT); }
    public void off () { set(OFF); }
  }
  protected final Output m[] = new Output[5];
  protected final Output m1 = m[1] = new Output(M1);
  protected final Output m2 = m[2] = new Output(M2);
  protected final Output m3 = m[3] = new Output(M3);
  protected final Output m4 = m[4] = new Output(M4);
  /** manipulate outputs.
      @param mask M1.. or-ed together.
    */
  protected void left (int mask) { iface.set(mask, LEFT); }
  protected void right (int mask) { iface.set(mask, RIGHT); }
  protected void off (int mask) { iface.set(mask, OFF); }
  /** represent a digital input as an object.
    */
  protected class Input implements Interface.DigitalObserver {
    /** @param mask E1...
      */
    Input (int mask) { this.mask = mask; }
    protected final int mask;
    
    public synchronized void edge (Interface iface, int input,
						boolean val, int count) {
      this.val = val; this.count = count; if (delta != 0) notify();
    }
    protected boolean val; protected int delta, count;
    /** @return true if input is set.
      */
    public boolean isOn () { return val; }
    /** @return true if input is not set.
      */
    public boolean isOff () { return !val; }
    /** @param n edges to wait for.
      */
    public synchronized void count (int n) {
      if (n <= 0) throw new IllegalArgumentException("bad count");
      iface.set(mask, count = 0, delta = 1);
      try {
        while (count < n) wait();
      } catch (InterruptedException e) { }
      iface.setDelta(mask, delta = 0);
    }
  }
  protected final Input e[] = new Input[9];
  protected final Input e1 = e[1] = new Input(E1);
  protected final Input e2 = e[2] = new Input(E2);
  protected final Input e3 = e[3] = new Input(E3);
  protected final Input e4 = e[4] = new Input(E4);
  protected final Input e5 = e[5] = new Input(E5);
  protected final Input e6 = e[6] = new Input(E6);
  protected final Input e7 = e[7] = new Input(E7);
  protected final Input e8 = e[8] = new Input(E8);
  /** synchronized on all selected inputs.
      @param mask E1.. or-ed together.
      @return true if all inputs are set. 
    */
  public boolean isOn (int mask) {
    if (mask != 0)
      for (int n = 1, bit = E1; n < e.length; ++ n, bit <<= 1)
        if ((bit & mask) != 0) return isOn(mask, n, bit);
    throw new IllegalArgumentException("null mask");
  }
  private boolean isOn (int mask, int n, int bit) {
    synchronized (e[n]) {
      if (e[n].isOff()) return false;
      for (++ n, bit <<= 1; n < e.length; ++ n, bit <<= 1)
        if ((bit & mask) != 0) return isOn(mask, n, bit);
      return true;
    }
  }
  /** synchronized on all selected inputs.
      @param mask E1.. or-ed together.
      @return true if all inputs are not set. 
    */
  public boolean isOff (int mask) {
    if (mask != 0)
      for (int n = 1, bit = 1; n < e.length; ++ n, bit <<= 1)
        if ((bit & mask) != 0) return isOff(mask, n, bit);
    throw new IllegalArgumentException("null mask");
  }
  private boolean isOff (int mask, int n, int bit) {
    synchronized (e[n]) {
      if (e[n].isOn()) return false;
      for (++ n, bit <<= 1; n < e.length; ++ n, bit <<= 1)
        if ((bit&mask) != 0) return isOff(mask, n, bit);
      return true;
    }
  }

// ------------------------------------------------------ controller application

  /** <tt>ft.Controller commPortId class</tt>
    */
  public static void main (String[] args) {
    if (args != null && args.length > 1)
      try {
        Interface iface = Interface.open(args[0]);
	Thread thread = new Thread(iface); thread.setDaemon(true);
	Class clazz = Class.forName(args[1]);
	final Controller ctrl = (Controller)
		clazz.getConstructor(new Class[] {Interface.class})
			.newInstance(new Object[] {iface});
	
	Frame frame = new Frame(args[1]+" ["+args[0]+"]");
	System.setProperty("ft.Diagnose.analog", "false");
	frame.add(new Diagnose(iface), BorderLayout.CENTER);

	Button button = new Button("start");
	Panel panel = new Panel(); panel.add(button);
	frame.add(panel, BorderLayout.SOUTH);
	
        frame.addWindowListener(new WindowAdapter() {
          public void windowClosing (WindowEvent e) { System.exit(0); }
        });
	button.addActionListener(new ActionListener() {
	  public void actionPerformed (ActionEvent ae) {
          if (ae.getActionCommand().equals("exit")) System.exit(0);
          ((Button)ae.getSource()).setLabel("exit");
	    synchronized(ctrl) { ctrl.notify(); }
	  }
	});
        frame.pack(); frame.show();
	
	thread.start();
	synchronized(ctrl) { ctrl.wait(); }
	ctrl.run();
      } catch (Exception e) { System.err.println(e); System.exit(1); }
    else System.err.println("usage: java ft.Controller commPortId class");
  }
}