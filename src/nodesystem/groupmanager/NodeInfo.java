package nodesystem.groupmanager;
public class NodeInfo extends java.lang.Object
{

    public void setAbleToHaveChild(boolean b)
    {
        ableToHaveChild=b;
    }

    public boolean isAbleToHaveChild()
    {
        return ableToHaveChild ;
    }

    public boolean ableToHaveChild;

    public NodeInfo(String iaddr,int port,int sno)
    {
        inetAddr=iaddr;
        this.port=port;
        serialNo=sno;
        ableToHaveChild=false;
    }
    public void positionFromSerialNo()
    {
//        depth=(int)(Math.log(sequenceNumber)/Math.log(2.0));
//        numberFromLeft=sequenceNumber-(int)(Math.exp(depth*Math.log(2.0)));
    }
    public int depth;
    public int serialNo;
    public boolean eqInfo(NodeInfo info)
    {
        if(port!=info.port) return false;
        if(inetAddr.equals(info.inetAddr)) return true;
        return false;
    }
    public int port;
    public String inetAddr;
}

