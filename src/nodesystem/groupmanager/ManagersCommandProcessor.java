package nodesystem.groupmanager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import util.TraceSW;

import nodesystem.AMessage;
import nodesystem.Client;
import nodesystem.MessageGui;
import nodesystem.StringIO;
public class ManagersCommandProcessor extends Client
{
    public Vector cqueue;
    GroupManager gui;
    TraceSW tracing;

    public void start()
    {
        if(this.me==null){
            me=new Thread(this,"ChildProc2");
            me.start();
        }
    }

    public void stop()
    {
        if(me!=null){
            me=null;
        }
    }

    public void run()
    {
        while(me!=null){
            try{
            	AMessage m=this.sio.readString();
               this.command=m.getHead();
            }
            catch(IOException e){
            	gui.appendMessage(""+e.toString());
                this.close();
                this.stop();
                me=null;
                return;
            }
            String raddr=this.sio.getRemoteAddress();
            String rport=this.sio.getRemotePort();
            gui.appendMessage("from "+raddr+":"+rport+" "+command+"\n");
            this.queue.addRequest(this);
        }
    }


    public void close()
    {
        try{Thread.sleep(100);} catch(InterruptedException e){}
        if(this.sio!=null){
        	this.gui.appendMessage("close "+
        			sio.getRemoteAddress()+":"+sio.getRemotePort()+"\n");
          try{
               sio.in.close();
               sio.out.close();
          }
          catch(IOException e){
          }
        }
        if(this.sock!=null){
            try{
                sock.close();
            }
            catch(IOException e){
            }
        }
        this.stop();
    }


    public BinaryTreeNode pendingNode;

    public ManagersProcessQueue queue;

    public StringIO sio;

    public String command;

    public InetAddress ria;

    public synchronized void processTheRequest()
    {
//    	System.out.println(command);
    	gui.appendMessage("processing the '"+command+"'...\n");
        if(command==null) {
            gui.appendMessage("i/o stream closing.\n");
            this.close();
            gui.appendMessage("end of closing.\n");
            return;
        }        
        if(command.indexOf("addnode-teacher")==0){
        	if(!treeManager.isThereTeacher()){
                NodeInfo ninfo=new NodeInfo(ria.toString(),0,0);
                this.pendingNode=new BinaryTreeNode(ninfo);
                addNode(this.pendingNode);
                returnTime(ria,sio);
                try{
                	String ack=(this.sio.readString()).getHead();
                	this.gui.appendMessage(ack+" from "+sio.getRemoteAddress()
                			+":"+sio.getRemotePort()+"\n");
                }
                catch(Exception e){
                	this.gui.appendMessage(e.toString());
                }
                pendingNode.nodeInfo.setAbleToHaveChild(true);
                treeManager.setTeacherIs(true);
                return;
        	}
        	else{
            	this.gui.appendMessage("none to "+sio.getRemoteAddress()
            			+":"+sio.getRemotePort()+"\n");
        		try{
				  sio.writeString("none");
        		}
        		catch(IOException e){
					this.gui.appendMessage(e.toString()+" while adding root(/leaf) node.+\n");
        		}
        	}
        	return;
        	
        }
        else
		if(command.indexOf("addnode-student")==0){
			NodeInfo ninfo=new NodeInfo(ria.toString(),0,0);
			this.pendingNode=new BinaryTreeNode(ninfo);
			addNode(this.pendingNode);
			returnTime(ria,sio);
            try{
            	String ack=(this.sio.readString()).getHead();
            	this.gui.appendMessage(ack+" from "+sio.getRemoteAddress()
            			+":"+sio.getRemotePort()+"\n");
            }
            catch(Exception e){
            	this.gui.appendMessage(e.toString());
            }
			pendingNode.nodeInfo.setAbleToHaveChild(true);
			return;
		}
		else
        if(command.indexOf("delnode ")==0){
        	this.delNode(command);
            return;
        }
        else
        if(command.indexOf("gettime")==0){
            String stime=gui.nodeControl("groupControl returnTime");
            gui.appendMessage("time is requested\n");
            gui.appendMessage(stime+" was returned.\n");
//            returnTime(ria,sio);
            return;
        }
        else
        if(command.indexOf("getSerialNo")==0){
            String sno=gui.nodeControl("groupControl getSerialNo");
            gui.appendMessage("serialNo is requested from "+
            		sio.getRemoteAddress()+":"+sio.getRemotePort()+"\n");
            try{
            sio.writeString(sno);
            }
            catch(IOException e){}
            gui.appendMessage(sno+" was returned.\n");
//            returnSerialNo(ria,sio);
            return;
        }
        else
        if(command.indexOf("getNodeNumber")==0){
        	int x=this.gui.getSerialNumber();
        	try{
        		sio.writeString(""+x);
        	}
        	catch(IOException e){}
        	gui.appendMessage(""+x+" was returned as a node number in the group.");
        }
        else
        if(command.indexOf("getRoot")==0){
            String rootAddr=gui.nodeControl("groupControl getRoot");
            this.gui.appendMessage("getRoot is requested rootAddr="+rootAddr+"\n");
            try{
            sio.writeString(rootAddr);
            }
            catch(IOException e){}
            gui.appendMessage(rootAddr + " was returned.\n");
            returnTime(ria,sio);
            return;
        }
        else
        if(command.indexOf("close")==0){   
            gui.appendMessage("i/o stream closing.\n");
            this.close();
            gui.appendMessage("end of closing.\n");
//        this.treeManager.unlockConnection();
            return;
        }
        else
        if(command.indexOf("delupper ")==0){
        	deleteUpperNode(command);
        	return;
        }
        else
        if(command.indexOf("reset")==0){
        	this.gui.initButton_actionPerformed(null);
        	try{ 
        		sio.writeString("done"); 
//        		sio.close();
        	}
        	catch(Exception e){
        		gui.appendMessage(""+e.toString()+"\n");
        	}
        	return;
        }
    }

