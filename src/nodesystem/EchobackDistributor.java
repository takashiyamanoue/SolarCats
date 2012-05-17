package nodesystem;

import java.io.IOException;

public class EchobackDistributor extends nodesystem.Distributor
{
    public synchronized void putS(String s,int myID)
    {
//        DataOutputStream o=null;
        StringIO o=null;
        for(int j=0;j<smax;j++){
//              if(j!=myID){
                    o=streams[j];
                    if(o!=null){
                        try{
                           o.writeString(s);
                        }
                        catch(IOException e){
                            System.out.println(e.toString()
                               +" while distributor.puts(s,id)");
                        }
                        
                    }
                }
//          }
    }

	public EchobackDistributor(String code, int sm)
	{
		super(code, sm);
	}
}