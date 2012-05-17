package nodesystem.groupmanager;

import nodesystem.MulticastSender;


public class MulticastDistributor extends nodesystem.Distributor
{
    public MulticastSender mcastSender;

    public void setMulticastAddressPort(String addr, int port)
    {
        this.mcastSender=new MulticastSender(addr,port);
    }

    public void putS(String x, int myID)
    {
        this.mcastSender.sendMessage(x);
    }

	public MulticastDistributor(String code, int sm)
	{
		super(code, sm);
	}
}