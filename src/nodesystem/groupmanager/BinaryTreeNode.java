package nodesystem.groupmanager;

import nodesystem.MessageGui;

public class BinaryTreeNode extends java.lang.Object
{

    public BinaryTreeNode last;
    public BinaryTreeNode previous;
    public BinaryTreeNode toTop;
    public BinaryTreeNode root()
    {
        if(upNode==null) return this;
        return upNode.root();
    }
    public boolean addRight(BinaryTreeNode node)
    {
        if(rightNode!=null) return false;
        node.upNode=this;
        rightNode=node;
        return true;
    }
    public boolean addLeft(BinaryTreeNode node)
    {
        if(leftNode!=null) return false;
        node.upNode=this;
        leftNode=node;
        return true;
    }
    public void init()
    {
        upNode=null;
        rightNode=null;
        leftNode=null;
        next=null;
    }
    public BinaryTreeNode(NodeInfo info)
    {
        nodeInfo=info;
        init();
    }
    public BinaryTreeNode()
    {
        init();
    }
    public NodeInfo nodeInfo;
    public BinaryTreeNode next;
    public BinaryTreeNode rightNode;
    public BinaryTreeNode leftNode;
    public BinaryTreeNode upNode;
    public void printStatus(MessageGui gui){
    	gui.appendMessage("node-"+this.nodeInfo.serialNo);
    	gui.appendMessage("...(address="+this.nodeInfo.inetAddr);
    	gui.appendMessage(",port="+this.nodeInfo.port+")\n");
    	if(this.upNode==null)
    		gui.appendMessage(" up-null");
    	else
    		gui.appendMessage(" up-"+this.upNode.nodeInfo.serialNo);
    	if(this.leftNode==null)
    		gui.appendMessage(" left-null");
    	else
    		gui.appendMessage(" left-"+this.leftNode.nodeInfo.serialNo);
    	if(this.rightNode==null)
    		gui.appendMessage(" right-null\n");
    	else
    		gui.appendMessage(" right-"+this.rightNode.nodeInfo.serialNo+"\n");
    	if(this.previous==null)
    		gui.appendMessage(" *previous-null");
    	else
    		gui.appendMessage(" *previous-"+this.previous.nodeInfo.serialNo);
    	if(this.next==null)
    		gui.appendMessage(" *next-null\n");
    	else
    		gui.appendMessage(" *next-"+this.next.nodeInfo.serialNo+"\n");
  
    }
}

