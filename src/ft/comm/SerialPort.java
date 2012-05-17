package ft.comm;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;
/** rudimentary serial port for FischerTechnik.
    With ideas from the rxtx package for Linux.
  */
final class SerialPort extends javax.comm.SerialPort {
  SerialPort (String portName) throws IOException {
    out = new SerialOutputStream(portName);
    in = new SerialInputStream(portName);
  }
  private InputStream in;
  private OutputStream out;
  
  public void close () {
    if (!down)
      try {
        in.close(); out.close();
      } catch (IOException e) {
      } finally { down = true; super.close(); }
  }
  private boolean down;

  protected void finalize() throws Throwable { close(); }
  
  void closed () { if (down) throw new IllegalStateException("Port Closed"); }
  
  public InputStream getInputStream () throws IOException {
    closed(); return in;
  }

  public OutputStream getOutputStream () throws IOException {
    closed(); return out;
  }
  
// ---------------------------------------------------------------- fake methods

  public int getBaudRate () { closed(); return 9600; }
  public int getDataBits () { closed(); return DATABITS_8; }
  public int getStopBits () { closed(); return STOPBITS_1; }
  public int getParity () { closed(); return PARITY_NONE; }
  public void setSerialPortParams (int baudrate, int dataBits, int stopBits,
  			int parity) throws UnsupportedCommOperationException {
    closed(); // throw new UnsupportedCommOperationException();
  }

  private boolean dtr = true;
  public void setDTR (boolean dtr) { closed(); this.dtr = dtr; }
  public boolean isDTR () { closed(); return dtr; }

  private boolean rts = true;
  public void setRTS (boolean rts) { closed(); this.rts = rts; }
  public boolean isRTS () { closed(); return rts; }

  public boolean isCTS () { closed(); return true; }
  public boolean isDSR () { closed(); return true; }
  public boolean isRI () { closed(); return false; }
  public boolean isCD () { closed(); return true; }

// --------------------------------------------------------- inoperative methods

  public void setFlowControlMode (int flowcontrol)
				throws UnsupportedCommOperationException {
    closed(); throw new UnsupportedCommOperationException();
  }
  public int getFlowControlMode () { closed(); return FLOWCONTROL_NONE; }

  public void enableReceiveThreshold (int thresh)
				throws UnsupportedCommOperationException {
    closed(); throw new UnsupportedCommOperationException();
  }
  public void disableReceiveThreshold () { closed(); }
  public boolean isReceiveThresholdEnabled () { closed(); return false; }
  public int getReceiveThreshold () { closed(); return -1; }

  public void enableReceiveTimeout (int rcvTimeout)
  				throws UnsupportedCommOperationException {
    closed(); throw new UnsupportedCommOperationException();
  }
  public void disableReceiveTimeout () { closed(); }
  public boolean isReceiveTimeoutEnabled () { closed(); return false; }
  public int getReceiveTimeout () { closed(); return -1; }

  public void enableReceiveFraming (int framingByte)
  				throws UnsupportedCommOperationException {
    closed(); throw new UnsupportedCommOperationException();
  }
  public void disableReceiveFraming () { closed(); }
  public boolean isReceiveFramingEnabled () { closed(); return false; }
  public int getReceiveFramingByte() { closed(); return -1; }

  public void setInputBufferSize (int size) { closed(); }
  public int getInputBufferSize () { closed(); return 0; }

  public void setOutputBufferSize (int size) { closed(); }
  public int getOutputBufferSize () { closed(); return 0; }

  public void sendBreak (int millis) { closed(); }

  public void addEventListener (SerialPortEventListener lsnr)
  					throws TooManyListenersException {
    closed(); throw new TooManyListenersException();
  }
  public void removeEventListener () { closed(); }
  public void notifyOnDataAvailable (boolean notify) { closed(); }
  public void notifyOnOutputEmpty (boolean notify) { closed(); }
  public void notifyOnCTS (boolean notify) { closed(); }
  public void notifyOnDSR (boolean notify) { closed(); }
  public void notifyOnCarrierDetect (boolean notify) { closed(); }
  public void notifyOnRingIndicator (boolean notify) { closed(); }
  public void notifyOnOverrunError (boolean notify) { closed(); }
  public void notifyOnParityError (boolean notify) { closed(); }
  public void notifyOnFramingError (boolean notify) { closed(); }
  public void notifyOnBreakInterrupt (boolean notify) { closed(); }
}