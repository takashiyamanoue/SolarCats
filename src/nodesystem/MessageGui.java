package nodesystem;
public interface MessageGui
{
    public String nextDoorControl(String cmd, Client clt);

    public String nodeControl(String s);
    public void appendMessage(String s);
}

