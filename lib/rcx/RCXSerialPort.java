package rcx;

import java.io.*; 

/*
 * RCXSerialPort
 * @author Dario Laverde
 * @version 1.1
 * Copyright 1999 Dario Laverde, under terms of GNU LGPL
 */
public interface RCXSerialPort
{
    public boolean open(String portName);
    public void close();
    public OutputStream getOutputStream();
    public InputStream getInputStream();
}
