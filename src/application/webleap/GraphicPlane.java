package application.webleap;
import application.draw.GraphicsPrimitives;
public class GraphicPlane extends javax.swing.JComponent  
{

    public GraphicPlane()
    {
        init();
    }
    public void init()
    {
        glist=new GraphicsPrimitiveList();
    }
    public void add(GraphicsPrimitives x)
    {
        glist.add(x);
    }
    public GraphicsPrimitiveList glist;
}

