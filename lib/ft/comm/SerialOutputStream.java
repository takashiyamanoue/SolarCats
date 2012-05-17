package ft.comm;
import java.io.IOException;
import java.io.OutputStream;
/** optimized for write(byte[2]).
  */
final class SerialOutputStream extends OutputStream {
  private final int portHandle;
  
  SerialOutputStream (String portName) throws IOException {
    portHandle = nativeConstructor(portName);
  }
  protected void finalize() throws Throwable { close(portHandle); }
  public void close () throws IOException { close(portHandle); }
  public void write (int ch) throws IOException {
    byte[] buf = new byte[] { (byte)ch }; write(portHandle, buf, 0, 1);
  }
  public void write (byte[] buf) throws IOException {
    write(portHandle, buf, 0, buf.length);
  }
  public void write (byte[] buf, int offset, int len)
  					throws java.io.IOException {
    if (offset < 0 || len <= 0 || offset+len > buf.length)
      throw new IllegalArgumentException("bad write buffer range");
    write(portHandle, buf, offset, len);
  }
  public void flush () { };
  
  private native int nativeConstructor (String portName) throws IOException;
  private native void close (int portHandle) throws IOException;
  private native int write (int portHandle, byte[] buf, int offset, int len)
						throws IOException;
}