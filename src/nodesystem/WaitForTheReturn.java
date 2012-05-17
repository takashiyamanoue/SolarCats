package nodesystem;

import java.util.Hashtable;

import controlledparts.ControlledFrame;

public class WaitForTheReturn implements Runnable
{
    Hashtable waitingList;
	Thread me;
	String callCommand;
    String returnMessage=null;
    String actionCommand=null;
    String receiver=null;
    ControlledFrame cf=null;
    ControlledFrame rf=null;
    CommunicationNode cn=null;
    
	public WaitForTheReturn(){
	
	}
	
	public WaitForTheReturn(CommunicationNode n,String to, String command, ControlledFrame r, String action){
		me=null;
		callCommand=command;
		cn=n;
		rf=r;
		this.actionCommand=action;
		cf=cn.lookUp(to);
		System.out.println("waiting "+callCommand);
		this.start();
	}

	public void start(){
		if(me==null){
			me=new Thread(this,"WaitForTheReturn..."+callCommand);
			me.start();
		}
	}
	public void stop(){
		me=null;
	}
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		String rtn=cf.parseCommand(callCommand);
		System.out.println("return "+rtn);
//		rf=cn.lookUp(receiver);
		rf.parseCommand(this.actionCommand+" "+rtn);
		me=null;
	}

}
