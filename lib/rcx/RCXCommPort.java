package rcx;

import java.io.*; 
import javax.comm.*;

/*
 * RCXCommPort
 * @author Dario Laverde
 * @version 1.1
 * Copyright 1999 Dario Laverde, under terms of GNU LGPL
 */
public class RCXCommPort implements RCXSerialPort
{
    private static int TIMEOUT = 3000;
    private SerialPort sPort;
    public boolean open(String portName) {
       CommPortIdentifier portId;
        try {
            portId = CommPortIdentifier.getPortIdentifier(portName);
        } catch (NoSuchPortException e) {
            return false;
        }
        try {
            sPort = (SerialPort)portId.open("RCXPort", 1000);
        } catch (PortInUseException e) {
            return false;
        }

    	try {
    	    sPort.setSerialPortParams(2400,
    	                              SerialPort.DATABITS_8,
    	                              SerialPort.STOPBITS_1,
                                	  SerialPort.PARITY_ODD );
            sPort.enableReceiveTimeout(TIMEOUT);
            sPort.enableReceiveThreshold(14);
    	} catch (Exception e) {
    	    e.printStackTrace();
    	    return false;
    	}
        return true;
    }

    public void close() {
    	if (sPort != null) {
       	    sPort.close();
    	}
    }

    public OutputStream getOutputStream() {
        if(sPort!=null) {
            try {
                return sPort.getOutputStream();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public InputStream getInputStream() {
        if(sPort!=null) {
            try {
                return sPort.getInputStream();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

