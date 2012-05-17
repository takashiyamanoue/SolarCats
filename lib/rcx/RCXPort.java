package rcx; 

import java.io.*;

/*
 * RCXPort
 * @author Dario Laverde
 * @version 1.1
 * Copyright 1999 Dario Laverde, under terms of GNU LGPL
 */
public class RCXPort
{
    private static int MAXRETRIES = 3;
    private static int BUFFSIZE = 4096;
    private String portName;
    private OutputStream os;
    private InputStream is;
    private boolean open;
    private byte[] responseArray = new byte[BUFFSIZE];
    private RCXListener rcxListener;
    private int numread;
    private int numretry;
    private byte[] lastcommandArray;
    private byte[] message;
    private boolean towerError;
    private boolean rcxError;
    private byte inputcommand;    
    private byte lastcommand;
    private String lasterror;
    private RCXOpcode opcodes;

    private String OS = System.getProperty("os.name");
    private RCXSerialPort sPort;
        
    public RCXPort(String port) {
        opcodes = new RCXOpcode();
    	portName = port;
    	open = false;
    	open();
    	byte[] alivemsg = new byte[1];
    	alivemsg[0] = (byte)0x10;
    	write(alivemsg);
    }

    public void addRCXListener(RCXListener rl) {
        rcxListener = rl;
    }

    public boolean open() {
        if(open) 
            return true;
        
//        if(!OS.startsWith("Mac")) {
            sPort = new RCXCommPort();
//        }
//        else {
//            sPort = new RCXMacPort();
//        }
        
        open = sPort.open(portName);

        try {
            os = new BufferedOutputStream(sPort.getOutputStream(),4096);
            is = new BufferedInputStream(sPort.getInputStream(),4096);
        } catch (Exception e) {
            sPort.close();
            return open;
        }

        return open;
    }

    public void close() {
    	if (!open)
    	    return;
    	    
    	if (sPort != null) {
    	    try {
    	    	os.close();
    	    	is.close();
    	    } catch (IOException e) {
        		System.err.println(e);
    	    }
       	    sPort.close();
    	}
    	
    	open = false;
    }

    public boolean isOpen() {
	    return open;
    }

    public OutputStream getOutputStream() {
        return os;
    }

    public InputStream getInputStream() {
        return is;
    }

    public synchronized boolean write(byte[] bArray) {
        if(bArray.length<1) return false;
        lasterror=null;
        byte[] rcxArray = new byte[bArray.length*2+5];
        rcxArray[0]=(byte)0x55;rcxArray[1]=(byte)0xff;rcxArray[2]=(byte)0x00;
        int index=3; int sum=0;
        if(lastcommand==bArray[0]) {
            if((bArray[0]&(byte)0x08)==0) {
                bArray[0]=(byte)(bArray[0]|(byte)0x08);
            }
            else {
                bArray[0]=(byte)(bArray[0]&(byte)0xf7);
            }
        }
        lastcommand=bArray[0];
        for(int loop=0;loop<bArray.length;loop++) {
            rcxArray[index]=bArray[loop];
            rcxArray[index+1]=(byte)~bArray[loop];
            sum+=rcxArray[index];
            index+=2;
        }
        rcxArray[index]=(byte)sum;
        rcxArray[index+1]=(byte)~sum;
        try {
            os.write(rcxArray);
            os.flush();
        } catch(Exception e) {
            lasterror="error writing to port";
            lastcommandArray=null;
            return false;
        }
        lastcommandArray=rcxArray;
        processRead();
        return true;
    }

    private void processRead() {
        int avail=0;
        while (numretry < MAXRETRIES) {
            if(rcxError||towerError) {
                rcxError=false;
                towerError=false;
                try {
                    os.write(lastcommandArray);
                    os.flush();
                } catch(Exception e) {
                    lastcommandArray=null;
                    lasterror="error writing to port";
                    if(rcxListener!=null)
                        rcxListener.receivedError(lasterror);
                    break;
                }
            }
            if(((String)RCXOpcode.Opcodes.get(new Byte((byte)(lastcommand&(byte)0xf7)))).endsWith("C"))
                break;
            if(((lastcommand&(byte)0xf7)==RCXOpcode.GETMEMMAP)) {
                try{Thread.sleep(2000);}catch(Exception e){ }
            }
            try {
                numread = is.read(responseArray,0,BUFFSIZE);
            } catch(Exception e) {
                lasterror="error reading from port";                
                if(rcxListener!=null)
                    rcxListener.receivedError(lasterror);
                rcxError=false;
                towerError=false;
                break;
            }

            if(lastcommandArray.length>numread) {
                lasterror="error reading message from tower";
                towerError=true;
                numretry++;
                continue;
            }
            if((responseArray[0]!=(byte)0x55&&responseArray[0]!=(byte)0xaa)||(responseArray[1]!=(byte)0xff)||(responseArray[2]!=(byte)0x00))
            {
                lasterror="error in message header from tower";                
                towerError=true;
                numretry++;
                continue;
            }

            int echolen = lastcommandArray.length;

            if(echolen==numread) {
                lasterror="no reponse from RCX";
                rcxError=true;
                numretry++;
                continue;
            }
            if(numread<echolen+5 || (numread-echolen-3)%2!=0) {
                lasterror="error in response length from RCX";
                rcxError=true;
                numretry++;
                continue;
            }
            if((responseArray[echolen]!=(byte)0x55&&responseArray[echolen]!=(byte)0xaa)||responseArray[echolen+1]!=(byte)0xff||responseArray[echolen+2]!=(byte)0x00) {
                lasterror="error in response header from RCX";                
                rcxError=true;
                numretry++;
                continue;
            }
            message=new byte[(numread-(echolen+3))/2-1];
            int sum=0;
            int loop=0;
            int msgcount=0;
            for(loop=echolen+3;loop<numread;loop+=2) {
                if(responseArray[loop+1]!=~responseArray[loop]) {
                    lasterror="error in response from RCX";  
                    rcxError=true;
                    numretry++;
                    continue;
                }
                if(loop<numread-2) {
                    message[msgcount++] = responseArray[loop];
                    sum+=responseArray[loop];
                }
            }
            if((byte)sum!=responseArray[numread-2]) {
                    lasterror="error in response from RCX";                  
                    rcxError=true;
                    numretry++;
                    continue;
            }
            if(responseArray[echolen+3]!=~lastcommandArray[3])
            {
                lasterror="error - invalid response from RCX";
                rcxError=true;
                numretry++;
                continue;
            }
            if(!rcxError&&!towerError)
            {
                numretry=0;
                if((message[0]&(byte)0x08)==(byte)0x08) {
                    message[0]=(byte)(message[0]&(byte)0xf7);
                }
                if(rcxListener!=null)
                    rcxListener.receivedMessage(message);
                break;
            }
        }
        if(towerError || rcxError) {
            if(rcxListener!=null)
                rcxListener.receivedError(lasterror);
            message=null;
            numretry=0;
        }
    }

    public String getLastError() {
        return lasterror;
    }   
}
