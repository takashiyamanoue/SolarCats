package ft;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;
/** manages connection to the interface.
  */
public class Interface implements Constants, Runnable {
  /** get E1..8 */		protected static final byte Fn = (byte)0xc1;
  /** get E1..8 and EX */	protected static final byte Fx = (byte)0xc5;
  /** get E1..8 and EY */	protected static final byte Fy = (byte)0xc9;
  /** add ft.properties to system properties.
    */
  static {
    if (!Boolean.getBoolean("ft.properties"))
      try {
        System.getProperties().load(new FileInputStream("ft.properties"));
      } catch (IOException e) { System.err.println(e); }
  }
  /** locate port by name.
      @param name of port identifier, e.g., COM1 or /dev/ttyS0.
    */
  public static Interface open (String name) throws FileNotFoundException {
    try {
      CommPortIdentifier portId =  CommPortIdentifier.getPortIdentifier(name);
      return new Interface(portId);
    } catch (Exception e) { throw new FileNotFoundException(name); }
  }
  /** acquire the port within ft.timeout seconds.
    */
  protected Interface (CommPortIdentifier portId) throws IOException,
  			PortInUseException, UnsupportedCommOperationException {
    this.portId = portId;
    port = portId.open(getClass().getName(), timeout);
    if (port instanceof SerialPort)
      ((SerialPort)port).setSerialPortParams(9600, SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    in = port.getInputStream();
    out = port.getOutputStream();
  }
  /** "name" of port */		protected final CommPortIdentifier portId;
  /** connection to device */	protected final CommPort port;
  /** query, set outputs */	protected final OutputStream out;
  /** get inputs */		protected final InputStream in;
  /** release the port.
    */
  protected synchronized void finalize () { port.close(); }
  /** monitor the interface; run once only.
      Loops to output motor state and input digital and analog values.
      Informs the various models.
    */
  public void run () {
    final byte[] inBuf = new byte[3];
    
    Thread timer = null;			// timer thread, if any
    final boolean rastered[] = new boolean[1];	// monitor for timer thread
    try {
      if (raster > 0) {				// run timer thread, if any
        timer = new Thread() {
          public void run () {
            try {
              for (;;) {
                sleep(raster);
                synchronized (rastered) {
		  rastered[0] = true; rastered.notify();
		}
              }
            } catch (InterruptedException e) { }
          }
        };
        timer.setDaemon(true); timer.start();
      }
      
      queue.setDaemon(true); queue.start();	// run notification thread
      
      for (;;) {
        byte request;
        synchronized (outBuf) {
	  request = outBuf[0];			// remember last request
	  out.write(outBuf);			// send request and motor state
	}
        if (request == Fn) {			// Fn: get digital inputs
          int n = in.read();
          if (n < 0) throw new IOException("eof on "+portId.getName());
	  for (int d = 0; d < digital.length; ++ d) digital[d].setCurrent(n);
        } else {				// Fx or Fy: digital and analog
          int n = in.read(inBuf);
	  if (n < 0) throw new IOException("eof on "+portId.getName());
	  else while (n < 3) {			// [not ft.comm.Driver]
	    int ch = in.read();
	    if (ch < 0) throw new IOException("eof on "+portId.getName());
	    inBuf[n++] = (byte)ch;
	  }
	  n = inBuf[0] & 0xff;
	  for (int d = 0; d < digital.length; ++ d) digital[d].setCurrent(n);
	  analog[request == Fx ? 0 : 1]
		.setCurrent(queue, inBuf[1]<<8 & 0xff00 | inBuf[2] & 0xff);
        }
        synchronized (outBuf) {
          switch (mode) {			// next request...
          case 0:				// ...digital only
            outBuf[0] = Fn; break;
	  case EX:				// ...single analog
            if (-- skip <= 0) { outBuf[0] = Fx; skip = query; break; }
	  case EY:				// ...single analog
            if (-- skip <= 0) { outBuf[0] = Fy; skip = query; break; }
          default:				// ...else flip Fx/Fy
            if (-- skip > 1) outBuf[0] = Fn;	// query-2 times Fn
            else switch (request) {		// and then Fx and then Fy
              case Fn: outBuf[0] = Fx; skip = 0; break;
              case Fx: outBuf[0] = Fy; skip = 0; break;
              case Fy: outBuf[0] = (skip = query) > 2 ? Fn : Fx;
              }
          }
	}
	if (timer != null)			// wait for timer if any...
	  synchronized (rastered) {
	    while (!rastered[0]) rastered.wait();
	    rastered[0] = false;
	  }
	else					// ...or yield to observers
	  Thread.yield();
	cycle.incr();				// report cycle completion
      }
    } catch (IOException e) { System.err.println(e);
    } catch (InterruptedException e) {
    } finally {
      queue.interrupt();
      if (timer != null) timer.interrupt();
    }
  }
  /** [1]: outputs M1..4 */	protected final byte[] outBuf =
							new byte[]{ Fn, 0 };
  /** cycles between Fx/Fy */	protected int skip;

// -------------------------------------------------- thread to notify observers

  /** thread to notify observers */	protected final Queue queue =
  								new Queue();
  /** used to detach notifying any model's observers
      from changing the model's state.
    */
  protected static class Queue extends Thread {
    /** pending, must not overflow. */	protected Model queue[] =
    						new Model[Mnum+Enum+Anum+Cnum];
    /** indices, front <= back. */	protected int front, back;
    /** uniquely add model with pending state to run queue.
      */
    public synchronized void enqueue (Model model) {
      for (int n = front; n != back; n = n+1 < queue.length ? n+1 : 0)
        if (queue[n] == model) return;
      queue[back++] = model; if (back >= queue.length) back = 0;
      if (back == front) throw new IndexOutOfBoundsException("queue overflow");
      notify();
    }
    /** keep dequeueing and call update for each model in queue.
      */
    public void run () {
      try {
        for (;;) {
          Model model;
          synchronized (this) {
            while (front == back) wait();
            model = queue[front++]; if (front >= queue.length) front = 0;
          }
          model.update();
        }
      } catch (InterruptedException e) { }
    }
  }

// ------------------------------------------------------- base class for models

  /** base class for models, manages state and calls observers.
      This is static so that Change may be nested.
      This requires that queue must be an argument to setCurrent().
    */
  protected static abstract class Model {
    /** define mask identifying model.
      */
    public Model (int me) {
      this.me = me;
    }
    /** mask identifying model */	protected final int me;
    /** model's current state */	protected int current;
    /** model's next state */		protected int pending;		
    /** @return current state.
      */
    public synchronized int getCurrent () { return current; }
    /** suggests new state.
        If this is called too fast, intermittent new states are lost.
        @param pending if different from current state make it pending and
		       ask queue to schedule an update.
      */
    public synchronized void setCurrent (Queue queue, int pending) {
      if (current != pending) {
	this.pending = pending; queue.enqueue(this);
      }
    }
    /** list of observers. */		protected Object observer[];
    /** @param observer to receive appropriate edge().
        @throws IllegalArgumentException if observer is null.
      */
    public synchronized void addObserver (Object observer) {
      if (observer == null) throw new IllegalArgumentException("null observer");
      if (this.observer == null) {
        this.observer = new Object[]{ observer };
	return;
      }
      for (int n = 0; n < this.observer.length; ++ n)
	if (this.observer[n] == observer) return;
      int l = this.observer.length;
      Object o[] = new Object[l+1];
      System.arraycopy(this.observer, 0, o, 0, l); o[l] = observer;
      this.observer = o;
    }
    /** @param observer to be removed.
        @throws IllegalArgumentException if observer is null or not observing.
      */
    public synchronized void removeObserver (Object observer) {
      if (observer == null) throw new IllegalArgumentException("null observer");
     if (this.observer != null)
        for (int n = 0; n < this.observer.length; ++ n)
          if (this.observer[n] == observer) {
	    if (this.observer.length == 1) this.observer = null;
	    else {
              int l = this.observer.length-1;
              Object o[] = new Object[l];
              if (n > 0) System.arraycopy(this.observer, 0, o, 0, n);
              if (n < l) System.arraycopy(this.observer, n+1, o, n, l-n);
	      this.observer = o;
	    }
            return;
          }
      throw new IllegalArgumentException("not observing");
    }
    /** pushes pending into current state.
        Called by Queue or other thread.
        Should call notifyObservers() unsynchronized if there is a change.
      */
    public abstract void update ();
    /** declares callback from notifyObservers() back to Model.
      */
    protected abstract static class Change {
      /** should send edge() to observer, called by update/notifyObservers.
	  @param observer to inform.
	*/
      public abstract void edge (Object observer);
    }
    /** @param change called back with interested observers.
      */
    protected void notifyObservers (Change change) {
      synchronized(this) {
        if (this.observer == null) return;
        Object observer[] = (Object[])this.observer.clone();
      }
      for (int n = 0; n < observer.length; ++ n)
	change.edge(observer[n]);
    }
  }

// ------------------------------------------------- observer pattern for motors

  /** models for outputs */		protected final Motor motor[];
  { motor = new Motor[Mnum];
    for (int n = 0; n < motor.length; ++ n) motor[n] = new Motor(n);
  }
  /** must be implemented to observe changes of motor states.
    */
  public interface MotorObserver extends Constants {
    /** reports change, must be completed quickly.
        @param iface observed Interface.
        @param motor M1..
        @param value new (current) value of motor: LEFT RIGHT OFF.
      */
    void edge (Interface iface, int motor, int value);
  }
  /** holds state of one output.
    */
  protected class Motor extends Model {
    /** @param myIndex motor number, counted from 0.
      */
    public Motor (int myIndex) {
      super(M1 << myIndex*Mshift);
      this.myIndex = myIndex;
    }
    protected final int myIndex;
    /** checks argument.
        @param how LEFT OFF RIGHT.
	@throws IllegalArgumentException if how is bad.
    */
    public void setCurrent (int how) {
      switch (how) {
      case OFF: case LEFT: case RIGHT:
        super.setCurrent(queue, how); return;
      }
      throw new IllegalArgumentException("bad output value");
    }
    /** push pending into current state, invokes notifyObservers() on changes.
      */
    public void update () {
      final int current;
      synchronized (this) {
        if (this.current == pending) return;
	current = this.current = pending;
      }
      synchronized (outBuf) {
	outBuf[1] &= ~me; outBuf[1] |= current << myIndex*Mshift;
      }
      notifyObservers(new Model.Change() {
        public void edge (Object observer) {
          ((MotorObserver)observer).edge(Interface.this, me, current);
        }
      });
    }
  }
  /** @param mask M1.. or-ed together for motors to be observed.
      @param o to receive edge() notifications.
    */
  public void addObserver (int mask, MotorObserver o) {
    if (mask != 0)
      for (int n = 0, bit = M1; n < motor.length; ++ n, bit <<= Mshift)
        if ((mask & bit) != 0) motor[n].addObserver(o);
  }
  /** @param mask M1.. or-ed together for motors to be ignored.
      @param o to be removed.
    */
  public void removeObserver (int mask, MotorObserver o) {
    if (mask != 0)
      for (int n = 0, bit = M1; n < motor.length; ++ n, bit <<= Mshift)
        if ((mask & bit) != 0) motor[n].removeObserver(o);
  }
  /** set outputs.
      @param mask M1.. or-ed together.
      @param how LEFT OFF RIGHT.
    */
  public void set (int mask, int how) {
    if (mask != 0)
      for (int n = 0, bit = M1; n < motor.length; ++ n, bit <<= Mshift)
        if ((mask & bit) != 0) motor[n].setCurrent(how);
  }
  
// ----------------------------------------- observer pattern for digital inputs

  /** models for digital inputs */	protected final Digital digital[];
  { digital = new Digital[Enum];
    for (int n = 0; n < digital.length; ++ n) digital[n] = new Digital(n);
  }
  /** must be implemented to observe edges and digital inputs.
    */
  public interface DigitalObserver extends Constants {
    /** reports 0->1 or 1->0 edge, must be completed quickly.
        @param iface observed Interface.
        @param input E1..
        @param value new (current) value of sensor.
        @param count number of edges seen since last reset.
      */
    void edge (Interface iface, int input, boolean value, int count);
  }
  /** holds state of all digital inputs.
    */
  protected class Digital extends Model {
    /** @param myIndex digital input number, counted from 0.
      */
    public Digital (int myIndex) {
      super(E1 << myIndex*Eshift);
      this.myIndex = myIndex;
    }
    protected final int myIndex;
    /** edges, immediately updated */	protected int count;
    /** incr, immediately changed */	protected int delta = 1;
    /** pending count */		protected int pendingCount [];
    /** updates count immediately if there is a change.
        @param pending masked by me to determine new value of current, if any.
      */
    public synchronized void setCurrent (int pending) {
      if (current != (pending &= me)) {
        count += delta;
        super.setCurrent(queue, pending);
      }
    }
    /** manipulate edge counts.
        @param count new value, set by enqueueing.
        @param delta increment, set immediately without notifying observers.
      */
    public synchronized void set (int count, int delta) {
      if (pendingCount == null) pendingCount = new int[]{ count };
      else pendingCount[0] = count;
      this.delta = delta;
      queue.enqueue(this);
    }
    /** @param delta increment, set immediately without notifying observers.
      */
    public synchronized void set (int delta) {
      this.delta = delta;
    }
    /** is digital input set?
      */
    public synchronized boolean isSet () { return current != 0; }
    /** report edge count.
      */
    public synchronized int isAt () { return count; }
    /** push pending into current state, invokes notifyObservers() on changes.
        Pushes pending counts and clears pendingCount mask
	before notifying observers.
      */
    public void update () {
      final int count;
      final boolean current;
      synchronized (this) {
        if (pendingCount != null) {
          count = this.count = pendingCount[0]; pendingCount = null;
	} else if (this.current == pending) return;
	else count = this.count;
	current = (this.current = pending) != 0;
      }
      notifyObservers(new Model.Change() {
        public void edge (Object observer) {
          ((DigitalObserver)observer).edge(Interface.this, me, current, count);
        }
      });
    }
  }
  /** @param mask E1.. or-ed together for digital inputs to be observed.
      @param o to receive edge() notifications.
    */
  public void addObserver (int mask, DigitalObserver o) {
    if (mask != 0)
      for (int n = 0, bit = E1; n < digital.length; ++ n, bit <<= Eshift)
        if ((mask & bit) != 0) digital[n].addObserver(o);
  }
  /** @param mask E1.. or-ed together for digital inputs to be ignored.
      @param o to be removed.
    */
  public void removeObserver (int mask, DigitalObserver o) {
    if (mask != 0)
      for (int n = 0, bit = E1; n < digital.length; ++ n, bit <<= Eshift)
        if ((mask & bit) != 0) digital[n].removeObserver(o);
  }
  /** manipulate edge counts.
      @param mask E1.. or-ed together for digital inputs to be manipulated.
      @param count new value.
      @param delta increment.
    */
  public void set (int mask, int count, int delta) {
    if (mask != 0)
      for (int n = 0, bit = E1; n < digital.length; ++ n, bit <<= Eshift)
        if ((mask & bit) != 0) digital[n].set(count, delta);
  }
  /** manipulate deltas only.
      @param mask E1.. or-ed together for digital inputs to be manipulated.
      @param delta increment.
    */
  public void setDelta (int mask, int delta) {
    if (mask != 0)
      for (int n = 0, bit = E1; n < digital.length; ++ n, bit <<= Eshift)
        if ((mask & bit) != 0) digital[n].set(delta);
  }
  /** report if digital input is set.
      @param mask E1.. for digital inputs to be reported.
      @throws IllegalArgumentException if not exactly one input is selected.
  */
  public boolean isSet (int mask) {
    if (mask != 0)
      for (int n = 0, bit = E1; n < digital.length; ++ n, bit <<= Eshift)
        if (mask == bit) return digital[n].isSet();
    throw new IllegalArgumentException("bad digital input");
  }
  /** report edge count of one digital input.
      @param mask E1.. for digital input to be reported.
      @throws IllegalArgumentException if not exactly one input is selected.
    */
  public int isAt (int mask) {
    if (mask != 0)
      for (int n = 0, bit = E1; n < digital.length; ++ n, bit <<= Eshift)
        if (mask == bit) return digital[n].isAt();
    throw new IllegalArgumentException("bad digital input");
  }

// ----------------------------------------- observer pattern for analog inputs

  /** models for analog inputs */	protected final Analog analog[];
  { analog = new Analog[Anum];
    for (int n = 0, bit = EX; n < analog.length; ++ n, bit <<= Ashift)
      analog[n] = new Analog(bit);
  }
  /** must be implemented to observe values of analog inputs.
    */
  public interface AnalogObserver extends Constants {
    /** reports value change, must complete very quickly.
        @param iface observed Interface.
        @param input EX..
        @param value new (current) value of sensor.
      */
    void edge (Interface iface, int input, int value);
  }
  /** holds state of one analog input.
    */
  protected class Analog extends Model {
    public Analog (int me) { super(me); }
    /** scale: a*raw + b */		protected float a;
    /** scale: a*raw + b */		protected float b;
    /** set scale of analog input: raw input is scaled as a*raw + b.
        Observers are only informed, if scaled value changed.
    */
    public synchronized void setScale (float a, float b) {
      this.a = a; this.b = b; queue.enqueue(this);
    }
    /** get scale of one analog input.
      */
    public synchronized float[] getScale () { return new float[] { a, b }; }
    /** overwritten to turn analog monitoring on if needed.
      */
    public synchronized void addObserver (Object o) {
      if (observer == null) synchronized (outBuf) { mode |= me; }
      super.addObserver(o);
    }
    /** overwritten to turn analog monitoring off if not required.
      */
    public synchronized void removeObserver (Object o) {
      super.removeObserver(o);
      if (observer == null) synchronized (outBuf) { mode &= ~me; }
    }
    /** if scaled values differ, pushes pending into current state
        and invokes notifyObservers().
      */
    public void update () {
      final int current;
      synchronized (this) {
        current = (int)(a*pending + b);
	if (current == (int)(a*this.current + b)) return;
	this.current = pending;
      }
      notifyObservers(new Model.Change() {
        public void edge (Object observer) {
          ((AnalogObserver)observer).edge(Interface.this, me, current);
        }
      });
    }
  }
  /** @param mask EX.. or-ed together for analog inputs to be observed.
      @param o to receive edge() notifications.
    */
  public void addObserver (int mask, AnalogObserver o) {
    if (mask != 0)
      for (int n = 0, bit = EX; n < analog.length; ++ n, bit <<= Ashift)
        if ((mask & bit) != 0) analog[n].addObserver(o);
  }
  /** @param mask EX.. or-ed together for analog inputs to be ignored.
      @param o to be removed.
    */
  public void removeObserver (int mask, AnalogObserver o) {
    if (mask != 0)
      for (int n = 0, bit = EX; n < analog.length; ++ n, bit <<= Ashift)
        if ((mask & bit) != 0) analog[n].removeObserver(o);
  }
  /** set scale of analog inputs: raw input is scaled as a*raw + b.
      Observers are only informed, if scaled value changed.
      @param mask EX.. or-ed together for analog inputs to be manipulated.
    */
  public void setScale (int mask, float a, float b) {
    if (mask != 0)
      for (int n = 0, bit = EX; n < analog.length; ++ n, bit <<= Ashift)
        if ((mask & bit) != 0) analog[n].setScale(a, b);
  }
  /** get scale of one analog input.
      @param mask EX.. for analog input to be reported.
      @throws IllegalArgumentException if not exactly one input is selected.
    */
  public float[] getScale (int mask) {
    if (mask != 0)
      for (int n = 0, bit = EX; n < analog.length; ++ n, bit <<= Ashift)
        if (mask == bit) return analog[n].getScale();
    throw new IllegalArgumentException("bad analog input");
  }
  
// ------------------------------------------------- observer pattern for cycles

  /** model for refresh cycles  */	protected final Cycle cycle =
								new Cycle();
  /** must be implemented to observe state refresh cycles.
    */
  public interface CycleObserver extends Constants {
    /** reports change, must be completed quickly.
        @param iface observed Interface.
        @param cycles number of cycles seen since last reset.
      */
    void edge (Interface iface, int cycles);
  }
  /** holds cycle counter.
    */
  protected class Cycle extends Model {
    public Cycle () { super(0); }
    /** increment count.
        This can clobber an attempt to set the cycles.
      */
    public synchronized void incr () {
      this.pending = current+1; queue.enqueue(this);
    }
    /** push pending into current count, invokes notifyObservers() on changes.
      */
    public void update () {
      final int current;
      synchronized (this) {
	if (pending == this.current) return;
	current = this.current = pending;
      }
      notifyObservers(new Model.Change() {
        public void edge (Object observer) {
          ((CycleObserver)observer).edge(Interface.this, current);
        }
      });
    }
  }
  /** @param o to receive edge() notifications.
    */
  public void addObserver (CycleObserver o) { cycle.addObserver(o); }
  /** @param o to be removed.
    */
  public void removeObserver (CycleObserver o) { cycle.removeObserver(o); }
  /** increment cycle count.
    */
  public void incr() { cycle.incr(); }
  /** manipulate cycle count.
      @param cycles new value.
    */
  public void set (int cycles) { cycle.setCurrent(queue, cycles); }
  
// ------------------------------------------------------------------ properties

  /** milliseconds to open port */	protected int timeout;
  /** i/o cycles before analog input */	protected int query;
  /** type of i/o cycles: EX.. or-ed */	protected int mode;
  /** milliseconds for timer */		protected int raster;
  
  /** retrieve current value.
    */
  public int getQuery () { return query; }
  /** change the query pattern. This does not start/stop analog observing.
    */
  public void setQuery (int query) {
    synchronized (outBuf) { skip = this.query = query; }
  }

  { String prefix = getClass().getName();
    timeout = Integer.getInteger(prefix+".timeout", 2).intValue() * 1000;
    query = Integer.getInteger(prefix+".query", 10).intValue();
    mode = Integer.getInteger(prefix+".mode", 0).intValue();
    raster = Integer.getInteger(prefix+".raster", 0).intValue();
  }

// ------------------------------------------------------------ performance test

  /** hack to measure performance.
      Use ft.properties to set digital vs. complete queries.
      <tt>ft.Interface commPortId [count]</tt>
      will output the number of milliseconds for count inquiries.
    */
  public static void main (String[] args) {
    if (args != null && args.length > 0)
      try {
        Interface iface = Interface.open(args[0]);
        iface.addObserver(Eall, new DigitalObserver() {
          public void edge (Interface iface, int in, boolean val, int count) {
            System.err.println(in+" "+val+" "+count);
          }
        });

	int mask = Eall;
        if (iface.mode != 0)
	  iface.addObserver(mask, new AnalogObserver() {
            public void edge (Interface iface, int in, int val) {
              System.err.println(in+" "+val);
            }
          });
	
        final long start = System.currentTimeMillis();	// timing
        final int count = args.length > 1 ? Integer.parseInt(args[1]) : 1000;
	iface.addObserver(new CycleObserver() {
          public void edge (Interface iface, int cycles) {
	    if (cycles > count) {
              long msec = System.currentTimeMillis() - start;
              System.err.println(count+" in "+msec+" msec, "
	      					+count*1000/msec+" Hz");
	      System.exit(0);
	    }
	  }
	});
	  
        iface.run();
      } catch (Exception e) { System.err.println(e); System.exit(1); }
    else System.err.println("usage: java ft.Interface commPortId [count]");
  }
}