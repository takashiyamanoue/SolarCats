package ft.comm;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;
import javax.comm.ParallelPortEventListener;
import javax.comm.UnsupportedCommOperationException;
/** rudimentary parallel port for FischerTechnik.
    Makes parallel interface behave like serial interface.
  */
final class ParallelPort extends javax.comm.ParallelPort {
  ParallelPort (String portName) throws IOException {
    out = new ParallelOutputStream(portName);	// first!
    in = new ParallelInputStream(portName);
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

  public int getOutputBufferFree () { closed(); return 2; }
  public boolean isPaperOut () { closed(); return false; }
  public boolean isPrinterBusy () { closed(); return false; }
  public boolean isPrinterSelected () { closed(); return false; }
  public boolean isPrinterTimedOut () { closed(); return false; }
  public boolean isPrinterError () { closed(); return false; }

// --------------------------------------------------------- inoperative methods

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

  public void restart () { closed(); }
  public void suspend () { closed(); }

  public int getMode () { closed(); return LPT_MODE_SPP; }
  public int setMode (int mode) throws UnsupportedCommOperationException {
    closed(); throw new UnsupportedCommOperationException();
  }

  public void addEventListener (ParallelPortEventListener lsnr)
  					throws TooManyListenersException {
    closed(); throw new TooManyListenersException();
  }
  public void removeEventListener () { closed(); }

  public void notifyOnError (boolean notify) { closed(); }
  public void notifyOnBuffer(boolean notify) { closed(); }
}