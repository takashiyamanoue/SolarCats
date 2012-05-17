package ft.comm;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.comm.CommDriver;
import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
/** rudimentary communication port driver for FischerTechnik.
    Influenced by the rxtx package for Linux.
  */
public final class Driver implements CommDriver {
  /** called (once, we hope) by the CommPortIdentifier's static initializer.
      Load native library, check platform.
      Add ft.properties to system properties, load driver properties:
	ft.comm.Driver.trace.flags
	ft.comm.Driver.idle
	ft.comm.Driver.scale
	ft.comm.Driver.disable
	ft.comm.Driver.load.out
	ft.comm.Driver.serial
	ft.comm.Driver.parallel
    */
  public void initialize () {
    String osName = System.getProperty("os.name");
    if (osName.startsWith("Windows ")) System.loadLibrary("ft.comm");
    else {
      System.err.println("unknown os.name: "+osName); System.exit(1);
    }

    if (!Boolean.getBoolean("ft.properties"))
      try {
        System.getProperties().load(new FileInputStream("ft.properties"));
      } catch (IOException e) { System.err.println(e); }

    String prefix = getClass().getName();
    traceFlags = Integer.getInteger(prefix+".trace.flags", 0).intValue();
    idle = Integer.getInteger(prefix+".idle", 2).intValue();
    scale = Integer.getInteger(prefix+".scale", 4).intValue();
    disable = Boolean.getBoolean(prefix+".disable");
    loadOut = Boolean.getBoolean(prefix+".load.out");

    String s = System.getProperty(prefix+".serial");
    if (s != null && s.length() > 0)
      for (StringTokenizer st = new StringTokenizer(s); st.hasMoreTokens(); ) {
        s = st.nextToken();
        if (traceFlags != 0) System.err.println(prefix+" adds \""+s+"\"");
        CommPortIdentifier.addPortName(s, CommPortIdentifier.PORT_SERIAL, this);
      }
      
    s = System.getProperty(prefix+".parallel");
    if (s != null && s.length() > 0)
      for (StringTokenizer st = new StringTokenizer(s); st.hasMoreTokens(); ) {
        s = st.nextToken();
        if (traceFlags != 0) System.err.println(prefix+" adds \""+s+"\"");
        CommPortIdentifier.addPortName(s, CommPortIdentifier.PORT_PARALLEL,
									this);
      }
  }
  /** Win32: can be set to trace parallel driver functions:
      1[|2|4] output 8[|16|32] digital 64[|128] analog.
    */
  protected static int traceFlags;
  /** Win32: can be set to tune parallel driver.
    */	
  protected static int idle;
  /** Win32: can be set to tune analog values in parallel driver.
    */
  protected static int scale;
  /** Win32: can be set to control interrupts in parallel driver.
    */
  protected static boolean disable;
  /** controls loadOut line during input.
      Documentation wants it high; experiment shows that it needs to be low.
    */
  protected static boolean loadOut;
  /** called by CommPortIdentifier from openPort().
      @param portName should have been registered above.
    */
  public CommPort getCommPort (String portName, int portType) {
    try {
      switch (portType) {
      case CommPortIdentifier.PORT_SERIAL:
        return new SerialPort(portName);
      case CommPortIdentifier.PORT_PARALLEL:
        return new ParallelPort(portName);
      default:
        System.err.println(portName+" "+portType); System.exit(1);
      }
    } catch (IOException e) { e.printStackTrace(); }
    return null;
  }
}