    public void deleteUpperNode(String command){
        StringTokenizer st=new StringTokenizer(command," ");
        String c=st.nextToken();
        String sno=st.nextToken();
		gui.appendMessage("fixing the node "+sno+"'s mother's connection.\n"); 
    	int serialNo=(new Integer(sno)).intValue();
    	BinaryTreeNode myNode=this.treeManager.searchNode(serialNo);
    	if(myNode==null) {
       		gui.appendMessage("this node("+sno+") isn't registerd.");
    		return;
    	}
    	BinaryTreeNode motherNode=myNode.upNode;
    	if(motherNode==null) {
    		gui.appendMessage("can't fix because the mother node is the root node.");
    		return;
    	}
    	if(motherNode.leftNode==myNode){
        	this.delNode(motherNode.nodeInfo.serialNo);
        	return;
    	}
    	if(motherNode.rightNode==myNode){
    		this.delNode(motherNode.nodeInfo.serialNo);
    	}
    }
    
    public void closeTheConnection(BinaryTreeNode from,String direction)
    {
    	String fromAddr=from.nodeInfo.inetAddr;
		StringTokenizer st=new StringTokenizer(fromAddr,"/");
		String fromName=st.nextToken();
		String fromIaddr="";
		if(st.hasMoreTokens())
    		fromIaddr=st.nextToken();
        else
            fromIaddr=fromName;
		int fromPort=from.nodeInfo.port;
		/*
		gui.appendMessage("deleting the connection from "+fromIaddr+" to "+direction+"\n");
        StringIO sio=new StringIO(fromIaddr,fromPort);
        if(!sio.isConnected())return;
        */
        /*
          node control command

          nodeControl delnode upper|left|right

        */
		/*
        try{
           sio.writeString("nodeControl discon "+direction);
           //
           sio.close();
        }
        catch(IOException e){}
        gui.appendMessage("end of redirection\n");
        */
    }
    
