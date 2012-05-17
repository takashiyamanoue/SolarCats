package nodesystem.eventrecorder;

public class BlockToText extends java.lang.Object
{
    private BlockedFileManager bf;

    public void main(String arg[])
    {
        BlockToText btt=new BlockToText(arg[0]);
        int max=btt.bf.getMaxIndex();
        for(int i=0;i<max;i++){
            System.out.println(btt.bf.getMessageAt(i).getHead());
        }
    }

	public BlockToText(String dir)
	{
	    bf=new BlockedFileManager(dir,"sjis");
	}
}