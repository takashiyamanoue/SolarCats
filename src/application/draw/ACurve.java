package application.draw;

import java.awt.Color;
import java.awt.Graphics;

import nodesystem.TemporalyText;

public class ACurve extends ALines
{
    public void mouseExitAtTheText(int id, int x, int y)
    {
    }

    public boolean save(TemporalyText outS)
    {

        PointList p;
        p=plist;
        if(!saveHeader(outS,"curve")) return false;
        if(!savePoints(outS)) return false;
        return true;
    }

    public AFigure copyThis()
    {
        ACurve f=(ACurve)(copyThis2(new ACurve(canvas)));
  //      f.pgon=pgon;
        return (AFigure)f;
    }

    public ACurve()
    {
        plist=null;
        init();
    
		//{{INIT_CONTROLS
		//}}
	}

    public ACurve(FigCanvas c)
    {
        canvas=c;
        init();
    }


    public void drawTemp(Graphics g)
    {
        PointList pt;
 //       writeMessage("ALines.drawTemp\n");
        drawCurve(g);
        drawLineW(g,lastXY.x+offX,lastXY.y+offY,offX+x2,offY+y2,lineWidth);
        showEdges(g);

        return;

    }


    public void draw(Graphics g)
    {
        Color cc=g.getColor();
        g.setColor(color);
        if(isEditing()) drawTemp(g);
        else drawCurve(g);
        g.setColor(cc);
    }


double poly(int n, double a[], double x)
{
  int i;
  double y;
  y=0;
  for(i=0;i<n;i++) y=y*x+a[i];
  return y;
} 

public void curveform(int n, double x[], double a[])
{
   double A[][]=new double[n][n+1];
   int i,j;
   double y,z;
   for(i=0;i<n;i++) {
     for(j=0;j<n;j++) {
           y=(double)(n-1-i);
           z=(double)(n-1-j);
           if(z==0.0) A[i][j]=1.0;
           else if(y==0.0) A[i][j]=0.0;
           else A[i][j]=Math.pow(y,z);
     }
     A[i][n]=x[i];
   }
   gauss(n,A);
   for(i=0;i<n;i++) a[i]=A[i][n];
}

public void curve(Graphics g,int n, double x[], double y[],int from, int to)
{
   double a[]=new double[20];
   double b[]=new double[20];
   double t,xt0,yt0,xt1,yt1;
   int i;
   
   curveform(n,x,a);
   curveform(n,y,b);
   for(t=(double)from;t<(double)to;t+=0.1){
     xt0=poly(n,a,t);     yt0=poly(n,b,t);
     xt1=poly(n,a,t+0.1); yt1=poly(n,b,t+0.1);
     if(lineWidth>1){
       this.drawLineW( g, (int)xt0, (int)yt0, (int)xt1, (int)yt1,this.lineWidth);
       g.fillOval(xView((int)(xt1-lineWidth/2.0)),yView((int)(yt1-lineWidth/2.0)),
                         lineWidth, lineWidth);
     }
     else
     {
        g.drawLine((int)xt0,(int)yt0,(int)xt1,(int)yt1);
     }
  }
}

    public void gauss(int n, double a[][])
    {
        int i,j,k;
        double p;
        for(i=0;i<n;i++){
            p=a[i][i];
            for(k=i;k<n+1;k++) a[i][k]=a[i][k]/p;
            for(j=0;j<n;j++){
                if(i!=j){
                    p=a[j][i];
                    for(k=i;k<n+1;k++) a[j][k]=a[j][k]-p*a[i][k];
                }
            }
        }
    }

    public void drawCurve(Graphics g)
    {
        PointList pt;
        int x1,y1,x2,y2,i;
        double x[]=new double[10];
        double y[]=new double[10];
        pt=plist;
        if(pt==null) return;
        int size=plist.size();
        x1=0;y1=0;i=0;
        
        if(lineWidth>1)
              g.fillOval(xView(offX-lineWidth/2),yView(offY-lineWidth/2),
                         lineWidth, lineWidth);
                         
        if(size==1){
            
               x[i]=(double)(offX); y[i]=(double)(offY);
               i++;
               
               x[i]=(double)(pt.x+offX); y[i]=(double)(pt.y+offY);
               i++;
               pt=pt.next;
                
               if(lineWidth<=1) {
                 g.drawLine(xView((int)(x[0])),yView((int)(y[0])),
                            xView((int)(x[1])),yView((int)(y[1])));
               }
               else
               {
                 drawLineW(g,(int)(x[0]),(int)(y[0]),
                           (int)(x[1]),(int)(y[1]),lineWidth);
                  g.fillOval(xView((int)(x[1])-lineWidth/2),yView((int)(y[1])-lineWidth/2),
                            lineWidth, lineWidth);
               }
               return;
        }
            
        if(size==2){
                x[i]=(double)(offX); y[i]=(double)(offY);
                i++;
               
                x[i]=(double)(pt.x+offX); y[i]=(double)(pt.y+offY);
                i++;
                pt=pt.next;
                
                x[i]=(double)(pt.x+offX); y[i]=(double)(pt.y+offY);
                i++;
                pt=pt.next;
                
                curve(g,3,x,y,0,2);
                return;
        }
            
        if(size==3){
               x[i]=(double)(offX); y[i]=(double)(offY);
               i++;
               
                for(int k=0;k<3;k++){
                   x[i]=(double)(pt.x+offX); y[i]=(double)(pt.y+offY);
                   i++;
                   pt=pt.next;
                }       
                curve(g,4,x,y,0,3);
                return;
         }
            
         if(size>=4){
               x[0]=(double)(offX); y[0]=(double)(offY);
               
               for( i=1;i<4;i++){
                    x[i]=(double)(pt.x+offX); y[i]=(double)(pt.y+offY);
                    pt=pt.next;
                }
                curve(g,4,x,y,1,3);
                
                while(pt!=null){
                    for(int j=0;j<3;j++){
                        x[j]=x[j+1]; y[j]=y[j+1];
                    }
                    x[3]=(double)(pt.x+offX); y[3]=(double)(pt.y+offY);
                    pt=pt.next;
                    curve(g,4,x,y,1,2);
                }
                
                curve(g,4,x,y,0,1);
         }
        return;

    }

	//{{DECLARE_CONTROLS
	//}}
}