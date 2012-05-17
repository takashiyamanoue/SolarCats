package application.webleap;
import java.awt.Graphics;
import java.util.Vector;

import application.draw.GraphicsPrimitives;
public class GraphicsPrimitiveList extends java.lang.Object
{
    public void paint()
    {
        int i;
        if(primitives==null) return;
        int last=primitives.size();
        for(i=0;i<last;i++){
            GraphicsPrimitives
              x=(GraphicsPrimitives)(primitives.elementAt(i));
            x.paint();
        }
    }

    public void mouseDown(int x, int y)
    {
        int i;
        GraphicsPrimitives gp;
        if(primitives==null) return;
        int last=primitives.size();
        for(i=0;i<last;i++){
//            GraphicsPrimitives
              gp=(GraphicsPrimitives)(primitives.elementAt(i));
            gp.mouseDown(x, y);
        }
    }
    public GraphicsPrimitiveList() // void?
    {
        init();
    }
    public void paint(Graphics g)
    {
        int i;
        if(primitives==null) return;
        int last=primitives.size();
        for(i=0;i<last;i++){
            GraphicsPrimitives
              x=(GraphicsPrimitives)(primitives.elementAt(i));
            x.paint(g);
        }
    }
    public void init()
    {
        primitives=new Vector();
    }
    public void add(GraphicsPrimitives x)
    {
        primitives.addElement(x);
    }
    public Vector primitives;
}