    public void returnTime(InetAddress ria, StringIO sio)
    {
        String stime=gui.nodeControl("groupControl returnTime");
        gui.appendMessage("time is requested\n");
        try{
        sio.writeString(stime);
        }
        catch(IOException e){}
        gui.appendMessage(stime+" was returned.\n");
    }
    

    
    public void redirectNode(BinaryTreeNode from,
    		BinaryTreeNode to)
   {
    	if(from==null) return;
    	String fromAddr=from.nodeInfo.inetAddr;
		StringTokenizer st=new StringTokenizer(fromAddr,"/");
		String fromName=st.nextToken();
		String fromIaddr="";
		if(st.hasMoreTokens())
    		fromIaddr=st.nextToken();
        else
            fromIaddr=fromName;
		int fromPort=from.nodeInfo.port;

		if(to==null){
			return;
		}
		
    	String toAddr=to.nodeInfo.inetAddr;
		st=new StringTokenizer(toAddr,"/");
		String toName=st.nextToken();
		String toIaddr="";
		if(st.hasMoreTokens())
    		toIaddr=st.nextToken();
        else
            toIaddr=toName;
		int toPort=to.nodeInfo.port;

        gui.appendMessage("redirecting "+fromIaddr+":"+fromPort+
                 " to "+toIaddr+":"+toPort+"\n");
        StringIO sio=new StringIO(fromIaddr,fromPort,tracing);
        /*
           node control command
            nodeControl redirect <new up node> <new up port>
        */
        try{
          sio.writeString("nodeControl redirect "+toIaddr+" "+toPort);
          sio.close();
        }
        catch(IOException e){}
        gui.appendMessage("end of redirection\n");

    }

    public void setSerialNoToTheNode(BinaryTreeNode n, int no){
    	String addr=n.nodeInfo.inetAddr;
		StringTokenizer st=new StringTokenizer(addr,"/");
		String name=st.nextToken();
		String iaddr="";
		if(st.hasMoreTokens())
    		iaddr=st.nextToken();
        else
            iaddr=name;
		int port=n.nodeInfo.port;

        gui.appendMessage("setting Sirial no. "+no+" to the "+addr+"\n");
        StringIO sio=new StringIO(iaddr,port,tracing);
        /*
           node control command
            nodeControl setserial <no>
        */
        try{
          sio.writeString("nodeControl setserial "+no);
          sio.close();
        }
        catch(IOException e){}
        gui.appendMessage("end of set serial no.\n");

    	
    }
    
    public void delNode(String command){
        StringTokenizer st=new StringTokenizer(command," ");
        String c=st.nextToken();
        String sno=st.nextToken();
    	int serialNo=(new Integer(sno)).intValue();
    	this.delNode(serialNo);
    }
    
