package ft.comm;
import java.io.IOException;
import java.io.InputStream;
/** optimized for read() and read(byte[3]).
  */
final class ParallelInputStream extends InputStream {
  private final int portHandle;
  
  ParallelInputStream (String portName) throws IOException {
    portHandle = nativeConstructor(portName);
  }
  // protected void finalize() throws Throwable { }
  // public void close () throws IOException { }
  public int read () { return read(portHandle); }
  public int read (byte[] buf) throws IOException {
    return read(portHandle, buf, 0, buf.length);
  }
  public int read (byte[] buf, int offset, int len) throws IOException {
    if (offset < 0 || len <= 0 || offset+len > buf.length)
      throw new IllegalArgumentException("bad read buffer range");
    return read(portHandle, buf, offset, len);
  }
  public long skip (long len) { return 0; }
  // public int available () throws IOException { return 0; }
  // public boolean markSupported () { return false; }
  // public void mark (int) { }
  // public void reset () throws IOException { throw new IOException(); }
  
  private native int nativeConstructor (String portName) throws IOException;
  private native int read (int portHandle);
  private native int read (int portHandle, byte[] buf, int offset, int len)
						throws IOException;
}