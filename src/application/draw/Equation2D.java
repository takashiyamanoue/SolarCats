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
class Equation2D extends java.lang.Object
{
    public Equation2D(double x1,double y1,double x2, double y2)
    {
        ERR=0.0000001;
        this.x1=x1; this.y1=y1; this.x2=x2; this.y2=y2;
    }

    public double vdy()
    {
        double rtn;
        double dx=x2-x1;
        double dy=y2-y1;
        double r=Math.sqrt(dx*dx+dy*dy);
        rtn=dx/r;
        return rtn;
    }


    public double distance;

    public double vdx()
    {
        double rtn;
        double dx=x2-x1;
        double dy=y2-y1;
        double r=Math.sqrt(dx*dx+dy*dy);
        rtn=-dy/r;
        return rtn;
    }

 /*
      vertical half share line

      argument        |
                      |
      (x1,y1)----|----|----|----(x2,y2)
                      |
                      | a*x+b*y=c

      input : x1,y1,x2,y2
      output: a,b,c
*/
public boolean vShareLine()
{
    double de,x1x2,y1y2,x1x1,x2x2,y1y1,y2y2;
    x1x2=(x1-x2)*(x1-x2);
    y1y2=(y1-y2)*(y1-y2);
    de=x1x2+y1y2;
    if(de<ERR) return false;
    else{
      a=x2-x1;
      b=y2-y1;
      x1x1=x1*x1;
      x2x2=x2*x2;
      y1y1=y1*y1;
      y2y2=y2*y2;
      c=(-x1x1+x2x2-y1y1+y2y2)/2.0;
      return true;
    }
}
    public double ERR;
    public Equation2D()
    {
        ERR=0.0000001;
    }
/*
       line equation from two points

       argument

          (x1,y1)------------------(x2,y2)
                  a*x+b*y=c
Š 
       input : x1,y1,x2,y2
       output: a,b,c
*/
public boolean edges2abc()
{   double de;
   de=Math.abs(x1-x2)+Math.abs(y1-y2);
   if(de<ERR) return false;
   else{
     a=y2-y1;
     b=x1-x2;
     c=x1*y2-x2*y1;
     return true;
   }
}
    public double y2;
    public double x2;
    public double y1;
    public double x1;
    public double c;
    public double b;
    public double a;
}