    public void delNode(int serialNo){
        /*         
             A
            / \
           /   \
          /     \
         /       \
   -->   B       C
        / \     / \
       /   \   /   \
      /     \ /     \
     D      E F     G
     / \    / / \   / \

     When node B is quitting from the group           

             A
            / \
           /   \
          /     \
         /       \
   -->   X       C
        / \     / \
       /   \   /   \
      /     \ /     \
     D      E F     G
     / \    / / \   / \

     Replace B with X which is the last node in the group

*/
		gui.appendMessage("delete the node "+serialNo+".\n"); 
    	BinaryTreeNode motherNode=this.treeManager.searchNode(serialNo);
    	//motherNode is the deleting node.
    	if(motherNode==null){
			gui.appendMessage("can't fix because the node is not registerd.\n");
			return;
    	}
    	if(motherNode==treeManager.root){
    		gui.appendMessage("no node in the group.\n");
    		gui.nodeControl("groupControl init");
    		return;
    	}
   		BinaryTreeNode youngerSisterNode=motherNode.rightNode;
   		BinaryTreeNode elderSisterNode=motherNode.leftNode;
   		BinaryTreeNode grandMotherNode=motherNode.upNode;
   		if(grandMotherNode==null) {
    			gui.appendMessage("can't fix because her Mother is the root.\n");
    			return;
    		}
    	BinaryTreeNode newMotherNode=this.treeManager.takeAwayTheLastNode();
    	if(newMotherNode==null) {
    		gui.appendMessage("no node in the group.\n");
    		this.gui.nodeControl("groupControl init");
    		return;
    	}
//    	this.closeTheConnection(motherNode,"upNode");
        ClosingTheNodeConnection nc=new ClosingTheNodeConnection(motherNode,"upper");
    	if(newMotherNode==motherNode) return;
    	if(grandMotherNode.leftNode==motherNode){
    			grandMotherNode.leftNode=newMotherNode;
//    			this.closeTheConnection(grandMotherNode,"left");
    	}
    	else
    	if(grandMotherNode.rightNode==motherNode){
    	    	grandMotherNode.rightNode=newMotherNode;
//    	    	this.closeTheConnection(grandMotherNode,"right");
    	}
        newMotherNode.upNode=grandMotherNode;
		BinaryTreeNode p=motherNode.previous;
		BinaryTreeNode n=motherNode.next;
		p.next=newMotherNode;
		newMotherNode.previous=p;
		if(n!=null)
    		n.previous=newMotherNode;
		newMotherNode.next=n;
    	newMotherNode.nodeInfo.serialNo=serialNo;
    	if(newMotherNode!=elderSisterNode){
            newMotherNode.leftNode=elderSisterNode;
            if(elderSisterNode!=null){
               elderSisterNode.upNode=newMotherNode;
    	       this.redirectNode(elderSisterNode, newMotherNode);
            }
    	}
    	if(newMotherNode!=youngerSisterNode){
            newMotherNode.rightNode=youngerSisterNode;
            if(youngerSisterNode!=null){
               youngerSisterNode.upNode=newMotherNode;
        	   this.redirectNode(youngerSisterNode, newMotherNode);
            }
    	}
   	    this.redirectNode(newMotherNode,grandMotherNode);
        this.setSerialNoToTheNode(newMotherNode,newMotherNode.nodeInfo.serialNo);
        this.redirectNode(motherNode,null);
     }
    
