package application.networktester;
import java.util.Random;
import java.util.Vector;
public class RandomSequence
{
    public void sortMe1()
    {
        int max=n/2;
        for(int i=0;i<max-1;i++){
            for(int j=i+1;j<max;j++){
                if(numbers[i*2]>numbers[j*2]){
                    int w=numbers[i*2];
                    numbers[i*2]=numbers[j*2];
                    numbers[j*2]=w;
                    w=numbers[i*2+1];
                    numbers[i*2+1]=numbers[j*2+1];
                    numbers[j*2+1]=w;
                }
            }
        }
    }

    public int nextNumber()
    {
        int rtn=numbers[numIndex];
        numIndex++;
        return rtn;
    }

    public int n;

    public boolean equals(RandomSequence rs)
    {
        if(n!=rs.n) return false;
        for(int i=0;i<n;i++){
            if(numbers[i]!=rs.numbers[i]) return false;
        }
        return true;
    }


    public int numIndex;

    public int[] numbers;

    public boolean hasMore()
    {
        return numIndex<n;
    }

    public Random random;

    public RandomSequence(int n, int seed)
    {
        random=new Random((long)seed);
        Vector tempNumbers=new Vector();
        for(int i=0;i<n;i++){tempNumbers.addElement((new Integer(i)));}
        for(int i=0;i<n;i++){
            double rx=random.nextDouble();
            int ix=(int)(rx*(n-i));
            numbers[i]=((Integer)(tempNumbers.elementAt(ix))).intValue();
            tempNumbers.removeElementAt(ix);            
        }
        numIndex=0;
        this.n=n;
    }

}