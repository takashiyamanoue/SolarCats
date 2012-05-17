package nodesystem.groupmanager;

import nodesystem.MessageGui;


/*
 * 
 * @author yamachan
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
public class TreeManager extends java.lang.Object
{
    public GroupManager groupManager;
    boolean thereIsTeacher=false;
    public boolean isThereTeacher(){
    	return thereIsTeacher;
    }
    public void setTeacherIs(boolean v){
    	thereIsTeacher=v;
    }
/*
 * portMode : "floating" or "<portNo>"...<portNo> is an integer which represents the port;
*/
	public void setPortMode(String mode){
		this.portMode=mode;
		if(!portMode.equals("floating")){
			this.currentPort=(new Integer(portMode)).intValue();
		}
	}

    public int getNextSerialNumber()
    {
        return 0;
    }

    public void setSuccessorFlag(BinaryTreeNode p,boolean torf)
    {
        if(p==null) return;
        p.nodeInfo.setAbleToHaveChild(torf);
        setSuccessorFlag(p.leftNode,torf);
        setSuccessorFlag(p.rightNode,torf);
    }

//    public int serialNo;
   private boolean lockFlag;
    public synchronized void lockConnection()
    {
         while(lockFlag){
            try{Thread.sleep(20);}
            catch(InterruptedException e){}
        }
        lockFlag=true;
        
   }
    public void unlockConnection()
    {
        this.lockFlag=false;
    }
    public int upPort(BinaryTreeNode node)
    {
        if(node.upNode==null) return -1;
        return node.upNode.nodeInfo.port;
    }
    public int currentPort;
    
    public BinaryTreeNode getLastNode(){
    	return this.last;
    }
    
    public BinaryTreeNode takeAwayTheLastNode(){
    	BinaryTreeNode n=this.getLastNode();
    	BinaryTreeNode p=n.previous;
    	if(p!=null) {
        	p.next=null;
    	}
       	n.previous=null;
    	BinaryTreeNode up=n.upNode;
    	if(up!=null){
    	   if(up.leftNode==n){
    		   up.leftNode=null;
    	   }
    	   else
    	   if(up.rightNode==n){
    		   up.rightNode=null;
    	   }
    	}
    	this.groupManager.decreaseSerialNumber();
    	this.last=p;
        return n;	
    }

    public synchronized boolean delNode(BinaryTreeNode p)
    {
        BinaryTreeNode ux=p.upNode;
        String lOrR;
//
       if(addable==p) addable=p.previous;
       if(p.next!=null) {
          p.next.previous=p.previous;
          p.previous.next=p.next;
       }
       else{
          p.previous.next=null;
          last=p.previous;
       }
       
        p.leftNode=null;
        p.rightNode=null;
        if(ux.leftNode==p){
            ux.leftNode=null;
            return true;
        }
        if(ux.rightNode==p){
            ux.rightNode=null;
            return true;
        }
        return false;
/*
        //
        if(ux==null){
            root=p.leftNode;
            if(root!=null) root.upNode=null;
            if(p.rightNode!=null)  addNewNode(p.rightNode);
            return true;
        }
        if(ux.leftNode==p){
            lOrR="left";
            p.upNode.leftNode=p.leftNode;
            if(p.rightNode!=null) addNewNode(p.rightNode);
            return true;
        }
        else
        if(ux.rightNode==p)
        {
            lOrR="right";
            p.upNode.rightNode=p.leftNode;
            if(p.rightNode!=null) addNewNode(p.rightNode);
            return true;
        }
        else return false;
        */
    }
    public synchronized BinaryTreeNode searchNode(NodeInfo info)
    {
        BinaryTreeNode rtn,p;
        p=root;
        while(p!=null){
           if(p.nodeInfo.eqInfo(info)) return p;
           p=p.next;
           /*
           if(leftNode!=null){
               rtn=leftNode.searchNode(info);
               if(rtn!=null) return rtn;
           }
           if(rightNode!=null){
               rtn=rightNode.searchNode(info);
               if(rtn!=null) return rtn;
           }
           return null;
           */

        }
        return null;
    }
    public synchronized BinaryTreeNode searchNode(int serialNo)
    {
        BinaryTreeNode rtn,p;
        p=root;
        while(p!=null){
        	int no=p.nodeInfo.serialNo;
           if(no==serialNo) return p;
           p=p.next;
           /*
           if(leftNode!=null){
               rtn=leftNode.searchNode(info);
               if(rtn!=null) return rtn;
           }
           if(rightNode!=null){
               rtn=rightNode.searchNode(info);
               if(rtn!=null) return rtn;
           }
           return null;
           */

        }
        return null;
    }
    public void init()
    {
        root=null;
        last=null;
        addable=null;
        currentPort=27182;
//        serialNo=1;
		this.portMode="floating";
    }
    public TreeManager(GroupManager m)
    {
        this.groupManager=m;
        init();
    }
       public synchronized boolean addNewNode(BinaryTreeNode node)
    {
        /*
        node.nodeInfo.serialNo=serialNo;
        serialNo++;
        */
        node.nodeInfo.serialNo=this.groupManager.getNewSerialNumber();
        if(node.nodeInfo.port==0){
            node.nodeInfo.port=currentPort;
            node.nodeInfo.setAbleToHaveChild(true);
            if(this.portMode.equals("floating")){
				currentPort++;
            }
        }
        //
        if(root==null){
            root=node;
            addable=node;
            last=node;
            node.leftNode=null;
            node.rightNode=null;
            return true;
        }
        if(node.previous==null) {
            node.previous=last;
            last.next=node;
            last=node;
            node.next=null;
        }
        BinaryTreeNode p=root;
        while(p!=null){
            if(p.nodeInfo.isAbleToHaveChild()){
              if(p.addLeft(node)) {
//                  addable=p;
                  return true;
              }
              if(p.addRight(node)) {
//                  addable=p;
                  return true;
              }
            }
            p=p.next;
        }
        return false;

    }

    public BinaryTreeNode addable;
    public BinaryTreeNode last;
    public BinaryTreeNode root;
    public String portMode="floating";
    public void showCurrentConnection(MessageGui gui){
    	if(gui==null) return;
    	BinaryTreeNode p=this.root;
    	while(p!=null){
    		p.printStatus(gui);
    		p=p.next;
    	}
    	gui.appendMessage("------------------\n");
    }

}

