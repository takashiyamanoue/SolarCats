package rcx;

import java.io.*;
import com.apple.MacOS.SerialPort;

/*
 * RCXMacPort
 * @author Dario Laverde
 * @version 1.1
 * Copyright 1999 Dario Laverde, under terms of GNU LGPL
 */
public class RCXMacPort implements RCXSerialPort
{
    public static int TIMEOUT = 3000;
    public static int THRESHOLD = 14;
    private SerialPort sPort;

    public boolean open(String portName) {
        sPort = new SerialPort(SerialPort.baud2400);
        return true;
    }

    public void close() {
    	if (sPort != null) {
            sPort.Dispose();
    	}
    }

    public OutputStream getOutputStream() {
        return new MacOutputStream(sPort);
    }

    public InputStream getInputStream() {
        return new MacInputStream(sPort);
    }
}

class MacInputStream extends InputStream
{
    private SerialPort sPort;
    public MacInputStream(SerialPort sport) {
        sPort = sport;
    }
    public int available() throws IOException {
        return sPort.bytesAvailable();
    }
    public void close() throws IOException {
    }
    public synchronized void mark(int readlimit) {
    }
    public boolean markSupported() {
        return false;
    }
    public int read() throws IOException {
        byte[] b = new byte[1];
        sPort.read(b);
        int val = (byte)b[0];
        if(val<0)
            val=256+val;
        return val;
    }
    public int read(byte b[]) throws IOException {
        return sPort.read(b);
    }
    public int read(byte b[], int off, int len) throws IOException {
        byte[] buff = new byte[len];
        int numread = -1;
        long initTime = System.currentTimeMillis();
        long timeOut = initTime;
        while(timeOut-initTime<RCXMacPort.TIMEOUT) {
            try {
                Thread.sleep(200);
            } catch(Exception e) {}
            if(available()>=RCXMacPort.THRESHOLD)
                break;
            timeOut = System.currentTimeMillis();
        }
        if(timeOut-initTime>=RCXMacPort.TIMEOUT)
            return -1;

        numread = sPort.read(buff);

        if(numread>0)
            System.arraycopy(buff,0,b,off,numread);

        return numread;
    }
    public synchronized void reset() throws IOException {
    }
    public long skip(long n) throws IOException {
        return -1L;
    }
}

class MacOutputStream extends OutputStream
{
    private SerialPort sPort;
    public MacOutputStream(SerialPort sport) {
        sPort = sport;
    }
    public void close() throws IOException {
    }
    public void flush() throws IOException {
    }
    public void write(byte b[]) throws IOException {
        sPort.write(b);
    }
    public void write(int b) throws IOException {
        byte[] barray = new byte[1];
        barray[0] = (byte)b;
        sPort.write(barray);
    }
    public void write(byte b[], int off, int len) throws IOException {
        byte[] buff = new byte[len];
        System.arraycopy(b,off,buff,0,len);
        sPort.write(buff);
    }
}
