/*
NetPaint

     by T. Yamanoue,
     Kyushu Institute of Technology, Japan,
     Aug.1, 1997

 
   A Paint tool for the Internet.
   
   Drawing tool on a Web brouser.
   A Co-operative drawing tool.
   Drawing a paint on the Internet by linking parts
   
   
*/
package application.draw;
class PointList extends java.lang.Object
{
    public PointList()
    {
    }

    public int size()
    {
        int i=0;
        PointList p;
        p=this;
        while(p!=null) { p=p.next; i++;}
        return i;
    }

    public int y;
    public int x;
    public PointList next;
}

