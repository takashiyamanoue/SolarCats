package nodesystem;
import java.util.Vector;
public class StringQueue
{
    public synchronized void flush()
    {
        while(!(this.isEmpty)){
            try{
                Thread.sleep(50);
            }
            catch(Exception e){}
        }
    }

    public void init()
    {
         head=0;
        tail=0;
        this.unlock();
        this.isEmpty=true;
        this.isFull=false;
   }

    private  void waitUntilUnlocked()
    {
        while(this.isLocked){
            try{
                Thread.sleep(10);
            }
            catch(InterruptedException e){
                System.out.println("exception while locked."+e);
            }
        }
    }

    private boolean isLocked;

    private void waitUntilPut()
    {
        this.unlock();
        while(this.isEmpty){
            try{
                Thread.sleep(5);
            }
            catch(InterruptedException e){
                System.out.println("exception while empty queue."+e);
            }
        }
    }

    private void waitUntilGet()
    {
        this.unlock();
        while(this.isFull){
            try{
                Thread.sleep(50);
            }
            catch(InterruptedException e){
                System.out.println("exception while full buffer. "+e);
            }
        }
    }

    public boolean isFull;

    public boolean isEmpty;

    public int tail;

    public int head;

    public StringQueue()
    {
        queue=new Vector(100);
        queue.setSize(100);
        this.init();
    }

    private  void unlock()
    {
        this.isLocked=false;
    }

    private  void lock()
    {
        this.waitUntilUnlocked();
        this.isLocked=true;
    }

    public StringQueue(int max)
    {
        queue=new Vector(max);
        queue.setSize(max);
        this.init();
    }

    public  String get()
    {
        this.lock();
        this.isFull=true;
//        System.out.println("get");
        String x=null;
        if(tail==head){           // when queue is empty
            this.isEmpty=true;
            this.waitUntilPut();
            return get();
        }
        if(tail<head){
            x=(String)(queue.elementAt(tail));
            tail++;
            this.unlock();
            this.isFull=false;
            return x;
        }
        if(tail>head && tail<queue.size()){
            x=(String)(queue.elementAt(tail));
            tail++;
            this.unlock();
            this.isFull=false;
            return x;
        }
        if(tail>head && tail>=queue.size()){
            if(head>0){
                x=(String)(queue.elementAt(0));
                tail=1;
                this.unlock();
                this.isFull=false;
                return x;
            }
            else{
                this.isEmpty=true;
                waitUntilPut();
                this.unlock();
                return get();
            }
        }
        this.isEmpty=true;
        waitUntilPut();
        this.unlock();
        return get();
    }

    public void put(String s)
    {
        /*
        
            +-----+
            |     |   
            +-----+
            |     | <---  tail
            +-----+
            |     |
              ...
            |     |
            +-----+
            |     | <---  head
            +-----+
            |     |
            +-----+
                    <---  queue.size()
                    
                                        
        */
        if(head==tail-1){
            this.isFull=true;
            this.waitUntilGet();
            this.put(s);
            return;
        }
        this.isEmpty=true;
        this.lock();
        if(head>=tail && head<queue.size()){
            queue.setElementAt(s,head);
            head++;
            this.isEmpty=false;
            this.unlock();
            return;
        }
        if(head>=tail && head>=queue.size()){
            if(tail>0){
                queue.setElementAt(s,0);
                head=1;
                this.isEmpty=false;
                this.unlock();
                return;
            }
            else {
                this.isFull=true;
                waitUntilGet();
                 put(s);
                this.isEmpty=false;
                this.unlock();
                return;
            }
        }
        if(head<tail){
            queue.setElementAt(s,head);
            head++;
            this.isEmpty=false;
            this.unlock();
            return;
        }
        
    }

    public Vector queue;

}