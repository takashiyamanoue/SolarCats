package rcx;

import java.util.*;

/* 
 * RCXListener
 * @author Dario Laverde
 * @version 1.1
 * Copyright 1999 Dario Laverde, under terms of GNU LGPL
 */
public interface RCXListener extends EventListener {
    public void receivedMessage(byte[] message);
    public void receivedError(String error);
}