    public void addNode(BinaryTreeNode node)
    {
//        NodeInfo ninfo=new NodeInfo(ria.toString(),0,0);
//        BinaryTreeNode node=new BinaryTreeNode(ninfo);
        if(!treeManager.addNewNode(node)) {
            this.pendingNode=node;
            this.command="addnode";
            queue.addRequest(this);
            return;
        }
        int serialNo=node.nodeInfo.serialNo;
        int xport=node.nodeInfo.port;
        gui.appendMessage("port="+xport+"\n");
        gui.appendMessage("new serialNo="+serialNo+"\n");
        if(node.upNode==null){
            // the first node in this group
            
            gui.appendMessage("root node added\n");
            if(this.treeManager.groupManager.upperSio!=null){
                // add a leaf node to the root node of the upper group
                
                String uRoot=this.treeManager.groupManager.upperRootAddress;
                int uPort=this.treeManager.groupManager.upperRootPort;
                
                gui.appendMessage("To "+sio.getRemoteAddress()+":"+
                		   sio.getRemotePort()+" leaf node added. up="+uRoot+":"+uPort+
                		   ", xport:"+xport+", serialNo:"+serialNo+"\n");
                try{
                  sio.writeString("leaf");
                  sio.writeString("up:");
                  sio.writeString(uRoot);
                  sio.writeString(""+uPort);
                  sio.writeString(""+xport);
                  sio.writeString(""+serialNo);
                }
                catch(IOException e){
                    System.out.println(e.toString()+" while adding root(/leaf) node.");
                }
            }
            else{
                gui.appendMessage("To "+sio.getRemoteAddress()+":"+
             		   sio.getRemotePort()+" root node added. xport:"+xport+", serialNo:"+serialNo+"\n");
                try{
                  sio.writeString("root");
                  sio.writeString(""+xport);
                  sio.writeString(""+serialNo);
                }
                catch(IOException e){
                    System.out.println(e.toString()+" while adding root node.");
                }
            }
        }
        else{
//            gui.appendMessage("leaf node added\n");
            gui.appendMessage("To "+sio.getRemoteAddress()+":"+
         		   sio.getRemotePort()+" leaf node added. up="
         		     +node.upNode.nodeInfo.inetAddr+
         		   ":"+node.upNode.nodeInfo.port+
         		   ", xport:"+xport+", serialNo:"+serialNo+"\n");
            try{
            sio.writeString("leaf");
            sio.writeString("up:");
            sio.writeString(node.upNode.nodeInfo.inetAddr);
            sio.writeString(""+node.upNode.nodeInfo.port);
            sio.writeString(""+xport);
            sio.writeString(""+serialNo);
            }
            catch(IOException e){
                System.out.println(e.toString()+" while adding leaf node.");
            }
        }
//
    }
    public TreeManager treeManager;
    public ManagersCommandProcessor( Socket ns, 
//             ServerSocket ss,
             GroupManager g,TreeManager tm,ManagersProcessQueue q,TraceSW tr)
    {
        /*
        
        */
        init();
        this.tracing=tr;
        gui=g;   
        sock=ns;
        treeManager=tm;
        queue=q;
        ria=sock.getInetAddress();
        cqueue=new Vector();
        int rport=sock.getPort();
        gui.appendMessage("Connected from "+ria.toString()+":"+rport+"\n");
        sio=new StringIO(sock,tracing);
//        sio.setTracing(true);
//        this.start();
        try{
        	if(this.queue.getSize()>5){
                gui.appendMessage("To "+sio.getRemoteAddress()+":"+
              		   sio.getRemotePort()+" wait.\n");
        		this.sio.writeString("wait");
        	}
        	else{
                gui.appendMessage("To "+sio.getRemoteAddress()+":"+
               		   sio.getRemotePort()+" no-wait.\n");
        		this.sio.writeString("no-wait");
        	}
        }
        catch(IOException e){}
        this.getAndSubmit();
	}
    public void getAndSubmit(){
        try{
        	AMessage m=this.sio.readString();
           this.command=m.getHead();
        }
        catch(IOException e){
            this.close();
            this.stop();
            me=null;
            return;
        }
        String raddr=this.sio.getRemoteAddress();
        String rport=this.sio.getRemotePort();
        gui.appendMessage("from "+raddr+":"+rport+" "+command+"\n");
        this.queue.addRequest(this);    	
    }
    public void comment()
    {
        /*
        1. receive requests from CommunicationNodes and manage the group of
           the nodes. Nodes of the group are connected to form a tree structure.
        
        2. receive requests from GroupManagers and connect the groups of the nodes.
        
        Each request is the one of the followings
        
           - addnode
           - delnode
           - gettime
           - 
        
        
        1. receive the node information
        2. construct the tree (system structure) infomation
        3. send the connection infomation to the node.
        */
    }
	//{{DECLARE_CONTROLS
	//}}
    class ClosingTheNodeConnection
    implements Runnable
    {
    	String fromName;
    	String fromAddr;
    	String fromIaddr;
    	String direction;
    	int fromPort;
    	Thread me;
    	public void run(){
    	   while(me!=null){	
               StringIO sio=new StringIO(fromIaddr,fromPort,tracing);
               if(!sio.isConnected()){
            	   gui.appendMessage("can't connect to "+fromIaddr+"\n");
            	   me=null;
            	   return;
               }
               /*
               node control command

               nodeControl discon upper|left|right

             */
     		 gui.appendMessage("from:"+fromIaddr+":"+fromPort);
             try{
                sio.writeString("nodeControl discon "+direction);
                //
                sio.close();
             }
             catch(IOException e){}
             gui.appendMessage("end of redirection\n");
             me=null;
    		 return;
    	   }
    	}
    	public ClosingTheNodeConnection(BinaryTreeNode from, String d){
        	fromAddr=from.nodeInfo.inetAddr;
    		StringTokenizer st=new StringTokenizer(fromAddr,"/");
    		fromName=st.nextToken();
    		fromIaddr="";
    		this.direction=d;
    		if(st.hasMoreTokens())
        		fromIaddr=st.nextToken();
            else
                fromIaddr=fromName;
    		int fromPort=from.nodeInfo.port;
    		gui.appendMessage("deleting the connection from "+fromIaddr+" to "+direction+"\n");
    		this.start();
    	}
    	public void start(){
    		if(me==null){
    			me=new Thread(this);
    			me.start();
    		}
    	}
    	public void stop(){
    		me=null;
    	}
    }
}

