package application.fileTransfer;

import controlledparts.*;

public class ParseFileTransferEvent extends ParseEvent
{
    byte[] binary;
    public void setBinary(byte[] x){
    	binary=x;
    }
	public boolean parseFileTransferEnd(FileTransferFrame l)
	{
		if(!iq.rString("end()")) return false;
		if(l!=null){
			if(l!=null){
			   l.endData();
			}
		}
		return true;
	}

	public boolean parseFileTransferStart(FileTransferFrame l)
	{
		if(!iq.rString("start()")) return false;
		if(l!=null){
			if(l!=null)
			   l.startReceiveData();
		}
		return true;
	}

	public boolean parseFileTransferPut(FileTransferFrame l)
	{
		if(!iq.rString("put(")) return false;
		if(iq.rString("binary")){
			if(l!=null){
				   l.receiveData(this.binary);
			}						
	   		if(!iq.rString(")")) return false;
		}
		else{
    		StringBuffer x=this.iq.rStrConst();
    		if(!iq.rString(")")) return false;
//		int i=c.intValue();
    		if(l!=null){
		    	   l.receiveData(x.toString());
		    }
		}
		return true;
	}

	public boolean parseDataTransfer(FileTransferFrame l)
	{
		parseB();
		if(!iq.rString("send.")) return false;
		if(parseFileTransferStart(l) ||
			parseFileTransferPut(l)  ||
			parseFileTransferEnd(l)
		 )
		 return true;
		 else return false;
	}

	public boolean parseParts(FileTransferFrame l)
	{
//		if(!(l.isControlledByLocalUser())) return true;
		boolean rtn;
		if( parseButtonEvent(l)     ||
			parseTextEvent(l)       ||
			super.parseScrollBarEvent(l)
		  )
			 rtn= true;
		else rtn= false;
		return rtn;
	}

/*
	public boolean parseFileCopy(FileTransferFrame gui)
	{
		parseB();
		if(!iq.rString("transfer.")) return false;
		//
		if(parseParts(gui)) return true;
		return false;
	 }
*/
	public boolean parseEvent()
	{
		boolean rtn;
		if(parseDataTransfer(gui)||parseParts(gui))
				rtn= true;
		else rtn= false;
		return rtn;		
	}

	public void run()
	{
		 super.run();
	}

	FileTransferFrame gui;

	public ParseFileTransferEvent(FileTransferFrame f, InputQueue q)
	{
		iq=q;
		gui=f;
		me=null;
//		  start();
	}

}
